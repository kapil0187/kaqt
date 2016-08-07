package kaqt.supersonic.ui;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class AlgoSelectionListListener implements ListSelectionListener {

	StartScreen frm;
	
	public AlgoSelectionListListener(StartScreen frm) {
		super();
		this.frm = frm;
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		frm.updateParameters();
	}
}
