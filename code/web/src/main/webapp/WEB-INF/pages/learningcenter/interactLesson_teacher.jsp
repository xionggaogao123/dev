<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
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
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="/static_new/css/interactlesson/teacher_hudong.css?v=2015082601" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
</head>
<body>


<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->


	<div id="YCourse_player" class="player-container">
		<div id="player_div" class="player-div"></div>
		<div id="sewise-div"
			style="display: none; width: 800px; height: 450px; max-width: 800px;">
			<script type="text/javascript"
				src="/static/plugins/sewiseplayer/sewise.player.min.js"></script>
            <span onclick="closeCloudView()" class="player-close-btn"></span>
		</div>

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
	
	
	
<!--#content-->
<div id="content" class="clearfix">

    <!--.col-left-->
   <%@ include file="../common_new/col-left.jsp" %>
    <!--/.col-left-->

    <!--.col-right-->
    <div class="col-right">
        <div class="tab-head clearfix">
            
            
            <script id="class_script" type="text/x-dot-template">
						{{~it:value:index}}
                               {{  if(index==0){ }}
                                   <li id="{{=value.idStr}}" class="cur currt"><a href="javascript:chanageClass('{{=value.idStr}}')">{{=value.value}}</a></li>
                               {{ }else{ }}
                                  <li id="{{=value.idStr}}"><a href="javascript:chanageClass('{{=value.idStr}}')">{{=value.value}}</a></li>
                               {{ } }}
						{{~}}
			</script>
					  
            <div class="uo">
                <ul id="class_ui">
                   <!--  
                    <li class="cur currt"><a href="#">三年级一班</a></li>
                    <li><a href="#">三年级二班</a></li>
                    -->
                </ul>
            </div>

             <script id="video_script" type="text/x-dot-template">
						{{~it:value:index}}
                               <div id="{{=value.id}}" class="floder-connent-info">
                                   <img src="{{=value.value1}}?imageView/1/h/110/w/160" width="160" height="110">
                                   <p>{{=value.value}}</p>
                                   <a class="btn btn-default btn-try" video-length="{{=value.size}}" onclick="tryPlayYCourse($(this));" vurl="{{=value.value2}}" vid="{{=value.id}}">试看</a>
                                   {{ if(value.value3==0) { }}
                                   <a id="push_{{=value.id}}" href="javascript:pushLesson('{{=value.id}}')">推送</a>
                                   {{ } }}
                                   <a href="javascript:removeLesson('{{=value.id}}')">删除</a>
                                   <span>{{=value.type}}</span>
                               </div>
						{{~}}
			</script>
            <!-- 内容 -->
            <div class="floder-connent-right" id="vodeo_div">
            </div>
            <!-- 内容 -->
        </div>
    </div>
    <!--/.col-right-->


</div>
<!--/#content-->



<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->

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


<script type="text/javascript" src="/static/js/sharedpart.js"></script>
<script type="text/javascript" src="/static/js/cloudclass.js"></script>
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041605"></script>
<script>
    seajs.use('interactlesson',function(interactlesson){
    	interactlesson.init();
    });
</script>

</body>
</html>