__author__ = 'kapilsharma'

import pandas as pd


def futures_metadata():
    data = pd.read_csv("https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Futures/meta.csv")
    return data