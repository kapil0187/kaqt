package kaqt.foundation.uielements;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class SpinnerTableCellEditor extends AbstractCellEditor implements
		TableCellEditor, TableCellRenderer, ActionListener, MouseListener
{
	private static final long serialVersionUID = 1L;

	private JSpinner spinner;

	public SpinnerTableCellEditor(SpinnerNumberModel model)
	{
		spinner = new JSpinner(model);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column)
	{

		if (isSelected)
		{
			spinner.setValue(value);
			TableModel model = table.getModel();
			model.setValueAt(value, row, column);
		}

		return spinner;
	}

	@Override
	public Object getCellEditorValue()
	{
		return spinner.getValue();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		// TODO Auto-generated method stub
		return null;
	}
}