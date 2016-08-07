package kaqt.sim.exchange;

import com.lmax.disruptor.EventFactory;

public class OrderRequestEventFactory implements EventFactory<OrderRequestEvent>{

	public OrderRequestEvent newInstance() {
		return new OrderRequestEvent();
	}

}
