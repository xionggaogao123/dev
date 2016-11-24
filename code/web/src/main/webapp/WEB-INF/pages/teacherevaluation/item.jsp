<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2016/4/27
  Time: 13:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <title>复兰科技-教师评价系统</title>
    <meta name="renderer" content="webkit">
    <link rel="stylesheet" type="text/css" href="/static_new/css/teacherevaluation/evaluation.css">
    <link rel="stylesheet" type="text/css" href="/static_new/css/reset.css">
    <link href="/static_new/js/modules/ueditor/themes/default/css/ueditor.css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="/static/js/select2/select2.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>
<body evid="${evaluationId}">
<input id="leader" type="text" value="${leader}" hidden="true"/>
<input id="manager" type="text" value="${manager}" hidden="true"/>
<input id="member" type="text" value="${member}" hidden="true"/>
<input id="teacher" type="text" value="${teacher}" hidden="true"/>
<input id="timeState" type="text" value="${timeState}" hidden="true"/>
<input id="userId" type="text" value="${sessionValue.id}" hidden="true"/>

<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

    <!--.col-left-->
    <%@ include file="../common_new/col-left.jsp" %>
    <!--/.col-left-->

    <!--广告-->
    <c:choose>
        <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
        </c:when>
        <c:otherwise>
            <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
        </c:otherwise>
    </c:choose>

    <!--.col-right-->
    <div class="col-right clearfix">

        <!--.tab-col-->
        <div class="right-main">
            <ul class="top-nav clearfix">
                <li class="cur-li">考核打分</li>
                <li style="display: none">历史考核</li>
                <c:if test="${teacher==1}">
                    <li style="display: none">我的成绩</li>
                </c:if>
            </ul>
            <div class="right-cont right-cont1">
                <div class="green-nav layout-nav clearfix">
                    <c:if test="${manager==1}">
                        <span class="span3">填写实证资料<em>></em></span>
                    </c:if>
                    <c:if test="${(teacher==1 || leader==1) && state == 1}">
                        <span class="span4">查看实证资料<em>></em></span>
                    </c:if>
                    <c:if test="${teacher==1 && state == 1}">
                        <span class="span1">编辑个人陈述<em>></em></span>
                    </c:if>
                    <c:if test="${leader==1}">
                        <span class="span2">查看个人陈述<em>></em></span>
                    </c:if>
                    <%--<c:if test="${teacher==1}">--%>
                    <%--<span class="span5">组内互评<em>></em></span>--%>
                    <%--</c:if>--%>
                    <%--<c:if test="${leader==1}">--%>
                    <%--<span class="span6">查看组内互评<em>></em></span>--%>
                    <%--</c:if>--%>
                    <c:if test="${leader==1 || member==1 || teacher==1}">
                        <span class="span7">考核打分<em>></em></span>
                    </c:if>
                    <span class="span8">查看成绩与排名</span>
                </div>
                <div class="px-o div-px">
                    <h3>本次评选规则</h3>

                    <div class="div-gz" id="rule">
                    </div>
                </div>
                <div class="px1 div-px">
                    <p><em class="em-back">
                        <返回
                    </em></p>
                    <h4>个人陈述</h4>

                    <div class="personal">
                        <textarea placeholder="请在这里写下个人陈述" id="teacherStatement"></textarea>
                    </div>
                    <button id="updateTeacherStatement" class="btn-save">保存</button>
                </div>
                <div class="px2 div-px">
                    <p><em class="em-back">
                        <返回
                    </em></p>
                    <h4>个人陈述
                        <select class="groupNames">
                            <%--<option>语文组</option>--%>
                            <%--<option>数学组</option>--%>
                        </select>
                        <script id="groupNamesTmpl" type="text/template">
                            {{ for(var i in it) { }}
                            <option value="{{=it[i].groupId}}">{{=it[i].groupName}}</option>
                            {{ } }}
                        </script>
                    </h4>
                    <div class="member2 teachers clearfix">
                        <%--<span class="span-green">张三</span>--%>
                        <%--<span>张三</span>--%>
                    </div>
                    <script id="membersTmpl" type="text/template">
                        {{ for(var i in it) { }}
                        {{if(it[i].value == 1){ }}
                        <span id="{{=it[i].id}}">{{=it[i].name}}</span>
                        {{}}}
                        {{ } }}
                    </script>
                    <div class="personal">
                        <p id="title">老师的个人陈述</p>

                        <p id="statement"></p>
                    </div>
                </div>
                <div class="px3 div-px">
                    <p><em class="em-back">
                        <返回
                    </em></p>
                    <h4>填写实证资料
                        <select class="groupNames">
                            <%--<option>语文组</option>--%>
                            <%--<option>数学组</option>--%>
                        </select>
                    </h4>

                    <p class="p-px">评选名单
                        <span class="text-orange">*橙色框线内为已提交资料教师</span>
                    </p>

                    <div class="member2 teachers clearfix">
                        <span class="span-green">张三</span>
                        <span>张三</span>
                        <span class="border-blue">张三</span>
                    </div>
                    <p class="p-sz">实证资料</p>

                    <div class="personal">
                        <script type="text/plain" id="evidenceEditor"></script>
                    </div>
                    <button class="btn-save" id="saveEvidence">保存</button>
                </div>
                <div class="px4 div-px">
                    <p><em class="em-back">
                        <返回
                    </em></p>
                    <h4>实证资料
                        <c:if test="${leader==1}">
                            <select class="groupNames">
                                    <%--<option>高一组</option>--%>
                            </select>
                        </c:if>
                    </h4>
                    <c:if test="${leader==1}">
                        <p class="p-px">评选名单
                            <span class="text-orange">*橙色框线内为已提交资料教师</span>
                        </p>

                        <div class="member2 teachers clearfix">
                                <%--<span class="span-green">张三</span>--%>
                                <%--<span>张三</span>--%>
                                <%--<span class="border-blue">张三</span>--%>
                        </div>
                    </c:if>
                    <div class="personal" id="evidenceForLeader">
                        <%--实证资料内容--%>
                    </div>
                </div>
                <div class="px5 div-px clearfix">
                    <p><em class="em-back">
                        <返回
                    </em></p>
                    <h4>组内互评</h4>

                    <p class="p-px">评选名单
                        <span class="text-orange">*橙色框线内为已打分教师</span>
                    </p>

                    <div class="member2 clearfix" id="myGroupTeachers">
                        <%--<span class="span-green">张三</span>--%>
                        <%--<span>张三</span>--%>
                        <%--<span class="border-orange">张三</span>--%>
                    </div>
                    <script id="myGroupTeachersTmpl" type="text/template">
                        {{ for(var i in it) { }}
                        <span id="{{=it[i].id}}">{{=it[i].value}}</span>
                        {{ } }}
                    </script>
                    <div class="pb-cont clearfix">
                        <p class="p-sz">实证材料</p>

                        <div class="tea-cs" id="evi">
                            <%--实证材料实证材料实证材料实证材料--%>
                        </div>
                        <p class="p-sz">教师陈述</p>

                        <div class="tea-cs" id="teacherState">
                            <%--教师陈述教师陈述教师陈述--%>
                        </div>
                    </div>
                    <table class="table-df">
                        <thead>
                        <tr>
                            <th>评比项目</th>
                            <th>分数</th>
                        </tr>
                        </thead>
                        <tbody class="kaohe">
                        <%--<tr>--%>
                        <%--<td>德</td>--%>
                        <%--<td>--%>
                        <%--<input type="text">--%>
                        <%--</td>--%>
                        <%--</tr>--%>
                        <%--<td>廉</td>--%>
                        <%--<td>--%>
                        <%--<div class="div-orange">99</div>--%>
                        <%--</td>--%>
                        <%--</tr>--%>
                        </tbody>
                        <script id="kaoheTmpl" type="text/template">
                            {{ for(var i in it) { }}
                            <tr id="{{=it[i].id}}">
                                <td>{{=it[i].name}}</td>
                                <td>
                                    {{var grades = getGrades();}}
                                    <table class="score" val="">
                                        <tr>
                                            {{ for(var j=0; j<4; j++){ }}
                                            <td>{{=grades[j].name}}</td>
                                            {{ } }}
                                        </tr>
                                        <tr>
                                            {{ for(var j=0; j<4; j++){ }}
                                            <td>
                                                <select class="select">
                                                    {{ for(var k=upInt(it[i].score, grades[j].begin/100); k<=
                                                    downInt(it[i].score, grades[j].end/100); k++) { }}
                                                    <option value="{{=k}}">{{=k}}</option>
                                                    {{}}}
                                                </select>
                                            </td>
                                            {{ } }}
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            {{ } }}
                        </script>
                        <tr>
                            <td colspan="2" style="border:none;">
                                <button class="btn-save1" id="saveHuPingScore">保存</button>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="px6 div-px">
                    <p><em class="em-back">
                        <返回
                    </em></p>
                    <h4>组内互评
                        <select class="groupNames">
                            <%--<option>语文组</option>--%>
                            <%--<option>数学组</option>--%>
                        </select>
                    </h4>

                    <p class="p-px">评选名单
                        <span class="text-orange">*橙色框线内为已打分教师</span>
                    </p>

                    <div class="member2 teachers clearfix">
                        <%--<span class="span-green">张三</span>--%>
                        <%--<span>张三</span>--%>
                        <%--<span class="border-orange">张三</span>--%>
                    </div>
                    <table class="table-score">
                        <thead>
                        <tr class="kaoheForLeader">
                            <%--<th>评分老师姓名</th>--%>
                            <%--<th>德</th>--%>
                            <%--<th>能</th>--%>
                            <%--<th>勤</th>--%>
                            <%--<th>绩</th>--%>
                            <%--<th>廉</th>--%>
                        </tr>
                        </thead>
                        <script id="kaoheForLeaderTmpl" type="text/template">
                            <th>评分老师姓名</th>
                            {{ for(var i in it) { }}
                            <th>{{=it[i].name}}</th>
                            {{ } }}
                        </script>
                        <tbody id="huping">
                        <%--<tr>--%>
                        <%--<td>张三</td>--%>
                        <%--<td>88</td>--%>
                        <%--<td>88</td>--%>
                        <%--<td>88</td>--%>
                        <%--<td>88</td>--%>
                        <%--<td>88</td>--%>
                        <%--</tr>--%>
                        </tbody>
                        <script id="hupingTmpl" type="text/template">
                            {{ for(var i in it) { }}
                            <tr>
                                <td>{{=it[i].tname}}</td>
                                {{var scores = it[i].scores;}}
                                {{ for(var j in scores){ }}
                                <td>{{=scores[j].value}}</td>
                                {{} }}
                            </tr>
                            {{ } }}
                        </script>
                    </table>
                </div>
                <div class="px7 div-px clearfix">
                    <p><em class="em-back">
                        <返回
                    </em></p>
                    <h4>考核打分
                        <select class="groupNames groupNames1">
                            <%--<option>语文组</option>--%>
                            <%--<option>数学组</option>--%>
                        </select>
                    </h4>
                    <div style="font-size:16px;color: red">提醒：在给每位教师评价时，请先点击教师姓名，查看其实证材料和个人陈述，然后再评价。</div>
                    <div style="width:729px;overflow:auto;">
                        <table class="table-df" style="min-width:727px;!important;">
                            <thead id="elements">
                            <%--<tr>--%>
                                <%--<th class="th-name">姓名</th>--%>
                                <%--<th class="th-num">考核一</th>--%>
                            <%--</tr>--%>
                            </thead>
                            <script id="elementsTmpl" type="text/template">
                                <tr>
                                    <th class="th-name">姓名</th>
                                    {{~ it:value:index}}
                                    <th class="th-num">{{=value.name}}</th>
                                    {{~ }}
                                </tr>
                            </script>
                            <tbody id="teachers">
                            <%--<tr>--%>
                                <%--<td>张三</td>--%>
                                <%--<td>--%>
                                   <%--<input type="text">--%>
                                <%--</td>--%>
                            <%--</tr>--%>
                            <%--<tr>--%>
                                <%--<td>李四</td>--%>
                                <%--<td>--%>
                                    <%--<div class="div-orange">99</div>--%>
                                <%--</td>--%>
                            <%--</tr>--%>
                            </tbody>
                            <script id="teachersTmpl" type="text/template">
                                {{~ it.list:value:index}}
                                <tr tid="{{=value.teacherId}}" class="teacher">
                                    <td><div style="width:60px;cursor: pointer" class="ls-name">{{=value.teacherName}}</div></td>
                                    {{=it.tmpl}}
                                </tr>
                                {{~ }}
                            </script>
                        </table>
                    </div>
                    <button class="btn-save1" id="saveKaoHeScore0" style="margin-left: 40%;">临时保存</button>
                    <button class="btn-save1" id="saveKaoHeScore1" style="margin-left: 10%;">评完提交</button>
                </div>
                <div class="px8a div-px">
                    <p><em class="em-back1">
                        <返回
                    </em></p>
                    <h4>总成绩与排名
                        <c:if test="${leader==1}">
                            <button id="calculate">计算考核成绩和最新排名</button>
                            <button id="integrity">检查打分完整性</button>
                        </c:if>
                    </h4>
                    <table class="table-pm">
                        <thead>
                        <tr>
                            <th>老师姓名</th>
                            <th class="th2">所在小组</th>
                            <th>年度考核成绩</th>
                            <th>组内排名</th>
                            <th>等次</th>
                            <%--<th hidden="true">操作</th>--%>
                        </tr>
                        </thead>
                        <tbody id="ranking">
                        <%--<tr>--%>
                        <%--<td>阿斯顿</td>--%>
                        <%--<td>高一组</td>--%>
                        <%--<td>59</td>--%>
                        <%--<td>1</td>--%>
                        <%--<td><span class="check">查看</span></td>--%>
                        <%--</tr>--%>
                        </tbody>
                        <script id="rankingTmpl" type="text/template">
                            {{ for(var i in it) { }}
                            <tr>
                                <td>{{=it[i].teacherName}}</td>
                                <td>{{=it[i].groupName}}</td>
                                <td>{{=it[i].finalStdScore}}</td>
                                <td>{{=it[i].ranking}}</td>
                                <td>{{=it[i].gradeName}}</td>
                                <%--<td><span class="check" tid="{{=it[i].teacherId}}">查看</span></td>--%>
                            </tr>
                            {{ } }}
                        </script>
                        <script id="rankingTmpl1" type="text/template">
                            {{ for(var i in it) { }}
                            <tr>
                                <td>{{=it[i].teacherName}}</td>
                                <td>{{=it[i].groupName}}</td>
                                <td>{{=it[i].gradeName}}</td>
                                <%--<td>{{=it[i].ranking}}</td>--%>
                            </tr>
                            {{ } }}
                        </script>
                    </table>
                </div>
                <div class="px8b div-px">
                    <p><em class="em-back2">
                        <返回成绩总列表
                    </em></p>
                    <h4>个人评分</h4>
                    <table class="table-person">
                        <thead>
                        <tr id="kaoheForDetail">
                            <%--<th class="th1">分数类型</th>--%>
                            <%--<th>德</th>--%>
                            <%--<th>能</th>--%>
                            <%--<th>勤</th>--%>
                            <%--<th>绩</th>--%>
                            <%--<th>廉</th>--%>
                            <%--<th>操作</th>--%>
                        </tr>
                        </thead>
                        <script id="kaoheForDetailTmpl" type="text/template">
                            <th class="th1">分数类型</th>
                            {{ for(var i in it) { }}
                            <th eid="{{=it[i].id}}">{{=it[i].name}}</th>
                            {{ } }}
                            <th>操作</th>
                        </script>
                        <tbody>
                        <tr id="HP">
                            <td>组内互评</td>
                            <%--<td>99</td>--%>
                            <%--<td>99</td>--%>
                            <td><span class="check1 check">详情</span></td>
                        </tr>
                        <tr id="LD">
                            <td>考核小组领导</td>
                            <%--<td>99</td>--%>
                            <%--<td>99</td>--%>
                            <td><span class="check2 check">详情</span></td>
                        </tr>
                        <tr id="GP">
                            <td>考核小组成员</td>
                            <%--<td>99</td>--%>
                            <%--<td>99</td>--%>
                            <td><span class="check3 check">详情</span></td>
                        </tr>
                        <tr id="total">
                            <td>总分</td>
                            <%--<td>99</td>--%>
                            <%--<td>99</td>--%>
                            <td></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="px8c div-px">
                    <p><em class="em-back3">
                        <返回个人评分
                    </em></p>
                    <h4>组内互评</h4>
                    <table class="table-zu">
                        <thead>
                        <tr class="kaoheForLeader">
                            <th class="th1">评分老师姓名</th>
                            <%--<th>德</th>--%>
                            <%--<th>能</th>--%>
                            <%--<th>勤</th>--%>
                            <%--<th>绩</th>--%>
                            <%--<th>廉</th>--%>
                        </tr>
                        </thead>
                        <tbody id="huping1">
                        <%--<tr>--%>
                        <%--<td>曹总</td>--%>
                        <%--<td>99</td>--%>
                        <%--<td>99</td>--%>
                        <%--<td>99</td>--%>
                        <%--<td>99</td>--%>
                        <%--<td>99</td>--%>
                        <%--</tr>--%>
                        </tbody>
                    </table>
                </div>
                <div class="px8d div-px">
                    <p><em class="em-back3">
                        <返回个人评分
                    </em></p>
                    <h4>领导小组评分</h4>
                    <table class="table-zu">
                        <thead>
                        <tr class="kaoheForLeader">
                            <th class="th1">评分老师姓名</th>
                            <%--<th>德</th>--%>
                            <%--<th>能</th>--%>
                            <%--<th>勤</th>--%>
                            <%--<th>绩</th>--%>
                            <%--<th>廉</th>--%>
                        </tr>
                        </thead>
                        <tbody id="leadergroup">
                        <%--<tr>--%>
                        <%--<td>曹总</td>--%>
                        <%--<td>99</td>--%>
                        <%--<td>99</td>--%>
                        <%--<td>99</td>--%>
                        <%--<td>99</td>--%>
                        <%--<td>99</td>--%>
                        <%--</tr>--%>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="right-cont right-cont2">
                <div class="ssi clearfix">
                    <select class="select1" id="years">
                        <%--<option>2015-2016</option>--%>
                    </select>
                    <input type="text" placeholder="老师姓名" id="search">
                </div>
                <table class="table-his">
                    <thead>
                    <tr>
                        <th class="th1">老师姓名</th>
                        <th class="th2">所在小组</th>
                        <th class="th3">标准成绩</th>
                        <th class="th4">总排名</th>
                    </tr>
                    </thead>
                    <tbody id="history">
                    <%--<tr>--%>
                    <%--<td>事实上</td>--%>
                    <%--<td>高一组</td>--%>
                    <%--<td>200</td>--%>
                    <%--<td>2</td>--%>
                    <%--</tr>--%>
                    </tbody>
                    <script id="historyTmpl" type="text/template">
                        {{ for(var i in it) { }}
                        <tr>
                            <td>{{=it[i].teacherName}}</td>
                            <td>{{=it[i].groupName}}</td>
                            <td>{{=it[i].finalStdScore}}</td>
                            <td>{{=it[i].ranking}}</td>
                        </tr>
                        {{ } }}
                    </script>
                    <script id="historyTmpl1" type="text/template">
                        {{ for(var i in it) { }}
                        <tr>
                            <td>{{=it[i].teacherName}}</td>
                            <td>{{=it[i].groupName}}</td>
                            <td>{{=it[i].gradeName}}</td>
                        </tr>
                        {{ } }}
                    </script>
                </table>
            </div>
            <div class="right-cont right-cont3">
                <table class="table-my">
                    <thead>
                    <tr>
                        <th class="th1">考核时间</th>
                        <th class="th2">所在小组</th>
                        <th class="th3">总成绩</th>
                        <th class="th4">总排名</th>
                    </tr>
                    </thead>
                    <tbody id="myscores">
                    <%--<tr>--%>
                    <%--<td>2016-2017学年</td>--%>
                    <%--<td>高一组</td>--%>
                    <%--<td>200</td>--%>
                    <%--<td>2</td>--%>
                    <%--</tr>--%>
                    </tbody>
                    <script id="myscoresTmpl" type="text/template">
                        {{ for(var i in it) { }}
                        <tr>
                            <td>{{=it[i].year}}</td>
                            <td>{{=it[i].groupName}}</td>
                            <td>{{=it[i].stdScore}}</td>
                            <td>{{=it[i].rank}}</td>
                        </tr>
                        {{ } }}
                    </script>
                </table>
            </div>
        </div>

    </div>
    <div class="tea-alert">
        <div class="alert-title clearfix">
            <p>教师评价</p>
            <span class="alert-close" style="font-size: 18px;">x</span>
        </div>
        <div class="alert-main" style="padding: 0px 15px 40px;">
            <div style="margin-bottom: 20px;">
                <span style="display:block;vertical-align: middle;text-align: left">实证材料</span>
                <div style="display: inline-block;padding: 5px;width:95%;height:250px;min-height: 250px;overflow: auto;border: 1px solid #bbb;" id="evidence">
                </div>
            </div>
            <div>
                <span style="display: inline-block;vertical-align: middle;display: block;text-align:left;">教师陈述</span>
                <div style="display: inline-block;padding: 5px;width:95%;height: 250px;min-height:250px;overflow: auto;border: 1px solid #bbb;" id="teacherstatement">
                </div>
            </div>
        </div>
    </div>
    <!--/.col-right-->

</div>
<!--/#content-->
<div class="bg"></div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->

<script id="membersStateTmpl" type="text/template">
    {{ for(var i in it) { }}
    <span id="{{=it[i].id}}" {{if(it[i].state==1){ }}class="border-orange" {{}}}>{{=it[i].name}}</span>
    {{ } }}
</script>

<script src="/static_new/js/modules/ueditor/ueditor.config.js"></script>
<script src="/static_new/js/modules/ueditor/ueditor.all.js"></script>
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static_new/js/modules/teacherevaluation/0.1.0/item');

    function upInt(num, pre) {
        return Math.ceil(num * pre);
    }
    function downInt(num, pre) {
        if (pre == 1) {
            return Math.ceil(num * pre);
        }
        return Math.ceil(num * pre) - 1;
    }
</script>
</body>
</html>
