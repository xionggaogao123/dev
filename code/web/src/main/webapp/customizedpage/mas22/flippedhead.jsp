<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="/css/style.css"/>
<link rel="stylesheet" href="/css/activate.css" type="text/css"/>
<link rel="stylesheet" href="/css/jquery-ui.css" type="text/css"/>
<link rel="stylesheet" href="/css/forIE.css" type="text/css"/>
<style>
    #fubg {
        background-color: gray;
        left: 0;
        opacity: 0.5;
        position: fixed;
        top: 0;
        z-index: 30;
        filter: alpha(opacity=50);
        -moz-opacity: 0.5;
        -khtml-opacity: 0.5;
        width: 100%;
        height: 100%;
        display: none;
    }

    .popu div {
        overflow: hidden
    }
</style>
<script type="text/javascript" src="/js/validate-form.js"></script>
<script type="text/javascript" src="/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/js/timepicker.js"></script>
<script type="text/javascript" src="/js/sharedpart.js"></script>
<script type="text/javascript" src="/js/ypxxUtility.js"></script>
<script type="text/javascript">
    function initActivateUser(returnUrl) {
        $("#dialog-activate .title a").unbind("click");
        $("#dialog-activate .dialog-submit a").unbind("click");
        $("#dialog-activate .title a").bind("click", function () {
            closeActivate(returnUrl);
        });

        $("#dialog-activate .dialog-submit a").bind("click", function () {
            activateUser(returnUrl);
        });

        $("#email").blur(function () {
            var emailValue = $(this).val();
            var email = this;
            $.ajax({
                url: "/user/initializeEmail.action",
                type: "get",
                data: {
                    email: emailValue
                },
                success: function (data) {
                    if (data.status != "ok") {
                        $('.error').text(data.errorMessage);
                        $(email).css("border", "1px solid red");
                    }
                    else if (data.status == "ok") {
                        $(email).css("border", "1px solid #cccccc");
                        $('.error').text("");
                    }
                    else {

                    }
                }
            });
        });

        $.ajax({
            url: "/selectUserName.action",
            type: "get",
            data: {},
            success: function (data) {
                if (data.result) {
                    $("#userName").val(data.result.userName);
                    $("#userName").data("val", data.result.userName);
                    $("#userSex").val(data.result.sex);
                }
            }
        });
    }
    function showActivate(returnUrl) {
        $("#fubg").css({
            display: "block"
        });
        $("#dialog-activate").show();
        initActivateUser(returnUrl);
    }


    function closeActivateWindow(returnUrl) {
        $("#fubg").hide();
        $("#dialog-activate").hide();
        varcpage = window.location.href;
        if (!currentPageID) {
            if (typeof returnUrl != "undefined") {
                window.location.href = returnUrl;
            }
            else {
                window.location.href = "/main";
            }
        } else {
            if (typeof returnUrl != "undefined") {
                window.location.href = returnUrl;
            }
            else {
                window.location.reload();
            }
        }
    }
    function activateUser(returnUrl) {
        var option = {
            userName: {
                required: true,
                maxLength: 20,
                bCode: true
            },
            nPassword: {
                required: true,
                maxLength: 20,
                minLength: 6
            },
            confirm_password: {
                required: true,
                minLength: 6,
                confirm: 'nPassword'
            },
            email: {
                required: true,
                maxLength: 255,
                email: true
            },
            pwdProtectA: {
                required: true,
                maxLength: 40,
                bCode: true
            }
        };

        if (validateForm(option, {con: $('.error'), msg: ['设置用户名']})) {
            $.ajax({
                url: "/user/doInitializeUser.action",
                type: "post",
                dataType: "json",
                async: true,
                data: {
                    'username': $('#userName').val(),
                    'sex': $('#userSex').val(),
                    'password': $('#nPassword').val(),
                    'email': $('#email').val(),
                    'pwdProtectQ': $('#pwdProtectQ').val(),
                    'pwdProtectA': $('#pwdProtectA').val()
                },
                success: function (data) {
                    if (data.status == "ok") {
                        closeActivateWindow(returnUrl);
                    } else {
                        $(".error").text(data ? data.errorMessage : "");
                    }
                }
            });
        }
    }
    function closeActivate() {
        $("#fubg").hide();
        $("#dialog-activate").hide();

        $.ajax({
            url: "/user/clearSession.action",
            type: "post",
            data: {
                'inJson': true
            },
            success: function (data) {
                window.location.reload();
            }
        });
    }

    function loginout(t) {
        $.ajax({
            url: "/logout.action",
            type: "post",
            dataType: "json",
            data: {
                'inJson': true
            },
            success: function (data) {
                if (data.result == "ok") {
                	if ((typeof data.domain == "undefined") || (data.domain == "")) {
                		 window.location.href = "/";
                	} else {
                		  window.location.href = "/"+data.domain;
                	}
                }
            }
        });
    }

    $(function () {
        $("#dialog-activate iframe").attr('src',
                "/obligations.html");
    });
    $.ajaxSetup({cache: false });


    getPrivateLetterCount();
    //setInterval("getPrivateLetterCount()", 60000);//定时刷新未读私信数
    function getPrivateLetterCount() {
        $.ajax({
            url: "/user/getPrivateLetterCount.action",
            type: "post",
            data: {
                'inJson': true
            },
            success: function (data) {
                //$("#sl").html(data.messageCount);

                if (data.messageCount > 0) {
                    $('#message').css('background', 'url(/img/newmessage.png) no-repeat 5px 0px');
                    $("#sl").html(data.messageCount);
                } else {
                    $('#message').css('background', 'url(/img/nomessage.png) no-repeat 10px 10px');
                }
            }
        });
    }
</script>
<div id="flipped_headr">
    <div id="flipped_headr_top">
        <div id="mid">
            <%
			int xxid = 232;

			if (session.getAttribute("islogout") != null && "1".equals(session.getAttribute("islogout"))) {
				session.putValue("islogout", "0");
			%>
			<script type="text/javascript">loginout(true);	</script>	
			<% 
			}
			if (session.getAttribute("xxid") != null) {
				xxid = Integer
						.parseInt((String) (session.getAttribute("xxid")));
			}
			 if (xxid>0) {
			%>
				<a href="" style="float:left;"><img alt="" style="position: relative;top: 10px;margin-right:5px;vertical-align: initial;" src="/img/logofz<%=xxid%>.png"
							style="cursor: pointer;"></a>
			<%
			 }
			%>
			<div id="fz_phone_geo">
				<span>400-820-6735</span>
			</div>
			<% if (session.getAttribute("currentUser") != null) { %>
				<div id="fz_h_cart" style="overflow: hidden;">

	                    <span id="fz_online"><a id="ad"  style="color: red;cursor: default;">${currentUser.userName}</a>，欢迎您
						<span id="fz_out" style="cursor: pointer;" onclick="loginout();">[退出]</span>
						</span>	
						<span id="message" style="position: relative;top:35px;float:right;">
						<a href="/message"><span id="sl" style="cursor:pointer;"></span></a>
						</span>			
				</div>
			<% } %>
		</div>
	</div>
	<div id="fz_liebiao">
	  <img src="/img/logo_fulaan.png" alt="" style="float:left;">
		<div id="fz_me">	
			  <% if (session.getAttribute("currentUser") == null) { %>
	   <% } else if(((UserInfo)session.getAttribute("currentUser")).getRole()==1){  %>
	   	    <a href="/user"><span id="menu0" class="xuanxiang" style="border-left: 5px solid #FF8C25;">我的首页</span></a>
            <a href="/cloudClass"><span id="menu1" class="xuanxiang">我的微课</span></a>
            <a href="/teacher/class"><span id="menu2" class="xuanxiang">我的班级</span></a>
            <% if (((UserInfo)session.getAttribute("currentUser")).getIsmanage()==1){   %>
            <a href="/manager"><span id="menu5" class="xuanxiang">管理员功能</span></a>
            <% } %>
			<a href="/message"><span id="menu4" class="xuanxiang">个人中心</span></a>
		<% } else if(((((UserInfo)session.getAttribute("currentUser")).getRole()==2||((UserInfo)session.getAttribute("currentUser")).getRole()==9) && "0".equals(session.getAttribute("headmaster"))) ||(((UserInfo)session.getAttribute("currentUser")).getRole()==3)){  %>
            <!-- 未带班校长 “我的首页”相当于“全校管理” -->
            <a href="/user"><span id="menu6" class="xuanxiang" style="border-left: 5px solid #FF8C25;">我的首页</span></a>
            <a href="/cloudClass"><span id="menu1" class="xuanxiang">我的微课</span></a>
             <% if (((UserInfo)session.getAttribute("currentUser")).getIsmanage()==1){   %>
            <a href="/manager"><span id="menu5" class="xuanxiang">管理员功能</span></a>
            <% } %>
            <a href="/message"><span id="menu4" class="xuanxiang">个人中心</span></a>
	   <% } else if((((UserInfo)session.getAttribute("currentUser")).getRole()==2||((UserInfo)session.getAttribute("currentUser")).getRole()==9) && "1".equals(session.getAttribute("headmaster"))){  %> 
      		<a href="/user"><span id="menu0" class="xuanxiang" style="border-left: 5px solid #FF8C25;">我的首页</span></a>
            <a href="/cloudClass"><span id="menu1" class="xuanxiang">我的微课</span></a>
            <a href="/teacher/class"><span id="menu2" class="xuanxiang">我的班级</span></a>
			<a href="/headmaster"><span id="menu6" class="xuanxiang">全校管理</span></a>
			 <% if (((UserInfo)session.getAttribute("currentUser")).getIsmanage()==1){   %>
            <a href="/manager"><span id="menu5" class="xuanxiang">管理员功能</span></a>
            <% } %>
			<a href="/message"><span id="menu4" class="xuanxiang">个人中心</span></a>
	   <% } else if(((UserInfo)session.getAttribute("currentUser")).getRole()==0){  %> 
	        <a href="/user"><span id="menu0" class="xuanxiang" style="border-left: 5px solid #FF8C25;">我的首页</span></a>
            <a href="/cloudClass"><span id="menu1" class="xuanxiang">我的微课</span></a>
            <a href="/student/class"><span id="menu2" class="xuanxiang">我的班级</span></a>
			<a href="/message"><span id="menu4" class="xuanxiang">个人中心</span></a>
		<% } else if(((UserInfo)session.getAttribute("currentUser")).getRole()==4){  %>
	        <a href="/user"><span id="menu0" class="xuanxiang" style="border-left: 5px solid #FF8C25;">我的首页</span></a> 
			<a href="/student/course"><span id="menu1" class="xuanxiang">我的微课</span></a> 
			<a href="/student/class"><span id="menu2" class="xuanxiang">我的班级</span></a> 
			<a href="/message"><span id="menu4" class="xuanxiang">个人中心</span></a>
		<% } else if(((UserInfo)session.getAttribute("currentUser")).getRole()==6){  %>
            <a href="/user"><span id="menu0" class="xuanxiang" style="border-left: 5px solid #FF8C25;">我的首页</span></a>
            <a href="/cloudClass"><span id="menu1" class="xuanxiang">我的微课</span></a>
            <a href="/teacher/class"><span id="menu2" class="xuanxiang">我的班级</span></a>
			<a href="/browser"><span id="menu3" class="xuanxiang">我的学科组</span></a> 
			 <% if (((UserInfo)session.getAttribute("currentUser")).getIsmanage()==1){   %>
            <a href="/manager"><span id="menu5" class="xuanxiang">管理员功能</span></a>
            <% } %>
			<a href="/message"><span id="menu4" class="xuanxiang">个人中心</span></a>
	    <% } else if(((UserInfo)session.getAttribute("currentUser")).getRole()==7) { %>
	    	<a href="/managerself"><span id="menu5" class="xuanxiang">管理员功能</span></a>
	     <% } else {}  %>
		</div>
	</div>
	<%-- <div id="flipped_headr_bottom" class="forIEFloatP">
	   <% if (session.getAttribute("currentUser") == null) { %>
		<span>欢迎来到在线云课堂，祝你学习愉快!</span>
	   <% } else if(((UserInfo)session.getAttribute("currentUser")).getRole()==1){  %>
	   <span><a href="/reverse/userPage.action">我的首页</a></span><span class="FloatC"><a href="javascript:loginout();">退出登录</a></span>
	   <% } else if(((UserInfo)session.getAttribute("currentUser")).getRole()==0){  %>
	    <span><a href="/reverse/userPage.action">我的首页</a></span><span class="FloatC"><a href="javascript:loginout();">退出登录</a></span>
	    <% } else {}  %>
	</div> --%>
</div>

<div id="loading" class="loading"><img alt="" src="/img/loading.gif" style="position:fixed; left:50%;top:50%;"></div>
<div id="fubg" style="width: 100%; height: 100%; display: none;"></div>

<div id="dialog-activate" class="dialog-ar" style="display:none;margin-left:-199px;width:auto">
	<form name="frm">
		<div class="title no-titleIcon">
			<span class="title-login blue">账户激活</span> <span
				style="margin-left: 20px">您是第一次登陆，请先激活账号</span> <a href="#"><img src="/img/error-grey.png"
				class="clostBtn" /></a>
		</div>
		<div class="dialog-content">
			<div class="dialog-content-right">
				<div>
					<div class="input-div">
						<span>设置新用户名</span> &nbsp;&nbsp;<input id="userName"
							class="input loginNameInput" name="userName" type="text" placeholder="设置新用户名"
							class="input" />
					</div>
					<div class="input-div">
						<span>用户性别</span>&nbsp;&nbsp; <select class="cipher" id="userSex" 
							name="userSex">
							<option value="1">男</option>
							<option value="0">女</option>
						</select>
					</div>
					<div class="input-div">
						<span>设置新密码</span>&nbsp;&nbsp;<input id="nPassword" name="nPassword" placeholder="设置新密码"
							type="password" class="input passwordInput" />
					</div>
					<div class="input-div">
						<span>确认新密码</span> &nbsp;&nbsp;<input id="confirm_password" placeholder="确认新密码"
							type="password" class="input passwordInput" />
					</div>
				</div>

				<hr>

				<div id="activate-tip" class="tip">激活安全邮箱或密码保护以后，您可以通过邮箱或密保找回密码</div>

				<div class="dialog-attachment">
					<div>
						<div class="input-div">
							<span>个人安全邮箱</span> &nbsp;&nbsp;<input id="email" name="email" placeholder="个人安全邮箱"
								type="email" class="input" />
						</div>
						<div class="input-div">
							<span>密码保护问题</span>&nbsp;&nbsp; <select class="cipher"
								id="pwdProtectQ" name="pwdProtectQ">
								<option value="0">您的出生地在哪？</option>
								<option value="1">你母亲是生日？</option>
								<option value="2">您高中班主任的名字是？</option>
								<option value="3">您父亲的姓名是？</option>
								<option value="7">您母亲的姓名是？</option>
								<option value="4">您小学班主任的名字是？</option>
								<option value="5">您的小学校名是？</option>
								<option value="6">您的学号（或工号）是？</option>
							</select>
						</div>
						<div class="input-div">
							<span>密码保护答案</span> &nbsp;&nbsp;<input id="pwdProtectA"  placeholder="密码保护答案"
								type="text" name="pwdProtectA" class="input" />
						</div>
					</div>
					<div class="dialog-submit">
						<label class="error"
							style="color: #FF0000; font-size: 12px; height: 20px; text-align: center; display: block"></label>
						<label style="height: 30px; text-align: center"><a
							class="blue-btn"
							style="left: 150px; height: 16px; cursor: pointer">激活</a></label>
						<div class="clear"></div>
					</div>
				</div>
			</div>
		</div>
	</form>
</div>
