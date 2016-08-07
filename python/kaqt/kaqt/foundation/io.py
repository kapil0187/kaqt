__author__ = 'kapilsharma'

import os
from kaqt.foundation.constants import kaqt_dir

def read_dated_folder():
    pass


def read_to_list(filename):
    file_data = open(filename)
    lines = file_data.readlines()
    file_data.close()
    return lines


def kaqt_path():
    kp = None
    try:
        kp = os.environ["KAQT_PATH"]
    except:
        kp = os.path.abspath(kaqt_dir)
    return kp