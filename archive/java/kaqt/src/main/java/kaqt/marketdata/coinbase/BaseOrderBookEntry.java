package kaqt.marketdata.coinbase;

import java.time.Instant;
import java.util.List;

public class BaseOrderBookEntry {
	
	private String orderId;
	private Double price;
	private Double quantity;
	private Long timestamp;
	
	public BaseOrderBookEntry(double price, double quantity, String orderId, Long timestamp) {
		this.price = price;
		this.quantity = quantity;
		this.orderId = orderId;
		this.timestamp = timestamp;
	}
	
	public BaseOrderBookEntry(List<String> jsonRecord) {
		this.price = Double.parseDouble(jsonRecord.get(0));
		this.quantity = Double.parseDouble(jsonRecord.get(1));		
		this.orderId = jsonRecord.get(2);
		this.timestamp = Instant.now().getEpochSecond();
	}
	
	
	public String getOrderId() {
		return this.orderId;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public double getQuantity() {
		return this.quantity;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public Long getTimestamp() {
		return timestamp;
	}
}
