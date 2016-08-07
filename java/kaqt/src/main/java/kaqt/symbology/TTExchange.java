package kaqt.symbology;

import kaqt.foundation.symbology.IExchange;

public class TTExchange implements IExchange
{
	private String exchangeName;
	
	public TTExchange(String name)
	{
		this.exchangeName = name;
	}
	
	public String getName()
	{
		return this.exchangeName;
	}
}
