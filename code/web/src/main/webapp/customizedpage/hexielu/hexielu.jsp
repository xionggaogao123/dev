<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>和谐路小学</title>
    <link rel="stylesheet" type="text/css" href="/customizedpage/hexielu/style/style.css"/>
    <link rel="stylesheet" type="text/css" href="/customizedpage/hexielu/style/new.css"/>
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">

    <script type="text/javascript" src="/customizedpage/hexielu/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="/customizedpage/hexielu/js/indexjq.js"></script>
    <script type="text/javascript" src="/customizedpage/hexielu/js/iepng.js"></script>
    <script type="text/javascript" src="/customizedpage/hexielu/js/hexielu.js"></script>
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
<input type="hidden" id="hexieluSchoolId" value="559cf00b63e73dbde154a70b">
<div class="middle_info">
    <dl>
        <dt><a href="/customizedpage/hexielu/flippedClassroom.jsp">首页</a>&nbsp;>&nbsp;<span id="hexielu_type">新闻首页<span></dt>
        <dd>
            <table>
                <tr>
                    <!--==============================左边部分==============================-->
                    <td>
                        <div class="middle_left">
                            <ul>
                                <li><a href="/customizedpage/hexielu/hexielu.jsp?type=notice" class="cur">公告</a></li>
                                <li><a href="/customizedpage/hexielu/hexielu.jsp?type=news">新闻</a></li>
                            </ul>
                        </div>
                    </td>
                    <!--==============================公告部分==============================-->
                    <td>
                        <div class="middle_right_I middle_right_II">
                            <div>
                                <dl class="recomand_notice">
                                    <%--<dt>
                                        <em>推荐公告</em>
                                    </dt>
                                    <dd>
                                        <a href="notice_hexielu.jsp">爱丽丝的飞机拉开就是地方了卡健身拉了分开就阿里上看见对方了卡机斯蒂芬李会计爱丽丝的疯狂就拉斯加对方离开房</a>
                                        <i>2015年06月30日</i>
                                    </dd>--%>

                                </dl>
                                <div class="middle_view"></div>
                                <dl class="recent_notice">
                                    <%--<dt>
                                        <em>最新公告</em>
                                    </dt>
                                    <dd>
                                        <a href="notice_hexielu.jsp">爱丽丝的飞机拉开就是地方了卡健身拉了分开就阿里上看见对方了卡机斯蒂芬李会计爱丽丝的疯狂就拉斯加对方离开房</a>
                                        <i>2015年06月30日</i>
                                    </dd>
--%>
                                </dl>
                                <!--分页-->
                                <div class="notice-page-paginator">
                                    <span class="first-page">首页</span>
                                    <span class="page-index">
                                        <span class="active">1</span>
                                        <span>2</span>
                                        <span>3</span>
                                        <span>4</span>
                                        <span>5</span>
                                        <i>···</i>
                                    </span>
                                    <span class="last-page">尾页</span>

                                </div>

                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="middle_right_II">
                            <div>
                                <dl class="recomand_notice">
                                    <dt>
                                        <em>推荐新闻</em>
                                    </dt>
                                    <%--<dd>
                                        <a href="news_hexielu.jsp">爱丽丝的飞机拉开就是地方了卡健身拉了分开就阿里上看见对方了卡机斯蒂芬李会计爱丽丝的疯狂就拉斯加对方离开房</a>
                                        <i>2015年06月30日</i>
                                    </dd>--%>

                                </dl>
                                <div class="middle_view"></div>
                                <dl class="recent_notice">
                                    <dt>
                                        <em>最新新闻</em>
                                    </dt>
                                    <%--<dd>
                                        <a href="news_hexielu.jsp">爱丽丝的飞机拉开就是地方了卡健身拉了分开就阿里上看见对方了卡机斯蒂芬李会计爱丽丝的疯狂就拉斯加对方离开房</a>
                                        <i>2015年06月30日</i>
                                    </dd>--%>

                                </dl>
                                <!--分页-->
                                <div class="notice-page-paginator">
                                    <span class="first-page">首页</span>
                                    <span class="page-index">
                                        <span class="active">1</span>
                                        <span>2</span>
                                        <span>3</span>
                                        <span>4</span>
                                        <span>5</span>
                                        <i>···</i>
                                    </span>
                                    <span class="last-page">尾页</span>

                                </div>

                            </div>
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