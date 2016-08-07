package kaqt.marketdata.coinbase;

import java.util.List;

public class OrderBookSnapshot {
	public int sequence;
	public List<List<String>> bids;
	public List<List<String>> asks;
	
	public String toString() {
		return bids.size() + "; " + asks.size();
	}
}
