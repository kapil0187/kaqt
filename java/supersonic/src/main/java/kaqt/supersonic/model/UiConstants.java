package kaqt.supersonic.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UiConstants {
	public static Map<MarketDataType, ArrayList<String>> MDViewTableHeaders = new HashMap<MarketDataType, ArrayList<String>>() {
		{
		put(MarketDataType.OHLC, new ArrayList<String>(Arrays.asList("SYMBOL","TIME","OPEN", "HIGH", "LOW", "CLOSE")));
		put(MarketDataType.QUOTE, new ArrayList<String>(Arrays.asList("SYMBOL","TIME", "BID SIZE", "BID", "ASK", "ASK SIZE")));
		put(MarketDataType.TRADE, new ArrayList<String>(Arrays.asList("SYMBOL","TIME","PRICE", "SIZE")));		
		}
	};
	
	public static ArrayList<String> COMPLETED_ORDERS_COLUMN_HEADERS = new ArrayList<String>(
			Arrays.asList("CLIENT ID", "ORDER ID", "SYMBOL", "TIME", "TYPE", "SIDE", "PRICE", "QUANTITY", "STATUS"));

	public static ArrayList<String> FILLS_COLUMN_HEADERS = new ArrayList<String>(
			Arrays.asList("CLIENT ID", "ORDER ID", "SYMBOL", "TIME" ,"FILL QTY", "FILL PRICE", "TYPE"));
	
	public static ArrayList<String> OPEN_ORDERS_COLUMN_HEADERS = new ArrayList<String>(
			Arrays.asList("CLIENT ID", "ORDER ID", "SYMBOL", "TIME", "SIDE", "PRICE", "QUANTITY", "STATUS"));
	
	public static ArrayList<String> INSTRUMENT_SEARCH_HEADERS = new ArrayList<String>(Arrays.asList("ID", "SYMBOL",
			"EXCHANGE", "DESCRIPTION", "EXPIRY"));

}
