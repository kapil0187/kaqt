package kaqt.mktdata.sim;

import java.util.Properties;

import org.zeromq.ZMQ.Context;

import kaqt.foundation.Router;
import kaqt.foundation.connections.ContextSingleton;
import kaqt.symbology.SymbologyStore;

public class SimMktDataRouter extends Router
{
	private SimMktDataPublisher publisher;
	private Thread publisherThread;
	private Context context;
	
	public SimMktDataRouter(Properties properties)
	{
		super(properties);
	}

	@Override
	public void initialize()
	{
		this.context = ContextSingleton.getInstance();
		String connectAddress = String.format(
				"tcp://%s:%s",
				properties.getProperty("SYMBOLOGY_SERVER_BIND_HOST"),
				properties.getProperty("SYMBOLOGY_SERVER_BIND_PORT"));
		
		SymbologyStore store = SymbologyStore.getStore(connectAddress); 
		
		String bindAddress = String.format(
				"tcp://*:%s",
				properties.getProperty("SIM_MKTDATA_BIND_PORT"));
		
		this.publisher = new SimMktDataPublisher(this.context, bindAddress, store);
		this.publisherThread = new Thread(publisher::start);
		this.publisherThread.start();
	}

	@Override
	public void shutdown()
	{
		this.publisher.stop();
		try
		{
			this.publisherThread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
