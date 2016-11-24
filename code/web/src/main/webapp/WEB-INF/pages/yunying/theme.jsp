<%--
  Created by IntelliJ IDEA.
  User: fulaan
  Date: 15-7-2
  Time: 下午5:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>复兰科技--话题</title>
  <meta charset="utf-8"/>
  <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
  <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css">
  <link rel="stylesheet" type="text/css" href="/static/js/select2/select2.css">
  <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
  <link rel="stylesheet" type="text/css" href="/static/css/manage.css">
  <link rel="stylesheet" type="text/css" href="/static/css/yunying/theme.css">
  <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css"/>
  <script type="text/javascript" src="/static/js/jquery.min.js"></script>
  <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"></script>
  <script type="text/javascript" src="/static/js/select2/select2.min.js"></script>
  <script type="text/javascript" src="/static/js/shareforflipped.js"></script>
  <script type="text/javascript" src="/static/js/sharedpart.js"></script>
  <script type="text/javascript" src="/static/js/WdatePicker.js"></script>
  <%--<script type="text/javascript" src="/static/js/manage/manage-news.js"></script>--%>
  <script type="text/javascript" src="/static_new/js/modules/yunying/0.1.0/theme.js"></script>
  <script type="text/javascript">

  </script>
</head>
<body>
<!--==========================stat-引入头部=============================-->

<%@ include file="../common_new/head.jsp" %>
<input type="hidden" id="hiddenValue" name="hiddenValue" value="${param.tag}"/>
<!--==========================end-引入头部=============================-->

<div class="manange_ck">


  <!--==========================stat-引入左边导航=============================-->
  <%--<%@ include file="../common_new/col-left.jsp" %>--%>
  <!--==========================end-左边导航=============================-->


  <!--==========================stat-引入头部广告=============================-->
  <%@ include file="../common/right.jsp" %>
  <!--==========================end-头部广告=============================-->
  <!--=======================================栏目管理====================================================-->

  <div class="news_manage_bg  content_manage_bg">
    <%--<div class="news_manage_top">--%>
    <%--<span>新闻管理&nbsp;&nbsp;></span><span>&nbsp;&nbsp;栏目管理</span>--%>
    <%--</div>--%>
    <div class="news_manage_info">
      <button style="margin-left: 450px;margin-top: 5px;" class="list-edit">新建话题</button>
      <dl class="mews_column_dl" style="margin-top: 40px;">
        <dt>
          <a href="/yunying/teacherBlance.do" class="news_top_I">用户查询</a>
          <a href="/yunying/withdrawcash.do?version=85&tag=5" class="news_top_II">提现列表</a>
          <a class="news_top_III news_top_H">话题</a>
        </dt>
        <dd class="news_manage_info_top">
          <%--<input class="newsColum_chooseAll" type="checkbox" name="newsColumnTitle">--%>
          <em>话题</em>
          <em>操作</em>
        </dd>
      </dl>
    </div>
  </div>

</div>


<!--===================================弹出框===========================================-->
<div class="news_manage_bf"></div><!--====================黑色背景===================-->
<div class="news_manage_cg">
  <dl>
    <dt>&nbsp;&nbsp;&nbsp;&nbsp;新建话题<span class="news_close">x</span></dt>
    <dd>
      <input id="userid" hidden="hidden"/>
      <span>话题</span><input id="columnName" type="text">
    </dd>
    <dd>
      <button class="edit-commit-btn">提交</button>
      <button class="news_close">取消</button>
    </dd>
  </dl>
</div>



<%@ include file="../common_new/foot.jsp" %>
</body>
</html>
