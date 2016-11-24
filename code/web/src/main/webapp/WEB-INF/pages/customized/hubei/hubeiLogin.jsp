<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/8/16
  Time: 12:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
  <title>湖北省教育厅援藏教学平台</title>
  <meta charset="utf-8">
  <script type="text/javascript" src="/static/js/jquery.min.js"></script>
  <%--<script type="text/javascript" src="js/live.js"></script>--%>
  <link rel="stylesheet" type="text/css" href="/customizedpage/hubei/css/live2.css">
</head>
<body>
<div class="header">
  <img src="/customizedpage/hubei/images/hubeiLogo.png">
</div>
<div class="container">
  <img src="/customizedpage/hubei/images/login_bg2.jpg" class="c-bg">
  <div class="login-wind">
    <p class="p-welcome">欢迎登录</p>
    <P class='p-input'>
      <img src="/customizedpage/hubei/images/img_input1.png">
      <input type="text" placeholder="用户名" id="userName">
    </P>
    <P class='p-input'>
      <img src="/customizedpage/hubei/images/img_input2.png">
      <input type="password" placeholder="密码" id="password">
    </P>
    <button class="btn-sub" onclick="customizedLogin()">登录</button>
    <%--<button class="btn-sub" onclick="rec()">录播</button>--%>
  </div>
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
  function customizedLogin(){
    var userName=$('#userName').val();
    var password=$('#password').val();
    if(userName==""||userName==undefined){
      alert("用户名不能为空！");
      return;
    }
    if(password==""||password==undefined){
      alert("密码不能为空！");
      return;
    }
    var url='/customized/login.do';
    var requestData={};
    requestData.userName=userName;
    requestData.password=password;
    $.ajax({
      type: "GET",
      data:requestData,
      url: url,
      async: false,
      dataType: "json",
      contentType: "application/x-www-form-urlencoded; charset=UTF-8",
      success: function(resp){
        if(resp.code==200){
          location.href="/hubei";
        }else{
          $('#userName').val("");
          $('#password').val("");
          alert(resp.message);
        }
      }
    });

  }

  </script>
</body>
</html>
