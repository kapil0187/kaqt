package kaqt.orderex.coinbase;

public class Account {
    private String id;
    private Double balance;
    private Double hold;
    private Double available;
    private String currency;
    
	public Account(String id, Double balance, Double hold, Double available,
			String currency) {
		super();
		this.id = id;
		this.balance = balance;
		this.hold = hold;
		this.available = available;
		this.currency = currency;
	}
	
	public String getId() {
		return id;
	}
	public Double getBalance() {
		return balance;
	}
	public Double getHold() {
		return hold;
	}
	public Double getAvailable() {
		return available;
	}
	public String getCurrency() {
		return currency;
	}

}
