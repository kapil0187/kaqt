import pandas as pd
import tables
import datetime
from optparse import OptionParser
from kaqt.backtest.data import ReutersSettlePrice

def main():

    print("*************** " + str(datetime.datetime.now()) + " ***************")

    usage = "usage: %prog [options] arg1 arg2"
    parser = OptionParser(usage=usage)
    parser.add_option("-f", "--file_name", help="File with the settles data",
                      action="store", dest="file_name")
    (options, args) = parser.parse_args()

    if options.file_name is None:
        print("No filename provided")
    else:
        settles_data = pd.read_csv(options.file_name)

        filter = tables.Filters(complevel=1, complib='zlib')
        h5file = tables.open_file('settles.h5', mode='w', filters=filter)
        group = h5file.create_group('/', "reuters", "Futures settles prices")
        table = h5file.create_table(group, "prices", ReutersSettlePrice, "Reuters Settles Data")
        row = table.row

        settles_data.Date = pd.to_datetime(settles_data.Date.astype(str), format='%Y-%m-%d')
        settles_data.Date = settles_data.Date.apply(lambda x: int(x.strftime("%Y%m%d")))
        settles_data.RIC = settles_data.RIC.astype(str)

        for index, sd in settles_data.iterrows():
            row['ric'] = sd['RIC']
            row['date'] = sd['Date']
            row['price'] = sd['SettlePrice']
            row.append()

        table.flush()

        h5file.flush()
        h5file.close()


if __name__ == '__main__':
    main()

