__author__ = 'kapilsharma'

import pandas as pd
from pandas.tseries.offsets import BDay
import datetime
import gzip


def read_gzip(f):
    csv_file = gzip.open(f, 'r')
    data = pd.read_csv(csv_file)
    csv_file.close()
    return data


def read_tick_data(symbol, date=pd.datetime.today() - BDay(1), base_dir="/Users/kapilsharma/GitHub/kaqt/data/csv"):
    date_dir = date.strftime('%Y.%m.%d')
    file_name = base_dir + "/" + date_dir + "/" + date_dir + "." + symbol + ".csv.gz"
    print("Reading " + file_name)
    print(read_gzip(file_name))


def main():
    dt = datetime.date(2015, 02, 19)
    read_tick_data("NGF6", date=dt)

if __name__ == '__main__':
    main()




