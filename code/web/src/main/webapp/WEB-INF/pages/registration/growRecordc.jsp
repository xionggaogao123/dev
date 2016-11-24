<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.fulaan.base.controller.BaseController" %>
<%@ page import="com.pojo.user.UserRole" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>


<%@page import="com.pojo.app.SessionValue"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
  pageContext.setAttribute("basePath",basePath);
  int userRole = new BaseController().getSessionValue().getUserRole();
  boolean isAdmin = UserRole.isHeadmaster(userRole) || UserRole.isK6ktHelper(userRole);
  boolean isEdu = UserRole.isEducation(userRole);
  boolean baseCanEdit = false;
%>
<!DOCTYPE html>
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>复兰科技-学生评价系统</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="<%=basePath%>static_new/css/reset.css" rel="stylesheet" />
  <!-- jquery artZoom4Liaoba styles -->
  <!-- Custom styles -->
  <link href="<%=basePath%>static_new/css/growRecord_myself.css?v=2015041602" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
</head>
<script>

  var schools = ${schools};
  var terms = ${terms};
  var isEdu = "<%=isEdu%>";

</script>
<body>


<!--#head-->
<%@ include file="../common_new/head-cloud.jsp"%>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

  <!--.col-left-->
  <%--<%@ include file="../common_new/col-left.jsp" %>--%>
  <!--/.col-left-->

  <!--.col-right-->
  <div class="col-right" style="width: 1000px;">

    <!--.banner-info-->
    <!--  
    <img src="http://placehold.it/770x100" class="banner-info" />
    -->
    <!--/.banner-info-->

    <div class="grow-col">

      <div class="grow-col-head clearfix">
        <h3>学生评价系统</h3>
      </div>

      <div class="grow-tab-head clearfix">
        <ul>
          <li class="cur"><a href="#cjdId">学生成绩列表</a></li>
          <li ><a href="#szeduId">素质教育成绩</a></li>
        </ul>
      </div>

      <div>
        <!-- 成绩单开始 -->
        <div class="cjd" id="cjdId">
          <div class="grow-select">
            <select id="termSelection">
            </select>
            <select id="schoolSelection" <% if(!isEdu){ %> style="display:none" <% } %>>
            </select>
            <select id="gradeSelection">
            </select>
            <select id="classSelection">
            </select>
            <select id="examSelection">
            </select>
          </div>
          <div class="scrollable">
            <table class="gray-table" >
              <thead id="tableHead">
              </thead>
              <tbody id="tableList">
              </tbody>
            </table>
          </div>
          <!-- 分页div -->
          <div class="new-page-links" id="page_score">
          </div>
        </div>
        <!-- 成绩单结束 -->

        <!-- 素质教育开始 -->
        <div class="hide szedu" id="szeduId">
          <div class="grow-select">
            <select id="schoolSelection_quality" <% if(!isEdu){ %> style="display:none" <% } %>>
            </select>
            <select id="gradeSelection_quality">
            </select>
            <select id="classSelection_quality">
            </select>
          </div>
          <div class="scrollable">
            <table class="gray-table" >
              <thead>
              <th width="120">学生姓名</th>
              <th width="130">操作</th>
              </thead>
              <tbody id="tableList_quality">
              </tbody>
            </table>
          </div>
          <!-- 分页div -->
          <div class="new-page-links" id="page_quality">
          </div>
        </div>
        <!-- 素质教育结束 -->
      </div>
    </div>

  </div>
  <!--/.col-right-->

</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->



<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="<%=basePath%>static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('growthRecord')
</script>
</body>
</html>