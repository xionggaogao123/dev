<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.fulaan.base.controller.BaseController" %>
<%@ page import="com.pojo.user.UserRole" %>
<%@ taglib prefix="cc" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>  
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>

  
<%@page import="com.pojo.app.SessionValue"%>
<!DOCTYPE html>
<% 
  String path = request.getContextPath();    
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";    
    pageContext.setAttribute("basePath",basePath); 
    %>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技-区域联考</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <link rel="shortcut icon" href="" type="image/x-icon" />
    <link rel="icon" href="" type="image/x-icon" />
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="<%=basePath%>static_new/css/reset.css" rel="stylesheet" />
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="<%=basePath%>static_new/css/growRecord.css" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
  <!--   <link rel="stylesheet" type="text/css" href="../fileUpload/webuploader-0.1.5/webuploader.css"> -->
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
    <%--<c:choose>
        <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
        </c:when>
        <c:otherwise>
            <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
        </c:otherwise>
    </c:choose>--%>
    <!--/.banner-info-->

        <div class="grow-tab-head clearfix contactExam">
            <ul class="clearfix">
                <li class="cur"><a href="#cjdId">区域联考成绩录入</a></li>
            </ul>

        </div>    

    <div class="grow-col">

        <div>
            <!-- 成绩单-->
            <div class="cjd " id="cjdId"> 
                <div >             
                    <h3 class="examTitle">${titleName}</h3>
                </div>  
                <div class="toEduDepart clearfix">
                
                <cc:choose>
  					<cc:when test="${Flag==0}">
  					   <span>未提交到教育局</span>
   					</cc:when>
   					<cc:otherwise>
   					   <span>已提交到教育局</span>
   					</cc:otherwise>
				</cc:choose>

                    <ul>
                    	<a href="#subStatus" class="subTo" jointExamId="${examId}">提交到教育局</a>
                        <a href="#importExamScore" class="inputScores" examId="${id}">导入</a>
                    </ul>
                </div>              
                
                <table class="gray-table exam-table" width="100%">
                    <thead class="th">
                <!--    <th >姓名</th>
                        <th width="80">班级</th>
                        <th >总分</th>
                        <th >数学</th>
                        <th >语文</th>
                        <th >外语</th>
                        <th >物理</th>
                        <th >化学</th>
                        <th >生物</th>
                        <th >历史</th>
                        <th >地理</th>
                        <th >政治</th> 
          -->
                    </thead>
                  <tbody id="subScoreList">
                        <tr >
                            <!-- <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                             -->
                        </tr>
                    </tbody>        
                </table>

            </div>
            <div class="hide szedu" id="szeduId">

            </div>
           
        </div>
    </div>

</div>
<!--/.col-right-->
<!-- 分页div -->
<div class="new-page-links"></div>

</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- 查看状态上报状态 -->
<div class="pop-wrap" id="subStatus">
    <div class="pop-title">提交确认<span class="closePop"></span></div>
    <cc:choose>
  	<cc:when test="${Flag==0}">
  		   <div class="tip">确定要提交到教育局吗？</div>
   		</cc:when>
   		<cc:otherwise>
   	       <div class="tip">确定再次提交到教育局吗？</div>
   	       <div class="tip">之前的数据将被覆盖。</div>
   		</cc:otherwise>
	</cc:choose>
     
    <div class="pop-btn"><span class="active">确定</span><span class='close-dialog-class'>取消</span></div>
</div>

<div class="pop-wrap" id="importExamScore">
    <div class="pop-title"><span>区域联考成绩导入</span><span class="closePop"></span></div>
    <div class="pop-content">
        <form enctype="multipart/form-data" id="importExamItemForm" method="post">
            <ul>
                <li>
                    <button type="button" class="gray-big-btn" id="exportExamItemScoreModelBtn" >生成模版</button>
                </li>
                <li><i class="icon-down"></i></li>
                <li>
                    <input id="uploadExamItemScoreInput" name="examItemData" type="file" accept=".xls,.xlsx" class="file-input-class">
                </li>
                
            </ul>
        </form>
    </div>
    <div class="pop-btn"><span class="active" id="importExamItemScoreBtn">导入</span><span class='close-dialog-class'>取消</span></div>
    <!-- <div class="pop-btn"><span class="active" id="importExamItemScoreBtn" examid="55d175700cf22ec8fa67a71b" subjectid="55935198f6f28b7261c9bf80">导入</span><span id="cancelImportExamItenScore">取消</span></div> -->
</div>

<!-- 导出用表单 -->
	<form action="/regional/exportModel.do" method="post" id="exportExamItemScoreModelForm" style="display:none" >
		<input type="text" id="eId" name="areaExamId" value=""/>
<!--		<input type="text" id="sId" name="subjectId" value=""/>  -->
	</form> 

<div class="pop-wrap input-wait" id="waitWindow_score" >
    <div class="pop-content">
        <h3 class="infoWindowH">正在导入区域联考成绩,请等待...</h3>
    </div>
</div>

<div class="pop-wrap input-wait" id="waitWindow_submit_to_edu" >
    <div class="pop-content">
        <h3 class="infoWindowH">正在提交成绩到教育局,请等待...</h3>
    </div>
</div>

<div class="pop-wrap done-btn" id="overWindow_score" >
    <div class="pop-content">
        <h3 class="infoWindowH sec" id="overWindowH_score">区域联考分数导入成功！</h3>
        <h3 class="cen" id="overWindowH_scoreMsg"></h3>
    </div>
    <div class="pop-btn"><span class="active lef" id="overWindowBtn_score">确定</span></div>
</div>

<div class="bg-dialog"></div>


<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="<%=basePath%>static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js"></script>
<script>
    seajs.use('inputGrade');
</script>
</body>
</html>
