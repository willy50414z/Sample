<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>JSP解碼測試</title>
	</head>
	<body>
		<form action="login" method="post">
			<table width="423" height="264" border="1" align="center">
				<tr>
					<td colspan="2">
						<center>	登入	</center>
					</td>
				</tr>
				<tr>
					<td width="140">	使用者名稱：</td>
					<td width="267">
						<label for="textfield"></label>
						<input type="text" name="userName" id="textfield">
					</td>
				</tr>
				<tr>
					<td>	密碼：	</td>
					<td>
						<input type="text" name="password" id="textfield2">
					</td>
				</tr>
				<tr>
					<td height="51" colspan="2">
			<input type="submit" name="button" id="button" value="傳送">
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
						