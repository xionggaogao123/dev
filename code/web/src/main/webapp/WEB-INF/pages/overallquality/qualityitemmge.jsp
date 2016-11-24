<%--
  Created by IntelliJ IDEA.
  User: guojing
  Date: 2016/8/11
  Time: 9:41
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
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
                <li id="XXYH"><a href="/qualityitem/imageBank.do">形象银行</a></li>
                <li id="XXCS"><a href="/qualityitem/imageMarket.do" >形象超市</a></li>
                <li id="WMBL" class="m-active"><a href="javascript:;">文明部落</a></li>
            </ul>
        </div>
        <div class="tab-main">
            <c:if test="${roles:isStudent(sessionValue.userRole)}">
                <div class="clearfix">
                    <div class="score-left">
                        <span>${gradeName}</span>
                        <span>${className}</span>
                    </div>
                </div>
            </c:if>
            <div class="xxcs-title clearfix">
                <!--================================校长部落管理start========================================-->
                <ul class="blgl-tab clearfix">
                    <c:if test="${roles:isHeadmaster(sessionValue.userRole)}">
                        <li id="PBSZ" class="yx-active"><a href="javascript:;">评比设置</a><em></em></li>
                        <li id="BLGL"><a href="javascript:;">部落管理</a><em></em></li>
                        <li id="MDBL"><a href="javascript:;">文明部落</a><em></em></li>
                    </c:if>
                    <c:if test="${!(roles:isHeadmaster(sessionValue.userRole))}">
                        <li id="MDBL" class="yx-active"><a href="javascript:;">我的部落</a><em></em></li>
                    </c:if>
                </ul>
                <a href="javascript:;" class="bluo-build">部落建设</a>
            </div>
            <div class="blgl-main">
                <c:if test="${(roles:isHeadmaster(sessionValue.userRole))}">
                    <div class="bl-gl" id="tab-BLGL">
                        <div class="yx-title clearfix">
                            <div class="yx-title-left">
                                <input id="curDate" name="curDate" type="hidden" value="${curDate}">
                                <input id="refDate" name="refDate" type="hidden" value="${refDate}">
                                <select id="selGrade">
                                    <c:forEach items="${gradelist}" var="grade">
                                        <option value="${grade.id}">${grade.name}</option>
                                    </c:forEach>
                                </select>
                                <select id="selClass">
                                    <option></option>
                                </select>
                            </div>
                            <div class="yx-title-right">
                                <ul class="clearfix">
                                    <li id="prev"><a href="javascript:;">上周</a></li>
                                    <li id="curr" class="title-active"><a href="javascript:;">本周</a></li>
                                    <li id="next"><a href="javascript:;">下周</a></li>
                                </ul>
                            </div>
                        </div>
                        <table class="yxTable">
                            <thead id="yxThead">

                            </thead>
                            <script type="text/template" id="j-tmpl2">
                                <tr>
                                <th style="width:20%;">班级/日期</th>
                                {{ for(var i in it) { }}
                                <th style="width:16%;">{{=it[i]}}</th>
                                {{ } }}
                                </tr>
                            </script>
                            <tbody id="yxTbody">

                            </tbody>
                            <script type="text/template" id="j-tmpl3">
                            {{ for(var i in it) { }}
                            {{ var obj=it[i];}}
                            <tr itemId="{{=obj.itemId}}">
                                <td style="background:#ececec;color:#3999d5;">{{=obj.itemName}}</td>
                                {{ for (var j = 0, l = obj.coqiList.length; j < l; j++) {}}
                                    {{ var obj1=obj.coqiList[j];}}
                                    <td class="operate" id="{{=obj1.id}}" flagDate="{{=obj1.flagDate}}" flag="{{=obj1.flag}}">
                                        {{ if(obj1.flag=='0'){ }}
                                        <span class="table-right"></span>
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
                    <!--================================评比设置start========================================-->
                    <div class="eva-set" id="tab-PBSZ">
                        <div class="evaset-title clearfix">
                            <span>当前已有项目</span>
                            <a href="javascript:;" class="new-add">新增</a>
                        </div>
                        <table class="yxTable-s">
                            <thead>
                            <tr>
                                <th>项目名称</th>
                                <th>对应分值</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody class="info-list">

                            </tbody>
                        </table>

                        <script type="text/template" id="j-tmpl">
                            {{ if(it.list.length>0){ }}
                                {{ for (var i = 0, l = it.list.length; i < l; i++) { }}
                                    {{var obj=it.list[i];}}
                                    <tr>
                                        <td>{{=obj.itemName}}</td>
                                        <td>{{=obj.scoreName}}</td>
                                        <td id="{{=obj.id}}">
                                            <a href="javascript:;" class="table-edit">编辑</a>
                                            <em>|</em>
                                            <a href="javascript:;" class="table-del">删除</a>
                                        </td>
                                    </tr>
                                {{ } }}
                            {{ } }}
                        </script>
                    </div>
                    <!--================================评比设置end========================================-->
                </c:if>
                <!--================================文明部落start========================================-->
                <div class="wmbl-con" id="tab-MDBL">
                    <input type="hidden" id="curGradeId" value="">
                    <input type="hidden" id="curClassId" value="">
                    <c:if test="${roles:isNotStudentAndParent(sessionValue.userRole)}">
                        <div class="market-mgr-title clearfix">
                            <select id="gradeId">
                                <c:forEach items="${gradelist}" var="grade">
                                    <option value="${grade.id}">${grade.name}</option>
                                </c:forEach>
                            </select>
                            <select id="classId">
                            </select>
                        </div>
                    </c:if>
                    <!--============================文明部落部分start===============================-->
                        <div class="wmbl-main">
                            <div class="wmbl-score clearfix">
                                <div class="score-left">
                                    <span>我的部落现有：</span>
                                    行规<span id="hanggui"></span>分;
                                    卫生<span id="weisheng"></span>分;
                                    体锻<span id="tiduan"></span>分;
                                </div>
                                <div class="score-right">
                                    <span>大本营数量：<em id="baseCampCount"></em>个</span>
                                    <span>城堡数量：<em id="castleCount"></em>个</span>
                                    <span>村民数量：<em id="villagerCount"></em>个</span>
                                    <span>士兵数量：<em id="soldiersCount"></em>个</span>
                                </div>
                            </div>
                        </div>
                        <!--==========================奖励物品end==================================-->
                        <!--==========================部落排名start==================================-->
                        <div class="bl-rank">
                            <div class="xxcs-title xx-market">
                                <ul class="yx-tab clearfix">
                                    <li class="yx-active"><a href="javascript:;">部落排名</a></li>
                                </ul>
                            </div>
                            <span class="rank-num">部落排名:<em id="number"></em></span>
                            <ul class="rank-list clearfix">

                            </ul>
                            <script type="text/template" id="j-tmpl4">
                                {{ if(it.dtoList.length>0){ }}
                                {{var num=10;}}
                                {{ for (var i = it.dtoList.length-1; i >=0; i--) { }}
                                {{var obj=it.dtoList[i];}}
                                <li>
                                    <div class="rank-img">
                                        <img src="/static_new/images/overallquality/rank-{{=num-i}}.jpg">
                                    </div>
                                    <dl>
                                        {{ if(obj.ourClass){ }}
                                        <dt class="rank-self">{{=obj.className}}</dt>
                                        {{ }else{ }}
                                        <dt>{{=obj.className}}</dt>
                                        {{ } }}
                                        <dd>第{{=obj.number}}名</dd>
                                    </dl>
                                </li>
                                {{ } }}
                                {{ } }}
                            </script>
                        </div>
                        <!--==========================部落排名end==================================-->
                        <!--==========================奖励物品start==================================-->
                        <div class="goods-award">
                            <div class="xxcs-title clearfix">
                                <ul class="yx-tab clearfix">
                                    <li class="yx-active"><a href="javascript:;">奖励物品</a></li>
                                </ul>
                            </div>
                            <ul class="award-list">
                                <li>
                                    <div class="award-img">
                                        <img src="/static_new/images/overallquality/rank-1.jpg">
                                    </div>
                                    <dl>
                                        <dt>大本营</dt>
                                    </dl>
                                </li>

                                <%--<li>
                                    <div class="award-img">
                                        <img src="/static_new/images/overallquality/goods-1.jpg">
                                    </div>
                                    <dl>
                                        <dt>村民</dt>
                                    </dl>
                                </li>
                                <li>
                                    <div class="award-img">
                                        <img src="/static_new/images/overallquality/goods-2.jpg">
                                    </div>
                                    <dl>
                                        <dt>士兵</dt>
                                    </dl>
                                </li>--%>
                            </ul>
                        </div>
                        <!--==========================奖励物品end==================================-->
                    <!--============================文明部落部分end===============================-->
                </div>
                <!--================================文明部落end========================================-->
            </div>
            <!--================================校长部落管理end========================================-->
        </div>
        <!--================================文明部落end========================================-->
    </div>
</div>
<!--================================新增评比选项弹窗start========================================-->
<div class="newadd-alert">
    <div class="alert-title clearfix">
        <p>新增评比选项</p>
        <span class="alert-close">X</span>
        <input id="itemId" type="hidden" value="">
    </div>
    <div class="alert-main">
        <span class="eva-name">评比名称</span>
        <input id="itemName" type="text" class="eva-input">
        <span class="eva-score">对应分值</span>
        <select id="scoreId" class="eva-select">
            <option value="">--请选择--</option>
            <c:forEach items="${oqsList}" var="item">
                <option value="${item.id}">${item.scoreName}</option>
            </c:forEach>
        </select>
    </div>
    <div class="alert-btn">
        <button class="alert-btn-sure">确定</button>
        <button class="alert-btn-qx">取消</button>
    </div>
</div>
<!--================================新增评比选项弹窗end========================================-->
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
    seajs.use('qualityitemmge',function(qualityitemmge){
        qualityitemmge.init();
    });
</script>

</body>
</html>
