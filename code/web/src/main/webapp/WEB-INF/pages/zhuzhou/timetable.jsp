<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<!DOCTYPE html>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <title>3+3走班</title>
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/css/reset.css" rel="stylesheet"/>
    <link href="/static_new/css/zouban/zoubannew.css" rel="stylesheet"/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
    <style>
        table tbody tr td {
            height: 65px !important;
        }
    </style>
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
    <div class="col-right">

        <!--.tab-col右侧-->
        <div class="tab-col">

            <div class="tab-head clearfix">
                <ul>
                    <li id="XZB" class="cur"><a href="javascript:;">行政班课表</a><em></em></li>
                    <li id="LSKB"><a href="javascript:;">老师课表</a><em></em></li>
                    <li id="XSKB"><a href="javascript:;">学生课表</a><em></em></li>
                </ul>
            </div>
            <div class="zouban-title clearfix">
                <div class="title-right">
                    <a href="/zouban/zhuzhou/index.do" class="zouban-back"
                       style="display: inline-block;">&lt;&nbsp;返回走班教务管理</a>
                </div>
            </div>
            <div class="tab-main">
                <!--=================================第五步行政班课表start==================================-->
                <div class="xzb-con" id="tab-XZB">
                    <div class="fifthstep-title clearfix">
                        <div class="fstep-select">
                            <span>年级：<em>高一年级</em></span>
                            <span>行政班:</span>
                            <select id="adminClassSelect">
                                <option value="G1605" selected>G1605</option>
                                <option value="G1609">G1609</option>
                                <option value="G1610">G1610</option>
                            </select>
                        </div>
                    </div>
                    <table class="newTable">
                        <thead>
                        <tr>
                            <th width="20%">上课时间</th>
                            <th width="16%">周一</th>
                            <th width="16%">周二</th>
                            <th width="16%">周三</th>
                            <th width="16%">周四</th>
                            <th width="16%">周五</th>
                        </tr>
                        </thead>
                        <tbody id="classTableCtx">
                        </tbody>
                    </table>
                    <script id="timetableTmpl" type="application/template">
                        {{~it.conf.classTime:value:index}}
                        <tr>
                            <td style="background:#ececec;">
                                <span>第{{=index+1}}节</span>
                            </td>
                            {{~it.conf.classDays:value1:index1}}
                            {{?(index == 6 || index == 7) && (index1 == 1 || index1 == 3)}}
                            <td style="background-color: #5DB870;">
                                <span>
                                    {{=it.course[index][index1].cName}}<br>
                                    {{?it.course[index][index1].tName}}
                                    <em style="color: #004276">{{=it.course[index][index1].tName}}</em><br>
                                    {{=it.course[index][index1].classroom}}
                                    {{?}}
                                </span>
                            </td>
                            {{??}}
                            <td>
                                <span>{{=it.course[index][index1].cName}}<br/>
                                    <em style="color: #004276">{{=it.course[index][index1].tName}}</em></span>
                            </td>
                            {{?}}
                            {{~}}
                        </tr>
                        {{~}}
                    </script>
                </div>
                <!--=================================第五步行政班课表end==================================-->
                <!--=================================第五步老师课表end==================================-->
                <div class="lskb-con" id="tab-LSKB">
                    <div class="fifthstep-title clearfix">
                        <div class="fstep-select">
                            <span>年级：<em>高一年级</em></span>
                            <span>老师:</span>
                            <select id="teacherListCtx">
                            </select>
                            <script id="teacherListTmpl" type="application/template">
                                {{~ it:value:index}}
                                <option value="{{=value}}">{{=value}}</option>
                                {{~}}
                            </script>
                        </div>
                    </div>
                    <table class="newTable">
                        <thead>
                        <tr>
                            <th width="20%">上课时间</th>
                            <th width="16%">周一</th>
                            <th width="16%">周二</th>
                            <th width="16%">周三</th>
                            <th width="16%">周四</th>
                            <th width="16%">周五</th>
                        </tr>
                        </thead>
                        <tbody id="teacherTableCtx">
                        </tbody>
                    </table>
                </div>
                <!--=================================第五步老师课表end==================================-->
                <!--=================================第五步学生课表end==================================-->
                <div class="xskb-con" id="tab-XSKB">
                    <div class="fifthstep-title clearfix">
                        <div class="fstep-select">
                            <span>年级：<em>高一年级</em></span>
                            <span>行政班:</span>
                            <select id="classSelect">
                                <option value="G1605" selected>G1605</option>
                                <option value="G1609">G1609</option>
                                <option value="G1610">G1610</option>
                            </select>
                            <span>学生:</span>
                            <select id="studentsCtx">
                            </select>
                            <script id="studentsTmpl" type="application/template">
                                {{~ it:value:index}}
                                <option value="{{=value.studentId}}">{{=value.userName}}</option>
                                {{~}}
                            </script>
                        </div>
                    </div>
                    <table class="newTable">
                        <thead>
                        <tr>
                            <th width="20%">上课时间</th>
                            <th width="16%">周一</th>
                            <th width="16%">周二</th>
                            <th width="16%">周三</th>
                            <th width="16%">周四</th>
                            <th width="16%">周五</th>
                        </tr>
                        </thead>
                        <tbody id="studentTableCtx">
                        </tbody>
                    </table>
                </div>
                <!--=================================第五步学生课表end==================================-->
        </div>
        <!--/.col-right-->

    </div>
    <!--/#content-->
</div>
</div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js"></script>
<script>
    seajs.use('/static_new/js/modules/zhuzhou/timetable.js', function(timetable) {
        timetable.init();
    });
</script>

</body>
</html>
