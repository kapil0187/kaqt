package kaqt.supersonic.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import kaqt.supersonic.model.MarketDataType;
import kaqt.supersonic.model.UiConstants;
import net.miginfocom.swing.MigLayout;

public class MarketDataQuoteView extends JFrame {
	
	final int TIME_STAMP_COLUMN = 1;
	final int BIDQTYCOLUMN = 2;
	final int BIDCOLUMN = 3;
	final int ASKCOLUMN = 4;
	final int ASKQTYCOLUMN = 5;
	
	public class MDQTableRow {
		public int rowIndex;
		public Quote quote;
	}
	
	private StartScreen parent;
	private JPanel contentPane;
	private JTable marketDataTable;
	private DefaultTableModel mdTableModel;
	private JButton btnRemove;
	private JButton btnStop;
	private Map<String, MDQTableRow> tableRows;
	private Timer timer;
	private boolean isUpdating = false;

	public MarketDataQuoteView(final StartScreen parent) {
		setFont(new Font("Courier New", Font.PLAIN, 12));
		setTitle("MD VIEW");
		timer = new Timer();		
		
		this.parent = parent;
		setIconImage(Toolkit.getDefaultToolkit().getImage(MarketDataQuoteView.class.getResource("/kaqt/supersonic/resources/supersonic.jpg")));
		
		ArrayList<String> columnNames = UiConstants.MDViewTableHeaders.get(MarketDataType.QUOTE);
		
		tableRows = new HashMap<String, MDQTableRow>();
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 650, 300);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[100px:n,fill]20[grow]", "[35px:n,fill][][][35px:n,fill][grow,fill]"));
		
		btnRemove = new JButton("REMOVE");
		btnRemove.setFont(new Font("Cambria Math", Font.BOLD, 13));
		contentPane.add(btnRemove, "cell 0 0");
		
		btnStop = new JButton("STOP");
		btnStop.setFont(new Font("Cambria Math", Font.BOLD, 13));
		contentPane.add(btnStop, "cell 0 3");
		
		JScrollPane mdScrolPane = new JScrollPane();
		contentPane.add(mdScrolPane, "cell 1 0 1 5,grow");
		
		mdTableModel = new ReadOnlyTableModel();
		
		for (String col : columnNames) {
			mdTableModel.addColumn(col);
		}
		
		marketDataTable = new JTable(mdTableModel);
		marketDataTable.setForeground(new Color(255, 215, 0));
		marketDataTable.setFont(new Font("Cambria Math", Font.PLAIN, 11));
		marketDataTable.setFillsViewportHeight(true);
		marketDataTable.setToolTipText("");
		marketDataTable.setRowSelectionAllowed(true);
		marketDataTable.setColumnSelectionAllowed(false);
		mdScrolPane.setColumnHeaderView(marketDataTable);
		mdScrolPane.setViewportView(marketDataTable);
	}
	
	private void dataDisplayStart() {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				for (String key : tableRows.keySet()) {
					MDQTableRow row = tableRows.get(key);
					mdTableModel.setValueAt(row.quote.getTimeStamp(), row.rowIndex, TIME_STAMP_COLUMN);
					mdTableModel.setValueAt(row.quote.getBidSize(), row.rowIndex, BIDQTYCOLUMN);
					mdTableModel.setValueAt(new BigDecimal(row.quote.getBid()).setScale(3, RoundingMode.HALF_DOWN), 
							row.rowIndex, BIDCOLUMN);
					mdTableModel.setValueAt(new BigDecimal(row.quote.getAsk()).setScale(3, RoundingMode.HALF_DOWN), 
							row.rowIndex, ASKCOLUMN);
					mdTableModel.setValueAt(row.quote.getAskSize(), row.rowIndex, ASKQTYCOLUMN);
				}
			}
		}, 1000,500);
	}
	
	
	public void onPriceUpdate(Quote quote) {
		MDQTableRow row = tableRows.get(quote.getTicker());
		row.quote = quote;
	}
	
	public void addInstrument(String instrument) {
		if (!isUpdating) {
			this.dataDisplayStart();
			isUpdating = false;
		}
		
		if (!tableRows.containsKey(instrument)) {
			MDQTableRow tr = new MDQTableRow();
			tr.quote = new Quote(0.0, 0.0, 0, 0, instrument, "NA");
			tableRows.put(instrument, tr);
					
			mdTableModel.addRow(new Object[] {instrument, tr.quote.getTimeStamp(),
					tr.quote.getBid(), tr.quote.getAsk(), tr.quote.getBidSize(), 
					tr.quote.getAskSize()});

			tr.rowIndex = mdTableModel.getRowCount() - 1;	
		}
	}
}