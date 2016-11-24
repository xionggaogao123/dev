<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>阜阳市颍东区和谐路小学</title>
    <link rel="stylesheet" type="text/css" href="/customizedpage/hexielu/style/style.css"/>
    <link rel="stylesheet" type="text/css" href="/customizedpage/hexielu/style/new.css"/>
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">

    <script type="text/javascript" src="/customizedpage/hexielu/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="/customizedpage/hexielu/js/flippedClassroom.js"></script>

    <script type="text/javascript" src="/customizedpage/hexielu/js/iepng.js"></script>
    <script type="text/javascript" src="/customizedpage/hexielu/js/indexjq.js"></script>


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
                            window.location = "/myschool/paike.do";
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
                        $("#username2").val("");
                        $("#password2").val("");
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
<!-- top -->
<div class="topbox">
    <div class="top">
        <div class="logo">
            <h1>
                <a href="javascript:void(0);">公司的名字或广告语写在这里</a>
            </h1>
        </div>
        <div class="toplink" style="">
            <!--================================登录==================================-->
            <%--<a target="_blank" class="login-R" href="http://edu.wanfangdata.com.cn/"><img src="/customizedpage/hexielu/images/heixielu_picture.png"></a>--%>
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
<!-- banner -->
<div class="index-bannerbox">
    <div id="qie">
        <ul id="index-banner">
            <li><img src="/customizedpage/hexielu/images/banner-1.png"/></li>
            <li><img src="/customizedpage/hexielu/images/banner-2.png"/></li>
            <li><img src="/customizedpage/hexielu/images/banner-1.png"/></li>
            <li><img src="/customizedpage/hexielu/images/banner-2.png"/></li>
            <li><img src="/customizedpage/hexielu/images/banner-1.png"/></li>
        </ul>
        <a href="javascript:" class="left"></a>
        <a href="javascript:" class="right"></a>
    </div>
</div>
<div class="bg-line"></div>
<!-- news-->
<input type="hidden" id="hexieluSchoolId" value="559cf00b63e73dbde154a70b">

<div class="index-newsbox">
    <div class="newsleft">
        <div class="newsleft-top">
            <div class="newsleft-top-dl">
                <%--<dl >
                    <dt><img src="/customizedpage/hexielu/images/pit01.jpg" /></dt>
                    <dd>
                            <h5><a href="#">和谐路小学卓雅大讲堂第一期：青春期来袭，你准备好了吗？</a></h5>
                            <p>
                                为进一步加强学校和毕业班家长之间的交流、沟通，增进家校之间的联系，更好的形成家校教育的合力，6月9日下午3点，和谐路小学召开了六年级毕业班学生家长会，报告厅内座无虚席，会议由副校长张飞主持。 会议伊始，张飞首先代表学校感谢各位家长在百忙之中抽出宝贵的时间来校参加毕业班的家长会，并向各位家长们简要介绍了学校近期所取得的成绩。他希望家长能够积极参与到孩子的教育管理中来，使孩子能够顺利地向中学的学习过渡。.... ...
                            </p>
                            <a class="btn-more" href="javascript:void(0);"></a>

                    </dd>
                </dl>
                <dl >
                    <dt><img src="/business/ypxx/special/jinan/images/pic02.jpg" /></dt>
                    <dd>
                            <h5><a href="#">省教育厅召开党的群众路线教育实践活动整改落实工作总结会议</a></h5>
                            <p>
                                会上，厅长、党组书记刘传铁同志对我厅整改落实工作进行了总结，他指出，省教育厅从一开始就在“真”和“实”上下功夫，把整改落实贯彻活动始终，坚持问题导向开局就改，凝神聚力抓好落实，上下联动协同整改，态度是严肃认真的，也是实实在在的，成效也是明显的。他强调，不论是抓教育实践活动、抓整改落实，还是抓教育改革、抓事业发展，都必须坚持领导带头、以身作则.... ...
                            </p>
                            <a class="btn-more" href="news_hexielu.jsp"></a>
                    </dd>
                </dl>--%>

            </div>
            <div class="btnnum">
                <span class="btn1"></span>
                <span class="btn2"></span>
            </div>
        </div>


    </div>
    <div class="newsright">
        <div class="heixelu">
            <dl>
                <dt>
                    <span>【链接】</span>
                </dt>
                <dd>
                    <a href="http://www.k6kt.com/fuyang21" target="_blank">
                        <span>阜阳市第二十一中学</span>
                        <img src="/customizedpage/fuyang21/images/hexielu_logo_3_07.png">
                    </a>
                </dd>
                <dd>
                    <a href="http://edu.wanfangdata.com.cn/" target="_blank">
                        <span>数字图书馆</span>
                        <img src="/customizedpage/fuyang21/images/hexielu_logo_3_07.png">
                    </a>
                </dd>
                <dd>
                    <a onclick="$('#login-bg2').fadeIn('fast')" href="/customizedpage/hexielu/hexielu-new.jsp" target="-_blank">
                        <span>排课系统</span>
                        <img src="/customizedpage/fuyang21/images/hexielu_logo_3_07.png">
                    </a>
                </dd>
            </dl>
        </div>
        <div class="view">
            <dl class="hexielunews">
                <dt><span>【公告】</span><a class="bg-more" href="/customizedpage/hexielu/hexielu.jsp?type=notice"></a></dt>
                <%-- <dd>
                     <a href="http://www.fyhxlxx.com/"><span>和谐路小学卓雅大讲堂第一期：青春期来袭，你准备好了吗？</span><em>2015/06/10</em></a>
                 </dd>--%>

            </dl>
        </div>
    </div>
</div>
<%--<div id="login-bg2">
    <div id="login2">
        <div id="logincontent2">
            <div class="line" style="line-height:30px; ">
                <div><img src="/img/loginicon.png" /></div>
                <div style="color:rgb(51, 173, 225); font-weight:bold; font-size:14px; margin-left: 10px">登录</div>
                <i class='fa fa-times fa-lg' style="cursor:pointer; float: right; color:rgb(51, 173, 225); line-height: 30px" onclick="closepop()"></i>
            </div>
            <div class="line">
                <input type="text" placeholder="用户名" class="username2" name="username2" id="username2"/>
            </div>
            <div class="line">
                <input type="password" placeholder="密码" class="password2" name="password2" id="password2" oncopy="return false"  onpaste="return false"/>
            </div>
            <div class="line" style="line-height:30px;">
                <img id="buttonloadingindex" src="/img/loading4.gif" style="position: absolute;left:185px;display:none; vertical-align:middle;"/><input style="vertical-align:middle;" type="submit" value=" 登 录 " onclick="loginIndex(2);">
            </div>
        </div>
    </div>
</div>--%>
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