package com.ws.ob.demo.websocket;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.java_websocket.WebSocketImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 测试boot
 *
 * @author YanZhen 2018-04-23 13:43:51 HelloController
 */
@Controller
public class HelloController {

	@RequestMapping("/index")
	public String index(Map<String, Object> map, HttpServletRequest request) {
		WebSocketImpl.DEBUG = false;
		new WsServer(8887).start();
		return "index";
	}
}
