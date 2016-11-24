<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    pageContext.setAttribute("basePath", basePath);
%>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="#"/>
    <title>复兰科技</title>
    <meta name="description" content="">
    <meta name="author" content=""/>
    <meta name="copyright" content=""/>
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="<%=basePath%>static_new/css/reset.css" rel="stylesheet"/>
    <!-- Custom styles -->
    <link href="<%=basePath%>static_new/css/gongziview.css?v=2015071201"/>
    <!-- Custom styles -->
    <script type="text/javascript"
            src="<%=basePath%>static_new/js/modules/core/0.1.0/jquery.min.js?v=2015041602"></script>
</head>
<body>
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">


    <!--.col-left-->
    <%@ include file="../common_new/col-left.jsp" %>
    <!--/.col-left-->

    <!--.col-right-->
    <div class="col-right">

        <!--.banner-info-->
        <%--<img src="http://placehold.it/770x100" class="banner-info"/>--%>
        <!--/.banner-info-->

        <!--.tab-col-new-->
        <div class="tab-col-new">
            <div class="tab-head-new clearfix">
                <ul>
                    <li class="cur">
                        <a>工资查询</a>
                    </li>
                </ul>
            </div>
            <div class="tab-main-new clearfix">
                <div class="gongzi-table">
                    <h3>&nbsp;</h3>
                    <div class="gongzi-form clearfix">
                        <select id="gognziview-beginyear">
                            <c:forEach var="year" items="${yearList}">
                                <option value="${year}">${year}</option>
                            </c:forEach>
                        </select>
                        年
                        <select id="gognziview-beginmonth">
                            <option value="1">01</option>
                            <option value="2">02</option>
                            <option value="3">03</option>
                            <option value="4">04</option>
                            <option value="5">05</option>
                            <option value="6">06</option>
                            <option value="7">07</option>
                            <option value="8">08</option>
                            <option value="9">09</option>
                            <option value="10">10</option>
                            <option value="11">11</option>
                            <option value="12">12</option>
                        </select>
                        月
                        <b>——</b>
                        <select id="gognziview-endyear">
                            <c:forEach var="year" items="${yearList}">
                                <option value="${year}">${year}</option>
                            </c:forEach>
                        </select>
                        年
                        <select id="gognziview-endmonth"> 
                            <option value="1">01</option>
                            <option value="2">02</option>
                            <option value="3">03</option>
                            <option value="4">04</option>
                            <option value="5">05</option>
                            <option value="6">06</option>
                            <option value="7">07</option>
                            <option value="8">08</option>
                            <option value="9">09</option>
                            <option value="10">10</option>
                            <option value="11">11</option>
                            <option value="12">12</option>
                        </select>
                        月
                    </div>
                </div>
                <table class="gray-table" width="500">
                    <thead>
                    <th width="50">#</th>
                    <th>发放名称</th>
                    <th width="100">发放日期</th>
                    <th width="100">金额</th>
                    <th width="50">明细</th>
                    </thead>
                    <tbody id="gongziview-datalist">
                    </tbody>
                </table>

            </div>
        </div>
        <!--/.tab-col-new-->
    </div>
    <!--/.col-right-->
</div>
<div class="pop-wrap" id="viewSalaryDetail">
    <div class="pop-title" id="viewSalaryDetailTitle">2015年03月份第1次工资单</div>
    <div class="pop-content">
        <table class="gray-table" width="100%">
            <thead>
            <th width="50">#</th>
            <th>发放名称</th>
            <th width="80">金额</th>
            </thead>
            <tbody id="view-salary-detail">
            </tbody>
        </table>
    </div>
    <div class="pop-btn"><span class="active">确定</span><span>取消</span></div>
</div>

<div class="bg-dialog"></div>
<!--/#content-->
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<script type="text/template" id="itemListTemp">
    {{~it:value:index}}
    <tr>
        <td>{{=index+1}}</td>
        <td>{{=value.itemName}}</td>
        <td>{{=value.mStr}}</td>
    </tr>
    {{~}}
</script>
<script type="text/template" id="yearListTemp">
    {{~it:value:index}}
    <option value="{{=value}}">{{=value}}</option>
    {{~}}
</script>
<script type="text/template" id="salaryItemListTemp">
    {{~it:value:index}}
    <tr id="{{=value.id}}">
        <td>{{=index+1}}</td>
        <td>{{=value.timesName}}</td>
        <td>{{=value.salaryDate}}</td>
        <td>{{=value.asStr}}</td>
        <td><a href="javascript:void(0)" class="viewSalary">查看</a></td>
    </tr>
    {{~}}
</script>
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="<%=basePath%>static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('viewSalary', function (viewSalary) {
        $("#gognziview-beginyear").val(${currYear});
        $("#gognziview-beginmonth").val(${currMonth});
        $("#gognziview-endyear").val(${currYear});
        $("#gognziview-endmonth").val(${currMonth});
        viewSalary.init();
    });
</script>
</body>
</html>