<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery3.4.0.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-confirm/3.3.2/jquery-confirm.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-confirm/3.3.2/jquery-confirm.min.js"></script>
<style>
.msg-bg {
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    right: 0;
    background: #000;
    opacity: .4;
    z-index: 1050;
}
.msg-alert {
    position: absolute;
    top: 50%;
    left: 50%;
    background: #fff;
    border: 1px solid #eee;
    border-top: 3px solid #2d83ea;
    width: 400px;
    transform: translate(-50%,-50%);
    z-index: 1051;
}
.msg-header,.msg-body{
    border-bottom: 1px solid #eee;
    padding: 15px;
}
.msg-content {
    padding: 15px;
}
.msg-dismiss {
    cursor: pointer;
    background: transparent;
    float: right;
    border: 0;
    line-height: 1;
    font-size: 22px;
    color: #aaa;
    margin-top: -2px;
}
.msg-dismiss:hover {
    color: #bc1b1b;
}
.msg-footer {
    padding: 8px;
    text-align: right;
}
.msg-footer .msg-btnOk {
    background-color: #2f83ea;
    border: 1px solid #2f83ea;
    color: #fff;
    padding: 5px 15px;
}
.msg-footer .msg-btnOk:hover {
    background-color:#3f94fc;
}
.msg-title {
    margin: 0;
}
/*自制提示框 end*/
</style>
<script>
//alert 背景
var alertBg = document.createElement("div");
alertBg.setAttribute("class", "msg-bg");
//alert 外框
var alertWrapper = document.createElement("div");
alertWrapper.setAttribute("class", "msg-alertWrap");
var alertWrapper1 = document.createElement("div");
alertWrapper1.setAttribute("class", "msg-alert");
//alert 內容
var alertBody = document.createElement("div");
alertBody.setAttribute("class", "msg-body");
var alertContent = document.createElement("div");
alertContent.setAttribute("class", "msg-content");
alertBody.append(alertContent);
//alert footer
var alertFooter = document.createElement("div");
alertFooter.setAttribute("class", "msg-footer");
var alertConfirmBtn = document.createElement("button");
alertConfirmBtn.setAttribute("type", "button");
alertConfirmBtn.setAttribute("class", "msg-btnOk");
alertConfirmBtn.innerText="確認";
alertFooter.append(alertConfirmBtn);
//assemble
alertWrapper1.append(alertBody);
alertWrapper1.append(alertFooter);
alertWrapper.append(alertWrapper1);

function alertMsg(text) {
    if ($(".msg-bg") == null) {
    	$("body").append(alertBg);
    }
    $("body").append(alertWrapper);
	$(".msg-alertWrap:last-child .msg-content").html(text);
    $(".msg-btnOk,.msg-dismiss").on("click", function () {
        $(this).parents(".msg-alertWrap").remove();
        if ($(".msg-alertWrap").length == 0) {
            $(".msg-bg").remove();
        }
    })
}

alert = alertMsg
function dd(){
	alert('dd');
}
function confirmMsg(message, action) {
	if(action == null){
		alert(message);
		return true;
	}
    $("body").append(alertBg).append("<div class='msg-alertWrap'><div class='msg-alert'><div class='msg-body'><div class='msg-content'>"+message+"</div></div><div class='msg-footer'><button type='button' class='msg-btnOk'>確認</button><button type='button' class='msg-btnCancel'>取消</button></div></div></div>");
    
	$(".msg-btnCancel").off("click").on("click", function() { //按下Yes時
		$(this).parents(".msg-alertWrap").remove();
		$(".msg-bg").remove();
	});
	$(".msg-btnOk").off("click").on("click", function() { //按下Yes時
		$(this).parents(".msg-alertWrap").remove();
		$(".msg-bg").remove();
		action();
	});
}
// confirm=confirmMsg

function test(){
	 $( "#dialog-confirm" ).dialog({
	      resizable: false,
	      height: "auto",
	      width: 400,
	      modal: true,
	      buttons: {
	        "Delete all items": function() {
		        console.log('aa');
	          $( this ).dialog( "close" );
	        },
	        Cancel: function() {
	        	console.log('bb');
	          $( this ).dialog( "close" );
	        }
	      }
	    });
	 console.log('cc');
}

function test1(){
	alert('abc');
	console.log('cde');
}

function test2(){
	$.confirm({
	    title: 'Confirm!',
	    content: 'Simple confirm!',
	    buttons: {
	        confirm: function () {
	        	console.log('true');
	        	return true;
	        },
	        cancel: function () {
	        	console.log('false');
	        	return false;
	        },
// 	        somethingElse: {
// 	            text: 'Something else',
// 	            btnClass: 'btn-blue',
// 	            keys: ['enter', 'shift'],
// 	            action: function(){
// 	                $.alert('Something else?');
// 	            }
// 	        }
	    }
	});
	console.log('ddd');
}
var tt = null;
function aa(){
	this.tt = null;
	setTimeout(function(){
		promise()
		  .then((success) => {
		    console.log(success);
		    this.tt = success;
		    console.log('aa tt['+tt+']');
		  }, (fail) => {
		    console.log(fail);
		    this.tt = fail;
		    console.log('vv tt['+tt+']');
		  });
	}, 5000);
	
	while(this.tt == null){
		console.log('while tt['+this.tt+']date['+new Date().getTime()+']');
	}
// 	setInterval(function() {console.log(this.tt)}, 1000);
}
function promise() {
  return new Promise((resolve, reject) => {
    // 隨機取得 0 or 1
    const num = Math.random() > 0.5 ? 1 : 0;
    console.log('num => '+num);
    // 1 則執行 resolve，否則執行 reject
    if (num) { 
      resolve('成功');
    }
    reject('失敗')
  });
}
</script>
</head>
<body>
	<div id="dialog-confirm" title="Empty the recycle bin?">
  <p><span class="ui-icon ui-icon-alert" style="float:left; margin:12px 12px 20px 0;"></span>These items will be permanently deleted and cannot be recovered. Are you sure?</p>
</div>
 
<p>Sed vel diam id libero <a href="http://example.com">rutrum convallis</a>. Donec aliquet leo vel magna. Phasellus rhoncus faucibus ante. Etiam bibendum, enim faucibus aliquet rhoncus, arcu felis ultricies neque, sit amet auctor elit eros a lectus.</p>
 
 
</body>
</html>