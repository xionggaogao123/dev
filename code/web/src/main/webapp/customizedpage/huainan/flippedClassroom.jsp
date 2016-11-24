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
        <link rel="stylesheet" type="text/css" href="/customizedpage/ah/css/style.css"/>
        <link rel="stylesheet" type="text/css" href="/customizedpage/huainan/css/main.css"/>
        <link rel="stylesheet" href="/static/css/nivo-slider.css" type="text/css" />
        <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
        <script type='text/javascript' src='/static/plugins/bootstrap/js/bootstrap.min.js'></script>
        <script type='text/javascript' src='/static/js/k6kt-index.js'></script>
        <script src='http://static.polyv.net/file/polyvplayer_v2.0.min.js'></script>
        <script src="/static/js/jquery.nivo.slider.js"></script>
        <style type="text/css">
        #vote-container dl {
			width: 100%;
			height: 100%;
			position: relative;
		}
		.wk-title-hn {
			color: #fff;
			font: 400 22px 'Microsoft YaHei';
			line-height: 32px;
			left: 30px;
			text-align: center;
			margin-top: -22px;
			text-indent: -2em;
			}
		.descript-hn {
			color: #868686;
			font: 600 14px 'Microsoft YaHei';
			margin-top: -35px;
			text-align: right;
			margin-right: 168px;
			}
        </style>
        <script>
			$(function() {
        	/*index 轮播图*/
        		var $key=0;
        		var timer=setInterval(autoplay, 2000);

        		function autoplay(){

					$key++;
        			if($key>2)
        			{
        				$key=0;
        				$("#index-banner").css("top",0);
        				$("#index-banner").animate({top:0},1000);
        			}

        			$("#index-banner").stop().animate({top:-$key*45},1000);

        		}

        		/* $(".right").click(function(event) {
        		     autoplay();//右侧和左侧按钮效果一样
        		});

        		$(".left").click(function(event) {//左侧按钮开始
        			$key--;
        			if($key<0)
        			{
        				$key=3; 
        				$("#index-banner").css("left","-5120px");
        			}
        		
        			$("#index-banner").stop().animate({left:-$key*1280},2000);
        		}); */
        		
        	    $("#unions").hover(function() {//  清除定时器 
        	      	clearInterval(timer);
        	      }, function() {
        	      	clearInterval(timer);  
        	      	timer=setInterval(autoplay, 2000);
        	    });

        	 /*  index-news部分  */
        	     $(".btnnum span").click(function(){
        				$(".newsleft-top-dl dl").eq($(this).index()).show().siblings().hide();
        						
        		});
        	});
        </script>
    </head>
    <body>
        <%@ include file="/WEB-INF/pages/common/ypxxhead.jsp"%>
		<div id="intro-player">
			<div id="player_div">

			</div>
			<span onclick="closeMovie()" class="close-dialog"><i class="fa fa-times-circle fa-lg"></i></span>
		</div>
        <div class="main-container">
            <div class="login-bar">
                <div class='title-bar-container' style="height:105px;">
                    <img class="title-logo" src="/customizedpage/huainan/img/logo_huainan3.png">
                    <a class="login-btn" href="javascript:;">登 录</a>
                    <input class="input-password" type="password" placeholder="密码" tabindex="2">
                    <input class="input-account" type="text" placeholder="邮箱/手机号/用户名" tabindex="1">
                    <div id="tips-msg">
                        <a class="password-error">密码错误</a>
                        <a class="forget-pass" href='#'></a>
                        <a class="username-error">用户名不存在</a>
                    </div>
                </div>
            </div>
            <div class="main-content-container">
                <div class='content-container'>
                    <img class="text-1" src="/img/K6KT/main-page/index_banner1.png" />
                   <%-- <img class="text-2" src="/img/K6KT/main-page/text-2.png" />--%>

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
                            <img src="/img/K6KT/main-page/screen-1.png"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="shcool-container">
	            <div class="shcool-about">
	            	<div class="school-news">
	            		<div class="news-title">
	            			<span style="font: 600 20px 'Microsoft YaHei';">新闻动态</span>
	            			<a href="http://www.hnjw.gov.cn/" target="_blank" style="color: #5D9E20;font: 600 14px 'Microsoft YaHei';margin-left: 130px;">更多...</a>
	            			<hr style="background: #5D9E20;height: 3px;"/>
	            		</div>
	            		<div class="new-list">
                           <%-- <a href="http://www.hnjw.gov.cn/bencandy.php?fid=8&id=3774" target="_blank">
                                <dl>
                                    <dt class="date">2015-4-15</dt>
                                    <dd class="title">关于公布2015年淮南市初中信息技术基本功竞赛暨优质课评选结果的通知</dd>
                                    <!-- <dd class="author">作者：姚卫国</dd> -->
                                </dl>
                            </a>
                            <a href="http://www.ahhnedu.cn/SiteManager/20140623104157/view.aspx?ContentID=10026457" target="_blank">
                                <dl>
                                    <dt class="date">2015-4-20</dt>
                                    <dd class="title">市教育局举办贯彻落实十八届四中全会精神推进依法治教报告会</dd>
                                    <!-- <dd class="author">作者：刘爱和</dd> -->
                                </dl>
                            </a>
	            			<a href="http://www.ahhnedu.cn/SiteManager/20140623104157/view.aspx?ContentID=10026666" target="_blank">
	            			<dl>
	            				<dt class="date">2015-4-22</dt>
	            				<dd class="title">以技能大赛促学校发展、促学生成才</dd>
	            				<!-- <dd class="author">作者：姚卫国</dd> -->
	            			</dl>
	            			</a>
	            			<a href="http://www.ahhnedu.cn/SiteManager/20140623104157/view.aspx?ContentID=10026688" target="_blank">
	            			<dl>
	            				<dt class="date">2015-4-22</dt>
	            				<dd class="title">全省信息学奥林匹克赛淮南市小学队荣获第一</dd>
	            				<!-- <dd class="author">作者：姚卫国</dd> -->
	            			</dl>
	            			</a>--%>
							   <a href="http://www.ahhnedu.cn/SiteManager/20140623104157/view.aspx?ContentID=10092085" target="_blank">
								   <dl>
									   <dt class="date">2016-09-19</dt>
									   <dd class="title">市教育局召开“两学一做”学习教育、文明城市创建工作推进会</dd>
									   <!-- <dd class="author">作者：姚卫国</dd> -->
								   </dl>
							   </a>
							   <a href="http://www.ahhnedu.cn/SiteManager/20140623104157/view.aspx?ContentID=10091369" target="_blank">
								   <dl>
									   <dt class="date">2016-09-13</dt>
									   <dd class="title">淮南市第四届中小学班主任基本功大赛圆满落幕</dd>
									   <!-- <dd class="author">作者：刘爱和</dd> -->
								   </dl>
							   </a>
							   <a href="http://www.ahhnedu.cn/SiteManager/20140623104157/view.aspx?ContentID=10091049" target="_blank">
								   <dl>
									   <dt class="date">2016-09-12</dt>
									   <dd class="title">国家教育督导组莅临我市督查秋季开学工作</dd>
									   <!-- <dd class="author">作者：姚卫国</dd> -->
								   </dl>
							   </a>
							   <a href="http://www.ahhnedu.cn/SiteManager/20140623104157/view.aspx?ContentID=10090826" target="_blank">
								   <dl>
									   <dt class="date">2016-09-09</dt>
									   <dd class="title">教育系统道德讲堂建设暨“友善和谐、感念师恩”道德讲堂观摩会举行</dd>
									   <!-- <dd class="author">作者：姚卫国</dd> -->
								   </dl>
							   </a>
                               <a href="http://www.ahhnedu.cn/SiteManager/20140623104157/view.aspx?ContentID=10090658" target="_blank">
                                   <dl>
                                       <dt class="date">2016-09-09</dt>
                                       <dd class="title">市领导教师节走访慰问教师</dd>
                                       <!-- <dd class="author">作者：姚卫国</dd> -->
                                   </dl>
                               </a>
                               <a href="http://www.ahhnedu.cn/SiteManager/20140623104157/view.aspx?ContentID=10089155" target="_blank">
                                   <dl>
                                       <dt class="date">2016-08-30</dt>
                                       <dd class="title">淮南市第二届“谁是球王”横空出世</dd>
                                       <!-- <dd class="author">作者：刘爱和</dd> -->
                                   </dl>
                               </a>
	            		</div>
	            		
	            		<%-- 备选方案
	            		<div class="news-title">
	            			<span style="font: 600 20px 'Microsoft YaHei';">新闻动态</span>
	            			<a href="http://www.ahhnedu.cn/" target="_blank" style="color: #5D9E20;font: 600 14px 'Microsoft YaHei';margin-left: 130px;">更多...</a>
	            			<hr style="background: #5D9E20;height: 3px;"/>
	            		</div>
	            		<div class="new-list">
	            			<a href="http://www.hnssz.com/Article/dongtai/2014-12-25/1800.html" target="_blank">
	            			<dl>
	            				<dt class="date">2014-12-25</dt>
	            				<dd class="title">淮南市第一届中小学微课大赛制作要求</dd>
	            				<dd class="author">淮南市第十三中学校园网</dd>
	            			</dl>
	            			</a>
	            			<a href="http://www.ahhnedu.cn/Item/38580.aspx " target="_blank">
	            			<dl>
	            				<dt class="date">2014-11-12</dt>
	            				<dd class="title">2014年全省初中化学生物学学科教师实验教学基本功竞赛在淮南成功举办</dd>
	            				<!-- <dd class="author">作者：刘爱和</dd> -->
	            			</dl>
	            			</a>
	            			<a href="http://www.ahhnedu.cn/Item/36061.aspx" target="_blank">
	            			<dl>
	            				<dt class="date">2014-5-13</dt>
	            				<dd class="title">关于举办2014年淮南市教育教学信息化大赛的通知</dd>
	            				<!-- <dd class="author">作者：姚卫国</dd> -->
	            			</dl>
	            			</a>
	            			
	            		</div> 备选方案--%>
	            	</div>
	            	<div class="teachers-info">
	            		<div>
		            		<span style="font: 600 20px 'Microsoft YaHei';">翻转课堂联盟校展示</span>
		            		<hr style="background: #5D9E20;height: 3px;"/>
	            		</div>
	            		<div id="unions">
	            			<ul id="index-banner">
	            				<li><img src="/customizedpage/huainan/img/huainan1.jpg"></li>
	            				<li><img src="/customizedpage/huainan/img/huainan2.jpg"></li>
	            				<li><img src="/customizedpage/huainan/img/huainan3.jpg"></li>
	            				<li><img src="/customizedpage/huainan/img/huainan4.jpg"></li>
	            				<li><img src="/customizedpage/huainan/img/huainan5.jpg"></li>
	            				<li><img src="/customizedpage/huainan/img/huainan6.jpg"></li>
	            				<li><img src="/customizedpage/huainan/img/huainan7.jpg"></li>
	            				<li><img src="/customizedpage/huainan/img/huainan8.jpg"></li>
	            				<li><img src="/customizedpage/huainan/img/huainan9.jpg"></li>
	            				<li><img src="/customizedpage/huainan/img/huainan10.jpg"></li>
	            				<li><img src="/customizedpage/huainan/img/huainan11.jpg"></li>
	            				<li><img src="/customizedpage/huainan/img/huainan12.jpg"></li>
	            				<li><img src="/customizedpage/huainan/img/huainan13.jpg"></li>
	            				<li><img src="/customizedpage/huainan/img/huainan14.jpg"></li>
	            				<li><img src="/customizedpage/huainan/img/huainan15.jpg"></li>
	            				<li><img src="/customizedpage/huainan/img/huainan16.jpg"></li>
	            				
	            			</ul>
	            		</div>
	            		<div>
		            		<span style="font: 600 20px 'Microsoft YaHei';">精品微课</span>
		            		<hr style="background: #5D9E20;height: 3px;"/>
	            		</div>
	            		<div id="vote-container">
	            			<a target="_blanck" href="/customizedpage/huainan/weikeVoteList.jsp">
	            			<dl style="background: #45b4e9;border: 2px solid #29ace9;">
	            				<dt><img src="/customizedpage/ah/img/vote_chuzhong.png"/></dt>
	            				<dd class="wk-title-hn">淮南市第一届 中小学微课大赛</dd>
	            				<dd class="descript-hn"><img src="/customizedpage/ah/img/arrow_in.png"/></dd>
	            			</dl>
	            			</a>
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
</html>