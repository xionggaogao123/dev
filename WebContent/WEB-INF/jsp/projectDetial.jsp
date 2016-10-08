<%@ include file="/common/taglib.jsp" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html lang="en">
    <head>
    	<title>我的项目清单</title>
        <meta charset="utf-8">
        <script type="text/javascript" src="${ctx }/js/jquery-1.11.1.js"></script>
        <link rel="stylesheet" type="text/css" href="${ctx }/css/reset.css">
    </head>
    <body>
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
                <p class="p-detial-top">
                    <em class="em1" onclick="window.location.href='${ctx}/project/list'">我的项目清单</em>
                    <em class="em2">></em>
                    <em class="em2">${project.projectName }</em>
                </p>
                <div class="detial-cont1">
                    <p class="detial-item-tit dt1">基本信息</p>
                    <p class="p1 clearfix">
                        <span class="sp1">项目名称：</span>
                        <span class="sp2">${project.projectName }</span>
                    </p>
                    <p class="p1 clearfix">
                        <span class="sp1">项目起止时间：</span>
                        <span><fmt:formatDate value="${project.startDate }"/>至<fmt:formatDate value="${project.endDate }"/></span>
                    </p>
                    <p class="p1 clearfix">
                        <span class="sp1">项目编号：</span>
                        <span class="sp2">${project.projectNumber }</span>
                    </p>
                    <p class="p1 clearfix">
                        <span class="sp1">项目描述：</span>
                        <span class="sp3">${project.projectDesc }</span>
                    </p>
                </div>
                <div class="detial-cont1">
                    <p class="detial-item-tit dt2">项目团队</p>
                    <p class="p2 detial-cont1-p3">
                        <span class="sp1">
                            <em>负责人</em>
                        </span>
                        <em>${project.projectOwner.name }</em>
                    </p>
                    <c:forEach items="${sdtoMap }" var="map">
                    	<p class="p2">
                        <span class="sp1">
                            <em>${map.key }</em>
                        </span>
                        <c:forEach items="${map.value }" var="list">
                        	<em>${list.name }</em>
                        </c:forEach>
                    	</p>
                    </c:forEach>
                </div>
                <div class="detial-cont1">
                    <p class="detial-item-tit dt3">项目文档
                        <span class="sp1"><em>+</em>新建文件夹</span>
                        <span class="sp2">上传</span>
                    </p>
                    <p class="p-doc-all">
                        <label>
                            <input type="checkbox">全选
                        </label>
                        <button>下载</button>
                    </p>
                    <ul class="ul-doc">
                        <li>
                            <label>
                                <input type="checkbox">
                                <img src="${ctx }/images/img_floder.png">
                                <span>国庆节放假通知</span>
                            </label>
                            <em>下载</em>
                            <em>重命名</em>
                            <em>删除</em>
                        </li>
                        <li>
                            <label>
                                <input type="checkbox">
                                <img src="${ctx }/images/img_word.png">
                                <span>国庆节放假通知</span>
                            </label>
                            <em>下载</em>
                            <em>重命名</em>
                            <em>删除</em>
                        </li>
                    </ul>
                </div>
            </div>
    	</div>
    </body>
</html>