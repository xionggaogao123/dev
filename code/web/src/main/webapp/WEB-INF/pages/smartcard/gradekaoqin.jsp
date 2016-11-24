<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技-年级考勤统计</title>
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
    <link rel="stylesheet" type="text/css" href="/static_new/css/smartcard/stukaoqin.css">
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
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

    <!--.col-right-->
    <div class="col-right">
        <div class="stukq-con">
            <div class="clearfix stukq-nav">
                <ul class="clearfix stukq-ul">
                    <li><a href="/smartCard/myKaoQin.do">我的考勤</a><em></em></li>
                    <c:if test="${roles:isTeacher(sessionValue.userRole)}">
                        <li><a href="/smartCard/stuKaoQin.do">学生考勤统计</a><em></em></li>
                    </c:if>
                    <c:if test="${roles:isHeadmaster(sessionValue.userRole)}">
                        <li class="stukq-active"><a href="javascript:;">年级考勤统计</a><em></em></li>
                        <li><a href="/smartCard/teaKaoQin.do">老师考勤统计</a><em></em></li>
                    </c:if>

                </ul>
            </div>
            <div class="stukq-mian">
                <div class="headkq-main-nav clearfix">
                    <ul>
                        <li id="xq"><a href="javascript:;">本学期</a></li>
                        <li id="by"><a href="javascript:;">本月</a></li>
                        <li id="bz" class="stukq-main-active"><a href="javascript:;">本周</a></li>
                    </ul>
                </div>

                <div class="headkq-title">
                    <span>正常考勤率:</span><em id="normal"></em>
                    <span>迟到率:</span><em id="late"></em>
                    <span>早退率:</span><em id="punctual"></em>
                    <span>旷课率:</span><em id="kuangke"></em>
                </div>
                <table class="stukq-table">
                    <tr>
                        <th>年级</th>
                        <th>正常出勤率</th>
                        <th>迟到率</th>
                        <th>早退率</th>
                        <th>旷课率</th>
                    </tr>
                    <tbody class="stukq-data">

                    </tbody>
                </table>
                <script type="text/template" id="j-tmpl">
                    {{ if(it.list.length>0){ }}
                    {{ for (var i = 0, l = it.list.length; i < l; i++) { }}
                    {{var obj=it.list[i];}}
                    <tr class="user-tr" style="cursor: pointer;" id="{{=obj.gradeId}}" name="{{=obj.gradeName}}">
                        <td style="background:#ececec;color:#3999d4;">
                            {{=obj.gradeName}}
                        </td>
                        <td>
                            {{=obj.normalReta}}
                        </td>
                        <td>
                            {{=obj.lateReta}}
                        </td>
                        <td>
                            {{=obj.punctualReta}}
                        </td>
                        <td>
                            {{=obj.kuangkeReta}}
                        </td>
                    </tr>
                    {{}}}
                    {{}}}
                </script>
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
    seajs.use('gradekaoqin',function (gradekaoqin) {
        gradekaoqin.init();
    });
</script>

</body>
</html>