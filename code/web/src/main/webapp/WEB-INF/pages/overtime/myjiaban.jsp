<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@page import="com.pojo.app.SessionValue"%>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>加班</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link rel="stylesheet" type="text/css" href="/static_new/css/zhiban/jiaban.css"/>
  <!-- jquery artZoom4Liaoba styles -->
  <!-- Custom styles -->
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>
<body>

<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

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

    <!--.tab-col右侧-->
    <!--.tab-col右侧-->
    <div class="tab-col">

      <div class="tab-head clearfix">
        <ul>
          <c:if test="${roles:isHeadmaster(sessionValue.userRole)||roles:isManager(sessionValue.userRole)}">
          <li><a href="/overTime/jiaban.do?index=5&version=23&type=1" >加班申请管理</a><em></em></li>
          <li><a href="/overTime/jiaban.do?index=5&version=23&type=2" >加班薪酬管理</a><em></em></li>
          <li><a href="/overTime/jiaban.do?index=5&version=23&type=3" >加班审核</a><em></em></li>
          <li><a href="/overTime/jiaban.do?index=5&version=23&type=4" >教师加班</a><em></em></li>
          </c:if>
          <li id="LWDJB"><a href="javascript:;" >我的加班</a><em></em></li>
          <li id="LJBXC"><a href="javascript:;" >加班薪酬</a><em></em></li>

        </ul>
      </div>

      <div class="tab-main">
        <!--================我的加班start=================-->
        <div id="tab-LWDJB" class="tab-LWDJB">
          <div class="LWDJB-top">
            <div class="LWDJB-top-left">
              <span>当前时间</span><em id="curtime">2016/11/11</em>
            </div>
            <div class="LWDJB-top-right">
              <span class="h-cursor view-log">查看加班记录</span>
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
              <tbody class="myOverTime">

              </tbody>
              <script type="text/template" id="myOverTime_templ">
                {{ if(it.rows.length>0){ }}
                {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                {{var obj=it.rows[i];}}
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
                  <td widht="565">
                    {{~obj.overTimeDTOs:value:index}}
                    <div class="LWDJB-TX-RC" style="float: left;">
                      <em>{{=value.timeDuan}}加班事由：{{=value.cause}}</em>
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
        <!--================我的加班end=================-->
        <!--================查看加班记录start=================-->
        <div class="zb-lookjl">
          <div class="zb-jl-title">
            <a href="javascript:;" class="zhiban-back">&lt;返回我的加班</a>
            <div class="zhiban-title">
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
              <span class="zhiban-qr">查询</span>
            </div>
          </div>
          <div class="zb-table-jl clearfix">
            <table class="zb-jl-table">
              <thead>
              <tr>
                <th style="width:16%;">加班日期</th>
                <th style="width:16%;">加班时间</th>
                <th style="width:16%;">加班事由</th>
                <th style="width:30%;">加班记录</th>
                <th style="width:22%;">加班附件</th>
              </tr>
             </thead>
              <tbody class="jiaban-log">

              </tbody>
              <script type="text/template" id="jiaban-log_templ">
                {{ if(it.rows.length>0){ }}
                {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                {{var obj=it.rows[i];}}
                <tr>
                  <td style="background:#ececec;">{{=obj.date}}</td>
                  <td>{{=obj.timeDuan}}</td>
                  <td>{{=obj.cause}}</td>
                  <td>{{=obj.content}}</td>
                  <td>
                  {{~obj.lessonWareList:value:index}}
                  <div class="zhiban-FJ"><em>{{=value.name}}</em><a pth="{{=value.path}}" class="view">预览</a><a href="{{=value.path}}" target='_blank'>下载</a></div>
                  {{~}}
                  </td>
                </tr>
                {{ } }}
                {{ } }}
              </script>
            </table>
          </div>
        </div>
        <!--================查看加班记录end=================-->
        <!--===============加班薪酬start=================-->
        <div class="tab-LJBXC" id="tab-LJBXC">
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
                <th width="150">加班日期</th>
                <th width="115">加班时间</th>
                <th width="245">加班内容</th>
                <th width="245">加班时长(h)</th>
                <th width="150">加班薪酬(元)</th>
              </tr>
              </thead>
              <tbody class="myJiaBanSalary">

              </tbody>
              <script type="text/template" id="myJiaBanSalary_templ">
                {{ if(it.rows.length>0){ }}
                {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                {{var obj=it.rows[i];}}
                <tr>
                  <td class="JBSQ-td" width="150">{{=obj.date}}</td>
                  <td width="115">{{=obj.timeDuan}}</td>
                  <td width="245">{{=obj.cause}}</td>
                  <td width="245">{{=obj.times}}</td>
                  <td width="150">
                    {{=obj.salary}}
                  </td>
                </tr>
                {{ } }}
                {{ } }}
              </script>
            </table>
          </div>
        </div>
        <!--================加班薪酬管理end=================-->
      </div>
    </div>
    <!--/.tab-col右侧-->

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
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('myjiaban');
</script>

</body>
</html>