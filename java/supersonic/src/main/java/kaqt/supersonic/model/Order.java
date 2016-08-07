package kaqt.supersonic.model;

public class Order {
	private String type;
	private double price;
	private int quantity;
	
	public Order(String type, double price, int quantity) {
		super();
		this.type = type;
		this.price = price;
		this.quantity = quantity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
