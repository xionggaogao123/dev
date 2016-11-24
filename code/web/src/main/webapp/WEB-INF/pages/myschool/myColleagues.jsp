<%--
  Created by IntelliJ IDEA.
  User: fulan
  Date: 14-11-7
  Time: 下午3:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>--%>
<!DOCTYPE html>
<html>
<head>
    <title>我的同事-复兰科技 K6KT-快乐课堂</title>
    <meta charset="utf-8">
    <meta http-equiv = "X-UA-Compatible" content="IE=edge" />
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap-combined.school.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/dialog.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/message.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap-combined.school.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/k6kt.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/headmaster.css"/>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js" ></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/js/fzprivatemessage.js"></script>
    <script type="text/javascript" src="/static/js/card.js"></script>
    <script type="text/javascript" src="/static/js/common/role.js"></script>
    <script type="text/javascript">
        $(function(){
            var subjectId = $('.headmaster-teacher-subject').find('.active').attr('sid');
            var gradeId = $('.headmaster-teacher-grade').find('.active').attr('cid');
            getBrowserList(subjectId, gradeId, 1);

            $('.headmaster-teacher-subject>li,.headmaster-teacher-grade>li').on('click', function() {
                $(this).parent().find('.active').removeClass('active');
                $(this).addClass('active');
                var subjectId = $('.headmaster-teacher-subject').find('.active').attr('sid');
                var gradeId = $('.headmaster-teacher-grade').find('.active').attr('cid');
                getBrowserList(subjectId, gradeId, 1);
            });

            function getBrowserList(sid, gid, bType) {
                var postData = {};

                if (typeof bType == 'undefined') {
                    postData.browserType = 0;
                } else if (bType == 1) {
                    postData.browserType = bType;
                } else {
                    postData.browserType = bType;
                }
                if (typeof gid != 'undefined') {
                    postData.gradeId = gid;
                } else {
                    postData.gradeId = 0;
                }
                if (typeof sid != 'undefined') {
                    postData.subjectId = sid;
                } else {
                    postData.subjectId = 0;
                }

                $.ajax({
                    url: "/myschool/selteacher.do",
                    type: "get",
                    dataType: "json",
                    async: true,
                    data: postData,
                    success: function(data) {
                        ret = data;
                        if (bType == 0) {
                            showClassList(data);
                        } else {
                            showTeacherList(data);
                        }
                    },
                    error: function() {
                        console.log('selSubjectTeacher error');
                    }
                });
            }

            function showTeacherList(data) {
                var html = '';
                if (data.teacherList) {
                    for (var i = 0; i < data.teacherList.length; i++) {
                        var te = data.teacherList[i];
                        var attach = '';
                        if (nb_role == 2 || nb_role == 9 || nb_role == 3) {
                            attach = classid + '/';
                        }
                        //如果被迭代的用户名是当前登录用户,则退出迭代
                        if(te.userName == '${sessionValue.userName}'){
                            continue;
                        }
                        html += '<div  class="teacher-info">';
                        html += '<img class="teacher-img"  src="' + te.imgUrl + '" role="' + te.role + '" target-id="' + te.id + '">';
                        html += '<div class="teacher-name" style="height: 18px;width: 80px;">' + te.userName + ' ' + te.subjectName + '</div>';
                        html += '<div class="teacher-name" style="height: 18px;width: 80px;"> 经验值: <span class="experience-span">'+te.experienceValue+'</span></div></div>';
                    }
                    var teacherList = $('#teacher-list');
                    teacherList.html(html);
                } else {
                    for (var i = 0; i < data.rows.length; i++) {
                        var te = data.rows[i];
                        var attach = '';
                        if(te.userName == '${sessionScope.currentUser.userName}'){
                            continue;
                        }
                        // href="/teacher/course/' + te.teacherId + '"
                        html += '<div class="teacher-info">';
                        html += '<img class="teacher-img"  src="' + te.imgUrl + '" role="' + te.role + '" target-id="' + te.id + '">';
                        html += '<div class="teacher-name" style="height: 18px;width: 80px;">' + te.userName + '</div>';
                        html += '<div class="teacher-name" style="height: 18px;width: 80px;"> 经验值: <span class="experience-span">'+te.experienceValue+'</span></div></div>';
                    }
                    $(".headmaster-teacher-info").html(html);
                }
            }

        })


    </script>

    <script type="text/javascript">
        var currentPageID = 4;
    </script>
</head>
<body>
<!-- user info start -->

<%@ include file="../common_new/head.jsp" %>
<jsp:include page="/infoBanner.do">
    <jsp:param name="bannerType" value="userCenter" />
    <jsp:param name="menuIndex" value="4" />
</jsp:include>

<div id="content_main_container">
    <div id="content_main">
        <!-- 左侧导航-->
        <%@ include file="../common_new/col-left.jsp" %>

        <!-- left end -->
        <!-- right start-->
        <div id="right-container" style="float: none">
            <div style="height:111px;">
            <%@ include file="../common/right.jsp" %>
            </div>
            <!-- user info end -->
            <!-- 老师开始 -->
            <div class="main-container">
                <div class="headmaster-teacher-container">
                    <div class="select-subject">
                        <ul class="headmaster-teacher-subject">
                            <li class='active' sid="0">全部学科</li>
                            <c:forEach items="${subjectList}" var="subject">
                                <li sid="${subject.subjectId}">${subject.name}</li>
                            </c:forEach>
                        </ul>
                    </div>
                    <div class="select-grade">
                        <ul class="headmaster-teacher-grade">
                            <li class="active">全部年级</li>
                            <c:forEach items="${gradeList}" var="grade">
                                <li cid="${grade.gradeId}">${grade.name}</li>
                            </c:forEach>
                        </ul>
                    </div>

                    <div class="headmaster-teacher-info">

                    </div>

                </div>
                <div style="margin-top: 30px; margin-bottom: 30px; text-align:center">
                    <div id="example" style="display:none;"></div>
                </div>
            </div>
            <!-- 老师结束 -->
        </div>
        <!-- right end -->
    </div>
</div>

<div class="info-card"></div>
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
<%@ include file="../common_new/foot.jsp"%>
</body>
</html>
