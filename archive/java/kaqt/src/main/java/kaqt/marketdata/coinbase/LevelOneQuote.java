package kaqt.marketdata.coinbase;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class LevelOneQuote {
	private double bid;
	private double ask;
	private double last;
	private double bidQuantity;
	private double askQuantity;
	private double lastQuantity;
	private long bidUpdateTime;
	private long askUpdateTime;
	private long lastUpdateTime;

	private Integer instrumentId;
	private Long timeStamp;

	public LevelOneQuote(double bid, double ask, double last,
			double bidQuantity, double askQuantity, double lastQuantity,
			long bidUpdateTime, long askUpdateTime, long lastUpdateTime,
			Integer instrumentId, Long timeStamp) {
		super();
		this.bid = bid;
		this.ask = ask;
		this.last = last;
		this.bidQuantity = bidQuantity;
		this.askQuantity = askQuantity;
		this.lastQuantity = lastQuantity;
		this.bidUpdateTime = bidUpdateTime;
		this.askUpdateTime = askUpdateTime;
		this.lastUpdateTime = lastUpdateTime;
		this.instrumentId = instrumentId;
		this.timeStamp = timeStamp;
	}
	
	public double getBid() {
		return bid;
	}
	
	public double getAsk() {
		return ask;
	}
	
	public double getLast() {
		return last;
	}
	
	public long getBidUpdateTime() {
		return bidUpdateTime;
	}
	
	public long getAskUpdateTime() {
		return askUpdateTime;
	}
	
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}
	
	public Integer getInstrumentId() {
		return instrumentId;
	}
	
	public Long getTimeStamp() {
		return timeStamp;
	}
	

	public double getBidQuantity() {
		return bidQuantity;
	}

	public double getAskQuantity() {
		return askQuantity;
	}

	public double getLastQuantity() {
		return lastQuantity;
	}

	@Override
	public String toString() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		df.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
		
		return df.format(timeStamp) + " [bid=" + bid + ", ask=" + ask + ", last=" + last
				+ ", bidQuantity=" + bidQuantity + ", askQuantity="
				+ askQuantity + ", lastQuantity=" + lastQuantity + "]";
	}
}
