package com.ws.ob.demo.websocket_sockjs_stomp;

/**
 * 响应消息
 *
 * @author YanZhen 2018-11-29 16:26:42 ResponseMessage
 */
public class ResponseMessage {

	private String id;
	private String name;
	private String content;

	public ResponseMessage(String id, String name, String content) {
		super();
		this.id = id;
		this.name = name;
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getContent() {
		return content;
	}

}
