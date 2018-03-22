package kaqt.apps.coinbase.marketdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import rx.Observable;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import kaqt.foundation.EventArgs;
import kaqt.foundation.coinbase.EndPointRelatedConstants;
import kaqt.foundation.coinbase.WebsocketClientConfiguration;
import kaqt.marketdata.coinbase.LevelOneQuote;
import kaqt.marketdata.coinbase.OrderBook;
import kaqt.marketdata.coinbase.WebsocketMessageUpdate;
import kaqt.marketdata.coinbase.WebsocketMessageUpdateEventFactory;
import kaqt.marketdata.coinbase.OrderBookUpdatesPublisher;
import kaqt.marketdata.coinbase.WebsocketClient;

public class App {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		WebsocketClientConfiguration config = new WebsocketClientConfiguration(
				EndPointRelatedConstants.API_URL
						+ "/products/LTC-USD/book?level=3",
				"wss://ws-feed.gdax.com",
				"{\"type\":\"subscribe\",\"product_id\":\"LTC-USD\"}",
				"dvtp-disco");

		Executor executor = Executors.newCachedThreadPool();
		
		WebsocketMessageUpdateEventFactory factory = new WebsocketMessageUpdateEventFactory();
		int buffer = 1024;
		Disruptor<EventArgs<WebsocketMessageUpdate>> disruptor = new Disruptor<EventArgs<WebsocketMessageUpdate>>(
				factory, buffer, executor);
		OrderBook book = new OrderBook();
		disruptor.handleEventsWith(new OrderBookUpdatesPublisher(book));
		disruptor.start();
		
		LevelOneQuoteSubscriber observer = new LevelOneQuoteSubscriber();
		book.subscribe(observer);
		
		RingBuffer<EventArgs<WebsocketMessageUpdate>> ringbuffer = disruptor.getRingBuffer();
		final WebsocketClient client = new WebsocketClient(ringbuffer, config);
		client.connect();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = "";
		try {
			do {
				input = br.readLine();
			} while (!input.equals("exit"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		book.unsubscribe(observer);
		disruptor.shutdown();
		client.disconnect();
		System.exit(0);
	}
}
