<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技-门禁统计</title>
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

    <!--.col-right-->
    <div class="col-right">
        <div class="stukq-con">
            <div class="clearfix stukq-nav">
                <ul class="clearfix stukq-ul">
                    <li class="stukq-active"><a href="javascript:;">门禁统计</a><em></em></li>
                </ul>
            </div>
            <div class="stukq-mian">
                <div class="stukq-select">
                    姓名：<input id="name" name="name" value="" style="width: 80px;">
                    出入：<select id="selState" name="selState" style="width: 80px;">
                    <option value="">全部</option>
                    <option value="出门">出门</option>
                    <option value="进门">进门</option>
                    </select>
                    门号：<select id="doorNum" name="doorNum" style="width: 80px;">
                    <option value="">全部</option>
                    <option value="1号门">1号门</option>
                    <option value="2号门">2号门</option>
                    <option value="3号门">3号门</option>
                    <option value="4号门">4号门</option>
                </select>
                    时间段：<input id="dateStart" name="dateStart" value="" style="width:90px; height:25px; border:1px solid #a9a9a9; outline:none; margin-right:4px;"
                           onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'dateEnd\')}'})"/>
                    <input id="dateEnd" name="dateEnd" value="" style="width:90px; height:25px; border:1px solid #a9a9a9; outline:none; margin-right:4px;"
                           onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'dateStart\')}'})"/>
                    <button id="searchBtn" name="searchBtn">确认</button>
                </div>

                <table class="stukq-table">
                    <tr>
                        <th>姓名</th>
                        <th>出/入门</th>
                        <th>时间</th>
                        <th>门号</th>
                    </tr>
                    <tbody class="stukq-data">

                    </tbody>
                </table>
                <script type="text/template" id="j-tmpl">
                    {{ if(it.rows.length>0){ }}
                    {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                    {{var obj=it.rows[i];}}
                    <tr>
                        <td style="background:#ececec;color:#3999d4;">
                            {{=obj.name}}
                        </td>
                        <td>
                            {{=obj.doorState}}
                        </td>
                        <td>
                            {{=obj.cardDate}}
                        </td>
                        <td>
                            {{=obj.doorNum}}
                        </td>
                    </tr>
                    {{}}}
                    {{}}}
                </script>
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
    seajs.use('userdoor',function (userdoor) {
        userdoor.init();
    });
</script>

</body>
</html>