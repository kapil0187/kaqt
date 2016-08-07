package kaqt.foundation;

import java.io.IOException;
import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

@ClientEndpoint
public class WebsocketClientConnection {
	
	private Session userSession = null;

	public WebsocketClientConnection(URI endpointURI) {
		try {
			WebSocketContainer container = ContainerProvider
					.getWebSocketContainer();
			container.connectToServer(this, endpointURI);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	private static final EventTranslatorOneArg<EventArgs<String>, String> TRANSLATOR = new EventTranslatorOneArg<EventArgs<String>, String>() {
//		public void translateTo(EventArgs<String> event, long sequence,
//				String message) {
//			event.set(message);
//		}
//	};

	@OnOpen
	public void onOpen(Session userSession) throws InterruptedException {
		System.out.println("opening websocket");
		this.userSession = userSession;
	}

	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		System.out.println("closing websocket");
		this.userSession = null;
	}

	@OnMessage
	public void onMessage(String message) {
		System.out.println(message);
	}

	@OnError
	public void onError(Throwable t) {
		t.printStackTrace();
	}

	public void sendMessage(String message) {
		try {
			this.userSession.getBasicRemote().sendText(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
