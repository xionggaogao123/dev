<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>注册</title>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
    <script src="//assets.kf5.com/supportbox/main.js" id="kf5-provide-supportBox"
            kf5-domain="fulankeji.kf5.com"></script>
    <style>
        .lunt-index-hide {
            display: none;
        }
    </style>
    <script src="/static/js/sea.js"></script>
    <script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
    <script>
        seajs.use('/static/js/modules/mall/0.1.0/register');
    </script>
</head>
<body>
<%@ include file="head.jsp" %>
<div class="tc-nav-top" <c:if test="${type == 2}">style="display: none;"</c:if>>
    <img src="/static/images/forum/weixin_logo_fr.png" class="tec-logo">
    <p><em>Hi,${userName}，</em>欢迎使用${thirdType}账号登陆复兰教育社区</p>
</div>
<div class="tc-nav-top" <c:if test="${type == 1}">style="display: none;"</c:if>>
    <img src="/static/images/forum/qq_logo_fr.jpg" class="tec-logo">
    <p><em>Hi,${userName}，</em>欢迎使用${thirdType}账号登陆复兰教育社区</p>
</div>
<p class="tc-nav">
    <span class="sp1 cur-tc">创建新账号</span>
    <span class="sp2">已有账号</span>
</p>
<div class="register-main" style="width: 720px;">
    <dl>
        <dd style="position: relative">
            <div class=""><img class="waitload" src="../../../images/zz.png" width="25px" height="25px" hidden></div>
            <em></em>
            <button class="regi-now">立即注册</button>
        </dd>
    </dl>
</div>
<div class="login-main">
    <input type="hidden" value="<c:out value="${objectId}" ></c:out>" id="cacheThirdId">
    <dl>
        <dd>
            <em>用户名：</em>
            <input type="text" placeholder="用户名/邮箱/手机号" id="account">
        </dd>
        <dd>
            <em>密码：</em>
            <input type="password" id="password">
        </dd>
        <dd>
            <em style="color:#fff;">ss</em>
            <button id="loginBind">登陆</button>
        </dd>
    </dl>
</div>
<%@ include file="../mall/footer.jsp" %>
</body>
</html>
