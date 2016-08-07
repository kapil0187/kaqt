package kaqt.supersonic.ui;

import java.awt.Font;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import kaqt.supersonic.model.InitiatedOrderUpdate;
import kaqt.supersonic.model.UiConstants;
import kaqt.supersonic.ui.utils.UiColors;
import net.miginfocom.swing.MigLayout;

public class OpenOrdersView extends JFrame {
	
	final int STATUS_COL = 7;
	
	private JPanel contentPane;
	private JTable openOrdersTable;
	private DefaultTableModel openOrdersTableModel;
	private Map<String, Integer> indexes;
	private boolean isUpdating = false;

	public OpenOrdersView() {
		setFont(new Font("Cambria Math", Font.PLAIN, 12));
		setTitle("OPEN ORDERS");
		setIconImage(Toolkit.getDefaultToolkit().getImage(OpenOrdersView.class.getResource("/kaqt/supersonic/resources/supersonic.jpg")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		indexes = new ConcurrentHashMap<String, Integer>();
		
		JScrollPane openOrdersSP = new JScrollPane();
		contentPane.add(openOrdersSP, "cell 0 0,grow");
		
		openOrdersTableModel = new ReadOnlyTableModel();
		
		for (String colName : UiConstants.OPEN_ORDERS_COLUMN_HEADERS) {
			openOrdersTableModel.addColumn(colName);
		}
		
		openOrdersTable = new JTable(openOrdersTableModel);
		openOrdersTable.setFont(new Font("Cambria Math", Font.PLAIN, 11));
		openOrdersTable.setForeground(UiColors.GOLDEN_TEXT);
		openOrdersSP.setColumnHeaderView(openOrdersTable);
		openOrdersSP.setViewportView(openOrdersTable);
	}

	public void initiateOrder(InitiatedOrderUpdate iou) {
		openOrdersTableModel.addRow(new Object[] { iou.getClient_id(),
				iou.getOrder_id(), iou.getSymbol(), iou.getTimestamp(),
				iou.getOrder().getType(), new BigDecimal(iou.getOrder().getPrice()).setScale(3, RoundingMode.HALF_DOWN),
				iou.getOrder().getQuantity(), "INITIATED" });
		openOrdersTable.changeSelection(openOrdersTable.getRowCount() - 1, 0, false, false);
		indexes.put(iou.getClient_id(), openOrdersTableModel.getRowCount() - 1);
		isUpdating = true;
	}
	
	public void markAccepted(String clientId) {
		if (isUpdating) {
			openOrdersTable.changeSelection(openOrdersTable.getRowCount() - 1, 0, false, false);
			openOrdersTableModel.setValueAt("ACCEPTED", indexes.get(clientId), STATUS_COL);
		}
	}

	public void markCompleted(String clientId) {
		if (isUpdating) {
			openOrdersTable.changeSelection(openOrdersTable.getRowCount() - 1, 0, false, false);
			openOrdersTableModel.setValueAt("COMPLETED", indexes.get(clientId), STATUS_COL);
			indexes.remove(clientId);			
		}
	}
	
	public void markFilled(String clientId) {
		if (isUpdating) {
			openOrdersTable.changeSelection(openOrdersTable.getRowCount() - 1, 0, false, false);
			openOrdersTableModel.setValueAt("FILLED", indexes.get(clientId), STATUS_COL);
		}
	}
	
	public void setUpdating(boolean isUpdating) {
		this.isUpdating = isUpdating;
	}
}
