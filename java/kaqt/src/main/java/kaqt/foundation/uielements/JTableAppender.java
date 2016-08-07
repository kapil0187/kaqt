package kaqt.foundation.uielements;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Arrays;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

public class JTableAppender extends AppenderSkeleton
{
	private JTable component;
	private DefaultTableModel model;
	private int maxRows = 9;

	public JTableAppender(JTable component)
	{
		this.component = component;

		component.setDefaultRenderer(Object.class, new CustomRenderer());
		model = new DefaultTableModel()
		{
			private static final long serialVersionUID = 1517207045150848944L;

			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};

		model.addColumn("Level");
		model.addColumn("Message");

		component.setModel(model);
		component.getColumn("Level").setWidth(40);
		component.getColumn("Level").setMaxWidth(70);
	}

	class CustomRenderer extends DefaultTableCellRenderer
	{
		private static final long serialVersionUID = 2053598198333396752L;
		private Color erroFg = new Color(222, 55, 55);
		private Color warnFg = new Color(200, 200, 72);
		private Color evenBg = new Color(230, 230, 230);
		private Color infoFg = new Color(10, 140, 10);

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column)
		{
			Component c = super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
			if (value instanceof Level)
			{
				if (Level.ERROR.equals(value))
				{
					c.setForeground(Color.WHITE);
					c.setBackground(erroFg);
					c.setFont(c.getFont().deriveFont(Font.BOLD));
				}
				else if (Level.WARN.equals(value))
				{
					c.setForeground(Color.WHITE);
					c.setBackground(warnFg);
					c.setFont(c.getFont().deriveFont(Font.BOLD));
				}
				else
				{
					c.setForeground(infoFg);
					c.setBackground(row % 2 > 0 ? Color.WHITE : evenBg);
				}

			}
			else
			{
				c.setForeground(Color.BLACK);
				c.setBackground(row % 2 > 0 ? Color.WHITE : evenBg);
			}
			return c;
		}
	}

	protected void append(LoggingEvent event)
	{
		if (model.getRowCount() > maxRows)
		{
			model.removeRow(0);
		}
		model.addRow(new Object[]
		{
				event.getLevel(),
				String.format(
						"<html><font color=gray>[%s]</font> %s %s</html>",
						event.getLoggerName(),
						event.getRenderedMessage(),
						event.getThrowableStrRep() == null ? "" : " "
								+ Arrays.toString(event.getThrowableStrRep())) });

		component.scrollRectToVisible(component.getCellRect(
				component.getRowCount() - 1, 0, true));

	}

	public void close()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean requiresLayout()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

}