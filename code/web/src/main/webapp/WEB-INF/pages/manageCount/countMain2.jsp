<%--
  Created by IntelliJ IDEA.
  User: Tony
  Date: 2015/4/13
  Time: 10:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<html>
<head>
    <title>管理统计-教育局首页</title>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/css/manageCount/jiaoyuju1.css">
    <%--<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>--%>
    <%--<script type="text/javascript" src="/js/manageCount/xiaozhang1.js "></script>--%>
    <%--<script type="text/javascript" src="/js/manageCount/WdatePicker.js"></script>--%>
</head>
<body>
<!--=================================引入头部============================================-->
<%@ include file="../common_new/head.jsp" %>
<div class="student_infromation_main">
    <div style="width:1200px; margin: 0 auto; overflow: hidden; ">
    <!--=============================引入左边导航=======================================-->
        <%@ include file="../common_new/col-left.jsp" %>
        <!--广告-->
        <c:choose>
            <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
                <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
            </c:when>
            <c:otherwise>
                <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
            </c:otherwise>
        </c:choose>
        <!--广告-->
    <div class="right">
        <div class="right_top">
            <p>
                <a href="javascript:;" class="current" style="width:130px;">统计首页</a>
                <span style="float:right;"><a style="width:130px;" href="javascript:;" onclick="location.href='/manageCount/educationschools.do'">全部学校统计</a></span>
            </p>
        </div>
        <div class="right_bottom">
            <ul>
        		<c:forEach items="${list}" var="item">
                    <a target="_blank" href="/managetotal/${item.schoolId}"><li>${item.schoolName}</li></a>
                </c:forEach>
            </ul>
        </div>
    </div>
</div>
<div style="clear: both"></div>
<%@ include file="../common_new/foot.jsp"%>
</body>
</html>




