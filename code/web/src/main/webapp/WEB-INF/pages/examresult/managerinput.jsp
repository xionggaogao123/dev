<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2015/7/10
  Time: 11:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
  <title></title>
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

<%@ include file="../common_new/head.jsp" %>
<div id="content" class="clearfix">
  <%@ include file="../common_new/col-left.jsp" %>

  <div class="col-right">

    <%--<!--.banner-info-->--%>
    <%--<img src="http://placehold.it/770x100" class="banner-info" />--%>
    <%--<!--/.banner-info-->--%>

    <!--.tab-col-->
    <div class="tab-col">

      <div class="tab-head clearfix">
        <ul>
          <li class="cur"><a href="#">管理考试</a></li>
        </ul>
      </div>

      <div class="tab-main">

        <!--.tiaojian-col-->
        <div class="tiaojian-col clearfix examine_info">
          <div class="examine_top">
            <dl>
              <dt>
                <span>考试类型：</span>
                <select id="type">
                  <option>期中考试</option>
                  <option>期末考试</option>
                  <option>其他</option>
                </select>
              </dt>
            </dl>
            <dl>
              <dt>
                <span>考试名称：</span>
                <input id="examName">
              </dt>
            </dl>
            <dl>
              <dt>
                <span>考试时间：</span>
                <%--<input id="date">--%>
                <input  type="text" id="date"
                       onfocus="WdatePicker({dateFmt:'yyyy/MM/dd HH:mm'})"
                       value=""/>
              </dt>
            </dl>
            <dl>
              <dt>
                <span>学科：</span>
              <ul id="subjectList">

              </ul>
              </dt>
            </dl>
            <dl><div id="warningMessage" style="color:#FF0000"></div></dl>
            <dl>
              <dt>
                <button id="manager_CJ">创建</button>
                <button id="manager_BJ" style="display: none">编辑</button>
                <!--==================保存按钮======================-->
                <!--<button>保存</button>-->
              </dt>
            </dl>
          </div>
          <dl>
            <dt></dt>
            <dd class="examine-list">
              <h4 id="title"></h4>
              <table width="100%">
                <thead>
                <th class="20%"><em>考试名称</em></th>
                <th class="20%"><em>考试时间</em></th>
                <th class="20%"><em>学科</em></th>
                <th class="20%"><em>考试类型</em></th>
                <th><em>操作</em></th>
                </thead>
                <tbody id="examList">

                </tbody>
              </table>
              <div id="page_bar"></div>
            </dd>
          </dl>
        </div>
        <!--/.tiaojian-col-->

        <!--.charts-col-->

      </div>

    </div>
    <!--/.tab-col-->

  </div>
  <!--/.col-right-->

</div>
<!--/#content-->



<%@ include file="../common_new/foot.jsp" %>

<script id="subjectListTmpl" type="text/template">
  {{for (var i = 0, l = it.rows.length; i< l; i++) { }}
  <li>
    <input type="checkbox" class="examine_IN" value="{{= it.rows[i].id }}">
    <i id = "{{= it.rows[i].id }}">{{= it.rows[i].name }}</i>
  </li>
  {{ }}}
</script>

<script id="examListTmpl" type="text/template">
  {{for (var i = 0, l = it.length; i< l; i++) { }}
  <tr>
    <td>{{=it[i].examName}}</td>
    <td>{{=it[i].date}}</td>
    <td>{{=it[i].subjectNameList}}</td>
    <td>{{=it[i].type}}</td>
    <td>
      <button class="examine_bianji" examId="{{=it[i].examId}}">编辑</button>
      <button class="examine_delete" examId="{{=it[i].examId}}">删除</button>
    </td>
  </tr>
  {{ }}}
</script>



<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('/static_new/js/modules/chengji/0.1.0/page_first', function(pagefirst) {
    pagefirst.initPage();
  });
</script>
<script>
  function splitPage(page,pageSize){
    seajs.use('/static_new/js/modules/chengji/0.1.0/page_first', function(pagefirst) {
      pagefirst.splitPage(page,pageSize);
    });
  }
</script>
</body>
</html>