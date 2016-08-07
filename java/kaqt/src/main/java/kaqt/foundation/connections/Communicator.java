package kaqt.foundation.connections;

import java.util.Map;

import kaqt.foundation.interfaces.ICommunicator;

public abstract class Communicator implements ICommunicator
{
	private Map<ClientType, String> connections;
	
	public Communicator(Map<ClientType, String> connections)
	{
		this.connections = connections;
	}
}
