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

import os
import posixpath

import xml.etree.ElementTree as ElementTree


def __create_classpath_from_ant_build_file(ant_build_file, classpath_filename, application_base_dir):
    """ Creates classpath file named classpath_filename.
        Paths are relative to the start_dir directory.
    """
    tree = ElementTree.parse(ant_build_file)
    root = tree.getroot()
    content = ""

    for location_node in root.findall("./path[@id='classpath']/pathelement[@location]"):
        location = location_node.get('location')
        if os.path.isabs(location):
            content += (location + '\n')
        else:
            # The location attribute is relative to the app's project base directory
            content += (posixpath.normpath(application_base_dir + posixpath.sep + location) + '\n')

    with open(classpath_filename, 'w') as classpath_file:
        classpath_file.write(content)


if __name__ == '__main__':
    __create_classpath_from_ant_build_file("../../TEST_71_ext4j_1l/TEST_71_ext4j_1l-ctd-amplified-tests/build.xml",
                                           "../../TEST_71_new_classpath.xml",
                                           "/home/victoria/testing/tackle-test-generator-cli/TEST_71_ext4j_1l/TEST_71_ext4j_1l-ctd-amplified-tests")
