<%@ include file="/common/taglib.jsp" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
    	<title>我的项目清单</title>
        <meta charset="utf-8">
        <script type="text/javascript" src="${ctx }/js/jquery-1.11.1.js"></script>
        <link rel="shortcut icon" href="${ctx }/images/page_logo1.png" >
        <link rel="stylesheet" type="text/css" href="${ctx }/css/bootstrap.min.css">
        <script type="text/javascript" src="${ctx }/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="${ctx }/js/jqPaginator.js"></script>
        <script type="text/javascript" src="${ctx }/js/projectList.js"></script>
        <script type="text/javascript" src="${ctx }/js/dateTimePicker/bootstrap-datetimepicker.min.js"></script>
        <link rel="stylesheet" type="text/css" href="${ctx }/css/reset.css">
    </head>
    <body>
    	<input type="hidden" id="ctx" value="${ctx }">
    	<input type="hidden" id="totalPage" value="${totalPage }">
    	<div class="header">
    		<button onclick="window.location.href='${ctx}/user/logout'">退出</button>
    		<p class="p2">${loginedStaff.name }&nbsp;&nbsp;&nbsp;${loginedStaff.jobTitle }<br>${loginedStaff.loginName }</p>
    		<img class="img2" src="${ctx }/images/img_top_head.png">
    		<img class="img1" src="${ctx }/images/top_logo.png">
    		<p class="p1">复兰项目管理</p>
    	</div>
    	<div class="left-nav">
    		<ul>
    			<li class="li1" onclick="window.location.href='${ctx}/project/new_project'">
    				<div class="li1-img"></div>
    				<p>创建新项目</p>
    			</li>
    			<li class="li2" onclick="window.location.href='${ctx}/project/list'">
    				<div class="li2-imgi"></div>
    				<p class="p-blue">我的项目清单</p>
    			</li>
    			<li class="li3" onclick="window.location.href='${ctx}/staff/list'">
    				<div class="li3-img"></div>
    				<p>公司人员管理</p>
    			</li>
    		</ul>
    	</div>

    	<div class="container">
            <div class="my-list-cont">
                <h3>我的项目清单</h3>
                <ul class="ul-company-member" id='pro_list'>
                </ul>

                <div class="page" id="paginaion">
                    <ul id="list" class="pagination"></ul>
                </div>
            </div>
    	</div>

    </body>
</html>