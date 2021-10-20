# ***************************************************************************
# Copyright IBM Corporation 2021
#
# Licensed under the Eclipse Public License 2.0, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# ***************************************************************************

import logging
import os
import shutil
import toml
import sys
import subprocess
import pathlib
import zipfile

from . import constants, config_options
from .logging_util import tkltest_status
from .constants import *
from tkltest.util import command_util


def load_config(args=None, config_file=None):
    """Loads config options.

    Creates default config options object, updates it with options specified in the toml file and the
    command line (in that order, so that command-line values override toml file values for options that
    are specified in both places), and returns the final options object.

    Args:
        args: parsed command-line arguments
        config_file: name of config file to be loaded

    Returns:
        dict: dictionary containing configuration options for run
    """
    # initialize config
    tkltest_config = init_config()

    # if neither command-line args nor config file specified, return initialized config
    if args is None and config_file is None:
        return tkltest_config

    # load config toml file and merge it into initialized config; this ensures that options missing
    # in the toml file are initialized to their default values
    # load config from the file if provided or from the file specified in command line
    if config_file is not None:
        toml_config = toml.load(config_file)
    else:
        toml_config = toml.load(args.config_file)
    __merge_config(tkltest_config, toml_config)
    logging.debug('config: {}'.format(tkltest_config))

    # update general options with values specified in command line
    __update_config_with_cli_value(config=tkltest_config['general'],
        options_spec=config_options.get_options_spec(command='general'),
        args=args)

    # if args specified, get command and subcommand
    command = None
    subcommand = None
    if args is not None:
        command = args.command
        if hasattr(args, 'sub_command') and args.sub_command:
            subcommand = args.sub_command.replace('-', '_')

    # if command-line args provided, update config for options specified in the command line
    if command:
        # update command options with values specified in command line
        __update_config_with_cli_value(config=tkltest_config[command],
            options_spec=config_options.get_options_spec(command=command),
            args=args)

    # update subcommand options with values specified in command line
    if subcommand:
        __update_config_with_cli_value(config=tkltest_config[command][subcommand],
            options_spec=config_options.get_options_spec(command=command, subcommand=subcommand),
            args=args)

    # validate loaded config information, exit if validation errors occur
    val_failure_msgs = __validate_config(config=tkltest_config, command=command, subcommand=subcommand)
    if val_failure_msgs:  # pragma: no cover
        tkltest_status('configuration options validation failed:\n{}'.format(''.join(val_failure_msgs)), error=True)
        sys.exit(1)

    # map base test generator name to the internal code component name
    if subcommand == 'ctd_amplified':
        tkltest_config[args.command][subcommand]['base_test_generator'] = \
            constants.BASE_TEST_GENERATORS[tkltest_config[args.command][subcommand]['base_test_generator']]

    logging.debug('validated config: {}'.format(tkltest_config))
    return tkltest_config


def init_config():
    """Initializes config.

    Initializes and returns config data structure containing default values for all
    configuration options (excluding non-toml options, which should not be loaded).

    Returns:
        dict containing initialized options
    """
    # get config spec
    options_spec = config_options.get_options_spec()
    config = {}

    # set general options to default values
    general_opts_spec = options_spec['general']
    for option in general_opts_spec.keys():
        config['general'] = __init_options(general_opts_spec)

    # iterate over commands
    # for cmd in ['generate', 'execute', 'classify']:
    for cmd in ['generate', 'execute']:
        cmd_opts_spec = options_spec[cmd]

        # get subcommands, if any, for command
        subcmd_opts_spec = cmd_opts_spec.pop('subcommands', {})

        # set command options to default values
        config[cmd] = __init_options(cmd_opts_spec)

        # set subcommand options to default values
        for subcmd in subcmd_opts_spec.keys():
            config[cmd][subcmd] = __init_options(subcmd_opts_spec[subcmd])

    return config


def __validate_config(config, command=None, subcommand=None):
    """Validate loaded config information.

    Validates the given loaded config information in the context of the given command and (optionally)
    the given subcommand. The validation checks ensure that: (1) required parameters do not have their
    default values, and (2) enum types have valid values. If any validation check fails, prints error
    message and exits.

    """
    # get general options spec and options spec for the given command and subcommand
    options_spec = {
        'general': config_options.get_options_spec('general')
    }
    if command is not None:
        options_spec[command] = config_options.get_options_spec(command)
    if subcommand is not None:
        options_spec[subcommand] = config_options.get_options_spec(command, subcommand)

    # initialize validation errors
    val_errors = {
        scope: {
            'missing_required_params': [],
            'invalid_enum_values': {},
            'param_constraint_violation': []
        } for scope in ['general', command, subcommand] if scope is not None
    }

    for scope in options_spec.keys():
        if scope == subcommand:
            __validate_config_scope(config[command][scope], options_spec[scope], val_errors[scope],
                                    loaded_config=config)
        else:
            __validate_config_scope(config[scope], options_spec[scope], val_errors[scope])

    # if validation errors are detected, print error message and exit
    val_failure_msgs = []
    for scope, scope_val_errors in val_errors.items():
        if scope_val_errors['missing_required_params']:
            val_failure_msgs.append('\t- Missing required options for "{}": {}\n'.format(
                scope if scope in ['general', command] else command+' '+subcommand.replace('_', '-'),
                scope_val_errors['missing_required_params']
            ))
        if scope_val_errors['invalid_enum_values']:
            for opt_name, msg in scope_val_errors['invalid_enum_values'].items():
                val_failure_msgs.append('\t- Value for option "{}" {}'.format(opt_name, msg))
        for constraint_err in scope_val_errors['param_constraint_violation']:
            val_failure_msgs.append('\t- Violated parameter constraint: {}\n'.format(constraint_err))

    return val_failure_msgs


def __validate_config_scope(config, options_spec, val_errors, loaded_config=None):
    """
    Performs validation for a specific command scope. Updates the given validation errors structure with
    new detected errors.

    Args:
        config: loaded config for a command scope
        options_spec: options specification for a command scope
        val_errors: validation errors for a command scope
    """
    # iterate over options in the spec, perform checks, and store validation errors
    for opt_name in options_spec.keys():
        opt = options_spec[opt_name]

        # if "required" spec is a callable, which occurs for conditionally required options, call the
        # checker to determine whether the option is required in the context of the loaded config
        is_required = opt['required']
        if callable(opt['required']):
            is_required = opt['required'](opt_name, loaded_config)
        if is_required and config[opt_name] == opt['default_value']:
            # for java_jdk_path check whether it can be read from env var JAVA_HOME
            if opt_name == 'java_jdk_home':
                env_java_home = os.getenv("JAVA_HOME", None)
                if env_java_home:
                    config[opt_name] = env_java_home
                    continue
            val_errors['missing_required_params'].append(opt_name)
        if 'choices' in opt.keys() and opt_name in config.keys() and config[opt_name] not in opt['choices']:
            val_errors['invalid_enum_values'][opt_name] = 'must be one of {}: {}'.format(
                opt['choices'], config[opt_name])

        # check parameter dependency constraints
        if opt_name == 'augment_coverage':
            if config[opt_name] is True and config['base_test_generator'] == 'randoop':
                val_errors['param_constraint_violation'].append(
                    'To use option "{}/{}", base test generator must be "combined" or "evosuite"'.format(
                        opt['short_name'], opt['long_name']
                    ))


def __init_options(options_spec):
    """
    Given a dictionary of options spec, creates a config with each option to its default value while
    excluding non-toml options
    """
    ret_config = {}
    options_spec.pop('help_message', None)
    for option_name in options_spec.keys():
        if options_spec[option_name]['is_toml_option']:
            ret_config[option_name] = options_spec[option_name]['default_value']
    return ret_config


def __update_config_with_cli_value(config, options_spec, args):
    """Updates config object with cli option values.

    For the given config object and options spec (for a command or subcommand), updates the config value for
    options that are specified in the given command-line args.
    """
    for opt_name in options_spec.keys():
        if options_spec[opt_name]['is_cli_option'] and options_spec[opt_name]['is_toml_option']:
            if hasattr(args, opt_name):
                opt_value = getattr(args, opt_name)
                if opt_value:
                    config[opt_name] = opt_value


def __merge_config(base_config, update_config):
    """Merge two config specs.

    Updates base config with data in update config.
    """
    for key, val in update_config.items():
        if isinstance(val, dict):
            baseval = base_config.setdefault(key, {})
            __merge_config(baseval, val)
        else:
            base_config[key] = val


def __fix_relative_path(path):
    if path != "" and not os.path.isabs(path):
        return os.path.join('..', path)
    return path


def __fix_relative_paths_recursively(options_spec, config):

    for option_name, options in options_spec.items():
        if type(options) is not dict:
            continue
        if option_name == 'subcommands':
            for subcommands_option_name, subcommands_option in options.items():
                __fix_relative_paths_recursively(subcommands_option, config[subcommands_option_name])
            return
        if option_name not in config.keys():
            continue
        fix_type = options_spec[option_name].get('relative_fix_type', 'none')
        if fix_type == 'path':
            if options_spec[option_name].get('type') == str:
                config[option_name] = __fix_relative_path(config[option_name])
            else:
                config[option_name] = [__fix_relative_path(path) for path in config[option_name]]
        elif fix_type == 'paths_list_file':
            classpath_file = __fix_relative_path(config[option_name])
            if classpath_file != "":
                with open(classpath_file) as file:
                    lines = file.readlines()
                lines = [__fix_relative_path(path) for path in lines]
                new_file = os.path.basename(classpath_file)
                #todo - we will have a bug if the users uses two different files with the same name
                with open(new_file, 'w') as f:
                    f.writelines(lines)
                config[option_name] = new_file
        else:
            __fix_relative_paths_recursively(options, config[option_name])



def __fix_relative_pathes(tkltest_config):
    options_spec = config_options.get_options_spec()
    if tkltest_config.get('relative_fixed', False) == True:
        return
    __fix_relative_paths_recursively(options_spec, tkltest_config)
    tkltest_config['relative_fixed'] = True


def __resolve_claaspath(tkltest_config):
    app_name = tkltest_config['general']['app_name']
    if tkltest_config['general']['app_classpath_file'] != "":
        #todo - check that there is no build files?
        return

    if tkltest_config['general']['gradle_build_file'] == "":
        print("error no classpath file")
        # todo: need to check ant and maven
        # todo: error? create empty file?
        return

    #create dependencies directory
    dependencies_dir = os.path.join(os.getcwd(), app_name + "-dependencies")
    posix_dependencies_dir = pathlib.PurePath(dependencies_dir).as_posix()
    if os.path.isdir(dependencies_dir):
        shutil.rmtree(dependencies_dir)
    os.mkdir(dependencies_dir)

    #create build and settings gradle files
    gradle_file = tkltest_config['general']['gradle_build_file']
    tkltest_gradle_file = os.path.join(os.path.dirname(gradle_file), "tkltest_build.gradle")
    shutil.copyfile(gradle_file, tkltest_gradle_file)
    f = open(tkltest_gradle_file, "a")
    f.write("\ntask tkltest_get_dependencies(type: Copy) {\n")
    f.write("    from sourceSets.main.runtimeClasspath\n")
    f.write("    into '" + posix_dependencies_dir + "'\n")
    f.write("}\n")
    f.close()

    gradle_settings_file = tkltest_config['general']['gradle_settings_file']
    if gradle_settings_file:
        tkltest_gradle_settings_file = os.path.join(os.path.dirname(gradle_settings_file), "tkltest_settings.gradle")
        relative_gradle_file = pathlib.PurePath(os.path.relpath(tkltest_gradle_file,os.path.dirname(gradle_settings_file))).as_posix()
        shutil.copyfile(gradle_settings_file, tkltest_gradle_settings_file)
        f = open(tkltest_gradle_settings_file, "a")
        f.write("\nrootProject.buildFileName = '"+relative_gradle_file+"'\n")
        f.close()

    #run gradle
    gradle_command = "gradle -q -b " + os.path.abspath(tkltest_gradle_file)
    if gradle_settings_file:
        gradle_command += " -c " + os.path.abspath(tkltest_gradle_settings_file)
    gradle_command += " tkltest_get_dependencies"
    logging.info(gradle_command)

    try:
        command_util.run_command(command=gradle_command, verbose=tkltest_config['general']['verbose'])
    except subprocess.CalledProcessError as e:
        tkltest_status('gradle command failed: {}\n{}'.format(e, e.stderr), error=True)
        #todo - exit?

    os.remove(tkltest_gradle_file)
    if gradle_settings_file:
        os.remove(tkltest_gradle_settings_file)

    #remove non jar entries
    for jar_file in os.listdir(dependencies_dir):
        jar_file_path = os.path.join(dependencies_dir, jar_file)
        if os.path.isdir(jar_file_path):
            shutil.rmtree(jar_file_path)
        elif not jar_file_path.endswith(".jar"):
            os.remove(jar_file_path)

    #collect monolit modules
    monolith_app_paths = tkltest_config['general']['monolith_app_path']
    app_paths_modules = dict()
    for monolith_app_path in monolith_app_paths:
        app_path_modules = set()
        for root, dirs, files in os.walk(monolith_app_path):
            if len([file for file in files if file.endswith(".class")]):
                posix_module_path = pathlib.PurePath(root.replace(monolith_app_path, "")).as_posix()
                posix_module_path = "/".join(posix_module_path.split('/'))
                app_path_modules.add(posix_module_path)
        app_paths_modules[monolith_app_path] = app_path_modules

    #collect jars modules modules
    jars_modules = dict()
    for jar_file in os.listdir(dependencies_dir):
        jar_file_path = os.path.join(dependencies_dir, jar_file)
        if jar_file_path.endswith(".jar"):
            archive = zipfile.ZipFile(jar_file_path, 'r')
            class_files = set([file for file in archive.namelist() if file.endswith(".class")])
            archive.close()
            jars_modules[jar_file] = set([os.path.dirname(class_file) for class_file in class_files])

    #compare jars modules to monolit modules, remove matching jars
    for jar_file, jar_modules in jars_modules.items():
        jar_file_path = os.path.join(dependencies_dir, jar_file)
        for app_path, app_path_modules in app_paths_modules.items():
            if len(jar_modules) and jar_modules == app_path_modules:
                print(jar_file + " is " + app_path + " not adding to gradle classpath")
                os.remove(jar_file_path)
                break

    #write the classpath file
    classpath_file = os.path.join(os.getcwd(), app_name + "_gradle_classpath.txt")
    classpath_fd = open(classpath_file, "w")
    for jar_file in os.listdir(dependencies_dir):
        jar_file_path = os.path.join(dependencies_dir, jar_file)
        classpath_fd.write(jar_file_path+"\n")
    classpath_fd.close()
    tkltest_config['general']['app_classpath_file'] = classpath_file


def fix_config(tkltest_config):
    __fix_relative_pathes(tkltest_config)
    __resolve_claaspath(tkltest_config)


if __name__ == '__main__':
    config_file = sys.argv[1]
    print('config_file={}'.format(config_file))
    with open(config_file, 'r') as f:
        file_config = toml.load(f)
    print('file_config={}'.format(file_config))
    base_config = init_config()
    print('base_config={}'.format(base_config))
    __merge_config(base_config, file_config)
    print('updated_config={}'.format(base_config))
    failure_msgs = __validate_config(base_config, command='generate', subcommand='ctd_amplified')
    print('failure_msgs={}'.format(failure_msgs))
