from abc import ABCMeta, abstractmethod
import threading


class Backtester(threading.Thread):
    __metaclass__ = ABCMeta

    def __init__(self):
        super(Backtester, self).__init__()

    @abstractmethod
    def initialize(self):
        raise NotImplementedError('override start method in derived class')

    @abstractmethod
    def stop(self):
        raise NotImplementedError('override stop method in derived class')

