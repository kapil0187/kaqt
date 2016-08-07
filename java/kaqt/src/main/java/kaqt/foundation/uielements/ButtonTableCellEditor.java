package kaqt.foundation.uielements;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ButtonTableCellEditor extends AbstractCellEditor implements
		TableCellEditor
{
	private JButton button;
	
	public ButtonTableCellEditor(ActionListener al, String buttonText)
	{
		this.button = new JButton(buttonText);
		this.button.addActionListener(al);
	}
	
	@Override
	public Object getCellEditorValue()
	{
		return null;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
