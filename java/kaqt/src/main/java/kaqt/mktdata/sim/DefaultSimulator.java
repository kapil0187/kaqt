package kaqt.mktdata.sim;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import kaqt.foundation.mktdata.Quote;

public class DefaultSimulator extends QuoteSimulator
{
	private static final double MIN_PRICE_VALUE = 1.0;
	private static final double MAX_PRICE_VALUE = 100.0;
	private static final double TICK_SIZE = 0.01;
	private static final int MIN_SIZE = 1;
	private static final int MAX_SIZE = 10;
	
	private Random random;
	
	public DefaultSimulator()
	{
		super(QuoteSimulatorType.DEFAULT);
		this.random = new Random();
	}

	@Override
	public Quote newQuote(Quote oldValue)
	{
		Quote quote = new Quote();
		quote.setBidPrice((new BigDecimal(MIN_PRICE_VALUE + random.nextDouble()*(MAX_PRICE_VALUE - MIN_PRICE_VALUE)).setScale(2, RoundingMode.FLOOR).doubleValue()));
		quote.setAskPrice(quote.getBidPrice() + random.nextInt(2)*TICK_SIZE + TICK_SIZE );
		quote.setLastPrice(random.nextBoolean() == true ? quote.getBidPrice() : quote.getAskPrice());
		quote.setBidQty(MIN_SIZE + random.nextInt(MAX_SIZE - MIN_SIZE));
		quote.setAskQty(MIN_SIZE + random.nextInt(MAX_SIZE - MIN_SIZE));
		quote.setLastQty(quote.getBidQty() < quote.getAskQty() ? quote.getBidQty() : quote.getAskQty());
		quote.setPosixTime(System.currentTimeMillis());		
		return quote;
	}

}
