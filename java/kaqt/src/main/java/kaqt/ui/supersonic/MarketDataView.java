package kaqt.ui.supersonic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import org.apache.log4j.Logger;

import quickfix.field.OrdRejReason;
import kaqt.foundation.uielements.ComboboxTableCellEditor;
import kaqt.foundation.uielements.ComplexTable;
import kaqt.foundation.uielements.ParameterValueTableModel;
import kaqt.foundation.uielements.ParametersTable;
import kaqt.foundation.uielements.SpinnerTableCellEditor;
import kaqt.utils.TableColumnNames;
import kaqt.utils.TableRowNames;
import net.miginfocom.swing.MigLayout;

import javax.swing.JSplitPane;

public class MarketDataView extends JFrame
{
	final static Logger logger = Logger.getLogger(MarketDataView.class);

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JSplitPane mdViewSplitPane;

	// TABLES
	private JTable orderTicketTable;
	private JScrollPane orderTicketSP;
	private DefaultTableModel orderTicketTableModel;

	private JScrollPane marketDataSP;
	private JTable marketDataTable;
	private DefaultTableModel marketDataTableModel;
	private JPanel panel;
	private JButton btnSend;

	public MarketDataView()
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1200, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane
				.setLayout(new MigLayout("", "[][300px,grow,fill]", "[grow]"));

		mdViewSplitPane = new JSplitPane();
		contentPane.add(mdViewSplitPane, "cell 1 0,grow");

		this.initializeOrderTicket();
		this.initializeMarketDataTable();

		logger.info("INITIALIZED MARKET DATA VIEW");
	}

	private void initializeOrderTicket()
	{
		orderTicketTableModel = new ParameterValueTableModel();
		for (String colName : TableColumnNames.PARAMETER_VALUE_TABLE)
		{
			orderTicketTableModel.addColumn(colName);
		}

		panel = new JPanel();
		mdViewSplitPane.setLeftComponent(panel);
		panel.setLayout(new MigLayout("", "[fill]", "[grow,fill][50px,fill]"));

		orderTicketSP = new JScrollPane();
		panel.add(orderTicketSP, "cell 0 0");

		Map<Integer, TableCellEditor> editors = new HashMap<Integer, TableCellEditor>();
		editors.put(OrderTicketRowNum.EXECUTION,
				new ComboboxTableCellEditor<String>(new ArrayList<String>()));
		editors.put(
				OrderTicketRowNum.ORDER_TYPE,
				new ComboboxTableCellEditor<String>(Arrays
						.asList("BUY", "SELL")));
		editors.put(
				OrderTicketRowNum.SIDE,
				new ComboboxTableCellEditor<String>(Arrays
						.asList("BUY", "SELL")));
		editors.put(OrderTicketRowNum.PRICE, new SpinnerTableCellEditor(
				new SpinnerNumberModel(0.0, 0.0, 1000000.00, 0.01)));
		editors.put(OrderTicketRowNum.QUANTITY, new SpinnerTableCellEditor(
				new SpinnerNumberModel(0, 0, 1000, 1)));

		TableRowNames.ORDER_TICKET_ROWS.keySet().stream().sorted()
				.forEach(index -> orderTicketTableModel.addRow(new Object[]
				{ TableRowNames.ORDER_TICKET_ROWS.get(index) }));

		orderTicketTable = new ParametersTable(orderTicketTableModel, editors);
		orderTicketSP.setViewportView(orderTicketTable);

		btnSend = new JButton("SEND");
		panel.add(btnSend, "cell 0 1");

	}

	private void initializeMarketDataTable()
	{
		marketDataTableModel = new DefaultTableModel();
		for (String colName : TableColumnNames.MARKET_DATA_GRID)
		{
			marketDataTableModel.addColumn(colName);
		}

		marketDataSP = new JScrollPane();
		mdViewSplitPane.setRightComponent(marketDataSP);
		marketDataTable = new ComplexTable(marketDataTableModel, null);
		marketDataSP.setColumnHeaderView(marketDataTable);
		marketDataSP.setRowHeaderView(marketDataTable);
		marketDataSP.setViewportView(marketDataTable);
	}

	private class MarketDataTableIndices
	{
		public static final int INTRUMENT = 0;
		public static final int TIME = 1;
		public static final int BID_VOLUME_COL = 2;
		public static final int BID_PRICE_COL = 3;
		public static final int ASK_PRICE_COL = 6;
		public static final int ASK_VOLUME_COL = 7;
		public static final int REMOVE_COL = 8;
	}

	private class OrderTicketRowNum
	{
		public static final int EXECUTION = 0;
		public static final int INSTRUMENT = 1;
		public static final int ORDER_TYPE = 2;
		public static final int SIDE = 3;
		public static final int PRICE = 4;
		public static final int QUANTITY = 5;

	}
}
