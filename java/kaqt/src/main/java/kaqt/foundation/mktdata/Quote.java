package kaqt.foundation.mktdata;

public class Quote 
{
	int id;
	double bidPrice;
	double askPrice;
	double lastPrice;
	int bidQty;
	int askQty;
	int lastQty;
	long posixTime;
	
	public Quote() {
		this(0, 0.0, 0.0, 0.0, 0, 0, 0, 0);
	}
	
	public Quote(int id, double bidPrice, double askPrice, double lastPrice,
			int bidQty, int askQty, int lastQty, long posixTime)
	{
		super();
		this.id = id;
		this.bidPrice = bidPrice;
		this.askPrice = askPrice;
		this.lastPrice = lastPrice;
		this.bidQty = bidQty;
		this.askQty = askQty;
		this.lastQty = lastQty;
		this.posixTime = posixTime;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public double getBidPrice()
	{
		return bidPrice;
	}

	public void setBidPrice(double bidPrice)
	{
		this.bidPrice = bidPrice;
	}

	public double getAskPrice()
	{
		return askPrice;
	}

	public void setAskPrice(double askPrice)
	{
		this.askPrice = askPrice;
	}

	public double getLastPrice()
	{
		return lastPrice;
	}

	public void setLastPrice(double lastPrice)
	{
		this.lastPrice = lastPrice;
	}

	public int getBidQty()
	{
		return bidQty;
	}

	public void setBidQty(int bidQty)
	{
		this.bidQty = bidQty;
	}

	public int getAskQty()
	{
		return askQty;
	}

	public void setAskQty(int askQty)
	{
		this.askQty = askQty;
	}

	public int getLastQty()
	{
		return lastQty;
	}

	public void setLastQty(int lastQty)
	{
		this.lastQty = lastQty;
	}

	public long getPosixTime()
	{
		return posixTime;
	}

	public void setPosixTime(long posixTime)
	{
		this.posixTime = posixTime;
	}

	@Override
	public String toString()
	{
		return "Quote [id=" + id + ", bidPrice=" + bidPrice + ", askPrice="
				+ askPrice + ", lastPrice=" + lastPrice + ", bidQty=" + bidQty
				+ ", askQty=" + askQty + ", lastQty=" + lastQty
				+ ", posixTime=" + posixTime + "]";
	}
}
