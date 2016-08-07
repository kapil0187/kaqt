package kaqt.edutils.app;

import java.awt.EventQueue;

import javax.swing.UIManager;

import kaqt.edutils.ui.EurodollarAnalysisWindow;

public class App 
{
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
					EurodollarAnalysisWindow frame = new EurodollarAnalysisWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
