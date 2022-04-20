package com.willy.sbmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller//非文字回傳型別需用@Controller
public class MainController {
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
	@GetMapping("/Router/{jspName}")
	public String router(@PathVariable("jspName") String jspName) {
        return jspName;
	}
	
	@GetMapping("/UIRouter/{jspName}")
	public String uirouter(@PathVariable("jspName") String jspName) {
        return "uisample/"+jspName;
	}
}
