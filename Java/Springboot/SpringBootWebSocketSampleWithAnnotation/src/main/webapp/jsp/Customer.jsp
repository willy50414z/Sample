<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset="UTF-8">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script type="text/javascript">
	var ws = null;
	function connect() {
		var username = $("#username").val()
		if (username != null) {
			//清空信息區
			$("#content").html('');
			
			//初始化websocket
			if ('WebSocket' in window) {
				ws = new WebSocket("ws://127.0.0.1:8090/socketServer/" + $("#username").val());
			} else if ('MozWebSocket' in window) {
				ws = new MozWebSocket("ws://127.0.0.1:8090/socketServer/" + $("#username").val());
			} else {
				alert("该浏览器不支持websocket");
			}
			
			//註冊接收信息時event
			ws.onmessage = function(evt) {
				var content = $("#content").html();
				$("#content").html( content + '<div style="text-align: right;margin-bottom: 8px">\n<p><q style="color: mediumorchid">服务端:</q><span>'+ evt.data + '</span></p>\n</div>\n<br/>');
			};
			//註冊斷開連線時event
			ws.onclose = function(evt) {
				var content = $("#content").html();
				$("#content").html(content + '<div style="margin-bottom: 8px">\n<p><q style="color: coral">客户端:</q><span>连接中断</span></p>\n</div>\n<br/>');
			};
			//註冊開啟連線時event
			ws.onopen = function(evt) {
				$("#content").html('<div style="margin-bottom: 8px">\n<p><q style="color: coral">客户端:</q><span>连接成功...</span></p>\n</div>\n<br/>');
			};
		} else {
			alert("请输入您的昵称");
		}
	}

	//發送消息
	function sendMsg() {
		ws.send($("#writeMsg").val());
		var content = $("#content").html();
		$("#content").html(content+ '<div>\n<p><q style="color: coral">客户端:</q><span>'+ $("#writeMsg").val() + '</span></p>\n</div>\n<br/>');
	}
</script>
</head>
<body>
	<div>
		<h1 class="threed">WebSocket客户端</h1>
		<h1>客户端</h1>

		<input id="username" type="text" name="title" placeholder="请输入昵称"
			autocomplete="off" class="layui-input">
		<button class="layui-btn" onclick="connect()">连接</button>
		<hr>
		<label class="layui-form-label">请发送内容</label> <input id="writeMsg"
			type="text" name="title" placeholder="请输入要发送的内容" autocomplete="off"
			class="layui-input">
		<button class="layui-btn" onclick="sendMsg()">发送</button>


		<div style="margin-top: 100px">
			<div class="layui-card-header">
				<h1>操作详情</h1>
			</div>

			<div id="content" class="layui-card-body">
				<h3 align="center"
					style="color: #007DDB; margin-top: 20px; margin-bottom: 20px">这里将显示操作信息</h3>
			</div>
		</div>
	</div>
</body>
</html>