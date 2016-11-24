<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
        <meta name="renderer" content="webkit">
        <title>复兰科技 K6KT-快乐课堂</title>
        <link rel="stylesheet" type="text/css" href="/admin/static/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="/css/font-awesome.min.css" />
        <link rel="stylesheet" type="text/css" href="/business/ypxx/special/maanshan2/css/style.css"/>
        <link rel="stylesheet" type="text/css" href="/business/ypxx/special/maanshan2/css/main.css"/>
        <link rel="stylesheet" href="/css/nivo-slider.css" type="text/css" />
        <script type='text/javascript' src='/js/jquery.min.js'></script>
        <script type='text/javascript' src='/admin/static/js/bootstrap.min.js'></script>
        <script src="/js/jquery.nivo.slider.js"></script>
        <script src='/business/ypxx/special/maanshan2/js/indexjq.js'></script>
        <script src='http://static.polyv.net/file/polyvplayer_v2.0.min.js'></script>
        <script type='text/javascript' src='/js/k6kt-index.js'></script>
        
        <style>
        .login-bar {
			border-bottom: 3px solid #decaaa;
		}
        </style>

    </head>
    <body>
        <%@ include file="/business/slice/ypxxhead.jsp"%>
        <div id="intro-player">
            <div id="player_div" style="width:830px;height:480px;">
        	<script type="text/javascript" src="/plugins/sewiseplayer/sewise.player.min.js"></script>
            </div>
    	    <script type="text/javascript">
                SewisePlayer.setup({
                    server: "vod",
                    type: "m3u8",
                    skin: "vodFlowPlayer",
                    logo: "none",
                    lang: "zh_CN",
                    topbardisplay: 'disabled',
                    videourl: ''
                });
            </script>
            <span onclick="closeTheMovie()" class="close-dialog"><i class="fa fa-times-circle fa-lg"></i></span>
        </div>
        <div class="main-container">
            <div class="login-bar">
                <div class='title-bar-container' style="height:105px;">
                    <img class="title-logo" src="/business/ypxx/special/maanshan2/img/ah_k6kt.png">
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
            		<span id="content-title" style="font: 600 20px 'Microsoft YaHei';">



                       
            		</span>
            		<hr style="background: #5D9E20;height: 3px;">
           		</div>
           		<div id="weike-container">
           			
           		</div>
            </div>
            <!-- <div class='center-container' style="margin-top: 30px; margin-bottom: 30px; text-align:center;overflow:hidden;">
		        <div id="example" scroll="no" style="width:500px"></div>
		    </div> -->
        </div>
        <!-- 页尾 -->
        <%@ include file="/business/slice/flippedroot.jsp"%>
        <!-- 页尾 -->
    </body>
</html>