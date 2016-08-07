package kaqt.apps.ttsecdef;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import kaqt.apps.ttmktdata.TTMktDataServer;
import kaqt.mktdata.tt.TTMktDataRouter;
import kaqt.symbology.TTSecDefFixClient;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class TTSecDef
{
	private static Logger logger = Logger.getLogger(TTMktDataServer.class);

	public static void main(String[] args)
	{
		PropertyConfigurator.configure("log4j.properties");

		logger.info("LOADING CONFIGURATION AND INITIALIZING PROPERTIES");

		Properties properties = new Properties();
		InputStream input = null;

		try
		{
			input = new FileInputStream("config.properties");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		TTSecDefFixClient secDefFixClient = null;
		
		if (input != null)
		{
			try
			{
				properties.load(input);
				secDefFixClient = new TTSecDefFixClient(properties.getProperty("TTFIX_MKTDATA_CONFIG"));
				secDefFixClient.setPassword(properties.getProperty("TTFIX_PASSWORD"));
				secDefFixClient.start();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		logger.info("STARTED SECURITY DEFINITION CLIENT");
		
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

		secDefFixClient.stop();

		logger.info("STOPPED SECURITY DEF CLIENT");
	}

}
