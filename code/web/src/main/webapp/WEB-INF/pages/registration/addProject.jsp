<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%    
    String path = request.getContextPath();    
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";    
    pageContext.setAttribute("basePath",basePath);  
%> 
<!DOCTYPE html>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技-项目管理</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="<%=basePath%>static_new/css/reset.css" rel="stylesheet" />
    <link href="<%=basePath%>static_new/css/project.css" rel="stylesheet" />
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="<%=basePath%>static_new/css/projectManage.css?v=2015041602" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
</head>
<script>
	var PUBLIC_PARENT_ID = "${pid}";
</script>
<style>
    .pop-wrap {
        width: 360px;
        background: #fff;
        position: absolute;
        top: 30px;
        left: 50%;
        margin-left: -180px;
        z-index: 99;
        display: none;
    }
    .bg-dialog{
        width: 100%;
        height: 100%;
        background: rgba(0,0,0,.7);
        position: absolute;
        top: 0;
        left: 0;
        z-index: 98;
        display: none;
    }
    .pop-title{
        height: 48px;
        line-height: 48px;
        border-bottom: 1px solid #808080;
        color: #5db75d;
        font-size: 18px;
        padding-left: 40px;
        background: #fff;
        display: block;
    }
    .pop-content{
        width: 100%;
        background: #fff;
        padding-top: 10px;
    }
    .pop-content table{
        width: 230px;
        margin: 0 auto;
    }
    .pop-content table td{
        line-height: 40px;
    }
    .pop-btn{
        width: 210px;
        margin: 30px auto;
    }
    .pop-btn span{
        display: inline-block;
        width: 76px;
        height: 28px;
        line-height: 28px;
        text-align: center;
        color: #000;
        font-size: 14px;
        border: 1px solid #d1d1d1;
        cursor: pointer;
    }
    .pop-btn span.active{
        background: #ffa800;
        border: 1px solid #ffa800;
        color: #fff;
        margin-right: 25px;
    }
</style>
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
        <!--/.banner-info-->

        <div class="grow-col">

            <div class="grow-col-head clearfix proMana">
                <h3><span onclick="location.href='/quality/list.do';" style="cursor:pointer">项目管理</span>>子项目</h3>
                <a href="#" class="addSubProject">+新建子项目</a>
            </div>
            <div class="project clearfix">
            
            	<c:forEach var="project" items="${datas}" >
  					<div class="projectList" flag="${project.id}">
                    	<div class="projectDetail no-pointer">
                        	<p>评定要素：${project.name}</p>
                        	<p>主要表现要求：${project.requirement}</p>
                    	</div>
                    	<div class="projectEdit">
                        	<a href="#" class="orange-btn-new" myname="${project.name}" myrequirement="${project.requirement}">编辑</a>
                        	<a href="#" class="grey-btn-delete">删除</a>
                    	</div>
                	</div>
				</c:forEach>
            

            </div>

       

        </div>

    </div>
    <!--/.col-right-->

</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp" %> 
<!--#foot-->

<!--.添加子项目-->
<div class="pop-wrap" id="addSubProj">
    <div class="pop-title">添加子项目<span class="depic"></span></div>
    <div class="pop-content">
        <table class="addSubProj-table">
            <tr>
                <td >评定要素</td>
                <td width="210"><input type="text" id="newSubProjectName"/></td>
                
            </tr>
            <tr>
                <td >表现要求</td>
                <td><input type="text" id="newSubProjectRequirement"/></td>
            </tr>
            
            
        </table>
    </div>
    <div class="pop-btn"><span class="active" id="newSubConfirmBtn">确定</span>
    </div>
</div>
<!--/.添加子项目-->

<!--.编辑子项目-->
<div class="pop-wrap" id="editSubProj">
    <div class="pop-title">编辑子项目<span class="depic"></span></div>
    <div class="pop-content">
        <table class="addSubProj-table">
            <tr>
                <td >评定要素</td>
                <td width="210"><input type="text" id="editSubProjectName"/></td>
                
            </tr>
            <tr>
                <td >表现要求</td>
                <td><input type="text" id="editSubProjectRequirement"/></td>
            </tr>
            
            
        </table>
    </div>
    <div class="pop-btn"><span class="active" id="editSubConfirmBtn">确定</span>
    </div>
</div>
<!--/.添加子项目-->

<div class="bg-dialog"></div>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="<%=basePath%>static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('addProject')
</script>
</body>
</html>