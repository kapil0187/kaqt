package kaqt.orders;

import quickfix.SessionID;
import kaqt.foundation.connections.FixClient;
import kaqt.foundation.orders.CancelRequest;
import kaqt.foundation.orders.Order;
import kaqt.foundation.orders.OrderManager;
import kaqt.foundation.orders.OrderRequest;
import kaqt.foundation.orders.ReplaceRequest;

public class TTOrderFixClient extends FixClient implements OrderManager
{
	public TTOrderFixClient(String configFile)
	{
		super(configFile);
	}

	@Override
	public void onLogon(SessionID sessionId)
	{
		super.onLogon(sessionId);
	}
	
	public Order create(OrderRequest request)
	{
		return null;
	}

	public void send(Order order)
	{
		
	}

	public void send(OrderRequest request)
	{
	}

	public void cancel(CancelRequest request)
	{
	}

	public void replace(ReplaceRequest request)
	{
	}
}
