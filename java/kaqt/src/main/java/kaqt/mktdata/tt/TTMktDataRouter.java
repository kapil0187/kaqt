package kaqt.mktdata.tt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import kaqt.foundation.Router;
import kaqt.foundation.mktdata.MktDataHandler;
import kaqt.foundation.mktdata.Price;
import kaqt.foundation.mktdata.Quote;
import kaqt.providers.protobuf.Symbology;
import kaqt.providers.protobuf.Symbology.FuturesInstrument;
import kaqt.symbology.SymbologyZmqClient;

public class TTMktDataRouter extends Router implements MktDataHandler
{
	private static Logger logger = Logger.getLogger(TTMktDataRouter.class);

	private TTMktDataFixClient mdFixClient;
	private SymbologyZmqClient symbologyClient;
	
	// TO DO: This should be the KAQT Instrument instead of protobuf instrument
	// Hence List<int>. and connect to instrument service to get TT id's
	private Map<String, FuturesInstrument> instruments;

	public TTMktDataRouter(Properties properties)
	{
		super(properties);
	}

	@Override
	public void initialize()
	{
		//=================================================================
		instruments = new HashMap<String, Symbology.FuturesInstrument>();
		String connectAddress = String.format("tcp://%s:%s",
				super.properties.getProperty("SYMBOLOGY_SERVER_BIND_HOST"),
				super.properties.getProperty("SYMBOLOGY_SERVER_BIND_PORT"));

		this.symbologyClient = new SymbologyZmqClient(connectAddress);
		symbologyClient.start();

		logger.info(String.format("CONNECTED SYMBOLOGY CLIENT TO %s",
				connectAddress));
		// TODO: Need to replace this with Instrument repository
		//=================================================================

		List<FuturesInstrument> queryResults = symbologyClient.queryAll();
		queryResults.stream().forEach(i -> instruments.put(i
				.getAlternateId(0).getId(), i));

		logger.info(String.format("LOADED %s INSTRUMENTS FROM INSTDEF SERVER",
				instruments.size()));

		mdFixClient = new TTMktDataFixClient(
				super.properties.getProperty("TTFIX_MKTDATA_CONFIG"),
				this::onQuote, instruments);
		mdFixClient.setUsername(super.properties.getProperty("TTFIX_USERNAME"));
		mdFixClient.setPassword(super.properties.getProperty("TTFIX_PASSWORD"));
		mdFixClient.start();
	}

	@Override
	public void shutdown()
	{
		mdFixClient.stop();
		symbologyClient.stop();
	}

	public void onQuote(Quote quote)
	{
		logger.info("RECEIVED QUOTE");
		logger.info(quote.toString());
		// TODO: Add to disruptor queue
	}

	public void onPrice(Price price)
	{
		// TODO: Add to disruptor queue
	}
}
