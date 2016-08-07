__author__ = 'kapilsharma'

import zmq
import pymongo as pm
from multiprocessing import Process
from zmq.eventloop import ioloop, zmqstream
import threading
import kaqt.providers.protobuf.symbology_pb2 as pbs
import time
import numpy as np

class FuturesInstrumentsClient(threading.Thread):
    def __init__(self, id):
        self.id_ = id
        threading.Thread.__init__(self)

    def run(self):
        context = zmq.Context()
        socket = context.socket(zmq.DEALER)
        identity = self.id_
        socket.identity = identity.encode('ascii')
        socket.connect('tcp://127.0.0.1:9090')

        poll = zmq.Poller()
        poll.register(socket, zmq.POLLIN)

        while True:
            test_id = 5
            req = pbs.FuturesInstrumentRequest()
            #req.id = test_id
            socket.send(req.SerializeToString())
            sockets = dict(poll.poll(1000))
            if socket in sockets:
                msg = socket.recv()
                rep = pbs.FuturesInstrumentResponse()
                rep.ParseFromString(msg)
                print("Received reply: " + msg)


class FuturesInstrumentsServer(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)

    def run(self):
        context = zmq.Context()
        frontend = context.socket(zmq.ROUTER)
        #frontend.bind('ipc://frontend.ipc')
        frontend.bind('tcp://*:9090')
        backend = context.socket(zmq.DEALER)
        backend.bind('inproc://backend')

        worker = FIServerWorker(context)
        worker.start()

        poll = zmq.Poller()
        poll.register(frontend, zmq.POLLIN)
        poll.register(backend,  zmq.POLLIN)

        while True:
            sockets = dict(poll.poll())
            if frontend in sockets:
                ident, msg = frontend.recv_multipart()
                backend.send_multipart([ident, msg])
            if backend in sockets:
                ident, msg = backend.recv_multipart()
                frontend.send_multipart([ident, msg])


        frontend.close()
        backend.close()
        context.term()

    def stop(self):
        pass


class FIServerWorker(threading.Thread):
    def __init__(self, context):
        threading.Thread.__init__(self)
        self.context_ = context
        self.db_ = pm.MongoClient()

    def run(self):
        symbols_ref = {}
        symbols = self.db_.kaqt.symbology.find()
        for record in symbols:
            symbols_ref[record['id']] = record
        worker = self.context_.socket(zmq.DEALER)
        worker.connect('inproc://backend')
        while True:
            ident, msg = worker.recv_multipart()
            req = pbs.FuturesInstrumentRequest()
            req.ParseFromString(msg)
            rep_to_be_sent = pbs.FuturesInstrumentResponse()
            if req.id is not 0:
                print("Received request for id = " + str(req.id))
                rep = rep_to_be_sent.instruments.add()
                found_instr = symbols_ref[req.id]
                rep.id = found_instr['id']
                rep.ticker = found_instr['ticker']
                rep.underlying = found_instr['underlying']
                rep.description = found_instr['description']
                rep.exchange_group = found_instr['exchange_group']
                rep.exchange = found_instr['exchange']
                rep.expiry_posix_datetime = int(found_instr['expiry_posix_datetime'])
                rep.min_order_size = found_instr['min_order_size']
                rep.tick_size = found_instr['tick_size']
                rep.tradeable_tick_size = found_instr['tradeable_tick_size']
                rep.currency = found_instr['currency']
            else:
                for k in symbols_ref.keys():
                    rep = pbs.FuturesInstrument()
                    found_instr = symbols_ref[k]
                    rep.id = found_instr['id']
                    rep.ticker = found_instr['ticker']
                    rep.underlying = found_instr['underlying']
                    rep.description = found_instr['description']
                    rep.exchange_group = found_instr['exchange_group']
                    rep.exchange = found_instr['exchange']
                    rep.expiry_posix_datetime = int(found_instr['expiry_posix_datetime'])
                    rep.min_order_size = found_instr['min_order_size']
                    rep.tick_size = found_instr['tick_size']
                    rep.tradeable_tick_size = found_instr['tradeable_tick_size']
                    rep.currency = found_instr['currency']

            print("Sending all instruments to identity = " + str(ident))
            worker.send_multipart([ident, rep_to_be_sent.SerializeToString()])

        worker.close()
        self.db_.close()

    def stop(self):
        pass


if __name__ == '__main__':
    server = FuturesInstrumentsServer()
    client = FuturesInstrumentsClient('123')

    server.start()
    client.start()

    server.join()