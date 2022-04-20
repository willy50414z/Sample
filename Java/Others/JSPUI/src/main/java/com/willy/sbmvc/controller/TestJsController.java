package com.willy.sbmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TestJsController {
	@RequestMapping("/TestSubmit")
	public String testJs(
			String col1
			,String col2
			,String col3) {
		System.out.println("col1 = "+col1+";col2 = "+col2+";col3 = "+col3);
		return "TestJS";
	}
}
