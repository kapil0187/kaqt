package kaqt.symbology;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import kaqt.foundation.interfaces.ISymbologyRepository;
import kaqt.providers.protobuf.Symbology.FuturesInstrument;

public class SymbologyStore implements ISymbologyRepository, AutoCloseable
{
	;
	private static Logger logger = Logger.getLogger(SymbologyStore.class);

	private static SymbologyStore instance = null;
	private static List<FuturesInstrument> futuresInstruments = new ArrayList<FuturesInstrument>();
	private static ConcurrentMap<String, SymbologyZmqClient> clients = new ConcurrentHashMap<String, SymbologyZmqClient>();

	protected SymbologyStore()
	{
	}

	// Lazy Initialization
	public static SymbologyStore getStore(String connectAddress)
	{
		if (!clients.containsKey(connectAddress))
		{
			clients.put(connectAddress, new SymbologyZmqClient(connectAddress));
			clients.get(connectAddress).start();
			futuresInstruments.addAll(clients.get(connectAddress).queryAll());
		}
		
		if (instance == null)
		{
			synchronized (SymbologyStore.class)
			{
				if (instance == null)
				{
					instance = new SymbologyStore();
				}
			}
		}
		
		return instance;
	}

	@Override
	public List<FuturesInstrument> getAllDefinitions()
	{
		return futuresInstruments;
	}

	@Override
	public void close() throws Exception
	{
		clients.values().stream().forEach(c -> c.stop());
		instance = null;
	}
}
