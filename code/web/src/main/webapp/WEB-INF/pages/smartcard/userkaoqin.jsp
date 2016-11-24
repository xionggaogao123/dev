<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技-${userRoleDes}考勤</title>
    <!-- Basic Page Needs-->
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
    <link href="/static_new/css/smartcard/expand.css?v=2015041602" rel="stylesheet" />
    <link rel="stylesheet" type="text/css" href="/static_new/css/smartcard/mykaoqin.css">
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/js/WdatePicker.js"></script>
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

    <!--.col-right-->
    <div class="col-right">
        <div class="right_top">
        <div class="mykq-con">
            <div class="clearfix mykq-nav">
                <ul class="clearfix mykq-ul">
                    <li><a href="/smartCard/myKaoQin.do">我的考勤</a><em></em></li>
                    <c:if test="${roles:isTeacher(sessionValue.userRole)}">
                        <c:if test="${userRole==2}">
                            <li><a href="/smartCard/stuKaoQin.do">学生考勤统计</a><em></em></li>
                        </c:if>
                        <c:if test="${userRole==1}">
                            <li class="mykq-active"><a href="javascript:;">${userRoleDes}考勤详情</a><em></em></li>
                        </c:if>
                    </c:if>

                    <c:if test="${roles:isHeadmaster(sessionValue.userRole)}">
                        <li><a href="/smartCard/gradeKaoQin.do">年级考勤统计</a><em></em></li>
                        <c:if test="${userRole==2}">
                            <li class="mykq-active"><a href="javascript:;">${userRoleDes}考勤详情</a><em></em></li>
                        </c:if>
                        <c:if test="${userRole==1}">
                            <li><a href="/smartCard/teaKaoQin.do">老师考勤统计</a><em></em></li>
                        </c:if>
                    </c:if>
                </ul>
            </div>
            <div class="mykq-mian">
                <p class="stukq-back" backUrl="${backUrl}">&lt;返回${userRoleDes}考勤统计列表</p>
                <input type="hidden" id="userId" name="userId" value="${userId}">
                <span class="mykq-name">${userName}</span>
                <div class="mykq-select">
                    <input id="selDate" name="selDate" value="${selDate}" style="width:80px; height:30px; border:1px solid #a9a9a9; outline:none; margin-right:4px;" onClick="WdatePicker({dateFmt:'yyyy-MM',maxDate:'%y-%M'})"/>
                    <button id="searchBtn" name="searchBtn">查询</button>
                </div>
                <table class="mykq-table">

                </table>

                <!--.list-info-->
                <script type="text/template" id="j-tmpl">
                    <tr>
                        <th>日</th>
                        <th>一</th>
                        <th>二</th>
                        <th>三</th>
                        <th>四</th>
                        <th>五</th>
                        <th>六</th>
                    </tr>
                    {{ if(it.list.length>0){ }}
                        {{var index=1;}}
                        {{ for (var i = 0, l = it.list.length; i < l; i++) { }}
                            <tr>
                            {{var obj=it.list[i];}}
                            {{ for (var j = 0, n = obj.length; j < n; j++) { }}
                                {{var obj1=obj[j];}}
                                {{if(obj1.state=='null'){}}
                                    <td></td>
                                {{}else{}}
                                    <td>
                                        {{ if(j==0){ }}
                                            <span class="mykq-weekend">{{=index++}}</span>
                                        {{}else if(j==6){ }}
                                            <span class="mykq-weekend">{{=index++}}</span>
                                        {{}else{}}
                                            <span>{{=index++}}</span>
                                        {{}}}
                                        {{if(obj1.state=='holiday'){}}
                                            <img title="周末" src="/static_new/images/smartcard/kq-relax.png">
                                        {{}else if(obj1.state=='normal'){}}
                                            <img src="/static_new/images/smartcard/kq-right.png">
                                        {{}else{}}
                                            {{var stateDes="",imgurl="kq-warning.png";}}
                                            {{if(obj1.state=='late'){}}
                                            {{stateDes="迟到";imgurl="kq-late.png";}}
                                            {{}else if(obj1.state=='punctual'){}}
                                            {{stateDes="早退";imgurl="kq-punctual.png";}}
                                            {{}else{}}
                                            {{stateDes="旷课";imgurl="kq-warning.png";}}
                                            {{}}}
                                            <img title="{{=stateDes}}" src="/static_new/images/smartcard/{{=imgurl}}">
                                        {{}}}
                                    </td>
                                {{}}}
                            {{}}}
                            </tr>
                        {{}}}
                    {{}}}
                </script>
            </div>
        </div>
        </div>
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
    seajs.use('mykaoqin',function (mykaoqin) {
        mykaoqin.init();
    });
</script>
</body>
</html>