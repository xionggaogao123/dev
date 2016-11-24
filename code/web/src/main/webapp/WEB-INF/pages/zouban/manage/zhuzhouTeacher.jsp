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
                        <li class="cur"><a href="javascript:;">非走班老师设置</a></li>
                    </ul>
                    <a class="backUrl tab-back"
                       href="javascript:;"<%--href="../paike/index.do?version=58" class="tab-back"--%>>&lt;返回教务管理首页</a>
                </div>
                <!--=============================设置分段 start=========================-->
                <div class="tab-rules">


                    <div class="right-main-tea">
                        <div class="main1-2">

                            <span id="termShow" style="padding-left: 35px;">${term}</span>
                            <span id="gradeShow" gid="${gradeId}">${gradeName}</span>
                            <em class="subject-head">科目</em>
                            <select id="subjectlist-tea" style="width: 110px;">
                            </select>

                            <div class="main1-2btn" id="autoSetTea" style="width: 127px;">一键设置走班老师</div>


                        </div>
                        <table class="edit-tab" id="courseclslist-tea">
                        </table>
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

                    </dl>
                </div>
                <!--=============================设置分段 end=========================-->

            </div>
            <script type="application/template" id="subjectlistTempJs">
                <option value="-1">全部</option>
                {{~it.data:value:index}}
                <option value="{{=value.id}}">{{=value.name}}</option>
                {{~}}
            </script>
            <script type="application/template" id="fenduanlistTempJs">
                <option value="-1">全部</option>
                {{~it.data:value:index}}
                <option value="{{=value.id}}">第{{=value.group}}段</option>
                {{~}}
            </script>
        </div>
        <!--背景 start-->
        <div class="bg"></div>
        <!--背景 end-->


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

    <!-- Javascript Files -->
    <!-- initialize seajs Library -->
    <script src="/static_new/js/sea.js?v=1"></script>
    <!-- Custom js -->
    <script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
    <script>
        seajs.use('zhuzhouTeacher');
    </script>
</head>
<body>

</body>
</html>
