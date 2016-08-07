package kaqt.apps.coinbase.orderex;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import kaqt.foundation.coinbase.EndPointRelatedConstants;
import kaqt.foundation.coinbase.RestClientConfiguration;
import kaqt.orderex.coinbase.Account;
import kaqt.orderex.coinbase.AuthenticatedRestClient;
import kaqt.orderex.coinbase.Fill;
import kaqt.orderex.coinbase.NewOrderRequest;
import kaqt.orderex.coinbase.NewOrderResponse;
import kaqt.orderex.coinbase.OpenOrder;

public class App {
	public static void main(String[] args) {
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
		String url = null;
		String apiKey = null;
		String apiKeySecret = null;
		String passphrase = null;

		CommandLineParser parser = new BasicParser();

		Options options = new Options();
		options.addOption("e", "environment", true,
				"Coinbase Trading Environment - Sandbox / Production");
		options.addOption("k", "key", true, "Coinbase Api Key");
		options.addOption("s", "secret", true, "Coinbase Api Key Secret");
		options.addOption("p", "passphrase", true,
				"Coinbase Api Key Passphrase");

		CommandLine line = null;

		try {
			line = parser.parse(options, args);

			if (line.hasOption("environment") && line.hasOption("key") && line.hasOption("secret")
					&& line.hasOption("passphrase")) {
				url = line.getOptionValue("environment").equals("Sandbox") ? EndPointRelatedConstants.API_SANDBOX_URL
						: EndPointRelatedConstants.API_URL;
				apiKey = line.getOptionValue("key");
				apiKeySecret = line.getOptionValue("secret");
				passphrase = line.getOptionValue("passphrase");
			} else {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("coinbase-orderex", options);
				return; 
			}
		} catch (ParseException exp) {
			System.out.println("Unexpected exception:" + exp.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("coinbase-orderex", options);
			return;
		}

		RestClientConfiguration config = new RestClientConfiguration(apiKey,
				apiKeySecret, passphrase);

		AuthenticatedRestClient client = new AuthenticatedRestClient(config,
				url);

		System.out.println("Accounts :- ");

		Account[] accounts = null;

		try {
			accounts = client.getAccounts();
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}

		for (Account account : accounts) {
			System.out.println("[" + account.getId() + ", "
					+ account.getCurrency() + ", " + account.getAvailable()
					+ ", " + account.getBalance() + ", " + account.getHold()
					+ "]");
		}

		System.out.println("Open Orders :- ");

		OpenOrder[] openOrders = null;

		try {
			openOrders = client.getOpenOrders();
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}

		for (OpenOrder openOrder : openOrders) {
			System.out.println("[" + openOrder.getId() + ", "
					+ openOrder.getCreated_at() + ", "
					+ openOrder.getProduct_id() + ", " + openOrder.getSide()
					+ ", " + openOrder.getSide() + ", " + openOrder.getStatus()
					+ ", " + openOrder.getFill_fees() + ", "
					+ openOrder.getFilled_size() + ", " + openOrder.getPrice()
					+ ", " + openOrder.getSize());
		}

		NewOrderRequest order = new NewOrderRequest(new BigDecimal(200.00),0.01,"buy","BTC-USD");

		try {
			NewOrderResponse response = client
					.sendNewOrderRequest(order);
			System.out.println(response);
		} catch (GeneralSecurityException | IOException e1) {
			e1.printStackTrace();
		}
	}
}
