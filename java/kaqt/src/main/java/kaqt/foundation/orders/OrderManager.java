package kaqt.foundation.orders;

public interface OrderManager
{
	public Order create(OrderRequest request);
	public void send(Order order);
	public void send(OrderRequest request);
	public void cancel(CancelRequest request);
	public void replace(ReplaceRequest request);
}
