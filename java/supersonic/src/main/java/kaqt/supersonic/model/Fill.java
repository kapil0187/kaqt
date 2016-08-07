package kaqt.supersonic.model;

public class Fill {
	private double price;
	private int quantity;
	private String type;
	
	public Fill(double price, int quantity, String type) {
		super();
		this.price = price;
		this.quantity = quantity;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
