package com.willy.clouldcustomerservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
@RestController
public class ServiceController {
	@RequestMapping("hello")
    public String hello() {
    	return "Hello";
    }
}
