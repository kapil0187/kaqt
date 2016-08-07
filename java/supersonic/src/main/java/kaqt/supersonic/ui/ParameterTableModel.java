package kaqt.supersonic.ui;

import javax.swing.table.DefaultTableModel;

public class ParameterTableModel extends DefaultTableModel {
	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == 0) {
			return true;
		} else {
			return false;
		}
	}
}
