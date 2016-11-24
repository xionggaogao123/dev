<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
        <meta name="renderer" content="webkit">
        <title>泰州市姜堰区教育局数字化学习平台</title>
        <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css" />
        <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
        <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
        <link rel="stylesheet" href="/static/css/nivo-slider.css" type="text/css" />
        <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
        <script type='text/javascript' src='/static/plugins/bootstrap/js/bootstrap.min.js'></script>
        <script type='text/javascript' src='/static/js/k6kt-index.js'></script>
		<script src='http://static.polyv.net/file/polyvplayer_v2.0.min.js'></script>
        <script src="/static/js/jquery.nivo.slider.js"></script>
        <style type="text/css">
        .main-title{
        	height: 73px;
			line-height: 73px;
			padding: 15px 20px;
			font-size: 24px;
			font-weight: 700;
			font-family: 'Microsoft YaHei';
        }
        #unions {
		margin:0;
		}
        #unions ul li {
			margin-bottom: 7px;
		}
		#unions ul li img:hover{
			box-shadow: 0px 3px 5px;
		}
        </style>
        <script type="text/javascript">
        	/* $(function(){
        		var w,h;
        		$('#unions ul li img').mouseover(function(){
        			w = $(this).width();
        			h = $(this).height();
        			$(this).height(h*1.01);
        			$(this).width(h*1.01);
        			$(this).attr('style','z-index:100;');
        		});
        		$('#unions ul li img').mouseout(function(){
        			$(this).height(h);
        			$(this).width(w);
        			$(this).attr('style','z-index:0;');
        		});
        	}); */
        </script>
    </head>
    <body>
        <%@ include file="/WEB-INF/pages/common/ypxxhead.jsp"%>
		<div id="intro-player">
			<div id="player_div"></div>
			<span onclick="closeMovie()" class="close-dialog"><i class="fa fa-times-circle fa-lg"></i></span></div>
        <div class="main-container">
            <div class="login-bar">
                <div class='title-bar-container' style="height:105px;">
                    <img class="title-logo" src="/customizedpage/xiamen/fulann_logo.png"><span class="main-title">泰州市姜堰区教育局数字化学习平台</span>
                    <a class="login-btn" href="javascript:;">登 录</a>
                    <input style="width: 185px;" class="input-password" type="password" placeholder="密码" tabindex="2">
                    <input style="width: 185px;" class="input-account" type="text" placeholder="邮箱/手机号/用户名" tabindex="1">
                    <div id="tips-msg">
                        <a class="password-error">密码错误</a>
                        <a class="forget-pass" href='#'>忘记密码？</a>
                        <a class="username-error">用户名不存在</a>
                    </div>
                </div>
            </div>
            <div class="main-content-container">
                <div class='content-container'>
                    <img class="text-1" src="/img/K6KT/main-page/text-1.png" />
                    <img class="text-2" src="/img/K6KT/main-page/text-2.png" />

                    <a onclick="playMovie()">
                        <div class="player-hand">
                            <div></div>
                        </div>
                    </a>
                    <div class="teacher-apply" onclick="go2appuse()">
                        教师申请试用
                    </div>
                    <div class="app-link">
                        <img src="/img/K6KT/iphone.png" />
                        <div>
                            <div>手机客户端 APP</div>
                            <div>
                                <span><img src="/img/K6KT/ios.png" /></span>
                                <span>|</span>
                                <span><img src="/img/K6KT/android.png" /></span>
                                <span><a href="/mobile">点击下载 ></a></span>
                            </div>
                        </div>
                    </div>

                    <div class="monitor-div">
                        <div class="carousel nivoSlider" id='slider'>
                            <img src="/img/K6KT/main-page/screen-1.png"/>
                            <img src="/img/K6KT/main-page/screen-2.png"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="shcool-container">
	            <div class="shcool-about">
	            	<div class="school-news">
	            		<div class="news-title">
	            			<span style="font: 600 20px 'Microsoft YaHei';">新闻动态</span>
	            			<a href="http://www.jysedu.com/" target="_blank" style="color: #5D9E20;font: 600 14px 'Microsoft YaHei';margin-left: 135px;">更多...</a>
	            			<hr style="background: #5D9E20;height: 3px;"/>
	            		</div>
	            		<div class="new-list">
                            <a href="http://www.jysedu.com/html/s/c38/2015/04/15/142920007-69037.html" target="_blank">
                                <dl>
                                    <dt class="date">2015-04-15</dt>
                                    <dd class="title">姜堰区2015年春季公开招聘教师面试方案 </dd>
                                    <!-- <dd class="author">作者：姚卫国</dd> -->
                                </dl>
                            </a>
                            <a href="http://www.jysedu.com/html/s/c38/2015/04/17/143653617-69134.html " target="_blank">
                                <dl>
                                    <dt class="date">2015-04-17</dt>
                                    <dd class="title">区教育局启动新一轮机关服务品牌建设活动</dd>
                                    <!-- <dd class="author">作者：姚卫国</dd> -->
                                </dl>
                            </a>
	            			<a href="http://www.jysedu.com/html/s/c38/2015/04/20/103256289-69180.html" target="_blank">
	            			<dl>
	            				<dt class="date">2015-04-20</dt>
	            				<dd class="title">区教育局组织初中校长赴浙江考察学习</dd>
	            				<%--<dd class="author">作者：石宏明</dd>--%>
	            			</dl>
	            			</a>
	            			<a href="http://www.jysedu.com/html/s/c38/2015/04/20/094959499-69174.html" target="_blank">
	            			<dl>
	            				<dt class="date">2015-04-20</dt>
	            				<dd class="title">区政府督查、肯定我局信访工作</dd>
	            				<%--<dd class="author">作者：张益明</dd>--%>
	            			</dl>
	            			</a>
	            		</div>
	            	</div>
	            	<div class="teachers-info">
	            		<div>
		            		<span style="font: 600 20px 'Microsoft YaHei';">翻转课堂联盟校展示</span>
		            		<hr style="background: #5D9E20;height: 3px;"/>
	            		</div>
	            		<div id="unions" style="height: 258px;">
	            			<ul>
	            				<!-- <li><img style="width:120px;height:120px;" src="/business/customizedpage/special/jiangyan/baimi.jpg"/></li> -->
	            				<!-- <li><img style="width:300px;height:77px;" src="/business/customizedpage/special/jiangyan/luotang.jpg"/></li>
	            				<li><img style="width:229px;height:55px;margin-right:0px;" src="/business/customizedpage/special/jiangyan/qiaotou.png"/></li>
	            				<li><img style="width:229px;height:55px;margin-right:0px;" src="/business/customizedpage/special/jiangyan/qingtong.jpg"/></li>
	            				<li><img style="width:148px;height:37px;position: absolute;top: 740px;right: 860px;" src="/business/customizedpage/special/jiangyan/wangshi.jpg"/></li>
	            				<li><img style="width:138px;height:37px;position: absolute;top: 740px;right: 710px;" src="/business/customizedpage/special/jiangyan/lihua.jpg"/></li>
	            				<li><img style="width:300px;height:77px;position: absolute;right: 850px;" src="/business/customizedpage/special/jiangyan/nanyuan.jpg"/></li>
	            				<li><img style="width:358px;height:77px;margin-right: 0;position: absolute;right: 464px;" src="/business/customizedpage/special/jiangyan/dalun.jpg"/></li> -->
	            				<li><img src="/customizedpage/jiangyan/logo.jpg"/></li>
	            			</ul>
	            		</div>
	            		<div>
		            		<span style="font: 600 20px 'Microsoft YaHei';">精品微课</span>
		            		<hr style="background: #5D9E20;height: 3px;"/>
	            		</div>
	            		<div>
	            			<dl>
	            				<dt><img src="/customizedpage/ah/img/ah_wk1.jpg"/></dt>
	            				<dd class="wk-title">语文:病句修改金典例题</dd>
	            				<dd class="descript">包含1节课程</dd>
	            				<dd class="descript">陈应德  高考语文老师</dd>
	            			</dl>
	            			<dl>
	            				<dt><img src="/customizedpage/ah/img/ah_wk2.jpg"/></dt>
	            				<dd class="wk-title">英语:语法和语态</dd>
	            				<dd class="descript">包含6节课程</dd>
	            				<dd class="descript">董杏林  高考英语老师</dd>
	            			</dl>
	            			<dl>
	            				<dt><img src="/customizedpage/ah/img/ah_wk3.jpg"/></dt>
	            				<dd class="wk-title">物理:机械振动和机械波</dd>
	            				<dd class="descript">包含3节课程</dd>
	            				<dd class="descript">张元伟  高考物理老师</dd>
	            			</dl>
	            			<dl>
	            				<dt><img src="/customizedpage/ah/img/ah_wk4.jpg"/></dt>
	            				<dd class="wk-title">数学:三角函数</dd>
	            				<dd class="descript">包含2节课程</dd>
	            				<dd class="descript">胡春林  高考数学老师</dd>
	            			</dl>
	            		</div>
	            	</div>
	            </div>
	         </div>
        </div>
        <!-- 页尾 -->
        <%@ include file="/WEB-INF/pages/common/flippedroot.jsp"%>
        <!-- 页尾 -->
    </body>
    <script>
        function go2appuse(){
            window.location.href="/customizedpage/application.jsp";
        }
    </script>
    <script>
        $(function(){
            var h=$(window).height()
            var w=$(window).width();
            if(h<700){
                $("#II").css({height:276,width:401,marginLeft:-200,marginTop:-138})
            }
        })

    </script>
</html>