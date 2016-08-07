package kaqt.foundation.mktdata;

public interface MktDataSubscriber
{
	void subscribeTopOfBook(String securityId);
	void unsubscribeTopOfBook(String securityId);
	void subscribeDepthOfBook(String securityId, int depth);
	void unsubscribeDepthOfBook(String securityId, int depth);
}
