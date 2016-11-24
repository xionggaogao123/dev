<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2016/8/1
  Time: 9:19
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

      <div class="tab-head clearfix">
        <ul>
          <li id="JSPJ" class="cur"><a href="javascript:;" >教师评价</a><em></em></li>
        </ul>
      </div>
      <div class="tab-main">
        <!--=================================教师评价首页start==================================-->
        <div class="evaluate-index">
          <div class="evaluate-title clearfix">
            <select class="eva-year" id="year">
              <option value="2016-2017">2016-2017学年</option>
              <option value="2015-2016">2015-2016学年</option>
            </select>
            <c:if test="${headmaster}">
              <button class="eva-add">添加</button>
            </c:if>
          </div>
          <ul class="eva-list" id="evaluations">
            <%--<li class="clearfix">--%>
              <%--<span class="eva-name">评比名称评比名称评比名称评比名称</span>--%>
              <%--<div class="eva-option">--%>
                <%--<c:if test="${headmaster}">--%>
                <%--<span>基础设置</span>--%>
                <%--</c:if>--%>
                <%--<span>打分</span>--%>
                <%--<c:if test="${headmaster}">--%>
                  <%--<span>编辑</span>--%>
                  <%--<span>删除</span>--%>
                <%--</c:if>--%>
              <%--</div>--%>
            <%--</li>--%>
          </ul>
          <script id="evaluationsTmpl" type="text/template">
            {{~ it:value:index }}
            <li class="clearfix" evid="{{=value.id}}">
              <span class="eva-name">{{=value.name}}</span>
              <div class="eva-option">
                <c:if test="${headmaster}">
                  <span id="config">基础设置</span>
                  {{? value.state == 1}}
                  <span id="sign" {{? value.timeState != -1}}class="grey"{{?}}>取消报名</span>
                  {{?? value.state == -1}}
                  <span id="sign" {{? value.timeState != -1}}class="grey"{{?}}>报名</span>
                  {{?}}
                  {{? value.timeState == 1}}
                  <span id="item">查看成绩</span>
                  {{??}}
                  <span id="item">打分</span>
                  {{?}}
                  <span id="edit">编辑</span>
                  <span id="delete">删除</span>
                </c:if>
                <c:if test="${!headmaster}">
                  {{? value.state == 1}}
                  <span id="sign">取消报名</span>
                  {{?? value.state == -1}}
                  <span id="sign">报名</span>
                  {{?}}
                  <span id="item">打分</span>
                </c:if>
              </div>
            </li>
            {{~ }}
          </script>
        </div>
        <!--=================================教师评价首页end==================================-->
      </div>
      <!--=================================教师评价添加弹窗start==================================-->
      <div class="eva-add-alert">
        <div class="alert-title clearfix">
          <p>教师评价设置</p>
          <span class="alert-close">X</span>
        </div>
        <div class="alert-main">
          <span class="eva-mc">评比名称</span>
          <input type="text" class="eva-con" id="evaluation-name">
        </div>
        <div class="alert-btn">
          <button class="alert-btn-sure" id="save">确定</button>
          <button class="alert-btn-qx">取消</button>
        </div>
      </div>
      <!--=================================教师评价添加弹窗end==================================-->
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
  seajs.use('/static_new/js/modules/teacherevaluation/0.1.0/list');
</script>

</body>
</html>
