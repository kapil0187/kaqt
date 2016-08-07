package kaqt.supersonic.model;

import java.util.ArrayList;

import kaqt.supersonic.core.IParameter;

public class Algorithm {
	private String algoName;
	private ArrayList<IParameter> params;
	
	public Algorithm(String algoName, ArrayList<IParameter> params) {
		super();
		this.algoName = algoName;
		this.params = params;
	}

	public String getAlgoName() {
		return algoName;
	}

	public ArrayList<IParameter> getParams() {
		return params;
	}
}
