<%--
  Created by IntelliJ IDEA.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <title>教学检查</title>
  <!-- css files -->
  <!-- Normalize default styles -->
  <meta name="renderer" content="webkit">
  <link rel="stylesheet" type="text/css" href="/static_new/css/teacherevaluation/teaPlan.css">
  <link rel="stylesheet" type="text/css" href="/static_new/css/reset.css">
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>

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
    <div class="tab-col">
      <div class="plan-title"><em class="path-root">教学检查</em><em style="display:none;" class="next-path">&gt;&nbsp;详情</em></div>
      <div class="check-con">
        <div class="plan-set clearfix">
          <div class="plan-set-l fl">
            <select class="select-year" id="termlist">
              <c:forEach items="${termList}" var="term">
                <option value="${term}">${term}</option>
              </c:forEach>
            </select>
            <input type="text" placeholder="搜索关键字：姓名" id="name"/>
            <button class="plan-search">查询</button>
          </div>
        </div>
        <table class="newTable">
          <thead>
          <tr>
            <th style="width:186px;">编号</th>
            <th style="width:184px;">教师姓名</th>
            <th style="width:186px;">任教学科</th>
            <th style="width:189px;">操作</th>
          </tr>
          </thead>
          <tbody class="checklist">

          </tbody>
          <script type="text/template" id="checklist_templ">
            {{ if(it.rows.length>0){ }}
            {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
            {{var obj=it.rows[i];}}
            <tr>
              <td>{{=i+1}}</td>
              <td>{{=obj.userName}}</td>
              <td>{{=obj.subjectName}}</td>
              <td>
                <a href="javascript:;" class="table-check" nm="{{=obj.userName}}" sbid="{{=obj.subjectId}}" uid="{{=obj.userId}}">检查</a>
              </td>
            </tr>
            {{ } }}
            {{ } }}
          </script>
        </table>
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
      <div class="checkxq-con" style="display:none;">
        <div class="plan-set clearfix">
          <div class="plan-set-l fl">
            <span class="tea-name">教师姓名：<span id="teaname"></span></span>
            <span class="check-time">检查时间：<span id="checktime"></span></span>
          </div>
        </div>
        <table class="newTable">
          <thead>
          <tr>
            <th style="width:186px;">项目</th>
            <th style="width:184px;">完成数量</th>
            <th style="width:186px;">质量状况</th>
            <th style="width:189px;">分值</th>
          </tr>
          </thead>
          <tbody class="teacheck">

          </tbody>
          <input hidden="hidden" id="checkId">
          <script type="text/template" id="teacheck_templ">
            {{ if(it.rows.length>0){ }}
            {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
            {{var obj=it.rows[i];}}
            <tr tcnm="{{=obj.projectName}}">
              {{?obj.type==1}}
              <td class="table-blue"><a href="{{=obj.hrefUrl}}" target="_blank">{{=obj.projectName}}</a></td>
              <td>{{=obj.count}}</td>
              {{?}}
              {{?obj.type==2}}
              <td>{{=obj.projectName}}</td>
              <td><input value="{{=obj.count}}" class="check-count"></td>
              {{?}}
              <td><input value="{{=obj.quality}}" class="check-quality"></td>
              <td><input value="{{=obj.score}}" class="check-score"></td>
            </tr>
            {{ } }}
            {{ } }}
          </script>
        </table>
      </div>
    </div>
    <!--/.tab-col右侧-->

  </div>
  <!--/.col-right-->

</div>
<!--/#content-->
</div>
</div>
<div class="bg"></div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp"%>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('/static_new/js/modules/teacherevaluation/0.1.0/teaCheck');
</script>

</body>
</html>
