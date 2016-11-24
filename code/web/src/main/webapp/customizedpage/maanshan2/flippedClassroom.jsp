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
        <link rel="stylesheet" type="text/css" href="/customizedpage/maanshan2/css/style.css"/>
        <link rel="stylesheet" type="text/css" href="/customizedpage/maanshan2/css/main.css"/>
        <link rel="stylesheet" href="/static/css/nivo-slider.css" type="text/css" />
        <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
        <script type='text/javascript' src='/static/plugins/bootstrap/js/bootstrap.min.js'></script>
        <script type='text/javascript' src='/static/js/k6kt-index.js'></script>
        <script src='http://static.polyv.net/file/polyvplayer_v2.0.min.js'></script>
        <script src="/static/js/jquery.nivo.slider.js"></script>
        <style type="text/css">
			.maanshan_4pic{
				width: 700px;height: 80px;
			}
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

					<span class="title" style="font: bold 24px 'microsoft yahei';margin-left: 5px;margin-top: 32px;">马鞍山市名师在线 快乐课堂</span>
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
                    <img class="text-1" src="/img/K6KT/main-page/index_banner1.png"/>
          <%--          <img class="text-2" src="/img/K6KT/main-page/text-2.png"/>--%>

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
            <div style="height: 30px;text-align: center;line-height: 30px;;width: 100%;background-color:#EAE9DC;position: relative;top:-20px;border-bottom: 1px solid #C0C0C0;font-family: 'Microsoft YaHei';font-weight: bold;color: #5a5a5a">
                <%--<p>欢迎北京大学入驻复兰大学K6KT-翻转课堂</p>--%>
            </div>
            <div class="shcool-container">
	            <div class="shcool-about">
	            	<div class="school-news">
	            		<div class="news-title">
	            			<span style="font: bold 20px 'Microsoft YaHei';">新闻动态</span>
	            			<a href="http://www.masedu.cn/" target="_blank" style="color: #5D9E20;font: 600 14px 'Microsoft YaHei';margin-left: 135px;">更多...</a>
	            			<hr style="background: #5D9E20;height: 3px;"/>
	            		</div>
	            		<%--<div class="new-list">
	            			<a href="http://www.mas.gov.cn/content/detail/5498c33552c4ccdd31cec913.html" target="_blank">
	            			<dl>
	            				<dt class="date">2014-12-23</dt>
	            				<dd class="title">马鞍山4所中小学将建机器人实验室</dd>
	            				<!-- <dd class="author">作者：姚卫国</dd> -->
	            			</dl>
	            			</a>
	            			<a href="http://www.ceiea.com/html/201412/201412191103006057.shtml" target="_blank">
	            			<dl>
	            				<dt class="date">2014-12-19</dt>
	            				<dd class="title">马鞍山市四村小学召开首届“教师现代教育技术应用能力大赛”表彰会</dd>
	            				<!-- <dd class="author">作者：刘爱和</dd> -->
	            			</dl>
	            			</a>
	            			<a href="http://www.masedu.cn/DocHtml/1/2014/12/22/2826169643441.html " target="_blank">
	            			<dl>
	            				<dt class="date">2014-12-22</dt>
	            				<dd class="title">我市在省学生资助工作研究会成立大会上交流发言</dd>
	            				<!-- <dd class="author">作者：姚卫国</dd> -->
	            			</dl>
	            			</a>
	            			<a href="http://www.masedu.cn/DocHtml/1/2014/10/8/9530988340284.html " target="_blank">
	            			<dl>
	            				<dt class="date">2014-10-8</dt>
	            				<dd class="title">含山县举行“向国旗敬礼”主题活动启动仪式</dd>
	            				<!-- <dd class="author">作者：姚卫国</dd> -->
	            			</dl>
	            			</a>
	            		</div>--%>
                        <div class="new-list">
                            <a href="http://www.masedu.cn/DocHtml/1/2016/9/28/9169675566784.html" target="_blank">
                                <dl>
                                    <dt class="date">2016-09-28</dt>
                                    <dd class="title">含山县召开高效课堂教学改革研讨会</dd>
                                    <!-- <dd class="author">作者：姚卫国</dd> -->
                                </dl>
                            </a>
                            <a href="http://www.masedu.cn/DocHtml/1/2016/9/28/9253995566736.html" target="_blank">
                                <dl>
                                    <dt class="date">2015-09-28</dt>
                                    <dd class="title">马鞍山市首届“1+4”互惠教育研究共同体成立大会成功召开</dd>
                                    <!-- <dd class="author">作者：刘爱和</dd> -->
                                </dl>
                            </a>
                            <a href="http://www.masedu.cn/DocHtml/1/2016/9/27/7354596766718.html" target="_blank">
                                <dl>
                                    <dt class="date">2016-09-27</dt>
                                    <dd class="title">信息技术与课堂教学深度融合 提升英语教师信息化能力</dd>
                                    <!-- <dd class="author">作者：姚卫国</dd> -->
                                </dl>
                            </a>
                            <a href="http://www.masedu.cn/DocHtml/1/2016/9/27/2501072566704.html" target="_blank">
                                <dl>
                                    <dt class="date">2016-09-27</dt>
                                    <dd class="title">朱家贤赴多校开展调研活动</dd>
                                    <!-- <dd class="author">作者：姚卫国</dd> -->
                                </dl>
                            </a>
                            <a href="http://www.masedu.cn/DocHtml/1/2016/9/14/2932874366166.html" target="_blank">
                                <dl>
                                    <dt class="date">2016-09-14</dt>
                                    <dd class="title">月圆中秋 梦圆大学</dd>
                                    <!-- <dd class="author">作者：姚卫国</dd> -->
                                </dl>
                            </a>
                        </div>
	            	</div>
	            	<div class="teachers-info">
	            		<div>
		            		<span style="font: bold 20px 'Microsoft YaHei';">马鞍山市联盟校展示</span>
		            		<hr style="background: #5D9E20;height: 3px;"/>
	            		</div>
	            		<div id="unions">
	            			<ul id="index-banner">
	            				<li><img src="/customizedpage/maanshan2/img/dangtuerzhong.jpg"></li>
	            				<li><img src="/customizedpage/maanshan2/img/maanshanzhognjiashuangyuxuexiao.jpg"></li>
	            				<li><img src="/customizedpage/maanshan2/img/maanshanjiankangluxiaoxue.jpg"></li>
	            				<li><img src="/customizedpage/maanshan2/img/maanshanshierzhong.jpg"></li>
	            				<li><img src="/customizedpage/maanshan2/img/maanshanangongdafuzhong.jpg"></li>
	            				<li><img src="/customizedpage/maanshan2/img/maanshanshiyanxiaoxue.jpg"></li>
	            				<li><img src="/customizedpage/maanshan2/img/maanshanshiwangjiashanxiaoxue.jpg"></li>
	            				<li><img src="/customizedpage/maanshan2/img/maanshanshidierzhongxue.jpg"></li>
	            				<li><img src="/customizedpage/maanshan2/img/maanshanshidibazhongxue.jpg"></li>
	            				<li><img src="/customizedpage/maanshan2/img/maanshanshihongxingzhongxue.jpg"></li>
	            				<li><img src="/customizedpage/maanshan2/img/maanshanshihuayuanchujizhongxue.jpg"></li>
	            				<li><img src="/customizedpage/maanshan2/img/maanshanshiyuanxiaoxue.jpg"></li>
	            				<li><img src="/customizedpage/maanshan2/img/maanshansongyuanpeizhengzhongxue.jpg"></li>
	            				<li><img src="/customizedpage/maanshan2/img/maanshanlingliangxiaoxue.jpg"></li>
	            				<li><img src="/customizedpage/maanshan2/img/maanshandiershierzhongxue.jpg"></li>
	            				<li><img src="/customizedpage/maanshan2/img/maanshanjinruixiaoxue.jpg"></li>

	            			</ul>
	            		</div>
	            		<div>
		            		<span style="font: bold 20px 'Microsoft YaHei';">名师在线</span>
		            		<hr style="background: #5D9E20;height: 3px;"/>
	            		</div>
						<div id="vote-container">
							<a target="_blank" href="/business/ypxx/special/maanshan2/weikeVoteList.jsp?type=high">
								<span class="maanshan_4pic" style="background-image: url(/customizedpage/maanshan2/img/college.jpg)">
								</span>
							</a>
							<a target="_blank" href="/business/ypxx/special/maanshan2/weikeVoteList.jsp?type=middle">
								<span class="maanshan_4pic" style="background-image: url(/customizedpage/maanshan2/img/middleexam.jpg)">
								</span>
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