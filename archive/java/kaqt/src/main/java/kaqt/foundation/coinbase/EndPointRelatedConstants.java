package kaqt.foundation.coinbase;

public class EndPointRelatedConstants {
	public static String WEBSOCKET_URL = "wss://ws-feed.gdax.com";	
	public static String WEBSOCKET_SANDBOX_URL = "wss://ws-feed-public.sandbox.gdax.com";	
	
	public static String WEBSOCKET_SUBSCRIBE_MESSAGE = "{\"type\":\"subscribe\",\"product_id\":\"LTC-USD\"}";
	
	public static String API_URL = "https://api.gdax.com";
	public static String API_SANDBOX_URL = "https://api-public.sandbox.gdax.com";
	
	public static String ORDERS_ENDPOINT = "/orders";
	public static String ACCOUNTS_ENDPOINT = "/accounts";
	public static String ACCOUNT_HISTORY_ENDPOINT_SUFFIX = "/ledger";
	public static String ACCOUNT_HOLDS_ENDPOINT_SUFFIX = "/hols";
	public static String FILLS_ENDPOINT = "/fills";
	public static String PRODUCTS_ENDPOINT = "/products";
	public static String PRODUCTS_STATISTICS_ENDPOINT_SUFFIX = "/stats";
	public static String PRODUCTS_CANDLES_ENDPOINT_SUFFIX = "/candles";	
	
	public static String LEVEL1_BOOK_URL = "https://api.gdax.com/products/BTC-USD/book?level=1";
	public static String LEVEL2_BOOK_URL = "https://api.gdax.com/products/BTC-USD/book?level=2";
	public static String LEVEL3_BOOK_URL = "https://api.gdax.com/products/BTC-USD/book?level=3";
}
