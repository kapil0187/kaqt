package kaqt.symbology;

import kaqt.foundation.symbology.IProduct;

public class TTProduct implements IProduct
{
	private String productName;
	
	public TTProduct(String name)
	{
		this.productName = name;
	}
	
	public String getProductType()
	{
		return this.productName;
	}
	
}
