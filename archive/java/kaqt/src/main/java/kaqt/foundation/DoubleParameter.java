package kaqt.foundation;

public class DoubleParameter extends Parameter<Double> implements Comparable<Double> {
	
	public DoubleParameter() {
		super();
	}
	
	public int compareTo(Double o) {
		if (this.Value == o) {
			return 0;
		} else if (this.Value > o) {
			return 1;
		} else {
			return -1;
		}
	}

}
