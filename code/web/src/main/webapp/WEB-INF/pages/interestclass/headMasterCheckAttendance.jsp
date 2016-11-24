<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2016/5/16
  Time: 12:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
  <title>拓展课</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <link href="/static/css/homepage.css" type="text/css" rel="stylesheet">
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link href="/static_new/css/interestclass/attendance.css" rel="stylesheet" />
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
  <div class="col-right classlist">
    <h3>班级列表</h3>
    <p>
        <select id="terms" style="display: none;">
            <%--<option value="">2015-2016学年第一学期</option>--%>
        </select>
        <script id="termsTmpl" type="text/template">
            {{ for(var i=it.length-1;i>=0;i--) { }}
            <option value="{{=it[i].value}}">{{=it[i].name}}</option>
            {{ } }}
        </script>
        <select id="grades">
            <%--<option value="">全部年级</option>--%>
        </select>
        <script id="gradesTmpl" type="text/template">
            <option value="">全部年级</option>
            {{ for(var i in it) { }}
            <option value="{{=it[i].id}}">{{=it[i].name}}</option>
            {{ } }}
        </script>
        <select id="category">
            <%--<option value="">全部分类</option>--%>
        </select>
        <script id="categoryTmpl" type="text/template">
            <option value="allCategory">全部分类</option>
            {{ for(var i in it) { }}
            <option value="{{=it[i].id}}">{{=it[i].name}}</option>
            {{ } }}
        </script>
        <select id="weeks">
            <%--<option>第一周</option>--%>
        </select>
        <script id="weeksTmpl" type="text/template">
            {{ for(var i=it; i>=1; i--) { }}
            <option value="{{=i}}">第{{=transfer(i)}}周</option>
            {{ } }}
        </script>
        <input type="button" value="查询" class="fc_button" id="query">
    </p>
    <table>
        <thead>
        <tr>
            <th class="th1">课程名</th>
            <th class="th2">老师</th>
            <th class="th3">考勤</th>
            <th class="th4">课时数</th>
            <th class="th5">课时名</th>
            <th class="th6">教室</th>
        </tr>
        </thead>
        <tbody id="attendance">
            <%--<tr>--%>
                <%--<td>足球课</td>--%>
                <%--<td>嘿嘿嘿</td>--%>
                <%--<td>1/24</td>--%>
                <%--<td>12</td>--%>
                <%--<td>运球/第7周</td>--%>
                <%--<td>3011</td>--%>
            <%--</tr>--%>
        </tbody>

    </table>
      <script id="attendanceTmpl" type="text/template">
          {{ for(var i in it) { }}
          <tr>
              <td>{{=it[i].cNm}}</td>
              <td>{{=it[i].tNm}}</td>
              <td>{{=it[i].at}}</td>
              <td>{{=it[i].li}}</td>
              <td>{{=it[i].lNm}}</td>
              <td>{{=it[i].cRoom}}</td>
          </tr>
          {{ } }}
      </script>

      <div class="nodata">本周暂无老师提交考勤信息</div>

  </div>
  <!--/.col-right-->

</div>
<!--/#content-->



<div style="clear: both"></div>
<%@ include file="../common_new/foot.jsp"%>



<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static_new/js/modules/interestclass/0.1.0/headMasterCheckAttendance.js');
</script>
</body>
</html>
