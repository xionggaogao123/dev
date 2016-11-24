<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<html>
<head>
    <meta charset="utf-8">
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
    <link href="/static_new/css/zouban/arranging.css?v=2015041602" rel="stylesheet"/>
    <link href="/static_new/css/zouban/bianban.css?v=2015041602" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="/static_new/css/zouban/xiaozoubanreset.css">

    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>
<body>
<input type="hidden" id="classConfId">
<!--#head-->
<%@ include file="../../common_new/head.jsp" %>
<!--/#head-->
<!--#content-->
<div id="content" class="clearfix">
    <!--.col-left-->
    <%@ include file="../../common_new/col-left.jsp" %>
    <!--/.col-left-->
    <!--.col-right-->
    <!--广告-->
<%--    <c:choose>
        <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
        </c:when>
        <c:otherwise>
            <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
        </c:otherwise>
    </c:choose>--%>
    <!--广告-->
    <div class="col-right">
        <input type="hidden" id="gradeId" value="${gradeId}">
        <input type="hidden" id="termVal" value="${term}">
        <input type="hidden" id="mode" value="${mode}">
        <div class="tab-col">
            <div class="tab-top clearfix">
                <ul>
                    <li class="cur timetable" id="timetable"><a href="javascript:;">课表结构设置</a></li>
                    <li class="Rclass" id="Rclass"><a href="javascript:;">走班</a></li>
                    <li id="Xclass"><a href="javascript:;">小走班</a></li>
                    <li id="Pclass"><a href="javascript:;">体育走班</a></li>
                    <li id="Nclass"><a href="javascript:;">非走班</a></li>
                    <li id="Oclass"><a href="javascript:;">其他课程</a></li>
                </ul>
                <a class="backUrl tab-back" href="javascript:;"<%--href="../paike/index.do?version=58"--%>>&lt;返回教务管理首页</a>
            </div>
            <div id="contentt">
                <!--================课表结构设置 start===================-->
                <div class="tab-timetable" id="tab-timetable">
                    <dl id="courseConfShow">
                        <dd>
                            <em id="term">${term}</em>
                        <%--</dd>
                        <dd>--%>
                            <em>年级:${gradeName}</em>
                            <%--<select id="gradeShow">
                                &lt;%&ndash;<option>高一年级</option>
                                <option>高二年级</option>&ndash;%&gt;
                            </select>--%>

                            <%--<button id="confGet">筛选</button>--%>
                            <button id="confSave">保存</button>
                        </dd>
                        <dd>
                            <em>上课天数</em>
                            <input type="checkbox" value="1" name="classDays"><i>星期一</i>
                            <input type="checkbox" value="2" name="classDays"><i>星期二</i>
                            <input type="checkbox" value="3" name="classDays"><i>星期三</i>
                            <input type="checkbox" value="4" name="classDays"><i>星期四</i>
                            <input type="checkbox" value="5" name="classDays"><i>星期五</i>
                            <input type="checkbox" value="6" name="classDays"><i>星期六</i>
                            <input type="checkbox" value="7" name="classDays"><i>星期日</i>
                        </dd>
                        <dd>
                            <em>每天节数</em>
                            <select class="timetable-SE">
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
                            </select>
                            <button id="addMeeting">集体会议</button>
                        </dd>
                        <dd>
                            <table id="tableShow">

                            </table>
                            <script type="application/template" id="tableTempJs">
                                <tr>
                                    <th>节次/时间</th>
                                    <th>星期一</th>
                                    <th>星期二</th>
                                    <th>星期三</th>
                                    <th>星期四</th>
                                    <th>星期五</th>
                                    <th>星期六</th>
                                    <th>星期日</th>
                                </tr>
                                {{~it.data.count:value:index}}
                                <tr>
                                    <td>
                                        <strong>第{{=index+1}}节</strong>
                                        <input class="startTime" value="{{=it.data.classTime[index]}}">
                                    </td>
                                    {{~it.data.classDays:value1:index1}}
                                    {{?it.data.classSelectDays.indexOf(value1)==-1}}
                                    <td class="arranging-N">
                                    </td>
                                    {{??}}
                                    <td class="arranging-Y">
                                        {{~it.data.events:value2:index2}}
                                            {{?value2.xIndex==value1 && value2.yIndex==index+1}}
                                                <%--{{?value2.forbidEvent.length==1}}
                                                    {{=value2.forbidEvent[0]}}
                                                {{??}}
                                                    {{?value2.groupStudy.length>0 && value2.personEvent.length>0}}
                                                        <em class="arranging-JY">教研<i>({{=value2.groupStudy.length}})</i></em>
                                                        <em class="arranging-GR">会议&lt;%&ndash;<i>({{=value2.personEvent.length}})</i>&ndash;%&gt;</em>
                                                    {{??value2.groupStudy.length>0 }}
                                                        <em class="arranging-JY2">教研<i>({{=value2.groupStudy.length}})</i></em>
                                                    {{??value2.personEvent.length>0}}
                                                        <em class="arranging-GR2">会议&lt;%&ndash;<i>({{=value2.personEvent.length}})</i>&ndash;%&gt;</em>
                                                    {{?}}
                                                {{?}}--%>
                                                {{?value2.forbidEvent.length==1}}
                                                    {{?value2.groupStudy.length>0 && value2.personEvent.length>0}}
                                                        <em class="arranging-JY">教研<%--<i>({{=value2.groupStudy.length}})</i>--%></em>
                                                        <em class="arranging-GR">会议/{{=value2.forbidEvent[0]}}</em>
                                                        <%--<em class="arranging-JY"></em>--%>
                                                    {{??value2.groupStudy.length>0}}
                                                        <em class="arranging-JY">教研<%--<i>({{=value2.groupStudy.length}})</i>--%></em>
                                                        <em class="arranging-JY">{{=value2.forbidEvent[0]}}</em>
                                                    {{??value2.personEvent.length>0 }}
                                                        <em class="arranging-GR">会议</em>
                                                        <em class="arranging-JY">{{=value2.forbidEvent[0]}}</em>
                                                    {{??}}
                                                        <em class="arranging-JY2">{{=value2.forbidEvent[0]}}</em>
                                                    {{?}}
                                                {{??}}
                                                    {{?value2.groupStudy.length>0 && value2.personEvent.length>0}}
                                                        <em class="arranging-JY">教研<%--<i>({{=value2.groupStudy.length}})</i>--%></em>
                                                        <em class="arranging-GR">会议</em>
                                                    {{??value2.groupStudy.length>0 }}
                                                        <em class="arranging-JY2">教研<i>({{=value2.groupStudy.length}})</i></em>
                                                    {{??value2.personEvent.length>0}}
                                                        <em class="arranging-GR2">会议</em>
                                                    {{?}}
                                                {{?}}
                                            {{?}}
                                        {{~}}
                                    </td>
                                    {{?}}
                                    {{~}}
                                </tr>
                                {{~}}
                            </script>
                        </dd>
                    </dl>

                    <script type="application/template" id="gradeTempJs">
                        {{~it.data:value:index}}
                        <option value="{{=value.id}}">{{=value.name}}</option>
                        {{~}}
                    </script>
                </div>
                <!--===================课表结构设置 end===================-->
                <!--=====================走班排课 start========================-->
                <div class="tab-Rclass" id="tab-Rclass">
                    <dl>
                        <dd>
                            <em>年级:${gradeName}</em>
                            <em id="fenduan">分段</em>
                            <select id="groupShow2">
                                <%--<option>第一段</option>
                                <option>第二段</option>--%>
                            </select>
                            <script type="application/template" id="groupTempJs2">
                                {{~it.data:value:index}}
                                <option value="{{=value.groupId}}">{{=value.groupName}}</option>
                                {{~}}
                            </script>
                            <em>班级</em>
                            <select id="classShow2">
                                <%--<option>一班</option>
                                <option>二班</option>--%>
                            </select>
                            <script type="application/template" id="classTempJs2">
                                {{~it.data:value:index}}
                                <option value="{{=value.classId}}">{{=value.className}}</option>
                                {{~}}
                            </script>
                            <button id="selectValue">筛选</button>
                            <button id="lockTable">未锁定(锁定)</button>
                        </dd>
                        <dd <%--style="display: none;"--%>>
                            <p id="alertShow2">提醒：浅绿色标注位置代表预留的小走班排课位置，请根据本校小走班课时安排酌情安排。
                                <%--<a>查看原因</a>
                                <i>X</i>--%>
                            </p>
                        </dd>
                        <dd>
                            <div class="Rclass-main clearfix">
                                <div class="Rclass-main-left">
                                    <div class="Rclass-main-left-top">
                                        未排课程
                                    </div>
                                    <ul id="unArrangeCourseShow2">
                                    </ul>
                                    <script type="application/template" id="unArrangeCourseTempJs2">
                                        {{~it.data:value:index}}
                                        <li class="unArrangeList" style="cursor: pointer" cid="{{=value.courseId}}" sid="{{=value.subjectId}}"
                                            crid="{{=value.classRoomId}}" tid="{{=value.teacherId}}">
                                            <em>{{=value.courseName}}</em>
                                            <em>{{=value.teacherName}}</em>
                                            <em>{{=value.classRoom}}</em>
                                        </li>
                                        {{~}}
                                    </script>
                                </div>
                                <div class="Rclass-main-right">
                                    <table id="tableShow2">

                                    </table>
                                    <script type="application/template" id="tableTempJs2">
                                        <tr>
                                            <th>节次/时间</th>
                                            {{~it.data.classDays:value:index}}
                                            <th>星期{{?value==1}}一
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
                                        {{~it.data.classTime:value:index}}
                                        <tr class="zbtr">
                                            <td>
                                                <em>第{{=index+1}}节</em><br>
                                                <i>{{=value}}</i>
                                            </td>
                                            {{~it.data.classDays:value1:index1}}
                                            <%--x:value1 y:index+1
                                             遍历 chooseCourse.courseList  已选课
                                             遍历 pointList 可选位置
                                             遍历 events :不可排课位置--%>
                                            {{~it.data.detail:value2:index2}}
                                            {{?it.data.state==0}}
                                            {{?value2.x==value1 && value2.y==index+1}}
                                            {{?value2.type==0}}
                                            <td class="Rclass-main-ZB ZB_table_base ZBdelete" x="{{=value1}}" y="{{=index+1}}"
                                                courseId="{{~value2.courseList:value3:index3}}{{=value3.courseId}},{{~}}">
                                                {{=value2.subjectName}}走班({{=value2.courseList.length}})
                                                {{?value2.fb.length>0}}
                                                {{=value2.fb[0]}}
                                                {{??value2.gs.length>0&&value2.te.length>0}}
                                                <br>教研|个人
                                                {{??value2.gs.length>0}}
                                                <br><i style="color: #fd2b2b;">[教研]</i>
                                                {{??value2.te.length>0}}
                                                <br>个人
                                                {{?}}
                                                <!--===========走班弹出框=============-->
                                                <div class="Rclass-ZB-SJ">
                                                    <div class="inner"></div>
                                                </div>
                                                <div class="Rclass-ZB-SJJ">

                                                </div>
                                                <div class="Rclass-ZB-TC">
                                                    {{~value2.courseList:value3:index3}}
                                                        <div class="Rclass-ZB-TCCC">
                                                            <em>{{=value3.courseName}}</em>
                                                            <em>{{=value3.teacherName}}</em>
                                                            <em>{{=value3.classRoom}}</em>
                                                            <%--<i courseId="{{=value3.courseId}}" class="removeCourseX">X</i>--%>
                                                        </div>
                                                    {{~}}
                                                    {{~value2.gs:value4:index4}}
                                                        <div class="Rclass-ZB-TCC">
                                                            <em>集体教研</em>
                                                            <em>{{=value4.name}}</em>
                                                        </div>
                                                    {{~}}
                                                    {{~value2.te:value4:index4}}
                                                        <div class="Rclass-ZB-TCC-stl">
                                                            <em>个人事务</em>
                                                            <em>{{=value4.subjectName}}({{=value4.teacherName}})</em>
                                                        </div>
                                                    {{~}}
                                                </div>
                                            </td>
                                            {{??value2.type==2}}
                                            <td class="Rclass-main-Gray Rclass-main-PE ZB_table_base" x="{{=value1}}" y="{{=index+1}}">
                                                {{=value2.courseList[0].courseName}}
                                                {{?value2.fb.length>0}}
                                                    {{=value2.fb[0]}}
                                                {{??value2.gs.length>0&&value2.te.length>0}}
                                                    <br>教研|个人
                                                    <div class="Rclass-ZB-SJ">
                                                        <div class="inner"></div>
                                                    </div>
                                                    <div class="Rclass-ZB-SJJ">

                                                    </div>
                                                    <div class="Rclass-ZB-TC">
                                                        {{~value2.gs:value4:index4}}
                                                        <div class="Rclass-ZB-TCC">
                                                            <em>集体教研</em>
                                                            <em>{{=value4.name}}</em>
                                                        </div>
                                                        {{~}}
                                                        {{~value2.te:value4:index4}}
                                                        <div class="Rclass-ZB-TCC-stl">
                                                            <em>个人事务</em>
                                                            <em>{{=value4.subjectName}}({{=value4.teacherName}})</em>
                                                            <%--<em>{{=value4.teacherName}}</em>--%>
                                                            <%--<em>{{=value4.event}}</em>--%>
                                                        </div>
                                                        {{~}}
                                                    </div>
                                                {{??value2.gs.length>0}}
                                                    <br><i style="color: #fd2b2b;">[教研]</i>
                                                    <div class="Rclass-ZB-SJ">
                                                        <div class="inner"></div>
                                                    </div>
                                                    <div class="Rclass-ZB-SJJ">

                                                    </div>
                                                    <div class="Rclass-ZB-TC">
                                                        {{~value2.gs:value4:index4}}
                                                        <div class="Rclass-ZB-TCC">
                                                            <em>集体教研</em>
                                                            <em>{{=value4.name}}</em>
                                                        </div>
                                                        {{~}}
                                                    </div>
                                                {{??value2.te.length>0}}
                                                    <br>个人
                                                    <div class="Rclass-ZB-SJ">
                                                        <div class="inner"></div>
                                                    </div>
                                                    <div class="Rclass-ZB-SJJ">

                                                    </div>
                                                    <div class="Rclass-ZB-TC">
                                                        {{~value2.te:value4:index4}}
                                                        <div class="Rclass-ZB-TCC-stl">
                                                            <em>个人事务</em>
                                                            <em>{{=value4.subjectName}}({{=value4.teacherName}})</em>
                                                            <%--<em>{{=value4.teacherName}}</em>--%>
                                                            <%--<em>{{=value4.event}}</em>--%>
                                                        </div>
                                                        {{~}}
                                                    </div>
                                                {{?}}
                                            </td>
                                            {{??value2.type==3}}
                                            <td class="Rclass-main-Gray Rclass-main-PE ZB_table_base" x="{{=value1}}" y="{{=index+1}}">
                                                <%--无课--%>
                                                {{?value2.fb.length>0}}
                                                    {{=value2.fb[0]}}
                                                {{??value2.gs.length>0&&value2.te.length>0}}
                                                    <br>教研|个人
                                                    <div class="Rclass-ZB-SJ">
                                                        <div class="inner"></div>
                                                    </div>
                                                    <div class="Rclass-ZB-SJJ">

                                                    </div>
                                                    <div class="Rclass-ZB-TC">
                                                        {{~value2.gs:value4:index4}}
                                                        <div class="Rclass-ZB-TCC">
                                                            <em>集体教研</em>
                                                            <em>{{=value4.name}}</em>
                                                        </div>
                                                        {{~}}
                                                        {{~value2.te:value4:index4}}
                                                        <div class="Rclass-ZB-TCC-stl">
                                                            <em>个人事务</em>
                                                            <em>{{=value4.subjectName}}({{=value4.teacherName}})</em>
                                                            <%--<em>{{=value4.teacherName}}</em>--%>
                                                            <%--<em>{{=value4.event}}</em>--%>
                                                        </div>
                                                        {{~}}
                                                    </div>
                                                    {{??value2.gs.length>0}}
                                                    <i style="color: #fd2b2b;">[教研]</i>
                                                    <div class="Rclass-ZB-SJ">
                                                        <div class="inner"></div>
                                                    </div>
                                                    <div class="Rclass-ZB-SJJ">

                                                    </div>
                                                    <div class="Rclass-ZB-TC">
                                                        {{~value2.gs:value4:index4}}
                                                        <div class="Rclass-ZB-TCC">
                                                            <em>集体教研</em>
                                                            <em>{{=value4.name}}</em>
                                                        </div>
                                                        {{~}}
                                                    </div>
                                                    {{??value2.te.length>0}}
                                                    个人
                                                    <div class="Rclass-ZB-SJ">
                                                        <div class="inner"></div>
                                                    </div>
                                                    <div class="Rclass-ZB-SJJ">

                                                    </div>
                                                    <div class="Rclass-ZB-TC">
                                                        {{~value2.te:value4:index4}}
                                                        <div class="Rclass-ZB-TCC-stl">
                                                            <em>个人事务</em>
                                                            <em>{{=value4.subjectName}}({{=value4.teacherName}})</em>
                                                        </div>
                                                        {{~}}
                                                    </div>
                                                {{?}}
                                            </td>
                                            {{?}}
                                            {{?}}
                                            {{??}}
                                            {{?value2.x==value1 && value2.y==index+1}}
                                            {{?value2.isOk==1 || value2.isOk==3}}
                                                {{?value2.type==0}}
                                                    <td class="{{?value2.isOk==3}}xzbPointTag{{?}} Rclass-main-ZB ZB_table_base Rclass-main-Green"
                                                        x="{{=value1}}" y="{{=index+1}}">
                                                        {{=value2.subjectName}}走班({{=value2.courseList.length}})
                                                        {{?value2.fb.length>0}}
                                                        {{=value2.fb[0]}}
                                                        {{??value2.gs.length>0&&value2.te.length>0}}
                                                        <br>教研|个人
                                                        {{??value2.gs.length>0}}
                                                        <br><i style="color: #fd2b2b;">[教研]</i>
                                                        {{??value2.te.length>0}}
                                                        <br>个人
                                                        {{?}}
                                                    </td>
                                                {{??value2.type==2}}
                                                    <td class="{{?value2.isOk==3}}xzbPointTag{{?}} Rclass-main-Green" x="{{=value1}}" y="{{=index+1}}">
                                                        {{=value2.courseList[0].courseName}}
                                                        {{?value2.fb.length>0}}
                                                        {{=value2.fb[0]}}
                                                        {{??value2.gs.length>0&&value2.te.length>0}}
                                                        <br>教研|个人
                                                        {{??value2.gs.length>0}}
                                                        <br><i style="color: #fd2b2b;">[教研]</i>
                                                        {{??value2.te.length>0}}
                                                        <br>个人
                                                        {{?}}
                                                    </td>
                                                {{??value2.type==3}}
                                                    <td class="{{?value2.isOk==3}}xzbPointTag{{?}} Rclass-main-Green" x="{{=value1}}" y="{{=index+1}}">
                                                        {{?value2.fb.length>0}}
                                                        {{=value2.fb[0]}}
                                                        {{??value2.gs.length>0&&value2.te.length>0}}
                                                        教研|个人
                                                        {{??value2.gs.length>0}}
                                                        <i style="color: #fd2b2b;">[教研]</i>
                                                        {{??value2.te.length>0}}
                                                        个人
                                                        {{?}}
                                                    </td>
                                                {{?}}
                                            {{??value2.isOk==0}}
                                                {{?value2.type==0}}
                                                    <td class="Rclass-main-ZB ZB_table_base Rclass-main-Red"
                                                        x="{{=value1}}" y="{{=index+1}}">
                                                        {{=value2.subjectName}}走班({{=value2.courseList.length}})
                                                        {{?value2.fb.length>0}}
                                                        {{=value2.fb[0]}}
                                                        {{??value2.gs.length>0&&value2.te.length>0}}
                                                        <br>教研|个人
                                                        {{??value2.gs.length>0}}
                                                        <br><i style="color: #fd2b2b;">[教研]</i>
                                                        {{??value2.te.length>0}}
                                                        <br>个人
                                                        {{?}}
                                                    </td>
                                                {{??value2.type==2}}
                                                    <td class="Rclass-main-Red" x="{{=value1}}" y="{{=index+1}}">
                                                        {{=value2.courseList[0].courseName}}
                                                        {{?value2.fb.length>0}}
                                                        {{=value2.fb[0]}}
                                                        {{??value2.gs.length>0&&value2.te.length>0}}
                                                        <br>教研|个人
                                                        {{??value2.gs.length>0}}
                                                        <br><i style="color: #fd2b2b;">[教研]</i>
                                                        {{??value2.te.length>0}}
                                                        <br>个人
                                                        {{?}}
                                                    </td>
                                                {{??value2.type==3}}
                                                    <td class="Rclass-main-Red" x="{{=value1}}" y="{{=index+1}}">
                                                        {{?value2.fb.length>0}}
                                                        {{=value2.fb[0]}}
                                                        {{??value2.gs.length>0&&value2.te.length>0}}
                                                        教研|个人
                                                        {{??value2.gs.length>0}}
                                                        <i style="color: #fd2b2b;">[教研]</i>
                                                        {{??value2.te.length>0}}
                                                        个人
                                                        {{?}}
                                                    </td>
                                                {{?}}
                                            {{??value2.isOk==2}}
                                                {{?value2.type==0}}
                                                    <td class="Rclass-main-ZB ZB_table_base Rclass-main-JY"
                                                        x="{{=value1}}" y="{{=index+1}}">
                                                        {{=value2.subjectName}}走班({{=value2.courseList.length}})
                                                        {{?value2.fb.length>0}}
                                                        {{=value2.fb[0].courseName}}
                                                        {{??value2.gs.length>0&&value2.te.length>0}}
                                                        <br>教研|个人
                                                        {{??value2.gs.length>0}}
                                                        <br><i style="color: #fd2b2b;">[教研]</i>
                                                        {{??value2.te.length>0}}
                                                        <br>个人
                                                        {{?}}
                                                    </td>
                                                {{??value2.type==2}}
                                                    <td class="Rclass-main-JY" x="{{=value1}}" y="{{=index+1}}">
                                                        {{=value2.courseList[0].courseName}}
                                                        {{?value2.fb.length>0}}
                                                        {{=value2.fb[0]}}
                                                        {{??value2.gs.length>0&&value2.te.length>0}}
                                                        <br>教研|个人
                                                        {{??value2.gs.length>0}}
                                                        <br><i style="color: #fd2b2b;">[教研]</i>
                                                        {{??value2.te.length>0}}
                                                        <br>个人
                                                        {{?}}
                                                    </td>
                                                {{??value2.type==3}}
                                                    <td class="Rclass-main-JY" x="{{=value1}}" y="{{=index+1}}">
                                                        {{?value2.fb.length>0}}
                                                        {{=value2.fb[0]}}
                                                        {{??value2.gs.length>0&&value2.te.length>0}}
                                                        教研|个人
                                                        {{??value2.gs.length>0}}
                                                        <i style="color: #fd2b2b;">[教研]</i>
                                                        {{??value2.te.length>0}}
                                                        个人
                                                        {{?}}
                                                    </td>
                                                {{?}}
                                            {{?}}
                                            {{?}}
                                            {{?}}
                                            {{~}}
                                            {{~}}
                                        </tr>
                                        {{~}}
                                    </script>
                                </div>
                            </div>
                        </dd>
                    </dl>
                </div>
                <!--=====================走班排课 end========================-->
                <!--=====================小走班排课 start========================-->
                <div class="tab-Xclass" id="tab-Xclass">
                    <dl>
                        <dd>
                            <em>年级:${gradeName}</em>
                            <button id="clearXZB" style="width:125px;">清除小走班课程</button>
                            <button id="autoXZB">自动排课</button>
                            <button class="setTeaCRxzb" style="width:125px;">设置老师/教室</button>
                        </dd>
                        <dd>
                            <div class="Xclass-main clearfix">
                                <script type="application/template" id="tableTempFZBJs">
                                    <table>
                                    <tr>
                                        <th>节次/时间</th>
                                        {{~it.data.conf.classDays:value:index}}
                                        <th>星期{{?value==1}}一
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
                                    {{~it.data.conf.classTime:value:index}}
                                    <tr>
                                        <td>
                                            <em>第{{=index+1}}节</em><br>
                                            <i>{{=value}}</i>
                                        </td>
                                        {{~it.data.conf.classDays:value1:index1}}
                                        <%--x:value1 y:index+1
                                         遍历 chooseCourse.courseList  已选课
                                         遍历 pointList 可选位置
                                         遍历 events :不可排课位置--%>
                                        {{~it.course:value2:index2}}

                                        {{?value2.xIndex==value1 && value2.yIndex==index+1}}
                                        {{?value2.courseIdList.length==0}}
                                        <td class="Nclass-main-ZB ZB_table_base" style="background: #FEDEDE;color: #000000" x="{{=value1}}" y="{{=index+1}}">
                                        {{??}}
                                        <td class="Nclass-main-ZB ZB_table_base" style="background: #F7B84A;color: #000000" x="{{=value1}}" y="{{=index+1}}">
                                        {{?}}
                                            {{=value2.name}}
                                            <!--===========弹出框=============-->
                                            {{?value2.courseIdList.length>0}}
                                            <div class="Nclass-ZB-SJ">
                                                <div class="inner"></div>
                                            </div>
                                            <div class="Nclass-ZB-SJJ">

                                            </div>
                                            <div class="Nclass-ZB-TC">
                                                {{~value2.courseIdList:value3:index3}}
                                                <div class="Nclass-ZB-TCC">
                                                    <em>{{=value3.courseName}}</em>
                                                    <em>{{=value3.teacherName?value3.teacherName:"未设置"}}</em>
                                                    <em>{{=value3.classRoom?value3.classRoom:"未设置"}}</em>
                                                    <%--<i courseId="{{=value3.courseId}}" class="removeCourseX">X</i>--%>
                                                </div>
                                                {{~}}
                                            </div>
                                            {{?}}
                                        </td>
                                        {{?}}
                                        {{~}}
                                        {{~}}
                                    </tr>
                                    {{~}}
                                    </table>
                                </script>
                                <div class="Xclass-main-right" id="FZBcourseShow" style="float: left">

                                </div>
                            </div>
                        </dd>
                    </dl>
                </div>
                <!--=====================小走班排课 end========================-->
                <!--=====================体育走班排课 start========================-->
                <div class="tab-Xclass" id="tab-Pclass">
                    <dl>
                        <dd>
                            <em>年级:${gradeName}</em>

                            <button id="clearPhy" style="width: 125px;">清除体育走班课程</button>
                            <button id="autoSortPhy">自动编排</button>
                            <button id="setTeaPhy" style="width: 125px;">设置老师/教室</button>
                        </dd>
                        <dd>
                            <div class="Xclass-main clearfix" >
                                <script type="application/template" id="tableTempPhyJs">
                                    <table>
                                        <tr>
                                            <th>节次/时间</th>
                                            {{~it.data.conf.classDays:value:index}}
                                            <th>星期{{?value==1}}一
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
                                        {{~it.data.conf.classTime:value:index}}
                                        <tr>
                                            <td>
                                                <em>第{{=index+1}}节</em><br>
                                                <i>{{=value}}</i>
                                            </td>
                                            {{~it.data.conf.classDays:value1:index1}}
                                            <%--x:value1 y:index+1
                                             遍历 chooseCourse.courseList  已选课
                                             遍历 pointList 可选位置
                                             遍历 events :不可排课位置--%>
                                            {{~it.course:value2:index2}}

                                            {{?value2.xIndex==value1 && value2.yIndex==index+1}}
                                            {{?value2.courseIdList.length==0}}
                                            <td class="Nclass-main-ZB ZB_table_base" style="background: #FEDEDE;color: #000000" x="{{=value1}}" y="{{=index+1}}">
                                            {{??}}
                                            <td class="Nclass-main-ZB ZB_table_base" style="background: #F7B84A;color: #000000" x="{{=value1}}" y="{{=index+1}}">
                                            {{?}}
                                                {{=value2.name}}
                                                <!--===========弹出框=============-->
                                                {{?value2.courseIdList.length>0}}
                                                <div class="Nclass-ZB-SJ">
                                                    <div class="inner"></div>
                                                </div>
                                                <div class="Nclass-ZB-SJJ">

                                                </div>
                                                <div class="Nclass-ZB-TC">
                                                    {{~value2.courseIdList:value3:index3}}
                                                    <div class="Nclass-ZB-TCC">
                                                        <em>{{=value3.courseName.substring(0,4)}}</em>
                                                        <em>{{=value3.teacherName?value3.teacherName:"未设置"}}</em>
                                                    </div>
                                                    {{~}}
                                                </div>
                                                {{?}}
                                            </td>
                                            {{?}}
                                            {{~}}
                                            {{~}}
                                        </tr>
                                        {{~}}
                                    </table>
                                </script>
                                <div class="Xclass-main-right" id="physicalCourseShow" style="float: left;">

                                </div>
                            </div>
                        </dd>
                    </dl>
                </div>
                <!--=====================体育走班排课 end========================-->
                <!--=====================非走班排课 start========================-->
                <div class="tab-Nclass" id="tab-Nclass">
                    <dl>
                        <dd>
                            <em>年级:${gradeName}</em>
                            <em>班级</em>
                            <select id="classShow4">
                                <%--<option>一班</option>--%>
                                <%--<option>二班</option>--%>
                            </select>
                            <script type="application/template" id="classTempJs4">
                                {{~it.data:value:index}}
                                <option value="{{=value.classId}}">{{=value.className}}</option>
                                {{~}}
                            </script>
                            <button id="selectFeizouban">筛选</button>
                            <button id="removeFeizouban">清空已排课程</button>
                            <button id="autoArrange">自动编排</button>
                        </dd>
                        <dd id="FZBshow" style="display: none">
                            <p style="color: #ff0000">可用课程已无法安排“$课程”，请重新调整。
                                <a>查看原因</a>
                                <i>X</i>
                            </p>
                        </dd>
                        <dd>
                            <div class="Nclass-main clearfix">
                                <div class="Nclass-main-left">
                                    <div class="Nclass-main-left-top">
                                        未排课程
                                    </div>
                                    <ul id="unArrangeCourseShow4">
                                    </ul>
                                    <script type="application/template" id="unArrangeCourseTempJs4">
                                        {{~it.data:value:index}}
                                        <li class="unArrangeListFZB" style="cursor: pointer" cid="{{=value.courseId}}" sid="{{=value.subjectId}}"
                                            crid="{{=value.classRoomId}}" tid="{{=value.teacherId}}">
                                            <em>{{=value.courseName}}</em>
                                            <em>{{=value.teacherName}}</em>
                                            <%--<em>{{=value.classRoom}}</em>--%>
                                        </li>
                                        {{~}}
                                    </script>
                                </div>
                                <div class="Nclass-main-right">
                                    <table  id="tableShow4">

                                    </table>
                                    <script type="application/template" id="tableTempJs4">
                                        <tr>
                                            <th>节次/时间</th>
                                            {{~it.data.classDays:value:index}}
                                            <th>星期{{?value==1}}一
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
                                        {{~it.data.classTime:value:index}}
                                        <tr>
                                            <td>
                                                <em>第{{=index+1}}节</em><br>
                                                <i>{{=value}}</i>
                                            </td>
                                            {{~it.data.classDays:value1:index1}}
                                            <%--x:value1 y:index+1
                                             遍历 chooseCourse.courseList  已选课
                                             遍历 pointList 可选位置
                                             遍历 events :不可排课位置--%>
                                            {{~it.data.detail:value2:index2}}
                                            {{?it.data.state==0}}
                                            {{?value2.x==value1 && value2.y==index+1}}
                                            {{?value2.type==0}}
                                            <td class="Nclass-main-ZB ZB_table_base" x="{{=value1}}" y="{{=index+1}}">
                                                {{=value2.subjectName}}走班({{=value2.courseList.length}})
                                                {{?value2.fb.length>0}}
                                                {{=value2.fb[0]}}
                                                {{??value2.gs.length>0&&value2.te.length>0}}
                                                <br>教研|个人
                                                {{??value2.gs.length>0}}
                                                <br><i style="color: #fd2b2b;">[教研]</i>
                                                {{??value2.te.length>0}}
                                                <br>个人
                                                {{?}}
                                                <!--===========弹出框=============-->
                                                <div class="Nclass-ZB-SJ">
                                                    <div class="inner"></div>
                                                </div>
                                                <div class="Nclass-ZB-SJJ">

                                                </div>
                                                <div class="Nclass-ZB-TC">
                                                    {{~value2.courseList:value3:index3}}
                                                    <div class="Nclass-ZB-TCCC">
                                                        <em>{{=value3.courseName}}</em>
                                                        <em>{{=value3.teacherName}}</em>
                                                        <em>{{=value3.classRoom}}</em>
                                                        <%--<i courseId="{{=value3.courseId}}" class="removeCourseX">X</i>--%>
                                                    </div>
                                                    {{~}}
                                                    {{~value2.gs:value4:index4}}
                                                    <div class="Rclass-ZB-TCC">
                                                        <em>集体教研</em>
                                                        <em>{{=value4.name}}</em>
                                                    </div>
                                                    {{~}}
                                                    {{~value2.te:value4:index4}}
                                                    <div class="Rclass-ZB-TCC-stl">
                                                        <em>个人事务</em>
                                                        <em>{{=value4.subjectName}}({{=value4.teacherName}})</em>
                                                        <%--<em>{{=value4.teacherName}}</em>--%>
                                                        <%--<em>{{=value4.event}}</em>--%>
                                                    </div>
                                                    {{~}}
                                                </div>
                                            </td>
                                            {{??value2.type==2}}
                                                <td class="Nclass-main-Gray feizoubanDel Nclass-main-PE ZB_table_base" x="{{=value1}}" y="{{=index+1}}" cid="{{=value2.courseList[0].courseId}}">
                                                    {{=value2.courseList[0].courseName}}
                                                    {{?value2.fb.length>0}}
                                                    {{=value2.fb[0]}}
                                                    {{??value2.gs.length>0&&value2.te.length>0}}
                                                    <br>教研|个人
                                                    <div class="Nclass-ZB-SJ">
                                                        <div class="inner"></div>
                                                    </div>
                                                    <div class="Nclass-ZB-SJJ">
                                                    </div>
                                                    <div class="Nclass-ZB-TC">
                                                        {{~value2.gs:value4:index4}}
                                                        <div class="Rclass-ZB-TCC">
                                                            <em>集体教研</em>
                                                            <em>{{=value4.name}}</em>
                                                        </div>
                                                        {{~}}
                                                        {{~value2.te:value4:index4}}
                                                        <div class="Rclass-ZB-TCC-stl">
                                                            <em>个人事务</em>
                                                            <em>{{=value4.subjectName}}({{=value4.teacherName}})</em>
                                                        </div>
                                                        {{~}}
                                                    </div>
                                                    {{??value2.gs.length>0}}
                                                    <br><i style="color: #fd2b2b;">[教研]</i>
                                                    <div class="Nclass-ZB-SJ">
                                                        <div class="inner"></div>
                                                    </div>
                                                    <div class="Nclass-ZB-SJJ">

                                                    </div>
                                                    <div class="Nclass-ZB-TC">
                                                        {{~value2.gs:value4:index4}}
                                                        <div class="Rclass-ZB-TCC">
                                                            <em>集体教研</em>
                                                            <em>{{=value4.name}}</em>
                                                        </div>
                                                        {{~}}
                                                    </div>
                                                    {{??value2.te.length>0}}
                                                    <br>个人
                                                    <div class="Nclass-ZB-SJ">
                                                        <div class="inner"></div>
                                                    </div>
                                                    <div class="Nclass-ZB-SJJ">

                                                    </div>
                                                    <div class="Nclass-ZB-TC">
                                                        {{~value2.te:value4:index4}}
                                                        <div class="Rclass-ZB-TCC-stl">
                                                            <em>个人事务</em>
                                                            <em>{{=value4.subjectName}}({{=value4.teacherName}})</em>
                                                        </div>
                                                        {{~}}
                                                    </div>
                                                    {{?}}
                                                </td>
                                            {{??value2.type==3 || value2.type==4}}
                                            <td class="Nclass-main-Gray Nclass-main-PE ZB_table_base" x="{{=value1}}" y="{{=index+1}}">
                                                <%--无课或体育课--%>

                                                {{?value2.fb.length>0}}
                                                    {{=value2.fb[0]}}
                                                {{??value2.gs.length>0&&value2.te.length>0}}
                                                    教研|个人
                                                    {{?value2.type==4}}<br>体育走班{{?}}
                                                <%--{{??value2.gs.length>0}}
                                                    教研
                                                {{??value2.te.length>0}}
                                                    个人
                                                {{?}}--%>
                                                    <div class="Nclass-ZB-SJ">
                                                        <div class="inner"></div>
                                                    </div>
                                                    <div class="Nclass-ZB-SJJ">

                                                    </div>
                                                    <div class="Nclass-ZB-TC">
                                                        {{~value2.courseList:value3:index3}}
                                                        <div class="Nclass-ZB-TCC">
                                                            <em>{{=value3.courseName}}</em>
                                                            <em>{{=value3.teacherName}}</em>
                                                            <%--<i courseId="{{=value3.courseId}}" class="removeCourseX">X</i>--%>
                                                        </div>
                                                        {{~}}
                                                        {{~value2.gs:value4:index4}}
                                                        <div class="Rclass-ZB-TCC">
                                                            <em>集体教研</em>
                                                            <em>{{=value4.name}}</em>
                                                        </div>
                                                        {{~}}
                                                        {{~value2.te:value4:index4}}
                                                        <div class="Rclass-ZB-TCC-stl">
                                                            <em>个人事务</em>
                                                            <em>{{=value4.subjectName}}({{=value4.teacherName}})</em>
                                                            <%--<em>{{=value4.teacherName}}</em>--%>
                                                           <%-- <em>{{=value4.event}}</em>--%>
                                                        </div>
                                                        {{~}}
                                                    </div>
                                                {{??value2.gs.length>0}}
                                                    教研<br>{{?value2.type==4}}体育走班{{?}}
                                                    <div class="Nclass-ZB-SJ">
                                                        <div class="inner"></div>
                                                    </div>
                                                    <div class="Nclass-ZB-SJJ">

                                                    </div>
                                                    <div class="Nclass-ZB-TC">
                                                        {{~value2.courseList:value3:index3}}
                                                        <div class="Nclass-ZB-TCC">
                                                            <em>{{=value3.courseName}}</em>
                                                            <em>{{=value3.teacherName}}</em>
                                                            <%--<i courseId="{{=value3.courseId}}" class="removeCourseX">X</i>--%>
                                                        </div>
                                                        {{~}}
                                                        {{~value2.gs:value4:index4}}
                                                        <div class="Rclass-ZB-TCC">
                                                            <em>集体教研</em>
                                                            <em>{{=value4.name}}</em>
                                                        </div>
                                                        {{~}}
                                                    </div>
                                                {{??value2.te.length>0}}
                                                    个人<br>{{?value2.type==4}}体育走班{{?}}
                                                    <div class="Nclass-ZB-SJ">
                                                        <div class="inner"></div>
                                                    </div>
                                                    <div class="Nclass-ZB-SJJ">

                                                    </div>
                                                    <div class="Nclass-ZB-TC">
                                                        {{~value2.courseList:value3:index3}}
                                                        <div class="Nclass-ZB-TCC">
                                                            <em>{{=value3.courseName}}</em>
                                                            <em>{{=value3.teacherName}}</em>
                                                            <%--<i courseId="{{=value3.courseId}}" class="removeCourseX">X</i>--%>
                                                        </div>
                                                        {{~}}
                                                        {{~value2.te:value4:index4}}
                                                        <div class="Rclass-ZB-TCC-stl">
                                                            <em>个人事务</em>
                                                            <em>{{=value4.subjectName}}({{=value4.teacherName}})</em>
                                                            <%--<em>{{=value4.teacherName}}</em>--%>
                                                            <%--<em>{{=value4.event}}</em>--%>
                                                        </div>
                                                        {{~}}
                                                    </div>
                                                {{??value2.type==4}}
                                                    体育走班
                                                    <div class="Nclass-ZB-SJ">
                                                        <div class="inner"></div>
                                                    </div>
                                                    <div class="Nclass-ZB-SJJ">

                                                    </div>
                                                    <div class="Nclass-ZB-TC">
                                                        {{~value2.courseList:value3:index3}}
                                                        <div class="Nclass-ZB-TCC">
                                                            <em>{{=value3.courseName}}</em>
                                                            <em>{{=value3.teacherName}}</em>
                                                            <%--<i courseId="{{=value3.courseId}}" class="removeCourseX">X</i>--%>
                                                        </div>
                                                        {{~}}
                                                    </div>
                                                {{?}}
                                            </td>
                                            {{?}}
                                            {{?}}
                                            {{??}}
                                            {{?value2.x==value1 && value2.y==index+1}}
                                            {{?value2.isOk==1}}
                                            {{?value2.type==0}}
                                            <td class="Nclass-main-ZB ZB_table_base Nclass-main-Green"
                                                x="{{=value1}}" y="{{=index+1}}">
                                                {{=value2.subjectName}}走班({{=value2.courseList.length}})
                                                {{?value2.fb.length>0}}
                                                {{=value2.fb[0]}}
                                                {{??value2.gs.length>0&&value2.te.length>0}}
                                                <br>教研|个人
                                                {{??value2.gs.length>0}}
                                                <br><i style="color: #fd2b2b;">[教研]</i>
                                                {{??value2.te.length>0}}
                                                <br>个人
                                                {{?}}

                                            </td>
                                            {{??value2.type==2}}
                                            <td class="Nclass-main-Green" x="{{=value1}}" y="{{=index+1}}">
                                                {{=value2.courseList[0].courseName}}
                                                {{?value2.fb.length>0}}
                                                {{=value2.fb[0]}}
                                                {{??value2.gs.length>0&&value2.te.length>0}}
                                                <br>教研|个人
                                                {{??value2.gs.length>0}}
                                                <br><i style="color: #fd2b2b;">[教研]</i>
                                                {{??value2.te.length>0}}
                                                <br>个人
                                                {{?}}
                                            </td>
                                            {{??value2.type==3}}
                                            <td class="Nclass-main-Green feizoubanAdd" x="{{=value1}}" y="{{=index+1}}">
                                                {{?value2.fb.length>0}}
                                                {{=value2.fb[0]}}
                                                {{??value2.gs.length>0&&value2.te.length>0}}
                                                教研|个人
                                                {{??value2.gs.length>0}}
                                                <i style="color: #fd2b2b;">[教研]</i>
                                                {{??value2.te.length>0}}
                                                个人
                                                {{?}}
                                            </td>
                                            {{??value2.type==4}}
                                            <td class="Nclass-main-ZB" x="{{=value1}}" y="{{=index+1}}">
                                                体育走班
                                                {{?value2.fb.length>0}}
                                                {{=value2.fb[0]}}
                                                {{??value2.gs.length>0&&value2.te.length>0}}
                                                教研|个人
                                                {{??value2.gs.length>0}}
                                                <i style="color: #fd2b2b;">[教研]</i>
                                                {{??value2.te.length>0}}
                                                个人
                                                {{?}}
                                            </td>
                                            {{?}}
                                            {{??value2.isOk==0}}
                                            {{?value2.type==0}}
                                            <td class="Nclass-main-ZB ZB_table_base Nclass-main-Red"
                                                x="{{=value1}}" y="{{=index+1}}">
                                                {{=value2.subjectName}}走班({{=value2.courseList.length}})
                                                {{?value2.fb.length>0}}
                                                {{=value2.fb[0]}}
                                                {{??value2.gs.length>0&&value2.te.length>0}}
                                                <br>教研|个人
                                                {{??value2.gs.length>0}}
                                                <br><i style="color: #fd2b2b;">[教研]</i>
                                                {{??value2.te.length>0}}
                                                <br>个人
                                                {{?}}
                                            </td>
                                            {{??value2.type==2}}
                                            <td class="Nclass-main-Red" x="{{=value1}}" y="{{=index+1}}">
                                                {{=value2.courseList[0].courseName}}
                                                {{?value2.fb.length>0}}
                                                {{=value2.fb[0]}}
                                                {{??value2.gs.length>0&&value2.te.length>0}}
                                                <br>教研|个人
                                                {{??value2.gs.length>0}}
                                                <br><i style="color: #fd2b2b;">[教研]</i>
                                                {{??value2.te.length>0}}
                                                <br>个人
                                                {{?}}
                                            </td>
                                            {{??value2.type==3}}
                                            <td class="Nclass-main-Red" x="{{=value1}}" y="{{=index+1}}">
                                                {{?value2.fb.length>0}}
                                                {{=value2.fb[0]}}
                                                {{??value2.gs.length>0&&value2.te.length>0}}
                                                教研|个人
                                                {{??value2.gs.length>0}}
                                                <i style="color: #fd2b2b;">[教研]</i>
                                                {{??value2.te.length>0}}
                                                个人
                                                {{?}}
                                            </td>
                                            {{??value2.type==4}}
                                            <td class="Nclass-main-ZB" x="{{=value1}}" y="{{=index+1}}">
                                                体育走班
                                                {{?value2.fb.length>0}}
                                                {{=value2.fb[0]}}
                                                {{??value2.gs.length>0&&value2.te.length>0}}
                                                教研|个人
                                                {{??value2.gs.length>0}}
                                                <i style="color: #fd2b2b;">[教研]</i>
                                                {{??value2.te.length>0}}
                                                个人
                                                {{?}}
                                            </td>
                                            {{?}}
                                            {{??value2.isOk==2}}
                                            {{?value2.type==0}}
                                            <td class="Nclass-main-ZB ZB_table_base Nclass-main-JY"
                                                x="{{=value1}}" y="{{=index+1}}">
                                                {{=value2.subjectName}}走班({{=value2.courseList.length}})
                                                {{?value2.fb.length>0}}
                                                {{=value2.fb[0]}}
                                                {{??value2.gs.length>0&&value2.te.length>0}}
                                                <br>教研|个人
                                                {{??value2.gs.length>0}}
                                                <br><i style="color: #fd2b2b;">[教研]</i>
                                                {{??value2.te.length>0}}
                                                <br>个人
                                                {{?}}
                                            </td>
                                            {{??value2.type==2}}
                                            <td class="Nclass-main-JY" x="{{=value1}}" y="{{=index+1}}">
                                                {{=value2.courseList[0]}}
                                                {{?value2.fb.length>0}}
                                                {{=value2.fb[0]}}
                                                {{??value2.gs.length>0&&value2.te.length>0}}
                                                <br>教研|个人
                                                {{??value2.gs.length>0}}
                                                <br><i style="color: #fd2b2b;">[教研]</i>
                                                {{??value2.te.length>0}}
                                                <br>个人
                                                {{?}}
                                            </td>
                                            {{??value2.type==3}}
                                            <td class="Nclass-main-JY" x="{{=value1}}" y="{{=index+1}}">
                                                {{?value2.fb.length>0}}
                                                {{=value2.fb[0]}}
                                                {{??value2.gs.length>0&&value2.te.length>0}}
                                                教研|个人
                                                {{??value2.gs.length>0}}
                                                <i style="color: #fd2b2b;">[教研]</i>
                                                {{??value2.te.length>0}}
                                                个人
                                                {{?}}
                                            </td>
                                            {{??value2.type==4}}
                                            <td class="Nclass-main-ZB " x="{{=value1}}" y="{{=index+1}}">
                                                体育走班
                                                {{?value2.fb.length>0}}
                                                {{=value2.fb[0]}}
                                                {{??value2.gs.length>0&&value2.te.length>0}}
                                                教研|个人
                                                {{??value2.gs.length>0}}
                                                <i style="color: #fd2b2b;">[教研]</i>
                                                {{??value2.te.length>0}}
                                                个人
                                                {{?}}
                                            </td>
                                            {{?}}
                                            {{?}}
                                            {{?}}
                                            {{?}}
                                            {{~}}
                                            {{~}}
                                        </tr>
                                        {{~}}
                                    </script>
                                </div>
                            </div>
                        </dd>
                    </dl>
                </div>
                <!--=====================非走班排课 end========================-->
                <!--=====================其他课程设置============================-->
                <div class="tab-Nclass" id="tab-Oclass" style="display: none">
                    <dl>
                        <dd>
                            <em>年级:${gradeName}</em>
                            <em>班级</em>
                            <select id="classShow5">
                                <%--<option>一班</option>--%>
                                <%--<option>二班</option>--%>
                            </select>
                            <script type="application/template" id="classTempJs5">
                                {{~it.data:value:index}}
                                <option value="{{=value.classId}}">{{=value.className}}</option>
                                {{~}}
                            </script>
                            <button id="selectFeizouban5">筛选</button>
                            <button id="autoArrange5">一键编排</button>
                            <button id="autoClear5">一键清空</button>
                        </dd>
                        <%--<dd id="FZBshow" style="display: none">
                            <p style="color: #ff0000">可用课程已无法安排“$课程”，请重新调整。
                                <a>查看原因</a>
                                <i>X</i>
                            </p>
                        </dd>--%>
                        <dd>
                            <div class="Nclass-main clearfix">
                                <div class="Nclass-main-right" style="float: none">
                                    <table  id="tableShow5">

                                    </table>
                                    <script type="application/template" id="tableTempJs5">
                                        <tr>
                                            <th>节次/时间</th>
                                            {{~it.data.classDays:value:index}}
                                            <th>星期{{?value==1}}一
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
                                        {{~it.data.classTime:value:index}}
                                        <tr>
                                            <td>
                                                <em>第{{=index+1}}节</em><br>
                                                <i>{{=value}}</i>
                                            </td>
                                            {{~it.data.classDays:value1:index1}}
                                            <%--x:value1 y:index+1
                                             遍历 chooseCourse.courseList  已选课
                                             遍历 pointList 可选位置
                                             遍历 events :不可排课位置--%>
                                            {{~it.data.detail:value2:index2}}
                                            {{?it.data.state==0}}
                                            {{?value2.x==value1 && value2.y==index+1}}
                                            {{?value2.type==0}}
                                                <td class="Nclass-main-ZB ZB_table_base" x="{{=value1}}" y="{{=index+1}}">
                                                    {{=value2.subjectName}}走班({{=value2.courseList.length}})
                                                    {{?value2.fb.length>0}}
                                                    {{=value2.fb[0]}}
                                                    {{??value2.gs.length>0&&value2.te.length>0}}
                                                    <br>教研|个人
                                                    {{??value2.gs.length>0}}
                                                    <br><i style="color: #fd2b2b;">[教研]</i>
                                                    {{??value2.te.length>0}}
                                                    <br>个人
                                                    {{?}}
                                                    <!--===========弹出框=============-->
                                                    <div class="Nclass-ZB-SJ">
                                                        <div class="inner"></div>
                                                    </div>
                                                    <div class="Nclass-ZB-SJJ">

                                                    </div>
                                                    <div class="Nclass-ZB-TC">
                                                        {{~value2.courseList:value3:index3}}
                                                        <div class="Nclass-ZB-TCC">
                                                            <em>{{=value3.courseName}}</em>
                                                            <em>{{=value3.teacherName}}</em>
                                                            <em>{{=value3.classRoom}}</em>
                                                            <%--<i courseId="{{=value3.courseId}}" class="removeCourseX">X</i>--%>
                                                        </div>
                                                        {{~}}
                                                        {{~value2.gs:value4:index4}}
                                                        <div class="Rclass-ZB-TCC">
                                                            <em>集体教研</em>
                                                            <em>{{=value4.name}}</em>
                                                        </div>
                                                        {{~}}
                                                        {{~value2.te:value4:index4}}
                                                        <div class="Rclass-ZB-TCC-stl">
                                                            <em>个人事务</em>
                                                            <em>{{=value4.subjectName}}({{=value4.teacherName}})</em>
                                                            <%--<em>{{=value4.teacherName}}</em>--%>
                                                            <%--<em>{{=value4.event}}</em>--%>
                                                        </div>
                                                        {{~}}
                                                    </div>
                                                </td>
                                            {{??value2.type==2}}
                                                <td class="Nclass-main-Gray Nclass-main-PE ZB_table_base" x="{{=value1}}" y="{{=index+1}}" cid="{{=value2.courseList[0].courseId}}">
                                                    {{=value2.courseList[0].courseName}}
                                                        {{?value2.fb.length>0}}
                                                    {{=value2.fb[0]}}
                                                    {{??value2.gs.length>0&&value2.te.length>0}}
                                                        <br>教研|个人
                                                        <div class="Nclass-ZB-SJ">
                                                            <div class="inner"></div>
                                                        </div>
                                                        <div class="Nclass-ZB-SJJ">

                                                        </div>
                                                        <div class="Nclass-ZB-TC">
                                                            {{~value2.gs:value4:index4}}
                                                            <div class="Rclass-ZB-TCC">
                                                                <em>集体教研</em>
                                                                <em>{{=value4.name}}</em>
                                                            </div>
                                                            {{~}}
                                                            {{~value2.te:value4:index4}}
                                                            <div class="Rclass-ZB-TCC-stl">
                                                                <em>个人事务</em>
                                                                <em>{{=value4.subjectName}}({{=value4.teacherName}})</em>
                                                            </div>
                                                            {{~}}
                                                        </div>
                                                    {{??value2.gs.length>0}}
                                                        <br><i style="color: #fd2b2b;">[教研]</i>
                                                        <div class="Nclass-ZB-SJ">
                                                            <div class="inner"></div>
                                                        </div>
                                                        <div class="Nclass-ZB-SJJ">

                                                        </div>
                                                        <div class="Nclass-ZB-TC">
                                                            {{~value2.gs:value4:index4}}
                                                            <div class="Rclass-ZB-TCC">
                                                                <em>集体教研</em>
                                                                <em>{{=value4.name}}</em>
                                                            </div>
                                                            {{~}}
                                                        </div>
                                                    {{??value2.te.length>0}}
                                                        <br>个人
                                                        <div class="Nclass-ZB-SJ">
                                                            <div class="inner"></div>
                                                        </div>
                                                        <div class="Nclass-ZB-SJJ">

                                                        </div>
                                                        <div class="Nclass-ZB-TC">
                                                            {{~value2.te:value4:index4}}
                                                                <div class="Rclass-ZB-TCC-stl">
                                                                    <em>个人事务</em>
                                                                    <em>{{=value4.subjectName}}({{=value4.teacherName}})</em>
                                                                    <%--<em>{{=value4.teacherName}}</em>--%>
                                                                    <%--<em>{{=value4.event}}</em>--%>
                                                                </div>
                                                            {{~}}
                                                        </div>
                                                    {{?}}
                                                </td>
                                            {{??value2.type==3 || value2.type==4}}
                                                <td class="Nclass-main-Gray Nclass-main-PE ZB_table_base" x="{{=value1}}" y="{{=index+1}}">
                                                    <%--无课或体育课--%>

                                                    {{?value2.fb.length>0}}
                                                        {{=value2.fb[0]}}
                                                    {{??value2.gs.length>0&&value2.te.length>0}}
                                                        教研|个人
                                                        {{?value2.type==4}}<br>体育走班{{?}}
                                                        <%--{{??value2.gs.length>0}}
                                                            教研
                                                        {{??value2.te.length>0}}
                                                            个人
                                                        {{?}}--%>
                                                        <div class="Nclass-ZB-SJ">
                                                            <div class="inner"></div>
                                                        </div>
                                                        <div class="Nclass-ZB-SJJ">

                                                        </div>
                                                        <div class="Nclass-ZB-TC">
                                                            {{~value2.courseList:value3:index3}}
                                                                <div class="Nclass-ZB-TCC">
                                                                    <em>{{=value3.courseName}}</em>
                                                                    <em>{{=value3.teacherName}}</em>
                                                                    <%--<i courseId="{{=value3.courseId}}" class="removeCourseX">X</i>--%>
                                                                </div>
                                                            {{~}}
                                                            {{~value2.gs:value4:index4}}
                                                                <div class="Rclass-ZB-TCC">
                                                                    <em>集体教研</em>
                                                                    <em>{{=value4.name}}</em>
                                                                </div>
                                                            {{~}}
                                                            {{~value2.te:value4:index4}}
                                                                <div class="Rclass-ZB-TCC-stl">
                                                                    <em>个人事务</em>
                                                                    <em>{{=value4.subjectName}}({{=value4.teacherName}})</em>
                                                                </div>
                                                            {{~}}
                                                        </div>
                                                    {{??value2.gs.length>0}}
                                                        教研<br>{{?value2.type==4}}体育走班{{?}}
                                                        <div class="Nclass-ZB-SJ">
                                                            <div class="inner"></div>
                                                        </div>
                                                        <div class="Nclass-ZB-SJJ">

                                                        </div>
                                                        <div class="Nclass-ZB-TC">
                                                            {{~value2.courseList:value3:index3}}
                                                                <div class="Nclass-ZB-TCC">
                                                                    <em>{{=value3.courseName}}</em>
                                                                    <em>{{=value3.teacherName}}</em>
                                                                    <%--<i courseId="{{=value3.courseId}}" class="removeCourseX">X</i>--%>
                                                                </div>
                                                            {{~}}
                                                            {{~value2.gs:value4:index4}}
                                                                <div class="Rclass-ZB-TCC">
                                                                    <em>集体教研</em>
                                                                    <em>{{=value4.name}}</em>
                                                                </div>
                                                            {{~}}
                                                        </div>
                                                    {{??value2.te.length>0}}
                                                        个人<br>{{?value2.type==4}}体育走班{{?}}
                                                        <div class="Nclass-ZB-SJ">
                                                            <div class="inner"></div>
                                                        </div>
                                                        <div class="Nclass-ZB-SJJ">

                                                        </div>
                                                        <div class="Nclass-ZB-TC">
                                                            {{~value2.courseList:value3:index3}}
                                                                <div class="Nclass-ZB-TCC">
                                                                    <em>{{=value3.courseName}}</em>
                                                                    <em>{{=value3.teacherName}}</em>
                                                                    <%--<i courseId="{{=value3.courseId}}" class="removeCourseX">X</i>--%>
                                                                </div>
                                                            {{~}}
                                                            {{~value2.te:value4:index4}}
                                                                <div class="Rclass-ZB-TCC-stl">
                                                                    <em>个人事务</em>
                                                                    <em>{{=value4.subjectName}}({{=value4.teacherName}})</em>
                                                                    <%--<em>{{=value4.teacherName}}</em>--%>
                                                                    <%--<em>{{=value4.event}}</em>--%>
                                                                </div>
                                                            {{~}}
                                                        </div>
                                                    {{??value2.type==4}}
                                                        体育走班
                                                        <div class="Nclass-ZB-SJ">
                                                            <div class="inner"></div>
                                                        </div>
                                                        <div class="Nclass-ZB-SJJ">

                                                        </div>
                                                        <div class="Nclass-ZB-TC">
                                                            {{~value2.courseList:value3:index3}}
                                                                <div class="Nclass-ZB-TCC">
                                                                    <em>{{=value3.courseName}}</em>
                                                                    <em>{{=value3.teacherName}}</em>
                                                                </div>
                                                            {{~}}
                                                        </div>
                                                    {{?}}
                                                </td>
                                            {{?}}
                                            {{?}}
                                            {{?}}
                                            {{~}}
                                            {{~}}
                                        </tr>
                                        {{~}}
                                    </script>
                                </div>
                            </div>
                        </dd>
                    </dl>
                </div>
                <!--============================================================-->
                <!--=====================小走班老师教室设置==========================-->
                <div class="tiyumanage_main" id="xzbSet">
                    <input type="hidden">
                    <span>年级:${gradeName}</span>
                    <button id="autoSetXZB">一键设置</button>
                    <button id="finsihBtn">完成</button>
                    <script type="application/template" id="courseListTempJs">
                        <tr>
                            <th>课程时间</th>
                            <th>教学班</th>
                            <th>人数</th>
                            <th>任课老师</th>
                            <th>上课教室</th>
                            <th>操作</th>
                        </tr>
                                {{~it.data:value:index}}
                                <tr>
                                    <td>周{{=value.xIndex}}第{{=value.yIndex}}节</td>
                                    <td>{{=value.courseName}}</td>
                                    <td class="view" coid="{{=value.courseId}}" style="cursor: pointer">{{=value.studentCount}}</td>
                                    <td>{{=value.teacherName?value.teacherName:""}}</td>
                                    <td>{{=value.classroomName?value.classroomName:""}}</td>
                                    <td><a href="#" class="setting" sid="{{=value.subjectId}}" coid="{{=value.courseId}}"
                                           nm="{{=value.courseName}}" x="{{=value.xIndex}}" y="{{=value.yIndex}}"
                                           rid="{{=value.classroomId}}" tid="{{=value.teacherId}}">设置</a></td>
                                </tr>
                                {{~}}

                    </script>
                    <table rules=all id="courseListShow">
                    </table>
                </div>
                <!--===============================设置结束================================-->
                <!--=====================体育班老师教室设置==========================-->
                <div class="tiyumanage_main" id="physicalSet">
                    <span>年级:${gradeName}</span>
                    <button id="autoSetPhy">一键设置</button>
                    <button id="finsihBtnPhy">完成</button>
                    <script type="application/template" id="phyCourseListTempJs">
                        <tr>
                            <th>课程时间</th>
                            <th>教学班</th>
                            <th>人数</th>
                            <th>行政班</th>
                            <th>任课老师</th>
                            <th>操作</th>
                        </tr>
                        {{~it.data:value:index}}
                        <tr>
                            <td>周{{=value.xIndex}}第{{=value.yIndex}}节</td>
                            <td>{{=value.courseName}}</td>
                            <td>{{=value.studentCount}}</td>
                            <td>{{=value.className}}</td>
                            <td>{{=value.teacherName?value.teacherName:""}}</td>
                            <td><a href="#" class="settingPhy" sid="{{=value.subjectId}}" coid="{{=value.courseId}}"
                                   nm="{{=value.courseName}}" classnm="{{=value.className}}" tid="{{=value.teacherId}}">设置</a></td>
                        </tr>
                        {{~}}
                    </script>
                    <table rules=all id="phyCourseListShow">
                    </table>
                </div>
                <%--小走班详情--%>
                <div class="clash-detial">
                    <%--<div class="detial-bg"></div>--%>
                    <div class="detial-wind">
                        <div class="setwind-top">
                            <i style="font-size:16px;">学生详情</i>
                            <i class="detial-cl">×</i>
                        </div>
                        <div class="clash-info">
                            <p class="p1"></p>
                            <p id="teacher">老师</p>
                            <p id="classRoom"></p>
                        </div>
                        <div id="table" style="max-height: 276px;overflow-y: auto">
                            <table class="clash-tab">

                            </table>
                        </div>
                        <script type="application/template" id="detailTempJs">
                            <tr class="row1">
                                <td>学生姓名</td>
                                <td>性别</td>
                                <td>行政班级</td>
                            </tr>
                            {{~it.data:value:index}}
                            <tr class="row">
                                <td>{{=value.student}}</td>
                                <td>{{?value.sex==0}}女{{??}}男{{?}}</td>
                                <td>{{=value.className}}</td>
                            </tr>
                            {{~}}
                        </script>

                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--背景 start-->
    <%--<div class="bg"></div>--%>
    <!--背景 end-->

    <!--弹窗 教研 start-->
    <div class="Rclass-JY-wind">
        <div class="JY-wind-1"> &nbsp;&nbsp;&nbsp;教研<em class="JY-wind-x JY-wind-cl">×</em> </div>
        <span>该时段有教研活动,不能选课！</span>
        <div class="JY-wind-2">
            <button class="submitData">确定</button>
            <button class="JY-wind-x JY-wind-canc">取消</button>
        </div>
    </div>
    <div class="Rclass-JY-wind2">
        <div class="JY-wind-1"> &nbsp;&nbsp;&nbsp;教研<em class="JY-wind-x JY-wind-cl">×</em> </div>
        <span>该时段有教研活动,不能选课！</span>
        <div class="JY-wind-2">
            <button class="submitData2">确定</button>
            <button class="JY-wind-x JY-wind-canc">取消</button>
        </div>
    </div>
    <!--弹窗 教研 end-->
    <!--弹出框 教研活动 start-->
    <div class="gring-CUR" id="gring-CUR1">
        <div class="gring-CUR-top">
            <em>不可排课事件/集体教研</em>
            <i class="update-X">X</i>
        </div>
        <dl>
            <dt id="gringTopShow">

            </dt>
            <script type="application/template" id="gringTopTempJs">
                <strong>年级:</strong><strong>${gradeName}</strong><strong></strong>
                <strong>{{=it.data.xIndex}}</strong><strong>{{=it.data.yIndex}}</strong>
                <strong>{{=it.data.time}}</strong>
            </script>
            <dd id="forbieEventShow">
                <%--<button>午饭</button><button>不排课</button>--%>
            </dd>
            <script type="application/template" id="forbitEventJs">
                {{?it.data.indexOf("午饭")>-1}}
                <button class="fbEvent selected">午饭</button>
                {{??}}
                <button class="fbEvent">午饭</button>
                {{?}}
                {{?it.data.indexOf("不排课")>-1}}
                <button class="fbEvent selected">不排课</button>
                {{??}}
                <button class="fbEvent">不排课</button>
                {{?}}
                {{?it.data.length>0&&it.data.indexOf("午饭")==-1&&it.data.indexOf("不排课")==-1}}
                    <button class="fbEvent selected">{{=it.data[0]}}</button>
                {{?}}
                <button id="customBtn">自定义</button>

            </script>
            <dd id="custom"  style="display: none">
                <input type="input" id="customVal" style="border: 1px solid #000"> <button id="customAdd">添加</button>
            </dd>
            <dd>
                <em class="bold-weight">集体教研</em>
            </dd>
            <dd id="subjectListShow">

            </dd>
            <script type="application/template" id="subjectListTempJs">
                {{~it.data:value:index}}
                {{?value.have==1}}
                <button value="{{=value.id}}" class="groupEvent selected">{{=value.name}}</button>
                {{??}}
                <button value="{{=value.id}}" class="groupEvent">{{=value.name}}</button>
                {{?}}
                {{~}}
            </script>
            <%--<dd>
                <em class="bold-weight">集体会议/个人事务</em>
            </dd><dd>
                <em class="bold-weight">名称：</em>
                <input style="border:1px solid #959595;width: 243px;height: 32px;" class="teacherEvent" id="meetingName">
                <button class="arranging-TJ addMeeting" style="margin-left: 130px;width: 100px;vertical-align: top;
  margin: 6px 0 0 0;">添加会议</button>
            </dd>
            <dd class="dd-sub-tea">
                <script type="application/template" id="subTeacJs">
                    {{~it.data:value:index}}
                    <div class="div-sub clearfix">
                        <span sid="{{=value.t.idStr}}">{{=value.t.value}}：</span>
                        <div class="div-subtea">
                            &lt;%&ndash;<label class="label-sub"><input type="checkbox" class="seleAll">全选</label>&ndash;%&gt;
                            {{~value.list:value1:index1}}
                            <label class="label-sub"><input class="meetingTea" name="choosedTeacher" value="{{=value1.idStr}}" sid="{{=value.t.idStr}}" type="checkbox">{{=value1.value}}</label>
                            {{~}}
                        </div>
                    </div>
                    {{~}}
                </script>
                <div class="left" id="subjectTeaListShow">

                </div>


            </dd>
            <div class="addedMettingDiv">
            </div>
            <script type="application/template" id="meetingJs">
                <label>已添加会议:</label>
                {{~it:value:index}}
                <lable class="viewMeeting" v="{{=value}}">{{=value}}</lable><em class="deleteMetting" style="cursor: pointer" nm="{{=value}}">[删除]</em>
                {{~}}
                {{?it.length>0}}
                <em class="addContinue" style="cursor: pointer">继续添加新会议</em>
                {{?}}
            </script>--%>

            <%--<dd>
                <em>学科</em>
                <select id="pSubjectListShow">
                    &lt;%&ndash;<option>数学</option>
                    <option>英语</option>
                    <option>语文</option>&ndash;%&gt;
                </select>
                <script type="application/template" id="pSubjectListTempJs">
                    {{~it.data:value:index}}
                    <option value="{{=value.id}}">{{=value.name}}</option>
                    {{~}}
                </script>
            </dd>
            <dd>
                <em>老师</em>
                <select id="pTeacherShow">
                    &lt;%&ndash;<option>siri</option>
                    <option>Yujia</option>&ndash;%&gt;
                </select>
                <script type="application/template" id="pTeacherTempJs">
                    {{~it.data:value:index}}
                    <option value="{{=value.id}}">{{=value.name}}</option>
                    {{~}}
                </script>
            </dd>
            <dd>
                &lt;%&ndash;<em>事务</em>
                <textarea style="border:1px solid #959595;" class="teacherEvent"></textarea>&ndash;%&gt;
                <button class="arranging-TJ" style="margin-left: 130px;width: 100px;">添加事务/会议</button>
            </dd>
            <dd>
                <div style="width:370px;max-height:106px; overflow-y:auto;">

                    <table id="eventShow" style="max-height: 105px;margin-top: 0px;">
                    </table>
                </div>
                <script type="application/template" id="eventTempJs">
                    {{~it.data:value:index}}
                    <tr>
                        <td sid="{{=value.subjectId}}">{{=value.subjectName}}</td>
                        <td tid="{{=value.teacherId}}">{{=value.teacherName}}</td>
                        <td>{{=value.event}}</td>
                        <td class="removeX">X</td>
                    </tr>
                    {{~}}
                </script>
            </dd>--%>
            <dd class="arranging-bottom">
                <button class="arranging-QD">确定</button>
                <button class="arranging-QX">取消</button>
            </dd>
        </dl>
    </div>
    <div class="gring-CUR" id="gring-CUR2">
        <div class="gring-CUR-top">
            <em>集体会议</em>
            <i class="update-X2">X</i>
        </div>
        <dl>
            <%--<dt id="gringTopShow">

            </dt>
            <script type="application/template" id="gringTopTempJs">
                <strong>年级:</strong><strong>${gradeName}</strong><strong></strong>
                <strong>{{=it.data.xIndex}}</strong><strong>{{=it.data.yIndex}}</strong>
                <strong>{{=it.data.time}}</strong>
            </script>--%>


            <dd>
                <em class="bold-weight">名称：</em>
                <input style="border:1px solid #959595;width: 243px;height: 32px;" class="teacherEvent" id="meetingName">
                <button class="arranging-TJ addMeeting" style="margin-left: 130px;width: 100px;vertical-align: top;
  margin: 6px 0 0 0;">添加会议</button>
            </dd>
                <div class="addedMettingDiv">
                    <em class="bold-weight">已添加会议:</em>
                    <%--<label>已添加会议</label>
                    <lable>会议一</lable>x
                    <label>会议er</label>x--%>
                </div>
                <dd id="choosedTimeList">
                    <em class="bold-weight">会议时间：</em>
                </dd>
                <script type="application/template" id="choosedTimeJs">
                    <em class="bold-weight">会议时间：</em>
                            {{~it.data:value:index}}
                    <label>星期{{?value.x==1}}一{{??value.x==2}}二{{??value.x==3}}三{{??value.x==4}}四{{??value.x==5}}五{{??value.x==6}}六{{??value.x==7}}日{{?}}第{{=value.y}}节</label><img class="removeTime" style="cursor: pointer" x="{{=value.x}}" y="{{=value.y}}"src="/images/del.png"/>
                    {{~}}
                </script>
                <dd>
                    <em>时间</em>
                    <select style="width: 120px;" id="meetWeek">
                        <option value="1">星期一</option>
                        <option value="2">星期二</option>
                        <option value="3">星期三</option>
                        <option value="4">星期四</option>
                        <option value="5">星期五</option>
                        <option value="6">星期六</option>
                        <option value="7">星期日</option>
                    </select>
                    <em>节数</em>
                    <select style="width: 120px;" id="meetCourse">
                        <option value="1">第1节</option>
                        <option value="2">第2节</option>
                        <option value="3">第3节</option>
                        <option value="4">第4节</option>
                        <option value="5">第5节</option>
                        <option value="6">第6节</option>
                        <option value="7">第7节</option>
                        <option value="8">第8节</option>
                        <option value="9">第9节</option>
                        <option value="10">第10节</option>
                    </select>
                    <button id="addTime">添加时间</button>
                </dd>

            <dd class="dd-sub-tea">
                <script type="application/template" id="subTeacJs">
                    {{~it.data:value:index}}
                    <div class="div-sub clearfix">
                        <span sid="{{=value.t.idStr}}">{{=value.t.value}}：</span>
                        <div class="div-subtea">
                            <label class="label-sub"><input type="checkbox" class="seleAll">全选</label>
                            {{~value.list:value1:index1}}
                            <label class="label-sub"><input class="meetingTea" name="choosedTeacher" value="{{=value1.idStr}}" sid="{{=value.t.idStr}}" type="checkbox">{{=value1.value}}</label>
                            {{~}}
                        </div>
                    </div>
                    {{~}}
                </script>
                <div class="left" id="subjectTeaListShow">
                </div>


            </dd>

            <script type="application/template" id="meetingJs">
                <em class="bold-weight">已添加会议:</em>
                {{~it:value:index}}
                <lable class="viewMeeting" v="{{=value}}">{{=value}}</lable><img class="deleteMetting" style="cursor: pointer" nm="{{=value}}" src="/images/del.png"/>
                {{~}}
                {{?it.length>0}}
                <em class="addContinue" style="cursor: pointer">继续添加新会议</em>
                {{?}}
            </script>
            <dd class="arranging-bottom">
                <button class="arranging-QD2">确定</button>
                <button class="arranging-QX2">取消</button>
            </dd>
        </dl>
    </div>
    <!--弹出框 教研活动 end-->

</div>
<div class="phy-conf">
    <div class="edit-set-wind" style="height: 250px ">
        <div class="setwind-top">
            <i style="font-size:16px;">自动排课</i>
            <i class="setwind-cl py-btn">×</i>
        </div>
        <div class="edit-form" >
            <span>每周课时数（1~5）</span><input id="classCount" type="number" style="border: 1px solid #000" value="3"><br>
            <span>同一时间允许排两门课</span><input type="checkbox" id="repeat">
        </div>
        <div class="cc-btn">
            <div class="cofi-btn py-ok">确定</div>
            <div class="canc-btn py-btn">取消</div>
        </div>
    </div>
</div>
<div class="edit-set-div">
    <input type="hidden"  id="courseType">
    <input type="hidden" id="courseId">
    <%--<div class="edit-set-bg"></div>--%>
    <div class="edit-set-wind">
        <div class="setwind-top">
            <i style="font-size:16px;">设置</i>
            <i class="setwind-cl">×</i>
        </div>
        <div class="edit-form">
            <span>班级名称：</span><i id="coursenm"></i><br />
            <span>任课老师</span>
            <select id="teacherlist">
            </select>
            <script type="application/template" id="teacherlistTempJs">
                {{~it.data:value:index}}
                <option value="{{=value.teacherId}}">{{=value.teacherName}}</option>
                {{~}}
            </script>
            <br />
            <span class="xzbSetEl">上课教室</span>
            <select class="xzbSetEl" id="classroomlist">
            </select>
            <script type="application/template" id="classroomlistTempJs">
                {{~it.data:value:index}}
                <option value="{{=value.id}}">{{=value.roomName}}</option>
                {{~}}
            </script>
        </div>
        <div class="cc-btn">
            <div class="cofi-btn">确定</div>
            <div class="canc-btn">取消</div>
        </div>
    </div>
</div>

<!--=======================================弹出层背景 start========================================-->
<div class="bg"></div>
<!--=======================================弹出层背景 end========================================
<!--#foot-->
<%@ include file="../../common_new/foot.jsp" %>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js?v=1"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('arranging');
</script>
</body>
</html>