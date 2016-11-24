
<%@page import="com.fulaan.cache.CacheHandler"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.bson.types.ObjectId"%>
<%@page import="com.pojo.app.SessionValue"%>
<%@ page import="java.net.URLDecoder" %>

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
        <link rel="stylesheet" type="text/css" href="/customizedpage/ah/css/main.css"/>
        <link rel="stylesheet" href="/static/css/nivo-slider.css" type="text/css" />
        <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
        <script type='text/javascript' src='/static/plugins/bootstrap/js/bootstrap.min.js'></script>
        <script type='text/javascript' src='/customizedpage/ah/js/ah_k6kt_index.js'></script>
        <script type='text/javascript' src='/customizedpage/ah/js/video.js'></script>
        <script src='http://static.polyv.net/file/polyvplayer_v2.0.min.js'></script>
        <script src="/static/js/jquery.nivo.slider.js"></script>
        
        
         <%
        
        String ui="";
        String userName = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("momcallme".equals(cookie.getName())) {
                    userName = URLDecoder.decode(cookie.getValue(), "UTF-8");
                }
                if ("ui".equals(cookie.getName())){
                	ui=cookie.getValue();
                }
            }
        }
        
        if(StringUtils.isNotBlank(ui))
        {
        	SessionValue sv=CacheHandler.getSessionValue(ui);
        	if(null!=sv)
        	{
        		String uid=sv.getId();
        		if(null!=uid && ObjectId.isValid(uid) && sv.getK6kt()==1)
        		{
        		  response.sendRedirect("/user/homepage.do");
        		}
                if(null!=uid &&  ObjectId.isValid(uid) && sv.getK6kt()==0)
                {
                    response.sendRedirect("/mall");
                }
        	}
        }
    %>
    
    
        <script>
        	$(function() {
        	/*index 轮播图*/
        		var $key=0;  
        		var timer=setInterval(autoplay, 2000);
        		
        		function autoplay(){
        			$key++;
        			if($key>17)
        			{
        				$key=1;  
        				$("#index-banner").css("top",0);
        			}
        			
        			$("#index-banner").stop().animate({top:-$key*45},1000);
        		};
        		
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

    <div id="content_main_container">
    <div id="play_I"
         style="width: 745px;height: 425px;position: fixed;top: 50%;left: 50%;margin-left: -365px;margin-top:-207px;z-index: 999;display: none;background-color: rgba(255, 255, 255, 0.5);box-shadow: 0 0 10px #666;">
        <div id='sewise-div' class="video-player-container" style="height: 100%">
            <script type="text/javascript" src="/static/plugins/sewiseplayer/sewise.player.min.js"></script>
        </div>
        <div>
            <a style="display: none;position: absolute;top: 1%;left: 98%;color: #666666;z-index: 999"
               onclick="closeMoviee()" class="close-dialog"><i class="fa fa-times-circle fa-lg"></i></a>
        </div>
    </div>
    <script type="text/javascript">
        SewisePlayer.setup({
            server: "vod",
            type: "m3u8",
            skin: "vodFlowPlayer",
            logo: "none",
            lang: "zh_CN",
            topbardisplay: 'enable',
            videourl: ''
        });
        var isFlash = false;
        function playMovie(url) {
            try {
                SewisePlayer.toPlay(url, "", 0, true);
            } catch (e) {
                playerReady.videoURL = url;
                isFlash = true;
            }
            $("#sewise-div").fadeIn();
            $("#play_I").fadeIn();
            $(".close-dialog").fadeIn();
        }



        function playerReady(name){
            if(isFlash){
                SewisePlayer.toPlay(playerReady.videoURL, "", 0, true);
            }
        }
        function closeMoviee() {
            var $player_container = $(".close-dialog");
            $player_container.fadeOut();
            $("#sewise-div").fadeOut();
            $("#play_I").fadeOut();
            /* $("#sewise-div").hide();
             $("#play_I").hide();*/
            window.location.reload();
        }
    </script>
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
                    <img class="title-logo" src="/customizedpage/ah/img/ah_k6kt.png">
                    <a class="login-btn" href="javascript:;">登 录</a>
                    <input class="input-password" type="password" placeholder="密码" tabindex="2">
                    <input class="input-account" type="text" placeholder="邮箱/手机号/用户名" tabindex="1">
                    <div id="tips-msg">
                        <a class="password-error">密码错误</a>
                        <a class="forget-pass" href='#'>忘记密码？</a>
                        <a class="username-error">用户名不存在</a>
                    </div>

                    <!--==============一键绑定================-->
                    <div class="ah-BD" onclick="loginIndex(true)" style="display:none;">
                                               一键绑定
                    </div>
                </div>
            </div>
            <div class="main-content-container">
                <div class='content-container'>
                    <img class="text-1" src="/img/K6KT/main-page/index_banner1.png" />
                   <%-- <img class="text-2" src="/img/K6KT/main-page/text-2.png" />--%>

                    <a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/570daeeb0cf2372af0f4b7bf.mp4.m3u8')">
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
            <div style="height: 30px;text-align: center;line-height: 30px;;width: 100%;background-color:#EAE9DC;position: relative;top:-20px;border-bottom: 1px solid #C0C0C0;font-family: 'Microsoft YaHei';font-weight: bold;color: #5a5a5a">
               <%-- <p>欢迎北京大学入驻复兰大学K6KT-翻转课堂</p>--%>
            </div>
            <div class="shcool-container">
				<div class="shcool-about">
					<div class="school-news">
						<div class="news-title">
							<span style="font: 600 20px 'Microsoft YaHei';">新闻动态</span>
							<a href="http://www.ahedu.gov.cn/" target="_blank"
							   style="color: #5D9E20;font: 600 14px 'Microsoft YaHei';margin-left: 135px;">更多...</a>
							<hr style="background: #5D9E20;height: 3px;"/>
						</div>
						<div class="new-list">
                           <%-- <a href="/customizedpage/ah/news.jsp" target="_blank">
                                <dl>
                                    <dd class="title">在全省“基于微课的翻转课堂项目研究”观摩研讨座谈会上的讲话摘要</dd>
                                    <dd class="author">作者：程艺</dd>
                                </dl>
                            </a>
                            <a href="/customizedpage/ah/newsList.jsp?type=new1" target="_blank">
                                <dl>
                                    <dd class="title">“基于微课的翻转课堂项目试验的推进”重要讲话</dd>
                                    <dd class="author">作者：李灿莉</dd>
                                </dl>
                            </a>
                            <a href="/customizedpage/ah/newsList.jsp?type=new3" target="_blank">
                                <dl>
                                    <dd class="title">基础教育慕课与翻转课堂——C20慕课联盟学校的实践探索</dd>
                                    <dd class="author">作者：田爱丽</dd>
                                </dl>
                            </a>--%>
                          <%--  <a href="http://www.ahjky.com.cn/html/jygl/xwdt/2286.html " target="_blank">
                                <dl>
                                    <dt class="date">2015-3-26</dt>
                                    <dd class="title">关于建立省级教育考试命题专业人才库的通知</dd>
                                    <!-- <dd class="author">作者：刘爱和</dd> -->
                                </dl>
                            </a>
							<a href="http://www.ahjky.com.cn/html/jygl/xwdt/2292.html" target="_blank">
								<dl>
									<dt class="date">2015-4-20</dt>
									<dd class="title">省教科院召开基于微课的翻转课堂项目研究观摩研讨现场会</dd>
									<!-- <dd class="author">作者：姚卫国</dd> -->
								</dl>
							</a>
							<a href="http://www.ahedu.gov.cn/29/view/270448.shtml " target="_blank">
								<dl>
									<dt class="date">2015-4-23</dt>
									<dd class="title">太和县多措并举加快义务教育均衡发展</dd>
									<!-- <dd class="author">作者：姚卫国</dd> -->
								</dl>
							</a>--%>

                               <a href="http://ahjyxxh.ahedu.gov.cn/show.asp?id=1840" target="_blank">
                                   <dl>
                                       <dt>2016-09-20</dt>
                                       <dd class="title">铜陵市以教育信息化推动联合办学现代化</dd>
                                       <%-- <dd class="author">作者：李灿莉</dd>--%>
                                   </dl>
                               </a>

                               <a href="http://ahjyxxh.ahedu.gov.cn/show.asp?id=1838" target="_blank">
                                   <dl>
                                       <dt>2016-09-19</dt>
                                       <dd class="title">亳州市首批省教育信息化试点学校接受市级检阅</dd>
                                       <%-- <dd class="author">作者：田爱丽</dd>--%>
                                   </dl>
                               </a>
                               <a href="http://ahjyxxh.ahedu.gov.cn/show.asp?id=1836" target="_blank">
                                   <dl>
                                       <dt>2016-09-18</dt>
                                       <dd class="title">阜阳信息技术教育激发学生动手能力</dd>
                                       <%-- <dd class="author">作者：田爱丽</dd>--%>
                                   </dl>
                               </a>
                               <a href="http://ahjyxxh.ahedu.gov.cn/show.asp?id=1830" target="_blank">
                                   <dl>
                                       <dt>2016-09-13</dt>
                                       <dd class="title">亳州市五举措推动微课教研与教育信息技术深度融合</dd>
                                       <%--<dd class="author">作者：田爱丽</dd>--%>
                                   </dl>
                               </a>
                               <a href="http://ahjyxxh.ahedu.gov.cn/show.asp?id=1808" target="_blank">
                                   <dl>
                                       <dt>2016-08-30</dt>
                                       <dd class="title">马鞍山市全力推进教育信息化融合课堂教学工作</dd>
                                       <%-- <dd class="author">作者：程艺</dd>--%>
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
	            			<ul id="index-banner">
	            				<li><img src="/customizedpage/ah/img/ah_1.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_2.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_3.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_4.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_5.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_6.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_7.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_8.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_9.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_10.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_11.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_12.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_13.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_14.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_15.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_16.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_17.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_18.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_19.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_20.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_21.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_22.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_23.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_24.jpg"></li>
	            				<li><img style="width: 160px;height: 35px;" src="/customizedpage/ah/img/ah_25.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_26.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_27.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_28.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_29.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_30.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_31.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_32.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_33.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_34.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_35.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_36.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_37.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_38.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_39.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_40.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_41.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_42.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_43.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_44.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_45.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_46.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_47.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_1.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_38.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_39.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_40.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_41.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_42.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_43.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_44.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_45.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_46.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_47.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_48.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_49.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_50.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_51.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_52.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_53.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_54.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_55.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_56.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_57.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_58.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_59.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_60.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_61.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_62.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_63.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_64.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_65.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_66.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_67.jpg"></li>
	            				<li><img src="/customizedpage/ah/img/ah_68.jpg"></li>
	            			</ul>
	            		</div>
	            		<div>
		            		<span style="font: 600 20px 'Microsoft YaHei';">精品微课</span>
		            		<hr style="background: #5D9E20;height: 3px;"/>
	            		</div>
	            		<div id="vote-container">
							<a target="_blanck" href="/customizedpage/ah/weikeVoteList.jsp?type=1&owner=397">
	            			<dl style="background: #f0724f;margin-right: 10px;border: 2px solid #f05633;">
	            				<dt><img src="/customizedpage/ah/img/vote_gaozhong.png"/></dt>
	            				<dd class="wk-title">安徽省高中化学微课评选</dd>
	            				<dd class="descript"><img src="/customizedpage/ah/img/arrow_in.png"/></dd>
	            			</dl>
	            			</a>
							<a target="_blanck" href="/customizedpage/ah/weikeVoteList.jsp?type=0&owner=397">
	            			<dl style="background: #45b4e9;border: 2px solid #29ace9;">
	            				<dt><img src="/customizedpage/ah/img/vote_chuzhong.png"/></dt>
	            				<dd class="wk-title">安徽省初中化学微课评选</dd>
	            				<dd class="descript"><img src="/customizedpage/ah/img/arrow_in.png"/></dd>
	            			</dl>
	            			</a>
	            		</div>
                        <%--<div id="vote-container" style="margin-top: 10px;">--%>
                            <%--<a target="_blanck" href="/customizedpage/ah/weikeVoteList.jsp?type=middle">--%>
                                <%--<dl style="background: #f0724f;margin-right: 10px;border: 2px solid #f05633;">--%>
                                    <%--<dt></dt>--%>
                                    <%--<dd class="wk-title" style="padding-left: 55px;">中考名师在线</dd>--%>
                                    <%--<dd class="descript"><img src="/customizedpage/ah/img/arrow_in.png"/></dd>--%>
                                <%--</dl>--%>
                            <%--</a>--%>
                            <%--<a target="_blanck" href="/customizedpage/ah/newsList.jsp?type=new2">--%>
                                <%--<dl style="background: #45b4e9;border: 2px solid #29ace9;">--%>
                                    <%--<dt></dt>--%>
                                    <%--<dd class="wk-title" style="padding-left: 25px;">翻转课堂经验分享</dd>--%>
                                    <%--<dd class="descript"><img src="/customizedpage/ah/img/arrow_in.png"/></dd>--%>
                                <%--</dl>--%>
                            <%--</a>--%>
                        <%--</div>--%>
	            		<%--<div style="width:700px;height:93px;">
		            			<a target="_blank" href="/business/customizedpage/special/maanshan2/weikeVoteList.jsp?type=middle">
									<span class="maanshan_4pic" style="background-image: url(/business/customizedpage/special/maanshan2/img/middleexam.jpg);width:695px;display:inline-block;margin-top:10px;height:93px;">
									</span>
								</a>
	            	    </div>--%>
	            	</div>
	            </div>
	         </div>
        </div>
        <!-- 页尾 -->
        <%@ include file="/WEB-INF/pages/common/flippedroot.jsp"%>
        <!-- 页尾 -->



    <!--==============一键绑定弹出框==============-->
    <div class="ah-TC" id="ah_bind_controll" style="display:none;">
        <div class="TC-top">
            <em>提示</em><i onclick="hideAhDialog()">X</i>
        </div>
        <dl>
            <dd>绑定安徽基础教育资源应用平台账号</dd>
            <dd>是否对&nbsp;用户名：<i id="sso_name"></i>&nbsp;绑定</dd>
            <dd>
                <span onclick="bind()">是</span><span onclick="hideAhDialog()">否</span>
            </dd>
        </dl>
    </div>
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