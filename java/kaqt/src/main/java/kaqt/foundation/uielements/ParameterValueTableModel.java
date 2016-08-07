package kaqt.foundation.uielements;

import javax.swing.table.DefaultTableModel;

public class ParameterValueTableModel extends DefaultTableModel
{
	@Override
	public boolean isCellEditable(int row, int column)
	{
		boolean rv = true;

		if (column == 0)
		{
			rv = false;
		}

		return rv;
	}
}
