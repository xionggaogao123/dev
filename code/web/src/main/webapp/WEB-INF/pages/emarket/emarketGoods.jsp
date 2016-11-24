<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Tony
  Date: 2014/8/12
  Time: 18:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html style="background: none;">
<head>
    <title>电子超市</title>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/static/css/teacherSheet.css"/>
    <link rel="stylesheet" href="/static/css/flippedclassroom.css"/>
    <link rel="stylesheet" href="/static/css/dialog.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/class-course.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/cloudclass.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/zTreeStyle/zTreeStyle.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/tree.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/courseSale.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/excellentLesson.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/artDialog/ui-dialog.css"/>

    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script src="/static/js/ztree/jquery.ztree.all-3.5.min.js"></script>
    <script src="/static/js/bootstrap-paginator.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="/static/js/swfobject.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/js/excellentclass.js"></script>
    <script type="text/javascript" src="/static/js/excellentLesson.js"></script>
    <script type="text/javascript" src="/static/js/flippedcoursemaintain.js"></script>
    <script type="text/javascript" src="/static/js/lessons/coursesManage.js"></script>
    <script type="text/javascript" src="/static/js/experienceScore.js"></script>
    <script src="/static/js/artDialog/dialog-plus-min.js"></script>
    <script type="text/javascript" src="/static/js/excellentPay.js"></script>
<!--     <script type="text/javascript" src="/static/js/cloudclass.js"></script> -->
    <script type="text/javascript">
        var currentPageID = 11;
        var saleCourseDialog = null;
    </script>
    <style>
    #select-container{width:790px;float:left;border-left: 1px solid #d2d2d2;border-top: 1px solid #d2d2d2;border-right: 1px solid #d2d2d2;}
    #myaccount{float:left;margin-left: 20px;background: #f5f5f5;height: 153px;border-bottom: 1px solid #bfd5dd;}
    .select-course{border:0;}
    .title-bar{background-color: #FF7B00;line-height: 30px;padding: 0 12px;cursor:pointer;}
    #myaccount-manager:hover{text-shadow: 0 2px 3px #fff !important;}

    .account-btn{border-radius: 4px;color: rgb( 89, 89, 89 );border: 1px solid rgb(179,179,179);width: 80px;
      background-image: -moz-linear-gradient( 90deg, rgb(229,229,199) 0%, rgb(253,253,253) 100%);
      background-image: -webkit-linear-gradient( 90deg, rgb(229,229,229) 0%, rgb(253,253,253) 100%);
      background-image: -ms-linear-gradient( 90deg, rgb(229,229,229) 0%, rgb(253,253,253) 100%);
      cursor:pointer;
    }
    .currentItem,.account-btn:hover{
      color: #fff;
      background-image: -moz-linear-gradient( 90deg, rgb(236,122,29) 0%, rgb(249,160,47) 100%);
	  background-image: -webkit-linear-gradient( 90deg, rgb(236,122,29) 0%, rgb(249,160,47) 100%);
	  background-image: -ms-linear-gradient( 90deg, rgb(236,122,29) 0%, rgb(249,160,47) 100%)}
	  .file-toolbar a.btn {margin: 0 3px;}
    .edui-default .edui-editor-iframeholder{height: 110px !important;}
    </style>
</head>
<body style="background: #fff;">
<!-- 页头 -->
<%@ include file="../common_new/head.jsp" %>
<!-- 页头 -->
<%
	//TODO
//     int role = ((UserInfo) session.getAttribute("currentUser")).getRole();
	int role = 1;
%>
<%-- <jsp:include page="/infoBanner.do"> --%>
<%--     <jsp:param name="bannerType" value="excellent"/> --%>
<%--     <jsp:param name="menuIndex" value="2"/> --%>
<%-- </jsp:include> --%>
<div id="fullbg"></div>
<div id="YCourse_player" style="position:fixed;top:50%;left:50%;margin-left:-430px;margin-top:-302px;padding:15px 15px 0 0;"></div>

<div id="content_main_container" style="width:1000px;position: relative;background-color: white; overflow: hidden;margin:0 auto;">
    <%@ include file="../common_new/col-left.jsp" %>
	<div id="content_main" style="overflow: visible;width:780px !important;float: right;">
		<!-- 左侧导航-->

        <!--广告-->
        <c:choose>
            <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
                <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
            </c:when>
            <c:otherwise>
                <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
            </c:otherwise>
        </c:choose>
        <!--广告-->
		<!-- left end -->
		<!-- right start-->
		<div id="right-container" style="padding-left:15px;padding-top:10px;width: 770px !important;">
			<div class='center-container select-course' style="margin-top: 8px;">
				<div id="select-container" style="width:750px !important;">
					<div class="retrieval">
						<div>
							<select class="retrieval-search" id="retrieval-search">
								<c:forEach items="${styStionList}" var="sty">
									<option value="${sty.type}">${sty.value}</option>
								</c:forEach>
							</select>
						</div>
						<div class="retrieval-search-bar" style="margin-left:10px;">
							<input class="retrieval-search-form" type='text' placeholder="在该条件下查找">
						</div>
						<button class="retrieval-search-button" style="background: url(/img/search.png) no-repeat center;background-size: 11px;"></button>
						<div class="course-select-container">
						<c:choose>
							<c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
								<a href="javascript:void(0)" onclick="getMyExcellentLesson(2,this)"><button id="boughtExclt" class="account-btn" style="margin:0 10px;">已购课程</button></a>
								<a href="javascript:void(0)" onclick="getMyExcellentLesson(3,this)"><button id="freeExclt" class="account-btn">免费课程</button></a>
							</c:when>
							<c:otherwise>
								<a href="javascript:void(0)" onclick="getMyExcellentLesson(1,this)"><button id="myExclt" class="account-btn">我的课程</button></a>
								<a href="javascript:void(0)" onclick="getMyExcellentLesson(2,this)"><button id="boughtExclt" class="account-btn">已购课程</button></a>
								<a href="javascript:void(0)" onclick="getMyExcellentLesson(3,this)"><button id="freeExclt" class="account-btn">免费课程</button></a>
							</c:otherwise>
						</c:choose>
						</div>
						<c:choose>
							<c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
							</c:when>
							<c:otherwise>
								<div class="fast-excellent" onclick="fastPushExcellent()">快速上传至电子超市</div>
							</c:otherwise>
						</c:choose>

					</div>
					<div class="retrieval-subject">
						<div class='subject-title'>科目</div>
						<ul class="subject-ul exellent" id="subject-ul">
						</ul>
					</div>
					<div class="retrieval-grade">
						<div class='grade-title'>年级</div>
						<ul class="grade-ul exellent" id="grade-ul">
						</ul>
					</div>
					<!-- <div class="retrieval-knowledge" id="retrieval-knowledge">
                        <div class='grade-title knowledge-title'>知识点</div>
                        <div class="knowledge-down" id="knowledge-down">更多</div>
                        <ul id="knowledge-ul" class="knowledge-ul exellent" style="width: 557px;">
                        </ul>
                    </div> -->
				</div>
				<%--<div id="myaccount">
					<div class="title-bar"><a href="/excellentLesson/accountOrder"><span id="myaccount-manager" style="color: #fff;font-size: 14px;">个人账户管理</span><img src="/img/headicon.png" style="position: relative;right: -48px;top: 5px;" /></a></div>
					<div class="operations">
						<a href="/excellentLesson/order"><span>我的订单</span></a>
						<a href="/excellentLesson/balance"><span>充值</span></a>

						<%
							if (role == 1 || role == 2 || role == 9) {
						%>
						<a href="/excellentLesson/withDraw"><span>提现</span></a>
						<a href="/excellentLesson/saleList"><span style="border:0;">销售</span></a>
						<%}else{ %>
						<a href="/excellentLesson/withDraw"><span style="border:0;">提现</span></a>
						<%} %>
					</div>
                        <div class="course-select-container">
						<%
							if (role == 1 || role == 2 || role == 9) {
						%>
						<a href="javascript:void(0)" onclick="getMyExcellentLesson(1,this)"><button id="myExclt" class="account-btn">我的课程</button></a>
						<a href="javascript:void(0)" onclick="getMyExcellentLesson(2,this)"><button id="boughtExclt" class="account-btn">已购课程</button></a>
						<% }else{ %>
						<a href="javascript:void(0)" onclick="getMyExcellentLesson(2,this)"><button id="boughtExclt" class="account-btn" style="margin:0 50px;">已购课程</button></a>
						<%} %>
					</div>
				</div>--%>
			</div>
			<div id="y_courses_container" style="overflow: visible">
				<div id="y_courses" class='center-container' style="min-height:500px;">
				</div>
				<div class='center-container' style="margin-top: 30px; margin-bottom: 30px; text-align:center;overflow:hidden;">
					<div id="example" scroll="no" style="width:500px"></div>
				</div>
			</div>
		</div>
	</div>
</div>


<div class="inside-dialog"
     style="position:fixed;width:300px;margin-left:-150px;overflow: auto; top: 5%; max-height: 90%">
    <div class="dialog-title">
        <i class="fa fa-cloud-upload fa-2x" style="vertical-align: middle;margin-right:10px"></i>推送课程
        <i class="fa fa-times-circle fa-lg" style="margin-top:5px;float: right;cursor: pointer"
           onclick="closeDialog('.inside-dialog')"></i>
    </div>
    <div style="margin: 20px 20px 0;">
        <dl>
            <dt>备课空间：</dt>
            <dd>
                <ul id="teacherDirUl" class="ztree dir-tree"></ul>
            </dd>
            <dd style="margin-top: 20px">
                <input type="button" onclick="submitPushDir()" class="submit-button" value="推送"
                       style="background: #FF7E00"/>
            </dd>
        </dl>

    </div>
</div>
<!-- 页尾 -->
<%@ include file="../common_new/foot.jsp" %>
<!-- 页尾 -->
<!-- 推送到精品课程 START -->
<div id="select-course" style="display:none;">
<div id="courseName" style="margin-bottom: 18px;overflow:hidden;display:none;">
	<span class="courselabel">课程名称</span>
	<input id="course-name" class="radis-input" type="text" style="width:300px;text-align: left;">
	<span id="course-name-require" class="require-msg">*  输入课程名称</span>
</div>
<div>
	<span id="stage-require" style="float: right;margin-right: 20px;" class="require-msg">*  选择"中小学"</span>
	<span id="grade-require" style="float: right;margin-right: 20px;" class="require-msg">*  选择"年级"</span>
</div>
<div class="sub-container-left">推送到分类</div>
<div class="course-type">
	<div class="retrieval-stage" id="retrieval-stage" style="margin-left: 0;">
	<div class="stage-title">中小学</div>
	<ul class="stage-ul" id="stages-ul">
	<c:forEach items="${styStionList}" var="sty">
		<c:choose>
			<c:when test="${sty.type == '1'}">
			<li data-stageId="${sty.type}"><input type="checkbox" value="${sty.type}" checked="checked"/>${sty.value}</li>
			</c:when>
			<c:otherwise>
			<li data-stageId="${sty.type}"><input type="checkbox" value="${sty.type}"/>${sty.value}</li>
			</c:otherwise>
		</c:choose>
	</c:forEach>
	</ul>
	</div>
	<div class="retrieval-grade" id="retrieval-grade" style="margin-left: 0;">
	<div class="grade-title">年级</div>
	<ul class="grade-ul" id="grades-ul">
	</ul>
	</div>
	<div class="retrieval-subject" id="retrieval-subject" style="margin-left: 0;">
	<div class="subject-title">科目</div>
	<ul class="subject-ul" id="subjects-ul">
	</ul>
	</div>
<!-- 	<div class="retrieval-knowledge" id="retrieval-knowledges">
	<div class="grade-title knowledge-title">知识点</div>
	<div class="knowledge-down" id="knowledges-down">更多</div>
	<ul id="knowledges-ul" class="knowledge-ul" style="width:607px;">
	</ul>
	</div> -->
</div>
<div class="middle-container">
	<span class="courselabel">课程价格</span>
	<!-- <input id="course-price" class="radis-input" type="text" value="" placeholder="0.00" min="0.00" step="1.00" style="width:100px;"/>元 -->
	<select id="course-price" class="radis-input" onchange="calculatePrice()">
		<option value="0">0</option>
		<option value="1">1</option>
		<option value="2">2</option>
		<option value="3">3</option>
		<option value="4">4</option>
		<option value="5">5</option>
	</select>元
	<span id="price-require" style="margin-left: 6px;" class="require-msg">*  不要忘了定价哦</span>
	<span  style="margin-left:15px;color:red;">[课程有效期为6个月,实际收入<span id="actual-price"></span>元  (价格*0.7)]</span>
	<!-- <span style="margin-left:70px;margin-right: 3px;">课程有效期</span><input id="course-validity" class="radis-input" type="text" placeholder="0" min="0"/>月
	<span style="margin-left:15px;color:red;">(不填写或者填写0,代表不限制时间)</span> -->
</div>
<div class="sub-container-left">课程介绍</div>
<div style="height: 173px;">
	<script id="container" name="content" type="text/plain">
       
    </script>
    <!-- 配置文件 -->
    <script type="text/javascript" src="/static/plugins/ueditor/ueditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <script type="text/javascript" src="/static/plugins/ueditor/ueditor.all.js"></script>
    <!-- 实例化编辑器 -->
    <script type="text/javascript">
        var ue = UE.getEditor('container');
        
    </script>
</div>
<div class="middle-container">
<div class="sub-container-left">教师自我介绍</div><div style="float:left;"><textarea id="teach-descript" cols="80" rows="3" placeholder="老师,请介绍一下自己的光辉事迹哦~~~比如:职称、个人荣誉及其他成功经历..."></textarea></div><div><a href="javascript:saveContainer()"><span class="link-btn underline">保存</span></a></div>
</div>
<div class="middle-container" style="margin-bottom:5px;">
	<div style="margin-left:40px;">
		<span>个人信息</span><span style="padding: 1px 8px;" id="isOpen"><input type="radio" name="openinfo" value="1" checked="checked">公开<input type="radio" name="openinfo" value="0" style="margin-left: 18px;">不公开</span>
        <span id="teacherinfo" style="padding: 1px 8px;font-size: 12px;color: #888;"></span>
    </div>
</div>
<%--<div class="middle-container" style="margin-bottom:5px;">
    <span class="allowspan">是否允许下载者购买视频：<input type="radio" name="allow-download">接受<input type="radio" name="allow-download">拒绝</span>
</div>
<div class="middle-container" style="margin-bottom:5px;">
    <span class="allowspan">是否允许购买者编辑并免费推送给其任教班级的学生：<input type="radio" name="allow-push">接受<input type="radio" name="allow-push">拒绝</span>
</div>--%>
    <%--电子超市注释voyage_wu--%>

<div>
    <div style="margin-left:90px;">
        <span class="protocol"><input id="agree" type="checkbox" style="vertical-align: middle;" onclick="switchAgree()"><span style="vertical-align: middle;margin: 0 4px;">我已阅读<a href="/protocol/k6kt" target="_blank" style="font-weight: 600;color: #ff7b00;">《电子超市使用协议》</a></span></span>
    </div>
</div>
</div>
<!-- 推送到精品课程 START -->
</body>
</html>
