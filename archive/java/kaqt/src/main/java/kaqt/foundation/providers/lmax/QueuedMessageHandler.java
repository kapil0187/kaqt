package kaqt.foundation.providers.lmax;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

import kaqt.foundation.EventArgs;
import kaqt.foundation.MessageHandler;

public class QueuedMessageHandler<T> implements MessageHandler<T> {
	
	private final RingBuffer<EventArgs<T>> ringbuffer;

	public QueuedMessageHandler(RingBuffer<EventArgs<T>> ringbuffer) {
		super();
		this.ringbuffer = ringbuffer;
	}

	private final EventTranslatorOneArg<EventArgs<T>, T> TRANSLATOR = new EventTranslatorOneArg<EventArgs<T>, T>() {
		public void translateTo(EventArgs<T> event, long sequence, T message) {
			event.set(message);
		}
	};
	
	@Override
	public void handleMessage(T message) {
		ringbuffer.publishEvent(TRANSLATOR, message);
	}	
}
