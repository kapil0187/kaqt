package kaqt.marketdata.coinbase;

import static com.googlecode.cqengine.query.QueryFactory.equal;

import java.time.Instant;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import kaqt.foundation.providers.rx.ConcurrentObserverProcessor;
import kaqt.marketdata.coinbase.OrderBookEntry.OrderType;

import com.googlecode.cqengine.CQEngine;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.unique.UniqueIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.resultset.ResultSet;

public class OrderBook {

	public void setQuotesObservable(Observable<LevelOneQuote> quotesObservable) {
		this.quotesObservable = quotesObservable;
	}

	public int sequenceNumber = -1;
	private IndexedCollection<OrderBookEntry> book;
	private long timestamp;
	private double bestBid;
	private double bestAsk;
	private double lastTradePrice;
	private long lastTradeTime;
	private double lastTradeQuantity;
	private ConcurrentObserverProcessor<LevelOneQuote> quotesObserverProcessor;
	private Observable<LevelOneQuote> quotesObservable;
	
	public OrderBook() {
		this.quotesObserverProcessor = new ConcurrentObserverProcessor<LevelOneQuote>();
		this.quotesObservable = Observable.create(quotesObserverProcessor);
		this.bestBid = 0;
		this.bestAsk = Double.MAX_VALUE;
		book = CQEngine.newInstance();
		book.addIndex(UniqueIndex.onAttribute(OrderBookEntry.ORDER_ID));
		book.addIndex(NavigableIndex.onAttribute(OrderBookEntry.PRICE));
		book.addIndex(HashIndex.onAttribute(OrderBookEntry.TYPE));
	}
	
	public void add(OrderBookEntry record) {
		book.add(record);
		this.updateBestBidAskPrices(record);
	}
	
	public void remove(String orderId, Boolean sendUpdate) {
		Query<OrderBookEntry> query = equal(OrderBookEntry.ORDER_ID, orderId);
		ResultSet<OrderBookEntry> results = book.retrieve(query);
		for (OrderBookEntry order : results) {
			if (order.getOrderId().equals(orderId)) {
				book.remove(order);
				this.resetBestBidAskPrices(order.getOrderType());
				return;
			}
		}
	}
	
	public void update(OrderBookEntry record) {
		Query<OrderBookEntry> query = equal(OrderBookEntry.ORDER_ID, record.getOrderId());
		ResultSet<OrderBookEntry> results = book.retrieve(query);
		if (results.size() == 1) {
			for (OrderBookEntry order : results) {
				if (order.getOrderId().equals(record.getOrderId())) {
					order.setQuantity(record.getQuantity());
					order.setPrice(record.getPrice());	
					this.updateBestBidAskPrices(order);
					return;
				}
			}
		} else if (results.size() > 1) {
			System.out.println("Update recond fuction found multiple order ids");
		} else {
			this.add(record);
		}
	}
	
	public void match(String makerOrderId, String takerOrderId, double tradeSize) {
		Query<OrderBookEntry> makerQuery = equal(OrderBookEntry.ORDER_ID,
				makerOrderId);
		Query<OrderBookEntry> takerQuery = equal(OrderBookEntry.ORDER_ID,
				takerOrderId);
		ResultSet<OrderBookEntry> makerOrder = book.retrieve(makerQuery);
		ResultSet<OrderBookEntry> takerOrder = book.retrieve(takerQuery);
		for (OrderBookEntry order : makerOrder) {
			Boolean remove = tradeSize - order.getQuantity() < 0.00000001;
			order.setQuantity(order.getQuantity() - tradeSize);
			this.lastTradePrice = order.getPrice();
			this.lastTradeTime = order.getTimestamp();
			if (remove) {
				this.remove(makerOrderId, false);
			} else {
				this.resetBestBidAskPrices(order.getOrderType());
			}
		}
		for (OrderBookEntry order : takerOrder) {
			Boolean remove = tradeSize - order.getQuantity() < 0.00000001;
			order.setQuantity(order.getQuantity() - tradeSize);
			this.lastTradePrice = order.getPrice();
			this.lastTradeTime = order.getTimestamp();
			if (remove) {
				this.remove(takerOrderId, false);
			} else {
				this.resetBestBidAskPrices(order.getOrderType());
			}
		}
		this.lastTradeQuantity = tradeSize;
	}
	
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	
	public double getBestBidPrice() {
		return this.bestBid;
	}
	
	public double getBestAskPrice() {
		return this.bestAsk;
	}
	
	public double getBestBidQuantity() {
		Query<OrderBookEntry> query = equal(OrderBookEntry.PRICE, this.bestBid);
		double quantity = 0;
		for (OrderBookEntry order :  book.retrieve(query)) {
			quantity+=order.getQuantity();
		}
		return Math.round(quantity*100000000)/100000000.0d;
	}
	
	public double getBestAskQuantity() {
		Query<OrderBookEntry> query = equal(OrderBookEntry.PRICE, this.bestAsk);
		double quantity = 0;
		for (OrderBookEntry order :  book.retrieve(query)) {
			quantity+=order.getQuantity();
		}
		return Math.round(quantity*100000000)/100000000.0d;
	}
	
	private void updateBestBidAskPrices(OrderBookEntry order) {
		if (this.bestBidUpdatable(order)) {
			this.bestBid = order.getPrice();
			if (this.getBestBidQuantity() != 0 && this.getBestAskQuantity() != 0) {
				publishLevelOneQuote();
			}
		} else if (this.bestAskUpdatable(order)) {
			this.bestAsk = order.getPrice();
			if (this.getBestBidQuantity() != 0 && this.getBestAskQuantity() != 0) {
				publishLevelOneQuote();
			}
		}
	}
	
	public void resetBestBidAskPrices(OrderType type) {
		if (type == OrderType.BID) {
			this.resetBestBidPrice();
		} else {
			this.resetBestAskPrice();
		}
		publishLevelOneQuote();
	}
	
	private void resetBestBidPrice() {
		Query<OrderBookEntry> query = equal(OrderBookEntry.TYPE, OrderType.BID);
		double bb = 0;
		for (OrderBookEntry order: book.retrieve(query)) {
			if (bb < order.getPrice()) {
				bb = order.getPrice();
			}
		}
		this.bestBid = bb;
	}
	
	private void resetBestAskPrice() {
		Query<OrderBookEntry> query = equal(OrderBookEntry.TYPE, OrderType.ASK);
		double ba = 1000;
		for (OrderBookEntry order: book.retrieve(query)) {
			if (ba > order.getPrice()) {
				ba = order.getPrice();
			}
		}
		this.bestAsk = ba;
	}
	
	private Boolean bestBidUpdatable(OrderBookEntry order) {
		if (order.getOrderType() == OrderType.BID) {
			return this.bestBid < order.getPrice();
		}
		return false;
	}
	
	private Boolean bestAskUpdatable(OrderBookEntry order) {
		if (order.getOrderType() == OrderType.ASK) {
			return this.bestAsk > order.getPrice();
		}
		return false;
	}
	
	private void publishLevelOneQuote() {
		LevelOneQuote quote = new LevelOneQuote(bestBid, bestAsk, lastTradePrice, this.getBestBidQuantity(), 
				this.getBestAskQuantity(), this.lastTradeQuantity, 
				timestamp, timestamp, lastTradeTime, 1, Instant.now().toEpochMilli());
		this.onNext(quote);
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//		df.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
//		String result = df.format(timestamp);
//
//		System.out.println("[" + result + "] " + "Bid Price: " + getBestBidPrice()
//				+ " | Bid Quantity: " + getBestBidQuantity() + " || "
//				+ "Ask Price: " + getBestAskPrice() + " | Ask Quantity: "
//				+ getBestAskQuantity() + "| Last Price " + this.lastTradePrice);
	}
	
	public double getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public Observable<LevelOneQuote> getQuotesObservable() {
		return quotesObservable;
	}
	
	public Subscription subscribe(Observer<LevelOneQuote> observer) {
		return this.quotesObservable.subscribe(observer);
	}
	
	public void unsubscribe(Subscriber<LevelOneQuote> subscriber) {
		this.quotesObserverProcessor.unsubscribe(subscriber);
	}
	
	private void onNext(LevelOneQuote quote) {
		this.quotesObserverProcessor.onNext(quote);
	}
	
	private void onCompleted() {
		this.quotesObserverProcessor.onCompleted();
	}
	
	private void onError(Throwable t) {
		this.quotesObserverProcessor.onError(t);
	}
 }
