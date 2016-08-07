package kaqt.foundation.coinbase;

public class WebsocketClientConfiguration {
	private String snapshotUrl;
	private String websocketUrl;
	private String subscribeMessage;
	private String userAgent;
	
	public WebsocketClientConfiguration(String snapshotUrl,
			String websocketUrl, String subscribeMessage, String userAgent) {
		super();
		this.snapshotUrl = snapshotUrl;
		this.websocketUrl = websocketUrl;
		this.subscribeMessage = subscribeMessage;
		this.userAgent = userAgent;
	}

	public String getSnapshotUrl() {
		return snapshotUrl;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public String getWebsocketUrl() {
		return websocketUrl;
	}

	public String getSubscribeMessage() {
		return subscribeMessage;
	}
}
