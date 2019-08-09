package com.willy.clouldcustomerservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
@RestController
public class ServiceController {
	@Autowired
	private RestTemplate restTempl;
	@RequestMapping("hello")
    public String hello() {
    	return "Hello";
    }
    @RequestMapping(value = "/findUserInfoByID/{id}")
    @HystrixCommand(fallbackMethod="fallBackMethod")//此方法發生Exception時，調用fallBackMethod方法
    public String findUserInfoByID(@PathVariable("id") Long id) {
    	//調用ClouldCustomerDao中的findUserInfoByID方法
    	return restTempl.getForObject("http://cloud-customer-dao-ribbon/findUserInfoByID/" + id, String.class);
    }
    //回傳型別及Param皆需與來源端一樣
    public String fallBackMethod(@PathVariable("id") Long id) {
    	return "目前服務發生異常，麻煩刷新再試一次，謝謝";
    }
}
