package kaqt.orderex.coinbase;

import java.math.BigDecimal;

public class Fill {
	private Integer trade_id; 
	private String product_id; 
	private String price;
	private Double size; 
	private String order_id; 
	private String created_at; 
	private BigDecimal fee; 
	private Boolean settled;
	private String side;
    
	public Fill(int trade_id, String product_id, String price, Double size,
			String order_id, String cre_ated_at, BigDecimal fee, Boolean settled,
			String side) {
		super();
		this.trade_id = trade_id;
		this.product_id = product_id;
		this.price = price;
		this.size = size;
		this.order_id = order_id;
		this.created_at = created_at;
		this.fee = fee;
		this.settled = settled;
		this.side = side;
	}
	
	public int getTrade_id() {
		return trade_id;
	}
	
	public String getProduct_id() {
		return product_id;
	}
	
	public String getPrice() {
		return price;
	}
	
	public Double getSize() {
		return size;
	}
	
	public String getOrder_id() {
		return order_id;
	}
	
	public String getCreated_at() {
		return created_at;
	}
	
	public BigDecimal getFee() {
		return fee;
	}
	
	public Boolean getSettled() {
		return settled;
	}
	
	public String getSide() {
		return side;
	} 
}
