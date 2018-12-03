package com.ws.ob.demo.websocket_sockjs_stomp2;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * 消息推送系统
 * DisposableBean销毁专用
 *
 * @author YanZhen
 * 2018-11-30 17:18:09
 * PushServer
 */
@Component
public class PushServer/* implements DisposableBean*/ {
	private Logger LOG = LoggerFactory.getLogger(PushServer.class);
	
	@Autowired
	private SimpMessagingTemplate template;

	/**
	 * 链接后调用
	 *
	 * 2018-11-30 17:39:23
	 * @param p void
	 */
	public void afterClientConnected(Principal p) {
		// 链接服务器后，收到信息进行处理
		String name = p.getName();
		LOG.warn("{} 已经连接成功！", name);
		
		// 对 name 用户发送消息
		template.convertAndSendToUser(name, "/message", name + " 登录成功！");
		// template.convertAndSend("/user/p2p/message", name + " 登录成功！(个人)");
		
		// 对订阅了 ‘/bro/getResponse’ 的所有用户推送消息
		template.convertAndSend("/bro/getResponse", name + " 登录成功！");
	}
	
	/**
	 * 客户端断开链接后调用
	 *
	 * 2018-11-30 17:41:11
	 * @param p void
	 */
	public void afterClientDisconnect(Principal p) {
		// 客户端断开链接
		LOG.warn("{} 已经断开链接！", p.getName());
	}
}
