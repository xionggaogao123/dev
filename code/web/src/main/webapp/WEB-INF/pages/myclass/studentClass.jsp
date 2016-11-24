<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%--------------------------------------学生角色 我的班级---------------------------------------------------------------%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>我的班级</title>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap-combined.school.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/teacherSheet.css"/>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"></script>
    <script type="text/javascript" src="/static/js/card.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <style type="text/css">

        #fc_student_container {
            width: 770px;
            min-height: 620px;
            padding-left: 15px;
        }

        #fc_student_container h4 {
            font-size: 17px;
            margin-top: 10px;
            border-left: 4px solid #ff7e00;
            padding-left: 10px;
        }

        #fc_student_container h4 span {
            float: right;
            height: 20px;
            text-align: center;
            margin-right: 10px;
            padding-top: 2px;
            cursor: pointer;
        }

        #fc_student_container h4 span a:first-child {
            margin-right: 5px;
        }

        #fc_student_container h4 span a {
            display: inline-block;
            float: left;
            color: white;
            background-color: #FD8002;
            width: 20px;
        }

        .fc_student_div {
            width: 175px;
            float: left;
            margin-bottom: 30px;
            position: relative;
            margin-left: 5px;
            height: 90px;
        }

        .fc_student_div a {
            display: inline-block;
            float: left;
            cursor: default;
            border: 0px none;
        }

        .fc_student_div a img {
            /* width: 80px; */
            height: 80px;
        }

        .fc_student_div > div {
            position: absolute;
            bottom: 10px;
            right: 10px;
            overflow: hidden;
            color: #929699;
            font-size: 14px;
            font-weight: bold;
            width: 70px;
        }

        #experience {
            width: 130px;
            height: 140px;
            right: 110px;
            position: absolute;
            top: 0px;
            background-repeat: no-repeat;
            background-position: center;
            background-size: contain;
        }

        .control-bar {
            margin: 20px 0 0 10px;
        }

        .ypstudent {
            height: 30px;
            display: inline-block;
            margin-left: 15px;
            text-align: center;
            cursor: pointer;
            font-weight: bold;
            font-size: 16px;
        }

        .ypstudent.active {
            border-bottom: 2px solid #FF8900;
            color: #FFA800;
            font-weight: bold;
        }
    </style>

    <script type="text/javascript">
        //解决console 未定义bug
        if (!window.console || !console.firebug) {
            var names = ["log", "debug", "info", "warn", "error", "assert", "dir", "dirxml",
                "group", "groupEnd", "time", "timeEnd", "count", "trace", "profile", "profileEnd"];

            window.console = {};
            for (var i = 0; i < names.length; ++i)
                window.console[names[i]] = function () {
                }
        }


        var currentPageID = 2;
        var currentPage = 0;
        var stulist = [];
        var stuPage = 0;
        var stuListPageSize = 15;

        function getstudentInfo() {
            var ret = [];

            return ret;
        }
        function getstudentlist(cid,classType) {
            var ret = [];
            $.ajax({
                url: "/myclass/stulist.do",
                type: "get",
                dataType: "json",
                async: false,
                data: {
                    classId: cid,
                    classType:classType
                },
                success: function (data) {
                    showAllStudent(data);
                },
                complete: function () {
                }
            });
            return ret;
        }

        //显示用户同班同学
        function showAllStudent(data) {
            var html = '';
            console.log(data);
            //该信息要有data.rows这个属性
            if (data.rows) {
                for (var i = 0; i < data.rows.length; i++) {
                    //每个student的信息通过rows数组得到.
                    var stu = data.rows[i];
                    if (i == 0 || i % 5 == 0) {
                        html += '<div class="fc_student_div">';
                    }
                    else {
                        html += '<div class="fc_student_div" style="margin-left: 0px;">';
                    }

                    html += '<a href="##"><img style="height:80px;width:80px" class="stu-list-img" src="' + stu.imgUrl + '" target-id="' + stu.id + '" role="' + stu.role + '"/></a>';
                    html += '<div>';
                    html += '<p>' + stu.userName + '</p>';
                    html += '<p><span>经验值</span><span style="margin-left: 10px;">' + stu.experienceValue + '</span></p>';
                    html += '</div>';
                    html += '</div>';
                }
                var tdiv = $('#fc_student_list');
                tdiv.html(html);
            }
        }


        $(function () {
            $('.loading').show();
            getstudentInfo();
            $('.control-bar').find('span.class-item:nth(0)').addClass('active');
            var currentClassId = $('.control-bar .class-item.active').attr('cid');
            var classtype = $('.control-bar .class-item.active').attr('classtype');
            //班级切换
            $(".control-bar .class-item").each(function (i) {
                $(this).bind('click', function () {
                    $(this).siblings().removeClass('active');
                    $(this).addClass('active');
                    var cid = $(this).attr('cid');
                    var classtype = $(this).attr('classtype');
                    currentClassId = cid;
                    getstudentlist(currentClassId,classtype);
                });
            });
            getstudentlist(currentClassId,classtype);
            $('.loading').fadeOut(1000);
        });
    </script>
</head>
<body stuid="${currentUser.id}">
<%@ include file="../common_new/head.jsp" %>
<div id="content_main_container">
    <div id="content_main">

        <%@ include file="../common_new/col-left.jsp" %>
        <%@ include file="../common/right_2.jsp" %>


        <div id="right-container">

            <c:set var="classList" value="${fzStudentClassInfoList}" scope="request"/>

            <div class="control-bar">
                <c:forEach items="${classInfos}" var="classInfo">
                    <span cid="${classInfo.id}" classtype="${classInfo.classType}"
                          class="item class-item ypstudent">${classInfo.className}</span>
                </c:forEach>
            </div>
            <div id="fc_student_container" class="forIEFloatP">
                <h4>同学列表</h4>
                <hr style="background-color:#ff7e00;margin-top:10px;">
                <div id="fc_student_list">

                </div>

            </div>


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
        </div>

    </div>
</div>
<%@ include file="../common_new/foot.jsp" %>
</body>
</html>