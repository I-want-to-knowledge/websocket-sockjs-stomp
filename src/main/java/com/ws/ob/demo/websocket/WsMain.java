package com.ws.ob.demo.websocket;

import org.java_websocket.WebSocketImpl;

/**
 * 
 *
 * @author YanZhen
 * 2018-11-27 19:30:55
 * WsMain
 */
public class WsMain {

	public static void main(String[] args) {
		WebSocketImpl.DEBUG = false;
		new WsServer(8887).start();
	}

}
