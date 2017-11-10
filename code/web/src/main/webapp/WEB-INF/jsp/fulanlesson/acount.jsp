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
		<div class="tit">学生总活跃度统计<em>（班级平均活跃度：28）</em></div>
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
				<tbody>
					<tr>
						<td>1</td>
						<td>肚子疼</td>
						<td>23</td>
						<td></td>
					</tr>
					<tr>
						<td>1</td>
						<td>肚子疼</td>
						<td>23</td>
						<td></td>
					</tr>
					<tr>
						<td>1</td>
						<td>肚子疼</td>
						<td>23</td>
						<td></td>
					</tr>
					<tr>
						<td>1</td>
						<td>肚子疼</td>
						<td>23</td>
						<td></td>
					</tr>
					<tr>
						<td>1</td>
						<td>肚子疼</td>
						<td>23</td>
						<td></td>
					</tr>
				</tbody>
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
				<tbody>
					<tr>
						<td></td>
						<td>2</td>
						<td>肚子疼</td>
						<td>23</td>
					</tr>
					<tr>
						<td></td>
						<td>2</td>
						<td>肚子疼</td>
						<td>23</td>
					</tr>
					<tr>
						<td></td>
						<td>2</td>
						<td>肚子疼</td>
						<td>23</td>
					</tr>
					<tr>
						<td></td>
						<td>2</td>
						<td>肚子疼</td>
						<td>23</td>
					</tr>
					<tr>
						<td></td>
						<td>2</td>
						<td>肚子疼</td>
						<td>23</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="tit">答题统计<em>（班级平均得分：28）</em></div>
		<div class="divsel">
			<select>
				<option>第一次答题</option>
			</select>
		</div>
		<div class="chart" id="anna1"></div>

		<div class="tit">答题结果详情</div>
			<table class="gretab wd100 mb100">
				<thead>
					<tr>
						<th width="15%">序号</th>
						<th width="25%">姓名</th>
						<th width="30%">结果</th>
						<th width="30%">用时</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>1</td>
						<td>肚子疼</td>
						<td>A</td>
						<td>23</td>
					</tr>
					<tr>
						<td>1</td>
						<td>肚子疼</td>
						<td>A</td>
						<td>23</td>
					</tr>
					<tr>
						<td>1</td>
						<td>肚子疼</td>
						<td>A</td>
						<td>23</td>
					</tr>
					<tr>
						<td>1</td>
						<td>肚子疼</td>
						<td>A</td>
						<td>23</td>
					</tr>
				</tbody>
			</table>
	</div>
	<script src="/static/js/sea.js"></script>
	<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
	<script>
		seajs.use('fulanlessonresult');
	</script>
</body>
</html>