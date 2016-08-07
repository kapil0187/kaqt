import pandas as pd
import numpy as np
from optparse import OptionParser
import logging
import os
import re
import datetime

from kaqt.foundation.constants import raw_data_path
from kaqt.foundation.constants import clean_data_path
from kaqt.foundation.io import kaqt_path
from kaqt.backtest.data import quotes_data, clean_quotes
from kaqt.backtest.data import create_mid_quotes

if __name__ == "__main__":
    logging.basicConfig(level=logging.INFO)
    logger = logging.getLogger(__name__)
    usage = "usage: %prog [options] arg1 arg2"
    parser = OptionParser(usage=usage)
    parser.add_option('-d', '--start', help='Date for data in format YYYYMMDD',
                      action='store', type='string', dest='date')
    parser.add_option('-f', '--file_path', help='path with kaqt data',
                      action='store', type='string', dest='file_path')

    (options, args) = parser.parse_args()
    missing_args = not options.date

    if missing_args:
        parser.print_help()
    else:
        if kaqt_path() != '':
            files_dir = options.file_path if options.file_path else os.path.join(kaqt_path(), raw_data_path)
            files_dir = os.path.join(files_dir, options.date)
            data_files = np.array(os.listdir(files_dir))
            reg_pattern = re.compile('(\\d{4}[.]\\d{2}[.]\\d{2}[.]((\\S*)[F|G|H|J|K|M|N|Q|U|V|X|Z][0-9])[.]csv[.]gz)')
            __vget_instr__ = np.vectorize(lambda x: reg_pattern.match(x).group(3))
            __get_contract__ = lambda x : reg_pattern.match(x).group(2)

            h5_file_name = os.path.join(kaqt_path(), clean_data_path, "ohlc.h5")
            h5_store = pd.HDFStore(h5_file_name)

            all_data = {}

            for df in data_files:
                logger.info("Loading " + df)
                contract = __get_contract__(df)
                qd = quotes_data(contract, date=datetime.datetime.strptime(options.date, "%Y%m%d"))
                if len(qd.index) is not 0:
                    qd = clean_quotes(qd)
                    if len(qd.index) is not 0:
                        qd = create_mid_quotes(qd.ffill().dropna())
                        qd = qd.Mid.resample('min', how='ohlc').ffill()
                        all_data[contract] = qd

            data_panel = pd.Panel(all_data)
            h5_store[options.date] = data_panel
            h5_store.flush()
            h5_store.close()














