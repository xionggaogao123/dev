<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2017/11/2
  Time: 9:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Java后端WebSocket的Tomcat实现</title>
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
    <script type="text/javascript" src="/static/js/modules/forum/jquery-browser.js"></script>
    <script type="text/javascript" src="/static/js/modules/forum/jquery.qqFace.js"></script>
</head>
<body login="${login}">
Welcome<br/><input id="text" type="text"/>
<button onclick="send()">发送消息</button>
<hr/>
<button onclick="closeWebSocket()">关闭WebSocket连接</button>
<hr/>
<div id="message"></div>

<span id="lg" <c:if test="${login == false}"> style="display: none" </c:if>>Hi, ${userName}</span>
<span id="ll" <c:if test="${login == true}"> style="display: none" </c:if>>登录</span>
</body>

<script type="text/javascript">
    var login = $('body').attr('login');
    if (login=="false") {
        var websocket = null;

        var userId = null;
        //判断当前浏览器是否支持WebSocket
        if ('WebSocket' in window) {
            websocket = new WebSocket('ws://' + window.location.host + '/ws?tokenId=${tokenId}');
        }
        else {
            alert('当前浏览器 Not support websocket')
        }

        //连接发生错误的回调方法
        websocket.onerror = function () {
            setMessageInnerHTML("WebSocket连接发生错误");
        };

        //连接成功建立的回调方法
        websocket.onopen = function () {
            setMessageInnerHTML("WebSocket连接成功");
        }

        //接收到消息的回调方法
        websocket.onmessage = function (event) {
//        setMessageInnerHTML(event.data);
            var message = event.data;
            if (message.indexOf(",") > -1) {
                var items = message.split(",");
                if (items[0] == "T20") {
                    userId = items[1];
                    closeWebSocket();
                }
            } else {
                setMessageInnerHTML(event.data);
            }
        }

        //连接关闭的回调方法
        websocket.onclose = function () {
            setMessageInnerHTML("WebSocket连接关闭");
            if (null != userId && userId != "") {
                var param = {};
                param.userId = userId;
                param.tokenId = ${tokenId};
                var url = "/user/tokenLogin.do"
                $.ajax({
                    type: "GET",
                    data: param,
                    url: url,
                    async: false,
                    dataType: "json",
                    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                    success: function (result) {
                        if(result.code=="200"){
                            $('#lg').show();
                        }else{
                            alert(result.message);
                        }
                    }
                });
            }
        }

        //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
        window.onbeforeunload = function () {
            closeWebSocket();
        }

        window.setTimeout(closeWebSocket, 10000);

        //将消息显示在网页上
        function setMessageInnerHTML(innerHTML) {
            document.getElementById('message').innerHTML += innerHTML + '<br/>';
        }

        //关闭WebSocket连接
        function closeWebSocket() {
            websocket.close();
        }

        //发送消息
        function send() {
            var message = document.getElementById('text').value;
            websocket.send(message);

        }
    }
</script>
</html>
