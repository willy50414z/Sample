<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
    <%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
</head>
<body>
<h1>login success</h1>
<h1>�b�� : ${IndexActionForm.userName}/// �K�X : ${IndexActionForm.passWord}</h1>
${IndexActionForm.userName}
-
${userName}
//
${IndexActionForm.passWord}
-
${passWord}
<table>
<logic:iterate id="people" name="personlist">
	<tr>
		<td><bean:write name="people" property="name"/></td>
		<td><bean:write name="people" property="weight"/></td>
		<td><bean:write name="people" property="height"/></td>
	</tr>
</logic:iterate>
<bean:write name="aa"/>
<hr/>

</table>
<html:form action="/TagAction.do">
<h1>�U��struts����</h1><h3>�`�N!!!!�H�U���ҬҶ��t�mAction�Φbactionform���t�mproperty</h3>
<h3>html:checkbox---value�u��έ^��</h3><br/>

<html:checkbox property="checkbox" value="aaa">��|</html:checkbox>
<html:checkbox property="checkbox" value="bbb">����</html:checkbox>
<html:checkbox property="checkbox" value="ccc">���z</html:checkbox><br/>
<h3>html:file</h3><br/>
<html:file property="file"></html:file><br/>
<h3>html:hidden</h3><br/>
<html:hidden property="hidden"/><br/>
<h3>html:multibox--checkbox��List</h3><br/>
<h2>�������Olist�ܼƦW��</h2><br/>
<logic:iterate id="multiboxlist" name="multiboxlist" >
        <html:multibox  property="multiboxlist">
            <bean:write name="multiboxlist"/>
        </html:multibox>
        <bean:write name="multiboxlist"/>
    </logic:iterate>
<h3>html:password</h3><br/>
<html:password property="password"></html:password><br/>
<h3>html:radio</h3><br/>
<html:radio property="radio" value="�ﶵ�@">�ﶵ�@</html:radio><br/>
<html:radio property="radio" value="�ﶵ�G">�ﶵ�G</html:radio><br/>
<html:radio property="radio" value="�ﶵ�T">�ﶵ�T</html:radio><br/>
<h3>html:option</h3><br/>
<html:select property="select">
	<html:option value="select1"></html:option>
	<html:option value="select2"></html:option>
	<html:option value="select3"></html:option>
</html:select><br/>
<h3>html:options</h3><br/>
<html:select property="select1">
<html:options collection="personlist" property="weight" labelName="name" />
</html:select><br/>
<h3>html:text</h3><br/>
<html:text property="text"></html:text><br/>
<h3>html:textarea</h3><br/>
<html:textarea property="textarea"></html:textarea><br/>
<bean:write name="people" property="name"/>
<h3>html:image</h3><br/>
<html:image src="WEB-INF/lib/aa.jpg"></html:image><br/>
<h3>html:submit</h3><br/>
<html:submit></html:submit><br/>
<h3>html:reset</h3><br/>
<html:reset></html:reset><br/>
<h3>html:button</h3><br/>
<html:button property="button"></html:button><br/>
<h3>html:cancel</h3><br/>
<html:cancel></html:cancel><br/>
</html:form>
<h1>Bean ����</h1>
<p><b>bean:write id="���O�W��" property="�ݩʦW��"<br/>
bean:cookie�Bbean:parameter�Bbean:header���������scope����ơA���L�����X�A�u���i��L�ܼ�<br/>
name���header���ݩ�---id����iAttribute��key</b></p>
<br/>
<bean:header name="host" id="hostz"/>
<bean:write name="hostz"/><br/>



<p><b>bean:struts---��JActionForm��T�A���</b></p>
<bean:struts id="tagform" formBean="TagActionForm"/>
<bean:write name="tagform"/>

<p><b>bean:struts---��Jaction�M�g��T�A���</b>--mapping�n��Jpath�Ѽ�</p>
<bean:struts id="mapping" mapping="/IndexAction"/>
<bean:write name="mapping"/>

<p><b>bean:include---��J��L�����A�O�J</b></p>
<bean:include id="google" href="http://www.google.com"/>
<bean:write name="google" filter="false"/><br/>

<h1>logic ����</h1><br/>
<h2><b>logic:present�Plogic:notEmpty�t�O�b��</b></h2>
<ul>
	<li>logic:present�O�u�n�O����Ŷ��S���L�N��null</li>
	<li>logic:notEmpty�����O����Ŷ��n���L�A�Ӫ���value�٤��o����</li>
</ul><br/>
<h3><b>logic:notEmpty name="personlist"--�p�G��personlist�o�Ӫ���</b></h3><br/>
<h3><b>logic:present name="personlist"--�p�Gpersonlist�o�Ӫ������</b></h3><br/>
<logic:notEmpty name="personlist">
	<logic:present name="personlist">
	<table>
		<logic:iterate id="people" name="personlist">
	<tr>
		<td><bean:write name="people" property="name"/></td>
		<td><bean:write name="people" property="weight"/></td>
		<td><bean:write name="people" property="height"/></td>
	</tr>
</logic:iterate>
</table><br/>
	</logic:present>
	<h3><b>logic:present name="personlist"--�p�Gpersonlist111�o�Ӫ���S�����</b></h3><br/>
	<logic:notPresent  name="personlist111">
		<p>personlist111���s�b<p>
	</logic:notPresent>
</logic:notEmpty>

<h1>nest ����</h1><br/>
<h3><b>���MOGNL�|�۰����ڭ�set�ȶiclass�̭��A���ڭ̦baction�������H</b></h3><br/>
		<code>People p = (People) form;</code><br/>
			<code>People people=new people();</code><br/>
			<code>people.setNAME=p.name;</code><br/>
			<code>people.setWEIGHT=p.weight;</code><br/>
			<code>....</code><br/>
		<br/><h3><b>���o����A���Y�ڭ̥Hnested���Nhtml((html:text)-->(nested:text))�A�ڭ̧Y�i�baction���H</b></h3><br/>
		<code>
			People people=(People) form.getPeople()
		</code><br/><h3><b>���覡�������oPeople����</b></h3>


</body>
</html>