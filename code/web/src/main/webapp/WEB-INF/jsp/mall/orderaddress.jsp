<%--<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>订单确认页</title>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
    <%--<script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>--%>
    <!-- Start of KF5 supportbox script -->
    <script src="//assets.kf5.com/supportbox/main.js" id="kf5-provide-supportBox"
            kf5-domain="fulankeji.kf5.com"></script>
    <!-- End of KF5 supportbox script -->
</head>
<body style="background: white;">
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
    <div class="store-affirm-top">
        <em>填写并确认订单</em>
    </div>
    <div class="store-affirm-main">
        <%--选择地址--%>
        <div class="store-address-choose">
            <dl id="addressCtx"></dl>
        </div>
        <%--地址模板--%>
        <script id="addressScript" type="text/x-dot-template">
            <dt>
                <em>选择收货地址</em>
                <em class="em-new">+新增收货地址</em>
            </dt>
            {{~it:value:index}}
            <dd class="clearfix addressDD" id="{{=value.id}}" province="{{=value.province}}">
                <span class="spa1">{{=value.userName}}</span>
                <span class="spa2">
                    {{=value.province}} {{=value.city}} {{=value.district}} {{=value.address}}
                </span>
                <span class="spa3">{{=value.telephone}}</span>
                <span class="spa4">
                    <em onclick="editAddress('{{=value.id}}','{{=value.userName}}','{{=value.province}}','{{=value.city}}','{{=value.district}}','{{=value.address}}','{{=value.telephone}}')">修改</em>
                </span>
                <span class="spa5" isDefault="{{=value.isDefault}}">
                    <button class="defaultAdd">默认地址</button>
                    <em class="setDefault">设为默认地址</em>
                </span>
                <i class="address-default"></i>
            </dd>
            {{~}}
        </script>
        <%--新增收货地址--%>
        <div id="newAddressDiv" style="display: none;">
            <div class="store-affirm-info">
                <dl>
                    <dd class="store-DZ">
                        <p>新增收货地址</p>
                    </dd>
                    <dd>
                        <em>*收货人：</em><input class="store-TE" type="text" id="user_input">
                    </dd>

                    <dd>
                        <em>*省市区 ：</em>
                        <select id="province">
                            <option value="">请选择</option>
                        </select>
                        <select id="city">
                            <option value="">请选择</option>
                        </select>
                        <select id="district">
                            <option value="">请选择</option>
                        </select>
                    </dd>
                    <script id="proviceTempJS" type="application/template">
                        {{~it:value:index}}
                        <option value="{{=value.name}}" pid="{{=value.id}}">{{=value.fullname}}</option>
                        {{~}}
                    </script>
                    <script id="cityTempJS" type="application/template">
                        {{~it:value:index}}
                        <option value="{{=value.name}}" cid="{{=value.id}}">{{=value.fullname}}</option>
                        {{~}}
                    </script>
                    <script id="districtTempJS" type="application/template">
                        <option value="">--</option>
                        {{~it:value:index}}
                        <option value="{{=value.fullname}}" did="{{=value.id}}">{{=value.fullname}}</option>
                        {{~}}
                    </script>
                    <dd>
                        <em>*详细地址：</em><input class="store-TEE" type="text" id="address_input">
                    </dd>
                    <dd>
                        <em>*手机号码：</em><input class="store-TE" type="text" id="telephone_input">
                    </dd>
                    <dd>
                        <button id="addBtn" addressId="" onclick="addAddress()">保存收货人信息</button>
                    </dd>
                </dl>
            </div>
        </div>


        <%--选择家长--%>
        <c:if test="${parentPay == true}">
            <div class="store-affirm-info store-affirm-infof">
                <dl>
                    <dt>选择商品支付人</dt>
                    <dd>
                        <input type="radio" name="parent" value="${father}" checked/><em>我的爸爸</em>
                        <input type="radio" name="parent" value="${mother}"/><em>我的妈妈</em>
                    </dd>
                </dl>
            </div>
        </c:if>

        <div class="store-affirm-SP">
            <div class="store-affirm-SP-top">
                <em class="store-affirm-YL">商品预览</em>
                <em class="store-affirm-MC">名称</em>
                <em class="store-affirm-DJ">单价</em>
                <em class="store-affirm-SL">数量</em>
                <em class="store-affirm-XS">小计</em>
            </div>

            <script id="goods_script" type="text/x-dot-template">
                {{~it:value:index}}
                <li class="goods_List">
                    <div>
                        <em class="store-affirm-YL">
                            <input type="hidden" name="goodsId" value="{{=value.id}}">
                            <a target="_blank" href="/mall/detail.do?id={{=value.id}}">
                                <img src="{{=value.image}}">
                            </a>
                        </em>
                        <em class="store-affirm-MC">
                            <p>
                                <a target="_blank" href="/mall/detail.do?id={{=value.id}}">
                                    {{=value.name}}
                                </a>
                            </p>
                            <i>{{=value.kind}}</i>
                        </em>
                        <em class="store-affirm-DJ">
                            <label>{{=value.priceStr1}}</label>
                        </em>
                        <em class="store-affirm-SL">
                            <label>{{=value.count}}</label>
                        </em>
                        <em class="store-affirm-XS">
                            <i>{{=value.priceStr}}</i>
                        </em>
                    </div>
                    <div class="store-affirm-LY">
                        <em>您的留言</em><input placeholder="如颜色、尺码、快递的要求">
                        <em class="store-affirm-FH">由复兰发货并提供售后</em>
                    </div>
                </li>
                {{~}}
            </script>

            <div class="store-affirm-UL">
                <ul id="goodsList"></ul>
            </div>

            <!--======================优惠券领取=============================-->
            <div class="store-coupon">
                <div class="store-coupon-top">
                    <i id="usevoucher">使用优惠券<span id="vouchercount"></span></i>
                    <%--<a>我也要领优惠券</a>--%>
                </div>
                <div class="store-coupon-down" id="voucherCtx">
                    您当前订单无可用抵用券
                </div>
                <script id="voucherTmpl" type="application/template">
                    <p>
                        <input type="radio" name="voucher" value="" off="0" checked/>&nbsp;不使用优惠券
                    </p>
                    {{~it:voucher:index}}
                    <p>
                        <input type="radio" name="voucher" value="{{=voucher.id}}" off="{{=voucher.denomination}}"/>
                        券号：{{=voucher.number}} ，面额：{{=voucher.denomination / 100}} 元
                    </p>
                    {{~}}
                </script>
            </div>
            <div class="store-coupon">
                <div class="store-coupon-top">
                    <i id="useexp">使用商城积分</i>
                    <%--<a>商城积分获取和使用规则</a>--%>
                </div>
                <div class="store-coupon-down">
                    <em>使用K6KT经验值兑换商城积分</em>
                    <input class="store-coupon-K" type="text" id="usedExp" value="0"><em>可抵金额<label
                        class="expoff">￥0.0</label>元</em><br>
                    <label>（您有经验值<em id="totalE">0</em>，对应积分<em id="totalExp">0</em>，此笔交易最多可抵用积分
                        <em id="availableExp">0</em>）</label>
                </div>
            </div>
            <!--===================提交订单====================-->
            <div class="store-affirm-TJ">
                <dl>
                    <dd class="store-affirm-integral">
                        <p><em id="goodscount"></em>件商品，总商品金额：<i id="tprice_div">￥0.00</i></p>
                        <p>运费：<i id="expAmount">￥0.00</i></p>
                        <p>优惠券优惠：<i id="voucheroff">￥0.00</i></p>
                        <p>积分抵用：<i class="expoff">￥0.00</i></p>
                        <em class="store-YY">应付总额：<i class="store-YY tprice_em">￥0.00</i></em>
                    </dd>
                    <dd>
                        <div class="store-affirm-DD">
                            <p>收货人：<label id="label_name"></label></p>
                            <p>地址：<label id="label_address"></label></p>
                            <p>手机号码：<label id="label_tel"></label></p>
                            <p class="store-affirm-DDJR">订单支付金额：<i class="tprice_em">￥0.00</i></p>
                            <p>
                                <a href="/mall/cars/load.do">&lt;&lt;返回购物车修改</a>
                                <span id="submit">提交订单</span>
                            </p>
                        </div>
                    </dd>
                </dl>
            </div>
        </div>
    </div>


    <form id="orderForm" action="/mall/order/create.do" method="post">
        <input type="hidden" id="addressId" name="addressId" value=""/>
        <input type="hidden" id="orderusedExp" name="usedExp" value="0"/>
        <input type="hidden" id="ordervoucherId" name="voucherId" value=""/>
        <input type="hidden" id="message" name="message" value=""/>
        <c:if test="${parentPay == true || buyNow == true}">
            <input type="hidden" name="goodsList"/>
        </c:if>
        <c:if test="${parentPay == true}">
            <input type="hidden" id="parentId" name="parentId"/>
        </c:if>
    </form>


</div>


<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>

<script id="listAcTml" type="text/template">
    <li class="li1" onclick="window.open('/mall/mallSection.do')">商城专区首页<em>></em>
        <div class="ul-nav-bg "></div>
    </li>
    <li class="li2" onclick="window.open('/integrate.do')"><span>节日特惠专区</span><em>></em></li>
    <li class="li2" onclick="window.open('/mall/discount.do')"><span>全积分兑换专区</span><em>></em></li>
    {{ for(var i in it) { }}
    <li class="li2">
        <integratespan value="{{=it[i].id}}" class="listData"></integratespan>
        <em>></em></li>
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
    seajs.use('eaddress', function (eaddress) {
        var goodsList = eval('${goodsList}');
        if (goodsList) {
            $('input[name="goodsList"]').val(JSON.stringify(${goodsList}));
        } else {
            goodsList = null;
        }
        eaddress.init(goodsList);
    });
</script>
</body>
</html>

