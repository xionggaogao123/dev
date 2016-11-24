<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2016/9/18
  Time: 10:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <title>教师评价-老师</title>
  <!-- css files -->
  <!-- Normalize default styles -->
  <meta name="renderer" content="webkit">
  <link rel="stylesheet" type="text/css" href="/static_new/css/teacherevaluation/evaluation.css">
  <link rel="stylesheet" type="text/css" href="/static_new/css/reset.css">
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
    <div class="tab-col">

      <div class="tab-head clearfix">
        <ul>
          <li id="GRCS" class="cur"><a href="javascript:;" >个人陈述</a><em></em></li>
          <li id="SZZL"><a href="javascript:;" >实证资料</a><em></em></li>
        </ul>
      </div>

      <div class="tab-main">
        <!--==============================个人陈述start==================================-->
        <div class="per-say" id="tab-GRCS">
          <span class="eva-title">个人陈述</span>
          <textarea class="eva-txt" id="statement"></textarea>
          <div class="eva-btn clearfix">
            <button class="save-btn" id="save">保存</button>
            <button class="push-btn" id="saveAndPush">保存并推送</button>
          </div>
        </div>
        <!--==============================个人陈述end==================================-->
        <!--==============================我的实证资料start==================================-->
        <div class="self-inf" id="tab-SZZL">
          <span class="eva-title">我的实证资料</span>
          <textarea class="eva-txt" style="height:490px;min-height:490px;" id="evidence" readonly></textarea>
        </div>
        <!--==============================我的实证资料end==================================-->
      </div>
    </div>
    <!--/.tab-col右侧-->

  </div>
  <!--/.col-right-->

</div>
<!--/#content-->
<div class="push-success">
  <div class="alert-title clearfix">
    <p>消息</p>
    <span class="alert-close">x</span>
  </div>
  <div class="alert-main">
    <div class="img-wrap"></div>
    <span class="push-inf">已成功推送到本次考核</span>
    <div class="alert-btn">
      <button class="btn-back">返回</button>
    </div>
  </div>
</div>
<div class="push-select">
  <div class="alert-title clearfix">
    <p>推送</p>
    <span class="alert-close">x</span>
  </div>
  <div class="alert-main">
    <dl class="push-target" id="items">
      <dt>选择要推送的考核:</dt>
      <%--<dd>--%>
        <%--<label><input type="radio" name="push-name">党员考核</label>--%>
      <%--</dd>--%>
      <%--<dd>--%>
        <%--<label><input type="radio" name="push-name">优秀老师考核</label>--%>
      <%--</dd>--%>
      <%--<dd>--%>
        <%--<label><input type="radio" name="push-name">教职工先进考核</label>--%>
      <%--</dd>--%>
      <%--<dd>--%>
        <%--<label><input type="radio" name="push-name">年度考核</label>--%>
      <%--</dd>--%>
    </dl>
    <script id="itemsTmpl" type="text/template">
      <dt>选择要推送的考核:</dt>
      {{ for(var i in it) { }}
      <dd>
        <label><input type="radio" name="push-name" value="{{=it[i].id}}">{{=it[i].name}}</label>
      </dd>
      {{ } }}
    </script>
    <div class="alert-btn">
      <button class="btn-sure" id="push">确定</button>
      <button class="btn-qx">取消</button>
    </div>
  </div>
</div>
<div class="push-fail">
  <div class="alert-title clearfix">
    <p>提示</p>
    <span class="alert-close">x</span>
  </div>
  <div class="alert-main">
    <div class="img-wrap-f"></div>
    <span class="push-inf">当前没有进行中的考核可供推送</span>
    <div class="alert-btn">
      <button class="btn-know" style="margin:0px;">我知道了</button>
    </div>
  </div>
</div>
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
  seajs.use('/static_new/js/modules/teacherevaluation/0.1.0/teacher');
</script>

</body>
</html>
