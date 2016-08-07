package kaqt.ui.supersonic;

import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import kaqt.foundation.uielements.ReadOnlyTableModel;
import kaqt.providers.protobuf.Symbology.FuturesInstrument;
import kaqt.utils.TableColumnNames;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SymbologyView extends JFrame
{
	private ControlWindow parent;

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JSplitPane splitPane;

	// TABLES
	private JScrollPane symbologyDatabaseSP;
	private JTable symbologyDatabaseTable;
	private DefaultTableModel symbologyDbTableModel;

	private JScrollPane instrumentDetailSP;
	private JTable instrumentDetailTable;
	private DefaultTableModel instrumentDetailTableModel;

	// RIGHT CLICK
	private JPopupMenu rightClickMenu;
	private JMenuItem sendToMdView;

	public SymbologyView(ControlWindow parent)
	{
		this.parent = parent;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1000, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("",
				"[150px,fill][100px,fill][300px,grow,fill]",
				"[200px,grow,fill]"));

		splitPane = new JSplitPane();
		contentPane.add(splitPane, "cell 0 0 3 1,grow");

		symbologyDatabaseSP = new JScrollPane();
		splitPane.setLeftComponent(symbologyDatabaseSP);
		splitPane.setDividerLocation(500);

		symbologyDbTableModel = new ReadOnlyTableModel();
		TableColumnNames.SYMBOLOGY.stream().forEach(
				c -> symbologyDbTableModel.addColumn(c));
		symbologyDatabaseTable = new JTable(symbologyDbTableModel);
		symbologyDatabaseTable
				.setFont(new Font("Cambria Math", Font.PLAIN, 10));
		symbologyDatabaseSP.setViewportView(symbologyDatabaseTable);

		instrumentDetailSP = new JScrollPane();
		splitPane.setRightComponent(instrumentDetailSP);

		instrumentDetailTableModel = new DefaultTableModel();
		TableColumnNames.PARAMETER_VALUE_TABLE.stream().forEach(
				c -> instrumentDetailTableModel.addColumn(c));
		instrumentDetailTable = new JTable(instrumentDetailTableModel);
		instrumentDetailTable.setFont(new Font("Cambria Math", Font.BOLD, 12));
		instrumentDetailSP.setViewportView(instrumentDetailTable);

		symbologyDatabaseTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener()
				{
					@Override
					public void valueChanged(ListSelectionEvent e)
					{
						int selectedId = (int) symbologyDatabaseTable.getValueAt(
								symbologyDatabaseTable.getSelectedRow(),
								SymbologyDbTableIndices.ID_COL);
						FuturesInstrument fi = parent.getInstruments().get(
								selectedId);
						populateInstrDetailTable(fi);
					}
				});

		this.addRightClickMenu();
		this.addDoubleClickListener();
	}

	private void sendToMarketDataView(int id)
	{
		parent.subscribeToMktdata(parent.getInstruments().get(id));
	}

	public void populateInstrumentsTable(
			Map<Integer, FuturesInstrument> instruments)
	{
		instruments.forEach((k, v) -> symbologyDbTableModel.addRow(new Object[]
		{ v.getId(), v.getTicker(), v.getDescription(), v.getExchange() }));
	}

	private void populateInstrDetailTable(FuturesInstrument fi)
	{
		if (instrumentDetailTable.getRowCount() > 0)
		{
			for (int i = instrumentDetailTable.getRowCount(); i > 0; --i)
			{
				instrumentDetailTableModel.removeRow(i - 1);
			}
		}

		instrumentDetailTableModel.addRow(new Object[]
		{ "ID", fi.getId() });
		instrumentDetailTableModel.addRow(new Object[]
		{ "TICKER", fi.getTicker() });
		instrumentDetailTableModel.addRow(new Object[]
		{ "UNDERLYING", fi.getUnderlying() });
		instrumentDetailTableModel.addRow(new Object[]
		{ "DESCRIPTION", fi.getDescription() });
		instrumentDetailTableModel.addRow(new Object[]
		{ "EXCHANGE GROUP", fi.getExchangeGroup() });
		instrumentDetailTableModel.addRow(new Object[]
		{ "EXCHANGE", fi.getExchange() });
		instrumentDetailTableModel.addRow(new Object[]
		{ "MIN ORDER SIZE", fi.getMinOrderSize() });
		instrumentDetailTableModel.addRow(new Object[]
		{ "TICK SIZE", fi.getTickSize() });
		instrumentDetailTableModel.addRow(new Object[]
		{ "TRADEABLE TICK SIZE", fi.getTradeableTickSize() });
		instrumentDetailTableModel.addRow(new Object[]
		{ "CURRENCY", fi.getCurrency() });

		fi.getAlternateIdList().stream()
				.forEach(id -> instrumentDetailTableModel.addRow(new Object[]
				{ String.format("%s ID", id.getIdType()), id.getId() }));
	}

	private class SymbologyDbTableIndices
	{
		public static final int ID_COL = 0;
		// public static final int DESCRIPTION_COLL = 1;
		// public static final int EXCHANGE_COL = 2;
	}

	private void addDoubleClickListener()
	{
		symbologyDatabaseTable.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent me)
			{
				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				int row = table.rowAtPoint(p);
				if (me.getClickCount() == 2)
				{
					sendToMarketDataView((int) symbologyDatabaseTable
							.getValueAt(
									symbologyDatabaseTable.getSelectedRow(),
									SymbologyDbTableIndices.ID_COL));
				}

				if (SwingUtilities.isRightMouseButton(me))
				{
					ListSelectionModel model = table.getSelectionModel();
					model.setSelectionInterval(row, row);
				}
			}
		});
	}

	private void addRightClickMenu()
	{
		rightClickMenu = new JPopupMenu();
		sendToMdView = new JMenuItem("Market Data");
		sendToMdView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{

			}
		});
		rightClickMenu.add(sendToMdView);
		symbologyDatabaseTable.setComponentPopupMenu(rightClickMenu);
	}
}
