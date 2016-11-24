<%--
  Created by IntelliJ IDEA.
  User: fulaan
  Date: 15-8-10
  Time: 下午5:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>阜阳市二十一中学</title>
    <link rel="stylesheet" type="text/css" href="/customizedpage/fuyang21/style/style.css"/>
    <link rel="stylesheet" type="text/css" href="/customizedpage/fuyang21/style/new.css"/>
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">

    <script type="text/javascript" src="/customizedpage/fuyang21/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="/customizedpage/fuyang21/js/flippedClassroom.js"></script>

    <script type="text/javascript" src="/customizedpage/fuyang21/js/indexjq.js"></script>
    <script type="text/javascript" src="/customizedpage/fuyang21/js/iepng.js"></script>
    <!--[if IE 6]>
    <script src="js/iepng.js" type="text/javascript"></script>
    <script type="text/javascript">
        EvPNG.fix('div,ul,img,li,input,span,b,h1,h2,h3,h4,a');
    </script>

    <![endif]-->
    <script type="text/javascript">
        $(function () {

            $('.input-password').keydown(function (event) {
                if (event.which == 13) {
                    loginIndex(1);
                }
            });
            $('.password2').keydown(function (event) {
                if (event.which == 13) {
                    loginIndex(2);
                }
            });
        });


        function loginIndex(index) {
            var username = $(".input-account").val();
            var password = $(".input-password").val();
            if (index==2){
                $('.username2').removeClass('error');
                $('.password2').removeClass('error');
                $('#buttonloadingindex').css('display','');
                $("#logincontent2 .errorMessage").html("");
                username = $("#username2").val();
                password = $("#password2").val();
            }
            if (index==1) {
                $('.login-btn').addClass('active');
            }
            $.ajax({
                url: "/user/login.do",
                type: "post",
                dataType: "json",
                data: {
                    'name': username,
                    'pwd':password
                },
                success: function (data) {
                    if (index==1) {
                        if (data.code == 200) {
                            window.location = "/user?version=91&index=6";
                        } else {

                            if (data.message == '用户名非法') {
                                $('.input-account').addClass('error');
                                $('.username-error').show();
                            } else {
                                $('.input-password').addClass('error');
                                $('.password-error').show();
                            }
                        }
                    } else {
                        if (data.code == 200) {
                            window.location = "/customizedpage/fuyang21/scheduling.jsp";
                            $('#login-bg2').fadeOut('fast');
                        } else {
                            if (data.message == '用户名非法') {
                                $('.username2').addClass('error');
                            } else {
                                $('.password2').addClass('error');
                            }
                        }
                    }
                },
                complete: function () {
                    if (index==1) {
                        $('.login-btn').removeClass('active');
                    } else {
                        $('#buttonloadingindex').css('display','none');
                    }

                }
            });
        }
        $(document).ready(function () {
            $('.username-error').hide();
            $('.password-error').hide();
            $('.input-account').focus(function () {
                $(this).removeClass('error');
                $('.username-error').hide();

            });
            $('.input-password').focus(function () {
                $(this).removeClass('error');
                $('.password-error').hide();

            });

            $(".login-btn").on("click", function () {
                loginIndex(1);
            });
        });
        function closepop() {
            $('#login-bg2').fadeOut('fast');
            $("#username2").val("");
            $("#password2").val("");
        }
    </script>
</head>
<body>
<div id="login-bg2">
    <div id="login2">
        <div id="logincontent2">
            <div class="line" style="line-height:30px; ">
                <div><img src="/img/loginicon.png" /></div>
                <div style="color:rgb(51, 173, 225); font-weight:bold; font-size:14px; margin-left: 10px">登录</div>
                <%--<i class='fa fa-times fa-lg' style="cursor:pointer; float: right; color:rgb(51, 173, 225); line-height: 30px" onclick="closepop()"></i>--%>
            </div>
            <div class="line">
                <input type="text" placeholder="用户名" class="username2" name="username2" id="username2"/>
            </div>
            <div class="line">
                <input type="password" placeholder="密码" class="password2" name="password2" id="password2" oncopy="return false"  onpaste="return false"/>
            </div>
            <div class="line" style="line-height:30px;">
                <img id="buttonloadingindex" src="/img/loading4.gif" style="position: absolute;left:185px;display:none; vertical-align:middle;"/>
                <a <%--href="/customizedpage/fuyang21/scheduling.jsp"--%> target="-_blank"><input style="vertical-align:middle;" type="submit" value=" 登 录 " onclick="loginIndex(2);"></a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
