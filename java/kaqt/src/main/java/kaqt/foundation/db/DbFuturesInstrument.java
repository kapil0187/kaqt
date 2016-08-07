package kaqt.foundation.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="instruments")
public class DbFuturesInstrument
{
	@Id
	private String id;
	
	private String description;
	private String exchange;
	
	@Field("exchange_group")
	private String exchangeGroup;
	private String currency;
	private String underlying;
	private Integer key;
	
	@Field("alternate_ids")
	private List<DbAlternateId> alternateIds = new ArrayList<DbAlternateId>();
	
	private String ticker;
	
	@Field("tick_size")
	private Double tickSize;
	
	@Field("min_order_size")
	private Integer minOrderSize;
	
	@Field("expiry_posix_datetime")
	private Integer expiryPosixDatetime;
	
	@Field("tradeable_tick_size")
	private Double tradeableTickSize;
	
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getExchange()
	{
		return exchange;
	}

	public void setExchange(String exchange)
	{
		this.exchange = exchange;
	}

	public String getExchangeGroup()
	{
		return exchangeGroup;
	}

	public void setExchangeGroup(String exchangeGroup)
	{
		this.exchangeGroup = exchangeGroup;
	}

	public String getCurrency()
	{
		return currency;
	}

	public void setCurrency(String currency)
	{
		this.currency = currency;
	}

	public String getUnderlying()
	{
		return underlying;
	}

	public void setUnderlying(String underlying)
	{
		this.underlying = underlying;
	}

	public Integer getKey()
	{
		return key;
	}

	public void setKey(Integer key)
	{
		this.key = key;
	}

	public List<DbAlternateId> getAlternateIds()
	{
		return alternateIds;
	}

	public void setAlternateIds(List<DbAlternateId> alternateIds)
	{
		this.alternateIds = alternateIds;
	}

	public String getTicker()
	{
		return ticker;
	}

	public void setTicker(String ticker)
	{
		this.ticker = ticker;
	}

	public Double getTickSize()
	{
		return tickSize;
	}

	public void setTickSize(Double tickSize)
	{
		this.tickSize = tickSize;
	}

	public Integer getMinOrderSize()
	{
		return minOrderSize;
	}

	public void setMinOrderSize(Integer minOrderSize)
	{
		this.minOrderSize = minOrderSize;
	}

	public Integer getExpiryPosixDatetime()
	{
		return expiryPosixDatetime;
	}

	public void setExpiryPosixDatetime(Integer expiryPosixDatetime)
	{
		this.expiryPosixDatetime = expiryPosixDatetime;
	}

	public Double getTradeableTickSize()
	{
		return tradeableTickSize;
	}

	public void setTradeableTickSize(Double tradeableTickSize)
	{
		this.tradeableTickSize = tradeableTickSize;
	}

	public Map<String, Object> getAdditionalProperties()
	{
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value)
	{
		this.additionalProperties.put(name, value);
	}
}
