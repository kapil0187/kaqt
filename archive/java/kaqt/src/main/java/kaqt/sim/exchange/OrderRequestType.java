package kaqt.sim.exchange;

public enum OrderRequestType {
	INVALID(0),
	NEWORDERSINGLE(1),
	CANCEL(2),
	CANCELREPLACE(3);
	
    private final int value;
    private OrderRequestType(int value) {
    	this.value = value;
	}
    public int getValue() { 
    	return value; 
	}
}
