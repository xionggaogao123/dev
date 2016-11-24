<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<%----------------------------------我的班级  全班同学 统计页面 老师可见-------------------------%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/flippedStatistics.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/teacherSheet.css"/>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/js/card.js"></script>
    <script type="text/javascript" src="/static/js/common/role.js"></script>
    <script type="text/javascript">


    </script>
    <title>班级统计</title>
    <style>
        #fc_ques_statistics {
            background: white;
        }

        .header_foot {
            height: 20px;
            background: #ECF5FF;
            border-left: 1px solid #D0D0D0;
            border-right: 1px solid #D0D0D0;
        }

        .ypheader > div:first-child {
            padding: 10px 5px;
            border: 1px solid #D0D0D0;
        }

        .fc_statistics_list span.I {
            width: 180px;
            text-align: left;
            padding-left: 120px;
        }

        .fc_statistics_list span.II {
            width: 300px;
        }

        .fc_statistics_list span.III {
            width: 250px;
        }

        #fc_class_container {
            margin: 0 auto;
            width: 900px;
            min-height: 66%;
            position: relative;
            left: 10px;
        }

        #fc_class_container h4 {
            font-size: 17px;
            margin-top: 10px;
            border-left: 4px solid #ff7e00;
            padding-left: 10px;
        }

        #fc_class_list {
            transition-duration: 1.5s;
            transition-property: height;
        }

        .fc_class_detail > div {
            display: inline-block;
            float: left;
            margin-bottom: 40px;
            border-bottom: none;
        }

        .fc_class_detail > div > * {
            float: left;
        }

        .fc_class_detail > div > div {
            text-align: center;
            background: white;
            border: 1px solid #D0D0D0;
        }

        .fc_class_detail > div > div > div {
            margin: 5px 0;
            padding: 0 10px;
            float: left;
            min-width: 80px;
        }

        .fc_class_detail > div > div > div > span:first-child {
            margin-top: 20px;
            display: block;
        }

        .fc_class_detail > div > div > div > span:first-child + span {
            margin-bottom: 20px;
        }

        #fc_teacher_list {
            background: white;
            border: 1px solid #D0D0D0;
            border-bottom: none;
        }

        .user-info {
            margin: 20px 13px;
            display: inline-block;
            float: left;
        }

        .user-info img {
            width: 130px;
            height: 130px;
            border-radius: 4px;
            border: 1px solid #d0d0d0;
        }

        .user-name {
            margin: 10px 0px 10px 5px;
        }

        .stu-list-img {
            width: 60px;
            height: 60px;
            vertical-align: middle;
            margin-right: 10px;
        }

        .shotclass {
            border-right: 1px solid #DDDDDD;
            width: 98px;
            height: 38px;
            line-height: 38px;
            color: #363636;
            background: #F1F1F1;
            background-repeat: no-repeat;
            cursor: pointer;
        }
        .active {
            width: 98px;
            background: white;
            border-top: 4px solid #FF8900;
            margin-top: -4px;
            border-right: 1px solid #DDDDDD;
            color: #FF8900;
        }
    </style>
</head>
<body classid="${classId}">
<%@ include file="../common_new/head.jsp" %>

<c:if test="${role:isEducation(sessionValue.userRole)}" >
    <div id="nav_to_my" style="background:white;padding:10px 0;">
        <div style="margin:0 auto;width:900px;"><a href="/user">&nbsp;首页&nbsp;</a>><a href="/browser">&nbsp;班级&nbsp;</a>>&nbsp;<a id="classname" href="#"></a>&nbsp;</div>
    </div>
</c:if>
<div id="fc_teacher_list" class="ypheader center">
    <div style="border:none;border-bottom:1px solid #d0d0d0;">
        <h4>老师列表</h4>
    </div>
    <ul>

    </ul>
</div>

<div id="fc_stu_list" class="fc_statistics_list ypheader center">
    <div>
        <h4>学生列表</h4>
    </div>

    <div class="header">
        <span class="I">学生姓名</span>|
        <span class="II">看完视频数</span>|
        <span class="III">做题数</span>
    </div>
    <div class="header_foot"></div>
    <ul class="shot_class_1">

    </ul>
    <ul class="shot_class_2">

    </ul>
</div>
<%@ include file="../common_new/foot.jsp" %>
<input hidden="hidden" id="className" name="className"/>
<div class="alert-bg"></div>
<div class="letter-container">
    <div class="letter-title">发送私信给</div>
    <div class="letter-close">&times;</div>
    <textarea class="letter-input" rows="3" placeholder="请输入您要对ta说的话..."></textarea>
    <a href="javascript:;" class="letter-release">发送</a>
</div>
<div class="send-result-bg"></div>
<div class="send-result">√ 发送成功</div>
<div class="info-card"></div>
</body>
</html>