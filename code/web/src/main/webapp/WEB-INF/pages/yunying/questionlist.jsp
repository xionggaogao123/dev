<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2015/11/23
  Time: 13:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html>
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>复兰科技-问卷调查</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <!-- jquery artZoom4Liaoba styles -->
  <!-- Custom styles -->
  <link href="/static_new/css/questionnair.css" rel="stylesheet" />
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
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
  <!--广告-->
  <c:choose>
    <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
      <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
    </c:when>
    <c:otherwise>
      <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
    </c:otherwise>
  </c:choose>

  <div class="col-right">
    <div class="quest-top">
      <h1 id="title" userId="${userId}">问卷调查</h1>
      <%--<c:if test="${role == 1}">--%>
                <%--<span style="margin-left: 60px;">--%>
                    <%--<input type="radio" name="questall-raido" checked value="0">浏览全部--%>
                  <%--<select style="width: 180px;" id="schoolid">--%>
                    <%--<option value="">全校</option>--%>
                    <%--<c:forEach items="${schools}" var="schoolinfo">--%>
                      <%--<option value="${schoolinfo.schoolId}">${schoolinfo.schoolName}</option>--%>
                    <%--</c:forEach>--%>
                  <%--</select>--%>
                    <%--<input type="radio" name="questall-raido" value="1">我发布的--%>
                <%--</span>--%>
        <a href="/questionnaire#/new2" style="margin-right: 330px;">新建问卷</a>
        <%--<a href="/questionnaire#/new" target="_blank">新建问卷</a>--%>
      <%--</c:if>--%>
    </div>
    <ul class="quest-ul" id="list">
      <%--<li >--%>
        <%--<span>问卷名称</span>--%>
        <%--<span>参与状态</span>--%>
        <%--<span>发布人</span>--%>
        <%--<span>截止日期</span>--%>
        <%--<span>操作</span>--%>
      <%--</li>--%>
      <%--<li>--%>
      <%--<span>嘿嘿哈哈哈哈哈哈哈哈哈</span>--%>
      <%--<span></span>--%>
      <%--<span>heih</span>--%>
      <%--<span>2015/12/30</span>--%>
      <%--<span><a href="writequestionnair.html" target="_blank"><i>填写问卷</i></a></span>--%>
      <%--</li>--%>
      <%--<li>--%>
      <%--<span>嘿嘿哈哈哈哈哈哈哈哈哈</span>--%>
      <%--<span></span>--%>
      <%--<span>heih</span>--%>
      <%--<span>2015/12/30</span>--%>
      <%--<span>已填写</span>--%>
      <%--</li>--%>
      <%--<li>--%>
      <%--<span>嘿嘿哈哈哈哈哈哈哈哈哈</span>--%>
      <%--<span></span>--%>
      <%--<span>heih</span>--%>
      <%--<span>2015/12/30</span>--%>
      <%--<span>已结束</span>--%>
      <%--</li>--%>
      <%--<li>--%>
      <%--<span>嘿嘿哈哈哈哈哈哈哈哈哈</span>--%>
      <%--<span></span>--%>
      <%--<span>heih</span>--%>
      <%--<span>2015/12/30</span>--%>
      <%--<span><a href="cktjquestionnair.html" target="_blank"><em>查看统计</em></a><em>删除</em></span>--%>
      <%--</li>--%>

    </ul>
    <div class="new-page-links"></div>
  </div>
  <!--/.col-right-->

</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp"%>
<!--#foot-->


<script id="list_tmpl" type="text/template">
  <li class="quest-li1">
    <span class="span1">问卷名称</span>
    <span class="span2">参与状态</span>
    <span class="span3">发布人</span>
    <span class="span4">截止日期</span>
    <span class="span5">操作</span>
  </li>
  {{for (var i in it){ }}
  <li>
    <span class="span1" title="{{=it[i].name}}">{{=it[i].name}}</span>
    {{? it[i].state == 1}}
    <span class="span2">已发布</span>
    {{?? it[i].state == 2}}
    <span class="span2">已结束</span>
    {{?? it[i].state == 3}}
    <span class="span2">已填写</span>
    {{?? it[i].state == 4}}
    <span class="span2">未填写</span>
    {{?}}
    <%--<span></span>--%>
    <span class="span3">{{=it[i].publishName}}</span>
    <span class="span4">{{=it[i].stringEndDate}}</span>
    {{? it[i].state == 1}}
    <span class="span5"><a href="/questionnaire#/static/{{=it[i].id}}" target="_blank"><em>查看统计</em></a><em id="{{=it[i].id}}" class="remove">删除</em></span>
    {{?? it[i].state == 2}}
    <span class="span5"><em qid="{{=it[i].id}}" class="review">查看</em></span>
    {{?? it[i].state == 3}}
    <span class="span5"><em qid="{{=it[i].id}}" class="review">查看</em></span>
    {{?? it[i].state == 4}}
    <span class="span5"><a href="/questionnaire#/answer/{{=it[i].id}}"><em>填写问卷</em></a></span>
    {{?}}

  </li>
  {{} }}
</script>


<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('/static_new/js/modules/yunying/0.1.0/questionlist');
</script>
</body>
</html>
