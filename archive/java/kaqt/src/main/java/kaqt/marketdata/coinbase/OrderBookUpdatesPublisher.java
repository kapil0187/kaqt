package kaqt.marketdata.coinbase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;

import kaqt.foundation.EventArgs;
import kaqt.foundation.MicrosecondUtcDateTime;
import kaqt.marketdata.coinbase.OrderBookEntry.OrderType;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lmax.disruptor.EventHandler;

public class OrderBookUpdatesPublisher implements
		EventHandler<EventArgs<WebsocketMessageUpdate>> {

	private OrderBook book = null;
	private SimpleDateFormat sdf; 
	private Integer snapshotSequenceNumber = -1;

	public OrderBookUpdatesPublisher(OrderBook book) {
		super();
		this.book = book;
		this.sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
	}

	@Override
	public void onEvent(EventArgs<WebsocketMessageUpdate> event, long sequence,
			boolean endOfBatch) throws Exception {
		WebsocketMessageUpdate update = event.get();
		int currentSequenceNumber = update.getUpdate().get("sequence")
				.getAsInt();
		if (update.getUpdateType() == OrderBookUpdateTypeEnum.INITIAL_SNAPSHOT) {
			this.snapshotSequenceNumber = currentSequenceNumber;
			this.syncOrderBook(update.getUpdate());
		}

		if (update.getUpdateType() == OrderBookUpdateTypeEnum.BOOK_UPDATE
				&& currentSequenceNumber > snapshotSequenceNumber) {
			this.updateOrderBook(update.getUpdate());
		}
	}

	private void updateOrderBook(JsonObject bookUpdate) {
		book.setSequenceNumber(bookUpdate.get("sequence").getAsInt());
		String messageType = bookUpdate.get("type").getAsString();
		long timestamp = 0;
		try {
			timestamp = (new MicrosecondUtcDateTime(bookUpdate.get("time").getAsString())).getMilliSeconds();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		book.setTimestamp(timestamp);
		
		if (messageType.equals("open") || messageType.equals("change")) {
			double price = bookUpdate.get("price")
					.getAsDouble();
			String id =  bookUpdate.get("order_id")
					.getAsString();
			OrderType type = bookUpdate.get("side")
					.getAsString().equals("buy") ? OrderType.BID
					: OrderType.ASK;
			
			double qty = 0;
			
			if (messageType.equals("open")) {
				qty = bookUpdate.get("remaining_size").getAsDouble();
			} else {
				qty = bookUpdate.get("new_size").getAsDouble();
			}
			
			OrderBookEntry update = new OrderBookEntry(price, qty, id, timestamp, type);
			book.update(update);
		}

		if (messageType.equals("done")) {
			book.remove(bookUpdate.get("order_id")
					.getAsString(), true);
		}
		
		if (messageType.equals("match")) {
			book.match(bookUpdate.get("maker_order_id")
					.getAsString(), bookUpdate.get("taker_order_id")
					.getAsString(), bookUpdate.get("size").getAsDouble());
		}
	}

	private void syncOrderBook(JsonObject update) {
		this.book.setSequenceNumber(snapshotSequenceNumber);
		Gson gson = new Gson();
		OrderBookSnapshot snapshot = gson.fromJson(update, OrderBookSnapshot.class);
		if (snapshot != null) {
			for (List<String> jsonRecord : snapshot.bids) {
				this.book.setTimestamp(Instant.now().toEpochMilli());
				book.add(new OrderBookEntry(jsonRecord,
						OrderBookEntry.OrderType.BID));
			}
			
			for (List<String> jsonRecord : snapshot.asks) {
				this.book.setTimestamp(Instant.now().toEpochMilli());
				book.add(new OrderBookEntry(jsonRecord,
						OrderBookEntry.OrderType.ASK));
			}
			
		} else {
			System.out.println("We are getting null");
		}
	}
}
