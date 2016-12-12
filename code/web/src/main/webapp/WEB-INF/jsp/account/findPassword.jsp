<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>找回密码</title>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="/static/css/account/main.css">

</head>
<body>

<jsp:include page="../common/head.jsp"/>

<div class="reg-container">
    <div class="tit2">
        <span class="sp1">找回密码</span>
        <span class="sp2">如果你的学校正在使用K6KT平台，请使用实名直接登录！&nbsp;&nbsp;&nbsp;已有帐号？<a href="/login">马上登录</a></span>
    </div>
    <div class="re-cont">
        <ul class="ul-luc">
            <li class="li1 orali">
                <span class="sp1">1</span>
                <span class="sp2"></span>
                <span class="sp3">填写账户</span>
            </li>
            <li class="li1">
                <span class="sp1">2</span>
                <span class="sp2"></span>
                <span class="sp3">验证身份</span>
            </li>
            <li class="li1">
                <span class="sp1">3</span>
                <span class="sp2"></span>
                <span class="sp3">设置新密码</span>
            </li>
            <li class="li2">
                <span class="sp1">√</span>
                <span class="sp3">完成</span>
            </li>
        </ul>
        <div class="re-cont1 re-conts">
            <ul class="re-form ul1">
                <li>
                    <input type="text" placeholder="请输入注册的用户名/邮箱/手机号" class="username">
                    <span class="sp1">用户名 : </span>
                </li>
                <li>
                    <input type="text" class="in1 verifyCode1">
                    <span class="sp1">验证码 : </span>
                    <img id="verifyImg2" alt="" src="/verify/verifyCode.do" class="sp6" style="top: 5px;width: auto;height: auto"/>
                </li>
                <li>
                    <button class="re-btn1">下一步</button>
                </li>
            </ul>
        </div>

        <div class="re-cont2 re-conts">
            <div class="tab">
                <span class="tab-cur">手机验证</span><span>邮箱验证</span>
            </div>
            <ul class="re-form ul1">
                <li>
                    <input type="text">
                    <span class="sp1">手机验证 : </span>
                    <span class="sp3">请输入11位正确的手机号码</span>
                </li>
                <li>
                    <input type="text" class="in1">
                    <span class="sp1">验证码 : </span>
                    <img id="verifyImg" alt="" src="/verify/verifyCode.do" class="sp6" style="top: 5px;width: auto;height: auto"/>
                </li>
                <li>
                    <input type="text" class="in1">
                    <span class="sp1">短信验证码 : </span>
                    <span class="sp4">发送验证码</span>
                </li>
                <li>
                    <span class="sp2"><label><input type="checkbox">我同意《复兰教育社区协议》</label></span>
                    <button class="re-btn2">下一步</button>
                </li>
            </ul>
            <ul class="re-form ul2">
                <li>
                    <input type="text">
                    <span class="sp1">邮箱 : </span>
                </li>
                <li>
                    <button class="re-btn-email">确认并发送邮件</button>
                </li>
            </ul>
            <ul class="re-form ul21">
                <li>
                    <span class="sp7">我们已经向您的注册邮箱shawn****s@qq.com发送了一封密码找回邮件，请您注意<a href="###">接收邮件</a></span>
                </li>
                <li>
                    <button class="re-btn2">去邮箱接收邮件</button>
                </li>
            </ul>


            <!--未绑定手机/邮箱-->
            <ul class="re-form ul11">
                <li>
                    <span class="sp7">该账户未绑定手机号码，您也可以通过使用电子邮箱的方式找回密码</span>
                </li>
                <li>
                    <button>去试试</button>
                </li>
            </ul>


        </div>
        <div class="re-cont3 re-conts">
            <ul class="re-form ul3">
                <li>
                    <input type="text">
                    <span class="sp1">新密码 : </span>
                </li>
                <li>
                    <input type="text">
                    <span class="sp1">新密码确认 : </span>
                    <span class="sp3">两次输入的密码不一致</span>
                </li>
                <li>
                    <button class="re-btn3">下一步</button>
                </li>
            </ul>
        </div>
        <div class="re-cont4 re-conts">
            <ul class="re-form ul3">
                <li>
                    <span class="sp7">恭喜你，密码重置成功！</span>
                </li>
                <li>
                    <button>去登录</button>
                </li>
            </ul>
        </div>
    </div>
</div>
</body>

<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/account/findPassword.js', function (findPassword) {
        findPassword.init();
    });
</script>
</html>