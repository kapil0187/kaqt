__author__ = 'kapilsharma'

import pandas as pd
import tables

na_stocks_ref_google = \
    {
        'NA Stocks':
            'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Stock+Exchanges/Stocks-GOOG.csv',
        'NA Stocks Info':
            'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Stock+Exchanges/stockinfo-GOOG.csv',
        'NA ETFs':
            'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Google/ETFs-GOOG.csv',
        'NASDAQ':
            'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Stock+Exchanges/NASDAQ.csv',
        'NYSE':
            'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Stock+Exchanges/NYSE.csv',
        'NYSE AMEX':
            'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Stock+Exchanges/AMEX.csv',
        'NYSE ARCA':
            'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Google/NYSEARCA.csv',
        'NYSE MKT':
            'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Google/NYSEMKT.csv',
        'TSX Ventures':
            'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Google/CVE.csv',
        'CNSX':
            'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Google/CNSX.csv',
        'Pink Sheets':
            'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Stock+Exchanges/PINK.csv',
        'OTC':
            'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Stock+Exchanges/OTC.csv',
        'FINRA OTC BB':
            'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Google/OTCBB.csv',
        'FINRA Other OTC':
            'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Google/OTCMKTS.csv'
    }

na_stocks_ref_yahoo = \
    {
        'NA Stocks':
            'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Stock+Exchanges/Stocks-YAHOO.csv',
        'NA Stocks Info':
            'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Stock+Exchanges/stockinfo-YAHOO.csv',
        'NA ETFS':
            'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Yahoo/ETFs-YAHOO.csv',
        'Indices':
            'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Stock+Exchanges/Indicies.csv',
        'Mutual Funds':
            'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Stock+Exchanges/funds.csv',
        'TSX':
            'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Yahoo/TSX.csv',
        'TSX Ventures':
            'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Yahoo/TSXV.csv',
        'MX Stocks':
            'https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Yahoo/MX.csv'
    }


def read_stocks_reference(key, source, malformed=False):
    if malformed:
        if source == "Google":
            return pd.read_excel(na_stocks_ref_google[key])
        else:
            if source == 'Yahoo':
                return pd.read_excel(na_stocks_ref_yahoo[key])
            else:
                return None
    else:
        if source == "Google":
            return pd.read_csv(na_stocks_ref_google[key])
        else:
            if source == 'Yahoo':
                return pd.read_csv(na_stocks_ref_yahoo[key])
            else:
                return None


def north_america_stocks(source='Google', detail=False):
    if source == 'Yahoo':
        if detail:
            return read_stocks_reference('NA Stocks Info', source, malformed=True)
        else:
            return read_stocks_reference('NA Stocks', source)
    else:
        if source == 'Google':
            if detail:
                return read_stocks_reference('NA Stocks Info', source, malformed=True)
            else:
                return read_stocks_reference('NA Stocks', source, malformed=True)


def futures_metadata():
    data = pd.read_csv("https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Futures/meta.csv")
    return data


def commodities():
    data = pd.read_csv("https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/commodities.csv")
    return data


def currency_codes():
    data = pd.read_csv("https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Currencies.csv")
    return data


def country_codes():
    data = pd.read_csv("https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/ISOCodes.csv")
    return data


def currency_cross_rates():
    data = pd.read_csv("https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/currencies.csv")
    return data


def main():
    df = currency_cross_rates()
    print(df)


if __name__ == "__main__":
    main()