<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/5/19
  Time: 10:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>复兰商城--找回密码</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="/static/css/account/retrieve.css" type="text/css">
</head>
<body>
<!--====================引入头部=======================-->
<div class="retrieve-main">
    <div class="retrieve-top">
        <h1>找回密码</h1>
    </div>
    <!--=============填写账户==============-->
    <div class="retrieve-info" id="stepFind_1">
        <dl>
            <dt></dt>
            <dd>
                <em>
                    <i>*</i>
                    账户名：
                </em>
                <input id="userName" class="retrieve-ZHM">
            </dd>
            <dd>
                <em>
                    <i>*</i>
                    验证码：
                </em>
                <input id="vCode" class="retrieve-YZM">
                <img id="imgCode" alt="" src="">
            </dd>
            <dd>
                <span class="retrieve-TJ" id="firstCheck" onclick="">提交</span>
            </dd>
        </dl>
    </div>
    <!--=============验证身份===============-->
    <div class="verify-info" id="stepFind_2">
        <dl>
            <dt></dt>
            <dd>
                <em>
                    账户名：
                </em>
                <!--<input class="retrieve-ZHM" id="">-->
                <label id="user_Name"></label>
            </dd>
            <dd>
                <em>
                    已验证手机：
                </em>
                <label id="phone"></label>
            </dd>
            <dd>
                <em>
                    <i>*</i>
                    图片验证码：
                </em>
                <input class="retrieve-YZM" id="vCode1">
                <img id="imgCode1" alt="" src="">
                <!--<span class="verify-QD">11111</span>-->
            </dd>
            <dd>
            <dd>
                <em>
                    <i>*</i>
                    请填写手机验证码：
                </em>
                <input class="retrieve-YZM" id="checkCode">
                <span class="verify-HQ">获取短信效验码</span>
            </dd>
            <dd>
                <span class="retrieve-TJ" id="secondCheck" onclick="">提交</span>
            </dd>
        </dl>
    </div>
    <!--==================设置新密码================-->
    <div class="newPass" id="stepFind_3">
        <dl>
            <dt></dt>
            <dd>
                <em>
                    <i></i>
                    新的登陆密码：
                </em>
                <input class="retrieve-ZHM" id="newPwd">
                <label id="abl" hidden>输入密码不能为空</label>
            </dd>
            <dd>
                <em>
                    <i>*</i>
                    请再输入一次密码：
                </em>
                <input class="retrieve-ZHM" id="newPwdAgain">
                <label id="abll" hidden>密码不能为空</label>
            </dd>
            <dd>
                <em>
                    <i>*</i>
                    图片验证码：
                </em>
                <input class="retrieve-YZM" id="vodeCheck">
                <img id="imgCode2" alt="" src="">
                <label id="vImage" hidden>验证码不能为空</label>
            </dd>
            <dd>
                <span class="retrieve-TJ" id="thirdCheck">提交</span>
            </dd>
        </dl>
    </div>
    <!--=====================找回密码======================-->
    <div class="findPass" id="stepFind_4">
        <dl>
            <dt></dt>
            <dd class="dd-ok">
                恭喜您，您的新密码已经设置成功，为保证安全,<br>
                建议您定期更改密码以保护账户安全。<br>
                <button id="btn">去逛逛</button>
            </dd>
        </dl>
    </div>
</div>
<!--================================底部版权===================================-->
<script src="/static/js/sea.js"></script>
<!-- Custom js -->
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    //seajs.use('/static/js/modules/mall/0.1.0/findPwd');
    seajs.use('/static/js/modules/mall/0.1.0/findPwd', function (findPwd) {
        findPwd.init();
    });
</script>
</body>
</html>
