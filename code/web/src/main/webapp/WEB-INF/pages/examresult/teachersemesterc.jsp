<%--
  Created by IntelliJ IDEA.
  User: qinbo
  Date: 15/6/21
  Time: 上午12:24
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
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link href="/static_new/css/chengji.css?v=2015041602" rel="stylesheet" />

  <link href="/static_new/css/examine.css?v=2015051901" rel="stylesheet" />
  <link href="/static/css/homepage.css" type="text/css" rel="stylesheet">
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>
<body>

<!--=================================引入头部============================================-->
<%@ include file="../common_new/head-cloud.jsp" %>
<div id="content" class="clearfix">
  <%--<%@ include file="../common_new/col-left.jsp" %>--%>
  <!--.col-right-->
  <!--广告-->
  <%--<c:choose>--%>
    <%--<c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">--%>
      <%--<jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>--%>
    <%--</c:when>--%>
    <%--<c:otherwise>--%>
      <%--<jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>--%>
    <%--</c:otherwise>--%>
  <%--</c:choose>--%>
  <!--广告-->
  <div class="col-right" style="width: 1000px;">


    <div class="tab-col">

      <div class="tab-head clearfix">
        <ul>
          <li><a href="/score/teacher.do?a=10000">首页</a></li>
          <li class="cur"><a href="#">学期成绩</a></li>
        </ul>
        <a href="/score/teacher/input.do?a=10000" class="green-btn">成绩录入</a>
      </div>

      <div class="tab-main">



        <!--.charts-col-->
        <div class="charts-col">

          <h3 id="semesterTitle"></h3>

          <div class="charts-list" >

            <dl>
              <dt><em>1</em>班级成绩对比</dt>
              <dd id="cjdbId" class="charts-height" style="width: 960px;height: 260px;"></dd>
              <dd>
                <ul class="examine_col">
                  <li>平时成绩<em id="pscj"></em></li>
                  <li>期中成绩<em id="qzcj"></em></li>
                  <li>期末成绩<em id="qmcj"></em></li>
                </ul>
              </dd>
            </dl>



            <dl>
              <dt><em>2</em>成绩统计</dt>
              <dd class="clearfix">

                <div class="huan-charts" style="width: 300px;">
                  <span id="pscjId" style="width: 290px;"></span>
                  <ul class="clearfix" style="width: 290px;">
                    <li>班合格率<em id="pshgl"><i>%</i></em></li>
                    <li>班优秀率<em id="psyxl"><i>%</i></em></li>
                  </ul>
                </div>

                <div class="huan-charts" style="width: 300px;">
                  <span id="qzcjId" style="width: 290px;"></span>
                  <ul class="clearfix" style="width: 290px;">
                    <li>班合格率<em id="qzhgl"><i>%</i></em></li>
                    <li>班优秀率<em id="qzyxl"><i>%</i></em></li>
                  </ul>
                </div>

                <div class="huan-charts" style="width: 300px;">
                  <span id="qmcjId" style="width: 290px;"></span>
                  <ul class="clearfix" style="width: 290px;">
                    <li>班合格率<em id="qmhgl"><i>%</i></em></li>
                    <li>班优秀率<em id="qmyxl"><i>%</i></em></li>
                  </ul>
                </div>
              </dd>
            </dl>

            <dl>
              <dt><em>3</em>学生列表</dt>
              <dd class="std-list">
                <h4 id="stuListTitle" style="width: 976px;"></h4>
                <table width="100%">
                  <thead>
                  <th class="25%"><em>名字</em></th>
                  <th class="25%"><em id="usualScore">平时成绩<i></i></em></th>
                  <th class="25%"><em id="midScore">期中成绩<i></i></em></th>
                  <th><em id="finalScore">期末成绩<i></i></em></th>
                  </thead>
                  <tbody id="scorelist_body">
                  <!-- <tr>
                       <td>张同学</td>
                       <td>97</td>
                       <td>95</td>
                       <td>98</td>
                   </tr>-->
                  </tbody>
                </table>
                <div id="page_bar"></div>
              </dd>
            </dl>

          </div>

        </div>
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

<script id="subjectScoreListTmpl" type="text/template">
  {{ for (var i = 0, l = it.length; i < l; i++) { }}
  <tr>
    <td>{{= it[i].studentName}}</td>
    {{?it[i].usualScore}}
    <td>{{= it[i].usualScore}}</td>
    {{??}}
    <td>{{="--"}}</td>
    {{?}}
    {{?it[i].midtermScore}}
    <td>{{= it[i].midtermScore}}</td>
    {{??}}
    <td>{{="--"}}</td>
    {{?}}
    {{?it[i].finalScore}}
    <td>{{= it[i].finalScore}}</td>
    {{??}}
    <td>{{="--"}}</td>
    {{?}}
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
    chengJi.getTeacherSemesterPageData();
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
