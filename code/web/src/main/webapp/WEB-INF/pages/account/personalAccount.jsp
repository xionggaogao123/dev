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
        	getAccountOrderList(1,10);
        });
        function getAccountOrderList(page, size) {
           $.ajax({
        	   url:'/emarket/accountOrderList.do',
        	   data:{
                   "page": page,
                   "pageSize": size
               },
               dataType:'json',
               success:function(data){
            	   $('#records-table tbody tr th').parent().siblings().html('');
            	   var html = '';
            	   var recharge = '-';
            	   var expense = '-';
            	   var set = data.rows;
            	   for(var i = 0; i < set.length; i++){
            		   var paytype;
            		   if(set[i].price > 0){
            			   expense = '-';
            			   recharge = set[i].price;
            			   paytype = '支付宝';
            		   } else {
            			   recharge = '-';
            			   expense = set[i].price;
            			   paytype = '提现';
            		   }
            		   /* if(set[i].payType == 0){
            			   paytype = '支付宝';
            		   } */
            		   html += '<tr><td>'+ set[i].lastUpdateTime +'</td><td>'+ paytype +'</td><td>'+ recharge +'</td><td>'+ expense +'</td><td>'+ set[i].id +'</td></tr>';
            	   }
            	   $('#records-table').append(html);
            	   $('#records-table tbody tr:even').css("backgroundColor","#eee");
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
                	getAccountOrderList(page, 10);
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

<div id="content_main_container"style="position:relative;">
    <div id="content_main">
        <!-- 左侧导航-->
        <%@ include file="../common_new/col-left.jsp" %>
        <!-- left end -->
        <!-- right start-->
        <div id="right-container">
            <div id="content_main_containerr" style="/*margin-top: 5px;*/">
                <div id="main-content" style="position: relative; overflow:hidden;width: auto;" class="main-content-msg">
                    <div id="account-right">
                        <div id="account-right-title" style="border-bottom:none; ">
                            <span><a class="help selectedd" href="/emarket/myAccountOrder.do">个人账户</a></span>
                            <span><a class="help" href="/emarket/myOrder.do">我的订单</a></span>
                            <span><a class="help" href="/emarket/myBalance.do">我要充值</a></span>
                            <span><a class="help" href="/emarket/withDraw.do">我要提现</a></span>
                            <span><a class="help" href="/emarket/myExpireLesson.do">过期课程</a></span>
                            <%if(role == 1 || role == 2 || role == 9){ %>
                            <span><a class="help" href="/emarket/saleList.do">销售订单</a></span>
                            <%} %>
                        </div>
                        <div id="account-right-content" class="account-right-content">
                            <div id="myaccount">
                                <h3>我的账户</h3>
                                <div class="account-balance select-pay">
                                    <span id="balance">您的账户余额：<strong class="money-numb">${balance}</strong>元</span>
                                    <a href="/emarket/myBalance.do" style="float:right;">账号充值</a>
                                </div>
                                <h3>账户记录</h3>
                                <div class="account-records">
                                    <table id="records-table">
                                        <tr><th>时间</th><th>类型</th><th>充值金额</th><th>消费金额</th><th>订单ID</th></tr>
                                    </table>
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
    <div style="clear: both;">




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
