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
    <link href="/static_new/css/zouban/clashcheck.css?v=2015041602" rel="stylesheet"/>

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
    <div class="col-right">
        <input type="hidden" id="gradeId" value="${gradeId}">
        <input type="hidden" id="termShow" value="${term}">
        <input type="hidden" id="mode" value="${mode}">
        <!--.tab-col-->
        <div class="cont-right">
            <div class="right-title">
                <div class="title-t">检查冲突</div>
                <span class="backindex">< 返回教务管理首页</span>
            </div>
            <div class="right-main">
                <div class="right-main1">
                    <span>${term}
                    </span>
                    <span>年级:${gradeName}
                    </span>

                    <div class="main1-btn conflictTest">检查冲突</div>
                    <span>分段
                        <select id="groupShow">

                        </select>
                        <script type="application/template" id="groupTempJs">
                            {{~it.data:value:index}}
                            <option value="{{=value.groupId}}">第{{=value.group}}段</option>
                            {{~}}
                        </script>
                    </span>
                    <span>教学班
                        <select style="width:157px;" id="courseShow">
                        </select>
                        <script type="application/template" id="courseTempJs">
                            <option value="0" class="select-all" selected="selected">全选</option>
                            {{~it.data:value:index}}
                            <option value="{{=value.courseId}}">{{=value.courseName}}</option>
                            {{~}}
                        </script>
                    </span>

                    <div class="main1-btn select">筛选</div>
                </div>
                <div class="right-main2">
                    <script type="application/template" id="tableTempJs">
                        <tr class="row1">
                            {{?it.data.length>0}}
                            <td class="col1"> 教学班<br/> ({{=it.data.length}})</td>
                            <td class="col2">冲突数量</td>
                            {{?}}
                            {{?it.classId==0||it.classId=="0"}}
                            {{~it.data:value:index}}
                            <td class="col" nm="{{=value.courseName}}">{{=value.courseName}}<br/>{{=value.teacherName}}</td>
                            {{~}}
                            {{??}}
                            {{~it.data:value:index}}
                            {{?value.courseId==it.classId}}
                            <td class="col" nm="{{=value.courseName}}">{{=value.courseName}}<br/>{{=value.teacherName}}</td>
                            {{?}}
                            {{~}}
                            {{?}}
                        </tr>
                        {{~it.data:value:index}}
                        <tr class="row">
                            <td class="col" nm="{{=value.courseName}}">{{=value.courseName}}<br/>{{=value.teacherName}}</td>
                            <td class="col2">{{=value.conflictCount}}</td>
                            {{?it.classId==0||it.classId=="0"}}
                            {{~it.data:value1:index1}}
                                {{?value.courseId==value1.courseId}}
                                <td style="background:#ccc;"></td>
                                {{??$.inArray(value1.courseId,value.conflictCourseIds)>-1}}
                                <td class="red-clash" style="background:#ff5959;" cid1="{{=value.courseId}}"
                                        cid2="{{=value1.courseId}}" index="{{=index1}}"></td>
                                {{??}}
                                <td style="background:#a6e87a;"></td>
                                {{?}}
                            {{~}}
                            {{??}}
                            {{~it.data:value1:index1}}
                            {{?value1.courseId==it.classId}}
                            {{?value.courseId==value1.courseId}}
                            <td style="background:#ccc;"></td>
                            {{??$.inArray(value1.courseId,value.conflictCourseIds)>-1}}
                            <td class="red-clash" style="background:#ff5959;" cid1="{{=value.courseId}}"
                                cid2="{{=value1.courseId}}" index="0"></td>
                            {{??}}
                            <td style="background:#a6e87a;"></td>
                            {{?}}
                            {{?}}
                            {{~}}
                            {{?}}
                        </tr>
                        {{~}}
                    </script>
                    <table class="main2-tab" id="MyTable" cellspacing="0"
                           cellpadding="0">

                    </table>
                </div>
            </div>
        </div>

        <!--/.tab-col-->
    </div>
    <!--/.col-right-->

</div>
<!--/#content-->
<div class="clash-detial">
    <div class="detial-bg"></div>
    <div class="detial-wind">
        <div class="setwind-top">
            <i style="font-size:16px;">冲突详情</i>
            <i class="detial-cl">×</i>
        </div>
        <div class="clash-info">
            <p class="p1">物理-等级考-A1/化学-等级考-B2 冲突详情</p>
            <p id="teacher">任课教师冲突：xxx</p>
            <p id="classRoom">上课教室冲突：高一1班教室</p>
            <p>学生冲突</p>
        </div>
        <div id="table" style="max-height: 276px;overflow-y: auto">
        <table class="clash-tab">

        </table>
        </div>
        <script type="application/template" id="detailTempJs">
            <tr class="row1">
                <td>学生姓名</td>
                <td>行政班级</td>
            </tr>
            {{~it.data:value:index}}
            <tr class="row">
                <td>{{=value.student}}</td>
                <td>{{=value.className}}</td>
            </tr>
            {{~}}
        </script>

    </div>
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
    seajs.use('clashcheck');
</script>
</body>
</html>