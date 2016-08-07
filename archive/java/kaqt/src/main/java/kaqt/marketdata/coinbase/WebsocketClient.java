package kaqt.marketdata.coinbase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lmax.disruptor.RingBuffer;

import kaqt.foundation.EventArgs;
import kaqt.foundation.coinbase.HeaderRelatedConstants;
import kaqt.foundation.coinbase.WebsocketClientConfiguration;
import kaqt.foundation.providers.lmax.QueuedMessageHandler;

@ClientEndpoint
public class WebsocketClient extends
		QueuedMessageHandler<WebsocketMessageUpdate> {

	private WebsocketClientConfiguration config;
	private BlockingQueue<String> messageQueue;
	private Session userSession = null;
	private AtomicBoolean isProcessing;
	private JsonParser parser;
	private WebSocketContainer container;

	public WebsocketClient(
			RingBuffer<EventArgs<WebsocketMessageUpdate>> ringbuffer,
			WebsocketClientConfiguration config) {
		super(ringbuffer);
		this.config = config;
		this.parser = new JsonParser();
		this.isProcessing = new AtomicBoolean(false);
		this.messageQueue = new ArrayBlockingQueue<String>(1024);
		this.container = ContainerProvider.getWebSocketContainer();
	}

	@OnOpen
	public void onOpen(Session userSession) throws InterruptedException,
			IOException {
		System.out.println("opening websocket");
		this.userSession = userSession;
		this.userSession.getBasicRemote().sendText(
				this.config.getSubscribeMessage());
		this.syncMessages();
	}

	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		System.out.println("closing websocket");
		try {
			System.out.println("Reconnecting in 10 secs");
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			this.userSession = null;
		}

		this.connect();
	}

	@OnMessage
	public void onMessage(String message) throws InterruptedException {
		if (!isProcessing.get()) {
			messageQueue.put(message);
		} else {
			JsonObject update = (JsonObject) parser.parse(message);
			super.handleMessage(new WebsocketMessageUpdate(update,
					OrderBookUpdateTypeEnum.BOOK_UPDATE));
		}
	}

	@OnError
	public void onError(Throwable t) {
		t.printStackTrace();
	}

	public void connect() {
		try {
			container.connectToServer(this,
					new URI(this.config.getWebsocketUrl()));
		} catch (DeploymentException | IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		this.userSession = null;
	}

	private void syncMessages() {
		HttpClient client = HttpClientBuilder.create().build();

		HttpGet snapshotGetRequest = new HttpGet(this.config.getSnapshotUrl());
		snapshotGetRequest.addHeader(HeaderRelatedConstants.USER_AGENT,
				this.config.getUserAgent());

		HttpResponse response = null;
		try {
			response = client.execute(snapshotGetRequest);
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}

		JsonElement root = parser.parse(br);

		JsonObject snapshotMessage = root.getAsJsonObject();

		super.handleMessage(new WebsocketMessageUpdate(snapshotMessage,
				OrderBookUpdateTypeEnum.INITIAL_SNAPSHOT));
		isProcessing.set(true);

		while (!messageQueue.isEmpty()) {
			String currentMessage = null;
			try {
				currentMessage = messageQueue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			JsonObject currentUpdate = (JsonObject) parser
					.parse(currentMessage);
			super.handleMessage(new WebsocketMessageUpdate(currentUpdate,
					OrderBookUpdateTypeEnum.BOOK_UPDATE));
		}
	}
}
