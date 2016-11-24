<%@ page import="com.pojo.lesson.DirType" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@page import="com.pojo.app.SessionValue"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="renderer" content="webkit">
    <title>班级课程</title>
    <link rel="stylesheet" type="text/css" href="/static/plugins/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/flippedclassroom.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap-combined.school.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/teacherSheet.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/class-course.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/cloudclass.css"/>
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
    <script type="text/javascript" src="/static/js/experienceScore.js"></script>
    
</head>
<body>
<!-- 页头 -->
<%@ include file="../common_new/head.jsp" %>

<!-- 页头 -->

<div id="content_main_container">
    <div id="content_main">
        <!-- 左侧导航-->
        <%@ include file="../common_new/col-left.jsp" %>
        <!-- left end -->
        <!-- right start-->
        <div id="right-container">
            <jsp:include page="../common/teacherInfoHead.jsp">
                <jsp:param name="dir" value="class"/>
            </jsp:include>
            <div class="main-container" style="margin: 10px;background: #eeeeee">
                <div style="font-size: 30px;color: #333333;min-height: 100px;margin: 230px 90px;width: 590px;line-height: 45px;">
                    <c:choose>
                        <c:when test="${roles:isHeadmaster(sessionValue.userRole) && !roles:isTeacher(sessionValue.userRole)}">
                            您没有班级，无法查看
                        </c:when>
                        <c:otherwise>
                            班级课程已移动到翻转课堂--作业，请
                            <a href="/homework/teacher.do" style="color: #ff8a00">点击</a>
                            作业查看
                        </c:otherwise>
                    </c:choose>

                </div>
                <div class="course-left-container">
                    <div id="file-tools" style="display: none">
                        <ul>
                            <a id="newFolder" onclick="newDirByToolBtn()" class="tool-not-allowed" title="请先选择班级或文件夹">
                                <li class="fa fa-plus fa-1.5x"><label style="margin-left:7px;"
                                                                      class="tool-not-allowed">新建文件夹</label></li>
                            </a>
                            <a id="deleteFolder" onclick="deleteDirByToolBtn()" class="tool-not-allowed"
                               title="请先选择文件夹">
                                <li class="fa fa-trash-o fa-1.5x"><label style="margin-left:7px;"
                                                                         class="tool-not-allowed">删除</label></li>
                            </a>
                        </ul>
                    </div>
                    <ul id="class-course-ul" class="ztree dir-tree" style="display: none">
                    </ul>
                </div>
                <div class="course-right-container">
                </div>
            </div>
        </div>
        <!-- right end-->
    </div>
</div>



<!-- 页尾 -->
<%@ include file="../common_new/foot.jsp" %>
<!-- 页尾 -->
</body>
</html>