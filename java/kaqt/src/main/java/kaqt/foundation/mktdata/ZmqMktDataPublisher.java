package kaqt.foundation.mktdata;

import org.zeromq.ZMQ.Context;

import kaqt.foundation.connections.ZmqPublisherSocket;
import kaqt.providers.protobuf.LeveloneQuote.LevelOneQuote;
import kaqt.symbology.SymbologyStore;

public abstract class ZmqMktDataPublisher extends ZmqPublisherSocket<LevelOneQuote>
{
	protected SymbologyStore store;
	
	public ZmqMktDataPublisher(Context context, String bindAddress, SymbologyStore store)
	{
		super(context, bindAddress);
		this.store = store;
	}

	@Override
	public void send(String topic, LevelOneQuote data)
	{
		super.socket.sendMore(topic.getBytes());
		super.socket.send(data.toByteArray());
	}	
	
	@Override
	public void start()
	{
		super.start();
		this.process();
	}
	
	public abstract void process();
}
