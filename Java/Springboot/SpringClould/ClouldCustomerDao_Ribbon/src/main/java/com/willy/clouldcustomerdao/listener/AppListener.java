package com.willy.clouldcustomerdao.listener;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
@Component
public class AppListener implements ApplicationListener<WebServerInitializedEvent>{
	private static int serverPort;
    public void onApplicationEvent(WebServerInitializedEvent event) {
        serverPort = event.getWebServer().getPort();
    }
    public static int getPort() {
        return serverPort;
    }
}
