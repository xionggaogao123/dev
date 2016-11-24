<%@ page import="java.net.URLDecoder" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
    <meta name="renderer" content="webkit">
    <title>复兰科技 K6KT-快乐课堂</title>
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/customizedpage/huaibei/css/main.css"/>
    <link rel="stylesheet" href="/static/css/nivo-slider.css" type="text/css"/>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type='text/javascript' src='/static/plugins/bootstrap/js/bootstrap.min.js'></script>
    <script type='text/javascript' src='/static/js/k6kt-index.js'></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script src='http://static.polyv.net/file/polyvplayer_v2.0.min.js'></script>
    <script src="/static/js/jquery.nivo.slider.js"></script>
    <%
        String userName = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("momcallme".equals(cookie.getName())) {
                    userName = URLDecoder.decode(cookie.getValue(), "UTF-8");
                }
            }
        }
    %>
    <script type="text/javascript">
        $(function () {
            var strCookie=document.cookie;
            var arrCookie=strCookie.split("; ");
            var closeflag = false;
            for(var i=0;i<arrCookie.length;i++){
                var arr=arrCookie[i].split("=");
                if("closesheep"==arr[0]){
                    closeflag = true;
                }
            }
            if(!closeflag) {
                document.getElementById('light').style.display='block';
            }
        });

        function closesheepdiv() {
            document.cookie = "closesheep=true";
            $('#light').hide();
        }
    </script>
</head>
<body>
<%@ include file="/WEB-INF/pages/common/ypxxhead.jsp" %>

<div id="intro-player">
    <div id="player_div">

    </div>
    <span onclick="closeMovie()" class="close-dialog"><i class="fa fa-times-circle fa-lg"></i></span>
</div>

<div class="main-container">
    <div class="login-bar" style="margin: 0 auto;">
        <div class='title-bar-container' style="height:105px;overflow: hidden">
            <img class="title-logo" src="/img/K6KT/main-page/logo.png">
            <!-- <a id="teacher-test" href="/application-use">教师申请试用</a> -->
            <a class="login-btn" href="javascript:;">登 录</a>
            <input id="input-first" class="input-password" type="password" placeholder="密码" tabindex="2">
            <input id="input-last" class="input-account" type="text" placeholder="用户名" tabindex="1"
                   value="<%=userName%>">

            <div id="tips-msg">
                <span class="password-error">密码错误</span>
                <!--a class="forget-pass" href='#'>忘记密码？</a-->
                <span class="username-error">用户名不存在</span>
            </div>
        </div>
    </div>
    <div class="main-content-container">
        <div class='content-container'>
            <img class="text-1" src="/img/K6KT/main-page/text-1.png"/>
            <img class="text-2" src="/img/K6KT/main-page/text-2.png"/>

            <a onclick="playMovie()">
                <div class="player-hand">
                    <div></div>
                </div>
            </a>
            <div class="teacher-apply" onclick="go2appuse()">
                教师申请试用
            </div>
            <div class="app-link">
                <img src="/img/K6KT/iphone.png"/>

                <div>
                    <div>手机客户端 APP</div>
                    <div>
                        <span><img src="/img/K6KT/ios.png"/></span>
                        <span>|</span>
                        <span><img src="/img/K6KT/android.png"/></span>
                        <span><a href="/mobile">点击下载 ></a></span>
                    </div>
                </div>
            </div>

            <div class="monitor-div">
                <div class="carousel nivoSlider" id='slider'>
                    <img src="/img/K6KT/main-page/screen-1.png"/>
                    <!--img src="/img/K6KT/main-page/screen-2.png"/ -->
                </div>
            </div>
        </div>
    </div>
    <div style="height: 30px;text-align: center;line-height: 30px;;width: 100%;background-color:#EAE9DC;position: relative;top:-20px;border-bottom: 1px solid #C0C0C0;font-family: 'Microsoft YaHei';font-weight: bold;color: #5a5a5a">
        <%--<p>欢迎北京大学入驻复兰大学K6KT-翻转课堂</p>--%>
    </div>
    <div style="clear: both"></div>
    <div class="new-schoolelogo">
        <div class="HB_left">
            <span class="HB_left_I">新闻动态</span>
            <a href="" class="HB_left_II">更多...</a>
            <hr>
            <ul>
                <li>
                    <span>2015-3-20</span>
                    <div>
                        <a href="http://www.hbjy.net/dt2111111128.asp?DocID=2111199035">濉溪县教育局召开2015年教科研工作会议</a>
                    </div>
                </li>
                <li>
                    <span>2015-3-21</span>
                    <div>
                        <a href="http://www.hbjy.net/dt2111111128.asp?DocID=2111199043">市实验高中十八岁成人仪式让青春更精彩</a>
                    </div>
                </li>
                <li>
                    <span>2015-3-23</span>
                    <div>
                        <a href="http://www.hbjy.net/dt2111111128.asp?DocID=2111199115">相山区教育局五措并举做好学校安全工作</a>
                    </div>
                </li>
                <li>
                    <span>2015-3-24</span>
                    <div>
                        <a href=" http://www.hbjy.net/dt2111111128.asp?DocID=2111199149">市三实小诵中华经典讲传统故事</a>
                    </div>
                </li>
                <li>
                    <span>2015-3-24</span>
                    <div>
                        <a href="http://www.hbjy.net/dt2111111128.asp?DocID=2111199150">淮北二中举办首届校园足球联赛</a>
                    </div>
                </li>

            </ul>
        </div>
        <div class="HB_right">
            <span class="HB_right_I">翻转课堂联盟校展示</span>
            <hr>
            <div class="HB_middle">

                    <img src="/customizedpage/huaibei/img/HB_img%20(2).jpg">


                    <img src="/customizedpage/huaibei/img/HB_img%20(3).jpg">


                    <img src="/customizedpage/huaibei/img/HB_img%20(4).jpg">


                    <img src="/customizedpage/huaibei/img/HB_img%20(5).jpg">


                    <img src="/customizedpage/huaibei/img/HB_img%20(6).jpg">

            </div>
            <span class="HB_right_I">微课评选</span>
            <hr>

                 <img src="/customizedpage/huaibei/img/HB_img%20(1).png">

        </div>
    </div>
</div>
<!-- 页尾 -->
<%@ include file="/WEB-INF/pag../common_new/foot.jsp" %>

<div id="light" class="white_content" style="display: none" >

    <a onclick="closesheepdiv()" class="close_but"></a>
</div>
<!-- 页尾 -->
</body>
<script>
    function go2appuse(){
        window.location.href="/customizedpage/application.jsp";
    }
</script>
<script>
    $(function(){
        var h=$(window).height()
        var w=$(window).width();
        if(h<700){
            $("#II").css({height:276,width:401,marginLeft:-200,marginTop:-138})
        }
    })

</script>
</html>