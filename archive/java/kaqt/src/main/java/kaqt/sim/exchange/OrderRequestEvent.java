package kaqt.sim.exchange;

public class OrderRequestEvent {
	private IOrderRequest request;
	
	public void set(IOrderRequest request) {
		this.request = request;
	}
	
	public IOrderRequest get() {
		return request;
	}
}
