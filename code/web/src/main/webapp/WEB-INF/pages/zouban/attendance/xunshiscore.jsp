<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>巡视打分</title>
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
<body id="${param.id}">
<!--#head-->
<%@ include file="../../common_new/head.jsp" %>
<!--/#head-->
<!--#content-->
<div id="content" class="clearfix">
    <!--.col-left-->
    <%@ include file="../../common_new/col-left.jsp" %>

    <div class="col-right">
        <!-- 广告部分 -->
        <div class="clslist_content clearfix">
            <div class="clslist_tab clearfix">
                <ul class="clearfix">
                    <li><a href="#">巡视打分</a></li>
                </ul>
                <a href="/attendance/course.do?index=7&version=5" class="stulist_back">&lt;返回班级列表</a>
            </div>
            <div class="clslist_mian">
                <table class="clslist_mian_title" id="lessonList">
                    <tr>
                        <td>${param.term}</td>
                        <td>${param.grade}</td>
                        <td>${param.className}</td>
                        <td>任课老师：${param.teacherName}</td>
                        <td>学生：${param.studentsCount}</td>
                    </tr>
                </table>
                <table id="xunshiScoreCtx" class="clslist_mian_table"></table>
                <script id="xunshiScoreTmpl" type="application/template">
                    <tr>
                        <th>打分序号</th>
                        <th>上课时间</th>
                        <th>任课老师评分</th>
                        <th>班级评分</th>
                    </tr>
                    {{~it:value:index}}
                    <tr id="{{=value.id}}">
                        <td>{{=index+1}}</td>
                        <td>{{=value.date}} 第{{=value.week}}周 星期{{=value.day}} 第{{=value.section}}节</td>
                        <td class="stars-container" score="{{=value.teacherScore}}" type="1">
                            <i class="fa fa-star-o fa-lg orange-color"></i>
                            <i class="fa fa-star-o fa-lg orange-color"></i>
                            <i class="fa fa-star-o fa-lg orange-color"></i>
                            <i class="fa fa-star-o fa-lg orange-color"></i>
                            <i class="fa fa-star-o fa-lg orange-color"></i>
                            <span></span>
                        </td>
                        <td class="stars-container" score="{{=value.classScore}}" type="2">
                            <i class="fa fa-star-o fa-lg orange-color"></i>
                            <i class="fa fa-star-o fa-lg orange-color"></i>
                            <i class="fa fa-star-o fa-lg orange-color"></i>
                            <i class="fa fa-star-o fa-lg orange-color"></i>
                            <i class="fa fa-star-o fa-lg orange-color"></i>
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
    seajs.use('xunshiScore', function (xunshiScore) {
        xunshiScore.init();
    });
</script>
</body>
</html>
