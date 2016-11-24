<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html>
<html>
<head>
<title>课程购买成功</title>
<meta charset="utf-8"/>
<link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
<link rel="stylesheet" href="/static/css/dialog.css" type="text/css"/>
<link rel="stylesheet" href="/static/css/bootstrap-combined.school.css"/>
<link rel="stylesheet" href="/static/css/excellentLesson.css"/>
<script type="text/javascript" src="/static/js/jquery.min.js"></script>
<script type="text/javascript" src="/static/js/sharedpart.js"></script>
<script src="/static/js/bootstrap-paginator.min.js" type="text/javascript"></script>
<style type="text/css">
	#prompt-div{
		padding-top:300px;
	}
</style>
<script type="text/javascript">
var currentPageID = 11;
$(function(){
	setTimeout(function(){
		location.href = '/excellentLesson?type=2';
	},3*1000); 
	MessageBox('购买成功,页面将跳转至已购课程...', 1);
});
</script>
</head>
<body csid="<% if(request.getParameter("courseId")!=null){out.println(java.net.URLDecoder.decode(request.getParameter("courseId"),"UTF-8")); }%>">

<%@ include file="../common_new/head.jsp" %>
<div id="content_main_container">
	<div id="content_main">
		<!-- 左侧导航-->
		<%@ include file="../common_new/col-left.jsp" %>
		<!-- left end -->
		<!-- right start-->
		<div id="right-container">
			<div id="content_main_container" style="margin-top: 5px;">
				<div id="main-content" style="position: relative; overflow:hidden;background:#fff;margin-bottom: 50px;">
					<div id="account-right">
						<div class="account-right-content" style="text-align:center;">
							<div class="account-balance">
								<span class="payresult" style="margin-left: 280px;"><img src="/img/paysuccess.png" style="margin-right: 85px;"/></span>
								<span class="big-blod payresult ellipsis" style="width: 200px;">您已成功购买${lessonName}!</span>
							</div>
							<div class="operations">
								<span style="margin-right: 10px;">您可能需要:</span>
								<a href="/excellentLesson/accountOrder"><span>查看余额</span></a>
								<a href="/excellentLesson/order"><span>交易记录</span></a>
								<a href="/excellentLesson/balance"><span style="border:0;">我要充值</span></a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- right end-->
	</div>
	<div>

<%@ include file="../common_new/foot.jsp" %>
</body>
</html>