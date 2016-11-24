<%--
  Created by IntelliJ IDEA.
  User: wang_xinxin
  Date: 2015/10/16
  Time: 17:52
  To change this template use File | Settings | File Templates.
--%>
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
    <link href="/static_new/css/zouban/chooselesson.css?v=2015041602" rel="stylesheet"/>

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

    <div class="col-right">
        <input hidden="hidden" id="xuankeid">
        <input type="hidden" id="mode" value="${mode}">

        <div class="tab-col">
            <div class="tab-top clearfix">
                <ul>
                    <li class="cur"><a href="javascript:;">选课进度</a></li>
                </ul>
                <a class="backUrl tab-back" href="javascript:;">&lt;返回教务管理首页</a>
            </div>
            <!--===========================调整学生选课 start================================-->
            <div class="tab-schedule">
                <dl>
                    <dd>
                        <span id="term">${term}</span>
                        <em>年级</em>
                        <%--<select id="gradeShow">--%>
                        <%--<c:forEach items="${gradelist}" var="g">--%>
                        <%--<option value="${g.id}">${g.name}</option>--%>
                        <%--</c:forEach>--%>
                        <%--</select>--%>
                        <span id="gradeShow" gid="${gradeId}">${gradeName}</span>
                        <%--<button id="seachxk">筛选</button>--%>
                    </dd>
                    <dd>
                        <span>开放时间：</span>
                        <span id="startdt"></span>
                        <span>-</span>
                        <span id="enddt"></span>
                    </dd>
                    <dd>
                    <span>未完成选课：
                        <i id="xkcnt"></i>
                    </span>
                    <span>
                        <%--<button class="autoXuanke">模拟选课</button>--%>
                        <button class="tab-schedule-TZ">调整学生选课</button>
                    </span>
                    </dd>
                    <dd>
                        <table id="subjectlist">
                        </table>
                        <script type="application/template" id="subjectConfTempJs">
                            <tr>
                                <th>科目</th>
                                <th>等级考</th>
                                <th>合格考</th>
                                <th>说明</th>
                                <th>明细</th>
                            </tr>
                            {{~it.data:value:index}}
                            <tr>
                                <td>{{=value.subjectName}}</td>
                                <td>{{=value.advUserCount}}</td>
                                <td>{{=value.simUserCount}}</td>
                                <td class="tab-schedule-SM">{{=value.explain}}</td>
                                <td class="viewdetail" scid="{{=value.subjectConfId}}" sid="{{=value.subjectId}}">查看
                                </td>
                            </tr>
                            {{~}}
                        </script>
                    </dd>
                </dl>
            </div>
            <!--===========================调整学生选课 end================================-->
            <!--============================搜索 start=================================-->
            <div class="tab-Sschedule">
                <dl>
                    <dt>
                        <span id="backBtn">&lt;返回</span>
                    </dt>
                    <dd>
                        <span id="term3"></span>/
                        <em id="grade3"></em>
                        <a href="../paike/importExcel.do" style="  float: right;color: #ff0000;">导入选课结果</a>
                    </dd>
                    <dd>
                        <span>行政班</span>
                        <select id="classlist2">
                        </select>
                        <script type="application/template" id="classlistTempJs2">
                            {{~it.data:value:index}}
                            <option value="{{=value.id}}">{{=value.className}}</option>
                            {{~}}
                        </script>
                        <select id="choosetype">
                            <option value="0">未完成选课</option>
                            <option value="1">完成选课</option>
                        </select>
                        <input id="username">
                        <button id="seachuser">搜索</button>
                    </dd>
                    <dd>
                        <table id="subjectlist2">
                        </table>
                        <script type="application/template" id="subjectConfTempJs2">
                            <tr>
                                <th>学生姓名</th>
                                <th>所属行政班</th>
                                <th>等级考</th>
                                <th>合格考</th>
                                <th>操作</th>
                            </tr>
                            {{~it.data:value:index}}
                            <tr>
                                <td>{{=value.username}}</td>
                                <td>{{=value.className}}</td>
                                <td>{{=value.advName}}</td>
                                <td>{{=value.simName}}</td>
                                <td class="xuankeTd" uid="{{=value.userid}}" style="cursor: pointer">选课</td>
                            </tr>
                            {{~}}
                        </script>
                    </dd>
                </dl>
            </div>
            <!--============================搜索 end=================================-->
            <!--=============================选课进度明细 start=========================-->
            <div class="tab-Xschedule">
                <dl>
                    <dt>
                        <span id="backBtn2">&lt;返回</span>
                    </dt>
                    <dd>
                        <span id="term2"></span>/
                        <em id="grade2"></em>
                    </dd>
                    <dd>
                        <span>科目</span>
                        <select id="subjectmaps">
                        </select>
                        <script type="application/template" id="subjectmapTempJs">
                            {{~it.data:value:index}}
                            <option value="{{=value.subjectId}}">{{=value.subjectName}}</option>
                            {{~}}
                        </script>
                        <span>行政班</span>
                        <select id="classlist">
                        </select>
                        <script type="application/template" id="classlistTempJs">
                            <option value="">全部</option>
                            {{~it.data:value:index}}
                            <option value="{{=value.id}}">{{=value.className}}</option>
                            {{~}}
                        </script>
                        <button id="getXuankeDetail">筛选</button>
                    </dd>
                    <dd>
                        <table id="stulist">
                        </table>
                        <script type="application/template" id="stulistTempJs">
                            <tr>
                                <th>课程</th>
                                <th>学生姓名</th>
                                <th>性别</th>
                                <th>所属行政班</th>
                                <th>操作</th>
                            </tr>
                            {{~it.data:value:index}}
                            <tr>
                                <td>{{=value.courseName}}</td>
                                <td>{{=value.username}}</td>
                                <td>{{?value.sex==0}}女{{??}}男{{?}}</td>
                                <td>{{=value.className}}</td>
                                <td class="TZxuanke" uid="{{=value.userid}}" style="cursor: pointer">调整选课</td>
                            </tr>
                            {{~}}
                        </script>
                    </dd>
                </dl>
            </div>
            <!--=============================选课进度明细 end=========================-->
        </div>
    </div>
    <!--背景 start-->
    <div class="bg"></div>
    <!--背景 end-->
    <!--选课弹出框 start-->
    <div class="schedule-CUR">
        <input id="userId" type="hidden">

        <div class="schedule-CUR-top">
            <em>选课</em>
            <i class="update-X">X</i>
        </div>

        <dl id="tableShow">
        </dl>
        <script type="application/template" id="tempJs">
            <dt>
                <span>{{=it.className}}&nbsp;&nbsp;&nbsp;&nbsp;</span>/<span>&nbsp;&nbsp;&nbsp;&nbsp;{{=it.stuName}}</span>
            </dt>
            <dd>
                <em>已选课程</em>
            </dd>
            <dd>
                <em>等级考课程:</em>

                <div>
                    {{~it.data.adv:value:index}}
                    <button>{{=value.value}}</button>
                    {{~}}
                </div>
            </dd>
            <dd>
                <em>合格考课程:</em>

                <div>
                    {{~it.data.sim:value:index}}
                    <button>{{=value.value}}</button>
                    {{~}}
                </div>
            </dd>
            <dd>
                <em>修改课程</em>
            </dd>
            <%--<dd>
              <em>需要选择<i>{{=it.data.conf.advanceCount}}</i>门等级考科目</em>
              <em>需要选择<i>{{=it.data.conf.simpleCount}}</i>门合格考科目</em>
            </dd>--%>
            <dd>
                <table class="choose-tctb">
                    <tr>
                        <th>科目名称</th>
                        <th>等级考</th>
                        <th>合格考</th>
                        <th>说明</th>
                    </tr>
                    {{~it.data.conf.subConfList:value:index}}
                    <tr sid="{{=value.subjectId}}">
                        <td>{{=value.subjectName}}</td>
                        <td>
                            {{?$.inArray(value.subjectName,it.advChoose)>-1}}
                            <span class="schedule-CUR-hov advcourse">已选</span>
                            {{??}}
                            <span class="advcourse">我选</span>
                            {{?}}
                        </td>
                        <td>
                            {{?$.inArray(value.subjectName,it.simChoose)>-1}}
                            <span class="schedule-CUR-hov simcourse">已选</span>
                            {{??}}
                            {{?value.simpleTime==0}}
                            <span style="width: 38px;margin: auto;">-----</span>
                            {{??}}
                            <span class="simcourse">我选</span>
                            {{?}}
                            {{?}}
                        </td>
                        <td>{{=value.explain}}</td>
                    </tr>
                    {{~}}

                </table>
            </dd>
            <dd>
                <div class="schedule-CUR-bottom">
                    <button class="submitXuanke">确定</button>
                    <button class="schedule-CUR-BU">取消</button>
                </div>
            </dd>
        </script>
    </div>
    <!--选课弹出框 end-->
    <!--/.col-right-->
</div>
<!--/#content-->
<!--=======================================弹出层背景 start========================================-->
<div class="bg"></div>
<!--=======================================弹出层背景 end========================================-->
<!--#foot-->
<%@ include file="../../common_new/foot.jsp" %>
<!--#foot-->

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js?v=1"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('chooselesson');
</script>
</body>
</html>
