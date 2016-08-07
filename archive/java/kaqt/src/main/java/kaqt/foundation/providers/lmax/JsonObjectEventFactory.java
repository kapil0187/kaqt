package kaqt.foundation.providers.lmax;

import kaqt.foundation.EventArgs;

import com.google.gson.JsonObject;
import com.lmax.disruptor.EventFactory;

public class JsonObjectEventFactory implements EventFactory<EventArgs<JsonObject>>{

	@Override
	public EventArgs<JsonObject> newInstance() {
		return new EventArgs<JsonObject>();
	}

}
