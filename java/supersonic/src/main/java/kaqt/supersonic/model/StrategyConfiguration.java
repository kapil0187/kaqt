package kaqt.supersonic.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class StrategyConfiguration {
	private String configFile;
	private ArrayList<String> configs;

	public StrategyConfiguration(String configFile) {
		this.configFile = configFile;
		this.configs = new ArrayList<String>();
	}

	public ArrayList<String> getConfigs() {
		try (BufferedReader br = new BufferedReader(new FileReader(
				this.configFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				configs.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return configs;
	}
}
