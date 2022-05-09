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

import subprocess
import sys
import shlex
import os

def run_command(command, verbose, env_vars=None):
    """Runs a command using subprocess.

    Runs the given command using subprocess.run(). If verbose is false, stdout is
    discarded. A pipe is opened to stderr of the subprocess so that the subprocess
    error messages can be captured and printed by the CLI if the command fails.
    If env_vars is specified, the command is executed under the given environment
    variable values
    """
    if verbose:
        if env_vars:
            subprocess.run(command, shell=True, check=True, stderr=subprocess.PIPE, env=env_vars,
                           encoding=sys.getfilesystemencoding())
        else:
            subprocess.run(command, shell=True, check=True, stderr=subprocess.PIPE,
                           encoding=sys.getfilesystemencoding())
    else:
        if env_vars:
            subprocess.run(command, shell=True, check=True, stdout=subprocess.DEVNULL,
                           stderr=subprocess.PIPE, env=env_vars, encoding=sys.getfilesystemencoding())
        else:
            subprocess.run(command, shell=True, check=True, stdout=subprocess.DEVNULL,
                           stderr=subprocess.PIPE, encoding=sys.getfilesystemencoding())

def start_command(command, verbose):
    if os.name == 'nt':
        exec_command = command
    else:
        exec_command = shlex.split(command)
    if verbose:

        proc = subprocess.Popen(exec_command, shell=False, stderr=subprocess.PIPE,
                      encoding=sys.getfilesystemencoding())
    else:
        proc = subprocess.Popen(exec_command, shell=False, stdout=subprocess.DEVNULL,
                           stderr=subprocess.PIPE, encoding=sys.getfilesystemencoding())
    return proc

