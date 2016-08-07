package kaqt.foundation.uielements;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

public class CheckboxTableCellEditor extends AbstractCellEditor implements TableCellEditor
{
	private JCheckBox checkbox;
	
	public CheckboxTableCellEditor()
	{
		checkbox = new JCheckBox();
	}
	
	@Override
	public Object getCellEditorValue()
	{
		String rv = "NO";
		if (checkbox.isSelected())
		{
			rv = "YES";
		}
		return rv;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column)
	{
		if (isSelected)
		{
			checkbox.setSelected((boolean) value);
			TableModel model = table.getModel();
			model.setValueAt(value, row, column);
		}

		return checkbox;
	}

}
