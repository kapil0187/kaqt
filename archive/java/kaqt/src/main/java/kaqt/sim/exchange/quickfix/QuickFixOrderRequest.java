package kaqt.sim.exchange.quickfix;

import quickfix.FieldNotFound;
import quickfix.field.ClOrdID;
import quickfix.field.MsgType;
import quickfix.field.OrderQty;
import quickfix.field.OrigClOrdID;
import quickfix.field.Price;
import quickfix.field.Side;
import quickfix.Message;
import kaqt.foundation.DoubleParameter;
import kaqt.foundation.Parameter;
import kaqt.sim.exchange.IOrderRequest;
import kaqt.sim.exchange.OrderRequestType;
import kaqt.sim.exchange.OrderSide;

public class QuickFixOrderRequest implements IOrderRequest {
	
	Message message;
	
	public QuickFixOrderRequest(Message message) {
		this.message = message;
	}

	public OrderSide getSide() {
		Side side = new Side();
		try {
			message.getField(side);
		}
		catch (FieldNotFound ex) {
			ex.printStackTrace();
		}
		OrderSide rv;
		
		switch (side.getValue()) {		
			case '1':  rv = OrderSide.BUY;
			break;
			case '2' : rv = OrderSide.SELL;
			break;
			case '5': rv = OrderSide.SELLS;
			break;
			case '6': rv = OrderSide.SELLSE;
			break;
			default: rv = OrderSide.INVALID;
			break;
		}
		return rv;
	}

	public OrderRequestType getRequestType() {
		MsgType msgType = new MsgType();
		String type = null;
		try {
			type = message.getHeader().getField(msgType).getValue();
		} catch (FieldNotFound e) {
			e.printStackTrace();
		}
		OrderRequestType rv = OrderRequestType.INVALID;
		if (type == "D") {
			rv = OrderRequestType.NEWORDERSINGLE;
		} else if (type == "F") {
			rv = OrderRequestType.CANCEL;
		} else if (type == "G") {
			rv = OrderRequestType.CANCELREPLACE;
		} else {
			rv = OrderRequestType.INVALID;
		}
		return rv;
	}

	public int getInstrumentId() {
		return 1;
	}

	public Parameter<String> getOrigClOrdId() {
		Parameter<String> rv = new Parameter<String>();
		if (this.getRequestType() != OrderRequestType.CANCEL) {
			rv.IsValid = false;
			return rv;
		}
		OrigClOrdID id = new OrigClOrdID();
		try {
			message.getField(id);
		}
		catch (FieldNotFound ex) {
			ex.printStackTrace();
		}
		rv.Value = id.getValue();
		rv.IsValid = true;
		return rv;
	}

	public DoubleParameter getQuantity() {
		DoubleParameter rv = new DoubleParameter();
		if (this.getRequestType() == OrderRequestType.CANCEL) {
			rv.IsValid = false;
			return rv;
		}
		OrderQty qty = new OrderQty();
		try {
			message.getField(qty);
		}
		catch (FieldNotFound ex) {
			ex.printStackTrace();
		}
		rv.Value = qty.getValue();
		rv.IsValid = true;
		return rv;
	}

	public DoubleParameter getPrice() {
		DoubleParameter rv = new DoubleParameter();
		if (this.getRequestType() == OrderRequestType.CANCEL) {
			rv.IsValid = false;
			return rv;
		}
		Price price = new Price();
		try {
			message.getField(price);
		}
		catch (FieldNotFound ex) {
			ex.printStackTrace();
		}
		rv.Value = price.getValue();
		rv.IsValid = true;
		return rv;
	}
	
	public String getClOrdId() {
		ClOrdID orderId = new ClOrdID();
		try {
			message.getField(orderId);
		}
		catch (FieldNotFound ex) {
			ex.printStackTrace();
		}
		return orderId.getValue();
	}
	
	public String toString() {
		return message.toString();
	}
}
