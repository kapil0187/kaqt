package kaqt.foundation.db;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Field;

public class DbAlternateId
{
	@Field("type")
	private String type;
	
	@Field("id")
	private String id;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
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