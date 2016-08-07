package kaqt.orderex.coinbase;

public class NewOrderResponse {
	private String id;
	private String price;
	private String product_id;
	private String side;
	private String stp;
	private String size;

	public NewOrderResponse(String id, String price, String product_id,
			String side, String stp, String size) {
		super();
		this.id = id;
		this.price = price;
		this.product_id = product_id;
		this.side = side;
		this.stp = stp;
		this.size = size;
	}
	
	public String getId() {
		return id;
	}

	public String getPrice() {
		return price;
	}

	public String getProduct_id() {
		return product_id;
	}

	public String getSide() {
		return side;
	}

	public String getStp() {
		return stp;
	}

	public String getSize() {
		return size;
	}

	@Override
	public String toString() {
		return "NewOrderResponse [id = " + id + ", price = " + price
				+ ", product_id = " + product_id + ", side = " + side
				+ ", stp = " + stp + ", size = " + size + "]";
	}
}
