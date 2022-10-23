# ***************************************************************************
# Copyright IBM Corporation 2021
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# ***************************************************************************

import logging
import shutil
import toml
import sys
import subprocess
import pathlib
import zipfile
import re
import copy
import xml.etree.ElementTree as ElementTree
import json
from tkltest.util import config_options, command_util
from tkltest.util.logging_util import tkltest_status
from tkltest.util.constants import *
from tkltest.unit.util import dir_util
from tkltest.ui.util import config_options_ui


def load_config(test_level='unit', args=None, config_file=None):
    """Loads config options.

    Creates default config options object, updates it with options specified in the toml file and the
    command line (in that order, so that command-line values override toml file values for options that
    are specified in both places), and returns the final options object.

    Args:
        test_level: level of testing (unit, ui)
        args: parsed command-line arguments
        config_file: name of config file to be loaded

    Returns:
        dict: dictionary containing configuration options for run
    """
    # initialize config
    tkltest_config = init_config(test_level)

    # if neither command-line args nor config file specified, return initialized config
    if args is None and config_file is None:
        return tkltest_config

    # load config from the file if provided or from the file specified in command line
    if config_file is not None:
        toml_config = toml.load(config_file)
    else:
        toml_config = toml.load(args.config_file)

    # read internal options for tkltest-ui so they can be checked during config merging to avoid
    # printing warning messages about unsupported options for internal options that occur in the toml file
    if test_level == 'ui':
        internal_config_options = config_options_ui.get_options_spec_internal()
    else:
        internal_config_options = {}

    # merge config loaded from file into initialized config; this ensures that options missing
    # in the toml file are initialized to their default values
    __merge_config(tkltest_config, toml_config, base_config_internal=internal_config_options)
    logging.debug('config: {}'.format(tkltest_config))

    # update general options with values specified in command line
    __update_config_with_cli_value(config=tkltest_config['general'],
        options_spec=config_options.get_options_spec(command='general', test_level=test_level),
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
            options_spec=config_options.get_options_spec(command=command, test_level=test_level),
                                       args=args)

    # update subcommand options with values specified in command line
    if subcommand:
        __update_config_with_cli_value(config=tkltest_config[command][subcommand],
            options_spec=config_options.get_options_spec(command=command, subcommand=subcommand, test_level=test_level),
                                       args=args)

    # validate loaded config information, exit if validation errors occur
    val_failure_msgs = __validate_config(config=tkltest_config, test_level=test_level, command=command,
                                         subcommand=subcommand)
    if val_failure_msgs:  # pragma: no cover
        tkltest_status('configuration options validation failed:\n{}'.format(''.join(val_failure_msgs)), error=True)
        sys.exit(1)

    # map base test generator name to the internal code component name
    if subcommand == 'ctd_amplified':
        tkltest_config[args.command][subcommand]['base_test_generator'] = \
            BASE_TEST_GENERATORS[tkltest_config[args.command][subcommand]['base_test_generator']]

    logging.debug('validated config: {}'.format(tkltest_config))
    return tkltest_config


def init_config(test_level='unit'):
    """Initializes config.

    Initializes and returns config data structure containing default values for all
    configuration options (excluding non-toml options, which should not be loaded).

    Args:
        test_level: level of testing (unit, ui)

    Returns:
        dict containing initialized options
    """
    # get config spec
    options_spec = config_options.get_options_spec(test_level=test_level)
    config = {}

    for opt_name in options_spec.keys():

        if not options_spec[opt_name]['is_cli_command']:
            # set general and dev_tests options to default values
            config[opt_name] = __init_options(options_spec[opt_name])
        else:
            # set command and subcommand options to default values
            cmd_opts_spec = options_spec[opt_name]

            # get subcommands, if any, for command
            subcmd_opts_spec = cmd_opts_spec.pop('subcommands', {})

            # set command options to default values
            config[opt_name] = __init_options(cmd_opts_spec)

            # set subcommand options to default values
            for subcmd in subcmd_opts_spec.keys():
                config[opt_name][subcmd] = __init_options(subcmd_opts_spec[subcmd])

    return config


def __validate_config(config, test_level, command=None, subcommand=None):
    """Validate loaded config information.

    Validates the given loaded config information in the context of the given command and (optionally)
    the given subcommand. The validation checks ensure that: (1) required parameters do not have their
    default values, and (2) enum types have valid values. If any validation check fails, prints error
    message and exits.

    """
    # get general and dev_tests options spec (options that are always checked)
    general_scopes = ['general']
    if test_level == 'unit':
        general_scopes.append('dev_tests')

    # get options spec for the given command and subcommand
    options_spec = {scope: config_options.get_options_spec(scope, test_level=test_level) for scope in general_scopes}
    if command is not None:
        options_spec[command] = config_options.get_options_spec(command, test_level=test_level)
    if subcommand is not None:
        options_spec[subcommand] = config_options.get_options_spec(command, subcommand, test_level=test_level)

    # initialize validation errors
    val_errors = {
        scope: {
            'missing_required_params': [],
            'missing_conditionally_required_params': {},
            'invalid_enum_values': {},
            'param_constraint_violation': []
        } for scope in general_scopes + [command, subcommand] if scope is not None
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
        if opt_name == 'no_augment_coverage':
            if config[opt_name] is False and config['base_test_generator'] == 'randoop':
                val_errors['param_constraint_violation'].append(
                    'For coverage augmentation, base test generator must be "combined" or "evosuite"; ' +
                    'to "randoop" as the only base generator, specify also the option {}/{}'.format(
                        opt['short_name'], opt['long_name']
                    ))


def __init_options(options_spec):
    """
    Given a dictionary of options spec, creates a config with each option to its default value while
    excluding non-toml options
    """
    ret_config = {}
    options_spec.pop('help_message', None)
    options_spec.pop('is_cli_command', None)
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


def __merge_config(base_config, update_config, key_prefix="", base_config_internal={}):
    """Merge two config specs.

    Updates base config with data in update config.
    Prints warnings about unrecognized configuration flags from update_config, still adds them to base_config.
    """
    for key, val in update_config.items():
        full_key = key if key_prefix == "" else key_prefix + '.' + key
        if key not in base_config:
            if not base_config_internal or key not in base_config_internal:
                tkltest_status('Warning: Unsupported flag in toml file: {}'.format(full_key))
        if isinstance(val, dict):
            baseval = base_config.setdefault(key, {})
            basevalint = base_config_internal.setdefault(key, {})
            __merge_config(baseval, val, full_key, basevalint)
        else:
            base_config[key] = val


def __fix_relative_path(path, path_fix):
    if path != '' and not os.path.isabs(path):
        return os.path.join(path_fix, path)
    return path


def __fix_relative_paths_recursively(options_spec, config, output_dir, path_fix, app_name):

    for option_name, options in options_spec.items():
        if type(options) is not dict:
            continue
        if option_name == 'subcommands':
            for subcommands_option_name, subcommands_option in options.items():
                if subcommands_option_name in config.keys():
                    __fix_relative_paths_recursively(subcommands_option, config[subcommands_option_name], output_dir, path_fix, app_name)
            return
        if option_name not in config.keys():
            continue
        fix_type = options_spec[option_name].get('relpath_fix_type', 'none')
        if fix_type == 'path':
            if options_spec[option_name].get('type') == str:
                config[option_name] = __fix_relative_path(config[option_name], path_fix)
            else:
                config[option_name] = [__fix_relative_path(path, path_fix) for path in config[option_name]]
        elif fix_type == 'paths_list_file':
            file_path = config[option_name]
            if file_path != "":
                with open(file_path) as file:
                    lines = file.readlines()
                lines = [__fix_relative_path(path, path_fix) for path in lines if path.strip()]
                file_base_name = os.path.basename(file_path)
                if not file_base_name.startswith(app_name):
                    # we add app_name so the file will not be deleted
                    file_base_name = app_name + file_base_name
                new_file = os.path.join(output_dir, file_base_name)
                #todo - we will have a bug if the users uses two different files with the same name
                with open(new_file, 'w') as f:
                    f.writelines(lines)
                config[option_name] = new_file
        else:
            __fix_relative_paths_recursively(options, config[option_name], output_dir, path_fix, app_name)


def fix_relative_paths(tkltest_config):
    """
    since we run the cli on a dedicated directory, we need to fix all the relative paths at the config
    Args:
        tkltest_config: the config to fix

    """

    options_spec = config_options.get_options_spec()
    if tkltest_config.get('relative_fixed', False) == True:
        return
    module_name = tkltest_config['general'].get('module_name', '')
    if module_name:
        path_fix = os.path.join('..', '..')
    else:
        path_fix = '..'
    output_dir = dir_util.get_output_dir(tkltest_config['general']['app_name'], module_name)
    __fix_relative_paths_recursively(options_spec, tkltest_config, output_dir, path_fix, tkltest_config['general']['app_name'])
    tkltest_config['relative_fixed'] = True


def __create_modified_build_file_for_dependencies(app_build_file, toy_program_dir_path):
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


def __run_ant_command(modified_build_file_name,
                      app_settings_file,
                      app_build_ant_target,
                      output_dir):
    """
    Runs the ant command with the modified copy of the build file.
    Also, removes the modified build file copy after running command.
    :param modified_build_file_name: the modified build file to run Ant with
    :param app_settings_file: settings file for user properties
    :param app_build_ant_target: Ant target to run
    :return: path to the file containing the output
    """
    # create output file or override previous output
    ant_output_filename = os.path.join(output_dir, 'tkltest_ant_output.txt')
    with open(ant_output_filename, 'w') as output_file:
        output_file.write('')

    # write command that will run the written target tkltest_target_name, set output to different files
    run_ant_command = 'ant -f ' + modified_build_file_name
    if app_settings_file:
        run_ant_command += ' -propertyfile ' + os.path.abspath(app_settings_file)
    run_ant_command += ' ' + app_build_ant_target + ' >> ' + ant_output_filename
    logging.info(run_ant_command)

    # execute ant command
    try:
        command_util.run_command(command=run_ant_command, verbose=True)
    except subprocess.CalledProcessError as e:
        tkltest_status(
            'running ant task "{}" failed: {}\n{}'.format(run_ant_command, e, e.stderr),
            error=True)
        os.remove(modified_build_file_name)
        sys.exit(1)

    os.remove(modified_build_file_name)
    return ant_output_filename


def __parse_ant_output_for_dependencies(ant_output_filename):
    """
    Parses Ant output for extracting dependencies, deletes output file when finished.
    :param ant_output_filename: filename of the Ant command output
    :return: class_path_order: list of the united dependencies of the compilation process
    """
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
    javac_class_files = [path for line in javac_files_lines for path in line.split(',')]
    javac_class_files_set = list(dict.fromkeys(javac_class_files))  # removing duplicates while preserving order

    # collect all relevant jars
    # we need the jars that are from javac_class_files_set, that are not in java_class_path_set, and are not from a java_home directory
    class_path_order = []
    for item in javac_class_files_set:
        if (item not in java_class_path_set) and (item.endswith('.jar')):
            # exclude item if it is from a directory inside one of the java_home_paths
            if not [path for path in java_home_paths if path in item]:
                class_path_order.append(item)

    os.remove(ant_output_filename)
    return class_path_order


def __get_source_of_ant_javac(javac_task):
    """
    Returns the source parameter passed to given javac task, in an Ant build file.
    :param javac_task: Given task, ElementTree variable.
    :return: a string for single source, or a list of sources.
    """
    source_options = ['srcdir', 'modulesourcepath', 'modulesourcepathref']
    for source in source_options:
        srcdir = javac_task.get(source)
        if srcdir is not None:
            return '${toString:' + srcdir + '}' if source == 'modulesourcepathref' else srcdir
    # source not given as attribute, search for nested <src> elements
    srcdir = []
    for element in javac_task.findall('src'):
        src_path = element.get('path')
        if src_path is not None:
            srcdir.append(src_path)
    return srcdir


def __create_modified_build_file_for_monolith_app_path(app_build_file):
    """
    Creates a modified copy of the build file that will be used to extracting the monolith_app_path.
    The modified copy has the original property and antcall tasks, and every javac task was replaced
    by echo of the destination directory.
    :param app_build_file: path to Ant build file (build.xml)
    :return: tuple of (path to the created modified copy of the build file, basedir of build file)
    """

    # create a copy of the build file
    tkltest_app_build_file = os.path.join(os.path.dirname(app_build_file),
                                          'tkltest_' + os.path.basename(app_build_file))
    # tree and root of original build file
    build_file_tree = ElementTree.parse(app_build_file)
    project_root = build_file_tree.getroot()
    basedir = project_root.get('basedir')
    basedir = basedir if os.path.isabs(basedir) else os.path.join(os.path.dirname(app_build_file), basedir)

    # iterate on the project's targets
    for element in project_root.findall('target'):
        # create a new element with identical attributes and no sub-elements (no tasks)
        modified_element = ElementTree.Element('target', element.attrib)
        for task in element:
            if task.tag == 'property':
                # add as is to modified_element
                modified_element.append(task)
            elif task.tag == 'antcall':
                # add as is to modified_element
                modified_element.append(task)
            elif task.tag == 'javac':
                # create instead an echo task to print the "destdir" of javac
                destdir = task.get('destdir')
                if destdir is None:
                    # "class" files are created near original "java" files
                    destdir = __get_source_of_ant_javac(task)
                elif destdir == "":
                    # "class" files are created in basedir
                    destdir = basedir

                if isinstance(destdir, list):
                    # when destdir is not given and there are multiple source files
                    for src_path in destdir:
                        modified_element.append(ElementTree.Element('echo', {'message': 'destdir: ' + src_path}))
                else:
                    modified_element.append(ElementTree.Element('echo', {'message': 'destdir: ' + destdir}))
        project_root.remove(element)
        project_root.append(modified_element)
    build_file_tree.write(tkltest_app_build_file)
    return tkltest_app_build_file, basedir


def __add_and_run_gradle_task(app_build_file, app_settings_file, task_name, task_text, verbose):
    '''
    insert a task to the gradle build file, and run it
    :param app_build_file: build.gradle file
    :param app_settings_file: setting.gradle file
    :param task_name: the task name to run
    :param task_text: the text of the task
    :param verbose: verbose
    '''
    # add the task to a copy of the gradle file
    tkltest_app_build_file = os.path.join(os.path.dirname(app_build_file), task_name + '_build.gradle')
    shutil.copyfile(app_build_file, tkltest_app_build_file)
    with open(tkltest_app_build_file, "a") as f:
        f.write('\n')
        for line in task_text:
            f.write(line + '\n')

    # also update the setting files with a link to the copy
    if app_settings_file:
        tkltest_app_settings_file = os.path.join(os.path.dirname(app_settings_file), task_name + '_settings.gradle')
        shutil.copyfile(app_settings_file, tkltest_app_settings_file)
        relative_app_build_file = pathlib.PurePath(
            os.path.relpath(tkltest_app_build_file, os.path.dirname(app_settings_file))).as_posix()
        with open(tkltest_app_settings_file, "a") as f:
            f.write("\nrootProject.buildFileName = '" + relative_app_build_file + "'\n")

    # run the task with gradle
    get_dependencies_command = 'gradle '
    if not verbose:
        get_dependencies_command += '-q '
    get_dependencies_command += '-b ' + os.path.abspath(tkltest_app_build_file)
    if app_settings_file:
        get_dependencies_command += " -c " + os.path.abspath(tkltest_app_settings_file)
    get_dependencies_command += " " + task_name
    logging.info(get_dependencies_command)

    try:
        command_util.run_command(command=get_dependencies_command, verbose=verbose)
    except subprocess.CalledProcessError as e:
        tkltest_status('running gradle task {} failed: {}\n{}'.format( task_name, e, e.stderr),error=True)
        os.remove(tkltest_app_build_file)
        if app_settings_file:
            os.remove(tkltest_app_settings_file)
        sys.exit(1)

    os.remove(tkltest_app_build_file)
    if app_settings_file:
        os.remove(tkltest_app_settings_file)


def resolve_app_path(tkltest_config):
    '''
    get the app path from the user build file
    :param tkltest_config: the config
    :return:
    '''
    if tkltest_config['general']['monolith_app_path']:
        return
    app_name = tkltest_config['general']['app_name']
    app_build_type = tkltest_config['general']['build_type']
    if len(tkltest_config['generate']['app_build_files']) != 1 or len(tkltest_config['generate']['app_build_settings_files']) > 1:
        # it is a rare case, in which the user gives more that one build file, however we obtain only one module
        tkltest_status('resolving app_path supported for only a single app_build_file', error=True)
        sys.exit(1)
    app_build_file = tkltest_config['generate']['app_build_files'][0]
    if tkltest_config['generate']['app_build_settings_files']:
        app_settings_file = tkltest_config['generate']['app_build_settings_files'][0]
    else:
        app_settings_file = ''
    output_dir = dir_util.get_output_dir(app_name, tkltest_config['general'].get('module_name', ''))
    if app_build_type == 'gradle':
        app_path_file = pathlib.PurePath(os.path.join(output_dir, app_name + '_' + app_build_type + '_app_path.txt')).as_posix()
        task_name = 'tkltest_get_app_path'
        write_classes_dirs_line = '    fw.write("${project.sourceSets.main.output.classesDirs.getFiles()}\\n");'
        if app_settings_file:
            write_classes_dirs_line += '\n    project.rootProject.subprojects.forEach { fw.write( "${it.sourceSets.main.output.classesDirs.getFiles()}\\n" ); }'
        task_text = [
            'public class WriteStringClass extends DefaultTask {',
            '  @TaskAction',
            '  void writeString(){',
            '    FileWriter fw;',
            '    fw = new FileWriter( "' + app_path_file + '");',
            write_classes_dirs_line,
            '    fw.close();',
            '  }',
            '}',
            'task ' + task_name + ' (type:WriteStringClass) {}']

        __add_and_run_gradle_task(app_build_file=app_build_file,
                                  app_settings_file=app_settings_file,
                                  task_name=task_name,
                                  task_text=task_text,
                                  verbose=tkltest_config['general']['verbose'])

        with open(app_path_file) as f:
            app_path = [p.strip('[]') for p in f.read().split('\n')]
            app_path.remove('')
            app_path = [path for path in app_path if os.path.isdir(path)]
            tkltest_config['general']['monolith_app_path'] = app_path

    elif app_build_type == 'ant':
        app_build_ant_target = tkltest_config['generate']['app_build_ant_target']
        # create a modified build file
        modified_build_file_name, build_base_dir = __create_modified_build_file_for_monolith_app_path(app_build_file)
        ant_output_filename = __run_ant_command(modified_build_file_name, app_settings_file, app_build_ant_target, output_dir)

        with open(ant_output_filename, 'r') as output_file:
            lines = output_file.read().splitlines()
        os.remove(ant_output_filename)
        echo_prefix = '[echo] destdir: '
        app_path = list(set([s.replace(echo_prefix, '').lstrip() for s in lines if s.lstrip().startswith(echo_prefix)]))
        app_path = os.path.commonpath(app_path)
        app_path = app_path if os.path.isabs(app_path) else os.path.abspath(os.path.join(build_base_dir, app_path))
        tkltest_config['general']['monolith_app_path'] = [app_path]

    elif app_build_type == 'maven':
        app_path_file = os.path.join(output_dir, app_name + '_' + app_build_type + '_app_path.txt')
        build_directory_name = 'project.build.directory'

        get_apppath_command = 'mvn org.kuali.maven.plugins:properties-maven-plugin:2.0.1:write-project-properties'
        get_apppath_command += ' -f ' + app_build_file
        get_apppath_command += ' -Dproperties.includeStandardMavenProperties=true'
        get_apppath_command += ' -Dproperties.encoding=' + sys.getfilesystemencoding()
        get_apppath_command += ' -Dproperties.include=' + build_directory_name
        get_apppath_command += ' -Dproperties.outputFile=' + app_path_file
        logging.info(get_apppath_command)
        # run maven
        try:
            command_util.run_command(command=get_apppath_command, verbose=tkltest_config['general']['verbose'])
        except subprocess.CalledProcessError as e:
            tkltest_status('running {} command "{}" failed: {}\n{}'.format(app_build_type, get_apppath_command, e, e.stderr), error=True)
            sys.exit(1)

        with open(app_path_file) as f:
            app_path_lines = [l for l in f.read().split('\n') if build_directory_name in l]

        app_path_lines = [l.replace(build_directory_name + '=', '', 1) for l in app_path_lines]
        app_path_lines = [l.replace('\\:\\\\', ':\\\\') for l in app_path_lines]
        app_path_lines = [os.path.join(l, 'classes') for l in app_path_lines]
        app_path_lines = [l for l in app_path_lines if os.path.isdir(l)]
        tkltest_config['general']['monolith_app_path'] = app_path_lines


def __collect_jar_packages(jar_file_path, jars_packages):
    """ Collects the packages of the jar and adds them to the dictionary jars_packages. """
    archive = zipfile.ZipFile(jar_file_path, 'r')
    class_files = set([file for file in archive.namelist() if file.endswith(".class")])
    archive.close()
    jars_packages[jar_file_path] = set(
        ["-".join(re.split("[\\\\/]+", os.path.dirname(class_file))) for class_file in class_files])


def resolve_classpath(tkltest_config, command):
    """
    1. creates a directory of all the app dependencies
    using the app build files
    2. creates a classpath file pointing to the jars in the directory

    Args:
         tkltest_config - the configuration - to get the relevant build files, and to update the classpath_file
    """
    app_name = tkltest_config['general']['app_name']
    output_dir = dir_util.get_output_dir(app_name, tkltest_config['general'].get('module_name', ''))
    build_classpath_file = os.path.join(output_dir, app_name + "_build_classpath.txt")
    if command not in ["generate", 'execute']:
        return
    app_classpath_file = tkltest_config['general']['app_classpath_file']
    if app_classpath_file:
        return

    if command == 'execute':
        if os.path.isfile(build_classpath_file):
            tkltest_config['general']['app_classpath_file'] = build_classpath_file
            return
        else:
            tkltest_status('app_classpath_file is missing for execute run\n', error=True)
            sys.exit(1)

    app_build_type = tkltest_config['general']['build_type']
    if len(tkltest_config['generate']['app_build_files']) != 1 or len(tkltest_config['generate']['app_build_settings_files']) > 1:
        # it is a rare case, in which the user gives more that one build file, however we obtain only one module
        tkltest_status('resolving classpath supported for only a single app_build_files', error=True)
        sys.exit(1)

    app_build_file = tkltest_config['generate']['app_build_files'][0]
    if tkltest_config['generate']['app_build_settings_files']:
        app_settings_file = tkltest_config['generate']['app_build_settings_files'][0]
    else:
        app_settings_file = ''
    # list for keeping ordered classpath jars
    class_path_order = []

    if app_build_type == 'gradle':
        # create build and settings files
        task_name = 'tkltest_get_dependencies'
        gradle_classpath_file = os.path.join(output_dir, 'GradleClassPath.txt')
        task_text = [
            'public class WriteStringClass extends DefaultTask {',
            '  @TaskAction',
            '  void writeString(){',
            '    FileWriter fw;',
            '    fw = new FileWriter( "' + pathlib.PurePath(gradle_classpath_file).as_posix() + '", true);',
            '    fw.write("${project.sourceSets.main.runtimeClasspath.getFiles()}")',
            '    fw.close();',
            '  }',
            '}',
            'task ' + task_name + ' (type:WriteStringClass) {}']

        __add_and_run_gradle_task(app_build_file=app_build_file,
                                  app_settings_file=app_settings_file,
                                  task_name=task_name,
                                  task_text=task_text,
                                  verbose=tkltest_config['general']['verbose'])
        with open(gradle_classpath_file) as f:
            class_path_order = f.read().replace('[', '').replace(']', '').replace(' ', '').split(',')
        os.remove(gradle_classpath_file)

    elif app_build_type == 'maven':
        mvn_classpath_file = os.path.abspath('MavenClassPath.txt')
        get_cpfile_command = 'mvn dependency:build-classpath -f ' + app_build_file + ' -Dmdep.outputFile=' + mvn_classpath_file
        get_cpfile_command += ' "-Dmdep.pathSeparator=;"'
        logging.info(get_cpfile_command)
        try:
            command_util.run_command(command=get_cpfile_command, verbose=tkltest_config['general']['verbose'])
        except subprocess.CalledProcessError as e:
            tkltest_status('running {} failed: {}\n{}'.format(app_build_type, e, e.stderr), error=True)
            sys.exit(1)
        with open(mvn_classpath_file) as f:
            class_path_order = f.read().split(';')
        os.remove(mvn_classpath_file)

    elif app_build_type == 'ant':
        app_build_ant_target = tkltest_config['generate']['app_build_ant_target']

        # writing a toy program for compiling when running ant command
        toy_program_dir_path = os.path.abspath(os.path.join(output_dir, 'tkltest_toy_program'))
        if os.path.isdir(toy_program_dir_path):
            shutil.rmtree(toy_program_dir_path)
        os.mkdir(toy_program_dir_path)
        with open(os.path.join(toy_program_dir_path, 'ToyProgram.java'), 'w') as java_file:
            java_file.write("public class ToyProgram {\n")
            java_file.write("    public static void main(String[] argv) {}\n")
            java_file.write("}\n")

        # create a modified build file
        modified_build_file_name = __create_modified_build_file_for_dependencies(app_build_file, toy_program_dir_path)
        ant_output_filename = __run_ant_command(modified_build_file_name, app_settings_file, app_build_ant_target, output_dir)
        class_path_order = __parse_ant_output_for_dependencies(ant_output_filename)
        # removing the toy program
        shutil.rmtree(toy_program_dir_path)

    """
    the class_path_order contains files to remove:
     1. app class files
     2. directories
     3. jar files with app packages

    so we:
     1. delete directories and non jars files
     2. collect the app packages in a dict: monolit_path -> set of packages name
     3. collect the app packages in a dict: jar files -> set of packages name

     if the set of a monolit_path equal to the set of the jar file, we delete the jar file
     (a jar file is represent as a list of directories)
    """

    # collect monolith packages
    monolith_app_paths = tkltest_config['general']['monolith_app_path']
    app_paths_packages = dict()
    for monolith_app_path in monolith_app_paths:
        app_path_packages = set()
        for root, dirs, files in os.walk(monolith_app_path):
            if len([file for file in files if file.endswith(".class")]):
                posix_module_path = "-".join(re.split("[\\\\/]+", root.replace(monolith_app_path, "").lstrip('\\/')))
                app_path_packages.add(posix_module_path)
        app_paths_packages[monolith_app_path] = app_path_packages

    # remove non jar entries
    # collect jars packages
    jars_packages = dict()
    for jar_file_path in class_path_order:
        if jar_file_path.endswith('.jar'):
            __collect_jar_packages(jar_file_path, jars_packages)

    # compare jars packages to monolith packages, remove matching jars
    for app_path, app_path_packages in app_paths_packages.items():
        for jar_file, jar_packages in jars_packages.items():
            if len(jar_packages) and jar_packages == app_path_packages:
                del jars_packages[jar_file]
                break

    # write the classpath file
    classpath_fd = open(build_classpath_file, "w")
    for jar_file in class_path_order:
        if jar_file in jars_packages.keys():
            classpath_fd.write(jar_file + '\n')
    classpath_fd.close()
    tkltest_config['general']['app_classpath_file'] = build_classpath_file


def resolve_tkltest_configs(tkltest_user_config, command):
    '''
    creates the configs that we are going to run on.
     we need to:
      1. resolve some missing parameters (app apth, class path)
      2. see if there is more than one module, and create a toml per module
    :param tkltest_user_config: the user config that he wrote at the toml file
    :param command: the command to be run
    :return: a list of tkltest_configs, each will be run with generate/execute command
    '''
    app_name = tkltest_user_config['general']['app_name']
    tkltest_config_file_suffix = '_generated_tkltest_config.toml'
    if tkltest_user_config['general']['build_type'] == 'ant' or \
            not tkltest_user_config['generate']['app_build_files'] or \
            tkltest_user_config['general']['monolith_app_path']:
        # this is the case in we can not try to get the modules (ant, no user build file), or already have app_path.
        resolve_app_path(tkltest_user_config)
        resolve_classpath(tkltest_user_config, command)
        fix_relative_paths(tkltest_user_config)
        return [tkltest_user_config]
    elif command != 'generate':
        # it is not a generate - we will not look modules. we will look for toml files that was created during generate
        toml_files = list(pathlib.Path(dir_util.get_app_output_dir(app_name)).glob('**/*' + tkltest_config_file_suffix))
        if toml_files:
            tkltest_status('Running on {} config files that were created by the generate command  '.format(len(toml_files)))
            configs = []
            for toml_file in toml_files:
                config = toml.load(toml_file)
                config['general']['config_file_path'] = os.path.abspath(toml_file)
                fix_relative_paths(config)
                configs.append(config)
            # fixing the user config too:
            fix_relative_paths(tkltest_user_config)
            return configs
        else:
            resolve_app_path(tkltest_user_config)
            resolve_classpath(tkltest_user_config, command)
            fix_relative_paths(tkltest_user_config)
            return [tkltest_user_config]
    else:
        # we first obtain the module properties from the build file
        modules_properties = get_modules_properties(tkltest_user_config)
        if len(modules_properties) == 1:
            # we have only one module - we will use the user config
            resolve_app_path(tkltest_user_config)
            resolve_classpath(tkltest_user_config, command)
            fix_relative_paths(tkltest_user_config)
            return [tkltest_user_config]
        
        # here we got more than one module, we will create a config per module, and save it in a toml file
        tkltest_configs = __resolve_multi_modules_tkltest_configs(tkltest_user_config, modules_properties, command, tkltest_config_file_suffix)
        #fixing the user config too:
        fix_relative_paths(tkltest_user_config)
        tkltest_status('Obtained {} modules from the build files. creating a config file per module.'.format(len(tkltest_configs)))
        return tkltest_configs


def __resolve_multi_modules_tkltest_configs(tkltest_user_config, modules_properties, command, tkltest_config_file_suffix):
    '''
    create a config per module, and update the config with the module properties
    :param tkltest_user_config:
    :param modules_properties:
    :return:
    '''
    app_name = tkltest_user_config['general']['app_name']
    tkltest_configs = []
    for module_properties in modules_properties:
        module_name = module_properties['name']
        tkltest_config = copy.deepcopy(tkltest_user_config)
        tkltest_config['general']['module_name'] = module_name
        tkltest_config['general']['monolith_app_path'] = module_properties['app_path']
        tkltest_config['generate']['app_build_files'] = [module_properties['build_file']]
        if 'user_settings_file' in module_properties.keys():
            tkltest_config['generate']['app_build_settings_files'] = [module_properties['user_settings_file']]
        if tkltest_config['general']['test_directory']:
            tkltest_config['general']['test_directory'] = os.path.join(tkltest_config['general']['test_directory'], module_name)
        if tkltest_config['general']['reports_path']:
            tkltest_config['general']['reports_path'] = os.path.join(tkltest_config['general']['reports_path'], module_name)

        if module_properties['classpath']:
            build_classpath_file = os.path.join(dir_util.get_output_dir(app_name, module_name),
                                                app_name + '_' + module_name + "_build_classpath.txt")
            with open(build_classpath_file, 'w') as f:
                f.write('\n'.join(module_properties['classpath']))
            tkltest_config['general']['app_classpath_file'] = build_classpath_file
        else:
            resolve_classpath(tkltest_config, command)
        tkltest_config_file = os.path.join(dir_util.get_output_dir(app_name, module_name),
                                           app_name + '_' + module_name + tkltest_config_file_suffix)
        with open(tkltest_config_file, 'w') as f:
            toml.dump(tkltest_config, f)
        tkltest_config['general']['config_file_path'] = tkltest_config_file
        fix_relative_paths(tkltest_config)
        tkltest_configs.append(tkltest_config)

    return tkltest_configs


def get_modules_properties(tkltest_user_config):
    '''
    get from the config a list of pom files of an app, and find all the modules and their properties (name, build file,...)
    eliminate modules that we do not need (no app_path, ...)
    :param tkltest_user_config: the config we got from the user
           modules_properties_file: the xml file to save the the properties
    :return: list of dict of module names and properties
    '''

    app_name = tkltest_user_config['general']['app_name']
    app_build_type = tkltest_user_config['general']['build_type']
    app_build_files = tkltest_user_config['generate']['app_build_files']
    app_settings_files = tkltest_user_config['generate']['app_build_settings_files']

    modules_properties_file = os.path.join(dir_util.get_app_output_dir(app_name), app_name + '_modules_properties.json')
    if os.path.isfile(modules_properties_file):
        os.remove(modules_properties_file)

    tkltest_status('Automatically obtaining modules from user build files')
    if app_build_type == 'maven':
        '''
        for each user pom file, we call exec:exec with executable "echo".
        exec:exec runs the executable on every module of the project
        we redirect the echo output to the modules_properties_file 
        '''
        for app_build_file in app_build_files:
            # set the parameter to the echo executable
            get_modules_args = '{ '
            get_modules_args += '"name" : "${project.artifactId}", '
            get_modules_args += '"directory" : "${basedir}", '
            get_modules_args += '"build_file" : "${basedir}/pom.xml", '
            get_modules_args += '"app_path" : "${project.build.outputDirectory}", '
            get_modules_args += '"user_build_file" : "' + app_build_file + '"'
            get_modules_args += ' },'

            # exec:exec can not have " in the arguments, so we replace it with _tkltest_quot_
            get_modules_args = get_modules_args.replace('"', '_tkltest_quot_')
            #call exec:exec with echo:
            get_modules_command = 'mvn --quiet'
            get_modules_command += ' -f ' + app_build_file
            if os.name == 'nt':
                get_modules_command += ' exec:exec -Dexec.executable=cmd.exe -Dexec.args='
                get_modules_command += '"/c echo ' + get_modules_args + '"'
            else:
                get_modules_command += ' exec:exec -Dexec.executable="echo" -Dexec.args='
                get_modules_command += '\'' + get_modules_args + '\''

            get_modules_command += ' >> ' + modules_properties_file
            try:
                command_util.run_command(command=get_modules_command, verbose=tkltest_user_config['general']['verbose'])
            except subprocess.CalledProcessError as e:
                tkltest_status('running {} command "{}" failed: {}\n{}'.
                               format(app_build_type, get_modules_command, e, e.stderr), error=True)
                try:
                    tkltest_status('Command to obtain modules from user build files failed with the following output:')
                    get_modules_command = get_modules_command.replace('--quiet', '')
                    get_modules_command = get_modules_command.replace(' >> ' + modules_properties_file, '')
                    command_util.run_command(command=get_modules_command, verbose=True)
                except subprocess.CalledProcessError as e:
                    pass
                sys.exit(1)

    elif app_build_type == 'gradle':
        if not app_settings_files:
            app_settings_files = [''] * len(app_build_files)
        elif len(app_build_files) != len(app_settings_files):
            tkltest_status('app_build_files and app_settings_files must have the same size', error=True)
            sys.exit(1)

        for app_build_file, app_settings_file in zip(app_build_files, app_settings_files):
            # set the parameter to the echo executable
            task_name = 'tkltest_get_module_properties'
            properties_dict = '{ '
            properties_dict += ' "name" : "${project.name}",'
            properties_dict += ' "directory" : "${project.projectDir}",'
            properties_dict += ' "build_file" : "${project.projectDir}/build.gradle",'
            properties_dict += ' "app_path" : "${project.sourceSets.main.output.classesDirs.getFiles()}",'
            properties_dict += ' "classpath" : "${project.sourceSets.main.runtimeClasspath.getFiles()}",'
            properties_dict += ' "user_build_file" : "' + pathlib.PurePath(app_build_file).as_posix() + '"'
            if app_settings_files:
                properties_dict += ', "user_settings_file" : "' + pathlib.PurePath(app_settings_file).as_posix() + '"'
            properties_dict += ' },'

            # gradle can not have " in the write(), so we replace it with _tkltest_quot_
            properties_dict = properties_dict.replace('"', '_tkltest_quot_')
            print_properties_line = '     fw.write( "' + properties_dict + '\\n" ); '
            if app_settings_file:
                print_properties_line += '\n    project.rootProject.subprojects.forEach { fw.write( "' + properties_dict.replace('${project.', '${it.') + '\\n" ); }'

            task_text = [
                'public class WriteStringClass extends DefaultTask {',
                '  @TaskAction',
                '  void writeString(){',
                '    FileWriter fw;',
                '    fw = new FileWriter( "' + pathlib.PurePath(modules_properties_file).as_posix() + '", true);',
                print_properties_line,
                '    fw.close();',
                '  }',
                '}',
                'task ' + task_name + ' (type:WriteStringClass) {}']

            __add_and_run_gradle_task(app_build_file=app_build_file,
                                      app_settings_file=app_settings_file,
                                      task_name=task_name,
                                      task_text=task_text,
                                      verbose=tkltest_user_config['general']['verbose'])
    elif app_build_type == 'ant':
        tkltest_status('Automatically obtaining modules from Ant build files is not supported', error=True)
        sys.exit(1)

    with open(modules_properties_file) as f:
        modules_properties = f.read()
    modules_properties = modules_properties.replace('[', '').replace(']', '')
    modules_properties = modules_properties[:-2] + '\n' # removing the final "," from the list
    modules_properties = modules_properties.replace('_tkltest_quot_', '"')
    modules_properties = modules_properties.replace('\\', '\\\\')
    modules_properties = '[\n' + modules_properties + ']'
    with open(modules_properties_file, 'w') as f:
        f.write(modules_properties)

    with open(modules_properties_file) as f:
        all_modules = json.load(f)

    if not all_modules:
        tkltest_status('Failed to load modules from properties file {}.'.format(modules_properties_file), error=True)

    for module in all_modules:
        if 'classpath' not in module.keys():
            module['classpath'] = ''
        module['app_path'] = module['app_path'].replace(' ', '').split(',')
        module['app_path'] = [path for path in module['app_path'] if os.path.isdir(path)]
        module['classpath'] = module['classpath'].replace(' ', '').split(',')
        module['classpath'] = [path for path in module['classpath'] if os.path.isfile(path)]
    modules_names = set([m['name'] for m in all_modules])

    modules = []
    for module_name in modules_names:
        module_entries = [m for m in all_modules if m['name'] == module_name]
        '''
        we check that all entries are of the same module. 
        i.e has the same build file
        '''
        if len(module_entries) > 1:
            module_build_files = set([m['build_file'] for m in module_entries])
            if len(module_build_files) > 1:
                tkltest_status('got a module with the same name "{}", in {} different build files:\n{}\n'.
                               format(module_name, len(module_build_files), '\n'.join(module_build_files)), error=True)
                sys.exit(1)
        module = module_entries[0]
        if module['app_path']:
            modules.append(module)
        else:
            tkltest_status(' app_path dir of module {} does not exist, omitting the module '.format(module['name']))

    if not modules:
        tkltest_status('Failed to automatically obtain modules from user build files. all {} modules were omitted.\n'
                       'for more details, see modules properties at {}'
                       .format(len(all_modules), modules_properties_file), error=True)
        sys.exit(1)
    tkltest_status('Obtained {} module{} from user build files'.format(len(modules),
                                                                       "" if len(modules) == 1 else "s"))
    return modules

if __name__ == '__main__':
    config_file = sys.argv[1]
    print('config_file={}'.format(config_file))
    with open(config_file, 'r') as f:
        file_config = toml.load(f)
    print('file_config={}'.format(file_config))
    base_config = init_config(test_level='ui')
    print('base_config={}'.format(base_config))
    __merge_config(base_config, file_config, base_config_internal=config_options_ui.get_options_spec_internal())
    print('updated_config={}'.format(base_config))
    # failure_msgs = __validate_config(base_config, test_level='unit', command='generate', subcommand='ctd_amplified')
    # print('failure_msgs={}'.format(failure_msgs))
