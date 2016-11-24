<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta property="qc:admins" content="227117016363634637575144"/>
    <title>注册复兰教育社区</title>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
    <!-- Start of KF5 supportbox script -->
    <script src="//assets.kf5.com/supportbox/main.js" id="kf5-provide-supportBox"
            kf5-domain="fulankeji.kf5.com"></script>
    <!-- End of KF5 supportbox script -->
</head>
<body>
<%--===============头部================--%>
<%--<%@ include file="head.jsp" %>--%>
<%--===============头部================--%>
<div class="store-home-top">
    <div class="store-top-center">
        <a href="/mall" class="store-top-SCSY">商城首页</a>
        <a href="/mall">Home&gt;用户注册</a>
        <div>
            <%--<a>--%>
            <%--<em id="car">我的购物车</em><i id="goodsCount">（0件商品）</i>--%>
            <%--</a>--%>
        </div>
    </div>
</div>
<!--======================注册=============================-->
<div class="register-main" style="width: 65%;">
    <dl>
        <dt>
        <h1>注册复兰教育社区账号</h1><span>已有账号，<a class="show">马上登录</a>！</span>
        </dt>
        <%--<dt style="border:none;height: 130px;text-align: center">
           <img src="../../../images/hot_banner_zc.png">
        </dt>--%>
        <%--<dd>
          <h1>注册复兰商城账号，购买正品精品！</h1>
        </dd>--%>
        <dd style="padding:0 0 0 175px;height: 30px;line-height: 30px;font-weight: bold;font-size: 14px;color:#ff8e1f;">
            如果您的学校正在使用K6KT平台，请使用实名直接登录！
        </dd>
        <dd>
            <em><i>*</i>用户名：</em><input id="name">
            <span id="name1">6-20位字符，支持汉字、字母、数字、下划线、特殊字符</span>
            <span class="register-ZC" id="name2" style="line-height: 37px;">用户名最多20位</span>
            <span class="register-ZC" id="name3" style="line-height: 37px;">用户名不能是邮箱</span>
            <span class="register-ZC" id="name4" style="line-height: 37px;">用户名不能是手机号</span>
            <span class="register-ZC" id="name5" style="line-height: 37px;">用户名至少6位</span>
            <span class="register-ZC" id="name6" style="line-height: 37px;">用户名已被使用，请重新输入</span>
        </dd>
        <dd>
            <em><i>*</i>设置密码：</em><input id="pswd" type="password">
            <span id="pswd1">6-20位字符，支持字母、数字、下划线、特殊字符</span>
            <span class="register-ZC-W" id="pswd2">密码比较简单，建议您更改为复杂密码，如“字母+数字+特殊字符”的组合</span>
            <span class="register-ZC" id="pswd3" style="line-height: 37px;">密码长度只能在6-20位字符之间</span>
        </dd>
        <dd>
            <em><i>*</i>确认密码：</em><input id="pswdcopy" type="password">
            <span class="register-ZC" id="pswdcopy2" style="line-height: 37px;">两次输入密码不一致</span>
        </dd>
        <dd>
            <em>选择注册方式：</em>
            <label class="sel-phone"><input type="radio" name="registway" style="width: 20px;" value="1"
                                            id="radio-phone"/>手机</label>
            <label class="sel-mail"><input type="radio" name="registway" style="width: 20px;" value="2"/>邮箱</label>
        </dd>
        <dd class="dd-phone">
            <em><i>*</i>验证手机：</em><input id="phone">
            <span id="phone1">完成验证后，您可以用该手机号登录和找回密码</span>
            <span class="register-ZC" id="phone2" style="line-height: 37px;">请输入正确的手机号码</span>
            <span class="register-ZC" id="phone3">该手机号已被使用，请重新输入。如果您是该用户，请立即<a
                    class="show">登录</a><%--或<a>找回密码</a>--%></span>
        </dd>
        <dd class="dd-img">
            <em><i>*</i>验证码：</em><input id="verifyCode" style="width: 110px"><img id="imgObj" alt=""
                                                                                  src="/verify/verifyCode.do"
                                                                                  style="position: relative;top: 7px;left: 2px"/>
            <span id="verifyCode1" style="line-height: 37px;">完成验证后，您可以收到手机验证码</span>
            <span class="register-ZC" id="verifyCode2" style="line-height: 37px;">请输入图片验证码</span>
            <span class="register-ZC" id="verifyCode3" style="line-height: 37px;">图片验证码错误或已失效<%--或<a>找回密码</a>--%></span>
        </dd>
        <dd class="dd-phone">
            <em><i>*</i>手机验证码：</em><input class="register-YZ">
            <button class="register-HQ">点击获取</button>
            <span class="register-ZC-G" id="YZ1" style="line-height: 37px;">已发送</span>
        </dd>
        <dd class="dd-mail">
            <em><i>*</i>邮箱：</em><input id="email">
            <span id="email1" style="line-height: 37px;">请填写您的常用邮箱</span>
            <span class="register-ZC" id="email2" style="line-height: 37px;">请输入有效的邮箱</span>
            <span class="register-ZC" id="email3">该邮箱已被使用，请重新输入。如果您是该用户，请立即<a
                    class="show">登录</a><%--或<a>找回密码</a>--%></span>
        </dd>
        <dd>
            <em></em><input type="checkbox" class="register-TY" checked id="ty">我同意<a href="/agreement.html"
                                                                                      target="_blank">《复兰教育社区协议》</a>
            <span class="register-ZC" id="ty2" style="line-height: 37px;">请接收服务条款</span>
        </dd>
        <dd style="position: relative">
            <div class=""><img class="waitload" src="../../../images/zz.png" width="25px" height="25px" hidden></div>
            <em></em>
            <button class="register-LJ">立即注册</button>
        </dd>
    </dl>
</div>


<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>

<!--=============================注册弹出框,赠送的现金券现已到账，================================-->
<!--==========背景============-->
<div class="bg" hidden></div>
<!--=============注册成功送优惠券=================-->
<div id="successDiv" class="wind">
    <p>提示</p>
    <div>
        注册成功！恭喜你，成为复兰教育社区用户！<br>可以在<a href="/mall/userCenter/voucher.do">个人中心</a>查看查看。<br>
        <input type="button" value="确定">
    </div>
</div>

<div id="failDiv" class="wind">
    <p>提示</p>
    <div>
        邮箱未激活，请去激活！
    </div>
</div>


<div class="store-register" hidden>
    <dl>
        <dt>
            <em>登录</em>
            <%--<div>没有账号，<a href="register.do">立即注册</a></div>--%>
            <i id="close">X</i>
        </dt>
        <dd>
            <div hidden id="accountError" class="error">帐户名与密码不匹配，请重新输入</div>
            <h1>中国最专业的青少年素质教育社区!</h1>
            <div class="store-MF" hidden>该登录名不存在<a href="/mall/register.do">免费注册？</a></div>
        </dd>
        <dd>
            <em>用户名：</em><input type="text" placeholder="用户名/邮箱/手机号" id="account">
        </dd>
        <dd>
            <em>密码：</em><input type="password" id="password">
            <span class="mm-forget" onclick="window.open('/mall/findUserPwd.do')">忘记密码</span>
        </dd>
        <%--<dd>--%>
        <%--<em>验证码：</em><input class="store-IN" id="verifyCode">--%>
        <%--<span><img id="vc" alt="" src="/verify/verifyCode.do"></span>--%>
        <%--</dd>--%>
        <dd>
            <div class="store-YZ error" hidden id="passwordError">密码错误</div>
            <div class="store-YZ error" hidden id="vcError">验证码错误</div>
            <div class="store-YZ error" hidden id="vcEmpty">验证码为空</div>
            <em></em><span class="store-DL" id="logIn">登录</span>
        </dd>
    </dl>
</div>
<script src="/static/js/sea.js"></script>
<!-- Custom js -->
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/mall/0.1.0/register');
</script>
</body>
</html>
