package kaqt.supersonic.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MDViewLauncher implements ActionListener {
	
	private StartScreen frm;
	
	public MDViewLauncher(StartScreen frm) {
		super();
		this.frm = frm;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		frm.launchMdQuotesView();
	}
}
