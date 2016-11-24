<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<html>
<head>
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn"/>
    <title>课表导入</title>
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
    <link rel="stylesheet" type="text/css" href="/static_new/css/friendcircle/rome.css?v=1"/>
    <link href="/static_new/css/zouban/zoubanindex.css?v=2015041602" rel="stylesheet"/>
    <link href="/static_new/css/schedule/kebiaodaoru.css?v=2015041602" rel="stylesheet"/>

    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>
<body>
<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->
<!--#content-->
<div id="content" class="clearfix">
    <!--.col-left-->
    <%@ include file="../common_new/col-left.jsp" %>
    <!--/.col-left-->
    <!--.col-right-->
    <input type="hidden" id="term" value="${term}">
    <div class="col-right clearfix">
        <div class="kebiao_main">
            <div class="kebiao_title clearfix">
                <ul>
                    <li id="scheduleLi">课程表</li>
                    <li id="weekLi" class="unchoosed-li">教学周设置</li>
                </ul>
                <button id="importSchedule">导入课表</button>
            </div>
            <div class="kebiao_maincon">
                <div class="kebiao_select">
                    <em class="">学期</em>
                    <select class="term_select">
                    </select>
                    <script type="application/template" id="termTempJs">
                        {{~it.data:value:index}}
                            <option>{{=value}}</option>
                        {{~}}
                    </script>
                    <%--<input type="text" value="2015-2016学年第二学期">--%>
                    <em>年级</em>
                    <select class="grade_select">
                </select>
                    <script type="application/template" id="gradeTempJs">
                        {{~it.data:value:index}}
                                <option value="{{=value.id}}">{{=value.name}}</option>
                        {{~}}
                    </script>
                    <em>班级</em><select class="class_select">
                </select>
                    <select id="weekList">

                    </select>
                    <script type="application/template" id="weekTempJs">
                        {{~it.data:value:index}}
                            <option value="{{=value}}">第{{=value}}周</option>
                        {{~}}
                    </script>
                    <button id="publishSchedule" style="display: none">发布课表</button>
                    <span id="noSchedule" style="display: none">无课表</span>
                    <span id="published" style="display: none">已发布</span>
                </div>
                <script type="application.template" id="courseTableJs">
                    <thead>
                    <tr>
                        {{?it.data.classDays.length>0}}
                        <td class="kebiao_first_col">节次/时间</td>
                        {{?}}
                    {{~it.data.classDays:value:index}}
                        <td>星期{{?value==1}}一
                            {{??value==2}}二
                            {{??value==3}}三
                            {{??value==4}}四
                            {{??value==5}}五
                            {{??value==6}}六
                            {{??value==7}}日
                            {{?}}</td>
                    {{~}}
                    </tr>
                    </thead>
                    <tbody>
                    {{~it.data.classCount:value1:index1}}
                    <tr>
                        <td class="kebiao_first_col">第{{=value1}}节{{=it.data.classTime[index1]}}</td>
                        {{~it.data.classDays:value2:index2}}
                            {{~it.data.course:value:index}}
                                {{?value.xIndex==value2&&value.yIndex==value1}}
                                    <td><span class="courseName">{{=value.className}}</span><br><span class="teacherName">{{=value.teacherName}}</span></td>
                                {{?}}
                            {{~}}
                        {{~}}
                    </tr>
                    {{~}}
                    </tbody>
                </script>
                <table class="kebiao_table" rules=all>
                </table>
            </div>
            <div class="right3-main">
                <div class="cal-term"><span>
                        学年
                        <select id="termShow2">
                        </select>
                    </span></div>
                <div class="right3-1">设置教学周</div>
                <div class="tip">
                    <span>温馨提示：设置教学周请在发布课表之前完成，每周课表将会根据此处配置生成。</span>
                </div>
                <div class="right3-2">
                    <span class="yearShow"></span>
                    <i id="firstTermWeek">第一学期&nbsp;共18周</i>
                    <span id="firstTermTime">2015-09-01~2016-01-24</span>
                </div>
                <div class="right3-3">
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
                            <span>第1周</span>
                            <span>第2周</span>
                            <span>第3周</span>
                            <span>第4周</span>
                            <span>第5周</span>
                            <span>第6周</span>
                        </div>
                        <script type="application/template" id="firstTermWeekTempJS">
                            {{~it.term:value:index}}
                            {{?index%7==0}}
                            <span>第{{=index/7+1}}周</span>
                            {{?}}
                            {{~}}
                        </script>
                        <table class="cont-date" style="margin-top: 0px; margin-left: 0px; margin-bottom: 0px; font-weight: normal;" id="firstTermData">
                        </table>
                        <script type="application/template" id="firstTermDateTempJS">
                            {{~it.term:value:index}}
                            {{?index%7==0}}
                            <tr>
                                {{?}}
                                <td style="border: none">{{=value}}</td>
                                {{?index%7==6}}
                            </tr>
                            {{?}}
                            {{~}}
                        </script>
                    </div>
                </div>
                <hr />
                <div class="right3-4">
                    <span class="yearShow"></span>
                    <i id="secondTermWeek">第二学期&nbsp;共18周</i>
                    <span id="secondTermTime">2015-03-01~2016-07-24</span>
                </div>
                <div class="right3-5">
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
                            <span>第{{=index/7+1}}周</span>
                            {{?}}
                            {{~}}
                        </script>
                        <table class="cont-date"  style="margin-top: 0px; margin-left: 0px; margin-bottom: 0px; font-weight: normal;" id="secondTermData">

                        </table>
                        <script type="application/template" id="secondTermDateTempJS">
                            {{~it.term:value:index}}
                            {{?index%7==0}}
                            <tr>
                                {{?}}
                                <td style="border: none">{{=value}}</td>
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
    <!--/.col-right-->

</div>
<!-- 设置教学周弹窗start -->
<div class="weekwind">
    <div class="hide-top">设置教学周<em class="hide-x">×</em></div>
    <dl>
        <dd class="yearShow"></dd>
        <dd><span>第一学期</span><span id="firstWeek">共18周</span></dd>
        <dd><span>开学日期</span><input type="text" readonly="true" id="firstStart"/></dd>
        <dd><span>结束日期</span><input type="text" readonly="true" id="firstEnd"/></dd>
        <dd><span>第二学期</span><span id="secondWeek">共18周</span></dd>
        <dd><span>开学日期</span><input type="text" readonly="true" id="secondStart"/></dd>
        <dd><span>结束日期</span><input type="text" readonly="true" id="secondEnd"/></dd>
        <dd><button class="week-conf">确定</button><button class="hide-canc">取消</button></dd>
    </dl>
</div>
<div class="bg"></div>
<!-- 设置教学周弹窗end -->
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js?v=1"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('schedule');
</script>
</body>
</html>