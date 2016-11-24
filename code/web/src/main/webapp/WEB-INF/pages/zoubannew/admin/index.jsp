<%--
  Created by IntelliJ IDEA.
  User: Wangkaidong
  Date: 2016/7/11
  Time: 14:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <title>3+3走班-基础设置</title>
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/css/reset.css" rel="stylesheet"/>
    <link href="/static_new/css/zouban/zoubannew.css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="/static_new/css/friendcircle/rome.css?v=1"/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
</head>
<body term="${param.term}" gradeId="${param.gradeId}">
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
            <div class="set-index">
                <div class="base-set">
                    <span class="index-title">全校基础设置</span>
                    <ul class="set-list clearfix">
                        <li id="JSGL">教室管理</li>
                        <li id="JXGL">教学周管理</li>
                        <li id="KBPZ">课表结构设置</li>
                    </ul>
                    <p class="warning-inf">请先完成全校基础设置，再进行排课</p>
                </div>
                <!--=====================================走班教务管理start=============================-->
                <div id="tab-ZNGL" class="tab-ZNGL">
                    <span class="index-title" style="margin-bottom: 28px;">排课</span>
                    <div class="ZNGL-top">
                        <%--学期--%>
                        <select id="termListCtx"></select>
                        <script id="termListTmpl" type="application/template">
                            {{~it:value:index}}
                            <option value="{{=value}}">{{=value}}</option>
                            {{~}}
                        </script>
                        <%--年级--%>
                        <select id="gradeListCtx"></select>
                        <script id="gradeListTmpl" type="application/template">
                            {{~it:value:index}}
                            <option value="{{=value.id}}">{{=value.name}}</option>
                            {{~}}
                        </script>
                    </div>
                    <div class="ZNGL-main">
                        <div class="ZNGL-info">
                            <ul class="step-list clearfix">
                                <li>
                                    <span class="step-num">第<em>1</em>步</span>
                                    <span class="step-name" id="step1">课程设置</span>
                                </li>
                                <li>
                                    <span class="step-num">第<em>2</em>步</span>
                                    <span class="step-name" id="step2">走班选课</span>
                                </li>
                                <li>
                                    <span class="step-num">第<em>3</em>步</span>
                                    <span class="step-name" id="step3">走班分班</span>
                                </li>
                                <li>
                                    <span class="step-num">第<em>4</em>步</span>
                                    <span class="step-name" id="step4">排课</span>
                                </li>
                                <li>
                                    <span class="step-num">第<em>5</em>步</span>
                                    <span class="step-name" id="step5">查看课表</span>
                                </li>
                                <li>
                                    <span class="step-num">第<em>6</em>步</span>
                                    <span class="step-name" id="step6">调课</span>
                                </li>
                            </ul>
                        </div>
                        <div class="ZNGL-bottm">
                            <dl>
                                <dt>
                                    <em>走班步骤说明：</em>
                                </dt>
                                <dd>
                                    <em class="ZNGL-bottom-I"></em><label>设置如学期、年级、教师、教室、和教学时间等基础数据；</label>
                                </dd>

                                <dd>
                                    <em class="ZNGL-bottom-II"></em><label>查看学生选课进度，相关教师可以调整学生选课结果；</label>
                                </dd>
                                <dd>
                                    <em class="ZNGL-bottom-III"></em><label>根据学校的实际情况（1.分段，给年级里所有的班级分成合理的几个分段；2.按照分段和学科自动编班；3.设置走班课和非走班课的老师；4.学生编班；5.设置走班课的教师）；设置如学期、年级、教师、教室和教学时间等基础数据；</label>
                                </dd>
                                <dd>
                                    <em class="ZNGL-bottom-IV"></em><label>根据学校的实际情况排课（1.设置课表结构；2.走班排课；3.非走班排课）；</label>
                                </dd>
                                <dd>
                                    <em class="ZNGL-bottom-V"></em><label>先筛选教学周，按照不同的条件查看课表，分三种课表（1.行政班课表；2.教学班课表；3.教师课表）；</label>
                                </dd>
                                <dd>
                                    <em class="ZNGL-bottom-VI"></em><label>调课，按照（1.临时周内调课；2.临时跨周调课；3.长期调课）三种类型调课。</label>
                                </dd>
                            </dl>
                        </div>
                    </div>
                </div>
                <!--================================走班教务管理end==============================-->
            </div>
            <a class="back-index" href="javascript:;" style="display: none;">&lt;返回全校基础设置</a>
            <div class="tab-main">
                <!--================================教室管理start===============================-->
                <div id="tab-JSGL" style="display: none;">
                    <div class="JSGL-top">
                        <span id="addClassroom" class="h-cursor">+新增</span>
                    </div>
                    <div class="ZNGL-main">
                        <div class="ZNGL-info">
                            <table>
                                <thead>
                                <tr>
                                    <th width="250">教室名称</th>
                                    <th width="250">关联行政班</th>
                                    <th width="150">操作</th>
                                </tr>
                                </thead>
                                <tbody id="classRoomListCtx">
                                </tbody>
                            </table>
                            <script id="classRoomListTmpl" type="application/template">
                                {{~it:value:index}}
                                <tr>
                                    <td>{{=value.roomName}}</td>
                                    <td>{{=value.className}}</td>
                                    <td>
                                        <em class="h-cursor editClassroom" classroomId="{{=value.id}}"
                                            classroomName="{{=value.roomName}}"
                                            className="{{=value.className}}" classId="{{=value.classId}}">编辑</em>|
                                        <em class="h-cursor deleteClassroom" classroomId="{{=value.id}}">删除</em>
                                    </td>
                                </tr>
                                {{~}}
                            </script>
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
                <!--============================教室管理end=================================-->
                <%--===========================教学周管理start=============================--%>
                <div id="tab-JXGL" style="display:none;">
                    <div class="cal-term">
                        <span>
                            学年<select id="termListCtx2"></select>
                        </span>

                        <div class="right3-1">设置教学周</div>
                    </div>

                    <div class="tip">
                        <span>温馨提示：设置教学周请在发布课表之前完成，每周课表将会根据此处配置生成。</span>
                    </div>
                    <div class="right3-2">
                        <span class="yearShow"></span>
                        <i id="firstTermWeek">第一学期&nbsp;共20周</i>
                        <span id="firstTermTime">2015-09-01~2016-01-24</span>
                    </div>
                    <div class="right3-3">
                        <div class="top-day">
                            <i>日</i>
                            <i>一</i>
                            <i>二</i>
                            <i>三</i>
                            <i>四</i>
                            <i>五</i>
                            <i>六</i>
                        </div>
                        <div class="cont-day">
                            <div class="weeklist" id="firstTermList">
                                <span>第1周</span>
                                <span>第2周</span>
                                <span>第3周</span>
                            </div>
                            <script type="application/template" id="termWeekTempJS">
                                {{~it:value:index}}
                                {{?index%7==0}}
                                <span>第{{=index/7+1}}周</span>
                                {{?}}
                                {{~}}
                            </script>
                            <table class="cont-date" id="firstTermData"></table>
                            <script type="application/template" id="termDateTempJS">
                                {{~it:value:index}}
                                {{?index%7==0}}
                                <tr>
                                    {{?}}
                                    <td>{{=value}}</td>
                                    {{?index%7==6}}
                                </tr>
                                {{?}}
                                {{~}}
                            </script>
                        </div>
                    </div>
                    <hr/>
                    <div class="right3-4">
                        <span class="yearShow"></span>
                        <i id="secondTermWeek">第二学期&nbsp;共20周</i>
                        <span id="secondTermTime">2015-03-01~2016-07-24</span>
                    </div>
                    <div class="right3-5">
                        <div class="top-day">
                            <i>日</i>
                            <i>一</i>
                            <i>二</i>
                            <i>三</i>
                            <i>四</i>
                            <i>五</i>
                            <i>六</i>
                        </div>
                        <div class="cont-day">
                            <div class="weeklist" id="secondTermList"></div>
                            <table class="cont-date" id="secondTermData"></table>
                        </div>
                    </div>
                </div>
                <%--=============================教学周管理end================================--%>
                <%--=============================课表结构配置start================================--%>
                <div id="tab-KBPZ" style="display: none;">
                    <div class="zouban-addbtn">
                        <ul class="clearfix" style="width: 252px;">
                            <li class="subject-manage zb-active" id="SUB">①课表结构设置</li>
                            <li class="teacher-set" id="TES">②事务设置</li>
                        </ul>
                    </div>
                    <div class="ZNGL-top">
                        <div id="kb">
                            <%--学期--%>
                            <select id="kbTermList"></select>
                            <%--年级--%>
                            <select id="kbGradeListCtx"></select>
                            <script id="kbGradeListTmpl" type="application/template">
                                <option value="All">全校</option>
                                {{~it:value:index}}
                                <option value="{{=value.id}}">{{=value.name}}</option>
                                {{~}}
                            </script>
                            <select id="kbClassListCtx"></select>
                            <script id="kbClassListTmpl" type="application/template">
                                <option value="All">全年级</option>
                                {{~it:value:index}}
                                <option value="{{=value.id}}">{{=value.name}}</option>
                                {{~}}
                            </script>
                        </div>
                        <div id="sw" style="display: none;">
                            <select id="kbTermList2"></select>
                        </div>
                    </div>
                    <div>
                        <div class="kbjg-con" id="tab-KBJG" timetableConfId="">
                            <div class="class-day" id="classDay">
                                <span>上课天数</span>
                                <label><input type="checkbox" name="days" value="1">&nbsp;周一</label>
                                <label><input type="checkbox" name="days" value="2">&nbsp;周二</label>
                                <label><input type="checkbox" name="days" value="3">&nbsp;周三</label>
                                <label><input type="checkbox" name="days" value="4">&nbsp;周四</label>
                                <label><input type="checkbox" name="days" value="5">&nbsp;周五</label>
                                <label><input type="checkbox" name="days" value="6">&nbsp;周六</label>
                                <label><input type="checkbox" name="days" value="7">&nbsp;周日</label>
                            </div>
                            <div class="class-num" id="classNum">
                                <span>每天节数</span>
                                <select id="classCountSelect">
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                    <option value="5">5</option>
                                    <option value="6">6</option>
                                    <option value="7">7</option>
                                    <option value="8">8</option>
                                    <option value="9">9</option>
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                </select>
                                <a href="javascript:;" class="lock-conf" id="lock">保存</a>
                            </div>
                            <div class="kbjg-main clearfix">
                                <table class="kbjg-table-1">
                                    <thead>
                                    <tr>
                                        <th style="width:23%">上课时间</th>
                                        <th style="width:11%;">周一</th>
                                        <th style="width:11%;">周二</th>
                                        <th style="width:11%;">周三</th>
                                        <th style="width:11%;">周四</th>
                                        <th style="width:11%;">周五</th>
                                        <th style="width:11%;">周六</th>
                                        <th style="width:11%;">周日</th>
                                    </tr>
                                    </thead>
                                    <tbody id="timetableCtx"></tbody>
                                </table>

                                <script type="application/template" id="timetableTmpl">
                                    {{~it.section:section:index}}
                                    <tr>
                                        <td style="background:#ececec;">
                                            <span class="class-turn">{{=section}}</span>
                                            <input name="classTime" value="{{=it.classTime[index]}}">
                                        </td>
                                        {{~it.usedDays:day:index2}}
                                        {{?day}}
                                            {{?it.eventList[index]}}
                                            {{?it.eventList[index][index2]}}
                                            {{?it.eventList[index][index2].length > 0}}
                                            <td class="even even-color" x="{{=index2+1}}" y="{{=index+1}}" ><span>{{=it.eventList[index][index2][0].name}}</span></td>
                                            {{??}}
                                            <td class="even" x="{{=index2+1}}" y="{{=index+1}}"></td>
                                            {{?}}
                                            {{??}}
                                            <td></td>
                                            {{?}}
                                            {{??}}
                                            <td></td>
                                            {{?}}
                                        {{??}}
                                        <td class="zouban-unuse"></td>
                                        {{?}}
                                        {{~}}
                                    </tr>
                                    {{~}}
                                </script>
                                <script type="application/template" id="classEventTmpl">
                                    {{~it.section:section:index}}
                                    <tr>
                                        <td style="background:#ececec;">
                                            <span class="class-turn">{{=section}}</span>
                                            <input name="classTime" value="{{=it.classTime[index]}}">
                                        </td>
                                        {{~it.usedDays:day:index2}}
                                        {{?day}}
                                        {{?it.eventList[index][index2].length > 0}}
                                        <td class="even even-color" x="{{=index2+1}}" y="{{=index+1}}" ><span>{{=it.eventList[index][index2][0].name}}</span></td>
                                        {{?}}
                                        {{?it.eventList[index][index2].length <= 0}}
                                        <td class="even" x="{{=index2+1}}" y="{{=index+1}}"></td>
                                        {{?}}
                                        {{??}}
                                        <td class="zouban-unuse"></td>
                                        {{?}}
                                        {{~}}
                                    </tr>
                                    {{~}}
                                </script>
                                <script type="application/template" id="eventClassTmpl">
                                    {{~it.section:section:index}}
                                    <tr>
                                        <td style="background:#ececec;">
                                            <span class="class-turn">{{=section}}</span>
                                            <input name="classTime" value="{{=it.classTime[index]}}">
                                        </td>
                                        {{~it.usedDays:day:index2}}
                                        {{?day}}
                                        {{?it.eventList[index][index2].length > 0}}
                                        <td class="evenClass even-color" x="{{=index2+1}}" y="{{=index+1}}" ><span>{{=it.eventList[index][index2][0].name}}</span></td>
                                        {{?}}
                                        {{?it.eventList[index][index2].length <= 0}}
                                        <td class="evenClass" x="{{=index2+1}}" y="{{=index+1}}"></td>
                                        {{?}}
                                        {{??}}
                                        <td class="zouban-unuse"></td>
                                        {{?}}
                                        {{~}}
                                    </tr>
                                    {{~}}
                                </script>
                            </div>
                        </div>
                        <div id="tab-SWSZ" class="xzsy-con" style="display:none;">
                            <a href="javascript:;" class="new-add"><em>＋</em>新增事宜</a>
                            <table class="kbjg-table-2">
                                <thead>
                                <tr>
                                    <th style="width:25%;">名称</th>
                                    <th style="width:50%;">时间点</th>
                                    <th style="width:25%;">操作</th>
                                </tr>
                                </thead>
                                <tbody id="eventListCtx"></tbody>
                            </table>
                            <script id="eventListTmpl" type="application/template">
                                {{~it:value:index}}
                                <tr>
                                    <td style="background:#ececec;">{{=value.name}}</td>
                                    <td>
                                        {{~value.timeList:time:index2}}
                                        <span>{{=time}}</span>
                                        {{~}}
                                    </td>
                                    <td>
                                        <a href="javascript:;" class="table-edit editEvent" eventId={{=value.id}}>编辑</a>
                                        <em>|</em>
                                        <a href="javascript:;" class="table-del delEvent" eventId="{{=value.id}}">删除</a>
                                    </td>
                                </tr>
                                {{~}}
                            </script>
                        </div>
                    </div>
                </div>
                <%--=============================课表结构配置end================================--%>
            </div>
        </div>
        <!--/.tab-col右侧-->
        <!--=================================第四步事务设置弹窗start==================================-->
        <div class="classset-alert" id="setEventWindow" eventId="">
            <div class="alert-title clearfix">
                <p>事务设置</p>
                <span class="alert-close">X</span>
            </div>
            <div class="alert-main">
                <span class="alert-main-title">${term}<em>${gradeName}</em></span>

                <div class="alert-main-name">
                    <span>名称</span>
                    <input type="text" value="" id="eventName">
                </div>
                <div id="eventTime">
                    <div class="table-wrap">
                        <table class="classset-table" id="eventTimeCtx"></table>
                        <script id="eventTimeTmpl" type="application/template">
                            <thead>
                            <tr>
                                <th <%--style="width:20%;"--%>>&nbsp;</th>
                                {{~it.eventTimeHead:time:index}}
                                <th <%--style="width:16%;"--%>>{{=time}}</th>
                                {{~}}
                            </tr>
                            </thead>
                            <tbody>
                            {{~it.eventTimeSection:section:index2}}
                            <tr>
                                <td>{{=section}}</td>
                                {{~it.eventTimeHead:time2:index3}}
                                <td>
                                    <label>
                                        <input type="checkbox" name="time" x="{{=index3 + 1}}" y="{{=index2 + 1}}">&nbsp;{{=index3 + 1}}.{{=index2 + 1}}
                                    </label>
                                </td>
                                {{~}}
                            </tr>
                            {{~}}
                            </tbody>
                        </script>
                    </div>
                    <div class="alert-btn">
                        <button class="alert-btn-next">下一步</button>
                        <button class="alert-btn-qx">取消</button>
                    </div>
                </div>
                <div id="eventTeacher" class="alert-next">
                    <div class="table-wrap" style="width:420px;overflow-x: auto">
                        <table>
                            <tbody id="subjectTeacherCtx"></tbody>
                            <script id="subjectTeacherTmpl" type="application/template">
                                {{~it:value:index}}
                                <tr>
                                    <td id="{{=value.subjectId}}" class="td-sub">{{=value.subjectName}}</td>
                                    <td><label><input type="checkbox" name="AllSelect" class="AllSelect">全选</label>
                                    {{~value.teacherList:teacher:index2}}
                                    <td>
                                        <label><input class="selectTeacher" type="checkbox" name="teacher" value="{{=teacher.id}}" teacherName="{{=teacher.name}}">&nbsp;{{=teacher.name}}</label>
                                    </td>
                                    {{~}}
                                </tr>
                                {{~}}
                            </script>
                        </table>
                    </div>
                    <div class="alert-btn">
                        <button class="alert-btn-prev">上一步</button>
                        <button class="alert-btn-sure" id="submitEvent">确定</button>
                        <button class="alert-btn-qx">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <!--=================================第四步事务设置弹窗end==================================-->

    </div>
    <!--/.col-right-->

</div>
<!--/#content-->
<!--#foot-->
<%@ include file="../../common_new/foot.jsp" %>
<!--#foot-->
<div class="bg"></div>

<!--============================教室管理弹==================================-->
<div class="popup-JSGL">
    <div class="popup-JSGL-top">
        <em>教室管理</em><i class="h-cursor closeClassRoom">X</i>
    </div>
    <div class="popup-JSGL-main">
        <dl>
            <dd>
                <em>教室名称</em><input id="classroomName" type="text">
            </dd>
            <dd>
                <em>关联行政班</em>
                <select id="classListCtx"></select>
                <script id="classListTmpl" type="application/template">
                    {{~it:value:index}}
                    <option value="{{=value.id}}">{{=value.className}}</option>
                    {{~}}
                    <option value="5404a60cf6f28b7261cfad53">其他</option>
                </script>
            </dd>
        </dl>
    </div>
    <div class="popup-JSGL-bottom">
        <span class="JSGL-TJ h-cursor" classroomId="">提交</span>
        <span class="JSGL-QX h-cursor closeClassRoom">取消</span>
    </div>
</div>
<!--===============================教室管理弹窗框end==============================-->
<%--========================================教学周弹窗start=======================================--%>
<div class="weekwind">
    <div class="hide-top">设置教学周<em class="close">×</em></div>
    <dl>
        <dd><span id="term"></span></dd>
        <dd><span>第一学期</span><span id="firstWeek">共20周</span></dd>
        <dd><span>开学日期</span><input type="text" readonly="true" id="firstStart"/></dd>
        <dd><span>结束日期</span><input type="text" readonly="true" id="firstEnd"/></dd>
        <dd><span>第二学期</span><span id="secondWeek">共20周</span></dd>
        <dd><span>开学日期</span><input type="text" readonly="true" id="secondStart"/></dd>
        <dd><span>结束日期</span><input type="text" readonly="true" id="secondEnd"/></dd>
        <dd>
            <button class="week-conf" termId="">确定</button>
            <button class="close">取消</button>
        </dd>
    </dl>
</div>
<%--========================================教学周弹窗end=====================================--%>


<!-- Javascript Files -->
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js"></script>
<script>
    seajs.use('zoubanIndex', function (zoubanIndex) {
        zoubanIndex.init();
    });
</script>

</body>
</html>