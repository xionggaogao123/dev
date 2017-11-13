<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
	<meta charset='utf-8'>
	<title>平台统计管理</title>
	<link rel="stylesheet" type="text/css" href="/wap/css/plateform.css">
	<script type="text/javascript" src="/wap/js/jquery-2.1.1.min.js"></script>
	<script type="text/javascript" src="/wap/js/echarts.min.js"></script>
	<script type="text/javascript" src="/wap/js/macarons.js"></script>
	<script type="text/javascript" src="/wap/js/plateform.js"></script>
</head>
<body>
	<div class="head">
		<div class="head-cont">
			<img src="/wap/images/logo.png">
			<div class="head-info">欢迎您，${userName}</div>
			<input id="userId" type="hidden" value="${userId}">
			<input id="id" type="hidden" value="${id}">
			<input id="userName" type="hidden" value="${userName}">

		</div>
	</div>
	<div class="static-main">
		<div class="tit">学生总活跃度统计<em id="all">（班级平均活跃度：28）</em></div>
		<div class="clearfix">
			<table class="gretab wd50">
				<thead>
					<tr>
						<th width="25%">排名</th>
						<th width="25%">姓名</th>
						<th width="25%">活跃值</th>
						<th width="25%"></th>
					</tr>
				</thead>
				<tbody id="it1">

				</tbody>
				<script type="text/template" id="it1_teml">
					{{ if(it.message.dto1.length>0){ }}
					{{ for (var i = 0, l = it.message.dto1.length; i < l; i++) { }}
					{{var obj=it.message.dto1[i];}}
					<tr>
						<td>{{=obj.parming}}</td>
						<td>{{=obj.userName}}</td>
						<td>{{=obj.score}}</td>
						<td></td>
					</tr>
					{{ } }}
					{{ } }}
				</script>
			</table>
			<table class="gretab wd50">
				<thead>
					<tr>
						<th width="25%"></th>
						<th width="25%">排名</th>
						<th width="25%">姓名</th>
						<th width="25%">活跃值</th>
					</tr>
				</thead>
				<tbody id="it2">

				</tbody>
				<script type="text/template" id="it2_teml">
					{{ if(it.message.dto2.length>0){ }}
					{{ for (var i = 0, l = it.message.dto2.length; i < l; i++) { }}
					{{var obj2=it.message.dto2[i];}}
					<tr>
						<td></td>
						<td>{{=obj2.parming}}</td>
						<td>{{=obj2.userName}}</td>
						<td>{{=obj2.score}}</td>
					</tr>
					{{ } }}
					{{ } }}
				</script>
			</table>
		</div>
		<!--分页-->
		<div class="new-page-links">
		</div>
		<%--<div class="tit">答题统计<em>（班级平均得分：28）</em></div>--%>

		<div class="chart" id="anna1" style="display:none"></div>

		<div class="tit">答题结果详情</div>
		<div class="divsel tit">
			<select id="slist">
				<option>第一次答题</option>
			</select>
		</div>
		<div class="clearfix" id="answerList">
			<table class="gretab wd100 mb100">
				<thead>
					<tr>
						<th width="15%">序号</th>
						<th width="25%">姓名</th>
						<th width="30%">结果</th>
						<th width="30%">用时</th>
					</tr>
				</thead>
				<tbody id="it3">

				</tbody>
				<script type="text/template" id="it3_teml">
					{{ if(it.message.list.length>0){ }}
					{{ for (var i = 0, l = it.message.list.length; i < l; i++) { }}
					{{var obj2=it.message.list[i];}}
					<tr>
						<td>{{=obj2.parming}}</td>
						<td>{{=obj2.userName}}</td>
						<td>{{=obj2.answer}}</td>
						<td>{{=obj2.time}}</td>
					</tr>
					{{ } }}
					{{ } }}
				</script>
			</table>
			</div>
		<!--分页-->
		<div class="new-page-links2">
		</div>
	</div>
	<script src="/static/js/sea.js"></script>
	<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
	<script>
		seajs.use('fulanlessonresult');
	</script>
</body>
</html>