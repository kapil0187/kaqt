#!/usr/bin/env python3

from optparse import OptionParser
import logging
import os
import pandas as pd
import numpy as np
import tables
import datetime
import re

from kaqt.foundation.io import kaqt_path
from kaqt.foundation.constants import clean_data_path
from kaqt.foundation.constants import raw_data_path
from kaqt.foundation.constants import quotes_hdf_filename
from kaqt.foundation.constants import trades_hdf_filename
from kaqt.foundation.constants import hdf_repos_filters
from kaqt.foundation.utils import ptd
from kaqt.backtest.data import quotes_data, trades_data
from kaqt.backtest.data import clean_quotes, clean_trades
from kaqt.backtest.data import ReutersQuote, ReutersTrade

def main():
    logging.basicConfig(level=logging.INFO)
    logger = logging.getLogger(__name__)

    print("*************** " + str(datetime.datetime.now()) + " ***************")

    usage = "usage: %prog [options] arg1 arg2"
    parser = OptionParser(usage=usage)
    parser.add_option("-q", "--quiet", help="don't print log messages to stdout",
                      action='store_false', default=True, dest='verbose')
    parser.add_option("-p", "--path", help="Path with dated folders for tick data",
                      action="store", type="string", dest="data_path")
    parser.add_option("-d", "--destination", help="Destination directory",
                      action="store", type="string", dest="dest")
    parser.add_option("-c", "--clean", help="Clean data input before saving",
                      action='store_true', default=True, dest='clean')
    parser.add_option("-a", "--aggregate", help="Aggregate data with same time stamps",
                      action="store_true", default=False, dest="aggr")
    parser.add_option('-s', '--start', help='Start date for data in format YYYYMMDD',
                      action='store', type='string', dest='start_date')
    parser.add_option('-e', '--end', help='End date for data in format YYYYMMDD',
                      action='store', type='string', dest='end_date')

    (options, args) = parser.parse_args()

    if kaqt_path() == '':
        logger.error("KAQT_PATH environment variable is not set")
        return

    data_path = options.data_path if options.data_path else os.path.join(kaqt_path(), raw_data_path)
    if not os.path.exists(data_path):
        os.makedirs(data_path)

    dest_path = options.dest if options.dest else os.path.join(kaqt_path(), clean_data_path)
    if not os.path.exists(dest_path):
        os.makedirs(dest_path)

    start_date = options.start_date if options.start_date else ptd()
    if isinstance(start_date, str):
        try:
            start_date = datetime.datetime.strptime(start_date, '%Y%m%d')
        except ValueError as err:
            logger.error("Failed to parser start date. Error message - ", err)

    end_date = options.end_date if options.end_date else ptd()
    if isinstance(end_date, str):
        try:
            end_date = datetime.datetime.strptime(end_date, '%Y%m%d')
        except ValueError as err:
            logger.error("Failed to parser end date. Error message - ", err)

    if options.verbose:
        logger.info('Raw data directory: ' + data_path)
        logger.info('Destination data directory: ' + dest_path)
        logger.info('Start date: ' + start_date.strftime('%Y-%m-%d'))
        logger.info('End date: ' + end_date.strftime('%Y-%m-%d'))
        logger.info('Cleaning: ' + 'True' if options.clean else 'False')
        logger.info('Aggregating trades with same timestamp: ' + 'True' if options.aggr else 'False')

    quotes_store = tables.open_file(os.path.join(dest_path, quotes_hdf_filename),
                                    'r+', filters=hdf_repos_filters)
    trades_store = tables.open_file(os.path.join(dest_path, trades_hdf_filename),
                                    'r+', filters=hdf_repos_filters)

    for dt in pd.date_range(start=start_date, end=end_date, freq='D'):
        logger.info("Processing data for date " + dt.strftime('%Y-%m-%d'))
        date_dir = os.path.join(data_path, dt.strftime('%Y%m%d'))
        date_files = np.array(os.listdir(date_dir))
        reg_pattern = re.compile('(\\d{4}[.]\\d{2}[.]\\d{2}[.]((\\S*)[F|G|H|J|K|M|N|Q|U|V|X|Z][0-9])[.]csv[.]gz)')
        __vget_instr__ = np.vectorize(lambda x: reg_pattern.match(x).group(3))
        __get_contract__ = lambda x : reg_pattern.match(x).group(2)

        instruments = np.unique(__vget_instr__(date_files))

        for inst in instruments:
            h5_filename = os.path.join(dest_path, inst + '.h5')
            logger.info("Loading HDF file " + h5_filename)
            ir = re.compile("\\d{4}[.]\\d{2}[.]\\d{2}[.]" + inst +
                            "[F|G|H|J|K|M|N|Q|U|V|X|Z][0-9][.]csv[.]gz")
            inst_files = [f for f in date_files if ir.match(f)]
            if options.verbose:
                logger.info("Found " + str(len(inst_files)) + " files on drive for " + inst)

            for filename in inst_files:
                contract = __get_contract__(filename)

                inst_group = quotes_store.get_node('/', inst)
                table_name = contract + "_" + dt.strftime('%Y%m%d')
                contract_table = quotes_store.create_table(inst_group, table_name, ReutersQuote)
                row = contract_table.row

                if options.verbose:
                    logger.info("Processing quotes for file: " + filename)
                    logger.info('Loading table: ' + table_name)

                quotes = quotes_data(symbol=contract, date=dt, path=data_path)
                quotes = clean_quotes(quotes, verbose=True)
                quotes = quotes.ffill()
                num_rows = len(quotes.index)
                logger.info("Total rows with quotes: " + str(num_rows))
                if num_rows > 0:
                    quotes['DateTime'] = quotes.index.astype(np.int64)

                    for index, q in quotes.iterrows():
                        row['date_time'] = q['DateTime']
                        row['bid'] = q['Bid']
                        row['ask'] = q['Ask']
                        row['bid_size'] = -1 if np.isnan(q['BidSize']) else int(q['BidSize'])
                        row['ask_size'] = -1 if np.isnan(q['AskSize']) else int(q['AskSize'])
                        row.append()

                    contract_table.flush()

                quotes_store.flush()

                trades = trades_data(symbol=contract, date=dt, path=data_path)
                trades = clean_trades(trades, verbose=True)

                num_rows = len(trades.index)
                logger.info("Total rows with trades: " + str(num_rows))

                inst_group = trades_store.get_node('/', inst)
                table_name = contract + "_" + dt.strftime('%Y%m%d')
                contract_table = trades_store.create_table(inst_group, table_name, ReutersTrade)
                row = contract_table.row

                if num_rows > 0:
                    trades['DateTime'] = trades.index.astype(np.int64)

                    for index, trd in trades.iterrows():
                        row['date_time'] = trd['DateTime']
                        row['price'] = trd.Price
                        row['volume'] = int(trd.Volume)
                        row.append()
                    contract_table.flush()

                trades_store.flush()

    quotes_store.close()
    trades_store.close()

if __name__ == '__main__':
    main()


