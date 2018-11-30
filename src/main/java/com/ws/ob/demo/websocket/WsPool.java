package com.ws.ob.demo.websocket;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.WebSocket;

/**
 * web socket pool
 *
 * @author YanZhen 2018-11-27 16:29:46 WsPool
 */
public class WsPool {

	private final static Map<WebSocket, String> wsUserMap = new HashMap<>();
	// private final static Set<String> userSet = new HashSet<>();

	/**
	 * 通过socket链接获取用户
	 *
	 * 2018-11-27 16:33:36
	 * 
	 * @param ws
	 * @return String
	 */
	public static String getUserByWs(WebSocket ws) {
		return wsUserMap.get(ws);
	}

	/**
	 * 根据用户名，获取socket链接 只返回查询到的第一个链接
	 *
	 * 2018-11-27 16:39:39
	 * 
	 * @param username
	 * @return WebSocket
	 */
	public synchronized static WebSocket getWsByUser(String username) {
		for (WebSocket ws : wsUserMap.keySet()) {
			if (username.equals(wsUserMap.get(ws))) {
				return ws;
			}
		}
		return null;
	}

	/**
	 * 添加用户
	 *
	 * 2018-11-27 16:42:36 void
	 */
	public static boolean addUser(WebSocket ws, String username) {
		if (wsUserMap.containsValue(username)) {
			return false;
		}

		// 链接和用户名
		wsUserMap.put(ws, username);
		// 用户名
		/*
		 * boolean flag = userSet.add(username); if (!flag) { System.err.println("'"
		 * + username + "'该用户名使用过了！"); }
		 */
		return true;
	}

	/**
	 * 获取在线的所有用户
	 *
	 * 2018-11-27 17:08:47
	 * 
	 * @return Collection<String>
	 */
	public static Collection<String> getOnlineUser() {
		return wsUserMap.values();
	}

	/**
	 * 删除用户
	 *
	 * 2018-11-27 17:22:52
	 * 
	 * @param ws
	 * @return boolean
	 */
	public static boolean removeUser(WebSocket ws) {
		// String username = wsUserMap.get(ws);
		if (wsUserMap.containsKey(ws)) {
			// 删除链接
			wsUserMap.remove(ws);

			// 删除用户
			// userSet.remove(username);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 给指定用户发消息
	 *
	 * 2018-11-27 17:41:31
	 * @param ws
	 * @param message void
	 */
	public static void sendMessageToUser(WebSocket ws, String message) {
		if (wsUserMap.containsKey(ws)) {
			ws.send(message);
		} else {
			System.out.println("信息没有发送！");
		}
	}
	
	/**
	 * 向所有用户发送信息
	 *
	 * 2018-11-27 17:44:47
	 * @param message void
	 */
	public synchronized static void sendMessageToAll(String message) {
		for (WebSocket ws : wsUserMap.keySet()) {
			ws.send(message);
		}
	}
}
