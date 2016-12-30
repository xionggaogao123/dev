<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>注册</title>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="/static/css/account/main.css">
</head>
<body>

<jsp:include page="../common/head.jsp"/>

<div class="reg-container" style="margin-bottom: 100px">
    <div class="tit">
        <span class="sp1">登录复兰教育社区帐号</span>
        <span class="sp2">如果你的学校正在使用K6KT平台，请使用实名直接登录！&nbsp;&nbsp;&nbsp;没有帐号？<a href="/account/register">马上注册</a></span>
    </div>
    <div class="re-cont">
        <div class="h67"></div>
        <ul class="re-form ul1">
            <li>
                <input class="username um2" type="text" placeholder="用户名/邮箱/手机号">
                <span class="sp3" style="display: none">请输入用户名</span>
            </li>
            <li>
                <input type="password" class="password psw2" placeholder="请输入登录密码">
                <span class="sp5 forget-password">忘记密码？</span>
            </li>
            <li>
                <button>确认</button>
            </li>
            <li>
                <span class="sp-fl" >快捷登录</span>
                <span class="sp-qq" style="cursor: pointer">QQ登录</span>
                <span class="sp-wx" style="cursor: pointer">微信登录</span>
            </li>
        </ul>
    </div>
</div>

<%@include file="../common/footer.jsp" %>

</body>
<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/account/login.js', function (login) {
        login.init();
    });
</script>
</html>