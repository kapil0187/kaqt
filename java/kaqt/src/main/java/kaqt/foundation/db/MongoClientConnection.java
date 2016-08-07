package kaqt.foundation.db;

import kaqt.foundation.Constants;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

public class MongoClientConnection
{
	private ApplicationContext context;

	protected MongoOperations mongoOperations;

	public MongoClientConnection(String configFile)
	{
		this.context = new GenericXmlApplicationContext(configFile);
		this.mongoOperations = (MongoOperations) context
				.getBean(Constants.MONGO_DB_SPRING_TEMPLATE);
	}

}
