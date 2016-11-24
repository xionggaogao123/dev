<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
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
<body uid="${uid}" type="${type}">
<!--#head-->
<%@ include file="../common_new/head-cloud.jsp" %>
<!--/#head-->
<!--#content-->
<div id="content" class="clearfix">
    <!--.col-left-->
    <%--<%@ include file="../common_new/col-left.jsp" %>--%>
    <!--/.col-left-->
    <!--.col-right-->
    <!--广告-->
    <%--<c:choose>
        <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
        </c:when>
        <c:otherwise>
            <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
        </c:otherwise>
    </c:choose>--%>
    <!--广告-->
    <div class="col-right" style="width: 1000px;">
        <!--.tab-col右侧-->
        <div class="tab-col">
            <div class="tab-head clearfix">
                <ul id="unReadCountShow">

                </ul>
                <script type="application/template" id="unReadCountJs">
                    <c:choose>
                        <c:when test="${type==0}">
                            <li class="cur">
                                <a href="javascript:;">公文</a>
                                {{?it.unread>0}}
                        <span>
                            {{=it.unread}}
                        </span>
                                {{?}}
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li>
                                <a href="javascript:;">公文</a>
                                {{?it.unread>0}}
                        <span>
                            {{=it.unread}}
                        </span>
                                {{?}}
                            </li>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${type==1}">
                            <li class="cur">
                                <a href="javascript:;">审阅</a>
                                {{?it.uncheck>0}}
                                <span>{{=it.uncheck}}</span>
                                {{?}}
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li>
                                <a href="javascript:;">审阅</a>
                                {{?it.uncheck>0}}
                                <span>{{=it.uncheck}}</span>
                                {{?}}
                            </li>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${type==2}">
                            <li class="cur">
                                <a href="javascript:;">我的公文</a>
                                {{?it.promote>0}}
                                <span class="Lg">{{=it.promote}}</span>
                                {{?}}
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li>
                                <a href="javascript:;">我的公文</a>
                                {{?it.promote>0}}
                                <span class="Lg">{{=it.promote}}</span>
                                {{?}}
                            </li>
                        </c:otherwise>
                    </c:choose>
                </script>
            </div>
            <div class="tab_main">
                <div class="Xueqi clearfix">
                    <label for="">学期</label>
                    <select name="" class="Xueqi-I" id="searchTerm">

                    </select>
                    <script type="application/template" id="termJs">
                        {{~it.data:value:index}}
                        <option value="{{=value}}">{{=value}}</option>
                        {{~}}
                    </script>
                    <c:choose>
                        <c:when test="${type==1}">
                            <select name="" id="" class="Xueqi-II" style="display: block">
                                <option value="0">未处理</option>
                                <option value="1">已处理</option>
                            </select>
                        </c:when>
                        <c:otherwise>
                            <select name="" id="" class="Xueqi-II" style="display: none">
                                <option value="0">未处理</option>
                                <option value="1">已处理</option>
                            </select>
                        </c:otherwise>
                    </c:choose>


                    <input id="searchContent" type="text" placeholder="公文标题、关键词">
                    <button id="searchBtb">搜索</button>
                </div>
                <div class="Lb clearfix">
                </div>
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
                <!--我的公文-->
                <script type="application/template" id="myDocJs">
                    <a href="/docflow/documentCreate.do?version=51&a=10000" class="Zx">撰写</a>
                    <table width="100%">
                        <thead>
                        <tr>
                            <th>公文标题</th>
                            <th>撰写时间</th>
                            <th>当前审阅人</th>
                            <th>审阅部门</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        {{~it.data:value:index}}
                        <tr>
                            <td style="text-align: left">{{=value.title}}</td>
                            <td>{{=value.time}}</td>
                            <td>{{=value.checkPerson}}</td>
                            <td>{{=value.checkDepartment}}</td>
                            <td>{{=value.stateDesc}}</td>
                            {{?value.state==1 && value.userId=="${uid}"&&value.checkPerson=="${sessionValue.userName}"}}
                            <td onclick="location.href='/docflow/myDocumentModify.do?docId={{=value.id}}&version=51&a=10000'"
                                class="CK">编辑
                            </td>
                            {{??}}
                            <td onclick="location.href='/docflow/documentDetail.do?docId={{=value.id}}&p='+
                            encodeURI(encodeURI('{{=value.checkPerson}}'))+'&d='+encodeURI(encodeURI('{{=value.checkDepartment}}'))+'&type=2&version=51&a=10000'"
                                class="CK">查看
                            </td>
                            {{?}}
                        </tr>
                        {{~}}
                        </tbody>
                    </table>
                </script>
                <!--公文-->
                <script type="application/template" id="allDocJs">

                    <table width="100%">
                        <thead>
                        <tr>
                            <th>公文标题</th>
                            <th>发文部门</th>
                            <th>发布日期</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        {{~it.data:value:index}}
                        {{?value.tag==0}}
                        <tr>
                            <td style="text-align: left">{{=value.title}}</td>
                            <td>{{=value.checkDepartment}}</td>
                            <td>{{=value.checkTime}}</td>
                            {{?value.educationPub==0}}
                            <c:choose>
                                <c:when test="${roles:isHeadmaster(sessionValue.userRole)||roles.isManager(sessionValue.userRole)}">
                                    <td class="CK"><em
                                            onclick="location.href='/docflow/documentDetail.do?docId={{=value.id}}&p=&d=&type=0&version=51&a=10000'">查看</em>|
                                        <em onclick="location.href='/docflow/documentModify.do?docId={{=value.id}}&type=0&version=51&a=10000'">编辑</em>|
                                        <em class="revocation" docid="{{=value.id}}" title="{{=value.title}}">撤销</em>|
                                        <em class="deleteDoc" docid="{{=value.id}}" title="{{=value.title}}">删除</em>
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td onclick="location.href='/docflow/documentDetail.do?docId={{=value.id}}&p=&d=&type=0&version=51&a=10000'"
                                        class="CK">查看
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            {{??}}
                            <td onclick="location.href='/docflow/documentDetail.do?docId={{=value.id}}&p=&d=&type=0&version=51'"
                                class="CK">查看
                            </td>
                            {{?}}
                        </tr>
                        {{??}}
                        <tr class="Wd">
                            <td style="text-align: left">{{=value.title}}</td>
                            <td>{{=value.checkDepartment}}</td>
                            <td>{{=value.checkTime}}</td>
                            {{?value.educationPub==0}}
                            <c:choose>
                                <c:when test="${roles:isHeadmaster(sessionValue.userRole)||roles.isManager(sessionValue.userRole)}">
                                    <td class="CK"><em
                                            onclick="location.href='/docflow/documentDetail.do?docId={{=value.id}}&p=&d=&type=0&version=51&a=10000'">查看</em>|
                                        <em onclick="location.href='/docflow/documentModify.do?docId={{=value.id}}&type=0&version=51&a=10000'">编辑</em>|
                                        <em class="revocation" docid="{{=value.id}}" title="{{=value.title}}">撤销</em>|
                                        <em class="deleteDoc" docid="{{=value.id}}" title="{{=value.title}}">删除</em>
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td onclick="location.href='/docflow/documentDetail.do?docId={{=value.id}}&p=&d=&type=0&version=51&a=10000'"
                                        class="CK">查看
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            {{??}}
                            <td onclick="location.href='/docflow/documentDetail.do?docId={{=value.id}}&p=&d=&type=0&version=51'"
                                class="CK">查看
                            </td>
                            {{?}}
                        </tr>
                        {{?}}
                        {{~}}
                    </table>
                </script>
                <!--审阅未处理-->
                <script type="application/template" id="uncheckJs">
                    <table width="100%">
                        <thead>
                        <tr>
                            <th>公文标题</th>
                            <th>撰写时间</th>
                            <th>审阅意见</th>
                            <th>审阅时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        {{~it.data:value:index}}
                        <tr>
                            <td style="text-align: left">{{=value.title}}</td>
                            <td>{{=value.time}}</td>
                            <td>{{=value.stateDesc}}</td>
                            <td>{{=value.checkTime}}</td>
                            <td class="CK"
                                onclick="location.href='/docflow/documentCheck.do?docId={{=value.id}}&version=51&a=10000'">审阅
                            </td>
                        </tr>
                        {{~}}
                        </tbody>
                    </table>
                </script>
                <!--审阅已处理-->
                <script type="application/template" id="checkedDocJs">
                    <table width="100%">
                        <thead>
                        <tr>
                            <th>公文标题</th>
                            <th>撰写时间</th>
                            <th>状态</th>
                            <th>审阅时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        {{~it.data:value:index}}
                        <tr>
                            <td style="text-align: left">{{=value.title}}</td>
                            <td>{{=value.time}}</td>
                            <td>{{=value.stateDesc}}</td>
                            <td>{{=value.checkTime}}</td>
                            <td onclick="location.href='/docflow/documentDetail.do?docId={{=value.id}}&p=&d=&type=1&version=51&a=10000'"
                                class="CK">查看
                            </td>
                        </tr>
                        {{~}}
                        </tbody>
                    </table>
                </script>
            </div>
        </div>
        <!--/.tab-col右侧-->

    </div>
    <!--/.col-right-->

</div>
<!--/#content-->
<div class="popup-head" style="display:none">

</div>
<div class="popup-info" style="display:none">
    <dl>
        <dt>
            <em>提醒</em>
        </dt>
        <dd>
            <em class="popup-bt">公文标题：关于国庆放假通知</em>
        </dd>
        <dd>
            <em class="popup-op">确定要撤销改公文吗？</em>
        </dd>
        <dd>
            <em>
                <button class="he_qd">确定</button>
                <button class="he_qx">取消</button>
            </em>
        </dd>
    </dl>
</div>

<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js?v=1"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('documentList');
</script>
</body>
</html>