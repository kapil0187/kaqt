package kaqt.edutils.model;

public class Quote {
	private String leg;
	private double bid_price;
	private double ask_price;
	private int bid_volume;
	private int ask_volume;
	
	public Quote(String leg, double bid_price, double ask_price,
			int bid_volumne, int ask_volume) {
		super();
		this.leg = leg;
		this.bid_price = bid_price;
		this.ask_price = ask_price;
		this.bid_volume = bid_volumne;
		this.ask_volume = ask_volume;
	}
	
	public String getLeg() {
		return leg;
	}

	public void setLeg(String leg) {
		this.leg = leg;
	}
	
	public double getBid_price() {
		return bid_price;
	}
	
	public void setBid_price(double bid_price) {
		this.bid_price = bid_price;
	}
	
	public double getAsk_price() {
		return ask_price;
	}
	
	public void setAsk_price(double ask_price) {
		this.ask_price = ask_price;
	}
	
	public int getBid_volume() {
		return bid_volume;
	}
	
	public void setBid_volume(int bid_volumne) {
		this.bid_volume = bid_volumne;
	}
	
	public int getAsk_volume() {
		return ask_volume;
	}
	
	public void setAsk_volume(int ask_volume) {
		this.ask_volume = ask_volume;
	}

	@Override
	public String toString() {
		return "Quote [leg=" + leg + ", bid_price=" + bid_price
				+ ", ask_price=" + ask_price + ", bid_volumne=" + bid_volume
				+ ", ask_volume=" + ask_volume + "]";
	}
}
