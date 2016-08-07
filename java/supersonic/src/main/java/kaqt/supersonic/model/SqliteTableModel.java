package kaqt.supersonic.model;

import java.sql.Connection;

import javax.swing.table.AbstractTableModel;

public abstract class SqliteTableModel extends AbstractTableModel {
	private Connection conn;
}
