<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2015/9/8
  Time: 14:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<html>
<head>
  <title>成绩分析</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <link href="/static/css/homepage.css" type="text/css" rel="stylesheet">
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link href="/static_new/css/TY-jiaoyuju.css" rel="stylesheet" />
  <link href="/static_new/css/chengji.css?v=2015041602" rel="stylesheet" />
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>

</head>
<body>

<!--=================================引入头部============================================-->
<%@ include file="../common_new/head.jsp" %>
<div id="content" class="clearfix">
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
  <!---广告->
<!--.col-right-->
  <div class="col-right">
    <div class="tab-head clearfix">
      <div class="uo">
        <ul>
          <%--<li class="cur currt"><a href="#">三年级一班</a></li>--%>
          <%--<li><a href="#">三年级二班</a></li>--%>
        </ul>
      </div>


      <!-- 内容 -->
      <div class="jiaoyuju-connent-right hwk1">
        <div class="jiaoyuju-connent-IM" id="schoolList">
          <%--<img width="180" height="50" src="/img/logofz461.png">--%>
          <%--<img width="180" height="50">--%>
          <%--<img width="180" height="50">--%>
          <%--<img width="180" height="50">--%>
          <%--<img width="180" height="50">--%>
          <%--<img width="180" height="50">--%>
          <%--<img width="180" height="50">--%>
          <%--<img width="180" height="50">--%>
        </div>

      </div>
      <!-- 内容 -->
    </div>
  </div>
  <!--/.col-right-->

</div>
<!--/#content-->



<div style="clear: both"></div>
<%@ include file="../common_new/foot.jsp"%>



<script id="schoolList_tmpl" type="text/template">
  {{ for(var i in it) { }}
  <img width="180" height="50" src="{{=it[i].logo}}" schoolId="{{=it[i].schoolId}}" class="school">
  {{ } }}
</script>


<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('/static_new/js/modules/chengji/0.1.0/educationBureau', function(educationBureau){
    educationBureau.init();
  });
</script>
<%--<script>--%>
  <%--function splitPage(page,pageSize){--%>
    <%--seajs.use('/static_new/js/modules/chengji/0.1.0/headermain', function(headermain) {--%>
      <%--headermain.splitPage(page,pageSize);--%>
    <%--});--%>
  <%--}--%>
<%--</script>--%>
</body>
</html>

