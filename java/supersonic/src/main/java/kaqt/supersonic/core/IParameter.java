package kaqt.supersonic.core;

public interface IParameter {
	public String getDefinition();
	public <T> T getValue(Class<T> cls);
}

