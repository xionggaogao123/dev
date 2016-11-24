<%--
  Created by IntelliJ IDEA.
  User: wang_xinxin
  Date: 2016/6/16
  Time: 11:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@page import="com.pojo.app.SessionValue"%>
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>工资管理</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <link rel="shortcut icon" href="" type="image/x-icon" />
  <link rel="icon" href="" type="image/x-icon" />
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <!-- jquery artZoom4Liaoba styles -->
  <!-- Custom styles -->
  <link href="/static/css/expand.css?v=2015041602" rel="stylesheet" />
  <link rel="stylesheet" type="text/css" href="/static_new/css/gzgl/gongziguanli.css">
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
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
  <c:choose>
    <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
      <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
    </c:when>
    <c:otherwise>
      <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
    </c:otherwise>
  </c:choose>
  <!--.col-right-->
  <div class="col-right">
    
    <div class="gzgl-con">
      <div class="gzgl-nav clearfix">
        <ul>
        <c:choose>
          <c:when test="${roles:isHeadmaster(sessionValue.userRole) || roles:isManager(sessionValue.userRole)}">
          <li><a href="/wages/gzgl.do">薪酬管理</a><em></em></li>
          <li class="gzgl-active"><a href="javascript:;">我的工资条</a><em></em></li>
          <li><a href="/wages/gztj.do">工资统计</a><em></em></li>
          </c:when>
          <c:otherwise>
            <li class="gzgl-active"><a href="javascript:;">我的工资条</a><em></em></li>
          </c:otherwise>
        </c:choose>
        </ul>
      </div>
      <div class="gzgl-main">
        <div class="gzgl-gly-select">
          <select class="change" id="salaryYear">
          </select>
          <em>年</em>
          <select class="change" id="salaryMonth">
            <option value="1">01</option>
            <option value="2">02</option>
            <option value="3">03</option>
            <option value="4">04</option>
            <option value="5">05</option>
            <option value="6">06</option>
            <option value="7">07</option>
            <option value="8">08</option>
            <option value="9">09</option>
            <option value="10">10</option>
            <option value="11">11</option>
            <option value="12">12</option>
          </select>
          <em>月</em>
          <em>第</em>
          <select class="change" id="salaryNumber">
          </select>
          <em>次</em>
          <button id="seachMySalary">查询</button>
        </div>
        <table class="gzgl-my-table">
        </table>
        <script type="text/template" id="gzgl-my-table_temp">
          {{~it:value:index}}
          <tr>
            <th class="gzgl-th">工资条</th>
            <th class="gzgl-td" id="date">2016-3</th>
            <th class="gzgl-th">部门</th>
            <th class="gzgl-td"id="postion"></th>
            <th class="gzgl-th">姓名</th>
            <th class="gzgl-td">${sessionValue.userName}</th>
          </tr>
          {{ if(value.sendList.length>0){ }}
          {{~value.sendList:value2:index}}
            <tr>
            {{~value2:value4:index2}}
              <td class="gzgl-gray">{{=value4.itemName}}</td>
              <td>{{=value4.m}}</td>
            {{~}}
              {{ for (var i = 0, l = 3-value2.length; i < l; i++) { }}
              <td class="gzgl-gray"></td>
              <td></td>
              {{ } }}
            </tr>
          {{~}}
          <tr>
            <td class="gzgl-gray">&nbsp;</td>
            <td>&nbsp;</td>
            <td class="gzgl-gray">&nbsp;</td>
            <td>&nbsp;</td>
            <td class="gzgl-gray gzgl-bold">应发小计</td>
            <td class="gzgl-bold">{{=value.ssStr}}</td>
          </tr>
         {{ } }}
          {{ if(value.debitList.length>0){ }}
          {{~value.debitList:value3:index}}
            <tr>
            {{~value3:value5:index3}}
              <td class="gzgl-gray">{{=value5.itemName}}</td>
              <td>{{=value5.m}}</td>
            {{~}}
              {{ for (var i = 0, l = 3-value3.length; i < l; i++) { }}
              <td class="gzgl-gray"></td>
              <td></td>
              {{ } }}
            </tr>
          {{~}}
          <tr>
            <td class="gzgl-gray">&nbsp;</td>
            <td>&nbsp;</td>
            <td class="gzgl-gray">&nbsp;</td>
            <td>&nbsp;</td>
            <td class="gzgl-gray gzgl-bold">扣款小计</td>
            <td class="gzgl-bold">{{=value.msStr}}</td>
          </tr>
                  {{ } }}
          <tr>
            <td class="gzgl-gray">&nbsp;</td>
            <td>&nbsp;</td>
            <td class="gzgl-gray">&nbsp;</td>
            <td>&nbsp;</td>
            <td class="gzgl-gray gzgl-bold">实发工资</td>
            <td class="gzgl-green gzgl-bold">{{=value.asStr}}</td>
          </tr>
          <tr>
            <td class="gzgl-gray">备注</td>
            <td colspan="5" style="text-align: left;">{{=value.remark}}</td>
          </tr>
          {{~}}
        </script>
      </div>
    </div>


  </div>
  <!--/.col-right-->

</div>
<!--/#content-->
<div class="gzgl-meng"></div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>

<script type="text/template" id="yearListTemp">
  {{~it:value:index}}
  <option value="{{=value}}">{{=value}}</option>
  {{~}}
</script>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  var yearList = ${yearList};
  var currYear = ${currYear};
  var currMonth = ${currMonth};
  seajs.use('wdgz');
</script>
</body>
</html>
