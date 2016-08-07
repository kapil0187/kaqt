package kaqt.foundation.interfaces;

public interface SubscriberSocket<DataType>
{
	void start();
	void stop();
	void process();
	void connect();
	void subscribe(String topic);
	void unsubscribe(String topic);
	void handleUpdate(DataType update, String topic);
}
