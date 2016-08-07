package kaqt.foundation.symbology;


public interface SecDefSubscriber
{
	void subscribe(IExchange exchange);
	void subscribe(IExchange exchange, IProduct product);
	void subscribe(IProduct product);
	void subscribe(IExchange exchange, IProduct product, ISecurity security);
}
