from abc import ABCMeta, abstractmethod

import numpy as np

from kaqt.backtest.market_data import MarketDataResolution, MarketDataType
from kaqt.backtest.api.orders import OrderEvent, OrderStatus, OrderSide


class SecurityCache(object):

    def __init__(self, resolution, md_type=MarketDataType.TRADE_BAR, *args, **kwargs):
        self.resolution = resolution
        self.md_type = md_type
        self.last_data = None

    def add_data(self, data):
        self.last_data = data

    def reset(self):
        self.last_data = None


class SecurityHolding(object):

    def __init__(self, security, *args, **kwargs):
        self.security = security
        self.average_price = np.nan
        self.quantity = np.nan
        self.total_profit = 0.0
        self.last_trade_profit = 0.0
        self.total_fees = 0.0
        self.leverage = None
        self.price = None

    def holding_cost(self):
        return self.average_price * self.quantity

    def unlevered_holidng_cost(self):
        return self.holding_cost()/self.leverage

    def value(self):
        return self.price * self.quantity

    def istrading(self):
        return self.quantity != 0

    def is_long(self):
        return self.quantity > 0

    def is_short(self):
        return self.quantity < 0

    def net_profit(self):
        return self.total_profit - self.total_fees

    def unrealized_profit(self):
        pass

    def add_fees(self, fees):
        self.total_fees += fees

    def add_profit(self, profit):
        self.total_profit += profit


class AbstractSecurityTransactionModel(object):
    __metaclass__ = ABCMeta

    @abstractmethod
    def market_fill(self, security, order):
        pass

    @abstractmethod
    def limit_fill(self, security, order):
        pass

    @abstractmethod
    def slippage(self, security, order):
        pass

    @abstractmethod
    def fees(self, security, order):
        pass


class DefaultSecurityTransactionModel(AbstractSecurityTransactionModel):

    def __init__(self):
        super(DefaultSecurityTransactionModel, self).__init__()

    def market_fill(self, security, order):
        fill = OrderEvent(order_id=order.order_id)

        if order.status is OrderStatus.CANCELED:
            return fill

        if not self.is_exchange_open():
            return fill

        order.price = security.price
        order.status = OrderStatus.FILLED

        slip = self.slippage(security=security, order=order)

        if order.side is OrderSide.BUY:
            order.price += slip
        elif order.side is OrderSide.SELL:
            order.price -= slip

        fill.fill_price = order.price
        fill.fill_quantity = order.quantity
        fill.status = order.status

        return fill

    def limit_fill(self, security, order):
        fill = OrderEvent(order_id=order.order_id)

        if order.status is OrderStatus.CANCELED:
            return fill

        min_max = self.min_max_prices(security)

        if order.side is OrderSide.BUY:
            if min_max["Min"] < order.limit_price:
                order.status = OrderStatus.FILLED
                order.price = np.min(min_max["Max"], order.limit_price)
        elif order.side is OrderSide.SELL:
            if min_max["Max"] > order.limit_price:
                order.status = OrderStatus.FILLED
                order.price = np.max(min_max["Min"], order.limit_price)

        if any((order.status is OrderStatus.FILLED, order.status is OrderStatus.PARTIALLY_FILLED)):
            fill.fill_quantity = order.quantity
            fill.fill_price = order.price
            fill.status = order.status

        return fill

    def slippage(self, security, order):
        return 0

    def fees(self, security, order):
        return 0

    def is_exchange_open(self, security):
        return True

    def min_max_prices(self, security):
        mkt_data = security.security_cache.last_data

        min_price = None
        max_price = None

        if security.md_type is MarketDataType.TRADE_BAR:
            min_price = mkt_data.low
            max_price = mkt_data.high
        elif security.md_type is MarketDataType.QUOTE:
            min_price = mkt_data.last
            max_price = mkt_data.last

        return {"Min": min_price, "Max": max_price}


class Security(object):

    def __init__(self, *args, **kwargs):
        self.symbol = kwargs.pop("symbol")
        self.resolution = kwargs.pop("resolution", MarketDataResolution.MINUTE)
        self.md_type = kwargs.pop("md_type", MarketDataType.TRADE_BAR)
        self.security_cache = SecurityCache(resolution=self.resolution, md_type=self.md_type)
        self.security_holding = SecurityHolding(self)
        self.transaction_model = kwargs.pop("transaction_model",
                                            DefaultSecurityTransactionModel())



    def last_data(self):
        return self.security_cache.last_data






