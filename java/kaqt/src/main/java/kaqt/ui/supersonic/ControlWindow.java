package kaqt.ui.supersonic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;

import kaqt.foundation.connections.ContextSingleton;
import kaqt.foundation.interfaces.ICommunicator;
import kaqt.foundation.mktdata.Quote;
import kaqt.foundation.mktdata.ZmqMktDataPublisher;
import kaqt.foundation.mktdata.MktDataSubscriber;
import kaqt.foundation.mktdata.ZmqMktDataSubscriber;
import kaqt.foundation.uielements.ImageButton;
import kaqt.providers.protobuf.LeveloneQuote.LevelOneQuote;
import kaqt.providers.protobuf.Symbology;
import kaqt.providers.protobuf.Symbology.FuturesInstrument;
import kaqt.symbology.SymbologyZmqClient;
import net.miginfocom.swing.MigLayout;

public class ControlWindow extends JFrame implements ICommunicator
{
	final static Logger logger = Logger.getLogger(ControlWindow.class);

	private static final long serialVersionUID = 1L;

	private Properties properties;

	private JPanel contentPane;

	// COMMUNICATION
	private ZmqMktDataSubscriber subscriber;
	private Thread subscriberThread;
	private Context context;

	// WINDOWS
	private MarketDataView mdView;
	private boolean mdViewInit = false;

	private AuditTrailView auditTrailView;
	private boolean auditTrailInit = false;

	private Thread symbologyClientThread;
	private SymbologyView symbologyView;
	private boolean symbologyViewInit = false;
	private Map<Integer, FuturesInstrument> instruments;
	private SymbologyZmqClient symbologyClient;

	// ICONS
	private static String mdGridViewIconPath = "/mdview.png";
	private static String mdGridIconPath = "/mdgrid.gif";
	private static String mdMatrixIconPath = "/mdmatrix.png";
	private static String symbologyIconPath = "/symbology.png";
	private static String strategyIconPath = "/strategy.png";
	private static String backtestingIconPath = "/backtesting.png";
	private static String curveIconPath = "/curves.png";
	private static String logIconPath = "/log.png";
	private static String settingsIconPath = "/settings.png";
	private static String orderIconPath = "/order.png";
	private static String openOrdersIconPath = "/open_orders.png";
	private static String fillsIconPath = "/fills.png";

	// DATA
	private ConcurrentMap<Integer, Quote> latestQuotes;
	
	public ControlWindow(Properties properties)
	{
		this.properties = properties;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 125);
		this.setResizable(false);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnSymbology = new JMenu("SYMBOLOGY");
		menuBar.add(mnSymbology);

		JMenuItem mntmDatabase = new JMenuItem("DATABASE");
		mnSymbology.add(mntmDatabase);

		JMenu mnMarket = new JMenu("MARKET");
		menuBar.add(mnMarket);

		JMenuItem mntmMdview = new JMenuItem("MDView");
		mnMarket.add(mntmMdview);

		JMenuItem mntmMdgrid = new JMenuItem("MDGrid");
		mnMarket.add(mntmMdgrid);

		JMenuItem mntmMdmatrix = new JMenuItem("MDMatrix");
		mnMarket.add(mntmMdmatrix);

		JMenu mnExecution = new JMenu("EXECUTION");
		menuBar.add(mnExecution);

		JMenuItem mntmWorkingOrders = new JMenuItem("WORKING ORDERS");
		mnExecution.add(mntmWorkingOrders);

		JMenuItem mntmCompletedOrders = new JMenuItem("COMPLETED ORDERS");
		mnExecution.add(mntmCompletedOrders);

		JMenuItem mntmFills = new JMenuItem("FILLS");
		mnExecution.add(mntmFills);

		JMenu mnStrategy = new JMenu("STRATEGY");
		menuBar.add(mnStrategy);

		JMenuItem mntmStrategyRepo = new JMenuItem("STRATEGY REPO");
		mnStrategy.add(mntmStrategyRepo);

		JMenuItem mntmNew = new JMenuItem("NEW");
		mnStrategy.add(mntmNew);

		JMenu mnSettings = new JMenu("SETTINGS");
		menuBar.add(mnSettings);

		JMenuItem mntmConnections = new JMenuItem("CONNECTIONS");
		mnSettings.add(mntmConnections);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][]", "[20px,fill]"));

		JButton symbologyButton = new ImageButton(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getResource(symbologyIconPath)), 30,
				30);
		contentPane.add(symbologyButton, "cell 0 0");
		symbologyButton.setToolTipText("Symbology");
		symbologyButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				initializeSymbologyDatabaseWindow();
			}
		});

		JButton mdviewButton = new ImageButton(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getResource(mdGridViewIconPath)), 30,
				30);
		contentPane.add(mdviewButton, "cell 1 0");
		mdviewButton.setToolTipText("Market Data View");
		mdviewButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				initializeMarketDataWindow();
			}
		});

		JButton mdgridButton = new ImageButton(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getResource(mdGridIconPath)), 30, 30);
		contentPane.add(mdgridButton, "cell 2 0");
		mdgridButton.setToolTipText("Market Data Grid View");
		mdgridButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			}
		});

		JButton mdmatrixButton = new ImageButton(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getResource(mdMatrixIconPath)), 30,
				30);
		mdmatrixButton.setToolTipText("Market Data Matrix View");
		mdmatrixButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			}
		});
		contentPane.add(mdmatrixButton, "cell 3 0");

		JButton orderButton = new ImageButton(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getResource(orderIconPath)), 30, 30);
		orderButton.setToolTipText("New Order");
		orderButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			}
		});
		contentPane.add(orderButton, "cell 4 0");

		JButton openOrdersButton = new ImageButton(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getResource(openOrdersIconPath)), 30,
				30);
		openOrdersButton.setToolTipText("Open Orders");
		openOrdersButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			}
		});
		contentPane.add(openOrdersButton, "cell 5 0");

		JButton fillsButton = new ImageButton(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getResource(fillsIconPath)), 30, 30);
		fillsButton.setToolTipText("Fills");
		fillsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			}
		});
		contentPane.add(fillsButton, "cell 5 0");

		JButton strategyButton = new ImageButton(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getResource(strategyIconPath)), 30,
				30);
		strategyButton.setToolTipText("AlgoGator");
		strategyButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			}
		});
		contentPane.add(strategyButton, "cell 7 0");

		JButton backtestingButton = new ImageButton(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getResource(backtestingIconPath)),
				30, 30);
		backtestingButton.setToolTipText("Backtesting");
		backtestingButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			}
		});
		contentPane.add(backtestingButton, "cell 8 0");

		JButton curveAnalysisButton = new ImageButton(Toolkit
				.getDefaultToolkit().getImage(
						this.getClass().getResource(curveIconPath)), 30, 30);
		curveAnalysisButton.setToolTipText("Curve Analysis");
		curveAnalysisButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			}
		});
		contentPane.add(curveAnalysisButton, "cell 9 0");

		JButton logButton = new ImageButton(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getResource(logIconPath)), 30, 30);
		logButton.setToolTipText("Audit Trail");
		logButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				initializeLogWindow();
			}
		});
		contentPane.add(logButton, "cell 10 0");

		JButton settingsButton = new ImageButton(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getResource(settingsIconPath)), 30,
				30);
		settingsButton.setToolTipText("Settings");
		settingsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			}
		});
		contentPane.add(settingsButton, "cell 11 0");

		this.auditTrailView = new AuditTrailView();

		this.symbologyView = new SymbologyView(this);

		this.initializeData();
		
		this.start();
	}

	private void initializeData()
	{
		latestQuotes = new ConcurrentHashMap<Integer, Quote>();
	}
	
	private void initializeMarketDataWindow()
	{
		if (mdViewInit == false)
		{
			mdView = new MarketDataView();
			mdViewInit = true;
		}
		mdView.setVisible(true);
	}

	private void initializeSymbologyDatabaseWindow()
	{
		if (symbologyViewInit == false)
		{
			this.symbologyView.populateInstrumentsTable(this.instruments);
			symbologyViewInit = true;
		}

		symbologyView.setVisible(true);
	}

	private void initializeLogWindow()
	{
		if (!auditTrailInit)
		{
			auditTrailInit = true;
		}

		auditTrailView.setVisible(true);
	}

	// ICommunicatoe Members
	@Override
	public void start()
	{
		instruments = new HashMap<Integer, Symbology.FuturesInstrument>();
		String connectAddress = String.format("tcp://%s:%s",
				this.properties.getProperty("SYMBOLOGY_SERVER_BIND_HOST"),
				this.properties.getProperty("SYMBOLOGY_SERVER_BIND_PORT"));

		this.symbologyClient = new SymbologyZmqClient(connectAddress);
		symbologyClient.start();
		
//		symbologyClientThread = new Thread(this::queryInstDef);
//		symbologyClientThread.start();

		this.queryInstDef();
		
		logger.info("STARTED INSTDEF CLIENT");
		
		this.subscriberThread = new Thread(this::processMktData);
		this.subscriberThread.start();

		logger.info("STARTED MARKETDATA SUBSCRIBER");
		
		logger.info("SUBSCRIBED TO MKTDATA");
	}

	private void processMktData()
	{
		this.context = ContextSingleton.getInstance();
		String connectAddress = String.format("tcp://%s:%s",
				properties.getProperty("SIM_MKTDATA_BIND_HOST"),
				properties.getProperty("SIM_MKTDATA_BIND_PORT"));
		this.subscriber = new ZmqMktDataSubscriber(context, connectAddress,
				this::handleQuote);
		this.subscriber.connect();
		
//		instruments.values().stream()
//		.forEach(i -> subscriber.subscribe(i.getTicker()));
		
		this.subscriber.start();
		
		logger.info(String.format("CONNECTED TO MKTDATA PUBLISHER AT %s", connectAddress));
	}

	private void queryInstDef()
	{
		List<FuturesInstrument> queryResults = symbologyClient.queryAll();
		queryResults.stream().forEach(i -> instruments.put(i.getId(), i));
		this.symbologyView.populateInstrumentsTable(this.instruments);
		logger.info(String.format("LOADED %s INSTRUMENTS ", instruments.size()));
	}

	public void subscribeToMktdata(FuturesInstrument fi)
	{
		subscriber.subscribe(fi.getTicker());
		logger.info(String.format("SUBSCRIBED TO MKTDATA FOR %s", fi.getTicker()));
		// TODO: Create a row in md view and add the instrument
	}
	
	@Override
	public void stop()
	{
		this.symbologyClient.stop();
		this.subscriber.stop();
		try
		{
			symbologyClientThread.join();
			subscriberThread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void handleQuote(Quote quote)
	{
		this.latestQuotes.put(quote.getId(), quote);
		logger.info(quote.toString());
	}
	
	public Map<Integer, FuturesInstrument> getInstruments()
	{
		return instruments;
	}
}
