__author__ = 'kapilsharma'

from kaqt.rserver import download
from optparse import OptionParser
import pandas as pd
from pandas.tseries.offsets import BDay
import sys


if __name__ == "__main__":
    usage = "usage: %prog [options] arg1 arg2"
    parser = OptionParser(usage=usage)
    parser.add_option("-i", "--hostip", help="sftp host machine ip address",
                      action="store", type="string", dest="host")
    parser.add_option("-u", "--username", help="Username to connect to sftp",
                      action="store", type="string", dest="username")
    parser.add_option("-p", "--password", help="Password to connect to sftp",
                      action="store", type="string", dest="password")
    parser.add_option("-s", "--symbol", help="Symbols for which data is needed",
                      action="store", type="string", dest="symbol")
    parser.add_option("-d", "--dir", help="Directory to save data",
                      action="store", type="string", dest="dir")

    (options, args) = parser.parse_args()
    missing_args = not all((options.host, options.username, options.password, options.symbol))
    if missing_args:
        parser.print_help()
    else:
        if options.dir is None:
            download.download_futures(options.host,
                                      username=options.username,
                                      password=options.password,
                                      symbol=options.symbol,
                                      start_date=pd.datetime.today() - BDay(3),
                                      end_date=pd.datetime.today() - BDay(1))
        else:
            download.download_futures(options.host,
                                      username=options.username,
                                      password=options.password,
                                      symbol=options.symbol,
                                      start_date=pd.datetime.today() - BDay(3),
                                      end_date=pd.datetime.today() - BDay(1),
                                      to_dir=options.dir)
