package kaqt.foundation.connections;

import org.zeromq.ZMQ;
import org.zeromq.ZMQException;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import kaqt.foundation.interfaces.SubscriberSocket;

public abstract class ZmqSubscriberSocket<DataType> implements
		SubscriberSocket<DataType>
{
	private Context context;
	
	protected String connectAddress;
	protected boolean isTerminated = false;
	protected Socket socket;
	
	public ZmqSubscriberSocket(Context context, String connectAddress)
	{
		super();
		this.context = context;
		this.socket = this.context.socket(ZMQ.SUB);
		this.connectAddress = connectAddress;
	}

	@Override
	public void connect()
	{
		this.socket.connect(this.connectAddress);
	}
	
	@Override
	public void start()
	{
		this.process();
	}
	
	@Override
	public void stop()
	{
		this.isTerminated = true;
		this.socket.close();
	}

	@Override
	public void subscribe(String topic)
	{
		socket.subscribe(topic.getBytes());
	}

	@Override
	public void unsubscribe(String topic)
	{
		socket.unsubscribe(topic.getBytes());		
	}
}
