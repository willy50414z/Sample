package com.willy.sbmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller//非文字回傳型別需用@Controller
public class ControllerWithJSP {
	@GetMapping("/gotoJSP")
	public ModelAndView getoJSP() {
		ModelAndView view = new ModelAndView("index");
        view.addObject("userName", "Willy");
        return view;
	}
	
	@GetMapping("/MainPage")
	public ModelAndView mainPage() {
		ModelAndView view = new ModelAndView("main");
        return view;
	}
}
