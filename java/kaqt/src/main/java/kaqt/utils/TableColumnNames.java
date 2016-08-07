package kaqt.utils;

import java.util.Arrays;
import java.util.List;

public class TableColumnNames
{
	public static List<String> PARAMETER_VALUE_TABLE = Arrays.asList(
			"PARAMETER", "VALUE");

	public static List<String> MARKET_DATA_GRID = Arrays.asList("INSTRUMENT",
			"TIME", "BID VOLUME", "BID PRICE", "BUY", "SELL", "ASK PRICE",
			"ASK VOLUME", "REMOVE");

	public static List<String> SYMBOLOGY = Arrays.asList("KAQT ID", "TICKER",
			"DESCRIPTION", "EXCHANGE");
}
