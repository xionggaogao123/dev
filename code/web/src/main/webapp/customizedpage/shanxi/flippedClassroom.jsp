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
			font-size: 36px;
			font-weight: 700;
			font-family: 'Microsoft YaHei';
        }
        #unions ul li {
			float: left;
			margin-right: 7px;
		}
        </style>
    </head>
    <body>
    <%@ include file="/WEB-INF/pages/common/ypxxhead.jsp"%>
		<div id="intro-player">
			<div id="player_div"></div>
			<span onclick="closeMovie()" class="close-dialog"><i class="fa fa-times-circle fa-lg"></i></span></div>
        <div class="main-container">
            <div class="login-bar">
                <div class='title-bar-container' style="height:105px;">
                    <img class="title-logo" src="/customizedpage/xiamen/fulann_logo.png"><span class="main-title">陕西省教育厅</span>
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
            <div style="height: 30px;text-align: center;line-height: 30px;;width: 100%;background-color:#EAE9DC;position: relative;top:-20px;border-bottom: 1px solid #C0C0C0;font-family: 'Microsoft YaHei';font-weight: bold;color: #5a5a5a">
                <%--<p>欢迎北京大学入驻复兰大学K6KT-翻转课堂</p>--%>
            </div>
            <div class="shcool-container">
	            <div class="shcool-about">
	            	<div class="school-news">
	            		<div class="news-title">
	            			<span style="font: 600 20px 'Microsoft YaHei';">新闻动态</span>
	            			<a href="http://www.snedu.gov.cn/" target="_blank" style="color: #5D9E20;font: 600 14px 'Microsoft YaHei';margin-left: 135px;">更多...</a>
	            			<hr style="background: #5D9E20;height: 3px;"/>
	            		</div>
	            		<div class="new-list">
	            			<a href="http://www.snedu.gov.cn/jynews/jyyw/201410/19/44119.html" target="_blank">
	            			<dl>
	            				<dt class="date">2014-10-19</dt>
	            				<dd class="title">西安音乐学院庆祝建院65周年 徐沛东等出席</dd>
	            				<dd class="author">来源：陕西日报</dd>
	            			</dl>
	            			</a>
	            			<a href="http://www.snedu.gov.cn/jynews/jyyw/201410/17/44095.html" target="_blank">
	            			<dl>
	            				<dt class="date">2014-10-17</dt>
	            				<dd class="title">陕西省教育系统学习习近平教育实践活动大会重要讲话精神</dd>
	            				<dd class="author">来源：咸阳市教育局 渭南市教育局 西安工程大学 陕西省建筑材料工业学校 咸阳职业技术学院 </dd>
	            			</dl>
	            			</a>
	            			<a href="http://www.snedu.gov.cn/jynews/sxjy/201410/19/44141.html" target="_blank">
	            			<dl>
	            				<dt class="date">2014-10-19</dt>
	            				<dd class="title">岐山县加快教育均衡发展步伐全面完成职教资源优化整合</dd>
	            				<dd class="author">来源：宝鸡市教育局</dd>
	            			</dl>
	            			</a>
	            			<a href="http://www.snedu.gov.cn/jynews/sxjy/201410/19/44140.html" target="_blank">
	            			<dl>
	            				<dt class="date">2014-10-19</dt>
	            				<dd class="title">岐山县教体局出台六项措施全面提升中小学教育发展水平</dd>
	            				<dd class="author">来源：宝鸡市教育局</dd>
	            			</dl>
	            			</a>
	            		</div>
	            	</div>
	            	<div class="teachers-info">
	            		<div>
		            		<span style="font: 600 20px 'Microsoft YaHei';">翻转课堂联盟校展示</span>
		            		<hr style="background: #5D9E20;height: 3px;"/>
	            		</div>
	            		<div id="unions">
	            			<ul>
	            				<li><img style="width:202px;" src="/customizedpage/shanxi/gaoxindiyixiaoxue.jpg"/></li>
	            				<li><img src="/customizedpage/shanxi/dayantaxiaoxue.jpg"/></li>
	            				<li><img src="/customizedpage/shanxi/gongdafushuzhongxue.jpg"/></li>
	            				<li style="margin-right:0;"><img style="width:160px;" src="/customizedpage/shanxi/dibashijiuzhongxue.jpg"/></li>
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