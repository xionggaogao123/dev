<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>考勤</title>

    <meta name="description" content="">
    <meta name="author" content=""/>
    <meta name="copyright" content=""/>
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <link href="/static_new/css/reset.css?v=1" rel="stylesheet"/>
    <link href="/static_new/css/zouban/expand.css" rel="stylesheet"/>
    <link href="/static_new/css/zouban/attendance.css" rel="stylesheet"/>
    <link href="/static/css/font-awesome.min.css" rel="stylesheet"/>
</head>
<body id="${param.lessonId}">
<!--#head-->
<%@ include file="../../common_new/head.jsp" %>
<!--/#head-->
<!--#content-->
<div id="content" class="clearfix">
    <!--.col-left-->
    <%@ include file="../../common_new/col-left.jsp" %>

    <div class="col-right">
        <!-- 广告部分 -->
        <div class="stulist_content clearfix">
            <div class="stulist_tab clearfix">
                <ul class="clearfix">
                    <li><a href="#">学生列表</a></li>
                </ul>
                <a href="/attendance/lesson.do?index=7&version=5&id=${param.id}&className=${param.className}&teacherName=${param.teacherName}
                    &studentsCount=${param.studentsCount}&term=${param.term}&grade=${param.grade}"
                   class="stulist_back">&lt;返回课时列表</a>
            </div>
            <div class="stulist_mian">
                <table class="stulist_mian_title">
                    <tr>
                        <td>${param.term}</td>
                        <td>${param.grade}</td>
                        <td>${param.className}</td>
                        <td>任课老师：${param.teacherName}</td>
                        <td>学生：${param.studentsCount}</td>
                    </tr>
                </table>
                <table id="studentListCtx" class="stulist_mian_table"></table>
                <script id="studentListTmpl" type="application/template">
                    <tr>
                        <th style="width:15%;">学生姓名</th>
                        <th style="width:15%;">考勤</th>
                        <th style="width:24%;">课堂表现</th>
                        <th style="width:23%;">遵守纪律</th>
                        <th style="width:23%;">爱护公物</th>
                    </tr>
                    {{~it:value:index}}
                    <tr id="{{=value.studentId}}">
                        <td>{{=value.studentName}}</td>
                        <td>
                            <label>
                                <input type="radio" name="attendance_{{=index}}" value="1" {{?value.attendance==1}}checked{{?}}>到
                            </label>
                            <label>
                                <input type="radio" name="attendance_{{=index}}" value="0" {{?value.attendance==0}}checked{{?}}>
                                <span class="stulist_queqin">缺勤</span>
                            </label>
                        </td>
                        <td class="stars-container" scoreItem="sc1" score="{{=value.score1}}">
                            <i class="fa fa-star-o fa-lg orange-color"></i>
                            <i class="fa fa-star-o fa-lg orange-color"></i>
                            <i class="fa fa-star-o fa-lg orange-color"></i>
                            <i class="fa fa-star-o fa-lg orange-color"></i>
                            <i class="fa fa-star-o fa-lg orange-color"></i><br/>
                            <span></span>
                        </td>
                        <td class="stars-container" scoreItem="sc2" score="{{=value.score2}}">
                            <i class="fa fa-star-o fa-lg orange-color"></i>
                            <i class="fa fa-star-o fa-lg orange-color"></i>
                            <i class="fa fa-star-o fa-lg orange-color"></i>
                            <i class="fa fa-star-o fa-lg orange-color"></i>
                            <i class="fa fa-star-o fa-lg orange-color"></i><br/>
                            <span></span>
                        </td>
                        <td class="stars-container" scoreItem="sc3" score="{{=value.score3}}">
                            <i class="fa fa-star-o fa-lg orange-color"></i>
                            <i class="fa fa-star-o fa-lg orange-color"></i>
                            <i class="fa fa-star-o fa-lg orange-color"></i>
                            <i class="fa fa-star-o fa-lg orange-color"></i>
                            <i class="fa fa-star-o fa-lg orange-color"></i><br/>
                            <span></span>
                        </td>
                    </tr>
                    {{~}}
                </script>
            </div>
        </div>
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
    seajs.use('zoubanAttendance', function (attendance) {
        attendance.init();
    });
</script>
</body>
</html>
