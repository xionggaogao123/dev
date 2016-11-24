<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<html>
<head>
    <meta charset="utf-8">
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
    <link href="/static_new/css/zouban/course-teacher.css?v=2015041602" rel="stylesheet"/>

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
        <input type="hidden" id="termVal" value="${term}">
        <input type="hidden" id="yearVal" value="${year}">
        <input type="hidden" id="curweek" value="${curweek}">
        <input type="hidden" id="allweek" value="${allweek}">
        <!--.tab-col-->
        <div class="cont-right">
            <ul class="right-title">
                <li class="main1-li main1-l1 cur">
                    课表
                </li>
                <li class="main2-li main1-l2">
                    教学班信息
                </li>
                <li class="main3-li main1-l3">
                    选修课课程公示
                </li>

            </ul>
            <div class="right-main1">
                <div class="main1-1">
                    <span class="termShow">${term}</span>
                    <span style="margin-left: 20px;">年级</span>
                    <select id="gradeShow">

                    </select>
                    <script type="application/template" id="gradeTempJs">
                        {{~it.data:value:index}}
                                <option value="{{=value.id}}">{{=value.name}}</option>
                        {{~}}
                    </script>
                    <select id="weekShow" style="height: 35px;">

                    </select>
                    <script type="application/template" id="weekTempJs">
                        {{~it.data:value:index}}
                        <option value="{{=value.id}}">{{=value.name}}</option>
                        {{~}}
                    </script>
                    <button id="exportTable1">导出</button>
                    <button id="getDetail">筛选</button>
                </div>
                <script type="application/template" id="teacherTableTempJs">
                    <tr class="row1">
                        <td class="col1">节次/时间</td>
                        {{~it.data.conf.classDays:value:index}}
                        <td class="col">星期{{?value==1}}一
                            {{??value==2}}二
                            {{??value==3}}三
                            {{??value==4}}四
                            {{??value==5}}五
                            {{??value==6}}六
                            {{??value==7}}日
                            {{?}}</td>
                        {{~}}
                    </tr>
                    {{~it.data.conf.classTime:value:index}}
                    <tr class="row">
                        <td class="col1">
                            第{{=index+1}}节
                            <br />{{=it.data.conf.classTime[index]}}
                        </td>
                        {{~it.data.conf.classDays:value1:index1}}
                        <td class="col">
                            {{~it.data.course:value2:index2}}
                            {{?value2.xIndex==value1 && value2.yIndex==(index+1)}}
                            {{=value2.className}}<br/>{{=value2.classRoom}}
                            {{?}}
                            {{~}}
                        </td>
                        {{~}}
                    </tr>
                    {{~}}
                </script>
                <table class="main1-3">
                </table>
                <div id="noticeShow" style="margin: 35px;">
                    <%--<dd>
                        <em>调课通知</em>
                    </dd>
                    <dd>
                        <em>说明：因特殊原因，对课程进行调整，具体调整如下：</em>
                    </dd>
                    <dd class="asjust-tiaozheng">
                        <table>
                            <tr>
                                <th>班级</th>
                                <th>调整课程</th>
                                <th>任课教师</th>
                                <th>原上课时间</th>
                                <th>新上课时间</th>
                            </tr>
                            <tr>
                                <td>高一一班</td>
                                <td>数学</td>
                                <td>$siri</td>
                                <td>第三周星期四第三节</td>
                                <td>第三周星期四第三节</td>
                            </tr>
                        </table>
                    </dd>--%>
                </div>
                <script type="application/template" id="noticeTempJs">
                    <dd>
                        <em>调课通知</em>
                    </dd>
                    <dd>
                        <em>说明：因特殊原因，对课程进行调整，具体调整如下：</em>
                    </dd>
                    <dd class="asjust-tiaozheng">
                        <table>
                            <tr>
                                <th>班级</th>
                                <th>调整课程</th>
                                <th>任课教师</th>
                                <th>原上课时间</th>
                                <th>新上课时间</th>
                            </tr>
                            {{~it.data:value:index}}
                            <tr>
                                <td>{{=value.cl}}</td>
                                <td>{{=value.co}}</td>
                                <td>{{=value.te}}</td>
                                <td>{{=value.ot}}</td>
                                <td>{{=value.nt}}</td>
                            </tr>
                            {{~}}
                        </table>
                    </dd>
                </script>
            </div>

            <div class="right-main2">
                <div class="main2-1 termShow">
                    ${term}
                </div>

                <table class="main2-3">
                </table>
                <script type="application/template" id="courseTempJs">
                    <tr class="row1">
                        <td class="col1">教学班</td>
                        <td class="col2">人数</td>
                        <td class="col3">每周课时</td>
                        <td class="col4">上课教室</td>
                        <td class="col5">学生名单</td>
                    </tr>
                    {{~it.data:value:index}}
                    <tr>
                        <td>{{=value.courseName}}</td>
                        <td>{{=value.courseStudentAmount}}</td>
                        <td>{{=value.courseClassTimes}}</td>
                        <td>{{=value.classRoom}}</td>
                        <td><em cid="{{=value.courseId}}">查看</em></td>
                    </tr>
                    {{~}}
                </script>
            </div>


            <div class="right-main3">
                <div class="main3-2 termShow">
                    ${term}
                </div>
                <div class="main3-3">
                    <%--选修等级考数量：<span id="ac" style="margin-right: 30px;">3</span>合格考数量：<em id="sc">3</em>--%>
                    开放时间：<span id="time">2015-08-25~2015-09-01</span>
                </div>
                <script type="application/template" id="gongshiTempJs">
                    <tr class="row0">
                        <td class="col1">课程名</td>
                        <td class="col1">学科</td>
                        <td class="col1">任课老师</td>
                        <td class="col1">上课地点</td>
                        <td class="col1">面向对象</td>
                        <td class="col1">上课时间</td>
                        <td class="col1">人数上限</td>
                    </tr>
                    {{~it.data:value:index}}
                    <tr>
                        <td>{{=value.courseName}}</td>
                        <td>{{=value.subjectName}}</td>
                        <td>{{=value.teacherName}}</td>
                        <td>{{=value.classRoom}}</td>
                        <td>{{?value.groupName==""}}
                            全年级
                            {{??}}
                            {{=value.groupName}}
                            {{?}}</td>
                        <td>{{~value.pointEntrylist:v:i}}
                            星期{{=v.x}}第{{=v.y}}节
                            {{~}}</td>
                        <td>{{=value.max}}</td>
                    </tr>
                    {{~}}
                </script>
                <table class="main3-4">

                </table>
            </div>
            <div class="right-main7">
                <%--<span class="studye">2015-2016学年 xxx学期</span>--%>
                <i class="no-class">本学期选课未开放</i>
            </div>
            <div class="main2a">

                <div class="main2a-1">
                    < 返回
                </div>
                <div class="main2a-2 termShow">
                    ${term}
                </div>
                <div class="main2a-3">
                    <span id="s1">物理-等级考-A1</span><em id="s2">教室：高一1班教室</em> <em id="s3">人数：40</em>
                </div>
                <table class="main2a-4">

                </table>
                <script type="application/template" id="detailTempJs">
                    <tr class="row1">
                        <td>学生姓名</td>
                        <td>性别</td>
                        <td>所属行政班</td>
                    </tr>
                            {{~it.data:value:index}}
                    <tr>
                        <td>{{=value.studentName}}</td>
                        <td>{{?value.studentSex==0}}女{{??value.studentSex==1}}男{{?}}</td>
                        <td>{{=value.studentClass}}</td>
                    </tr>
                    {{~}}
                </script>
            </div>
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
    seajs.use('zhuzhou-course-teacher');
</script>
</body>
</html>