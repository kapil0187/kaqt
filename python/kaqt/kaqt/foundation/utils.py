import pandas as pd
from pandas.tseries.offsets import BDay


def ptd(n=1):
    return pd.datetime.today() - BDay(n)