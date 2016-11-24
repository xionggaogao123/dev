<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <style type="text/css">
			body, dl, dd, h1, h2, h3, h4, h5, h6, p, form{margin:0;}
			ol,ul{margin:0; padding:0; list-style: none;}
			:focus {outline: 1;}
			.clearfix:after{content:'.';display:block;clear:both;line-height:0;height:0;visibility:hidden;}
			img{border:0;}
			body{color:#333;font:normal normal normal 14px/20px 'Microsoft YaHei',Arial,sans-serif;background:#fff;}
			input,select,textarea,button{vertical-align:middle;font-size:100%;outline:0;font-family:'Microsoft YaHei',Arial,sans-serif;}
			a{color:#333;text-decoration:none;outline:0}a:hover{color:#ff9c00;text-decoration:none;}
			i,em{font-style: normal;}
			table{border:0;margin:0;border-collapse:collapse;border-spacing:0;}
			table td{padding:0;}
			textarea{resize: none;}
			button{cursor:pointer;}
			.header{height:88px;border-bottom:3px solid #EB7500;}
			.logo-vo{width:1000px;margin:0 auto; height:88px;background:url(k6kt_happy_vo.png) no-repeat 0 18px;}
			.find-pw{height:32px;width:1000px;margin:12px auto;border-bottom: 1px solid #000;font-size:16px;line-height:32px;}
			.gou{display:inline-block;width:31px;height:31px;background:url(right_vo.png) no-repeat;position:relative;top:9px;}
			.cont-main span{font-size:16px;color:#000;}
			.cont-main{width:500px;margin:30px auto 0;line-height:40px;text-align:center;}
			.cont-main .gray-dd{color:#A5A5A5;font-size:13px;}
			.cont-main button{color:#fff;background:#A5A5A5;width:69px;height:32px;border:none;}
			.container{height:670px;}
			.footer{height: 155px;
			background: #333333;
			margin-top: -6px;
			width: 100%;
			font-size: 13px;
			margin: 15px auto 0;}
			.store-foott-main {
			    width: 1000px;
			    margin: 0px auto;
			    background: #333333;
			    height: 125px;
			    padding:15px;
			}
			.store-foott-left {
			    width: 440px;
			    float: left;
			    color: #bfbfbf;
			    line-height: 35px;
			}
			.store-foott-left span {
			    margin-right: 20px;
			}
			.store-foott-left a {
			    color: #bfbfbf;
			    margin-right: 25px;
			    text-decoration: underline;
			    display: inline-block;
			}
			.store-foott-right {
			    float: right;
			    width: 305px;
			    color: #bfbfbf;
			    margin-top: 15px;
			}
			.store-foott-right div {
			    float: left;
			    width: 190px;
			}
			.store-foott-right div img {
			    margin-left: 5px;
			    cursor: pointer;
			}
			.store-foott-right div span {
			    display: inline-block;
			    margin-top: 20px;
			}
        </style>
    </head>
    <script type="text/javascript">
    
    function CloseWebPage(){
    	 if (navigator.userAgent.indexOf("MSIE") > 0) {
    	  if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
    	   window.opener = null;
    	   window.close();
    	  } else {
    	   window.open('', '_top');
    	   window.top.close();
    	  }
    	 }
    	 else if (navigator.userAgent.indexOf("Firefox") > 0) {
    	  window.location.href = 'about:blank ';
    	 } else {
    	  window.opener = null;
    	  window.open('', '_self', '');
    	  window.close();
    	 }
    	}
    
    </script>
    <body>
    	<div class="header">
    		<div class="logo-vo"></div>
    	</div>
    	<div class="container">
    		<div class="find-pw">
    			找回密码
    		</div>
    		<dl class="cont-main">
    			<dd>
    				<i class="gou"></i>
    				<span>安全邮箱${email }验证成功！
    				</span>
    			</dd>
    			<dd class="gray-dd">提示：关闭当前页面，继续找回密码服务</dd>
    			<dd>
    				<button onclick="CloseWebPage()">关闭</button>
    			</dd>
    		</dl>
    	</div>
    	<div class="footer">
    		<div class="store-foott-main">
        <div class="store-foott-left">
            <span>版权所有：上海复兰信息科技有限公司</span><a target="_blank" href="http://www.fulaan-tech.com">www.fulaan-tech.com</a>
                   <span>
                       <a href="/aboutus/k6kt">关于我们</a>
                       <a href="/contactus/k6kt">联系我们</a>
                       <a href="/service/k6kt">服务条款 </a>
                       <a href="/privacy/k6kt">隐私保护 </a>
                       <a href="http://wpa.qq.com/msgrd?v=1&amp;uin=2803728882&amp;site=qq&amp;menu=yes" target="_blank">在线客服</a>
                       <a href="http://wpa.qq.com/msgrd?v=1&amp;uin=2803728882&amp;site=qq&amp;menu=yes" style="position: relative;top: 5px;"></a>
                   </span>
            <span>沪ICP备14004857号</span>
        </div>
        <div class="store-foott-right">
            <div>
                <img src="store-phone.png">
                    <span>
                        <i>关注我们：</i>
                        <a target="_blank" href="http://weibo.com/FulaanTechnology"><img src="store-WEB.png"></a>
                        <a target="_blank" href="http://t.qq.com/FulaanTechnology"><img src="store-WEBI.png"></a>
                        <a><img src="store-WEX.png"></a>
                    </span>
            </div>
            <img src="store-WEII.jpg">
        </div>
    </div>
    	</div>
    </body>
</html>