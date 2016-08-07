from enum import Enum
from abc import ABCMeta, abstractmethod
import os
from kaqt.foundation.constants import clean_data_path
from kaqt.foundation.io import kaqt_path
import pandas as pd


class MarketDataResolution(Enum):
    TICK = 1
    SECOND = 2
    MINUTE = 3
    HOUR = 4
    DAILY = 5


class MarketDataType(Enum):
    QUOTE = 1
    TRADE_BAR = 2


class AbstractMarketDataCache(object):
    __metaclass__ = ABCMeta

    @abstractmethod
    def get_bars(self, symbol, n=1):
        raise NotImplementedError('override get_bars in derived class')

    @abstractmethod
    def update_bars(self, data):
        raise NotImplementedError('override update_bars in derived class')


class HistOhlcMarketDataCache(AbstractMarketDataCache):

    def __init__(self, events_queue, file_name, clean_dir=os.path.join(kaqt_path(), clean_data_path),
                 resolution=MarketDataResolution.MINUTE):
        self.events_queue = events_queue
        h5_store = pd.HDFStore(os.path.join(clean_dir, file_name))
        self.market_data = h5_store['20150814']
        h5_store.close()
        self.symbols = self.market_data.keys().values
        self.symbol_data = pd.DataFrame()
        self.mdtype = MarketDataType.TRADE_BAR
        self.resolution = resolution

    def get_bars(self, symbol, n=1):
        pass

    def update_bars(self, data):
        pass








