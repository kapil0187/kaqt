package kaqt.apps.coinbase.marketdata;

import kaqt.marketdata.coinbase.LevelOneQuote;
import rx.Subscriber;

public class LevelOneQuoteSubscriber extends Subscriber<LevelOneQuote> {

	@Override
	public void onCompleted() {
		System.out.println("OnCompleted");
		
	}

	@Override
	public void onError(Throwable e) {
		System.out.println(e.getMessage());
	}

	@Override
	public void onNext(LevelOneQuote t) {
		System.out.println(t.toString());
	}

}
