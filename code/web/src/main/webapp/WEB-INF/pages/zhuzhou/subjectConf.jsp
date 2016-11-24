<%@ page contentType="text/html; charset=UTF-8" language="java" %>
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
</head>
<body term="${term}" gradeId="${gradeId}" mode="${mode}">


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
                    <li class="cur" id="ZBK"><a href="javascript:;">走班课</a></li>
                    <li id="FZBK"><a href="javascript:;">非走班课</a></li>
                </ul>
            </div>

            <div class="zouban-title clearfix">
                <div class="title-left">
                    <h3>2016~2017学年<em>高一年级</em></h3>
                </div>
                <div class="title-right">
                    <a class="back" href="/zouban/zhuzhou/index.do">&lt;&nbsp;返回走班教务管理</a>
                </div>
            </div>
            <div class="tab-main">
                <!--================走班课start====================-->
                <div id="tab-ZBK" class="tab-ZBK">
                    <div class="ZBK-top">
                        <span class="ZBK-II-XZ h-cursor">+新增学科</span>
                    </div>
                    <div class="ZBK-info">
                        <table>
                            <thead>
                            <tr>
                                <th width="187">学科</th>
                                <th width="187">课时</th>
                                <th width="187">老师</th>
                                <th width="187">教室</th>
                                <th width="187">操作</th>
                            </tr>
                            </thead>
                            <tbody id="ZBSubjectCtx">
                            </tbody>
                            <script id="ZBSubjectTmpl" type="text/template">
                                {{~it:value:index}}
                                <tr>
                                    <td>{{=value.subjectName}}</td>
                                    <td>{{=value.lessonCount}}</td>
                                    <td>{{=value.teacherName}}</td>
                                    <td>{{=value.classroom}}</td>
                                    <td>
                                        <em class="h-cursor edit">编辑</em>|<em class="h-cursor del">删除</em>
                                    </td>
                                </tr>
                                {{~}}
                            </script>
                        </table>
                    </div>
                </div>
                <!--================走班课end====================-->

                <!--================非走班课start=================-->
                <div id="tab-FZBK" class="tab-FZBK">
                    <div class="zouban-addbtn">
                        <ul class="clearfix">
                            <li id="FZBK-I" class="subject-manage zb-active">①学科管理</li>
                            <li id="FZBK-II" class="teacher-set">②老师设置</li>
                        </ul>
                    </div>
                    <div class="fzbk">
                        <!--==============学科管理start=====================-->

                        <div class="FZBK-II" id="tab-FZBK-I">
                            <div class="FZBK-II-top">
                                <span class="FZBK-II-XZ h-cursor">+新增学科</span>
                            </div>
                            <div class="FZBK-info" id="tab-XKGl">
                                <table style="width: 99%">
                                    <thead>
                                    <tr>
                                        <th style="width: 35%">学科</th>
                                        <th style="width: 30%">每周课时数</th>
                                        <th style="width: 35%">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody id="FZBSubjectListCtx">
                                    </tbody>
                                    <script id="FZBSubjectListTmpl" type="text/template">
                                        {{~it:value:index}}
                                        <tr>
                                            <td>{{=value.subjectName}}</td>
                                            <td>{{=value.lessonCount}}</td>
                                            <td>
                                                <em class="h-cursor edit">编辑</em>|<em class="h-cursor del">删除</em>
                                            </td>
                                        </tr>
                                        {{~}}
                                    </script>
                                </table>
                            </div>
                        </div>
                        <!--==============学科管理end=====================-->
                        <!--==============老师设置start=====================-->
                        <div class="FZBK-I" id="tab-FZBK-II">
                            <div class="FZBK-sel">
                                <select id="subjectListCtx">
                                    <option>全部学科</option>
                                </select>
                                <script id="subjectListTmpl" type="text/template">
                                    <option value="">全部学科</option>
                                    {{~it:value:index}}
                                    <option value="{{=value.subjectName}}">{{=value.subjectName}}</option>
                                    {{~}}
                                </script>
                            </div>
                            <div class="FZBK-daoru">
                                <span class="FZBK-SZ h-cursor">一键设置</span>
                            </div>
                            <div class="FZBK-info">
                                <table>
                                    <thead>
                                    <tr>
                                        <th width="125">行政班</th>
                                        <th width="120">人数</th>
                                        <th width="130">学科</th>
                                        <th width="125">每周课时</th>
                                        <th width="125">任课老师</th>
                                        <th width="130">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody id="FZBCourseListCtx">
                                    </tbody>
                                    <script id="FZBCourseListTmpl" type="text/template">
                                        {{~it:value:index}}
                                        <tr>
                                            <td width="125">{{=value.className}}</td>
                                            <td width="120">{{=value.count}}</td>
                                            <td width="130">{{=value.subjectName}}</td>
                                            <td width="125">{{=value.lessonCount}}</td>
                                            <td width="125">{{=value.teacherName}}</td>
                                            <td width="130">
                                                <em class="h-cursor edit">编辑</em></em>
                                            </td>
                                        </tr>
                                        {{~}}
                                    </script>
                                </table>
                            </div>
                        </div>
                        <!--==============老师设置end=====================-->
                    </div>
                </div>
                <!--================非走班可end=================-->

            </div>
        </div>
    </div>
    <!--/.tab-col右侧-->

</div>
<!--/.col-right-->

</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<div class="bg"></div>

<!--=================走班弹窗框start===================-->
<div class="popup-XKGL-ZB">
    <div class="popup-XKGL-top">
        <em>学科管理</em><i class="h-cursor SQ-X zbClose">X</i>
    </div>
    <div class="popup-XKGL-main">
        <dl>
            <div>${term}<em>${gradeName}</em></div>
            <dd>
                <em>学科</em>
                <select id="subjectCtx">
                    <option>语文</option>
                </select>
                <script id="subjectTmpl" type="text/template">
                    {{~it:value:index}}
                    <option value="{{=value.subjectName}}">{{=value.subjectName}}</option>
                    {{~}}
                </script>
            </dd>
            <dd>
                <em>课时数</em>
                <select>
                    <option value="0">0</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                </select>
            </dd>
            <dd>
                <em>老师</em>
                <select id="teacherCtx">
                    <option value="0">0</option>
                </select>
                <script id="teacherTmpl" type="text/template">
                    {{~it:value:index}}
                    <option value="{{=value}}">{{=value}}</option>
                    {{~}}
                </script>
            </dd>
            <dd>
                <em>教室</em>
                <select>
                    <option value="101">A101</option>
                    <option value="102">A102</option>
                    <option value="103">A103</option>
                    <option value="104">A104</option>
                </select>
            </dd>
        </dl>
    </div>
    <div class="popup-XKGL-bottom">
        <span class="XKGL-ZB-TJ h-cursor addZB">确定</span>
        <span class="XKGL-QX h-cursor zbClose">取消</span>
    </div>
</div>
<!--=================走班弹窗框end===================-->

<!--=================非走班弹窗框start===================-->
<div class="popup-XKGL">
    <div class="popup-XKGL-top">
        <em>学科管理</em><i class="h-cursor SQ-X fzbClose">X</i>
    </div>
    <div class="popup-XKGL-main">
        <dl>
            <div>${term}<em>${gradeName}</em></div>
            <dd>
                <em>学科</em>
                <select id="FZBSubjectCtx">
                </select>
            </dd>
            <dd>
                <em>每周课时数</em>
                <select>
                    <option value="0">0</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                </select>
            </dd>
        </dl>
    </div>
    <div class="popup-XKGL-bottom">
        <span class="XKGL-TJ h-cursor addFZB">确定</span>
        <span class="XKGL-QX h-cursor fzbClose">取消</span>
    </div>
</div>
<!--=================非走班弹窗框end===================-->



<!-- Javascript Files -->
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js"></script>
<script>
    seajs.use('/static_new/js/modules/zhuzhou/subjectConf.js', function (subjectConf) {
        subjectConf.init();
    });
</script>

</body>
</html>
