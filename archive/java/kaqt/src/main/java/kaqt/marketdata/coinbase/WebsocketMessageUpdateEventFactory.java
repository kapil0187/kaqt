package kaqt.marketdata.coinbase;

import kaqt.foundation.EventArgs;

import com.lmax.disruptor.EventFactory;

public class WebsocketMessageUpdateEventFactory implements EventFactory<EventArgs<WebsocketMessageUpdate>> {

	@Override
	public EventArgs<WebsocketMessageUpdate> newInstance() {
		return new EventArgs<WebsocketMessageUpdate>();
	}

}
