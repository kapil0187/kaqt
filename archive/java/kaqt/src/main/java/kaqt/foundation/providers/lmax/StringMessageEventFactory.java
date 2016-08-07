package kaqt.foundation.providers.lmax;

import kaqt.foundation.EventArgs;

import com.lmax.disruptor.EventFactory;

public class StringMessageEventFactory implements EventFactory<EventArgs<String>> {

	@Override
	public EventArgs<String> newInstance() {
		return new EventArgs<String>();
	}

}
