<%--
  Created by IntelliJ IDEA.
  User: qinbo
  Date: 15/6/22
  Time: 下午3:42
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
  <link href="/static_new/css/examine.css?v=2015041602" rel="stylesheet" />
  <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
</head>
<body>

<%@ include file="../common_new/head-cloud.jsp" %>
<div id="content" class="clearfix">
  <%--<%@ include file="../common_new/col-left.jsp" %>--%>
  <!--.col-right-->
  <div class="col-right" style="width: 1000px;">

    <%--<!--.banner-info-->--%>
    <%--<img src="http://placehold.it/770x100" class="banner-info" />--%>
    <%--<!--/.banner-info-->--%>

    <!--.tab-col-->
    <div class="tab-col">

      <div class="tab-head clearfix">
        <ul>
          <li ><a href="/score/student.do?a=10000">首页</a></li>
          <li ><a href="/score/student/semester.do?a=10000">学期成绩</a></li>
          <li class="cur"><a href="#">历史成绩</a></li>
        </ul>
      </div>

      <div class="tab-main">

        <!--.tiaojian-col-->
        <div class="tiaojian-col clearfix">

          <div class="select-style">
            <select id="schoolYearSelect">

            </select>
            <select id="subjectSelect">

            </select>
          </div>



        </div>

        <!--.charts-col-->
        <div class="charts-col">

          <div class="charts-list" >

            <dl>
              <dt><em>1</em>考试列表</dt>
              <dd id="cjdbId" class="charts-height" style="width: 960px;height: 260px;"></dd>
            </dl>

            <dl>
              <dt><em>2</em>成绩表</dt>
              <dd class="std-list">
                <h4 id="cjbTitle" style="width: 976px;"></h4>
                <table width="100%">
                  <thead>
                  <th class="25%"><em>考试名称</em></th>
                  <th class="25%"><em id="score">考试分数<i></i></em></th>
                  <th class="25%"><em id="classScore">班级均分<i></i></em></th>
                  <th><em id="classRanking">班级排名<i></i></em></th>
                  </thead>
                  <tbody id="examList">




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



<%@ include file="../common_new/foot.jsp" %>
<script id="schoolYearSelectTmpl" type="text/template">
  {{ for(var index in it) {  }}
  <option>{{=it[index]}}</option>
  {{ } }}
</script>

<script id="subjectSelectTmpl" type="text/template">
  {{ for(var index in it) {  }}
  <option value="{{=it[index].id}}">{{=it[index].name}}</option>
  {{ } }}
</script>

<script id="examListTmpl" type="text/template">
  {{ for(var i=0; i < it.length; i++) {  }}
  <tr>
    <td>{{=it[i].examName}}</td>
    {{? it[i].score != null}}
    <td>{{=it[i].score}}</td>
    {{??}}
    <td>--</td>
    {{?}}
    <td>{{=it[i].classAverageScore}}</td>
    <td>{{=it[i].classRanking}}</td>
  </tr>
  {{ } }}
</script>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('/static_new/js/modules/chengji/0.1.0/student_history', function(stuHistory) {
    stuHistory.initPage();
  });
</script>

<script>
  function splitPage(page, pageSize) {
    seajs.use('/static_new/js/modules/chengji/0.1.0/student_history', function (stuHistory) {
      stuHistory.splitPage(page, pageSize);
    });
  }
</script>
</body>
</html>
