package com.test;

import javax.jws.WebService;

@WebService(targetNamespace = "http://test.com/", portName = "echoTestPort", serviceName = "echoTestService")
public class echoTest {
	public String echo(String msg) {
		return msg;
	}
}
