<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>复兰商城-物流信息</title>
    <meta charset="utf-8">
    <link href="/static/css/reset.css" rel="stylesheet" type="text/css">
    <link href="/static/css/mall/express.css" rel="stylesheet" type="text/css">
    <%--<link rel="stylesheet" type="text/css" href="/static/css/style.css"/>--%>
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>

</head>
<body>
<!--============引入头部========-->
<%@ include file="../common/head.jsp" %>


<input type="hidden" id="exCompanyNo" value="${exCompanyNo}">
<input type="hidden" id="expressNo" value="${expressNo}">

<div class="store-home-top">
    <div class="store-top-center">
        <a href="/mall" class="store-top-SCSY" href="">商城首页</a>
        <a href="/mall">复兰商城&nbsp;&nbsp;&gt;&nbsp;&nbsp;</a><a href="/mall/order/page.do">我的订单</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp;物流信息
    </div>
</div>

<div class="ogistics-main">
    <div class="ogistics-top">
        <ul>
            <li>查看物流</li>
        </ul>
    </div>
    <div class="ogistics-info">
        <div class="ogistics-info-top">
            <span>物流信息</span>
        </div>
        <!--物流信息-->
        <div id="expressInfo" class="ogistics-ig"></div>
        <!--物流信息template-->
        <script id="exInfo" type="application/template">
            {{~it:value:index}}
            <em>{{=value.AcceptTime}}&nbsp&nbsp{{=value.AcceptStation}}</em>
            {{~}}
        </script>

        <!--==============订单状况=================-->
        <div class="ogistics-Z">
            <div class="ogistics-Z-top">订单概况</div>
            <span>
        <input id="ecn" type="hidden" value="${exCompanyNo}">
        <em>运单号码：</em><Label>${expressNo}</label><em>物流公司：</em><label id="ecName"></label>
      </span>
            <%--<span><em>卖家:</em><label></label>复蓝科技</span>--%>
            <span><em>收货地址：</em><label>${address}</label><%--<label>${post}</label>--%><label>${username}</label><label>${phone}</label></span>
            <%--<span><em>发货地址：</em><label>上海市普陀区大渡河路168号长风景畔E楼12楼</label><label>200036</label><label>上海复兰信息科技有限公司</label><label>400-820-6735</label></span>--%>
            <span><em>商品信息：</em><label>${gName}</label><label>${gKind}</label></span>
        </div>
    </div>
</div>


<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>
<a hidden class="backtop"></a>

<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script src="//assets.kf5.com/supportbox/main.js" id="kf5-provide-supportBox" kf5-domain="fulankeji.kf5.com"></script>
<script>
    seajs.use('express', function (express) {
        express.init();
    });
</script>
</body>
</html>
