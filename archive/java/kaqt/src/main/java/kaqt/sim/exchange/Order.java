package kaqt.sim.exchange;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;

public class Order {
	private OrderSide side;
	private Integer instrumentId;
	private Double quantity;
	private Double price;
	private String orderId;
	
	public Order(OrderSide side, int instrumentId, 
			double quantity, double price, String orderId) {
		this.side = side;
		this.instrumentId = instrumentId;
		this.quantity = quantity;
		this.price = price;
		this.orderId = orderId;
	}
	
	public static final Attribute<Order, OrderSide> SIDE = new SimpleAttribute<Order, OrderSide>("side") {
		public OrderSide getValue(Order order) {
			return order.side;
		}
	};
	
	public static final Attribute<Order, Integer> INSTRUMENT_ID = new SimpleAttribute<Order, Integer>("instrumentId") {
		public Integer getValue(Order order) {
			return order.instrumentId;
		}
	};
	
	public static final Attribute<Order, Double> QUANTITY = new SimpleAttribute<Order, Double>("quantity") {
		public Double getValue(Order order) {
			return order.quantity;
		}
	};
	
	public static final Attribute<Order, Double> PRICE = new SimpleAttribute<Order, Double>("price") {
		public Double getValue(Order order) {
			return order.price;
		}
	};
	
	public static final Attribute<Order, String> ORDER_ID = new SimpleAttribute<Order, String>("orderId") {
		public String getValue(Order order) {
			return order.orderId;
		}
	};
	
	public OrderSide getSide() {
		return side;
	}
	
	public Integer getInstrumentId() {
		return instrumentId;
	}
	
	public Double getQuantity() {
		return quantity;
	}
	
	public Double getPrice() {
		return price;
	}
	
	public String getOrderId() {
		return orderId;
	}
}
