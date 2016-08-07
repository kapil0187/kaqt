package kaqt.foundation.mktdata;


public interface MktDataHandler
{
	void onQuote(Quote quote);
	void onPrice(Price price);
}
