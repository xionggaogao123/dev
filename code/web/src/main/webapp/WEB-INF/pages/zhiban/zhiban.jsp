<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@page import="com.pojo.app.SessionValue"%>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>值班</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link rel="stylesheet" type="text/css" href="/static_new/css/zhiban/zhiban.css"/>
  <!-- jquery artZoom4Liaoba styles -->
  <!-- Custom styles -->
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
    <script type="text/javascript">
    </script>
</head>
<body>
<!--#head-->
<%@ include file="../common_new/head.jsp" %>

<!--#content-->
<div id="content" class="clearfix">

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
  <div class="col-right">
    <input hidden="hidden" id="dutyId">
    <!--.tab-col右侧-->
    <div class="tab-col">
      <input hidden="hidden" id="role" value="${sessionValue.userRole}">
      <div class="tab-head clearfix">
        <input id="week" hidden="hidden" value="${week}">
        <input id="nowWeek" hidden="hidden" value="${week}">
        <input id="nowYear3" hidden="hidden" value="${year}">
        <input id="year3" hidden="hidden" value="${year}">
        <ul>
            <c:choose>
                  <c:when test="${roles:isHeadmaster(sessionValue.userRole) || roles:isManager(sessionValue.userRole)}">
                  <li class="cur" id="ZBSM"><a href="javascript:;" >值班说明</a><em></em></li>
                  <li id="ZBJH"><a href="javascript:;" >值班计划管理</a><em></em></li>
                  <li id="ZBXCGL"><a href="javascript:;" >值班薪酬管理</a><em></em></li>
                  <li id="HBSH"><a href="javascript:;" >换班审核</a><em></em></li>
                  <li id="ZBSZ"><a href="javascript:;" >设置</a><em></em></li>
                  <li id="MYZB"><a href="javascript:;" >我的值班</a><em></em></li>
                  <li id="ZBXC"><a href="javascript:;" >值班薪酬</a><em></em></li>

                </c:when>
                <c:otherwise>
                  <li class="cur" id="ZBSM"><a href="javascript:;" >值班说明</a><em></em></li>
                  <li id="ZBJH"><a href="javascript:;" >值班计划管理</a><em></em></li>
                  <li id="MYZB"><a href="javascript:;" >我的值班</a><em></em></li>
                  <li id="ZBXC"><a href="javascript:;" >值班薪酬</a><em></em></li>
                </c:otherwise>
            </c:choose>

        </ul>
      </div>

      <div class="tab-main">
        <!--================我的值班start=================-->
        <div class="tab-WDZB">
          <!--================换班审核start=================-->
          <div class="zhiban-hbsh" id="tab-HBSH">
            <div class="zhiban-title">
              <span>日期：</span><input class="Wdate" type="text" id="bTime3" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})" >
              <i>-</i>
              <input class="Wdate" type="text" id="sTime3" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})">
              <%--<select id="dtmid">--%>
                <%--<c:forEach items="${dutyTimes}" var="times">--%>
                  <%--<option value="${times.id}">${times.timeDesc}</option>--%>
                <%--</c:forEach>--%>
              <%--</select>--%>
              <span>姓名：</span>
              <input type="text" class="zhiban-name" id="username3">
              <button class="zhiban-qr sure2">确认</button>
            </div>
            <div class="zhiban-chart clearfix">
              <a href="javascript:;" class="zhiban-daochu">导出</a>
              <table class="zhiban-table">

              </table>
              <script type="text/template" id="zhiban-table_templ">
                <tr>
                  <th style="width:20%">申请人</th>
                  <th style="width:15%">日期</th>
                  <th style="width:15%">时段</th>
                  <th style="width:20%">理由</th>
                  <th style="width:10%">审核状态</th>
                  <th style="width:20%">操作</th>
                </tr>
                {{ if(it.rows.length>0){ }}
                {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                {{var obj=it.rows[i];}}
                <tr>
                  <td style="background:#ececec;width:20%">{{=obj.userName}}</td>
                  <td style="width:15%">{{=obj.date}}</td>
                  <td style="width:15%">{{=obj.timeDesc}}</td>
                  <td style="width:20%">{{=obj.cause}}</td>
                  <td style="width:10%">
                    {{?obj.type==0}}
                    未审核
                    {{?}}
                    {{?obj.type==1}}
                    审核通过
                  {{?}}
                    {{?obj.type==2}}
                    审核驳回
                  {{?}}
                  </td>
                  <td style="width:20%">
                    {{?obj.type==0}}
                    <a href="javascript:;" class="zb-hb-xq" dtd="{{=obj.timeDesc}}" dtduan="{{=obj.timeDuan}}" dtp="{{=obj.projectName}}" cus="{{=obj.cause}}">详情</a>
                    <i>|</i>
                    <a href="javascript:;" class="tongguo" duid="{{=obj.userId}}" dt="{{=obj.date}}" duser="{{=obj.userName}}" dtd="{{=obj.timeDesc}}" dtp="{{=obj.projectName}}" cus="{{=obj.cause}}" dsid="{{=obj.id}}">通过</a>
                    <i>|</i>
                    <a href="javascript:;" class="bohui" dsid="{{=obj.id}}">驳回</a>
                    {{??}}
                    <a href="javascript:;" class="zb-hb-xq" dtd="{{=obj.timeDesc}}" dtduan="{{=obj.timeDuan}}" dtp="{{=obj.projectName}}" cus="{{=obj.cause}}">详情</a>
                    {{?}}
                  </td>
                </tr>
                {{ } }}
                {{ } }}
              </script>
            </div>
          </div>
          <!--================换班审核end=================-->
          <!--================值班计划管理start=================-->
          <div class="zhiban-jhgl" id="tab-ZBJH" style="display:none;">
            <div class="zb-jhgl-title clearfix">
              <c:if test="${roles:isHeadmaster(sessionValue.userRole) || roles:isManager(sessionValue.userRole)}">
              <span class="zb-jhgl-school">生成平安校园记录</span>
              </c:if>
              <%--<span class="zb-jhgl-tj">值班记录统计</span>--%>
              <span class="zb-jhgl-look">查看值班记录</span>
            </div>
            <div class="zb-jhgl-main">
              <div class="zb-jhgl-mb">
                <c:if test="${roles:isHeadmaster(sessionValue.userRole) || roles:isManager(sessionValue.userRole)}">
                <%--<span class="zb-jhgl-save">保存为模板</span>--%>
                <%--<span class="zb-jhgl-use">使用模板</span>--%>
                <%--<span class="zb-jhgl-manage">模板管理</span>--%>
                </c:if>
                <span class="zb-jhgl-xz zb-ri">下周</span>
                <span class="zb-jhgl-zz zb-ri">本周</span>
                <span class="zb-jhgl-sz zb-ri">上周</span>
              </div>
              <table class="zb-jhgl-table">
              </table>
              <script type="text/template" id="zb-jhgl-table_templ">
                <tr>
                  <th style="width:20%;">时间</td>
                  {{~it.dutyTimes:value:index}}
                  <th style="width:20%;">{{=value}}</td>
                  {{~}}
                </tr>
                {{~it.times:value1:index1}}
                <tr>
                    {{?value1.time==it.curtime}}
                    <td class="zb-today" style="background:#ececec;">{{=value1.time}}
                    <img src="/static_new/images/zhiban/zhiban-8.png">
                    {{??}}
                  <td style="background:#ececec;">{{=value1.time}}
                    {{?}}
                  </td>

                  {{~it.dutyTimeCount:value2:index2}}
                  {{~it.rows:value3:index3}}
                  {{?value3.xIndex==value1.num&&value3.yIndex==value2}}

                    {{?value3.usernames==''}}
                  <td>
                  {{?value3.type==0}}
                    <c:if test="${roles:isHeadmaster(sessionValue.userRole) || roles:isManager(sessionValue.userRole)}">
                    <a href="javascript:;" class="zb-jhgl-add" idx="{{=value3.index}}"></a>
                    </c:if>
                  {{?}}
                  </td>
                  <%--<td>吴丽华<br>学校值班-日常值班<div class="zb-td-meng"></div><span class="zb-td-edit">编辑</span></td>--%>
                    {{??}}
                    <td>{{=value3.usernames}}<br>{{=value3.dutyProject}}<div class="zb-td-meng"></div>
                      {{?value3.type==0}}
                      <c:if test="${roles:isHeadmaster(sessionValue.userRole) || roles:isManager(sessionValue.userRole)}">
                      <span did="{{=value3.id}}" idx="{{=value3.index}}" class="zb-td-edit">编辑</span>
                      </c:if>
                  {{?}}
                    </td>
                    {{?}}
                  {{?}}
                  {{~}}
                  {{~}}
                </tr>
                {{~}}
                <%--{{ if(it.rows.length>0){ }}--%>
                <%--{{ for (var i = 0, l = it.rows.length; i < l; i++) { }}--%>
                <%--{{var obj=it.rows[i];}}--%>
                <%--<option value="{{=obj.id}}">{{=obj.content}}</option>--%>
                <%--{{ } }}--%>
                <%--{{ } }}--%>
              </script>
            </div>
          </div>
          <!--================值班计划管理end=================-->
          <!--=============模板管理start=============-->
          <div class="JBSQ-II">
            <a href="#" class="JBSQ-II-back">&lt;返回</a>
            <div class="JBSQ-main">
              <table>
                <thead>
                <tr>
                  <th width="55">序号</th>
                  <th width="355">模板名称</th>
                  <th width="190">模板类型</th>
                  <th width="150">操作</th>
                </tr>
                </thead>
                <tbody id="model-table">

                </tbody>
                <script type="text/template" id="model-table_templ">
                  {{ if(it.rows.length>0){ }}
                  {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                  {{var obj=it.rows[i];}}
                  <tr>
                    <td class="JBSQ-td" width="55">{{=i+1}}</td>
                    <td width="355">{{=obj.name}}</td>
                    <td width="190">值班模板</td>
                    <td width="150">
                      <em class="h-cursor edit-model" mid="{{=obj.id}}" mnm="{{=obj.name}}">编辑</em>|
                      <em class="h-cursor del-model" mid="{{=obj.id}}">删除</em>
                    </td>
                  </tr>
                  {{ } }}
                  {{ } }}
                </script>
              </table>
            </div>
          </div>
          <!--=============模板管理end=============-->
          <!-- -------------我的值班start------------------>
          <div id="tab-MYZB" class="tab-MYZB">
            <div class="LWDZB-I">
              <div class="LWDJB-top">
                <div class="LWDJB-top-left">
                  <span>当前时间</span><em id="curtime">2016/11/11</em>
                </div>
                <div class="LWDJB-top-right">
                  <span class="LWDJB-CK h-cursor">查看值班记录</span>
                </div>
              </div>
              <div class="LWDJB-main">
                <table>
                  <thead>
                  <tr>
                    <th width="185">时间</th>
                    <th width="565">事项</th>
                  </tr>
                  </thead>
                  <tbody id="myduty-list">

                  </tbody>
                  <script type="text/template" id="myduty-list_templ">
                    {{ if(it.myDutys.length>0){ }}
                    {{ for (var i = 0, l = it.myDutys.length; i < l; i++) { }}
                    {{var obj=it.myDutys[i];}}
                    <tr>
                      <td class="JBSQ-td" width="185">
                        {{?obj.isToday==1}}
                        <img src="/static_new/images/zhiban/LWDJB-day.jpg">
                        {{?}}
                        {{=obj.times}}
                      </td>
                      {{?obj.count==0}}
                      <td widht="565"></td>
                      {{??}}
                      <td width="565">
                        {{~obj.myDutyDTOs:value:index}}
                        <div class="LWDJB-TX-RC">
                            <em>{{=value.timeDesc}}{{=value.timeDuan}}{{=value.dutyProject}}</em>
                            <div>
                              {{?value.type==0}}
                                {{?value.type3==0}}
                                  <span>审核中</span>
                                {{?}}
                                {{?value.type3==2}}
                                  <span>驳回</span>
                                {{?}}
                              {{?value.type3!=0}}
                                {{?value.type2==0}}
                                <span class="LWDJB-HB h-cursor" dtd="{{=value.timeDesc}}" dtduan="{{=value.timeDuan}}" dtp="{{=value.dutyProject}}" duid="{{=value.id}}">申请换班</span>
                                {{?}}
                              {{?}}
                              <span class="LWDJB-QD h-cursor" duid="{{=value.id}}">签到</span>
                              {{?}}
                              {{?value.type==1}}
                                <span class="LWDJB-TX h-cursor" tdp="{{=value.dutyProject}}" tda="{{=value.timeDuan}}" tde="{{=value.timeDesc}}" duid="{{=value.id}}">填写值班记录</span><span class="LWDJB-QT h-cursor" duid="{{=value.id}}">签退</span>
                              {{?}}
                              {{?value.type==2}}
                                <span class="LWDJB-TX h-cursor" tdp="{{=value.dutyProject}}" tda="{{=value.timeDuan}}" tde="{{=value.timeDesc}}" duid="{{=value.id}}">填写值班记录</span>
                              {{?}}
                            </div>
                        </div>
                        {{~}}
                      </td>
                      {{?}}
                    </tr>
                    {{ } }}
                    {{ } }}
                  </script>
                </table>
              </div>
            </div>
            <div class="LWDZB-II">
              <a href="#">&lt;返回我的值班</a>
              <div class="JBSQ-top">
                <select id="year2">
                  <option value="2016">2016年</option>
                </select>
                <select id="month2">
                  <option value="1">一月</option>
                  <option value="2">二月</option>
                  <option value="3">三月</option>
                  <option value="4">四月</option>
                  <option value="5">五月</option>
                  <option value="6">六月</option>
                  <option value="7">七月</option>
                  <option value="8">八月</option>
                  <option value="9">九月</option>
                  <option value="10">十月</option>
                  <option value="11">十一月</option>
                  <option value="12">十二月</option>
                </select>
                <span id="seach-myDuty">查询</span>
                <span id="lastWeek2" style="margin-left: 130px;">上周</span>
                <span id="thisWeek2" style="margin-left: 10px;">本周</span>
                <span id="nextWeek2" style="margin-left: 10px;">下周</span>
              </div>
              <div class="LWDJB-main">
                <table>
                  <thead>
                  <tr>
                    <th width="105">值班日期</th>
                    <th width="80">值班时段</th>
                    <th width="110">值班时间</th>
                    <th width="115">值班项目</th>
                    <th width="150">出勤</th>
                    <th width="240">值班记录</th>
                  </tr>
                  </thead>
                  <tbody class="my-duty">

                  </tbody>
                  <script type="text/template" id="my-duty_templ">
                    {{ if(it.rows.length>0){ }}
                    {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                    {{var obj=it.rows[i];}}
                    <tr>
                      <td class="JBSQ-td" width="105">{{=obj.date}}</td>
                      <td width="80">{{=obj.timeDesc}}</td>
                      <td width="110">{{=obj.timeDuan}}</td>
                      <td width="115">{{=obj.projectName}}</td>
                      <td width="150">{{=obj.checkIn}}-{{=obj.checkOut}}</td>
                      <td width="240">{{=obj.content}}</td>
                    </tr>
                    {{ } }}
                    {{ } }}
                  </script>
                </table>
              </div>
            </div>
          </div>

          <!--================我的值班end=================-->
          <!--===============值班薪酬start=================-->
          <div class="tab-ZBXC" id="tab-ZBXC">
            <div class="JBSQ-top">
              <select id="year">
                <option value="2016">2016年</option>
              </select>
              <select id="month">
                <option value="1">一月</option>
                <option value="2">二月</option>
                <option value="3">三月</option>
                <option value="4">四月</option>
                <option value="5">五月</option>
                <option value="6">六月</option>
                <option value="7">七月</option>
                <option value="8">八月</option>
                <option value="9">九月</option>
                <option value="10">十月</option>
                <option value="11">十一月</option>
                <option value="12">十二月</option>
              </select>
              <span id="search-salary">查询</span>
            </div>
            <div class="JBSQ-middle">
              <span class="JBSQ-daochu h-cursor">导出</span>
            </div>
            <div class="JBSQ-main">
              <table>
                <thead>
                <tr>
                  <th width="100">值班日期</th>
                  <th width="100">值班时段</th>
                  <th width="125">值班时间</th>
                  <th width="125">值班内容</th>
                  <th width="120">计划时长(h)</th>
                  <th width="150">出勤</th>
                  <th width="110">值班薪酬(元)</th>
                </tr>
                </thead>
                <tbody class="salary-list">

                </tbody>
                <script type="text/template" id="salary-list_templ">
                  {{ if(it.rows.length>0){ }}
                  {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                  {{var obj=it.rows[i];}}
                  <tr>
                    <td class="JBSQ-td" width="100">{{=obj.date}}</td>
                    <td width="100">{{=obj.timeDesc}}</td>
                    <td width="125">{{=obj.timeDuan}}</td>
                    <td width="125">{{=obj.projectName}}</td>
                    <td width="120">{{=obj.times}}</td>
                    <td width="150">{{=obj.checkIn}}-{{=obj.checkOut}}</td>
                    <td width="110">
                      {{=obj.salary}}
                    </td>
                  </tr>
                  {{ } }}
                  {{ } }}
                </script>
              </table>
            </div>
          </div>

          <!--================值班薪酬end=================-->
          <!--===================值班说明start=====================-->
          <div class="tab-ZBSM" id="tab-ZBSM">
            <ul>
                <li>
                    <em>值班说明</em>
                <c:if test="${roles:isHeadmaster(sessionValue.userRole) || roles:isManager(sessionValue.userRole)}">
                  <div class="ZBSM-butn">
                    <span class="ZBSM-edit">编辑值班说明</span>
                  </div>
                </c:if>
                   <textarea disabled="disabled" id="dutyExplain"></textarea>
                </li>
            </ul>

          </div>
          <!--===================值班说明end=====================-->
          <!--================查看值班记录start=================-->
          <div class="zb-lookjl">
            <div class="zb-jl-title">
              <a href="javascript:;" class="zhiban-back">&lt;返回值班计划管理</a>
              <div class="zhiban-title">
                <span>时间：</span><input class="Wdate" type="text" id="bTime" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})">
                <i>-</i>
                <input class="Wdate" type="text" id="eTime" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})">
                <span>姓名：</span>
                <input type="text" class="zhiban-name" id="username">
                <%--<select id="projectids">--%>
                  <%--<option value="">全部</option>--%>
                  <%--<c:forEach items="${dutyProject2}" var="pro">--%>
                    <%--<option value="${pro.id}">${pro.content}</option>--%>
                  <%--</c:forEach>--%>
                <%--</select>--%>
                <button class="zhiban-qr sure3">查询</button>
                <button class="zhiban-jl lastWeek" style="margin-left: 65px;">上周</button>
                <button class="zhiban-jl thisWeek">本周</button>
                <button class="zhiban-jl nextWeek">下周</button>
              </div>
            </div>
            <div class="zb-table-jl clearfix">
              <a href="javascript:;" class="zb-table-dc export-selDutyUser">导出</a>
              <table class="zb-jl-table">

              </table>
              <script type="text/template" id="zb-jl-table_templ">
                <tr>
                  <th style="width:11%;">值班日期</th>
                  <th style="width:11%;">值班人员</th>
                  <th style="width:9%;">值班时段</th>
                  <th style="width:12%;">值班时间</th>
                  <th style="width:13%;">值班项目</th>
                  <th style="width:17%;">出勤</th>
                  <th style="width:20%;">值班记录</th>
                  <th style="width:20%;">值班附件</th>
                </tr>
                {{ if(it.rows.length>0){ }}
                {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                {{var obj=it.rows[i];}}
                <tr>
                  <td style="background:#ececec;">{{=obj.date}}</td>
                  <td>{{=obj.userName}}</td>
                  <td>{{=obj.timeDesc}}</td>
                  <td>{{=obj.timeDuan}}</td>
                  <td>{{=obj.projectName}}</td>
                  <td>{{=obj.checkIn}}-{{=obj.checkOut}}</td>
                  <td>{{=obj.content}}</td>
                  <td>
                  {{~obj.fileUploadDTOList:value:index}}
                    <div class="zhiban-FJ"><em>{{=value.name}}</em><a pth="{{=value.path}}" class="view">预览</a><a href="{{=value.path}}" target='_blank'>下载</a></div>
                  {{~}}
                  </td>
                </tr>
                {{ } }}
                {{ } }}
              </script>
            </div>
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
          <!--================查看值班记录end=================-->
          <!--================值班记录统计start=================-->
          <div class="zb-jl-tj">
            <div class="zhiban-title">
              <a href="javascript:;" class="zhiban-back">&lt;返回值班计划管理</a>
              <span>日期：</span><input class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})" id="startDate">
              <i>-</i>
              <input class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})" id="endDate">
              <span>姓名：</span>
              <input type="text" class="zhiban-name" id="name">
              <button class="zhiban-qr sure-log">导出</button>
            </div>
            <%--<div class="zb-tj-table clearfix">--%>
              <%--<a href="javascript:;" class="zb-table-dc">导出</a>--%>
              <%--<table class="zb-table-tj">--%>
                <%--<tr>--%>
                  <%--<th style="width:16%">值班日期</th>--%>
                  <%--<th style="width:12%">李媛媛</th>--%>
                  <%--<th style="width:12%">江莱</th>--%>
                  <%--<th style="width:12%">陈东飞</th>--%>
                  <%--<th style="width:12%">夏启瑞</th>--%>
                  <%--<th style="width:12%">方可欣</th>--%>
                  <%--<th style="width:12%">陈海</th>--%>
                  <%--<th style="width:12%">总计(h)</th>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                  <%--<td style="background:#ececec;">2016/6/1</td>--%>
                  <%--<td>4</td>--%>
                  <%--<td>4</td>--%>
                  <%--<td>6</td>--%>
                  <%--<td>0</td>--%>
                  <%--<td>2</td>--%>
                  <%--<td>4</td>--%>
                  <%--<td>20</td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                  <%--<td style="background:#ececec;">2016/6/2</td>--%>
                  <%--<td>0</td>--%>
                  <%--<td>4</td>--%>
                  <%--<td>6</td>--%>
                  <%--<td>2</td>--%>
                  <%--<td>4</td>--%>
                  <%--<td>4</td>--%>
                  <%--<td>20</td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                  <%--<td style="background:#ececec;">2016/6/3</td>--%>
                  <%--<td>4</td>--%>
                  <%--<td>2</td>--%>
                  <%--<td>2</td>--%>
                  <%--<td>0</td>--%>
                  <%--<td>2</td>--%>
                  <%--<td>2</td>--%>
                  <%--<td>12</td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                  <%--<td style="background:#ececec;">2016/6/4</td>--%>
                  <%--<td>4</td>--%>
                  <%--<td>4</td>--%>
                  <%--<td>6</td>--%>
                  <%--<td>6</td>--%>
                  <%--<td>2</td>--%>
                  <%--<td>0</td>--%>
                  <%--<td>22</td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                  <%--<td style="background:#ececec;">2016/6/5</td>--%>
                  <%--<td>4</td>--%>
                  <%--<td>4</td>--%>
                  <%--<td>6</td>--%>
                  <%--<td>0</td>--%>
                  <%--<td>2</td>--%>
                  <%--<td>4</td>--%>
                  <%--<td>20</td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                  <%--<td style="background:#ececec;">2016/6/6</td>--%>
                  <%--<td>0</td>--%>
                  <%--<td>2</td>--%>
                  <%--<td>4</td>--%>
                  <%--<td>0</td>--%>
                  <%--<td>2</td>--%>
                  <%--<td>4</td>--%>
                  <%--<td>12</td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                  <%--<td style="background:#ececec;">2016/6/7</td>--%>
                  <%--<td>4</td>--%>
                  <%--<td>4</td>--%>
                  <%--<td>6</td>--%>
                  <%--<td>0</td>--%>
                  <%--<td>6</td>--%>
                  <%--<td>4</td>--%>
                  <%--<td>22</td>--%>
                <%--</tr>--%>
                <%--<tr>--%>
                  <%--<td style="background:#ececec;">2016/6/8</td>--%>
                  <%--<td>4</td>--%>
                  <%--<td>4</td>--%>
                  <%--<td>6</td>--%>
                  <%--<td>6</td>--%>
                  <%--<td>2</td>--%>
                  <%--<td>4</td>--%>
                  <%--<td>26</td>--%>
                <%--</tr>--%>
              <%--</table>--%>
            <%--</div>--%>
          </div>
          <!--================值班记录统计end=================-->
          <!--================值班薪酬管理start=================-->
          <div class="zb-pay-gl" id="tab-ZBXCGL">
            <div class="zb-jl-title">
              <div class="zhiban-title">
                <span>时间：</span><input class="Wdate" type="text" id="sTime2" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})">
                <i>-</i>
                <input class="Wdate" type="text" id="bTime2" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})">
                <%--<select id="projectid2">--%>
                  <%--<option value="">全部</option>--%>
                  <%--<c:forEach items="${dutyProject2}" var="pro">--%>
                    <%--<option value="${pro.id}">${pro.content}</option>--%>
                  <%--</c:forEach>--%>
                <%--</select>--%>
                <span>姓名：</span>
                <input type="text" class="zhiban-name" id="userName2">
                <button class="zhiban-qr sure1">确认</button>
              </div>
            </div>
            <div class="zb-pay-table clearfix">
              <a href="javascript:;" class="zb-table-dc export-duty1">导出</a>
              <table class="zb-table-pay">

              </table>
              <script type="text/template" id="zb-table-pay_templ">
                <tr>
                  <th style="width:13%;">值班日期</th>
                  <th style="width:13%;">值班人员</th>
                  <th style="width:13%;">值班时间</th>
                  <th style="width:16%;">值班项目</th>
                  <th style="width:12%;">计划时长(h)</th>
                  <th style="width:19%;">出勤</th>
                  <th style="width:13%;">值班薪酬(元)</th>
                </tr>
                {{ if(it.rows.length>0){ }}
                {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                {{var obj=it.rows[i];}}
                <tr puid="{{=obj.id}}">
                  <td style="background:#ececec;">{{=obj.date}}</td>
                  <td>{{=obj.userName}}</td>
                  <td>{{=obj.timeDuan}}</td>
                  <td>{{=obj.projectName}}</td>
                  <td>{{=obj.times}}</td>
                  <td>{{=obj.checkIn}}-{{=obj.checkOut}}</td>
                  <td><input type="text" value="{{=obj.salary}}" class="zb-pay-num"></td>
                </tr>
                {{ } }}
                {{ } }}
              </script>

            </div>
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
          <!--================值班薪酬管理end=================-->
          <!--================使用模板创建本周计划start=================-->
          <div class="zb-plan-alert">
            <div class="zb-set-title clearfix">
              <p>节假日安排模板</p>
              <span class="zb-set-close">X</span>
            </div>
            <div class="zb-plan-main">
              <span>选择模板：</span>
              <select id="plan-model">
              </select>
              <script type="text/template" id="plan-model_templ">
                {{ if(it.rows.length>0){ }}
                {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                {{var obj=it.rows[i];}}
                <option value="{{=obj.id}}">{{=obj.name}}</option>
                {{ } }}
                {{ } }}
              </script>
            </div>
            <div class="zb-alert-btn">
              <button class="zb-btn-sure sure-plan">确定</button>
              <button class="zb-btn-qx">取消</button>
            </div>
          </div>
          <!--================使用模板创建本周计划end=================-->
          <!--================保存为模板start=================-->
          <div class="zb-model-alert">
            <div class="zb-set-title clearfix">
              <p>保存为模板</p>
              <span class="zb-set-close">X</span>
            </div>
            <input hidden="hidden" id="modelId">
            <div class="zb-model-main">
              <span>模板名称：</span><input type="text" id="modelname">
            </div>
            <div class="zb-alert-btn">
              <button class="zb-btn-sure sure-model">确定</button>
              <button class="zb-btn-qx">取消</button>
            </div>
          </div>
          <!--================保存为模板end=================-->
          <!--================值班人员弹窗start=================-->
          <div class="zb-ren-alert">
            <div class="zb-set-title clearfix">
              <p>设置值班班人员</p>
              <span class="zb-set-close">X</span>
            </div>
            <input hidden="hidden" id="index">
            <input hidden="hidden" id="dt-type">
            <div class="zb-ren-main">
              <ul class="zb-set-list">
                <li><span>值班日期:</span><em id="info1"></em></li>
                <li><span>值班时段:</span><em id="info2"></em></li>

                <li><span>值班项目:</span>
                  <select id="project1">
                    <c:forEach items="${orgDutyProject}" var="pro">
                      <option value="${pro.id}">${pro.content}</option>
                    </c:forEach>
                  </select>
                  <select id="project2">
                    <c:forEach items="${dutyProject}" var="pro2">
                      <option value="${pro2.id}">${pro2.content}</option>
                    </c:forEach>
                  </select>
                </li>
                <li>
                  <span style="vertical-align:top;">值班人员:</span>
                  <div style="display:inline-block;width:300px;" class="dtuser">
                    <a href="javascript:;" class="zb-ren-add" uid="">&nbsp;</a>
                  </div>
                </li>
              </ul>
              <div class="zb-alert-btn">
                <button class="zb-btn-sure sure-duty">确定</button>
                <button class="zb-btn-qx">取消</button>
              </div>
            </div>
          </div>
          <!--================值班人员弹窗end=================-->
          <!--==================设置替班人员弹窗start====================-->
          <div class="zb-set-alert">
            <div class="zb-set-title clearfix">
              <p>设置替班人员</p>
              <span class="zb-set-close">X</span>
            </div>
            <input hidden="hidden" id="dsid">
            <div class="zb-set-main">
              <ul class="zb-set-list">
                <li><span>申请换班:</span><em id="username4">张丽华</em></li>
                <li><span>值班日期:</span><em id="date4">2016/6/22</em></li>
                <li><span>值班时间:</span><em id="dateDesc4">晚间</em></li>
                <li><span>值班项目:</span><em id="project4">日常值班</em></li>
                <li><span>换班理由:</span><em id="cause4" >小孩感冒发烧</em></li>
                <li><span>替班人员:</span>
                  <select id="dutyUser2">

                  </select>
                  <script type="text/template" id="dutyUser2_templ">
                    {{ if(it.rows.length>0){ }}
                    {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                    {{var obj=it.rows[i];}}
                    <option value="{{=obj.id}}">{{=obj.userName}}</option>
                    {{ } }}
                    {{ } }}
                  </script>
                </li>
              </ul>
              <div class="zb-alert-btn">
                <button class="zb-btn-sure sure-shift">确定</button>
                <button class="zb-btn-qx">取消</button>
              </div>
            </div>
          </div>
          <!--==================设置替班人员弹窗end====================-->
          <!--==================新增值班项目弹窗start====================-->
          <div class="zb-set-newadd">
            <div class="zb-set-title clearfix">
              <p>新增值班项目</p>
              <span class="zb-set-close">X</span>
            </div>
            <div class="zb-newadd-main">
              <div class="zb-new-txt">
                <p>请在”上级“中选择父级类别，并在”值班项目“中输入值班项目名称,若上级选择”无“则作为一级类别展示</p>
                <span>上级</span>
                <select id="projectlist">

                </select>
                <script type="text/template" id="projectlist_templ">
                  <option value="">无</option>
                  {{ if(it.rows.length>0){ }}
                  {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                  {{var obj=it.rows[i];}}
                  <option value="{{=obj.id}}">{{=obj.content}}</option>
                  {{ } }}
                  {{ } }}
                  </script>
                <span>值班项目</span>
                <input id="dutyProjectId" hidden="hidden">
                <input type="text" placeholder="输入值班项目名称" id="conn">
              </div>
              <div class="zb-alert-btn">
                <button class="zb-btn-sure sure-project">确定</button>
                <button class="zb-btn-qx">取消</button>
              </div>
            </div>
          </div>
          <!--==================设置start====================-->
          <div class="zhiban-set" id="tab-ZBSZ">
            <div class="zhiban-set-title clearfix">
              <ul>
                <li class="zb-active" id="ZBREN">值班人员设置</li>
                <li id="ZBSD">值班时段设置</li>
                <li id="ZBXM">值班项目设置</li>
                <li id="ZBTIME">记录填报时间设置</li>
                <li id="ZBIP">值班IP设置</li>
              </ul>
            </div>
            <div class="zb-set">
              <!--==================记录填报时间设置start====================-->
              <div class="zhiban-set-time" id="tab-ZBTIME">
                <ul>
                  <%--<li>--%>
                    <%--<label>--%>
                      <%--<input type="radio" name="zhiban-time" checked value="1"><span class="zb-time-type">值班前</span>--%>
                      <%--<input type="text" class="zb-time-txt" value="" id="min1"><em>分钟</em>--%>
                    <%--</label>--%>
                  <%--</li>--%>
                  <li>
                    <label>
                        <input type="radio" name="zhiban-time" checked value="1">
                      <span class="zb-time-type">值班后</span>
                        <input type="text" class="zb-time-txt" value="" id="min2">
                      <em>分钟</em>
                    </label>
                  </li>
                  <li>
                    <label>
                        <input type="radio" name="zhiban-time" checked value="2">
                      <span class="zb-time-type">当天</span>
                    </label>
                  </li>
                  <li>
                    <label>
                        <input type="radio" name="zhiban-time" checked value="3">
                      <span class="zb-time-type">本周</span>
                    </label>
                  </li>
                  <li>
                    <label>
                        <input type="radio" name="zhiban-time" checked value="4">
                      <span class="zb-time-type">本月</span>
                    </label>
                  </li>
                </ul>
              </div>
              <!--==================记录填报时间设置end====================-->
              <!--==================值班IP配置start====================-->
              <div class="zhiban-set-ip clearfix" id="tab-ZBIP" style="display: none;">
                ip设定：<input id="ipset">
                <button id="sure-ip">保存</button>
                </div>
              <!--==================值班IP配置end====================-->
              <!--==================值班人员配置start====================-->
              <div class="zhiban-set-ren clearfix" id="tab-ZBREN">
                <a href="javascript:;" class="zhiban-set-new add-users">新增</a>
                <div class="zb-set-ren">

                </div>
                <script type="text/template" id="user_templ">
                  {{ if(it.userlist.length>0){ }}
                  {{ for (var i = 0, l = it.userlist.length; i < l; i++) { }}
                  {{var obj=it.userlist[i];}}
                  <span class="zb-set-active">{{=obj.userName}}
                    <img src="/static_new/images/zhiban/zhiban-2.png" uid="{{=obj.id}}" class="user-del">
                  </span>
                  {{ } }}
                  {{ } }}
                  </script>
              </div>
              <!--==================值班人员配置end====================-->
              <!--==================值班时段设置start====================-->
              <div class="zhiban-set-sd clearfix" id="tab-ZBSD">
                <a href="javascript:;" class="zhiban-set-new zbsdsd">新增</a>
                <div class="zb-set-sd">
                  <ul class="zbsdsd_ul">

                  </ul>
                  <script type="text/template" id="zbsdsd_ul_templ">
                    {{ if(it.dutyTimes.length>0){ }}
                    {{ for (var i = 0, l = it.dutyTimes.length; i < l; i++) { }}
                    {{var obj=it.dutyTimes[i];}}
                    <li did="{{=obj.id}}">
                      <input class="set-sd-type" id="set-nm" type="text" value="{{=obj.timeDesc}}" disabled>
                      <input type="text" class="zb-input" id="zb-starttime" value="{{=obj.startTime}}" onfocus="WdatePicker({dateFmt:'HH:mm'})" class="Wdate Wdate-enter" disabled>
                      <em> - </em>
                      <input type="text" class="zb-input" value="{{=obj.endTime}}" onfocus="WdatePicker({dateFmt:'HH:mm'})" id="zb-endtime" class="Wdate Wdate-enter" disabled>
                      <div class="zb-set-cz" did="{{=obj.id}}">
                        <a class="zb-set-edit zb-set-sd-edit">编辑</a>
                        <i>|</i>
                        <a class="zb-set-del zb-set-sd-del">删除</a>
                      </div>
                    </li>
                    {{ } }}
                    {{ } }}
                  </script>
                </div>
              </div>
              <!--==================值班时段设置end====================-->
              <!--==================值班项目设置start====================-->
              <div class="zhiban-set-xm clearfix" id="tab-ZBXM">
                <a href="javascript:;" class="zhiban-set-new add-project">新增</a>
                <table class="zb-set-xm">

                </table>
                <script type="text/template" id="zb-set-xm_templ">
                  <tr>
                    <th style="width:25%;">分类</th>
                    <th style="width:45%;">值班项目</th>
                    <th style="width:30%;">操作</th>
                  </tr>
                  {{ if(it.dutyProjectDTOs.length>0){ }}
                  {{ for (var i = 0, l = it.dutyProjectDTOs.length; i < l; i++) { }}
                  {{var obj=it.dutyProjectDTOs[i];}}
                    {{?obj.count==0}}
                      <tr>
                        <td style="background:#ececec">{{=obj.content}}</td>
                        <td></td>
                        <td>
                          <div class="project-dis">
                          <a href="javascript:;" class="zb-set-edit pro-edit" dpoid="{{=obj.id}}" dpcn="" dpid="">编辑</a>
                          <i>|</i>
                          <a href="javascript:;" class="zb-set-del pro-del" dpoid="{{=obj.id}}" dpid="">删除</a>
                          </div>
                        </td>
                      </tr>
                  {{??}}
                  {{~obj.dutyProject:value:index}}
                  <tr>
                    {{?value.type==1}}
                    <td rowspan='{{=value.count}}' style="background:#ececec">{{=value.orgContent}}</td>
                    {{?}}
                    <td>{{=value.content}}</td>
                    <td>
                      <div class="project-dis">
                      <a href="javascript:;" class="zb-set-edit pro-edit" dpoid="{{=value.orgId}}" dpcn="{{=value.content}}" dpid="{{=value.id}}">编辑</a>
                      <i>|</i>
                      <a href="javascript:;" class="zb-set-del pro-del" dpid="{{=value.id}}" dpoid="">删除</a>
                      </div>
                    </td>
                  </tr>
                  {{~}}
                  {{?}}
                  {{ } }}
                  {{ } }}
                  </script>
              </div>
              <!--==================值班项目设置end====================-->
            </div>
          </div>
          <!--==================设置end====================-->
        </div>
      </div>
    </div>
    <!--/.tab-col右侧-->

  </div>
  <!--/.col-right-->

</div>
<!--/#content-->
</div>
</div>
<!--=================新增值班人员弹窗框start===================-->
<div class="popup-XZZB">
  <div class="popup-XZZB-top">
    <em>值班记录</em><i class="h-cursor SQ-X">X</i>
  </div>
  <div class="popup-XZZB-main">
    <input hidden="hidden" id="poptype">
    <dl>
      <dd>
        <em>选择值班人员：</em>
        <select id="dutyUser">
        </select>
        <script type="text/template" id="dutyUser_templ">
          {{ if(it.rows.length>0){ }}
          {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
          {{var obj=it.rows[i];}}
          <option value="{{=obj.id}}">{{=obj.userName}}</option>
          {{ } }}
          {{ } }}
          </script>
      </dd>
    </dl>
  </div>
  <div class="popup-XZZB-bottom">
    <span class="XZZB-TJ h-cursor">提交</span>
    <span class="XZZB-QX h-cursor">取消</span>
  </div>
</div>
<!--=================新增值班人员弹窗框end===================-->
<!--=================申请换班弹窗框start===================-->
<div class="popup-HB request-huanban">
  <div class="popup-HB-top">
    <em>申请换班</em><i class="h-cursor SQ-X">X</i>
  </div>
  <div class="popup-HB-main">
    <dl>
      <input hidden="hidden" id="dutyId2">
      <dd>
        <em>申请人：</em><label>${sessionValue.userName}</label>
      </dd>
      <dd>
        <em>值班日期：</em><label id="timeduan">8:00-12:00</label>
      </dd>
      <dd>
        <em>值班时段：</em><label id="time">晚间</label>
      </dd>
      <dd>
        <em>值班项目：</em><label id="project">日常值班</label>
      </dd>
      <dd>
        <em>换班理由：</em>
        <textarea id="cause"></textarea>
      </dd>
    </dl>
  </div>
  <div class="popup-HB-bottom">
    <span class="HB-TJ h-cursor shift-submit">提交</span>
    <span class="HB-QX h-cursor">取消</span>
  </div>
</div>
<!--=================申请换班弹窗框end===================-->
<!--=================申请换班详细弹窗框start===================-->
<div class="popup-HB request-huanban2">
  <div class="popup-HB-top">
    <em>申请换班详细</em><i class="h-cursor SQ-X">X</i>
  </div>
  <div class="popup-HB-main">
    <dl>
      <dd>
        <em>申请人：</em><label id="username5">${sessionValue.userName}</label>
      </dd>
      <dd>
        <em>值班日期：</em><label id="timeduan2">8:00-12:00</label>
      </dd>
      <dd>
        <em>值班时段：</em><label id="time2">晚间</label>
      </dd>
      <dd>
        <em>值班项目：</em><label id="project5">日常值班</label>
      </dd>
      <dd>
        <em>换班理由：</em>
        <textarea id="cause2" disabled="disabled"></textarea>
      </dd>
    </dl>
  </div>
  <div class="popup-HB-bottom">
    <span class="HB-QX h-cursor">确定</span>
  </div>
</div>
<!--=================申请换班弹窗框end===================-->
<!--=================值班记录弹窗框start===================-->
<div class="popup-ZBJL">
  <div class="popup-HB-top">
    <em>值班记录</em><i class="h-cursor SQ-X">X</i>
  </div>
  <input hidden="hidden" id="dutyUserId">
  <div class="popup-HB-main">
    <dl>
      <dd>
        <em>值班日期：</em><label id="timeDuan6">8:00-12:00</label>
      </dd>
      <dd>
        <em>值班时段：</em><label id="timeDesc6">晚间</label>
      </dd>
      <dd>
        <em>值班项目：</em><label id="project6">日常值班</label>
      </dd>
      <dd>
        <em>值班地点：</em><label id="localip">192.168.11.111</label>
      </dd>
      <dd>
        <em>值班记录：</em>
        <textarea id="dutylog"></textarea>
      </dd>
      <dd>
        <label for="file_attach" style="cursor: pointer">
        <em  class="popup-TJ h-cursor"></em>
        </label> <img src="/img/loading4.gif" id="fileuploadLoading"
                      style="display: none;" />
        <div style="width: 0; height: 0; overflow: visible">
          <input id="file_attach" type="file" name="file" value="添加附件"
                 size="1" style="width: 0; height: 0; opacity: 0">
        </div>
        <div class="popup-bottom-LAB">
        </div>
      </dd>
    </dl>
  </div>
  <div class="popup-HB-bottom">
    <span class="HB-TJ h-cursor submit-log">提交</span>
    <span class="HB-QX h-cursor">取消</span>
  </div>
</div>
<!--=================值班记录弹窗框end===================-->
<!--================值班说明弹出框start==================-->
<div class="popup-ZBSM">
    <div class="popup-ZBSM-top">
        <em>值班说明编辑弹出框</em><i class="h-cursor SQ-X">X</i>
    </div>
    <div class="popup-mian">
        <em>值班说明：</em>
        <textarea id="explain"></textarea>
        <div>
            <span id="sure-explain">确定</span>
        </div>
    </div>
</div>
<!--================值班说明弹出框end====================-->

<div class="zhiban-meng"></div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('zhiban');
</script>

</body>
</html>