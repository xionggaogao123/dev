<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2016/7/26
  Time: 15:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <title>3+3走班</title>
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/css/reset.css" rel="stylesheet"/>
    <link href="/static_new/css/zouban/zoubannew.css" rel="stylesheet"/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>

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

        <!--.tab-col右侧-->
        <div class="tab-col">

            <div class="tab-head clearfix">
                <ul>
                    <li id="XZB" class="cur"><a href="javascript:;">行政班课表</a><em></em></li>
                    <li id="LSKB"><a href="javascript:;">老师课表</a><em></em></li>
                    <li id="XSKB"><a href="javascript:;">学生课表</a><em></em></li>
                    <c:if test="${mode != 0}">
                        <li id="ZBSTU"><a href="javascript:;">走班学生名单</a><em></em></li>
                    </c:if>
                </ul>
            </div>
            <div class="zouban-title clearfix">
                <div class="title-left">
                    <div class="w-wrap">
                        <select id="termListCtx"></select>
                        <script id="termListTmpl" type="application/template">
                            {{~it:value:index}}
                            <option value="{{=value}}">{{=value}}</option>
                            {{~}}
                        </script>

                        <select id="weekSelectCtx" style="width: 90px;"></select>
                        <script id="weekSelectTmpl" type="application/template">
                            {{~it:week:index}}
                            <option value="{{=week}}">第{{=week}}周</option>
                            {{~}}
                        </script>
                    </div>
                </div>
                <div class="title-right">
                    <a href="/zouban/baseConfig.do?term=${year}&gradeId=${gradeId}" class="zouban-back"
                       style="display: inline-block;">&lt;&nbsp;返回走班教务管理</a>
                </div>
            </div>
            <div class="tab-main">
                <!--=================================第五步行政班课表start==================================-->
                <div class="xzb-con" id="tab-XZB">
                    <div class="fifthstep-title clearfix">
                        <div class="fstep-select">
                            <span>年级：<em>${gradeName}</em></span>
                            <span>行政班:</span>
                            <select class="admin-classes">
                            </select>
                            <script id="admin-classesTmpl" type="text/template">
                                {{~ it:value:i }}
                                <option value="{{=value.id}}">{{=value.className}}</option>
                                {{~ }}
                            </script>
                        </div>
                        <a href="javascript:;" class="fstep-dc" id="exportTable">导出</a>
                    </div>
                    <table class="newTable" id="classTable">
                        <thead></thead>
                        <tbody>
                        </tbody>
                    </table>
                    <script id="classTableTmpl" type="text/template">
                        <thead>
                        <tr>
                            <th style="width: 20%;">上课时间</th>
                            {{~ it.conf.classDays:value:index }}
                            <th style="width: 11%;">周
                                {{?value==1}}一
                                {{??value==2}}二
                                {{??value==3}}三
                                {{??value==4}}四
                                {{??value==5}}五
                                {{??value==6}}六
                                {{??value==7}}日
                                {{?}}
                            </th>
                            {{~ }}
                        </tr>
                        </thead>
                        <tbody>

                        {{~it.conf.classTime:value:index}}
                        <tr>
                            <td style="background:#ececec;">
                                <span class="class-turn">{{=index+1}}</span>{{=it.conf.classTime[index]}}
                            </td>
                            {{~it.conf.classDays:value1:index1}}
                            <td>
                                {{~it.course:value2:index2}}
                                {{?value2.xIndex==value1 && value2.yIndex==(index+1)}}
                                    {{?value2.type == 1 || value2.type == 7}}
                                    <div class="zbtd zb zouban-useful" x="{{=value1}}" y="{{=index+1}}">
                                        {{=value2.className}}
                                    </div>
                                    {{??value2.type == 2 || value2.type == 8}}
                                    {{=value2.className}}<br/>{{=value2.teacherName}}
                                    {{??value2.type == 4}}
                                    {{=value2.className}}
                                    {{??}}
                                    {{=value2.className}}
                                    {{?}}
                                {{?}}
                                {{~}}
                                {{~it.conf.events:value3:index3}}
                                    {{?value3.xIndex==value1 && value3.yIndex==index+1}}
                                        {{?value3.forbidEvent.length==1}}
                                        {{=value3.forbidEvent[0]}}
                                        {{?}}
                                    {{?}}
                                {{~}}
                            </td>
                            {{~}}
                        </tr>
                        {{~}}
                        </tbody>
                    </script>
                </div>
                <!--=================================第五步行政班课表end==================================-->
                <!--=================================第五步老师课表end==================================-->
                <div class="lskb-con" id="tab-LSKB">
                    <div class="fifthstep-title clearfix">
                        <div class="fstep-select">
                            <span>年级：<em>${gradeName}</em></span>
                            <span>学科:</span>
                            <select id="subject2">
                                <%--<option>政治</option>--%>
                            </select>
                            <script id="subject2Tmpl" type="text/template">
                                {{~ it:value:index}}
                                <option value="{{=index}}">{{=value.subjectName}}</option>
                                {{~}}
                            </script>
                            <span>老师:</span>
                            <select id="teachers">
                                <%--<option>王雷</option>--%>
                            </select>
                            <script id="teachersTmpl" type="text/template">
                                {{~ it:value:index}}
                                <option value="{{=value.id}}">{{=value.name}}</option>
                                {{~}}
                            </script>
                        </div>
                        <a href="javascript:;" class="fstep-dc" id="exportTeacher">导出</a>
                    </div>
                    <table class="newTable" id="teacherTable">
                        <thead>
                        <tr>
                            <th>上课时间</th>
                            <th>周一</th>
                            <th>周二</th>
                            <th>周三</th>
                            <th>周四</th>
                            <th>周五</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>

                    <script type="application/template" id="teacherTableTmpl">
                        <thead>
                        <tr>
                            <th width="20%">上课时间</th>
                            {{~it.conf.classDays:value:index}}
                            <th width="11%">周{{?value==1}}一
                                {{??value==2}}二
                                {{??value==3}}三
                                {{??value==4}}四
                                {{??value==5}}五
                                {{??value==6}}六
                                {{??value==7}}日
                                {{?}}
                            </th>
                            {{~}}
                        </tr>
                        </thead>
                        {{~it.conf.classTime:value:index}}
                        <tr>
                            <td style="background:#ececec;">
                                <span class="class-turn">{{=index+1}}</span>{{=it.conf.classTime[index]}}
                            </td>
                            {{~it.conf.classDays:value1:index1}}
                            <td>
                                {{~it.course:value2:index2}}
                                {{?value2.xIndex==value1 && value2.yIndex==(index+1)}}
                                {{=value2.className}}<br/>{{=value2.classRoom}}<br>
                                {{?}}
                                {{~}}
                            </td>
                            {{~}}
                        </tr>
                        {{~}}
                    </script>
                </div>
                <!--=================================第五步老师课表end==================================-->
                <!--=================================第五步学生课表end==================================-->
                <div class="xskb-con" id="tab-XSKB">
                    <div class="fifthstep-title clearfix">
                        <div class="fstep-select">
                            <span>年级：<em>${gradeName}</em></span>
                            <span>行政班:</span>
                            <select class="admin-classes">
                                <%--<option>高二（1）</option>--%>
                            </select>
                            <span>学生:</span>
                            <select id="students">
                            </select>
                            <script id="studentsTmpl" type="text/template">
                                {{~ it:value:index}}
                                <option value="{{=value.studentId}}">{{=value.userName}}</option>
                                {{~}}
                            </script>
                        </div>
                        <a href="javascript:;" class="fstep-dc" id="exportStu">导出</a>
                    </div>
                    <table class="newTable" id="studentTable">
                        <thead>
                        <tr>
                            <th style="width:20%;">上课时间</th>
                            <th style="width:11%;">周一</th>
                            <th style="width:16%;">周二</th>
                            <th style="width:16%;">周三</th>
                            <th style="width:16%;">周四</th>
                            <th style="width:16%;">周五</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td style="background:#ececec;"><span class="class-turn">1</span>8:00~8:40</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                        </tbody>
                    </table>
                    <script type="application/template" id="studentTableTmpl">
                        <thead>
                        <tr>
                            <th style="width:20%;">上课时间</th>
                            {{~it.conf.classDays:value:index}}
                            <th style="width:11%;">周{{?value==1}}一
                                {{??value==2}}二
                                {{??value==3}}三
                                {{??value==4}}四
                                {{??value==5}}五
                                {{??value==6}}六
                                {{??value==7}}日
                                {{?}}
                            </th>
                            {{~}}
                        </tr>
                        </thead>
                        {{~it.conf.classTime:value:index}}
                        <tr>
                            <td style="background:#ececec;">
                                <span class="class-turn">{{=index+1}}</span>{{=it.conf.classTime[index]}}
                            </td>
                            {{~it.conf.classDays:value1:index1}}
                            <td>
                                {{~it.course:value2:index2}}
                                {{?value2.xIndex==value1 && value2.yIndex==(index+1)}}
                                {{=value2.className}}<br/>
                                {{?value2.teacherName!=""}}
                                <span>({{=value2.teacherName}})</span><br/>
                                {{?}}
                                {{?value2.type == 1 || value2.type == 7}}
                                {{=value2.classRoom}}
                                {{?}}
                                {{?}}
                                {{~}}
                            </td>
                            {{~}}
                        </tr>
                        {{~}}
                    </script>
                </div>
                <!--=================================第五步老师课表end==================================-->
                <!--=================================第五步行政班课表明细弹窗start==================================-->
                <div class="xz-mx-alert">
                    <div class="alert-title clearfix">
                        <p>明细</p>
                        <span class="alert-close">X</span>
                    </div>
                    <div class="alert-main">
                        <div class="alert-main-title">
                            <select id="weeks">
                                <%--<option>星期一</option>--%>
                            </select>
                            <script id="weeksTmpl" type="text/template">
                                {{~ it.data:value:index}}
                                <option value="{{=value}}" {{? it.x==value}}selected="selected"{{?}}>
                                    周
                                    {{? value==1}}一
                                    {{?? value==2}}二
                                    {{?? value==3}}三
                                    {{?? value==4}}四
                                    {{?? value==5}}五
                                    {{?? value==6}}六
                                    {{?? value==7}}日
                                    {{?}}
                                </option>
                                {{~}}
                            </script>
                            <select id="jieci">
                                <option>第三节</option>
                            </select>
                            <script id="jieciTmpl" type="text/template">
                                {{~it.data:value:index}}
                                {{?it.y==index+1}}
                                <option value="{{=index+1}}" selected="selected">第{{=index+1}}节</option>
                                {{??}}
                                <option value="{{=index+1}}">第{{=index+1}}节</option>
                                {{?}}
                                {{~}}
                            </script>
                            <span id="time">10:00~10:45</span>
                        </div>
                        <table class="alert-table">
                            <thead>
                            <tr>
                                <th style="width:28%;">教学班</th>
                                <th style="width:18%;">教学班人数</th>
                                <th style="width:18%;">任课老师</th>
                                <th style="width:18%;">上课教室</th>
                                <th style="width:18%;">本班学生</th>
                            </tr>
                            </thead>
                            <tbody id="adminclassdetail">
                            </tbody>
                            <script id="adminclassdetailTmpl" type="text/template">
                                {{~it:value:index}}
                                <tr>
                                    <td style="background:#ececec;">{{=value.className}}</td>
                                    <td>{{=value.people}}</td>
                                    <td>{{=value.teacherName}}</td>
                                    <td>{{=value.classRoom}}</td>
                                    <td>{{=value.myClassAmount}}</td>
                                </tr>
                                {{~}}
                            </script>
                        </table>
                    </div>
                </div>
                <!--=================================第五步行政班课表明细弹窗end==================================-->

                <!--=================================走班学生名单start==================================-->
                <div class="stuset-con" id="tab-ZBSTU">
                    <div class="lsandjs-title">
                        <span>分段</span>
                        <select id="studentGroupCtx"></select>
                        <script id="studentGroupTmpl" type="application/template">
                            <option value="">全部</option>
                            {{~it:group:index}}
                            <option value="{{=group.id}}">{{=group.groupName}}</option>
                            {{~}}
                        </script>
                        <span>班级类型</span>
                        <select id="level2">
                            <option value="1">等级考</option>
                            <option value="2">合格考</option>
                        </select>
                        <a href="javascript:;" class="fstep-dc" id="exportZBStuList">导出</a>
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
                                <a href="javascript:;" class="stuset-edit stu-detail"
                                   courseId="{{=course.zbCourseId}}" courseName="{{=course.courseName}}"
                                   teacherName="{{=course.teacherName}}"
                                   count="{{=course.studentsCount}}">查看</a>
                            </td>
                        </tr>
                        {{~}}
                        {{~}}
                    </script>
                </div>

                <!--=================================走班学生名单end==================================-->
            </div>
            <!--============================学生列表开始=======================================-->
            <div class="stu-changeClass">
                <a class="backMain">&lt;返回</a>
                <div>
                    <h3><span id="className"></span><span>人数：<em id="stuCount"></em></span></h3>
                    <table class="stuset-table">
                        <thead>
                        <tr>
                            <th width="25%">学生姓名</th>
                            <th width="20%">性别</th>
                            <th width="30%">所属行政班</th>
                        </tr>
                        </thead>
                        <tbody id="stuListCtx"></tbody>
                    </table>
                    <script id="stuListTmpl" type="application/template">
                        {{~it.studentList:value:index}}
                        <tr>
                            <td>{{=value.userName}}</td>
                            <td>{{=value.sex}}</td>
                            <td>{{=value.className}}</td>
                        </tr>
                        {{~}}
                    </script>
                </div>
            </div>

            <!--=================学生列表end=============================-->
        </div>
        <!--/.col-right-->

    </div>
    <!--/#content-->
</div>
</div>
<!--#foot-->
<%@ include file="../../common_new/foot.jsp" %>
<!--#foot-->

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js"></script>
<script>
    var base = {
        year: '${year}',
        gradeId: '${gradeId}',
        gradeName: '${gradeName}',
        curweek: ${curweek}
    }
    seajs.use('/static_new/js/modules/zouban/2.0/kebiao.js');
</script>

</body>
</html>
