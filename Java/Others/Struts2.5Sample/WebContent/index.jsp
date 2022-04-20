<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
</head>
<script>
	function jsAction(){
		var form=document.forms[4];
		form.action="hello";
		form.submit();
	}
</script>
<body>
<h1>submit by execute</h1>
   <form action="hello">
      <label for="name">Please enter your name</label><br/>
      <input type="text" name="name"/>
      <input type="submit" value="submit"/>
   </form>
   <br>
   <h1>submit by test</h1>
   <form action="hello1!test">
      <label for="name">Please enter your name</label><br/>
      <input type="text" name="name"/>
      <input type="submit" value="submit"/>
   </form>
   <br>
   <h1>submit by annotation & package</h1>
   <form action="index/annotationAction.action">
      <label for="name">Please enter your name</label><br/>
      <input type="text" name="name"/>
      <input type="submit" value="submit"/>
   </form>
   <br>
   <h1>submit to test servlet </h1>
   <form action="index/useServlet.action">
      <label for="name">Please enter your name</label><br/>
      <input type="text" name="name"/>
      <input type="submit" value="submit"/>
   </form>
   <h1>submit by js</h1>
   <form>
      <label for="name">Please enter your name</label><br/>
      <input type="text" name="name"/>
      <input type="button" value="submit" onclick="jsAction()"/>
      </form>
     <h1>* action</h1>
   <form action="addBook" method="post">
      <label for="name">Please enter your name</label><br/>
      <input type="text" name="name"/>
      <input type="text" name="name1"/>
      <input type="submit" value="submit"/>
      </form>
       <h1>Default action</h1>
   <form action="XXXXOOOO">
      <label for="name">Please enter your name</label><br/>
      <input type="text" name="name"/>
      <input type="submit" value="submit"/>
      </form>
      <h1>objectAndInterceptor</h1>
   <form action="objectAndInterceptor">
      <label for="name">Please enter your name</label><br/>
      name<input type="text" name="e.name"/>
      age<input type="text" name="e.age"/>
      gender<input type="text" name="e.gender"/>
      <input type="submit" value="submit"/>
      </form>
   <br>
</body>
</html>