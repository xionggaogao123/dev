<%--
  Created by IntelliJ IDEA.
  User: fulaan
  Date: 15-8-6
  Time: 下午4:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<html>
<head>
    <title>复兰科技-选课去向表</title>
    <link rel="stylesheet" href="/static_new/css/myschool/curriculaI.css">
    <link rel="stylesheet" href="/static_new/css/reset.css">
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
</head>
<body>
<div class="curricula-main">
    <!--=======================头部========================-->
    <!--==================start引入头部==================-->
    <%@ include file="../common_new/head.jsp" %>
    <!--====================end引入头部====================-->
    <!--====================左边导航==========================-->
    <!--========================广告============================-->
    <div class="curriculai-right">
        <div class="curricula-info">
            <dl>
                <dt>学生拓展课去向</dt>
                <dd>
                    <em>年级</em>
                    <select id="gradeId" name="gradeId" style="width:90px;">
                        <option value="">全部年级</option>
                        <c:forEach items="${gradeList}" var="item">
                            <option value="${item.id}">${item.name}</option>
                        </c:forEach>
                    </select>
                    <em>班级</em>
                    <select id="classId" name="classId" style="width:90px;">

                    </select>
                    <em>学期</em>
                    <select id="term" name="term" style="width:225px;">

                    </select>
                    <input type="hidden" id="schoolId" value="${schoolId}"/>
                    <button id="exportExcel" style="float:right;">按行政班导出</button>
                    <button id="searchData" style="float:right;">查询</button>
                    <div class="export">
                        <button id="exportExcel1" style="float:right;">按拓展班导出</button>
                    </div>

                </dd>
            </dl>
            <div class="curricula-top">
                <span class="curricula-top-I">学生姓名</span>|
                <span class="curricula-top-II">年级</span>|
                <span class="curricula-top-III">班级</span>|
                <span class="curricula-top-IIII">选课去向</span>
            </div>
            <div class="curricula-ta"></div>

            <!--.sub-info-list-->
            <div class="sub-info-list">

            </div>
            <!--.sub-info-list-->
            <!--.list-info-->
            <script type="text/template" id="j-tmpl">
                <ul class="curricula-bl">
                    {{~it.content:v:index}}
                    <li class="curricula-b">
                        <span class="b">
                            <img class="curricula-img" src="{{=v.imageURL}}" width="60px;" height="60px;">
                            <em class="curricula-sp">{{=v.userName}}</em>
                        </span>
                        <span class="curricula-a">{{=v.gradeName}}</span>
                        <span class="curricula-t">{{=v.className}}</span>
                        <span class="curricula-s">{{=v.interestClassInfo}}</span>
                    </li>
                    {{~}}
                </ul>
            </script>
            <div class="page-paginator">
                <span class="first-page">首页</span>
                        <span class="page-index">
                            <span class="active">1</span>
                            <span>2</span>
                            <span>3</span>
                            <span>4</span>
                            <span>5</span>
                            <i>···</i>
                        </span>
                <span class="last-page">尾页</span>

            </div>
        </div>
    </div>
</div>
<%@ include file="../common_new/foot.jsp"%>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('curriculaI');
</script>
</body>
</html>
