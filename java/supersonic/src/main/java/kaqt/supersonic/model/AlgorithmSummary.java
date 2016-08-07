package kaqt.supersonic.model;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import kaqt.supersonic.ui.Settings;

public class AlgorithmSummary {
	
	private static String INITIAL_EQUITY = "INITIAL EQUITY";
	private static String ENDING_EQUITY = "ENDING EQUITY";
	private static String TOTAL_TRADES = "TOTAL TRADES";
	private static String TOTAL_TRADES_LONG = "TOTAL LONG TRADES";	
	private static String TOTAL_TRADES_SHORT = "TOTAL SHORT TRADES";		
	private static String LONG_PROFIT_GROSS = "GROSS LONG PROFIT";
	private static String SHORT_PROFIT_GROSS = "GROSS SHORT PROFIT";
	private static String TOTAL_PROFIT = "TOTAL PROFIT";
	private static String ANNUAL_RETURN = "ANNUAL RETURN";
	private static String TRANSACTION_COSTS = "TRANSACTION COSTS";
	private static String TRANSACTION_COSTS_LONG = "TRANSACTION COSTS - LONGS";
	private static String TRANSACTION_COSTS_SHORT = "TRANSACTION COSTS - SHORTS";
	private static String AVERAGE_PROFIT_LOSS = "AVERAGE PROFIT/LOSS";
	private static String TOTAL_WINNERS = "TOTAL WINNERS";
	private static String WINNERS_PERCENTAGE = "WINNERS PERCENTAGE";
	private static String TOTAL_WINNERS_LONG = "TOTAL LONG WINNERS";	
	private static String TOTAL_WINNERS_SHORT = "TOTAL SHORT WINNERS";	
	
	private Map<String, BigDecimal> metrics;
	
	public AlgorithmSummary() {
		metrics = new LinkedHashMap<String, BigDecimal>();
		this.initialize();
	}
	
	public void initialize() {
		metrics.put(INITIAL_EQUITY, new BigDecimal(Settings.INITIAL_EQUITY));
		metrics.put(ENDING_EQUITY, new BigDecimal(0));
		metrics.put(TOTAL_TRADES, new BigDecimal(0));
		metrics.put(TOTAL_TRADES_LONG, new BigDecimal(0));
		metrics.put(TOTAL_TRADES_SHORT, new BigDecimal(0));
		metrics.put(LONG_PROFIT_GROSS, new BigDecimal(0));
		metrics.put(SHORT_PROFIT_GROSS, new BigDecimal(0));
		metrics.put(TOTAL_PROFIT, new BigDecimal(0));
		metrics.put(ANNUAL_RETURN, new BigDecimal(0));
		metrics.put(TRANSACTION_COSTS, new BigDecimal(0));
		metrics.put(TRANSACTION_COSTS_LONG, new BigDecimal(0));
		metrics.put(TRANSACTION_COSTS_SHORT, new BigDecimal(0));
		metrics.put(AVERAGE_PROFIT_LOSS, new BigDecimal(0));
		metrics.put(TOTAL_WINNERS, new BigDecimal(0));
		metrics.put(WINNERS_PERCENTAGE, new BigDecimal(0));		
		metrics.put(TOTAL_WINNERS_LONG, new BigDecimal(0));
		metrics.put(TOTAL_WINNERS_SHORT, new BigDecimal(0));
	}

	public Map<String, BigDecimal> getMetrics() {
		return metrics;
	}
}
