package kaqt.foundation.connections;

import kaqt.foundation.interfaces.PublisherSocket;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public abstract class ZmqPublisherSocket<DataType> implements PublisherSocket<DataType>
{
	private String bindAddress;
	private Context context;
	
	protected Socket socket;
	
	public ZmqPublisherSocket(Context context, String bindAddress)
	{
		this.context = context;
		this.socket = this.context.socket(ZMQ.PUB);
		this.bindAddress = bindAddress;
	}
	
	public void start()
	{
		socket.bind(this.bindAddress);
	}
	
	public void stop()
	{
		socket.close();
	}
}
