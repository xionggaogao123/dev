<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.fulaan.base.controller.BaseController"%>
<%@ page import="com.pojo.user.UserRole"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com"%>
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions"%>


<%@page import="com.pojo.app.SessionValue"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	pageContext.setAttribute("basePath", basePath);
	boolean isAdmin = UserRole.isManager(new BaseController()
			.getSessionValue().getUserRole());
%>
<html>
<head>
<!-- Basic Page Needs-->
<meta charset="utf-8">
<link rel="dns-prefetch" href="#" />
<title>复兰科技</title>
<meta name="description" content="">
<meta name="author" content="" />
<meta name="copyright" content="" />
<meta name="keywords" content="">
<meta name="viewport"
	content="width=device-width,initial-scale=1, maximum-scale=1">
<!-- css files -->
<!-- Normalize default styles -->
<link href="<%=basePath%>static_new/css/reset.css" rel="stylesheet" />
<!-- Custom styles -->
<link href="<%=basePath%>static_new/css/gongziview.css?v=2015071202"
	rel="stylesheet" />
<script type="text/javascript"
	src="<%=basePath%>static_new/js/modules/core/0.1.0/jquery.min.js?v=2015041602"></script>
</head>
<body>
	<%@ include file="../common_new/head.jsp"%>
	<!--/#head-->

	<!--#content-->
	<div id="content" class="clearfix">


		<!--.col-left-->
		<%@ include file="../common_new/col-left.jsp"%>
		<!--/.col-left-->

		<!--.col-right-->
		<div class="col-right">
			<!-- <img src="http://placehold.it/770x100" class="banner-info" />   -->
			<!--.tab-col-new-->
			<div class="txt-info">
				<div class="tab-head-new clearfix">
					<ul>
						<li><a href="itemList.do">工资项目管理</a></li>
						<li class="cur"><a href="showTable.do">薪酬表格</a></li>
					</ul>
				</div>
				<div class="tab-main-new clearfix">
					<form id="genTemplateForm">
						<input name="year" type="hidden" /> <input name="month"
							type="hidden" /> <input name="num" type="hidden" />
					</form>
					<div class="gongzi-table">
						<h3>薪酬表格</h3>

						<div class="gongzi-form clearfix">
							时间 <select class="change" id="salaryYear"></select> 年 <select
								class="change" id="salaryMonth" style="width: 50px;">
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
							</select>月 <span>发放</span> 第 <select class="change" id="salaryNumber"
								style="width: 60px;">
							</select> 次
							<button type="button" class="green-btn" id="generateTable"
								style="width: 50px;">制表</button>
							<button type="button" class="green-btn" id="deleteSalaryTable"
								style="width: 50px;">删除</button>
							工资薪酬类型<select id="itemListBox"></select>
						</div>
						<span class="btn-list"> <a href="javascript:void(0)"
							class="gray-line-btn" id="genSalaryTemplate">导出模版</a> <a
							href="javascript:void(0)" class="gray-line-btn"
							id="importSalaryData">导入数据</a>
						</span>
					</div>
				</div>
				<div class="tab-main-new clearfix">
					<h4 id="salary-list-title"></h4>
					<div id="salaryTableId">
						<table class="gray-table">
							<thead id="salaryHeaderBox">
							</thead>
							<tbody class="salary-body" id="salaryBodyBox">
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<!--/.txt-info-->
		</div>
		<!--/.col-right-->
	</div>
	<!--/#content-->
	<div class="pop-wrap daoru" id="importSalaryData-dialog">
		<div class="pop-title">导入工资单</div>
		<div class="pop-content clearfix">
			<span> <label>批量导入</label><input class="item-input"
				type="file" name="salaryData" id="importSalaryData-file"
				accept=".xls,.xlsx" />
			</span>
		</div>
		<div class="pop-btn">
			<span class="active" id="importSalaryData-confirm">导入</span><span>取消</span>
		</div>
	</div>
	<div class="bg-dialog"></div>

	<!--#foot-->
	<%@ include file="../common_new/foot.jsp"%>
	<!--#foot-->

	<script type="text/template" id="yearListTemp">
    {{~it:value:index}}
    <option value="{{=value}}">{{=value}}</option>
    {{~}}
</script>

	<script type="text/template" id="itemListTemp">
    {{~it:value:index}}
    <option value="{{=value.itemName}}">{{=value.itemName}}</option>
    {{~}}
</script>

	<script type="text/template" id="salaryHeaderTemp">
    <th>#</th>
    <th>姓名</th>
    {{~it:value:index}}
    <th title="{{=value.itemName}}"><em>{{=value.itemName}}</em></th>
    {{~}}
    <th>应发合计</th>
    <th>扣发合计</th>
    <th>实发合计</th>
</script>

	<script type="text/template" id="salaryListTemp">
    {{~it.salary:value:index1}}
    <tr id="{{=value.id}}">
        <td>{{=index1+1}}</td>
        <td title="{{=value.userName}}"><em>{{=value.userName}}</em></td>
        {{~it.item:i:index2}}
        {{var flag = false;}}
        {{~value.money:mon:index3}}
        {{ if(i.itemName == mon.itemName){ }}
        {{flag = true;}}
        <td id="{{=mon.itemName}}" class='editableTd'>{{=mon.m}}</td>
        {{ } }}
        {{~}}
        {{ if(!flag){ }}
        <td class='editableTd'>0</td>
        {{ } }}
        {{~}}
        <td>{{=value.ssStr}}</td>
        <td>{{=value.msStr}}</td>
        <td>{{=value.asStr}}</td>
    </tr>
    {{~}}
</script>
	<script type="text/template" id="salaryFilterHeaderTemp">
    <th>#</th>
    <th>姓名</th>
    {{~it:value:index}}
    <th title="{{=value.itemName}}"><em>{{=value.itemName}}</em></th>
    {{~}}
</script>
	<script type="text/template" id="salaryFilterListTemp">
    {{~it.salary:value:index1}}
    <tr id="{{=value.id}}">
        <td>{{=index1+1}}</td>
        <td title="{{=value.userName}}"><em>{{=value.userName}}</em></td>
        {{~it.item:i:index2}}
        {{var flag = false;}}
        {{~value.money:mon:index3}}
        {{ if(i.itemName == mon.itemName){ }}
        {{flag = true;}}
        <td id="{{=mon.itemName}}" class='editableTd'>{{=mon.m}}</td>
        {{ } }}
        {{~}}
        {{ if(!flag){ }}
        <td class='editableTd'>0</td>
        {{ } }}
        {{~}}
    </tr>
    {{~}}
</script>

	<!-- Javascript Files -->
	<!-- initialize seajs Library -->
	<script src="<%=basePath%>static_new/js/sea.js"></script>
	<!-- Custom js -->
	<script
		src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
	<script>
		var yearList = ${yearList};
	<%--var itemList = ${itemList};--%>
		var currYear = ${currYear};
		var currMonth = ${currMonth};
		seajs.use('salary');
	</script>
</body>
</html>