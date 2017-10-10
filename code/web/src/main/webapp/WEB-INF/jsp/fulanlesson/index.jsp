<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
	<meta charset='utf-8'>
	<title>平台统计管理</title>
	<link rel="stylesheet" type="text/css" href="/wap/css/plateform.css">
	<script type="text/javascript" src="/wap/js/jquery-2.1.1.min.js"></script>
</head>
<body>
	<div class="head">
		<div class="head-cont">
			<img src="/wap/images/logo.png">
			<div class="head-info">欢迎您, ${userName}</div>
			<input id="userId" type="hidden" value="${userId}">
			<input id="userName" type="hidden" value="${userName}">
		</div>
	</div>
	<div class="static-main">
			<ul class="ul-tab" id="studentList">

			</ul>
			<script type="text/template" id="studentList_templ">
				{{ if(it.message.rows.length>0){ }}
				{{ for (var i = 0, l = it.message.rows.length; i < l; i++) { }}
				{{var obj=it.message.rows[i];}}
				<li>
					<img src="/wap/images/table.png">
					<span class="sp-x" name="{{=obj.id}}"></span>
					<span class="sp-e" name="{{=obj.id}}"></span>
					<p class="p1 attr" name="{{=obj.id}}" style="cursor:pointer">{{=obj.name}}</p>
					<p class="p2 attr" name="{{=obj.id}}" style="cursor:pointer">{{=obj.dateTime}}</p>
				</li>
				{{ } }}
				{{ } }}
			</script>
			<%--<li>
				<img src="/wap/images/table.png">
				<span class="sp-x"></span>
				<span class="sp-e"></span>
				<p class="p1">吴杰操</p>
				<p class="p2">2017-09-12</p>
			</li>
			<li>
				<img src="/wap/images/table.png">
				<span class="sp-x"></span>
				<span class="sp-e"></span>
				<p class="p1">梅梁鑫</p>
				<p class="p2">2017-09-12</p>
			</li>--%>

	</div>
	<!--分页-->
	<div class="new-page-links">
	</div>
	<script src="/static/js/sea.js"></script>
	<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
	<script>
		seajs.use('fulanlesson');
	</script>
</body>
</html>