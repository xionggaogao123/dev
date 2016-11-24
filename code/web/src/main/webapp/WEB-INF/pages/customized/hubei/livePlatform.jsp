<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/8/15
  Time: 11:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
  <title>湖北省教育厅援藏教学平台</title>
  <meta charset="utf-8">
  <script type="text/javascript" src="/static/js/jquery.min.js"></script>
  <%--<script type="text/javascript" src="js/live.js"></script>--%>
  <link rel="stylesheet" type="text/css" href="/customizedpage/hubei/css/live.css">
</head>
<body>
<div class="header">
  <img src="/customizedpage/hubei/images/hubeiLogo.png">
    <div class="header-rebtn">
        <button class="btn1" onclick="window.open('/hubei/hubeishuiguohu')">观看直播</button>
        <button class="btn2" onclick="rec()">观看录播</button>
    </div>
  <span class="span-hello cu-pointer" onclick="customizedLogout()">退出</span>
  <span class="span-hello">hi,${userName}</span>
</div>
<div class="container">
  <img src="/customizedpage/hubei/images/live_bg.jpg" class="c-bg">
  <h1 class="h11">湖北省<br>水果湖高级中学</h1>
<%--    <a  style="cursor: pointer"  class="a1">观看直播</a>
    <a  style="cursor: pointer" class="a1" onclick="rec()">录播回顾</a>--%>
  <h1 class="h12">西藏自治区<br>山南市第二高级中学</h1>
<%--    <a  style="cursor: pointer" onclick="window.open('/hubei/hubeishuiguohu')" class="a2">观看直播</a>
    <a  style="cursor: pointer" class="a2" >录播回顾</a--%>>
</div>
<div class="footer">
  <div class="footer-cont">
    <span class="sp1">主办：湖北省教育厅&nbsp;&nbsp;&nbsp;&nbsp;/</span>
    <span class="sp1">技术支持：上海复兰信息科技有限公司&nbsp;&nbsp;&nbsp;&nbsp;/</span>
    <span class="sp1">客服热线：400 820 6735</span>
    <%--<a class="sp2">版主中心</a>--%>
    <%--<a class="sp2">站长管理&nbsp;|</a>--%>
    <%--<a class="sp2">后台管理&nbsp;|</a>--%>
  </div>
</div>

<script type="text/javascript">
  function rec(){
    window.open('/recordHubei');
  }
  function customizedLogout(){

    var url='/customized/logout.do';
    $.ajax({
      type: "GET",
      data:{},
      url: url,
      async: false,
      dataType: "json",
      contentType: "application/x-www-form-urlencoded; charset=UTF-8",
      success: function(resp){
         location.href="/customized/hubei/hubeiLogin.do";
      }
    });

  }

</script>
</body>
</html>
