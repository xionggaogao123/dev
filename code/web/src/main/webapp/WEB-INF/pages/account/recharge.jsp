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
    <style type="text/css">
        .mydiv {
            text-align: center;
            font-size: 9pt;
            z-index: 10;
            width: 500px;
            height: 300px;
            left: 50%;
            top: 50%;
            margin-left: -250px !important;
            margin-top: -150px !important;
            margin-top: 0px;
            position: fixed !important;
            position: absolute;
            _top: expression(eval(document.compatMode &&
            document.compatMode=='CSS1Compat') ?
            documentElement.scrollTop + (document.documentElement.clientHeight-this.offsetHeight)/2 :/*IE6*/
            document.body.scrollTop + (document.body.clientHeight - this.clientHeight)/2); /*IE5 IE5.5*/
        }

        .SContent-box {
            width: 440px;
            height: 230px;
            background: #fff;
            overflow: hidden;
        }

        .bg {
            background-color: #000;
            width: 100%;
            height: 100%;
            left: 0;
            top: 0;
            filter: alpha(opacity=40);
            opacity: 0.4;
            z-index: 1;
            position: fixed !important; /*FF IE7*/
            position: absolute;
            _top: expression(eval(document.compatMode && document.compatMode=='CSS1Compat') ?
            documentElement.scrollTop + (document.documentElement.clientHeight-this.offsetHeight)/2 :/*IE6*/
            document.body.scrollTop + (document.body.clientHeight - this.clientHeight)/2); /*IE5 IE5.5*/
        }
    </style>
    <script type="text/javascript">
        var currentPageID = 11;
        $(function(){
        	$('#records-table tbody tr:even').css("backgroundColor","#eee");
        });
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
                            <span><a class="help selectedd" href="/emarket/myBalance.do">我要充值</a></span>
                            <span><a class="help" href="/emarket/withDraw.do">我要提现</a></span>
                            <span><a class="help" href="/emarket/myExpireLesson.do">过期课程</a></span>
                            <%if(role == 1 || role == 2 || role == 9){ %>
                            <span><a class="help" href="/emarket/saleList.do">销售订单</a></span>
                            <%} %>
                        </div>
                        <div id="account-right-content" class="account-right-content">
                            <!-- <form id="online-excellent-pay-form" target="_blank" method="post"> -->
                            <!-- <input type="hidden" name="orderid" value="" /> -->
                            <div id="myaccount">
                                <h3>账户记录</h3>
                                <div class="account-records">
                                    <div class="recharge-info">
                                        <label>账户余额 ：</label><label><strong class="money-numb">${balance}</strong>元</label><input/>
                                        <label>充值金额 ：</label><input type="text" placeholder="请输入充值金额" id="recharge-numb"/>元
                                        <span id="recharge-require" style="margin-left: 6px;" class="require-msg">*  充值金额必须大于0.01</span>
                                    </div>
                                    <div class="recharge-type" style="overflow: visible">
                                        <ul>
                                            <li><input type="radio" id="paytype" value="0" checked="checked"><img class="type-icon" src="/img/zhifubao.png"/></li>
                                        </ul>
                                    </div>
                                </div>
                                <div style="float:right;"><button id="recharge-btn" class="confirm-btn">确认信息</button></div>
                            </div>
                            <!-- </form> -->
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- right end-->
    </div>
    <div style="clear: both">


<div id="post-pay-dialog" class="mydiv" style="display: none;">
  <div style="width:440px;height:40px;background:#546fb4;">
    <span style="font-size:16px;color:#fff;float:left;margin-left:20px;margin-top:10px;font-weight:bold;">网上支付提示</span>
  </div>
  <div class="SContent-box" style="height:150px;">
    <div style="float:left;margin-left:30px;margin-top:20px;overflow:visible">
      <i class="fa fa-spinner fa-spin fa-4x" style="vertical-align: middle; color:skyblue"></i>
    </div>
    <div style="font-family:Microsoft YaHei;margin-top:28px;font-size:14px;overflow:hidden;">
      <span style="align:left;margin-left:-40px;">支付完成后，请不要关闭支付验证窗口。</span><br>
      <span style="margin-top:5px;">&nbsp;&nbsp;&nbsp;&nbsp;支付完成后，请根据您支付的情况点击下面按钮。</span>
    </div>
    <br><br>
    <div>
      <a class='pay-success' href="/emarket/myAccountOrder.do">
        <span style="width:75px;height:25px;color:#fff;background:#ff7625;line-height:25px;font-weight:bold;font-size:15px;margin-left:10px;">支付完成
        </span>
      </a>
      <a href="/emarket/myBalance.do">
        <span style="width:110px;height:25px;color:#fff;background:#9d9d9d;line-height:25px;font-weight:bold;font-size:15px;margin-left:10px;">支付遇到问题
        </span>
      </a>
    </div>
  </div>
</div>
<div id="bg1" class="bg" style="display: none;"></div>
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
