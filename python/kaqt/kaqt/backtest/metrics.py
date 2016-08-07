__author__ = 'kapilsharma'

import pandas as pd
import numpy as np


class PivotPoints:
    def __init__(self, ohlc):
        self.ohlc_ = ohlc

    def standard(self):
        output = pd.DataFrame()
        output['PP'] = (self.ohlc_['High'] + self.ohlc_['Low'] + self.ohlc_['Close'])/3
        output['R1'] = 2 * output['PP'] - self.ohlc_['Low']
        output['R2'] = output['PP'] + self.ohlc_['High'] - self.ohlc_['Low']
        output['R3'] = output['R2'] + self.ohlc_['High'] - self.ohlc_['Low']
        output['R4'] = output['R3'] + self.ohlc_['High'] - self.ohlc_['Low']
        output['S1'] = 2 * output['PP'] - self.ohlc_['High']
        output['S2'] = output['PP'] - self.ohlc_['High'] + self.ohlc_['Low']
        output['S3'] = output['S2'] - self.ohlc_['High'] + self.ohlc_['Low']
        output['S4'] = output['S3'] - self.ohlc_['High'] + self.ohlc_['Low']
        return output

    def camarilla(self):
        output = pd.DataFrame()
        output['R1'] = ((self.ohlc_['High'] - self.ohlc_['Low'])*1.1/12) + self.ohlc_['Close']
        output['R2'] = ((self.ohlc_['High'] - self.ohlc_['Low'])*1.1/6) + self.ohlc_['Close']
        output['R3'] = ((self.ohlc_['High'] - self.ohlc_['Low'])*1.1/4) + self.ohlc_['Close']
        output['R4'] = ((self.ohlc_['High'] - self.ohlc_['Low'])*1.1/2) + self.ohlc_['Close']
        output['S1'] = -((self.ohlc_['High'] - self.ohlc_['Low'])*1.1/12) + self.ohlc_['Close']
        output['S2'] = -((self.ohlc_['High'] - self.ohlc_['Low'])*1.1/6) + self.ohlc_['Close']
        output['S3'] = -((self.ohlc_['High'] - self.ohlc_['Low'])*1.1/4) + self.ohlc_['Close']
        output['S4'] = -((self.ohlc_['High'] - self.ohlc_['Low'])*1.1/2) + self.ohlc_['Close']
        return output

    def woodie(self):
        output = pd.DataFrame()
        output['PP'] = (self.ohlc_['High'] + self.ohlc_['Low'] + self.ohlc_['Open'] * 2)/4
        output['R1'] = 2 * output['PP'] - self.ohlc_['Low']
        output['R2'] = output['PP'] + self.ohlc_['High'] - self.ohlc_['Low']
        output['R3'] = self.ohlc_['High'] + 2 * (output['PP'] - self.ohlc_['Low'])
        output['R4'] = output['R3'] + self.ohlc_['High'] - self.ohlc_['Low']
        output['S1'] = 2 * output['PP'] - self.ohlc_['High']
        output['S2'] = output['PP'] - self.ohlc_['High'] + self.ohlc_['Low']
        output['S3'] = self.ohlc_['Low'] - 2 * (self.ohlc_['High'] - output['PP'])
        output['S4'] = output['S3'] - self.ohlc_['High'] + self.ohlc_['Low']
        return output

    def floor(self):
        output = pd.DataFrame()
        output['PP'] = (self.ohlc_['High'] + self.ohlc_['Low'] + self.ohlc_['Close'])/3
        output['R1'] = 2 * output['PP'] - self.ohlc_['Low']
        output['S1'] = 2 * output['PP'] - self.ohlc_['High']
        output['R2'] = output['PP'] - output['S1'] + output['R1']
        output['S2'] = output['PP'] - output['R1'] + output['S1']
        output['R3'] = output['PP'] - output['S1'] + output['R2']
        output['S3'] = output['PP'] - output['R2'] + output['S1']
        return output

    def demark(self):
        output = pd.DataFrame()
        output['X'] = self.ohlc_.apply(self.__calc_demark_x__, axis=1)
        output['PP'] = output['X']/4
        output['R1'] = output['X']/2 - self.ohlc_['Low']
        output['S1'] = output['X']/2 - self.ohlc_['High']
        output = output[['R1', 'PP', 'S1']]
        return output

    @staticmethod
    def __calc_demark_x__(row):
        if row['Close'] < row['Open']:
            return row['High'] + row['Low'] * 2 + row['Close']
        if row['Close'] > row['Open']:
            return row['High'] * 2 + row['Low'] + row['Close']
        if row['Close'] == row['Open']:
            return row['High'] + row['Low'] + 2 * row['Close']


def hurst_coeff(p, log_transform=False):
    if log_transform:
        p = np.log(p)
    tau = []; lagvec = []
    for lag in range(2,100):
        pp = np.subtract(p[lag:],p[:-lag])
        lagvec.append(lag)
        tau.append(np.sqrt(np.std(pp)))
    m = np.polyfit(np.log10(lagvec),np.log10(tau),1)
    hurst = m[0]*2
    return hurst

def rolling_zscore(series, window=50):
    pd.rolling_apply(series, window=window,
                     func=lambda x: (x - pd.rolling_mean(x, window=window))/pd.rolling_std(x, window=window))


