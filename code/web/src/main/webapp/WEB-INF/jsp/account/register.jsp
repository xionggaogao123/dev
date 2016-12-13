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

<div id="successDiv" class="wind">
    <p>提示</p>
    <div>
        注册成功！恭喜你，成为复兰教育社区用户！<br>可以在<a href="/mall/userCenter/voucher.do">个人中心</a>查看查看。<br>
        <input type="button" value="确定">
    </div>
</div>

<div class="reg-container" style="margin-bottom: 100px">

    <div class="tit">
        <span class="sp1">登录复兰教育社区帐号</span>
        <span class="sp2">如果你的学校正在使用K6KT平台，请使用实名直接登录！&nbsp;&nbsp;&nbsp;已有帐号？<a href="/login">马上登录</a></span>
    </div>
    <div class="re-cont">
        <div class="h37"></div>
        <div class="tab">
            <span class="tab-cur">手机注册</span><span>邮箱注册</span>
        </div>
        <ul class="re-form ul1">
            <li>
                <input type="text" class="user-name">
                <span class="sp1">用户名 : </span>
                <span class="sp2">6-20位字符（汉字、字母、数字、下划线）</span>
                <span class="sp3" style="display: none">请输入用户名</span>
            </li>
            <li>
                <input type="password" class="password">
                <span class="sp1">设置密码 : </span>
                <span class="sp2">6-20位字符（字母、数字、下划线）</span>
                <span class="sp3" style="display: none">密码</span>
            </li>
            <li>
                <input type="password" class="r-password">
                <span class="sp1">确认密码 : </span>
                <span class="sp2">6-20位字符（字母、数字、下划线）</span>
                <span class="sp3" style="display: none">两次输入的密码不一致</span>
            </li>
            <li>
                <input type="text" id="phone">
                <span class="sp1">手机验证 : </span>
                <span class="sp3" style="display: none">请输入11位正确的手机号码</span>
            </li>
            <li>
                <input type="text" style="display: inline;width: 178px" id="verifyCode">
                <span class="sp1">验证码 : </span>
                <img id="imgObj" alt="" src="/verify/verifyCode.do" style="position: relative;top: 7px;left: 2px"/>
                <span class="sp3" style="display: none">验证码不正确</span>
            </li>
            <li>
                <input type="text" class="in1" id="code">
                <span class="sp1">短信验证码 : </span>
                <span class="sp4 sendYZM">发送验证码</span>
                <span class="sp3" style="display: none">参数不完整</span>
            </li>
            <li>
                <span class="sp2"><label><input type="checkbox" class="argument">我同意《复兰教育社区协议》</label></span>
                <button>确认</button>
                <span class="sp3" style="display: none;top: 26px">请填写参数</span>
            </li>
        </ul>
        <ul class="re-form ul2">
            <li>
                <input type="text" class="user-name">
                <span class="sp1">用户名 : </span>
                <span class="sp2">6-20位字符（汉字、字母、数字、下划线）</span>
                <span class="sp3" style="display: none">请输入用户名</span>
            </li>
            <li>
                <input type="password" class="password">
                <span class="sp1">设置密码 : </span>
                <span class="sp2">6-20位字符（字母、数字、下划线）</span>
                <span class="sp3" style="display: none">密码</span>
            </li>
            <li>
                <input type="password" class="r-password">
                <span class="sp1">确认密码 : </span>
                <span class="sp2">6-20位字符（字母、数字、下划线）</span>
                <span class="sp3" style="display: none">您两次输入的密码不一致</span>
            </li>
            <li>
                <input type="text" class="email-input">
                <span class="sp1">邮箱验证 : </span>
                <span class="sp3" style="display: none">请输入正确的邮箱地址</span>
            </li>
            <li>
                <span class="sp2"><label><input type="checkbox" class="argument" name="argument" value="1">我同意《复兰教育社区协议》</label></span>
                <button>确认</button>
                <span class="sp3" style="display: none;top: 26px">请填写参数</span>
            </li>
        </ul>
    </div>
</div>

<%@include file="../common/footer.jsp" %>
</body>
<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/account/register.js', function (register) {
        register.init();
    });
</script>
</html>