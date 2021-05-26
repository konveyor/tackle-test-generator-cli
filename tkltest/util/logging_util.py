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
