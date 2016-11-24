<%--
  Created by IntelliJ IDEA.
  User: guojing
  Date: 2016/8/25
  Time: 9:43
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
                <li id="XXYH"><a href="/qualityitem/imageBank.do">形象银行</a></li>
                <li id="XXCS" class="m-active"><a href="javascript:;" >形象超市</a></li>
                <li id="WMBL"><a href="/qualityitem/qualityItemMge.do">文明部落</a></li>
            </ul>
        </div>
        <div class="tab-main">
            <!--================================形象超市start========================================-->
            <div class="xxcs-con clearfix"  id="tab-XXCS">
                <div class="xxcs-main">
                    <!--================================形象超市首页start========================================-->
                    <div class="xxcs-index">
                        <c:if test="${roles:isStudent(sessionValue.userRole)}">
                            <div class="clearfix">
                                <div class="score-left">
                                    <span>${gradeName}</span>
                                    <span>${className}</span>
                                </div>
                            </div>
                        </c:if>
                        <div class="xxcs-title clearfix">
                            <ul class="xxcs-tab clearfix">
                                <li class="yx-active" id="XXXCS"><a href="javascript:;">形象超市</a></li>
                                <c:if test="${roles:isNotStudentAndParent(sessionValue.userRole)}">
                                    <li id="CSGL"><a href="javascript:;">超市管理</a></li>
                                </c:if>
                                <li id="DHJL"><a href="javascript:;">兑换记录</a></li>
                            </ul>
                            <c:if test="${roles:isNotStudentAndParent(sessionValue.userRole)}">
                                <a href="javascript:;" class="add-goods">添加新品</a>
                            </c:if>
                        </div>
                        <div class="xxcs-content" style="min-height: 500px;">
                            <div class="xxcs-one" id="tab-XXXCS">
                                <c:if test="${roles:isStudent(sessionValue.userRole)}">
                                    <div class="yx-title-left">
                                        <span>你已获得：</span>
                                        <span>努力学习币：<em>${dto.nlxxCount}</em></span>
                                        <span>热情参与币：<em>${dto.rqcyCount}</em></span>
                                        <span>岗位职责币：<em>${dto.gwzzCount}</em></span>
                                        <span>遵纪明理币：<em>${dto.zjmlCount}</em></span>
                                        <span>阳光可爱币：<em>${dto.ygkaCount}</em></span>
                                        <span>共计:<em>${dto.hideTotalCount}</em></span>
                                        <span>可用:<em id="totalCount">${dto.totalCount}</em></span>
                                    </div>
                                </c:if>
                                <div class="goods-list">
                                    <ul id="goodsShow" class="clearfix">
                                    </ul>
                                    <script type="text/template" id="j-tmpl">
                                        {{ if(it.list.length>0){ }}
                                        {{ for (var i = 0, l = it.list.length; i < l; i++) { }}
                                        {{var obj=it.list[i];}}
                                        <li>
                                            <div class="img-wrap">
                                                <img src="{{=obj.picPath}}?imageView/1/h/172/w/200">
                                            </div>
                                            <dl class="goods-inf" id="{{=obj.goodsId}}">
                                                <dt>{{=obj.goodsName}}</dt>
                                                <dd>价格：<span>形象币</span><em>￥{{=obj.goodsPrice}}</em></dd>
                                                <dd>库存：<em>{{=obj.goodsStock}}</em></dd>
                                                {{ if(obj.isStu){ }}
                                                    {{ if(obj.isChange){ }}
                                                        <dd class="yx-dh"><span class="change">兑换</span></dd>
                                                    {{ }else{ }}
                                                        <dd class="yx-nodh"><span>兑换</span></dd>
                                                    {{ } }}
                                                {{ }else{ }}
                                                <dd class="yx-operate"><span class="edit">编辑</span>&nbsp;<span class="del">删除</span></dd>
                                                {{ } }}
                                            </dl>
                                        </li>
                                        {{ } }}
                                        {{ } }}
                                    </script>
                                </div>
                            </div>
                            <!--================================形象超市超市管理start========================================-->
                            <c:if test="${roles:isNotStudentAndParent(sessionValue.userRole)}">
                                <div class="cs-manage" id="tab-CSGL">
                                    <div class="market-mgr-title clearfix">
                                        <select id="selGrade">
                                            <c:forEach items="${gradelist}" var="grade">
                                                <option value="${grade.id}">${grade.name}</option>
                                            </c:forEach>
                                        </select>
                                        <select id="selClass">
                                        </select>
                                    </div>
                                    <div class="csmanage-title clearfix">
                                        <div class="manage-left">
                                            <label><input id="checkAll" type="checkbox">&nbsp;全选</label>
                                        </div>
                                        <div class="manage-right">
                                            <a href="javascript:;" class="pl-agree">批量同意</a>
                                            <i>|</i>
                                            <a href="javascript:;" class="pl-refuse">批量拒绝</a>
                                        </div>
                                    </div>
                                    <table class="newTable" style="width:100%;">
                                        <thead>
                                        <tr>
                                            <th style="width:20%;">学生姓名</th>
                                            <th style="width:60%;">兑换物品</th>
                                            <th style="width:20%;">操作</th>
                                        </tr>
                                        </thead>
                                        <tbody id="changeGoods">

                                        </tbody>
                                        <script type="text/template" id="j-tmpl2">
                                            {{ if(it.list.length>0){ }}
                                            {{ for (var i = 0, l = it.list.length; i < l; i++) { }}
                                                {{var obj=it.list[i];}}
                                                <tr id="{{=obj.id}}">
                                                    <td>
                                                        <label>
                                                            {{ if(obj.changeState==0){ }}
                                                                <input name="changeCheck" type="checkbox">
                                                            {{ } }}
                                                            &nbsp;{{=obj.userName}}

                                                        </label>
                                                    </td>
                                                    <td>{{=obj.goodsName}}</td>
                                                    <td>
                                                        {{ if(obj.changeState==1){ }}
                                                        <a href="javascript:;" class="yx-agreed">已同意</a>
                                                        {{ } }}
                                                        {{ if(obj.changeState==2){ }}
                                                        <a href="javascript:;" class="yx-refused">已拒绝</a>
                                                        {{ } }}
                                                        {{ if(obj.changeState==0){ }}
                                                        <a href="javascript:;" class="yx-agree">同意</a>
                                                        <i>|</i>
                                                        <a href="javascript:;" class="yx-refuse">拒绝</a>
                                                        {{ } }}
                                                    </td>
                                                </tr>
                                            {{ } }}
                                            {{ } }}
                                        </script>
                                    </table>
                                </div>
                            </c:if>
                            <!--================================形象超市超市管理end========================================-->
                            <!--================================形象超市历史兑换记录start========================================-->
                            <div class="xxcs-history" id="tab-DHJL">
                                <div class="goods-list">
                                    <ul id="goods-history" class="clearfix">

                                    </ul>

                                    <script type="text/template" id="j-tmpl3">
                                        {{ if(it.list.length>0){ }}
                                        {{ for (var i = 0, l = it.list.length; i < l; i++) { }}
                                        {{var obj=it.list[i];}}
                                        <li>
                                            <div class="img-wrap">
                                                <img src="{{=obj.picPath}}?imageView/1/h/172/w/200">
                                            </div>
                                            <dl class="goods-inf">
                                                <dt>{{=obj.goodsName}}</dt>
                                                {{ if(!obj.isStu){ }}
                                                <dd>学生姓名：{{=obj.userName}}</dd>
                                                {{ } }}
                                                <dd>兑换日期：{{=obj.changeDate}}</dd>
                                                <dd>状态：
                                                    {{ if(obj.changeState==0){ }}
                                                    <i class="dh-fail">审核中</i>
                                                    {{ } }}
                                                    {{ if(obj.changeState==1){ }}
                                                    <i class="dh-success">兑换成功</i>
                                                    {{ } }}
                                                    {{ if(obj.changeState==2){ }}
                                                    <i class="dh-fail">兑换驳回</i>
                                                    {{ } }}
                                                </dd>
                                                {{ if(obj.changeState==2){ }}
                                                <dd>拒绝理由：<span class="goods-dic" title="{{=obj.refuseCon}}">{{=obj.refuseCon}}</span></dd>
                                                {{ } }}
                                            </dl>
                                        </li>
                                        {{ } }}
                                        {{ } }}
                                    </script>
                                </div>
                             </div>
                            <!--================================形象超市历史兑换记录end========================================-->
                        </div>
                    </div>
                    <!--================================形象超市首页end========================================-->
                </div>
            </div>
            <!--================================形象超市end========================================-->
        </div>
    </div>
    <!--================================兑换申请拒绝弹窗start========================================-->
    <div class="refuse-alert">
        <input id="refuseChangeIds" name="refuseChangeIds" type="hidden" value="">
        <div class="alert-title clearfix">
            <p>拒绝理由</p>
            <span class="alert-close">X</span>
        </div>
        <div class="alert-main">
            <span class="refuse-name">填写拒绝理由：</span>
            <textarea class="refuse-con"></textarea>
        </div>
        <div class="alert-btn">
            <button class="alert-btn-sure">确定</button>
            <button class="alert-btn-qx">取消</button>
        </div>
    </div>
    <!--================================兑换申请拒绝弹窗end========================================-->

    <!--================================添加新商品弹窗start========================================-->
    <div class="new-goods-add">
        <div class="goods-title clearfix">
            <p id="goodsTitle">添加新品</p>
            <span class="goods-close">X</span>
        </div>
        <div class="goods-main">
            <input id="goodsId" type="hidden" name="goodsId" value="">
            <dd>
            <span>商品名称</span><input id="goodsName" name="goodsName" value="">
            </dd>
            <dd>
                <span>商品图片</span>

                <div id="uploader-demo">
                    <div id="filePicker">选择图片</div>
                    <!--用来存放item-->
                    <div id="fileList" class="uploader-list"></div>
                </div>
                <input id="picPath" type="hidden" value=""/>
                <input id="qnKey" type="hidden" value=""/>
            </dd>
            <dd>
            <span>商品价格</span><input id="goodsPrice" name="goodsPrice" value="">形象币
            </dd>
            <dd>
            <span>库存量</span><input id="goodsStock" name="goodsStock" value="">件
            </dd>
        </div>
        <div class="goods-btn">
            <button class="goods-btn-sure">确定</button>
            <button class="goods-btn-qx">取消</button>
        </div>
    </div>
    <!--================================添加新商品弹窗end========================================-->
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
<script src="/static_new/js/modules/webuploader/webuploader.min.js"></script>
<script>
    seajs.use('imagemarket',function(imagemarket){
        imagemarket.init();
    });
</script>
</body>
</html>
