package com.ws.ob.demo.websocket_sockjs_stomp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class SubController {
	
	@Autowired
	private SimpMessagingTemplate template;

	@MessageMapping("/subscribe")
	public void subscribe(Token t, ReceiverMessage rm) {
		String[] names = {rm.getName() + "1", rm.getName() + "2", rm.getName() + "3"};
		for (int i = 0; i < names.length; i++) {
			// 给目的地‘/topic/getResponse’发消息
			template.convertAndSend("/topic/getResponse", t.getToken() + " 发来消息 : " + rm.getMessage() + "("+ names[i] +")");
			
			// 间隔一秒在发送
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@MessageMapping("/queue")
	public void queue(Token t, ReceiverMessage rm) {
		String[] names = {rm.getName() + "1", rm.getName() + "2", rm.getName() + "3"};
		for (int i = 0; i < names.length; i++) {
			// zhangsan为用户id，实现点对点客户端要订阅("/user/" + 用户id + "/message")，其中"/user/"是固定的
			template.convertAndSendToUser(rm.getName(),"/message", t.getToken() + " 发来消息 : " + rm.getMessage() + "("+ names[i] +")");
			
			// 间隔一秒在发送
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
