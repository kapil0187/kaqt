package kaqt.utils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class TableRowNames
{
	// ORDER TICKET FIELDS
	public static BiMap<Integer, String> ORDER_TICKET_ROWS = HashBiMap.create();
	static {
		ORDER_TICKET_ROWS.put(0, "EXECUTION");
		ORDER_TICKET_ROWS.put(1, "INSTRUMENT");
		ORDER_TICKET_ROWS.put(2, "ORDER TYPE");
		ORDER_TICKET_ROWS.put(3, "SIDE");
		ORDER_TICKET_ROWS.put(4, "PRICE");
		ORDER_TICKET_ROWS.put(5, "QUANTITY");
	}
	
}
