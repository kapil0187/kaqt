package kaqt.apps.instdef;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import kaqt.apps.ttmktdata.TTMktDataServer;
import kaqt.mktdata.tt.TTMktDataRouter;
import kaqt.symbology.SymbologyZmqServer;

public class InstDefServer
{
	public static void main(String[] args)
	{
		//PropertyConfigurator.configure("log4j.properties");

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

		SymbologyZmqServer server = new SymbologyZmqServer("SpringConfig.xml",
				Integer.parseInt(properties
						.getProperty("SYMBOLOGY_SERVER_BIND_PORT")));

		server.start();

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

		server.stop();
	}
}
