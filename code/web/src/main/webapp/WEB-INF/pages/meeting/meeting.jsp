<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>我的会务/活动</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link rel="stylesheet" type="text/css" href="/static_new/css/meeting/hyandhd.css">
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
  <script language="javascript" type="text/javascript" src="/static_new/js/modules/calendar/0.1.0/WdatePicker.js"></script>
  <script type="text/javascript">
    var nb_username = '${sessionValue.userName}';
    var userId =  '${sessionValue.id}';
  </script>
</head>
<body>
<!--#head-->
<%@ include file="../common_new/head.jsp"%>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

  <!--.col-left-->
  <%@ include file="../common_new/col-left.jsp" %>
  <!--/.col-left-->
  <!-- 广告部分 -->
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
    <input hidden="hidden" id="mtype">
    <!--.tab-col右侧-->
    <div class="tab-col">
      <div class="tab-head clearfix">
        <ul>
          <li class="cur" id="MYHD"><a href="javascript:;" >我的会务/活动</a><em></em></li>
          <li id="HWSP"><a href="javascript:;" >会务审批</a><em></em></li>
          <li id="HYCD"><a href="javascript:;" >会议存档管理</a><em></em></li>
        </ul>
      </div>
      <div class="tab-main">
        <!--==========================管理员首页start==========================-->
        <div class="gly-index" id="tab-HWSP">
          <div class="hyandhd-title">
            <span class="hyandhd-time">时间：</span><input class="Wdate" type="text" onfocus="WdatePicker({dateFmt:'yyyy/MM/dd'})" id="stime">
            <i>-</i>
            <input class="Wdate" type="text" onfocus="WdatePicker({dateFmt:'yyyy/MM/dd'})" id="etime">
            <select id="status">
              <option value="3">全部</option>
              <option value="0">待审批</option>
              <option value="1">通过审批</option>
              <option value="2">驳回</option>
            </select>
            <input id="keyword" class="hyandhd-key" type="text" placeholder="输入搜索关键词">
            <button id="submit-meeting">确认</button>
          </div>
          <table class="hyandhd-table">
            <thead>
              <tr>
                <th style="width:18%">名称</th>
                <th style="width:22%">议题</th>
                <th style="width:10%">状态</th>
                <th style="width:10%">申请人</th>
                <th style="width:24%">起止时间</th>
                <th style="width:16%">操作</th>
              </tr>
            </thead>
            <tbody id="meeting-table1">

            </tbody>
            <script type="text/template" id="meeting-table_templ1">
              {{ if(it.rows.length>0){ }}
              {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
              {{var obj=it.rows[i];}}
              <tr>
                <td style="background:#ececec;">{{=obj.name}}</td>
                <td>{{=obj.issue}}</td>
                <td>{{?obj.approvalType==0}}
                  待审批
                  {{??obj.approvalType==1}}
                  通过审批
                  {{??obj.approvalType==2}}
                  驳回
                  {{?}}
                </td>
                <td>{{=obj.userName}}</td>
                <td>{{=obj.time}}</td>
                <td>
                  <a href="javascript:;" class="hyandhd-xq shehe-detail" mid="{{=obj.id}}">详情</a>
                  {{?obj.approvalType==0}}
                  <em style="color:#d2d2d2;">|</em>
                  <a href="javascript:;" class="hyandhd-sp" mid="{{=obj.id}}">审批</a>
                  {{?}}
                </td>
              </tr>
              {{ } }}
              {{ } }}
            </script>
          </table>
        </div>
        <!--==========================管理员首页end==========================-->
        <!--==========================会议存档管理start==========================-->
        <div class="hyandhd-save" id="tab-HYCD">
          <div class="hyandhd-title">
            <span class="hyandhd-time">时间：</span><input class="Wdate" type="text" onfocus="WdatePicker({dateFmt:'yyyy/MM/dd'})" id="stime2">
            <i>-</i>
            <input class="Wdate" type="text" onfocus="WdatePicker({dateFmt:'yyyy/MM/dd'})" id="etime2">
            <%--<select id="status2">--%>
              <%--<option value="0">全部</option>--%>
              <%--<option value="1">未审核</option>--%>
              <%--<option value="2">未开始</option>--%>
              <%--<option value="3">进行中</option>--%>
              <%--<option value="4">结束</option>--%>
            <%--</select>--%>
            <input class="hyandhd-key" type="text" placeholder="输入搜索关键词" id="keyword2">
            <button id="submit-meeting2">确认</button>
          </div>
          <table class="hyandhd-table">
            <thead>
              <tr>
                <th style="width:20%">名称</th>
                <th style="width:26%">议题</th>
                <th style="width:10%">申请人</th>
                <th style="width:28%">起止时间</th>
                <th style="width:16%">操作</th>
              </tr>
            </thead>
            <tbody id="meeting-table2">

            </tbody>
            <script type="text/template" id="meeting-table_templ2">
              {{ if(it.rows.length>0){ }}
              {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
              {{var obj=it.rows[i];}}
              <tr>
                <td style="background:#ececec;">{{=obj.name}}</td>
                <td>{{=obj.issue}}</td>
                <td>{{=obj.userName}}</td>
                <td>{{=obj.time}}</td>
                <td>
                  <a href="javascript:;" class="hyandhd-xq log-detail" mid="{{=obj.meetId}}">详情</a>
                  <em style="color:#d2d2d2;">|</em>
                  <a href="javascript:;" class="hyandhd-del del-log" id="{{=obj.id}}">删除</a>
                </td>
              </tr>
              {{ } }}
              {{ } }}
            </script>
          </table>
        </div>
        <!--==========================会议存档管理end==========================-->
        <!--==========================我的会务/活动start==========================-->
        <div class="hyandhd-meeting" id="tab-MYHD">
          <div class="hyandhd-title">
            <span class="hyandhd-time">时间：</span><input class="Wdate" type="text" onfocus="WdatePicker({dateFmt:'yyyy/MM/dd'})" id="stime3">
            <i>-</i>
            <input class="Wdate" type="text" onfocus="WdatePicker({dateFmt:'yyyy/MM/dd'})" id="etime3">
            <select id="status3">
              <option value="0">全部</option>
              <option value="1">审核中</option>
              <option value="2">未开始</option>
              <option value="3">进行中</option>
              <option value="4">结束</option>
            </select>
            <input class="hyandhd-key" type="text" placeholder="输入搜索关键词" id="keyword3">
            <button id="submit-meeting3">确认</button>
          </div>
          <div class="hyandhd-operate clearfix">
            <c:if test="${roles:isHeadmaster(sessionValue.userRole)||roles:isManager(sessionValue.userRole)}">
            <a href="javascript:;" class="model-manage">模板管理</a>
            <a href="javascript:;" class="hyandhd-new">新建</a>
            </c:if>
          </div>
          <table class="hyandhd-table">
            <thead>
            <tr>
              <th style="width:20%;">名称</th>
              <th style="width:24%;">议题</th>
              <th style="width:12%;">状态</th>
              <th style="width:24%;">起始时间</th>
              <th style="width:20%;">操作</th>
            </tr>
            </thead>
            <tbody id="meeting-table">

            </tbody>
            <script type="text/template" id="meeting-table_templ">
              {{ if(it.rows.length>0){ }}
              {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
              {{var obj=it.rows[i];}}
              <tr>
                <td style="background:#ececec;">{{=obj.name}}</td>
                <td>{{=obj.issue}}</td>
                <td>

                        {{?obj.status==1}}
                     <span class="hyandhd-shz">
                        审核中
                    </span>

                        {{??obj.status==2}}
                     <span class="hyandhd-wks">
                         未开始
                    </span>

                       {{??obj.status==3}}
                      <span class="hyandhd-jxz">
                       进行中
                    </span>

                      {{??obj.status==4}}
                       <span class="hyandhd-end">
                       结束
                    </span>
                  {{?}}
                </td>
                <td>{{=obj.time}}</td>
                <td>
                  {{?obj.status==1}}
                  <span class="hyandhd-xq detail-meeting" mid="{{=obj.id}}">详情</span>
                  <em>|</em>
                  <span class="hyandhd-edit edit-meeting" mid="{{=obj.id}}">编辑</span>
                  {{??obj.status==2}}
                  <span class="meeting-tj" mid="{{=obj.id}}">提交材料</span>
                  {{??obj.status==3}}
                  <span class="meeting-join" mid="{{=obj.id}}">加入会议</span>
                  {{??obj.status==4}}
                  <span class="meeting-ck" mid="{{=obj.id}}">查看会议记录</span>
                  {{?}}
                </td>
              </tr>
              {{ } }}
              {{ } }}
            </script>

          </table>
        </div>
        <!--==========================我的会务/活动end==========================-->
        <!--==========================新建会务/活动start==========================-->
        <div class="newhw-alert">
          <div class="newhw-show">
            <div class="newhw-title clearfix">
              <a href="javascript:;" class="hw-back">&lt;返回我的会务活动列表</a>
              <span id="meet-name"></span>
              <input hidden="hidden" id="meetId2">
            </div>
            <div class="name-model">
              <span class="alert-name"><em>*</em>名称</span>
              <input type="text" placeholder="输入会议名称" id="meetname">
              <span class="alert-name">使用模板</span>
              <select id="model-sel">

              </select>
            </div>
            <div class="type-select">
              <span class="alert-name"><em>*</em>类型</span>
              <label><input type="radio" name="m-type" id="m-type" value="0" checked>&nbsp;会务</label>
              <label><input type="radio" name="m-type" id="m-type"  value="1">&nbsp;活动</label>
            </div>
            <div class="time-start-end">
              <span class="alert-name"><em>*</em>起止时间</span>
              <input class="Wdate" type="text" onfocus="WdatePicker({dateFmt:'yyyy/MM/dd'})" id="datetime">
              <input type="text" placeholder="00:00" class="time-start" onfocus="WdatePicker({dateFmt:'HH:mm'})" id="starttime">
              <i>-</i>
              <input type="text" placeholder="00:00"class="time-end" onfocus="WdatePicker({dateFmt:'HH:mm'})" id="endtime">
            </div>
            <div class="meeting-shiyou">
              <span class="alert-name"><em>*</em>会议事由</span>
              <input type="text" id="cause">
            </div>
          </div>
          <div class="newhw-hide">
            <div class="meeting-yc">
              <span class="alert-name">会务议程</span>
              <textarea id="process"></textarea>
            </div>
            <div class="meeting-chry">
              <span class="alert-name" style="vertical-align:top;">参会人员</span>
              <div class="meeting-dv">
                <span class="meeting-rad" uid=""></span>
              </div>
            </div>
            <div class="fyr-turn">
              <span class="alert-name">发言人顺序</span>
              <textarea id="order"></textarea>
            </div>
            <div class="meeting-yt">
              <span class="alert-name">议题</span>
              <input type="text" id="issue">
            </div>
            <div class="meeting-cail">
              <span class="alert-name" style="vertical-align:top;">会议材料</span>
              <%--<button class="meeting-upload-btn">上传</button>--%>
              <span class="iconvideo">
                            上传
                <input type="file" name="file_upload" id="file_upload"/>
                </span>
              <img src="/img/loading4.gif" id="picuploadLoading"/>
              <p>(支持图片、文档、视频等)</p>
              <ul class="upload-mlist">

              </ul>
            </div>
            <div class="meeting-spr">
              <span class="alert-name">审批人</span>
              <select id="approvalUserId">
                <option value="">无</option>
                <c:forEach items="${user}" var="u">
                  <option value="${u.id}">${u.userName}</option>
                </c:forEach>
              </select>
            </div>
          </div>
          <div class="alert-btn">
            <button class="alert-btn-submit save-meeting">提交</button>
            <button class="alert-btn-bcwmb save-meeting-model">保存为模板</button>
            <button class="alert-btn-qx qx-xj-meeting">取消</button>
          </div>
        </div>
        <!--==========================新建会务/活动end==========================-->
        <!--==========================会务/活动start==========================-->
        <div class="newhw-alert2">
          <div class="newhw-show">
            <div class="newhw-title clearfix">
              <a href="javascript:;" class="hw-back">&lt;返回我的会务活动列表</a>
              <span>会务/ 活动详情</span>
            </div>
            <div class="name-model">
              <span class="alert-name"><em>*</em>名称</span>
              <input type="text" placeholder="输入会议名称" id="meetname2" disabled>
            </div>
            <div class="type-select">
              <span class="alert-name"><em>*</em>类型</span>
              <label><input type="radio" name="m-type2" value="0" checked disabled>&nbsp;会务</label>
              <label><input type="radio" name="m-type2" value="1" disabled>&nbsp;活动</label>
            </div>
            <div class="time-start-end">
              <span class="alert-name"><em>*</em>起止时间</span>
              <input class="Wdate" type="text" onfocus="WdatePicker({dateFmt:'yyyy/MM/dd'})" id="datetime2" disabled>
              <input type="text" placeholder="00:00" class="time-start" onfocus="WdatePicker({dateFmt:'HH:mm'})" id="starttime2" disabled>
              <i>-</i>
              <input type="text" placeholder="00:00"class="time-end" onfocus="WdatePicker({dateFmt:'HH:mm'})" id="endtime2" disabled>
            </div>
            <div class="meeting-shiyou">
              <span class="alert-name"><em>*</em>会议事由</span>
              <input type="text" id="cause2" disabled>
            </div>
          </div>
          <div class="newhw-hide">
            <div class="meeting-yc">
              <span class="alert-name">会务议程</span>
              <textarea id="process2" disabled></textarea>
            </div>
            <div class="meeting-chry">
              <span class="alert-name" style="vertical-align:top;">参会人员</span>
              <div class="meeting-dv2">
                <%--<span class="meeting-rad" uid=""></span>--%>
              </div>
            </div>
            <div class="fyr-turn">
              <span class="alert-name">发言人顺序</span>
              <textarea id="order2" disabled></textarea>
            </div>
            <div class="meeting-yt">
              <span class="alert-name">议题</span>
              <input type="text" id="issue2" disabled>
            </div>
            <div class="meeting-cail">
              <span class="alert-name" style="vertical-align:top;">会议材料</span>
              <ul class="upload-mlist2">

              </ul>
            </div>
            <div class="meeting-spr">
              <span class="alert-name">审批人</span>
              <select id="approvalUserId2" disabled>
                <option value="">无</option>
                <c:forEach items="${user}" var="u">
                  <option value="${u.id}">${u.userName}</option>
                </c:forEach>
              </select>
            </div>
          </div>
        </div>
        <!--==========================会务/活动end==========================-->
        <!--==========================教师的模板管理start==========================-->
        <div class="model-tea-manage">
          <a href="javascript:;" class="hyandhd-back">&lt;&nbsp;返回</a>
          <table class="hyandhd-table">
            <thead>
            <tr>
              <th style="width:8%;">序号</th>
              <th style="width:47%;">模板名称</th>
              <th style="width:25%;">模板类型</th>
              <th style="width:20%;">操作</th>
            </tr>
            </thead>
            <tbody id="model-table">

            </tbody>
            <script type="text/template" id="model-table_templ">
              {{ if(it.rows.length>0){ }}
              {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
              {{var obj=it.rows[i];}}
              <tr>
                <td style="background:#ececec;">{{=i+1}}</td>
                <td>{{=obj.modelName}}</td>
                <td>{{?obj.type==1}}会议{{??}}活动{{?}}</td>
                <td>
                  <a href="javascript:;" class="hyandhd-edit edit-model" mid="{{=obj.id}}" mnm="{{=obj.modelName}}">编辑</a>
                  <i>|</i>
                  <a href="javascript:;" class="hyandhd-del del-model" mid="{{=obj.id}}">删除</a>
                </td>
              </tr>
              {{ } }}
              {{ } }}
            </script>
          </table>
        </div>
        <!--==========================教师的模板管理end==========================-->
        <!--==========================审批会务/活动start==========================-->
        <div class="sphw-alert">
          <div class="zb-set-title clearfix">
            <p>审批会务/活动</p>
            <span class="zb-set-close shenhe-close">X</span>
          </div>
          <div class="zb-set-main">
            <ul class="zb-set-list">
              <li><span>申请人:</span><em id="username">李锋锐</em></li>
              <li><span>名称:</span><em id="name">暑假会议</em></li>
              <li><span>类型:</span><em id="meettype">会务</em></li>
              <li><span>起止时间:</span><em id="time">2016-6-30&nbsp;15:00-17:00</em></li>
              <li><span>会议事由:</span><em id="meetcause">关于暑期校内安全管理</em></li>
            </ul>
            <%--<span class="look-more">查看更多</span>--%>
            <div class="alert-step">
              <p class="alert-legend">审批流程</p>
              <div class="alert-legend-con">

              </div>
            </div>
            <input hidden="hidden" id="meetId">
            <div class="alert-agree">
              <span>审批</span>
              <label><input type="radio" name="agree" value="0" checked>&nbsp;同意</label>
              <label><input type="radio" name="agree" value="1">&nbsp;驳回</label>
            </div>
            <div class="alert-nextsp">
              <span>下一级审批人</span>
              <select id="appUser">
                <option value="">无</option>
                <c:forEach items="${user}" var="u">
                  <option value="${u.id}">${u.userName}</option>
                </c:forEach>
              </select>
            </div>
            <div class="alert-beizhu">
              <span>备注</span>
              <input type="text" id="remark">
            </div>
            <div class="alert-btn">
              <button class="alert-btn-submit submit-shenhe">提交</button>
              <button class="alert-btn-qx qx-shenhe">取消</button>
            </div>
          </div>
        </div>
        <!--==========================审批会务/活动end==========================-->

      </div>
    </div>
    <!--/.tab-col右侧-->

  </div>
  <!--/.col-right-->

</div>
<!--/#content-->
</div>
</div>
<div class="zhiban-meng"></div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!--=================添加参会人员start===================-->
<div class="meeting-CH">
    <div class="meeting-CH-top">
        <em>添加老师</em><i>X</i>
    </div>
    <div class="meeting-CH-mil">
        <em>老师名称：</em>
        <select id="userlist">
          <c:forEach items="${user}" var="u">
            <option value="${u.id}">${u.userName}</option>
          </c:forEach>
        </select>
    </div>
    <div class="meeting-CH-bot">
        <span class="meeting-CH-QD add-user">确定</span>
        <span class="meeting-CH-QX">取消</span>
    </div>
</div>
<!--=================添加参会人员end=====================-->
<!-- 发布范围 -->
<div class="Tanchu1" style="z-index: 990000009;">
  <p class="clearfix">发布范围
    <span class="qx1">取消</span>
    <span class="QD1">确定</span>
  </p>

  <div class="xuanren">
    <p class="Hm" id="Hm">
      <em>未选择</em>
    </p>
    <script type="application/template" id="choosedPeopleJs">
      {{~it.data:value:index}}
      <em>@{{=value.name}}</em>
      {{~}}
    </script>
    <span>部门</span>
    <ul class="K1 publishShow">

    </ul>
    <script type="application/template" id="publishJs">
      {{~it.data:value:index}}
      <li class="departmentList" curId="{{=index}}">{{=value.t.value}}</li>
      <ul>
        {{~value.list:v:i}}
        {{?v.choosed==1}}
        <li class="publishList current" userId="{{=v.idStr}}" name="{{=v.value}}">{{=v.value}}
        </li>
        {{??v.choosed==0}}
        <li class="publishList" userId="{{=v.idStr}}" name="{{=v.value}}">{{=v.value}}</li>
        {{?}}
        {{~}}
      </ul>
      {{~}}
    </script>
  </div>
</div>
<!--=================添加模板start===================-->
<div class="meeting-MB">
    <div class="meeting-CH-top">
        <em>添加模板</em><i class="close-model">X</i>
    </div>
    <div class="meeting-CH-mil">
        <em>模板名称：</em>
      <input id="modelId" hidden="hidden">
        <input id="modelname">
    </div>
    <div class="meeting-CH-bot">
        <span class="meeting-CH-QD sure-model">确定</span>
        <span class="meeting-CH-QX">取消</span>
    </div>
</div>
<!--=================添加模板end=====================-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('meet');
</script>
</body>
</html>
