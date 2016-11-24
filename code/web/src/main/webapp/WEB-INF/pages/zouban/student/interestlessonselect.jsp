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
    <link href="/static_new/css/zouban/lessonselect.css?v=2015041602" rel="stylesheet"/>
    <link href="/static_new/css/zouban/expand.css?v=2015041602" rel="stylesheet"/>
    <link href="/static_new/css/zouban/tzxuanke.css?v=2015041602" rel="stylesheet"/>

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

    <div class="col-right">
        <input type="hidden" value="0" id="result">
        <!--.tab-col-->
        <div class="tzxuanke_content clearfix">
            <div class="tzxuanke_tab clearfix">
                <ul class="">
                    <li><a href="#" class="class-list">课表</a></li>
                    <li><a href="#" class="class-select tzxuanke_active">选课</a></li>
                </ul>
            </div>

            <div class="right-main1" style="display: none">
                <i class="no-class">本学期课表未发布</i>
            </div>
            <div class="tzxuanke_mian" style="display: block">
                <span>拓展课选课</span>
                <%--<table>
                    <thead>
                    <tr>
                        <th style="width:100px;">分类</th>
                        <th style="width:210px;">课程名称</th>
                        <th style="width:120px;">授课老师</th>
                        <th style="width:200px;">上课教室</th>
                        <th style="width:120px;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td rowspan="4"  class="tzxuanke_class">音乐</td>
                        <td>陶笛演奏</td>
                        <td>李卉</td>
                        <td>一单元1501班（周五）</td>
                        <td><a href="#">选课</a></td>
                    </tr>
                    <tr>
                        <td>陶笛演奏</td>
                        <td>李卉</td>
                        <td>一单元1501班（周五）</td>
                        <td><a href="#">选课</a></td>
                    </tr>
                    <tr>
                        <td>陶笛演奏</td>
                        <td>李卉</td>
                        <td>一单元1501班（周五）</td>
                        <td><a href="#" class="tzxuanke_selected">选课</a></td>
                    </tr>
                    <tr>
                        <td>陶笛演奏</td>
                        <td>李卉</td>
                        <td>一单元1501班（周五）</td>
                        <td><a href="#">选课</a></td>
                    </tr>
                    </tbody>
                </table>
                <table>
                    <thead>
                    <tr>
                        <th style="width:100px;">分类</th>
                        <th style="width:210px;">课程名称</th>
                        <th style="width:120px;">授课老师</th>
                        <th style="width:200px;">上课教室</th>
                        <th style="width:120px;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td rowspan="4" class="tzxuanke_class">美术</td>
                        <td>陶笛演奏</td>
                        <td>李卉</td>
                        <td>一单元1501班（周五）</td>
                        <td><a href="#">选课</a></td>
                    </tr>
                    <tr>
                        <td>陶笛演奏</td>
                        <td>李卉</td>
                        <td>一单元1501班（周五）</td>
                        <td><a href="#">选课</a></td>
                    </tr>
                    <tr>
                        <td>陶笛演奏</td>
                        <td>李卉</td>
                        <td>一单元1501班（周五）</td>
                        <td><a href="#">选课</a></td>
                    </tr>
                    <tr>
                        <td>陶笛演奏</td>
                        <td>李卉</td>
                        <td>一单元1501班（周五）</td>
                        <td><a href="#">选课</a></td>
                    </tr>
                    </tbody>
                </table>
                <table>
                    <thead>
                    <tr>
                        <th style="width:100px;">分类</th>
                        <th style="width:210px;">课程名称</th>
                        <th style="width:120px;">授课老师</th>
                        <th style="width:200px;">上课教室</th>
                        <th style="width:120px;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td rowspan="4" class="tzxuanke_class">体育</td>
                        <td>陶笛演奏</td>
                        <td>李卉</td>
                        <td>一单元1501班（周五）</td>
                        <td><a href="#">选课</a></td>
                    </tr>
                    <tr>
                        <td>陶笛演奏</td>
                        <td>李卉</td>
                        <td>一单元1501班（周五）</td>
                        <td><a href="#">选课</a></td>
                    </tr>
                    <tr>
                        <td>陶笛演奏</td>
                        <td>李卉</td>
                        <td>一单元1501班（周五）</td>
                        <td><a href="#">选课</a></td>
                    </tr>
                    <tr>
                        <td>陶笛演奏</td>
                        <td>李卉</td>
                        <td>一单元1501班（周五）</td>
                        <td><a href="#">选课</a></td>
                    </tr>
                    </tbody>
                </table>
                <table>
                    <thead>
                    <tr>
                        <th style="width:100px;">分类</th>
                        <th style="width:210px;">课程名称</th>
                        <th style="width:120px;">授课老师</th>
                        <th style="width:200px;">上课教室</th>
                        <th style="width:120px;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td rowspan="4" class="tzxuanke_class">校本选修课</td>
                        <td>陶笛演奏</td>
                        <td>李卉</td>
                        <td>1501班</td>
                        <td><a href="#">选课</a></td>
                    </tr>
                    <tr>
                        <td>陶笛演奏</td>
                        <td>李卉</td>
                        <td>1501班</td>
                        <td><a href="#">选课</a></td>
                    </tr>
                    <tr>
                        <td>陶笛演奏</td>
                        <td>李卉</td>
                        <td>1501班</td>
                        <td><a href="#">选课</a></td>
                    </tr>
                    <tr>
                        <td>陶笛演奏</td>
                        <td>李卉</td>
                        <td>1501班</td>
                        <td><a href="#">选课</a></td>
                    </tr>
                    </tbody>
                </table>--%>
            </div>
            <script type="application/template" id="zhuZhouCourseJs">
                <span>拓展课选课</span>
                {{~it.groupCourse:value:index}}
                <table>
                    <thead>
                    <tr>
                        <th style="width:100px;">分类</th>
                        <th style="width:210px;">课程名称</th>
                        <th style="width:120px;">授课老师</th>
                        <th style="width:200px;">上课教室</th>
                        <th style="width:120px;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    {{~value.list:v1:index1}}
                    <tr>
                        {{?index1==0}}
                        <td rowspan="{{=value.list.length}}"  class="tzxuanke_class">{{=value.subjectName}}</td>
                        {{?}}
                        <td>{{=v1.courseName}}</td>
                        <td>{{=v1.teacherName}}</td>
                        <td>{{=v1.classRoom}}</td>
                        {{?it.choosedCourse.indexOf(v1.zbCourseId)>=0}}
                        <td><a zid="{{=v1.zbCourseId}}" class="tzxuanke_selected">已选课</a></td>
                        {{??}}
                        <td><a zid="{{=v1.zbCourseId}}" class="unChoose">选课</a></td>
                        {{?}}
                    </tr>
                    {{~}}
                    </tbody>
                </table>
                {{~}}
                {{?it.singleCourse.length>0}}
                <table>
                    <thead>
                    <tr>
                        <th style="width:100px;">分类</th>
                        <th style="width:210px;">课程名称</th>
                        <th style="width:120px;">授课老师</th>
                        <th style="width:200px;">上课教室</th>
                        <th style="width:120px;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    {{~it.singleCourse:value:index}}
                    <tr>
                        {{?index==0}}
                        <td rowspan="{{=it.singleCourse.length}}" class="tzxuanke_class">校本选修课</td>
                        {{?}}
                        <td>{{=value.courseName}}</td>
                        <td>{{=value.teacherName}}</td>
                        <td>{{=value.classRoom}}</td>
                        {{?it.choosedCourse.indexOf(value.zbCourseId)>=0}}
                        <td><a zid="{{=value.zbCourseId}}" class="tzxuanke_selected">已选课</a></td>
                        {{??}}
                        <td><a zid="{{=value.zbCourseId}}" class="unChoose">选课</a></td>
                        {{?}}
                    </tr>
                    {{~}}
                    </tbody>
                </table>
                {{?}}
            </script>
        </div>
        <!--/.tab-col-->
    </div>
    <!--/.col-right-->

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
    seajs.use('interestlessonselect');
</script>
</body>
</html>