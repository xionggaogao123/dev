<%--
  Created by IntelliJ IDEA.
  User: guojing
  Date: 2015/7/9
  Time: 17:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>德育-成绩录入</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="/static_new/css/moralculture.css?v=2015041602" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <link href="/static/css/homepage.css" type="text/css" rel="stylesheet">
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



        <!--.tab-col右侧-->
        <div class="tab-col">

            <div class="tab-head clearfix">
                <ul>
                    <li class="cur"><a href="#">管理首页</a></li>
                    <li id="moralCultureScore"><a href="#">个人成绩</a></li>
                </ul>
            </div>

            <div class="tab-main">
                <!--.tiaojian-col-->
                <div class="tiaojian-col clearfix">
                    <div class="select-style">
                        <select id="semesterId" name="semesterId" style="width:216px;">
                            <c:forEach items="${semesters}" var="item">
                                <option value="${item.key}">${item.value}</option>
                            </c:forEach>
                        </select>
                        <input id="parentRole" name="parentRole" type="hidden" value="${roles:isParent(sessionValue.userRole)}">
                    </div>
                </div>
                <!--/.tiaojian-col-->
                <!-- 个人成绩录入 -->
                <dl class="gvsb">
                    <dt><em>1</em>成绩录入</dt>
                    <c:forEach items="${list}" var="item">
                        <dd>
                            <p>${item.moralCultureName}：</p>
                            <input name="id" type="hidden" value="${item.id}">
                            <c:if test="${roles:isStudent(sessionValue.userRole)}">
                                <input name="score" class="score" style="width: 90px;text-align: center" type="text" value="">
                            </c:if>
                            <c:if test="${roles:isParent(sessionValue.userRole)}">
                                &nbsp;&nbsp;&nbsp;&nbsp;<span name="score"></span>
                            </c:if>
                        </dd>
                    </c:forEach>
                    <c:if test="${roles:isStudent(sessionValue.userRole)}">
                        <button class="que">确认</button>
                    </c:if>
                </dl>
                <!-- 个人成绩录入 -->
            </div>

        </div>
        <!--/.tab-col右侧-->

    </div>
    <!--/.col-right-->

</div>
<!--/#content-->
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('studentmoralculture');//../js/deyu2.js
</script>
</body>
</html>
