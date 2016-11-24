<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
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
    $(function () {
      $(".path-start").click(function(){
        window.location.href = "/meeting/meeting.do";
      });
      $("#back").click(function(){
        window.location.href = "/meeting/meeting.do";
      });

    });
  </script>
</head>
<body>
<!--#head-->
<%@ include file="../common_new/head.jsp"%>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">
  <div class="meetingjl-con">
    <h3 class="hyandhd-h3">${name}</h3>
    <div class="hyandhd-path">
      <span class="path-start">会务活动列表</span>
      <em>&gt;</em>
      <span>会议记录</span>
      <span style="float: right;border: 1px;background: #5db85b;cursor: pointer;" id="back">返回</span>
    </div>
    <div class="hyandhd-mian clearfix">
      <%--<div class="hyandhd-wrap clearfix">--%>
        <%--&lt;%&ndash;<span class="hyandhd-excle">存档并生成报表</span>&ndash;%&gt;--%>
      <%--</div>--%>
      <dl class="hyandhd-dl">
        <dt class="dl-title">会议议程</dt>
        <dd>${process}</dd>
      </dl>
      <dl class="hyandhd-dl">
        <dt class="dl-title">发言人顺序</dt>
        <dd>${order}</dd>

      </dl>
      <dl class="hyandhd-dl">
        <dt class="dl-title">参会人员</dt>
        <dd>${users}</dd>
      </dl>
      <dl class="hyandhd-dl">
        <dt class="dl-title">投票</dt>
        <c:forEach items="${voteDTOs}" var="vote">
          <dd>${vote.name}</dd>
          <c:forEach items="${vote.chooseDTOList}" var="choose">
            <dd>${choose.name}   数量：${choose.count}</dd>
          </c:forEach>
        </c:forEach>
      </dl>
      <dl class="hyandhd-dl">
        <dt class="dl-title">讨论内容</dt>
        <c:forEach items="${message}" var="mesg">
          <dd>${mesg.userName} ${mesg.time}</dd>
          <dd>${mesg.content}</dd>
        </c:forEach>
      </dl>
    </div>
  </div>


</div>
<!--/#content-->
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!--=================添加模板end=====================-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
//  seajs.use('meet');
</script>
</body>
</html>
