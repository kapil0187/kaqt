package kaqt.supersonictrader.model;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;

public class ContextSingleton {
	private static final Context ctx = ZMQ.context(1);
	
	public static Context getInstance() {
		return ctx;
	}
}
