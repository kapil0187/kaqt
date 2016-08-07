import uuid
from enum import Enum
import datetime
from abc import ABCMeta, abstractmethod
import numpy as np
from kaqt.backtest.api.event import EventType, FillEvent


class OrderType(Enum):
    INVALID = 0
    LIMIT = 1
    MARKET = 2


class OrderSide(Enum):
    INVALID = 0
    BUY = 1
    SELL = 2


class OrderRequestType(Enum):
    NEW = 1
    UPDATE = 2
    CANCEL = 3


class OrderExecutionSystem(object):
    __metaclass__ = ABCMeta

    @abstractmethod
    def receive(self, event):
        raise NotImplementedError('override receive method in the derived class')

    @abstractmethod
    def acknowledge(self, event):
        raise NotImplementedError('override acknowledge method in the derived class')

    @abstractmethod
    def cancel(self, event):
        raise NotImplementedError('override cancel method in the derived class')

    @abstractmethod
    def fill(self, event):
        raise NotImplementedError('override fill method in the derived class')


class ShortCircuitOrderExecutionSystem(OrderExecutionSystem):

    def __init__(self, events_queus):
        super(ShortCircuitOrderExecutionSystem, self).__init__()
        self.events_queue = events_queus

    def fill(self, event):
        pass

    def acknowledge(self, event):
        pass

    def receive(self, event):
        if event.type == EventType.ORDER:
            fill = FillEvent(datetime.datetime.utcnow(), symbol=event.symbol,
                             exchange='SSX', quantity=event.quantity, direction=event.direction)
            self.events_queue.put(fill)

    def cancel(self, event):
        pass



