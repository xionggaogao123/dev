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
    <link rel="stylesheet" href="/static/css/flippedclassroom.css"/>
    <link rel="stylesheet" href="/static/css/dialog.css"/>
    <link rel="stylesheet" href="/static/css/excellentLesson.css"/>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/cloudclass.css"/>
    <!-- <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet"> -->
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script src="/static/js/ztree/jquery.ztree.all-3.5.min.js"></script>
    <script src="/static/js/bootstrap-paginator.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="/static/js/swfobject.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/js/cloudclass.js"></script>
    <script type="text/javascript" src="/static/js/flippedcoursemaintain.js"></script>
    <script type="text/javascript" src="/static/js/infopet.js"></script>
    <script type="text/javascript" src="/static/js/lessons/coursesManage.js"></script>
    <script type="text/javascript" src="/static/js/experienceScore.js"></script>
    <script type="text/javascript" src="/static/js/WdatePicker.js"></script>
    <style type="text/css">
    .Wdate{
		height:28px !important;
	}
    </style>
    <script type="text/javascript">
         var currentPageID = 11;
        var currentpage = 1;
        $(function(){
        	getSaleOrderList(1,20);
        });
        function getSaleOrderList(page, size) {
        	var selectStatus = $('#orderstate').val();
        	$('#blogpicuploadLoading').show();
            $.ajax({
         	   url:'/emarket/saleListInfo.do',
         	   data:{
                    "page": page,
                    "pageSize": size,
                    "lessonname":$('#coursename').val(),
                    "username":$('#customername').val(),
                    "starttime":$('#dealstarttime').val(),
                    "endtime":$('#deal-endtime').val(),
                    "paymentStatus":$('#orderstate').val()
                },
                type:'post',
                dataType:'json',
                success:function(data) {
					$('#records-table tbody tr th').parent().siblings().html('');
					var html = '';
					var set = data.rows;
					var paytype = '';
					//分别统计个数
					var count_1 = 0;
					var count_2 = 0;
					var count_3 = 0;
					if (set != null) {

						for (var i = 0; i < set.length; i++) {
							if (set[i].payType == 0) {
								paytype = '未支付';
							} else if (set[i].payType == 1) {
								paytype = '余额';
							} else if (set[i].payType == 2) {
								paytype = '支付宝';
							}
							var operation = '';
							if (set[i].state == 1) {
								operation = '未付款';
								count_1 += 1;
							} else if (set[i].state == 3) {
								operation = '交易完成';
								count_2 += 1;
							} else if (set[i].state == 4) {
								operation = '关闭交易';
								count_3 += 1;
							}
							html += '<tr><td>' + set[i].id + '</td><td>' + set[i].goodsInfo.value + '</td><td>' + set[i].price + '</td><td>' + paytype + '</td><td>' + set[i].lastUpdateTime + '</td><td>' + set[i].userInfo.value + '</td><td>' + operation + '</td></tr>';
						}
						$('#records-table').append(html);
						$('#waitToPay').html(count_1);
						$('#alreadyPay').html(count_2);
						$('#closeDeal').html(count_3);
						$('#allOrder').html(data.total);
						if (selectStatus == '0') {
							$('#waitToPay').parent().show();
							$('#waitToPay').parent().siblings().hide();
						} else if (selectStatus == '1') {
							$('#alreadyPay').parent().show();
							$('#alreadyPay').parent().siblings().hide();
						} else if (selectStatus == '2') {
							$('#closeDeal').parent().show();
							$('#closeDeal').parent().siblings().hide();
						} else {
							$('#statistics').children().show();
						}
						$('#records-table tbody tr:even').css("backgroundColor", "#eee");
					}
					$('#blogpicuploadLoading').hide();
					resetPaginator(Math.ceil(data.total / data.pageSize));
				}
            });
         }
         function resetPaginator(totalPages) {
             if (totalPages <= 0) {
                 totalPages = 1;
             }
             $('#example').bootstrapPaginator("setOptions", {
                 currentPage: currentpage,
                 totalPages: totalPages,
                 itemTexts: function (type, page, current) {  
                     switch (type) {  
                         case "first":  
                             return "首页";  
                         case "prev":  
                             return "<";  
                         case "next":  
                             return ">";  
                         case "last":  
                             return "末页"+page;  
                         case "page":  
                             return  page;  
                     }                 
                 },
                 onPageClicked: function(e, originalEvent, type, page) {
                	 currentpage = page;
                	 getSaleOrderList(page, 20);
                 }
             });
         }
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
                            <span><a class="help" href="/emarket/withDraw.do">我要提现</a></span>
                            <span><a class="help" href="/emarket/myExpireLesson.do">过期课程</a></span>
                            <%if(role == 1 || role == 2 || role == 9){ %>
                            <span><a class="help selectedd" href="/emarket/saleList.do">销售订单</a></span>
                            <%} %>
						</div>
						<div id="account-right-content" class="account-right-content">
							<div id="myaccount">
								<h3>销售课程</h3>
								<div class="account-records">
									<div>
										<div class="input-container">
											<label class="right-label min-label" for="coursename">课程名称:</label>
											<input id="coursename" class="account-input" type="text" placeholder="">
										</div>
										<div class="input-container">
											<label class="right-label min-label" for="deal-starttime">成交时间:</label>
											<input id="dealstarttime" class="Wdate account-input" type="text"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" value=""/>
											<label class="right-label min-label" for="deal-starttime">到</label>
											<input id="deal-endtime" class="Wdate account-input" type="text"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'dealstarttime\')}'})" value="">
										</div>
										<div class="input-container">
											<label class="right-label min-label" for="customername">买家昵称:</label>
											<input id="customername" class="account-input" type="text" placeholder="">
											<label class="right-label min-label" for="orderstate">订单状态:</label>
											<select class="account-input" id="orderstate">
												<option value="0">全部</option>
												<option value="1">未付款</option>
												<option value="4">关闭交易</option>
												<option value="3">交易完成</option>
											</select>
											<button id="search-salelist" onclick="getSaleOrderList(1,20)">搜索</button>
										</div>

									</div>
									<img src="/img/loading4.gif" id="blogpicuploadLoading" style="top: 50%; left: 50%;"/>
									<table id="records-table">
										<tr><th>订单ID</th><th>课程名称</th><th>订单金额</th><th>支付方式</th><th>成交时间</th><th>买家昵称</th><th>订单状态</th></tr>
									</table>
									<div style="float: right;margin-top: 18px;font-weight: 800;" id="statistics">
										<span class="info-strong">等待付款订单：<span id="waitToPay"></span></span>
										<span class="info-strong">已付款订单:<span id="alreadyPay"></span></span>
										<span class="info-strong">关闭交易:<span id="closeDeal"></span></span>
										<span class="info-strong">订单总数：<span id="allOrder"></span></span>
									</div>
								</div>
								<div class='center-container' style="margin-top: 30px; margin-bottom: 30px; text-align:center;overflow:hidden;">
									<div id="example" style="width:500px"></div>
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
