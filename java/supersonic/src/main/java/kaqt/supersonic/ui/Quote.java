package kaqt.supersonic.ui;

public class Quote {
	private double bid;
	private double ask;
	private int bidSize;
	private int askSize;
	private String ticker;
	private String timeStamp;
	
	Quote(double bid, double ask, int bidSize, int askSize, String ticker, String datetime) {
		this.bid = bid;
		this.ask = ask;
		this.ticker = ticker;
		this.timeStamp = datetime;
		this.bidSize = bidSize;
		this.askSize = askSize;
	}
		
	public double getBid() {
		return bid;
	}
	
	public double getAsk() {
		return ask;
	}
	
	public int getBidSize() {
		return bidSize;
	}
	
	public int getAskSize() {
		return askSize;
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public String getTimeStamp() {
		return timeStamp;
	}
	
	public void setBid(double bid) {
		this.bid = bid;
	}
	
	public void setAsk(double ask) {
		this.ask = ask;
	}
	
	public void setBidSize(int bidSize) {
		this.bidSize = bidSize;
	}
	
	public void setAskSize(int askSize) {
		this.askSize = askSize;
	}
	
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString() {
		return "Price [bid=" + bid + ", ask=" + ask + ", bidSize=" + bidSize
				+ ", askSize=" + askSize + ", ticker=" + ticker
				+ ", timeStamp=" + timeStamp + "]";
	}
}
