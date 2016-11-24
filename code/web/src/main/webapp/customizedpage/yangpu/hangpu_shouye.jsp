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
    <link rel="stylesheet" type="text/css" href="/admin/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/css/font-awesome.min.css"/>
    <link rel="stylesheet" type="text/css" href="/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/business/ypxx/special/yangpu/css/main.css"/>
    <link rel="stylesheet" href="/css/nivo-slider.css" type="text/css"/>
    <script type="text/javascript" src="/js/jquery-1.11.1.min.js"></script>
    <script type='text/javascript' src='/js/jquery.min.js'></script>
    <script type='text/javascript' src='/admin/static/js/bootstrap.min.js'></script>
    <script type='text/javascript' src='/js/k6kt-index.js'></script>
    <script type="text/javascript" src="/js/sharedpart.js"></script>
    <script src='http://static.polyv.net/file/polyvplayer_v2.0.min.js'></script>
    <script src="/js/jquery.nivo.slider.js"></script>
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
    <script type="text/javascript">
        $(function () {
            var strCookie=document.cookie;
            var arrCookie=strCookie.split("; ");
            var closeflag = false;
            for(var i=0;i<arrCookie.length;i++){
                var arr=arrCookie[i].split("=");
                if("closesheep"==arr[0]){
                    closeflag = true;
                }
            }
            if(!closeflag) {
                document.getElementById('light').style.display='block';
            }
        });

        function closesheepdiv() {
            document.cookie = "closesheep=true";
            $('#light').hide();
        }
    </script>
</head>
<body>
<%@ include file="ypxxhead.jsp" %>

<div id="intro-player">
    <div id="player_div">

    </div>
    <span onclick="closeMovie()" class="close-dialog"><i class="fa fa-times-circle fa-lg"></i></span>
</div>

<div class="main-container">
    <div class="login-bar" style="margin: 0 auto;">
        <div class='title-bar-container' style="height:105px;overflow: hidden">
            <img class="title-logo" src="/business/ypxx/special/yangpu/img/huangpu_3.png">
            <!-- <a id="teacher-test" href="/application-use">教师申请试用</a> -->
            <a class="login-btn" href="javascript:;">登 录</a>
            <input id="input-first" class="input-password" type="password" placeholder="密码" tabindex="2">
            <input id="input-last" class="input-account" type="text" placeholder="用户名" tabindex="1"
                   value="<%=userName%>">

            <div id="tips-msg">
                <span class="password-error">密码错误</span>
                <a class="forget-pass" href='#'>忘记密码？</a>
                <span class="username-error">用户名不存在</span>
            </div>
        </div>
    </div>
    <div class="main-content-container">
        <div class='content-container'>
            <img class="text-1" src="/img/K6KT/main-page/text-1.png"/>
            <img class="text-2" src="/img/K6KT/main-page/text-2.png"/>

            <a onclick="playMovie()">
                <div class="player-hand">
                    <div></div>
                </div>
            </a>
            <%--<div class="teacher-apply" onclick="go2appuse()">

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
    </div>
    <div style="height: 30px;text-align: center;line-height: 30px;;width: 100%;background-color:#EAE9DC;position: relative;top:-20px;border-bottom: 1px solid #C0C0C0;font-family: 'Microsoft YaHei';font-weight: bold;color: #5a5a5a">
        <%--<p>欢迎北京大学入驻复兰大学K6KT-翻转课堂</p>--%>
    </div>
    <div style="clear: both"></div>

</div>
<!-- 页尾 -->
<%@ include file="/WEB-INF/pages/common/flippedroot.jsp"%>

<div id="light" class="white_content" style="display: none" >

    <a onclick="closesheepdiv()" class="close_but"></a>
</div>
<!-- 页尾 -->
</body>
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
</html>