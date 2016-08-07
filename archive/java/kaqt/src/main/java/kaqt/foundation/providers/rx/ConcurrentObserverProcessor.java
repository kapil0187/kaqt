package kaqt.foundation.providers.rx;

import kaqt.foundation.ConcurrentHashSet;
import rx.Observable;
import rx.Subscriber;

public class ConcurrentObserverProcessor<T> implements Observable.OnSubscribe<T> {

	ConcurrentHashSet<Subscriber<? super T>> subscribers;
	
	public ConcurrentObserverProcessor() {
		super();
		subscribers = new ConcurrentHashSet<Subscriber<? super T>>();
	}
	
	@Override
	public void call(Subscriber<? super T> subscriber) {
		subscribers.put(subscriber);
	}
	
	public void unsubscribe(Subscriber<? super T> subscriber) {
		subscriber.unsubscribe();
		subscribers.remove(subscriber);
	}
	
	public void onNext(T update) {
		for (Subscriber<? super T> s : subscribers.getValues()) {
			s.onNext(update);
		}
	}

	public void onCompleted() {
		for (Subscriber<? super T> s : subscribers.getValues()) {
			s.onCompleted();
		}
	}
	
	public void onError(Throwable t) {
		for (Subscriber<? super T> s : subscribers.getValues()) {
			s.onError(t);
		}
	}
}
