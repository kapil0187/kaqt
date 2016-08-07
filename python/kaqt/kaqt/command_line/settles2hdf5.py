#!/usr/bin/env python3

from optparse import OptionParser
import logging
import os
import pandas as pd
import tables
import numpy as np
from pandas.tseries.offsets import BDay
import datetime
import re

from kaqt.foundation.io import kaqt_path
from kaqt.foundation.constants import clean_data_path
from kaqt.foundation.constants import settles_data_path
from kaqt.foundation.constants import historical_settles_filename
from kaqt.foundation.constants import settles_hdf_filename
from kaqt.foundation.constants import hdf_repos_filters
from kaqt.backtest.data import settles_history

def main():
    logging.basicConfig(level=logging.INFO)
    logger = logging.getLogger(__name__)

    print("*************** " + str(datetime.datetime.now()) + " ***************")

    usage = "usage: %prog [options] arg1 arg2"
    parser = OptionParser(usage=usage)
    parser.add_option("-q", "--quiet", help="don't print log messages to stdout",
                      action='store_false', default=True, dest='verbose')
    parser.add_option("-p", "--path", help="Path with setles data history",
                      action="store", type="string", dest="data_path")
    parser.add_option("-d", "--destination", help="Destination directory",
                      action="store", type="string", dest="dest")

    (options, args) = parser.parse_args()

    if kaqt_path() == '':
        logger.error("KAQT_PATH environment variable is not set")
        return

    data_path = options.data_path if options.data_path else os.path.join(kaqt_path(), settles_data_path)
    if not os.path.exists(data_path):
        os.makedirs(data_path)

    dest_path = options.dest if options.dest else os.path.join(kaqt_path(), clean_data_path)
    if not os.path.exists(dest_path):
        os.makedirs(dest_path)

    if options.verbose:
        logger.info('Raw data directory: ' + data_path)
        logger.info('Destination data directory: ' + dest_path)

    outfile_path = os.path.join(dest_path, settles_hdf_filename)

    settles = settles_history(path=data_path)
    settles.Date = settles.Date.apply(lambda x: int(x.strftime("%Y%m%d")))
    settles.RIC = settles.RIC.astype(str)

    file_dates = settles.Date.unique()

    h5_file = tables.open_file(outfile_path, mode='r+', filters=hdf_repos_filters)
    group = h5_file.get_node('/', "reuters")
    table = h5_file.get_node(group, "prices")
    row = table.row

    existing_dates = np.array(np.unique(table.cols.date[:]))
    missing_dates = np.setdiff1d(file_dates, existing_dates)

    settles = settles[settles.Date.isin(missing_dates.astype(list))]

    for index, sd in settles.iterrows():
        row['ric'] = sd['RIC']
        row['date'] = sd['Date']
        row['price'] = sd['SettlePrice']
        row.append()

    table.flush()

    h5_file.flush()
    h5_file.close()

if __name__ == '__main__':
    main()



