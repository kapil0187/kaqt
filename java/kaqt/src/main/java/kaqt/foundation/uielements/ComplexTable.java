package kaqt.foundation.uielements;

import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

public class ComplexTable extends JTable
{
	private Map<Integer, TableCellEditor> columnEditors;

	public ComplexTable(TableModel model, Map<Integer, TableCellEditor> editors)
	{
		super(model);
		this.columnEditors = editors;
	}

	@Override
	public TableCellEditor getCellEditor(int row, int column)
	{
		if (columnEditors.containsKey(column))
			return columnEditors.get(column);
		else
			return super.getCellEditor(row, column);
	}
}
