<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技-学生评价系统</title>
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
	<link href="/static_new/css/indicator/evaluateResultInfo.css?v=2016102001" rel="stylesheet" />
	<script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>

</head>
<body>
<!--#head-->
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
    <!--  
    <img src="http://placehold.it/770x100" class="banner-info" />
    -->
    <!--/.banner-info-->

    <div class="grow-col">
		<!--.tab-col右侧-->
		<div class="tab-col">
			<div class="grow-col-head clearfix">
				<h3 class="grow-col-stu">学生评价结果</h3><span class="go-back">&lt;返回</span>
				<input id="appliedId" type="hidden" value="${dto.id}">
				<input id="termType" type="hidden" value="${dto.termType}">
				<input id="snapshotId" type="hidden" value="${dto.snapshotId}">
				<input id="classId" type="hidden" value="">
				<input id="evaluateId" type="hidden" value="">
			</div>
			<div class="applied-info">
				<span>评价名称:</span><em>${dto.name}</em>
				<br>
				<span>评价说明:</span><em>${dto.describe}</em>
			</div>
			<div class="evaluate-tab">
				<ul id="classList">
				</ul>
				<script type="text/template" id="j-tmpl3">
					{{ if(it.message.length>0){ }}
					{{ for (var i = 0, l = it.message.length; i < l; i++) { }}
					{{var obj=it.message[i];}}
					{{if(i == 0){}}
					<li class="li-cur" claId="{{=obj.id}}">
					{{}else{}}
					<li claId="{{=obj.id}}">
					{{}}}
						<span title="{{=obj.className}}">{{=obj.className}}</span>
						<em title="{{=obj.teacherName}}">{{=obj.teacherName}}</em>
					</li>
					{{}}}
					{{}}}
				</script>
			</div>
			<div class="main" id="main1">
				<div class="evaluate-select">
					<input id="name" name="name" value="">
					<button id="searchBtn" name="searchBtn">查找学生</button>
				</div>
				<table class="evaluate-table">
					<tr>
						<th>学生</th>
						<th>总评</th>
						<th>评价日期</th>
					</tr>
					<tbody id="studentList">

					</tbody>
					<script type="text/template" id="j-tmpl2">
						{{ if(it.rows.length>0){ }}
						{{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
						{{var obj=it.rows[i];}}
						<tr>
							<td class="evaluate-name" id="{{=obj.id}}">
								<a class="evaluate-a">{{=obj.commonToName}}</a>
							</td>
							<td>
								{{=obj.totalScore}}
							</td>
							<td>
								{{=obj.createDate}}
							</td>
						</tr>
						{{}}}
						{{}}}
					</script>
				</table>
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
			</div>
		</div>
	</div>
<!--/.col-right-->

</div>
<!--/#content-->
</div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %> 
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
	seajs.use('evaluateRInfo',function(evaluateRInfo){
		evaluateRInfo.init();
	});

</script>
</body>
</html>