from abc import ABCMeta, abstractmethod


class PortfolioConstructionModel(object):
    __metaclass__ = ABCMeta

    def __init__(self):
        pass

    @abstractmethod
    def handle_signal(self, event):
        raise NotImplementedError('override the handle_signal method in the derived class')

    @abstractmethod
    def handle_ack(self, event):
        raise NotImplementedError('override the handle_signal method in the derived class')

    @abstractmethod
    def handle_cancel(self, event):
        raise NotImplementedError('override the handle_signal method in the derived class')

    @abstractmethod
    def handle_fill(self, event):
        raise NotImplementedError('override the handle_fill method in the derived class')