package kaqt.apps.tt.marketdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import kaqt.foundation.providers.quickfix.Fix42Initiator;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import quickfix.ConfigError;

public class App {

	public static void main(String[] args) {
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
		String configFile = null;
		String ttPassword = null;

		CommandLineParser parser = new BasicParser();

		Options options = new Options();
		options.addOption("c", "config", true,
				"Quickfix Config File");
		options.addOption("p", "password", true,
				"TT Password");
		
		CommandLine line = null;

		try {
			line = parser.parse(options, args);

			if (line.hasOption("config") && line.hasOption("password")) {
				configFile = line.getOptionValue("config");
				ttPassword = line.getOptionValue("password");
			} else {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("tt-marketdata", options);
				return; 
			}
		} catch (ParseException exp) {
			System.out.println("Unexpected exception:" + exp.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("tt-marketdata", options);
			return;
		}

		Fix42Initiator initiator = new Fix42Initiator(ttPassword, configFile);
		
		try {
			initiator.start();
		} catch (ConfigError e) {
			e.printStackTrace();
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = "";
		try {
			do {
				input = br.readLine();
			} while (!input.equals("exit"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		initiator.stop();
		System.exit(0);
	}

}
