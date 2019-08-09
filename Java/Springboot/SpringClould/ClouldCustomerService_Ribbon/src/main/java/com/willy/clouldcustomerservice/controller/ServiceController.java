package com.willy.clouldcustomerservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
@RestController
public class ServiceController {
	@Autowired
	private RestTemplate restTempl;
	@RequestMapping("hello")
    public String hello() {
    	return "Hello";
    }
    @RequestMapping(value = "/findUserInfoByID/{id}")
    public String findUserInfoByID(@PathVariable("id") Long id) {
    	//調用ClouldCustomerDao中的findUserInfoByID方法
    	return restTempl.getForObject("http://cloud-customer-dao-ribbon/findUserInfoByID/" + id, String.class);
    }
}
