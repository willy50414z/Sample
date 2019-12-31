package com.willy.websocket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import com.willy.websocket.config.interceptor.MyChannelInterceptor;
import com.willy.websocket.config.interceptor.MyHandShakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker//WebSocketMessageBroker類似於socket用的controller
public class SimpleWsBroker extends AbstractWebSocketMessageBrokerConfigurer {
	@Autowired
	private MyChannelInterceptor channelInterceptor;
	@Autowired
	private MyHandShakeInterceptor handShakeInterceptor;
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		//註冊websocket端點供前端訂閱
		registry.addEndpoint("/websocket-simple").setAllowedOrigins("*") // 添加允许跨域访问
				.addInterceptors(handShakeInterceptor)//註冊握手攔截器
				.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		//只供以/topic,/queue開頭的請求訂閱
		registry.enableSimpleBroker("/topic", "/queue");
	}

	@Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
    	ChannelRegistration channelRegistration = registration.setInterceptors(channelInterceptor);
        super.configureClientInboundChannel(registration);
    }
}
