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
  <title>复兰科技--用户查询</title>
  <meta charset="utf-8"/>
  <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css">
  <link rel="stylesheet" type="text/css" href="/static/js/select2/select2.css">
  <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
  <link rel="stylesheet" type="text/css" href="/static/css/manage.css">
  <link rel="stylesheet" type="text/css" href="/static/css/yunying/teacherBlance.css">
  <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css"/>
  <script type="text/javascript" src="/static/js/jquery.min.js"></script>
  <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"></script>
  <script type="text/javascript" src="/static/js/select2/select2.min.js"></script>
  <script type="text/javascript" src="/static/js/shareforflipped.js"></script>
  <script type="text/javascript" src="/static/js/sharedpart.js"></script>
  <script type="text/javascript" src="/static/js/WdatePicker.js"></script>
  <%--<script type="text/javascript" src="/static/js/manage/manage-news.js"></script>--%>
  <script type="text/javascript" src="/static_new/js/modules/yunying/0.1.0/teacherBlance.js"></script>
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
      <dl class="mews_column_dl">
        <dt>
          <a class="news_top_I news_top_H">用户查询</a>
          <a href="/yunying/withdrawcash.do?version=85&tag=5" class="news_top_II">提现列表</a>
          <a href="/yunying/theme.do" class="news_top_III">话题</a>
        </dt>
        <dd>
          <span>用户列表</span>
          <%--<button class="new_button_I">&nbsp;&nbsp;&nbsp;&nbsp;删除</button>--%>
          <%--<button class="new_button_II">+添加栏目</button>--%>
          <form action="/yunying/uploadExp.do" enctype="multipart/form-data" method="post">
          <input type="file" name="file1">
            <input type="submit" value="提交">
          </form>
        </dd>
        <dd>
          <span>用户名:</span>
          <input  type="text" style="margin-left: 25px;height: 30px;width: 150px; border: 1px solid rgb(103, 97, 97);" id="name">
          <span style="padding-left: 20px;">禁言:</span><select id="jysel" style="width: 100px;font-size: 15px;padding-left: 10px;">
            <option value="2">全部</option>
            <option value="0">未禁言</option>
            <option value="1">禁言</option>
          </select>
          <button class="new_button_I">检索</button>
        </dd>
        <dd class="news_manage_info_top">
          <%--<input class="newsColum_chooseAll" type="checkbox" name="newsColumnTitle">--%>
          <em>用户名</em>
          <em>班级</em>
          <em>学校</em>
          <em>电话</em>
          <em>邮箱</em>
          <em>经验值</em>
          <em>余额</em>
          <em>操作</em>
        </dd>
      </dl>
    </div>
    <div class="page-paginator">
      <span class="first-page">首页</span>
                        <span class="page-index">
                            <span class="active">1</span>
                            <span>2</span>
                            <span>3</span>
                            <span>4</span>
                            <span>5</span>
                            <i>···</i>
                        </span>
      <span class="last-page">尾页</span>
    </div>
  </div>

</div>


<!--===================================弹出框===========================================-->
<div class="news_manage_bf"></div><!--====================黑色背景===================-->
<div class="news_manage_cg">
  <dl>
    <dt>&nbsp;&nbsp;&nbsp;&nbsp;增加减少经验值<span class="news_close">x</span></dt>
    <dd>
      <input id="userid" hidden="hidden"/>
      <span>姓名</span><input id="columnName" type="text" disabled>
    </dd>
    <dd>
      <span style="color: red;margin-right: 10px;">*</span><span>经验值</span><input id="columnDir" type="text">
    </dd>
    <dd>
      <span style="color: red;margin-right: 10px;">*</span><span>备注</span><input id="connent" type="text">
    </dd>
    <dd>
      <button class="edit-commit-btn">提交</button>
      <button class="news_close">取消</button>
    </dd>
  </dl>
</div>
<div class="news_manage_cg2">
  <dl>
    <dt>&nbsp;&nbsp;&nbsp;&nbsp;增加减少余额<span class="news_close">x</span></dt>
    <dd>
      <input id="userid2" hidden="hidden"/>
      <span>姓名</span><input id="columnName2" type="text" disabled>
    </dd>
    <dd>
      <span style="color: red;margin-right: 10px;">*</span><span>金额</span><input id="columnDir2" type="text">
    </dd>
    <dd>
      <span style="color: red;margin-right: 10px;">*</span><span>备注</span><input id="connent2" type="text">
    </dd>
    <dd>
      <button class="edit-commit-btn">提交</button>
      <button class="news_close">取消</button>
    </dd>
  </dl>
</div>



<%--<%@ include file="../common_new/foot.jsp" %>--%>
</body>
</html>
