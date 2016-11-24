<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<html>
<head>
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn"/>
    <title>公文流转</title>
    <meta name="description" content="">
    <meta name="author" content=""/>
    <meta name="copyright" content=""/>
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static/css/homepage.css?v=1" rel="stylesheet"/>
    <link href="/static_new/css/reset.css?v=1" rel="stylesheet"/>
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="/static_new/css/docflow/gongwen.css?v=2015041602" rel="stylesheet"/>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>
<body docid="${docId}">
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
    <!--广告-->
    <div class="col-right">
        <!--.tab-col右侧-->
        <div class="tab-col">
            <div class="tab-head clearfix">
                <ul id="unReadCountShow">

                </ul>
                <script type="application/template" id="unReadCountJs">
                    <c:choose>
                        <c:when test="${type==0}">
                            <li class="cur">
                                <a href="/docflow/documentList.do?type=0&version=51">公文</a>
                                {{?it.unread>0}}
                                    <span>
                                        {{=it.unread}}
                                    </span>
                                {{?}}
                            </li>
                            <li>
                                <a href="/docflow/documentList.do?type=1&version=51">审阅</a>
                                {{?it.uncheck>0}}
                                <span>{{=it.uncheck}}</span>
                                {{?}}
                            </li>
                            <li>
                                <a href="/docflow/documentList.do?type=2&version=51">我的公文</a>
                                {{?it.promote>0}}
                                <span class="Lg">{{=it.promote}}</span>
                                {{?}}
                            </li>
                        </c:when>
                        <c:when test="${type==1}">
                            <li>
                                <a href="/docflow/documentList.do?type=0&version=51">公文</a>
                                {{?it.unread>0}}
                                    <span>
                                        {{=it.unread}}
                                    </span>
                                {{?}}
                            </li>
                            <li class="cur">
                                <a href="/docflow/documentList.do?type=1&version=51">审阅</a>
                                {{?it.uncheck>0}}
                                <span>{{=it.uncheck}}</span>
                                {{?}}
                            </li>
                            <li>
                                <a href="/docflow/documentList.do?type=2&version=51">我的公文</a>
                                {{?it.promote>0}}
                                <span class="Lg">{{=it.promote}}</span>
                                {{?}}
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li>
                                <a href="/docflow/documentList.do?type=0&version=51">公文</a>
                                {{?it.unread>0}}
                                    <span>
                                        {{=it.unread}}
                                    </span>
                                {{?}}
                            </li>
                            <li>
                                <a href="/docflow/documentList.do?type=1&version=51">审阅</a>
                                {{?it.uncheck>0}}
                                <span>{{=it.uncheck}}</span>
                                {{?}}
                            </li>
                            <li class="cur">
                                <a href="/docflow/documentList.do?type=2&version=51">我的公文</a>
                                {{?it.promote>0}}
                                <span class="Lg">{{=it.promote}}</span>
                                {{?}}
                            </li>
                        </c:otherwise>
                    </c:choose>
                </script>
            </div>
            <div class="tab_main">
                <script type="application/template" id="docDetail">
                    <a href="/docflow/documentList.do?type=${type}&version=51" class="Bk"> < 返回</a>
                    <c:choose>
                        <c:when test="${type==2}">
                            <p class="Bt" style="margin-top: 20px;">
                                <em>当前审阅人： ${person}</em>
                                <em>当前审阅部门： ${department}</em>
                            </p>
                        </c:when>
                    </c:choose>

                    <h3 class="title">{{=it.title}}</h3>
                    <p class="Bt">
                        <em>部门： {{=it.departmentName}}</em>
                        <em>发布日期： {{=it.time}}</em>
                    </p>
                    <c:choose>
                        <c:when test="${type==2||type==1}">
                            <p class="Bt">
                                <em>发布范围：
                                    {{~it.publishNames:value:index}}
                                    {{=value}},
                                    {{~}}
                                    本校校长以及管理员
                                </em>
                            </p>
                        </c:when>
                    </c:choose>

                    <!-- 正文 -->
                    <ul class="Zw">
                        <li>{{=it.content}}</li>
                    </ul>
                    <!-- 正文 -->
                    <div class="Fj">
                        {{?it.docList.length>0}}
                        <span>附件：</span>
                        {{?}}
                        {{~it.docList:value:index}}
                        <a href="{{=value.value}}" class="download" fn="{{=value.name}}" target="_blank" style="margin-right: 20px;">{{=value.name}}
                            <c:choose>
                                <c:when test="${type==1||type==2}">
                                    ({{=value.userName}})
                                </c:when>
                            </c:choose>
                        </a>
                        {{~}}
                    </div>
                    <c:choose>
                        <c:when test="${type==1||type==2}">
                            <div class="tab_LS">
                                <span>审阅历史</span>
                                <table>
                                    <tr>
                                        <th>操作人</th>
                                        <th>操作人部门</th>
                                        <th>审批意见</th>
                                        <th>备注</th>
                                        <th>操作</th>
                                    </tr>
                                    {{~it.checkDTOList:value:index}}
                                    <tr>
                                        <td>{{=value.userName}}</td>
                                        <td>{{=value.departmentName}}</td>
                                        <td>{{=value.opinionDesc}}</td>
                                        <td>{{=value.remark}}</td>
                                        <td>{{=value.time}}</td>
                                    </tr>
                                    {{~}}
                                </table>
                            </div>
                        </c:when>
                    </c:choose>
                </script>
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
<script src="/static_new/js/sea.js?v=1"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('documentDetail');
</script>
</body>
</html>