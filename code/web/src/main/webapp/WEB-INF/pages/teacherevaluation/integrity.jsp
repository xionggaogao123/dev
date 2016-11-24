<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2016/8/1
  Time: 15:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <title>复兰科技-教师评价系统</title>
  <meta name="renderer" content="webkit">
  <link rel="stylesheet" type="text/css" href="/static_new/css/teacherevaluation/evaluation.css">
  <link rel="stylesheet" type="text/css" href="/static_new/css/reset.css">
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
  <script type="text/javascript">
    $(function(){
      $('.eva-dc').click(function(){
        window.open("/teacherevaluation/${evid}/items/exportIntegrity.do");
      })
    })
  </script>
</head>

<body>


<!--#head-->
<%@ include file="../common_new/head.jsp"%>
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

    <!--.tab-col右侧-->
    <div class="tab-col">

      <div class="tab-head clearfix">
        <ul>
          <li id="JSPJ" class="cur"><a href="javascript:;" >教师评价</a><em></em></li>
        </ul>
      </div>
      <div class="tab-main">
        <!--=================================评分状态start==================================-->
        <div class="evaluate-state">
          <div class="evaluate-title clearfix">
            <%--<span class="eva-num">未评价人数：${count}</span>--%>
            <button class="eva-dc">导出名单</button>
          </div>
          <table class="newTable tea-name">
            <thead>
            <tr>
              <th>序号</th>
              <th>打分项目</th>
              <th>老师姓名</th>
              <th>漏打人数</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${list}" var="item" varStatus="status">
              <tr>
                <td>${status.index + 1}</td>
                <td>${item.step}</td>
                <td>${item.teacherName}</td>
                <td>${item.count}</td>
              </tr>
            </c:forEach>

            </tbody>
          </table>
        </div>
        <!--=================================评分状态end==================================-->
      </div>
      <!--/.col-right-->

    </div>
    <!--/#content-->
  </div>
</div>
<div class="bg"></div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp"%>
<!--#foot-->

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="../js/sea.js"></script>
<!-- Custom js -->
<script src="../js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('../chengjifenxi/js/stu-chengji.js');
</script>

</body>
</html>
