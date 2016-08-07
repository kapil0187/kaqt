package kaqt.mktdata.sim;

import kaqt.foundation.mktdata.Quote;

import org.apache.commons.math3.distribution.NormalDistribution;

public class OUSimulator extends QuoteSimulator
{
	private NormalDistribution normalDist;
	private double theta;
	private double mean;
	private double standardDeviation;

	public OUSimulator(double theta, double mu, double sigma)
	{
		super(QuoteSimulatorType.OU);

		this.normalDist = new NormalDistribution(0, 1);
		this.theta = theta;
		this.mean = mu;
		this.standardDeviation = sigma;
	}

	@Override
	public Quote newQuote(Quote oldValue)
	{
		double normalSample = normalDist.sample();
		oldValue.setBidPrice(oldValue.getBidPrice() + theta * (mean - oldValue.getBidPrice()) + normalSample
				* standardDeviation);
		oldValue.setAskPrice(oldValue.getBidPrice() + theta * (mean - oldValue.getAskPrice()) + normalSample
				* standardDeviation);
		oldValue.setLastPrice(oldValue.getAskPrice());
		return oldValue;
	}

}