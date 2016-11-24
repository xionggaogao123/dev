<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
 <meta charset="utf-8" />
 <meta http-equiv = "X-UA-Compatible" content="IE=edge" />
<title>积分历史</title>
<link rel="stylesheet" href="/static/css/bootstrap-combined.school.css"/>
<link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
<link rel="stylesheet" type="text/css" href="/static/css/flippedStatistics.css" />
<link rel="stylesheet" type="text/css" href="/static_new/css/reset.css">
<script type="text/javascript" src="/static/js/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/static/js/pet/petstore.js"></script>
<style stype="text/css">
         .ypheader >div:first-child{padding:10px 5px;border:1px solid #D0D0D0;}
         
        .fc_statistics_list span.I{width:200px;padding-left:10px;}
		.fc_statistics_list span.II{width:400px;}
		.fc_statistics_list span.III{width:250px;}
		.pet-tab {
	    width: 900px;
	    margin: 0 auto;
	    overflow: visible;
    }
    .pet-tab-common {
	    width: 80px;
	    border-right: 1px solid #DDDDDD;
	    color: #363636;
	    background: #F1F1F1;
	    background-repeat: no-repeat;
	    height: 38px;
	    line-height: 38px;
	    font: 16px/38px 'microsoft yahei';
	    padding-left: 17px;
    }
    .active{
	    background: white;
		border-top: 4px solid #FF8900;
		margin-top: -4px;
		border-right: 1px solid #DDDDDD;
		color: #FF8900;
    }
    .divjy{
        border: 1px solid #D0D0D0;
        text-align: center;
        padding: 10px 0;
    }
    .jy-zhu{
        font-size: 14px;
        color: #000;
        padding: 3px 0;
        width: 744px;
        text-align: left;
        margin: 10px 0 0 73px;
    }
         .jy-z{
             font-size: 14px;
             color: #FC8C00;
             padding: 3px 0;
             width: 744px;
             text-align: left;
             margin: 10px 0 10px 73px;
         }0
    .jy-e{
        color: #000;
    }
		
</style>
</head>
<body style="font-family:Microsoft YaHei;">
    <%@ include file="../common_new/head.jsp" %>
    <jsp:include page="../common/infoBanner.jsp">
        <jsp:param name="bannerType" value="petPage"/>
        <jsp:param name="menuIndex" value="2"/>
    </jsp:include>
   	<div style="margin-top: 5px;"></div>
	<div class="pet-tab">
		<a href="/petbag" class="title_a"><span class="pet-tab-common">宠物背包</span></a>
		<a href="/petCenter" class="title_a"><span class="pet-tab-common">宠物中心</span></a>
		<a href="/studentScoreList" class="title_a"><span class="pet-tab-common">历史积分</span></a>
		<a href="/experience/experienceRule.do" class="title_a"><span class="pet-tab-common active" style="width: 130px;">经验值获取规则</span></a>
	</div>

	<div id="fc_student_score_history" class="fc_statistics_list ypheader center">
		<c:if test="${roles:isStudent(role)}">
			<div class="jy-stu divjy">
				<img src="../../../images/jy_stu.jpg" width="744px;">
				<div class="jy-zhu">注：请大家为营造积极向上的校园平台环境一起努力！恶意刷屏或发布不文明用语，将会被管理员根据情节“禁言”处罚一周或更长。</div>
			</div>
		</c:if>
		<c:if test="${roles:isParent(role)}">
			<div class="jy-stu divjy">
				<img src="../../../images/jy_pa.png" width="744px;">
			</div>
		</c:if>
		<c:if test="${roles:isNotStudentAndParent(role)}">
			<div class="jy-stu divjy">
				<img src="../../../images/jy_te.png" width="744px;">
			</div>
		</c:if>

	</div>
     	
	<%@ include file="../common_new/foot.jsp" %>
</body>
</html>