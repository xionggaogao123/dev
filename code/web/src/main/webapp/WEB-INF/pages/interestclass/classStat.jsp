<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<%----------------------------------我的班级  全班同学 统计页面 老师可见  仅兴趣班可见-------------------------%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" href="/static_new/css/reset.css">
    <link rel="stylesheet" type="text/css" href="/static/css/flippedStatistics.css?v=1"/>
    <link rel="stylesheet" type="text/css" href="/static/css/teacherSheet.css"/>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/js/card.js"></script>
    <script type="text/javascript" src="/static/js/common/role.js"></script>
    <script type="text/javascript">
        var classid = 0;
        var termType = -1;
        var nb_role = ${sessionValue.userRole};
        var currentPageID = 2;
        function getClassStatistics() {
            $.ajax({
                url: "/myclass/intereststat.do",
                type: "get",
                dataType: "json",
                async: false,
                data: {
                    classId: classid,
                    termType:termType
                },
                success: function (data) {
                    if (data.totalList != null && data.totalList.length > 0) {
                        showStuList(data);
                    }
                }
            });
        }

        function showStuList(data) {
            var html = '';
            var html2 = '';
            if (data.totalList) {
                for (var i = 0; i < data.totalList.length; i++) {
                    var stu = data.totalList[i];
                    var attach = '';
                    if (isHeadMaster(nb_role)|| isK6ktHelper(nb_role)|| isEducation(nb_role)) {
                        attach = classid + '/';
                    }
//                    html2 += '<li class="content"><a href="/myclass/lead2stustat/' + attach + stu.studentId + '"><span class="I"><img class="stu-list-img"  src="' + stu.imageURL + '" target-id="' + stu.studentId + '" role="' + stu.role + '"/><span>' + stu.userName + '</span></span><span class="II">' + stu.endViewNum + '</span>';
                    html2 += '<li class="content">' +
//                    '<a  href="/myclass/stuLessons.do?classId='+classid+'&stuId='+stu.studentId+'&termType='+termType+'">' +
                   '<a  href="/myclass/studentScore.do?classId='+classid+'&stuId='+stu.studentId+'&termType='+termType+'">' +
                    '<span class="I"><img class="stu-list-img"  src="' + stu.imageURL + '" target-id="' + stu.studentId + '" role="' + stu.role + '"/><span>' + stu.userName + '</span></span>' ;
                    html2 +='&nbsp;&nbsp;<span class="II">' +stu.gradeName + '</span>';
                    html2 +='&nbsp;&nbsp;<span class="III">' +stu.className + '</span>';
                    html2 +='&nbsp;&nbsp;<span class="IV">' + stu.sexStr + '</span>';
                    html2 +='&nbsp;&nbsp;<span class="V">' + stu.endViewNum + '</span>';
                    html2 +='&nbsp;&nbsp;<span class="VI">' + stu.endQuestionNum + '</span>';
                    html2 +='&nbsp;&nbsp;<span class="VII">' +stu.attendance + '</span>';
                    html2 +='&nbsp;&nbsp;<span class="VIII">' + '<button>个人详情</button>'+'</span></a></li>';

                }
            }

            var tdiv = $('.shot_class_1');
            var tdiv2 = $('.shot_class_2');
            tdiv2.html(html2);
            tdiv.html(html);
        }

        function getTeacherList(cid) {
            $.ajax({
                url: "/myclass/teacherinofs.do",
                type: "get",
                dataType: "json",
                async: false,
                data: {
                    classId: cid
                },
                success: function (data) {
                    if ( isTeacher(data.role)) {
                        if (data.teacherList != null && data.teacherList.length != 0) {
                            showTeacherList(data);
                        }
                    } else {
                        $('#fc_teacher_list').hide();
                    }
                }
            });
        }

        function showTeacherList(data) {
            var html = '';
            if (data.teacherList) {
                for (var i = 0; i < data.teacherList.length; i++) {
                    var te = data.teacherList[i];
                    var attach = '';
                    if (isHeadMaster(nb_role)|| isK6ktHelper(nb_role)|| isEducation(nb_role)) {
                        attach = classid + '/';
                    }
                    
                    if(te.subjectNameList)
                    {
	                    for(var k=0;k<te.subjectNameList.length;k++){
	                        var teacher=te.subjectNameList[k];
	                        html += '<li class="user-info"><a href="/teacher/course/' + attach + te.id + '">';
	                        html += '<img class="teacher-img" src="' + te.imageUrl + '" target-id="' + te.id + '" role="' + te.role + '"></a><div>';
	                        html += '<span class="user-name">' + te.userName + '</span><span class="user-name">' + teacher + '老师</span></div></li>';
	                    }
                    }

                }
            }

            var tdiv = $('#fc_teacher_list ul');
            tdiv.html(html);
        }

        $(function () {
            $('.shotclass').each(function (i) {
                if (i == 0) {
                    $(this).addClass('active');
                    $('.shot_class_1').show();
                    $('.shot_class_2').hide();
                    $('.shot_class_1').attr('tid', 1);
                    $('.loading-img').fadeIn('fast');
                }
                $(this).click(function () {
                    $('.shotclass').removeClass('active');
                    $(this).addClass('active');
                    $('.loading-img').fadeIn('fast');
                    if (i == 0) {
                        $('.shot_class_1').show();
                        $('.shot_class_2').hide();
                        $('.shot_class_1').attr('tid', 1);
                    } else {
                        $('.shot_class_2').show();
                        $('.shot_class_1').hide();
                        $('.shot_class_2').attr('tid', 2);
                    }


                });
            });
            $('#classname').text($('#className').val());
            classid = $.trim($("body").attr("classid"));
            termType = $('body').attr('termType');
            $(".loading").show();
            getClassStatistics();
//            getTeacherList(classid);
            $(".loading").fadeOut(1000);
        });

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
            width: 142px;
            text-align: center;
        }

        .fc_statistics_list span.II {
            width: 103px;
        }

        .fc_statistics_list span.III {
            width: 113px;
        }

        .fc_statistics_list span.IV {
            width: 55px;
        }

        .fc_statistics_list span.V {
            width: 126px;
        }

        .fc_statistics_list span.VI {
            width: 103px;
        }
        .fc_statistics_list span.VII {
            width: 100px;
        }
        .fc_statistics_list span.VIII button{
            font-family: "Microsoft Yahei";
            background:#5bc1de;
            color: white;
            cursor: pointer;
            float: left;
            height: 30px;
            line-height: 30px;
            position: relative;
            top:15px;
            width: 80px;
            margin: 0 5px 0 5px;
            border-radius: 4px;
        }
        .fc_statistics_list span.VIII {
            width: 90px;
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
<body classid="${param.classId}" termType="${param.termType}">
<%@ include file="../common_new/head.jsp" %>

<c:if test="${role:isEducation(sessionValue.userRole)}" >
    <div id="nav_to_my" style="background:white;padding:10px 0;">
        <div style="margin:0 auto;width:900px;"><a href="/user">&nbsp;首页&nbsp;</a>><a href="/browser">&nbsp;班级&nbsp;</a>>&nbsp;<a id="classname" href="#"></a>&nbsp;</div>
    </div>
</c:if>
<%--<div id="fc_teacher_list" class="ypheader center">--%>
    <%--<div style="border:none;border-bottom:1px solid #d0d0d0;">--%>
        <%--<h4>老师列表</h4>--%>
    <%--</div>--%>
    <%--<ul>--%>

    <%--</ul>--%>
<%--</div>--%>

<div id="fc_stu_list" class="fc_statistics_list ypheader center">

    <ul>
        <li class="current curr fc_statistics_list-li  fc-ul">学生列表</li>
        <li class="fc_statistics_list-li"><a href="/myclass/lead2ls/1/${param.classId}/${param.termType}">课时详情</a></li>
    </ul>
    <%--<div>
        <h4>学生列表</h4>
    </div>--%>
    <div class="header">
        <span class="I">学生姓名</span>|
        <span class="II">年级</span>|
        <span class="III">班级</span>|
        <span class="IV">性别</span>|
        <span class="V">看完视频数</span>|
        <span class="VI">做题数</span>|
        <span class="VII">考勤</span>|
        <span class="VIII">操作</span>
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