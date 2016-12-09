<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" href="/static/css/forum/forum.css">
<link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
<link rel="stylesheet" type="text/css" href="/static/css/train/findtrain.css">
<%--<link rel="stylesheet" type="text/css" href="/static/css/forum/forum.css">--%>
<%--<link rel="stylesheet" type="text/css" href="/static/css/main.css"/>--%>
<script type="text/javascript" src="/static/js/modules/train/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="/static/js/modules/train/jquery.raty.min.js"></script>
<script type="text/javascript"
        src="http://api.map.baidu.com/api?v=2.0&ak=TzFCVsUAf4RzyoOdgZ5tB10fASv5Dswy"></script>
<%--<link rel="stylesheet" href="http://cache.amap.com/lbs/static/main1119.css"/>--%>
<script type="text/javascript"
        src="http://webapi.amap.com/maps?v=1.3&key=3a1cd4cff6fcbdf71ea760da6957fb94"></script>
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
        <a href="" class="find">
            <img src="/static/images/forum/nav_eye.png">
            <span class="nav-txt">发现</span>
            <span class="sp-nav">
                <span class="sp1">找家教</span>
                <span class="sp2" onclick="window.open('/train/trainList')">找培训</span>
                <span class="sp3">在线学习</span>
                <span class="sp4">亲子活动</span>
                <span class="sp5" onclick="window.open('/mate/friend')">找玩伴</span>
            </span>
        </a>
        <a href="/mall" class="ha2" target="_blank">教育商城</a>
        <a id="try" href="/competition" target="_blank" class="ha5">大赛<%--<img src="/static/images/logo_competitioin.png"><img class="hot_a" src="/static/images/forum/hot.png">--%></a>
        <a id="trr" href="/forum" target="_blank" class="ha4">论坛</a><%--<a id="trr" href="/friend" target="_blank" class="ha4">找伙伴</a><%--<a href="/integrate" target="_blank" class="ha3">特惠</a>--%>
        <a href="http://www.k6kt.com/" target="_blank" class="ha6">智慧校园</a>
        <a href="#" class="a-app"><%--<img src="/static/images/forum/forum_phone.png">--%><img class="er" src="/static/images/forum/forum_app.png">手机app</a>
        <c:if test="${login == true}">
            <span<%-- id="logout"--%> style="display: none">[退出]</span>
            <span<%-- id="userName"--%> style="display: none;" onclick="window.open('/forum/userCenter/user.do')">Hi, ${userName}</span>
            <div class="login-already">
                <div class="d1-set">
                    <div class="d1-img"></div>
                    <div class="d1-mk">
                        <div class="p1" onclick="window.open('/forum/userCenter/user.do')"><span></span>个人设置</div>
                        <div class="p2"  id="logout"><span></span>退出</div>
                    </div>
                </div>
                <div class="d2-msg">
                    <div class="d2-notice" <c:if test="${infoCount==0}">style="display: none" </c:if>>${infoCount}</div>
                    <div class="d2-img"></div>
                    <div class="d2-mk">
                        <div class="p1" onclick="window.location.href='/community/playmateNotice.do'"><span></span>玩伴通知<em <c:if test="${friendApplyCount==0}">style="display: none" </c:if>>${friendApplyCount}</em></div>
                        <div class="p2" onclick="window.location.href='/community/mySystemInfo.do'"><span></span>系统消息<em <c:if test="${systemInfoCount==0}">style="display: none" </c:if>>${systemInfoCount}</em></div>

                    </div>
                </div>
                <div class="login-name" id="userName">${userName}</div>
                <img src="${avatar}">
            </div>
        </c:if>
        <c:if test="${login == false}">
            <span class="spanss" onclick="window.open('/mall/register.do')">注册</span>
            <span class="spanss"  id="toplogin">登录</span>
            <a onclick="redirectQ()"><img src="/static/images/forum/Connect_logo_7.png"></a>
            <a onclick="loginWeiXin()"><img src="/static/images/forum/icon24_wx_button.png"/></a>
        </c:if>
    </div>
</div>
