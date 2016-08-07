package kaqt.sim.exchange.quickfix;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

import kaqt.foundation.EventArgs;
import kaqt.sim.exchange.IExchangeServer;
import quickfix.Acceptor;
import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.LogFactory;
import quickfix.Message;
import quickfix.MessageStoreFactory;
import quickfix.RejectLogon;
import quickfix.RuntimeError;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;
import quickfix.UnsupportedMessageType;
import quickfix.fix42.MessageCracker;
import quickfix.fix42.MessageFactory;

public class QuickFixExchangeServer extends MessageCracker implements Application, IExchangeServer {
	
	private SessionSettings settings;
	private MessageStoreFactory messageStoreFactory;
	private LogFactory logFactory;
	private Acceptor acceptor;
	private String configFile = null;
	private Boolean isInitialized = false;
	private final RingBuffer<EventArgs<Message>> ringbuffer;

	public QuickFixExchangeServer(String configFile, RingBuffer<EventArgs<Message>> ringbuffer) {
		this.configFile = configFile;
		this.ringbuffer = ringbuffer;
	}
	
    private static final EventTranslatorOneArg<EventArgs<Message>, Message> TRANSLATOR =
            new EventTranslatorOneArg<EventArgs<Message>, Message>()
            {
				public void translateTo(EventArgs<Message> event, long sequence,
						Message message) {
					event.set(message);
				}
            };
	
	public void fromAdmin(Message message, SessionID sessionId) throws FieldNotFound,
			IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		System.out.println(message);
	}

	public void fromApp(Message message, SessionID sessionId) throws FieldNotFound,
			IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		crack(message, sessionId);
	}

	public void onCreate(SessionID sessionId) {
		
	}

	public void onLogon(SessionID sessionId) {
		
	}

	public void onLogout(SessionID sessionId) {
		
	}

	public void toAdmin(Message message, SessionID sessionId) {
		System.out.println(message);
	}

	public void toApp(Message message, SessionID sessionId) throws DoNotSend {
		System.out.println(message);
	}

	public void onMessage( quickfix.fix42.NewOrderSingle message, SessionID sessionID )
		      throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		ringbuffer.publishEvent(TRANSLATOR, message);

	}

	public void onMessage( quickfix.fix42.OrderCancelRequest message, SessionID sessionID )
		      throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		ringbuffer.publishEvent(TRANSLATOR, message);
	}   
	
	public void onMessage( quickfix.fix42.OrderCancelReplaceRequest message, SessionID sessionID )
		      throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		ringbuffer.publishEvent(TRANSLATOR, message);
	}  
	
	private void setup() {
		try {
			System.out.println("Reading config file " + configFile);
			settings = new SessionSettings(new FileInputStream(configFile));
			messageStoreFactory = new FileStoreFactory(settings);
		    logFactory = new FileLogFactory(settings);
		    MessageFactory messageFactory = new MessageFactory();
		    acceptor = new SocketAcceptor
		      (this, messageStoreFactory, settings, logFactory, messageFactory);
		    isInitialized = true;
		} catch (FileNotFoundException e) {
			isInitialized = false;
			e.printStackTrace();
		} catch (ConfigError e) {
			isInitialized = false;
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		setup();
		if (isInitialized) {
		    try {
				acceptor.start();
			} catch (RuntimeError e) {
				isInitialized = false;
				e.printStackTrace();
			} catch (ConfigError e) {
				isInitialized = false;
				e.printStackTrace();
			}
		}
	}
	
	public void stop() {
		isInitialized = false;
	    acceptor.stop();
	}
	
}
