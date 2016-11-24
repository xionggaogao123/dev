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
    <%--<link href="/static/css/homepage.css?v=1" rel="stylesheet"/>--%>
    <link href="/static_new/css/reset.css?v=1" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="/static_new/css/friendcircle/rome.css?v=1"/>
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="/static_new/css/zouban/zoubanindex.css?v=2015041602" rel="stylesheet"/>

    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>
<body>
<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->
<!--#content-->
<div id="content" class="clearfix">
    <%--<input type="hidden" value="${year}" id="year">--%>
    <!--.col-left-->
    <%@ include file="../common_new/col-left.jsp" %>
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
        <input type="hidden" id="year" value="${year}">
        <!--.tab-col-->
        <div class="cont-right">
            <ul class="right-title">
                <li class="cont-style main1-title">走班教务管理</li>
                <li class="main2-title" >教室管理</li>
                <li class="main3-title">教学周管理</li>
            </ul>
            <div class="right-main">
                <div class="right-main1">
                    <span>
                        学年/学期
                        <select id="termShow">
                        </select>
                        <script type="application/template" id="termTempJs">
                            {{~it.data:value:index}}
                            <option value="{{=value}}">{{=value}}</option>
                            {{~}}
                        </script>
                    </span>
                    <span>
                        年级
                        <select id="gradeShow">
                        </select>
                        <script type="application/template" id="gradeTempJs">
                            {{~it.data:value:index}}
                            <option value="{{=value.id}}">{{=value.name}}</option>
                            {{~}}
                        </script>
                        <div class="main1-btn1">筛选</div>
                    </span>
                    <span style="margin-top: 22px;" id="chooseShow"></span>
                </div>

                <div class="right-mian2-gezhi">
                    <div class="main2-div1">
                        <div class="main2-child lie1 v1" >
                            <i clss="main2-i">①</i>
                            <div class="mian2-grandson" id="step01">
                                课程设置
                            </div>
                        </div>
                        <div class="main2-child lie2 v2">
                            <i clss="main2-i">②</i>
                            <div class="mian2-grandson" id="step02">
                                选课进度
                            </div>
                        </div>
                        <div class="main2-child lie3 v3">
                            <i clss="main2-i">③</i>
                            <div class="mian2-grandson" id="step03">
                                分段编班
                            </div>
                        </div>
                    </div>
                    <div class="main2-div2">
                        <div class="main2-child lie1 v4">
                            <i clss="main2-i">④</i>
                            <div class="mian2-grandson" id="step04">
                                排课
                            </div>
                        </div>
                        <div class="main2-child lie2 v5">
                            <i clss="main2-i">⑤</i>
                            <div class="mian2-grandson" id="viewTable0" >
                                发布课表
                            </div>
                        </div>
                        <div class="main2-child lie3 v6">
                            <i clss="main2-i">⑥</i>
                            <div class="mian2-grandson" id="viewTable1">
                                查看课表
                            </div>
                        </div>

                    </div>
                    <div class="main2-div3">
                        <div class="main2-child lie3" style="margin-left:518px;">
                            <div class="mian-grandson" id="adjsutCourse">
                                调课
                            </div>
                        </div>
                    </div>
                </div>
                <div class="right-mian3">
                    <div class="title">走班步骤解释说明:</div>
                    <div class="content">
                        <div class="t1">步骤一：</div>
                        <div class="t2">设置如学期、年级、教师、教室和教学时间等基础数据；</div>
                    </div>
                    <div class="content">
                        <div class="t1">步骤二：</div>
                        <div class="t2">查看学生选课进度，相关教师可以调整学生选课结果；</div>
                    </div>
                    <div class="content">
                        <div class="t1">步骤三：</div>
                        <div class="t2">根据学校的实际情况（1.分段，给年级里所有的班级分成合理的几个分段；2.自动编班；
                            3.设置老师和教室）；</div>
                    </div>
                    <div class="content">
                        <div class="t1">步骤四：</div>
                        <div class="t2">根据学校的实际情况排课（1设置课表结构；2走班排课；3体育课设置；4非走班排课）；</div>
                    </div>
                    <div class="content">
                        <div class="t1">步骤五：</div>
                        <div class="t2">发布课表，可以查看每个班级的课表，并且可以导出课表；</div>
                    </div>
                    <div class="content">
                        <div class="t1">步骤六：</div>
                        <div class="t2">先筛选教学周，按照不同的条件查看课表，分三种课表（1.行政班课表；2教学班课表；3教师课表）；</div>
                    </div>
                    <div class="content">
                        <div class="t1">步骤七：</div>
                        <div class="t2">调课，按照（1.临时周内调课；2.临时跨周调课；3.长期调课）三种类型调课。</div>
                    </div>
                </div>

            </div>
            <div class="right2-main">
                <button class="right2-btn">添加教室</button>
                <table class="right2-tab" id="classRoomShow">

                </table>
                <script type="application/template" id="classRoomTempJs">
                    <tr class="row1">
                        <td class="col1">教室名称(不得重复)</td>
                        <td class="col4">关联班级</td>
                        <td class="col2">备注</td>
                        <td class="col3">操作</td>
                    </tr>
                            {{~it.data:value:index}}
                    <tr class="row">
                        <td class="col1">{{=value.roomName}}</td>
                        <td class="col4">{{=value.className}}</td>
                        <td class="col2">{{=value.remark}}</td>
                        <td class="col3" crid="{{=value.id}}" cnm="{{=value.className}}" cid="{{=value.classId}}"><em class="tab-edit ">编辑</em>&nbsp;|&nbsp;<i class="delete">删除</i></td>
                    </tr>
                    {{~}}
                </script>
                <!--.page-links-->
                <div class="page-paginator">
                    <span class="first-page">首页</span>
                            <span class="page-index">
                            <span class="active">1</span>
                            <span>2</span>
                            <span>3</span>
                            <span>4</span>
                            <span>5</span>
                            <i>···</i>
                            </span>
                    <span class="last-page">尾页</span>
                </div>
                <!--/.page-links-->
            </div>
            <div class="right3-main">
                <div class="cal-term"><span>
                        学年/学期
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

        <!--/.tab-col-->
    </div>
    <!--/.col-right-->

</div>
<!--添加教室弹窗start-->
<div class="hide-add">
    <div class="hide-top">添加教室 <em class="hide-x">×</em></div>
    <div class="hide-1">
        教室名称<input type="text" class="error-border" id="add_name">
    </div>
    <div class="hide-3">
        关联班级
        <select id="classlist">
        </select>
        <script type="application/template" id="classlistTempJs">
            {{~it.data:value:index}}
            <option value="{{=value.id}}">{{=value.className}}</option>
            {{~}}
            <option value="5404a60cf6f28b7261cfad53">其他</option>
        </script>
    </div>
    <div class="hide-2">
        备注<textarea class="input" id="add_remark"></textarea>
    </div>
    <button class="hide-conf-add">确认</button>
    <button class="hide-canc">取消</button>
    <span class="error-span"><em class="error-x">×</em>教室名称已被使用，请重新填写</span>
</div>
<!--添加教室弹窗end-->

<!--编辑弹窗start-->
<div class="hide-edit">
    <input type="hidden" id="classRoomId">
    <div class="hide-top">编辑 <em class="hide-x">×</em></div>
    <div class="hide-1">
        教室名称<input type="text" class="error-border" id="update_name">

    </div>
    <div class="hide-3">
        关联班级
        <select id="classlist2">
        </select>
        <script type="application/template" id="classlistTempJs2">
            {{~it.data:value:index}}
            <option value="{{=value.id}}">{{=value.className}}</option>
            {{~}}
            <option value="5404a60cf6f28b7261cfad53">其他</option>
        </script>
    </div>
    <div class="hide-2">
        备注<textarea class="input" id="update_remark"></textarea>
    </div>
    <button class="hide-conf-update">确认</button>
    <button class="hide-canc">取消</button>
    <span class="error-span"><em class="error-x">×</em>教室名称已被使用，请重新填写</span>
</div>
<!--编辑弹窗end-->
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

<!-- 设置教学周弹窗end -->




<div class="bg"></div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js?v=1"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('changZhengIndex');
</script>
</body>
</html>