package kaqt.supersonic.ui;

import javax.swing.table.DefaultTableModel;

public class ReadOnlyTableModel extends DefaultTableModel {
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}
