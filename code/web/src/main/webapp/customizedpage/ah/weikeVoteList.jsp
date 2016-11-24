<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
        <meta name="renderer" content="webkit">
        <title>复兰科技 K6KT-快乐课堂</title>
        <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css" />
        <link rel="stylesheet" type="text/css" href="/customizedpage/ah/css/style.css"/>
        <link rel="stylesheet" type="text/css" href="/customizedpage/ah/css/main.css"/>
        <link rel="stylesheet" href="/static/css/nivo-slider.css" type="text/css" />
        <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
       <script type="text/javascript" src="/customizedpage/ah/js/video.js"></script>
        <style>
        .login-bar {
			border-bottom: 3px solid #decaaa;
		}
        </style>
        <script>
            var type = ${param.type},
                    owner = ${param.owner};
        </script>
    </head>
    <body>
    <%@ include file="/WEB-INF/pages/common/ypxxhead.jsp"%>
    <div id="YCourse_player" class="player-container" style="position:fixed;top:50%;left:50%;z-index:999999999999999999;margin-top: -225px;margin-left: -400px;">
		<div id="player_div" class="player-div" style="background-color: white;box-shadow: 0 10px 25px #000;box-sizing: content-box;border-radius: 4px;padding: 15px;display: none"></div>
        <span onclick="closeCloudView()" class="player-close-btn" id="player-close-btn" style="display: none"></span>
		<div id="sewise-div"
			style="display: none; width: 800px; height: 450px; max-width: 800px;background-color: white;box-shadow: 0 10px 25px #000;box-sizing: content-box;border-radius: 4px;padding: 15px">
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
    <div class="main-container" style="background: #ffffff">
        <div class="login-bar">
            <div class='title-bar-container' style="height:105px;">
                <img class="title-logo" src="/customizedpage/ah/img/ah_k6kt.png">
                <a class="login-btn" href="javascript:;">登 录</a>
                <input class="input-password" type="password" placeholder="密码" tabindex="2">
                <input class="input-account" type="text" placeholder="邮箱/手机号/用户名" tabindex="1">
                <div id="tips-msg">
                    <a class="password-error">密码错误</a>
                    <a class="forget-pass" href='#'>忘记密码？</a>
                    <a class="username-error">用户名不存在</a>
                </div>
            </div>
        </div>
        <div class="content-container">
            <div style="padding-top: 18px;">
                <span id="content-title" style="font: 600 20px 'Microsoft YaHei';">2014年安徽省高中化学微课评选</span>
                <hr style="background: #5D9E20;height: 3px;">
            </div>
            <div id="weike-container">

            </div>
        </div>
        <!-- <div class='center-container' style="margin-top: 30px; margin-bottom: 30px; text-align:center;overflow:hidden;">
            <div id="example" scroll="no" style="width:500px"></div>
        </div> -->
    </div>
    <%@ include file="/WEB-INF/pages/common/flippedroot.jsp"%>
    </body>
    <script type="text/javascript">
    function playTheMovie(url) {
   	 $('#player_div').hide();
     $('#sewise-div').show();
     try {
         SewisePlayer.toPlay(url, "云课程", 0, true);
     } catch (e) {
         playerReady.URL = url;
         isFlash = true;
     }
    }
    function playerReady(name){
        if(isFlash){
            SewisePlayer.toPlay(playerReady.URL, "云课程", 0, true);
        }
    }
    </script>
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
</html>