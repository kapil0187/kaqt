package kaqt.supersonic.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kaqt.supersonic.core.DoubleParameter;
import kaqt.supersonic.core.IParameter;
import kaqt.supersonic.core.IntegerParameter;
import kaqt.supersonic.core.StringParameter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class AlgorithmFactory {
	private StrategyConfiguration config;
	private Map<String, Algorithm> algorithms;

	public AlgorithmFactory(StrategyConfiguration config) throws ClassNotFoundException {
		super();
		this.config = config;
		this.algorithms = new HashMap<String, Algorithm>();
		this.registerStrategies();
	}

	private void registerStrategies() throws ClassNotFoundException {
		JsonParser parser = new JsonParser();
		ArrayList<String> strategies = config.getConfigs();
		for (String strategy : strategies) {
			ArrayList<IParameter> params = new ArrayList<IParameter>();
			JsonArray paramsJson = parser.parse(strategy).getAsJsonObject()
					.get("params").getAsJsonArray();
			Iterator<JsonElement> iterator = paramsJson.iterator();
			while (iterator.hasNext()) {
				JsonElement nextElement = iterator.next();
				String paramName = nextElement.getAsJsonObject().entrySet().iterator().next().getKey();
				String paramType = nextElement.getAsJsonObject().get(paramName).getAsJsonObject().get("type").getAsString();
				IParameter param = null;
				if (paramType.equals("Integer")) {
					param = new IntegerParameter(paramName, 
							nextElement.getAsJsonObject().get(paramName).getAsJsonObject().get("value").getAsInt());				
				} else if (paramType.equals("Double")) {
					param = new DoubleParameter(paramName, 
							nextElement.getAsJsonObject().get(paramName).getAsJsonObject().get("value").getAsDouble());				
				} else if (paramType.equals("String")) {
					param = new StringParameter(paramName, 
							nextElement.getAsJsonObject().get(paramName).getAsJsonObject().get("value").getAsString());
				}

				params.add(param);
			}
			String stratName = parser.parse(strategy).getAsJsonObject()
					.get("strategy_name").getAsString();
			
			Algorithm algo = new Algorithm(stratName, params);
			algorithms.put(stratName, algo);
		}
	}
	
	public Collection<Algorithm> getAlgos() {
		return algorithms.values();
	}
	
	public Algorithm buildAlgorithm(String name) {
		return algorithms.get(name);
	}
}
