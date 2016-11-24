<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Tony
  Date: 2014/8/12
  Time: 18:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>个人账户</title>
    <link rel="stylesheet" type="text/css" href="/static/css/message.css">
    <link rel="stylesheet" href="/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/static/css/teacherSheet.css"/>
    <link rel="stylesheet" href="/static/css/excellentLesson.css"/>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css"/>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script src="/static/js/bootstrap-paginator.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="/static/js/lessons/coursesManage.js"></script>
    <script type="text/javascript" src="/static/js/excellentPay.js"></script>
    <script type="text/javascript">
        var currentPageID = 11;
        var accountBalance = ${balance};
        $(function(){
        	$('#records-table tbody tr:even').css("backgroundColor","#eee");
        });
    </script>
    <script type="application/javascript">
        var withDrawZFB = true;
        $(function(){
            $('.account_type').each(function(i){
                if(i==0){
                    $(this).addClass('account_hover');
                    $('#accountdiv').show();
                    $('#openbankdiv').hide();
                    $('#bankaccountdiv').hide();
                    $(".recharge-info-YHK :input").each(function () {
                        $(this).val("");
                    });
                    $('#withdraw-btn').attr('tid', 1);
                }
                $(this).click(function(){
                    $('.account_type').removeClass('account_hover');
                    $(this).addClass('account_hover');
                    $(".recharge-info-YHK :input").each(function () {
                        $(this).val("");
                    });
                    if(i==0) {
                        $('#accountdiv').show();
                        $('#openbankdiv').hide();
                        $('#bankaccountdiv').hide();
                        $('#withdraw-btn').attr('tid', 1);
                        withDrawZFB = true;
                    }else{
                        $('#accountdiv').hide();
                        $('#openbankdiv').show();
                        $('#bankaccountdiv').show();
                        $('#withdraw-btn').attr('tid', 2);
                        withDrawZFB = false;
                    }
                });
            });
        })


    </script>

</head>
<body style="background: #fff;">
<!-- 页头 -->
<%@ include file="../common_new/head.jsp" %>
<!-- 页头 -->
<%
	//TODO
    //int role = ((UserInfo) session.getAttribute("currentUser")).getRole();
	int role = 1;
%>
<jsp:include page="/infoBanner.do">
    <jsp:param name="bannerType" value=""/>
    <jsp:param name="menuIndex" value="2"/>
</jsp:include>

<div id="fullbg"></div>
<div id="YCourse_player" style="position:fixed;top:50%;left:50%;margin-left:-430px;margin-top:-302px;padding:15px 15px 0 0;"></div>
<div id="content_main_container" style="position:relative;">
	<div id="content_main">
		<!-- 左侧导航-->
		<%@ include file="../common_new/col-left.jsp" %>
		<!-- left end -->
		<!-- right start-->
		<div id="right-container">
			<div id="content_main_containerr" style="/*margin-top: 5px;*/">
				<div id="main-content" style="position: relative; overflow:hidden;" class="main-content-msg">
					<div id="account-right">
						<div id="account-right-title" style="border-bottom:none; ">
							<span><a class="help" href="/emarket/myAccountOrder.do">个人账户</a></span>
                            <span><a class="help" href="/emarket/myOrder.do">我的订单</a></span>
                            <span><a class="help" href="/emarket/myBalance.do">我要充值</a></span>
                            <span><a class="help selectedd" href="/emarket/withDraw.do">我要提现</a></span>
                            <span><a class="help" href="/emarket/myExpireLesson.do">过期课程</a></span>
                            <%if(role == 1 || role == 2 || role == 9){ %>
                            <span><a class="help" href="/emarket/saleList.do">销售订单</a></span>
                            <%} %>
						</div>
						<div id="account-right-content" class="account-right-content">
							<div id="myaccount">

								<div class="account-records">
                                    <div class="account-YH">
                                        <span id="account_ZHB " class="account_ZHB account_type">提取余额到支付宝</span>
                                        <span id="account_YHK" class="account_YHK account_type">提取余额到银行卡</span>
                                        <span style="position: absolute;margin-left: 270px;">余额 ：<strong class="money-numb">${balance}</strong>元</span>
                                    </div>
									<%--<div class="recharge-info">--%>
										<%--<div class="input-container">--%>
											<%--<label class="right-label">支付宝账号 ：</label>--%>
											<%--<input type="text" id="zhibubao-account" class="account-input"/>--%>
											<%--<span id="account-require" style="margin-left: 6px;" class="require-msg">*  输入支付宝账号</span>--%>
										<%--</div>--%>
										<%--<div class="input-container">--%>
											<%--<label class="right-label">提取金额 ：</label>--%>
											<%--<input type="text" id="withdraw-numb" class="account-input" onblur="showTips()"/>元--%>
											<%--<span id="withdraw-require" style="margin-left: 6px;" class="require-msg">*  输入提取金额</span>--%>
											<%--<span id="withdraw-msg">[实际提现金额：<span id="actual-withdraw"></span>元，提现需要2-3个工作日到账，推广期免手续费]</span>--%>
										<%--</div>--%>
										<%--<div class="input-container">--%>
											<%--<label class="right-label">再次输入登录密码 ：</label>--%>
											<%--<input type="password" id="account-pwd" class="account-input"/>--%>
											<%--<span id="pwd-require" style="margin-left: 6px;" class="require-msg">*  输入密码</span>--%>
										<%--</div>--%>
									<%--</div>--%>
                                    <!--银行卡-->
                                    <div class="recharge-info-YHK">
                                        <div class="input-container" id="accountdiv">
                                            <label class="right-label">支付宝账号 ：</label>
                                            <input type="text" id="zhibubao-account" class="account-input"/>
                                            <span id="account-require" style="margin-left: 6px;" class="require-msg">*  输入支付宝账号</span>
                                        </div>
                                        <div class="input-container" id="openbankdiv">
                                            <label class="right-label">开户行 ：</label>
                                            <input placeholder="例如：中国银行XX支行XX分行" type="text" id="bank-account" class="account-input"/>
                                            <span id="account-require-YH" style="margin-left: 6px;" class="require-msg">*  输入银行卡号</span>
                                        </div>
                                        <div class="input-container" id="bankaccountdiv">
                                            <label class="right-label">收款银行卡号 ：</label>
                                            <input type="text" id="ZH-account" class="account-input"/>
                                            <span id="account-require-ZH" style="margin-left: 6px;" class="require-msg">*  输入账户</span>
                                        </div>
                                        <div class="input-container">
                                            <label class="right-label">收款人姓名 ：</label>
                                            <input type="text" id="XM-account" class="account-input"/>
                                            <span id="account-require-XM" style="margin-left: 6px;" class="require-msg">*  输入姓名</span>
                                        </div>
                                        <div class="input-container">
                                            <label class="right-label">联系手机 ：</label>
                                            <input type="text" id="phone-account" class="account-input"/>
                                            <span id="account-require-SJ" style="margin-left: 6px;" class="require-msg">*  输入手机号码</span>
                                        </div>
                                        <div class="input-container">
                                            <label class="right-label">提取金额 ：</label>
                                            <input type="text" id="withdraw-numb" class="account-input" onblur="showTips()"/>元
                                            <span id="withdraw-require" style="margin-left: 6px;" class="require-msg">*  输入提取金额</span>
                                            <span id="withdraw-msg">[实际提现金额：<span id="actual-withdraw"></span>元，提现需要2-3个工作日到账，推广期免手续费]</span>
                                        </div>
                                        <div class="input-container">
                                            <label class="right-label">再次输入登录密码 ：</label>
                                            <input type="password" id="account-pwd" class="account-input"/>
                                            <span id="pwd-require" style="margin-left: 6px;" class="require-msg">*  输入密码</span>
                                        </div>
                                        <div class="input-container">
                                            <label class="right-label">身份证 ：</label>
                                            <input type="text" id="card-num" class="account-input"/>
                                            <span id="card-require" style="margin-left: 6px;" class="require-msg">*  输入身份证</span>
                                            <p class="input-P">
                                                <label>1.根据国家相关法律规定，需要您提供身份信息用于税务申报等合法用途。</label>
                                                <label>2.我们承诺对您的个人身份信息保密，不会用于非法活动。</label>
                                                <label>3.为了保证您的权益，请您准确填写个人身份信息，如因身份信息填写错误而
                                                        导致的任何损失或责任，上海复兰科技不予承担责任。</label>
                                            </p>

                                        </div>
                                    </div>
									<div class="with-L"><button id="withdraw-btn" class="confirm-btn" style="border-radius: 4px;margin-left: 180px;">确认</button></div>
								</div>

							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- right end-->
	</div>
	<div  style="clear: both">




<!-- 页尾 -->
<%@ include file="../common_new/foot.jsp" %>
<!-- 页尾 -->
<div id="class-dialog-content">
    <ul id="classTree" class="ztree dir-tree"></ul>
</div>
<div id="school-dialog-content">
    <ul id="schoolTree" class="ztree dir-tree"></ul>
</div>
</body>
</html>
