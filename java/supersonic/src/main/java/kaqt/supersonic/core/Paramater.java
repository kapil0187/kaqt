package kaqt.supersonic.core;

public class Paramater<T> implements IParameter {
	T value;
	String definition;
	
	public Paramater(String definition, T value) {
		this.definition = definition;
		this.value = value;
	}

	@Override
	public String getDefinition() {
		return definition;
	}

	@Override
	@SuppressWarnings("hiding")
	public <T> T getValue(Class<T> cls) {
	    if (value == null) return null;
	    else {
	        if (cls.isInstance(value)) return cls.cast(value);
	        return null;
	    }
	}
}