<%--
  Created by IntelliJ IDEA.
  User: fulaan
  Date: 15-4-7
  Time: 下午5:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.net.URLDecoder" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html>
<head>
    <title></title>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>翻转课堂 云中心</title>
    <link rel="stylesheet" href="/customizedpage/meicheng/css/xf.css">
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script src="/customizedpage/meicheng/js/xf.js"></script>
    <script type='text/javascript' src='/static/plugins/bootstrap/js/bootstrap.min.js'></script>
    <script type='text/javascript' src='/static/js/k6kt-index.js'></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
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
<div class="xf_wrap">
    <!-- top -->
    <div class="xf_top">
        <div class="xf_topinner">
            <!-- 电话 -->
            <div class="xf_topleft">
                <img src="/customizedpage/meicheng/images/icon_dh.png" alt="">
                <span>0551-62522688</span>
            </div>
            <!-- 电话end -->
            <!-- 返回主页 -->
            <div class="xf_fhzy">
                <a href="http://www.mceduchina.com/"  target="_blank">返回主页</a>
            </div>
            <!-- 返回主页end -->
            <!-- 关注我们 -->
            <div class="xf_gzwm">
                <span>关注我们：</span>
                <a href="http://weibo.com/u/2378517103" target="_blank"><img src="/customizedpage/meicheng/images/icon_wxgz.png" alt=""></a>
                <a href="http://t.qq.com/meichenggu4963" target="_blank"><img src="/customizedpage/meicheng/images/icon_wbgz.png" alt=""></a>
            </div>
            <!-- 关注我们end -->
        </div>
    </div>
    <!-- topend -->

    <!-- inner -->
    <div class="xf_inner">
        <div class="xf_innerleft">
            <h1>翻转课堂 云中心</h1>
            <p>欢迎开启美诚K-12</p>
            <p>翻转课堂奇妙之旅</p>
            <p class="tsp">美诚国际教育--美国STEAM创新教育中国领导者</p>
            <p class="ywp">MeiCheng International Education- </p>
            <p class="ywp">The Leader of American STEAM innovation Education in China</p>
            <div class="app-link appp">
                <img src="/customizedpage/meicheng/images/iphone.png" class="icon_sj">
                <div class="khddiv">
                    <i>手机客户端 APP</i>
                    <div>
                        <span><img src="/customizedpage/meicheng/images/ios.png"></span>
                        <span>|</span>
                        <span><img src="/customizedpage/meicheng/images/android.png"></span>
                        <span><a href="/mobile">点击下载 &gt;</a></span>
                    </div>
                </div>
            </div>
        </div>
        <div class="xf_innerright">

            <input id="input-last" class="input-account" type="text" placeholder="用户名" tabindex="1"
                   value="<%=userName%>">
            <input id="input-first" class="input-password" type="password" placeholder="密码" tabindex="2">
            <a class="login-btn"  href="javascript:;">
                <img src="/customizedpage/meicheng/images/icon_dl.png">
            </a>
            <div id="tips-msg">
                <span class="password-error">密码错误</span>
                <!--a class="forget-pass" href='#'>忘记密码？</a-->
                <span class="username-error">用户名不存在</span>
            </div>
        </div>
    </div>
    <!-- innerend -->
</div>
</body>
</html>