package kaqt.supersonictrader.model;

public interface IZmqSubscriber {
	void handleUpdate(String address, String content);
	void subscribe(String topic);
}
