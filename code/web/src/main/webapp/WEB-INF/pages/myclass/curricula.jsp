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
    <title>复兰科技-选课去向</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="/static_new/css/myclass/classManange.css">
    <link rel="stylesheet" href="/static_new/css/myclass/curricula.css">
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <%--<script type="text/javascript" src="/static/js/sharedpart.js"></script>--%>
    <script type="text/javascript" src="/static/js/common/role.js"></script>

    <script src="/static_new/js/modules/core/0.1.0/doT.min.js?v=1"></script>
</head>
<body>
<input type="hidden" id="schoolId" value="${schoolId}"/>
<input type="hidden" id="gradeId" value="${gradeId}"/>
<input type="hidden" id="classId" value="${classId}"/>
<!--==================start引入头部==================-->
<%@ include file="../common_new/head.jsp" %>
<!--====================end引入头部====================-->
<div id="content_main_container">
    <div class="c-manage-main" style="overflow: hidden;">
        <!--==================start引入左边导航==================-->
        <%@ include file="../common_new/col-left.jsp" %>
        <!--====================end引入左边导航====================-->
        <!--==================start引入右边广告==================-->
        <%@ include file="../common/right.jsp" %>
        <!--====================end引入右边广告====================-->
        <div class="right-border" style="float:right; margin-bottom: 30px;">
            <div class="c-manage-right" style="width:770px; padding-top: 20px;">
                <!--========================头部=============================-->
                <div class="c-right-top">
                    <ul>
                        <li><a href="/myclass/statstu/${classId}">班级人员</a></li>
                        <li><a href="/myclass/attendance.do?classId=${classId}">班级考勤</a></li>
                        <li class="current">选课去向</li>
                    </ul>
                </div>
                <div class="c-right-middle">
                    <dl>
                        <dd class="c-right-line">
                            <em><i><span class="classNameShow"></span>${className}</i></em>
                            <button onclick="window.location='/myclass/exportStuInteClassExcel.do?schoolId=${schoolId}&gradeId=${gradeId}&classId=${classId}'">导出信息</button>
                        </dd>
                    </dl>
                </div>
                <!---=====================学生列表==============================-->
                <div class="curricula-bottom">
                    <div class="curricula-top">
                        <span>学生姓名</span>|
                        <span>选课去向</span>
                    </div>

                    <div class="curricula-ta"></div>

                    <!--.sub-info-list-->
                    <div class="sub-info-list">

                    </div>
                    <!--.sub-info-list-->
                    <!--.list-info-->
                    <script type="text/template" id="j-tmpl">
                        <ul class="curricula-bl">
                            {{~it.content:v:index}}
                                <li class="curricula-b">
                                    <span class="b">
                                        <img class="curricula-img" src="{{=v.imageURL}}" width="60px;" height="60px;">
                                        <em class="curricula-sp">{{=v.userName}}</em>
                                    </span>
                                    <span class="curricula-a">
                                         <%--{{=v.interestClassInfo}}--%>
                                        {{for (var i in v.interestClassList) {}}
                                        <em class="detail" id="{{=v.interestClassList[i].idStr}}" sid="{{=v.studentId}}">{{=v.interestClassList[i].value}};</em>
                                        {{} }}
                                    </span>
                                </li>
                            {{~}}
                        </ul>
                    </script>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="../common_new/foot.jsp"%>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('curricula',function(curricula){
        curricula.init();
    });
</script>
</body>
</html>
