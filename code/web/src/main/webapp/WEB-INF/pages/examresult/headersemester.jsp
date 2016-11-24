<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2015/9/7
  Time: 9:51
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

    <!--.banner-info-->
    <%--<img src="http://placehold.it/770x100" class="banner-info" />--%>
    <!--/.banner-info-->

    <!--.tab-col-->
    <div class="tab-col">

      <div class="tab-head clearfix">
        <ul>
          <li><a href="/score/header.do">首页</a></li>
          <li class="cur"><a href="#">学期成绩</a></li>
        </ul>
        <%--<a href="#" class="green-btn">新建考试</a>--%>
      </div>

      <div class="tab-main">

        <!--.tiaojian-col-->
        <div class="tiaojian-col clearfix">
          <div class="select-style" >
            <select id="grade_list">
              <%--<option>全部</option>--%>
              <%--<option>一年级</option>--%>
              <%--<option>二年级</option>--%>
              <%--<option>三年级</option>--%>
            </select>
            <select id="stu_class">
              <%--<option>全部</option>--%>
              <%--<option>一班</option>--%>
              <%--<option>二班</option>--%>
            </select>
          </div>

          <%--<div class="tianjian-btn">--%>
            <%--<ul>--%>
              <%--<li class="cur">实际分</li>--%>
              <%--<li>百分制</li>--%>
            <%--</ul>--%>
            <%--<select>--%>
              <%--<option>导出PDF</option>--%>
            <%--</select>--%>
          <%--</div>--%>

        </div>
        <!--/.tiaojian-col-->

        <!--.charts-col-->
        <div class="charts-col">

          <h3 id="title"></h3>

          <div class="charts-list" >

            <dl>
              <dt><em>1</em>班级成绩对比</dt>
              <dd class="semester_grades_hd">
                <span class="semester_grades_left"></span>
                <ul>
                  <div id="subjectList">
                    <%--<li class="semester_grades_F">--%>
                      <%--语文--%>
                      <%--<span class="semester_grades_SJ"></span>--%>
                    <%--</li>--%>
                    <%--<li>数学</li>--%>
                    <%--<li>英语</li>--%>
                  </div>
                </ul>
                <span class="semester_grades_right"></span>
              </dd>
              <dd id="cjdbId" class="charts-height"></dd>
            </dl>

            <dl>
              <dt></dt>
              <dd class="std-list">
                <h4 id="scoreDTOListTitle"></h4>
                <table width="100%">
                  <thead>
                  <th class="25%"><em>学科</em></th>
                  <th class="25%"><em>平时成绩</em></th>
                  <th class="25%"><em>期中成绩</em></th>
                  <th><em>期末成绩</em></th>
                  </thead>
                  <tbody id="scoreDTOList">
                  <%--<tr>--%>
                    <%--<td>语文</td>--%>
                    <%--<td>75</td>--%>
                    <%--<td>82</td>--%>
                    <%--<td>80</td>--%>
                  <%--</tr>--%>
                  </tbody>
                </table>
              </dd>
            </dl>



            <dl>
              <dt><em>2</em>成绩统计</dt>
              <dd class="clearfix">

                <div class="huan-charts">
                  <span id="pjfId"></span>
                  <ul class="clearfix">
                    <li>班合格率<em id="pshgl"></em></li>
                    <li>班优秀率<em id="psyxl"></em></li>
                  </ul>
                </div>

                <div class="huan-charts">
                  <span id="bhgId"></span>
                  <ul class="clearfix">
                    <li>班合格率<em id="qzhgl"></em></li>
                    <li>班优秀率<em id="qzyxl"></em></li>
                  </ul>
                </div>

                <div class="huan-charts">
                  <span id="njhgId"></span>
                  <ul class="clearfix">
                    <li>班合格率<em id="qmhgl"></em></li>
                    <li>班优秀率<em id="qmyxl"></em></li>
                  </ul>
                </div>
              </dd>
            </dl>


            <dl>
              <dt></dt>
              <dd class="std-list">
                <h4 id="subjectRateDTOListTitle"></h4>
                <table width="100%">
                  <thead>
                  <th class="25%"><em>名字</em></th>
                  <th class="25%"><em>平时成绩<br>合格率/优秀率</em></th>
                  <th class="25%"><em>期中成绩<br>合格率/优秀率</em></th>
                  <th><em>期末成绩<br>合格率/优秀率</em></th>
                  </thead>
                  <tbody id="subjectRateDTOList">
                  <%--<tr>--%>
                    <%--<td>语文</td>--%>
                    <%--<td>75%/78%</td>--%>
                    <%--<td>75%/78%</td>--%>
                    <%--<td>75%/78%</td>--%>
                  <%--</tr>--%>
                  </tbody>
                </table>
              </dd>
            </dl>

            <dl>
              <dt><em>3</em>学生列表</dt>
              <dd class="std-list">
                <h4 id="stuListTitle"></h4>
                <table width="100%">
                  <thead>
                  <th class="25%"><em>名字</em></th>
                  <th class="25%"><em id="usualScore">平时成绩<i></i></em></th>
                  <th class="25%"><em id="midScore">期中成绩<i></i></em></th>
                  <th><em id="finalScore">期末成绩<i></i></em></th>
                  </thead>
                  <tbody id="scorelist_body">
                  <%--<tr>--%>
                    <%--<td>张三</td>--%>
                    <%--<td>75</td>--%>
                    <%--<td>82</td>--%>
                    <%--<td>80</td>--%>
                  <%--</tr>--%>
                  </tbody>
                </table>
                <div id="page_bar"></div>
              </dd>
            </dl>

          </div>

        </div>
        <div id="noExam" style="color:#FF0000;display: none;">暂无考试信息！</div>
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



<script id="grade_list_tmpl" type="text/template">
  {{ for(var i in it) { }}
  <option gradeId="{{= it[i].id}}" name="grade">{{= it[i].name}}</option>
  {{ } }}
</script>

<script id="stu_classtmpl" type="text/template">
  {{ for(var i in it) { }}
  <option id="{{= it[i].id}}">{{= it[i].className }}</option>
  {{ } }}
</script>

<script id="subjectList_tmpl" type="text/template">
  {{for (var i in it) { }}
  <li subjectId="{{= it[i].id}}" class="subject">{{=it[i].name}}</li>
  {{ }}}
</script>

<script id="scoreDTOList_tmpl" type="text/template">
  {{ for (var i in it) { }}
  <tr>
    <td>{{=it[i].subjectName}}</td>
    <td>{{=it[i].usualScore}}</td>
    <td>{{=it[i].midtermScore}}</td>
    <td>{{=it[i].finalScore}}</td>
  </tr>
  {{}}}
</script>

<script id="subjectRateDTOList_tmpl" type="text/template">
  {{ for (var i in it) { }}
  <tr>
    <td>{{=it[i].subjectName}}</td>
    <td>{{=it[i].avecpr}}%/{{=it[i].avecer}}%</td>
    <td>{{=it[i].midcpr}}%/{{=it[i].midcer}}%</td>
    <td>{{=it[i].endcpr}}%/{{=it[i].endcer}}%</td>
  </tr>
  {{}}}
</script>

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
  seajs.use('/static_new/js/modules/chengji/0.1.0/headersemester', function(headersemester){
    headersemester.init();
  });
</script>
<script>
  function splitPage(page,pageSize){
    seajs.use('/static_new/js/modules/chengji/0.1.0/headersemester', function(headersemester) {
      headersemester.splitPage(page,pageSize);
    });
  }
</script>
</body>
</html>

