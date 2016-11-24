<%--
  Created by IntelliJ IDEA.
  User: fulaan
  Date: 15-7-11
  Time: 上午10:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<html>
<head>
    <title>复兰科技--班级管理</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="/static_new/css/myclass/classManange.css">
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"></script>
    <%--<script type="text/javascript" src="/static/js/sharedpart.js"></script>--%>
    <script type="text/javascript" src="/static/js/common/role.js"></script>

    <script src="/static_new/js/modules/core/0.1.0/doT.min.js?v=1"></script>
</head>
<body classid="${classId}">
    <input type="hidden" id="className"/>
    <!--==================start引入头部==================-->
    <%@ include file="../common_new/head.jsp" %>
    <!--====================end引入头部====================-->
    <div id="content_main_container">
    <div class="c-manage-main">
        <!--==================start引入左边导航==================-->
        <%@ include file="../common_new/col-left.jsp" %>
        <!--====================end引入左边导航====================-->
        <!--==================start引入右边广告==================-->
        <%@ include file="../common/right.jsp" %>
        <!--====================end引入右边广告====================-->
        <div class="right-border">
        <div class="c-manage-right">
            <!--========================头部=============================-->
            <div class="c-right-top">
                <ul>
                    <li class="current">班级人员</li>
                    <li><a href="/myclass/attendance.do?classId=${classId}">班级考勤</a></li>
                    <c:if test="${master == 1}">
                    <li><a href="/myclass/stuinteclasscountpage.do?classId=${classId}">选课去向</a></li>
                    </c:if>
                </ul>
            </div>
            <!---=====================老师列表==============================-->

            <div class="c-right-middle">
                <dl>
                    <dd class="c-right-line">
                        <em><i><span class="classNameShow"></span>老师列表</i></em>
                        <button style="width: 133px;" onclick="window.location.href='/myclass/nummanage.do?classId=${classId}'">导入学号及班干部</button>
                        <button onclick="window.location='/myclass/exportExcel.do?classId=${classId}'">导出信息</button>
                    </dd>

                    <script type="text/x-dot-template" id="teacherList">
                        {{~it.teacherList:v:index}}
                            {{~v.subjectNameList:value}}
                            <div class="c-right-im">
                                <a href="/teacher/course/{{=v.id}}">
                                <img src="{{=v.imageUrl}}" width="80px;" height="80px;" target-id="{{=v.id}}" role="{{=v.role}}"></a><br>
                                <span>
                                    {{=v.userName}}<br>
                                    <label>{{=value}}老师</label>
                                </span>
                            </div>
                            {{~}}
                        {{~}}
                    </script>

                    <dd id="classListDd">
                    </dd>
                    <dd class="c-right-line">
                        <em><i><span class="classNameShow"></span>学生列表</i></em>
                    </dd>
                </dl>
            </div>
            <!---=====================学生列表==============================-->
            <div class="c-right-bottom">

                <!--====================学生列表内容===============================-->
                <script type="text/x-dot-template" id="studentList">
                    <dt>

                        <span class="c-left-sx">学生姓名</span>
                        <span>性别</span>
                        <span class="c-right-sx">看完视频数</span>
                        <span>做题数</span>
                        <span class="c-right-sx">学号</span>
                        <span>班委和课代表</span>

                    </dt>
                    <dd class="c-right-li"></dd>
                    {{~it.totalList:v:index}}
                    <dd class="c-right-ll" target-id="{{=v.studentId}}" role="{=v.role}}">
                        <span class="c-right-I">
                            <img src="{{=v.imageURL}}" width="60px" height="60px" />
                            <em>{{=v.userName}}</em>
                        </span>
                            <span class="c-right-II">{{=v.sexStr}}</span>
                            <span class="c-right-VI">{{=v.endViewNum}}</span>
                            <span class="c-right-III">{{=v.endQuestionNum}}</span>
                        <span class="c-right-V">
                            <input type="hidden" value="{{=v.studentNum==''?' ':v.studentNum==null ? '':v.studentNum}}" class="stuNumInput_before"/> <input type="input" value="{{=v.studentNum==''?' ':v.studentNum==null ? '':v.studentNum}}" class="stuNumInput_after"/>
                        </span>
                        <span class="c-right-IV">
                            <input type="hidden" value="{{=v.studentJob==''?' ':v.studentJob==null ? '':v.studentJob}}" class="stuJobInput_before"/> <input type="input" value="{{=v.studentJob==''?' ':v.studentJob==null ? '':v.studentJob}}" class="stuJobInput_after"/>
                        </span>
                    </dd>
                    {{~}}
                </script>
                <dl id="studentList_dl">
                </dl>
            </div>
        </div>
    </div>
        </div>
</div>
<%@ include file="../common_new/foot.jsp"%>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use("classManage1", function (classManage) {
        classManage.pageInit();
        classManage.statistics();
    });
</script>
</body>
</html>
