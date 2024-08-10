package com.willy.sbmvc.controller;

import com.willy.sbmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerWithVar {
	@RequestMapping("/users/{username}")
	public String userProfile(@PathVariable("username") String username) {
	    return String.format("user : %s", username);
	}

	@Autowired
	UserService userService;

	@RequestMapping("/posts/{id}")
	public String post(@PathVariable("id") int id) {
			userService.getUserId();
	    return String.format("post : %d", id);
	}
}
