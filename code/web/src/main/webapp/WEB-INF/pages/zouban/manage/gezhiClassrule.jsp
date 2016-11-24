<%--
  Created by IntelliJ IDEA.
  User: wang_xinxin
  Date: 2015/10/16
  Time: 17:56
  To change this template use File | Settings | File Templates.
--%>
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
    <link href="/static_new/css/zouban/classrule.css?v=2015041602" rel="stylesheet"/>

    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>


    <!--#head-->
    <%@ include file="../../common_new/head.jsp" %>
    <!--/#head-->

    <!--#content-->
    <div id="content" class="clearfix">
        <input hidden="hidden" id="xuankeid">
        <input hidden="hidden" id="num">
        <input type="hidden" id="mode" value="${mode}">
        <input type="hidden" id="state" value="${state}">
        <!--.col-left-->
        <%@ include file="../../common_new/col-left.jsp" %>
        <!--/.col-left-->
        <!--广告-->
        <%--    <c:choose>
              <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
                <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
              </c:when>
              <c:otherwise>
                <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
              </c:otherwise>
            </c:choose>--%>
        <!--.col-right-->
        <div class="col-right">
            <div class="tab-col">
                <div class="tab-top ">
                    <ul>
                        <li class="cur"><a href="javascript:;">编班规则设置</a></li>
                    </ul>
                    <a class="backUrl tab-back"
                       href="javascript:;"<%--href="../paike/index.do?version=58" class="tab-back"--%>>&lt;返回教务管理首页</a>
                </div>
                <!--=============================设置分段 start=========================-->
                <div class="tab-rules">
                    <dl>
                        <!--设置分段 start-->
                        <dt class="tab-DT-I">
                            <span id="termShow" style="padding-left: 35px;">${term}</span>
                            <em></em>
                            <span id="gradeShow" gid="${gradeId}">${gradeName}</span>
                        </dt>
                        <!--设置分段 end-->
                        <!--设置每班人数 start-->
                        <dt class="tab-DT-II" style="display: none;">
                            <em>${term}</em>
                            <em>${gradeName}</em>
                        </dt>
                        <!--设置每班人数 end-->
                        <ul>
                            <li><a class="tab-rules-LI rules-LI" style="margin-right: 35px;">1.设置分段</a></li>
                            <%-- <li><a class="tab-rules-LII rules-LII">2.设置每班人数</a></li>
                             <li><a class="tab-rules-LIII rules-LII">3.设置分层</a></li>--%>
                            <li><a class="tab-rules-LIIII rules-LII" style="margin-right: 35px;">2.设置班级数</a></li>
                            <li><a class="tab-rules-V rules-LII" style="margin-right: 35px;" >3.设置老师</a></li>
                            <li><a class="tab-rules-VI rules-LII" style="margin-right: 35px;" >4.学生选班</a></li>
                            <li><a class="tab-rules-VII rules-LII" style="margin-right: 35px;" >5.设置教室</a></li>
                        </ul>
                        <!--设置分段-->
                        <dd class="sfd-dd">
                            <span class="span-bold">等级考选课人数分布</span>
                        </dd>

                        <dd class="sfd-dd">
                            <em>未分班班级：<em id="nocnt"></em></em>
                            <button class="rules-ZD">自动分段</button>
                        </dd>
                        <dd class="sfd-dd">
                            <table class="dd-table" id="allClass">
                            </table>
                            <script type="application/template" id="allClassTempJs">
                                <tr>
                                    <th>班级</th>
                                    <c:forEach items="${subjectlist}" var="t">
                                        <th>${t}</th>
                                    </c:forEach>
                                    <th>班级人数</th>
                                </tr>
                                {{~it.data:value:index}}
                                <tr>
                                    <td>{{=value.classNames}}</td>
                                    {{~value.cntlist:cnt:index2}}
                                    <td>{{=cnt}}</td>
                                    {{~}}
                                    <td>{{=value.count}}</td>
                                </tr>
                                {{~}}
                            </script>
                        </dd>
                    </dl>

                    <!-- 分段后表格st -->

                    <div class="fdh-tab">
                        <div>
                            <span class="span-bold">分段后情况</span>/<a class="fdh-a rules-CKFD">分段后明细</a>
                        </div>
                        <div id="duanlist"></div>

                    </div>
                    <script type="application/template" id="duanlistTempJs">
                        {{~it.data:value:index}}
                        <dl>
                            <dd>
                                第{{=value.group}}段<span>总人数：{{=value.count}}人</span>
                                <button class="tab-rules-TZ" fdid="{{=value.id}}">调整班级</button>
                            </dd>
                            <dd>
                                <table class="dd-table2">
                                    <tr>
                                        <th>班级</th>
                                        <c:forEach items="${subjectlist}" var="t">
                                            <th>${t}</th>
                                        </c:forEach>
                                        <th>班级人数</th>
                                    </tr>
                                    {{~value.duanlist:value2:index2}}
                                    <tr>
                                        <td>{{=value2.classNames}}</td>
                                        {{~value2.cntlist:cnt:index3}}
                                        <td>{{=cnt}}</td>
                                        {{~}}
                                        <td>{{?value2.count==0}}{{??}}{{=value2.count}}{{?}}</td>
                                    </tr>
                                    {{~}}
                                </table>
                            </dd>

                        </dl>
                        {{~}}
                    </script>
                    <!-- 分段后表格ed -->
                    <!--设置每班人数-->
                    <div class="tab-li-II">
                        <dd>
                            <em>设置每班人数</em>
                            <button class="tab-li-II-PL">批量设置</button>
                        </dd>
                        <dd>
                            <table id="coursecountlist">
                            </table>
                            <script type="application/template" id="coursecountlistTempJs">
                                <tr>
                                    <th>科目</th>
                                    <th>最少人数</th>
                                    <th>最多人数</th>
                                </tr>
                                {{~it.data:value:index}}
                                <tr tp="{{=value.type}}" scid="{{=value.subjectConfId}}">
                                    <td>{{=value.subjectName}}</td>
                                    <td><input class="stucnt1" style="text-align:center;" value="{{=value.advmin}}"/>
                                    </td>
                                    <td><input class="stucnt2" style="text-align:center;" value="{{=value.advmax}}"/>
                                    </td>
                                </tr>
                                {{~}}
                            </script>
                        </dd>
                        <%--<dd>--%>
                        <%--<button class="tab-rules-XY" style="margin-left: 24px;" id="nextto">下一步</button>--%>
                        <%--</dd>--%>
                    </div>
                    <!--设置分层-->
                    <div class="tab-li-III">
                        <%--<dd>--%>
                        <%--<select id="fcfdlist">--%>
                        <%--</select>--%>
                        <%--<script type="application/template" id="fcfdlistTempJs">--%>
                        <%--{{~it.data:value:index}}--%>
                        <%--<option value="{{=value.id}}">第{{=value.group}}段</option>--%>
                        <%--{{~}}--%>
                        <%--</script>--%>
                        <%--<button class="tab-li-II-SX">筛选</button>--%>
                        <%--</dd>--%>
                        <dd>
                            <table id="fencenglist">
                            </table>

                            <script type="application/template" id="fencenglistTempJs">
                                <tr>
                                    <th></th>
                                    <th>课程</th>
                                    <th>人数</th>
                                    <th>分层数</th>
                                    <th>操作</th>
                                </tr>
                                {{~it.data:value:index}}
                                <tr>
                                    <td>第{{=value.group}}段</td>
                                    <td>{{=value.subjectName}}</td>
                                    <td>{{=value.count}}</td>
                                    <td>{{=value.cenCount}}</td>
                                    <td scid="{{=value.subjectConfId}}" gid="{{=value.groupId}}" ctype="{{=value.type}}"
                                        duan="第{{=value.group}}段" class="setting">设置
                                    </td>
                                </tr>
                                {{~}}
                            </script>
                        </dd>
                        <%--<dd>--%>
                        <%--<button class="tab-rules-XY" id="finish">完成</button>--%>
                        <%--</dd>--%>
                    </div>
                    <div class="right-main1"><%--2.编班--%>
                        <div class="main1-2">
                            <em>分段</em>
                            <select id="fenduanlist2">
                            </select>
                            <script type="application/template" id="fenduanlistTempJs">
                                <option value="-1">全部</option>
                                {{~it.data:value:index}}
                                <option value="{{=value.id}}">第{{=value.group}}段</option>
                                {{~}}
                            </script>
                            <em>科目</em>
                            <select id="subjectlist">
                            </select>
                            <script type="application/template" id="subjectlistTempJs">
                                <option value="-1">全部</option>
                                {{~it.data:value:index}}
                                <option value="{{=value.id}}">{{=value.name}}</option>
                                {{~}}
                            </script>
                            <div class="main1-2btn" id="gezhiBianBan">自动编班</div>
                        </div>
                        <table class="edit-tab" id="courseclslist">
                        </table>
                        <script type="application/template" id="courseclslistTempJs">
                            <tr class="row1 row">
                                <td class="col2">分段</td>
                                <td class="col2">学科</td>
                                <td class="col1" style="width:250px">教学班</td>
                                <td class="col3" style="width: 170px">课时</td>
                            </tr>
                            {{~it.data:value:index}}
                            <tr class="row2 row">
                                <td class="col2">第{{=value.group}}段</td>
                                <td class="col2">{{=value.subjectName}}</td>
                                <td class="col1" style="width:250px">{{=value.courseName}}</td>
                                <td class="col3" style="width: 170px">{{=value.lessonCount}}</td>
                            </tr>
                            {{~}}
                        </script>
                    </div>
                    <div class="right-main-tea" style="display: none">
                        <div class="main1-2">

                            <em>分段</em>
                            <select id="fenduanlist2-tea" style="width: 110px;">
                            </select>
                            <em class="subject-head">科目</em>
                            <select id="subjectlist-tea" style="width: 110px;">
                            </select>
                            <em>类型</em>
                            <select id="subject-type" style="width: 110px;">
                                <option value="1">走班</option>
                                <option value="2">非走班</option>
                            </select>
                            <div class="main1-2btn" id="autoSetTea" style="width: 127px;">一键设置走班老师</div>


                        </div>
                        <table class="edit-tab" id="courseclslist-tea">
                        </table>
                        <script type="application/template" id="courseclslistTempJs-tea">
                            <tr class="row1 row">
                                <td class="col2">分段</td>
                                <td class="col1">教学班</td>
                                <td class="col4">每周课时</td>
                                <td class="col5" style="width: 140px">任课老师</td>
                                <td class="col8">操作</td>
                            </tr>
                            {{~it.data:value:index}}
                            <tr class="row2 row">
                                <td class="col2">第{{=value.group}}段</td>
                                <td class="col1">{{=value.courseName}}</td>
                                <td class="col4">{{=value.lessonCount}}</td>
                                <td class="col5" style="width: 140px;">{{=value.teacherName}}</td>
                                <%--<td class="col7"><em class="tab-check" couid="{{=value.zbCourseId}}">查看</em></td>--%>
                                <%--<td class="col8"><em class="edit-set" sid="{{=value.subjectId}}" snm="{{=value.subjectName}}" cn="{{=value.courseName}}" gp="{{=value.group}}" couid="{{=value.zbCourseId}}" tid="{{=value.teacherId}}" crid="{{=value.classRoomId}}">设置</em>&nbsp;|&nbsp;<em class="peo-set" couid="{{=value.zbCourseId}}" gp="{{=value.group}}" cn="{{=value.courseName}}">调整人员</em></td>--%>
                                <td class="col8"><em class="edit-set-tea" sid="{{=value.subjectId}}" snm="{{=value.subjectName}}"
                                                     cn="{{=value.courseName}}" gp="{{=value.group}}" cnt="1" cid="1"
                                                     couid="{{=value.zbCourseId}}" tid="{{=value.teacherId}}"
                                                     crid="{{=value.classRoomId}}">设置</em></td>
                            </tr>
                            {{~}}
                        </script>
                        <script type="application/template" id="feizoubanlistTempJs">
                            <tr class="row1">
                                <td class="col1" style="width: 135px;">行政班</td>
                                <td class="col1" style="width: 130px;">学科</td>
                                <td class="col2" style="width: 110px;">人数</td>
                                <td class="col3">每周课时</td>
                                <td class="col4" style="width: 140px;">任课老师</td>
                                <td class="col5">操作</td>
                            </tr>
                            {{~it.data:value:index}}
                            <tr>
                                <td style="width: 135px;">{{=value.className}}</td>
                                <td>{{=value.subjectName}}</td>
                                <td style="width: 110px;">{{=value.count}}</td>
                                <td>{{=value.lessonCount}}</td>
                                <td>{{=value.teacherName}}</td>
                                <td><i class="edit-set-tea" sid="{{=value.subjectId}}" cid="{{=value.classId}}" crid="1"
                                       snm="{{=value.subjectName}}" tid="{{=value.teacherId}}" cn="{{=value.className}}" cnt="{{=value.lessonCount}}"
                                       couid="{{=value.zbCourseId}}">设置</i></td>
                            </tr>
                            {{~}}
                        </script>
                    </div>
                    <div class="right-main-stu" style="display: none">
                        <div class="main1-2">
                            <em>分段</em>
                            <select id="fenduanlist2-stu">
                            </select>

                            <em>科目</em>
                            <select id="subjectlist-stu">
                            </select>

                            <div class="main1-2btn" id="main1-2btn-stu">一键编排学生</div>
                        </div>
                        <table class="edit-tab" id="courseclslist-stu">
                        </table>
                        <script type="application/template" id="courseclslistTempJs-stu">
                            <tr class="row1 row">
                                <td class="col2" style="width: 95px;">分段</td>
                                <td class="col2" style="width: 95px;">学科</td>
                                <td class="col1" style="width:180px">教学班</td>
                                <td class="col3" style="width: 100px">课时</td>
                                <td>人数</td>
                                <td class="col7">学生名单</td>
                                <%--<td class="col8">操作</td>--%>
                            </tr>
                            {{~it.data:value:index}}
                            <tr class="row2 row">
                                <td class="col2" style="width: 95px;">第{{=value.group}}段</td>
                                <td class="col2" style="width: 95px;">{{=value.subjectName}}</td>
                                <td class="col1" style="width:180px">{{=value.courseName}}</td>
                                <td class="col3" style="width: 100px">{{=value.lessonCount}}</td>
                                <td class="col3">{{=value.count}}</td>
                                <td class="col7"><em class="tab-check" couid="{{=value.zbCourseId}}">查看</em></td>
                                <%--<td class="col8"><em class="peo-set" couid="{{=value.zbCourseId}}" gp="{{=value.group}}"
                                                     cn="{{=value.courseName}}">调整人员</em></td>--%>
                            </tr>
                            {{~}}
                        </script>
                    </div>
                    <div class="right-main-room" style="display: none">
                        <div class="main1-2">

                            <em>分段</em>
                            <select id="fenduanlist2-room">
                            </select>
                            <em>科目</em>
                            <select id="subjectlist-room">
                            </select>
                            <div class="main1-2btn" id="autoSetClassroom">一键设置教室</div>


                        </div>
                        <table class="edit-tab" id="courseclslist-room">
                        </table>
                        <script type="application/template" id="courseclslistTempJs-room">
                            <tr class="row1 row">
                                <td class="col2" style="width: 120px;">分段</td>
                                <td class="col1" style="width: 120px;">教学班</td>
                                <td class="col4">每周课时</td>
                                <td class="col5" style="width: 140px">任课老师</td>
                                <td class="col5" style="width: 140px">教室</td>
                                <td class="col8" style="width: 100px;">操作</td>
                            </tr>
                            {{~it.data:value:index}}
                            <tr class="row2 row">
                                <td class="col2" style="width: 120px;">第{{=value.group}}段</td>
                                <td class="col1" style="width: 120px;">{{=value.courseName}}</td>
                                <td class="col4">{{=value.lessonCount}}</td>
                                <td class="col5" style="width: 140px;">{{=value.teacherName}}</td>
                                <td class="col5" style="width: 140px;">{{=value.classRoom}}</td>
                                <%--<td class="col7"><em class="tab-check" couid="{{=value.zbCourseId}}">查看</em></td>--%>
                                <%--<td class="col8"><em class="edit-set" sid="{{=value.subjectId}}" snm="{{=value.subjectName}}" cn="{{=value.courseName}}" gp="{{=value.group}}" couid="{{=value.zbCourseId}}" tid="{{=value.teacherId}}" crid="{{=value.classRoomId}}">设置</em>&nbsp;|&nbsp;<em class="peo-set" couid="{{=value.zbCourseId}}" gp="{{=value.group}}" cn="{{=value.courseName}}">调整人员</em></td>--%>
                                <td class="col8" style="width: 100px;"><em class="edit-set-room" sid="{{=value.subjectId}}" snm="{{=value.subjectName}}"
                                                     cn="{{=value.courseName}}" gp="{{=value.group}}" tnm="{{=value.teacherName}}" gid="{{=value.groupId}}"
                                                     couid="{{=value.zbCourseId}}" tid="{{=value.teacherId}}"
                                                     crid="{{=value.classRoomId}}">设置</em></td>
                            </tr>
                            {{~}}
                        </script>
                    </div>
                    </dl>
                </div>
                <!--=============================设置分段 end=========================-->
                <!--==============================3.0查看分段后选课情况 start======================-->
                <div class="tab-variable">
                    <dl>
                        <dd>
                            <span id="backBtn" style="cursor: pointer;">&lt;返回</span>
                        </dd>
                        <dd>
                            <em id="term3"></em>
                            <em id="grade3"></em>
                        </dd>
                        <dd>
                            <select id="fdlist">
                            </select>
                            <script type="application/template" id="fdlistTempJs">
                                {{~it.data:value:index}}
                                <option value="{{=value.id}}">第{{=value.group}}段</option>
                                {{~}}
                            </script>
                        </dd>
                        <dd>
                            <table id="couselist">
                            </table>
                            <script type="application/template" id="couselistTempJs">
                                <tr>
                                    <th>课程</th>
                                    <th><span id="duan"></span>选课人数</th>
                                    <th>操作</th>
                                </tr>
                                {{~it.data:value:index}}
                                <tr>
                                    <td>{{=value.subjectName}}</td>
                                    <td>{{=value.count}}</td>
                                    <td class="tab-variable-MX" scid="{{=value.subjectConfId}}" sctid="{{=value.type}}">
                                        查看明细
                                    </td>
                                </tr>
                                {{~}}
                            </script>
                        </dd>
                    </dl>
                </div>
                <!--==============================3.0查看分段后选课情况 end======================-->
                <!--====================3.1查看分段后选课情况 start===================-->
                <div class="tab-regulation">
                    <dl>
                        <dd>
                            <a href="#" id="backBtn2">&lt;返回</a>
                        </dd>
                        <dd>
                            <em id="term4"></em>/<i id="grade4"></i>
                        </dd>
                        <dd>
                            <em>课程</em>
                            <select id="courselist2">
                            </select>
                            <script type="application/template" id="courselistTempJs2">
                                {{~it.data:value:index}}
                                <option value="{{=value.subjectConfId}}">{{=value.subjectName}}</option>
                                {{~}}
                            </script>
                            <em>分段</em>
                            <select id="fdlist2">
                            </select>
                            <script type="application/template" id="fdlistTempJs2">
                                {{~it.data:value:index}}
                                <option value="{{=value.id}}">第{{=value.group}}段</option>
                                {{~}}
                            </script>
                            <button class="regulation-SX">筛选</button>
                        </dd>
                        <dd>
                            <table id="studentlist">

                            </table>
                            <script type="application/template" id="studentlistTempJs">
                                <tr>
                                    <th>学生姓名</th>
                                    <th>性别</th>
                                    <th>所属行政班</th>
                                    <th>调整选课</th>
                                </tr>
                                {{~it.data:value:index}}
                                <tr>
                                    <td>{{=value.username}}</td>
                                    <td>{{?value.sex==0}}女{{??}}男{{?}}</td>
                                    <td>{{=value.className}}</td>
                                    <td class="tab-regulation-XK" uid="{{=value.userid}}">选课</td>
                                </tr>
                                {{~}}
                            </script>
                        </dd>
                    </dl>
                </div>
                <!--====================3.1编班规则 查看分段后选课情况 send===================-->
                <!--=====================7.编班规则 设置分层教学 start============================-->
                <div class="tab-slice">
                    <dl>
                        <dd>
                            <a class="back-slice" style="cursor: pointer">&lt;返回</a>
                        </dd>
                        <dd>
                            <em id="groups"></em>/<i id="coursename"></i>/<i>分层设置</i>
                            <button id="saveFenceng">保存</button>
                        </dd>
                        <dd>
                            <%--<a href="/exam1/list.do?version=5d" target="_blank">导入成绩</a>--%>
                            <span id="exportCJ">导入成绩</span>
                        </dd>
                        <dd>
                            <div class="tab-slice-main">
                                <div class="tab-slice-left">
                                    <table id="fencengResultShow">
                                        <tr>
                                            <th class="tab-slice-I">分层</th>
                                            <th>成绩</th>
                                            <th>人数</th>
                                        </tr>
                                        <%--<tr>
                                          <td class="tab-slice-I">A</td>
                                          <td>90-100</td>
                                          <td>100</td>
                                        </tr>--%>
                                    </table>
                                    <script type="application/template" id="fencengResultTempJs">
                                        <tr>
                                            <th class="tab-slice-I">分层</th>
                                            <th>成绩</th>
                                            <th>人数</th>
                                        </tr>
                                        {{~it.data.fencengs:value:index}}
                                        <tr>
                                            <td class="tab-slice-I">{{=it.data.fencengs[index]}}</td>
                                            <td>{{=it.data.scores[index]}}</td>
                                            <td>{{=it.data.users[index].split(",").length}}</td>
                                        </tr>
                                        {{~}}
                                    </script>
                                </div>
                                <div class="tab-slice-right">
                                    <table id="sliceTable">

                                    </table>
                                    <script type="application/template" id="tab-sliceTemJs">
                                        <tr>
                                            <th>排名</th>
                                            <th>分数</th>
                                            <th>姓名</th>
                                            <th>性别</th>
                                            <th class="tab-slice-II">行政班</th>
                                            <th>设置分层点</th>
                                        </tr>
                                        {{~it.data.studentScore:value:index}}
                                        <tr>
                                            <td>{{=index+1}}</td>
                                            <td>{{=value.score}}</td>
                                            <td>{{=value.studentName}}</td>
                                            <td>{{?value.sex==0}}女{{??}}男{{?}}</td>
                                            <td class="tab-slice-II">{{=value.className}}</td>
                                            <td class="set-fenceng" index="{{=index}}" sid="{{=value.studentId}}"></td>
                                        </tr>
                                        {{~}}
                                    </script>
                                </div>
                            </div>
                        </dd>
                    </dl>
                </div>
                <!--=====================7.编班规则 设置分层教学 end============================-->
            </div>
            <div class="right-main2">
                <span class="back-btn">< 返回</span>
                <span style="display:inline-block;width:120px;" id="term6"></span> / <i style="margin-left:20px;"
                                                                                        id="grade6"></i>

                <div class="main2-info">
                    <i id="coursename2"></i>
                    <em>任课老师：</em><i id="teachername"></i>
                    <em>教室：</em><i id="classroom"></i>
                    <em>人数：</em><i id="usercnt"></i>
                </div>
                <table class="table-stu" id="userlist">
                </table>
                <script type="application/template" id="userlistTempJs">
                    <tr class="row1">
                        <td>学生姓名</td>
                        <td>性别</td>
                        <td>所属行政班</td>
                    </tr>
                    {{~it.data:value:index}}
                    <tr>
                        <td>{{=value.studentName}}</td>
                        <td>{{?value.sex==0}}女{{??}}男{{?}}</td>
                        <td>{{=value.className}}</td>
                    </tr>
                    {{~}}
                </script>
            </div>
            <div class="right-main3">
                <div style="width:790px;height:54px;margin-left:35px;">
                    <span class="back-btn">< 返回</span>
                    <span style="display:inline-block;width:120px;" id="term7"></span>
                    /
                    <i style="margin-left:20px;margin-right:20px;" id="grade7"></i>
                    /
                    <i style="margin-left:20px;" id="duan7"></i>
                </div>
                <div class="main3-set1">
                    <span style="display:inline-block;width:160px;" id="cousename7"></span>

                    <div class="select-all">全选</div>
                    <span class="aim-cla">目标教学班</span>
                    <select class="aim-class" id="classlist5"></select>
                    <script type="application/template" id="classlistTempJs5">
                        {{~it.data:value:index}}
                        <option value="{{=value.zbCourseId}}">{{=value.className}}</option>
                        {{~}}
                    </script>
                </div>
                <div class="main3-set2">
                    <em>人数：</em><i class="main3-num" id="cnt5"></i>
                    <em style="margin-left:10px;">任课老师：</em><i class="main3-teach" id="teachername5"></i>
                    <em>上课教室：</em><i class="main3-classroom" id="classromm5"></i>
                    <em style="margin-left:22px;">人数：</em><i class="main3-num" id="cnt6"></i>
                    <em style="margin-left:10px;">任课老师：</em><i class="main3-teach" id="teachername6"></i>
                    <em>上课教室：</em><i class="main3-classroom" id="classromm6"></i>
                </div>
                <div class="main3-2">
                    <div class="main3-sel1">
                        <ul style="padding-top:10px;" id="userlist5">
                        </ul>
                        <script type="application/template" id="userlistTempJs5">
                            {{~it.data:value:index}}
                            <li>
                                <input type="checkbox" uid="{{=value.studentId}}" name="checkbox"
                                       value="{{=value.studentId}}">
                                <span><i class="stu-name">{{=value.studentName}}</i> <i class="stu-sex"
                                                                                        style="margin-left:20px; ">{{?value.sex==0}}女{{??}}男{{?}}</i><i
                                        class="stu-class">{{=value.className}}</i></span>
                            </li>
                            {{~}}
                        </script>
                    </div>
                    <div class="ex-change">
                        <div class="ex-arrow1">>></div>
                        <div class="ex-arrow2"><<</div>
                    </div>
                    <div class="main3-sel2">
                        <ul style="padding:10px 0 0 15px;" id="userlist6">
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <!--背景 start-->
        <div class="bg"></div>
        <!--背景 end-->
        <!--设置分段调整班级 start-->
        <div class="adjust-CUR">
            <div class="adjust-CUR-top">
                <em>调整班级</em>
                <i class="update-X">X</i>
            </div>
            <div class="adjust-main">
                <div class="adjust-main-left">
                    <dl>
                        <dt>
                            <em id="grade5"></em>
                            <em id="duan5"></em>
                            <button id="choose">全选</button>
                        </dt>
                        <dd>
                            <ul id="classlist">
                            </ul>
                            <script type="application/template" id="classlistTemJs">
                                {{~it.data:value:index}}
                                <li>
                                    <input type="checkbox" name="checkbox" cid="{{=value.classId}}"
                                           gid="{{=value.groupId}}" name="checkbox" gop="{{=value.group}}"><em>{{=value.className}}</em><em>第{{=value.group}}段</em>
                                </li>
                                {{~}}
                            </script>
                        </dd>
                        <dd class="adjust-ZY">
                            <button id="add">&gt;&gt;</button>
                            <button id="remove">&lt;&lt;</button>
                        </dd>
                    </dl>
                </div>
                <div class="adjust-main-right">
                    <dl>
                        <dt>
                            <select id="duanSelect" class="duanSelect">
                            </select>
                            <script type="application/template" id="duanSelectTemJs">
                                {{~it.data:value:index}}
                                <option value="{{=value.id}}">第{{=value.group}}段</option>
                                {{~}}
                            </script>
                            <button id="choose2">全选</button>
                        </dt>
                        <dd>
                            <ul id="classlist2">
                            </ul>
                            <script type="application/template" id="classlistTemJs2">
                                {{~it.data:value:index}}
                                <li>
                                    <input type="checkbox" name="checkbox" cid="{{=value.classId}}"
                                           gid="{{=value.groupId}}" name="checkbox" gop="{{=value.group}}"><em>{{=value.className}}</em><em>第{{=value.group}}段</em>
                                </li>
                                {{~}}
                            </script>
                        </dd>
                    </dl>
                </div>
            </div>
            <div class="adjust-bottom">
                <button class="adjust-bottom-BU">确认</button>
                <button class="adjust-bottom-QX">取消</button>
            </div>
        </div>
        <!--设置分段调整班级 end-->
        <!--设置分段 自动分段 start-->
        <div class="section-CUR">
            <div class="section-CUR-top">
                <em>自动分段</em>
                <i class="update-X">X</i>
            </div>
            <dl>
                <dd>
                    <em id="grade2"></em>
                </dd>
                <dd>
                    <em>分段数目</em>
                    <select class="se1" id="count">
                        <option>1</option>
                        <option>2</option>
                        <option>3</option>
                        <option>4</option>
                        <option>5</option>
                    </select>
                </dd>
                <dd>
                    <em>每段学科教学班数量</em>
                    <select class="se2">
                        <option>1</option>
                        <option>2</option>
                        <option>3</option>
                        <option>4</option>
                        <option>5</option>
                        <option>6</option>
                    </select>
                </dd>

                <dd><em class="em-rule">分段规则</em>

                    <div class="div-rule">
                        <dl class="dl-rule">
                            <%--<dd><input type="radio" name="fd-rule">手动分段</dd>--%>
                            <dd><input type="radio" name="fd-rule" checked="true" id="duanchoose1">分班时尽量使班级人数最优</dd>
                            <%--<dd class="dd-padding">每班人数范围</dd>
                            <dd class="dd-text dd-padding"><input type="text" placeholder="最少人数"
                                                                  onfocus="this.placeholder=''"
                                                                  onblur="this.placeholder='最少人数'" id="mincnt">-<input
                                    type="text" placeholder="最多人数" onfocus="this.placeholder=''"
                                    onblur="this.placeholder='最多人数'" style="margin-left: 10px;" id="maxcnt"></dd>--%>
                            <dd id="error" style="display: none;">
                                <span class="edit-error">请输入人数且不能为零!</span>
                            </dd>
                            <%--<dd>
                                <select id="examlist2" disabled="disabled" style="width:215px;">
                                </select>
                                <script type="application/template" id="examlistTempJs2">
                                    {{~it.data:value:index}}
                                    <option value="{{=value.id}}">{{=value.name}}</option>
                                    {{~}}
                                </script>
                            </dd>--%>
                            <dd>
                                <%--<input type="checkbox" id="examtype">--%>
                                <input type="radio" name="fd-rule" id="duanchoose2">
                                <em>根据班级成绩分段</em>
                            </dd>
                            <dd>
                                <select id="examlist" disabled="disabled">
                                </select>
                                <script type="application/template" id="examlistTempJs">
                                    {{~it.data:value:index}}
                                    <option value="{{=value.id}}">{{=value.name}}</option>
                                    {{~}}
                                </script>
                            </dd>

                            <dd>
                                <a class="section-CK" href="/exam1/list.do?version=5d" id="findCJ" target="_blank">查看成绩情况</a>
                            </dd>
                        </dl>
                    </div>
                </dd>
                <dd>
                    <button class="section-CUR-QR">确认</button>
                    <button class="section-CUR-QX">取消</button>
                </dd>
            </dl>
        </div>
        <!--设置分段 自动分段 end-->
        <!--设置分段 班级排名 start-->
        <div class="ranking-CUR">
            <div class="ranking-CUR-top">
                <em>自动分段</em>
                <i class="update-X">X</i>
            </div>
            <dl>
                <dd>说明：提高班将被分到不同段里</dd>
                <dd>
                    <table>
                        <tr>
                            <th>排名</th>
                            <th>行政班</th>
                            <th>6门总分</th>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>高一（3）班</td>
                            <td>666</td>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>高一（3）班</td>
                            <td>666</td>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>高一（3）班</td>
                            <td>666</td>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>高一（3）班</td>
                            <td>666</td>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>高一（3）班</td>
                            <td>666</td>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>高一（3）班</td>
                            <td>666</td>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>高一（3）班</td>
                            <td>666</td>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>高一（3）班</td>
                            <td>666</td>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>高一（3）班</td>
                            <td>666</td>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>高一（3）班</td>
                            <td>666</td>
                        </tr>
                    </table>
                </dd>
                <dd>
                    <button class="ranking-CUR-QR">确认</button>
                    <button class="ranking-CUR-QX">取消</button>
                </dd>
            </dl>
        </div>
        <!--设置分段 班级排名 end-->
        <!--选课弹出框 start-->
        <div class="Sschedule-CUR">
            <input id="userId" type="hidden">

            <div class="Sschedule-CUR-top">
                <em>选课</em>
                <i class="update-X">X</i>
            </div>
            <dl id="tableShow">
                <dt>
                    <span>高一（2）班&nbsp;&nbsp;&nbsp;&nbsp;</span>/<span>&nbsp;&nbsp;&nbsp;&nbsp;学生名</span>
                </dt>
                <dd>
                    <em>已选课程</em>
                </dd>
                <dd>
                    <em>等级考课程:</em>

                    <div>
                        <button>物理</button>
                        <button>物理</button>
                        <button>物理</button>
                    </div>
                </dd>
                <dd>
                    <em>合格考课程:</em>
                    <button>生物</button>
                </dd>
                <dd>
                    <em>修改课程</em>
                </dd>
                <dd>
                    <em>需要选择<i>3</i>门等级考科目</em>
                    <em>需要选择<i>4</i>门合格考科目</em>
                </dd>
                <dd>
                    <table class="classrul-tab">

                    </table>
                </dd>
                <dd>
                    <div class="Sschedule-CUR-bottom">
                        <button>确定</button>
                        <button class="Sschedule-CUR-BU">取消</button>
                    </div>
                </dd>
            </dl>
            <script type="application/template" id="tempJs">
                <dt>
                    <span>{{=it.className}}&nbsp;&nbsp;&nbsp;&nbsp;</span>/<span>&nbsp;&nbsp;&nbsp;&nbsp;{{=it.stuName}}</span>
                </dt>
                <dd>
                    <em>已选课程</em>
                </dd>
                <dd>
                    <em>等级考课程:</em>

                    <div>
                        {{~it.data.adv:value:index}}
                        <button>{{=value.value}}</button>
                        {{~}}
                    </div>
                </dd>
                <dd>
                    <em>等级考课程:</em>

                    <div>
                        {{~it.data.sim:value:index}}
                        <button>{{=value.value}}</button>
                        {{~}}
                    </div>
                </dd>
                <dd>
                    <em>修改课程</em>
                </dd>
                <%-- <dd>
                   <em>需要选择<i>{{=it.data.conf.advanceCount}}</i>门等级考科目</em>
                   <em>需要选择<i>{{=it.data.conf.simpleCount}}</i>门合格考科目</em>
                 </dd>--%>
                <dd>
                    <table class="classrul-tab">
                        <tr>
                            <th>科目名称</th>
                            <th>等级考</th>
                            <th>合格考</th>
                            <th>说明</th>
                        </tr>
                        {{~it.data.conf.subConfList:value:index}}
                        <tr sid="{{=value.subjectId}}">
                            <td>{{=value.subjectName}}</td>
                            <td>
                                {{?$.inArray(value.subjectName,it.advChoose)>-1}}
                                <span class="Sschedule-CUR-hov advcourse">已选</span>
                                {{??}}
                                <span class="advcourse">我选</span>
                                {{?}}
                            </td>
                            <td>
                                {{?$.inArray(value.subjectName,it.simChoose)>-1}}
                                <span class="Sschedule-CUR-hov simcourse">已选</span>
                                {{??}}
                                {{?value.simpleTime==0}}
                                <span style="width: 38px;margin: auto;">-----</span>
                                {{??}}
                                <span class="simcourse">我选</span>
                                {{?}}
                                {{?}}
                            </td>
                            <td>{{=value.explain}}</td>
                        </tr>
                        {{~}}

                    </table>
                </dd>
                <dd>
                    <div class="Sschedule-CUR-bottom">
                        <button class="submitXuanke">确定</button>
                        <button class="Sschedule-CUR-BU">取消</button>
                    </div>
                </dd>
            </script>
        </div>
        <!--选课弹出框 end-->
        <!--批量设置 弹出框start-->
        <div class="batch-CUR">
            <div class="batch-CUR-top">
                <em>批量设置</em>
                <i class="update-X">X</i>
            </div>
            <dl>
                <dd>
                    <em>最少人数</em><input id="mincount">
                </dd>
                <dd>
                    <em>最多人数</em><input id="maxcount">
                </dd>
                <dd>
                    <button class="batch-CUR-QR">确认</button>
                    <button class="batch-CUR-QX">取消</button>
                </dd>
            </dl>
        </div>
        <!--批量设置 弹出框end-->
        <!--/.col-right-->
    </div>
    <!--/#content-->
    <!--#foot-->
    <%@ include file="../../common_new/foot.jsp" %>
    <!--#foot-->
    <!--提示 start-->
    <div class="tishi-CUR">
        <div class="tishi-CUR-top">
            <em>提示</em>
            <i class="update-X">X</i>
        </div>
        <div class="tishi-CUR-info">
            自动编班后将重新编班，是否确定自动编班！
        </div>
        <div class="tishi-CUR-buttom">
            <button class="curri-TJ">确定</button>
            <button class="curri-QX">取消</button>
        </div>
    </div>
    <div class="bianbanTip">
        <p>正在编排，请耐心等待。。。</p>
    </div>
    <%--//设置老师--%>
    <div class="edit-set-div-tea">
        <div class="edit-set-bg"></div>
        <input type="hidden" id="courseId-set">
        <input type="hidden" id="weekCnt-set">
        <input type="hidden" id="classId-set">
        <input type="hidden" id="roomId-set">
        <div class="edit-set-wind">
            <div class="setwind-top">
                <i style="font-size:16px;">设置</i>
                <i class="setwind-cl">×</i>
            </div>
            <div class="edit-info">
                <span style="width:180px;margin-right:25px;" id="term2"></span>
                <span id="grade2-tea"></span><br/>
                <i style="width:70px;" id="snm"></i><i style="width:60px;" id="ktp">等级考</i><i style="width:70px;" id="duan-tea">第一段</i>
            </div>
            <div class="edit-form">
                <span>班级名称：</span><i id="coursenm"></i><br/>
                <span>任课老师</span>
                <select id="teacherlist">
                </select>
                <script type="application/template" id="teacherlistTempJs">
                    {{~it.data:value:index}}
                    <option value="{{=value.teacherId}}">{{=value.teacherName}}</option>
                    {{~}}
                </script>
            </div>
            <div class="cc-btn">
                <div class="cofi-btn" id="tea-cofi-btn">确定</div>
                <div class="canc-btn">取消</div>
            </div>
        </div>
    </div>
    <div class="edit-set-div-room">
        <div class="edit-set-bg"></div>
        <input type="hidden" id="courseId-set-room">
        <input type="hidden" id="weekCnt-set-room">
        <input type="hidden" id="classId-set-room">
        <input type="hidden" id="teacherId-set-room">
        <input type="hidden" id="teacherName-set-room">
        <div class="edit-set-wind">
            <div class="setwind-top">
                <i style="font-size:16px;">设置</i>
                <i class="setwind-cl">×</i>
            </div>
            <div class="edit-info">
                <span style="width:180px;margin-right:25px;" id="term2-room"></span>
                <span id="grade2-room"></span><br/>
                <i style="width:50px;" id="snm-room"></i><i style="width:60px;" id="ktp-room">等级考</i><i style="width:70px;" id="duan-room">第一段</i>
            </div>
            <div class="edit-form">
                <span>班级名称：</span><i id="coursenm-room"></i><br/>
                <span>上课教室</span>
                <select id="classroomlist">
                </select>
                <script type="application/template" id="classroomlistTempJs">
                    {{~it.data:value:index}}
                    <option value="{{=value.id}}">{{=value.roomName}}</option>
                    {{~}}
                </script>
            </div>
            <div class="cc-btn">
                <div class="cofi-btn" id="room-cofi-btn">确定</div>
                <div class="canc-btn">取消</div>
            </div>
        </div>
    </div>
    <div class="gezhiConf" style="  ">
        <div class="tishi-CUR-top">
            <em>编班设置</em>
            <i class="update-X">X</i>
        </div>
        <div>
            <p class="tip">提示：以下为系统推荐，如需调整请根据实际人数调整班级数。</p>
        </div>
        <div id="temp">

        </div>
        <script id="tempConfJs" type="application/template">
            <table>
                <tr class="head">
                    <td class="gz_t1">科目</td>
                    <td class="gz_t2">分组</td>
                    <td>等级考人数</td>
                    <td class="gz_t3">等级考班级数</td>
                    <td class="gezhiOnly">合格考人数</td>
                    <td class="gezhiOnly gz_t4">合格考班级数</td>
                </tr>
                {{~it.data:value:index}}
                <tr>
                    <td class="gz_t1">{{=value.subjectName}}</td>
                    <td class="gz_t2">{{=value.groupName}}</td>
                    <td >{{=value.advStuCount}}</td>
                    <td class="gz_t3"><input class="adv" sid="{{=value.subjectId}}" gid="{{=value.groupId}}" style="width:50px;"
                               value="{{=value.advCount}}"></td>
                    <td class="gezhiOnly">{{=value.simStuCount}}</td>
                    <td class="gezhiOnly gz_t4"><input class="sim" style="width:50px;" sid="{{=value.subjectId}}" gid="{{=value.groupId}}"
                               value="{{=value.simCount}}"></td>
                </tr>
                {{~}}
            </table>
        </script>
        <script id="tempConfCZJs" type="application/template">
            <table>
                <tr class="head">
                    <td class="gz_t1">科目</td>
                    <td class="gz_t2">分组</td>
                    <td>等级考人数</td>
                    <td class="gz_t3">等级考班级数</td>
                    <td style="display: none">合格考人数</td>
                    <td style="display: none">合格考班级数</td>
                </tr>
                {{~it.data:value:index}}
                <tr>
                    <td class="gz_t1">{{=value.subjectName}}</td>
                    <td class="gz_t2">{{=value.groupName}}</td>
                    <td >{{=value.advStuCount}}</td>
                    <td class="gz_t3"><input class="adv" sid="{{=value.subjectId}}" gid="{{=value.groupId}}" style="width:50px;"
                                             value="{{=value.advCount}}"></td>
                    <td style="display: none">{{=value.simStuCount}}</td>
                    <td style="display: none"><input class="sim" style="width:50px;" sid="{{=value.subjectId}}" gid="{{=value.groupId}}"
                                                       value="{{=value.simCount}}"></td>
                </tr>
                {{~}}
            </table>
        </script>
        <div class="tishi-CUR-buttom">
            <button class="curri-TJ1">确定</button>
            <button class="curri-QX1">取消</button>
        </div>
    </div>


    <!--提示 end-->
    <!-- Javascript Files -->
    <!-- initialize seajs Library -->
    <script src="/static_new/js/sea.js?v=1"></script>
    <!-- Custom js -->
    <script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
    <script>
        seajs.use('gezhiClassrule');
    </script>
</head>
<body>

</body>
</html>
