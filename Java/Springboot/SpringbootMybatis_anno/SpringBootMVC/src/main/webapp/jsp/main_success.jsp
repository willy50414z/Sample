<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
<title>Spring MVC Tutorial Series by Crunchify.com</title>
</head>
<body>
<h2>普通submit</h2>
	<form action="welcome">
		<input type="submit" value="/welcome">
	</form>
<h2>普通submit-限制method</h2>
	<form action="user/welcome" method="post">
		<input type="submit" value="user/welcome-post">
	</form>
	<form action="user/welcome" method="get">
		<input type="text" name="user" value="aaa">
		<input type="text" name="pwd" value="bbb">
		<select name="select"><option value="abc">abc</option>
							<option value="cde">cde</option></select>
		<input type="submit" value="user/welcome-get">
	</form>
<h2>普通submit-限制method-帶參數(get)</h2>
	<form action="user/welcome" method="get">
		<input type="submit" value="user/welcome-get">
		<input type="text" name="user" value="cc">
		<input type="text" name="pwd" value="dd">
	</form>
<h2>普通submit-限制method-帶參數(action)</h2>
	<form action="user/1" method="get">
		<input type="submit" value="user/welcome-帶action參數">
	</form>
<h2>普通submit-限制method-帶參數(param)</h2>
	<form action="user/param" method="post">
		<input type="text" name="user" value="cc">
		<input type="text" name="pwd" value="dd">
		<input type="submit" value="user/welcome-帶action參數">
	</form>
<h2>普通submit-限制method-帶參數(bean)</h2>
	<form action="user/bean">
		<input type="text" name="name" value="willy">
		<input type="text" name="age" value="18">
		<input type="submit" value="user/bean-封裝">
	</form>
<h2>普通submit-redirect(重導向至"welcome" action)</h2>
	<form action="user/redirect">
		<input type="submit" value="普通submit-redirect">
	</form>
<h2>上傳檔案</h2>
	<form action="user/upload" method="post" enctype="multipart/form-data">
          <input type="file" name="file"><br>
          <input type="submit" value="submit">
      </form>
      <h2>錯誤處理</h2>
	<form action="user/error">
          <input type="submit" value="submit">
      </form>
</body>
</html>