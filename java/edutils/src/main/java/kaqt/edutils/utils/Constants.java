package kaqt.edutils.utils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Constants {
	public static final List<String> PRICES_TABLE_COLUMNS = Arrays.asList(
			"LEG", "BID VOLUME", "BID PRICE", "ASK PRICE", "ASK VOLUME");
	
	public static final String CURVE_QUOTES_TOPIC = "curve";
	
	public static final List<String> ED_CONTRACTS = Arrays.asList(
			"EDZ5", "EDH6", "EDM6", "EDU6",
           "EDZ6", "EDH7", "EDM7", "EDU7",
           "EDZ7", "EDH8", "EDM8", "EDU8",
           "EDZ8", "EDH9", "EDM9", "EDU9",
           "EDZ9", "EDH0", "EDM0", "EDU0",
           "EDZ0", "EDH1", "EDM1", "EDU1",
           "EDZ1", "EDH2", "EDM2", "EDU2",
           "EDZ2", "EDH3", "EDM3", "EDU3",
           "EDZ3", "EDH4", "EDM4", "EDU4",
           "EDZ4", "EDH5", "EDM5", "EDU5");
	
	public static final ConcurrentHashMap<String, Integer> CONTRACT_ROWS;
	static {
		CONTRACT_ROWS = new ConcurrentHashMap<String, Integer>();
		int rowNum = 0;
		for (String contract : ED_CONTRACTS) {
			CONTRACT_ROWS.put(contract, rowNum);
			++rowNum;
		}
	}
	
	public static final int BID_VOLUME_ROW = 1;
	public static final int BID_PRICE_ROW = 2;
	public static final int ASK_PRICE_ROW = 3;
	public static final int ASK_VOLUME_ROW = 4;
}
