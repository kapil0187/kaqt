__author__ = 'kapilsharma'

import pandas as pd

exchange_ref = \
    {
        'bxoptions':'ftp://ftp.nasdaqtrader.com/SymbolDirectory/bxoptions.txt',
        'bxo_lmm':'ftp://ftp.nasdaqtrader.com/SymbolDirectory/bxo_lmm.csv',
        'bxtraded':'ftp://ftp.nasdaqtrader.com/SymbolDirectory/bxtraded.txt',
        'mfundslist':'ftp://ftp.nasdaqtrader.com/SymbolDirectory/mfundslist.txt',
        'mpidlist':'ftp://ftp.nasdaqtrader.com/SymbolDirectory/mpidlist.txt',
        'options':'ftp://ftp.nasdaqtrader.com/SymbolDirectory/options.txt',
        'otclist':'ftp://ftp.nasdaqtrader.com/SymbolDirectory/otclist.txt',
        'pbot':'ftp://ftp.nasdaqtrader.com/SymbolDirectory/pbot.csv',
        'phlxoptions':'ftp://ftp.nasdaqtrader.com/SymbolDirectory/phlxoptions.csv',
        'psxtraded':'ftp://ftp.nasdaqtrader.com/SymbolDirectory/psxtraded.txt',
        'nasdaqlisted':'ftp://ftp.nasdaqtrader.com/SymbolDirectory/nasdaqlisted.txt',
        'nasdaqtraded':'ftp://ftp.nasdaqtrader.com/SymbolDirectory/nasdaqtraded.txt',
        'otherlisted':'ftp://ftp.nasdaqtrader.com/SymbolDirectory/otherlisted.txt',
        'phlxstrikes':'ftp://ftp.nasdaqtrader.com/SymbolDirectory/phlxstrikes.zip'
    }


def get_bx_options():
    data = pd.read_table(exchange_ref['bxoptions'], sep="|", skipinitialspace=True)
    return data.drop(data.index[-1])


def get_bxo_lmm():
    return pd.read_csv(exchange_ref['bxo_lmm'], skiprows=3, skipfooter=1, skipinitialspace=True)


def get_bx_traded():
    data = pd.read_table(exchange_ref['bxtraded'], sep="|", skipinitialspace=True)
    return data.drop(data.index[-1])


def get_mutual_funds():
    data = pd.read_table(exchange_ref['mfundslist'], sep="|", skipinitialspace=True)
    return data.drop(data.index[-1])


def get_mpid():
    data = pd.read_table(exchange_ref['mpidlist'], sep="|", skipinitialspace=True)
    return data.drop(data.index[-1])


def get_nasdaq_listed():
    data = pd.read_table(exchange_ref['nasdaqlisted'], sep="|", skipinitialspace=True)
    return data.drop(data.index[-1])


def get_nasdaq_traded():
    data = pd.read_table(exchange_ref['nasdaqtraded'], sep="|", skipinitialspace=True)
    return data.drop(data.index[-1])


def get_options():
    data = pd.read_table(exchange_ref['options'], sep="|", skipinitialspace=True)
    return data.drop(data.index[-1])


def get_otc():
    data = pd.read_table(exchange_ref['otclist'], sep="|", skipinitialspace=True)
    return data.drop(data.index[-1])


def get_pbot():
    return pd.read_csv(exchange_ref['pbot'],
                       names=['Commodity ID', 'NFX Product Symbol', 'NFX Product Description',
                               'Last Trade Date', 'Expiration Date'], skiprows=5, skipfooter=1)


def get_phlx_options():
    data = pd.read_csv(exchange_ref['phlxoptions'],
                       names=['Company', 'Cycle', 'Option Symbol', 'Stock Symbol', 'Specialist Unit',
                              'Primary Market', 'Single Listed', 'Primary Issue', 'Secondary Issue(s)'])
    return data.ix[5:].drop(data.index[-1])


def get_psx_traded():
    data = pd.read_table(exchange_ref['psxtraded'], sep="|", skipinitialspace=True)
    return data.drop(data.index[-1])


def main():
    data = get_nasdaq_traded()
    print(data)

if __name__ == '__main__':
    main()