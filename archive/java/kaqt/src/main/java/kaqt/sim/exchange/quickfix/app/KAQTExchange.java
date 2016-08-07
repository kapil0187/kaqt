package kaqt.sim.exchange.quickfix.app;

import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import kaqt.foundation.EventArgs;
import kaqt.foundation.providers.quickfix.QuickFixMessageEvent;
import kaqt.foundation.providers.quickfix.QuickFixMessageEventFactory;
import kaqt.sim.exchange.MatchingEngine;
import kaqt.sim.exchange.OrderRequestEvent;
import kaqt.sim.exchange.OrderRequestEventFactory;
import kaqt.sim.exchange.quickfix.QuickFixExchangeServer;
import kaqt.sim.exchange.quickfix.QuickFixOrderRequestRouter;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import quickfix.Message;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class KAQTExchange {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		String versionInfo = "KAQT Simulated Exchange and Matching Engine\nVersion 1.001";
		
		HelpFormatter help = new HelpFormatter();
		
		String configFile = null;
		
		CommandLineParser parser = new BasicParser();
		
		Options options = new Options();
		options.addOption("f", "fix-config", true, "Quickfix config file");
		options.addOption("v", "version", false, "Version info");
		options.addOption("h", "help", false, "Help");
		
		String header = "Provide a quickfix config file to start server";
		String footer = "------------------------------------------------";
		
		if (args.length == 0) {
			help.printHelp("kaqtexchange", header, options, footer, true);
			return;
		}

		CommandLine line = null;
		try {
			line = parser.parse(options, args);
		} catch (org.apache.commons.cli.ParseException e) {
			help.printHelp("kaqtexchange", header, options, footer, true);
			return;
		}
		if (line.hasOption("fix-config") || line.hasOption('f')) {
			configFile = line.getOptionValue("fix-config");
			System.out.println("Getting config file value = " + configFile);
		}
		if (line.hasOption("help") || line.hasOption('h')) {
			help.printHelp("kaqtexchange", header, options, footer, true);
			return;
		}
		if (line.hasOption("version") || line.hasOption('v')) {
			System.out.println(versionInfo);
			return;
		}
		
		Executor executor = Executors.newCachedThreadPool();
		
		QuickFixMessageEventFactory qfMsgFactory = new QuickFixMessageEventFactory();
		OrderRequestEventFactory orFactory = new OrderRequestEventFactory();
		
		int bufferSize = 1024;
		
		Disruptor<OrderRequestEvent> orderRequestDisruptor = 
				new Disruptor<OrderRequestEvent>(orFactory, bufferSize, executor);
		orderRequestDisruptor.handleEventsWith(new MatchingEngine());
		orderRequestDisruptor.start();
		RingBuffer<OrderRequestEvent> orRingBuffer = orderRequestDisruptor.getRingBuffer();
			
		Disruptor<EventArgs<Message>> fixDisruptor = 
				new Disruptor<EventArgs<Message>>(qfMsgFactory, bufferSize, executor);
		fixDisruptor.handleEventsWith(new QuickFixOrderRequestRouter(orRingBuffer));
		fixDisruptor.start();
		RingBuffer<EventArgs<Message>> fixRingbuffer = fixDisruptor.getRingBuffer();
		QuickFixExchangeServer server = new QuickFixExchangeServer(configFile, fixRingbuffer);
		
		server.start();
		
		Scanner reader = new Scanner(System.in);
		while (true) {
			System.out.println("Please press 1 to exit");
			int input = reader.nextInt();
			if (input == 1) {
				break;
			}
		}
		reader.close();
		
		server.stop();
		System.exit(0);
	}
}

