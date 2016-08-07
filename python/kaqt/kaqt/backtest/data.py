__author__ = 'kapilsharma'

import pandas as pd
from pandas.tseries.offsets import BDay
import numpy as np
import os
import datetime
from statsmodels.robust.scale import mad
from tables import IsDescription, Int32Col, Int64Col, Float64Col, StringCol
import re

from kaqt.foundation.io import kaqt_path
from kaqt.foundation.constants import raw_data_path
from kaqt.foundation.constants import settles_data_path


def __read_reuters_ticks_(symbol, date=pd.datetime.today() - BDay(1),
                   path=os.path.join(kaqt_path(), raw_data_path)):
    if isinstance(date, str):
        date = datetime.datetime.strptime(date, '%Y-%m-%d')
    if isinstance(path, str):
        path = os.path.abspath(path)
    date_dir = os.path.join(path, date.strftime('%Y%m%d'))
    to_read = os.path.join(date_dir, date.strftime("%Y.%m.%d") + '.' + symbol + ".csv.gz")
    if os.path.exists(to_read):
        rd = pd.read_csv(to_read, compression='gzip')
        rd['DateTime'] = pd.to_datetime(rd['Date[G]'] + " " + rd['Time[G]'],
                                        format='%d-%b-%Y %H:%M:%S.%f')
        rd.set_index('DateTime', inplace=True)
        return rd
    else:
        return None


def __read_reuters_settles_(filename, path=os.path.join(kaqt_path(), settles_data_path)):
    if isinstance(path, str):
        path = os.path.abspath(path)
    file_path = os.path.join(path, filename)
    if os.path.exists(file_path):
        sd = pd.read_csv(file_path, compression='gzip')
        sd['Date[L]'] = pd.to_datetime(sd['Date[L]'], format='%d-%b-%Y')
        return sd
    else:
        return None


def __read_historical_settles_(filename, path=os.path.join(kaqt_path(), settles_data_path)):
    if isinstance(path, str):
        path = os.path.abspath(path)
    file_path = os.path.join(path, filename)
    if os.path.exists(file_path):
        osd = pd.read_csv(file_path)
        return osd
    else:
        return None


def __print_cleanup_info__(original_count, final_count):
    print("====================================")
    print("Original number of rows: " + str(original_count))
    print("Removed " + str(original_count - final_count) + " rows")
    print("====================================")


def __check_quotes__(qdata):
    col_names = qdata.columns
    if not np.any(col_names == 'Bid'):
        raise ValueError('Could not find Bid in column names')
    if not np.any(col_names == 'Ask'):
        raise ValueError('Could not find Ask in column names')
    if not np.any(col_names == 'BidSize'):
        raise ValueError('Could not find BidSize in column names')
    if not np.any(col_names == 'AskSize'):
        raise ValueError('Could not find AskSize in column names')


def __check_trades__(tdata):
    col_names = tdata.columns
    if not np.any(col_names == 'Price'):
        raise ValueError('Could not find Price in column names')
    if not np.any(col_names == 'Volume'):
        raise ValueError('Could not find Volume in column names')


def quotes_data(symbol, **kargs):
    date = None
    path = None

    if 'date' in kargs.keys():
        date = kargs['date']

    if 'path' in kargs.keys():
        path = kargs['path']

    tick_data = None

    if date is None and path is None:
        tick_data = __read_reuters_ticks_(symbol=symbol)
    else:
        if date is None:
            tick_data = __read_reuters_ticks_(symbol=symbol, path=path)
        else:
            if path is None:
                tick_data = __read_reuters_ticks_(symbol=symbol, date=date)
            else:
                tick_data = __read_reuters_ticks_(symbol=symbol, date=date, path=path)

    if tick_data is not None:
        qd = tick_data[tick_data.Type == 'Quote']
        qd = qd[['Bid Price', 'Bid Size', 'Ask Price', 'Ask Size']]
        qd.columns = ['Bid', 'BidSize', 'Ask', 'AskSize']
        return qd
    else:
        return None


def trades_data(symbol, **kargs):
    date = None
    path = None

    if 'date' in kargs.keys():
        date = kargs['date']

    if 'path' in kargs.keys():
        path = kargs['path']

    tick_data = None

    if date is None and path is None:
        tick_data = __read_reuters_ticks_(symbol=symbol)
    else:
        if date is None:
            tick_data = __read_reuters_ticks_(symbol=symbol, path=path)
        else:
            if path is None:
                tick_data = __read_reuters_ticks_(symbol=symbol, date=date)
            else:
                tick_data = __read_reuters_ticks_(symbol=symbol, date=date, path=path)

    if tick_data is not None:
        td = tick_data[tick_data.Type == 'Trade']
        td = td[['Price', 'Volume']]
        return td
    else:
        return None


def settles_data(filename, historical=False, **kargs):
    settles = None
    if historical:
        settles = __read_historical_settles_(filename=filename, **kargs)
    else:
        settles = __read_reuters_settles_(filename=filename, **kargs)
        if settles is not None:
            settles = settles[['#RIC', 'Date[L]','Settle']]
            settles.columns = ['RIC', 'Date', 'SettlePrice']

    if settles is not None:
        return settles
    else:
        return None


def settles_history(**kargs):
    settle_files = None
    if 'path' in kargs.keys():
        settles_files_path = kargs['path']
        settle_files = os.listdir(settles_files_path)
    else:
        settle_files = os.listdir(os.path.join(kaqt_path(), settles_data_path))

    if len(settle_files) > 0:
        r = re.compile("\\S*-Futures_Settles\\S*[0-9][.]csv[.]gz")
        settle_files = np.array([f for f in settle_files if r.match(f)])
        all_data = []
        for f in settle_files:
            data = settles_data(f)
            all_data.append(data)
        rv = pd.concat(all_data)
        return rv
    else:
        return None


def merge_qt(quotes, trades):
    __check_quotes__(quotes)
    __check_trades__(trades)
    quotes['Type'] = 'Quote'
    trades['Type'] = 'Trade'
    qt_data = quotes.combine_first(trades)
    qt_data = qt_data[['Type', 'Ask', 'AskSize', 'Bid', 'BidSize', 'Price', 'Volume']]
    return qt_data


def check_trades(trades):
    __check_trades__(trades)
    return trades


def check_quotes(quotes):
    __check_quotes__(quotes)
    return quotes


def clean_quotes(quotes, verbose=False):
    if verbose:
        print("Removing zero quotes")
    quotes = no_zero_quotes(quotes,  verbose=verbose)

    if verbose:
        print("Removing erroneous quotes")
    quotes = rm_erroneous_quotes(quotes, verbose=verbose)

    if verbose:
        print("Removing quotes with abnormally high spreads")
    quotes = rm_large_spreads(quotes, verbose=verbose)

    if verbose:
        print("Removing quote outliers")
    quotes = rm_quote_outliers(quotes, verbose=verbose)
    return quotes


def clean_trades(trades, verbose=False):
    if verbose:
        print('Removing zero prices')
    trades = no_zero_prices(trades)
    return trades


def no_zero_quotes(quotes, verbose=False):
    __check_quotes__(quotes)
    original_count = len(quotes.index)
    quotes = quotes.ix[quotes.apply(__non_zero_quote, axis=1)]
    final_count = len(quotes.index)
    if verbose:
        __print_cleanup_info__(original_count, final_count)
    return quotes


def __non_zero_quote(row):
    return (row.Bid != 0 and row.Ask != 0 and row.BidSize != 0 and row.AskSize != 0) | \
           (row.Bid != 0 and row.BidSize != 0) | \
           (row.Ask != 0 and row.AskSize != 0)


def no_zero_prices(trades, verbose=False):
    original_count = len(trades.index)
    trades = trades.ix[trades.apply(__non_zero_price, axis=1)]
    final_count = len(trades.index)
    if verbose:
        __print_cleanup_info__(original_count, final_count)
    return trades


def __non_zero_price(row):
    return not(row.Price == 0 or row.Volume == 0 or np.isnan(row.Price) or np.isnan(row.Volume))


def rm_large_spreads(quotes, func=np.median, mult=50, verbose=False):
    __check_quotes__(quotes)
    temp = quotes.ffill()
    original_count = len(quotes.index)
    spreads = temp.Ask - temp.Bid
    indicator = func(spreads)
    to_keep = spreads <= mult*indicator
    quotes = quotes.ix[to_keep]
    final_count = len(quotes.index)
    if verbose:
        __print_cleanup_info__(original_count, final_count)
    return quotes


def rm_quote_outliers(quotes, mult=10, window=50, center=np.median, type='advanced', verbose=False):
    __check_quotes__(quotes)
    original_count = len(quotes.index)
    cleaned_qd = quotes
    if original_count > window:
        window = np.floor(window/2) * 2
        half_window = window / 2
        temp = quotes.ffill().bfill()
        mid_quotes = (temp.Bid + temp.Ask)/2
        mq_mad = mad(mid_quotes, center=center)
        if mq_mad == 0:
            m = mid_quotes
            s = np.append([True], (m[1:len(m)].values - m[0:(len(m)-1)].values) != 0)
            mq_mad = mad(mid_quotes[s])

        def __modified_median__(arr):
            w = len(arr)
            return np.median(np.append(arr[0:(w-1)/2], arr[(w/2 + 1):w]))

        roll_meds = None
        meds = pd.rolling_apply(mid_quotes, window=window + 1, func=__modified_median__, center=True)

        if type == 'standard':
            roll_meds = meds.ffill().bfill()

        if type == 'advanced':
            def __forward_median__(arr):
                w = (len(arr) - 1)/2
                return np.median(arr[w+1:])

            def __backward_median__(arr):
                w = (len(arr) - 1)/2
                return np.median(arr[:w])

            def __closest_to_mid_quote__(qq):
                qq[np.isnan(qq)] = qq[3]
                diff = np.abs(qq[0:2] - qq[3])
                select = np.min(diff) == diff
                value = qq[select]
                if len(value) > 1:
                    value = np.median(value)
                return value

            all_matrix = np.zeros((len(mid_quotes), 4))
            all_matrix[:, 0] = meds
            all_matrix[:, 1] = pd.rolling_apply(mid_quotes, window=2*window + 1, func=__forward_median__,
                                                center=True)
            all_matrix[:, 2] = pd.rolling_apply(mid_quotes, window=2*window + 1, func=__backward_median__,
                                                center=True)
            all_matrix[:, 3] = mid_quotes
            roll_meds = np.apply_along_axis(__closest_to_mid_quote__, axis=1, arr=all_matrix)

        max_criterion = roll_meds + mult * mq_mad
        min_criterion = roll_meds - mult * mq_mad
        min_condition = np.less(min_criterion, mid_quotes.values)
        max_condition = np.less(mid_quotes.values, max_criterion)
        condition = np.logical_and(min_condition, max_condition)
        cleaned_qd = quotes.ix[condition]
        final_count = len(cleaned_qd.index)
        if verbose:
            __print_cleanup_info__(original_count, final_count)
    return cleaned_qd


def combine_trades(trades, price_func=np.median, vol_func=np.sum, verbose=False):
    __check_trades__(trades)
    original_count = len(trades)
    trades.Price.fillna('ffill')
    trades = trades.groupby(trades.index).agg({'Price': price_func, 'Volume': vol_func})
    final_count = len(trades)
    if verbose:
        __print_cleanup_info__(original_count, final_count)
    return trades[['Price', 'Volume']]


def combine_quotes(quotes, price_func=np.median, size_func=np.max, verbose=False):
    __check_quotes__(quotes)
    original_count = len(quotes.index)
    quotes = quotes.ffill()
    quotes = quotes.groupby(quotes.index).agg({'Bid': price_func, 'Ask': price_func,
                                               'BidSize': size_func, 'AskSize': size_func})
    final_count = len(quotes.index)
    if verbose:
        __print_cleanup_info__(original_count, final_count)
    return quotes[['Bid', 'Ask', 'BidSize', 'AskSize']]


def rm_erroneous_quotes(quotes, verbose=False):
    __check_quotes__(quotes)
    original_count = len(quotes.index)
    temp = quotes.ffill()
    quotes = quotes.ix[temp.Bid < temp.Ask]
    final_count = len(quotes.index)
    if verbose:
        __print_cleanup_info__(original_count, final_count)
    return quotes


def create_mid_quotes(quotes, mid_type='simple'):
    type_options = np.array(['simple', 'weighted'])
    rv = pd.DataFrame()
    if np.any(type_options == mid_type):
        __check_quotes__(quotes)
        if mid_type == 'simple':
            rv['Mid'] = (quotes['Bid'] + quotes['Ask'])/2
        if mid_type == 'weighted':
            rv['Mid'] = (quotes['Bid']*quotes['BidSize'] + quotes['Ask']*quotes['AskSize'])
        return rv
    else:
        raise ValueError("type has incorrect value")


def create_spreads(quotes1, quotes2):
    __check_quotes__(quotes1)
    __check_quotes__(quotes2)
    spread_series = quotes1.join(quotes2, lsuffix='_1', rsuffix='_2').ffill()
    spread_series['Bid'] = spread_series['Bid_1'] - spread_series['Ask_2']
    spread_series['BidSize'] = spread_series[['BidSize_1', 'AskSize_2']].min(axis=1)
    spread_series['Ask'] = spread_series['Ask_1'] - spread_series['Bid_2']
    spread_series['AskSize'] = spread_series[['BidSize_2', 'AskSize_1']].min(axis=1)
    return spread_series[['Bid', 'BidSize', 'Ask', 'AskSize']]


class ReutersQuote(IsDescription):
    date_time = Int64Col()
    bid = Float64Col()
    ask = Float64Col()
    bid_size = Int32Col()
    ask_size = Int32Col()


class ReutersSettlePrice(IsDescription):
    ric = StringCol(itemsize=10)
    date = Int32Col()
    price = Float64Col()


class ReutersTrade(IsDescription):
    date_time = Int32Col()
    price = Float64Col()
    volume = Int32Col()