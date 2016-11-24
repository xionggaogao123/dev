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
    boolean isAdmin = UserRole.isManager(userRole);
%> 
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技-校区管理</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="<%=basePath%>static_new/css/reset.css" rel="stylesheet" />
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="<%=basePath%>static_new/css/classManage.css" rel="stylesheet" />
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

        <div class="grow-tab-head clearfix ">
            <ul class="clearfix">
                
                <li><a href="" class="">功能教室管理</a></li>
            </ul>

        </div>    

    <div class="grow-col">

        <div>
            <!-- 功能教室管理-->
            <div>
                <div class="clearfix">
                
                 <a class="addClass" href="">添加功能教室</a>
            </div>
                <table class="gray-table " width="100%">
                    <thead >
                        <th colspan="4" class="thead">教室列表</th>
                    </thead>
                    <tbody id="classRoomList">
                        <tr class="ttitle">
                           <td>序号</td>
                           <td>教室名称</td>
                           <td>委托管理者</td>
                           <td>操作</td>
                        </tr>
                        <!--  
                        <tr>
                            <td>1</td>
                            <td>篮球馆</td>
                            <td>张老师</td>
                            <td><a href="" class="abtn editClass">编辑</a><a href="" class="abtn count">统计</a><a href="" class="del">删除</a></td>
                        </tr>
                        <tr>
                            <td>2</td>
                            <td>大礼堂</td>
                            <td>张老师</td>
                            <td><a href="" class="abtn editClass">编辑</a><a href="" class="abtn count">统计</a><a href="" class="del">删除</a></td>
                        </tr>
                        <tr>
                            <td>3</td>
                            <td>通用技术教室</td>
                            <td>张老师</td>
                            <td><a href="" class="abtn editClass">编辑</a><a href="" class="abtn count">统计</a><a href="" class="del">删除</a></td>
                        </tr>
                        <tr>
                            <td>4</td>
                            <td>多媒体教室</td>
                            <td>张老师</td>
                            <td><a href="" class="abtn editClass">编辑</a><a href="" class="abtn count">统计</a><a href="" class="del">删除</a></td>
                        </tr>
                        -->
                    </tbody>        
                </table>
               
            </div>
           
        </div>
          <!-- 分页 -->
	    <div class="fenye">
	        <div class="inline clearfix">
	            <div class="new-page-links" id="fun-clasroom-list"></div>
	            
	        </div>
	    </div>
        <!-- 分页 -->
    </div>
  <input id="poptype" hidden="hidden">
</div>
<!--/.col-right-->

</div>
<!--/#content-->

<!--#foot-->
 <%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- 添加功能教室 -->
<div class="pop-wrap" id="addClass">
    <div class="pop-title">
        <span>添加功能教室</span>
        <span class="closePop"></span>
    </div>
    <div class="pop-content">
        <table>
            <tbody>
            <tr>
                <td class="right">序号</td>
                <td><input type="text" id="number"></td>                
            </tr>
            <tr>
                <td class="right">教室名称</td>
                <td ><input type="text" class="className" id="className"></td>                
            </tr>
            <tr>
                <td class="right">委托管理者</td>
                <td class="cruser">
                    <a class="manage-ren-alert zb-ren-add" uid=""></a>
                    <%--<select id="manager">
                        <option value="ALL">请选择</option>
                        <!--  
                        <option>教师</option>
                        <option>管理员</option>
                         -->
                    </select>--%>
                </td>                
            </tr>            
            </tbody>
        </table>
    </div>
    <div class="pop-btn">
        <a class="sure" id="addDone">确定</a>
        <a class="undo" id="addCancel">取消</a>
        
    </div>    
</div>
<!-- 编辑功能教室 -->
<div class="pop-wrap" id="editClass">
    <div class="pop-title">
        <span>编辑功能教室</span>
        <span class="closePop"></span>
    </div>
    <div class="pop-content">
        <table>
            <tbody>
            <tr>
                <td class="right">序号</td>
                <td><input type="text" id="editNum"></td>                
            </tr>
            <tr>
                <td class="right">教室名称</td>
                <td ><input type="text" class="className" id="editClassName"></td>                
            </tr>
            <tr>
                <td class="right">委托管理者</td>
                <td class="cruser2">
                    <a class="manage-ren-alert zb-ren-add" uid=""></a>
                    <%--<select id="manager">
                        <option value="ALL">请选择</option>
                        <!--
                        <option>教师</option>
                        <option>管理员</option>
                         -->
                    </select>--%>
                </td>
            </tr>            
            </tbody>
        </table>
    </div>
    <div class="pop-btn">
        <a class="sure" id="editDone">确定</a>
        <a class="undo" id="editCancel">取消</a>
        
    </div>    
</div>
<!-- 添加功能教室人员 -->
<div class="pop-wrapp" id="" style="display: none;">
    <div class="pop-title">
        <span>添加委托管理者</span>
        <span class="closePop change-user"></span>
    </div>
    <div class="pop-content">
        <table>
            <tbody>
            <tr>
                <td class="right">委托管理者</td>
                <td >
                    <select id="manager">
                        <%--<option value="ALL">请选择</option>--%>
                        <!--
                        <option>请选择</option>
                        <option>教师</option>
                        <option>管理员</option>
                        -->
                    </select>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="pop-btn">
        <a class="sure sure-user">确定</a>
        <a class="undo change-user">取消</a>

    </div>
</div>
<!-- 教师管理-统计 -->
<div class="pop-wrap" id="count">
    <div class="pop-title">
        <span>篮球馆预约详情</span>
        <span class="closePop"></span>
    </div>
    <div class="pop-content">
        <div class="term">
            <label>选择学期</label>
            <select id="term">
            <!--  <option>请选择</option> --> 
            </select>
        </div>
        <table>
            <tbody id="appointDetial">
                <tr>
                    <th>起始时间</th>
                    <th>结束时间</th>
                    <th>使用者</th>
                    <th>使用情况说明</th>

                </tr>
                <!-- 
                <tr>
                    <td>2015/12/12&nbsp;10:00</td>
                    <td>2015/12/12&nbsp;10:00</td>
                    <td>语文</td>
                    <td>张老师</td>
                    <td>上课</td>
                </tr>
                 -->
            </tbody>
            
        </table>
    </div>
    <div class="fenye">
        <div class="inline clearfix">
            <div class="new-page-links" id="count-detial"></div>
            
        </div>
    </div>
       
</div>


<div class="bg-dialog"></div>


<!-- Javascript Files -->
<!-- initialize seajs Library -->

<script src="<%=basePath%>static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js"></script>
<script>
    seajs.use('classManage');
    seajs.use('pagination');

</script>
</body>
</html>
