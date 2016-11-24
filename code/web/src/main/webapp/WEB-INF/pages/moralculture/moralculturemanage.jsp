<%--
  Created by IntelliJ IDEA.
  User: guojing
  Date: 2015/7/6
  Time: 11:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>德育-项目管理</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="/static_new/css/moralculture.css?v=2015041602" rel="stylesheet" />
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

    <%@ include file="../common/right.jsp" %>

    <!--.col-right-->
    <div class="col-right">
        <div class="tou">
            <h4>项目管理</h4>
            <a id="addProject" href="javascript:;">新建项目</a>
            <!-- 弹出层 -->
            <div class="gay"></div>
            <div class="xjxm">
                <h6><span id="winTitle">添加项目</span><span class="gb"></span></h6>
                <div class="xjxm_main clearfix">
                    <input id="projectId" name="projectId" type="hidden" value="">
                    <p>
                        <span>项目名称</span>
                        <input id="moralCultureName" name="moralCultureName" type="text" value="">
                    </p>
                    <button id="saveProject">确定</button>
                </div>
            </div>
            <!-- 弹出层 -->
        </div>
        <!--.sub-info-list-->
        <div class="sub-info-list">

        </div>
        <!--.sub-info-list-->
        <!--.list-info-->
        <script type="text/template" id="j-tmpl">
            {{ if(it.list.length>0){ }}
                <ul class="pf">
                    {{ for (var i = 0, l = it.list.length; i < l; i++) { }}
                        {{var obj=it.list[i];}}
                        <li id="{{=obj.id}}">
                            <h6>{{=obj.moralCultureName}}</h6>
                            <a href="javascript:;" class="edit"><img src="/static_new/images/pencil.jpg" height="11" width="13" alt=""></a>
                            <a href="javascript:;" class="del"><img src="/static_new/images/trash.jpg" height="11" width="11"alt=""></a>
                        </li>
                    {{ } }}
                </ul>
            {{}else{ }}
            <!-- 暂无记录 当.info-list没有的时候会用此替换掉-->
            <div class="record">暂无记录</div>
            <!-- 暂无记录 -->
            {{ } }}
        </script>
    </div>
    <!--/.col-right-->

</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('moralculturemanage',function(moralculturemanage){
        moralculturemanage.init();
    });
</script>
</body>
</html>
