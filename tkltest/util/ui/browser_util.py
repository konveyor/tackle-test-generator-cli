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
import psutil
import sys

def cleanup_browser_instances(browser):
    """Performs process cleanup based on platform and browser"""
    if browser in ['chrome', 'chrome_headless']:
        if sys.platform == 'darwin':
            __kill_processes(['chromedriver', 'chrome'])
        elif sys.platform in ['linux', 'linux2']:
             __kill_processes(['chromedriver'])
        elif sys.platform in ['win32', 'win64']:
            # TODO: check
            __kill_processes(['chromedriver.exe'])
    else:
        # TODO: firefox
        pass


def __kill_processes(proc_names):
    """Kills process with the given names"""
    for proc in psutil.process_iter():
        try:
            procname = proc.name()
        except (psutil.ZombieProcess, psutil.NoSuchProcess):
            pass
        else:
            if procname in proc_names:
                logging.info('Killing {} instance'.format(procname))
                try:
                    proc.kill()
                except Exception:
                    logging.info('Error terminating process "{}"'.format(procname))
                    pass
