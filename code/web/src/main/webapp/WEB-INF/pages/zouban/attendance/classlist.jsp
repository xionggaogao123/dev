<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>管理-班级列表</title>

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
<body>
<!--#head-->
<%@ include file="../../common_new/head.jsp" %>
<!--/#head-->
<!--#content-->
<div id="content" class="clearfix">
    <!--.col-left-->
    <%@ include file="../../common_new/col-left.jsp" %>

    <div class="col-right">

        <div class="clsmanage_content clearfix">
            <div class="clsmanage_tab clearfix">
                <ul class="clearfix">
                    <li><a href="javascript:void(0)">班级列表</a></li>
                </ul>
            </div>
            <div class="clsmanage_mian clearfix">
                <div class="clsmanage_select">
                    <select id="termCtx" class="clsmanage_xueqi">
                    </select>
                    <script id="termTmpl" type="application/template">
                        {{~it:value:index}}
                        <option value="{{=value}}">{{=value}}第一学期</option>
                        <option value="{{=value}}">{{=value}}第二学期</option>
                        {{~}}
                    </script>
                    <select id="gradeCtx" class="clsmanage_grade">
                    </select>
                    <script id="gradeTmpl" type="application/template">
                        {{~it:value:index}}
                        <option value="{{=value.id}}">{{=value.name}}年级</option>
                        {{~}}
                    </script>
                    <select id="courseType" class="clsmanage_class">
                        <option value="2">非走班课程</option>
                        <option value="1">走班课程</option>
                        <option value="6">选修课</option>
                        <option value="0">其他</option>
                    </select>
                    <select id="subjectCtx" class="clsmanage_subject">
                    </select>
                    <script id="subjectTmpl" type="application/template">
                        {{~it:value:index}}
                        <option value="{{=value.subjectId}}">{{=value.subjectName}}</option>
                        {{~}}
                    </script>
                </div>
                <div id="courseListCtx">
                </div>
                <script id="courseListTmpl" type="application/template">
                    {{~it:value:index}}
                    <div class="clsmanage_score"
                         <c:if test="${role == 2 || role == 10 || role == 66}">{{?value.teacherName == '${myName}'}}style="cursor:pointer;"
                         onclick="toLessonListPage('{{=value.zbCourseId}}','{{=value.courseName}}','{{=value.className}}','{{=value.teacherName}}','{{=value.studentsCount}}')" {{?}}</c:if>>
                        <img src="/img/clsmanage_img.png">
                        <table>
                            <tr>
                                <th>{{=value.courseName}}</th>
                                <th>任课老师</th>
                                <th>班级学生数</th>
                            </tr>
                            <tr>
                                <td>{{=value.className}}</td>
                                <td>{{=value.teacherName}}</td>
                                <td>{{=value.studentsCount}}</td>
                            </tr>
                        </table>
                        <c:if test="${role == 8 || role == 64 || role == 10 || role == 66}">
                            <a href="javascript:void(0);"
                               onclick="xunshiScore(event,'{{=value.zbCourseId}}','{{=value.courseName}}','{{=value.className}}','{{=value.teacherName}}','{{=value.studentsCount}}')">巡视打分</a>
                        </c:if>
                    </div>
                    {{~}}
                </script>
                <div class="new-page-links"></div>
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
    seajs.use('classList',function(classList){
        classList.init();
    });
</script>
</body>
</html>
