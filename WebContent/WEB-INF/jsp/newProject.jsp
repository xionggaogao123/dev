<%@ include file="/common/taglib.jsp" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
    	<title>新建项目</title>
    	<link rel="shortcut icon" href="${ctx }/images/page_logo1.png" >
        <meta charset="utf-8">
        <script type="text/javascript" src="${ctx }/js/jquery-1.11.1.js"></script>
        <script type="text/javascript" src="${ctx }/js/manage.js"></script>
        <script type="text/javascript" src="${ctx }/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="${ctx }/js/dateTimePicker/bootstrap-datetimepicker.min.js"></script>
        <script type="text/javascript" src="${ctx }/js/dateTimePicker/locals/bootstrap-datetimepicker.zh-CN.js"></script>
        <link rel="stylesheet" type="text/css" href="${ctx }/css/bootstrap-datetimepicker.min.css">
        <link rel="stylesheet" type="text/css" href="${ctx }/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="${ctx }/css/reset.css">
        <style type="text/css">
        	label {
    			font-weight: normal;
			}
			.dropdown-menu {
				top: 368px !important;
			}
        </style>
    </head>
    <body>
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
    				<div class="li1-imgi"></div>
    				<p class="p-blue">创建新项目</p>
    			</li>
    			<li class="li2" onclick="window.location.href='${ctx}/project/list'">
    				<div class="li2-img"></div>
    				<p>我的项目清单</p>
    			</li>
    			<li class="li3" onclick="window.location.href='${ctx}/staff/list'">
    				<div class="li3-img"></div>
    				<p>公司人员管理</p>
    			</li>
    		</ul>
    	</div>

    	<div class="container">
    		<form action="${ctx }/project/save" method="post" onsubmit="return check()">
    		<ul class="ul-new">
    			<li>
    				<h3>创建新项目</h3>
    			</li>
    			<li>
    				<span class="sp1"><em>*</em>项目名称</span>
    				<input type="text" name="projectName" placeholder="输入项目名称" class="inp1">
    			</li>
    			<li>
    				<span class="sp1"><em>*</em>项目编号</span>
    				<input type="text" name="projectNumber" placeholder="输入项目编号" class="inp1">
    			</li>
    			<li>
    				<span class="sp1"><em>*</em>项目起止时间</span>
    				<input type="text" placeholder="项目开始时间" name="startDate" class="inp2" id="startDate" readonly="readonly" style="top: 368px;">
    				<span class="sp2">-</span>
    				<input type="text" placeholder="项目结束时间" name="endDate" class="inp2" id="endDate" readonly="readonly">
    			</li>
    			<li>
    				<span class="sp1"><em>*</em>项目负责人</span>
                    <select name="projectOwner.id">
                    	<option value="-1">选择项目负责人</option>
                    	<c:forEach items="${staffs }" var="s">
                    		<option value="${s.id }">${s.name }</option>
                    	</c:forEach>
                    </select>
    			</li>
    			<%-- <li class="clearfix">
    				<span class="sp1"><em>*</em>项目成员</span>
    				<div class="clearfix item-conts">
    					<p class="p1">
    						<button class="btn-addm" type="button">添加成员</button>
    					</p>
    					<ul class="ul1 clearfix" id="all_member">
    				    	<c:forEach items="${sdtoSubDepartMap }" var="map">
    				    		<li class="clearfix" style="display: none;"> 
	    						<span>${map.value[0].department }<c:if test="${not empty map.value[0].subDepartment }">-${map.value[0].subDepartment }</c:if>
	    						</span>
	    						<p class="p-memb clearfix">
    				    			<c:forEach items="${map.value }" var="subDepartDto">
    				    				<input class="id_${subDepartDto.id}" type="hidden">
		    							<span class="id_${subDepartDto.id}" style="display: none;">${subDepartDto.name }<em>×</em></span>
    				    			</c:forEach>
	    						</p>
								</li>
    				    	</c:forEach>
    					</ul>
    				</div>
    			</li> --%>
    			<li>
    				<span class="sp1"><em>*</em>项目成员</span>
    				<span class="sp3"></span>
    				<div class="member-list">
    				    <c:forEach items="${sdtoMap }" var="map">
    				        <p>${map.key }<em class="arrow"></em></p>
    				        <ul class="ul-list-detial">
    				        <c:forEach items="${map.value }" var="sDto">
    				        	<li>
    								<label>
    									<span>${sDto.name }</span>
    									<input name="staffs" value="${sDto.id }" type="checkbox">
    								</label>
    							</li>
    				        </c:forEach>
    				        </ul>
    				    </c:forEach>
    				</div>
    			</li>
    			<li class="clearfix">
    				<span class="sp1"><em>*</em>项目描述</span>
    				<textarea name="projectDesc"></textarea>
    			</li>
    			<li class='li-pad'>
                    <button class="btn-ok" type="submit">确定</button>
                    <!-- <button class="btn-no">取消</button> -->
                </li>
    		</ul>
    		</form>
    	</div>
    	<div class="bg"></div>
    	<div class="wind-del wind">
            <p class="p1">提示<em></em></p>
            <ul class="ul-infor">
                <li id="info"></li>
                <li class="tcenter">
                    <button class="btn-ok">确定</button>
                    <button class="btn-no">取消</button>
                </li>
            </ul>
        </div>
    	<div class="wind-addmember wind" style="top: 50%">
    		<p class="p1">添加成员<em></em></p>
    		<ul class="ul-fl">
    			<c:forEach items="${sdtoMap }" var="map">
    				<li class="clearfix">
    					<span>${map.key }</span>
	    				<div class="clearfix p-wind-cont">
	    					<c:forEach items="${map.value }" var="sDto">
	    						<div class="div-per-cont">
		    						<label>
		    							<input value=${sDto.id } type="checkbox">${sDto.name }
		    						</label>
	    							<p class="p-infor">
	    								<span>职务：${sDto.jobTitle }</span>
	    								<span>工号：${sDto.jobNumber }</span>
	    							</p>
    							</div>
    				        </c:forEach>
	    				</div>
    				</li>
    			</c:forEach>
    			<li style="text-align: center">
    				<button class="btn-ok" onclick="chooseGroup()">确定</button>
    				<button class="btn-no">取消</button>
    			</li>
    		</ul>
    	</div>
    </body>
</html>