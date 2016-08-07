package kaqt.foundation.mktdata;

import java.util.function.Consumer;

import org.apache.log4j.Logger;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQException;

import kaqt.foundation.connections.ZmqSubscriberSocket;
import kaqt.providers.protobuf.LeveloneQuote.LevelOneQuote;
import kaqt.utils.ProtoBufHelper;

public class ZmqMktDataSubscriber extends ZmqSubscriberSocket<LevelOneQuote>
{	
	private static Logger logger = Logger.getLogger(ZmqMktDataSubscriber.class);
	
	protected Consumer<Quote> consumer;
	
	public ZmqMktDataSubscriber(Context context, String connectAddress, Consumer<Quote> consumer)
	{
		super(context, connectAddress);
		this.consumer = consumer;
	}

	@Override
	public void process()
	{
		logger.info(String.format("CONNECTING TO THE MKTDATA PUBLISHER AT %s", super.connectAddress));
		try
		{
			while(!super.isTerminated)
			{
				String topic = super.socket.recvStr();
				byte[] message = super.socket.recv();
				LevelOneQuote quote = LevelOneQuote.parseFrom(message);
				this.handleUpdate(quote, topic);
			}
		}
		catch (ZMQException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void handleUpdate(LevelOneQuote update, String topic)
	{
		Quote event = ProtoBufHelper.createQuote(update);
		consumer.accept(event);
	}
}
