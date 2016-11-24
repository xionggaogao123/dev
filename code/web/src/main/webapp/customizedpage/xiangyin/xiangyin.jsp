<%@ page import="java.net.URLDecoder" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
    <meta name="renderer" content="webkit">
    <title>复兰科技 K6KT-快乐课堂</title>
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css" />
    <link rel="stylesheet" type="text/css" href="/customizedpage/xiangyin/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/customizedpage/xiangyin/css/main.css"/>
    <link rel="stylesheet" href="/static/css/nivo-slider.css" type="text/css" />
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type='text/javascript' src='/static/plugins/bootstrap/js/bootstrap.min.js'></script>
    <script type='text/javascript' src='/static/js/k6kt-index.js'></script>
    <script src='http://static.polyv.net/file/polyvplayer_v2.0.min.js'></script>
    <script src="/static/js/jquery.nivo.slider.js"></script>
    <%
        String userName = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("momcallme".equals(cookie.getName())) {
                    userName = URLDecoder.decode(cookie.getValue(), "UTF-8");
                }
            }
        }
    %>
</head>
<body>
<%@ include file="/WEB-INF/pages/common/ypxxhead.jsp" %>

<div id="intro-player">
    <div id="player_div">

    </div>
    <span onclick="closeMovie()" class="close-dialog"><i class="fa fa-times-circle fa-lg"></i></span>
</div>

<div class="main-container">
    <div class="login-bar" style="margin: 0 auto;">
        <div class='title-bar-container' style="height:105px;overflow: hidden;z-index: 1">
            <img class="title-logo" src="/customizedpage/xiangyin/img/xiangyinglogo.bmp">
            <!-- <a id="teacher-test" href="/application-use">教师申请试用</a> -->
            <div>
              <%--  <a class="login-teacher" href="http://www.czzx.sh.cn" style=""   target="_blank">学校官方网站</a>--%>
                <a class="login-btn" href="javascript:;" style="text-decoration: none">登 录</a>

                <input class="input-password" type="password" placeholder="密码" tabindex="2">
                <input class="input-account" type="text" placeholder="用户名" tabindex="1"
                       value="<%=userName%>">

                <div id="tips-msg">
                    <span class="password-error">密码错误</span>
                    <!--a class="forget-pass" href='#'>忘记密码？</a-->
                    <span class="username-error">用户名不存在</span>
                </div>
            </div>
        </div>
    </div>
    <div class="main-content-container">

        <div class='content-container' style="z-index: 1">




            <img class="text-1" src="/img/K6KT/main-page/text-1.png"/>
            <img class="text-2" src="/img/K6KT/main-page/text-2.png"/>

           <%-- <a onclick="playMovie()">
                <div class="player-hand">
                    <div></div>
                </div>
            </a>--%>
           <%-- <div class="teacher-apply" onclick="go2appuse()">
                教师申请试用
            </div>--%>
            <div class="app-link">
                <img src="/img/K6KT/iphone.png"/>

                <div>
                    <div>手机客户端 APP</div>
                    <div>
                        <span><img src="/img/K6KT/ios.png"/></span>
                        <span>|</span>
                        <span><img src="/img/K6KT/android.png"/></span>
                        <span><a href="/mobile">点击下载 ></a></span>
                    </div>
                </div>
            </div>

            <div class="monitor-div">
                <div class="carousel nivoSlider" id='slider'>
                    <img src="/img/K6KT/main-page/screen-1.png"/>
                    <!--img src="/img/K6KT/main-page/screen-2.png"/ -->
                </div>
            </div>
        </div>
       <%-- <img src="img/logo_changzheng.jpg" style="background-repeat: no-repeat;height: 480px;width: 100%;position: absolute;top:130px;">--%>
    </div>
</div>
<div style="height: 30px;text-align: center;line-height: 30px;;width: 100%;background-color:#EAE9DC;position: relative;top:-20px;border-bottom: 1px solid #C0C0C0;font-family: 'Microsoft YaHei';font-weight: bold;color: #5a5a5a">
    <%--<p>欢迎北京大学入驻复兰大学K6KT-翻转课堂</p>--%>
</div>
<!-- 页尾 -->
<%@ include file="/WEB-INF/pages/common/flippedroot.jsp"%>
<!-- 页尾 -->
<%--<div style="position: absolute;right: 0;bottom: 0;" id="changzheng">
    <a href="http://www.czzx.sh.cn"  target="_blank"><img src="/customizedpage/changzheng/img/logo_CZ%201.png"><a><br>
    <a href="http://jclass.pte.sh.cn/jclass/"   target="_blank"><img src="/customizedpage/changzheng/img/logo_CZ%202.png"><a>
</div>--%>
</body>
<script>
   $(function(){
       var winH = $(window).height();
       var winW = $(window).width();
       if(winH < 770){
           $('#changzheng').css({bottom:0});
       }else{
           $('#changzheng').css({bottom:150});
       }
   });







</script>
<script>
    function go2appuse(){
        window.location.href="/customizedpage/application.jsp";
    }
</script>
<script>
    $(function(){
        var h=$(window).height()
        var w=$(window).width();
        if(h<700){
            $("#II").css({height:276,width:401,marginLeft:-200,marginTop:-138})
        }
    })

</script>