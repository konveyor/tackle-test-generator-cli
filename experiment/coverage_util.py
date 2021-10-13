import os
import subprocess
import sys
from typing import Optional, List

import toml


# names of files containing raw coverage data
RAW_COVERAGE_FILES = ['merged_jacoco.exec']

# jacoco CLI jar
JACOCO_CLI_JAR = os.path.join('exp_lib', 'org.jacoco.cli-0.8.7-nodeps.jar')

# tkltest config file name
TKLTEST_CONFIG_FILE = 'tkltest_config.toml'


def generate_coverage_xml(rootdir, only_dirs_containing: Optional[List]=None):
    """Generates jacoco XML files from raw coverage (.exec) files.

     Walks the directory tree starting at the given root directory, identifies relevant jacoco
     raw coverage files, and runs the jacoco CLI to generate XML report from the raw coverage file.
     The XML reports contains method-level coverage information (line, instruction, branch, etc.).
     To run the jacoco CLI, directories to the application classes are needed, which is iedentified
     using the tkltest config files.

     Args:
         rootdir: Root directory to begin traversal from
         only_dirs_containing: only generate for sub-directories whose name contains one of these strings (None=ignore)
    """
    # get directories containing tkltest config files; each such directory will be the root directory
    # for an experiment trial for which the config file is used to get spec of app classes directories
    # be used for generating XML coverage files for all raw coverage files occurring under that root
    dirs_with_tkltest_config = [
        dirpath for dirpath, dirs, files in os.walk(rootdir)
        for file in files if file == TKLTEST_CONFIG_FILE
    ]
    print('dirs_with_tkltest_config: {}'.format(dirs_with_tkltest_config))

    jacoco_cli_cmd = 'java -jar {} report'.format(JACOCO_CLI_JAR)

    # iterate over each experiment trial root directory
    for exp_trial_root in dirs_with_tkltest_config:

        # get app classpath list and create options to jacoco cli command
        tkltest_config = toml.load(os.path.join(exp_trial_root, TKLTEST_CONFIG_FILE))
        jacoco_classfiles_ops = ''
        for classpath in tkltest_config['general']['monolith_app_path']:
            jacoco_classfiles_ops += '--classfiles {} '.format(classpath)

        # find all raw coverage files under trial dir, and generate XML coverage file for each raw coverage file
        for dirpath, dirs, files in os.walk(exp_trial_root):
            if only_dirs_containing:
                skip = True
                for val in only_dirs_containing:
                    if val in dirpath:
                        skip = False
                        break
                if skip:
                    continue
            for file in files:
                if file in RAW_COVERAGE_FILES:
                    print('Generating jacoco XML file for {}: {}'.format(dirpath, file))
                    xml_cov_file = file.split('.')[0] + '.xml'
                    jacoco_cmd = '{} {} {} --xml {}'.format(jacoco_cli_cmd,
                                                            os.path.join(dirpath, file),
                                                            jacoco_classfiles_ops,
                                                            os.path.join(dirpath, xml_cov_file))
                    print('jacoco CLI command: {}'.format(jacoco_cmd))
                    try:
                        completed_proc = subprocess.run(jacoco_cmd, shell=True, check=True,
                                                        stdout=subprocess.PIPE,
                                                        stderr=subprocess.PIPE,
                                                        encoding=sys.getfilesystemencoding())
                        print('{}'.format(completed_proc.stdout))
                    except subprocess.CalledProcessError as e:
                        print('ERROR: {}'.format(e.stderr))


if __name__ == '__main__':
    generate_coverage_xml('output')
