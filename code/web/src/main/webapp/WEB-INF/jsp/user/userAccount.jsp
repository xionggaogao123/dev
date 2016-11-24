<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>复兰商城--找回密码</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="/static/css/user/Lpassword.css">
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
</head>
<body>
<!--====================引入头部=======================-->
<div class="retrieve-main">
    <div class="retrieve-top">
        <h1>找回密码</h1>
        <em>
            没有账号，<a href="/mall/register.do">立即注册</a>
        </em>
    </div>
    <!--=============填写账户==============-->
    <div class="retrieve-info" id="step_1">
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
                    图片验证码：
                </em>
                <input id="vCode" class="retrieve-YZM">
                <img id="imgObj" alt="" src="" onclick="changeImg()">
            </dd>
            <dd>
                <span class="retrieve-TJ" onclick="firstCheck()">下一步</span>
            </dd>
        </dl>
    </div>
    <!--=============验证身份===============-->
    <div class="verify-info" id="step_2">


        <dl id="type_0" style="display:none;">
            <dt></dt>
            <dd>
                <em>
                    <i>*</i>
                    密码提示问题1：
                </em>
                <input placeholder="请说出您其中一位任课老师的姓名？" disabled="disabled" class="retrieve-ZHM retrieve-QZHM">
            </dd>
            <dd>
                <em>
                    <i>*</i>
                    答案：
                </em>
                <input class="retrieve-ZHM" id="teacherName_0">
            </dd>
            <dd>
                <em>
                    <i>*</i>
                    密码提示问题2：
                </em>
                <input placeholder="请说出您其中一位同班同学的姓名？" disabled="disabled" class="retrieve-ZHM retrieve-QZHM">
            </dd>
            <dd>
                <em>
                    <i>*</i>
                    答案：
                </em>
                <input class="retrieve-ZHM" id="studentName_0">
            </dd>
            <dd>
                <span class="retrieve-TJ" onclick="secondCheck()">下一步</span>
            </dd>
        </dl>


        <dl id="type_1" style="display:none;">
            <dt></dt>
            <dd>
                <em>
                    <i>*</i>
                    密码提示问题1：
                </em>
                <input placeholder="请说出您孩子其中一位任课老师的姓名？" disabled="disabled" class="retrieve-ZHM">
            </dd>
            <dd>
                <em>
                    <i>*</i>
                    答案：
                </em>
                <input class="retrieve-ZHM" id="teacherName_1">
            </dd>
            <dd>
                <em>
                    <i>*</i>
                    密码提示问题2：
                </em>
                <input placeholder="请说出您孩子其中一位同班同学的姓名？" disabled="disabled" class="retrieve-ZHM">
            </dd>
            <dd>
                <em>
                    <i>*</i>
                    答案：
                </em>
                <input class="retrieve-ZHM" id="studentName_1">
            </dd>
            <dd>
                <span class="retrieve-TJ" onclick="secondCheck()">下一步</span>
            </dd>
        </dl>


        <dl id="type_2" style="display:none;">
            <dt></dt>
            <dd>
                <em>
                    <i>*</i>
                    密码提示问题1：
                </em>
                <input placeholder="请说出您一个同事的姓名？" disabled="disabled" class="retrieve-ZHM">
            </dd>
            <dd>
                <em>
                    <i>*</i>
                    答案：
                </em>
                <input class="retrieve-ZHM" id="workmate">
            </dd>

            <dd>
                <span class="retrieve-TJ" onclick="secondCheck()">下一步</span>
            </dd>
        </dl>


        <dl id="type_3" style="display:none;">
            <dt></dt>
            <dd>
                <em>
                    <i>*</i>
                    密码提示问题1：
                </em>
                <input placeholder="请说出您一个同事的姓名？" disabled="disabled" class="retrieve-ZHM">
            </dd>
            <dd>
                <em>
                    <i>*</i>
                    答案：
                </em>
                <input class="retrieve-ZHM">
            </dd>

            <dd>
                <span class="retrieve-TJ">下一步</span>
            </dd>
        </dl>

    </div>
    <!--==================绑定信息================-->
    <div class="bound" id="step_3">
        <dl>
            <dt></dt>
            <dd>
                <em>
                    <i>*</i>
                    图片验证码：
                </em>
                <input id="vCode2" class="retrieve-YZM">
                <img id="imgObj2" alt="" src="" onclick="changeImg()">
            </dd>
            <dd>
                <em>
                    <i>*</i>
                    绑定收手机号码：
                </em>
                <input id="mobileNUmber" class="retrieve-ZHM"><img class="bound-IM" src="/img/user/bound-r.png">
                <%-- <span class="bound-HQ" onclick="getVerificationCode()" value="获取短信效验码" >获取短信效验码</span>--%>
                <input class="bound-HQ" type="button" id="btn" value="免费获取验证码" onclick="getVerificationCode(this)"/>
            </dd>
            <dd>
                <em>
                    <i>*</i>
                    验证码：
                </em>
                <input class="retrieve-ZHM" id="checkCode">
            </dd>
            <dd>
                <em>
                    <i>*</i>
                    绑定安全邮箱：
                </em>
                <input class="retrieve-ZHM" id="email">
                <span class="bound-YZ" onclick="sendEmail()">立即验证</span>
            </dd>

            <dd>
                <span class="retrieve-TJ" onclick="thirdCheck()">下一步</span>

            </dd>
        </dl>
    </div>
    <!--==================设置新密码================-->
    <div class="newPass" id="step_4">
        <dl>
            <dt></dt>
            <dd>
                <em>
                    <i></i>
                    新密码：
                </em>
                <input id="newPwd" class="retrieve-ZHM" type="password">
            </dd>
            <dd>
                <em>
                    <i>*</i>
                    确认新密码：
                </em>
                <input id="newPwdAgain" class="retrieve-ZHM" type="password">
            </dd>
            <dd>
                <em>
                    <i>*</i>
                    图片验证码：
                </em>
                <input class="retrieve-YZM" id="vode1" onfocus="checkPwd()">
                <img id="imgObj1" alt="" src="" onclick="changeImg()">
            </dd>
            <dd>
                <span class="retrieve-TJ" onclick="fourCheck()">下一步</span>
            </dd>
        </dl>
    </div>
    <!--=====================找回密码======================-->
    <div class="findPass" id="step_5">
        <dl>
            <dt></dt>
            <dd>
                <em>
                    密码修改成功！
                </em>
            </dd>
        </dl>
    </div>
</div>
<!--================================底部版权===================================-->
<!--====================弹出框==================-->
<div class="bg"></div>
<div class="bound-popup">
    <div class="bound-p-top">
        <em>提示</em><i onclick="hide()">X</i>

    </div>
    <div class="popup-I">
        账户<i id="user_i"></i>，属于高权限用户，无法手动修改密码，<br>
        请联系客服帮助。电话：400-820-6735
    </div>
</div>


<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('userAccount', function (userAccount) {
        userAccount.init();
    });
</script>
</body>
</html>