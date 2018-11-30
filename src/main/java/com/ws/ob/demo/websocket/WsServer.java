package com.ws.ob.demo.websocket;

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

/**
 * web socket服务器的声明周期
 *
 * @author YanZhen
 * 2018-11-27 18:53:30
 * WsServer
 */
public class WsServer extends WebSocketServer {
	
	/**
	 * 注入端口号
	 * @param port
	 */
	public WsServer(int port) {
		super(new InetSocketAddress(port));
	}
	
	public WsServer(InetSocketAddress address) {
		super(address);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		// 链接时触发
		// TODO:
		System.out.println("已与服务器链接！");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		// 断开链接时触发
		WsPool.removeUser(conn);
		
		// 关闭时返回的code
		System.out.println("code =" + code);
		
		// 原因附加信息字符串	reason
		System.out.println("reason =" + reason);
		
		// 远程返回是否由远程主机发起关闭连接	remote
		System.out.println("remote =" + remote);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println("接收：" + message);
		if (message != null && message.startsWith("@1@")) {
			String username = message.replaceFirst("@1@", "");
			if (!WsPool.addUser(conn, username)) {
				conn.send("添加用户失败！");
			}
		} else if (message != null && message.startsWith("@0@")) {
			if (!WsPool.removeUser(conn)) {
				conn.send("删除用户失败！");
			}
		} else {
			// conn.send("发送的信息格式不正确！（起始位：1|0）");
			String[] split = message.split(":");
			if (split.length == 2) {
				WsPool.sendMessageToUser(WsPool.getWsByUser(split[0]), split[1]);
			} else if (split.length == 1) {
				WsPool.sendMessageToAll(split[0]);
			}
		}
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		// 错误时触发
		System.out.println("on error!");
		ex.printStackTrace();
	}

	@Override
	public void onStart() {
		// start();
	}

}
