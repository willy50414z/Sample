<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset="UTF-8">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script type="text/javascript">
       function sendMsg(){
    	   //檢核
           var user = [];
           $("input[name='check']:checked").each(function(i){
               user =user+$(this).val()+","
           });
            if (user.length > 0) {
                user = user.substr(0, user.length - 1);
            } else {
               console.log("未選中發送人")
                var content = $("#content").html();
                $("#content").html(content+'<div style="margin-bottom: 8px">\n<p><q style="color: red">'+'系統提示:請在多選框中選擇要發送的用戶'+ '</span></p>\n</div>\n<br/>');
                return
            }
           var msg = $("#msg").val();
		   if(msg!=null){
				$.ajax({
                    method: 'get',
                    url: '/sendmsg',
                    data: {
                        "username": user,
                        "msg": msg
                    },
                    success:function(data) {
                        var content = $("#content").html();
                        $("#content").html(content+'<div style="margin-bottom: 8px">\n<p><q style="color: #eb7350">'+'伺服器推送  '+msg+' -->'+user+ '</span></p>\n</div>\n<br/>');
                        console.log(data);
                    }
                })
		   }else{
			   alert("請填寫要發送的使用者昵稱或者發送內容");
		   }
	   }

	   function sendAll(){
		   var msg = $("#msg").val();
		   if(msg!=null){
               $.ajax({
                   method: 'get',
                   url: '/sendAll',
                   data:{
                       msg:msg
                   },
                   success:function(data) {
                       var content = $("#content").html();
                       $("#content").html(content+'<div style="margin-bottom: 8px">\n<p><q style="color: #eb7350">'+'伺服器推送  '+msg+' --> 所有用戶'+ '</span></p>\n</div>\n<br/>');
                       console.log(data);
                   }
               })
		   }else{
			   alert("請填寫要發送的內容");
		   }
	   }
       function connect(){
	       if ('WebSocket' in window){
	           ws = new WebSocket("ws://127.0.0.1:8090/socketServer/niezhiliang9595");
	       }
	       else if ('MozWebSocket' in window){
	           ws = new MozWebSocket("ws://127.0.0.1:8090/socketServer/niezhiliang9595");
	       }
	       else{
	           alert("該流覽器不支持websocket");
	       }
	       ws.onmessage = function(evt) {
	           var content = $("#content").html();
	           $("#content").html(content+'<div style="text-align: right;margin-bottom: 8px">\n<p><q style="color: mediumorchid;text-align: right">'+evt.data+ '</span></p>\n</div>\n<br/>');
	           console.log(msg)
	       };
	       ws.onclose = function(evt) {
	          console.log('連接關閉')
	       };
	       ws.onopen = function(evt) {
	           var content = $("#content").html();
	           $("#content").html(content+'<div style="margin-bottom: 8px">\n<p><q style="color: #eb7350">'+'伺服器初始化成功...'+ '</span></p>\n</div>\n<br/>');
	          console.log('連接成功')
	       };
       }
       connect();
   </script>
</head>
<body>
	<h1 style="margin-top: 100px">服務端</h1>
	<div class="layui-card-body-inline" style="padding-bottom: 20px">
		<div class="layui-form-item">
			<label class="layui-form-label">線上數量:${num}</label>
		</div>

		<form class="layui-form" action="">
			<label class="layui-form-label">線上人:</label><br>
			<c:forEach var="user" items="${users}">
				<input type="checkbox" value="${user}" name="check">${user}
			</c:forEach>
		</form>

		<div class="layui-form-item">
			<label class="layui-form-label">資訊內容:</label> <input id="msg"
				type="text" name="title" placeholder="請輸入要發送的內容" autocomplete="off"
				class="layui-input">
		</div>

		<button onclick="sendMsg()">發送</button>
		<button onclick="sendAll()">全部發送</button>
	</div>
	<div class="layui-card" style="margin-top: 100px">
		<div class="layui-card-header">
			<h1>操作詳情</h1>
		</div>
		<div id="content" class="layui-card-body" placeholder="請輸入要發送的內容">
		</div>
	</div>
</body>
</html>