<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>

<title>微课资源</title>
<link rel="stylesheet"
	href="/static/plugins/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" href="/static/css/teacherSheet.css" />
<link rel="stylesheet" href="/static/css/flippedclassroom.css" />
<link rel="stylesheet" href="/static/css/dialog.css" />
<link href="/static/css/font-awesome.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css"
	href="/static/css/class-course.css" />
<link rel="stylesheet" type="text/css"
	href="/static/css/cloudclass.css?ver=1118" />
<link rel="stylesheet" type="text/css"
	href="/static/css/zTreeStyle/zTreeStyle.css" />
<link rel="stylesheet" type="text/css" href="/static/css/tree.css" />
<link rel="stylesheet" type="text/css"
	href="/static/css/artDialog/ui-dialog.css" />
<script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
<script src="/static/js/ztree/jquery.ztree.all-3.5.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"
	type="text/javascript"></script>
<script src='http://static.polyv.net/file/polyvplayer_v2.0.min.js'></script>
<script type="text/javascript" src="/static/js/swfobject.js"></script>
<script type="text/javascript" src="/static/js/sharedpart.js"></script>
<script type="text/javascript" src="/static/js/cloudclass.js?ver=1118"></script>
<script type="text/javascript" src="/static/js/lessons/coursesManage.js"></script>
<script type="text/javascript" src="/static/js/experienceScore.js"></script>
<script type="text/javascript" src="/static/plugins/bowser.min.js"></script>
<script src="/static/js/artDialog/dialog-plus-min.js"></script>
<script type="text/javascript" src="static/js/ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript">
	var currentPageID = 1;
	var isStudent = false;
	var vedioSize = function() {
		var newWidth = $(window).width() * 0.8;
		var newHeight = $(window).height() * 0.8;
		//800 x 450
		if (!$("#sewise-div").is(':hidden')) {

			if (newWidth < 800 || newHeight < 450) {
				if (newWidth > newHeight / 0.56) {// newHeight
					$("#sewise-div").css("height", newHeight);
					$("#sewise-div").css("width", newHeight / 0.56);
					$('#YCourse_player').css('margin-left', -newHeight * 0.5);
					$('#YCourse_player').css('margin-top', -newHeight * 0.5);
				} else {//newWidth
					$("#sewise-div").css("width", newWidth);
					$("#sewise-div").css("height", newWidth * 0.56);
					$('#YCourse_player').css('margin-left', -newWidth * 0.5);
					$('#YCourse_player').css('margin-top',
							-newWidth * 0.5 * 0.56);
				}
			} else {
				$("#sewise-div").css("width", 800);
				$("#sewise-div").css("height", 450);
				$('#YCourse_player').css('margin-left', -400);
				$('#YCourse_player').css('margin-top', -225);
			}
		} else if (!$("#player_div").is(':hidden')) {
			if (newWidth < 800 || newHeight < 600) {
				if (newWidth > newHeight / 0.625) {//newHeight
					$("#player_div").css("height", newHeight * 0.8);
					$("#player_div").css("width", newHeight * 0.8 / 0.625);
					$('#YCourse_player').css('margin-left', -newHeight * 0.5);
					$('#YCourse_player').css('margin-top', -newHeight * 0.5);
				} else {
					$("#player_div").css("width", newWidth);
					$("#player_div").css("height", newWidth * 0.625);
					$('#YCourse_player').css('margin-left', -newWidth * 0.5);
					$('#YCourse_player').css('margin-top',
							-newWidth * 0.5 * 0.625);
				}
			} else {
				$("#player_div").css("width", 800);
				$("#player_div").css("height", 600);
				$('#YCourse_player').css('margin-left', -400).css('margin-top',
						-300);
			}
		}

	}
	$(window).resize(vedioSize);
	$(window).load(vedioSize);


</script>
</head>
<body style="background: #fff;">
	<!-- 页头 -->
	<%@ include file="../common_new/head.jsp"%>
	<div id="YCourse_player" class="player-container">
		<div id="player_div" class="player-div"></div>
		<div id="sewise-div"
			style="display: none; width: 800px; height: 450px; max-width: 800px;">
			<script type="text/javascript"
				src="/static/plugins/sewiseplayer/sewise.player.min.js"></script>
		</div>
		<span onclick="closeCloudView()" class="player-close-btn"></span>
	</div>

	<script type="text/javascript">
		SewisePlayer.setup({
			server : "vod",
			type : "m3u8",
			skin : "vodFlowPlayer",
			logo : "none",
			lang : "zh_CN",
			topbardisplay : 'disabled',
			videourl : ""
		});
	</script>
	<div id="content_main_container">
		<div id="content_main" style="overflow: hidden">
			<!-- 左侧导航-->
			<%@ include file="../common_new/col-left.jsp" %>
			<!-- left end -->

			<!-- right start-->
			<div style="overflow: visible;width: 780px;float: left;margin-left: 10px;">
				<c:choose>
					<c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">

						<jsp:include page="../common/right_2.jsp"></jsp:include>
						<jsp:include page="../common/infoBanner.jsp">
							<jsp:param name="bannerType" value="coursePage" />
							<jsp:param name="menuIndex" value="0" />
							<jsp:param name="template" value="student" />
						</jsp:include>

						<script type="text/javascript">
							var isStudent = true;
						</script>
					</c:when>
					<c:otherwise>
						<jsp:include page="../common/teacherInfoHead.jsp">
							<jsp:param name="dir" value="cloud" />
						</jsp:include>
						<c:set var="dir" value="cloud" />
					</c:otherwise>
				</c:choose>

				<div class='center-container select-course'>
					<div class="retrieval">
						<div class="sty-container">
							<c:forEach items="${styStionList}" var="sty" varStatus="status">
								<span sty-id="${sty.type}" class="sty_span ${status.first? "sty_span_actv" : ""}">${sty.value}</span>
							</c:forEach>
							
							
							
							<c:choose>
							<c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
							   <span  sty-id="100" class="sty_span" onclick="location.href='/cloudres/load.do?version=61'">同步资源</span>
							</c:when>
							</c:choose>
							
							
						</div>
						
						
						
						<div class="retrieval-search-bar"
							style="padding: 10px; position: relative; left: 193px; top: -2px;">
							<input class="retrieval-search-form" type='text'
								placeholder="在该条件下查找"
								style="padding: 6px 20px 6px 10px; border-color: #3d87c5;">

                            <span class="retrieval-search-button"
                                  style="position: absolute; top: 17px; right: 15px;"></span>
						</div>

					</div>
					<div class="retrieval-subject">
						<div class='subject-title cloud_select_title'>科目</div>
						<span style="float: left; color: #bfd5dd;">|</span>
						<ul class="subject-ul">
						</ul>
					</div>
					<div class="retrieval-grade">
						<div class='grade-title cloud_select_title'>年级</div>
						<span style="float: left; color: #bfd5dd;">|</span>
						<ul class="grade-ul">
						</ul>
					</div>
					<div class="retrieval-knowledge">
						<div class='grade-title knowledge-title cloud_select_title'>类别</div>
						<span style="float: left; color: #bfd5dd;">|</span>
						<div class="knowledge-down more-knowledge">
							更多<i class="fa fa-angle-down"></i>
						</div>
						<ul id="knowledge-ul" class="knowledge-ul">
						</ul>
					</div>
				</div>
				<div id="y_courses_container">
					<div id="y_courses" class='center-container'
						style="min-height: 500px; padding-left: 35px;overflow: visible"></div>
					<div class='center-container'
						style="margin-top: 30px; margin-bottom: 30px; text-align: right; overflow: hidden;">
						<div id="example" scroll="no" style="width: 500px"></div>
					</div>
				</div>
				<div ng-controller="ModalCtrl" style="display: none">
					<script type="text/ng-template" id="pushModal.html">
                    <div class="modal-header">
                        <h3 class="modal-title"><i class="fa fa-cloud-upload" style="vertical-align: middle;margin-right:10px"></i>推送课程</h3>
                    </div>
                    <div class="modal-body" load-tree>

                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-primary" onclick="submitPushDir()">推送</button>
                        <button class="btn btn-warning" ng-click="cancel()">取消</button>
                    </div>
                </script>

					<div id="treeContainer">
						<ul id="teacherDirUl" class="ztree dir-tree"></ul>
					</div>
					<div id="class-dialog-content">
						<ul id="classTree" class="ztree dir-tree"></ul>
					</div>
					<div id="school-dialog-content">
						<ul id="schoolTree" class="ztree dir-tree"></ul>
					</div>
				</div>
			</div>
			<!-- right end -->
            <%--推送成课程课件--%>
            <div class="wind-push-lesson" >
                <div class="wind-top">
                    推送至备课空间成为课程课件
                    <span>×</span>
                </div>
                <div class="left-ztree">
                <ul id="teacherDirUl1" class="ztree dir-tree"></ul>
             
                
                
                
                
                </div>
                <div class="video-list">
                    <div class="list-top">
                        <span id="lesson_count">0</span>个课程
                    </div>
                    <div id="backlist" class="video-fa">
                    
                      
                       
                    </div>
                </div>
                <div class="btn-vo">
                    <button class="btn-ok" onclick="pushToMultiBackups()">确定</button>
                    <button class="btn-no">取消</button>
                </div>
            </div>


        </div>
		</div>
    <div class="bg"></div>
	</div>


	<!-- 页尾 -->
	<%@ include file="../common_new/foot.jsp"%>
</body>
</html>
