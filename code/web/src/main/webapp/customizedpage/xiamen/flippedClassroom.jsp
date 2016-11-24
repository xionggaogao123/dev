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
                    <img class="title-logo" src="/customizedpage/xiamen/fulann_logo.png"><span class="main-title">厦门市教研院</span>
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
	            			<a href="http://www.xmaes.cn/" target="_blank" style="color: #5D9E20;font: 600 14px 'Microsoft YaHei';margin-left: 135px;">更多...</a>
	            			<hr style="background: #5D9E20;height: 3px;"/>
	            		</div>
	            		<div class="new-list">
                            <a href="http://www.xmedu.gov.cn/publish/portal0/tab110/info39671.htm" target="_blank">
                                <dl>
                                    <dt class="date">2015-03-10</dt>
                                    <dd class="title">2015年全市教育工作会议召开</dd>
                                    <%--<dd class="author">发表人/单位： 伯海英</dd>--%>
                                </dl>
                            </a>
	            			<a href="http://202.101.105.166/jyxw/bsjy/201504/t20150415_1067357.htm " target="_blank">
	            			<dl>
	            				<dt class="date">2015-04-15</dt>
	            				<dd class="title">市教育局举办2015年度教育技术装备暨政府采购管理工作培训活动</dd>
	            				<%--<dd class="author">发表人/单位： 潘诗求</dd>--%>
	            			</dl>
	            			</a>
	            			<a href="http://202.101.105.166/jyxw/bsjy/201504/t20150415_1067103.htm " target="_blank">
	            			<dl>
	            				<dt class="date">2015-04-15</dt>
	            				<dd class="title">叶重耕带队调研大中小学生如何培育和践行社会主义核心价值观</dd>
	            				<%--<dd class="author">发表人/单位： 傅兴春</dd>--%>
	            			</dl>
	            			</a>
                            <a href="http://202.101.105.166/jyxw/bsjy/201504/t20150420_1076132.htm " target="_blank">
                                <dl>
                                    <dt class="date">2015-04-22</dt>
                                    <dd class="title">教育系统工会达标创优活动现场会在金尚中学举行</dd>
                                    <%--<dd class="author">发表人/单位： 蔡彬彬</dd>--%>
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
	            				<li><img src="/customizedpage/xiamen/binglangxiaoxue.jpg"/></li>
	            				<li><img src="/customizedpage/xiamen/diyizhongxue.jpg"/></li>
	            				<li style="margin-right:0px;"><img src="/customizedpage/xiamen/shuangshizhongxue.jpg"/></li>
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