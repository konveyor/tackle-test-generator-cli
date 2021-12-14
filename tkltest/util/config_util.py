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
import re
import copy
import xml.etree.ElementTree as ElementTree

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
            'missing_conditionally_required_params': {},
            'invalid_enum_values': {},
            'param_constraint_violation': []
        } for scope in ['general', command, subcommand] if scope is not None
    }

    for scope in options_spec.keys():
        if scope == subcommand:
            __validate_config_scope(config[command][scope], options_spec[scope], val_errors[scope],
                                    loaded_config=config)
        else:
            __validate_config_scope(config[scope], options_spec[scope], val_errors[scope], loaded_config=config)

    # if validation errors are detected, print error message and exit
    val_failure_msgs = []
    for scope, scope_val_errors in val_errors.items():
        if scope_val_errors['missing_required_params']:
            val_failure_msgs.append('\t- Missing required options for "{}": {}\n'.format(
                scope if scope in ['general', command] else command+' '+subcommand.replace('_', '-'),
                scope_val_errors['missing_required_params']
            ))
        for opt, msg in scope_val_errors['missing_conditionally_required_params'].items():
            val_failure_msgs.append('\t- Missing conditionally required option for "{}": {} ({})\n'.format(
                scope if scope in ['general', command] else command+' '+subcommand.replace('_', '-'),
                opt, msg
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
        cond_rqd_msg = ''
        if callable(opt['required']):
            cond_rqd_msg = opt['required'](opt_name, loaded_config)
            is_required = (cond_rqd_msg != '')
        if is_required and config[opt_name] == opt['default_value']:
            # for java_jdk_path check whether it can be read from env var JAVA_HOME
            if opt_name == 'java_jdk_home':
                env_java_home = os.getenv("JAVA_HOME", None)
                if env_java_home:
                    config[opt_name] = env_java_home
                    continue
            if cond_rqd_msg:
                val_errors['missing_conditionally_required_params'][opt_name] = cond_rqd_msg
            else:
                val_errors['missing_required_params'].append(opt_name)
        if 'choices' in opt.keys() and opt_name in config.keys() and config[opt_name] not in opt['choices']:
            val_errors['invalid_enum_values'][opt_name] = 'must be one of {}: {}\n'.format(
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
        fix_type = options_spec[option_name].get('relpath_fix_type', 'none')
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
                lines = [__fix_relative_path(path) for path in lines if path.strip()]
                new_file = os.path.basename(classpath_file)
                #todo - we will have a bug if the users uses two different files with the same name
                with open(new_file, 'w') as f:
                    f.writelines(lines)
                config[option_name] = new_file
        else:
            __fix_relative_paths_recursively(options, config[option_name])


def __fix_relative_paths(tkltest_config):
    """
    since we run the cli on a dedicated directory, we need to fix all the relative paths at the config
    Args:
        tkltest_config: the config to fix

    """

    options_spec = config_options.get_options_spec()
    if tkltest_config.get('relative_fixed', False) == True:
        return
    __fix_relative_paths_recursively(options_spec, tkltest_config)
    tkltest_config['relative_fixed'] = True


def __create_modified_build_file(app_build_file, toy_program_dir_path):
    """
    Creates a modified build file based on original one.
    In the modified copy, every target has only necessary modified tasks: {property, javac, antcall}
    :param app_build_file: original build file for the app
    :return: the file name for the modified build file
    """

    # create a copy of the build file
    tkltest_app_build_file = os.path.join(os.path.dirname(app_build_file),
                                          'tkltest_' + os.path.basename(app_build_file))

    # tree and root of original build file
    build_file_tree = ElementTree.parse(app_build_file)
    project_root = build_file_tree.getroot()

    # destination directory for compiling the toy program
    toy_program_destdir_path = os.path.join(toy_program_dir_path, 'toy_destdir')

    tkltest_target_javac_attributes = {'srcdir': toy_program_dir_path,
                                       'sourcepath': toy_program_dir_path,
                                       'destdir': toy_program_destdir_path,
                                       'includeantruntime': 'no',
                                       'verbose': 'yes'}

    # iterate on the project's targets
    for element in project_root.findall('target'):
        # create a new element with identical attributes and no sub-elements (no tasks)
        modified_element = ElementTree.Element('target', element.attrib)

        for task in element:
            if task.tag == 'property':
                # add as is to modified_element
                modified_element.append(task)

            elif task.tag == 'javac':
                # create a modified copy and add to modified_element
                new_javac_task_attributes = copy.deepcopy(tkltest_target_javac_attributes)

                # case 1: class path passed to javac as classpath attribute
                classpath_value = task.get('classpath')
                if classpath_value is not None:
                    new_javac_task_attributes['classpath'] = classpath_value

                # case 2: class path passed to javac as classpathref attribute
                classpathref_value = task.get('classpathref')
                if classpathref_value is not None:
                    new_javac_task_attributes['classpathref'] = classpathref_value

                new_javac_element = ElementTree.Element('javac', new_javac_task_attributes)

                # case 3: class path passed to javac as classpath nested element
                classpath_node = task.find('./classpath')
                if classpath_node is not None:
                    new_javac_element.append(classpath_node)

                modified_element.append(ElementTree.Element('delete', {'dir': toy_program_destdir_path}))
                modified_element.append(ElementTree.Element('mkdir', {'dir': toy_program_destdir_path}))
                modified_element.append(ElementTree.Element('echo', {'message': 'Java home: ${java.home}'}))
                modified_element.append(ElementTree.Element('echo', {'message': 'Java class path: ${java.class.path}'}))
                modified_element.append(new_javac_element)
                modified_element.append(ElementTree.Element('delete', {'dir': toy_program_destdir_path}))

            elif task.tag == 'antcall':
                # add as is to modified_element
                modified_element.append(task)

        project_root.remove(element)
        project_root.append(modified_element)

    build_file_tree.write(tkltest_app_build_file)
    return tkltest_app_build_file


def __run_ant_command_and_parse_output(modified_build_file_name,
                                       app_settings_file,
                                       app_build_target,
                                       ant_output_filename,
                                       targets_classpath):
    """ Runs the ant command with the modified copy of the build file.
        Also, removes the modified build file copy and the output file after the parsing. """
    # create output file or override previous output
    with open(ant_output_filename, 'w') as output_file:
        output_file.write('')

    # write command that will run the written target tkltest_target_name, set output to different files
    run_ant_command = 'ant -f ' + modified_build_file_name
    if app_settings_file:
        run_ant_command += ' -propertyfile ' + os.path.abspath(app_settings_file)
    run_ant_command += ' ' + app_build_target + ' >> ' + ant_output_filename

    # execute ant command
    try:
        command_util.run_command(command=run_ant_command, verbose=True)
    except subprocess.CalledProcessError as e:
        tkltest_status(
            'running ant task {} failed: {}\n{}'.format(run_ant_command, e, e.stderr),
            error=True)
        os.remove(modified_build_file_name)
        sys.exit(1)

    os.remove(modified_build_file_name)

    # parse ant output
    java_home_prefix = '[echo] Java home: '
    java_class_path_prefix = '[echo] Java class path: '
    javac_class_files_prefix = '[javac] [search path for class files: '

    with open(ant_output_filename, 'r') as output_file:
        lines = output_file.read()
    line_list = lines.splitlines()

    # parse java_home paths, because some jars are imported from here to javac_class_files.
    java_home_paths = set([s.replace(java_home_prefix, '').lstrip() for s in line_list if s.lstrip().startswith(java_home_prefix)])

    # parse java_class_path, because some of those jars are imported from here to javac_class_files.
    java_class_path_lines = [s.replace(java_class_path_prefix, '').lstrip() for s in line_list if s.lstrip().startswith(java_class_path_prefix)]
    java_class_path_set = set([path for line in java_class_path_lines for path in line.split(';')])

    # parse javac_class_files, those are the actual locations and jars that ant uses for compiling the target.
    javac_files_lines = [s.replace(javac_class_files_prefix, '').lstrip().rstrip(']') for s in line_list if s.lstrip().startswith(javac_class_files_prefix)]
    javac_class_files_set = set([path for line in javac_files_lines for path in line.split(',')])

    # collect all relevant jars
    # we need the jars that are from javac_class_files_set, that are not in java_class_path_set, and are not from a java_home directory
    for item in javac_class_files_set:
        if (item not in java_class_path_set) and (item.endswith('.jar')):
            # exclude item if it is from a directory inside one of the java_home_paths
            if not [path for path in java_home_paths if path in item]:
                targets_classpath.add(item)

    os.remove(ant_output_filename)


def __resolve_classpath(tkltest_config, command):
    """
    1. creates a directory of all the app dependencies
    using the app build files
    2. creates a classpath file pointing to the jars in the directory

    Args:
         tkltest_config - the configuration - to get the relevant build files, and to update the classpath_file
    """
    app_name = tkltest_config['general']['app_name']
    app_build_type = tkltest_config['generate']['app_build_type']
    app_build_file = tkltest_config['generate']['app_build_config_file']
    app_settings_file = tkltest_config['generate']['app_build_settings_file']
    app_classpath_file = tkltest_config['general']['app_classpath_file']
    build_classpath_file = os.path.join(os.getcwd(), app_name + "_build_classpath.txt")
    if command not in ["generate", 'execute']:
        return
    if app_classpath_file:
        return
    if app_build_type not in ['gradle', 'ant', 'maven']:
        tkltest_status('Getting app dependencies using {} is not supported yet\n'.format(app_build_type), error=True)
        sys.exit(1)

    if command == 'execute':
        if os.path.isfile(build_classpath_file):
            tkltest_config['general']['app_classpath_file'] = build_classpath_file
        else:
            tkltest_status('app_classpath_file is missing for execute run\n', error=True)
            sys.exit(1)

    # create dependencies directory
    dependencies_dir = os.path.join(os.getcwd(), app_name + "-app-dependencies")
    posix_dependencies_dir = pathlib.PurePath(dependencies_dir).as_posix()
    if os.path.isdir(dependencies_dir):
        shutil.rmtree(dependencies_dir)
    os.mkdir(dependencies_dir)

    if app_build_type == 'gradle':
        # create build and settings files
        get_dependencies_task = 'tkltest_get_dependencies'
        tkltest_app_build_file = os.path.join(os.path.dirname(app_build_file), "tkltest_build.gradle")
        shutil.copyfile(app_build_file, tkltest_app_build_file)
        with open(tkltest_app_build_file, "a") as f:
            f.write("\ntask " + get_dependencies_task + "(type: Copy) {\n")
            f.write("    from sourceSets.main.runtimeClasspath\n")
            f.write("    into '" + posix_dependencies_dir + "'\n")
            f.write("}\n")

        if app_settings_file:
            tkltest_app_settings_file = os.path.join(os.path.dirname(app_settings_file), "tkltest_settings.gradle")
            shutil.copyfile(app_settings_file, tkltest_app_settings_file)
            relative_app_build_file = pathlib.PurePath(os.path.relpath(tkltest_app_build_file, os.path.dirname(app_settings_file))).as_posix()
            with open(tkltest_app_settings_file, "a") as f:
                f.write("\nrootProject.buildFileName = '" + relative_app_build_file+"'\n")

        # run gradle
        get_dependencies_command = "gradle -q -b " + os.path.abspath(tkltest_app_build_file)
        if app_settings_file:
            get_dependencies_command += " -c " + os.path.abspath(tkltest_app_settings_file)
        get_dependencies_command += " " +get_dependencies_task
        logging.info(get_dependencies_command)

        try:
            command_util.run_command(command=get_dependencies_command, verbose=tkltest_config['general']['verbose'])
        except subprocess.CalledProcessError as e:
            tkltest_status('running {} task {} failed: {}\n{}'.format(app_build_type, get_dependencies_task, e, e.stderr), error=True)
            os.remove(tkltest_app_build_file)
            if app_settings_file:
                os.remove(tkltest_app_settings_file)
            sys.exit(1)

        os.remove(tkltest_app_build_file)
        if app_settings_file:
            os.remove(tkltest_app_settings_file)

    elif app_build_type == 'maven':
        get_dependencies_task = 'tkltest_get_dependencies'
        get_dependencies_command = 'mvn dependency:copy-dependencies -f ' + app_build_file + ' -DoutputDirectory=' + dependencies_dir
        logging.info(get_dependencies_command)

        # run maven
        try:
            command_util.run_command(command=get_dependencies_command, verbose=tkltest_config['general']['verbose'])
        except subprocess.CalledProcessError as e:
            tkltest_status('running {} task {} failed: {}\n{}'.format(app_build_type, get_dependencies_task, e, e.stderr), error=True)
            sys.exit(1)

    elif app_build_type == 'ant':
        app_build_target = tkltest_config['generate']['app_build_target']

        # a set for the united dependencies of the compilation process
        targets_classpath = set()

        # file name for ant output, deleted after parsing the output
        ant_output_filename = os.path.join(os.getcwd(), 'tkltest_ant_output.txt')

        # writing a toy program for compiling when running ant command
        toy_program_dir_path = os.path.abspath(os.path.join(os.getcwd(), 'tkltest_toy_program'))
        if os.path.isdir(toy_program_dir_path):
            shutil.rmtree(toy_program_dir_path)
        os.mkdir(toy_program_dir_path)
        with open(os.path.join(toy_program_dir_path, 'ToyProgram.java'), 'w') as java_file:
            java_file.write("public class ToyProgram {\n")
            java_file.write("    public static void main(String[] argv) {}\n")
            java_file.write("}\n")

        # create a modified build file
        modified_build_file_name = __create_modified_build_file(app_build_file, toy_program_dir_path)

        __run_ant_command_and_parse_output(modified_build_file_name,
                                           app_settings_file,
                                           app_build_target,
                                           ant_output_filename,
                                           targets_classpath)
        # removing the toy program
        shutil.rmtree(toy_program_dir_path)

        # copy classpath jars to dependencies directory
        for jar_path in targets_classpath:
            jar_name = os.path.basename(jar_path)
            jar_copy_path = os.path.join(dependencies_dir, jar_name)
            shutil.copyfile(jar_path, jar_copy_path)

    """
    the dependencies directory contains files to remove:
     1. app class files
     2. directories
     3. jar files with app modules

    so we:
     1. delete directories and non jars files
     2. collect the app modules in a dict: monolit_path -> set of modules name
     3. collect the app modules in a dict: jar files -> set of modules name

     if the set of a monolit_path equal to the set of the jar file, we delete the jar file
     (a jar file is represent as a list of directories)
    """

    # collect monolith modules
    monolith_app_paths = tkltest_config['general']['monolith_app_path']
    app_paths_modules = dict()
    for monolith_app_path in monolith_app_paths:
        app_path_modules = set()
        for root, dirs, files in os.walk(monolith_app_path):
            if len([file for file in files if file.endswith(".class")]):
                posix_module_path = "-".join(re.split("[\\\\/]+", root.replace(monolith_app_path, "")))
                app_path_modules.add(posix_module_path)
        app_paths_modules[monolith_app_path] = app_path_modules

    # remove non jar entries
    # collect jars modules
    jars_modules = dict()
    for jar_file in os.listdir(dependencies_dir):
        jar_file_path = os.path.join(dependencies_dir, jar_file)
        if os.path.isdir(jar_file_path):
            shutil.rmtree(jar_file_path)
        elif not jar_file_path.endswith(".jar"):
            os.remove(jar_file_path)
        else:
            archive = zipfile.ZipFile(jar_file_path, 'r')
            class_files = set([file for file in archive.namelist() if file.endswith(".class")])
            archive.close()
            jars_modules[jar_file] = set(
                ["-".join(re.split("[\\\\/]+", os.path.dirname(class_file))) for class_file in class_files])

    # compare jars modules to monolith modules, remove matching jars
    for app_path, app_path_modules in app_paths_modules.items():
        for jar_file, jar_modules in jars_modules.items():
            if len(jar_modules) and jar_modules == app_path_modules:
                del jars_modules[jar_file]
                jar_file_path = os.path.join(dependencies_dir, jar_file)
                os.remove(jar_file_path)
                break

    # write the classpath file
    classpath_fd = open(build_classpath_file, "w")
    for jar_file in jars_modules.keys():
        jar_file_path = os.path.join(dependencies_dir, jar_file)
        classpath_fd.write(jar_file_path + "\n")
    classpath_fd.close()
    tkltest_config['general']['app_classpath_file'] = build_classpath_file


def fix_config(tkltest_config, command):
    """
    fix the config before running generate
    Args:
        tkltest_config: the config to fix
        command: current command

    Returns:

    """
    __fix_relative_paths(tkltest_config)
    __resolve_classpath(tkltest_config, command)


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
