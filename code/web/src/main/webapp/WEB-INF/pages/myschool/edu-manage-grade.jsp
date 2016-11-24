<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>管理校园-复兰科技 K6KT-快乐课堂</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <link rel="stylesheet" href="/static/css/inform.css">
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="/static/js/select2/select2.css">
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/manage.css">
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/manage/news.css"/>

    <%--<script type="text/javascript" src="/static/plugins/ueditor/ueditor.config.js"></script>--%>
    <%--<script type="text/javascript" src="/static/plugins/ueditor/ueditor.all.js"></script>--%>
    <%--<script type="text/javascript" src="/static/plugins/ui-bootstrap-tpls-0.11.2.min.js"></script>--%>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <%--<script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"></script>--%>
    <%--<script type="text/javascript" src="/static/js/select2/select2.min.js"></script>--%>
    <%--<script type="text/javascript" src="/static/js/WdatePicker.js"></script>--%>
    <%--<script type="text/javascript" src="/static/js/manage/edu-manage-grade.js"></script>--%>

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
    <style type="text/css">
        #ueditor_0{
            width: 480px !important;
            height: 100px !important;
        }
    </style>

    <%--<script type="application/javascript">--%>
        <%--var ue=new UE.ui.Editor({--%>

        <%--});--%>
        <%--ue.render('ueditor_0');--%>
    <%--</script>--%>
</head>
<body>
<%@ include file="../common_new/head.jsp" %>
<input type="hidden" id="hiddenValue" name="hiddenValue" value="${param.tag}"/>

<div id="content_main_container">
    <div id="content_main">
        <!-- 左侧导航-->
        <%@ include file="../common_new/col-left.jsp" %>
        <!-- left end -->
        <!-- right start-->
        <div class="modal-bg"></div>
        <div id="right-container" <%--style="float: none"--%>>
            <div class="main-container">
                <div class="manage-right-container">

                    <!-- 年级列表 -->
                    <div class="list-container grade-list" belong="grade">
                        <div class="list-title" style="line-height:28px;">
                            <img src="/img/K6KT/list_3.png"style="margin-right: 5px;float:left;">年级列表
                        </div>
                        <div class="list-add-grade"><i class="fa fa-plus"></i> 新建年级</div>
                        <ul class="list-ul" id="grade">
                            <%--<li>--%>
                                <%--<div class="list-content">--%>
                                    <%--<span>一年级</span>--%>
                                    <%--<span>年级组长：</span>--%>
                                    <%--<span class="grade-manager-name">吴峥</span>--%>
                                <%--</div>--%>
                                <%--<i class="fa fa-pencil list-edit"></i>--%>
                                <%--<i class="fa fa-trash-o list-delete"></i>--%>
                            <%--</li>--%>
                        </ul>
                    </div>


                    <div class="edit-container">
                        <!-- 编辑年级 -->
                        <div class="edit-info-container edit-grade">
                            <div class="edit-title-bar">
                                <div>编辑年级</div>
                                <i class="fa fa-times close-modal"></i>
                            </div>
                            <div class="edit-form">
                                <div class="edit-input-group">
                                    <span>年级名称</span>
                                    <input type="text" class="edit-grade-name">
                                </div>
                                <div class="edit-input-group">
                                    <span>年级属性</span>
                                    <select class="edit-info-select edit-grade-value">
                                    </select>
                                </div>
                                <%--<div class="edit-input-group">--%>
                                    <%--<span>年级组长</span>--%>
                                    <%--<select class="edit-info-select edit-grade-teacher">--%>
                                    <%--</select>--%>
                                <%--</div>--%>
                                <%--<div class="edit-input-group">--%>
                                    <%--<span>备课组长</span>--%>
                                    <%--<select class="edit-info-select edit-class-teacher">--%>
                                    <%--</select>--%>
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

<%@ include file="../common_new/foot.jsp" %>

</body>

<script id="grade_tmpl" type="text/template">
    {{for (var i in it) { }}
    <li ty="{{=it[i].gradeType}}" nm="{{=it[i].gradeName}}" gid="{{=it[i].gradeId}}">
        <div class="list-content">
            <span>{{=it[i].gradeName}}</span>
        </div>
        <i class="fa fa-pencil list-edit"></i>
        <i class="fa fa-trash-o list-delete"></i>
    </li>
    {{} }}
</script>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/manage/edu-manage-grade');
</script>
</html>