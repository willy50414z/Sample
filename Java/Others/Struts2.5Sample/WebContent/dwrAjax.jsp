<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Struts2 Ajax Google</title>
		<script type='text/javascript' src='/Struts2.5Sample/dwr/engine.js'></script>
		<script type='text/javascript'
			src='/Struts2.5Sample/dwr/interface/KeyWrod.js'></script>
		<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		<script type="text/javascript">
			$(document).ready(function() {

				$("#serch").keyup(function() {
					var key = $("#serch").val();
					KeyWrod.getName(key, function(data) {
						var key = "";
						for (i = 0; i < data.length; i++) {
							key += data[i] + "<br>";
						}
						$("#result").html("<b>" + key + "</b>");
					});

				});

			});
		</script>
</head>
<body>
<center>
			<input type="text" id="serch"
				style="width: 600px; height: 38px; font-size: 20px; font-weight: bold;" />
			<input type="button" id="sub" value="Google 搜尋" style="height: 40px" /><br/>
			<div id="result"></div>
		</center>
</body>
</html>