<%@ page language="java" contentType="text/html; charset=BIG5"
	pageEncoding="BIG5"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>

<!-- <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script> -->
<!-- include BlockUI -->
<!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.blockUI/2.70/jquery.blockUI.js"></script> -->
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery3.4.0.js"></script>
	<script type="text/javascript"
 	src="<%=request.getContextPath()%>/js/jquery.datatables.js"></script>
<!-- 	<script type="text/javascript" -->
<%--  	src="<%=request.getContextPath()%>/js/EcomMwSec_zh-tw.js"></script> --%>
<!-- <script type="text/javascript" -->
<%-- 	src="<%=request.getContextPath()%>/js/blockUI.js"></script> --%>
<script>
	$(function() {
		$("#block").on("click", function() {
			$.blockUI();
			setTimeout(function() {
				$.unblockUI();
			}, 10000);
		})

		$("#loadJs").on("click", function() {
			$.getScript("<%=request.getContextPath()%>/js/blockUI.js",function(){  //載入test.js,成功後，並執行回撥函式
				console.log("載入js檔案");
		});
		})
		$("#loadECom").on("click", function() {
			$.trim($(this).data("authJsPath"))
			$("#loadECom, #CheckEComLoad").data("tes","ff");
			console.log($(this).data("tes"));
			$.getScript("<%=request.getContextPath()%>/js/EcomMwSec_zh-tw.js",function(){  //載入test.js,成功後，並執行回撥函式
				alert($("#CTBCPKIAPI").length>0);
		});
		})
		$("#CheckEComLoad").on("click", function() {
			console.log($(this).data("tes"));
			alert($("#CTBCPKIAPI").length);
		})
	})
		
</script>
</head>
<body>
	<button id="block">Block</button>
	<button id="loadJs">LoadJs</button>
	<button id="loadECom">LoadECom</button>
	<button id="CheckEComLoad">CheckEComLoad</button>
</body>
</html>