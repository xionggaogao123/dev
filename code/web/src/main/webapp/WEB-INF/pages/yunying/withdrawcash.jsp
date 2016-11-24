<%--
  Created by IntelliJ IDEA.
  User: fulaan
  Date: 15-7-2
  Time: 下午8:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>复兰科技--提现列表</title>
  <meta charset="utf-8"/>
  <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css">
  <link rel="stylesheet" type="text/css" href="/static/js/select2/select2.css">
  <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
  <link rel="stylesheet" type="text/css" href="/static/css/manage.css">
  <link rel="stylesheet" type="text/css" href="/static/css/yunying/withdrawcash.css">
  <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css"/>

  <script type="text/javascript" src="/static/js/jquery.min.js"></script>
  <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"></script>
  <script type="text/javascript" src="/static/js/select2/select2.min.js"></script>
  <script type="text/javascript" src="/static/js/shareforflipped.js"></script>
  <script type="text/javascript" src="/static/js/sharedpart.js"></script>
  <script type="text/javascript" src="/static/js/WdatePicker.js"></script>
  <script type="text/javascript" src="/static_new/js/modules/yunying/0.1.0/withdrawcash.js"></script>
  <script type="text/javascript" src="/static/js/ajaxfileupload.js"></script>
  <script type="text/javascript" src="/static/plugins/jquery-upload/vendor/jquery.ui.widget.js"></script>
  <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.iframe-transport.js"></script>
  <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.fileupload.js"></script>
  <script src="http://www.w3cschool.cc/try/angularjs/1.2.5/angular.min.js"></script>
  <script type="text/javascript" src="/static/plugins/ui-bootstrap-tpls-0.11.2.min.js"></script>


  <script type="application/javascript">


  </script>
  <style type="text/css">
    #ueditor_0 {
      width: 412px !important;
      height: 200px !important;
    }
  </style>

</head>
<body>
<!--==========================stat-引入头部=============================-->

<%@ include file="../common_new/head.jsp" %>
<input type="hidden" id="hiddenValue" name="hiddenValue" value="${param.tag}"/>
<!--==========================end-引入头部=============================-->

<!--=====================================内容管理================================================-->

<div class="news_manage_bf"></div>

<div class="manange_cg">


  <!--==========================stat-引入左边导航=============================-->
  <%--<%@ include file="../common_new/col-left.jsp" %>--%>
  <!--==========================end-左边导航=============================-->


  <!--==========================stat-引入头部广告=============================-->
  <%@ include file="../common/right.jsp" %>
  <!--==========================end-头部广告=============================-->
  <div class="content_manage_bg">
    <%--<div class="news_manage_top">--%>
      <%--<span>新闻管理&nbsp;&nbsp;></span><span>&nbsp;&nbsp;内容管理</span>--%>
    <%--</div>--%>
    <div class="news_manage_info">
      <dl>
        <dt>
          <a href="/yunying/teacherBlance.do" class="news_top_I">用户查询</a>
          <a class="news_top_II news_top_H">提现列表</a>
          <a href="/yunying/theme.do" class="news_top_III">话题</a>
        </dt>
        <dd style="margin-top: 10px;">
          用户名：
          <input type="text" style="width: 140px;height: 30px;border: 1px solid #a9a9a9;outline: none;margin-right: 4px;" id="tname">
          开始时间：
          <input type="text" id="bTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" style="width: 140px;height: 30px;border: 1px solid #a9a9a9;outline: none;margin-right: 4px;">
          结束时间：<input type="text" id="eTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" style="width: 140px;height: 30px;border: 1px solid #a9a9a9;outline: none;margin-right: 4px;">
          <button id="seachindex" style="position: absolute;margin-left: 20px;">检索</button>
          <button id="download" style="position: absolute;margin-left: 125px;">下载</button>
        </dd>
        <%--<dd class="content_manage_br">--%>
          <%--<div class="content_manage_left">筛选</div>--%>
          <%--<div class="content_manage_right">--%>
            <%--<span id="newsColumnSearch">栏目</span>--%>
            <%--<select class="newsColumnSearchItem">--%>

              <%--<option>2</option>--%>
              <%--<option>3</option>--%>
            <%--</select>--%>
            <%--<span>标题</span>--%>
            <%--<input type="text" id="newsTitleSearch" name="">--%>
            <%--<button onclick="searchNews()">搜索</button>--%>
          <%--</div>--%>
        <%--</dd>--%>
        <dd class="content_manage_info_top">
          <%--<input class="newsTitle" type="checkbox" name="newsTitle">--%>

          <em style="width: 140px;">用户名</em>
            <em style="width: 200px;">学校</em>
          <em style="width: 70px;">金额</em>
          <em style="width: 220px;">账号</em>
            <em style="width: 150px;">账号姓名</em>
            <em style="width: 180px;">身份证</em>
            <em style="width: 100px;">手机号</em>
          <em style="width: 270px;">开户行</em>
          <em style="width: 160px;">时间</em>
           <em style="width: 100px;">提现状态</em>
           <em style="width: 160px;">备注</em>
        </dd>
      </dl>
    </div>
      <div class="page-paginator" style="margin: 90px auto 20px auto;">
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



  <script type="text/javascript" src="/static/plugins/ueditor/ueditor.config.js"></script>
  <script type="text/javascript" src="/static/plugins/ueditor/ueditor.all.js"></script>
  <script type="text/javascript" src="/static/plugins/ueditor/ueditor.parse.js"></script>
  <script type="text/javascript" src="/static/plugins/ueditor/lang/zh-cn/zh-cn.js"></script>
  <style type="text/css">
    .edui-default .edui-editor {
      width: 414px !important;
    }

    .edui-default .edui-editor-iframeholder {
      width: 414px !important;
    }
  </style>
  <script type="application/javascript">
    var ue = UE.getEditor('ueditor',{autoHeightEnabled :false});
  </script>


</div>
<%@ include file="../common_new/foot.jsp" %>

</body>
</html>
