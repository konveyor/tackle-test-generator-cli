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

from datetime import datetime
import logging
import logging.handlers
import sys

def init_logging(logfile, loglevel):
    logFormatter = logging.Formatter("%(asctime)s [%(thread)d] [%(levelname)-4.4s] [%(funcName)-10.10s] %(message)s")
    rootLogger = logging.getLogger()
    rootLogger.setLevel(getattr(logging, loglevel))

    # configure logging to file
    fileHandler = logging.handlers.RotatingFileHandler(logfile)
    fileHandler.setFormatter(logFormatter)
    fileHandler.setLevel(getattr(logging, loglevel))
    rootLogger.addHandler(fileHandler)
    
    # configure logging to console
    consoleHandler = logging.StreamHandler()
    consoleHandler.setFormatter(logFormatter)
    consoleHandler.setLevel(getattr(logging, loglevel))
    rootLogger.addHandler(consoleHandler)

def tkltest_status(msg, error=False):
    sys.stdout.write('[tkltest|{}] '.format(datetime.now().strftime('%H:%M:%S.%f')[:-3]))
    if error:
        sys.stdout.write('ERROR: ')
    print('{}'.format(msg))
