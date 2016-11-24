<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2015/7/10
  Time: 15:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
  <title></title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link href="/static_new/css/chengji.css?v=2015041602" rel="stylesheet" />
  <link href="/static_new/css/examine.css?v=2015041602" rel="stylesheet" />
  <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
</head>
<body>

<%@ include file="../common_new/head.jsp" %>
<div id="content" class="clearfix">
  <%@ include file="../common_new/col-left.jsp" %>

  <!--.col-right-->
  <div class="col-right">

    <!--.banner-info-->
   <!-- <img src="http://placehold.it/770x100" class="banner-info" />-->
    <!--/.banner-info-->

    <!--.tab-col-->
    <div class="tab-col">

      <div class="tab-head clearfix">
        <ul>
          <li class="cur"><a href="#">管理考试</a></li>
        </ul>
      </div>

      <div class="tab-main">

        <!--.tiaojian-col-->
        <div class="tiaojian-col clearfix managenent_info">
          <div>
            <span>年级列表</span>
          </div>
          <ul id="gradeLeader">
           <!-- <li>
              <strong>五年级</strong>
              <span>年级组长：<i>bob</i></span>
              <img src="/static_new/images/pencil.jpg">
              <img src="/static_new/images/trash.jpg">
            </li>
            <li>
              <strong>五年级</strong>
              <span>年级组长：<i>bob</i></span>
              <img src="../images/pencil.jpg">
              <img src="../images/trash.jpg">
            </li>
            <li>
              <strong>五年级</strong>
              <span>年级组长：<i>bob</i></span>
              <img src="../images/pencil.jpg">
              <img src="../images/trash.jpg">
            </li>-->
          </ul>
        </div>
        <!--/.tiaojian-col-->

        <!--.charts-col-->

      </div>

    </div>
    <!--/.tab-col-->

  </div>
  <!--/.col-right-->

</div>
<!--/#content-->



<%@ include file="../common_new/foot.jsp" %>

<script id="gradeLeaderTmpl" type="text/template">
  {{ for(var i = 0, l = it.rows.length; i < l; i++) {  }}
  <li class="grade">
    <strong id="gradeName">{{=it.rows[i].name}}</strong>
    <span id="gradeId" value="{{=it.rows[i].id}}">年级组长：<i>{{=it.rows[i].leaderName}}</i></span>
  </li>
  {{ } }}
</script>



<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('/static_new/js/modules/chengji/0.1.0/home_page', function(homepage) {
    homepage.initPage();
  });
</script>
</body>
</html>


