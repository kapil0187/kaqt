package kaqt.foundation;

import java.util.Properties;

public abstract class Router
{
	protected Properties properties;
	
	public Router(Properties properties)
	{
		this.properties = properties;
	}

	public Properties getProperties()
	{
		return properties;
	}
	
	public abstract void initialize();
	
	public abstract void shutdown();

}
