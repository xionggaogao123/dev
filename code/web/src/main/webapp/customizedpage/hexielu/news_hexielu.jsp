<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>和谐路小学</title>
    <link rel="stylesheet" type="text/css" href="/customizedpage/hexielu/style/style.css"/>
    <link rel="stylesheet" type="text/css" href="/customizedpage/hexielu/style/new.css"/>
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">

    <script type="text/javascript" src="/customizedpage/jinan/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="/customizedpage/jinan/js/indexjq.js"></script>
    <script type="text/javascript" src="/customizedpage/jinan/js/iepng.js"></script>
    <script type="text/javascript" src="/customizedpage/hexielu/js/newsAndNotice.js"></script>

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
                    loginIndex();
                }
            });
        });


        function loginIndex() {
            $('.login-btn').addClass('active');
            $.ajax({
                url: "/user/login.do",
                type: "post",
                dataType: "json",
                data: {
                    'name': $(".input-account").val(),
                    'pwd': $(".input-password").val()
                },
                success: function (data) {
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
                },
                complete: function () {

                    $('.login-btn').removeClass('active');

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
                loginIndex();
            });
        });
    </script>
</head>
<body>
<!-- top -->
<div class="topbox">
    <div class="top">
        <div class="logo">
            <h1>
                <a>公司的名字或广告语写在这里</a>
            </h1>
        </div>
        <div class="toplink" style="">
            <!--================================登录==================================-->
            <a class="login-btn" href="javascript:;">登 录</a>
            <input id="input-first" class="input-password" type="password" placeholder="密码" tabindex="2">
            <input id="input-last" class="input-account" type="text" placeholder="用户名" tabindex="1" value="">
            <!--==========================密码提示===============================-->
            <div id="tips-msg">
                <span class="password-error" style="">密码错误</span>
                <!--a class="forget-pass" href='#'>忘记密码？</a-->
                <span class="username-error">用户名不存在</span>
            </div>

        </div>
    </div>
</div>
<!-- info -->
<div class="middle_info">
    <dl>
        <dt><a href="/customizedpage/hexielu/flippedClassroom.jsp">首页</a>&nbsp;>&nbsp;<a href="/customizedpage/hexielu/hexielu.jsp?type=news">新闻首页</a></dt>
        <dd>
            <table>
                <tr>
                    <!--==============================左边部分==============================-->
                    <td>
                        <div class="middle_left">
                            <ul>
                                <li><a href="/customizedpage/hexielu/hexielu.jsp?type=notice">公告</a></li>
                                <li><a href="/customizedpage/hexielu/hexielu.jsp?type=news" class="cur">新闻</a></li>
                            </ul>
                        </div>
                    </td>
                    <!--==============================公告部分==============================-->
                    <td>
                        <div class="news_line">
                            <dl class="news_right">
                                <div class="news_main_title">asf</div>
                                <div class="news_main_riqi">
                                    <em>2015年06月30日</em>&nbsp;&nbsp;&nbsp;&nbsp;<em>阅读80人</em>
                                </div>
                                <div class="news_main_info">
                                    <img src="http://placehold.it/410x400">
                                </div>
                                <div class="news_main_bottom">
                                    <%--<p>爱了就点开放辣死快点减肥拉开手机对法拉克手机对法拉克司法局
                                        拉会计法埃里克受打击阿里款到即发拉克丝鉴定费阿里快睡觉狄拉克四季度发生颗粒剂发卡量就离开爱家卡洛斯的减肥拉快睡觉阿卡加的拉科技案例</p>--%>


                                </div>
                                <div class="news_main_f">
                                    <a href="hexielu.jsp">< 返回首页新闻</a><i>下一条 ></i><i>< 上一条</i>
                                </div>
                            </dl>
                        </div>
                    </td>
                </tr>
            </table>
        </dd>
    </dl>
</div>

<!-- footer -->
<div class="bg-line"></div>
<div class="footerbox">
    <div class="footer">
        <div class="host">
            <span>主办：和谐路小学 </span><span>技术支持：上海复兰信息科技有限公司</span><span>客服热线：400 820 6735</span>
        </div>
        <div class="footerlink">
            <a href="javascript:void(0);">后台管理</a>|
            <a href="javascript:void(0);">站长中心</a>|
            <a href="javascript:void(0);">帮助中心</a>
        </div>
    </div>
</div>


</body>
</html>