package kaqt.supersonic.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import kaqt.supersonic.model.FillOrderUpdate;
import kaqt.supersonic.model.UiConstants;
import net.miginfocom.swing.MigLayout;

public class FillsView extends JFrame {

	final int CLIENT_ID_COL = 0;
	final int ORDER_ID_COL = 1;
	final int TIME_COL = 2;
	final int SYMBOL_COL = 3;
	final int FILL_QTY_COL = 4;
	final int FILL_PRICE_COL = 5;
	final int TYPE_COL = 6;
	
	private JPanel contentPane;
	private JTable fillsTable;
	private DefaultTableModel fillsTableModel;

	public FillsView() {
		setFont(new Font("Cambria Math", Font.PLAIN, 11));
		setIconImage(Toolkit.getDefaultToolkit().getImage(FillsView.class.getResource("/kaqt/supersonic/resources/supersonic.jpg")));
		setTitle("FILLS");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[][grow]"));
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, "cell 0 1,grow");
		
		fillsTableModel = new ReadOnlyTableModel();
		
		for (String colName : UiConstants.FILLS_COLUMN_HEADERS) {
			fillsTableModel.addColumn(colName);
		}
		
		fillsTable = new JTable(fillsTableModel);
		fillsTable.setFont(new Font("Cambria Math", Font.PLAIN, 11));
		fillsTable.setForeground(new Color(255, 215, 0));
		scrollPane.setViewportView(fillsTable);
	}

	public void addFillUpdate(FillOrderUpdate fou) {
		fillsTableModel.addRow(new Object[] {
				fou.getClient_id(),
				fou.getOrder_id(),
				fou.getSymbol(),
				fou.getTimestamp(),
				fou.getFill().getQuantity(),
				new BigDecimal(fou.getFill().getPrice()).setScale(3, RoundingMode.HALF_DOWN),
				fou.getFill().getType()
		});
		fillsTable.scrollRectToVisible(fillsTable.getCellRect(fillsTable.getRowCount() - 1, 0, true));
	}		
}
