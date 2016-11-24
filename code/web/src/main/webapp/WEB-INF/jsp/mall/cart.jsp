<%--<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>购物车</title>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <meta charset="utf-8">
    <!-- Start of KF5 supportbox script -->
    <script src="//assets.kf5.com/supportbox/main.js" id="kf5-provide-supportBox"
            kf5-domain="fulankeji.kf5.com"></script>
    <!-- End of KF5 supportbox script -->
</head>
<body>
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
        <a href="/mall">复兰商城&nbsp;&nbsp;&gt;&nbsp;&nbsp;</a>购物车
    </div>
</div>
<div class="store-cart">
    <div class="store-cart-top">
        <input type="hidden" id="totalPay">
    </div>

    <table>
        <tr>
            <th><input id="t_checkbox" class="store-YL" type="checkbox" checked="checked" onchange="changeGoods()"/>
                商品预览
            </th>
            <th style="width: 230px;">名称</th>
            <th>品牌</th>
            <th>单价</th>
            <th>数量</th>
            <th>操作</th>
        </tr>

    </table>

    <table id="goodsTable"></table>
    <script id="goods_script" type="text/x-dot-template">
        {{~it:value:index}}
        <tr id="tr_{{=value.ebcId}}">
            <td>
                <input id="input_{{=value.ebcId}}" ebcId="{{=value.ebcId}}" {{?value.state== 1}}disabled{{?}}
                       class="store-CA" type="checkbox" name="chkItem" checked="checked" <%--style="display:none;"--%>
                       onchange="changeCartGoods('{{=value.ebcId}}','{{=value.price}}')"/>
                <a target="_blank" href="/mall/detail.do?id={{=value.id}}">
                    <img src="{{=value.image}}">
                </a>
                {{?value.state == 1}}<span style="color:#aaa;">该商品已下架</span>{{?}}
            </td>
            <td style="width: 230px;">
                <span><a target="_blank" href="/mall/detail.do?id={{=value.id}}">{{=value.name}}</a></span>
                <i>{{=value.kind}}</i>
            </td>
            <td>
                {{=value.pinpai==null ? '' : value.pinpai}}
            </td>
            <td>
                <em>
                    {{=value.priceStr1}}
                </em>
            </td>
            <td>
                <label onclick="update('{{=value.ebcId}}','{{=value.price}}',-1,true)">-</label>
                <span id="in_{{=value.ebcId}}" price="{{=value.price}}" count="{{=value.count}}" class="store-cart-IN">
                    <input name="count" type="text" ebcId="{{=value.ebcId}}" value="{{=value.count}}"
                           onkeyup="if(this.value.length==1||isNaN(value) || value <= 0 || value >= 9999){this.value=this.value.replace(/[^1-9]/g,'');execCommand('undo')}else{this.value=this.value.replace(/\D/g,'')}"
                           onafterpaste="if(this.value.length==1||isNaN(value) || value <= 0 || value >= 9999){this.value=this.value.replace(/[^1-9]/g,'');execCommand('undo')}else{this.value=this.value.replace(/\D/g,'')}"
                           onkeydown="mykeydown(this.value)"
                    />
                </span>
                <label onclick="update('{{=value.ebcId}}','{{=value.price}}',1,true)">+</label>
            </td>
            <td>
                <span class="store-cart-de store-cart-SC del" id="{{=value.ebcId}}">删除</span>
            </td>
        </tr>
        {{~}}
    </script>

    <table>
        <tr>
            <td colspan="6" class="stote-II">
                <div class="store-td-I">
                    <em id="tcount">0</em>件商品，总商品总额：
                </div>
                <div class="store-td-II">
                    总计（不含运费）：
                </div>
            </td>
            <td class="store-I">
                <div id="tprice_div">
                    ￥0.00
                </div>
                <div>
                    <em class="store-III" id="tprice_em" p>￥0.00</em>
                </div>
            </td>
        </tr>
    </table>
</div>

<form id="ebcform" action="/mall/order/address.do" method="post">
    <input type="hidden" id="ebcIds" name="ebcIds"/>
</form>

<div class="store-bottom">
    <span class="store-bottom-left"><a href="/mall/index.do">继续购物</a></span>
    <span class="store-bottom-right"><a href="javascript:toAddress()">立即去结算</a></span>
</div>
<%--==========购物车为空============--%>
<div class="empty-car">购物车空空如也，赶紧去<a href="/mall/index.do">逛逛吧></a></div>

<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>
<!--删除商品弹出框-->
<div class="bg"></div>
<div class="store-affirm-bb">
    <dl>
        <dt>提示</dt>
        <dd>确认要从购物车删除删除该商品吗？</dd>
        <dd>
            <span class="store-bb-QR">确认</span><span class="store-bb-QX">取消</span>
        </dd>
    </dl>
</div>
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
    seajs.use('ecart', function (ecart) {
        ecart.init();
    });
</script>
</body>
</html>