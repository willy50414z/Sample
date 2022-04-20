<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
<!-- <script type="text/javascript" -->
<%-- 	src="<%=request.getContextPath()%>/js/jquery3.4.0.js"></script> --%>
<script>
	function loadJS(){
		var fileref = document.createElement('script')//创建标签
		fileref.setAttribute("type", "text/javascript")//定义属性type的值为text/javascript
		fileref.setAttribute("src",
				"<%=request.getContextPath()%>/js/jquery3.4.0.js")//文件的地址
		document.getElementsByTagName("head")[0].appendChild(fileref)
	}
	function checkJs(){
		if (typeof jQuery == 'undefined') {
			alert("no");
		} else {
			alert("yes");
		}
	}
	function checkLoadJs(){
		if (typeof jQuery == 'undefined') {
			alert("no");
			loadJS();
			if (typeof jQuery == 'undefined') {
				alert("no");
			} else {
				alert("yes");
			}
		} else {
			alert("yes");
		}
	}
	
	$(function() {
		$("#submitByForm").on("click", function() {
			$("#TestSubmit").submit();
		})
		$("#submitByJs").on("click", function() {
			// 			$.post("/TestSubmit", {col1:"val1",col2:"val2",col3:"val3"}, function(result){});
			var temp = document.createElement("form");
			temp.action = "/TestSubmit";
			temp.method = "post";
			temp.style.display = "none";

			opt = document.createElement("textarea");
			opt.name = "col1";
			opt.value = "val1";
			temp.appendChild(opt);
			opt = document.createElement("textarea");
			opt.name = "col2";
			opt.value = "val2";
			temp.appendChild(opt);
			opt = document.createElement("textarea");
			opt.name = "col3";
			opt.value = "val3";
			temp.appendChild(opt);

			document.body.appendChild(temp);
			temp.submit();
		})
	})
</script>
</head>
<body>
	<form id="TestSubmit" action="/TestSubmit" method="POST">
		<input type="hidden" name="col1" value="val1"/>
		<input type="hidden" name="col2" value="val2"/>
		<input type="hidden" name="col3" value="val3"/>
	</form>
	<button id="submitByForm">submitByForm</button>
	<button id="submitByJs1">submitByJs</button>
	<button id="submitByJs2" onClick="loadJS()">loadJs</button>
	<button id="submitByJs3" onClick="checkJs()">checkJs</button>
	<button id="submitByJs4" onClick="checkLoadJs()">checkLoadJs</button>
</body>
</html>