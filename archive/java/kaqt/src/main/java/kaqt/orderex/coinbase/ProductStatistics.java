package kaqt.orderex.coinbase;

public class ProductStatistics {
	private String open;
	private String volume;
	private String high;
	private String low;

	public ProductStatistics(String open, String volume, String high, String low) {
		super();
		this.open = open;
		this.volume = volume;
		this.high = high;
		this.low = low;
	}

	public String getOpen() {
		return open;
	}

	public String getVolume() {
		return volume;
	}

	public String getHigh() {
		return high;
	}

	public String getLow() {
		return low;
	}
}
