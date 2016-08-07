from abc import ABCMeta, abstractmethod


class RiskModel(object):
    __metaclass__ = ABCMeta

    pass


class TransactionCostModel(object):
    __metaclass__ = ABCMeta

    @abstractmethod
    def calculate_fee(self, event):
        raise NotImplementedError('override the calculate_fee method in derived class')