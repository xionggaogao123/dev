<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <head>
        <!-- Basic Page Needs-->
        <meta charset="utf-8">
        <link rel="dns-prefetch" href="//source.ycode.cn" />
        <title>复兰科技-学生评价系统</title>
        <meta name="description" content="">
        <meta name="author" content="" />
        <meta name="copyright" content="" />
        <meta name="keywords" content="">
        <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
        <!-- css files -->
        <!-- Normalize default styles -->
        <link href="/static_new/css/reset.css" rel="stylesheet" />
        <link rel="stylesheet" type="text/css" href="/static_new/css/indicator/evaluateResult.css">
        <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
        <!-- jquery artZoom4Liaoba styles -->
        <!-- Custom styles -->
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
    <!--.col-right-->
    <div class="col-right">
        <div class="evaluate-con">
            <div class="clearfix evaluate-head">
                <h3>学生评价结果</h3>
            </div>
            <div class="main" id="main1">
                <div class="evaluate-select">
                    <input id="name" name="name" value="">
                    <button id="searchBtn" name="searchBtn">查询评价</button>
                </div>
                <table class="evaluate-table">
                    <tr>
                        <th width="255">评价名称</th>
                        <th width="255">评价发起人</th>
                        <th width="255">日期</th>
                    </tr>
                    <tbody class="evaluate-data">

                    </tbody>
                </table>
                <script type="text/template" id="j-tmpl">
                    {{ if(it.rows.length>0){ }}
                    {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                    {{var obj=it.rows[i];}}
                    <tr>
                        <td width="255" class="evaluate-name" id="{{=obj.id}}">
                            <a title="{{=obj.name}}" class="evaluate-a">{{=obj.name}}</a>
                        </td>
                        <td width="255">
                            {{=obj.createrName}}
                        </td>
                        <td width="255">
                            {{=obj.createDate}}
                        </td>
                    </tr>
                    {{}}}
                    {{}}}
                </script>
                <!--.page-links-->
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
                <!--/.page-links-->
            </div>
        </div>
    </div>
    <!--/.col-right-->
    <!--/#content-->
</div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('evaluateResult',function(evaluateResult){
        evaluateResult.init();
    });
</script>
</body>
</html>