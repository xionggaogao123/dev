<%--
  Created by IntelliJ IDEA.
  User: Tony
  Date: 2015/4/13
  Time: 10:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>平台功能统计明细</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <link rel="stylesheet" type="text/css" href="/static_new/css/managecount/managecount3.css">
    <link rel="stylesheet" type="text/css" href="/static_new/css/dialog.css">
    <link href="/static/css/homepage.css" type="text/css" rel="stylesheet">
</head>
<body>

<!--=================================引入头部============================================-->
<%@ include file="../common_new/head.jsp" %>
<div style="width:1200px; margin: 0 auto; overflow: hidden; ">
    <!--=============================引入左边导航=======================================-->
    <%@ include file="../common_new/col-left.jsp" %>
    <div class="right">
        <div class="right_top">
            <p>
                <a href="/managetotal/${schoolId}" >平台使用统计</a><a href="javascript:;" style="width:130px;"  class="current">平台功能统计</a>
            </p>
            <input type="hidden" id="operateUserId" name="operateUserId" value="${operateUserId}">
            <input type="hidden" id="role" name="role" value="${role}">
            <input type="hidden" id="funId" name="funId" value="${funId}">
            <input type="hidden" id="userName" name="userName" value="${userName}">
            <input type="hidden" id="funName" name="funName" value="${funName}">
            <input type="hidden" id="dateStart" name="dateStart" value="${dateStart}">
            <input type="hidden" id="dateEnd" name="dateEnd" value="${dateEnd}">
        </div>
        <div class="right_bottom">
            <ul id="mainUl">
            </ul>
        </div>
        <div class="page-paginator">
            <span class="first-page">首页</span>
                        <span class="page-index">
                            <span class="active">1</span>
                            <span>2</span>
                            <span>3</span>
                            <span>4</span>
                            <span>5</span>
                            <i>···</i>
                        </span>
            <span class="last-page">尾页</span>
        </div>
    </div>
</div>
<div style="clear: both"></div>
<%@ include file="../common_new/foot.jsp" %>
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('funUseDetail',function(funUseDetail){
        funUseDetail.init();
    });
</script>
</body>
</html>



