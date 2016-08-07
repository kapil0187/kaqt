package kaqt.sim.exchange;

import kaqt.foundation.DoubleParameter;
import kaqt.foundation.Parameter;

public interface IOrderRequest {
	OrderSide getSide();
	OrderRequestType getRequestType();
	int getInstrumentId();
	DoubleParameter getQuantity();
	DoubleParameter getPrice();
	Parameter<String> getOrigClOrdId();
	String getClOrdId();
	String toString();
}
