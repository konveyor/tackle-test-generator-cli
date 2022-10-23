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


from tkltest.unit.util import java_class_parser


def get_methods_lines(class_file_name):

    '''
    parse a byte code .class file, and return a dict. of {method signature: list of line number}
    :param class_file_name: .class file path
    :return: a dict of {method signature: list of line numbers}
    '''

    byte_code_data = java_class_parser.JavaClass.from_file(class_file_name)
    byte_code_lines_tables = {}
    for byte_code_method in byte_code_data.methods:
        descriptor_index = byte_code_method.descriptor_index - 1
        name_index = byte_code_method.name_index - 1
        name = byte_code_data.constant_pool[name_index].cp_info.value
        desc = byte_code_data.constant_pool[descriptor_index].cp_info.value
        signature = name + desc
        code_attributes = [att for att in byte_code_method.attributes if att.name_as_str == 'Code']
        if not len(code_attributes):
            continue
        code_attribute_info = code_attributes[0].info
        line_number_attributes = [att for att in code_attribute_info.attributes if att.name_as_str == 'LineNumberTable']
        if not len(line_number_attributes):
            continue
        line_number_attribute_info = line_number_attributes[0].info
        byte_code_lines_tables[signature] = \
                [line_entry.line_number for line_entry in line_number_attribute_info.line_number_table]
    return byte_code_lines_tables



def get_method_parameters(description):
    '''
    see https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.3.2
    the "desc" is in the form of:
    (full name parameters)full name return value
    for example:
    (ILjava/lang/String;Ljava/sql/Timestamp;)Ljava/lang/String;
    we will get a list of only the base name of the parameters, and drop the return value:
    parameters = [int, 'String', 'Timestamp',]
    :param desc:
    :return: parameters list, return_value
    '''

    base_types_names = {'B': 'byte', 'C': 'char', 'D': 'double', 'F': 'float',
                        'I': 'int', 'J': 'long', 'S': 'short', 'Z': 'boolean', 'V': 'void'}
    params_return_str = description.replace('(', '').replace(')', '')
    params_with_return = []
    while len(params_return_str):
        array_dim = len(params_return_str) - len(params_return_str.lstrip('['))
        params_return_str = params_return_str.lstrip('[')
        base_type, params_return_str = params_return_str[0], params_return_str[1:]
        if base_type == 'L':
            parameter, sep, params_return_str = params_return_str.partition(';')
            parameter = parameter.split('/').pop()
        else:
            parameter = base_types_names[base_type]
        parameter += '[]'*array_dim
        params_with_return.append(parameter)

    return_value = params_with_return.pop()
    return params_with_return, return_value

