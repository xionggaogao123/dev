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
    		<button onclick="window.location.href='${ctx}/static/ch-pw.html'" class="ch-pw">修改密码</button>
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
                <div class="status-div">
                	<em class="em-tx">项目状态：</em>
                	<select id="prjStatus" class="status-sel">
                		<option value="-1">全部</option>
                		<c:forEach items="${status }" var="s">
                			<option value="${s.value }">${s.label }</option>
                		</c:forEach>
                	</select>
                	<button id="searchBtn" class="btn-ok">确定</button>
                </div>
                <table class="tab-project mt38">
                    <tbody>
                    	<tr>
                        	<th class="th1">项目名称</th>
                        	<th class="th2">项目负责人</th>
                        	<th class="th3">项目创建人</th>
                        	<th class="th4">创建日期</th>
                    	</tr>
                	</tbody>
                </table>
                <ul class="ul-company-member" id='pro_list'>
                </ul>

                <div class="page" id="paginaion">
                    <ul id="list" class="pagination"></ul>
                </div>
            </div>
    	</div>

    </body>
</html>