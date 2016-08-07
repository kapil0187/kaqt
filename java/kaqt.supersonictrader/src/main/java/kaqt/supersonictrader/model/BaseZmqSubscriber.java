package kaqt.supersonictrader.model;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

public abstract class BaseZmqSubscriber extends Thread implements IZmqSubscriber {
	private Socket subscriber;
	
	public BaseZmqSubscriber(String connection) {
		subscriber = ContextSingleton.getInstance().socket(ZMQ.SUB);
		subscriber.connect(connection);
	}
	
	public void handleUpdate(String address, String content) {
		
	}
	
	public void subscribe(String topic) {
		subscriber.subscribe(topic.getBytes());
	}
	
	@Override
	public void run() {
		while (this.isInterrupted()) {
			String address = subscriber.recvStr();
			String content = subscriber.recvStr();
			this.handleUpdate(address, content);
		}
	}
}
