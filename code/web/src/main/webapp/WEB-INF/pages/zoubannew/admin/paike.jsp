<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>3+3走班-排课</title>
    <!-- css files -->
    <link href="/static_new/css/reset.css" rel="stylesheet"/>
    <link href="/static_new/css/zouban/zoubannew.css" rel="stylesheet"/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
</head>
<body term="${term}" gradeId="${gradeId}" mode="${mode}">
<!--#head-->
<%@ include file="../../common_new/head.jsp" %>
<!--/#head-->

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
                    <c:if test="${mode != 0}">
                        <li id="ZBK"><a href="javascript:;">排走班课</a><em></em></li>
                    </c:if>
                    <li id="FZZBK"><a href="javascript:;">排分层走班课</a><em></em></li>
                    <li id="TYK"><a href="javascript:;">排体育课</a><em></em></li>
                    <li id="FZBK"><a href="javascript:;">排非走班课</a><em></em></li>
                    <li id="CTSW"><a href="javascript:;">冲突事务</a><em></em></li>
                    <%--<li id="ZXJS"><a href="javascript:;">排自习教室</a><em></em></li>--%>
                </ul>
            </div>
            <div class="fourstep-title clearfix">
                <span class="fstep-inf">${term}<em>${gradeName}</em></span>
                <a href="javascript:;" class="fourstep-back">&lt;&nbsp;返回走班教务管理</a>
            </div>
            <div class="tab-main">
                <!--=================================第四步排走课班start==================================-->
                <div class="zbk-con" id="tab-ZBK">
                    <div class="fzbk-operate clearfix">
                        <div class="operate-left">
                            <span>分段</span>
                            <select id="groupSelectCtx"></select>
                            <script id="groupSelectTmpl" type="application/template">
                                {{~it:group:index}}
                                <option value="{{=group.id}}">{{=group.groupName}}</option>
                                {{~}}
                            </script>
                        </div>
                        <div class="operate-right">
                            <%--<span class="class-clear" id="zbClearTimetable">清空课程</span>--%>
                        </div>
                    </div>
                    <div class="fzbk-main clearfix">
                        <div class="fzbk-left">
                            <span class="left-wpkc">待排课程</span>
                            <ul class="left-list" id="arrangingZBCourseCtx"></ul>
                            <script id="arrangingZBCourseTmpl" type="application/template">
                                {{~it:list:index}}
                                <li class="arranging-zouban-before">
                                    {{~list:course:index2}}
                                    <span courseId="{{=course.id}}">{{=course.name}}</span><br>
                                    {{~}}
                                </li>
                                {{~}}
                            </script>
                        </div>
                        <div class="fzbk-right">
                            <table class="fzbk-table" id="zbTimetableCtx"></table>
                            <script id="zbTimetableTmpl" type="application/template">
                                <thead>
                                <tr>
                                    <th style="width:20%;">上课时间</th>
                                    {{~it.days:day:index}}
                                    <th style="width:11%;">{{=day}}</th>
                                    {{~}}
                                </tr>
                                </thead>
                                <tbody>
                                {{~it.sections:section:index}}
                                <tr>
                                    <td style="background:#ececec;">
                                        <span class="class-turn">{{=section}}</span>{{=it.classTime[index]}}
                                    </td>
                                    {{~it.days:day:index2}}
                                    {{?it.courseItemList[index][index2].classEvent != ''}}
                                    <td class="point-zouban forbidden-event" x="{{=index2 + 1}}" y="{{=index + 1}}">
                                        {{=it.courseItemList[index][index2].classEvent}}
                                    </td>
                                    {{??}}
                                        {{?it.courseItemList[index][index2].courseList.length > 0}}
                                        {{? it.courseItemList[index][index2].type == 1}}
                                        <td class="point-zouban used-zouban-point {{?it.courseItemList[index][index2].eventList.length > 0}}used-event-point{{?}}"
                                            x="{{=index2 + 1}}" y="{{=index + 1}}">
                                                    <span>
                                                        <span>走班({{=it.courseItemList[index][index2].courseList.length}})</span><br>
                                                        {{?it.courseItemList[index][index2].eventList.length > 0}}
                                                        <span style="color: #ca0202;">事务</span>
                                                        {{?}}
                                                    </span>
                                            <div class="zouban-class-con">
                                                <ul>
                                                    {{~it.courseItemList[index][index2].courseList:course:index3}}
                                                    <li>{{=course.courseName}}<br>{{=course.teacherName}}<br>{{=course.classRoom}}</li>
                                                    {{~}}
                                                    {{~it.courseItemList[index][index2].eventList:event:index4}}
                                                    <li>{{=event.name}}</li>
                                                    {{~}}
                                                </ul>
                                            </div>
                                        </td>
                                        {{??}}
                                        <td class="point-zouban used-point {{?it.courseItemList[index][index2].eventList.length > 0}}used-event-point{{?}}"
                                            x="{{=index2 + 1}}" y="{{=index + 1}}">
                                            {{?it.courseItemList[index][index2].eventList.length > 0}}
                                            <span style="color: #ca0202;">事务</span>
                                            <div class="zouban-class-con">
                                                <ul>
                                                    {{~it.courseItemList[index][index2].eventList:event:index4}}
                                                    <li>{{=event.name}}</li>
                                                    {{~}}
                                                </ul>
                                            </div>
                                            {{?}}
                                        </td>
                                        {{?}}
                                        {{??}}
                                        <td class="point-zouban unused-point {{?it.courseItemList[index][index2].eventList.length > 0}}used-event-point{{?}}"
                                            x="{{=index2 + 1}}" y="{{=index + 1}}">
                                            {{?it.courseItemList[index][index2].eventList.length > 0}}
                                            <span style="color: #ca0202;">事务</span>
                                            <div class="zouban-class-con">
                                                <ul>
                                                    {{~it.courseItemList[index][index2].eventList:event:index4}}
                                                    <li>{{=event.name}}</li>
                                                    {{~}}
                                                </ul>
                                            </div>
                                            {{?}}
                                        </td>
                                        {{?}}
                                    {{?}}
                                    {{~}}
                                </tr>
                                {{~}}
                                </tbody>
                            </script>
                        </div>
                    </div>
                </div>
                <!--=================================第四步排走课班end==================================-->
                <!--=================================第四步排分组走课班start==================================-->
                <div class="fzzb-con" id="tab-FZZBK">
                    <div class="fzbk-operate clearfix">
                        <div class="operate-right">
                            <%--<span class="class-clear" id="fzzbClearTimetable">清空课程</span>--%>
                        </div>
                    </div>
                    <div class="fzbk-main clearfix">
                        <div class="fzbk-left">
                            <span class="left-wpkc">待排课程</span>
                            <ul class="left-list" id="arrangingFZZBCourseCtx"></ul>
                            <script id="arrangingFZZBCourseTmpl" type="application/template">
                                {{~it:list:index}}
                                <li class="arranging-fzzb-before">
                                    {{~list:course:index2}}
                                    <span courseId="{{=course.id}}">{{=course.name}}</span><br>
                                    {{~}}
                                </li>
                                {{~}}
                            </script>
                        </div>
                        <div class="fzbk-right">
                            <table class="fzbk-table" id="fzzbTimetableCtx"></table>
                            <script id="fzzbTimetableTmpl" type="application/template">
                                <thead>
                                <tr>
                                    <th style="width:20%;">上课时间</th>
                                    {{~it.days:day:index}}
                                    <th style="width:11%;">{{=day}}</th>
                                    {{~}}
                                </tr>
                                </thead>
                                <tbody>
                                {{~it.sections:section:index}}
                                <tr>
                                    <td style="background:#ececec;">
                                        <span class="class-turn">{{=section}}</span>{{=it.classTime[index]}}
                                    </td>
                                    {{~it.days:day:index2}}
                                    {{?it.courseItemList[index][index2].classEvent != ''}}
                                    <td class="point-fzzb forbidden-event" x="{{=index2 + 1}}" y="{{=index + 1}}">
                                        {{=it.courseItemList[index][index2].classEvent}}
                                    </td>
                                    {{??}}
                                    {{?it.courseItemList[index][index2].courseList.length > 0}}
                                        {{? it.courseItemList[index][index2].type == 7}}
                                        <td class="point-fzzb used-fzzb-point {{?it.courseItemList[index][index2].eventList.length > 0}}used-event-point{{?}}"
                                            x="{{=index2 + 1}}" y="{{=index + 1}}">
                                                <span>
                                                    <span>分层走班({{=it.courseItemList[index][index2].courseList.length}})</span><br>
                                                    {{?it.courseItemList[index][index2].eventList.length > 0}}
                                                    <span style="color: #ca0202;">事务</span>
                                                    {{?}}
                                                </span>
                                            <div class="fzzb-class-con">
                                                <ul>
                                                    {{~it.courseItemList[index][index2].courseList:course:index3}}
                                                    <li>{{=course.courseName}}<br>{{=course.teacherName}}<br>{{=course.classRoom}}</li>
                                                    {{~}}
                                                    {{~it.courseItemList[index][index2].eventList:event:index4}}
                                                    <li>{{=event.name}}</li>
                                                    {{~}}
                                                </ul>
                                            </div>
                                        </td>
                                        {{??}}
                                        <td class="point-fzzb used-point {{?it.courseItemList[index][index2].eventList.length > 0}}used-event-point{{?}}"
                                            x="{{=index2 + 1}}" y="{{=index + 1}}">
                                            {{?it.courseItemList[index][index2].eventList.length > 0}}
                                            <span style="color: #ca0202;">事务</span>
                                            <div class="zouban-class-con">
                                                <ul>
                                                    {{~it.courseItemList[index][index2].eventList:event:index4}}
                                                    <li>{{=event.name}}</li>
                                                    {{~}}
                                                </ul>
                                            </div>
                                            {{?}}
                                        </td>
                                        {{?}}
                                    {{??}}
                                    <td class="point-fzzb unused-point {{?it.courseItemList[index][index2].eventList.length > 0}}used-event-point{{?}}"
                                        x="{{=index2 + 1}}" y="{{=index + 1}}">
                                        {{?it.courseItemList[index][index2].eventList.length > 0}}
                                        <span style="color: #ca0202;">事务</span>
                                        <div class="zouban-class-con">
                                            <ul>
                                                {{~it.courseItemList[index][index2].eventList:event:index4}}
                                                <li>{{=event.name}}</li>
                                                {{~}}
                                            </ul>
                                        </div>
                                        {{?}}
                                    </td>
                                    {{?}}
                                    {{?}}
                                    {{~}}
                                </tr>
                                {{~}}
                                </tbody>
                            </script>
                        </div>
                    </div>
                </div>
                <!--=================================第四步排分组走课班end==================================-->
                <!--=================================第四步体育课start==================================-->
                <div class="tiyuke-con" id="tab-TYK">
                    <div class="fzbk-operate clearfix">
                        <div class="operate-right">
                            <%--<span class="class-clear" id="peClearTimetable">清空课程</span>--%>
                            <span class="class-bian" id="autoArrangePE">自动排课</span>
                        </div>
                    </div>
                    <div class="fzbk-main clearfix">
                        <div class="fzbk-left">
                            <span class="left-wpkc">待排课程</span>
                            <ul class="left-list" id="arrangingPECourseCtx"></ul>
                            <script id="arrangingPECourseTmpl" type="application/template">
                                {{~it:list:index}}
                                <li class="arranging-pe-before">
                                    {{~list:course:index2}}
                                    <span courseId="{{=course.id}}">{{=course.name}}</span><br>
                                    {{~}}
                                </li>
                                {{~}}
                            </script>
                        </div>
                        <div class="fzbk-right">
                            <table class="fzbk-table" id="peTimetableCtx"></table>
                            <script id="peTimetableTmpl" type="application/template">
                                <thead>
                                <tr>
                                    <th style="width:20%;">上课时间</th>
                                    {{~it.days:day:index}}
                                    <th style="width:11%;">{{=day}}</th>
                                    {{~}}
                                </tr>
                                </thead>
                                <tbody>
                                {{~it.sections:section:index}}
                                <tr>
                                    <td style="background:#ececec;">
                                        <span class="class-turn">{{=section}}</span>{{=it.classTime[index]}}
                                    </td>
                                    {{~it.days:day:index2}}
                                    {{?it.courseItemList[index][index2].classEvent != ''}}
                                    <td class="point-zouban forbidden-event" x="{{=index2 + 1}}" y="{{=index + 1}}">
                                        {{=it.courseItemList[index][index2].classEvent}}
                                    </td>
                                    {{??}}
                                    {{?it.courseItemList[index][index2].courseList.length > 0}}
                                        {{? it.courseItemList[index][index2].type == 4}}
                                        <td class="point-pe used-pe-point {{?it.courseItemList[index][index2].eventList.length > 0}}used-event-point{{?}}"
                                            x="{{=index2 + 1}}" y="{{=index + 1}}">
                                            <span>
                                                <span>体育({{=it.courseItemList[index][index2].courseList.length/2}})</span><br>
                                                {{?it.courseItemList[index][index2].eventList.length > 0}}
                                                <span style="color: #ca0202;">事务</span>
                                                {{?}}
                                            </span>
                                            <div class="pe-class-con">
                                                <ul>
                                                    {{~it.courseItemList[index][index2].courseList:course:index3}}
                                                    <li>{{=course.courseName}}<br>{{=course.teacherName}}<br></li>
                                                    {{~}}
                                                    {{~it.courseItemList[index][index2].eventList:event:index4}}
                                                    <li>{{=event.name}}</li>
                                                    {{~}}
                                                </ul>
                                            </div>
                                        </td>
                                        {{??}}
                                        <td class="point-pe used-point {{?it.courseItemList[index][index2].eventList.length > 0}}used-event-point{{?}}"
                                            x="{{=index2 + 1}}" y="{{=index + 1}}">
                                            {{?it.courseItemList[index][index2].eventList.length > 0}}
                                            <span style="color: #ca0202;">事务</span>
                                            <div class="zouban-class-con">
                                                <ul>
                                                    {{~it.courseItemList[index][index2].eventList:event:index4}}
                                                    <li>{{=event.name}}</li>
                                                    {{~}}
                                                </ul>
                                            </div>
                                            {{?}}
                                        </td>
                                        {{?}}
                                    {{??}}
                                    <td class="point-pe unused-point {{?it.courseItemList[index][index2].eventList.length > 0}}used-event-point{{?}}"
                                        x="{{=index2 + 1}}" y="{{=index + 1}}">
                                        {{?it.courseItemList[index][index2].eventList.length > 0}}
                                        <span style="color: #ca0202;">事务</span>
                                        <div class="zouban-class-con">
                                            <ul>
                                                {{~it.courseItemList[index][index2].eventList:event:index4}}
                                                <li>{{=event.name}}</li>
                                                {{~}}
                                            </ul>
                                        </div>
                                        {{?}}
                                    </td>
                                    {{?}}
                                    {{?}}
                                    {{~}}
                                </tr>
                                {{~}}
                                </tbody>
                            </script>
                        </div>
                    </div>
                </div>
                <!--=================================第四步体育课end==================================-->
                <!--=================================第四步排非走课班start==================================-->
                <div class="fzbk-con" id="tab-FZBK">
                    <div>
                        <a class="publish-table" id="publishTimetable">发布课表</a>
                        <span style="color: red;margin-left: 10px;display: none" id="tips">请排完所有课后发布课表</span>
                    </div>
                    <div class="fzbk-operate clearfix">
                        <label>班级</label>
                        <select id="classListCtx"></select>
                        <script id="classListTmpl" type="application/template">
                            {{~it:value:index}}
                            <option value="{{=value.id}}">{{=value.name}}</option>
                            {{~}}
                        </script>
                        <div class="operate-right">
                            <%--<span class="class-clear" id="clearFZBCourse">清空课程</span>--%>
                            <span class="class-bian" id="autoArrangeFZBCourse">自动排课</span>
                        </div>
                    </div>
                    <div class="fzbk-main clearfix">
                        <div class="fzbk-left">
                            <span class="left-wpkc">待排课程</span>
                            <ul class="left-list" id="arrangingFZBCourseCtx"></ul>
                            <script id="arrangingFZBCourseTmpl" type="application/template">
                                {{~it:list:index}}
                                <li class="arranging-fzb-before">
                                    {{~list:course:index2}}
                                    <span courseId="{{=course.id}}">{{=course.name}}</span><br>
                                    {{~}}
                                </li>
                                {{~}}
                            </script>
                        </div>
                        <div class="fzbk-right">
                            <table class="fzbk-table" id="fzbTimetableCtx"></table>
                            <script id="fzbTimetableTmpl" type="application/template">
                                <thead>
                                <tr>
                                    <th style="width:20%;">上课时间</th>
                                    {{~it.days:day:index}}
                                    <th style="width:11%;">{{=day}}</th>
                                    {{~}}
                                </tr>
                                </thead>
                                <tbody>
                                {{~it.sections:section:index}}
                                <tr>
                                    <td style="background:#ececec;">
                                        <span class="class-turn">{{=section}}</span>{{=it.classTime[index]}}
                                    </td>
                                    {{~it.days:day:index2}}
                                    {{?it.courseItemList[index][index2].classEvent != ''}}
                                    <td class="point-zouban forbidden-event" x="{{=index2 + 1}}" y="{{=index + 1}}">
                                        {{=it.courseItemList[index][index2].classEvent}}
                                    </td>
                                    {{??it.courseItemList[index][index2].type == 0}}
                                    <td class="point-fzb unused-point {{?it.courseItemList[index][index2].eventList.length > 0}}used-event-point{{?}}"
                                        x="{{=index2 + 1}}" y="{{=index + 1}}">
                                        <span>
                                            <span style="color: #ca0202;">
                                                {{?it.courseItemList[index][index2].eventList.length > 0}}事务{{?}}
                                            </span><br>
                                        </span>
                                        {{?it.courseItemList[index][index2].eventList.length > 0}}
                                        <div class="fzb-class-con">
                                            <ul>
                                                {{~it.courseItemList[index][index2].eventList:event:index4}}
                                                <li>{{=event.name}}</li>
                                                {{~}}
                                            </ul>
                                        </div>
                                        {{?}}
                                    </td>
                                    {{??it.courseItemList[index][index2].type == 1}}
                                    <td class="point-fzb fzb-zb-point {{?it.courseItemList[index][index2].eventList.length > 0}}used-event-point{{?}}"
                                        x="{{=index2 + 1}}" y="{{=index + 1}}">
                                        <span>
                                            {{?it.courseItemList[index][index2].courseList.length > 0}}
                                            <span>走班({{=it.courseItemList[index][index2].courseList.length}})</span><br>
                                            {{?}}
                                            <span style="color: #ca0202;">
                                                {{?it.courseItemList[index][index2].eventList.length > 0}}事务{{?}}
                                            </span><br>
                                        </span>
                                        {{?it.courseItemList[index][index2].courseList.length > 0 ||
                                        it.courseItemList[index][index2].eventList.length > 0}}
                                        <div class="fzb-class-con">
                                            <ul>
                                                {{~it.courseItemList[index][index2].courseList:course:index3}}
                                                <li>{{=course.courseName}}<br>{{=course.teacherName}}<br>{{=course.classRoom}}<br>
                                                </li>
                                                {{~}}
                                                {{~it.courseItemList[index][index2].eventList:event:index4}}
                                                <li>{{=event.name}}</li>
                                                {{~}}
                                            </ul>
                                        </div>
                                        {{?}}
                                    </td>
                                    {{??it.courseItemList[index][index2].type == 4}}
                                    <td class="point-fzb fzb-pe-point {{?it.courseItemList[index][index2].eventList.length > 0}}used-event-point{{?}}"
                                        x="{{=index2 + 1}}" y="{{=index + 1}}">
                                        <span>
                                            {{?it.courseItemList[index][index2].courseList.length > 0}}
                                            <span>体育</span><br>
                                            {{?}}
                                            <span style="color: #ca0202;">
                                                {{?it.courseItemList[index][index2].eventList.length > 0}}事务{{?}}
                                            </span><br>
                                        </span>
                                        {{?it.courseItemList[index][index2].courseList.length > 0 ||
                                        it.courseItemList[index][index2].eventList.length > 0}}
                                        <div class="fzb-class-con">
                                            <ul>
                                                {{~it.courseItemList[index][index2].courseList:course:index3}}
                                                <li>{{=course.courseName}}<br>{{=course.teacherName}}<br></li>
                                                {{~}}
                                                {{~it.courseItemList[index][index2].eventList:event:index4}}
                                                <li>{{=event.name}}</li>
                                                {{~}}
                                            </ul>
                                        </div>
                                        {{?}}
                                    </td>
                                    {{??it.courseItemList[index][index2].type == 2}}
                                    <td class="point-fzb {{?it.courseItemList[index][index2].courseList.length > 0}}used-fzb-point{{?}}
                                        {{?it.courseItemList[index][index2].eventList.length > 0}}used-event-point{{?}}"
                                        x="{{=index2 + 1}}" y="{{=index + 1}}">
                                        <span>
                                            {{?it.courseItemList[index][index2].courseList.length > 0}}
                                            <span>{{=it.courseItemList[index][index2].courseList[0].courseName}}</span><br>
                                            {{?}}
                                            <span style="color: #ca0202;">
                                                {{?it.courseItemList[index][index2].eventList.length > 0}}事务{{?}}
                                            </span><br>
                                        </span>
                                        {{?it.courseItemList[index][index2].courseList.length > 0 ||
                                        it.courseItemList[index][index2].eventList.length > 0}}
                                        <div class="fzb-class-con">
                                            <ul>
                                                {{~it.courseItemList[index][index2].courseList:course:index3}}
                                                <li>{{=course.courseName}}<br>{{=course.teacherName}}<br></li>
                                                {{~}}
                                                {{~it.courseItemList[index][index2].eventList:event:index4}}
                                                <li>{{=event.name}}</li>
                                                {{~}}
                                            </ul>
                                        </div>
                                        {{?}}
                                    </td>
                                    {{??it.courseItemList[index][index2].type == 8}}
                                    <td class="point-fzb {{?it.courseItemList[index][index2].courseList.length > 0}}used-fzb-point{{?}}
                                        {{?it.courseItemList[index][index2].eventList.length > 0}}used-event-point{{?}}"
                                        x="{{=index2 + 1}}" y="{{=index + 1}}">
                                        <span>
                                            {{?it.courseItemList[index][index2].courseList.length > 0}}
                                            <span>{{=it.courseItemList[index][index2].courseList[0].courseName}}</span>/
                                            <span>{{=it.courseItemList[index][index2].courseList[1].courseName}}</span><br>
                                            {{?}}
                                            <span style="color: #ca0202;">
                                                {{?it.courseItemList[index][index2].eventList.length > 0}}事务{{?}}
                                            </span><br>
                                        </span>
                                        {{?it.courseItemList[index][index2].courseList.length > 0 ||
                                        it.courseItemList[index][index2].eventList.length > 0}}
                                        <div class="fzb-class-con">
                                            <ul>
                                                {{~it.courseItemList[index][index2].courseList:course:index3}}
                                                <li>{{=course.courseName}}<br>{{=course.teacherName}}<br></li>
                                                {{~}}
                                                {{~it.courseItemList[index][index2].eventList:event:index4}}
                                                <li>{{=event.name}}</li>
                                                {{~}}
                                            </ul>
                                        </div>
                                        {{?}}
                                    </td>
                                    {{??it.courseItemList[index][index2].type == 7}}
                                    <td class="point-fzb fzb-zb-point {{?it.courseItemList[index][index2].eventList.length > 0}}used-event-point{{?}}"
                                        x="{{=index2 + 1}}" y="{{=index + 1}}">
                                        <span>
                                            {{?it.courseItemList[index][index2].courseList.length > 0}}
                                            <span>分层走班({{=it.courseItemList[index][index2].courseList.length}})</span><br>
                                            {{?}}
                                            <span style="color: #ca0202;">
                                                {{?it.courseItemList[index][index2].eventList.length > 0}}事务{{?}}
                                            </span><br>
                                        </span>
                                        {{?it.courseItemList[index][index2].courseList.length > 0 ||
                                        it.courseItemList[index][index2].eventList.length > 0}}
                                        <div class="fzb-class-con">
                                            <ul>
                                                {{~it.courseItemList[index][index2].courseList:course:index3}}
                                                <li>{{=course.courseName}}<br>{{=course.teacherName}}<br>{{=course.classRoom}}<br>
                                                </li>
                                                {{~}}
                                                {{~it.courseItemList[index][index2].eventList:event:index4}}
                                                <li>{{=event.name}}</li>
                                                {{~}}
                                            </ul>
                                        </div>
                                        {{?}}
                                    </td>
                                    {{?}}
                                    {{~}}
                                </tr>
                                {{~}}
                                </tbody>
                            </script>
                        </div>
                    </div>
                </div>
                <!--=================================第四步排非走课班end==================================-->
                <!--=====================================第四步-事务冲突start=================================-->
                <div class="ctsw-con" id="tab-CTSW">
                    <table class="conflict-table">
                        <caption>事务冲突列表</caption>
                        <thead>
                        <tr>
                            <th width="20%">事务名称</th>
                            <th width="20%">课程</th>
                            <th width="20%">老师</th>
                            <th width="20%">行政班</th>
                            <th width="20%">时间</th>
                        </tr>
                        </thead>
                        <tbody id="conflictListCtx"></tbody>
                        <script id="conflictListTmpl" type="application/template">
                            {{~it:value:index}}
                            <tr>
                                <td>{{=value.eventName}}</td>
                                <td>{{=value.courseName}}</td>
                                <td>{{=value.teacherName}}</td>
                                <td>{{=value.className}}</td>
                                <td>{{=value.timeStr}}</td>
                            </tr>
                            {{~}}
                        </script>
                    </table>
                </div>
                <!--=====================================第四步-事务冲突end=================================-->

                <!--=================================第四步排自习教室start==================================-->
                <div class="zxjs-con clearfix" id="tab-ZXJS">
                    <div class="zxjs-index">
                        <div class="operate-right">
                            <span class="class-bian">自动排课</span>
                        </div>
                        <table class="zxjs-table">
                            <tr>
                                <th style="width:10%;">序号</th>
                                <th style="width:23%;">时间</th>
                                <th style="width:10%;">人数</th>
                                <th style="width:34%;">自习教室</th>
                                <th style="width:23%;">操作</th>
                            </tr>
                            <tr>
                                <td>1</td>
                                <td>周一第1节</td>
                                <td>80</td>
                                <td>A302/A301</td>
                                <td>
                                    <a href="javascript:;" class="table-xq">详情</a>
                                    <em>|</em>
                                    <a href="javascript:;" class="table-edit">编辑</a>
                                </td>
                            </tr>
                            <tr>
                                <td>1</td>
                                <td>周一第1节</td>
                                <td>80</td>
                                <td>A302/A301</td>
                                <td>
                                    <a href="javascript:;" class="table-xq">详情</a>
                                    <em>|</em>
                                    <a href="javascript:;" class="table-edit">编辑</a>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <!--=================================第四步排自习教室详情start==================================-->
                    <div class="zxjs-xq">
                        <div class="zxjs-xq-title clearfix">
                            <a href="javascript:history.go(-1);">&lt;&nbsp;返回自习教室安排</a>
                            <span>发布课表</span>
                        </div>
                        <table class="zxjs-xq-table">
                            <tr>
                                <th style="width:25%;">学生姓名</th>
                                <th style="width:25%;">来自行政班</th>
                                <th style="width:25%;">自习教室</th>
                                <th style="width:25%;">操作</th>
                            </tr>
                            <tr>
                                <td style="background:#ececec;">张淮安</td>
                                <td>高二（1）班</td>
                                <td>A301</td>
                                <td>
                                    <a href="javascript:;" class="table-edit">编辑</a>
                                </td>
                            </tr>
                            <tr>
                                <td style="background:#ececec;">张淮安</td>
                                <td>高二（1）班</td>
                                <td>A301</td>
                                <td>
                                    <a href="javascript:;" class="table-xq-edit">编辑</a>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <!--=================================第四步排自习教室详情end==================================-->
                </div>

                <!--=================================第四步排自习教室end==================================-->
            </div>

        </div>
        <!--/.tab-col右侧-->
        <!--=================================第四步排自习教室编辑弹窗start==================================-->
        <div class="zxjs-alert">
            <div class="alert-title clearfix">
                <p>自习教室设置</p>
                <span class="alert-close">X</span>
            </div>
            <div class="alert-main">
                <h3 class="zxjs-alert-title">周一第1节</h3>
                <span class="zxjs-select">选择自习教室</span>

                <div class="label-wrap">
                    <label><input type="checkbox">&nbsp;A301</label>
                    <label><input type="checkbox">&nbsp;A302</label>
                    <label><input type="checkbox">&nbsp;A303</label>
                </div>
                <div class="alert-btn">
                    <button class="alert-btn-sure">确定</button>
                    <button class="alert-btn-qx">取消</button>
                </div>
            </div>
        </div>
        <!--=================================第四步排自习教室编辑弹窗end==================================-->
        <!--=================================第四步自习教室详情编辑弹窗start==================================-->
        <div class="xq-edit-alert">
            <div class="alert-title clearfix">
                <p>自习教室设置</p>
                <span class="alert-close">X</span>
            </div>
            <div class="alert-main">
                <h3 class="zxjs-alert-title">周一第1节</h3>
                <span class="zxjs-name">张淮安</span><em class="zxjs-grade">高二（1）班</em>
                <br>
                <span class="zxjs-room">自习教室</span>

                <div class="label-wrap">
                    <label><input type="radio" name="class-num">&nbsp;A301</label>
                    <label><input type="radio" name="class-num">&nbsp;A302</label>
                    <label><input type="radio" name="class-num">&nbsp;A303</label>
                </div>
            </div>
            <div class="alert-btn">
                <button class="alert-btn-sure">确定</button>
                <button class="alert-btn-qx">取消</button>
            </div>
        </div>
        <!--=================================第四步自习教室详情编辑弹窗end==================================-->
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
    seajs.use('paike', function (paike) {
        paike.init();
    });
</script>
</body>
</html>
