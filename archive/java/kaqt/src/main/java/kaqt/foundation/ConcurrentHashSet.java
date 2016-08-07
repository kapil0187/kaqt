package kaqt.foundation;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashSet<T> {
	ConcurrentHashMap<Integer, T> haskKeyMap;
	
	public ConcurrentHashSet() {
		haskKeyMap = new ConcurrentHashMap<Integer, T>();
	}
	
	public void put(T value) {
		haskKeyMap.put(value.hashCode(), value);
	}
	
	public Object remove(T value) {
		return haskKeyMap.remove(value.hashCode());
	}
	
	public Object get(T value) {
		return haskKeyMap.get(value.hashCode());
	}
	
	public Collection<T> getValues() {
		return haskKeyMap.values();
	}
	
}
