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
import os
import subprocess
import sys
import pathlib
import shutil

from yattag import Doc, indent
from jinja2 import Environment, PackageLoader
import xml.etree.ElementTree as ElementTree
from xml.dom import minidom

from tkltest.util import constants
from tkltest.util.logging_util import tkltest_status

required_lib_jars = {
    ###
    # this is a list of dependencies that are needed for both the testing build file, and the app build file
    # each dependency is represented as:
    # (is_needed_for_app_build_file, groupId, artifactId, version, classifier)
    ###
    (True, 'junit', 'junit', '4.13.1', ''),
    (True, 'org.hamcrest', 'hamcrest-all', '1.3', ''),
    (True, 'com.github.EvoSuite.evosuite', 'evosuite-standalone-runtime', constants.EVOSUITE_VERSION, ''),
    (True, 'com.github.EvoSuite.evosuite', 'evosuite-master', constants.EVOSUITE_VERSION, ''),
    (False, 'org.jacoco', 'org.jacoco.cli', constants.JACOCO_MAVEN_VERSION, 'nodeps'),
    (False, 'org.jacoco', 'org.jacoco.agent', constants.JACOCO_MAVEN_VERSION, ''),
}

# def __get_jars_for_tests_execution():
#     required_lib_jars = {
#         constants.JACOCO_CLI_JAR_NAME,
#         'org.jacoco.agent-0.8.7.jar',
#         'junit-4.13.1.jar',
#         'hamcrest-all-1.3.jar',
#         'evosuite-standalone-runtime-' + constants.EVOSUITE_VERSION + '.jar',
#         'evosuite-master-' + constants.EVOSUITE_VERSION + '.jar',
#     }
#     return [
#         os.path.join(os.path.abspath(dp), f) for dp, dn, filenames in os.walk(constants.TKLTEST_LIB_DIR) for f in filenames
#         if os.path.splitext(f)[1] == '.jar' and f in required_lib_jars
#     ]

def __get_jars_for_tests_execution():
    required_lib_jars_names = [artifactId + '-' + version + ('-' + classifier if classifier else '') + '.jar'
                               for needed_for_app_build_file, groupId, artifactId, version, classifier in required_lib_jars]
    return [
        os.path.join(os.path.abspath(dp), f) for dp, dn, filenames in os.walk(constants.TKLTEST_LIB_DIR) for f in filenames
        if os.path.splitext(f)[1] == '.jar' and f in required_lib_jars_names
    ]


def get_build_classpath(config, subcommand='ctd-amplified'):
    """Creates and returns build classpath.

    Creates and returns build path, consisting of app library dependencies and
    tkltest dependencies required for building and running the generated test cases.

    Args:
        config: loaded configuration information
        # partition: name of partition (if build is being done for a partition of the refactored app)

    Returns:
        string representing build path
    """
    class_paths = []
    classpath_file = config['general']['app_classpath_file']

    # add app libraries specified in classpath file to classpath
    with open(classpath_file) as file:
        for line in file:
            if line.strip():
                if line.endswith('\n'):
                    class_paths.append(os.path.abspath(line[:-1]))
                else:
                    class_paths.append(os.path.abspath(line))

    # add path for app classes for mono or micro version
    # if partition is not None:
    #     if partition not in config['execute']['micro']['partition_paths'].keys():
    #         raise Exception('Partition path not specified for partition: {}'.format(partition))
    #     for part_path in config['execute']['micro']['partition_paths'][partition]:
    #         class_paths.insert(0, os.path.abspath(part_path))
    #     logging.debug('Added paths for partition {}: {}'.format(
    #         partition, config['execute']['micro']['partition_paths'][partition]
    #     ))

    class_paths.extend(__get_jars_for_tests_execution())

    # if config['generate']['jee_support'] and subcommand == 'ctd-amplified':
    #         class_paths.insert(0, os.path.abspath(config['general']['app_name']+constants.TKL_EVOSUITE_OUTDIR_SUFFIX))  # for EvoSuite Scaffolding classes

    classpath_str = os.pathsep.join(class_paths)
    logging.info('classpath: {} '.format(classpath_str))
    return classpath_str


def generate_build_xml(app_name, build_type, monolith_app_path, app_classpath, test_root_dir, test_dirs,
                       # partitions_file,
                       target_class_list, main_reports_dir, app_packages='',
                       collect_codecoverage=False, offline_instrumentation=False, output_dir=''):
    """Generates Ant build.xml, Maven pom.xml, or Gradle build.gradle for running tests.

    Generates a build file depending on the build_type, for running generated tests and collecting coverage information.

    Args:
        app_name: name of the app under test
        build_type: build type of the app, type of build file to generate (ant, maven or gradle)
        monolith_app_path: paths to classes for the app under test
        app_classpath: Java CLASSPATH for building the app under test
        test_root_dir: root directory of test cases
        test_dirs: subdirectories containing test cases under root directory
        # partitions_file: file containing partitions information
        target_class_list: list of target classes for testing
        main_reports_dir: root directory for test reports
        app_packages: app packages to be tracked for code coverage
        collect_codecoverage: whether to collect code coverage data
        offline_instrumentation whether to perform offline instrumentation of app classes
        output_dir: running directory
    """
    # if partitions_file:
    #     with open(app_name + constants.TKL_CTD_TEST_PLAN_FILE_SUFFIX) as ctd_model:
    #         ctd_model_data = json.load(ctd_model)
    #         class_list = [item[1].keys() for item in ctd_model_data["models_and_test_plans"].items()]
    #         class_set = set().union(*class_list)
    #         app_reported_packages = list(class_set)
    # elif
    if target_class_list:
        app_reported_packages = target_class_list
    else:
        app_reported_packages = []

    # set the build xml file name and content based on the build file
    # if micro:
    #     build_xml_file += 'micro.xml'
    # else:
    #     build_xml_file += 'mono.xml'

    if build_type == 'ant':
        generated_build_file = test_root_dir + os.sep + 'build.xml'
        __build_ant(app_classpath, app_name, monolith_app_path, test_root_dir, test_dirs, collect_codecoverage,
                    app_packages, app_reported_packages, offline_instrumentation, main_reports_dir,
                    generated_build_file, output_dir)

        # TODO: this is a hack to enable defining namespace in the build file, since doc tags do not allow colons in attributes
        with open(generated_build_file, 'r') as inp:
            content = inp.read().replace("xmlnsjacoco", "xmlns:jacoco")
        with open(generated_build_file, 'w') as outp:
            outp.write(content)

    elif build_type == 'maven':
        generated_build_file = test_root_dir + os.sep + 'pom.xml'
        __build_maven(app_classpath, app_name, monolith_app_path, test_root_dir, test_dirs, collect_codecoverage,
                      app_packages, app_reported_packages, offline_instrumentation, main_reports_dir,
                      generated_build_file, output_dir)

    else:
        generated_build_file = test_root_dir + os.sep + 'build.gradle'
        __build_gradle(app_classpath, app_name, monolith_app_path, test_root_dir, test_dirs, collect_codecoverage,
                       app_packages, offline_instrumentation, main_reports_dir, generated_build_file, output_dir)

    return generated_build_file


def __build_ant(classpath_list, app_name, monolith_app_paths, test_root_src_dir, test_src_dirs, collect_codecoverage,
                app_collected_packages, app_reported_classes, offline_instrumentation, report_output_dir,
                build_xml_file, output_dir):
    classpath_list = classpath_list.split(os.pathsep)
    doc, tag, text = Doc().tagtext()
    test_root_src_dir = os.path.abspath(test_root_src_dir)
    main_junit_dir = os.path.abspath(report_output_dir + os.sep + constants.TKL_JUNIT_REPORT_DIR)
    main_coverage_dir = os.path.abspath(report_output_dir + os.sep + constants.TKL_CODE_COVERAGE_REPORT_DIR + os.sep +
                                        os.path.basename(test_root_src_dir))
    inst_app_path = os.path.join(output_dir, app_name + "-instrumented-classes")
    with tag('project', name='tkl_tests'):

        with tag('taskdef', uri="antlib:org.jacoco.ant", resource="org/jacoco/ant/antlib.xml"):
            doc.stag('classpath', path=os.path.abspath(os.path.join(constants.TKLTEST_LIB_DOWNLOAD_DIR,
                                                                    "org.jacoco.ant-0.8.7-nodeps.jar")))

        with tag('path', id='classpath'):
            for current_str in classpath_list:
                doc.stag('pathelement', location=current_str)
            for mono_path in monolith_app_paths:
                if collect_codecoverage and offline_instrumentation:
                    doc.stag('pathelement', location=os.path.abspath(inst_app_path))
                else:
                    doc.stag('pathelement', location=os.path.abspath(mono_path))

        with tag('target', name='delete-classes'):
            with tag('delete'):
                doc.stag('fileset', dir=test_root_src_dir, includes="**/*.class")
                if collect_codecoverage:
                    doc.stag('fileset', dir=test_root_src_dir, includes="**/*jacoco.exec")
            if collect_codecoverage and offline_instrumentation:
                doc.stag('mkdir', dir=inst_app_path)
                with tag('delete'):
                    doc.stag('fileset', dir=inst_app_path, includes="**/*.class")
                with tag('jacoco:instrument', destdir=os.path.abspath(inst_app_path),
                         xmlnsjacoco="antlib:org.jacoco.ant"):
                    for path in monolith_app_paths:
                        if os.path.isdir(path):
                            doc.stag('fileset', dir=os.path.abspath(path))  # , includes=app_collected_packages)
                        else:
                            doc.stag('fileset', file=os.path.abspath(path))

        for test_src_dir in test_src_dirs:
            current_partition = os.path.basename(test_src_dir)
            test_src_dir = os.path.abspath(test_src_dir)

            # compile each partition separately due to duplicate class names
            with tag('target', name='compile-classes_' + current_partition, depends='delete-classes'):
                # running with debug set, so we can see failing liine numbers in junit output
                with tag('javac', srcdir=test_src_dir, includeantruntime='false', debug=True):
                    doc.stag('classpath', refid='classpath')

            # run with or without code coverage collection
            with tag('target', name='execute-tests_' + current_partition,
                     depends='compile-classes_' + current_partition):
                current_output_dir = main_junit_dir + '/' + current_partition
                doc.stag('mkdir', dir=current_output_dir)
                doc.stag('mkdir', dir=current_output_dir + '/raw')
                doc.stag('mkdir', dir=current_output_dir + '/html')
                if collect_codecoverage:
                    with tag('jacoco:coverage', destfile=test_src_dir + "/jacoco.exec",
                             includes=":".join(app_collected_packages),
                             xmlnsjacoco="antlib:org.jacoco.ant"):
                        __create_junit_task(doc, tag, classpath_list, test_src_dir, current_output_dir)
                else:
                    __create_junit_task(doc, tag, classpath_list, test_src_dir, current_output_dir)

            with tag('target', name='test-reports_' + current_partition, depends='execute-tests_' + current_partition):
                with tag('junitreport', todir=current_output_dir):
                    with tag('fileset', dir=current_output_dir + '/raw'):
                        doc.stag('include', name='TEST-*.xml')
                    doc.stag('report', format='noframes', todir=current_output_dir + '/html')

            with tag('target', name='coverage-reports_' + current_partition,
                     depends="test-reports_" + current_partition):
                with tag('jacoco:report', xmlnsjacoco="antlib:org.jacoco.ant"):
                    with tag('executiondata'):
                        doc.stag('file', file=test_src_dir + '/jacoco.exec')
                    with tag('structure', name='Jacoco'):
                        with tag('classfiles'):
                            for path in monolith_app_paths:
                                if app_reported_classes:
                                    for cls in app_reported_classes:
                                        cls_file = path + os.sep + cls.replace('.', os.sep) + ".class"
                                        if os.path.isfile(cls_file):
                                            doc.stag('fileset', file=os.path.abspath(cls_file))
                                else:
                                    if os.path.isdir(path):
                                        doc.stag('fileset', dir=os.path.abspath(path))
                                    else:
                                        doc.stag('fileset', file=os.path.abspath(path))
                    doc.stag('html', destdir=main_coverage_dir + "/" + current_partition)

        partitions_tasks = ['test-reports_' + os.path.basename(test_src_dir) for test_src_dir in test_src_dirs]
        tasks_joined = ','.join(partitions_tasks)

        with tag('target', name='merge-coverage', depends=tasks_joined):
            with tag('jacoco:merge', destfile=test_root_src_dir + '/merged_jacoco.exec',
                     xmlnsjacoco="antlib:org.jacoco.ant"):
                doc.stag('fileset', dir=test_root_src_dir, includes="**/*.exec")

        with tag('target', name='merge-coverage-report', depends='merge-coverage'):
            with tag('jacoco:report', xmlnsjacoco="antlib:org.jacoco.ant"):
                with tag('executiondata'):
                    doc.stag('file', file=test_root_src_dir + '/merged_jacoco.exec')

                with tag('structure', name='Jacoco'):
                    with tag('classfiles'):
                        for path in monolith_app_paths:
                            if app_reported_classes:
                                for cls in app_reported_classes:
                                    cls_file = path + os.sep + cls.replace('.', os.sep) + ".class"
                                    if os.path.isfile(cls_file):
                                        doc.stag('fileset', file=os.path.abspath(cls_file))
                            else:
                                if os.path.isdir(path):
                                    doc.stag('fileset', dir=os.path.abspath(path))
                                else:
                                    doc.stag('fileset', file=os.path.abspath(path))
                doc.stag('html', destdir=main_coverage_dir)
                doc.stag('csv', destfile=os.path.join(main_coverage_dir, os.path.basename(test_root_src_dir) + ".csv"))
                doc.stag('xml', destfile=os.path.join(main_coverage_dir, "jacoco.xml"))

    result = indent(
        doc.getvalue(),
        indentation=' ' * 4,
        newline='\r\n'
    )
    with open(build_xml_file, 'w') as outfile:
        outfile.write(result)


def __create_junit_task(doc, tag, classpath_list, test_src_dir, current_output_dir):
    with tag('junit', printsummary='on', haltonfailure="no", fork='true', forkmode='once',
             showoutput='yes'):
        with tag('classpath'):
            doc.stag('path', refid='classpath')
            for cp_str in classpath_list:
                if cp_str.__contains__("junit"):
                    doc.stag('pathelement', location=cp_str)
                if cp_str.__contains__("hamcrest"):
                    doc.stag('pathelement', location=cp_str)
            # for mono_path in monolith_app_paths:
            #   doc.stag('pathelement', location=os.path.abspath(mono_path))
            doc.stag('pathelement', location=test_src_dir)

        with tag('batchtest', todir=current_output_dir + '/raw'):
            doc.stag('fileset', dir=test_src_dir, includes="**/*.class",
                     excludes="**/*ESTest_scaffolding.class")
        doc.stag('formatter', type='xml')


def __build_maven(classpath_list, app_name, monolith_app_paths, test_root_dir, test_dirs, collect_codecoverage,
                  app_collected_packages, app_reported_packages, offline_instrumentation, report_output_dir,
                  build_xml_file, output_dir):
    classpath_list = classpath_list.split(os.pathsep)
    doc, tag, text, line = Doc().ttl()
    test_root_dir = os.path.abspath(test_root_dir)
    main_junit_dir = os.path.abspath(report_output_dir + os.sep + constants.TKL_JUNIT_REPORT_DIR)
    main_coverage_dir = os.path.abspath(report_output_dir + os.sep + constants.TKL_CODE_COVERAGE_REPORT_DIR + os.sep +
                                        os.path.basename(test_root_dir))
    with tag('project', xmlns="http://maven.apache.org/POM/"+constants.MAVEN_VERSION):
        line('modelVersion', constants.MAVEN_VERSION)
        line('groupId', 'org.jacoco')
        line('artifactId', 'Jacoco')
        line('version', constants.JACOCO_MAVEN_VERSION)
        with tag('properties'):
            line('maven.compiler.source', constants.JAVA_VERSION_FOR_MAVEN)
            line('maven.compiler.target', constants.JAVA_VERSION_FOR_MAVEN)
        with tag('dependencies'):
            with tag('dependency'):
                line('groupId', 'org.glassfish.main.extras')
                line('artifactId', 'glassfish-embedded-all')
                line('version', '3.1.2.2')
                line('scope', 'test')
            for full_path in classpath_list:
                if full_path.strip() and os.path.isdir(full_path):
                    try:
                        subprocess.run('jar cf '+os.path.basename(full_path)+'.jar -C '+full_path+" .", shell=True, check=True)
                    except subprocess.CalledProcessError as e:
                        tkltest_status('Creating a jar for dependency folder failed: {}\n{}'.format(e, e.stderr), error=True)
                        sys.exit(1)
                    full_path += ".jar"
                file_name = full_path.rsplit(os.path.sep,1)[1]
                file_name = file_name.replace('.jar', '')
                if 'org.jacoco.agent' in file_name:
                    with tag('dependency'):
                        line('groupId', 'org.jacoco')
                        line('artifactId', 'org.jacoco.agent')
                        line('version', constants.JACOCO_MAVEN_VERSION)
                        line('scope', 'test')
                        line('classifier', 'runtime')
                elif os.path.isfile(full_path):
                    with tag('dependency'):
                        line('groupId', file_name)
                        line('artifactId', file_name)
                        line('version', '1.0')
                        line('scope', 'system')
                        line('systemPath', full_path)

        with tag('build'):
            with tag('resources'):
                for app_path in monolith_app_paths:
                    with tag('resource'):
                        line('directory', os.path.abspath(app_path))
            for test_src_dir in test_dirs:
                if os.path.basename(test_src_dir) in ['target', 'build']:
                    continue # skip compilation output directory
                current_partition = os.path.basename(test_src_dir)
                junit_output_dir = os.path.join(main_junit_dir, current_partition)
                line('testSourceDirectory', os.path.abspath(test_src_dir))
                with tag('plugins'):
                    with tag('plugin'):
                        line('groupId', 'org.apache.maven.plugins')
                        line('artifactId', 'maven-site-plugin')
                        line('version', constants.MAVEN_SITE_PLUGIN_VERSION)
                        with tag('configuration'):
                            line('outputDirectory', junit_output_dir + '/html')
                    with tag('plugin'):
                        line('groupId', 'org.apache.maven.plugins')
                        line('artifactId', 'maven-project-info-reports-plugin')
                        line('version', constants.MAVEN_REPORTS_PLUGIN_VERSION)
                    if collect_codecoverage:
                        with tag('plugin'):
                            line('groupId', 'org.jacoco')
                            line('artifactId', 'jacoco-maven-plugin')
                            line('version', constants.JACOCO_MAVEN_VERSION)
                            with tag('executions'):
                                if offline_instrumentation:
                                    with tag('execution'):
                                        line('id', 'default-instrument')
                                        with tag('goals'):
                                            line('goal', 'instrument')
                                    with tag('execution'):
                                        line('id', 'default-restore-instrumented-classes')
                                        with tag('goals'):
                                            line('goal', 'restore-instrumented-classes')
                                else:
                                    with tag('execution'):
                                        line('id', 'jacoco-initialize')
                                        with tag('goals'):
                                            line('goal', 'prepare-agent')
                                        with tag('configuration'):
                                            line('destFile', os.path.join(os.path.abspath(test_src_dir), 'jacoco.exec'))
                                with tag('execution'):
                                    line('id', 'generate-code-coverage-report')
                                    with tag('goals'):
                                        line('goal', 'report')
                                    with tag('configuration'):
                                        line('dataFile', os.path.join(os.path.abspath(test_src_dir), 'jacoco.exec'))
                                        line('outputDirectory', main_coverage_dir)
                                        if app_reported_packages:
                                            with tag('rules'):
                                                with tag('rule'):
                                                    line('element', 'CLASS')
                                                    with tag('includes'):
                                                        for reported_class in app_reported_packages:
                                                            line('include', reported_class.replace('.', '/')+".*")
                                        else:
                                            with tag('rules'):
                                                with tag('rule'):
                                                    line('element', 'PACKAGE')
                                                    with tag('includes'):
                                                        for package in app_collected_packages:
                                                            if package != '*':
                                                                line('include', package.replace('.', '/') + "*/*.class")
                                                            else:
                                                                line('include', '**/*')

                    with tag('plugin'):
                        line('groupId', 'org.apache.maven.plugins')
                        line('artifactId', 'maven-surefire-plugin')
                        line('version', constants.MAVEN_SURFIRE_VERSION)
                        with tag('configuration'):
                            line('testFailureIgnore', 'true')
                            line('reportsDirectory', junit_output_dir + '/raw')
                            with tag('systemPropertyVariables'):
                                line('jacoco-agent.destfile', os.path.join(os.path.abspath(test_src_dir), 'jacoco.exec'))
                        with tag('dependencies'):
                           with tag('dependency'):
                                line('groupId', 'org.apache.maven.surefire')
                                line('artifactId', 'surefire-junit47')
                                line('version', constants.MAVEN_SURFIRE_VERSION)
        with tag('reporting'):
                with tag('plugins'):
                    with tag('plugin'):
                        line('groupId', 'org.apache.maven.plugins')
                        line('artifactId', 'maven-surefire-report-plugin')
                        line('version', constants.MAVEN_SURFIRE_VERSION)
                        with tag('reportSets'):
                            with tag('reportSet'):
                                with tag('reports'):
                                    line('report', 'report-only')
                        with tag('configuration'):
                            for test_src_dir in test_dirs:
                                if os.path.basename(test_src_dir) in ['target', 'build']:
                                    continue  # skip compilation output directory
                                current_partition = os.path.basename(test_src_dir)
                                junit_output_dir = os.path.join(main_junit_dir, current_partition)
                                line('reportsDirectories', junit_output_dir + '/raw')
                    if collect_codecoverage:
                        with tag('plugin'):
                            line('groupId', 'org.jacoco')
                            line('artifactId', 'jacoco-maven-plugin')
                            with tag('reportSets'):
                                with tag('reportSet'):
                                    with tag('reports'):
                                        line('report', 'report')

    result = indent(
        doc.getvalue(),
        indentation=' ' * 4
    )
    with open(build_xml_file, 'w') as outfile:
        outfile.write(result)


def __build_gradle(classpath_list, app_name, monolith_app_paths, test_root_dir, test_dirs, collect_codecoverage,
                  app_packages, offline_instrumentation, report_output_dir, build_gradle_file, output_dir):

    #gradle accept only posix paths, so we uses PurePath to convert:
    classpath_list = [pathlib.PurePath(os.path.abspath(classpath)).as_posix() for classpath in classpath_list.split(os.pathsep)]
    inst_classes = pathlib.PurePath(os.path.join(output_dir, app_name + "-instrumented-classes")).as_posix()
    test_dirs = [pathlib.PurePath(os.path.abspath(test_dir)).as_posix() for test_dir in test_dirs if not os.path.basename(test_dir) == 'build']
    monolith_app_paths = [pathlib.PurePath(os.path.abspath(monolith_app_path)).as_posix() for monolith_app_path in monolith_app_paths]
    app_packages = [pathlib.PurePath(os.path.abspath(app_package)).as_posix() for app_package in app_packages]
    main_junit_report_dir = pathlib.PurePath(os.path.abspath(report_output_dir + os.sep + constants.TKL_JUNIT_REPORT_DIR)).as_posix()
    main_coverage_report_dir = pathlib.PurePath(os.path.abspath(report_output_dir + os.sep + constants.TKL_CODE_COVERAGE_REPORT_DIR + os.sep +
                                        os.path.basename(test_root_dir))).as_posix()
    coverage_exec_file = pathlib.PurePath(os.path.join(os.path.abspath(test_root_dir), 'jacoco.exec')).as_posix()
    coverage_xml_file = pathlib.PurePath(os.path.join(os.path.abspath(main_coverage_report_dir), 'jacoco.xml')).as_posix()
    coverage_csv_file = pathlib.PurePath(os.path.join(os.path.abspath(main_coverage_report_dir), 'jacoco.csv')).as_posix()

    # load gradle jinja template using package loader
    env = Environment(loader=PackageLoader('tkltest.util.unit'))
    template = env.get_template('build_gradle.jinja')

    test_dependsOn = ''
    app_classes_for_tests = monolith_app_paths
    if collect_codecoverage:
        if offline_instrumentation:
            test_dependsOn = ',instrument'
            app_classes_for_tests = [inst_classes]
        final_task = 'clean, jacocoTestReport'
    else:
        final_task = 'clean, test'

    s = template.render(classpath_list=classpath_list,
                        monolith_app_paths=monolith_app_paths,
                        app_packages=app_packages,
                        test_dirs=test_dirs,
                        inst_classes=inst_classes,
                        app_classes_for_tests=app_classes_for_tests,
                        main_junit_report_dir=main_junit_report_dir,
                        main_coverage_report_dir=main_coverage_report_dir,
                        coverage_exec_file=coverage_exec_file,
                        coverage_xml_file=coverage_xml_file,
                        coverage_csv_file=coverage_csv_file,
                        test_dependsOn=test_dependsOn,
                        final_task=final_task)

    with open(build_gradle_file, 'w') as outfile:
        outfile.write(s)


def __get_xml_element(parent_element, namespaces, name, text='', duplicate=False):
    '''
    add an element to an xml tree
    :param parent_element: the perent to add to
    :param namespaces: the tree namesapce, need for find()
    :param name: element name
    :param text: element text
    :param duplicate: bool - create another element, if an element with the same name exist
    :return: the element that was added
    '''
    if not duplicate:
        element = parent_element.find(name, namespaces)
    if duplicate or not element:
        element = ElementTree.Element(name)
        if text:
            element.text = text
        parent_element.append(element)
    return element

def integrate_tests_into_app_build_file(app_build_files, app_build_type, test_dirs):
    '''
    Adding the test directory and the dependencies to the user app file
    :param app_build_files: the app build file
    :param app_build_type: build type
    :param test_dirs: list of test directories
    :return:
    '''
    if not app_build_files:
        return
    app_build_file = app_build_files[0]
    tkltest_app_build_file = os.path.abspath('tkltest_app_' + os.path.basename(app_build_file))
    dependencies_jars = __get_jars_for_tests_execution()
    abs_test_dirs = [os.path.abspath(test_src_dir) for test_src_dir in test_dirs if os.path.basename(test_src_dir) not in ['target', 'build']]
    if app_build_type == 'maven':
        # tree and root of original build file
        build_file_tree = ElementTree.parse(app_build_file)
        project_root = build_file_tree.getroot()
        namespace = project_root.tag.split('}')[0].strip('{') if project_root.tag.startswith('{') else None
        namespaces = {'': namespace} if namespace else None
        if namespace:
            ElementTree.register_namespace('', namespace)

        # adding jitpack repository:
        repositories_element = __get_xml_element(project_root, namespaces, 'repositories')
        repository_element = __get_xml_element(repositories_element, namespaces, 'repository', '', True)
        __get_xml_element(repository_element, namespaces, 'id', 'jitpack.io')
        __get_xml_element(repository_element, namespaces, 'url', 'https://jitpack.io')

        # adding the dependencies:
        dependencies_element = __get_xml_element(project_root, namespaces, 'dependencies')
        for needed_for_app_build_file, groupId, artifactId, version, classifier in required_lib_jars:
            if needed_for_app_build_file:
                dependency_element = __get_xml_element(dependencies_element, namespaces, 'dependency', '', True)
                __get_xml_element(dependency_element, namespaces, 'groupId', groupId)
                __get_xml_element(dependency_element, namespaces, 'artifactId', artifactId)
                __get_xml_element(dependency_element, namespaces, 'version', version)
                if classifier:
                    __get_xml_element(dependency_element, namespaces, 'classifier', classifier)
                __get_xml_element(dependency_element, namespaces, 'scope', 'test')


        # adding the test directories:
        # see http://www.mojohaus.org/build-helper-maven-plugin/usage.html
        build_element = __get_xml_element(project_root, namespaces, 'build')
        plugins_element = __get_xml_element(build_element, namespaces, 'plugins')
        plugin_element = __get_xml_element(plugins_element, namespaces, 'plugin', '', True)
        __get_xml_element(plugin_element, namespaces, 'groupId', 'org.codehaus.mojo')
        __get_xml_element(plugin_element, namespaces, 'artifactId', 'build-helper-maven-plugin')
        __get_xml_element(plugin_element, namespaces, 'version', '3.3.0')
        executions_element = __get_xml_element(plugin_element, namespaces, 'executions')
        execution_element = __get_xml_element(executions_element, namespaces, 'execution')
        __get_xml_element(execution_element, namespaces, 'id', 'add-test-source')
        __get_xml_element(execution_element, namespaces, 'phase', 'generate-test-sources')
        __get_xml_element(__get_xml_element(execution_element, namespaces, 'goals',), namespaces, 'goal', 'add-test-source')
        configuration_element = __get_xml_element(execution_element, namespaces, 'configuration')
        sources_element = __get_xml_element(configuration_element, namespaces, 'sources')

        for abs_test_dir in abs_test_dirs:
            __get_xml_element(sources_element, namespaces, 'source', abs_test_dir)

        # writing the new file, removing empty lines
        lines = minidom.parseString(ElementTree.tostring(project_root)).toprettyxml(indent="   ").split('\n')
        with open(tkltest_app_build_file, 'w') as f:
            f.write('\n'.join([line for line in lines if line.strip()]))
        # todo  - use this comments for all types when ant implemented
        tkltest_status('Generated tests are integrated into {}. New build file is saved as: {}.'.format(app_build_file, tkltest_app_build_file))

    elif app_build_type == 'gradle':
        shutil.copy(app_build_file, tkltest_app_build_file)
        with open(tkltest_app_build_file, 'a') as f:
            # adding jitpack repository:
            f.write('\nrepositories{\n maven {url \'https://jitpack.io\'}\n}\n')
            # adding the dependencies:
            f.write('dependencies {\n')
            for needed_for_app_build_file, groupId, artifactId, version, classifier in required_lib_jars:
                if needed_for_app_build_file:
                    dependency = ':'.join([groupId, artifactId, version])
                    if classifier:
                        dependency += ':' + classifier
                    f.write('    implementation \'' + dependency + '\'\n')
            f.write('}\n')
            f.write('sourceSets.test.java.srcDirs = sourceSets.test.java.srcDirs + [\n')
            for abs_test_dir in abs_test_dirs:
                f.write('    \'' + pathlib.PurePath(abs_test_dir).as_posix() + '\',\n')
            f.write(']\n')
        # todo  - use this comments for all types when ant implemented
        tkltest_status('Generated tests are integrated into {}. New build file is saved as: {}.'.format(app_build_file, tkltest_app_build_file))
