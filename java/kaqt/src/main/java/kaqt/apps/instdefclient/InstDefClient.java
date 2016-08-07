package kaqt.apps.instdefclient;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import kaqt.providers.protobuf.Symbology.FuturesInstrument;
import kaqt.symbology.SymbologyStore;
import kaqt.symbology.SymbologyZmqClient;
import kaqt.symbology.SymbologyZmqServer;

public class InstDefClient
{
	private static Logger logger = Logger.getLogger(InstDefClient.class);
	
	public static void main(String[] args)
	{	
		Properties properties = new Properties();
		InputStream input = null;
		
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
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		String connectAddress = String.format(
				"tcp://%s:%s",
				properties.getProperty("SYMBOLOGY_SERVER_BIND_HOST"),
				properties.getProperty("SYMBOLOGY_SERVER_BIND_PORT"));
		
		SymbologyStore store = SymbologyStore.getStore(connectAddress); 
		
		List<FuturesInstrument> instruments = store.getAllDefinitions();
				
		instruments.stream().forEach(System.out::println);
		
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

		try
		{
			store.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
