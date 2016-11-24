<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2016/3/8
  Time: 14:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>订单管理</title>
    <meta charset="utf-8">
    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static_new/css/mall/manageOrders.css"/>
</head>
<body ng-app="myApp" ng-controller="myCtrl">

<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="navbar-header">
        <a class="navbar-brand">订单状态</a>
    </div>
    <div>
        <ul class="nav navbar-nav">
            <li class="active" state="-1"><a>全部</a></li>
            <li state="1"><a>待付款</a></li>
            <li state="2"><a>待发货</a></li>
            <li state="6"><a>已发货</a></li>
            <li state="3"><a>已完成</a></li>
            <li state="4"><a>已撤销</a></li>
            <li state="5"><a>已删除</a></li>
        </ul>
    </div>
</nav>

<ul class="list-group" id="orderList"></ul>
<div class="new-page-links"></div>

<script id="orderListTmpl" type="text/template">
    {{~it:order:index}}
    <li class="list-group-item" orderId="{{=order.id}}" addr="{{=order.address}}" ph="{{=order.phoneNumber}}">
        <div>
            <em class="order-user">
                {{=order.date}}
                &nbsp;&nbsp;&nbsp;&nbsp;订单号:{{=order.orderNumber}}
                &nbsp;&nbsp;&nbsp;&nbsp;用户姓名：{{=order.userName}}
                &nbsp;&nbsp;&nbsp;&nbsp;收件人姓名：{{=order.receiver}}
                &nbsp;&nbsp;&nbsp;&nbsp;收件人手机号：{{=order.phoneNumber}}
                &nbsp;&nbsp;&nbsp;&nbsp;收件人地址：{{=order.province}} {{=order.city}} {{=order.district}} {{=order.address}}
                &nbsp;&nbsp;&nbsp;&nbsp;更改状态
                <select class="order-state">
                    <%--<option value="1" {{if(order.state==1){}}selected{{}}}>待付款</option>
                    <option value="2" {{if(order.state==2){}}selected{{}}}>待发货</option>
                    <option value="6" {{if(order.state==6){}}selected{{}}}>已发货</option>
                    <option value="3" {{if(order.state==3){}}selected{{}}}>已完成</option>
                    <option value="4" {{if(order.state==4){}}selected{{}}}>已撤销</option>
                    <option value="5" {{if(order.state==5){}}selected{{}}}>已删除</option>--%>
                    {{?order.state==1}}
                    <option value="1" selected="selected">待付款</option>
                    <option value="2">待发货</option>
                    <option value="4">已撤销</option>
                    <option value="5">已删除</option>
                    {{??order.state==2}}
                    <option value="2" selected="selected">待发货</option>
                    <option value="6">已发货</option>
                    {{??order.state==6}}
                    <option value="6" selected="selected">已发货</option>
                    <option value="3">已完成</option>
                    {{??order.state==3}}
                    <option value="3" selected="selected">已完成</option>
                    {{??order.state==4}}
                    <option value="4" selected="selected">已撤销</option>
                    {{??order.state==5}}
                    <option value="5" selected="selected">已删除</option>
                    {{?}}
                </select>
            </em>
        </div>
        <dl>
            {{~order.goodsList:goods:i}}
            <dd goodsId="{{=goods.id}}" objKinds="{{=goods.objKinds}}">
                <span class="ebusiness-I">
                      <a target="_blank" href="/mall/detail.do?id={{=goods.id}}">
                          <img src="{{=goods.image}}">
                      </a>
                    <p>{{=goods.name}}</p>
                    <label>{{=goods.kind}}</label>
                </span>
                <span class="ebusiness-II">
                    <i>{{=goods.count}}</i>件
                </span>
                <span class="ebusiness-II">
                    <i>{{=goods.priceStr}}</i>
                </span>
                <span class="ebusiness-III" en="{{=goods.expressNo}}" ecn="{{=goods.exCompanyNo}}">
                    <label>{{=goods.stateStr}}</label>
                    {{?order.state == 2}}
                    <select class="exCompanyNoCtx" style="width:150px;height:26px;">
                        <option value="">请选择物流公司</option>
                    </select>
                    <input class="en" placeholder="快递单号" value="{{=goods.expressNo}}">
                    {{??order.state == 3 || order.state == 6}}
                    <label class="ecn" no="{{=goods.exCompanyNo}}">{{=goods.exCompanyNo}}</label>
                    <label class="en">{{=goods.expressNo}}</label>
                    <em class="express">查看物流</em>
                    {{?}}
                </span>
                <span class="ebusiness-IV">
                    <i>留言：{{=goods.message}}</i>
                </span>
            </dd>
            {{~ }}
        </dl>
    </li>
    {{~}}
</script>
<%--快递公司编号select Template--%>
<script id="exCompanyNoTmpl" type="application/template">
    {{~it:value:index}}
    <option value="{{=value.no}}">{{=value.name}}</option>
    {{~}}
</script>


<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static_new/js/modules/mall/0.1.0/admin/manageOrders.js');
</script>
</body>
</html>
