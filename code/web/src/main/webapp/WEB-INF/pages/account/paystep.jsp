<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html>
<html>
<head>
<title>课程支付</title>
<meta charset="utf-8"/>
<link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
<link rel="stylesheet" href="/static/css/bootstrap-combined.school.css"/>
<link rel="stylesheet" href="/static/css/excellentLesson.css"/>
<script type="text/javascript" src="/static/js/jquery.min.js"></script>
<script src="/static/js/bootstrap-paginator.min.js" type="text/javascript"></script>
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
var currentCommPage = 1;
var sequence = 0;
var CurrentUId = '${currentUser.id}';
var role = '${currentUser.role}';
var myBalance = ${balance};
var payAmount = ${price};
var paySum = 0;
$(function() {
	
	if(myBalance <= 0 && payAmount > 0){
		$('#useBalance').attr('disabled','disabled');
	}else if(myBalance <= 0 && payAmount == 0){
		$('#useBalance').attr('disabled',false);
		$('.paytypelist').hide();
	}
	if (payAmount == 0) {
		$('#useBalance').attr('disabled',false);
		$('.paytypelist').hide();
	}
	$('#useBalance').on('click', function() {
		if($(this).prop('checked')){//使用余额支付
			$('#balancePay').show();
			$(this).parent().addClass('select-pay');
			if(myBalance >= payAmount){
				$('#balancePay .money-numb').html('${price}');
				$('#zhifubao').attr('disabled','disabled');
				$('#zfbPay').hide();
				$('#zhifubao').parent().removeClass('select-pay');
				$('#zhifubao').prop('checked',false);
				paySum = payAmount;
				//支付宝
				$('#zhifubao').prop('checked',false);
				$('#zfbPay').hide();
			}else{
				$('#balancePay .money-numb').html('${balance}');
				
				paySum = myBalance;
				//支付宝
				$('#zhifubao').removeAttr('disabled');
				if($('#zhifubao').prop('checked')){
					$('#zfbPay .money-numb').html(payAmount-myBalance);
				}
			}
		}else{
			$('#balancePay').hide();
			$(this).parent().removeClass('select-pay');
			$('#zhifubao').removeAttr('disabled');
			paySum = 0;
			//支付宝
			$('#zfbPay .money-numb').html(payAmount);
		}
		checkAvailable();
	});
	
	$('#zhifubao').click(function(){
		if($(this).prop('checked')){
			//支付宝
			$('#zfbPay').show();
			var zfbShowMoney = payAmount - paySum;
			if((zfbShowMoney+'').indexOf('.') < 0 ){
				zfbShowMoney = zfbShowMoney +'.0';
			}
			$('#zfbPay .money-numb').html(zfbShowMoney);
			$(this).parent().addClass('select-pay');
		}else{
			$('#zfbPay').hide();
			$(this).parent().removeClass('select-pay');
		}
		checkAvailable();
		
	});
	$('#pay-next').click(function(){
		if($(this).prop('className') == 'confirm-btn'){
			if ($('#zhifubao').prop('checked')||$('#useBalance').prop('checked')) {
				var money1 = 0;
				var money2 = 0;
				if ($('#zhifubao').prop('checked')) {
					money2 = $('#money2').html();
				}
				if ($('#useBalance').prop('checked')) {
					money1 = $('#money1').html();
				}
				if ($('#zhifubao').prop('checked')) {
					 var url = '/pay/fzpaypal.do';
				    $('#post-pay-dialog, #bg1').show();
				    //var f = document.getElementById('online-excellent-lessonpay-form');
				    var f = document.createElement("form");
				    var orderid = document.createElement('input');
				    var account = document.createElement('input');
				    var alipay = document.createElement('input');
				    var lessonName = document.createElement('input');
				    
				    orderid.setAttribute('name','orderid');
				    account.setAttribute('name','account');
				    alipay.setAttribute('name','alipay');
				    lessonName.setAttribute('name','lessonName');
				    
				    f.setAttribute('action',url);
				    f.setAttribute('target','_blank');
				    f.setAttribute('method','post');
				    
				    orderid.value = "${orderId}";
				    account.value = money1;
				    alipay.value = money2;
				    lessonName.value = "${lessonName}";
				    
				    document.body.appendChild(f);
				    f.appendChild(orderid);
				    f.appendChild(account);
				    f.appendChild(alipay);
				    f.appendChild(lessonName);
				    
				    f.submit();
				    $('.pay-success').attr('href', '/business/user/onlinepayResult.jsp?oid=' + data.orderId);
				} else{
					$('#myaccount').hide();
					$('#myaccountnext').show();
				}
			} else {
				alert("请先选择支付方式！");
			}
		}
	});

	$('#withdraw-btn').click(function(){
		if ($.trim($('#account-pwd').val()).length==0) {
			alert("请输入支付密码！");
		} else {
			$.ajax({
                url: '/emarket/payLessonByBlance.do',
                type: 'post',
                dataType: 'json',
                data: {
                    password: $.trim($('#account-pwd').val()),
                    orderId: '${orderId}'
                }
            }).success(function(data) {
                if (data.resultCode==0) {
                   window.location.href = '/emarket/orderFinish.do?lessonName=${lessonName}';
               } else if(data.resultCode==2){
            	   $('#pwd-error').show();
               }else{
                   //window.location.href = '/emarket/orderFail.do?lessonName=${lessonName}';
                   alert(data.mesg);
               }
                
            }).error(function() {
                alert('服务器错误！');
            });
		}
		
	});
	
	function checkAvailable(){
		var balance = 0;
		var zhifubao = 0;
		if(!$('#balancePay').is(":hidden")){
			balance = $('#money1').html();
		}
		if(!$('#zfbPay').is(":hidden")){
			zhifubao = $('#money2').html();
		}
		var total = parseFloat(balance) + parseFloat(zhifubao);
		if(total == payAmount){
			$('#pay-next').removeClass('not-allow').addClass('confirm-btn');
		}else{
			$('#pay-next').addClass('not-allow').removeClass('confirm-btn');
		}
	}
});
</script>
</head>
<body csid="<% if(request.getParameter("courseId")!=null){out.println(java.net.URLDecoder.decode(request.getParameter("courseId"),"UTF-8")); }%>">

<%@ include file="../common_new/head.jsp" %>
<div id="content_main_container">
	<div id="content_main">
		<!-- 左侧导航-->
		<%@ include file="../common_new/col-left.jsp" %>
		<!-- left end -->
		<!-- right start-->
		<div id="right-container">
			<div id="content_main_container" style="margin-top: 5px;">
				<div id="main-content" style="position: relative; overflow:hidden;background:#fff;margin-bottom: 50px;">
					<div id="account-right">
						<div id="account-right-content" class="account-right-content">
							<div id="myaccount">
								<h3>课程清单</h3>
								<div class="account-records">
									<table id="records-table">
										<tr><th>订单ID</th><th>课程名称</th><th>下单时间</th><th>有效期</th><th>价格</th></tr>
										<tr><td>${orderId}</td><td>${lessonName}</td><td>${createdate}</td><td>${expiretime}</td><td>${price}</td></tr>
									</table>
									<div style="float: right;margin-top: 18px;font-weight: 800;min-width: 180px;overflow: hidden;"><span>实付款:</span><span class="pricelabel money-numb">￥${price}</span></div>
								</div>
								<h3>支付方式</h3>
								<div class="account-records">
									<div class="recharge-info">
										<div class="recharge-info">
											<div class="account-balance">
												<input type="checkbox" id="useBalance" style="vertical-align: top;margin-right: 5px;"/><span id="balance">我的账户余额：<strong class="money-numb">${balance}</strong>元</span>
												<span id="balancePay" class="pay-item">支付<strong class="money-numb" id="money1"></strong>元</span>
											</div>
										</div>
										<div class="recharge-type" style="overflow: visible">
											<ul class="paytypelist">
												<li class="paytypeitem"><input type="checkbox" id="zhifubao" value="0"><img class="type-icon" src="/img/zhifubao.png"/>
													<span id="zfbPay" class="pay-item">支付<strong class="money-numb" id="money2"></strong>元</span></li>
											</ul>
										</div>
									</div>
								</div>
								<div style="float:right;"><button id="pay-next" class="not-allow">下一步</button></div>
								<div style="clear:both;"></div>
							</div>
							<div id="myaccountnext" style="display: none">
								<h3 class="ellipsis">${lessonName}</h3>
								<div class="account-records">
									<div class="recharge-info">
										<div class="input-container"><label class="right-label">付款方式 ：我的账户可支付${balance}元</label><span style="float: right;width: 50%;">支付<strong class="money-numb">${price}</strong>元</span></div>
										<div class="input-container"><label class="right-label">再次输入登录密码 ：</label><input type="password" id="account-pwd" class="account-input"/><span id="pwd-error" class="require-msg">密码错误!</span></div>
									</div>
									<div style="float:right;width: 540px"><button id="withdraw-btn" class="confirm-btn" style="border-radius: 4px;">确认</button></div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- right end-->
	</div>
	<div>

<!-- </form> -->
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
      <a class='pay-success' href="/excellentLesson/order">
        <span style="width:75px;height:25px;color:#fff;background:#ff7625;line-height:25px;font-weight:bold;font-size:15px;margin-left:10px;">支付完成
        </span>
      </a>
      <a href="/excellentLesson/order">
        <span style="width:110px;height:25px;color:#fff;background:#9d9d9d;line-height:25px;font-weight:bold;font-size:15px;margin-left:10px;">支付遇到问题
        </span>
      </a>
    </div>
  </div>
</div>
<div id="bg1" class="bg" style="display: none;"></div>
</div>
<%@ include file="../common_new/foot.jsp" %>
</body>
</html>