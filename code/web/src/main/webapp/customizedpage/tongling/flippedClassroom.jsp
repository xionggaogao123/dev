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
        <%--<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css" />
        <link rel="stylesheet" type="text/css" href="/customizedpage/tongling/css/style.css"/>--%>
        <link rel="stylesheet" type="text/css" href="/customizedpage/tongling/css/main.css"/>
        <%--<link rel="stylesheet" type="text/css" href="/customizedpage/tongling/css/reset.css"/>--%>
        <link rel="stylesheet" type="text/css" href="/customizedpage/tongling/css/tlyunindex.css"/>

        <link rel="stylesheet" href="/static/css/nivo-slider.css" type="text/css" />
        <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
        <script type='text/javascript' src='/static/plugins/bootstrap/js/bootstrap.min.js'></script>
        <script type='text/javascript' src='/static/js/k6kt-index.js'></script>
        <script src='http://static.polyv.net/file/polyvplayer_v2.0.min.js'></script>
        <script src="/static/js/jquery.nivo.slider.js"></script>
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
                $("#enter").click(function()
                {
                    var account=$("#account").val();
                    if(account=="")
                    {
                        alert("请输入身份证号码");
                    }
                    else{
                        window.location.href="http://60.173.37.162:8081?account="+account+"&password=1234";
                    }
                });
        	});
        </script>
    </head>
    <body>
        <div class="main-container">
            <div class="login-bar">
                <div class='title-bar-container' style="height:105px;">
                    <span class="span-logo"></span>
                    <a class="login-btn but-dl" href="javascript:;">登 录</a>
                    <input class="input-password" type="password" placeholder="密码" tabindex="2">
                    <input class="input-account" type="text" placeholder="邮箱/手机号/用户名" tabindex="1">
                    <div id="tips-msg">
                        <a class="password-error">密码错误</a>
                        <a class="forget-pass" href='#'>忘记密码？</a>
                        <a class="username-error">用户名不存在</a>
                    </div>
                </div>
            </div>
        </div>
        <%--<div class="container">
            <div class="cont-main">
                <div class="gotolive">
                    <a href="http://139.196.198.137:8081/" class="gotolive-A"></a>
                </div>
                <div class="gototeac">
                    <a href="http://www.fulaan.com/" class="gotolive-A"></a>
                </div>
            </div>
        </div>--%>
        <%--<div class="container">
            <div class="cont-main">
                <div class="gototeac">
                    <dl>
                        <dd><p class="p-rk">游客入口</p></dd>
                        <dd><input type="text" id="account" placeholder="身份证信息"></dd>
                        <dd><button id="enter">进入</button></dd>
                        <dd class="dd-re">
                            <p class="p2-rk">平台使用规则：</p>
                            <p>1.本平台为铜陵市体育中高考直播平台，提供视频直播以及录播回顾服务。</p>
                            <p>2.登陆请通过身份证号码以及密码进入。</p>
                            <p>3.如您通过身份证号码不能成功登陆，请联系管理员将您的身份证号码录入系统。</p>
                            <div class="div-ewm">
                                <img src="/customizedpage/tongling/images/tl-ewm.png" width="147px" height="147px;">
                                <span>用手机随时观看直播</span>
                            </div>
                        </dd>
                    </dl>

                </div>
            </div>
        </div>--%>
        <div class="container">
            <div class="cont-main">
                <div class="gototeac">
                    <dl>
                        <dd><p class="p-rk">登录入口</p></dd>
                        <dd><input type="text" id="account" placeholder="准考证号码或tledu"></dd>
                        <%--<dd class="errinfo">${err}</dd>--%>
                        <dd><button id="enter">进入</button></dd>
                        <dd class="dd-re">
                            <p class="p2-rk">系统使用规则：</p>
                            <p>1.本系统为铜陵市体育中考直播系统，提供视频直播以及录播回顾服务。</p>
                            <p>2.登录入口请输入考生准考证号码或tledu账号进入。</p>
                            <p>3.支持浏览器：火狐浏览器、谷歌浏览器、IE8以上浏览器。建议分辨率：1280*720及以上。</p>
                            <div class="div-ewm">
                                <img src="/customizedpage/tongling/images/tl-ewm.png" width="100px" height="100px;">

                                <img src="/customizedpage/tongling/images/ios-ewm.png" width="100px" height="100px;">
                                <span>下载安卓客户端观看</span>
                                <span>下载苹果客户端观看</span>
                            </div>
                        </dd>
                    </dl>

                </div>
            </div>
        </div>
        <div class="footer">
            <div class="copyright">
                <p>版权所有：上海复兰信息科技有限公司<a href="http://www.fulaan-tech.com/">www.fulaan-tech.com</a>&nbsp;&nbsp;&nbsp;&nbsp;沪ICP备14004857号</p>
                <span><a href="/aboutus/k6kt">关于我们</a></span>
                <span> <a href="/contactus/k6kt">联系我们</a></span>
                <span><a href="/service/k6kt">服务条款 </a></span>
                <span><a href="/privacy/k6kt">隐私保护 </a></span>
                <span><a href="http://wpa.qq.com/msgrd?v=1&amp;uin=2803728882&amp;site=qq&amp;menu=yes" target="_blank">在线客服</a></span>
            </div>
        </div>
        <!-- 页尾 -->
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