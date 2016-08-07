package kaqt.edutils.ui;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.google.gson.Gson;

import kaqt.edutils.model.Quote;
import kaqt.edutils.utils.Constants;
import java.awt.Font;

public class EurodollarAnalysisWindow extends JFrame {
	
	private Context ctx;
	private Socket curveQuotesSubscriber;
	
	private Gson gson;
	
	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	
	//TABS
	private JPanel curvePanel;
	private JPanel calendarsPanel;
	
	//TABLES
	
	// PRICES TABLE
	private JScrollPane pricesTableSP;
	private JTable pricesTable;
	private DefaultTableModel pricesTableModel;
	private ConcurrentHashMap<String, Quote> pricesMap;
	// SYNTHETIC CALENDARS PRICES TABLE
	private JScrollPane syntheticCalendarsPricesSP;
	private JTable syntheticCalendarsPricesTable;
	private DefaultTableModel syntheticCalPricesTableModel;
	
	//CHART
	private CurveChart curveChart;
	
	//THREADS
	private Thread mdThread;
	
	//TIMERS
	private Timer pricesTimer;
	private Timer curveTimer;
	private JPanel curveChartPanel;
	
	public EurodollarAnalysisWindow() {
		gson = new Gson();
		ctx = ZMQ.context(1);
		curveQuotesSubscriber = ctx.socket(ZMQ.SUB);
		curveQuotesSubscriber.connect("tcp://localhost:5683");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow,fill]", "[grow]"));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, "cell 0 0,grow");
		
		curvePanel = new JPanel();
		tabbedPane.addTab("CURVE", null, curvePanel, null);
		curvePanel.setLayout(new MigLayout("", "[grow,fill]", "[grow][250px,grow,fill]"));
		
		calendarsPanel = new JPanel();
		tabbedPane.addTab("CALENDARS", null, calendarsPanel, null);
		calendarsPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		this.initializePricesTable();
		this.initializeCurveChartPanel();
		this.initializeSyntheticCalPricesTable();
		
		mdThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!Thread.currentThread().isInterrupted()) {
					String address = curveQuotesSubscriber.recvStr();
					String contents = curveQuotesSubscriber.recvStr();
					handleJsonPrice(contents);
				}
				terminate();
			}
		});
		curveQuotesSubscriber.subscribe(Constants.CURVE_QUOTES_TOPIC.getBytes());
		mdThread.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
		    @Override
		    public void run()
		    {
		    	try {
		    		terminate();
					mdThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    }
		});
		
        pricesTimer = new Timer();
        pricesTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				for (Quote q : pricesMap.values()) {
					int rowNum = Constants.CONTRACT_ROWS.get(q.getLeg());
					pricesTableModel.setValueAt(q.getBid_volume(), rowNum, Constants.BID_VOLUME_ROW);
					pricesTableModel.setValueAt(new BigDecimal(q.getBid_price()).setScale(2, RoundingMode.FLOOR),
							rowNum, Constants.BID_PRICE_ROW);
					pricesTableModel.setValueAt(new BigDecimal(q.getAsk_price()).setScale(2, RoundingMode.FLOOR),
							rowNum, Constants.ASK_PRICE_ROW);
					pricesTableModel.setValueAt(q.getAsk_volume(), rowNum, Constants.ASK_VOLUME_ROW);
				}
			}
		}, 1000, 1000);	
        
        curveTimer = new Timer();
        curveTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				curveChart.updatePrices(pricesMap);
			}
		}, 1000, 1000);
	}

	
	private void handleJsonPrice(String contents) {
		Quote quote = gson.fromJson(contents, Quote.class);
		pricesMap.put(quote.getLeg(), quote);
	}
	
	private void initializePricesTable() {
		pricesTableSP = new JScrollPane();
		curvePanel.add(pricesTableSP, "cell 0 0,grow");
		
		pricesTableModel = new DefaultTableModel();
		for (String colName : Constants.PRICES_TABLE_COLUMNS) {
			pricesTableModel.addColumn(colName);
		}
		
		pricesTable = new JTable(pricesTableModel);
		pricesTable.setFont(new Font("Cambria Math", Font.PLAIN, 11));
		pricesTableSP.setColumnHeaderView(pricesTable);
		pricesTableSP.setRowHeaderView(pricesTable);
		pricesTableSP.setViewportView(pricesTable);
		
		for (String rowName: Constants.ED_CONTRACTS) {
			pricesTableModel.addRow(new Object[] {rowName, "N/A", "N/A", "N/A", "N/A"});
		}
		
		pricesMap = new ConcurrentHashMap<String, Quote>();
	}
	
	private void initializeSyntheticCalPricesTable() {
		syntheticCalendarsPricesSP = new JScrollPane();
		calendarsPanel.add(syntheticCalendarsPricesSP, "cell 0 0,grow");
		
		syntheticCalendarsPricesTable = new JTable();
		syntheticCalendarsPricesSP.setViewportView(syntheticCalendarsPricesTable);
	}
	
	private void initializeCurveChartPanel() {
		curveChartPanel = new JPanel();
		curvePanel.add(curveChartPanel, "cell 0 1,grow");
		curveChartPanel.setLayout(new MigLayout("", "[grow,fill]", "[grow,fill]"));
		curveChart = new CurveChart(curveChartPanel, "EURODOLLAR CURVE", pricesMap);
	}
	
	public void terminate() {
		curveQuotesSubscriber.close();
		ctx.term();
	}
}
