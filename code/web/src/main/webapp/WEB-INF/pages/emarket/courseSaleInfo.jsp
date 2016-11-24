<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html>
<html>
<head>
<title>电子超市</title>
<meta charset="utf-8"/>
<link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
<link rel="stylesheet" href="/static/css/bootstrap-combined.school.css"/>
<link rel="stylesheet" href="/static/css/courseSale.css"/>
<script type="text/javascript" src="/static/js/jquery.min.js"></script>
<script src="/static/js/bootstrap-paginator.min.js" type="text/javascript"></script>
<script type="text/javascript">
var currentPageID = 11;

var CurrentUId = '${currentUser.id}';
var role = '${currentUser.role}';
$(function() {
	$('.buybtn').on('click', function() {
		$.ajax({
                url: '/emarket/createLessonOrder.do',
                type: 'post',
                dataType: 'json',
                data: {
                    lessonId: '${buyLessonInfo.id}'
                },
            }).success(function(data) {
                if (data.resultCode==0) {
                  window.location.href='/emarket/selLessonOrder.do?orderId='+data.orderId;
               } else {
                alert("订单生成失败！请重新创建！");
               }
                
            }).error(function() {
                alert('服务器错误！');
            });
    });
	$('.btnfor').click(function(){
			var idVal = $(this).prop('id');
			$(this).siblings().removeClass('selected');
			$(this).addClass('selected');
			$('.instruct-container').hide();
			$('#'+ idVal +'-detail').show();
	});
	
});

function recmdbuy(id) {
	window.location.href='/emarket/buyLessonDetail.do?goodId='+id;	
}
</script>
<c:choose>
    <c:when test="${buyLessonInfo.imageUrl != null && buyLessonInfo.imageUrl != ''}">
         <c:set var="imgUrl" value="${buyLessonInfo.imageUrl}"></c:set>
        <%--<c:set var="imgUrl" value="/img/default_cover.jpg"></c:set>--%>
    </c:when>
    <c:otherwise>
        <c:set var="imgUrl" value="/img/default_cover.jpg"></c:set>
    </c:otherwise>
</c:choose>
</head>

<body csid="<% if(request.getParameter("courseId")!=null){out.println(java.net.URLDecoder.decode(request.getParameter("courseId"),"UTF-8")); }%>">

<%@ include file="../common_new/head.jsp" %>
<div id="content_main_container">
    <div id="content_main">
     
        <%@ include file="../common_new/col-left.jsp" %>
        
        <div id="right-container">
<%-- <s:hidden id="className" name="className"></s:hidden> --%>
<%-- <s:hidden id="classId" name="classId"></s:hidden> --%>
<%-- <s:hidden id="teacherName" name="teacherName"></s:hidden> --%>
<%-- <s:hidden id="teacherId" name="teacherId"></s:hidden> --%>
<%-- <s:hidden id="courseName" name="courseName"></s:hidden> --%>
<div class="main-wrap" style="position:relative;left:10px;overflow:hidden;">
	<div class="main-container">
		<div class="course-title ellipsis" title="${buyLessonInfo.courseName}">${buyLessonInfo.courseName}</div>
		<div class="course-info">
			<div class="course-cover"><img src="${imgUrl}" style="height: 270px;"/></div>
			<div class="main-info">
				<div class="course-price">
					<span class="pricelabel">价格</span>
					<span class="pricelabel">￥${buyLessonInfo.price}</span>
					<span class="deal-success"><span style="font-size: 18px;text-align: center;">${buyLessonInfo.count}</span><span>交易成功</span></span>
					<button class="buybtn">我要购买</button></div>
				<div class="course-content">
					<div class="content include"><span class="course-desc">${buyLessonInfo.videoCount}个视频 ${buyLessonInfo.docCount}个文档 ${buyLessonInfo.questionCount}个习题</span></div>
					<div class="content validate"><span class="course-desc" style="margin-top: 124px;">
						<c:if test="${buyLessonInfo.expireTime==0}">
						长久
						</c:if>
						<c:if test="${buyLessonInfo.expireTime!=0}">
						${buyLessonInfo.expireTime}个月
						</c:if>
					</span></div>
					<div class="content course-teach">
						<c:if test="${buyLessonInfo.maxImageUrl != null && buyLessonInfo.maxImageUrl != ''}">
						<img style="margin-top: 16px;width:54px;height:54px;border-radius: 5px;border: 3px solid #fd6152;" src="${buyLessonInfo.maxImageUrl}"/>
						</c:if>
						<c:choose>
							<c:when test="${buyLessonInfo.isopen == '1'}">
								<span class="course-desc ellipsis" style="margin-top:35px;" title="${buyLessonInfo.schoolName}">${buyLessonInfo.schoolName}</span>
								<span class="ellipsis" title="${buyLessonInfo.teacherName}" style="width:98px;line-height: 1;margin-top: -1px;margin-left: 18px;display: block;">${buyLessonInfo.teacherName}</span>
							</c:when>
							<c:otherwise>
								<span class="ellipsis" title="" style="width:98px;line-height: 1;margin-top: 125px;margin-left: 22px;display: block;">匿名</span>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</div>
		<div class="detail-info">
			<div class="title-bar">
				<button id="course" class="title-btn btnfor selected">课程介绍</button>
				<!-- <button id="teacher" class="title-btn btnfor">教师介绍</button> -->
				<button class="title-btn" style="width: 492px;border-top: 0;border-right: 0;cursor: inherit;border-left: 0;border-radius: 0;"></button>
			</div>
			<div class="instruct-container" id="course-detail">
				<div class="title"><span>课程详细</span></div>
				<div class="courseinfo wordbreak">${buyLessonInfo.courseContent}</div>
				
				<div class="title"><span>老师介绍</span></div>
				<div class="courseinfo wordbreak">${buyLessonInfo.teacherIntroduce}</div>
			</div>
			<%-- <div class="instruct-container" id="teacher-detail">
				<div class="title"><span>教师介绍</span></div>
				<div class="courseinfo wordbreak">${buyLessonInfo.profileBrief}</div>
			</div> --%>
		</div>
		<div class="recommend-container">
			<div class="course-price recommend-title">热门课程</div>
			<div class="recommend-list">
				<c:if test="${buyLessonInfo.hotList!=null}">
				 <c:forEach items="${buyLessonInfo.hotList}" var="lessonlist">
					<div class="recommend-item">
						<div><img src="<c:choose><c:when test="${lessonlist.image != null && lessonlist.image != ''}">${lessonlist.image}</c:when><c:otherwise>/img/default_cover.jpg</c:otherwise></c:choose>" class="recommend-cover"></div>
						<div style="margin-left: 8px;"><div class="ellipsis" style="width:120px;">${lessonlist.name}</div><div class="recmd-price">￥${lessonlist.price}</div><div><button class="recmd-buy" onclick="recmdbuy('${lessonlist.id}')">购买</button></div></div>
					</div>
				 </c:forEach>
				</c:if>
			</div>
		</div>
	</div>
</div>

        </div>
			
     </div>
<div>

</div>
    <%@ include file="../common_new/foot.jsp" %>
    </div>

</body>
</html>