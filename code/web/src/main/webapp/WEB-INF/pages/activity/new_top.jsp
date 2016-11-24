<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!--===========================头部信息==================================-->
<link href="../static/css/homepage.css" type="text/css" rel="stylesheet">

	<script type="text/javascript">
    getPrivateLetterCount();
    //setInterval("getPrivateLetterCount()", 60000);//定时刷新未读私信数
    function getPrivateLetterCount() {
        $.ajax({
            url: "/letter/count.do",
            type: "post",
            data: {
                'inJson': true
            },
            success: function (data) {
                //$("#sl").html(data.messageCount);
                if (data > 0) {
                    $('#message').css('background', 'url(/img/newmessage.png) no-repeat 5px 0px');
                    $("#sl").html(data);
                    jQuery("#xyd").html(data);
                } else {
                    $('#message').css('background', 'url(/img/nomessage.png) no-repeat 10px 10px');
                    jQuery("#xyd").hide();
                }
            }
        });
    }
    </script>
<div class="homepage-top" style="line-height: 25px; overflow: visible;">
	<div class="homepage-top-main">
		<div class="homepage-top-left" style="height:38px">
			<%--#set($png=".png")--%>
			<img src="${sessionValue.schoolLogo}" style="margin-top: -10px;">

		</div>
		<div class="homepage-top-right"
			style="position: relative; left: -130px;">

			<div class="homepage-top-right-letter">
				<i class="fa fa-envelope"></i> <span class=" homepage-i"
					onclick="location.href='/message'">私信</span> <i
					class="fa fa-gear homepage-i"
					style="cursor: default; text-decoration: none; padding-left: 0px;"></i>
				<span class=" homepage-i" id="zhongxin">个人中心</span>
				<div class="homepage-hover">
					<div class="homepage-hove-I">
						<ul>
							<li><a href="/message">我的私信</a></li>
							<li><a href="/basic">个人设置</a></li>
							<li><a href="/personal/userhelp">用户手册</a></li>
							<li><a href="/friendcircle/getFriendList.do?type=0">我的好友</a></li>
						</ul>
					</div>
				</div>
				<span class="homepage-message"
					style="position: relative; left: -130px" id="xyd"></span> 
					<span>欢迎您，${currentUser.userName}
					<span id="fz_out" style="cursor: pointer; font-size: small;"
					onclick="loginout();"> [退出]</span>
				</span>
			</div>
		</div>
	</div>
</div>

