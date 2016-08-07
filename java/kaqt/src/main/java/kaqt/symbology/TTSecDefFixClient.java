package kaqt.symbology;

import java.util.UUID;

import quickfix.FieldNotFound;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.StringField;
import quickfix.UnsupportedMessageType;
import quickfix.field.SecurityReqID;
import quickfix.field.SecurityType;
import quickfix.fix42.SecurityDefinition;
import quickfix.fix42.SecurityDefinitionRequest;
import kaqt.foundation.connections.FixClient;
import kaqt.foundation.symbology.IExchange;
import kaqt.foundation.symbology.IProduct;
import kaqt.foundation.symbology.ISecurity;
import kaqt.foundation.symbology.SecDefSubscriber;
import kaqt.utils.FixMessageHelper;

public class TTSecDefFixClient extends FixClient implements SecDefSubscriber
{
	private static final String REQUEST_TICK_TABLE_FIELD_VALUE = "Y";
	
	private SessionID secDefSessionId;
	
	public TTSecDefFixClient(String configFile)
	{
		super(configFile);
	}

	@Override
	public void toAdmin(Message message, SessionID sessionId)
	{
		this.secDefSessionId = sessionId;
		message = FixMessageHelper.passwordify(message, super.getPassword());
		super.toAdmin(message, sessionId);
	}
	
	@Override
	public void onLogon(SessionID sessionId)
	{
		super.onLogon(sessionId);
		this.subscribe(new TTProduct("FUT"));
	}
	
	@Override
	public void onMessage(SecurityDefinition message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue
	{
		super.onMessage(message, sessionID);
		System.out.println(message);
	}
	
	public void subscribe(IExchange exchange)
	{
	}

	public void subscribe(IExchange exchange, IProduct product)
	{
	}

	public void subscribe(IProduct product)
	{
		SecurityDefinitionRequest sdf = new SecurityDefinitionRequest();
		UUID uuid = UUID.randomUUID();
		SecurityReqID secReqId = new SecurityReqID(uuid.toString());
		sdf.setField(secReqId);
		
		SecurityType secType = new SecurityType(product.getProductType());
		sdf.setField(secType);
	
		// Can create a class for this
		
		StringField requestTickTable = new StringField(FixMessageHelper.REQUEST_TICK_TABLE_FIELD);
		requestTickTable.setValue(REQUEST_TICK_TABLE_FIELD_VALUE);
		sdf.setField(requestTickTable);
		
		try
		{
			Session.sendToTarget(sdf, this.secDefSessionId);
		}
		catch (SessionNotFound e)
		{
			e.printStackTrace();
		}
	}

	public void subscribe(IExchange exchange, IProduct product,
			ISecurity security)
	{
	}
}
