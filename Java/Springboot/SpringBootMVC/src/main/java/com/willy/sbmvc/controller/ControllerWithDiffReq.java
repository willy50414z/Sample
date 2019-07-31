package com.willy.sbmvc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ControllerWithDiffReq {
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginGet() {
		return "Login Page";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginPost() {
		return "Login Post Request";
	}

	// //新版MVC標記方式
	// @GetMapping("/login")
	// public String loginGet1() {
	// return "Login Page";
	// }
	//
	// @PostMapping("/login")
	// public String loginPost1() {
	// return "Login Post Request";
	// }
}
