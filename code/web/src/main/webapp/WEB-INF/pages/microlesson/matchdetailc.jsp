<%--
  Created by IntelliJ IDEA.
  User: wang_xinxin
  Date: 2015/8/19
  Time: 19:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>复兰科技</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link href="/static_new/css/microlesson/microlesson.css?v=2015041602" rel="stylesheet" />
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
  <script type="text/javascript">
    function playview(id) {
      if (GetQueryString("a")!="10000") {
        location.href='/microlesson/view.do?lessonId=' + id;
        //window.open('/microlesson/view.do?lessonId=' + id);
      } else {
        location.href='/microlesson/view.do?a=10000&lessonId=' + id;
        //window.open('/microlesson/view.do?a=10000&lessonId=' + id);
      }

    }
    function GetQueryString(name)
    {
      var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
      var r = window.location.search.substr(1).match(reg);
      if(r!=null)return  unescape(r[2]); return null;
    }
    </script>
</head>

<body>
<!--#head-->
<%@ include file="../common_new/head-cloud.jsp"%>
<!--/#head-->
<!--#content-->
<div id="content" class="clearfix">
  <!--.col-left-->
  <%--<%@ include file="../common_new/col-left.jsp" %>--%>
  <!--/.col-left-->
  <%--<c:choose>--%>
    <%--<c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">--%>
      <%--<jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>--%>
    <%--</c:when>--%>
    <%--<c:otherwise>--%>
      <%--<jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>--%>
    <%--</c:otherwise>--%>
  <%--</c:choose>--%>
  <!--.col-right-->
  <div class="col-right" style="width: 1000px;">

    <!--.tab-col右侧-->
    <div class="tab-col bsxq">
      <h3 class="zuoB">${rows.matchname}</h3><!-- 教育局没有 -->
      <div class="neirong">
        <dl class="clearfix">
          <dd class="Time">
            <p>参赛时间：${rows.begintime} – ${rows.endtime}</p>
            <p>打分时间：${rows.scorebegintime} – ${rows.scoreendtime}</p>
          </dd>
          <dd class="Look">
            <c:choose>
              <c:when test="${roles:isEducation(sessionValue.userRole)}">
                <a href="javascript:;" class="kaishi" id="result">查看结果</a><!-- 教育局特有 -->
                <a href="javascript:;" class="downall" id="downall">下载全部</a>
                <a href="javascript:;" class="edit" id="edit">编辑</a>
                <a href="javascript:;" class="delmatch" id="delmatch">删除该比赛</a>
              </c:when>
              <c:otherwise>
                <c:if test="${rows.showup==2}">
                <a href="javascript:;" class="kaishi" id="uplesson">上传课程</a><!-- 教育局没有 -->
                </c:if>
              </c:otherwise>
            </c:choose>


          </dd>
        </dl>
        <ul class="title">
          ${rows.content}
        </ul>
      </div>
      <h4>
        <select name="seachtype" id="seachtype">
          <c:forEach items="${rows.matchtypes}" var="p">
            <option value="${p.id}">${p.value}</option>
          </c:forEach>
        </select>
        <span>参赛课程</span>
      </h4>
      <input type="hidden" id="log" value="${rows.id}">
      <ul class="tupian clearfix">

      </ul>
      <script id="listbiao" type="text/template">
        {{ if(it.rows.length>0){ }}
        {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
        {{var obj=it.rows[i];}}
        <li cid="{{=obj.id}}" style="width: 221px;">
          <img src="{{=obj.imgUrl}}" alt="" onclick="playview('{{=obj.id}}')" style="width: 210px;height: 155px;">
          <p>{{=obj.name}}</p>
          <p>{{=obj.userName}}</p>
          <p>{{=obj.schoolName}}</p>
          {{ if(obj.isdelete){ }}
          <i class="delete" cid="{{=obj.id}}"></i>
          {{ } }}
        </li>
        {{ } }}
        {{ } }}
      </script>
    </div>

    <!--/.tab-col右侧-->

  </div>
  <!--/.col-right-->

</div>
<!--/#content-->


<!-- 添加评委的弹出层 -->
<div class="gay"></div>
<div class="att" style="display: none;">
  <p>新建课程<span class="gb"></span></p>
  <div class="hylb_main clearfix">
    <div class="hylb_left">
      <div class="b" cid="${rows.id}">
         <em>课程名</em><input id="lessonname"><br>
        <em>课程分类</em>

        <select id="seltype">
          <c:forEach items="${rows.matchtypes}" var="p">
          <option value="${p.id}">${p.value}</option>
          </c:forEach>
        </select>

        <br>
        <button id="typesuer">确认</button>
        <button class="tygry">取消</button>
      </div>
    </div>
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
  seajs.use('matchdetail');
</script>
</body>
</html>