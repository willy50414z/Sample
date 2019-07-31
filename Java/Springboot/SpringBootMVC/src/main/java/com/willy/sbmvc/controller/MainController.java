package com.willy.sbmvc.controller;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.willy.sbmvc.entity.PersonInfo;

@Controller
@RequestMapping("/user")
public class MainController {
	private final static String CONTROLLER_URL = "welcome";

	@RequestMapping(value = "/welcome")
	public ModelAndView welcome() {
		System.out.println("welcome");
		return new ModelAndView("main_success", "message", "");
	}

	// 同時可處理get、post
	//只有user=aaa 且 pwd=bbb的request才進得來
	@RequestMapping(value = CONTROLLER_URL
		, method = { RequestMethod.POST, RequestMethod.GET }
		, params = { "user=aaa","pwd=bbb" }
	 /* ,headers="Host=localhost:8080"限制請求資訊 */) 
	public ModelAndView helloWorld(String user, String pwd,String select) {
		System.out.println("user - " + user + "  pwd - " + pwd+ "  select - " + select);
		String message = "<br><div style='text-align:center;'>"
				+ "<h3>********** Hello World, Spring MVC Tutorial</h3>This message is coming from CrunchifyHelloWorld.java **********</div><br><br>";
		return new ModelAndView("main_success", "message", message);// 頁面、回傳變數名稱、回傳變數
	}

	// 帶參數
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") Integer id) {
		System.out.println("action=" + id);
		return new ModelAndView("main_success", "message", "");
	}

	// 帶參數--bean
	@RequestMapping(value = "/bean")
	public ModelAndView Bean(PersonInfo p) {
		System.out.println("name=" + p.getName() + "  -  age=" + p.getAge());
		return new ModelAndView("main_success", "message", "Bean");
	}
	// 帶參數--param
		@RequestMapping(value = "/param",method=RequestMethod.POST)
		public ModelAndView Bean(String user, String pwd) {
			System.out.println("name=" + user + "  -  age=" + pwd);
			return new ModelAndView("main_success", "message", "Param");
		}
	// 帶參數--redirect
	@RequestMapping(value = "/redirect")
	public String redirect() {
		return "redirect:main_success";
	}

	// 上傳檔案
	@RequestMapping(value = "/upload",method=RequestMethod.POST)
	public ModelAndView upload(HttpServletRequest req) throws  Exception{
		MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) req;
		MultipartFile file = mreq.getFile("file");
		String fileName = file.getOriginalFilename();
		FileOutputStream fos = new FileOutputStream("D:/test.txt");
		fos.write(file.getBytes());
		fos.flush();
		fos.close();
		return new ModelAndView("main_success", "message", "UploadFile");
	}
	//Exception 處理
	 @RequestMapping("/error")
	    public ModelAndView error(){
	        int i = 5/0;
	        return new ModelAndView("welcome", "message", "");
	    }
	@ExceptionHandler
    public ModelAndView exceptionHandler(Exception ex){
        ModelAndView mv = new ModelAndView("welcome", "message", "ERROR");
        mv.addObject("exception", ex);
		System.out.println("ERROR");
		return mv;
    }
}
