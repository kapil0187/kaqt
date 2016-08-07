package kaqt.marketdata.coinbase;

import com.google.gson.JsonObject;

public class WebsocketMessageUpdate {
	JsonObject update;
	OrderBookUpdateTypeEnum updateType;
	
	public WebsocketMessageUpdate(JsonObject update, OrderBookUpdateTypeEnum updateType) {
		super();
		this.update = update;
		this.updateType = updateType;
	}

	public JsonObject getUpdate() {
		return update;
	}

	public OrderBookUpdateTypeEnum getUpdateType() {
		return updateType;
	}
}
