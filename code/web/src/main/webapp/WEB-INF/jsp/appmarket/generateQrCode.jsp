<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2017/11/9
  Time: 9:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
    <script type="text/javascript" src="/static/js/modules/forum/jquery-browser.js"></script>
    <script type="text/javascript" src="/static/js/modules/forum/jquery.qqFace.js"></script>
    <script type="text/javascript" src="/static/js/jquery.qrcode.min.js"></script>
</head>
<body>
<div id="qrcode" style="margin-left: 300px;margin-top: 200px;"></div>
<br/>
<div><span>填入二维码路径:</span><input type="text" id="url"></div>
<span style="cursor: pointer;color: #00CE05" onclick="generate()">生成二维码</span>
<script type="text/javascript">
    function generate() {
        var url=$.trim($('#url').val());
        if(url==""){
            alert("路径不能为空");
            return;
        }
        $('#qrcode').qrcode({
            render: "canvas", //也可以替换为table
            width: 500,
            height: 500,
            text: url
        });
    }
</script>

</body>
</html>
