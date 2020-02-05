package com.willy.websocket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.willy.websocket.config.SocketServer;

@Controller
public class MainController {
	@Autowired
    private SocketServer socketServer;
	
	@RequestMapping
	public String visitCustomer() {
		return "Customer";
	}

   @RequestMapping(value = "/admin")
   public ModelAndView admin() {
	   ModelAndView view = new ModelAndView("Admin");
       int num = socketServer.getOnlineNum();
       List<String> list = socketServer.getOnlineUsers();
       view.addObject("num", num);
       view.addObject("users", list);
       return view;
   }

   //admin -> client
   @RequestMapping("sendmsg")
   @ResponseBody
   public String sendmsg(String msg, String username){
       String [] persons = username.split(",");
       SocketServer.SendMany(msg,persons);
       return "success";
   }
   @RequestMapping("sendAll")
   @ResponseBody
   public String sendAll(String msg){
       SocketServer.sendAll(msg);
       return "success";
   }
}
