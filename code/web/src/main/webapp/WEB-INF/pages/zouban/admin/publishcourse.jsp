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
    <link href="/static_new/css/zouban/publishcourse.css?v=2015041602" rel="stylesheet"/>

    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>
<body>
<!--#head-->
<%@ include file="../../common_new/head.jsp" %>
<!--/#head-->
<!--#content-->
<div id="content" class="clearfix">
    <input id="gradeId" type="hidden" value="${gradeId}">
    <input id="year" type="hidden" value="${year}">
    <input id="term" type="hidden" value="${term}">
    <input id="gradeName" type="hidden" value="${gradeName}">
    <input id="type" type="hidden" value="${type}">
    <input id="week" type="hidden" value="${curweek}">
    <input type="hidden" id="mode" value="${mode}">
    <%--<input id="year" type="hidden" value="${year}">--%>
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
    <div class="main21-hide">
        <div class="main2-hide-bg"></div>
        <div class="col-right0">
            <div class="right0-top"><%--<a class="backUrl"
                                       href="javascript:;"&lt;%&ndash;href="../paike/index.do?version=58"&ndash;%&gt;><span>< 返回教务管理首页</span></a>--%>
                <div class="main2wind-top2">
                    <i style="font-size:16px;">教学周选择</i>
                    <i class="main2wind-cl2">×</i>
                </div>
            </div>
            <div class="right0-main1">
                <div class="right0-2">
                    <span>${year}</span>
                    <%--<i id="firstTermWeek">第一学期&nbsp;共18周</i>--%>
                    <select id="termChange">
                        <option id="firstTermWeek" value="1">第一学期&nbsp;共18周</option>
                        <option id="secondTermWeek" value="2">第二学期&nbsp;共18周</option>
                    </select>
                    <span id="firstTermTime" style="float: right;">2015-09-01~2016-01-24</span>
                    <span id="secondTermTime" style="float: right;">2015-03-01~2016-07-24</span>
                </div>
                <div class="right0-3">
                    <div class="top-day">
                        <i>日</i>
                        <i>一</i>
                        <i>二</i>
                        <i>三</i>
                        <i>四</i>
                        <i>五</i>
                        <i>六</i>
                    </div>
                    <div class="cont-day">
                        <div class="weeklist" id="firstTermList">
                            <span>第1周</span><%--<em>调课课表</em>--%>
                            <span>第2周</span>
                            <span>第3周</span>
                            <span>第4周</span><%--<em>调课课表</em>--%>
                            <span>第5周</span>
                            <span>第6周</span><%--<em>调课课表</em>--%>
                        </div>
                        <script type="application/template" id="firstTermWeekTempJS">
                            {{~it.term:value:index}}
                            {{?index%7==0}}
                            <span style="cursor: pointer" class="link" term="第一学期" week="{{=index/7+1}}">第{{=index/7+1}}周</span>
                            {{?}}
                            {{~}}
                        </script>
                        <table class="cont-date" id="firstTermData">
                        </table>
                        <script type="application/template" id="firstTermDateTempJS">
                            {{~it.term:value:index}}
                            {{?index%7==0}}
                            <tr>
                                {{?}}
                                <td>{{=value}}</td>
                                {{?index%7==6}}
                            </tr>
                            {{?}}
                            {{~}}
                        </script>
                    </div>
                </div>
                <%--<hr />--%>
                <%--<div class="right0-4">
                    <span>${year}</span>
                    &lt;%&ndash;<i id="secondTermWeek">第二学期&nbsp;共18周</i>&ndash;%&gt;
                    <span id="secondTermTime">2015-03-01~2016-07-24</span>
                </div>--%>
                <div class="right0-5">
                    <div class="top-day">
                        <i>日</i>
                        <i>一</i>
                        <i>二</i>
                        <i>三</i>
                        <i>四</i>
                        <i>五</i>
                        <i>六</i>
                    </div>
                    <div class="cont-day">
                        <div class="weeklist" id="secondTermList">
                            <span>第1周</span>
                            <span>第2周</span>
                            <span>第3周</span>
                            <span>第4周</span>
                            <span>第5周</span>
                            <span>第6周</span>
                        </div>
                        <script type="application/template" id="secondTermWeekTempJS">
                            {{~it.term:value:index}}
                            {{?index%7==0}}
                            <span style="cursor: pointer" class="link" term="第二学期" week="{{=index/7+1}}">第{{=index/7+1}}周</span>
                            {{?}}
                            {{~}}
                        </script>
                        <table class="cont-date" id="secondTermData">

                        </table>
                        <script type="application/template" id="secondTermDateTempJS">
                            {{~it.term:value:index}}
                            {{?index%7==0}}
                            <tr>
                                {{?}}
                                <td>{{=value}}</td>
                                {{?index%7==6}}
                            </tr>
                            {{?}}
                            {{~}}
                        </script>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-right">
        <!--.tab-col-->
        <div class="cont-right">
            <ul class="right-title">
                <li class="main2-li cur">
                    行政班课表
                </li>
                <li class="main1-li">
                    教学班课表
                </li>
                <li class="main3-li">
                    教师课表
                </li>
                <i>课表已发布</i><span class="backUrl">< 返回教务管理首页</span>
            </ul>
            <div class="right-main1">
                <div class="main1-1"><span class="termShowSp">${term}</span><span class="weekShow2" style="margin-left: 30px;"></span></div>
                <div class="main1-2">
                            <span><i>年级:${gradeName}</i>
                            </span>
                            <span><i>学科</i>
                                <select id="subjectShow1">
                                    <option>英语</option>
                                </select>
                                <script type="application/template" id="subjectTempJs1">
                                    {{~it.data:value:index}}
                                    <option value="{{=value.subjectName}}">{{=value.subjectName}}</option>
                                    {{~}}
                                </script>
                            </span>
                            <span><i>教学班</i>
                                <select id="courseShow1">
                                </select>
                                <script type="application/template" id="courseTempJs1">
                                    {{~it.data:value:index}}
                                    <option value="{{=value.courseId}}">{{=value.courseName}}</option>
                                    {{~}}
                                </script>
                            </span>

                    <div class="main1-btn1">筛选</div>
                </div>
                <script type="application/template" id="courseTempJs">
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
                            {{?}}
                        </td>
                        {{~}}
                    </tr>
                    {{~it.data.conf.classTime:value:index}}
                    <tr class="row">
                        <td class="col1">
                            第{{=index+1}}节
                            <br/>{{=it.data.conf.classTime[index]}}
                        </td>
                        {{~it.data.conf.classDays:value1:index1}}
                        <td class="col">
                            {{~it.data.point:value2:index2}}
                            {{?value2.x==value1 && value2.y==(index+1)}}
                            {{=it.data.teacher}}<br/>{{=it.data.classroom}}
                            {{?}}
                            {{~}}
                        </td>
                        {{~}}
                    </tr>
                    {{~}}
                </script>
                <table class="main1-3">

                </table>
            </div>

            <div class="right-main2">

                <div class="main2-1"><span class="termShowSp">${term}</span><span class="weekShow2" style="margin-left: 30px;"></span></div>
                <div class="main2-2">
                            <span><i>年级:${gradeName}</i>
                            </span>
                    <span><input type="checkbox" id="adjust">已调课</span>
                            <span><i>班级</i>
                                <select id="classShow2">
                                </select>
                                <script type="application/template" id="classTempJs2">
                                    {{~it.data:value:index}}
                                    <option value="{{=value.classId}}">{{=value.className}}</option>
                                    {{~}}
                                </script>
                            </span>

                    <div class="main2-btn1 getClassTable" style="margin-left:16px;">筛选</div>
                    <div class="main2-btn1 exportTable" style="margin-left:26px;">导出</div>

                    <div class="main2-btn1 back" style="margin-left:66px;">其他周</div>
                    <%--<div class="main2-btn1" style="margin-left:16px;">明细</div>--%>
                </div>
                <table class="main2-3" id="classTableShow">
                </table>
                <script type="application/template" id="classTableTempJs">
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
                            {{?}}
                        </td>
                        {{~}}
                    </tr>
                    {{~it.data.conf.classTime:value:index}}
                    <tr class="row">
                        <td class="col1">
                            第{{=index+1}}节
                            <br/>{{=it.data.conf.classTime[index]}}
                        </td>
                        {{~it.data.conf.classDays:value1:index1}}
                        <td class="col col-class" <%--style="cursor: pointer"--%>>
                            {{~it.data.course:value2:index2}}
                            {{?value2.xIndex==value1 && value2.yIndex==(index+1)}}
                            {{?value2.type==0}}<%--走班--%>
                            <div class="zbtd zb" x="{{=value1}}" y="{{=index+1}}">{{=value2.className}}<br/>{{=value2.classRoom}}
                            </div>
                            {{??value2.type==2}}<%--非走班--%>
                            {{=value2.className}}<br/>{{=value2.classRoom}}
                            {{??value2.type==4}}<%--走班--%>
                            <div class="zbtd tyzb" x="{{=value1}}" y="{{=index+1}}">{{=value2.className}}</div>
                            {{??}}
                            {{=value2.className}}
                            {{?}}
                            {{?}}
                            {{~}}
                            {{~it.data.conf.events:value3:index3}}
                            {{?value3.xIndex==value1 && value3.yIndex==index+1}}
                            {{?value3.forbidEvent.length==1}}
                            {{=value3.forbidEvent[0]}}
                            {{?}}
                            {{?}}
                            {{~}}
                        </td>
                        {{~}}
                    </tr>
                    {{~}}
                </script>
            </div>


            <div class="right-main3">
                <div class="main3-1"><span class="termShowSp">${term}</span><span class="weekShow2" style="margin-left: 30px;"></span></div>
                <div class="main3-2">
                        <span><i>年级:${gradeName}</i>
                            </span>
                            <span><i>学科</i>
                                <select id="subjectShow3">

                                </select>
                                <script type="application/template" id="subjectTempJs3">
                                    {{~it.data:value:index}}
                                    <option value="{{=value.subjectId}}">{{=value.subjectName}}</option>
                                    {{~}}
                                </script>
                            </span>
                            <span><i>教师</i>
                                <select id="teacherShow3">
                                </select>
                                <script type="application/template" id="teacherTempJs3">
                                    {{~it.data:value:index}}
                                    <option value="{{=value.teacherId}}">{{=value.teacherName}}</option>
                                    {{~}}
                                </script>
                            </span>
                    <input type="text" style="display: none">

                    <div class="main3-btn1 selectTeacher">筛选</div>
                    <div class="main3-btn1 exportTeacher">导出</div>
                </div>
                <table class="main3-3" id="teacherTableShow">

                </table>
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
                            {{?}}
                        </td>
                        {{~}}
                    </tr>
                    {{~it.data.conf.classTime:value:index}}
                    <tr class="row">
                        <td class="col1">
                            第{{=index+1}}节
                            <br/>{{=it.data.conf.classTime[index]}}
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
            </div>
        </div>

        <!--/.tab-col-->
    </div>
</div>
<!--/.col-right-->


<div class="main2-hide">
    <div class="main2-hide-bg"></div>
    <div class="main2-hide-wind">
        <div class="main2wind-top">
            <i style="font-size:16px;">明细</i>
            <i class="main2wind-cl">×</i>
        </div>
        <div class="hide2-main1">
				<span>
					<i class="classShow">高一1班</i>
					<select id="weekshow">

                    </select>
                    <script type="application/template" id="weekTempJs">
                        {{~it.data:value:index}}
                        {{?it.x==value}}
                        <option value="{{=value}}" selected="selected">星期{{?value==1}}一
                            {{??value==2}}二
                            {{??value==3}}三
                            {{??value==4}}四
                            {{??value==5}}五
                            {{??value==6}}六
                            {{??value==7}}日
                            {{?}}
                        </option>
                        {{??}}
                        <option value="{{=value}}">星期{{?value==1}}一
                            {{??value==2}}二
                            {{??value==3}}三
                            {{??value==4}}四
                            {{??value==5}}五
                            {{??value==6}}六
                            {{??value==7}}日
                            {{?}}
                        </option>
                        {{?}}
                        {{~}}
                    </script>
					<select id="classDetailShow">

                    </select>
                    <script type="application/template" id="classDetailTempJs">
                        {{~it.data:value:index}}
                        {{?it.y==index+1}}
                        <option value="{{=index+1}}" selected="selected">第{{=index+1}}节</option>
                        {{??}}
                        <option value="{{=index+1}}">第{{=index+1}}节</option>
                        {{?}}
                        {{~}}
                    </script>
                    <i class="i2">8:50~9:30</i>
				</span>

            <div class="hidemain-btn">筛选</div>
            <%--<div class="hidemain-btn">导出</div>--%>
        </div>
        <table class="hide2-tab" id="detailShow">

        </table>
        <script type="application/template" id="detailTempJs">
            <tr class="row1 row">
                <td class="col1">教学班</td>
                <td class="col">教学班人数</td>
                <td class="col">任课老师</td>
                <td class="col room">上课教室</td>
                <td class="col">本班学生</td>
            </tr>
            {{~it.data:value:index}}
            <tr class="row">
                <td class="col1">{{=value.className}}</td>
                <td class="col">{{=value.people}}</td>
                <td class="col">{{=value.teacherName}}</td>
                <td class="col room">{{=value.classRoom}}</td>
                <td class="col">{{=value.myClassAmount}}</td>
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
    seajs.use('publishcourse');
</script>
</body>
</html>