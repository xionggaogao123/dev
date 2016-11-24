<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2015/12/21
  Time: 15:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>题库</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <!-- jquery artZoom4Liaoba styles -->
  <!-- Custom styles -->
  <link href="/static_new/css/tiku.css" rel="stylesheet" />

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
  <!-- 广告部分 -->
  <c:choose>
    <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
      <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
    </c:when>
    <c:otherwise>
      <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
    </c:otherwise>
  </c:choose>
  <!-- 广告部分 -->
  <!--.col-right-->
  <div class="col-right">

    <div class="up-cont">
      <div class="up-cont1">教师题库 > 上传试题</div>
      <dl class="up-cont2">
        <dd>
          <span>第一步</span>
          <span class="span2"><a href="http://7xiclj.com1.z0.glb.clouddn.com/568f20d80cf2141e36060200.doc" target="_blank">下载模板</a></span>
        </dd>
        <dd>
          <span>第二步</span>
          <input type="file" id="file" name="file"/>
          <em>*必选，支持doc类型文档</em>
        </dd>
        <dd>
          <button>开始导入</button>
        </dd>
      </dl>
    </div>

    <!--上传通知-->
    <div class="wind-up">
      <div class="tc-top">提示 <em>×</em></div>
      <div class="winde-up2">
        <span class="span-zz"></span>
        <span class="span-wait">文档解析中...请耐心等待</span>
      </div>
    </div>
    <!--上传通知-->

    <!--半透明背景-->
    <div class="bg"></div>
    <!--/半透明背景-->
  </div>
  <!--/.col-right-->
</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp"%>
<!--#foot-->


<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('/static_new/js/modules/itempool/0.1.0/upload');
</script>

</body>
</html>
