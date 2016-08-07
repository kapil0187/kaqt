__author__ = 'kapilsharma'

import pandas as pd
from pandas.tseries.offsets import BDay
import pysftp
import re
import os


def download_sftpfile(sftp_connection, filename, to_dir="/Users/kapilsharma/GitHub/kaqt/data/csv/"):
    print("Downloading..." + filename)
    to_move = os.path.abspath(filename)
    dt_dir = to_dir + filename[0:10]
    dest_file = dt_dir+"/"+filename
    if os.path.exists(dest_file):
        print(filename + " already exists")
    else:
        if os.path.exists(to_dir):
            if not os.path.exists(dt_dir):
                os.makedirs(dt_dir)
            sftp_connection.get(filename)
            if os.path.isfile(to_move):
                print("Moving... " + to_move + " to " + dest_file)
                os.rename(to_move, dest_file)


def download_futures(host, username, password, symbol, start_date=pd.datetime.today() - BDay(1),
                     end_date=pd.datetime.today() - BDay(1),
                     to_dir="/Users/kapilsharma/GitHub/kaqt/data/csv/"):
    try:
        sftp_connection = pysftp.Connection(host, username=username, password=password)
    except:
        print("Could not connect to sftp")
        sftp_connection = None
    if sftp_connection is not None:
        for date in pd.date_range(start_date, end_date, freq='D'):
            date_dir = date.strftime('%Y.%m.%d')
            print("Getting data for " + date_dir)
            r = re.compile("(\\S)+" + symbol + "..[.]csv")

            with sftp_connection.cd():
                sftp_connection.chdir("/home/storage/csv/" + date_dir)
                files = sftp_connection.listdir()
                files = [f.encode('ascii', 'ignore') for f in files]
                files = filter(r.match, files)
                print("Found " + len(files).__str__() + " files")
                [download_sftpfile(sftp_connection, x, to_dir=to_dir) for x in files]
    else:
        print("SFTP Connection is None")

    if sftp_connection is not None:
        sftp_connection.close()

