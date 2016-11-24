<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>考试</title>
    <link rel="stylesheet" type="text/css" href="/static/css/activity/titles.css">
    <link rel="stylesheet" type="text/css" href="/static/css/exercise/question.css">
    <link rel="stylesheet" type="text/css" href="/static/css/exercise/teacher_configuration.css">

    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="/static/css/dialog.css"/>


    <link rel="stylesheet" href="/static/plugins/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/static/css/k6kt.css"/>
    <link rel="stylesheet" href="/static/css/common/test-banner.css"/>


    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/vendor/jquery.ui.widget.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.iframe-transport.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.fileupload.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.fileupload-process.js"></script>
    <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap.min.js"></script>

    <script type="text/javascript" src="/static/plugins/angular-1.2.28/angular.min.js"></script>
    <script type="text/javascript" src="/static/plugins/ui-bootstrap-tpls-0.11.2.min.js"></script>

    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/js/exam/exam-index.js"></script>

    <style type="text/css">
        #content_main_container {
            width: 1100px;
            margin: 0 auto;
            position: relative;
            left: -50px;
        }

        #right-container {
            float: left;
        }

        .test_mian div {
            overflow: visible;
        }

        input.form-control {
            font-size: 12px;
        }

    </style>

</head>
<body>
<!--=======================头部==============================-->
<%@ include file="../common_new/head.jsp" %>

<div id="content_main_container" style="position: relative;left: 0px;width: 1000px;">
    <div id="content_main" style="width: 1000px; margin: 0 auto">
        <!-- 左侧导航-->
        <%@ include file="../common_new/col-left.jsp" %>
        <!-- left end -->

        <div ng-app="exam-index" id="right-container" style="/*margin-left: 10px;*/width: 780px;">
            <jsp:include page="../common/right.jsp"></jsp:include>

            <div class="test_mian" ng-controller="ExamUploadCtrl">
                <div class="container-fluid form-horizontal">
                    <form class="form-horizontal" id="submitWord" action="/exam/testpaper/upload.do" method="post" enctype="multipart/form-data">
                        <div class="form-group" style="margin-top: 10px;">
                            <h4 class="col-md-2 control-label">上传试卷</h4>
                        </div>
                        <div class="form-group">
                            <label for="practiseName" class="col-md-2 control-label">
                                试卷名称
                            </label>
                            <div class="col-md-7">
                                <input name="name" id="practiseName" placeholder="必填" class="form-control">
                            </div>

                        </div>
                        <div class="form-group" id="questDiv">
                            <label for="questWord" class="col-md-2 control-label">
                                上传试题
                            </label>
                            <div class="col-md-5">
                                <input type='text' name='textfield' placeholder="必填，支持doc或者docx类型文档" readonly="true" id='questWord' class="form-control"/>
                            </div>
                            <div class="col-md-2">
                                <label for="questWord1" class='btn btn-default form-control'>
                                    选择文件
                                </label>
                            </div>
                            <div style="width: 0; height: 0; overflow: hidden">
                                <input type="file" name="examDoc" class="file" id="questWord1" multiple size="28"
                                       onchange="document.getElementById('questWord').value=this.value"
                                       style="opacity: 0;filter:alpha(opacity=0);width:0;height:0;"/>
                            </div>

                        </div>
                        <div class="form-group" id="answerDiv">
                            <label class="col-md-2 control-label" for="answerWord">
                                上传解析
                            </label>

                            <div class="col-md-5">
                                <input type='text' name='textfield' placeholder="可选，学生交卷后可查看解析，文档类型同上" id='answerWord'
                                       readonly class='form-control'/>
                            </div>
                            <div class="col-md-2">
                                <label for="answerWord1" class='btn btn-default form-control'>
                                    选择文件
                                </label>
                            </div>
                            <div style="width: 0; height: 0; overflow: hidden">
                                <input type="file" name="parseDoc" class="file" id="answerWord1" multiple size="28"
                                       onchange="document.getElementById('answerWord').value=this.value"
                                       style="opacity: 0;filter:alpha(opacity=0);width:0;height:0;"/>
                            </div>


                        </div>
                        <div class="form-group">
                            <div class="col-md-2 control-label">推送班级</div>

                            <div class="col-md-7">
                                <c:forEach items="${classList}" var="classInfo">
                                    <div class="checkbox col-md-4">
                                        <label>
                                            <input name="pushClass" type="checkbox" value="${classInfo.id}" data-title="${classInfo.value}" get-classes>
                                                ${classInfo.value}
                                        </label>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-md-offset-2 col-md-5 text-right">
                                <!--没有选择班级-->
                                <span id="className" style="color: red; display: none">请选择班级！</span>
                            </div>
                            <div class="col-md-2 text-right">
                                <button type="button" class="btn btn-primary" ng-click="submitWord()">上传</button>
                            </div>
                        </div>
                        <input type="hidden" id="classIds" name="classes" value="">
                        <input type="hidden" name="type" value="1">
                    </form>
                </div>

            </div>
            <div class="test_bottom" ng-controller="ExamListCtrl">
                <img src="/img/loading3.gif" style="margin-bottom: 10px;" class="center-block" ng-hide="examData.loaded"/>
                <table class="table ng-hide" ng-show="examData.loaded">
                    <tr ng-repeat="(key, exam) in examData.examList">
                        <td>
                            {{exam.name}}
                        </td>
                        <td class="text-right">
                            <button id="btn_{{exam.id}}" class="btn btn-warning" ng-if="exam.submitStudent == 0" ng-click="configPaper(exam.id)">修改配置</button>
                            
                            <div class="btn-group">
                                <button class="btn btn-info dropdown-toggle" data-toggle="dropdown" aria-expanded="false">修改推送 <span class="caret"></span></button>
                                <ul class="dropdown-menu" role="menu">
                                    <li ng-repeat="classInfo in exam.alreadClasses" class="disabled">
                                        <a href>√ {{classInfo.value || '(班级名为空)'}}</a>
                                    </li>
                                    <li ng-repeat="classInfo in exam.classes">
                                        <a href ng-click="pushPaper(exam.id, classInfo.idStr)">{{classInfo.value || '(班级名为空)'}}</a>
                                    </li>
                                </ul>
                            </div>
                   
                             <button class="btn btn-success" ng-class="{'disabled': exam.submitStudent == 0}" ng-disabled="exam.submitStudent == 0" ng-click="correctPaper(exam.id)">
                             {{exam.isFinish==1 ? "试卷统计" : "批改试卷"}}
                             </button>
                           
                            <button class="btn btn-danger" ng-click="deletePaper(exam.id)">删除</button>
                        </td>
                        <td class="text-right">
                            {{exam.time}}
                        </td>
                        
                        <td class="text-center">
                            {{exam.submitStudent}}人提交 / 共{{exam.totalStudent}}人
                        </td>
                    </tr>
                </table>
            </div>
            <div class="text-center" ng-controller="ExamListPaginationCtrl">
                <pagination boundary-links="true" max-size="5" total-items="examData.total" items-per-page="examData.pageSize" ng-model="examData.page" ng-change="examData.loadPage()" class="pagination-sm" previous-text="上一页" next-text="下一页" first-text="第一页" last-text="最后一页"></pagination>
            </div>
            <!--=============================分页================================-->

            <div style="background-color: #ffffff;height: 20px; width: 100%"></div>

        </div>
        <!-- right end-->
        <div style="clear: both"></div>
    </div>
</div>


<%@ include file="../common_new/foot.jsp" %>
</body>
</html>