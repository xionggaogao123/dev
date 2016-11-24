<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>商品管理</title>
    <meta charset="utf-8">
    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static/css/mall/goodsManage.css"/>
</head>
<body>

<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="navbar-header">
        <a class="navbar-brand">商品状态</a>
    </div>
    <div>
        <ul class="nav navbar-nav">
            <li id="state0" state="0"><a>销售中</a></li>
            <li state="2"><a>未上架</a></li>
            <li state="1"><a>已下架</a></li>
        </ul>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-3"></div>
        <div class="col-md-6">
            <ul class="list-group" id="goodsList"></ul>
            <div class="new-page-links"></div>
        </div>
        <div class="col-md-3"></div>
    </div>
</div>

<script id="goodsListTmpl" type="text/template">
    {{~it:goods:index}}
    <li class="list-group-item" goodsId="{{=goods.goodsId}}">
        <span class="ebusiness-I">
            <a target="_blank" href="/mall/detail.do?id={{=goods.goodsId}}">
                <img src="{{=goods.suggestImage}}">
            </a>
        </span>
        <span>
            <p class="goods-name">商品名称：{{=goods.goodsName}}</p>
        </span>
        <span class="ebusiness-II">
            <em>价格：</em><i>{{=goods.price/100}}</i>
        </span>
        <button class="btn btn-primary edit">编辑</button>
        <button class="btn  btn-danger delete">删除</button>
    </li>
    {{~}}
</script>


<!-- Javascript Files -->
<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('goodsManage');
</script>
</body>
</html>
