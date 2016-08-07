package kaqt.foundation.providers.quickfix;

import quickfix.Message;

public class QuickFixMessageEvent {
	private Message message;
	
	public void set(Message message) {
		this.message = message;
	}
	
	public Message get() {
		return message;
	}
}
