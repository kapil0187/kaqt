package kaqt.mktdata.sim;

import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.zeromq.ZMQ.Context;

import kaqt.foundation.mktdata.ZmqMktDataPublisher;
import kaqt.foundation.mktdata.Quote;
import kaqt.providers.protobuf.LeveloneQuote.LevelOneQuote;
import kaqt.providers.protobuf.Symbology.FuturesInstrument;
import kaqt.symbology.SymbologyStore;
import kaqt.utils.ProtoBufHelper;

public class SimMktDataPublisher extends ZmqMktDataPublisher
{
	private static Logger logger = Logger.getLogger(SimMktDataPublisher.class);
	
	private static Quote STARTING_VALUE = new Quote(0, 99.0, 99.10, 99.0, 100,
			110, 10, 0);

	private List<FuturesInstrument> instruments;
	private QuoteSimulator simulator;
	private Random randomGenerator;
	private boolean isTerminated = false;

	public SimMktDataPublisher(Context context, String bindAddress,
			SymbologyStore store)
	{
		super(context, bindAddress, store);
		this.instruments = store.getAllDefinitions();
		this.simulator = new DefaultSimulator();
		this.randomGenerator = new Random();
	}
	
	@Override
	public void process()
	{
		FuturesInstrument randomInstrument = null;
		Quote q = simulator.newQuote(STARTING_VALUE);;
		while (!isTerminated)
		{
			randomInstrument = this.getRandomInstrument();
			q = simulator.newQuote(q);
			q.setId(randomInstrument.getId());
			LevelOneQuote newQuote = ProtoBufHelper
					.createLevelOneQuote(q);
			super.send(randomInstrument.getTicker(), newQuote);
			logger.info(String.format("SENDING NEW QUOTE FOR INSTRUMENT ID = %s and TOPIC = %s", 
					newQuote.getInstrumentId(), randomInstrument.getTicker()));
			System.out.println(newQuote);
			
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void start()
	{
		super.start();
	}

	@Override
	public void stop()
	{
		this.isTerminated = true;
		super.stop();
	}

	private FuturesInstrument getRandomInstrument()
	{
		return instruments.get(randomGenerator.nextInt(instruments.size()));
	}
}
