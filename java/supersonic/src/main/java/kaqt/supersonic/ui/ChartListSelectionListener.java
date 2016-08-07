package kaqt.supersonic.ui;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ChartListSelectionListener implements ListSelectionListener {
	private StartScreen frm;
	
	public ChartListSelectionListener(StartScreen frm) {
		this.frm  = frm;
	}
	
	public void valueChanged(ListSelectionEvent e) 
	{
		if(e.getValueIsAdjusting() == false) {
			frm.updateImage();
		}
	}
	
}
