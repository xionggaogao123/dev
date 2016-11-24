<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ page import="java.util.UUID" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>用户基本信息</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="/static/css/nivo-slider.css" type="text/css"/>
    <link rel="stylesheet" href="/static/css/main-nivo-thumb.css" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/message.css"/>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/js/experienceScore.js"></script>
    <script src="/static/js/jquery.nivo.slider.js"></script>
    <script type="text/javascript" src="/static/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="/static/sharedpart.js"></script>
    <!--<script type="text/javascript" src="/js/bxslider/jquery.bxslider.js"></script>

    <link rel="stylesheet" type="text/css" href="/js/bxslider/jquery.bxslider.css" />-->
    <script type="text/javascript">
        var currentPageID = 4;
        /* $(window).load(function () {
         $('#slider').nivoSlider({
         effect: 'fade',
         directionNav: false,             // Next & Prev navigation
         controlNav: true,               // 1,2,3... navigation
         controlNavThumbs: true        // Use thumbnails for Control Nav
         });
         }); */
        $(function () {
            $(".currentSelected").removeClass("currentSelected");
            $(".icon_config").addClass("currentSelected");
            var s = $(".dreamSchoolId").val();
            var t = $(".gradeId").val();
            var i = $(".sex").val();
            $("#sel1  option[value=" + s + "] ").attr("selected", true);
            $("#sel2  option[value=" + t + "] ").attr("selected", true);
            $("input[name='radio'][value=" + i + "]").attr("checked", true);
            /*$("#mobile_number").number();*/
            /*$("#qq").number();*/

            var currentUrl = window.location.href;
            if (/editphoto/.test(currentUrl)) {
                $('a[href="/editphoto"]').addClass('selected');
                $('a[href="/editphoto"]').attr("style", "color:#fff");
            } else if (/password/.test(currentUrl)) {
                $('a[href="/password"]').addClass('selected');
                $('a[href="/password"]').attr("style", "color:#fff");
            }
        });
        function openimgupload() {
            window.open('/personal/avatarpage.do?uid=<%=UUID.randomUUID().toString()%>&uploadtype=head', '图片上传', 'height=253,width=450,status=no,toolbar=no,menubar=no,location=no,scrollbars=no,resizable=no');

        }
    </script>
</head>
<body>
<!-- user info start -->
<%@ include file="../common/header.jsp" %>
<jsp:include page="/infoBanner.do">
    <jsp:param name="bannerType" value="userCenter"/>
    <jsp:param name="menuIndex" value="1"/>
</jsp:include>
<!-- user info end -->
<div id="content_main_container">
    <div id="content_main">
        <!-- 左侧导航-->
        <%@ include file="../common/left.jsp" %>
        <!-- left end -->
        <!-- right start-->
        <div id="right-container">
            <div id="content_main_container" style="margin-top: 5px;">
                <div id="main-content" style="position: relative; overflow:hidden;" class="main-content-msg">
                    <div id="account-right">
                        <div id="account-right-title">
                            <div id="account-r-sel">
                                <a href="/basic" <%--class="selected"--%> target="_self">基本信息</a>
                                <a href="/editphoto" <%--class="selected"--%> target="_self">修改头像</a>
                                <a href="/password" target="_self">修改密码</a>
                                <%--<a href="" target="-_self">基本信息</a>--%>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <div id="account-right-content" class="account-right-content">
                            <img style="height:200px;width:200px;" src='${avatar}'><br/><br/>
                            <input type="button" value="点击上传图片" class="orange-submit-btn"
                                   onclick="openimgupload();"><br/><br/>
                            <b>* 图片大小必须在10M以内,且格式为jpg,gif,png,bmp,tif</b>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- right end-->
    </div>
</div>

<div class="clear"></div>

<%@ include file="../common/flippedroot.jsp" %>
</body>
</html>
