package kaqt.apps.okcoin.marketdata;

import java.net.URI;
import java.net.URISyntaxException;

import kaqt.foundation.WebsocketClientConnection;

public class App {
	public static void main(String[] args) {
        try {
            final WebsocketClientConnection connection = 
            		new WebsocketClientConnection(new URI("wss://real.okcoin.cn:10440/websocket/okcoinapi"));
            
            String msg = "{'event':'addChannel','channel':'ok_btccny_ticker'}";
            
            System.out.println(msg);
            
            connection.sendMessage(msg);

            Thread.sleep(5000);

        } catch (InterruptedException ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }
    }

}
