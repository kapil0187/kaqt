package kaqt.foundation.interfaces;

public interface PublisherSocket<DataType>
{
	void start();
	void stop();
	void send(String topic, DataType data);
}
