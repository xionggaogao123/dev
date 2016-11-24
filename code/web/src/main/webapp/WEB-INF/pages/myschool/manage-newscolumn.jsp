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
    <title>复兰科技--管理新闻</title>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="/static/js/select2/select2.css">
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/manage.css">
    <link rel="stylesheet" type="text/css" href="/static/css/manage/news_manage.css">
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css"/>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"></script>
    <script type="text/javascript" src="/static/js/select2/select2.min.js"></script>
    <script type="text/javascript" src="/static/js/shareforflipped.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/js/WdatePicker.js"></script>
    <%--<script type="text/javascript" src="/static/js/manage/manage-news.js"></script>--%>
    <script type="text/javascript" src="/static/js/manage/manage-newscolumn.js"></script>
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
    <%@ include file="../common_new/col-left.jsp" %>
    <!--==========================end-左边导航=============================-->


<!--==========================stat-引入头部广告=============================-->
    <%@ include file="../common/right.jsp" %>
<!--==========================end-头部广告=============================-->
<!--=======================================栏目管理====================================================-->

    <div class="news_manage_bg  content_manage_bg">
        <div class="news_manage_top">
            <span>新闻管理&nbsp;&nbsp;></span><span>&nbsp;&nbsp;栏目管理</span>
        </div>
        <div class="news_manage_info">
            <dl class="mews_column_dl">
                <dt>
                    <a class="news_top_I news_top_H">栏目管理</a>
                    <a href="/myschool/managenews?version=85&tag=5" class="news_top_II">内容管理</a>
                </dt>
                <dd>
                    <span>栏目列表</span>
                    <button class="new_button_I">&nbsp;&nbsp;&nbsp;&nbsp;删除</button>
                    <button class="new_button_II">+添加栏目</button>
                </dd>
                <dd class="news_manage_info_top">
                    <input class="newsColum_chooseAll" type="checkbox" name="newsColumnTitle">
                    <em>栏目名称</em>
                    <i>操作</i>
                </dd>
                <%--<dd class="news_manage_info_middle">
                    <input type="checkbox" name="">
                    <em>啊老师的空间发拉克丝江东父老卡就是两地分居啊拉萨快递费就</em>
                    <button>编辑</button>
                    <button class="news_manage_button">删除</button>
                </dd>
                <dd class="news_manage_info_middle">
                    <input type="checkbox" name="">
                    <em>啊老师的空间发拉克丝江东父老卡就是两地分居啊拉萨快递费就</em>
                    <button>编辑</button>
                    <button class="news_manage_button">删除</button>
                </dd>
                <dd class="news_manage_info_middle">
                    <input type="checkbox" name="">
                    <em>啊老师的空间发拉克丝江东父老卡就是两地分居啊拉萨快递费就</em>
                    <button>编辑</button>
                    <button class="news_manage_button">删除</button>
                </dd>
                <dd class="news_manage_info_middle">
                    <input type="checkbox" name="">
                    <em>啊老师的空间发拉克丝江东父老卡就是两地分居啊拉萨快递费就</em>
                    <button>编辑</button>
                    <button class="news_manage_button">删除</button>
                </dd>--%>
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
        <dt>&nbsp;&nbsp;&nbsp;&nbsp;添加栏目<span class="news_close">x</span></dt>
        <dd>
            <span>栏目名称</span><input id="columnName" type="text">
        </dd>
        <dd>
            <span>栏目目录</span><input id="columnDir" type="text">
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
