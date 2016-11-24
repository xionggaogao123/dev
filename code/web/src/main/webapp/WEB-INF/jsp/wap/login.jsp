<%@ page contentType="text/html; UTF-8" language="java" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <!--[if IE]>
    <link type="text/css" rel="stylesheet" href="css/web01.css">
    <![endif]-->
    <link type="text/css" rel="stylesheet" href="css/web03.css" media="only screen and (max-width:19020px)"/>
    <!-- <link type="text/css" rel="stylesheet" href="css/detail01.css" media="only screen and (max-width:1080px)" />  -->
    <link type="text/css" rel="stylesheet" href="css/web02.css" media="only screen and (max-width:750px)"/>
    <script type="text/javascript" src="js/jquery-1.11.1.js"></script>
    <script type="text/javascript" src="js/webLogin.js"></script>
    <script src="http://g.tbcdn.cn/mtb/lib-flexible/0.3.4/??flexible_css.js,flexible.js"></script>
</head>
<body>
<img src="images/12334.png" class="l-bg">
<div class="container">
    <img class="head-pic" src="images/fulaan.png">
    <p class="p-input">
        <input type="text" placeholder="用户名/手机号码" id="name">
        <img src="images/input_bg1.png" class="img-input">
    </p>
    <p class="p-input">
        <input type="password" placeholder="输入密码" id="password">
        <img src="images/input_bg2.png" class="img-input">
    </p>
    <button class="btn1" id="login">登录</button>
</div>
<div class="other-txt">其他登录</div>
<div class="line-left"></div>
<div class="line-right"></div>
<div class="other-login">
    <div class="other-logo">
        <img class="oqq" src="images/index_weixin.png" id="wechat">
        <img class="owx" src="images/index_qq.png" id="qq">
    </div>
</div>
</body>

<script type="text/javascript">

    $(function () {
        $('#login').click(function () {
            loginWap();
        });

        $('#qq').click(function () {
            location.href = "/wap/third.do?type=2";
        });

        $('#wechat').click(function () {

            location.href = "/wap/third.do?type=1";
        });
    });

    function loginWap() {

        var name = $('#name').val();
        var password = $('#password').val();
        alert(name + password);
        $.ajax({
            url: "/wap/login.do",
            type: "post",
            dataType: "json",
            data: {
                "name": name,
                "password": password
            },
            success: function (result) {
                if (result.code == 200) {
                    location.href = result.message.map.redirectUrl;
                }

            }
        });
    }
</script>
</html> 