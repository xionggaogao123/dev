<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.fulaan.base.controller.BaseController" %>
<%@ page import="com.pojo.user.UserRole" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>  
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>
  
<%@page import="com.pojo.app.SessionValue"%>
<!DOCTYPE html>
<%    
    String path = request.getContextPath();    
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";    
    pageContext.setAttribute("basePath",basePath);  
    int userRole = new BaseController().getSessionValue().getUserRole();
    boolean isAdmin = UserRole.isManager(userRole) || UserRole.isHeadmaster(userRole) || UserRole.isK6ktHelper(userRole);
%>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="<%=basePath%>static_new/css/reset.css" rel="stylesheet" />
    <!-- jquery ui styles -->
    <link href="<%=basePath%>static_new/js/modules/treeView/0.1.0/jquery.treeview.css" rel="stylesheet" />
    <!-- Custom styles -->
    <link href="<%=basePath%>static_new/css/baoxiuguanli.css?v=2015071201" rel="stylesheet" />
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
            <!-- <img src="http://placehold.it/770x100" class="banner-info" /> -->
            <!--/.banner-info-->

            <!--.tab-col-new-->
            <div class="tab-col-new">
                <div class="tab-head-new clearfix">
                    <ul>
                        <li class="cur">
                            <i></i>
                            <a href="#bxclId">报修处理</a>
                        </li>
                    </ul>
                </div>
                <div class="tab-main-new clearfix">

                    <!--.报修处理-->
                    <div class="bxcl-view" id="bxclId">

                        <div class="bxqjcx-tit">
                            <label>报修学期:
                                <select id="eduHandlerTermType" name="termType">
                                	<c:forEach items="${list}" var="term">
                    					<option value="${term}">${term}</option>
               						</c:forEach>
               					</select>
                            </label>
                            <label>上报学校:
                                <select id="eduHandlershools" name="repairShoolId">
                                	<option value="ALL">所有学校</option>
                                	<c:forEach items="${shlList}" var="shool">
                    					<option value="${shool.id}">${shool.name}</option>
               						</c:forEach>
                                </select>
                            </label>
                            <label>报修状态:
                                <select id="eduHandlerRepairProcedure" name="repairProcedure">
                                	<option value="ALL">全部状态</option>
                                	<option value="待处理">待处理</option>
                                	<option value="已受理">已受理</option>
                                	<option value="已完毕">已完毕</option>
                                </select>
                            </label>
                            <button type="button" class="gray-sm-btn" id="queryEduHandlerListBtn">查询</button>
                        </div>

                        <h3 id="eduManageListTitle">报修明细  所有学校（2015年第一学期）</h3>

                        <table class="gray-table" width="100%">
                            <thead>
                                <th>报修学校</th>
                                <th>报修日期</th>
                                <th>报修人</th>
                                <th>报修地点</th>
                                <th>内容</th>
                                <th>进度</th>
                                <th>维修人</th>
                                <th>结果</th>
                                <th>操作</th>
                            </thead>
                            <tbody id="handlerEduList">
                               <!-- 
                                <tr>
                                    <td>复兰大学</td>
                                    <td>2015-07-06</td>
                                    <td>复兰管理员</td>
                                    <td>教学楼A座308室</td>
                                    <td>查看</td>
                                    <td>待处理</td>
                                    <td></td>
                                    <td>查看</td> 
                                    <td><a href="#" class="icon-set"></a></td>
                                </tr>
                                <tr>
                                    <td>复兰大学</td>
                                    <td>2015-07-06</td>
                                    <td>复兰管理员</td>
                                    <td>教学楼A座308室</td>
                                    <td>查看</td>
                                    <td>待处理</td>
                                    <td>刘师傅</td>
                                    <td>查看</td> 
                                    <td><a href="#" class="icon-set"></a></td>
                                </tr>
                                 -->
                            </tbody>
                        </table>
                        <div class="fenye"><div class="new-page-links" id="pageForEdu"></div></div>
						
                    </div>
                    <!--/.报修处理-->

                </div>
            </div>
            <!--/.tab-col-new-->

        </div>
        <!--/.col-right-->

    </div>
    <!--/#content-->

    <!--#foot-->
    <%@ include file="../common_new/foot.jsp" %>
    <!--#foot-->
    <div class="pop-wrap" id="jgcontentId">
        <div class="pop-title">内容</div>
        <dl class="pop-content">
            <dt>维修结果：</dt>
            <dd><p id="repairResultForView" class="huanhang"></p></dd>
        </dl>
        <div class="pop-btn"><span class="active">关闭</span></div>
    </div>
 	<div class="pop-wrap" id="bxresultId">
        <div class="pop-title">登记维修情况</div>
        <dl class="pop-content">
            <!-- <dt>维修结果：</dt> -->
            <dd><textarea id="repairResult" ></textarea></dd>
        </dl>
        <div class="pop-btn"><span class="active" id="repairResultSubmitBtn">提交</span><span id="closeResultBtn">取消</span></div>
    </div>
	<div class="pop-wrap" id="bxcontentId">
        <div class="pop-title">内容</div>
        <dl class="pop-content">
            <dt>报修描述：</dt>
            <dd><p id="repairContentForView" class="huanhang"></p></dd>
            <dt>上报原因：</dt>
            <dd><p id="repairContentForReason" class="huanhang"></p></dd>
        </dl>
        <div class="pop-btn"><span class="active">关闭</span></div>
    </div>
    <div class="pop-wrap" id="opstatusId">
     
        <div class="pop-title">分派维修人员</div>
         
        <div class="pop-content">
            <label>
                	受理人：<input type="txt" id="eduResolvePerson">
            </label>
        </div>
        <div class="pop-btn"  style="width: 206px;margin-left: 87px;"><span class="active" id="eduAssign">确定</span><span id="closeRepairBtn" >关闭</span></div>
    </div>
    <div class="bg-dialog"></div>

    <!-- Javascript Files -->
    <!-- initialize seajs Library -->
    <script src="<%=basePath%>static_new/js/sea.js"></script>
    <!-- Custom js -->
    <script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
    <script>
        seajs.use('edubxgl');
    </script>
</body>
</html>
