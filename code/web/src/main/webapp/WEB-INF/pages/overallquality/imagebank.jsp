<%--
  Created by IntelliJ IDEA.
  User: guojing
  Date: 2016/8/24
  Time: 11:37
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <!-- Basic Page Needs-->
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>杨小综合素质评价</title>
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
    <link rel="stylesheet" type="text/css" href="/static_new/css/overallquality/yangxiao.css"/>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
</head>
<body>

<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div class="bg-wrap">
    <div class="yx-con">
        <div class="yx-head clearfix">
            <ul>
                <li id="XXYH" class="m-active"><a href="javascript:;">形象银行</a></li>
                <li id="XXCS"><a href="/qualityitem/imageMarket.do" >形象超市</a></li>
                <li id="WMBL"><a href="/qualityitem/qualityItemMge.do">文明部落</a></li>
            </ul>
        </div>
        <div class="tab-main">
            <!--================================形象银行start========================================-->
            <div class="xxyh-con" id="tab-XXYH">
                <c:if test="${roles:isStudent(sessionValue.userRole)}">
                    <div class="clearfix">
                        <div class="score-left">
                            <span>${gradeName}</span>
                            <span>${className}</span>
                        </div>
                    </div>
                </c:if>
                <ul class="yx-tab clearfix">
                    <c:if test="${roles:isStudent(sessionValue.userRole)}">
                        <li id="XXCXD" class="yx-active"><a href="javascript:;">形象储蓄单</a><em></em></li>
                    </c:if>
                    <c:if test="${roles:isNotStudentAndParent(sessionValue.userRole)}">
                        <li id="YHGL" class="yx-active"><a href="javascript:;">银行管理</a><em></em></li>
                    </c:if>
                </ul>
                <div class="xxyh-main">
                    <!--================================形象储蓄单start========================================-->
                    <div class="xxcxd-con" id="tab-XXCXD">
                        <input id="curDate" name="curDate" type="hidden" value="${curDate}">
                        <input id="refDate" name="refDate" type="hidden" value="${refDate}">
                        <div class="yx-title clearfix">
                            <div class="yx-title-left">
                                <span>你已获得：</span>
                                <span>努力学习币：<em>${dto.nlxxCount}</em></span>
                                <span>热情参与币：<em>${dto.rqcyCount}</em></span>
                                <span>岗位职责币：<em>${dto.gwzzCount}</em></span>
                                <span>遵纪明理币：<em>${dto.zjmlCount}</em></span>
                                <span>阳光可爱币：<em>${dto.ygkaCount}</em></span>
                                <span>共计:<em>${dto.hideTotalCount}</em></span>
                            </div>
                            <div class="yx-title-right">
                                <ul class="clearfix">
                                    <li class="prev"><a href="javascript:;">上周</a></li>
                                    <li class="curr title-active"><a href="javascript:;">本周</a></li>
                                    <li class="next"><a href="javascript:;">下周</a></li>
                                </ul>
                            </div>
                        </div>
                        <table class="yxTable">
                            <thead id="yxTheadS">
                            </thead>
                            <tbody id="yxTbodyS">
                            </tbody>
                            <script type="text/template" id="j-tmpl3">
                                {{ for(var i in it) { }}
                                {{ var obj=it[i];}}
                                <tr>
                                    <td style="background:#ececec;color:#3999d5;">{{=obj.currencyName}}</td>
                                    {{ for (var j = 0, l = obj.coqiList.length; j < l; j++) {}}
                                    {{ var obj1=obj.coqiList[j];}}
                                    <td>
                                        {{ if(obj1.flag=='0'){ }}
                                        {{ if(obj.currencyType==1){ }}
                                        <span class="yx-nlxx"></span>
                                        {{}}}
                                        {{ if(obj.currencyType==2){ }}
                                        <span class="yx-rqcy"></span>
                                        {{}}}
                                        {{ if(obj.currencyType==3){ }}
                                        <span class="yx-gwzz"></span>
                                        {{}}}
                                        {{ if(obj.currencyType==4){ }}
                                        <span class="yx-zjml"></span>
                                        {{}}}
                                        {{ if(obj.currencyType==5){ }}
                                        <span class="yx-ygka"></span>
                                        {{}}}
                                        <br>
                                        <span>{{=obj.currencyName}}</span>
                                        {{}}}
                                        {{ if(obj1.flag=='1'){ }}
                                        &nbsp;
                                        {{}}}
                                    </td>
                                    {{ } }}
                                </tr>
                                {{ } }}
                            </script>

                        </table>
                    </div>
                    <!--================================形象储蓄单end========================================-->
                    <script type="text/template" id="j-tmpl">
                        <tr>
                            <th style="width:20%;">币种/日期</th>
                            {{ for(var i in it) { }}
                            <th style="width:16%;">{{=it[i]}}</th>
                            {{ } }}
                        </tr>
                    </script>
                    <!--================================银行管理start========================================-->
                    <div class="yhgl-con" id="tab-YHGL">
                        <div class="yx-title clearfix">
                            <div class="yx-title-left">
                                <select id="selGrade">
                                    <c:forEach items="${gradelist}" var="grade">
                                        <option value="${grade.id}">${grade.name}</option>
                                    </c:forEach>
                                </select>
                                <select id="selClass">
                                </select>
                                <select id="selStu">
                                </select>
                            </div>
                            <div class="yx-title-right">
                                <ul class="clearfix">
                                    <li class="prev"><a href="javascript:;">上周</a></li>
                                    <li class="curr title-active"><a href="javascript:;">本周</a></li>
                                    <li class="next"><a href="javascript:;">下周</a></li>
                                </ul>
                            </div>
                        </div>
                        <table class="yxTable">
                            <thead id="yxTheadT">

                            </thead>
                            <tbody id="yxTbodyT">

                            </tbody>
                            <script type="text/template" id="j-tmpl2">
                                {{ for(var i in it) { }}
                                {{ var obj=it[i];}}
                                <tr currencyType="{{=obj.currencyType}}">
                                    <td style="background:#ececec;color:#3999d5;">{{=obj.currencyName}}</td>
                                    {{ for (var j = 0, l = obj.coqiList.length; j < l; j++) {}}
                                    {{ var obj1=obj.coqiList[j];}}
                                    <td class="operate" id="{{=obj1.id}}" flagDate="{{=obj1.flagDate}}" flag="{{=obj1.flag}}">
                                        {{ if(obj1.flag=='0'){ }}
                                            {{ if(obj.currencyType==1){ }}
                                            <span class="yx-nlxx"></span>
                                            {{}}}
                                            {{ if(obj.currencyType==2){ }}
                                            <span class="yx-rqcy"></span>
                                            {{}}}
                                            {{ if(obj.currencyType==3){ }}
                                            <span class="yx-gwzz"></span>
                                            {{}}}
                                            {{ if(obj.currencyType==4){ }}
                                            <span class="yx-zjml"></span>
                                            {{}}}
                                            {{ if(obj.currencyType==5){ }}
                                            <span class="yx-ygka"></span>
                                            {{}}}
                                            <br>
                                            <span>{{=obj.currencyName}}</span>
                                        {{}}}
                                        {{ if(obj1.flag=='1'){ }}
                                        <span class="table-add">+</span>
                                        {{}}}
                                    </td>
                                    {{ } }}
                                </tr>
                                {{ } }}
                            </script>
                        </table>
                    </div>
                    <!--================================银行管理end========================================-->
                </div>
            </div>
            <!--================================形象银行end========================================-->
        </div>
    </div>
</div>
<!--/#content-->
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->


<div class="bg"></div>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('imagebank',function(imagebank){
        imagebank.init();
    });
</script>
</body>
</html>
