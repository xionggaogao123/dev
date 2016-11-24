<%--
  Created by IntelliJ IDEA.
  User: wang_xinxin
  Date: 2015/8/19
  Time: 19:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>复兰科技</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link href="/static_new/css/microlesson/microlesson.css?v=2015041602" rel="stylesheet" />
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>
<body>
<!--#head-->
<%@ include file="../common_new/head-cloud.jsp"%>
<!--/#head-->
<!--#content-->
<div id="content" class="clearfix">
  <!--.col-left-->
  <%--<%@ include file="../common_new/col-left.jsp" %>--%>
  <!--/.col-left-->
  <%--<c:choose>--%>
    <%--<c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">--%>
      <%--<jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>--%>
    <%--</c:when>--%>
    <%--<c:otherwise>--%>
      <%--<jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>--%>
    <%--</c:otherwise>--%>
  <%--</c:choose>--%>
  <!--.col-right-->
  <div class="col-right" style="width: 1000px;">

    <!--.tab-col右侧-->
    <div class="tab-col lookresults">
      <h3>${matchname}<span>打分时间：${sbegtime} – ${sendtime}</span></h3>
      <table width="100%">
        <thead>
        <tr>
          <th>课程名称</th>
          <th>总分</th>
          <th>平均分</th>
          <th>排名</th>
        </tr>
        </thead>
        <tbody class="liebiao">
        <c:forEach items="${scorelist}" var="score" varStatus="loop">

          <tr>
            <td>${score.lessonname}</td>
            <td>${score.allscore}</td>
            <td>${score.average}</td>
            <td>${score.sort}</td>
          </tr>
          </c:forEach>
       <%-- </script>--%>
        </tbody>
      </table>
    </div>
    <!--/.tab-col右侧-->

  </div>
  <!--/.col-right-->

</div>
<!--/#content-->






















<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('matchresult');
</script>
</body>
</html>
