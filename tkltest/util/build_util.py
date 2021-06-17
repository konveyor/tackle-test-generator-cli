import json
import logging
import os
import sys

from yattag import Doc, indent

from . import constants
from tkltest.util.logging_util import tkltest_status


def get_build_classpath(config, partition=None):
    """Creates and returns build classpath.

    Creates and returns build path in the Java CLASSPATH format, consisting of app library dependencies and
    tkltest cli library dependencies.

    Args:
        config: loaded configuration information
        partition: name of partition (if build is being done for a partition of the refactored app)

    Returns:
        string representing build path in the Java CLASSPATH format
    """
    class_paths = []
    classpath_file = config['general']['app_classpath_file']

    # add app libraries specified in classpath file to classpath
    with open(classpath_file) as file:
        for line in file:
            class_paths.append(os.path.abspath(line[:-1]))

    # add path for app classes for mono or micro version
    if partition is not None:
        if partition not in config['execute']['micro']['partition_paths'].keys():
            raise Exception('Partition path not specified for partition: {}'.format(partition))
        for part_path in config['execute']['micro']['partition_paths'][partition]:
            class_paths.insert(0, os.path.abspath(part_path))
        logging.debug('Added paths for partition {}: {}'.format(
            partition, config['execute']['micro']['partition_paths'][partition]
        ))

    # add lib dependencies for tkltest to classpath
    class_paths.extend([
        os.path.join(os.path.abspath(dp), f) for dp, dn, filenames in os.walk(constants.TKLTEST_LIB_DIR) for f in filenames
        if os.path.splitext(f)[1] == '.jar'
    ])

    if config['generate']['jee_support']:
        class_paths.insert(0, os.path.abspath("evosuite-tests"))  # for EvoSuite Scaffolding classes

    classpath_str = os.pathsep.join(class_paths)
    logging.info('classpath: {} '.format(classpath_str))
    return classpath_str


def generate_build_xml(app_name, monolith_app_path, app_classpath, test_root_dir, test_dirs,
                           partitions_file, target_class_list, main_reports_dir, app_packages='',
                           collect_codecoverage=False, offline_instrumentation=False):
    """Generates Ant build.xml file and Maven pom.xml for running tests.

    Generates Ant build.xml file and Maven pom.xml for running generated tests and collecting coverage information.

    Args:
        app_name: name of the app under test
        monolith_app_path: paths to classes for the app under test
        app_classpath: Java CLASSPATH for building the app under test
        test_root_dir: root directory of test cases
        test_dirs: subdirectories containing test cases under root directory
        partitions_file: file containing partitions information
        target_class_list: list of target classes for testing
        main_reports_dir: root directory for test reports
        app_packages: app packages to be tracked for code coverage
        collect_codecoverage: whether to collect code coverage data
        offline_instrumentation whether to perform offline instrumentation of app classes
    """
    if partitions_file:
        with open(app_name + constants.TKL_CTD_TEST_PLAN_FILE_SUFFIX) as ctd_model:
            ctd_model_data = json.load(ctd_model)
            class_list = [item[1].keys() for item in ctd_model_data["models_and_test_plans"].items()]
            class_set = set().union(*class_list)
            app_reported_packages = list(class_set)
    elif target_class_list:
        app_reported_packages = target_class_list
    else:
        app_reported_packages = []

    # set the build xml file name and content based on the build file
    ant_build_xml_file = test_root_dir + os.sep + 'build.xml'
    # if micro:
    #     build_xml_file += 'micro.xml'
    # else:
    #     build_xml_file += 'mono.xml'

    __build_ant(app_classpath, app_name, monolith_app_path, test_root_dir, test_dirs, collect_codecoverage,
                app_packages, app_reported_packages, offline_instrumentation, main_reports_dir,
                ant_build_xml_file)

    # TODO: this is a hack to enable defining namespace in the build file, since doc tags do not allow colons in attributes
    with open(ant_build_xml_file, 'r') as inp:
        content = inp.read().replace("xmlnsjacoco", "xmlns:jacoco")
    with open(ant_build_xml_file, 'w') as outp:
        outp.write(content)

    maven_build_xml_file = test_root_dir + os.sep + 'pom.xml'
    __build_maven(app_classpath, app_name, monolith_app_path, test_root_dir, test_dirs, collect_codecoverage,
                  app_packages, offline_instrumentation, main_reports_dir, maven_build_xml_file)

    return ant_build_xml_file, maven_build_xml_file


def __build_ant(classpath_list, app_name, monolith_app_paths, test_root_src_dir, test_src_dirs, collect_codecoverage,
                app_collected_packages, app_reported_classes, offline_instrumentation, report_output_dir,
                build_xml_file):
    classpath_list = classpath_list.split(os.pathsep)
    doc, tag, text = Doc().tagtext()
    test_root_src_dir = os.path.abspath(test_root_src_dir)
    main_junit_dir = os.path.abspath(report_output_dir + os.sep + constants.TKL_JUNIT_REPORT_DIR)
    main_coverage_dir = os.path.abspath(report_output_dir + os.sep + constants.TKL_CODE_COVERAGE_REPORT_DIR + os.sep +
                                        os.path.basename(test_root_src_dir))
    inst_app_path = os.path.join(os.path.dirname(test_root_src_dir), app_name + "-instrumented-classes")
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
                  app_packages, offline_instrumentation, report_output_dir,
                  build_xml_file):
    classpath_list = classpath_list.split(os.pathsep)
    doc, tag, text, line = Doc().ttl()
    test_root_dir = os.path.abspath(test_root_dir)
    main_junit_dir = os.path.abspath(report_output_dir + os.sep + constants.TKL_JUNIT_REPORT_DIR)
    main_coverage_dir = os.path.abspath(report_output_dir + os.sep + constants.TKL_CODE_COVERAGE_REPORT_DIR + os.sep +
                                        os.path.basename(test_root_dir))
    inst_app_path = os.path.join(os.path.dirname(test_root_dir), app_name + "-instrumented-classes")
    with tag('project', xmlns="http://maven.apache.org/POM/"+constants.MAVEN_VERSION):
        line('modelVersion', constants.MAVEN_VERSION)
        line('groupId', 'org.jacoco')
        line('artifactId', 'Jacoco')
        line('version', constants.JACOCO_MAVEN_VERSION)
        with tag('dependencies'):
            for full_path in classpath_list:
                file_name = full_path.rsplit(os.path.sep,1)[1]
                file_name = file_name.replace('.jar', '')
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
                        if offline_instrumentation:
                            line('directory', os.path.abspath(inst_app_path))
                        else:
                            line('directory', os.path.abspath(app_path))
            for test_src_dir in test_dirs:
                if os.path.basename(test_src_dir) == 'target':
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
                            #with tag('configuration'):
                                #with tag('includes'):
                                 #   for collected_package in app_packages:
                                  #      if collected_package == '*':
                                   #         line('include', '**/*')
                                    #    else:
                                     #       line('include', collected_package)
                            with tag('executions'):
                                with tag('execution'):
                                    line('id', 'jacoco-initialize')
                                    with tag('goals'):
                                        line('goal', 'prepare-agent')
                                    with tag('configuration'):
                                        line('destFile', os.path.join(os.path.abspath(test_src_dir), 'jacoco.exec'))
                                with tag('execution'):
                                    line('id', 'generate-code-coverage-report')
                                    line('phase', 'test')
                                    with tag('goals'):
                                        line('goal', 'report')
                                    with tag('configuration'):
                                        line('dataFile', os.path.join(os.path.abspath(test_src_dir), 'jacoco.exec'))
                                        line('outputDirectory', main_coverage_dir)
                                        with tag('rules'):
                                            with tag('rule'):
                                                line('element', 'package')
                                                with tag('includes'):
                                                    for collected_package in app_packages:
                                                        if collected_package == '*':
                                                            line('include', '**/*')
                                                        else:
                                                            line('include', collected_package)

                    with tag('plugin'):
                        line('groupId', 'org.apache.maven.plugins')
                        line('artifactId', 'maven-surefire-plugin')
                        line('version', constants.MAVEN_SURFIRE_VERSION)
                        with tag('configuration'):
                            line('reportsDirectory', junit_output_dir + '/raw')
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
                        with tag('configuration'):
                            line('reportsDirectories', junit_output_dir + '/raw')

    result = indent(
        doc.getvalue(),
        indentation=' ' * 4
    )
    with open(build_xml_file, 'w') as outfile:
        outfile.write(result)