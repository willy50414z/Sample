package com.willy.websocket.config.interceptor;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

//握手前的Interceptor，用來做權限、session、token...判斷
/**一定要是broker的子包下**/
@Component
public class MyHandShakeInterceptor implements HandshakeInterceptor {
	@Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        System.out.println(this.getClass().getCanonicalName() + "http協定轉換websoket協定進行前, 握手前"+request.getURI());
        // http協定轉換websoket協定進行前，可以在這裡通過session資訊判斷使用者登錄是否合法
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        //握手成功後,
        System.out.println(this.getClass().getCanonicalName() + "握手成功後...");
    }
}
