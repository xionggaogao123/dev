<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.fulaan.base.controller.BaseController" %>
<%@ page import="com.pojo.user.UserRole" %>
<%@ taglib prefix="cc" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>


<%@page import="com.pojo.app.SessionValue"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    pageContext.setAttribute("basePath",basePath);
    boolean isAdmin = UserRole.isManager(new BaseController().getSessionValue().getUserRole());
%>

<!DOCTYPE html>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn"/>
    <title>复兰科技</title>
    <meta name="description" content="">
    <meta name="author" content=""/>
    <meta name="copyright" content=""/>
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="<%=basePath%>static_new/css/reset.css" rel="stylesheet"/>
    <!-- Custom styles -->
    <link href="<%=basePath%>static_new/css/kaowuguanli.css?v=2015071201" rel="stylesheet"/>
    <link href="<%=basePath%>static_new/js/modules/treeView/0.2.0/zTreeStyle.css" rel="stylesheet" />
    <link href="<%=basePath%>static_new/css/school.css" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>

</head>
<body>
<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">


    <!--.col-left-->
    <%@ include file="../common_new/col-left.jsp" %>
    <!--/.col-left-->

    <!--.banner-info-->
    <c:choose>
        <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
        </c:when>
        <c:otherwise>
            <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
        </c:otherwise>
    </c:choose>
    <!--/.banner-info-->

    <!--.col-right-->
    <div class="col-right">



        <!--.tab-col-new-->
        <div class="tab-col">
            <div class="tab-head clearfix">
                <cc:set scope="page" var="isTeacher" value="${roles:isTeacher(sessionValue.userRole)}"/>
                <cc:set scope="page" var="isAdmin"
                        value="${roles:isHeadmaster(sessionValue.userRole) || roles:isManager(sessionValue.userRole) }"/>
                <ul>
                    <cc:if test="${isAdmin }">
                        <li class="cur">
                            <i></i>
                            <a href="#kaoshi-view" id="tabExamManage">考试管理</a>
                        </li>
                    </cc:if>
                    <li>
                        <i></i>
                        <a href="#chengji-view" id="tabScoreInput">成绩录入设置</a>
                    </li>
                    <cc:if test="${!isAdmin && isClassMaster }">
                        <li>
                            <i></i>
                            <a href="#kaosheng-view" id="tabStudentArrange">考生安排</a>
                        </li>
                    </cc:if>
                    <cc:if test="${isAdmin }">
                        <li>
                            <i></i>
                            <a href="#dengji-view" id="tabLevelSetting">等级设置</a>
                        </li>
                        <li>
                            <i></i>
                            <a href="#kaosheng-view" id="tabStudentArrange">考生安排</a>
                        </li>
                        <li>
                            <i></i>
                            <a href="#kaochang-view" id="tabExamroomSetting">考场资源设置</a>
                        </li>

                        <li>
                            <i></i>
                            <a href="#school-regional-exam-view" id="tabAteaEntranceExamination">区域联考</a>
                        </li>

                    </cc:if>
                </ul>
            </div>
            <div class="tab-main-new clearfix" id="page-content">
                <form id="commonFormDataContainer"></form>
                <cc:if test="${isAdmin }">
                    <%--考试管理--%>
                    <%@include file="examManage.jsp" %>

                    <%--等级设置--%>
                    <%@include file="scoreLevel.jsp" %>

                    <%--考生安排--%>
                    <%@include file="arrangeExamroom.jsp" %>

                    <%--考场管理--%>
                    <%@include file="examroom.jsp" %>
                    <%--区域联考 --%>
                    <%@include file="schoolExamArea.jsp" %>
                </cc:if>
                <%--成绩录入--%>
                <%@include file="score.jsp" %>
                <cc:if test="${!isAdmin && isClassMaster }">
                    <%--考生安排--%>
                    <%@include file="arrangeExamroom.jsp" %>
                </cc:if>
            </div>
        </div>
        <!--/.tab-col-new-->
    </div>
    <!--/.col-right-->
</div>
<!--/#content-->
<div class="bg-dialog" id="bgbgbgbg"></div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<script type="text/javascript" src="<%=basePath%>static_new/js/modules/core/0.1.0/jquery.min.js?v=2015041602"></script>
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="<%=basePath%>static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('kwgl', function (kwgl) {
        <cc:if test="${!roles:isHeadmaster(sessionValue.userRole) && !roles:isManager(sessionValue.userRole) }">
        kwgl.teacherFunc();
        </cc:if>
        kwgl.init();
    });
    <cc:if test="${isAdmin }">
    seajs.use('areaExam');

    </cc:if>


</script>
</body>
</html>