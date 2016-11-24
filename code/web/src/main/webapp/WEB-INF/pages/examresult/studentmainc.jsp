<%--
  Created by IntelliJ IDEA.
  User: qinbo
  Date: 15/6/22
  Time: 下午3:41
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
          <li class="cur"><a href="#">首页</a></li>
          <li><a href="/score/student/semester.do?a=10000">学期成绩</a></li>
          <li><a href="/score/student/history.do?a=10000">历史成绩</a></li>
        </ul>
      </div>

      <div class="tab-main">

        <!--.tiaojian-col-->
        <div class="tiaojian-col clearfix">
          <div class="select-style">
            <select id="stu_class">

            </select>
            <select id="stu_exam">

            </select>
            <select id="stu_subject">

            </select>
          </div>
        </div>

        <!--/.tiaojian-col-->

        <!--.charts-col-->
        <!--/.charts-col-->
        <div class="charts-col">


          <div class="student_list_dl" >
            <dl style="background-position: 103px 0">
              <dt>
              <h3 id="subject_exam"></h3>
              </dt>
              <dd>恭喜你在本次考试中取得了很不错的成绩，</dd>
              <dd>打败了班级<em id="dbbj"></em>的同学</dd>
              <dd>打败了年级<em id="dbnj"></em>的同学，继续加油吧~</dd>
            </dl>
          </div>
          <dl>
            <dt></dt>
            <dd class="student_clearfix">

              <div class="huan-charts" style="width: 460px;">
                <span id="pjfId" style="width: 350px;"></span>
                <ul class="student_clearfix">
                  <li>我的考分<em id="myScore"></em></li>
                  <li>班级均分<em id="classScore"></em></li>
                  <li>年级均分<em id="gradeScore"></em></li>
                </ul>
              </div>

              <div class="huan-charts" style="width: 460px;">
                <span id="bhgId" style="width: 350px;"></span>
                <ul class="student_clearfix">
                  <li>班级排名<em id="classRanking"></em></li>
                  <li>年级排名<em id="gradeRanking"></em></li>
                </ul>
              </div>

              <div class="huan-charts" id="threeScore">
                <span id="njhgId"></span>
                <ul class="student_clearfix">
                  <li>语文成绩<em id="chinese"></em></li>
                  <li>数学成绩<em id="math"></em></li>
                  <li>英语成绩<em id="english"></em></li>
                </ul>
              </div>
            </dd>
          </dl>
        </div>
      </div>

    </div>
    <!--/.tab-col-->

  </div>
  <!--/.col-right-->

</div>
<!--/#content-->



<%@ include file="../common_new/foot.jsp" %>

<script id="stu_classtmpl" type="text/template">
  {{ for(var prop in it) { }}
  {{ if(prop != 'code'){ }}
  <option id="{{= prop}}">{{= prop }}</option>
  {{}}}
  {{ } }}
</script>

<script id="stu_examtmpl" type="text/template">
  {{ for(var pro in it) { }}
  {{?pro!="classId"}}
  <option id="{{= pro}}">{{= pro }}</option>
  {{?}}
  {{ } }}
</script>

<script id="stu_subjecttmpl" type="text/template">
  {{for (var prop in it) { }}
  <option id = "{{= it[prop].id }}">{{= it[prop].name }}</option>
  {{ }}}
</script>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('/static_new/js/modules/chengji/0.1.0/student_main' ,function(stuMain){
    stuMain.initStuMainPage();
  });
</script>
</body>
</html>
