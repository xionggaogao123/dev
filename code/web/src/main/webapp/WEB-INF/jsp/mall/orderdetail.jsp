<%--<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>订单详情</title>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
    <!-- Start of KF5 supportbox script -->
    <script src="//assets.kf5.com/supportbox/main.js" id="kf5-provide-supportBox"
            kf5-domain="fulankeji.kf5.com"></script>
    <!-- End of KF5 supportbox script -->
</head>
<body style="background: white;" orderId="${orderId}">
<%@ include file="mallSectionHead.jsp" %>
<div style="border-top:4px solid #ff510d"></div>
<div class="ebusiness-T">
    <ul class="ul-nav-s" id="listAc">

    </ul>
</div>
<!--===============电子商城==================-->
<div class="store-home-top">
    <div class="store-top-center">
        <a href="/mall" class="store-top-SCSY" href="">商城首页</a>
        <a href="/mall">复兰商城&nbsp;&nbsp;&gt;&nbsp;&nbsp;</a>订单确认页
    </div>
</div>
<!--====================订单确认页=========================-->
<div class="store-affirm">
    <div class="store-affirm-main" style="min-height:665px;">
        <div class="store-affirm-SP">
            <div class="store-affirm-SP-top">
                <em class="store-affirm-YL">商品预览</em>
                <em class="store-affirm-MC">名称</em>
                <em class="store-affirm-DJ">单价</em>
                <em class="store-affirm-SL">数量</em>
                <em class="store-affirm-XS">小计</em>
            </div>
            <div id="detail"></div>

            <script id="goods_script" type="text/x-dot-template">
                {{~it:value:index}}
                <li class="goods_List">
                    <div>
                        <em class="store-affirm-YL">
                            <a target="_blank" href="/mall/detail.do?id={{=value.id}}">
                                <img src="{{=value.img}}">
                            </a>
                        </em>
                        <em class="store-affirm-MC">
                            <p>
                                <a target="_blank" href="/mall/detail.do?id={{=value.id}}">{{=value.goodsName}}</a>
                            </p>
                            <label>{{=value.kind}}</label>
                        </em>
                        <em class="store-affirm-DJ">
                            <label>{{=value.priceStr}}</label>
                        </em>
                        <em class="store-affirm-SL">
                            <label>{{=value.count}}</label>
                        </em>
                        <em class="store-affirm-XS">
                            <i>{{=value.sumPriceStr}}</i>
                        </em>
                    </div>
                </li>
                {{~}}
            </script>

            <div class="store-affirm-UL">
                <ul id="goodsList"></ul>
            </div>

            <div class="store-affirm-TJ">
                <dl>
                    <dd>
                        <div class="store-affirm-DD" style="height:270px;">
                            <p>运费：<i id="expressPrice">￥0.00</i></p>
                            <p>优惠券优惠：<i id="voff">￥0.00</i></p>
                            <p>积分抵用：<i id="exp">￥0.00</i></p>
                            <p>应付总额：<i id="totalPrice">￥0.00</i></p>
                            <p>收货人：<label id="name"></label></p>
                            <p>地址：<label id="address"></label></p>
                            <p>手机号码：<label id="tel"></label></p>
                        </div>
                    </dd>
                </dl>
            </div>
        </div>
    </div>


</div>

<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>
<a hidden class="backtop"></a>

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
<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>

<script>
    seajs.use('orderDetail', function (orderdetail) {
        orderdetail.init();
    });
</script>
</body>
</html>

