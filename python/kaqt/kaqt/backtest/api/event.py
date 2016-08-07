from abc import ABCMeta
from enum import Enum
from kaqt.backtest.api.orders import OrderSide, OrderType


class EventType(Enum):
    INVALID = 0
    MARKET_DATA = 1
    SIGNAL = 2
    ORDER = 3
    FILL = 4


class Event(object):
    __metaclass__ = ABCMeta
    pass


class MarketDataEvent(Event):
    def __init__(self):
        self.type = EventType.MARKET_DATA


class SignalEventType(Enum):
    INVALID = 0
    LONG = 1
    SHORT = 2
    FLAT = 3


class SignalEvent(Event):

    def __init__(self, algo_id, symbol, datetime, strength, signal_type=SignalEventType.INVALID):
        self.type = EventType.SIGNAL
        self.algo_id = algo_id
        self.symbol = symbol
        self.datetime = datetime
        self.strength = strength
        self.signal_type = signal_type


class OrderEvent(Event):

    def __init__(self, symbol, quantity, price=None, order_type=OrderType.LIMIT, direction=OrderSide.INVALID):
        self.type = EventType.ORDER
        self.symbol = symbol
        self.order_type = order_type
        self.quantity = quantity
        self.direction = direction

        if order_type is OrderType.LIMIT:
            if price is None:
                raise ValueError("price cannot be None for Limit order")
            self.price = price

    def __str__(self):
        print("Order: Symbol=%s, Type=%s, Quantity=%s, Direction=%s" %
              (self.symbol, str(self.order_type), str(self.quantity), str(self.direction)))


class FillEvent(Event):

    def __init__(self, fill_time, symbol, exchange, quantity,
                 direction=OrderSide.INVALID, fill_cost=0, commissions=0):

        self.fill_time = fill_time
        self.symbol = symbol
        self.exchange = exchange
        self.quantity = quantity
        self.direction = direction
        self.cost = fill_cost
        self.commissions = commissions





