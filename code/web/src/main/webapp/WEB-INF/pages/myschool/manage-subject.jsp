<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>学科管理</title>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="/static/js/select2/select2.css">
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/manage.css">
    <link rel="stylesheet" type="text/css" href="/static/css/increase/news.css">
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css"/>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"></script>
    <script type="text/javascript" src="/static/js/select2/select2.min.js"></script>
    <script type="text/javascript" src="/static/js/shareforflipped.js"></script>
    <%--<script type="text/javascript" src="/static/js/manage.js"></script>--%>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/js/WdatePicker.js"></script>
    <script type="text/javascript" src="/static/js/manage/manage-subject.js"></script>


    <script>
        var currentPageID = 5;
    </script>
</head>
<body ng-app="k6kt.manage" ng-controller="manageController">
<%@ include file="../common_new/head.jsp" %>
<input type="hidden" id="hiddenValue" name="hiddenValue" value="${param.tag}"/>

<div id="content_main_container">
    <div id="content_main">
        <!-- 左侧导航-->
        <%@ include file="../common_new/col-left.jsp" %>
        <!-- left end -->

        <!--.banner-info-->
        <c:choose>
            <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
                <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
            </c:when>
            <c:otherwise>
                <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
            </c:otherwise>
        </c:choose>
        <!--/.banner-info-->

        <!-- right start-->
        <div class="modal-bg"></div>
        <div id="right-container">
            <div class="main-container">
                <div class="manage-left-container" style="display: none;">
                    <%--<img class="manager-img" src="${sessionValue.avatar}">--%>

                    <div class="manager-name">${sessionValue.userName}</div>
                    <ul class="manage-select">
                        <li id="tag1" class="active" data-target="subject">
                            <div>管理学科</div>
                        </li>
                    </ul>
                </div>
                <div class="manage-right-container">


                    <!-- 学科列表 -->
                    <div class="list-container subject-list" belong="subject" style="overflow: hidden;">
                        <div class="list-title" style="line-height:28px;">
                            <img src="/img/K6KT/list_2.png" style="margin-right: 5px;float:left;">学科列表

                        </div>
                        <div class="list-add-subject"><i class="fa fa-plus"></i> 新建科目</div>
                        <ul class="list-ul">
                            <li>
                                <div class="list-content">
                                    <span>语文</span>
                                </div>
                                <i class="fa fa-pencil list-edit"></i>
                                <i class="fa fa-trash-o list-delete"></i>
                            </li>
                        </ul>
                    </div>
                    <div class="edit-container">
                        <!-- 编辑科目 -->
                        <div class="edit-info-container edit-subject">
                            <div class="edit-title-bar">
                                <div>编辑科目</div>
                                <i class="fa fa-times close-modal"></i>
                            </div>
                            <div class="edit-form">
                                <div class="edit-input-group">
                                    <span>科目名称</span>
                                    <input type="text" class="edit-subject-name">
                                </div>
                                <div class="edit-input-group">
                                    <div class="belong-grade">从属年级</div>
                                    <div style="width:340px;" class="edit-subject-selectgrade">
                                        <label class="checkbox-inline">
                                            <input type="checkbox"> 全部
                                        </label>
                                    </div>
                                </div>
                                <div class="edit-input-group">
                                    <span>学科组长</span>
                                    <select class="edit-info-select edit-subject-teacher">
                                    </select>
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


<script type="text/javascript" src="/static/plugins/ueditor/ueditor.config.js"></script>
<script type="text/javascript" src="/static/plugins/ueditor/ueditor.all.js"></script>
<script type="text/javascript" src="/static/plugins/ui-bootstrap-tpls-0.11.2.min.js"></script>
<%@ include file="../common_new/foot.jsp" %>
</body>
</html>