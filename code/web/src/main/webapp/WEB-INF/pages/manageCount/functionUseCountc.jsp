<%--
  Created by IntelliJ IDEA.
  User: guojing
  Date: 2015/4/9
  Time: 16:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>管理统计-平台功能统计</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link rel="stylesheet" type="text/css" href="/static_new/css/managecount/managecount2.css">
  <link rel="stylesheet" type="text/css" href="/static_new/css/dialog.css">
  <link href="/static/css/homepage.css" type="text/css" rel="stylesheet">
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
  <script type="text/javascript" src="/static/js/sharedpart.js"></script>
</head>
<body>

<!--=================================引入头部============================================-->
<%@ include file="../common_new/head-cloud.jsp" %>
<div class="student_infromation_main">
  <div style="width:1000px; margin: 0 auto; overflow: hidden; ">
    <!--=============================引入左边导航=======================================-->
    <%--<%@ include file="../common_new/col-left.jsp" %>--%>
    <!--=============================引入广告=======================================-->
    <!--广告-->
    <%--<c:choose>--%>
      <%--<c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">--%>
        <%--<jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>--%>
      <%--</c:when>--%>
      <%--<c:otherwise>--%>
        <%--<jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>--%>
      <%--</c:otherwise>--%>
    <%--</c:choose>--%>
    <!--广告-->


    <div class="right" style="width: 985px;">
      <div class="right_top">
        <p>
          <a href="/manageCount/schooltotal.do?a=10000&schoolid=${schoolId}" style="width: 130px;">平台使用统计</a><a href="javascript:;" style="width:130px;"  class="current">平台功能统计</a>
        </p>
        <div class="select">
          <select id="timeArea" name="timeArea" style="width:100px;">
            <c:forEach items="${timeAreas}" var="item">
              <option value="${item.key}">${item.value}</option>
            </c:forEach>
          </select>
          <input id="dateStart" name="dateStart"  style="width:140px; height:30px; border:1px solid #a9a9a9; outline:none; margin-right:4px;"
                 onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'dateEnd\')}'})"/>
          <input id="dateEnd" name="dateEnd"  style="width:140px; height:30px; border:1px solid #a9a9a9; outline:none; margin-right:4px;"
                 onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'dateStart\')}'})"/><br/>
          <input type="hidden" id="currTime" name="currTime" value="${currTime}">
          <input type="hidden" id="schoolId" name="schoolId" value="${schoolId}">
          <select id="role" name="role" style="width:100px;">
            <c:forEach items="${roles}" var="item">
              <option value="${item.key}">${item.value}</option>
            </c:forEach>
          </select>
          <select id="gradeId" name="gradeId" style="width:100px;">
            <option value="">全校</option>
            <c:forEach items="${grades}" var="item">
              <option value=${item.id}>${item.name}</option>
            </c:forEach>
          </select>
          <select id="classId" name="classId" style="width:129px;">
            <option value="">全校全部班级</option>
          </select>
          <select id="funId" name="funId" style="width:129px;">
            <c:forEach items="${roleFuns}" var="item">
              <option value="${item.key}">${item.value}</option>
            </c:forEach>
          </select>
          <span id="submitBtn">确定</span>
        </div>
      </div>
      <!--top-->
      <div class="tongji1">
        <div style="font-size: 28px; color:#333; text-align: center; line-height: 40px;">${schoolDTO.schoolName}</div>
        <p id="title1" name="title1" class="pp">
          <span>1</span>
          &nbsp;&nbsp;&nbsp; 微校园发帖数排行榜
        </p>
        <div class="table">
          <div id="title11" class="th">微校园发帖数</div>
          <div class="td" style="background:#f9f9f9;">
            <span style="border-right:1px solid #cccccc; height:12px; margin-top:14px; line-height:14px;">排名</span>
            <span style="border-right:1px solid #cccccc; height:12px; margin-top:14px; line-height:14px;">姓名</span>
            <span  id="title113">发帖数量</span>
          </div>
          <div id="mainTdiv1">
          </div>
          <script type="text/template" id="j-tmpl1">
            {{ for(var i in it) { }}
            <div style="cursor: pointer;" class="td detail" userId="{{=it[i].userId}}" userName="{{=it[i].name}}">
              <span>{{=it[i].number}}</span>
              <span>{{=it[i].name}}</span>
              <span>{{=it[i].newCountTotal}}</span>
            </div>
            {{ } }}
          </script>
        </div>
      </div>
      <!--统计1-->
      <div class="tongji2" style="border-bottom:none;">
        <p id="title2" name="title2" class="pp">
          <span>2</span>
          &nbsp;&nbsp;&nbsp;微校园发帖数统计
        </p>
        <div class="fangwen">
          <%--<div class="title">访问人数</div>--%>
          <div class="zhexian1" id="main1" style="width: 960px;height: 260px;"></div>
        </div>
        <div class="table">
          <div id="divTitle1" class="th">微校园发帖数统计</div>
          <div class="td" style="background:#f9f9f9;">
            <%--微校园（老师）发帖统计<span id="spanTitle1" style="border-right:1px solid #cccccc; height:12px; margin-top:14px; line-height:14px;"></span>
            <span style="border-right:1px solid #cccccc; height:12px; margin-top:14px; line-height:14px;">微校园新发帖数</span>--%>
            <span id="spanTitle1">老师姓名</span>

            <span id="spanTitle2">微校园发帖总数</span><%--微校园总发帖数--%>
          </div>
          <div id="mainTdiv2">
          </div>
          <script type="text/template" id="j-tmpl2">
            {{ for(var i in it) { }}
            <div style="cursor: pointer;" class="td detail" userId="{{=it[i].userId}}" userName="{{=it[i].name}}">
              <span>{{=it[i].name}}</span>
              <span>{{=it[i].newCountTotal}}</span>
            </div>
            {{ } }}
          </script>
        </div>
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
      <!--统计2-->
    </div>
  </div>
  <div style="clear: both"></div>
  <%@ include file="../common_new/foot.jsp" %>

  <!-- initialize seajs Library -->
  <script src="/static_new/js/sea.js"></script>
  <!-- Custom js -->
  <script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
  <script>
    seajs.use('funUseCount',function(funUseCount){
      funUseCount.init();
    });
  </script>
</body>
</html>
