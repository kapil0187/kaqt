package kaqt.sim.exchange;

public enum OrderSide
{
	INVALID(0),
	BUY(1),
	SELL(2),
	SELLS(3),
	SELLSE(4);
	
    private final int value;
    private OrderSide(int value) {
    	this.value = value;
	}
    public int getValue() { 
    	return value; 
	}
}
