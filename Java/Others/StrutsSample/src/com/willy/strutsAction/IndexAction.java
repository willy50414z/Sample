package com.willy.strutsAction;




import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.willy.model.People;
import com.willy.strutsActionForm.IndexActionForm;

public class IndexAction extends Action{

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		IndexActionForm acf=(IndexActionForm) form;
		// TODO Auto-generated method stub
		System.out.println("enter action---------------"+acf.getUserName()+"///"+acf.getPassWord());
		
		
		List multiboxlist=new ArrayList();
		multiboxlist.add("aa");
		multiboxlist.add("bb");
		multiboxlist.add("cc");
		List<People> personlist=new ArrayList();
		personlist.add(new People("willy",100,100));
		personlist.add(new People("steven",120,130));
		personlist.add(new People("jack",20,80));
		personlist.add(new People("woo",30,60));
		request.setAttribute("multiboxlist", multiboxlist);
		request.setAttribute("personlist", personlist);
		request.setAttribute("aa", "bb");
		System.out.println("Exit Action");
		return mapping.findForward("success");
	}
	
}
