package com.willy.struts.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.test.willy.calledClass;
import com.test.willy.calledClassInter;
import com.willy.struts.ActionForm.LoginActionForm;
import com.willy.test.Users;
import com.willy.test.inter.UsersInter;

public class LoginAction extends Action{

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		try {
		WebApplicationContext ctx=
				WebApplicationContextUtils
				.getWebApplicationContext(this.getServlet().getServletContext());
		UsersInter ui=(UsersInter) ctx.getBean("usersImp");
		calledClassInter cc=(calledClassInter) ctx.getBean("calledClass");
		cc.test();
		LoginActionForm laf= (LoginActionForm) form;
//		Users us=new Users(laf.getAccount(),laf.getPassword());
		
		System.out.println("密碼正確?? "+ui.checkUser(new Users(laf.getAccount(),laf.getPassword())));
		}catch(Exception e) {
			System.out.println(e);
		}
		return mapping.findForward("success");
		
		
		
	}
	public LoginAction() {
		super();System.out.println("action");
	}

}
