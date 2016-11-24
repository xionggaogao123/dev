<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>复兰科技-互动课堂</title>
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <link href="/static_new/css/lanclass/lanclass.css" rel="stylesheet">
    <meta charset="utf-8">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>

<body>
<!--#head-->
<!--=================================引入头部============================================-->
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

    <div class="col-right" style="">
        <p class="stukq-back">&lt;返回教师上课查询</p>
        <div class="tab-col">
            <div class="tab-top clearfix">
                <input type="hidden" id="isStudentOrParent" value="${roles:isStudentOrParent(sessionValue.userRole)}">
                <ul>
                    <c:choose>
                        <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
                            <li class="curl tab-lan" id="lan" cid="${classinfo.id}"><a href="javascript:;">${classinfo.className}</a></li>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${classinfo}" var="classInfo" varStatus="cls">
                                <c:if test="${cls.index==0}">
                                    <li class="curl tab-lan" id="lan" cid="${classInfo.id}"><a href="javascript:;">${classInfo.className}</a></li>
                                </c:if>
                                <c:if test="${cls.index!=0}">
                                    <li class="tab-lanclass" id="currlanclass" cid="${classInfo.id}"><a href="javascript:;">${classInfo.className}</a></li>
                                </c:if>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>

                    <%--<li class="cur tab-lan" id="lan"><a href="javascript:;">三年级一班（语文）</a></li>--%>
                    <%--<li class="tab-lanclass" id="currlanclass"><a href="javascript:;">三年级二班（数学）</a></li>--%>
                </ul>
            </div>
            <div class="lanclass-main">
                <c:if test="${roles:isStudentOrParent(sessionValue.userRole)}">
                    <div class="lanclass-list">
                        <dl>
                            <dt>
                                <em class='lanclass-kemu'>科目</em>
                                <c:forEach items="${subject}" var="subject">
                                    <span id="${subject.subjectId}" class="">
                                        ${subject.subjectName}
                                    </span>
                                </c:forEach>
                                <%--<span class="lanclass-kemu-hover">
                                    语文
                                </span>--%>
                            </dt>
                        </dl>
                    </div>
                </c:if>

                <ul class="lanlesson">

                </ul>
                <script type="text/template" id="lanlesson_templ">
                    {{~it:value:index}}
                    <li>
                        <div class="cours">
                            <img src="{{=value.imgurl}}" style="width: 82px;height: 56px;cursor: pointer;" class="lesimg" ilid="{{=value.ilid}}">
                            <span>
                                <input class="lanclass-in" id="UserId" value="{{=value.name}}" disabled style="border: 1px solid transparent;background-color: white;">
                                <em>{{=value.date}}</em>
                            </span>
                            {{?${roles:isTeacher(sessionValue.userRole)}}}
                            <span class="laclass-right">
                                <i class="lanclass-edi" ilid="{{=value.ilid}}"></i><!--修改-->
                                {{? value.lock=='0'}}<i class="lanclass-Nkock" ilid="{{=value.ilid}}"></i>{{??}}<i class="lanclass-lock" ilid="{{=value.ilid}}"></i>{{?}}
                                <%--<i class="lanclass-lock" ilid="{{=value.ilid}}"></i><!--锁定-->--%>
                                <i class="lanclass-del" ilid="{{=value.ilid}}"></i><!--删除-->
                            </span>
                            {{?}}
                        </div>
                    </li>
                    {{~}}
                </script>
            </div>

            <div class="page-paginator">
                <span class="first-page">首页</span>
                        <span class="page-index">
                            <span class="active">1</span>
                            <i>···</i>
                        </span>
                <span class="last-page">尾页</span>
            </div>

            <div class="lanclass-hov">
                <img src="/static_new/images/lanclass/lanclass-T.png">
            </div>
        </div>
    </div>
</div>
</dd>
</dl>
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
    seajs.use('lanclass');
</script>

</body>
</html>