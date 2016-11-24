<%--
  Created by IntelliJ IDEA.
  User: qinbo
  Date: 15/6/20
  Time: 下午11:10
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
          <li><a href="/score/teacher.do?a=10000">首页</a></li>
          <li><a href="/score/teacher/semester.do?a=10000">学期成绩</a></li>
        </ul>
        <a href="#" class="green-btn">成绩录入</a>
      </div>

      <div class="tab-main">
        <h2 id="teacherInputTitle" style="text-align:center;line-height: 50px;font-size: 28px;"></h2>
        <!--.tiaojian-col-->
        <div class="tiaojian-col clearfix result_col">

          <div class="result_info">
            <ul>
              <li>
                <img src="/static_new/images/teacher_LI.png">
                <span>考试名称：</span>
                <input class="result_col_I" name="name" id="examName">
              </li>
              <li>
                <img src="/static_new/images/teacher_LI.png">
                <span>考试时间：</span>
                <%--<input class="result_col_II" name="date" id="examDate">--%>
                <%--<div class="edit-input-group">--%>
                <%--<span>选课时间</span>--%>
                <%--<!-- <input type="datetime-local" class="edit-interest-opentime" placeholder="例如：2014/09/01 07:00:00"> -->--%>
                <input class="result_col_II" type="text" style="width:200px;height:32px;" name="date" id="examDate"
                       onfocus="WdatePicker()" readonly
                       value=""/>
                <%--<span style="margin-left: 8px;">开放</span>--%>
                <%--</div>--%>
                <img src="/static_new/images/teacher_LI.png">
                <span>满分：</span>
                <input class="result_col_III" name="fullScore" id="fullScore">
                <img src="/static_new/images/teacher_LI.png">
                <span>及格分：</span>
                <input class="result_col_III" name="failScore" id="failScore">
              </li>
              <li>
                <img  class="result_top" src="/static_new/images/teacher_LI.png">
                <span class="result_top">考试班级：</span>
                <div class="result_S" id="examClassList">

                </div>
              </li>
              <%--<li>--%>
              <%--<img  class="result_top" src="/static_new/images/teacher_LI.png">--%>
              <%--<span class="result_top">考试学科：</span>--%>
              <%--<div class="result_S" id="examSubjectList">--%>

              <%--</div>--%>

              <%--</li>--%>
              <div id="warningMessage" style="color:#FF0000"></div>
              <li>
                <button class="result_button_CJ" id="result_button_CJ">创建</button>
                <button class="result_button_CJ" id="result_button_BJ" style="display: none">编辑</button>
                <%--<button class="result_button_XZ">下载excel模板</button>--%>
              </li>
            </ul>
          </div>

          <div  class="result_bottom">
            <dl >
              <dd class="std-list">
                <table width="100%">
                  <thead>
                  <th class="25%"><em>考试名称</em></th>
                  <th class="25%"><em id="examTime">考试时间<i></i></em></th>
                  <th class="25%"><em id="process">打分进度<i></i></em></th>
                  <th><em>操作</em></th>
                  </thead>
                  <tbody id="existExamList">


                  </tbody>
                </table>
                <div id="page_bar"></div>
              </dd>
            </dl>
          </div>
        </div>
        <!--/.tiaojian-col-->

        <!--.charts-col-->
        <!--/.charts-col-->

      </div>

    </div>
    <!--/.tab-col-->

  </div>
  <!--/.col-right-->

</div>
<!--/#content-->



<%@ include file="../common_new/foot.jsp" %>

<script id="classListTmpl" type="text/template">

  {{ for(var i = 0, l = it.classList.length; i < l; i++) {  }}
  <div>
    <input class="result_col_V" type="checkbox" name="class" value="{{=it.classList[i].id}}">
    <span>{{=it.classList[i].name}}</span>
  </div>
  {{ } }}

</script>

<%--<script id="subjectListTmpl" type="text/template">--%>

<%--{{ for(var i = 0, l = it.subjectList.length; i < l; i++) {  }}--%>
<%--<div>--%>
<%--<input type="radio" name="radio" class="result_col_V"   value="{{=it.subjectList[i].id}}">--%>
<%--<span>{{=it.subjectList[i].name}}</span>--%>
<%--</div>--%>
<%--{{ } }}--%>

<%--</script>--%>

<script id="examListTmpl" type="text/template">
  {{ for(var i = 0, l = it.length; i < l; i++) {  }}
  <tr>
    <td>{{=it[i].examName}}</td>
    <td>{{=it[i].date}}</td>
    <td>{{=it[i].process}}</td>
    <td>
      <button class="result_TB_I inputscore"  examId="{{=it[i].examId}}" examName ="{{=it[i].examName}}" >打分</button>
      <%--<button class="result_TB_I">导入</button>--%>
      <button class="result_TB_I edit" examId="{{=it[i].examId}}" isGrade="{{=it[i].isGrade}}">编辑</button>
      <button class="result_TB_II delete" examId="{{=it[i].examId}}" isGrade="{{=it[i].isGrade}}">删除</button>
    </td>
  </tr>
  {{ } }}
</script>


<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('chengji', function(chengJi) {
    chengJi.init();
    chengJi.initTeacherInputPage();
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

