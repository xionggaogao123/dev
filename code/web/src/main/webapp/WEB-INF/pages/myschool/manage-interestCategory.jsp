<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>管理拓展课</title>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="/static/js/select2/select2.css">
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/manage.css">
    <%--<link rel="stylesheet" type="text/css" href="/static/css/increase/news.css">--%>
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css"/>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <%--<script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"></script>--%>
    <%--<script type="text/javascript" src="/static/js/select2/select2.min.js"></script>--%>
    <%--<script type="text/javascript" src="/static/js/shareforflipped.js"></script>--%>
    <%--&lt;%&ndash;<script type="text/javascript" src="/static/js/manage.js"></script>&ndash;%&gt;--%>
    <%--<script type="text/javascript" src="/static/js/sharedpart.js"></script>--%>
    <%--<script type="text/javascript" src="/static/js/WdatePicker.js"></script>--%>
    <%--<script type="text/javascript" src="/static/js/manage/edu-manage-subject.js"></script>--%>

    <%--<script>--%>
    <%--var currentPageID = 5;--%>
    <%--</script>--%>
</head>
<body ng-app="k6kt.manage" ng-controller="manageController">
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
                            <img src="/img/K6KT/list_2.png" style="margin-right: 5px;float:left;">课程分类

                        </div>
                        <c:if test="${role==1}">
                        <div class="list-add-subject"><i class="fa fa-plus"></i> 新建分类</div>

                        <div class="list-interest-change" id="xsxkqx"><i class="fa"></i>学生选课去向</div>

                        <div class="list-interest-change" id="newterm"><i class="fa"></i>开始新的选课</div>
                        </c:if>
                        <ul class="list-ul" id="subject">
                            <%--<li>--%>
                            <%--<div class="list-content">--%>
                            <%--<span>语文</span>--%>
                            <%--</div>--%>
                            <%--<i class="fa fa-pencil list-edit"></i>--%>
                            <%--<i class="fa fa-trash-o list-delete"></i>--%>
                            <%--</li>--%>
                        </ul>
                    </div>
                    <div class="edit-container">
                        <!-- 编辑科目 -->
                        <div class="edit-info-container edit-subject">
                            <div class="edit-title-bar">
                                <div>编辑类别</div>
                                <i class="fa fa-times close-modal"></i>
                            </div>
                            <div class="edit-form">
                                <div class="edit-input-group">
                                    <span>类别名称</span>
                                    <input type="text" class="edit-subject-name">
                                </div>
                                <%--<div class="edit-input-group">--%>
                                    <%--<span>科目属性</span>--%>
                                    <%--<select class="edit-info-select edit-subject-value">--%>
                                        <%--<option value="1">语文</option>--%>
                                        <%--<option value="2">数学</option>--%>
                                        <%--<option value="3">英语</option>--%>
                                        <%--<option value="4">物理</option>--%>
                                        <%--<option value="5">化学</option>--%>
                                        <%--<option value="6">生物</option>--%>
                                        <%--<option value="7">地理</option>--%>
                                        <%--<option value="8">历史</option>--%>
                                        <%--<option value="9">政治</option>--%>
                                        <%--<option value="0">其他</option>--%>
                                    <%--</select>--%>
                                <%--</div>--%>
                                <%--<div class="edit-input-group">--%>
                                    <%--<div class="belong-grade">从属年级</div>--%>
                                    <%--<div style="width:340px;" class="edit-subject-selectgrade">--%>
                                        <%--<label class="checkbox-inline">--%>
                                            <%--<input type="checkbox"> 全部--%>
                                        <%--</label>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
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
<div id="role" role="${role}"></div>

<%--<script type="text/javascript" src="/static/plugins/ueditor/ueditor.config.js"></script>--%>
<%--<script type="text/javascript" src="/static/plugins/ueditor/ueditor.all.js"></script>--%>
<%--<script type="text/javascript" src="/static/plugins/ui-bootstrap-tpls-0.11.2.min.js"></script>--%>
<%@ include file="../common_new/foot.jsp" %>
</body>

<script id="subject_tmpl" type="text/template">
    {{var role = $('#role').attr('role');}}
    {{for (var i in it) { }}
    <li icid="{{=it[i].id}}" nm="{{=it[i].name}}">
        <div class="list-content ic">
            <span>{{=it[i].name}}</span>
        </div>
        {{? role == 1 }}
        <i class="fa fa-pencil list-edit"></i>
        <i class="fa fa-trash-o list-delete"></i>
        {{?}}
    </li>
    {{} }}
    <li icid="" nm="未分类">
        <div class="list-content ic">
            <span>未分类</span>
        </div>
        <%--<i class="fa fa-pencil list-edit"></i>--%>
        <%--<i class="fa fa-trash-o list-delete"></i>--%>
    </li>
</script>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/manage/manage-interestCategory.js');
</script>
</html>