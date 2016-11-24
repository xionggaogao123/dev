<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2016/4/22
  Time: 17:28
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
  <link rel="stylesheet" type="text/css" href="/static_new/css/friendcircle/rome.css?v=1"/>
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>
<body evid="${evaluationId}">
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
        <li class="cur-li">人员分组</li>
        <li>评分比重</li>
        <li>考核内容</li>
        <li style="display: none">量化成绩</li>
        <li>等第设置</li>
        <li>考核模式</li>
        <li>评分时间</li>
        <li>评比规则</li>
      </ul>
      <div class="right-cont1 right-cont">
        <h3 class="year">2015-2016学年</h3>
          <button class="btn-dr">导入成员</button>
        <dl class="cont-dl clearfix">
          <dd class="clearfix">
            <div class="member-title">考核小组领导</div>
            <div class="member clearfix" id="leaders">
            </div>
            <script id="leadersTmpl" type="text/template">
              {{ for(var i in it) { }}
              <span id="{{=it[i].id}}">{{=it[i].value}}</span>
              {{ } }}
            </script>
            <button class="bj-khxz">编辑</button>
          </dd>
          <dd class="clearfix">
            <div class="member-title">考核小组成员</div>
            <div class="member clearfix" id="members">
              <%--<span id="">王二</span>--%>
            </div>
            <script id="membersTmpl" type="text/template">
              {{ for(var i in it) { }}
              <span id="{{=it[i].id}}">{{=it[i].value}}</span>
              {{ } }}
            </script>
            <button class="bj-khdx">编辑</button>
          </dd>

          <dd class="clearfix" id="teacherGroupDTOs">
            <%--<div class="member-title">小组成员--%>
              <%--<input type="button" value="添加小组" class="addTeacherGroup">--%>
            <%--</div>--%>
            <%--<div class="member-top">--%>
              <%--分组一/总人数90人/优秀人数/4人--%>
            <%--</div>--%>
            <%--<div class="member clearfix">--%>
              <%--<span>王二</span>--%>
            <%--</div>--%>
            <%--<button class="bj-xzry btn-up">编辑</button>--%>

          </dd>
          <script id="teacherGroupDTOsTmpl" type="text/template">
            <div class="member-title">小组成员
              <input type="button" value="添加小组" class="addTeacherGroup">
            </div>
            {{ for(var i in it) { }}
            <div class="member-top">
              {{=it[i].groupName}}(总人数{{=it[i].teachers.length}}人、优秀人数{{=it[i].num}}人、良好人数{{=it[i].liangNum}}人)
            </div>
            {{var teachers = it[i].teachers;}}
            <div class="member clearfix">
              {{ for(var j in teachers){ }}
              <span {{? teachers[j].value == 1}}class="sign" {{?}}>{{=teachers[j].name}}</span>
              {{} }}
            </div>
            <button class="bj-xzry btn-up" id="{{=it[i].groupId}}">编辑</button>
            <button class="delete-xzry btn-up" id="{{=it[i].groupId}}">删除</button>
            {{ } }}
            <%--<div class="member clearfix" style="margin-top: 10px;">--%>
              <%--图例：<span class="sign">XXX</span> 表示报名老师     <span>XXX</span>表示未报名老师--%>
            <%--</div>--%>
          </script>
        </dl>
      </div>
      <div class="right-cont2 right-cont">
        <h3 class="year">2015-2016学年</h3>
        <p>评分比重设置：</p>
        <span class="span1">总分</span>
        <span class="span3">0%</span>
        <div>
          <span class="span2">组内互评</span><i>%</i>
          <input type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" id="huPingPro" class="pro">
          <span>去掉<input type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" id="huPingMax" class="max-min">个最高分，</span>
          <span>去掉<input type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" id="huPingMin" class="max-min">个最低分</span>
        </div>
        <div>
          <span class="span2 fontb">考核小组领导</span><i>%</i>
          <input type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" id="leaderPro" class="pro">
          <span>去掉<input type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" id="leaderMax" class="max-min">个最高分，</span>
          <span>去掉<input type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" id="leaderMin" class="max-min">个最低分</span>
        </div>
        <div>
          <span class="span2 fontb">考核小组成员</span><i>%</i>
          <input type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" id="groupPro" class="pro">
          <span>去掉<input type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" id="groupMax" class="max-min">个最高分，</span>
          <span>去掉<input type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" id="groupMin" class="max-min">个最低分</span>
        </div>
        <%--<div class="pf-tree"></div>--%>
        <%--<div class="pf-tree2"></div>--%>
        <div class="clearfix">
            <button class="btn-save" id="saveProportion">保存</button>
        </div>
        <p class="notice">
          注：评分比重设置中组内互评、考核小组组长、考核小组成员打分比重之和为100%！
        </p>
      </div>
      <div class="right-cont3 right-cont">
        <h3 class="year">2015-2016学年</h3>
        <p>考核要素
          <input type="button" value="添加" id="addElement">
        </p>
        <div id="elements">
          <%--<div class="div-yq clearfix">--%>
            <%--<span>考核要求一</span>--%>
            <%--<button>85</button>--%>
            <%--<button class="bj-khnr">编辑</button>--%>
            <%--<button class="btn-del-element">删除</button>--%>
          <%--</div>--%>
        </div>
        <script id="elementsTmpl" type="text/template">
          {{ for(var i in it) { }}
            <div class="div-yq clearfix" id="{{=it[i].id}}">
              <span>{{=it[i].name}}</span>
              <button>{{=it[i].score}}</button>
              <button class="bj-khnr">编辑</button>
              <button class="btn-del-element">删除</button>
            </div>
          {{ } }}
        </script>
      </div>
      <div class="right-cont3s right-cont">
        <div class="back-cont3"><返回</div>
        <p>考核内容
          <input type="button" value="添加" id="addContentToElement">
        </p>
        <div id="contents">
          <%--<div class="div-yq clearfix">--%>
            <%--<span>考核要求一</span>--%>
            <%--<button class="btn-bj3s">编辑</button>--%>
            <%--<button class="btn-del-content">删除</button>--%>
          <%--</div>--%>
        </div>
        <script id="contentsTmpl" type="text/template">
          {{ for(var i in it) { }}
          <div class="div-yq clearfix" id="{{=it[i].id}}">
            <span>{{=it[i].value}}</span>
            <button class="btn-bj3s">编辑</button>
            <button class="btn-del-content">删除</button>
          </div>
          {{ } }}
        </script>
      </div>

      <div class="right-cont4 right-cont">
        <h3 class="year">2015-2016学年</h3>
        <p>量化成绩
          <input type="button" value="添加" id="addLiangHua">
        </p>
        <div id="lianghua">
          <%--<div class="div-yq clearfix">--%>
            <%--<span>量化成绩一量化成绩一量化成绩一量化成绩一量化成绩一量化成绩一量化成绩一量化成绩一</span>--%>
            <%--<button>85</button>--%>
            <%--<button class="bj-lhcj">编辑</button>--%>
            <%--<button class="btn-del-element">删除</button>--%>
          <%--</div>--%>
        </div>
        <script id="lianghuaTmpl" type="text/template">
          {{ for(var i in it) { }}
          <div class="div-yq clearfix" id="{{=it[i].id}}">
            <span>{{=it[i].name}}</span>
            <button>{{=it[i].score}}</button>
            <button class="bj-lhcj">编辑</button>
            <button class="btn-del-element">删除</button>
          </div>
          {{ } }}
        </script>
      </div>

      <div class="right-cont5 right-cont">
        <h3 class="year">2015-2016学年</h3>
        <table>
          <thead>
            <tr>
              <th>等第</th>
              <th>分数区间</th>
              <th>
                <button class="bj-ddsz">编辑</button>
              </th>
            </tr>
          </thead>
          <tbody id="grades">
            <%--<tr>--%>
              <%--<td>--%>
                <%--<div>优秀</div>--%>
              <%--</td>--%>
              <%--<td>--%>
                <%--<em>%></em>--%>
                <%--<i class="i1">≥</i>--%>
                <%--<i class="i2">%</i>--%>
                <%--<span>100</span>--%>
                <%--<span class="spann">90</span>--%>
              <%--</td>--%>
            <%--</tr>--%>
            <%--<tr>--%>
              <%--<td>--%>
                <%--<div>优秀</div>--%>
              <%--</td>--%>
              <%--<td>--%>
                <%--<em>%></em>--%>
                <%--<i class="i1">≥</i>--%>
                <%--<i class="i2">%</i>--%>
                <%--<span>100</span>--%>
                <%--<span class="spann">90</span>--%>
              <%--</td>--%>
            <%--</tr>--%>
            <%--<tr>--%>
              <%--<td>--%>
                <%--<div>优秀</div>--%>
              <%--</td>--%>
              <%--<td>--%>
                <%--<em>%></em>--%>
                <%--<i class="i1">≥</i>--%>
                <%--<i class="i2">%</i>--%>
                <%--<span>100</span>--%>
                <%--<span class="spann">90</span>--%>
              <%--</td>--%>
            <%--</tr>--%>
          </tbody>
          <script id="gradesTmpl" type="text/template">
            {{ for(var i in it) { }}
            <tr>
              <td>
                <div>{{=it[i].name}}</div>
              </td>
              <td>
                <em>%</em>
                <i class="i1">~</i>
                <i class="i2">%</i>
                <i class="i3">(含)</i>
                <span>{{=it[i].end}}</span>
                <span class="spann">{{=it[i].begin}}</span>
              </td>
            </tr>
            {{ } }}
          </script>
        </table>

      </div>
      <div class="right-cont6 right-cont">
        <div class="cont6-main">
          <span>请选择考核模式</span>
          <div class="rank-div">
            <label><input type="radio" name="eva-style" value="2">等级评分</label>
            <label>A<input gn="A" class="grade" type="text" value="95" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"></label>
            <label>B<input gn="B" class="grade" type="text" value="85" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"></label>
            <label>C<input gn="C" class="grade" type="text" value="75" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"></label>
            <label>D<input gn="D" class="grade" type="text" value="65" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"></label>
          </div>
          <div class="score-div">
            <label><input type="radio" name="eva-style" value="1">数值评分</label>
          </div>
          <p>注：等级评分需额外设置每个等第所对应分值</p>
          <div class="btn-wrap clearfix">
            <button class="btn-save" id="saveMode" style="margin-right: 350px; ">保存</button>
          </div>
        </div>
      </div>
      <div class="right-cont7 right-cont">
        <h3 class="year" style="margin-bottom: 20px;">2015-2016学年</h3>
        <div class="cont6-main">
          <%--<span>个人陈述时间</span>--%>
          <%--<input type="text" id="personalTimeBegin" readonly>--%>
          <%--<em></em>--%>
          <%--<input type="text" id="personalTimeEnd" readonly>--%>
          <%--<span>考核评分时间</span>--%>
          <%--<input type="text" id="groupTimeBegin" readonly>--%>
          <%--<em></em>--%>
          <%--<input type="text" id="groupTimeEnd" readonly>--%>
          <span class="w-span">考核评分时间</span>
          <input type="text" id="evaluationTimeBegin" readonly>
          <em>-</em>
          <input type="text" id="evaluationTimeEnd" readonly>

        </div>
          <button class="btn-save" id="saveTime" style="margin-left: 336px;">保存</button>
      </div>

      <div class="right-cont8 right-cont" style="display: none;">
        <div class="cont7-main">
          <textarea id="rule" style="width: 100%;height: 100%;border: none;"></textarea>
        </div>
        <button class="btn-save" id="saveRule">保存</button>
      </div>

    </div>

  </div>
  <!--/.col-right-->
  <!--考核小组编辑弹窗-->
  <div class="wind wind-khxz">
    <div class="wind-top clearfix">
      考核小组领导编辑
      <em>×</em>
    </div>
    <div class="wind-main">
      <div class="wind-list">
        <p>全部教师</p>
        <input type="text" placeholder="搜索教师" class="search">
        <ul class="ul-tea" id="leadersCandidate">
          <%--<li>张飞</li>--%>
        </ul>
        <script id="leadersCandidateTmpl" type="text/template">
          {{ for(var i in it) { }}
          <li id="{{=it[i].id}}">{{=it[i].name}}</li>
          {{ } }}
        </script>
      </div>
      <div class="jian" id="addTeacherToLeaders">></div>
      <div class="wind-list">
        <p>全部教师</p>
        <input type="text" placeholder="搜索教师" class="search">
        <ul class="ul-tea" id="leadersExistence">
          <%--<li>张飞<i></i></li>--%>
        </ul>
        <script id="leadersExistenceTmpl" type="text/template">
          {{ for(var i in it) { }}
          <li id="{{=it[i].id}}">{{=it[i].name}}<i></i></li>
          {{ } }}
        </script>
      </div>
      <button class="btn-ok">确认</button>
      <button class="btn-no">取消</button>
    </div>
  </div>
  <!--/考核小组编辑弹窗-->

  <!--考核人员编辑弹窗-->
  <div class="wind wind-khry">
    <div class="wind-top clearfix">
      考核小组成员编辑
      <em>×</em>
    </div>
    <div class="wind-main">
      <div class="wind-list">
        <p>全部教师</p>
        <input type="text" placeholder="搜索教师" class="search">
        <ul class="ul-tea" id="membersCandidate">
          <%--<li>张飞</li>--%>
        </ul>
        <script id="membersCandidateTmpl" type="text/template">
          {{ for(var i in it) { }}
          <li id="{{=it[i].id}}">{{=it[i].name}}</li>
          {{ } }}
        </script>
      </div>
      <div class="jian" id="addTeacherToMembers">></div>
      <div class="wind-list">
        <p>全部教师</p>
        <input type="text" placeholder="搜索教师" class="search">
        <ul class="ul-tea" id="membersExistence">
          <%--<li>张飞<i></i></li>--%>
        </ul>
        <script id="membersExistenceTmpl" type="text/template">
          {{ for(var i in it) { }}
          <li id="{{=it[i].id}}">{{=it[i].name}}<i></i></li>
          {{ } }}
        </script>
      </div>
      <button class="btn-ok">确认</button>
      <button class="btn-no">取消</button>
    </div>
  </div>
  <!--/考核人员编辑弹窗-->

  <!--小组人员编辑弹窗-->
  <div class="wind wind-xzry">
    <div class="wind-top clearfix">
      考核人员编辑
      <em>×</em>
    </div>
    <div class="wind-main">
      <span class="nm-spann">分组名称</span>
      <input type="text" class="nm-input" id="teacherGroupName">
      <span class="nm-span">总人数</span>
      <input type="text" class="nmm-input" id="allStuNo" readonly>
      <span class="nm-span">优秀人数</span>
      <input type="text" class="nmm-input" id="num" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
      <span class="nm-span">良好人数</span>
      <input type="text" class="nmm-input" id="lnum" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
      <div class="wind-list">
        <p>全部教师</p>
        <input type="text" placeholder="搜索教师" class="search">
        <ul class="ul-tea" id="teachersCandidate">
          <%--<li>张飞</li>--%>
        </ul>
        <script id="teachersCandidateTmpl" type="text/template">
          {{ for(var i in it) { }}
          <li id="{{=it[i].id}}">{{=it[i].name}}</li>
          {{ } }}
        </script>
      </div>
      <div class="jian" id="addTeacherToTeacherGroup">></div>
      <div class="wind-list">
        <p>全部教师</p>
        <input type="text" placeholder="搜索教师" class="search">
        <ul class="ul-tea" id="teachersExistence">
          <%--<li>张飞<i></i></li>--%>
        </ul>
        <script id="teachersExistenceTmpl" type="text/template">
          {{ for(var i in it) { }}
          <li id="{{=it[i].id}}">{{=it[i].name}}<i></i></li>
          {{ } }}
        </script>
      </div>
      <button class="btn-ok">确认</button>
      <button class="btn-no">取消</button>
    </div>
  </div>
  <!--/小组人员编辑弹窗-->

  <!--考核要素与分值编辑弹窗-->
  <div class="wind wind-khnr wind-khnr1">
    <div class="wind-top clearfix">
      考核要素与分值
      <em>×</em>
    </div>
    <span class="span1">考核要素</span>
    <textarea id="elementname"></textarea>
    <span class="span2">分值</span>
    <input type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" id="elementscore">
    <div class="btn-f">
      <button class="btn-ok" id="upsetElement">确认</button>
      <button class="btn-no">取消</button>
    </div>
  </div>
  <!--/考核要素与分值编辑弹窗-->

  <!--考核内容编辑弹窗-->
  <div class="wind wind-khnr2 wind-khnr">
    <div class="wind-top clearfix">
      考核内容
      <em>×</em>
    </div>
    <span class="span1">考核内容</span>
    <textarea id="ele-content"></textarea>
    <div class="btn-f">
      <button class="btn-ok" id="upsetContent">确认</button>
      <button class="btn-no">取消</button>
    </div>
  </div>
  <!--/考核内容编辑弹窗-->

  <!--量化成绩编辑弹窗-->
  <div class="wind wind-lhcj wind-khnr">
    <div class="wind-top clearfix">
      量化成绩
      <em>×</em>
    </div>
    <span class="span1">量化成绩</span>
    <textarea id="lianghuaname"></textarea>
    <span class="span2">分值</span>
    <input type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" id="lianghuascore">
    <div class="btn-f">
      <button class="btn-ok" id="upsetLiangHua">确认</button>
      <button class="btn-no">取消</button>
    </div>
  </div>
  <!--/量化成绩编辑弹窗-->

  <!--等第设置弹窗-->
  <div class="wind wind-ddsz">
    <div class="wind-top clearfix">
      等第设置
      <em>×</em>
    </div>
    <div class="ddtitle">
      <span class="span1">等第</span>
      <span class="span2">分数区间</span>
            <span class="span3">
                <button id="appendGrade">添加</button>
            </span>
    </div>
    <table id="gradeTable">
      <tr>
        <th class="th1"></th><th class="th2"></th><th class="th3"></th>
      </tr>
      <tr>
        <td class="td1">
          <input type="text" class="input1" value="优秀">
        </td>
        <td class="td2">
          <input value="100" type="text" class="input2" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"><em>%</em><i class="i1">~</i><span>%</span><i class="i2">(含)</i>
          <input value="90" type="text" class="input3" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
        </td>
        <td>
          <button class="btn-x1">删除</button>
        </td>
      </tr>
      <tr>
        <td class="td1">
          <input type="text" class="input1" value="良好">
        </td>
        <td class="td2">
          <input value="90" type="text" class="input2" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"><em>%</em><i class="i1">~</i><span>%</span><i class="i2">(含)</i>
          <input value="80" type="text" class="input3" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
        </td>
        <td>
          <button class="btn-x1">删除</button>
        </td>
      </tr>
      <tr>
        <td class="td1">
          <input type="text" class="input1" value="合格">
        </td>
        <td class="td2">
          <input value="80" type="text" class="input2" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"><em>%</em><i class="i1">~</i><span>%</span><i class="i2">(含)</i>
          <input value="70" type="text" class="input3" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
        </td>
        <td>
          <button class="btn-x1">删除</button>
        </td>
      </tr>
      <tr>
        <td class="td1">
          <input type="text" class="input1" value="基本合格">
        </td>
        <td class="td2">
          <input value="70" type="text" class="input2" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"><em>%</em><i class="i1">~</i><span>%</span><i class="i2">(含)</i>
          <input value="60" type="text" class="input3" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
        </td>
        <td>
          <button class="btn-x1">删除</button>
        </td>
      </tr>
      <tr>
        <td class="td1">
          <input type="text" class="input1" value="不合格">
        </td>
        <td class="td2">
          <input value="60" type="text" class="input2" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"><em>%</em><i class="i1">~</i><span>%</span><i class="i2">(含)</i>
          <input value="0" type="text" class="input3" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
        </td>
        <td>
          <button class="btn-x1">删除</button>
        </td>
      </tr>
    </table>
    <div class="btn-f">
      <button class="btn-ok1" id="updateGrades">确认</button>
      <button class="btn-no">取消</button>
    </div>
  </div>
  <!--/等第设置弹窗-->

    <!--导入人员弹窗-->
    <div class="wind wind-in">
        <div class="wind-top clearfix">
            导入成员
            <em>×</em>
        </div>
        <ul class="ul-dr">
            <li>
                <span id="exportTeachers">生成模板</span>
            </li>
            <li>
                <em></em>
            </li>
            <li>
                <input type="file" id="file" name="file">
            </li>
        </ul>
        <div class="div-btt clearfix">
            <button class="btn-ok1 import">导入</button>
            <button class="btn-no">取消</button>
        </div>
    </div>
    <!--/导入人员弹窗-->

  <div class="bg"></div>


</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp"%>
<!--#foot-->

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('/static_new/js/modules/teacherevaluation/0.1.0/config');
</script>
</body>
</html>
