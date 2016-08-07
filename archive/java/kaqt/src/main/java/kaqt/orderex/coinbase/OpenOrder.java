package kaqt.orderex.coinbase;

public class OpenOrder {
    private String id;
    private Double size;
    private Double price;
    private String product_id;
    private String status; 
	private Double filled_size; 
    private Double fill_fees; 
    private Boolean settled; 
    private String side; 
    private String created_at; 
    
    public OpenOrder(String id, Double size, Double price, String product_id,
			String status, Double filled_size, Double fill_fees,
			Boolean settled, String side, String created_at) {
		super();
		this.id = id;
		this.size = size;
		this.price = price;
		this.product_id = product_id;
		this.status = status;
		this.filled_size = filled_size;
		this.fill_fees = fill_fees;
		this.settled = settled;
		this.side = side;
		this.created_at = created_at;
	}
    
	public String getId() {
		return id;
	}
	
	public Double getSize() {
		return size;
	}
	
	public Double getPrice() {
		return price;
	}
	
	public String getProduct_id() {
		return product_id;
	}
	
	public String getStatus() {
		return status;
	}
	
	public Double getFilled_size() {
		return filled_size;
	}
	
	public Double getFill_fees() {
		return fill_fees;
	}
	
	public Boolean getSettled() {
		return settled;
	}
	
	public String getSide() {
		return side;
	}
	
	public String getCreated_at() {
		return created_at;
	}
}
