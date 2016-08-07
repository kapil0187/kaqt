package kaqt.supersonictrader.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;

import kaqt.supersonictrader.utils.TableColumnNames;

public class MarketDataView extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
		
	//BUTTONS
	private JButton btnSend;
	
	//TABLES
	private JTable orderTicketTable;
	private JScrollPane orderTicketSP;
	private DefaultTableModel orderTicketTableModel;
	
	private JScrollPane marketDataSP;
	private JTable marketDataTable;
	private DefaultTableModel marketDataTableModel;
	
	public MarketDataView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[300px,fill][grow]", "[grow][50px,fill]"));

		btnSend = new JButton("SEND");
		contentPane.add(btnSend, "cell 0 1");
		
		this.initializeOrderTicket();
		this.initializeMarketDataTable();
	}
	
	private void initializeOrderTicket() {
		orderTicketSP = new JScrollPane();
		contentPane.add(orderTicketSP, "cell 0 0,grow");
		orderTicketTableModel = new DefaultTableModel();
		for (String colName : TableColumnNames.ORDER_TICKET) {
			orderTicketTableModel.addColumn(colName);
		}
		orderTicketTable = new JTable(orderTicketTableModel);
		orderTicketSP.setColumnHeaderView(orderTicketTable);
		orderTicketSP.setRowHeaderView(orderTicketTable);
		orderTicketSP.setViewportView(orderTicketTable);
	}

	private void initializeMarketDataTable() {
		marketDataSP = new JScrollPane();
		contentPane.add(marketDataSP, "cell 1 0 1 2,grow");	
		marketDataTableModel = new DefaultTableModel();
//		for (String colName : TableColumnNames.MARKET_DATA_GRID) {
//			marketDataTableModel.addColumn(colName);
//		}
		marketDataTable = new JTable(marketDataTableModel);
		marketDataSP.setColumnHeaderView(marketDataTable);
		marketDataSP.setRowHeaderView(marketDataTable);
		marketDataSP.setViewportView(marketDataTable);
	}
	
	
	private class MarketDataTableIndices {
		public static final int INTRUMENT_COL = 0;
		public static final int TIME_COL = 1;
		public static final int BID_VOLUME_COL = 2;
		public static final int BID_PRICE_COL = 3;
		public static final int ASK_PRICE_COL = 6;
		public static final int ASK_VOLUME_COL = 7;
	}
}
