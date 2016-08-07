package kaqt.orderex.coinbase;

public class Order {
	
	private String id;
	private String price;
	private String product_id;
	private String side;
	private String status;
	private String settled;
	private String filled_size;
	private String done_at;
	private String created_at;
	private String done_reason;
	private String fill_fees;
	private String size;
	
	public Order(String id, String price, String product_id,
			String side, String status, String settled, String filled_size,
			String done_at, String created_at, String done_reason,
			String fill_fees, String size) {
		super();
		this.id = id;
		this.price = price;
		this.product_id = product_id;
		this.side = side;
		this.status = status;
		this.settled = settled;
		this.filled_size = filled_size;
		this.done_at = done_at;
		this.created_at = created_at;
		this.done_reason = done_reason;
		this.fill_fees = fill_fees;
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

	public String getStatus() {
		return status;
	}

	public String getSettled() {
		return settled;
	}

	public String getFilled_size() {
		return filled_size;
	}

	public String getDone_at() {
		return done_at;
	}

	public String getCreated_at() {
		return created_at;
	}

	public String getDone_reason() {
		return done_reason;
	}

	public String getFill_fees() {
		return fill_fees;
	}

	public String getSize() {
		return size;
	}
}
