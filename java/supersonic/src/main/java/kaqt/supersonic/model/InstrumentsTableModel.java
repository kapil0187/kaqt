package kaqt.supersonic.model;

import java.util.ArrayList;

public class InstrumentsTableModel extends SqliteTableModel {

	private ArrayList<Instrument> data;
	
	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return Instrument.class.getDeclaredFields().length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return null;
	}

}
