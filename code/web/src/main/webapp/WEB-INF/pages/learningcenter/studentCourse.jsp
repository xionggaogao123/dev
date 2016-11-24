<%@ page import="com.pojo.lesson.DirType" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="renderer" content="webkit">
    <title>我的微课</title>
    <link rel="stylesheet" type="text/css" href="/static/plugins/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/flippedclassroom.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap-combined.school.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/teacherSheet.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/class-course.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/zTreeStyle/zTreeStyle.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/tree.css"/>
    <link rel="stylesheet" type="text/css" href="/static/js/contextMenu/src/jquery.contextMenu.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/artDialog/ui-dialog.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css"/>
    <script type="text/javascript" src='/static/js/jquery-1.11.1.min.js'></script>
    <script type="text/javascript" src='/static/plugins/bootstrap/js/bootstrap.min.js'></script>
    <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"></script>
    <script type="text/javascript" src="/static/js/ztree/jquery.ztree.all-3.5.min.js"></script>
    <script type="text/javascript" src="/static/js/contextMenu/src/jquery.contextMenu.js"></script>
    <script type="text/javascript" src="/static/js/artDialog/dialog-plus-min.js"></script>
    <script type="text/javascript" src="/static/js/lessons/coursesManage.js"></script>

</head>
<body>
<!-- 页头 -->
<%@ include file="../common_new/head.jsp" %>
<div id="content_main_container">
    <div id="content_main">

        <%@ include file="../common_new/col-left.jsp" %>
        <jsp:include page="../common/right_2.jsp"></jsp:include>


        <div id="right-container">

            <jsp:include page="../common/infoBanner.jsp">
                <jsp:param name="bannerType" value="coursePage"/>
                <jsp:param name="menuIndex" value="1"/>
                <jsp:param name="template" value="student"/>
            </jsp:include>

            <div class="main-container"style="float: left;margin: 12px 0px 0px 12px;background-color: #EEE;width: 770px;min-height: 700px;height: auto;overflow: visible;">
                <div style="font-size: 30px;color: #333333;min-height: 100px;margin: 230px 90px;width: 590px;line-height: 45px;">
                    班级课程已移动到翻转课堂--作业，请
                    <a href="/homework/student.do" style="color: #ff8a00">点击</a>
                    作业查看
                </div>
                <div class="course-left-container" >
                    <ul id="student-course-ul" class="ztree dir-tree" style="display: none">
                    </ul>
                </div>
                <div class="course-right-container student-course-container" style="float: none">
                </div>
            </div>
            <!-- 页尾 -->

            <!-- 页尾 -->
        </div>

    </div>
    <%@ include file="../common_new/foot.jsp" %>
</div>

</body>
</html>