package com.learnWeb.struts2;





import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.learnWeb.Bean.Employee;
import com.opensymphony.xwork2.ActionSupport;
@Namespace("/index")
@ParentPackage(value="struts-default")
public class HelloWorldAction  extends ActionSupport{
	public Employee getE() {
		return e;
	}
	public void setE(Employee e) {
		this.e = e;
	}
	Employee e;
	
		public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
		String name;
		public String execute() throws Exception{
			System.out.println("execute method");
			return "success";
		}
		public String test() throws Exception{
			System.out.println("test method");
			return "success";
		}
		@Action(value="annotationAction", results= {
				@Result(name="success", type="dispatcher", location="/HelloWorld.jsp")
		})
		public String annotation() throws Exception{
			System.out.println("annotation method");
			return "success";
		}
		@Action(value="useServlet", results= {
				@Result(name="success", type="dispatcher", location="/HelloWorld.jsp")
		})
		public String useServlet() throws Exception{
			System.out.println("useServlet method");
			HttpServletRequest req=ServletActionContext.getRequest();
			HttpServletResponse res=ServletActionContext.getResponse();
			System.out.println("nameParam -- "+req.getParameter("name"));
			return "success";
		}
		public String addbook() throws Exception{
			System.out.println("xbook method");
			return "success";
		}
		
		public String object() throws Exception{
			System.out.println("object method");
			e.show();
			return "success";
		}
}
