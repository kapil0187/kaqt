import zmq
#
# class SymbologyZmqClient(object):
#
#     def __init__(self):
#         context = zmq.Context()
#         socket = context.socket(zmq.REQ)
#         socket.connect("tcp://localhost:10001")
#



import zmq

#  Prepare our context and sockets
from kaqt.providers.protobuf.symbology_pb2 import FuturesInstrumentRequest, FuturesInstrumentResponse

context = zmq.Context()
socket = context.socket(zmq.REQ)
socket.connect("tcp://localhost:10001")

req = FuturesInstrumentRequest()
req.type = FuturesInstrumentRequest.ALL

socket.send(req.SerializeToString())
msg = socket.recv()

rep = FuturesInstrumentResponse()
rep.ParseFromString(msg)

print(rep)
