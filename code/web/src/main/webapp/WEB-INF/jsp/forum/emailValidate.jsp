<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>注册成功</title>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
    <style type="text/css">
        .rs-cont {
            margin: 134px auto 256px;
            width: 516px;
            height: 147px;
            padding: 27px 0 0 80px;
            background: url('/static/images/forum/mall_register_ok.png') no-repeat 33px 35px;
            border: 2px solid #F4F3F4;
        }

        .rs-cont p {
            line-height: 30px;
            height: 30px;
            padding: 0;
            margin: 0;
            font-size: 16px;
        }

        .rs-cont p a {
            color: #49769E;
            text-decoration: none;
            font-size: 13px;
            margin-right: 20px;
        }

        .rs-cont p a:hover {
            text-decoration: underline;
        }

        .lunt-index-hide {
            display: none;
        }
    </style>
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
</head>
<body login="${login}">
<input type="text" id="email" value="${email}" hidden>
<input type="text" id="code" value="${code}" hidden>
<%@ include file="../common/head.jsp" %>
<div class="rs-cont">
    <p>感谢您注册 复兰教育社区</p>
    <p>系统给您发送了一封激活邮件，快去登录邮箱激活账号吧！</p>
    <p><a href="#" onclick="repeatSend()">重新接收验证邮件</a><a href="#" onclick="window.open('/')">先去逛逛</a></p>
</div>
<script type="text/javascript">
    function repeatSend() {
        var email = $('#email').val();
        var code = $('#code').val();
        var requestData = {};
        requestData.email = email;
        requestData.emailValidateCode = code;
        $.ajax({
            type: "get",
            data: requestData,
            url: '/mall/users/processValidate.do',
            async: false,
            dataType: "json",
            traditional: true,
            success: function (result) {
                if (result.code == 200) {
                    alert("邮件发送成功！");
                }
            }
        });
    }
</script>
<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>
<!--删除商品弹出框-->
<!--============登录================-->
<%@ include file="../common/login.jsp" %>
</body>
</html>
