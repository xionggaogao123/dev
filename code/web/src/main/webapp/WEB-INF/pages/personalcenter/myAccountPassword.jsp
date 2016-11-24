<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>修改密码</title>
    <meta charset="utf-8">

    <link rel="stylesheet" href="/static/css/nivo-slider.css" type="text/css"/>
    <link rel="stylesheet" href="/static/css/main-nivo-thumb.css" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script src="/static/js/jquery.nivo.slider.js"></script>
    <script src="/static/js/validate-form.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/css/message.css"/>
    <!--<script type="text/javascript" src="/js/bxslider/jquery.bxslider.js"></script>

    <link rel="stylesheet" type="text/css" href="/js/bxslider/jquery.bxslider.css" />-->
    <style type="text/css">
        .orange-submit-btn.active {
            background: #32A2E3 url(/img/loading4.gif) no-repeat 13px 7px;
        }
    </style>
    <script src="/static/js/experienceScore.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript">
        var currentPageID = 4;
        $(function () {
            <%
                Object score = request.getAttribute("score");
                if (score != null) {
            %>
            scoreManager("<%=request.getAttribute("scoreMsg")%>", <%=score%>);
            <%
                }else if(request.getAttribute("passwordChanged") != null){
            %>
            alert("密码修改成功！");
            <%
                }else if(request.getAttribute("passwordError") != null){
            %>
            alert("密码修改失败！");
            <%
                }
            %>
            $(".currentSelected").removeClass("currentSelected");
            $(".icon_config").addClass("currentSelected");

            $('.orange-submit-btn').on('click', function () {
                //window.location.href='/password';
                $(this).addClass('active');
            });

            var currentUrl = window.location.href;
            if (/editphoto/.test(currentUrl)) {
                $('a[href="/editphoto"]').addClass('selected');
                $('a[href="/editphoto"]').attr("style", "color:#fff");
            } else if (/password/.test(currentUrl)) {
                $('a[href="/password"]').addClass('selected');
                $('a[href="/password"]').attr("style", "color:#fff");
            }
            if("${firstlogin}"=="Y"){
                if(!confirm("您第一次登录系统,请修改密码!")){
                    window.location = "/user/homepage.do";
                }
                else{

                }
            }
        });
        function validatePassword() {
            if (validateForm({
                                loginpassword: {required: true, maxLength: 20},
                                newpassword: {required: true, maxLength: 20},
                                newrpassword: {required: true, maxLength: 20, confirm: "newpassword"},
                                verycode:{required: true, maxLength:4}
                            },
                            {con: $('.account-tb tr:lt(4)').find('td:last'), msg: ['登录密码', '新密码', '确认新密码','验证码']})) {
                if (checkPassword()) {
                    $('.orange-submit-btn').removeClass('active');

                    //发送ajax请求
                    $.ajax({
                        type: "POST",
                        url: "/personal/modifypassword.do",
                        data: {
                            password: $('#newpassword').val(),
                            verycode: $('#verycode').val(),
                            jCaptchaResponse: $('#jCaptchaResponse').val()
                        },
                        async: false,
                        success: function (data) {
//                             alert(data.score);
                            if ($.trim(data.result) == "success") {
                                alert("密码修改成功！");
//                                 changeImg();
                            }
                            else if ($.trim(data.result) == "error") {
                                alert("密码修改失败！");
                                changeImg();
                            }
                            else if ($.trim(data.result) == "vcOutTime") {
                                alert("验证码超时失效！");
                                changeImg();
                            }
                            else if ($.trim(data.result) == "vcIsNull") {
                                alert("验证码为空！");
                                changeImg();
                            }
                            else{
                                alert("验证码错误！");
                                changeImg();
                            }
                        }
                    });

                    $('#loginpassword').val('');
                    $('#newpassword').val('');
                    $('#newrpassword').val('');
                    $('#verycode').val('');
                    return false;
                } else {
                    alert("您输入的登陆密码有误，请重新输入！");
                    $('.orange-submit-btn').removeClass('active');


                    $('#loginpassword').val('');
                    $('#newpassword').val('');
                    $('#newrpassword').val('');
                    $('#verycode').val('');
                    return false;
                }
            }
            $('.orange-submit-btn').removeClass('active');



            $('#loginpassword').val('');
            $('#newpassword').val('');
            $('#newrpassword').val('');
            $('#verycode').val('');
            return false;
        }
        //验证用户名密码是否正确
        function checkPassword() {
            flag = false;
            $.ajax({
                type: "POST",
                url: "/personal/checkpass.do",
                data: {password: $('#loginpassword').val()},
                async: false,
                success: function (data) {
                    if ($.trim(data['repeat']) == $.trim('yes')) {
                        flag = true;
                    } else {
                        flag = false;
                    }
                }
            });
            return flag;
        }
        function changeImg(){
            var imgSrc = $("#imgObj");
            //var src = "verify/verifyCode.do";
            var src =imgSrc.attr("src");
            imgSrc.attr("src",chgUrl(src));
        }
        //时间戳
        //为了使每次生成图片不一致，即不让浏览器读缓存，所以需要加上时间戳
        function chgUrl(url){
            var timestamp = (new Date()).valueOf();
            url = url.substring(0,20);
            if((url.indexOf("&")>=0)){
                url = url + "×tamp=" + timestamp;
            }else{
                url = url + "?timestamp=" + timestamp;
            }
            return url;
        }
        //	    function changeImage() {
        //	        var jcaptcha_image = document.getElementById("jcaptcha_image");
        //	        var timestamp = new Date().getTime();
        //	        jcaptcha_image.src = "/user/jcaptcha_image.action?timestamp=" + timestamp;
        //	        var jCaptchaResponse = document.getElementById("jCaptchaResponse");
        //	        jCaptchaResponse.value = "";
        //	        jCaptchaResponse.focus();
        //	    }
    </script>

</head>
<body>


<%@ include file="../common/header.jsp" %>
<div id="content_main_container">
    <div id="content_main">

        <%@ include file="../common_new/col-left.jsp" %>
        <div id="right-container">


            <div id="content_main_container" style="margin-top: 5px;">
                <div id="main-content" style="position: relative; overflow:hidden;" class="main-content-msg">
                    <div id="account-right">
                        <div id="account-right-title">
                            <div id="account-r-sel">
                                <a href="/basic" target="_self">基本信息</a>
                                <a href="/editphoto" target="_self">修改头像</a>
                                <a href="/password" target="_self">修改密码</a>
                                <%--<a href="" target="-_self">基本信息</a>--%>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <div id="account-right-content" class="account-right-content">
                            <form action="/modifypassword" method="post" onsubmit="return validatePassword()">
                                <table class="account-tb">
                                    <tr>
                                        <td class="account-tb-r">* 登录密码：</td>
                                        <td><input type="password" name="" id="loginpassword"
                                                   style="border:1px solid #CCC; border-radius: 4px;height: 20px;padding: 2px;width: 200px"/>
                                        </td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td class="account-tb-r">* 新的登录密码：</td>
                                        <td><input type="password" name="password" id="newpassword"
                                                   style="border:1px solid #CCC; border-radius: 4px;height: 20px;padding: 2px;width: 200px"/>
                                        </td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td class="account-tb-r">*确认新密码：</td>
                                        <td><input type="password" name="rpassword" id="newrpassword"
                                                   style="border:1px solid #CCC; border-radius: 4px;height: 20px;padding: 2px;width: 200px"/>
                                        </td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td class="account-tb-r">*验证码：</td>
                                        <td>
                                            <input id="verycode" style="border:1px solid #CCC; border-radius: 4px;height: 20px;padding: 2px;width: 80px" name="verycode" type="text"/>
                                            <img id="imgObj" alt="" src="verify/verifyCode.do"/>
                                            <a href="javascript:;" onclick="changeImg()">看不清楚？换一张</a>
                                        </td>
                                        <td></td>
                                    </tr>
                                    <%--<tr>--%>
                                    <%--<td class="account-tb-r" valign="top">*验证码：</td>--%>
                                    <%--<td> <input type="text" name="jCaptchaResponse" value="" id="jCaptchaResponse"/></td>--%>
                                    <%--<td>--%>
                                    <%--<img align="absmiddle" id="jcaptcha_image"   src="/user/jcaptcha_image.action"/>--%>
                                    <%--<a href="javascript:changeImage()" >看不清楚？换一张</a>--%>
                                    <%--<font color="red" style="margin-left: 30px;">${errorMessage }${fieldErrors.jCaptchaResponse[0] }</font>--%>
                                    <%--</td>--%>
                                    <%--</tr>--%>
                                    <tr>
                                        <td class="account-tb-r"></td>
                                        <td colspan="2">
                                            <input type="submit" class="orange-submit-btn" value="保存"/>
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="clear"></div>

        </div>
    </div>
</div>

<%@ include file="../common_new/foot.jsp" %>
</body>
</html>