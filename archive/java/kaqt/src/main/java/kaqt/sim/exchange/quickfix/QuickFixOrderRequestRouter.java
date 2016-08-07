package kaqt.sim.exchange.quickfix;

import quickfix.Message;
import kaqt.foundation.EventArgs;
import kaqt.sim.exchange.IOrderRequest;
import kaqt.sim.exchange.OrderRequestEvent;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

public class QuickFixOrderRequestRouter implements EventHandler<EventArgs<Message>> {
	
	private final RingBuffer<OrderRequestEvent> ringbuffer;
	
	public QuickFixOrderRequestRouter(RingBuffer<OrderRequestEvent> ringbuffer) {
		this.ringbuffer = ringbuffer;
	}
	
    private static final EventTranslatorOneArg<OrderRequestEvent, IOrderRequest> TRANSLATOR =
            new EventTranslatorOneArg<OrderRequestEvent, IOrderRequest>()
            {
				public void translateTo(OrderRequestEvent event, long sequence,
						IOrderRequest request) {
					event.set(request);
				}
            };
	
	public void onEvent(EventArgs<Message> event, long sequence, boolean endOfBatch) {
		IOrderRequest request = new QuickFixOrderRequest(event.get());
		ringbuffer.publishEvent(TRANSLATOR, request);
	}
}
