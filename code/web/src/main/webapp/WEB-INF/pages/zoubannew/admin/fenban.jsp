<%--
  Created by IntelliJ IDEA.
  User: Wangkaidong
  Date: 2016/7/16
  Time: 14:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>3+3走班-分段编班</title>
    <!-- css files -->
    <link href="/static_new/css/reset.css" rel="stylesheet"/>
    <link href="/static_new/css/zouban/zoubannew.css" rel="stylesheet"/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
</head>
<body term="${term}" gradeId="${gradeId}" mode="${mode}">
<!--#head-->
<%@ include file="../../common_new/head.jsp" %>
<!--#content-->
<div id="content" class="clearfix">
    <!--.col-left-->
    <%@ include file="../../common_new/col-left.jsp" %>
    <!--/.col-left-->

    <!--.col-right-->
    <div class="col-right">

        <!--.tab-col右侧-->
        <div class="tab-col">

            <div class="tab-head clearfix">
                <ul>
                    <li id="ZBXKSZ" class="cur"><a href="javascript:;">走班分班</a><em></em></li>
                </ul>
            </div>

            <div class="tab-main">
                <div class="zouban-title clearfix">
                    <div class="title-left">
                        <div class="term-grade">
                            <h3>${term} <em>${gradeName}</em></h3>
                        </div>
                    </div>
                    <div class="title-right">
                        <a href="javascript:void(0);" class="back">&lt;&nbsp;返回走班教务管理</a>
                    </div>
                </div>
                <ul class="tstep-tab clearfix">
                    <c:if test="${mode == 1}">
                        <li id="FDSZ" class="m-active"><a href="javascript:;"><em>①</em>分段设置</a></li>
                        <%--<li id="FCSZ"><a href="javascript:;"><em>②</em>分层设置</a></li>--%>
                        <li id="BBSZ"><a href="javascript:;"><em>②</em>分班</a></li>
                        <li id="XSSZ"><a href="javascript:;"><em>③</em>学生设置</a></li>
                        <li id="LSHJS"><a href="javascript:;"><em>④</em>老师和教室设置</a></li>
                    </c:if>
                    <c:if test="${mode == 2}"><%-- 虚拟班模式--%>
                        <li id="BBSZ" class="m-active"><a href="javascript:;"><em>①</em>分班</a></li>
                        <li id="XSSZ"><a href="javascript:;"><em>②</em>学生设置</a></li>
                        <li id="LSHJS"><a href="javascript:;"><em>③</em>老师和教室设置</a></li>
                    </c:if>
                </ul>
                <div class="set-div">
                    <!--================================第三步分段设置start===============================-->
                    <div class="fdset-con" id="tab-FDSZ">
                        <div>
                            <c:if test="${mode != 2}">
                                <a href="javascript:void(0);" class="autofd">自动分段</a>
                                <a href="javascript:void(0);" class="tstep-fdsz">调整分段</a>
                            </c:if>
                        </div>
                        <table class="fdset-table">
                            <thead>
                            <tr>
                                <th style="width:34%;">分段</th>
                                <th style="width:33%;">行政班</th>
                                <th style="width:33%;">班级人数</th>
                            </tr>
                            </thead>
                            <tbody id="fenDuanCtx"></tbody>
                        </table>
                        <script id="fenDuanTmpl" type="application/template">
                            {{~it:group:index}}
                            {{~group.classList:clazz:index2}}
                            <tr>
                                {{?index2==0}}
                                <td id="{{=group.id}}" rowspan={{=group.count}} style="background:#ececec;">
                                    {{=group.groupName}}
                                </td>
                                {{?}}
                                <td id="{{=clazz.id}}">{{=clazz.name}}</td>
                                <td>{{=clazz.value}}</td>
                            </tr>
                            {{~}}
                            {{~}}
                        </script>
                    </div>
                    <!--================================第三步分段设置end===============================-->
                    <%--================================第三步分段设置-自动分段弹窗start==============================--%>
                    <div id="autoFenDuanWindow" class="fdset-alert">
                        <div class="zb-set-title clearfix">
                            <p>自动分段</p>
                            <span class="autoFenduanClose">X</span>
                        </div>
                        <div class="autofd-count">
                            <span>分段数</span>
                            <select id="count">
                                <option value="1">1</option>
                                <option value="2">2</option>
                                <option value="3">3</option>
                                <option value="4">4</option>
                                <option value="5">5</option>
                            </select>
                        </div>
                        <div class="alert-btn">
                            <button id="autoFenDuan" class="alert-btn-sure">确定</button>
                            <button class="alert-btn-qx autoFenduanClose">取消</button>
                        </div>
                    </div>
                    <%--================================第三步分段设置-自动分段弹窗end==============================--%>
                    <!--================================第三步分段设置-调整分段弹窗start===============================-->
                    <div id="changeFenDuanWindow" class="fdset-alert">
                        <div class="zb-set-title clearfix">
                            <p>分段设置</p>
                            <span class="changeClose">X</span>
                        </div>
                        <div class="table-wrap">
                            <table class="fd-alert-table">
                                <thead>
                                <tr>
                                    <th style="width:40%;">行政班</th>
                                    <th style="width:60%;">分段</th>
                                </tr>
                                </thead>
                                <tbody id="changeFenDuanCtx"></tbody>
                            </table>
                        </div>
                        <script id="changeFenDuanTmpl" type="application/template">
                            {{~it:fenduan:index}}
                            <tr classId="{{=fenduan.classId}}" groupId="{{=fenduan.groupId}}">
                                <td>{{=fenduan.className}}</td>
                                <td>
                                    {{~fenduan.groupList:group:index2}}
                                    {{?fenduan.groupId==group.id}}
                                    <label>
                                        <input type="radio" name="group_{{=index}}" value="{{=group.id}}" checked>&nbsp;{{=group.name}}
                                    </label>
                                    {{??}}
                                    <label>
                                        <input type="radio" name="group_{{=index}}" value="{{=group.id}}">&nbsp;{{=group.name}}
                                    </label>
                                    {{?}}
                                    {{~}}
                                </td>
                            </tr>
                            {{~}}
                        </script>
                        <div class="alert-btn">
                            <button id="changeConfirm" class="alert-btn-sure">确定</button>
                            <button class="alert-btn-qx changeClose">取消</button>
                        </div>
                    </div>
                    <!--================================第三步分段设置-调整分段弹窗end===============================-->
                    <!--================================第三步分层设置start===============================-->
                    <div class="fcset-con clearfix" id="tab-FCSZ">
                        <div id="subjectListCtx" class="fcset"></div>
                        <script id="subjectListTmpl" type="application/template">
                            <span>分层考试学科</span>
                            {{~it:value:index}}
                            <label><input type="checkbox" name="subject"
                                          value="{{=value.id}}">&nbsp;{{=value.name}}</label>
                            {{~}}
                        </script>
                        <div class="fcset-operate">
                            <form id="form" method="post" enctype="multipart/form-data" style="margin-top: 20px;">
                                <input type="file" id="file" name="file"/>
                            </form>
                            <div class="model-btn">
                                <button id="download" class="fcset-down" style="margin-top: 20px;">下载模板</button>
                                <button id="upload" class="" style="margin-top: 20px;">开始导入</button>
                            </div>
                            <div>
                                <div class="">导入数据说明：</div>
                                <p>1.导入数据请严格按照模板，请勿改变用户名和学科名。</p>
                            </div>
                        </div>
                    </div>
                    <!--================================第三步分层设置end===============================-->
                    <!--================================第三步编班设置start===============================-->
                    <div class="bbset-con clearfix" id="tab-BBSZ">
                        <div class="clearfix m-bbset-add">
                            <select id="groupListCtx" <c:if test="${mode == 2}">style="visibility: hidden"</c:if> ></select>
                            <script id="groupListTmpl" type="application/template">
                                <option value="">全部</option>
                                {{~it:group:index}}
                                <option value="{{=group.id}}">{{=group.groupName}}</option>
                                {{~}}
                            </script>
                            <c:if test="${mode == 1}">
                                <a href="javascript:void(0);" class="tstep-ksbb">自动分班</a>
                            </c:if>
                            <c:if test="${mode == 2}">
                                <a href="javascript:void(0);" class="tstep-import" id="importZBCourse">导入分班结果</a>
                            </c:if>
                        </div>
                        <div class="bbset-wrap">
                            <table class="bbset-table">
                                <thead>
                                <tr>
                                    <th style="width:25%;">班级类型</th>
                                    <th style="width:25%;">等级考教学班</th>
                                    <th style="width:25%;">合格考教学班</th>
                                    <th style="width:25%;">教学班个数</th>
                                </tr>
                                </thead>
                                <tbody id="couresListCtx"></tbody>
                            </table>
                            <script id="courseListTmpl" type="application/template">
                                {{~it:value:index}}
                                <tr>
                                    <td style="background:#ececec;">{{=value.subjectName}}</td>
                                    <td style="text-align: left !important;padding-left:30px;">
                                        {{~value.advCourseList:advCourse:index2}}
                                        <span>{{=advCourse}}</span><br>
                                        {{~}}
                                    </td>
                                    <td style="text-align: left !important;padding-left:30px;">
                                        {{~value.simCourseList:simCourse:index2}}
                                        <span>{{=simCourse}}</span><br>
                                        {{~}}
                                    </td>
                                    <td>{{=value.count}}</td>
                                </tr>
                                {{~}}
                            </script>
                        </div>
                    </div>
                    <!--================================第三步编班设置end===============================-->
                    <%--===============================第三步分班-导入分班结果start================================--%>
                    <div id="importZoubanCourseWindow" class="fdset-alert">
                        <div class="zb-set-title clearfix">
                            <p>导入分班结果</p>
                            <span class="importWindowClose">X</span>
                        </div>
                        <div style="margin: 5px auto;padding: 10px 35px;">
                            <div style="color: red;">导入数据说明：导入数据请严格按照模板，请勿修改表头.</div>
                            <form id="importZoubanForm" method="post" enctype="multipart/form-data"
                                  style="margin-top: 20px;">
                                <input type="file" id="zbCourseFile" name="file"/>
                            </form>
                            <div class="model-btn">
                                <button id="downloadZBCourseTemplate" class="fcset-down" style="margin-top: 20px;">
                                    下载模板
                                </button>
                                <button id="uploadCourseFile" style="margin-top: 20px;">开始导入</button>
                            </div>
                        </div>
                    </div>
                    <%--=================================第三步分班-导入分班结果end==================================--%>

                    <%--=================================第三步分班-自动分班弹窗start==============================--%>
                    <div id="autoFenBanWindow" class="fdset-alert">
                        <div class="zb-set-title clearfix">
                            <p>自动分班</p>
                            <span class="autoFenbanClose">X</span>
                        </div>
                        <div style="color: red; margin: 20px 145px;">
                            <span>建议上下限在20~50之间</span>
                        </div>
                        <dl>
                            <dd>
                                <span>分段</span>
                                <select id="groupCtx"></select>
                            </dd>
                            <dd>
                                <span>等级考班级人数上限</span>
                                <input type="text" id="advMax"
                                       onkeyup="if(isNaN(value))execCommand('undo')"
                                       onafterpaste="if(isNaN(value))execCommand('undo')">
                            </dd>
                            <dd>
                                <span>等级考班级人数下限</span>
                                <input type="text" id="advMin"
                                       onkeyup="if(isNaN(value))execCommand('undo')"
                                       onafterpaste="if(isNaN(value))execCommand('undo')">
                            </dd>
                            <dd>
                                <span>合格考班级人数上限</span>
                                <input type="text" id="simMax"
                                       onkeyup="if(isNaN(value))execCommand('undo')"
                                       onafterpaste="if(isNaN(value))execCommand('undo')">
                            </dd>
                            <dd>
                                <span>合格考班级人数下限</span>
                                <input type="text" id="simMin"
                                       onkeyup="if(isNaN(value))execCommand('undo')"
                                       onafterpaste="if(isNaN(value))execCommand('undo')">
                            </dd>
                            <dd>
                                <span>每段可用教室数</span>
                                <select id="classroomCount">
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                    <option value="5">5</option>
                                    <option value="6">6</option>
                                    <option value="7">7</option>
                                    <option value="8">8</option>
                                </select>
                            </dd>
                        </dl>
                        <div class="alert-btn">
                            <button id="autoFenBan" class="alert-btn-sure">确定</button>
                            <button class="alert-btn-qx autoFenbanClose">取消</button>
                        </div>
                    </div>
                    <%--=================================第三步分班-自动分班弹窗end==============================--%>
                    <!--================================第三步分班-学生设置start===============================-->
                    <div class="stuset-con" id="tab-XSSZ">
                        <div class="lsandjs-title">
                            <span <c:if test="${mode == 2}">style="visibility: hidden"</c:if>>分段</span>
                            <select id="studentGroupCtx"
                                    <c:if test="${mode == 2}">style="visibility: hidden"</c:if>></select>
                            <span>班级类型</span>
                            <select id="level2">
                                <option value="1">等级考</option>
                                <option value="2">合格考</option>
                            </select>
                        </div>
                        <table class="stuset-table">
                            <thead>
                            <tr>
                                <c:if test="${mode == 1}">
                                    <th style="width:15%;">逻辑位置</th>
                                </c:if>
                                <th style="width:30%;">教学班</th>
                                <th style="width:15%;">人数</th>
                                <th style="width:30%;">操作</th>
                            </tr>
                            </thead>
                            <tbody id="stuCourseListCtx">
                            </tbody>
                        </table>
                        <script id="stuCourseListTmpl" type="application/template">
                            {{~it:value:index}}
                            {{~value.courseList:course:index2}}
                            <tr>
                                <c:if test="${mode == 1}">
                                    {{?index2==0}}
                                    <td rowspan={{=value.count}} style="background:#ececec;">{{=index + 1}}</td>
                                    {{?}}
                                </c:if>
                                <td>{{=course.courseName}}</td>
                                <td>{{=course.studentsCount}}</td>
                                <td>
                                    <a href="javascript:;" class="stuset-edit stu-edit"
                                       courseId="{{=course.zbCourseId}}" courseName="{{=course.courseName}}"
                                       count="{{=course.studentsCount}}">编辑</a>
                                    <i>|</i>
                                    <a href="javascript:;" class="stuset-edit stu-detail"
                                       courseId="{{=course.zbCourseId}}" courseName="{{=course.courseName}}"
                                       count="{{=course.studentsCount}}">查看</a>
                                    <c:if test="${mode == 2}">
                                        <i>|</i>
                                        <a href="javascript:;" class="stuset-edit stu-distribution"
                                           courseId="{{=course.zbCourseId}}"
                                           courseName="{{=course.courseName}}">分配学生</a>
                                    </c:if>
                                </td>
                            </tr>
                            {{~}}
                            {{~}}
                        </script>
                    </div>
                    <!--================================第三步-学生设置end===============================-->
                    <!--================================第三步-学生设置教学班设置弹窗start===============================-->
                    <div id="updateCourseNameWindow" class="stuset-alert">
                        <div class="zb-set-title clearfix">
                            <p>教学班设置</p>
                            <span class="zb-set-close stuClose">X</span>
                        </div>
                        <div class="stuset-alert-main">
                            <h3 id="courseName">历史A1&nbsp;</h3>
                            <span>教学班名称</span>
                            <input type="text" id="newName">
                        </div>
                        <div class="alert-btn">
                            <button class="alert-btn-sure stuSubmit" courseId="">确定</button>
                            <button class="alert-btn-qx stuClose">取消</button>
                        </div>
                    </div>
                    <!--================================第三步-学生设置教学班设置弹窗end===============================-->

                    <!--=================第三步-学生设置分配学生弹窗框start===================-->
                    <div class="allot-alert" style="height: auto;" courseId="">
                        <div class="popup-SZ-top">
                            <em>分配学生</em><i class="h-cursor SQ-X">X</i>
                        </div>
                        <div class="alert-main">
                            <div class="allot-title">
                                <h3 style="text-align: center;margin-bottom: 20px;"></h3>
                                <select id="subjectGroupListCtx">
                                </select>
                                <script id="subjectGroupListTmpl" type="application/template">
                                    {{~it:group:index}}
                                    <option value="{{=group.id}}">{{=group.name}}</option>
                                    {{~}}
                                </script>
                                <span>选定学生</span>
                            </div>
                            <div class="allot-main clearfix">
                                <div class="allot-left">
                                    <ul class="left-ul">
                                    </ul>
                                    <script id="studentListTmpl" type="application/template">
                                        {{~it:value:index}}
                                        <li stuId="{{=value.id}}"><span>{{=value.name}}</span><em>√</em></li>
                                        {{~}}
                                    </script>
                                </div>
                                <div class="allot-mid">
                                    <img src="/static_new/images/allot-right.png" style="margin: 100px 0 50px 15px;">
                                    <img src="/static_new/images/allot-left.png" style="margin-left:15px;">

                                </div>
                                <div class="allot-right">
                                    <ul class="right-ul">
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="popup-SZ-bottom" style="padding-bottom: 30px;margin-top: 30px;">
                            <span class="SZ-TJ h-cursor">确定</span>
                            <span class="SZ-QX h-cursor">取消</span>
                        </div>
                    </div>
                    <!--=================第三步-学生设置分配学生弹窗框end===================-->


                    <!--================================第三步-老师和教室设置start===============================-->
                    <div class="lsandjs-con" id="tab-LSHJS">
                        <div class="lsandjs-title">
                            <span <c:if test="${mode == 2}">style="visibility: hidden"</c:if> >分段</span>
                            <select id="teacherGroupCtx" <c:if test="${mode == 2}">style="visibility: hidden"</c:if>></select>
                            <span>班级类型</span>
                            <select id="level">
                                <option value="1">等级考</option>
                                <option value="2">合格考</option>
                            </select>
                            <c:if test="${mode == 1}">
                                <a class="autoSetTeaAndClsrm">一键设置老师和教室</a>
                            </c:if>
                        </div>
                        <table class="lsandjs-table">
                            <thead>
                            <tr>
                                <c:if test="${mode == 1}">
                                    <th style="width:13%;">逻辑位置</th>
                                </c:if>
                                <th style="width:15%;">教学班</th>
                                <th style="width:15%;">人数</th>
                                <th style="width:15%;">任课老师</th>
                                <th style="width:15%;">上课教室</th>
                                <c:if test="${mode == 1}">
                                    <th style="width:12%;">操作</th>
                                </c:if>
                            </tr>
                            </thead>
                            <tbody id="teaCourseListCtx"></tbody>
                        </table>
                        <script id="teaCourseListTmpl" type="application/template">
                            {{~it:value:index}}
                            {{~value.courseList:course:index2}}
                            <tr>
                                <c:if test="${mode == 1}">
                                    {{?index2==0}}
                                    <td rowspan={{=value.count}} style="background:#ececec;">{{=index + 1}}</td>
                                    {{?}}
                                </c:if>
                                <td>{{=course.courseName}}</td>
                                <td>{{=course.studentsCount}}</td>
                                <td>{{=course.teacherName}}</td>
                                <td>{{=course.classRoom}}</td>
                                <c:if test="${mode == 1}">
                                    {{?index2==0}}
                                    <td rowspan={{=value.count}}>
                                        <a href="javascript:void(0);" class="lsandjs-edit" group="{{=course.groupId}}">编辑</a>
                                    </td>
                                    {{?}}
                                </c:if>
                            </tr>
                            {{~}}
                            {{~}}
                        </script>
                    </div>
                    <!--================================第三步-老师和教室设置end===============================-->

                    <!--================================第三步-老师和教室设置弹窗start===============================-->
                    <div class="lsandjs-alert">
                        <div class="zb-set-title clearfix">
                            <p>教学班老师和教室设置</p>
                            <span class="zb-set-close teaClose">X</span>
                        </div>
                        <ul class="lsandjs-list" id="teaAndClsrmCtx"></ul>
                        <script id="teaAndClsrmTmpl" type="application/template">
                            {{~it:value:index}}
                            <li courseId="{{=value.courseId}}">
                                <p><span>{{=value.courseName}}</span> / <span>{{=value.studentCount}}人</span></p>
                                <select class="teacher">
                                    {{~value.teacherList:teacher:index2}}
                                    <option value="{{=teacher.id}}" {{?value.teacherId==teacher.id}}selected{{?}}>
                                        {{=teacher.name}}
                                    </option>
                                    {{~}}
                                </select>
                                <select class="classroom">
                                    {{~value.classroomList:classroom:index3}}
                                    <option value="{{=classroom.id}}" {{?value.classroomId==classroom.id}}selected{{?}}>
                                        {{=classroom.name}}
                                    </option>
                                    {{~}}
                                </select>
                            </li>
                            {{~}}
                        </script>
                        <div class="alert-btn">
                            <button class="alert-btn-sure" id="setTeaClsrm">确定</button>
                            <button class="alert-btn-qx teaClose">取消</button>
                        </div>
                    </div>
                    <!--================================第三步-老师和教室设置弹窗end===============================-->
                </div>
            </div>

            <%--================================第三步学生设置-学生列表start=================================--%>
            <div class="stu-changeClass">
                <a class="backMain">&lt;返回</a>

                <div>
                    <h3><span id="className"></span><span>人数：<em id="stuCount"></em></span></h3>
                    <table class="stuset-table">
                        <thead>
                        <tr>
                            <th></th>
                            <th width="25%">学生姓名</th>
                            <th width="20%">性别</th>
                            <th width="30%">所属行政班</th>
                            <th width="20%">操作</th>
                        </tr>
                        </thead>
                        <tbody id="stuListCtx"></tbody>
                    </table>
                    <script id="stuListTmpl" type="application/template">
                        {{~it.studentList:value:index}}
                        <tr>
                            <td>{{=index + 1}}</td>
                            <td>{{=value.userName}}</td>
                            <td>{{=value.sex}}</td>
                            <td>{{=value.className}}</td>
                            {{?it.change == 1}}
                            <td>
                                <a class="active-change" studentId="{{=value.userId}}">调班</a>
                            </td>
                            {{??}}
                            <td>
                                <a class="notActive-change">调班</a>
                            </td>
                            {{?}}
                        </tr>
                        {{~}}
                    </script>
                </div>
            </div>
            <!--================================第三步学生设置教学班设置弹窗start===============================-->
            <div id="changeClassWindow" class="stuset-alert">
                <div class="zb-set-title clearfix">
                    <p>调班</p>
                    <span class="zb-set-close changeClassClose">X</span>
                </div>
                <div class="stuset-alert-main">
                    <span>可选教学班</span>
                    <select id="availableCourseCtx"></select>
                    <script id="availableCourseTmpl" type="application/template">
                        {{~it:value:index}}
                        <option value="{{=value.id}}">{{=value.name}}</option>
                        {{~}}
                    </script>
                </div>
                <div class="alert-btn">
                    <button class="alert-btn-sure changeSubmit" studentId="" courseId="">确定</button>
                    <button class="alert-btn-qx changeClassClose">取消</button>
                </div>
            </div>
            <!--================================第三步学生设置教学班设置弹窗end===============================-->
            <%--================================第三步学生设置-学生列表end=================================--%>
        </div>
        <!--/.tab-col右侧-->
    </div>
    <!--/.col-right-->


</div>
<!--/#content-->


<!--#foot-->
<%@ include file="../../common_new/foot.jsp" %>
<!--#foot-->
<div class="bg"></div>


<!-- Javascript Files -->
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js"></script>
<script>
    seajs.use('zoubanFenban', function (zoubanFenban) {
        zoubanFenban.init();
    });
</script>
</body>
</html>
