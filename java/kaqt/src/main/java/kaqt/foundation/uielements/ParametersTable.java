package kaqt.foundation.uielements;

import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

public class ParametersTable extends JTable
{
	private Map<Integer, TableCellEditor> editors;

	public ParametersTable(TableModel model, Map<Integer, TableCellEditor> editors)
	{
		super(model);
		this.editors = editors;
	}

	@Override
	public TableCellEditor getCellEditor(int row, int column)
	{
		if (editors.containsKey(row))
			return editors.get(row);
		else
			return super.getCellEditor(row, column);
	}
}
