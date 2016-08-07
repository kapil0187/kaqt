package kaqt.foundation;

public class StringParameter extends Parameter<String> implements Comparable<String> {
	
	public StringParameter() {
		super();
	}
	
	public int compareTo(String o) {
		if (this.Value == o) {
			return 0;
		} else {
			return -1;
		}
	}
}
