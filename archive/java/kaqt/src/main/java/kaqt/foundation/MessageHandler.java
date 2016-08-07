package kaqt.foundation;

public interface MessageHandler<T> {
	public void handleMessage(T message);
}
