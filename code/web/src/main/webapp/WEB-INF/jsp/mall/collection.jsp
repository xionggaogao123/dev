<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>我收藏的商品</title>
    <link href="/static/css/style.css" rel="stylesheet" type="text/css"/>
    <link href="/static/css/main.css" rel="stylesheet" type="text/css">
    <style type="text/css">
        .store-car {
            position: relative;
            top: 0px;;
        }
    </style>
</head>
<body>
<%@ include file="mallSectionHead.jsp" %>
<div style="border-top:4px solid #ff510d"></div>
<div class="ebusiness-T">
    <ul class="ul-nav-s" id="listAc">
    </ul>
    <%@include file="leftmenu.jsp" %>
    <div class="ebusiness-main-right">
        <div class="ebusiness-voucher-main">
            <div class="coupon-top">
                <ul>
                    <li>我收藏的商品</li>
                </ul>
            </div>
            <div class="store-guidee history-main" style="margin-top:20px;">
                <div style="clear: both">
                    <ul id="goodsListCtx"></ul>
                </div>
            </div>
        </div>
    </div>
</div>


<%--回到顶部--%>
<a hidden class="backtop"></a>
<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>


<script id="goodsListTmpl" type="text/template">
    {{~it:value:index}}
    <li class="detail" id="{{=value.id}}">
        <a href="/mall/detail.do?id={{=value.goodsId}}" target="_blank"></a>
        <div>
            <img class="history-IMM" src="{{=value.suggestImage}}?imageView2/2/w/200/h/200">
            <img class="history-IM delete" src="/static/images/detail/history-del.jpg">
            <dl>
                <dd>{{=value.goodsName}}</dd>
                <dt>
                    <em>￥&nbsp;{{=value.price/100}}</em>
                </dt>
            </dl>
        </div>
    </li>
    {{~}}
</script>

<script id="listAcTml" type="text/template">
    <li class="li1" onclick="window.open('/mall/mallSection.do')">商城专区首页<em>></em>
        <div class="ul-nav-bg "></div>
    </li>
    <li class="li2" onclick="window.open('/integrate.do')"><span>节日特惠专区</span><em>></em></li>
    <li class="li2" onclick="window.open('/mall/discount.do')"><span>全积分兑换专区</span><em>></em></li>
    {{ for(var i in it) { }}
    <li class="li2"><span value="{{=it[i].id}}" class="listData"></span><em>></em></li>
    {{ } }}
    <li class="li2" onclick="window.open('/mall/index.do')"><span>全部商品</span><em>></em></li>
</script>

<script id="listTml" type="text/template">
    {{ for(var i in it) { }}
    <span onclick="window.open('/mall/index.do?categoryId={{=it[i].parentId}}&levelCategoryId={{=it[i].id}}')">{{=it[i].name}}</span> /
    {{ } }}
</script>
<!-- Javascript -->
<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('collection', function (collection) {
        collection.init();
    });
</script>
</body>
</html>
