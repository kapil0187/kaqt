import uuid
from enum import Enum
import datetime
from abc import ABCMeta, abstractmethod
import numpy as np


class AlgoMode(Enum):
    LIVE = 1
    BACKTEST = 2


class OrderStatus(Enum):
    INVALID = -1
    NEW = 1
    SUBMITTED = 2
    PARTIALLY_FILLED = 3
    FILLED = 4
    CANCELED = 5
    NONE = 0


class Strategy(object):
    __metaclass__ = ABCMeta

    def __init__(self, *args, **kwargs):
        # self.algo_id = kwargs.pop('algo_id', str(uuid.uuid4()))
        self.algo_mode = kwargs.pop('algo_mode', AlgoMode.BACKTEST)
        # self.instruments = kwargs.pop('instruments', {})
        # self.portfolio_manager = kwargs.pop('portfolio_manager', {})
        # self.indicators = kwargs.pop('indicators', {})
        # self.order_manager = kwargs.pop('order_manager', {})
        # self.fills_manager = kwargs.pop('fills_manager', {})
        # self.debug_messages = np.array()
        # self.log_messages = np.array()
        # self.error_messages = np.array()
        # self.previous_debug_message = ""
        # self.previous_error_message = ""

        # self.start_date = kwargs.pop("start_date", datetime.datetime(2000, 1, 1, 0, 0, 0, 0))
        # self.end_date = kwargs.pop("end_date",datetime.datetime.now())

    @abstractmethod
    def initialize(self):
        raise NotImplementedError('override the initialize method in derived class')

    @abstractmethod
    def generate_signal(self, event):
        raise NotImplementedError('override the initialize method in derived class')
