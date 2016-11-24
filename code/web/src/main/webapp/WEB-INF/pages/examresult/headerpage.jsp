<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2015/9/6
  Time: 10:38
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


    <!--.tab-col-->
    <div class="tab-col">

      <div class="tab-head clearfix">
        <ul class="teachermain-cur">
          <li class="cur"><a href="#">首页</a></li>
          <li><a href="/score/header/semester.do">学期成绩</a></li>
        </ul>
        <%--<a href="/score/teacher/input.do" class="green-btn">成绩录入</a>--%>
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
            <select id="stu_exam">

            </select>
            <select id="stu_subject">

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
              <dd id="cjfbId" class="charts-height"></dd>
            </dl>

            <dl id="dlcjtj">
              <dt><em id="num3">3</em>成绩统计</dt>
              <dd class="clearfix">

                <div class="huan-charts">
                  <span id="pjfId"></span>
                  <ul class="clearfix">
                    <li>班级均分<em id="bjjf"></em></li>
                    <li id="linjjf">年级均分<em id="njjf"></em></li>
                  </ul>
                </div>

                <div class="huan-charts">
                  <span id="bhgId"></span>
                  <ul class="clearfix">
                    <li>班合格率<em id="bjhgl"></em></li>
                    <li>班优秀率<em id="bjyxl"></em></li>
                  </ul>
                </div>

                <div class="huan-charts" id="grade">
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
                <h4>成绩表</h4>
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
  seajs.use('/static_new/js/modules/chengji/0.1.0/headermain', function(headermain){
    headermain.init();
  });
</script>
<script>
  function splitPage(page,pageSize){
    seajs.use('/static_new/js/modules/chengji/0.1.0/headermain', function(headermain) {
      headermain.splitPage(page,pageSize);
    });
  }
</script>
</body>
</html>
