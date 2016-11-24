<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>管理-课时列表</title>

    <meta name="description" content="">
    <meta name="author" content=""/>
    <meta name="copyright" content=""/>
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <link href="/static_new/css/reset.css?v=1" rel="stylesheet"/>
    <link href="/static_new/css/zouban/expand.css" rel="stylesheet"/>
    <link href="/static_new/css/zouban/attendance.css" rel="stylesheet"/>
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
        <div class="clslist_content clearfix">
            <div class="clslist_tab clearfix">
                <ul class="clearfix">
                    <li><a href="#">课时列表</a></li>
                </ul>
                <a href="/attendance/course.do?index=7&version=5" class="clslist_back">
                    &lt;返回班级列表
                </a>
            </div>
            <div class="clslist_mian">
                <table class="clslist_mian_title" id="lessonList">
                    <tr>
                        <td id="term">${param.term}</td>
                        <td>${param.grade}</td>
                        <td>${param.className}</td>
                        <td>任课老师：${param.teacherName}</td>
                        <td>学生：${param.studentsCount}</td>
                    </tr>
                </table>
                <button id="add" class="attendance-add">新增</button>
                <table id="lessonCtx" class="clslist_mian_table"></table>
                <script id="lessonTmpl" type="application/template">
                    <tr>
                        <th>课时名称</th>
                        <th>上课时间</th>
                        <th>考勤情况</th>
                        <th>操作</th>
                    </tr>
                    {{~it:value:index}}
                    <tr>
                        <td><a id="{{=value.id}}" class="lessonDetail" href="javascript:void(0)">{{=value.lessonName}}</a></td>
                        <td>{{=value.date}} 第{{=value.week}}周 星期{{=value.day}} 第{{=value.section}}节</td>
                        <td>{{=value.attendedCount}}/{{=value.count}}</td>
                        <td>
                            <a onclick="toAttendance('${param.id}','${param.term}','${param.grade}','{{=value.id}}','${param.className}','${param.teacherName}','${param.studentsCount}')">考勤并打分</a>
                        </td>
                    </tr>
                    {{~}}
                </script>
            </div>
        </div>
    </div>
</div>


<!--==========弹出层背景=======-->
<div class="bg"></div>
<%--新增考勤--%>
<div class="attendance" id="">
    <div class="attendance-top">
        <em>新增/修改考勤</em>
        <i class="close">X</i>
    </div>
    <dl>
        <dd>
            <em>课时名称</em>
            <input type="text" id="lessonName" name="lessonName"/>
        </dd>
        <dd>
            <em>上课时间</em>
            <input class="Wdate" type="text" style="width:171px;height:32px;" id="date" name="date"
                   onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" value=""/>
        </dd>
        <dd>
            <em>周</em>
            <select id="week" name="week"></select>
            <script id="weekOptionsTmpl" type="application/template">
                {{~it:value:index}}
                <option value="{{=value.value}}">{{=value.text}}</option>
                {{~}}
            </script>
        </dd>
        <dd>
            <em></em>
            <select id="day" name="day">
                <option value="1">星期一</option>
                <option value="2">星期二</option>
                <option value="3">星期三</option>
                <option value="4">星期四</option>
                <option value="5">星期五</option>
                <option value="6">星期六</option>
                <option value="7">星期天</option>
            </select>
        </dd>
        <dd>
            <em></em>
            <select id="section" name="section">
                <option value="1">第一节</option>
                <option value="2">第二节</option>
                <option value="3">第三节</option>
                <option value="4">第四节</option>
                <option value="5">第五节</option>
                <option value="6">第六节</option>
                <option value="7">第七节</option>
                <option value="8">第八节</option>
                <option value="9">第九节</option>
                <option value="10">第十节</option>
            </select>
        </dd>
        <dd>
            <span>
                <button class="submit">提交</button>
                <button class="cancel">取消</button>
            </span>
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
<script type="text/javascript" src="/static_new/js/modules/calendar/0.1.0/WdatePicker.js"></script>
<script>
    seajs.use('lesson', function (lesson) {
        lesson.init();
    });
</script>
</body>
</html>
