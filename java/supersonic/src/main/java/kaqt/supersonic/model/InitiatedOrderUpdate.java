package kaqt.supersonic.model;


public class InitiatedOrderUpdate extends OrderUpdate {	
	private Order order;
	
	public InitiatedOrderUpdate(String order_id, String client_id,
			String timestamp, String symbol, String state, Order order) {
		super(order_id, client_id, timestamp, symbol, state);
		this.order = order;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
