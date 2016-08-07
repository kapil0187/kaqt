package kaqt.foundation.connections;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.RuntimeError;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.UnsupportedMessageType;
import quickfix.fix42.MessageCracker;
import quickfix.fix42.MessageFactory;

public class FixClient extends MessageCracker implements Application
{
	private static Logger logger = Logger.getLogger(FixClient.class);
	
	private String configFile;
    private FileStoreFactory fileStoreFactory;
    private FileLogFactory fileLogFactory;
    private MessageFactory messageFactory;
    private SessionSettings sessionSettings;
    private SocketInitiator initiator;
    private String username;
    private String password;

	public FixClient(String configFile)
	{
		this.configFile = configFile;
	}
	
	public void start()
	{
		try
		{
			sessionSettings = new SessionSettings(this.configFile);
		}
		catch (ConfigError e)
		{
			e.printStackTrace();
		}
		
		if (sessionSettings != null)
		{
			fileStoreFactory = new FileStoreFactory(sessionSettings);
			fileLogFactory = new FileLogFactory(sessionSettings);
			messageFactory = new MessageFactory();
			try
			{
				initiator = new SocketInitiator(this, fileStoreFactory, sessionSettings, fileLogFactory, messageFactory);
			}
			catch (ConfigError e)
			{
				e.printStackTrace();
			}
		}
		
		if (initiator != null) 
		{
			try
			{
				initiator.start();
			}
			catch (RuntimeError e)
			{
				e.printStackTrace();
			}
			catch (ConfigError e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void stop()
	{
		logger.log(Level.INFO, "CLOSING FIX CONNECTION");
		
		initiator.stop();
		fileLogFactory = null;
		fileStoreFactory = null;
		messageFactory = null;
		sessionSettings = null;
		initiator = null;
	}
	
	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public void onCreate(SessionID sessionId)
	{
		logger.info(String.format("CREATED FIX SESSION FOR %s", sessionId));
	}

	public void onLogon(SessionID sessionId)
	{
		logger.info(String.format("LOGGED ON: %s", sessionId));
	}

	public void onLogout(SessionID sessionId)
	{
		logger.info(String.format("LOGGED OUT: %s", sessionId));
	}

	public void toAdmin(Message message, SessionID sessionId)
	{
		logger.info(String.format("TO ADMIN: [%s] %s", sessionId, message));
	}

	public void fromAdmin(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
			RejectLogon
	{
		logger.info(String.format("FROM ADMIN: [%s] %s", sessionId, message));
	}

	public void toApp(Message message, SessionID sessionId) throws DoNotSend
	{
		//logger.info(String.format("TO APP: [%s] %s", sessionId, message));
	}

	public void fromApp(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
			UnsupportedMessageType
	{
		crack(message, sessionId);
	}
}
