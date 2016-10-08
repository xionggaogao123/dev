<%@ include file="/common/taglib.jsp" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
    	<title>管理系统-登录</title>
        <meta charset="utf-8">
        <script type="text/javascript" src="${ctx }/js/jquery-1.11.1.js"></script>
        <link rel="stylesheet" type="text/css" href="${ctx }/css/reset.css">
        <script type="text/javascript">
        	$(function() {
        		var msg = '${errorMsg}';
        		if(msg) {
        			alert(msg);
        		}
        	});
        </script>
    </head>
    <body style="background: #3FA8D8">
        <div class="login-cont">
            <div class="line1"></div>
            <div class="line2"></div>
            <div class="dintou"></div>
            <div class="dinxian"></div>
            <div class="wind-login">
                <form action="${ctx }/user/toLogin" method="post">
                	<div class="top">
                    	<span class="sp1"></span>
                    	<span class="sp2"></span>
                    	<div class="mid-do"></div>
                	</div>
                	<p class="p-input">
                    	<span>账号</span><br>
                    	<input type="input" name="loginName" class="inp1">
                	</p>
                	<p class="p-input">
                    	<span>密码</span><br>
                    	<input type="password" name="password" class="inp1">
                	</p>
                	<p class="p-login">
                    	<button type="submit">登录</button>
                	</p>
                </form>
            </div>
        </div>

    </body>
</html>