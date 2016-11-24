<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>管理校园-复兰科技 K6KT-快乐课堂</title>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="/static/js/select2/select2.css">
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/manage.css">
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/manage/news.css"/>

    <script type="text/javascript" src="/static/plugins/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" src="/static/plugins/ueditor/ueditor.all.js"></script>
    <script type="text/javascript" src="/static/plugins/ui-bootstrap-tpls-0.11.2.min.js"></script>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"></script>
    <script type="text/javascript" src="/static/js/select2/select2.min.js"></script>
    <script type="text/javascript" src="/static/js/shareforflipped.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/js/WdatePicker.js"></script>
    <script type="text/javascript" src="/static/js/manage.js"></script>
    <script type="text/javascript" src="/static/js/manage/manage-teacher.js"></script>
    <script type="text/javascript" src="/static/js/common/role.js"></script>

    <script>
        var currentPageID = 5;
    </script>
    <style type="text/css">

        .reset-teacher-password {
            padding: 7px 0px;
            background-color: #FF5555;
            margin-left: 150px;
            width: 130px;
            font-size: 14px;
            color: #fff;
            font-weight: bold;
            text-align: center;
            border-radius: 3px;
            cursor: pointer;
        }

        .Wdate {
            border: #999 1px solid;
            height: 20px;
            background: #fff url(/img/datePicker.gif) no-repeat right;
        }

        .Wdate::-ms-clear {
            display: none;
        }

        .WdateFmtErr {
            font-weight: bold;
            color: red;
        }
    </style>
</head>
<body ng-app="k6kt.manage">
<%@ include file="../common_new/head.jsp" %>
<input type="hidden" id="hiddenValue" name="hiddenValue" value="${param.tag}"/>

<div id="content_main_container">
    <div id="content_main">
        <!-- 左侧导航-->
        <%@ include file="../common_new/col-left.jsp" %>
        <!-- left end -->
        <!--广告-->
        <%@ include file="../common/right.jsp" %>
        <!--/广告-->

        <!-- right start-->
        <div class="modal-bg"></div>
        <div id="right-container" style="float: none">
            <div class="main-container">
                <div class="manage-left-container" style="display: none;">
                    <%--<img class="manager-img" src="${sessionValue.avatar}">--%>

                    <div class="manager-name">${sessionValue.userName}</div>
                    <ul class="manage-select">
                        <li id="tag2" data-target="teacher">
                            <div>管理老师</div>
                        </li>

                    </ul>
                </div>
                <div class="manage-right-container">


                    <!-- 老师列表 -->
                    <div class="list-container teacher-list" belong="teacher">
                        <div class="list-title" style="line-height:28px;overflow: hidden;"><img src="/img/K6KT/list_1.png"
                                                                               style="margin-right: 5px;float:left;">老师列表
                            <input class="retrieval-search-form" id="teacher-name-search" type="text"
                                   placeholder="请输入老师名字" onkeyup="getTeacherList(1)"
                                   style="border: 1px solid #d4d4d4; height: 30px;">

                            <div id="submit-teacher-search"
                                 style="position: relative; width: 23px; height: 23px; top: -30px; left: 220px;left: 215px\9; cursor: pointer; overflow: hidden;"
                                 onclick="getTeacherList(1)">
                                <img src="/img/search.png" style="margin:10px 0 0 10px;">
                            </div>
                        </div>
                        <div class="list-add-teacher"><i class="fa fa-plus"></i> 新建老师</div>
                        <ul class="list-ul">
                        </ul>
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
                    <div class="edit-container">

                        <!-- 编辑老师 -->
                        <div class="edit-info-container edit-teacher">
                            <div class="edit-title-bar">
                                <div>编辑老师</div>
                                <i class="fa fa-times close-modal"></i>
                            </div>
                            <div class="edit-form">

                                <div class="edit-input-group">
                                    <span>老师姓名</span>
                                    <input type="text" class="edit-teacher-name">
                                </div>
                                <div class="edit-input-group">
                                    <span>教职工号</span>
                                    <input type="text" class="edit-teacher-number">
                                </div>
                                <div class="edit-input-group">
                                    <span>权限</span>
                                    <select class="edit-info-select edit-teacher-permission"
                                            onchange="showmange(this.options[this.selectedIndex].text)">
                                        <option value="8">校领导</option>
                                        <option value="64">管理员</option>
                                        <option value="2">老师</option>
                                    </select>
                                </div>
                                <div class="user-techuser">
                                </div>
                                <div class="ismanageclass"
                                     style="font-size: 14px;padding-left: 85px;display:none;padding-bottom: 10px;">
                                    <input type="checkbox" id="ismanage"><span
                                        style="padding-left: 20px;">是否设置成管理员</span>
                                </div>
                                <div class="reset-password-form">
                                    <span class="reset-teacher-password">还原老师初始密码</span>
                                    <img src="/img/K6KT/yes.png" style="display:none;">
                                </div>
                            </div>
                        </div>
                        <div class="edit-commit-btn-container">
                            <div class="edit-commit-btn">确定</div>
                        </div>
                    </div>
                </div>

            </div>
            <!-- right end-->
        </div>
</div>
</div>
    <%@ include file="../common_new/foot.jsp" %>

</body>
</html>