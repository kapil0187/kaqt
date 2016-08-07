package kaqt.supersonic.app;

import java.awt.EventQueue;

import javax.swing.UIManager;

import kaqt.supersonic.model.AlgorithmFactory;
import kaqt.supersonic.model.StrategyConfiguration;
import kaqt.supersonic.ui.StartScreen;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class App {
	
	private static String CONFIG_FILENAME = "src/main/java/kaqt/supersonic/config/strategy_configs.json";
	private static Logger log = Logger.getLogger(App.class.getName());
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					PropertyConfigurator.configure("src/main/java/kaqt/supersonic/resources/log4j.properties");
					StrategyConfiguration sc = new StrategyConfiguration(CONFIG_FILENAME);
					AlgorithmFactory algoFactory = new AlgorithmFactory(sc);
					UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
					StartScreen frame = new StartScreen(algoFactory);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
