package kaqt.supersonic.model;


public class FillOrderUpdate extends OrderUpdate {
	private Fill fill;

	public FillOrderUpdate(String order_id, String client_id, String timestamp,
			String symbol, String state, Fill fill) {
		super(order_id, client_id, timestamp, symbol, state);
		this.fill = fill;
	}

	public Fill getFill() {
		return fill;
	}

	public void setFill(Fill fill) {
		this.fill = fill;
	}
}
