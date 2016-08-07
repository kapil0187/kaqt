package kaqt.supersonic.model;

public class OrderUpdate {
	private String order_id;
	private String client_id;
	private String timestamp;
	private String symbol;
	private String state;
	
	public OrderUpdate(String order_id, String client_id, String timestamp,
			String symbol, String state) {
		super();
		this.order_id = order_id;
		this.client_id = client_id;
		this.timestamp = timestamp;
		this.symbol = symbol;
		this.state = state;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOrder_id() {
		return order_id;
	}

	public String getClient_id() {
		return client_id;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getState() {
		return state;
	}
}
