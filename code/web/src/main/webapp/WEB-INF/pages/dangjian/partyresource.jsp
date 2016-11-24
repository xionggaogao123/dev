<%@ page import="com.pojo.lesson.DirType" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2016/3/23
  Time: 14:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
  <link rel="stylesheet" type="text/css" href="/static/css/zTreeStyle/zTreeStyle.css"/>
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
  <script src="/static/js/lessons/coursesManage.js"></script>
</head>
<body>


<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div class="clearfix dangjian_content">
  <div class="dangjian_nav">
    <ul>
      <li type="7"><a class="focus">学习园地</a></li>
      <li type="8"><a>文明创建</a></li>
      <li type="9"><a>专题教育</a></li>
      <li type="10"><a>志愿服务</a></li>
      <li type="11"><a>党课活动</a></li>
      <li type="12"><a>队伍建设</a></li>
      <li type="13"><a>共建活动</a></li>
      <li type="14"><a>党员风采</a></li>
      <li type="15"><a>工作布置</a></li>
      <li type="16"><a>组织生活</a></li>
    </ul>
  </div>
  <div class="dangjian_main clearfix">
    <div class="dangjian_main_left">
      <div class="dangjian_left_title">
        <img src="img/dangjian_add.png"><span class="dangjian_add" id="newFolder">新建文件夹</span>
        <img src="img/dangjian_del.png"><span class="dangjian_del" id="deleteFolder">删除</span>
      </div>

      <ul id="DirUl" class="ztree dir-tree">
      </ul>

    </div>

    <div class="dangjian_content_right clearfix">
      <div class="dangjian_right_title clearfix">
        <p><span id="filecount"></span>个文件</p>
        <c:if test="${roles:isHeadmaster(sessionValue.userRole) || roles:isManager(sessionValue.userRole)}">
        <button id="users">用户权限管理</button>
        </c:if>
        <button id="manage">开启资料管理</button>
        <button id="upload">我要上传</button>
      </div>

      <div class="dangjian_right_main">
        <select id="term">
          <option value="ALLTERM">全部学期</option>
          <option value="2015-2016学年第二学期">2015-2016学年第二学期</option>
          <option value="2015-2016学年第一学期">2015-2016学年第一学期</option>
        </select>
        <div class="dangjian_right_file" id="dtos">

          <%--<div class="dangjiang_file">--%>
            <%--<img src="/img/K6KT/filecourse.png" class="dangjiang_file_img"><br>--%>
            <%--<span>等比数列基本量的计算</span>--%>
            <%--<button class="dangjian_file_edit">编辑</button>--%>
            <%--<button class="dangjian_file_del">删除</button>--%>
          <%--</div>--%>

        </div>
        <div style="display: none" id="warning">对不起，您没有权限查看，请联系管理员！</div>
      </div>
    </div>
    <div class="new-page-links"></div>

  </div>


</div>
<!--/#content-->

<script id="dtosTmpl" type="text/template">
  {{ for(var i in it) { }}
  <div class="dangjiang_file" mine="{{=it[i].isMine}}" id="{{=it[i].id}}" fid="{{=it[i].srcs[0].id}}" src="{{=it[i].srcs[0].value}}">
    <img src="/img/K6KT/filecourse.png" class="dangjiang_file_img"><br>
    <span class="span-over">{{=it[i].name}}</span>
    <button class="dangjian_file_edit">编辑</button>
    <button class="dangjian_file_del">删除</button>
  </div>
  {{ } }}
</script>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  var dirType = '<%=DirType.getDirType(7)%>';
  var dirTypes = [];
  dirTypes[1] = '<%=DirType.getDirType(1)%>';
  dirTypes[3] = '<%=DirType.getDirType(3)%>';
  dirTypes[7] = '<%=DirType.getDirType(7)%>';
  dirTypes[8] = '<%=DirType.getDirType(8)%>';
  dirTypes[9] = '<%=DirType.getDirType(9)%>';
  dirTypes[10] = '<%=DirType.getDirType(10)%>';
  dirTypes[11] = '<%=DirType.getDirType(11)%>';
  dirTypes[12] = '<%=DirType.getDirType(12)%>';
  dirTypes[13] = '<%=DirType.getDirType(13)%>';
  dirTypes[14] = '<%=DirType.getDirType(14)%>';
  dirTypes[15] = '<%=DirType.getDirType(15)%>';
  dirTypes[16] = '<%=DirType.getDirType(16)%>';
  var dangjian;
  seajs.use('/static_new/js/modules/dangjian/0.1.0/dangjian.js', function(dj){
    dangjian = dj;
  });

</script>
</body>
</html>