package kaqt.foundation.providers.quickfix;

import java.io.InputStream;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.Dictionary;
import quickfix.DoNotSend;
import quickfix.FieldConvertError;
import quickfix.FieldNotFound;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.LogFactory;
import quickfix.Message;
import quickfix.StringField;
import quickfix.Message.Header;
import quickfix.MessageFactory;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.UnsupportedMessageType;
import quickfix.field.Password;
import quickfix.field.RawData;
import quickfix.fix42.Logon;
import quickfix.fix42.MessageCracker;
import quickfix.field.MsgType;
import quickfix.Field;


public class Fix42Initiator extends MessageCracker implements Application {
	private String configFile = null;
	private Boolean withPassword = false;
	private String password = null;
	private Boolean withusername = false;
	private String username = null;
	private SocketInitiator socketInitiator = null;
	private SessionSettings sessionSettings = null;

	public Fix42Initiator(String configFile) {
		super();
		this.configFile = configFile;
	}

	public Fix42Initiator(String password, String configFile) {
		super();
		this.withPassword = true;
		this.password = password;
		this.configFile = configFile;
	}

	public Fix42Initiator(String username, String password, String configFile) {
		super();
		this.withusername = true;
		this.withPassword = true;
		this.username = username;
		this.password = password;
		this.configFile = configFile;
	}

	@Override
	public void fromAdmin(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
			RejectLogon {
		System.out.println(message);
	}

	@Override
	public void fromApp(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
			UnsupportedMessageType {
		System.out.println(message);

	}

	@Override
	public void onCreate(SessionID sessionId) {
		System.out.println("Session Created");
	}

	@Override
	public void onLogon(SessionID sessionId) {
		System.out.println("Logged on");
	}

	@Override
	public void onLogout(SessionID sessionId) {
		System.out.println("Logged out");
	}

	@Override
	public void toAdmin(Message message, SessionID sessionId) {
		if (withPassword)
		{
			Header header = message.getHeader();
			MsgType msgType = new MsgType();
			try {
				if (header.getField(msgType).valueEquals("A")) {
					RawData pass = new RawData(this.password);
					message.setField(pass);
				}
			} catch (FieldNotFound e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void toApp(Message message, SessionID sessionId) throws DoNotSend {
		System.out.println(message);

	}

	public void onMessage(Message message, SessionID sessionId)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		System.out.println(message);
	}

	public void start() throws ConfigError {
		this.sessionSettings = new SessionSettings(configFile);
		FileStoreFactory fileStoreFactory = new FileStoreFactory(
				sessionSettings);
		LogFactory logFactory = new FileLogFactory(sessionSettings);
		MessageFactory messageFactory = new DefaultMessageFactory();
		this.socketInitiator = new SocketInitiator(this,
				fileStoreFactory, sessionSettings, logFactory, messageFactory);
		socketInitiator.start();
	}

	public void stop() {
		this.socketInitiator.stop();
	}

}
