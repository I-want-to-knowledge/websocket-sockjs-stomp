package com.ws.ob.demo.websocket_sockjs_stomp2;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

public class ClientDisconnectHandler implements ApplicationListener<SessionDisconnectEvent> {
	private Logger LOG = LoggerFactory.getLogger(ClientDisconnectHandler.class);
	
	@Autowired
	private PushServer pushServer;
	
	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {
		Principal user = event.getUser();
		if (user != null) {
			pushServer.afterClientDisconnect(user);
		} else {
			LOG.warn("没有收到用户的信息！");
		}
	}

}
