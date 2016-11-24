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

    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>
<body>
<!--#head-->
<%@ include file="../../common_new/head.jsp" %>
<!--/#head-->
<!--#content-->
<div id="content" class="clearfix">
    <input type="hidden" id="term" value="${term}">
    <input type="hidden" id="allweek" value="${allweek}">
    <input type="hidden" id="week" value="${week}">
    <!--.col-left-->
    <%@ include file="../../common_new/col-left.jsp" %>
    <!--/.col-left-->
    <!--.col-right-->
    <!--广告-->
 <%--   <c:choose>
        <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
        </c:when>
        <c:otherwise>
            <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
        </c:otherwise>
    </c:choose>--%>
    <!--广告-->
    <div class="col-right">
        <div class="cont-right">
            <ul class="right-title">
                <li class="class-list cont-style">课表</li>
                <li class="class-select " style="margin-left:2px;">选课</li>
                <%--<i> < 返回教务管理首页</i>--%>
            </ul>

            <div class="right-main1">

                <div class="class-cont2">
                    <%--<i>{{=it.data.conf.term}}</i>--%>
                    <i>${term}</i>
                    <select id="weekShow" style="height: 35px;">

                    </select>
                    <script type="application/template" id="weekTempJs">
                        {{~it.data:value:index}}
                        <option value="{{=value.id}}">{{=value.name}}</option>
                        {{~}}
                    </script>

                    <button id="printPDF1">导出</button>
                    <button id="select" style="margin-right: 25px">筛选</button>
                </div>
                <div class="lesson-list" id="div01">

                </div>
                <script type="application/template" id="tempJs">
                    <table id="tableShow">
                        <tr class="row1">
                            <td class="cell1">节次/时间</td>
                            {{~it.data.conf.classDays:value:index}}
                            <td>星期{{?value==1}}一
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
                        <tr class="row2 row">
                            <td class="cell1">
                                第{{=index+1}}节
                                <span>{{=it.data.conf.classTime[index]}}</span>
                            </td>
                            {{~it.data.conf.classDays:value1:index1}}
                            <td class="li-cot">
                                {{~it.data.course:value2:index2}}
                                    {{?value2.xIndex==value1 && value2.yIndex==(index+1)}}
                                        <em>{{=value2.className}}</em><br/>
                                        {{?value2.teacherName!=""}}
                                        <span>({{=value2.teacherName}})</span><br/>
                                        {{?}}
                                        <i>{{=value2.classRoom}}</i>
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

                    </table>
                </script>
                <div id="noticeShow" style="margin: 35px;margin-top:82px;">

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
                <i class="no-class">本学期选课已结束</i>
            </div>


        </div>
        <!--/.tab-col-->
    </div>
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
    seajs.use('lessonpublished');
</script>
</body>
</html>