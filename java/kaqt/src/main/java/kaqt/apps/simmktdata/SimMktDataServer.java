package kaqt.apps.simmktdata;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import kaqt.mktdata.sim.SimMktDataRouter;

import org.apache.log4j.Logger;

public class SimMktDataServer
{
	private static Logger logger = Logger.getLogger(SimMktDataServer.class);
	
	public static void main(String[] args)
	{
		Properties properties = new Properties();
		InputStream input = null;

		SimMktDataRouter router = null;

		try
		{
			input = new FileInputStream("config.properties");
		}
		catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}

		if (input != null)
		{
			try
			{
				properties.load(input);
				logger.info(String.format("LOADED %s PROPERTIES", properties.size()));
				router = new SimMktDataRouter(properties);
				router.initialize();

				logger.info("SIMULATED MARKET DATA ROUTER STARTED");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String exitInput = "";
		try
		{
			do
			{
				exitInput = br.readLine();
			}
			while (!exitInput.equals("q"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		logger.info("RECEIVED EXIT SIGNAL");

		router.shutdown();

		logger.info("ROUTER SHUTDOWN and EXITING");

		
	}
}
