package kaqt.mktdata.sim;

import kaqt.foundation.mktdata.Quote;

import org.apache.commons.math3.analysis.function.Exp;
import org.apache.commons.math3.distribution.NormalDistribution;

public class GBMSimulator extends QuoteSimulator
{
	private NormalDistribution normalDist;
	private Exp exponentialFunc;
	private double drift;
	private double volatility;

	public GBMSimulator(double mean, double sd)
	{
		super(QuoteSimulatorType.GBM);

		this.normalDist = new NormalDistribution(0, 1);
		this.drift = mean - ((sd * sd) / 2);
		this.volatility = sd;
		this.exponentialFunc = new Exp();
	}

	@Override
	public Quote newQuote(Quote oldValue)
	{
		double normalSample = normalDist.sample();
		oldValue.setBidPrice(oldValue.getBidPrice()*exponentialFunc.value(drift + volatility*normalSample));
		oldValue.setAskPrice(oldValue.getAskPrice()*exponentialFunc.value(drift + volatility*normalSample));
		oldValue.setAskPrice(oldValue.getLastPrice()*exponentialFunc.value(drift + volatility*normalSample));
		return oldValue;
	}
	
}
