package kaqt.orderex.coinbase;

public class Product {
	private String id;
	private String base_currency;
	private String quote_increment;
	private String base_min_size;
	private String quote_currency;
	private String base_max_size;
	
	public Product(String id, String base_currency, String quote_increment,
			String base_min_size, String quote_currency, String base_max_size) {
		super();
		this.id = id;
		this.base_currency = base_currency;
		this.quote_increment = quote_increment;
		this.base_min_size = base_min_size;
		this.quote_currency = quote_currency;
		this.base_max_size = base_max_size;
	}

	public String getId() {
		return id;
	}
	
	public String getBase_currency() {
		return base_currency;
	}

	public String getQuote_increment() {
		return quote_increment;
	}

	public String getBase_min_size() {
		return base_min_size;
	}

	public String getQuote_currency() {
		return quote_currency;
	}

	public String getBase_max_size() {
		return base_max_size;
	}
}
