package com.willy.websocket.controller;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.willy.websocket.dto.RequestMessage;
import com.willy.websocket.dto.ResponseMessage;

@Controller
public class BroadcastCtl {
    private static final Logger logger = LoggerFactory.getLogger(BroadcastCtl.class);

    // 收到消息记数
    private AtomicInteger count = new AtomicInteger(0);
    /**
     * @MessageMapping 指定要接收消息的地址，类似@RequestMapping。除了注解到方法上，也可以注解到类上
     * @SendTo 接收端會訂閱的message queue, 因此收到請求時將message發送到/topic/getResponse這個queue
     * @param requestMessage
     * @SendToUser僅返回給message發送者
     * @SendTo返回給所有訂閱者
     * @return
     */
    @MessageMapping("/receive")
    @SendTo("/topic/getResponse")
    //@SendToUser("/topic/getResponse")
    public ResponseMessage broadcast(RequestMessage requestMessage){
        logger.info("receive message = {}" , JSONObject.toJSONString(requestMessage));
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setResponseMessage("BroadcastCtl receive [" + count.incrementAndGet() + "] records");
        return responseMessage;
    }

    //測試頁面controller
    @RequestMapping(value="/broadcast/index")
    public String broadcastIndex(HttpServletRequest req){
        System.out.println(req.getRemoteHost());
        return "ws-broadcast";
    }

}
