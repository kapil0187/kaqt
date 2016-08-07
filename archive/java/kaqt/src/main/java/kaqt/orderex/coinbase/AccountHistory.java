package kaqt.orderex.coinbase;

public class AccountHistory {
	private String id;
	private String created_at;
	private String amount; 
	private String balance; 
	private String type; 
	private Details details;
	
    public String getId() {
		return id;
	}
	
	public String getCreated_at() {
		return created_at;
	}
	
	public String getAmount() {
		return amount;
	}
	
	public String getBalance() {
		return balance;
	}
	
	public String getType() {
		return type;
	}
	
	public Details getDetails() {
		return details;
	} 
}
