<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>我的优惠券</title>
    <meta charset="utf-8">
    <%-- <link href="/static/css/reset.css" rel="stylesheet" type="text/css">--%>
    <link href="/static/css/main.css" rel="stylesheet" type="text/css">
    <link href="/static/css/coupon.css" rel="stylesheet" type="text/css">
</head>
<body>
<%@ include file="mallSectionHead.jsp" %>
<div style="border-top:4px solid #ff510d"></div>
<div class="ebusiness-T">
    <ul class="ul-nav-s" id="listAc">
    </ul>
    <%@include file="leftmenu.jsp" %>
    <div class="ebusiness-main-right">
        <div class="coupon-main">
            <div class="coupon-top">
                <ul>
                    <li>我的卡券</li>
                </ul>
            </div>
            <div class="coupon-query">
                <div class="coupon-query-top">
                    <div class="coupon-query-CX">
                        <ul>
                            <li class="coupon-H" value="unused">未使用</li>
                            <li value="used">已使用</li>
                            <li value="expiration">已过期</li>
                        </ul>
                    </div>
                    <div class="coupon-CZ">
                        <span class="recharge">抵用券充值</span>
                    </div>
                </div>
                <%--抵用券--%>
                <div class="coupon-XQ">
                    <dl id="vouchers"></dl>
                    <%--抵用券模板--%>
                    <script id="vouchersTmpl" type="text/template">
                        <dt>
                            <em class="XQ-G">适用购买渠道</em>
                            <em class="XQ-C">来源</em>
                            <em class="XQ-D">券号</em>
                            <em class="XQ-L">面额</em>
                            <em class="XQ-K">截止期限</em>
                            <em class="XQ-Y">充值日期</em>
                            <%--<em class="XQ-S">待生效点数</em>--%>
                            <em></em>
                        </dt>
                        {{ for(var i in it) { }}
                        <dd id="{{=it[i].id}}">
                            <em class="XQ-G">复兰商城</em>
                            <em class="XQ-C">商城推广</em>
                            <em class="XQ-D">{{=it[i].number}}</em>
                            <em class="XQ-L">{{=it[i].denomination/100}}元</em>
                            <em class="XQ-K">{{=it[i].expTimeInfo}}</em>
                            <em class="XQ-Y">{{=it[i].rechargeTimeInfo}}</em>
                            <%--<em class="XQ-S">100</em>--%>
                            <%--<em class="XQ-del">
                                <img src="/static/images/coupon-del.png">
                            </em>--%>
                        </dd>
                        {{ } }}
                    </script>
                </div>
            </div>
        </div>
    </div>
</div>

<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>

<div class="bg"></div>

<%--抵用券充值--%>
<div class="coupon-bg">
    <div class="coupon-bg-top">
        抵用券充值
    </div>
    <div class="coupon-bg-info">
        <em>输入充值密码：</em><input id="voucherno">
        <i hidden id="warning">您输入的充值密码不存在或已被充值，请重新确认</i>
    </div>
    <div class="coupon-bg-bottom">
        <span class="coupon-QD">确定</span>
        <span class="coupon-QX">取消</span>
    </div>
</div>

<!--=========积分使用说明弹出框============-->
<%--<div class="jifen-bg">
    <div class="jifen-bg-top">
        积分使用规则
    </div>
    <div class="jifen-bg-info">
        <em>一、</em>

        <p>积分由经验值兑换，兑换比例为1:1；</p><br>
        <em>二、</em>

        <p>商城结算时可用积分抵扣，抵扣额度为10积分：1元，最高抵扣15%；</p><br>
        <em>三、</em>

        <p>积分抵扣可与抵用券同时使用；</p><br>
        <em>四、</em>

        <p>经验值获取方法见：校级平台-宠物-经验值获取规则；</p>
    </div>
    <div class="jifen-bg-bottom">
        <span class="jifen-ck">关闭</span>
    </div>
</div>--%>


<!--=========抵用券使用说明弹出框============-->
<%--<div class="diyong-bg">
    <div class="diyong-bg-top">
        抵用券使用规则
    </div>
    <div class="diyong-bg-info">
        <p>使用范围：</p><br>

        <p>本券仅限在复兰商城使用，全网通用</p><br>

        <p>使用方法：</p><br>

        <p>1.登录复兰商城（http：//www.k6kt.com//mall），我的卡券-抵用券充值-输入充值吗</p>

        <p>2.选择商品，点击购物车，点击结账，选择抵用券后自动抵扣，确认结算。</p>

        <p>3.抵用券最高只能抵扣商品金额的15%或20%、折抵上限50元（抵用券面值50）</p>

        <p>4.一张订单只能使用一张本券，不找零（一次没使用完本券，剩余作废）、不抵扣运费。每个账户只能充值一次本券，不可与其他优惠券叠加使用。</p>

        <p>使用时间：</p>

        <p>过期不能充值；充值后30天内使用，过期作废。</p>
    </div>
    <div class="diyong-bg-bottom">
        <span class="diyong-ck">关闭</span>
    </div>
</div>--%>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
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
<!-- Custom js -->
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/mall/0.1.0/voucher.js');
</script>
</body>
</html>