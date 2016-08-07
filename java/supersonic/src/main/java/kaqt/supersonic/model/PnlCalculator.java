package kaqt.supersonic.model;

public class PnlCalculator {
	private double numBuys = 0;
	private double numSells = 0;
	private volatile double avgBuyPrice = 0;
	private volatile double avgSellPrice = 0;
	
	public void onNext(Fill fill, String side) {

		if (side.equals("buy")) {
			System.out.println(avgBuyPrice + ";" + fill.getPrice() + ";" + fill.getType());
			avgBuyPrice = (avgBuyPrice * numBuys + fill.getPrice())/(numBuys + 1);
			numBuys += 1;
		} else if (side.equals("sell")) {
			avgSellPrice = (avgSellPrice * numSells + fill.getPrice())/(numSells + 1);
			numSells += 1;
		}
	}

	public double getPnl() {
		return Math.min(numBuys, numSells)*(avgBuyPrice - avgSellPrice);
	}
}
