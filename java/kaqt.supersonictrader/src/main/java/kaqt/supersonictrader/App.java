package kaqt.supersonictrader;

import java.awt.EventQueue;

import javax.swing.UIManager;

import kaqt.supersonictrader.ui.ControlWindow;

public class App 
{
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
					ControlWindow frame = new ControlWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
