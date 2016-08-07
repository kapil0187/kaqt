package kaqt.orderex.coinbase;

import java.math.BigDecimal;

public class NewOrderRequest {
    private String client_oid; //optional
    private BigDecimal price; 
    private Double size;
    private String side;
    private String product_id;
    private String stp; //optional: values are dc, co , cn , cb
    
    public NewOrderRequest(BigDecimal price, Double size, String side,
			String product_id) {
		super();
		this.price = price;
		this.size = size;
		this.side = side;
		this.product_id = product_id;
	}

	public String getStp() {
        return stp;
    }

    public void setStp(String stp) {
        this.stp = stp;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getSide() {
        return side;
    }

    public Double getSize() {
        return size;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getClient_oid() {
        return client_oid;
    }

	public void setClient_oid(String client_oid) {
		this.client_oid = client_oid;
	}
}
