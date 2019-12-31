<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Spring Boot+WebSocket+廣播式</title>
<!-- jquery  -->
	<script src="/js/jquery.js"></script>
	<!-- stomp協議的用戶端指令碼 -->
	<script src="/js/stomp.js"></script>
	<!-- SockJS的用戶端指令碼 -->
	<script src="/js/sockjs.js"></script>

	<script type="text/javascript">
		var stompClient = null;

		function setConnected(connected) {
			document.getElementById('connect').disabled = connected;
			document.getElementById('disconnect').disabled = !connected;
			document.getElementById('conversationDiv').style.visibility = connected ? 'visible'
					: 'hidden';
			$('#response').html();
		}

		function connect() {
			// websocket的連接位址(Broker)，在SimpleWsBroker中註冊
			var socket = new SockJS('/websocket-simple'); //1
			stompClient = Stomp.over(socket);
			stompClient.connect({}, function(frame) {
				setConnected(true);
				console.log('Connected: ' + frame);
				// 訂閱/topic/getResponse這個message queue
				stompClient.subscribe('/topic/getResponse', function(respnose) {
					//處裡回傳值方式
					showResponse(JSON.parse(respnose.body).responseMessage);
				});
			});
		}
		function showResponse(message) {
			var response = $("#response");
			response.html(message + "<br\>" + response.html());
		}

		function disconnect() {
			if (stompClient != null) {
				stompClient.disconnect();
			}
			setConnected(false);
			console.log("Disconnected");
		}

		function sendName() {
			var name = $('#name').val();
			// 握手號發送/receive至後端
			stompClient.send("/receive", {}, JSON.stringify({
				'name' : name
			}));
		}
	</script>
</head>
<body>
<body onload="disconnect()">
	<div>
		<div>
			<button id="connect" onclick="connect();">連接</button>
			<button id="disconnect" disabled="disabled" onclick="disconnect();">斷開連接</button>
		</div>
		<div id="conversationDiv">
			<label>輸入你的名字</label><input type="text" id="name" />
			<button id="sendName" onclick="sendName();">發送</button>
			<p id="response"></p>
		</div>
	</div>
</body>
</body>
</html>
