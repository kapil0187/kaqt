package kaqt.symbology;

import kaqt.foundation.symbology.ISecurity;

public class TTSecurity implements ISecurity
{
	private String securityName;
	
	public TTSecurity(String name)
	{
		this.securityName = name;
	}
	
	public String getTicker()
	{
		return this.securityName;
	}
}
