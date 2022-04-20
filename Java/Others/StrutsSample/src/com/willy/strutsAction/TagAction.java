package com.willy.strutsAction;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.willy.model.People;
import com.willy.strutsActionForm.TagActionForm;

public class TagAction extends Action{

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		TagActionForm taf=(TagActionForm) form;

		for(String a:taf.getCheckbox()){
			System.out.println("ch"+a.toString());
		}
		
		System.out.println("b"+taf.getPassword());
		System.out.println("c"+taf.getFile());
		System.out.println("d"+taf.getRadio());
		System.out.println("e"+taf.getSelect());
		System.out.println("f"+taf.getSelect1());
		System.out.println("g"+taf.getText());
		System.out.println("h"+taf.getTextarea());
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
		return mapping.findForward("success");
	}

}
