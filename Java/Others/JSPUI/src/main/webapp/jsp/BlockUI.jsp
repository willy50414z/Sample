<%@ page language="java" contentType="text/html; charset=BIG5"
	pageEncoding="BIG5"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
<!-- <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script> -->
<!-- include BlockUI --> 
<!-- <script	src="https://cdnjs.cloudflare.com/ajax/libs/jquery.blockUI/2.70/jquery.blockUI.js"></script> -->
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery3.4.0.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/blockUI.js"></script>
<script>
	// invoke blockUI as needed -->
	$(document).on('click', '#myButton', function() {
		try {
			$.blockUI();
		} catch (err) {
			alert(err.message == '$.blockUI is not a function');
		}
		$.blockUI();
		setTimeout(function() {
			$(".blockUI").fadeOut();
		}, 3000);
		setTimeout(function() {
			$.unblockUI();
		}, 10000);
	});
</script>
</head>
<body>
	<button id="myButton">myButton</button>
	<button id="alertButton">alertBlockUIButton</button>
</body>
</html>