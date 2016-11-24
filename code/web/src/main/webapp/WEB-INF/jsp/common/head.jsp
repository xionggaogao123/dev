<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" href="/static/css/forum/forum.css">
<link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
<script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript">
    $(function () {

        $('#toplogin').click(function () {//登录
            $('.store-register').fadeToggle();
            $('.bg').fadeToggle();
        })

        $('#logout').click(function () {//退出登录
            logout();
        });

        $('#quitLog').click(function () {//退出登录
            logout();
        });


        $('#loginText').click(function () {//登录
            $('.store-register').fadeToggle();
            $('.bg').fadeToggle();
        })

        $('#redirect').click(function () {//登录

        })

    })

    function logout() {
        $.ajax({
            url: "/user/logout.do",
            type: "post",
            dataType: "json",
            data: {
                'inJson': true
            },
            success: function () {
                location.reload();
            }
        });
        ssoLoginout();
    }


    function ssoLoginout() {
        var logoutURL = "http://ah.sso.cycore.cn/sso/logout";

        $.ajax({
            url: logoutURL,
            type: "GET",
            dataType: 'jsonp',
            jsonp: "callback",
            crossDomain: true,
            cache: false,
            success: function (html) {

            },
            error: function (data) {

            }
        });
    }

    function showLoginTitle(login) {
        if (login) {
            $('.header-bar').show();
            $('.header').hide();
        } else {
            $('.header').hide();
            $('.header-bar').show();
        }
    }

    function redirectQ() {

        location.href = '/user/qqlogin.do?historyUrl=' + encodeURIComponent(encodeURIComponent(window.location.href));
    }

    function loginWeiXin() {

        location.href = '/user/wechatlogin.do?historyUrl=' + window.location.href;
    }

</script>

<div class="header">
    <div class="header-cont">
        <img src="/static/images/entrance/fl_mall_logo.png" style="cursor: pointer" onclick="window.open('/')">
        <a href="/" target="_blank" class="ha1">首页</a>
        <a href="/community/communityAllType.do" target="_blank">我的社区</a>
        <a id="try" href="/competition" target="_blank" class="ha5">
            <img src="/static/images/logo_competitioin.png">大赛<img class="hot_a" src="/static/images/forum/hot.png">
        </a>
        <a id="trr" href="/forum" target="_blank" class="ha4">论坛</a>
        <%--<a id="trr" href="/friend" target="_blank" class="ha4">找伙伴</a>--%>
        <a href="/mall" class="ha2" target="_blank">商城</a>
        <a href="/integrate" target="_blank" class="ha3">特惠</a>
        <a href="http://www.k6kt.com/" target="_blank" class="ha6">智慧校园</a>
        <a href="#" class="a-app"><img src="/static/images/forum/forum_phone.png">
            <img class="er" src="/static/images/forum/forum_app.png">
            手机app
        </a>
        <c:if test="${login == true}">
            <span id="logout">[退出]</span>
            <span id="userName" onclick="window.open('/forum/userCenter/user.do')">Hi, ${userName}</span>
        </c:if>
        <c:if test="${login == false}">
            <span onclick="window.open('/mall/register.do')">注册</span>
            <span id="toplogin">登录</span>
            <a onclick="redirectQ()"><img src="/static/images/forum/Connect_logo_7.png"></a>
            <a onclick="loginWeiXin()"><img src="/static/images/forum/icon24_wx_button.png"/></a>
        </c:if>
    </div>
</div>
