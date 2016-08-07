#!/usr/bin/env python

from optparse import OptionParser
import logging
import os
import pandas as pd
import numpy as np
import datetime
import pysftp
import re
import pymongo as pm

def main():
    logging.basicConfig(level=logging.INFO)
    logger = logging.getLogger(__name__)

    print("*************** " + str(datetime.datetime.now()) + " ***************")

    usage = "usage: %prog [options] arg1 arg2"
    parser = OptionParser(usage=usage)
    parser.add_option("-q", "--quiet", help="Don't show Verbose output",
                      action='store_false', default=True, dest='verbose')
    parser.add_option("-f", "--filename", help="CSV file with symbols",
                      action="store", type="string", dest="filename")

    (options, args) = parser.parse_args()
    if options.filename is None:
        parser.print_help()
    else:
        client = pm.MongoClient('localhost', 27017)
        instruments_coll = client.kaqt.instruments
        symbols = pd.read_csv(options.filename)
        for index, row in symbols.iterrows():
            to_upload = {
                'key': row['id'],
                'ticker': row['ticker'],
                'underlying': row['underlying'],
                'description': row['description'],
                'exchange_group': row['exchange_group'],
                'exchange': row['exchange'],
                'expiry_posix_datetime': 0,
                'min_order_size': row['min_order_size'],
                'tick_size': row['tick_size'],
                'tradeable_tick_size': row['tradeable_tick_size'],
                'currency': row['currency'],
                'alternate_ids': [{'type': 'TT', 'id': row['tt_symbol_id']}]
            }
            instruments_coll.insert(to_upload)

if __name__  == '__main__':
    main()


