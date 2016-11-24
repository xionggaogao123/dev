<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.fulaan.base.controller.BaseController" %>
<%@ page import="com.pojo.user.UserRole" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>  
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>

  
<%@page import="com.pojo.app.SessionValue"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%    
    String path = request.getContextPath();    
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";    
    pageContext.setAttribute("basePath",basePath);  
    int userRole = new BaseController().getSessionValue().getUserRole();
    boolean isAdmin = UserRole.isHeadmaster(userRole) || UserRole.isK6ktHelper(userRole);
    boolean isEdu = UserRole.isEducation(userRole);
    boolean baseCanEdit = false;
    if(isAdmin){
    	baseCanEdit = true;
    }
    boolean rCanEdit = false;
    if(isAdmin){
    	rCanEdit = true;
    }
%> 
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技-资源管理</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="<%=basePath%>static_new/css/reset.css" rel="stylesheet" />
    <!-- dialog files -->
    <link href="<%=basePath%>static_new/js/modules/dialog/dialog.css" rel="stylesheet" />
    <!-- Custom styles -->
    <link href="<%=basePath%>static_new/css/ziyuan.css" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
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

    <!--.ziyuan-col-->
    <div class="ziyuan-col">

       

        <dl id="selectionArea">
            <dt>
                <label>分类</label>
                <select id="propertyType">
                    <option value="ver">教材分类</option>
                    <option value="kno">知识点分类</option>
                </select>
            </dt>
            
            
        </dl>

        <table class="zy-gray-table" width="100%">
            <thead>
                <th width="125">名称/封面</th>
                <th width="110">上传日期</th>
                <th width="120">教材版本</th>
                <th width="110">知识点</th>
                <th width="140">上传信息</th>
                <th>操作</th>
            </thead>
            <tbody id="coursewareList">
              
            </tbody>
        </table>


    </div>
    <!--/.ziyuan-col-->
	<!-- 分页div -->
	<div class="new-page-links">
	</div>

</div>
<!--/.col-right-->

</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script type="text/javascript" src="<%=basePath%>static_new/js/modules/core/0.1.0/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath%>static_new/js/modules/dialog/dialog.js"></script>
<script type="text/javascript" src="<%=basePath%>static_new/js/modules/flexpaper/flexpaper.js"></script>
<script type="text/javascript" src="<%=basePath%>static_new/js/modules/flexpaper/flexpaper_flash.js"></script>
<script type="text/javascript" src="<%=basePath%>static_new/js/modules/flexpaper/flexpaper_handlers.js"></script>
<script type="text/javascript" src="<%=basePath%>static_new/js/modules/player/sewise.player.min.js"></script>
<script type="text/javascript" src="<%=basePath%>static_new/js/modules/player/player.js"></script>
<script src="<%=basePath%>static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('ziyuanguanli');
</script>
</body>
</html>