package kaqt.supersonic.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultCaret;

import kaqt.supersonic.core.IParameter;
import kaqt.supersonic.model.Algorithm;
import kaqt.supersonic.model.AlgorithmFactory;
import kaqt.supersonic.model.AlgorithmSummary;
import kaqt.supersonic.model.FillOrderUpdate;
import kaqt.supersonic.model.InitiatedOrderUpdate;
import kaqt.supersonic.model.Instrument;
import kaqt.supersonic.model.OrderUpdate;
import kaqt.supersonic.model.PnlCalculator;
import kaqt.supersonic.ui.utils.UiColors;
import kaqt.supersonic.ui.utils.VertGradientLabel;
import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.google.gson.Gson;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class StartScreen extends JFrame {
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = Logger.getLogger(StartScreen.class.getName());
	
	private final int ENDING_EQUITY_ROW = 1;
	private final int TOTAL_TRADES_ROW = 2;
	private final int TOTAL_LONG_TRADES_ROW = 3;
	private final int TOTAL_SHORT_TRADES_ROW = 4;
	private final int GROSS_LONG_PROFIT_ROW = 5;
	private final int GROSS_SHORT_PROFIT_ROW = 6;
	private final int TOTAL_PROFIT_ROW = 7;
	private final int TRANSACTION_COST_ROW = 9;
	private final int TRANSACTION_COST_LONG_ROW = 10;
	private final int TRANSACTION_COST_SHORT_ROW = 11;
	private final int AVERAGE_PROFIT_LOSS_ROW = 12;
	private final int TOTAL_WINNERS_ROW = 13;
	private final int TOTAL_WINNERS_PERC_ROW = 14;	
	private final int TOTAL_LONG_WINNERS_ROW = 15;
	private final int TOTAL_SHORT_WINNERS_ROW = 16;

	private static String guiIconPath = "/kaqt/supersonic/resources/supersonic.jpg"; 
	
	private Gson gson;
	
	private Map<String, Instrument> instruments;
	
	private Context ctx;
	private Socket mdQuotesSubscriber;
	private Socket newsSubscriber;
	private Socket ordersSubscriber;
	
	private Thread mdThread;
	private Thread newsThread;
	private Thread ordersThread;
	
	private JPanel contentPane;
	private ImageIcon currentIcon;
	private JTable strategyParamsTable;
	private JList<String> strategyList;
	private JList<String> chartList;
	private ChartsListModel chartListModel;
	private ReadOnlyTableModel summaryTM;
	private Map<String, File> availableCharts; 
	private DefaultListModel<String> logListModel;
	private ParameterTableModel paramTblModel;
	private JTable summaryTable;
	private MarketDataQuoteView mdQuotesView;
	private InstrumentSearchView instrumentsDatabase;
	private FillsView fillsView;
	private OpenOrdersView openOrdersView;
	private MDViewLauncher launchMDQuotes;
	private MDViewLauncher launchMDTrades;
	private MDViewLauncher launchMDOhlc;

	private TimeSeriesChart pnlChart;
	private Timer chartTimer;
	private AlgorithmFactory algorithmFactory;
	private DefaultListModel<String> algoListModel;
	private AlgorithmSummary summary;
	private int totalTrades = 0;
	private int totalLongTrades = 0;
	private double totalLongProfit = 0;
	private double totalProfit = 0;
	private double transactionCosts = 0;
	private double longTransactionCosts = 0;
	private int totalWinners = 0;
	private int totalLongWinners = 0;
	private TextAreaLogAppender appender;
	
	private boolean isInstDefShowing = false;
	private boolean isCompletedOrdersShowing = false;
	private boolean isFillsViewShowing = false;
	private boolean isOpenOrdersShowing = false;
	private boolean isTerminated = false;
	private boolean newsFeedOn = false;
	private boolean isOrderServiceSubscribed = false;
	
	private MongoClient mongoClient = null;
	
	private Map<String, PnlCalculator> pnlCalcs;
	
	public StartScreen(AlgorithmFactory algoFactory) {
		
		gson = new Gson();
		instruments = new HashMap<String, Instrument>();
		pnlCalcs = new HashMap<String, PnlCalculator>();
		pnlCalcs.put("EDH6", new PnlCalculator());
		pnlCalcs.put("EDZ5", new PnlCalculator());
		
		mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase db = mongoClient.getDatabase("kaqt");
		MongoCollection<Document> collection = db.getCollection("instruments");
		for (Document doc : collection.find()) {
			ObjectId id = doc.getObjectId("_id");
			doc.remove("_id");
			Instrument instrument = gson.fromJson(doc.toJson(), Instrument.class);
			instrument.set_id(id.toString());
			instruments.put(id.toString(), instrument);
		}
		
		summary = new AlgorithmSummary();
		
		this.algorithmFactory = algoFactory;
		this.algoListModel = new DefaultListModel<String>();
		
		paramTblModel = new ParameterTableModel();
		
		setFont(new Font("Cambria Math", Font.PLAIN, 12));
		ctx = ZMQ.context(1);
		mdQuotesSubscriber = ctx.socket(ZMQ.SUB);
		newsSubscriber = ctx.socket(ZMQ.SUB);
		ordersSubscriber = ctx.socket(ZMQ.SUB);
		mdQuotesSubscriber.connect("tcp://localhost:5680");
		newsSubscriber.connect("tcp://localhost:5681");
		ordersSubscriber.connect("tcp://localhost:5682");
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(StartScreen.class.getResource(guiIconPath)));
		this.setTitle("SuperSonic");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 700);
			
		availableCharts = new HashMap<String, File>();
		File folder = new File("../../charts");		
		for ( File f : folder.listFiles()) {
			availableCharts.put(f.getName(), f);
		}
		chartListModel = new ChartsListModel(availableCharts.keySet());
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Courier New", Font.BOLD, 12));
		menuBar.setForeground(new Color(255, 215, 0));
		menuBar.setToolTipText("Menu");
		setJMenuBar(menuBar);
		
		JMenu mnMain = new JMenu("MAIN");
		mnMain.setFont(new Font("Cambria Math", Font.BOLD, 12));
		menuBar.add(mnMain);
		
		JMenuItem mntmStop = new JMenuItem("STOP");
		mntmStop.setFont(new Font("Cambria Math", Font.PLAIN, 11));
		mnMain.add(mntmStop);
		
		JMenuItem mntmPause = new JMenuItem("PAUSE");
		mntmPause.setFont(new Font("Cambria Math", Font.PLAIN, 11));
		mnMain.add(mntmPause);
		
		JMenuItem mntmExit = new JMenuItem("EXIT");
		mntmExit.setFont(new Font("Cambria Math", Font.PLAIN, 11));
		mnMain.add(mntmExit);
		
		JMenu mnSymbology = new JMenu("SYMBOLOGY");
		mnSymbology.setFont(new Font("Cambria Math", Font.BOLD, 12));
		menuBar.add(mnSymbology);
		
		JMenuItem mntmDatabase = new JMenuItem("DATABASE");
		mntmDatabase.setFont(new Font("Cambria Math", Font.PLAIN, 11));
		mntmDatabase.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				launchInstrumentsDatabase();
			}
		});
		mnSymbology.add(mntmDatabase);
		
		JMenuItem mntmSearch = new JMenuItem("BACKTEST SYMBOLS");
		mntmSearch.setFont(new Font("Cambria Math", Font.PLAIN, 11));
		mnSymbology.add(mntmSearch);
				
		JMenu mnStrategy = new JMenu("MARKET DATA");
		mnStrategy.setFont(new Font("Cambria Math", Font.BOLD, 12));
		menuBar.add(mnStrategy);
		
		JMenuItem mntmIndicatorWindow = new JMenuItem("INDICATORS");
		mntmIndicatorWindow.setFont(new Font("Cambria Math", Font.PLAIN, 11));
		
		mdQuotesView = new MarketDataQuoteView(this);
		
		launchMDQuotes = new MDViewLauncher(this);
		JMenuItem mntmQuotes = new JMenuItem("QUOTES");
		mntmQuotes.setFont(new Font("Cambria Math", Font.PLAIN, 11));
		mntmQuotes.addActionListener(launchMDQuotes);
		mnStrategy.add(mntmQuotes);
		
//		launchMDOhlc = new MDViewLauncher(this, MarketDataType.OHLC);
//		JMenuItem mntmOhlc = new JMenuItem(MarketDataType.OHLC.name());
//		mntmOhlc.setFont(new Font("Cambria Math", Font.PLAIN, 11));
//		mntmOhlc.addActionListener(launchMDOhlc);
//		mnStrategy.add(mntmOhlc);
//		
//		launchMDTrades = new MDViewLauncher(this, MarketDataType.TRADE);
//		JMenuItem mntmTrades = new JMenuItem(MarketDataType.TRADE.name());
//		mntmTrades.setFont(new Font("Cambria Math", Font.PLAIN, 11));
//		mntmTrades.addActionListener(launchMDTrades);
//		mnStrategy.add(mntmTrades);

		
		
		mnStrategy.add(mntmIndicatorWindow);
		
		JMenu mnOrders = new JMenu("EXECUTION");
		mnOrders.setFont(new Font("Cambria Math", Font.BOLD, 12));
		menuBar.add(mnOrders);
		
		JMenuItem mntmOrders = new JMenuItem("ORDERS");
		mntmOrders.setFont(new Font("Cambria Math", Font.PLAIN, 11));
		mntmOrders.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				launchOpenOrdersView();
			}
		});
		mnOrders.add(mntmOrders);
		
		JMenuItem mntmFills = new JMenuItem("FILLS");
		mntmFills.setFont(new Font("Cambria Math", Font.PLAIN, 11));
		mntmFills.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				launchFillsView();
			}
		});
		mnOrders.add(mntmFills);
		
		JMenuItem mntmPositions = new JMenuItem("POSITIONS");
		mntmPositions.setFont(new Font("Cambria Math", Font.PLAIN, 11));
		mnOrders.add(mntmPositions);
		
		JMenu mnRisk = new JMenu("RISK");
		mnRisk.setFont(new Font("Cambria Math", Font.BOLD, 12));
		menuBar.add(mnRisk);
		
		JMenuItem mntmMargin = new JMenuItem("MARGIN");
		mntmMargin.setFont(new Font("Cambria Math", Font.PLAIN, 11));
		mnRisk.add(mntmMargin);
		
		JMenu mnSettings = new JMenu("SETTINGS");
		mnSettings.setFont(new Font("Cambria Math", Font.BOLD, 12));
		menuBar.add(mnSettings);
		
		JMenuItem mntmConnections = new JMenuItem("CONNECTIONS");
		mntmConnections.setFont(new Font("Cambria Math", Font.PLAIN, 11));
		mnSettings.add(mntmConnections);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[:150px:150px,fill][:150px:150px,fill][634px,grow,fill][:300px:300px,fill]", "[1.00px][25.00px,fill][350px:n,grow,fill][300px,fill][25px,fill]"));
				
				JButton btnLaunch = new JButton("LAUNCH");
				btnLaunch.setFont(new Font("Cambria Math", Font.BOLD, 11));
				contentPane.add(btnLaunch, "cell 0 1");
				
				JButton btnNewresetbutton = new JButton("RESET");
				btnNewresetbutton.setFont(new Font("Cambria Math", Font.BOLD, 11));
				btnNewresetbutton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
					}
				});
				contentPane.add(btnNewresetbutton, "cell 1 1");
				
				JLabel lblChartTitle = new VertGradientLabel("CHART TITLE");
				lblChartTitle.setText("STRATEGY ANALYSIS");
				lblChartTitle.setForeground(new Color(255, 215, 0));
				lblChartTitle.setFont(new Font("Cambria Math", Font.BOLD, 13));
				lblChartTitle.setHorizontalAlignment(SwingConstants.CENTER);
				contentPane.add(lblChartTitle, "cell 2 1");
		
		JLabel lblCharts = new VertGradientLabel("CHARTS");
		lblCharts.setForeground(new Color(255, 215, 0));
		lblCharts.setFont(new Font("Cambria Math", Font.BOLD, 13));
		lblCharts.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblCharts, "cell 3 1");
				
		JScrollPane strategyListSP = new JScrollPane();
		contentPane.add(strategyListSP, "cell 0 2 2 1,grow");
		
		for (Algorithm algo : algoFactory.getAlgos()) {
			algoListModel.addElement(algo.getAlgoName());
		}
		
		strategyList = new JList<String>(algoListModel);
		strategyList.addListSelectionListener(new AlgoSelectionListListener(this));
		strategyListSP.setViewportView(strategyList);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Cambria Math", Font.BOLD, 12));
		contentPane.add(tabbedPane, "cell 2 2,grow");
		
		JPanel liveChartPanel = new JPanel();
		tabbedPane.addTab("P/L", null, liveChartPanel, null);
		liveChartPanel.setLayout(new MigLayout("", "[grow,fill]", "[grow,fill]"));
		pnlChart = new TimeSeriesChart(liveChartPanel, "EQUITY CURVE", Settings.INITIAL_EQUITY);
		
		JPanel analysisChartsPanel = new JPanel();
		tabbedPane.addTab("ANALYSIS", null, analysisChartsPanel, null);
				
		JScrollPane chartListSP = new JScrollPane();
		contentPane.add(chartListSP, "cell 3 2,grow");
		
		chartList = new JList<String>(chartListModel);
		chartList.setFont(new Font("Cambria Math", Font.PLAIN, 11));
		chartList.addListSelectionListener(new ChartListSelectionListener(this));
		chartListSP.setViewportView(chartList);
		
		JScrollPane strategyParamsSP = new JScrollPane();
		contentPane.add(strategyParamsSP, "cell 0 3 2 1,grow");
		
		paramTblModel.addColumn("PARAMETER");
		paramTblModel.addColumn("VALUE");
		
		strategyParamsTable = new JTable(paramTblModel);
		strategyParamsTable.setFont(new Font("Cambria Math", Font.PLAIN, 11));
		strategyParamsTable.setFont(new Font("Cambria Math", Font.PLAIN, 12));		
		strategyParamsSP.setColumnHeaderView(strategyParamsTable);
		strategyParamsSP.setViewportView(strategyParamsTable);
				
		JTabbedPane analysisTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		analysisTabbedPane.setFont(new Font("Cambria Math", Font.BOLD, 11));
		contentPane.add(analysisTabbedPane, "cell 2 3 2 1,grow");
		
		logListModel = new DefaultListModel<String>();
		
		JPanel summaryPanel = new JPanel();
		analysisTabbedPane.addTab("SUMMARY", null, summaryPanel, null);
		
		summaryTM = new ReadOnlyTableModel();
		this.initializeSummaryTable();
		
		summaryPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JScrollPane summaryTableSP = new JScrollPane();
		summaryPanel.add(summaryTableSP, "1, 1, 2, 2, fill, fill");
		
		summaryTable = new JTable(summaryTM);
		summaryTable.setFont(new Font("Cambria Math", Font.PLAIN, 10));
		summaryTableSP.setRowHeaderView(summaryTable);
		summaryTableSP.setViewportView(summaryTable);
		
		JPanel monteCarloPanel = new JPanel();
		analysisTabbedPane.addTab("MONTE CARLO", null, monteCarloPanel, null);
		
		JScrollPane newsSP = new JScrollPane();
		newsSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		analysisTabbedPane.addTab("NEWS", null, newsSP, null);
		
		final JTextArea newsTextArea = new JTextArea();
		newsTextArea.setEditable(false);
		newsTextArea.setLineWrap(true);
		newsTextArea.setFont(new Font("Cambria Math", Font.ITALIC, 11));
		newsTextArea.setForeground(UiColors.GOLDEN_TEXT);
		((DefaultCaret)newsTextArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		newsSP.setViewportView(newsTextArea);
		
		JScrollPane logsSP = new JScrollPane();
		analysisTabbedPane.addTab("LOGS", null, logsSP, null);
		
		final JTextArea logsTextArea = new JTextArea();
		logsTextArea.setLineWrap(true);
		logsTextArea.setForeground(new Color(255, 215, 0));
		logsTextArea.setFont(new Font("Cambria Math", Font.ITALIC, 11));
		logsTextArea.setEditable(false);
		((DefaultCaret)logsTextArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		logsSP.setRowHeaderView(logsTextArea);
		logsSP.setViewportView(logsTextArea);
		
		appender = new TextAreaLogAppender(logsTextArea);
		logger.addAppender(appender);
				
		final JLabel newsLabel = new JLabel("");
		newsLabel.setFont(new Font("Cambria Math", Font.PLAIN, 10));
		contentPane.add(newsLabel, "cell 1 4 3 1");
		
		final JToggleButton tglbtnNewToggleButton = new JToggleButton("NEWS FEED");
		tglbtnNewToggleButton.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (tglbtnNewToggleButton.isSelected()) {
					newsSubscriber.subscribe("Tweets".getBytes());
					newsFeedOn = true;
				} else {
					newsFeedOn = false;
					newsSubscriber.unsubscribe("Tweets".getBytes());
					newsLabel.setText("");
				}
				
			}
		});
		tglbtnNewToggleButton.setFont(new Font("Cambria Math", Font.BOLD, 10));
		contentPane.add(tglbtnNewToggleButton, "cell 0 4");
		
		mdThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!Thread.currentThread().isInterrupted()) {
					String address = mdQuotesSubscriber.recvStr();
					String contents = mdQuotesSubscriber.recvStr();
					this.handleJsonPrice(address, contents);
				}
				terminate();
			}

			private void handleJsonPrice(String address, String contents) {
				if (mdQuotesView != null) {
					Quote p = gson.fromJson(contents, Quote.class);
					mdQuotesView.onPriceUpdate(p);
				}
			}
		});
		
		mdThread.start();
		
		newsThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!Thread.currentThread().isInterrupted()) {
					String address = newsSubscriber.recvStr();
					String contents = newsSubscriber.recvStr();
					if (newsFeedOn) {
						newsLabel.setText(contents);
						newsTextArea.append(contents + "\n");						
					}
				}
			}
		});
		
		newsThread.start();
		
		ordersThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!Thread.currentThread().isInterrupted()) {
					String address = ordersSubscriber.recvStr();
					String contents = ordersSubscriber.recvStr();
					handleOrderUpdate(address, contents);
				}
			}
		});
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				mdThread.interrupt();
				newsThread.interrupt();
			}
		});
		
        chartTimer = new Timer();
        chartTimer.schedule(new TimerTask() {
			Random rg = new Random();
			double endingEquity = Settings.INITIAL_EQUITY;
			@Override
			public void run() {
				//if (rg.nextBoolean() == true) {
//					int profit = (rg.nextInt(10) - rg.nextInt(9))*10;
//					endingEquity += profit;
//	                pnlChart.update(endingEquity);
//	                summaryTM.setValueAt(endingEquity, ENDING_EQUITY_ROW, 1);
//	                
//	                totalProfit += profit;
//	                if (profit > 0) {
//	                	totalWinners += 1;
//	                }
//	                
//	                totalTrades += 1;
//	                if (rg.nextBoolean() == true) {
//	                	totalLongTrades += 1;
//	                	totalLongProfit += profit;
//		                if (profit > 0) {
//		                	totalLongWinners += 1;
//		                }
//	                }

					int profit = (int)(pnlCalcs.get("EDH6").getPnl()*25);
					System.out.println(profit);
					endingEquity = Settings.INITIAL_EQUITY + profit;
	                pnlChart.update(endingEquity);
	                summaryTM.setValueAt(endingEquity, ENDING_EQUITY_ROW, 1);

	                totalProfit = profit;
	                if (profit > 0) {
	                	totalWinners += 1;
	                }

	                totalTrades += 1;
	                if (rg.nextBoolean() == true) {
	                	totalLongTrades += 1;
	                	totalLongProfit += profit;
		                if (profit > 0) {
		                	totalLongWinners += 1;
		                }
	                }

					
	                summaryTM.setValueAt(totalTrades, TOTAL_TRADES_ROW, 1);
	                summaryTM.setValueAt(totalLongTrades, TOTAL_LONG_TRADES_ROW, 1);
	                summaryTM.setValueAt(totalTrades - totalLongTrades, TOTAL_SHORT_TRADES_ROW, 1);
	                summaryTM.setValueAt(totalLongProfit, GROSS_LONG_PROFIT_ROW, 1);
	                summaryTM.setValueAt(totalProfit - totalLongProfit, GROSS_SHORT_PROFIT_ROW, 1);
	                summaryTM.setValueAt(totalProfit, TOTAL_PROFIT_ROW, 1);
	                summaryTM.setValueAt(new BigDecimal(Settings.TRANSACTION_COST_RATE*totalTrades).setScale(3, RoundingMode.HALF_DOWN), TRANSACTION_COST_ROW, 1);
	                summaryTM.setValueAt(new BigDecimal(Settings.TRANSACTION_COST_RATE*totalLongTrades).setScale(3, RoundingMode.HALF_DOWN), TRANSACTION_COST_LONG_ROW, 1);
	                summaryTM.setValueAt(new BigDecimal(Settings.TRANSACTION_COST_RATE*(totalTrades - totalLongTrades)).setScale(3, RoundingMode.HALF_DOWN), 
	                		TRANSACTION_COST_SHORT_ROW, 1);	                
	                summaryTM.setValueAt(new BigDecimal(totalProfit/totalTrades).setScale(3, RoundingMode.FLOOR), AVERAGE_PROFIT_LOSS_ROW, 1);
	                summaryTM.setValueAt(new BigDecimal(totalProfit/totalTrades).setScale(3, RoundingMode.FLOOR), AVERAGE_PROFIT_LOSS_ROW, 1);
	                summaryTM.setValueAt(totalWinners, TOTAL_WINNERS_ROW, 1);
	                summaryTM.setValueAt((new BigDecimal((double)totalWinners*100/totalTrades).setScale(3,RoundingMode.FLOOR)).toString() + "%" , TOTAL_WINNERS_PERC_ROW, 1);	                
	                summaryTM.setValueAt(totalLongWinners, TOTAL_LONG_WINNERS_ROW, 1);
	                summaryTM.setValueAt(totalWinners - totalLongWinners, TOTAL_SHORT_WINNERS_ROW, 1);	              
				//}
			}
		}, 1000, 1000);		
	}
	
	public void launchInstrumentsDatabase() {
		if (!isInstDefShowing) {
			instrumentsDatabase = new InstrumentSearchView(instruments, this);
			instrumentsDatabase.setVisible(true);
			isInstDefShowing = true;
		}
	}
	
	public void launchMdQuotesView() {
		mdQuotesView.setVisible(true);
	}
	
	public void displayLog(String message) {
		logListModel.addElement(message);
	}
	
	public void addInstrumentToMDView(String ticker) {
		mdQuotesView.addInstrument(ticker);
		mdQuotesSubscriber.subscribe(ticker.getBytes());
		mdQuotesView.setVisible(true);
	}
	
	public void updateImage() {
		
	}
	
	public void launchFillsView() {
		if (!isFillsViewShowing) {
			fillsView = new FillsView();
			isFillsViewShowing = true;
			if (!isOrderServiceSubscribed) {
				subscribeToOrderService();
				isOrderServiceSubscribed = true;
			}
			ordersSubscriber.subscribe("filled".getBytes());
		}
		fillsView.setVisible(true);
	}
	
	public void launchOpenOrdersView() {
		if (!isOpenOrdersShowing) {
			openOrdersView = new OpenOrdersView();
			isOpenOrdersShowing = true;
			if (!isOrderServiceSubscribed) {
				subscribeToOrderService();
				isOrderServiceSubscribed = true;
			}
			ordersSubscriber.subscribe("initiated".getBytes());
			ordersSubscriber.subscribe("accepted".getBytes());
			ordersSubscriber.subscribe("filled".getBytes());
			ordersSubscriber.subscribe("completed".getBytes());
		}
		openOrdersView.setVisible(true);
	}
	
	public void terminate() {
		if (!isTerminated) {
			mdQuotesSubscriber.close();
			newsSubscriber.close();
			ordersSubscriber.close();
			ctx.term();
		}
	}
	
	public void handleOrderUpdate(String address, String contents) {
		Random rg = new Random();
		logger.debug("[" + address + "] " + contents );
		boolean updateOpenOrderView = openOrdersView != null;
		if (address.equals("filled")) {
			if (fillsView != null) {
				FillOrderUpdate fou = gson.fromJson(contents, FillOrderUpdate.class);
				fillsView.addFillUpdate(fou);
				System.out.println(fou.getSymbol());
				pnlCalcs.get(fou.getSymbol()).onNext(fou.getFill(), rg.nextBoolean() == true ? "buy" : "sell");
			}
			
			if (updateOpenOrderView) {
				openOrdersView.markFilled(gson.fromJson(contents, OrderUpdate.class).getClient_id());				
			}
		} else if (address.equals("initiated")) {
			if (updateOpenOrderView) {
				InitiatedOrderUpdate iou = gson.fromJson(contents, InitiatedOrderUpdate.class);
				openOrdersView.initiateOrder(iou);
			}
		} if (address.equals("accepted")) {
			if (updateOpenOrderView) {
				openOrdersView.markAccepted(gson.fromJson(contents, OrderUpdate.class).getClient_id());
			}
		} if (address.equals("completed")) {
			if (updateOpenOrderView) {
				openOrdersView.markCompleted(gson.fromJson(contents, OrderUpdate.class).getClient_id());
			}
		} 
	}
	
	public void subscribeToOrderService() {
		ordersThread.start();
	}
	
	public void updateParameters() {
		for (int i = paramTblModel.getRowCount() - 1; i >= 0; --i) {
			paramTblModel.removeRow(i);
		}
		
		this.initializePropertyGuid();
		
		ArrayList<IParameter> params = algorithmFactory.buildAlgorithm(strategyList.getSelectedValue().toString()).getParams();
		for (IParameter p : params) {
			paramTblModel.addRow(new Object[] {p.getDefinition(), p.getValue(Object.class)});	
		}
	}
	
	private void initializePropertyGuid() {
		paramTblModel.addRow(new Object[] {"INITIAL EQUITY", Settings.INITIAL_EQUITY});
		paramTblModel.addRow(new Object[] {"START DATE", Settings.START_DATE.toString("YYYY-MM-dd")});
		paramTblModel.addRow(new Object[] {"END DATE", Settings.END_DATE.toString("YYYY-MM-dd")});
	}
	
	private void initializeSummaryTable(){
		summaryTM.addColumn("METRIC");
		summaryTM.addColumn("VALUE");
		
		Map<String, BigDecimal> metrics = summary.getMetrics();
		for (String key : metrics.keySet()) {
			summaryTM.addRow(new Object[] {key, metrics.get(key)});
		}
	}
}
