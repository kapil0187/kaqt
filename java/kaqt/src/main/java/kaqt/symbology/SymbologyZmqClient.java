package kaqt.symbology;

import java.util.List;

import kaqt.foundation.connections.ContextSingleton;
import kaqt.foundation.db.DbFuturesInstrument;
import kaqt.providers.protobuf.Symbology.FuturesInstrument;
import kaqt.providers.protobuf.Symbology.FuturesInstrumentRequest;
import kaqt.providers.protobuf.Symbology.FuturesInstrumentResponse;
import kaqt.utils.ProtoBufHelper;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.google.protobuf.InvalidProtocolBufferException;

public class SymbologyZmqClient
{
	private Context context;
	private Socket requester;
	private String connectAddress;
	
	public SymbologyZmqClient(String connectAddress)
	{
		this.connectAddress = connectAddress;
	}
	
	public List<FuturesInstrument> queryAll()
	{
		List<FuturesInstrument> rv = null;
		
		FuturesInstrumentRequest request = ProtoBufHelper.createFuturesInstrumentRequst("ALL");
		requester.send(request.toByteArray(), 0);
		byte[] replyBytes = requester.recv(0);
		FuturesInstrumentResponse reply = null;
		try
		{
			reply = FuturesInstrumentResponse.parseFrom(replyBytes);
		}
		catch (InvalidProtocolBufferException e)
		{
			e.printStackTrace();
		}
		
		if (reply != null)
		{
			rv = reply.getInstrumentsList();
		}
		
		return rv;
	}
	
	public void start()
	{
		this.context = ContextSingleton.getInstance();
		this.requester = context.socket(ZMQ.REQ);
		this.requester.connect(this.connectAddress);
	}
		
	public void stop()
	{
		this.requester.close();
	}
}
