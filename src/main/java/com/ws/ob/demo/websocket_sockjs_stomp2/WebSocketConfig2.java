package com.ws.ob.demo.websocket_sockjs_stomp2;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * 只推送信息
 * Web socket 配置
 * EnableWebSocketMessageBroker 注解表明： 这个配置类不仅配置了 WebSocket，还配置了基于代理的 STOMP消息；
 *
 * @author YanZhen
 * 2018-11-29 14:07:56
 * WebSocketConfig
 */
@Configuration
@EnableWebSocketMessageBroker
@MessageMapping
public class WebSocketConfig2 implements WebSocketMessageBrokerConfigurer {
	private Logger LOG = LoggerFactory.getLogger(WebSocketConfig2.class);

	/**
	 * 添加一个服务端点，接收客户端的链接，将‘/xxxx’路径注册为stomp的断点；
	 * 这个路径与发送和接收消息的目的路径不同，这是一个端点，客户端订阅或发布消息前要先链接该端点；
	 * 即用户发送请求，URL = 'http://127.0.0.1:8080/xxxx'与stomp端点链接，在转到订阅url；
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		/*// 添加一个 /endpointChat 端点，客户端通过这个端点进行链接，withSockJS作用是添加SockJS支持；
		registry.addEndpoint("/endpointChat").setHandshakeHandler(new DefaultHandshakeHandler() {
			@Override
			protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
					Map<String, Object> attributes) {
				// 服务器和客户端保持一致的标志，可以用用户名和用户id
				return new MyPrincipal("test");
			}
		}).withSockJS();*/
		registry.addEndpoint("/webServer").withSockJS();// 广播
    registry.addEndpoint("/queueServer").withSockJS();// 点对点
	}
	
	/**
	 * 配置一个简单的消息代理，发送应用程序消息会带有‘/app’前缀
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 定义一个或多个客户端订阅的地址的前缀消息，服务端-->客户端
		registry.enableSimpleBroker("/topic","/user");//topic用来广播，queue用来实现p2p
		
		/*// 定义服务端接收消息的前缀，客户端-->服务端
		registry.setApplicationDestinationPrefixes("/app");
		
		// 点对点使用的订阅前缀
		registry.setUserDestinationPrefix("/user/");*/
	}
	
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(new ChannelInterceptor() {
			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
				if (!StompCommand.CONNECT.equals(accessor.getCommand())) {
					LOG.info("断开 : {}", accessor.getCommand());
					LOG.info("message : {}", message);
					return message;
				}
				
				String header = accessor.getFirstNativeHeader("Token");
				if (!StringUtils.isEmpty(header)) {
					accessor.setUser(new MyPrincipal(header));
					return message;
				}
				
				LOG.info("没有token信息！");
				return null;
			}
		});
	}
	
	/**
	 * 用户信息
	 *
	 * @author YanZhen
	 * 2018-11-29 19:15:07
	 * MyPrincipal
	 */
	class MyPrincipal implements Principal {
		private String name;
		
		public MyPrincipal(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
		
	}
}
