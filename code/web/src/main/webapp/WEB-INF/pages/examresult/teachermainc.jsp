<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%--
  Created by IntelliJ IDEA.
  User: qinbo
  Date: 15/6/18
  Time: 下午4:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@page import="com.pojo.app.SessionValue"%>

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
  <link href="/static_new/css/chengji.css?v=2015041602" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>

</head>
<body>

<!--=================================引入头部============================================-->
<%@ include file="../common_new/head-cloud.jsp" %>
<div id="content" class="clearfix">
  <%--<%@ include file="../common_new/col-left.jsp" %>--%>
  <!--广告-->
  <%--<c:choose>--%>
    <%--<c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">--%>
      <%--<jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>--%>
    <%--</c:when>--%>
    <%--<c:otherwise>--%>
      <%--<jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>--%>
    <%--</c:otherwise>--%>
  <%--</c:choose>--%>
  <!---广告->
<!--.col-right-->
  <div class="col-right" style="width: 1000px;">


    <%--<!--广告-->--%>
    <%--<c:choose>--%>
    <%--<c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">--%>
    <%--<jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>--%>
    <%--</c:when>--%>
    <%--<c:otherwise>--%>
    <%--<jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>--%>
    <%--</c:otherwise>--%>
    <%--</c:choose>--%>
    <%--<!--广告-->--%>

    <!--.tab-col-->
    <div class="tab-col">

      <div class="tab-head clearfix">
        <ul class="teachermain-cur">
          <li class="cur"><a href="#">首页</a></li>
          <li><a href="/score/teacher/semester.do?a=10000">学期成绩</a></li>
        </ul>
        <a href="/score/teacher/input.do?a=10000" class="green-btn">成绩录入</a>
      </div>

      <div class="tab-main">

        <!--.tiaojian-col-->
        <div class="tiaojian-col clearfix">

          <div class="select-style" >
            <select id="exam_teacher_select_classsubject">

            </select>
            <select id = "exam_teacher_select_exam">

            </select>
          </div>



          <div class="tianjian-btn">
            <ul>
              <li class="cur" id="reality">实际分</li>
              <li class="cu" id="hundred">百分制</li>
            </ul>

          </div>

        </div>
        <!--/.tiaojian-col-->

        <!--.charts-col-->
        <div class="charts-col">

          <h3 id="totaltitle"></h3>

          <div class="charts-list" >

            <dl id="dlbjcjdb">
              <dt><em>1</em>班级成绩对比</dt>
              <dd id="cjdbId" class="charts-height"></dd>
            </dl>

            <dl id="dlcjfb">
              <dt><em id="num2">2</em>成绩分布</dt>
              <dd id="cjfbId" class="charts-height" style="width: 960px;height: 260px;"></dd>
            </dl>

            <dl id="dlcjtj">
              <dt><em id="num3">3</em>成绩统计</dt>
              <dd class="clearfix">

                <div class="huan-charts" style="width: 450px;">
                  <span id="pjfId" style="width: 400px;"></span>
                  <ul class="clearfix">
                    <li>班级均分<em id="bjjf"></em></li>
                    <li id="linjjf">年级均分<em id="njjf"></em></li>
                  </ul>
                </div>

                <div class="huan-charts" style="width: 450px;">
                  <span id="bhgId" style="width: 400px;"></span>
                  <ul class="clearfix">
                    <li>班合格率<em id="bjhgl"></em></li>
                    <li>班优秀率<em id="bjyxl"></em></li>
                  </ul>
                </div>

                <div class="huan-charts" id="grade" >
                  <span id="njhgId"></span>
                  <ul class="clearfix">
                    <li>年级合格率<em id="njhgl"></em></li>
                    <li>年级优秀率<em id="njyxl"></em></li>
                  </ul>
                </div>
              </dd>
            </dl>

            <dl id="dlxslb">
              <dt><em id="num4">4</em>学生列表</dt>
              <dd class="std-list">
                <h4 style="width: 976px;">成绩表</h4>
                <table width="100%">
                  <thead>
                  <th class="25%"><em>名字</em></th>
                  <th class="25%"><em>考试分数</em></th>
                  <th class="25%"><em>班级排名</em></th>
                  <th><em>年级排名</em></th>
                  </thead>
                  <tbody id="scorelist_body">

                  </tbody>
                </table>
                <div id="page_bar"></div>
              </dd>
            </dl>

          </div>

        </div>
        <div id="noExam" style="color:#FF0000;display: none;">本班级暂无考试信息！</div>
        <!--/.charts-col-->

      </div>

    </div>
    <!--/.tab-col-->

  </div>
  <!--/.col-right-->

</div>
<!--/#content-->



<div style="clear: both"></div>
<%@ include file="../common_new/foot.jsp"%>



<script id="exam_classsubjecttmpl" type="text/template">
  {{ for(var prop in it) { }}
  <option id="{{= prop}}">{{= prop }}</option>
  {{ } }}
</script>

<script id="exam_selectexamtmpl" type="text/template">
  {{ for (var i = 0, l = it.examList.length; i < l; i++) { }}
  <option id = "{{= it.examList[i].examId }}">{{= it.examList[i].examName }}</option>
  {{}}}
</script>

<script id="exam_scorelisttmpl" type="text/template">
  {{ for (var i = 0, l = it.length; i < l; i++) { }}
  <tr>
    <td>{{= it[i].studentName}}</td>
    {{?it[i].score != null}}
    <td>{{= it[i].score}}</td>
    {{??}}
    <td>--</td>
    {{?}}
    <td>{{= it[i].classRanking}}</td>
    <td>{{= it[i].gradeRanking}}</td>
  </tr>
  {{}}}
</script>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('chengji', function(chengJi) {
    chengJi.init();
    chengJi.initTeacherMainPageData('${sessionValue.id}');
  });
</script>
<script>
  function splitPage(page,pageSize){
    seajs.use('chengji', function(chengJi) {
      chengJi.splitPage(page,pageSize);
    });
  }
</script>
</body>
</html>
