<%--
  Created by IntelliJ IDEA.
  User: wangkaidong
  Date: 2016/10/12
  Time: 11:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>3+3走班-调课</title>
    <meta charset="utf-8">
    <!-- css files -->
    <link href="/static_new/css/reset.css" rel="stylesheet"/>
    <link href="/static_new/css/zouban/zoubannew.css" rel="stylesheet"/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
</head>

<body year="${year}" term="${term}" gradeId="${gradeId}" gradeName="${gradeName}" allweek="${allweek}" curweek="${curweek}"
      onbeforeunload="closeOrRefresh()">
<script type="text/javascript">
    function closeOrRefresh() {
        var term = $('body').attr('term');
        $.ajax({
            type: "GET",
            data: {term: term},
            url: '/zouban/tiaoke/test.do',
            async: false,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (rep) {

            }
        });
    }
</script>
<!--#head-->
<%@ include file="../../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">
    <!--.col-left-->
    <%@ include file="../../common_new/col-left.jsp" %>
    <!--/.col-left-->
    <div class="col-right">
        <!--.col-right-->
        <div class="tab-col">
            <div class="tab-head clearfix">
                <ul>
                    <li class="cur"><a href="javascript:;">调课</a><em></em></li>
                </ul>
            </div>
            <div class="fourstep-title clearfix">
                <span class="fstep-inf">${term}<em>${gradeName}</em></span>
                <a href="javascript:;" class="fourstep-back">&lt;&nbsp;返回走班教务管理</a>
            </div>
            <div class="tab-main">
                <div id="tab-TKJL">
                    <div style="margin-bottom: 20px;">
                        <button class="save-btn" id="addNotice">新增</button>
                    </div>
                    <table class="newTable" id="newTable">
                        <thead>
                        <tr>
                            <th style="width:20%;">课题申请人</th>
                            <th style="width:20%;">调课类型</th>
                            <th style="width:20%;">调课发布时间</th>
                            <th style="width:20%;">发布状态</th>
                            <th style="width:20%;">操作</th>
                        </tr>
                        </thead>
                        <tbody id="tiokeCtx"></tbody>
                        <script id="tiokeCtxTmpl" type="text/template">
                            {{~it:Item:index}}
                            <tr>
                                <td>{{=Item.userName}}</td>
                                <td>{{=Item.typeStr}}</td>
                                <td>{{=Item.publishTime}}</td>
                                <td>{{=Item.stateStr}}</td>
                                <td>
                                    <em class="TKTC {{?Item.state == 0}}adjust{{??}}not-adjust{{?}}" noticeId="{{=Item.id}}">调课</em> |
                                    <em class="detail" noticeId="{{=Item.id}}">详情</em>
                                    |<em noticeState="{{=Item.state}}"  class="DCTK {{?Item.state == 0}}not-adjust{{??}}adjust{{?}}" noticeId="{{=Item.id}}">导出</em>
                                </td>
                            </tr>
                            {{~}}
                        </script>
                    </table>
                    <div class="new-page-links"></div>
                </div>
                <div id="tab-TKTK">
                    <div class="adjust-title clearfix">
                        <div style="margin-bottom: 20px;">
                            <label style="margin-right: 20px;">时间</label> <span id="startWeek"></span>~  <span id="endWeek"></span>
                            <a href="javascript:;" class="adjust-back">&lt;&nbsp;返回</a>
                        </div>
                        <div class="adjust-left">
                            <span>班级</span>
                            <select id="classListCtx"></select>
                            <script id="classListTmpl" type="application/template">
                                {{~ it:value:i }}
                                <option value="{{=value.id}}">{{=value.className}}</option>
                                {{~ }}
                            </script>
                        </div>
                        <div class="adjust-right">
                            <button class="save-btn" id="saveChange" noticeId="">保存并发布</button>
                            <button class="alert-btn-qx" id="cancelChange">取消并还原</button>
                        </div>
                    </div>
                    <div class="fzbk-con" id="tab-FZBK" style="margin-top: 20px;">
                        <table class="tk-table" id="classTable"></table>
                        <script id="classTableTmpl" type="text/template">
                            <thead>
                            <tr>
                                <th style="width: 15%;">上课时间</th>
                                {{~it.days:day:index}}
                                <th style="width:17%;">{{=day}}</th>
                                {{~}}
                            </tr>
                            </thead>
                            <tbody>

                            {{~it.sections:section:index}}
                            <tr>
                                <td style="background:#ececec;">
                                    <span class="class-turn">{{=section}}</span>{{=it.classTime[index]}}
                                </td>
                                {{~it.days:day:index2}}
                                <td class="{{?it.courseItemList[index][index2].type == 2 }}tiaoke-available-before{{??}}tiaoke-notavailable{{?}}"
                                    x="{{=index2 + 1}}" y="{{=index + 1}}"
                                    teacherId="{{=it.courseItemList[index][index2].teacherId}}"
                                    courseid="{{=it.courseItemList[index][index2].courseId}}"
                                    courseItemId="{{=it.courseItemList[index][index2].courseItemId}}">
                                    <%--{{?it.courseItemList[index][index2].type==1}}
                                    {{=it.courseItemList[index][index2].className}}--%>
                                    {{?it.courseItemList[index][index2].type == 2 ||
                                    it.courseItemList[index][index2].type == 8}}
                                    {{=it.courseItemList[index][index2].className}}<br/>{{=it.courseItemList[index][index2].teacherName}}<br/>
                                    <%--{{??it.courseItemList[index][index2].type==4}}
                                    {{=it.courseItemList[index][index2].className}}
                                    {{??it.courseItemList[index][index2].type==7}}
                                    {{=it.courseItemList[index][index2].className}}
                                    {{??it.courseItemList[index][index2].type==8}}
                                    {{=it.courseItemList[index][index2].className}}<br/>{{=it.courseItemList[index][index2].teacherName}}<br/>--%>
                                    {{??}}
                                    {{=it.courseItemList[index][index2].className}}
                                    {{?}}
                                </td>
                                {{~}}
                            </tr>
                            {{~}}
                            </tbody>
                        </script>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../../common_new/foot.jsp" %>
<!--#foot-->

<div class="bg"></div>

<!--=================================新增调课弹窗start==================================-->
<div class="adjust-save">
    <div class="alert-title clearfix">
        <p>新增调课</p>
        <span class="alert-close">X</span>
    </div>
    <div class="alert-main">

        <div class="alert-main-title" style="font-weight: normal;text-align: left;">
            <div class="alert-first">
                <span>调课申请人</span><input id="userName" type="text" style="width: 200px;">
            </div>
            <div class="alert-sec">
                <span>调课原因说明</span><textarea id="description" rows="3" style="width: 200px;"></textarea>
            </div>
            <div class="alert-third">
                <span>时间</span>
                <select id="startWeekCtx" style="width: 90px;"></select>
                <em>~</em>
                <select id="endWeekCtx" style="width: 90px;"></select>
                <script id="weekSelectTmpl" type="application/template">
                    {{~it:week:index}}
                    <option value="{{=week}}">第{{=week}}周</option>
                    {{~}}
                </script>
            </div>
        </div>
        <div class="alert-btn">
            <button class="alert-btn-save" id="saveNotice">保存</button>
            <button class="alert-btn-qx">取消</button>
        </div>
    </div>
</div>
<!--=================================新增调课弹窗end==================================-->

<!--=================================调课记录详情弹窗start==================================-->
<div class="adjust-alert">
    <div class="alert-title clearfix">
        <p>调课详情</p>
        <span class="alert-close">X</span>
    </div>
    <div class="alert-main">
        <div class="alert-main-title" style="font-weight: normal;">
            <span class="adjust-fir">调课申请人：<em>张思德</em></span>
            <span class="adjust-sec">调课原因：<em>请假</em></span>
        </div>
        <table class="alert-table">
            <thead>
            <tr>
                <th style="width:14%;">老师</th>
                <th style="width:18%;">班级</th>
                <th style="width:18%;">课程</th>
                <th style="width:25%;">原上课时间</th>
                <th style="width:25%;">新上课时间</th>
            </tr>
            </thead>
            <tbody id="noticeDetailCtx">
            </tbody>
            <script id="noticeDetailTmpl" type="text/template">
                {{~it:Item:index}}
                <tr>
                    <td>{{=Item.teacherName}}</td>
                    <td>{{=Item.className}}</td>
                    <td>{{=Item.courseName}}</td>
                    <td>{{=Item.oldTime}}</td>
                    <td>{{=Item.newTime}}</td>
                </tr>
                {{~}}
            </script>
        </table>
    </div>
</div>
<!--=================================调课记录详情弹窗end==================================-->


<!-- Javascript Files -->
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js"></script>
<script>
    seajs.use('tiaoke', function (tiaoke) {
        tiaoke.init();
    });
</script>
</body>
</html>
