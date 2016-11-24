<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2016/6/14
  Time: 18:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>复兰科技-党建</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <link rel="shortcut icon" href="" type="image/x-icon" />
  <link rel="icon" href="" type="image/x-icon" />
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="/static_new/css/reset.css" rel="stylesheet" type="text/css">
  <!-- jquery artZoom4Liaoba styles -->
  <!-- Custom styles -->

  <link rel="stylesheet" type="text/css" href="/static_new/css/dangjian.css"/>
</head>
<body>


<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div class="djry-set">
  <span class="djry-set-title">党建功能老师权限设置</span>
  <table class="djry-set-table">
    <thead>
      <tr>
        <th>老师姓名</th>
        <th>党员</th>
        <th>中心组成员</th>
        <th>党支书</th>
      </tr>
    </thead>
    <tbody id="dtos">
      <%--<tr>--%>
        <%--<td>张三</td>--%>
        <%--<td><input type="checkbox"></td>--%>
        <%--<td><input type="checkbox"></td>--%>
        <%--<td><input type="checkbox"></td>--%>
      <%--</tr>--%>
    </tbody>
    <script id="dtosTmpl" type="text/template">
      {{ for(var i in it) { }}
      <tr id="{{=it[i].id}}" uid="{{=it[i].userId}}" sid="{{=it[i].schoolId}}">
        <td>{{=it[i].userName}}</td>
        <td><input class="pm" type="checkbox" {{ if(it[i].isPartyMember == 1){ }} checked {{ } }}></td>
        <td><input class="cm" type="checkbox" {{ if(it[i].isCenterMember == 1){ }} checked {{ } }}></td>
        <td><input class="ps" type="checkbox" {{ if(it[i].isPartySecretary == 1){ }} checked {{ } }}></td>
      </tr>
      {{ } }}
    </script>
  </table>
</div>
<div class="new-page-links" style="left: 132px;position: relative;"></div>
<!--/#content-->




<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('/static_new/js/modules/dangjian/0.1.0/djuser.js');
</script>
</body>
</html>
