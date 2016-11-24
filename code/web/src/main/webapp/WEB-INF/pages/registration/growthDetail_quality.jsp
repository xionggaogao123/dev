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
%> 
<!DOCTYPE html>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技-素质教育详情</title>
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
    <link href="<%=basePath%>static_new/css/growthDetail.css?v=2015041602" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
</head>
<script>
	var PUBLIC_STUDENT_ID = "${studentId}";
	var PUBLIC_GROWTH_ID = "${growthId}";
	var isMyself = ${isMyself};
	var isMaster = ${isMaster};
</script>
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

        <div class="grow-col-head clearfix">
            <h3>素质教育详情</h3>
            
             <%
             SessionValue sv =(SessionValue)request.getAttribute(BaseController.SESSION_VALUE);
            if(null!=sv && sv.getUserRole()!=UserRole.STUDENT.getRole())
            {
            	%>
            	<a style="background-color:#fff;color:#000;line-height:40px;" href="/growth/list.do?index=6">&lt; 返回</a>
            	<%
            }
            %>
             
        </div>


        <!-- 成绩单、素质教育 -->
        <div>
            <!-- 成绩单-->
            <div class="cjd hide" id="cjdId">
                <div class="grow-select">
                    <select id="reportTermSelection">
                        <c:forEach var="reportTerm" items="${reportTermList}" >
	  						<option value="${reportTerm}">${reportTerm}</option>
						</c:forEach>
                    </select>
                    <select id="reportExamSelection">
                    </select>
                </div>
                <h4 class="gray-table-h4">${studentName}成绩单</h4>
                <table class="gray-table" width="100%">

                    <thead>
                        <th width="25%">科目</th>
                        <th width="25%">成绩</th>
                        <th width="25%">科目</th>
                        <th width="25%">成绩</th>
                    </thead>
                    <tbody id="reportExamTable">
                        <tr>
                            <td class="bg-td">语文</td>
                            <td></td>
                            <td class="bg-td">数学</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td class="bg-td">英语</td>
                            <td></td>
                            <td class="bg-td">物理</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td class="bg-td">化学</td>
                            <td></td>
                            <td class="bg-td">历史</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td class="bg-td">地理</td>
                            <td></td>
                            <td class="bg-td">政治</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td class="bg-td">体育</td>
                            <td></td>
                            <td class="bg-td">生物</td>
                            <td></td>
                        </tr>
                    </tbody>        
                </table>
                <table class="gray-table" width="100%">

                    <thead>
                        <th width="25%">素质教育</th>
                        <th width="25%">等第</th>
                        <th width="25%">素质教育</th>
                        <th width="25%">等第</th>
                    </thead>
                    <tbody id="reportQualityTable">
                        <tr>
                            <td class="bg-td">综合素质</td>
                            <td>优</td>
                            <td class="bg-td">道德品质</td>
                            <td>优</td>
                        </tr>
                        <tr>
                            <td class="bg-td">公民素养</td>
                            <td>良</td>
                            <td class="bg-td">交流与合作</td>
                            <td>优</td>
                        </tr>
                        <tr>
                            <td class="bg-td">审美与表现</td>
                            <td>一般</td>
                            <td class="bg-td">学习能力</td>
                            <td>良</td>
                        </tr>
                        <tr>
                            <td class="bg-td">运动与健康</td>
                            <td>需努力</td>
                            <td colspan="2"></td>
                            
                        </tr>
                        
                    </tbody>        
                </table>
                
                <div class="report-txt">
                	<c:if test="${isMaster}">
                		<p class="clearfix">
                        <label>突出表现</label>
                        	<textarea id="goodPerformanceArea"  >${goodPerformance}</textarea>
                    	</p>
                    	<p class="clearfix">
                        	<label>班主任评语</label>
                        	<textarea id="masterCommentArea"  >${masterComment}</textarea>  
                    	</p>
                	</c:if>
                	
                	<c:if test="${!isMaster}">
                		<p class="clearfix">
                        	<label>突出表现</label>
                        	<textarea id="goodPerformanceArea"  readonly >${goodPerformance}</textarea> 
                    	</p>
                    	<p class="clearfix">
                        	<label>班主任评语</label>
                        	<textarea id="masterCommentArea" readonly >${masterComment}</textarea>
                    	</p>
                	</c:if>
                
                
                    
                </div>
                
            </div>
            <!-- 成绩单-->


            <!-- 素质教育-->
            <div class="szedu" id="szeduId">
                <div class="grow-select">
                    <select id="growthTermSelection">
                         <c:forEach var="growthTerm" items="${growthTermList}" >
	  						<option value="${growthTerm}">${growthTerm}</option>
						</c:forEach>
                    </select>
                </div>

                <h4 class="gray-table-h4">${studentName}的素质教育成绩单</h4>

                <table class="suzhiedu" width="100%">
                    <thead>
                        <th >评价指标</th>
                        <th >评定要素</th>
                        <th >主要表现要求</th>
                        <th >自我评价</th>
                        <th >教师评价</th>
                        <th >综合评定</th>
                    </thead>
                    <tbody id="growthQualityTable">
                        
                    </tbody>
                
                </table>
            </div>
            <!-- 素质教育-->
        </div>
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
<script src="<%=basePath%>static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('growthDetail');
</script>
</body>
</html>