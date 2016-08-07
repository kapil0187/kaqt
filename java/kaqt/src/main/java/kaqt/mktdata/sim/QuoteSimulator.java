package kaqt.mktdata.sim;

import kaqt.foundation.mktdata.Quote;

public abstract class QuoteSimulator
{
	private QuoteSimulatorType type;
	
	public QuoteSimulator(QuoteSimulatorType type)
	{
		this.type = type;
	}
	
	public QuoteSimulatorType getType()
	{
		return type;
	}

	public abstract Quote newQuote(Quote oldValue);
	
	public static enum QuoteSimulatorType
	{
		OU,
		GBM,
		DEFAULT
	}
}
