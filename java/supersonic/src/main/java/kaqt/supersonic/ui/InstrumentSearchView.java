package kaqt.supersonic.ui;

import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.TableRowSorter;

import kaqt.supersonic.model.Instrument;
import kaqt.supersonic.model.UiConstants;
import kaqt.supersonic.ui.utils.UiColors;
import kaqt.supersonic.ui.utils.VertGradientLabel;
import net.miginfocom.swing.MigLayout;

public class InstrumentSearchView extends JFrame {

	private StartScreen frm;

	private JPanel contentPane;
	private JTextField searchField;
	private JTable instrumentsSearchTable;
	private JPopupMenu rightClickMenu;
	private JMenuItem detailsMenuItem;
	private RowFilter<ReadOnlyTableModel, Object> rf;
	private TableRowSorter<ReadOnlyTableModel> sorter;

	private Map<String, Instrument> instruments;

	private ReadOnlyTableModel instSrchTableModel;

	public InstrumentSearchView(Map<String, Instrument> instruments,
			StartScreen frm) {
		this.instruments = instruments;
		this.frm = frm;

		setTitle("INSTRUMENTS");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit
				.getDefaultToolkit()
				.getImage(
						InstrumentSearchView.class
								.getResource("/kaqt/supersonic/resources/supersonic.jpg")));
		setBounds(100, 100, 580, 437);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[72.00,fill][439.00,grow,fill]", "[35px:n,fill][35px:n,fill][35px:n,fill][grow]"));

		JLabel lblInstrument = new JLabel("INSTRUMENT   ");
		lblInstrument.setForeground(UiColors.GOLDEN_TEXT);
		lblInstrument.setFont(new Font("Cambria Math", Font.BOLD, 13));
		contentPane.add(lblInstrument, "cell 0 0,alignx left");

		searchField = new JTextField();

		searchField.setFont(new Font("Cambria Math", Font.BOLD, 11));
		searchField.setToolTipText("Search Instrument");
		contentPane.add(searchField, "cell 1 0,growx");
		searchField.setColumns(10);

		JLabel lblSearchResults = new VertGradientLabel("SEARCH RESULTS");
		lblSearchResults.setHorizontalAlignment(SwingConstants.CENTER);
		lblSearchResults.setForeground(UiColors.GOLDEN_TEXT);
		lblSearchResults.setFont(new Font("Cambria Math", Font.BOLD, 13));
		contentPane.add(lblSearchResults, "cell 0 1 2 1");

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, "cell 0 2 2 2,grow");

		instSrchTableModel = new ReadOnlyTableModel();

		for (String colName : UiConstants.INSTRUMENT_SEARCH_HEADERS) {
			instSrchTableModel.addColumn(colName);
		}

		instrumentsSearchTable = new JTable(instSrchTableModel);
		instrumentsSearchTable
				.setFont(new Font("Cambria Math", Font.PLAIN, 11));

		for (String key : instruments.keySet()) {
			Instrument inst = instruments.get(key);
			instSrchTableModel.addRow(new Object[] { key, inst.getInstrument(),
					inst.getExchange(), inst.getDescription(),
					inst.getMaturity_date() });
		}

		scrollPane.setColumnHeaderView(instrumentsSearchTable);
		scrollPane.setViewportView(instrumentsSearchTable);

		sorter = new TableRowSorter<ReadOnlyTableModel>(instSrchTableModel);

		instrumentsSearchTable.setRowSorter(sorter);

		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String text = searchField.getText().toUpperCase();
				if (text.length() == 0) {
					sorter.setRowFilter(null);
				} else {
					sorter.setRowFilter(RowFilter.regexFilter(text));
				}
			}
		});

		instrumentsSearchTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				int row = table.rowAtPoint(p);
				if (me.getClickCount() == 2) {
					frm.addInstrumentToMDView(instrumentsSearchTable
							.getValueAt(
									instrumentsSearchTable.getSelectedRow(), 1)
							.toString());
				}

				if (SwingUtilities.isRightMouseButton(me)) {
					ListSelectionModel model = table.getSelectionModel();
					model.setSelectionInterval(row, row);
				}
			}
		});

		addPopUpMenu();
	}

	private void launchInstrumentDetails() {
		InstrumentDetails details = new InstrumentDetails(
				instruments.get(instrumentsSearchTable.getValueAt(
						instrumentsSearchTable.getSelectedRow(), 0)));
		details.setVisible(true);
	}

	private void addPopUpMenu() {
		rightClickMenu = new JPopupMenu();
		detailsMenuItem = new JMenuItem("DETAILS");
		detailsMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				launchInstrumentDetails();
			}
		});
		rightClickMenu.add(detailsMenuItem);
		instrumentsSearchTable.setComponentPopupMenu(rightClickMenu);
	}

}
