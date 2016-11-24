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
    <link href="/static_new/css/zouban/adjust.css?v=2015041602" rel="stylesheet"/>

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
    <!--.col-right-->
    <div class="col-right">
        <input type="hidden" id="term" value="${term}">
        <input type="hidden" id="year" value="${year}">
        <input type="hidden" id="courseConfId">
        <input type="hidden" id="gradeId" value="${gradeId}">
        <input type="hidden" id="allweek" value="${allweek}">
        <input type="hidden" id="curweek" value="${curweek}">
        <input type="hidden" id="mode" value="${mode}">
        <div class="tab-col">
            <div class="tab-top clearfix">
                <ul>
                    <li class="cur tab-zouban" id="curriculum"><a href="javascript:;">调课</a></li>
                </ul>
                <a class="backUrl tab-back" href="javascript:;"<%--href="../paike/index.do?version=58"--%>>&lt;返回教务管理首页</a>
            </div>
            <div id="contentt">
                <!--==========================调课列表==============================-->
                <div class="tab-adjust">
                    <dl>
                        <dt>
                            <em>学期：${term}</em><i>${gradeName}</i>
                        </dt>
                        <dd>
                            <select id="typeSelect">
                                <option value="-1">全部</option>
                                <option value="0">临时周内调课</option>
                                <option value="1">临时跨周调课</option>
                                <option value="2">长期调课</option>
                            </select>
                            <button class="adjust-BU adjust-TJ">添加</button>
                        </dd>
                        <dd>
                            <table id="noticeListShow" style="width:712px;">

                            </table>
                            <script type="application/template" id="noticeListTempJs">
                                <tr>
                                    <th>调课名称</th>
                                    <th>类型</th>
                                    <th>被调整课表</th>
                                    <th>修改时间</th>
                                    <%--<th>调课通知</th>--%>
                                    <th>操作</th>
                                </tr>
                                {{~it.data:value:index}}
                                <tr>
                                    <td class="TD">{{=value.name}}</td>
                                    <td>{{=value.type_str}}</td>
                                    <td class="TD">第{{=value.week}}周</td>
                                    <td>{{=value.time}}</td>
                                    <%--<td class="hover viewNotice" nid="{{=value.id}}" >查看</td>--%>
                                    <td nid="{{=value.id}}">
                                        <em class="hover viewNotice">查看</em>|<em class="hover deleteNotice" tableIds="{{=value.tableIds}}">删除</em>
                                    </td>
                                </tr>
                                {{~}}
                            </script>

                        </dd>
                        <!--.page-links-->
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
                        <!--/.page-links-->
                    </dl>
                </div>
                <!--=======================临时周内调课=========================-->
                <div class="tab-linshi">
                    <dl>
                        <dt>
                            <a class="back" id="back" style="cursor: pointer">&lt;返回</a>
                        </dt>
                        <dd>
                            <em>${gradeName}</em><em>/</em><i class="typeShow">临时周内调课(第三周)</i>
                            <button class="adjust-BU linshi-FB" id="publish_course">发布调课</button>
                            <button class="adjust-BU" id="cancel">取消</button>
                            <%--<button class="adjust-BU">保存</button>--%>
                        </dd>
                        <dd>
                            <select id="courseTypeSelect">
                                <option value="0">非走班</option>
                                <option value="1">走班课程</option>
                            </select>
                            <select id="groupShow1">
                                <option>第一段</option>
                                <option>第二段</option>
                            </select>
                            <script type="application/template" id="groupTempJs1">
                                {{~it.data:value:index}}
                                <option value="{{=value.groupId}}">{{=value.groupName}}</option>
                                {{~}}
                            </script>
                            <select id="classShow1">
                                <option>高一（1）班</option>
                                <option>高一（2）班</option>
                            </select>
                            <script type="application/template" id="classTempJs1">
                                {{~it.data:value:index}}
                                <option value="{{=value.classId}}">{{=value.className}}</option>
                                {{~}}
                            </script>
                            <button class="selectBtn">筛选</button>
                        </dd>
                        <dd id="twoWeek">
                            <div class="linshi-top">
                                <span class="hover c-kebiao firstWeek">第三周（本周）课表</span>
                                <span class="hover secondWeek">第四周课表</span>
                            </div>
                        </dd>
                        <dd>
                            <div class="adjust-mian">
                                <div class="adjust-mian-left" id="left1">
                                    <div class="adjust-mian-left-top">
                                        暂存区
                                    </div>
                                    <div class="ulBorder">
                                        <ul id="waitCourseShow">

                                        </ul>
                                    </div>
                                    <script type="application/template" id="waitCourseTempJs">
                                        {{~it.data:value:index}}
                                                <li cid="{{=value.courseId}}" tid="{{=value.teacherId}}" sid="{{=value.subjectId}}"
                                                    rid="{{=value.roomId}}" x="{{=value.x}}" y="{{=value.y}}">
                                                    <strong>{{=value.courseName}}</strong>
                                                    <strong>{{=value.teacherName}}</strong>
                                                    {{?value.roomName!=""}}
                                                    <strong>{{=value.roomName}}</strong>
                                                    {{?}}
                                                </li>
                                        {{~}}
                                    </script>
                                </div>
                                <div class="adjust-mian-left" id="left2">
                                    <div class="adjust-mian-left-top2">
                                        暂存区
                                    </div>
                                    <div class="ulBorder2">
                                        <ul id="waitCourseShow2">

                                        </ul>
                                    </div>
                                    <div class="adjust-mian-left-top2">
                                        暂存区
                                    </div>
                                    <div class="ulBorder2">
                                    <ul id="waitCourseShow3">

                                    </ul>
                                        </div>
                                </div>
                                <div class="adjust-mian-right Rclass-main-right">
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
                                        <tr>
                                            <td>
                                                <em style="margin: 0;">第{{=index+1}}节</em><br>
                                                <em style="margin: 0;">{{=value}}</em>
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
                                                {{?value2.week==0}}
                                                <td class="Rclass-main-ZB ZB_table_base" x="{{=value1}}" y="{{=index+1}}">
                                                {{??value2.week==1}}
                                                <td class="Rclass-main-ZB ZB_table_base TB_black" x="{{=value1}}" y="{{=index+1}}">
                                                {{?}}
                                                    {{=value2.subjectName}}走班({{=value2.courseList.length}})
                                                    {{?value2.fb.length>0}}
                                                    {{=value2.fb[0]}}
                                                    {{??value2.gs.length>0&&value2.te.length>0}}
                                                    <br>教研|个人
                                                    {{??value2.gs.length>0}}
                                                    <br>教研
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
                                                        <div class="Rclass-ZB-TCC">
                                                            <em>{{=value3.courseName}}</em>
                                                            <em>{{=value3.teacherName}}</em>
                                                            <em>{{=value3.classRoom}}</em>
                                                            {{?value2.mode==1&&value2.week==0}}
                                                            <i courseId="{{=value3.courseId}}" tid="{{=value3.teacherId}}"
                                                               roomId="{{=value3.classRoomId}}" class="removeCourseX">X</i>
                                                            {{?}}
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
                                                {{?value2.week==1}}
                                                <td class="Rclass-main-Gray Rclass-main-PE ZB_table_base TB_black" x="{{=value1}}" y="{{=index+1}}">
                                                {{??value2.mode==0}}
                                                <td class="Rclass-main-Gray Rclass-main-PE ZB_table_base feizoubanDel" x="{{=value1}}" y="{{=index+1}}" cid="{{=value2.courseList[0].courseId}}"
                                                        cname="{{=value2.courseList[0].courseName}}" sid="{{=value2.courseList[0].subjectId}}" tname="{{=value2.courseList[0].teacherName}}" tid="{{=value2.courseList[0].teacherId}}"
                                                        rid="{{=value2.courseList[0].classRoomId}}">
                                                {{??value2.mode==1}}
                                                <td class="Rclass-main-Gray Rclass-main-PE ZB_table_base" x="{{=value1}}" y="{{=index+1}}">
                                                {{?}}
                                                    {{=value2.courseList[0].courseName}}{{?value2.courseList[0].teacherName!=""}}({{=value2.courseList[0].teacherName}}){{?}}
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
                                                        </div>
                                                        {{~}}
                                                    </div>
                                                    {{??value2.gs.length>0}}
                                                    <br>教研
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
                                                        </div>
                                                        {{~}}
                                                    </div>
                                                    {{?}}
                                                </td>
                                            {{??value2.type==3 || value2.type==4}}
                                                {{?value2.week==0}}
                                                <td class="Rclass-main-Gray Rclass-main-PE ZB_table_base" x="{{=value1}}" y="{{=index+1}}">
                                                {{??value2.week==1}}
                                                <td class="Rclass-main-Gray Rclass-main-PE ZB_table_base TB_black" x="{{=value1}}" y="{{=index+1}}">
                                                {{?}}
                                                    <%--无课--%>
                                                    {{?value2.fb.length>0}}
                                                    {{=value2.fb[0]}}
                                                    {{??value2.gs.length>0&&value2.te.length>0}}
                                                        教研|个人
                                                    {{?value2.type==4}}<br>体育走班{{?}}
                                                    <div class="Rclass-ZB-SJ">
                                                        <div class="inner"></div>
                                                    </div>
                                                    <div class="Rclass-ZB-SJJ">

                                                    </div>
                                                    <div class="Rclass-ZB-TC">
                                                        {{~value2.courseList:value3:index3}}
                                                            <div class="Nclass-ZB-TCC">
                                                                <em>{{=value3.courseName}}</em>
                                                                <em>{{=value3.teacherName}}</em>
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
                                                        <div class="Rclass-ZB-SJ">
                                                            <div class="inner"></div>
                                                        </div>
                                                        <div class="Rclass-ZB-SJJ">

                                                        </div>
                                                        <div class="Rclass-ZB-TC">
                                                            {{~value2.courseList:value3:index3}}
                                                            <div class="Nclass-ZB-TCC">
                                                                <em>{{=value3.courseName}}</em>
                                                                <em>{{=value3.teacherName}}</em>
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
                                                        <div class="Rclass-ZB-SJ">
                                                            <div class="inner"></div>
                                                        </div>
                                                        <div class="Rclass-ZB-SJJ">

                                                        </div>
                                                        <div class="Rclass-ZB-TC">
                                                            {{~value2.courseList:value3:index3}}
                                                            <div class="Nclass-ZB-TCC">
                                                                <em>{{=value3.courseName}}</em>
                                                                <em>{{=value3.teacherName}}</em>
                                                            </div>
                                                            {{~}}
                                                            {{~value2.te:value4:index4}}
                                                            <div class="Rclass-ZB-TCC-stl">
                                                                <em>个人事务</em>
                                                                <em>{{=value4.subjectName}}({{=value4.teacherName}})</em>
                                                            </div>
                                                            {{~}}
                                                        </div>
                                                    {{??value2.type==4}}
                                                        体育走班
                                                        <div class="Rclass-ZB-SJ">
                                                            <div class="inner"></div>
                                                        </div>
                                                        <div class="Rclass-ZB-SJJ">

                                                        </div>
                                                        <div class="Rclass-ZB-TC">
                                                            {{~value2.courseList:value3:index3}}
                                                            <div class="Rclass-ZB-TCC">
                                                                <em>{{=value3.courseName}}</em>
                                                                <em>{{=value3.teacherName}}</em>
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
                                                {{?value2.week==0}}
                                                <td class="Rclass-main-ZB ZB_table_base Rclass-main-Green"
                                                    x="{{=value1}}" y="{{=index+1}}">
                                                {{??}}
                                                <td class="Rclass-main-ZB ZB_table_base TB_black"
                                                    x="{{=value1}}" y="{{=index+1}}">
                                                {{?}}
                                                    {{=value2.subjectName}}走班({{=value2.courseList.length}})
                                                    {{?value2.fb.length>0}}
                                                    {{=value2.fb[0]}}
                                                    {{??value2.gs.length>0&&value2.te.length>0}}
                                                    <br>教研|个人
                                                    {{??value2.gs.length>0}}
                                                    <br>教研
                                                    {{??value2.te.length>0}}
                                                    <br>个人
                                                    {{?}}
                                                </td>
                                            {{??value2.type==2}}
                                                {{?value2.week==0}}
                                                <td class="Rclass-main-Green" x="{{=value1}}" y="{{=index+1}}">
                                                    {{??}}
                                                <td class="TB_black" x="{{=value1}}" y="{{=index+1}}">
                                                    {{?}}
                                                    {{=value2.courseList[0].courseName}}{{?value2.courseList[0].teacherName!=""}}({{=value2.courseList[0].teacherName}}){{?}}
                                                    {{?value2.fb.length>0}}
                                                    {{=value2.fb[0]}}
                                                    {{??value2.gs.length>0&&value2.te.length>0}}
                                                    <br>教研|个人
                                                    {{??value2.gs.length>0}}
                                                    <br>教研
                                                    {{??value2.te.length>0}}
                                                    <br>个人
                                                    {{?}}
                                                </td>
                                            {{??value2.type==3|| value2.type==4}}
                                                {{?value2.week==0}}
                                                    <td class="Rclass-main-Green" x="{{=value1}}" y="{{=index+1}}">
                                                {{??}}
                                                    <td class="TB_black" x="{{=value1}}" y="{{=index+1}}">
                                                {{?}}
                                                {{?value2.fb.length>0}}
                                                    {{=value2.fb[0]}}<br>{{?value2.type==4}}体育走班{{?}}
                                                {{??value2.gs.length>0&&value2.te.length>0}}
                                                    教研|个人<br>{{?value2.type==4}}体育走班{{?}}
                                                {{??value2.gs.length>0}}
                                                    教研<br>{{?value2.type==4}}体育走班{{?}}
                                                {{??value2.te.length>0}}
                                                    个人<br>{{?value2.type==4}}体育走班{{?}}
                                                {{??value2.type==4}}
                                                    体育走班
                                                {{?}}
                                                </td>
                                            {{?}}
                                            {{??value2.isOk==0}}
                                            {{?value2.type==0}}
                                                {{?value2.week==0}}
                                                <td class="Rclass-main-ZB ZB_table_base Rclass-main-Red"
                                                    x="{{=value1}}" y="{{=index+1}}">
                                                    {{??}}
                                                <td class="Rclass-main-ZB ZB_table_base TB_black"
                                                    x="{{=value1}}" y="{{=index+1}}">
                                                    {{?}}
                                                    {{=value2.subjectName}}走班({{=value2.courseList.length}})
                                                    {{?value2.fb.length>0}}
                                                    {{=value2.fb[0]}}
                                                    {{??value2.gs.length>0&&value2.te.length>0}}
                                                    <br>教研|个人
                                                    {{??value2.gs.length>0}}
                                                    <br>教研
                                                    {{??value2.te.length>0}}
                                                    <br>个人
                                                    {{?}}
                                                </td>
                                            {{??value2.type==2}}
                                                {{?value2.week==0}}
                                                <td class="Rclass-main-Red" x="{{=value1}}" y="{{=index+1}}">
                                                    {{??}}
                                                <td class="TB_black" x="{{=value1}}" y="{{=index+1}}">
                                                {{?}}
                                                    {{=value2.courseList[0].courseName}}{{?value2.courseList[0].teacherName!=""}}({{=value2.courseList[0].teacherName}}){{?}}
                                                    {{?value2.fb.length>0}}
                                                    {{=value2.fb[0]}}
                                                    {{??value2.gs.length>0&&value2.te.length>0}}
                                                    <br>教研|个人
                                                    {{??value2.gs.length>0}}
                                                    <br>教研
                                                    {{??value2.te.length>0}}
                                                    <br>个人
                                                    {{?}}
                                                </td>
                                            {{??value2.type==3 ||value2.type==4}}
                                                {{?value2.week==0}}
                                                    <td class="Rclass-main-Red" x="{{=value1}}" y="{{=index+1}}">
                                                {{??}}
                                                    <td class="TB_black" x="{{=value1}}" y="{{=index+1}}">
                                                {{?}}
                                                    {{?value2.fb.length>0}}
                                                        {{=value2.fb[0]}}<br>{{?value2.type==4}}体育走班{{?}}
                                                    {{??value2.gs.length>0&&value2.te.length>0}}
                                                        教研|个人<br>{{?value2.type==4}}体育走班{{?}}
                                                    {{??value2.gs.length>0}}
                                                        教研<br>{{?value2.type==4}}体育走班{{?}}
                                                    {{??value2.te.length>0}}
                                                        个人<br>{{?value2.type==4}}体育走班{{?}}
                                                    {{??value2.type==4}}
                                                        体育走班
                                                    {{?}}
                                                </td>
                                            {{?}}
                                            {{??value2.isOk==2}}
                                            {{?value2.type==0}}
                                                {{?value2.week==0}}
                                                <td class="Rclass-main-ZB ZB_table_base Rclass-main-JY"
                                                    x="{{=value1}}" y="{{=index+1}}">
                                                {{??}}
                                                <td class="Rclass-main-ZB ZB_table_base TB_black"
                                                    x="{{=value1}}" y="{{=index+1}}">
                                                {{?}}
                                                    {{=value2.subjectName}}走班({{=value2.courseList.length}})
                                                    {{?value2.fb.length>0}}
                                                    {{=value2.fb[0].courseName}}
                                                    {{??value2.gs.length>0&&value2.te.length>0}}
                                                    <br>教研|个人
                                                    {{??value2.gs.length>0}}
                                                    <br>教研
                                                    {{??value2.te.length>0}}
                                                    <br>个人
                                                    {{?}}
                                                </td>
                                            {{??value2.type==2}}
                                            {{?value2.week==0}}
                                            <td class="Rclass-main-JY" x="{{=value1}}" y="{{=index+1}}">
                                                {{??}}
                                            <td class="TB_black" x="{{=value1}}" y="{{=index+1}}">
                                                {{?}}
                                                {{=value2.courseList[0].courseName}}
                                                {{?value2.courseList[0].teacherName!=null}}({{=value2.courseList[0].teacherName}}){{?}}
                                                {{?value2.fb.length>0}}
                                                {{=value2.fb[0]}}
                                                {{??value2.gs.length>0&&value2.te.length>0}}
                                                <br>教研|个人
                                                {{??value2.gs.length>0}}
                                                <br>教研
                                                {{??value2.te.length>0}}
                                                <br>个人
                                                {{?}}
                                            </td>
                                            {{??value2.type==3||value2.type==4}}
                                            {{?value2.week==0}}
                                            <td class="Rclass-main-JY" x="{{=value1}}" y="{{=index+1}}">
                                                {{??}}
                                            <td class="TB_black" x="{{=value1}}" y="{{=index+1}}">
                                                {{?}}
                                                {{?value2.fb.length>0}}
                                                    {{=value2.fb[0]}}<br>{{?value2.type==4}}体育走班{{?}}
                                                {{??value2.gs.length>0&&value2.te.length>0}}
                                                    教研|个人<br>{{?value2.type==4}}体育走班{{?}}
                                                {{??value2.gs.length>0}}
                                                    教研<br>{{?value2.type==4}}体育走班{{?}}
                                                {{??value2.te.length>0}}
                                                    个人<br>{{?value2.type==4}}体育走班{{?}}
                                                {{??value2.type==4}}
                                                    体育走班
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
                        <dd style="margin-top: 120px;">
                            <em>调课通知</em>
                        </dd>
                        <dd>
                            <em>说明：因特殊原因，对课程进行调整，具体调整如下：</em>
                        </dd>
                        <dd class="asjust-tiaozheng">
                            <table class="noticeShow">

                            </table>
                            <script type="application/template" id="noticeTempJs">
                                <tr>
                                    <th>班级</th>
                                    <th>调整课程</th>
                                    <th>任课教师</th>
                                    <th>原上课时间</th>
                                    <th>新上课时间</th>
                                </tr>
                                {{~it.data:value:index}}
                                <tr>
                                    <td>{{=value.className}}</td>
                                    <td>{{=value.courseName}}</td>
                                    <td>{{=value.teacherName}}</td>
                                    <td>{{=value.oldTime}}</td>
                                    <td>{{=value.newTime}}</td>
                                </tr>
                                {{~}}
                            </script>
                        </dd>
                    </dl>
                </div>
                <!--==========================跨周调课 start================================-->
                <div class="tab-kuazhou">
                    <dl>
                        <dt>
                            <a href="" target="_self">&lt;返回</a>
                        </dt>
                        <dd>
                            <em>高一年级</em><em>/</em><i>临时周内调课(第三周)</i>
                            <button class="adjust-BU linshi-FB">发布调课</button>
                            <button class="adjust-BU">取消</button>
                            <button class="adjust-BU">保存</button>
                        </dd>
                        <dd>
                            <em>调课名称</em>
                            <input>
                            <select>
                                <option>非走班</option>
                                <option>走班课程</option>
                            </select>
                            <select>
                                <option>第一段</option>
                                <option>第二段</option>
                            </select>
                            <select>
                                <option>高一（1）班</option>
                                <option>高一（2）班</option>
                            </select>
                        </dd>
                        <dd>
                            <div class="linshi-top">
                                <span class="hover c-kebiao">第三周（本周）课表</span>
                                <span class="hover">第四周课表</span>
                            </div>
                        </dd>
                        <dd>
                            <div class="adjust-mian">
                                <div class="adjust-mian-left">
                                    <div class="adjust-mian-left-top">
                                        暂存区
                                    </div>
                                    <ul>
                                        <li class="adjust-BK">
                                            <strong>11</strong>
                                            <strong>11</strong>
                                        </li>
                                        <li>ll</li>
                                        <li>ll</li>
                                        <li>ll</li>
                                        <li>ll</li>
                                        <li>ll</li>
                                        <li>ll</li>
                                        <li>ll</li>
                                        <li>ll</li>
                                        <li>ll</li>
                                    </ul>
                                </div>
                                <div class="adjust-mian-right">
                                    <table>
                                        <tr>
                                            <th>节次/时间</th>
                                            <th>星期一</th>
                                            <th>星期二</th>
                                            <th>星期三</th>
                                            <th>星期四</th>
                                            <th>星期五</th>
                                        </tr>
                                        <tr>
                                            <td>
                                                <strong>第一节</strong>
                                                <strong>11:00</strong>
                                            </td>
                                            <td>
                                                <label>物理走班</label>
                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <strong>第二节</strong>
                                                <strong>11:00</strong>
                                            </td>
                                            <td>
                                                <label>物理走班</label>
                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <strong>第三节</strong>
                                                <strong>11:00</strong>
                                            </td>
                                            <td>
                                                <label>物理走班</label>
                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <strong>第四节</strong>
                                                <strong>11:00</strong>
                                            </td>
                                            <td>
                                                <label>物理走班</label>
                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <strong>第五节</strong>
                                                <strong>11:00</strong>
                                            </td>
                                            <td  class="adjust-NT">
                                                <label>物理走班</label>
                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <strong>第六节</strong>
                                                <strong>11:00</strong>
                                            </td>
                                            <td>
                                                <label>物理走班</label>
                                            </td>
                                            <td class="adjust-GR">
                                                <label>个人事务</label>
                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <strong>第七节</strong>
                                                <strong>11:00</strong>
                                            </td>
                                            <td>
                                                <label>物理走班</label>
                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td class="adjust-JY">
                                                <label>教研</label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <strong>第八节</strong>
                                                <strong>11:00</strong>
                                            </td>
                                            <td>
                                                <label>物理走班</label>
                                            </td>
                                            <td>
                                                <label>物理</label>
                                                <label>$任课老师</label>
                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td  class="adjust-KC"></td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                        </dd>
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
                                <tr>
                                    <td>高一一班</td>
                                    <td>数学</td>
                                    <td>$siri</td>
                                    <td>第三周星期四第三节</td>
                                    <td>第三周星期四第三节</td>
                                </tr>
                            </table>
                        </dd>
                    </dl>
                </div>
                <!--===========================发布调课 start==============================-->
                <div class="tab-lesson">
                    <dl>
                        <dd>
                            <a class="back-main" style="display: none;cursor: pointer;">返回</a>
                        </dd>
                        <dd>
                            <em>${gradeName}</em><em>/</em>
                            <em id="classShow4">高一一班</em><em>/</em>
                            <em id="adjustType">临时调课通知</em>
                        </dd>
                        <dd>
                            <em>调课名称</em>
                            <input style="border: 1px solid #ddd;" id="noticeName">
                        </dd>
                        <dd style="margin-bottom: 10px;">
                            <em>说明</em>
                        </dd>
                        <dd style="margin-top: 10px;">
                            <textarea id="notice-des" style="border: 1px solid #ddd;"></textarea>
                        </dd>
                        <dd>
                            <table class="noticeShow">
                            </table>
                        </dd>
                        <dd  class="lesson-botton">
                            <strong>教务处</strong>
                            <strong id="time">2015-11-11</strong>
                        </dd>
                        <dd style="text-align: center">
                            <button class="adjust-BU2" id="publish_ok">确定</button>
                            <button class="adjust-BU2" id="publish_cancel">返回</button>
                        </dd>
                    </dl>
                </div>
            </div>
        </div>
    </div>
    <!--/.col-right-->

</div>
<!--=======================================弹出层背景 start========================================-->
<div class="bg"></div>
<!--=======================================弹出层背景 end========================================-->


<!--/添加--选择类型 start-->
<div class="adjust-CUR">
    <div class="adjust-CUR-top">
        <em>添加——选择类型</em>
        <i class="update-X">X</i>
    </div>
    <dl>
        <dt>
            ${gradeName}
        </dt>
        <dd>
            <em>类型</em>
            <select id="CUR-select">
                <option value="0">临时周内调课</option>
                <option value="1">临时跨周调课</option>
                <option value="2">长期调课</option>
            </select>
        </dd>
        <dd id="type0">
            <em>教学周</em>
            <select id="weekShow1_1">
                <option>第三周</option>
            </select>
            <script type="application/template" id="weekTempJs">
                {{~it.data:value:index}}
                        <option value="{{=value.value}}">{{=value.text}}</option>
                {{~}}
            </script>
        </dd>
        <dd id="type1" style="height: 80px">
            <em>教学周</em>
            <select id="weekShow2_1">
                <option>第三周</option>
            </select>
            <em style="text-align: right;">和</em>
            <select id="weekShow2_2">
                <option>第三周</option>
            </select>
        </dd>
        <dd id="type2" style="height: 80px">
            <em>教学周</em>
            <select id="weekShow3_1">
                <option>第三周</option>
            </select>
            <em style="text-align: right;">至</em>
            <select id="weekShow3_2">
                <option>第三周</option>
            </select>
        </dd>
        <dd>
            <div>
                <button class="C-queding hover">确定</button>
                <button class="C-quxiao hover">取消</button>
            </div>
        </dd>
    </dl>
</div>

<!--#foot-->
<%@ include file="../../common_new/foot.jsp" %>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js?v=1"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('adjust');
</script>
</body>
</html>