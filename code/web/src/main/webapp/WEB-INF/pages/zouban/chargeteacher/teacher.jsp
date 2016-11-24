<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<html>
<head>
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn"/>
    <title>走班选课</title>
    <meta name="description" content="">
    <meta name="author" content=""/>
    <meta name="copyright" content=""/>
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static/css/homepage.css?v=1" rel="stylesheet"/>
    <link href="/static_new/css/reset.css?v=1" rel="stylesheet"/>
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="/static_new/css/zouban/charge-teacher.css?v=2015041602" rel="stylesheet"/>
    <%--<link href="/static_new/css/zouban/course-teacher.css?v=2015041602" rel="stylesheet"/>--%>

    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>
<body>
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
        <input hidden="hidden" id="xuankeid">
        <!--.tab-col-->
        <input type="hidden" id="gradeId">
        <input type="hidden" id="classId">
        <input type="hidden" id="termVal" value="${term}">
        <input type="hidden" id="yearVal" value="${year}">
        <input type="hidden" id="curweek" value="${curweek}">
        <input type="hidden" id="allweek" value="${allweek}">
        <div class="cont-right">
            <ul class="right-title">
                <li class="main1-li main1-l1 cur">
                    本班课表
                </li>
                <li class="main2-li main1-l2">
                    本班学生去向
                </li>
                <li class="main3-li main1-l3">
                    本班选课进度
                </li>
                <li class="main4-li main1-l3">
                    选课课程公示
                </li>
                <li class="main5-li main1-l2">
                    我的课表
                </li>
                <li class="main6-li main1-l2">
                    教学班信息
                </li>
            </ul>
            <div class="right-main1">
                <div class="main1-1">
                    <span class="termShow">${term}</span>
                </div>
                <div class="main1-2">
                    类型<select class="showType">
                    <option value="3">全部</option>
                    <option value="0">走班</option>
                    <option value="2">非走班</option>
                </select>
                    <em>学生</em><select id="stuList">
                    <option></option>
                    <option></option>
                </select>
                    <script type="application/template" id="stuTemJs">
                        <option value="0">全部</option>
                        {{~it.data:value:index}}
                        <option value="{{=value.id}}">{{=value.userName}}</option>
                        {{~}}
                    </script>
                    <select id="weekShow" style="height: 35px;">

                    </select>
                    <script type="application/template" id="weekShowTempJs">
                        {{~it.data:value:index}}
                        <option value="{{=value.id}}">{{=value.name}}</option>
                        {{~}}
                    </script>
                    <%--<input type="text" class="text1">--%>
                    <button id="getTimetable">筛选</button>
                    <button id="exportTable">导出</button>
                    <%--<button>明细</button>--%>
                </div>
                <script type="application/template" id="classTableTempJs">
                    <tr class="row1">
                        <td class="col1">节次/时间</td>
                        {{~it.data.conf.classDays:value:index}}
                        <td class="col">星期{{?value==1}}一
                            {{??value==2}}二
                            {{??value==3}}三
                            {{??value==4}}四
                            {{??value==5}}五
                            {{??value==6}}六
                            {{??value==7}}日
                            {{?}}</td>
                        {{~}}
                    </tr>
                    {{~it.data.conf.classTime:value:index}}
                    <tr class="row">
                        <td class="col1">
                            第{{=index+1}}节
                            <br />{{=it.data.conf.classTime[index]}}
                        </td>

                        {{?it.type==3}}<%--全部--%>
                        {{~it.data.conf.classDays:value1:index1}}
                        <td class="col col-class" style="cursor: pointer">
                            {{~it.data.course:value2:index2}}
                                {{?value2.xIndex==value1 && value2.yIndex==(index+1)}}
                                    {{?value2.type==0}}<%--走班--%>
                                        <div class="zbtd zb" x="{{=value1}}" y="{{=index+1}}" >{{=value2.className}}<br/>{{=value2.classRoom}}</div>
                                    {{??value2.type==2}}<%--非走班--%>
                                        {{=value2.className}}<br/>{{=value2.teacherName}}
                                    {{??value2.type==4}}<%--体育走班--%>
                                        <div class="zbtd tyzb" x="{{=value1}}" y="{{=index+1}}" >{{=value2.className}}</div>
                                    {{??value2.type==5}}
                                        {{=value2.className}}
                                    {{?}}
                                {{?}}
                            {{~}}
                            {{~it.data.conf.events:value3:index3}}
                                {{?value3.xIndex==value1 && value3.yIndex==index+1}}
                                    {{?value3.forbidEvent.length==1}}
                                        {{=value3.forbidEvent[0]}}
                                    {{?}}
                                {{?}}
                            {{~}}
                        </td>
                        {{~}}
                        {{??it.type==0}}<%--走班--%>
                        {{~it.data.conf.classDays:value1:index1}}
                        <td class="col col-class" style="cursor: pointer">
                            {{~it.data.course:value2:index2}}
                                {{?value2.xIndex==value1 && value2.yIndex==(index+1)&&value2.type==0}}
                                    <div class="zbtd zb" x="{{=value1}}" y="{{=index+1}}" >{{=value2.className}}<br/>{{=value2.classRoom}}</div>
                                {{??value2.xIndex==value1 && value2.yIndex==(index+1)&&value2.type==4}}<%--体育走班--%>
                                    <div class="zbtd tyzb" x="{{=value1}}" y="{{=index+1}}" >{{=value2.className}}</div>
                                {{?}}
                            {{~}}
                            {{~it.data.conf.events:value3:index3}}
                                {{?value3.xIndex==value1 && value3.yIndex==index+1}}
                                    {{?value3.forbidEvent.length==1}}
                                        {{=value3.forbidEvent[0]}}
                                    {{?}}
                                {{?}}
                            {{~}}
                        </td>
                        {{~}}
                        {{??it.type==2}}<%--非走班--%>
                        {{~it.data.conf.classDays:value1:index1}}
                        <td class="col col-class" style="cursor: pointer">
                            {{~it.data.course:value2:index2}}
                                {{?value2.xIndex==value1 && value2.yIndex==(index+1) && value2.type==2}}
                                    {{=value2.className}}<br/>{{=value2.teacherName}}
                                {{?}}
                            {{~}}
                            {{~it.data.conf.events:value3:index3}}
                                {{?value3.xIndex==value1 && value3.yIndex==index+1}}
                                    {{?value3.forbidEvent.length==1}}
                                        {{=value3.forbidEvent[0]}}
                                    {{?}}
                                {{?}}
                            {{~}}
                        </td>
                        {{~}}
                        {{?}}
                    </tr>
                    {{~}}
                </script>
                <script type="application/template" id="tempJs">
                    <tr class="row1">
                        <td class="col1">节次/时间</td>
                        {{~it.data.conf.classDays:value:index}}
                        <td class="col">星期{{?value==1}}一
                            {{??value==2}}二
                            {{??value==3}}三
                            {{??value==4}}四
                            {{??value==5}}五
                            {{??value==6}}六
                            {{??value==7}}日
                            {{?}}</td>
                        {{~}}
                    </tr>
                    {{~it.data.conf.classTime:value:index}}
                    <tr class="row">
                        <td class="col1">
                            第{{=index+1}}节
                            <span>{{=it.data.conf.classTime[index]}}</span>
                        </td>
                        {{?it.type==3}}
                            {{~it.data.conf.classDays:value1:index1}}
                                <td class="col">
                                    {{~it.data.course:value2:index2}}
                                        {{?value2.xIndex==value1 && value2.yIndex==(index+1)}}
                                            <i>{{=value2.className}}</i><br/>
                                            {{?value2.teacherName!=""}}
                                                ({{=value2.teacherName}})<br/>
                                            {{?}}
                                            {{=value2.classRoom}}
                                        {{?}}
                                    {{~}}
                                    {{~it.data.conf.events:value3:index3}}
                                        {{?value3.xIndex==value1 && value3.yIndex==index+1}}
                                            {{?value3.forbidEvent.length==1}}
                                                {{=value3.forbidEvent[0]}}
                                            {{?}}
                                        {{?}}
                                    {{~}}
                                </td>
                            {{~}}
                        {{??it.type==0}}
                            {{~it.data.conf.classDays:value1:index1}}
                                <td class="col">
                                    {{~it.data.course:value2:index2}}
                                        {{?value2.xIndex==value1 && value2.yIndex==(index+1) && value2.type==0}}
                                            <i>{{=value2.className}}</i><br/>
                                            ({{=value2.teacherName}})<br/>
                                            {{=value2.classRoom}}
                                        {{?}}
                                    {{~}}
                                    {{~it.data.conf.events:value3:index3}}
                                        {{?value3.xIndex==value1 && value3.yIndex==index+1}}
                                            {{?value3.forbidEvent.length==1}}
                                                {{=value3.forbidEvent[0]}}
                                            {{?}}
                                        {{?}}
                                    {{~}}
                                </td>
                            {{~}}
                        {{??it.type==2}}
                            {{~it.data.conf.classDays:value1:index1}}
                                <td class="col">
                                    {{~it.data.course:value2:index2}}
                                        {{?value2.xIndex==value1 && value2.yIndex==(index+1)&& value2.type==2}}
                                            <i>{{=value2.className}}</i><br/>
                                            {{?value2.teacherName!=""}}
                                                ({{=value2.teacherName}})<br/>
                                            {{?}}
                                            {{=value2.classRoom}}
                                        {{?}}
                                    {{~}}
                                    {{~it.data.conf.events:value3:index3}}
                                        {{?value3.xIndex==value1 && value3.yIndex==index+1}}
                                            {{?value3.forbidEvent.length==1}}
                                                {{=value3.forbidEvent[0]}}
                                            {{?}}
                                        {{?}}
                                    {{~}}
                                </td>
                            {{~}}
                        {{?}}
                    </tr>
                    {{~}}
                </script>
                <table class="main1-3">
                </table>
                <div id="noticeShow2" style="margin: 35px;">
                </div>
            </div>

            <div class="right-main2">
                <div class="main2-1">
                    <span class="termShow">${term}</span>
                </div>
                <div class="main2-2">
                    <em>科目</em>
                    <select id="subjectShow">
                    </select>
                    <script type="application/template" id="sublistTempJs">
                        {{~it.data:value:index}}
                                <option value="{{=value.id}}">{{=value.name}}</option>
                        {{~}}
                    </script>
                    <button id="getStudentList">筛选</button>
                    <%--<button>导出</button>--%>
                </div>
                <script type="application/template" id="stuChooseTempJs">

                    <tr class="row1">
                        <td class="col1">学生姓名</td>
                        <td class="col2">教学班</td>
                        <td class="col3">任课老师</td>
                        <td class="col4">上课教室</td>
                    </tr>
                    {{~it.data:value:index}}
                    <tr>
                        <td>{{=value.studentName}}</td>
                        <td>{{=value.courseName}}</td>
                        <td>{{=value.teacherName}}</td>
                        <td>{{=value.classRoom}}</td>
                    </tr>
                    {{~}}
                </script>
                <table class="main2-3">
                </table>
            </div>


            <div class="right-main3">
                <div class="main3-1">
                    <span class="termShow">${term}</span>
                </div>
                <div class="main3-2">
                    <em id="wwc">未完成选课：8&nbsp;/&nbsp;50</em>
                    <span id="timeShow">开放时间：2015-08-25~2015-09-01</span>
                    <button class="check-list">查看名单</button>
                </div>
                <script type="application/template" id="subjectConfTempJs">
                    <tr class="row1">
                        <td class="col1">科目</td>
                        <td class="col2">等级考</td>
                        <td class="col3">合格考</td>
                        <td class="col4">学生名单</td>
                    </tr>
                    {{~it.data:value:index}}
                    <tr>
                        <td>{{=value.subjectName}}</td>
                        <td>{{=value.advUserCount}}</td>
                        <td>{{=value.simUserCount}}</td>
                        <td><em class="tab-detial" snm="{{=value.subjectName}}" sid="{{=value.subjectId}}">明细</em></td>
                    </tr>
                    {{~}}
                </script>
                <table class="main3-3">
                </table>
            </div>

            <div class="main3a">
                <div class="main3a-1">
                    < 返回
                </div>
                <div class="main3a-2">
                    <span class="termShow">${term}</span>
                </div>
                <table class="main3a-3">

                </table>
                <script type="application/template" id="xuankeDetailTempJs">
                    <tr class="row1">
                        <td class="col1">学生姓名</td>
                        <td class="col2">等级考</td>
                        <td class="col3">合格考</td>
                        <td class="col4">操作</td>
                    </tr>
                    {{~it.data:value:index}}
                    <tr>
                        <td>{{=value.username}}</td>
                        <td>{{=value.advName}}</td>
                        <td>{{=value.simName}}</td>
                        <td><%--{{?value.advName==""}}--%><em class="tab-choose" uid="{{=value.userid}}">选课</em><%--{{?}}--%></td>
                    </tr>
                    {{~}}
                </script>
            </div>

            <div class="right-main4">
                <div class="main3-1">
                    <span class="termShow">${term}</span>
                </div>
                <div class="main4-2">
                    <%--选修等级考数量：<span id="ac" style="margin-right: 30px;">3</span>合格考数量：<em id="sc">3</em>--%>
                    开放时间：<span id="time">2015-08-25~2015-09-01</span>
                </div>
                <script type="application/template" id="gongshiTempJs">
                    <tr class="row0">
                        <td class="col1">科目</td>
                        <td class="col2">合格考每周课时</td>
                        <td class="col3">等级考每周课时</td>
                        <td class="col4">是否分层</td>
                        <td class="col5">说明</td>
                    </tr>
                    {{~it.data:value:index}}
                    <tr>
                        <td>{{=value.courseName}}</td>
                        <td>{{=value.simpleClassTime}}</td>
                        <td>{{=value.advanceClassTime}}</td>
                        <td>{{?value.fengceng==1}}是{{??}}否{{?}}</td>
                        <td>{{=value.remark}}</td>
                    </tr>
                    {{~}}
                </script>
                <table class="main4-3">

                </table>
            </div>

            <%--个人课表--%>
            <div class="right-main5">
                <div class="main1-1">
                    <span class="termShow">${term}</span>
                    <span style="margin-left: 20px;">年级</span>
                    <select id="gradeShow">

                    </select>
                    <script type="application/template" id="gradeTempJs">
                        {{~it.data:value:index}}
                        <option value="{{=value.id}}">{{=value.name}}</option>
                        {{~}}
                    </script>
                    <select id="weekShow2" style="height: 35px;">

                    </select>

                    <button id="exportTable1">导出</button>
                    <button id="getDetail">筛选</button>
                </div>
                <script type="application/template" id="teacherTableTempJs">
                    <tr class="row1">
                        <td class="col1">节次/时间</td>
                        {{~it.data.conf.classDays:value:index}}
                        <td class="col">星期{{?value==1}}一
                            {{??value==2}}二
                            {{??value==3}}三
                            {{??value==4}}四
                            {{??value==5}}五
                            {{??value==6}}六
                            {{??value==7}}日
                            {{?}}</td>
                        {{~}}
                    </tr>
                    {{~it.data.conf.classTime:value:index}}
                    <tr class="row">
                        <td class="col1">
                            第{{=index+1}}节
                            <br />{{=it.data.conf.classTime[index]}}
                        </td>
                        {{~it.data.conf.classDays:value1:index1}}
                        <td class="col">
                            {{~it.data.course:value2:index2}}
                                {{?value2.xIndex==value1 && value2.yIndex==(index+1)}}
                                    {{=value2.className}}<br/>{{=value2.classRoom}}
                                {{?}}
                            {{~}}
                            {{~it.data.conf.events:value3:index3}}
                                {{?value3.xIndex==value1 && value3.yIndex==index+1}}
                                    {{?value3.forbidEvent.length==1}}
                                        {{=value3.forbidEvent[0]}}
                                    {{?}}
                                {{?}}
                            {{~}}
                            {{~it.sub:value4:index4}}
                                {{?value4.x==value1&&value4.y==index+1}}
                                    <br>集体教研
                                {{?}}
                            {{~}}
                        </td>
                        {{~}}
                    </tr>
                    {{~}}
                </script>
                <table class="main5-3">
                </table>
                <div id="noticeShow" style="margin: 35px;">
                </div>
                <script type="application/template" id="noticeTempJs">
                    <dd>
                        <em>调课通知</em>
                    </dd>
                    <dd>
                        <em>说明：因特殊原因，对课程进行调整，具体调整如下：</em>
                    </dd>
                    <dd class="asjust-tiaozheng">
                        <table>
                            <tr>
                                <th>班级</th>
                                <th>调整课程</th>
                                <th>任课教师</th>
                                <th>原上课时间</th>
                                <th>新上课时间</th>
                            </tr>
                            {{~it.data:value:index}}
                            <tr>
                                <td>{{=value.cl}}</td>
                                <td>{{=value.co}}</td>
                                <td>{{=value.te}}</td>
                                <td>{{=value.ot}}</td>
                                <td>{{=value.nt}}</td>
                            </tr>
                            {{~}}
                        </table>
                    </dd>
                </script>
            </div>

            <div class="right-main6">
                <div class="main2-1 termShow">
                    2015-2016学年&nbsp;xxx学期
                </div>

                <table class="main6-3">
                </table>
                <script type="application/template" id="courseTempJs">
                    <tr class="row1">
                        <td class="col1">教学班</td>
                        <td class="col2">人数</td>
                        <td class="col3">每周课时</td>
                        <td class="col4">上课教室</td>
                        <td class="col5">学生名单</td>
                    </tr>
                    {{~it.data:value:index}}
                    <tr>
                        <td>{{=value.courseName}}</td>
                        <td>{{=value.courseStudentAmount}}</td>
                        <td>{{=value.courseClassTimes}}</td>
                        <td>{{=value.classRoom}}</td>
                        <td><em cid="{{=value.courseId}}">查看</em></td>
                    </tr>
                    {{~}}
                </script>
            </div>

            <div class="main2a">

                <div class="main2a-1">
                    < 返回
                </div>
                <div class="main2a-2 termShow">
                    ${term}
                </div>
                <div class="main2a-3">
                    <span id="s1">物理-等级考-A1</span><em id="s2">教室：高一1班教室</em> <em id="s3">人数：40</em>
                </div>
                <table class="main2a-4">

                </table>
                <script type="application/template" id="detailTempJs2">
                    <tr class="row1">
                        <td>学生姓名</td>
                        <td>性别</td>
                        <td>所属行政班</td>
                    </tr>
                    {{~it.data:value:index}}
                    <tr>
                        <td>{{=value.studentName}}</td>
                        <td>{{?value.studentSex==0}}女{{??value.studentSex==1}}男{{?}}</td>
                        <td>{{=value.studentClass}}</td>
                    </tr>
                    {{~}}
                </script>
            </div>

            <div class="right-main7">
                <i class="no-class">本学期选课未开放</i>
            </div>
        </div>

        <!--/.tab-col-->
    </div>
    <!--/.col-right-->

</div>
<!--选课弹窗start-->
<div class="main3-hide">
    <input id="userId" type="hidden">
    <div class="hide3-1">
        <em>选课</em>
        <i>×</i>
    </div>
    <script type="application/template" id="xuankeTempJs">
        <dd>
            <span>{{=it.stuName}}</span>
        </dd>
        <dd>
            <em>已选课程</em>
        </dd>
        <dd class="dl2">
            <em>等级考课程:</em>
                {{~it.data.adv:value:index}}
                <span>{{=value.value}}</span>
                {{~}}
        </dd>
        <dd class="dl3">
            <em>合格考课程:</em>
                {{~it.data.sim:value:index}}
                <span>{{=value.value}}</span>
                {{~}}
        </dd>
        <dd class="dl4">
            <em>修改课程</em>
        </dd>
        <%--<dd class="dl5">
            <em>需要选择{{=it.data.conf.advanceCount}}门等级考科目  {{=it.data.conf.simpleCount}}门合格考科目</em>
        </dd>--%>
        <dd class="dl6">
            <table>
                <tr class="row0">
                    <td>科目名称</td>
                    <td>等级考</td>
                    <td>合格考</td>
                    <td>说明</td>
                </tr>
                {{~it.data.conf.subConfList:value:index}}
                <tr sid="{{=value.subjectId}}">
                    <td>{{=value.subjectName}}</td>
                    <td>
                        {{?$.inArray(value.subjectName,it.advChoose)>-1}}
                        <em class="schedule-CUR-hov advcourse ch-me">已选</em>
                        {{??}}
                        <em class="advcourse">我选</em>
                        {{?}}
                    </td>
                    <td>
                        {{?$.inArray(value.subjectName,it.simChoose)>-1}}
                        <em class="schedule-CUR-hov simcourse ch-me">已选</em>
                        {{??}}
                            {{?value.simpleTime==0}}
                            <em style="width: 38px;margin: auto;">-----</em>
                            {{??}}
                            <em class="simcourse">我选</em>
                            {{?}}
                        {{?}}
                    </td>
                    <td>{{=value.explain}}</td>
                </tr>
                {{~}}

            </table>
        </dd>
        <dd class="dl7">
            <button class="submitXuanke btn-qd">确定</button>
            <button class="schedule-CUR-BU btn-qx">取消</button>
        </dd>
    </script>
    <dl class="hide3-2">
    </dl>
</div>

<!--/选课弹窗end -->

<!--走班明细弹窗start-->
<div class="main1-hide">
    <div class="hide1-1">
        <em>明细</em>
        <i class="hide1-x">×</i>
    </div>
    <div class="hide1-2">
        <%--<i class="classShow">高一1班</i>--%>
        <select id="weekshow" style="margin-left: 0px">

        </select>
        <script type="application/template" id="weekTempJs">
            {{~it.data:value:index}}
            {{?it.x==value}}
            <option value="{{=value}}" selected="selected">星期{{?value==1}}一
                {{??value==2}}二
                {{??value==3}}三
                {{??value==4}}四
                {{??value==5}}五
                {{??value==6}}六
                {{??value==7}}日
                {{?}}</option>
            {{??}}
            <option value="{{=value}}">星期{{?value==1}}一
                {{??value==2}}二
                {{??value==3}}三
                {{??value==4}}四
                {{??value==5}}五
                {{??value==6}}六
                {{??value==7}}日
                {{?}}</option>
            {{?}}
            {{~}}
        </script>
        <select id="classDetailShow">

        </select>
        <script type="application/template" id="classDetailTempJs">
            {{~it.data:value:index}}
            {{?it.y==index+1}}
            <option value="{{=index+1}}" selected="selected">第{{=index+1}}节</option>
            {{??}}
            <option value="{{=index+1}}">第{{=index+1}}节</option>
            {{?}}
            {{~}}
        </script>
        <i class="i2">8:50~9:30</i>
        <button class="select-btn">筛选</button>
        <%--<button>导出</button>--%>
    </div>
    <script type="application/template" id="detailTempJs">
        <tr class="row1 row">
            <td class="col1">教学班</td>
            <td class="col2">教学班人数</td>
            <td class="col3">任课老师</td>
            <td class="col4 room">上课教室</td>
            <td class="col5">本班学生</td>
        </tr>
        {{~it.data:value:index}}
        <tr>
            <td>{{=value.className}}</td>
            <td>{{=value.people}}</td>
            <td>{{=value.teacherName}}</td>
            <td class="room">{{=value.classRoom}}</td>
            <td>{{=value.myClassAmount}}</td>
        </tr>
        {{~}}
    </script>
    <table class="hide1-3" id="detailShow">
    </table>
</div>
<!--/走班明细弹窗end -->


<!-- 选课明细弹窗start -->
<div class="main3-hide2">
    <div class="hide1-1">
        <em>明细</em>
        <i class="hide1-x">×</i>
    </div>
    <div class="hide4-2">
        <em class="termShow">${term}</em> <i id="subjectName">物理</i>
    </div>
    <div id="border">
    <table class="hide4-3">

    </table>
        </div>
    <script type="application/template" id="chooseListTempJs">
        <tr class="row0">
            <td>等级考({{=it.al}})</td>
            <td>合格考({{=it.sl}})</td>
        </tr>
                {{~it.adv:value:index}}
        <tr>
            <td>{{=value}}</td>
            <td>{{=it.sim[index]}}</td>
        </tr>
        {{~}}
    </script>
    <div class="hide4-4">
        <button class="btn-qd btn-qx">确定</button>
        <button class="btn-qx">取消</button>
    </div>
</div>

<!-- /选课明细弹窗end -->


<div class="bg"></div>
<!--#foot-->
<%@ include file="../../common_new/foot.jsp" %>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js?v=1"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('teacher');
</script>
</body>
</html>