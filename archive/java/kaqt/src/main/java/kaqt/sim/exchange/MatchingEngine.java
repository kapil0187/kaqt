package kaqt.sim.exchange;

import com.googlecode.cqengine.CQEngine;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.unique.UniqueIndex;
import com.lmax.disruptor.EventHandler;

public class MatchingEngine implements EventHandler<OrderRequestEvent> {
	
	IndexedCollection<Order> orderBook;
	
	public MatchingEngine() {
		orderBook = CQEngine.newInstance();
		orderBook.addIndex(UniqueIndex.onAttribute(Order.ORDER_ID));
		orderBook.addIndex(NavigableIndex.onAttribute(Order.PRICE));
		orderBook.addIndex(HashIndex.onAttribute(Order.SIDE));
		orderBook.addIndex(HashIndex.onAttribute(Order.INSTRUMENT_ID));		
	}
	
	private void handleNewOrderSingle(IOrderRequest nos) {
		Double key = nos.getPrice().Value;
	}
	
	private void handleOrderCancelRequest(IOrderRequest ocr) {
		
	}
	
	private void handleOrderCancelReplaceRequest(IOrderRequest ocrr) {
		
	}
	
	public void onEvent(OrderRequestEvent event, long sequence, boolean endOfBatch)
			throws Exception {
		IOrderRequest toAdd = event.get();
		OrderRequestType type = toAdd.getRequestType();
		if (type == OrderRequestType.NEWORDERSINGLE) {
			handleNewOrderSingle(toAdd);
		} else if (type == OrderRequestType.CANCEL) {
			handleOrderCancelRequest(toAdd);
		} else if (type == OrderRequestType.CANCELREPLACE) {
			handleOrderCancelReplaceRequest(toAdd);
		}

	}

	public void updateOrderBook() {
	}
	
}
