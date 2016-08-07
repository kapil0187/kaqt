package kaqt.marketdata.coinbase;

import java.util.List;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;

public class OrderBookEntry extends BaseOrderBookEntry {
	
	public enum OrderType {BID, ASK};
	
	private OrderType type;
	
	public OrderBookEntry(double price, double quantity, String orderId, Long timestamp, OrderBookEntry.OrderType type) {
		super(price, quantity, orderId, timestamp);
		this.type = type;
	}
	
	public OrderBookEntry(List<String> jsonRecord, OrderBookEntry.OrderType type) {
		super(jsonRecord);
		this.type = type;
	}

	public static final Attribute<OrderBookEntry, String> ORDER_ID = new SimpleAttribute<OrderBookEntry, String>("orderId") {
		@Override
		public String getValue(OrderBookEntry order) {
			return order.getOrderId();
		}
	};
	
	public static final Attribute<OrderBookEntry, Double> PRICE = new SimpleAttribute<OrderBookEntry, Double>("price") {
		@Override
		public Double getValue(OrderBookEntry order) {
			return order.getPrice();
		}
	};
		
	public static final Attribute<OrderBookEntry, OrderType> TYPE = new SimpleAttribute<OrderBookEntry, OrderType>("type") {
		@Override
		public OrderType getValue(OrderBookEntry order) {
			return order.type;
		}
	};
	
	public OrderType getOrderType() {
		return this.type;
	}
}
