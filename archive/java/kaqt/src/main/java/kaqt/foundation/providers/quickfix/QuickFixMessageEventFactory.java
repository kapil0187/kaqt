package kaqt.foundation.providers.quickfix;

import quickfix.Message;
import kaqt.foundation.EventArgs;

import com.lmax.disruptor.EventFactory;

public class QuickFixMessageEventFactory implements EventFactory<EventArgs<Message>> {

	public EventArgs<Message> newInstance() {
		return new EventArgs<Message>();
	}
}
