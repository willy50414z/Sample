package com.willy.line.LineBot;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class LinebotController {
	@ResponseBody
	@RequestMapping("/callback")
	public void visit(HttpServletRequest request) throws ParseException {
	}
}
