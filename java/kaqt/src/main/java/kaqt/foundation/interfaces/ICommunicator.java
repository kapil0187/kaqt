package kaqt.foundation.interfaces;

import kaqt.foundation.mktdata.Quote;

public interface ICommunicator
{
	public void start();
	
	public void stop();
	
	public void handleQuote(Quote quote);
}
