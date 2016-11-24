<%--
  Created by IntelliJ IDEA.
  User: fulaan
  Date: 15-7-24
  Time: 下午4:20
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
    <link rel="stylesheet" href="/customizedpage/fuyang21/style/scheduling.css">

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
    <%--<script type="text/javascript">
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
    </script>--%>
    <script>
        function downloadclass() {
            window.location = "/myschool/paike.do";
        }

    </script>
</head>
<body>
<div class="scheduling-main">
<!--================================start头部=========================================-->
   <%-- <%@ include file="/WEB-INF/pag../common_new/head.jsp" %>--%>
 <!--================================end头部=========================================-->
    <!--================================start导航=========================================-->
    <!--================================end导航=========================================-->
    <!--====================================start-scheduling-right===============================================-->
    <div class="scheduling-right">
        <!--================================start广告=========================================-->
        <!--================================end广告=========================================-->
        <dl>
            <dd class="scheduling-right-T">
                <div>
                    <p class="scheduling-right-I">
                        <i style="font-size: 34px;font-weight: normal">最好的排课软件！</i><br>
                        <em>完美地导入Excel数据,真正生成Excel课程表，先进的排课算法，功能强大的手动排课，完美解决令人头疼的学校排课问题。</em>
                    </p>
                    <img src="/customizedpage/fuyang21/images/scheduling-xx.png" onclick="downloadclass()">
                </div>
            </dd>
            <dd>
                <p>
                    <i>【简介一】拖动就是调课：</i>
                    首创的拖动式调课，方便快捷。
                </p>
            </dd>
            <dd>
                <div>
                     <img src="/customizedpage/fuyang21/images/scheduling-TY.png">
                </div>
            </dd>
            <dd>
                <p>
                    <i>【简介二】活如游鱼般自定义条件和预设：</i>
                    有强大无比的排课算法和预设功能，可以设置多大十几种的预设限制，结合强大的排课算法，适合各类学校的排课需求，满足任何苛刻的排课要求。经过多年的研究，最新版本的排课质量更是遥遥领先于同类软件。
                </p>
                <div>
                    <img src="/customizedpage/fuyang21/images/scheduling-middle.png">
                </div>
            </dd>
            <dd>
                <p>
                    <i>【简介三】Excel导入导出自由自在：</i>
                    完美支持EXCEL的导入，导出，完美地生成EXCEL课程表，方便二次编辑。
                </p>
                <div>
                    <img  src="/customizedpage/fuyang21/images/scheduling-DR.png">
                    <img  src="/customizedpage/fuyang21/images/scheduling-DC.png">
                </div>
            </dd>
            <dd>
                <p>
                    <i>【简介四】兼容日常教务工作：</i>
                    完全按照教务工作者的思路设计，完全与日常教务工作兼容。如：设置科目时，根本就是在填一张课程设置表。
                </p>
                <div>
                    <img class="scheduling-CP" src="/customizedpage/fuyang21/images/scheduling-7_03.png">
                </div>
            </dd>
            <dd>
                <p>
                    <i>【简介五】操作易如反掌：</i>
                    每个界面都尽量切合学者的思路，都有独立详细的帮助说明，且包含全程视频演示，令操作易如反掌。
                </p>
            </dd>
        </dl>
    </div>
    <!--====================================end-scheduling-right===============================================-->
</div>
<div id="footer" style="font-size: 12px;  clear: both;  overflow: hidden;background-color: rgb(0, 0, 0);opacity: 0.7;filter: alpha(opacity=70);height: 32px;line-height: 32px;color: white">
    <div style="width: 1000px;margin: 0 auto">
        <span style="float: left">版权所有：上海复兰信息科技有限公司          <a href="http://www.fulaan-tech.com" target="_blank">www.fulaan-tech.com</a>        沪ICP备14004857号</span>
        <span style="float: right"><a href="/aboutus/k6kt">关于我们</a>  |  <a href="/contactus/k6kt">联系我们</a>   |  <a href="/service/k6kt">服务条款 </a> |  <a href="/privacy/k6kt">隐私保护 </a> |  <a style="position: relative;top:5px;" href='http://wpa.qq.com/msgrd?v=1&uin=2803728882&site=qq&menu=yes' target="_blank"><img src="/img/QQService.png"></a></span>
    </div>
</div>
</body>
</html>
