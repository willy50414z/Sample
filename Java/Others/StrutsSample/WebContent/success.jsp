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
<h1>帳號 : ${IndexActionForm.userName}/// 密碼 : ${IndexActionForm.passWord}</h1>
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
<h1>各種struts標籤</h1><h3>注意!!!!以下標籤皆須配置Action及在actionform中配置property</h3>
<h3>html:checkbox---value只能用英文</h3><br/>

<html:checkbox property="checkbox" value="aaa">體育</html:checkbox>
<html:checkbox property="checkbox" value="bbb">音樂</html:checkbox>
<html:checkbox property="checkbox" value="ccc">物理</html:checkbox><br/>
<h3>html:file</h3><br/>
<html:file property="file"></html:file><br/>
<h3>html:hidden</h3><br/>
<html:hidden property="hidden"/><br/>
<h3>html:multibox--checkbox的List</h3><br/>
<h2>全部都是list變數名稱</h2><br/>
<logic:iterate id="multiboxlist" name="multiboxlist" >
        <html:multibox  property="multiboxlist">
            <bean:write name="multiboxlist"/>
        </html:multibox>
        <bean:write name="multiboxlist"/>
    </logic:iterate>
<h3>html:password</h3><br/>
<html:password property="password"></html:password><br/>
<h3>html:radio</h3><br/>
<html:radio property="radio" value="選項一">選項一</html:radio><br/>
<html:radio property="radio" value="選項二">選項二</html:radio><br/>
<html:radio property="radio" value="選項三">選項三</html:radio><br/>
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
<h1>Bean 標籤</h1>
<p><b>bean:write id="類別名稱" property="屬性名稱"<br/>
bean:cookie、bean:parameter、bean:header都能獲取該scope的資料，不過不能輸出，只能放進其他變數<br/>
name表示header的屬性---id為塞進Attribute的key</b></p>
<br/>
<bean:header name="host" id="hostz"/>
<bean:write name="hostz"/><br/>



<p><b>bean:struts---放入ActionForm資訊再顯示</b></p>
<bean:struts id="tagform" formBean="TagActionForm"/>
<bean:write name="tagform"/>

<p><b>bean:struts---放入action映射資訊再顯示</b>--mapping要放入path參數</p>
<bean:struts id="mapping" mapping="/IndexAction"/>
<bean:write name="mapping"/>

<p><b>bean:include---放入其他網頁再嵌入</b></p>
<bean:include id="google" href="http://www.google.com"/>
<bean:write name="google" filter="false"/><br/>

<h1>logic 標籤</h1><br/>
<h2><b>logic:present與logic:notEmpty差別在於</b></h2>
<ul>
	<li>logic:present是只要記憶體空間沒有他就報null</li>
	<li>logic:notEmpty不但記憶體空間要有他，該物件的value還不得為空</li>
</ul><br/>
<h3><b>logic:notEmpty name="personlist"--如果有personlist這個物件</b></h3><br/>
<h3><b>logic:present name="personlist"--如果personlist這個物件有顯示</b></h3><br/>
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
	<h3><b>logic:present name="personlist"--如果personlist111這個物件沒有顯示</b></h3><br/>
	<logic:notPresent  name="personlist111">
		<p>personlist111不存在<p>
	</logic:notPresent>
</logic:notEmpty>

<h1>nest 標籤</h1><br/>
<h3><b>雖然OGNL會自動幫我們set值進class裡面，但我們在action中仍須以</b></h3><br/>
		<code>People p = (People) form;</code><br/>
			<code>People people=new people();</code><br/>
			<code>people.setNAME=p.name;</code><br/>
			<code>people.setWEIGHT=p.weight;</code><br/>
			<code>....</code><br/>
		<br/><h3><b>取得物件，但若我們以nested取代html((html:text)-->(nested:text))，我們即可在action中以</b></h3><br/>
		<code>
			People people=(People) form.getPeople()
		</code><br/><h3><b>的方式直接取得People物件</b></h3>


</body>
</html>