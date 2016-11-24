<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">

    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="/static_new/css/interactlesson/teacher_hudong.css?v=2015082601" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
</head>
<body>


<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->


	
<!--#content-->
<div id="content" class="clearfix">

    <!--.col-left-->
   <%@ include file="../common_new/col-left.jsp" %>
    <!--/.col-left-->

    <!--.col-right-->
    <div class="col-right">
       <img alt="" src="/images/no_content.png">
    </div>
    <!--/.col-right-->


</div>
<!--/#content-->



<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041605"></script>
<script>
    seajs.use('interactlesson',function(interactlesson){
    	interactlesson.init1();
    });
</script>


</body>
</html>