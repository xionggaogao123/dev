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
    <script type="text/javascript">
         var currentPageID = 11;
        var currentpage = 1;
        $(function(){
        	
        	getMyOrderList(1,10);
        });
        function getMyOrderList(page, size) {
        	$('#blogpicuploadLoading').show();
            $.ajax({
         	   url:'/emarket/myOrderList.do',
         	   data:{
                    "page": page,
                    "pageSize": size
                },
                dataType:'json',
                success:function(data){
                	$('#records-table tbody tr th').parent().siblings().html('');
             	   var html = '';
             	   var set = data.rows;
             	   var paytype = '';
             	   
             	   for(var i = 0; i < set.length; i++){
             		  if(set[i].payType == 0){
                 		  paytype = '未支付';
                 	   }else if(set[i].payType == 1){
                 		  paytype = '余额';
                 	   }else if(set[i].payType == 2){
                 		  paytype = '支付宝';
                 	   }
             		   var paymentStatus = '';
             		   var operation = '';
             		   if(set[i].state == 3){
             			  paymentStatus = '已付款';
             			  operation = '<div><button class="cancel" onclick="rebuyLesson(\''+ set[i].id +'\')">再次购买</button></div>';
             		   }else if(set[i].state == 1){
             			  paymentStatus = '未付款';
             			  operation = '<div><button class="confirm" onclick="buyLesson(\''+ set[i].id +'\')">立即购买</button></div><div><button class="cancel" onclick="cancelOrder(\''+ set[i].id +'\')">取消</button></div>';             		  
             	   	   }else if(set[i].state == 4){
             	   		  paymentStatus = '关闭交易';
             	   	   }
             		   html += '<tr><td>'+ set[i].orderid +'</td><td>'+ set[i].goodsInfo.value +'</td><td>'+ set[i].price +'</td><td>'+ paytype +'</td><td>'+ set[i].lastUpdateTime +'</td><td>'+ paymentStatus +'</td><td>'+ operation +'</td></tr>';
             	   }
             	   $('#records-table').append(html);
             	   $('#waitToPay').html(data.total - data.paycount);
             	   $('#alreadyPay').html(data.paycount);
             	   $('#allOrder').html(data.total);
             	   $('#records-table tbody tr:even').css("backgroundColor","#eee"); 
             	   $('#blogpicuploadLoading').hide();
             	   resetPaginator(Math.ceil(data.total/data.pageSize));
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
                	 getMyOrderList(page, 10);
                 }
             });
         }
         function cancelOrder(id){
        	 if(confirm("真的要取消订单?")){
        		 $.ajax({
            		 url:'/emarket/cancelOrder.do',
            		 data:{'id':id},
            		 success:function(data){
            			 if(data.resultCode == 0){
            				 window.location.href = '/emarket/myOrder.do';
            			 }else{
            				 alert('订单取消未成功!');
            			 }
            		 }
            	 });
        	 }
         }
         function buyLesson(id) {
            window.location.href='/emarket/selLessonOrder.do?orderId='+id;
         }

         function rebuyLesson(id) {
            $.ajax({
                 url: '/emarket/rebuyLesson.do',
                 type: 'post',
                 dataType: 'json',
                 data: {
                     orderId: id
                 }
             }).success(function(data) {
                 if (data.resultCode==0) {
                    window.location.href='/emarket/selLessonOrder.do?orderId='+data.newOrderId;
                } else {
                 alert("订单生成失败！请重新创建！");
                }
                 
             }).error(function() {
                 alert('服务器错误！');
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
                <div id="main-content" style="position: relative; overflow:hidden;width:auto;" class="main-content-msg">
                    <div id="account-right">
                        <div id="account-right-title" style="border-bottom:none; ">
                            <span><a class="help" href="/emarket/myAccountOrder.do">个人账户</a></span>
                            <span><a class="help selectedd" href="/emarket/myOrder.do">我的订单</a></span>
                            <span><a class="help" href="/emarket/myBalance.do">我要充值</a></span>
                            <span><a class="help" href="/emarket/withDraw.do">我要提现</a></span>
                            <span><a class="help" href="/emarket/myExpireLesson.do">过期课程</a></span>
                            <%if(role == 1 || role == 2 || role == 9){ %>
                            <span><a class="help" href="/emarket/saleList.do">销售订单</a></span>
                            <%} %>
                        </div>
                        <div id="account-right-content" class="account-right-content">
                            <div id="myaccount">
                                <h3>已购课程</h3>
                                <div class="account-records">
                                    <img src="/img/loading4.gif" id="blogpicuploadLoading" style="top: 50%; left: 50%;"/>
                                    <table id="records-table">
                                        <tr><th>订单ID</th><th>课程名称</th><th>订单金额</th><th>支付方式</th><th>下单时间</th><th>订单状态</th><th style="width:38px;">操作</th></tr>
                                    </table>
                                    <div style="float: right;margin-top: 18px;font-weight: 800;">
                                        <span class="info-strong">等待付款订单：<span id="waitToPay"></span></span><span class="info-strong">已付款订单<span id="alreadyPay"></span></span><span class="info-strong">订单总数：<span id="allOrder"></span></span></div>
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
    <div style="clear: both">




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
