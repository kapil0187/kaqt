__author__ = 'kapilsharma'

import pandas as pd


index_constituents_ref = \
    {
    'S&P 500 Index':
        'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Indicies/SP500.csv',
    'Dow Jones Industrial Average':
        'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Indicies/dowjonesIA.csv',
    'NASDAQ Composite Index':
        'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Indicies/NASDAQComposite.csv',
    'NASDAQ 100 Index':
        'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Indicies/nasdaq100.csv',
    'NYSE Composite Index':
        'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Indicies/NYSEComposite.csv',
    'NYSE 100 Index':
        'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Indicies/nyse100.csv',
    'FTSE 100 Index':
        'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Indicies/FTSE100.csv'
    }


def read_index_constituent(key):
    data = pd.read_csv(index_constituents_ref[key])
    return data


def ic_sp_500_index():
    return read_index_constituent('S&P 500 Index')


def ic_dow_jones_indus_avg():
    return read_index_constituent('Dow Jones Industrial Average')


def ic_nasdaq_composite():
    return read_index_constituent('NASDAQ Composite Index')


def ic_nasdaq_100():
    return read_index_constituent('NASDAQ 100 Index')


def ic_nyse_composite():
    return read_index_constituent('NYSE Composite Index')


def ic_nyse_100():
    return read_index_constituent('NYSE 100 Index')


def ic_ftse_100():
    return read_index_constituent('FTSE 100 Index')


def main():
    data = ic_dow_jones_indus_avg()
    print(data)

if __name__ == '__main__':
    main()