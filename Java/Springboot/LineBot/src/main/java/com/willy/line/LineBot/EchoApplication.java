package com.willy.line.LineBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@SpringBootApplication
@LineMessageHandler
public class EchoApplication {
    private final Logger log = LoggerFactory.getLogger(EchoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
        
//        /**
//         * 	發送push message
//         */
//        LineMessagingClient client = LineMessagingClient.builder("KVvyvg/oTD6BXNT37UcUeNl8oCq1SZMJHN6vMp/6pzSGCLjER+QxDb2JVainLalOYhqet3xQaUZPTFuBBLPwBvmy9hmBW4DdbsvAp+nO7lSNFHTO1dHy+RhYI7h9TyQ8xh8GOXvMrZSvgEg0YOU9NAdB04t89/1O/w1cDnyilFU=").build();
//    	
//	   	TextMessage textMessage = new TextMessage("我不是故意說謊的999");
//	   	PushMessage pushMessage = new PushMessage("Uad6f409721ead19233feb11b030d3d8b", textMessage);
//	
//	   	BotApiResponse botApiResponse;
//	   	try {
//	   	    botApiResponse = client.pushMessage(pushMessage).get();
//	   	} catch (Exception e) {
//	   	    e.printStackTrace();
//	   	    return;
//	   	}
//	   	System.out.println(botApiResponse);
    }

    //處理文字訊息
    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        log.info("event: " + event);
        log.info("userId: " + event.getSource().getUserId());
        log.info("text: " + event.getMessage().getText());
        final String originalMessageText = event.getMessage().getText();
        return new TextMessage(originalMessageText);
    }
    //處理非文字訊息
    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}