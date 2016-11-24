<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html style="background: white;">
<head>
    <title>所有订单</title>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
</head>
<body>
<%@ include file="mallSectionHead.jsp" %>
<div style="border-top:4px solid #ff510d"></div>
<div class="ebusiness-T">
    <ul class="ul-nav-s" id="listAc">

    </ul>
    <%@include file="leftmenu.jsp" %>
    <div class="ebusiness-main-right">
        <div class="ebusiness-min">
            <!--===============我的订单==================-->
            <div class="store-home-top">
                <div class="store-top-center">
                    <a href="/mall" class="store-top-SCSY" href="">商城首页</a>
                    <a href="/mall">复兰商城&nbsp;&nbsp;&gt;&nbsp;&nbsp;</a>我的订单
                </div>
            </div>
            <!--==================订单状态==========================-->
            <div class="coupon-top">
                <ul>
                    <li>我的订单</li>
                </ul>
            </div>
            <div class="ebusiness-info">
                <ul>
                    <li class="ebusiness-info-li" orderstate="-1">所有订单</li>
                    <li orderstate="1">待付款
                        <c:if test="${orderstate1 != 0}">
                            <em>${orderstate1}</em>
                        </c:if>
                    </li>
                    <li orderstate="2">待发货
                        <c:if test="${orderstate2 != 0}">
                            <em>${orderstate2}</em>
                        </c:if>
                    </li>
                    <li orderstate="6">待收货
                        <c:if test="${orderstate6 != 0}">
                            <em>${orderstate6}</em>
                        </c:if>
                    </li>
                    <li orderstate="3">已完成</li>
                </ul>
            </div>
            <div class="ebusiness-DD">
        <span class="ebusiness-I">
            <em>订单商品</em>
        </span>
                <span class="ebusiness-II">商品金额</span>
                <span class="ebusiness-III">订单状态</span>
                <span class="ebusiness-IV">订单金额</span>
                <span class="ebusiness-V">操作</span>


                <script id="goods_script" type="text/x-dot-template">
                    {{~it:value:index}}
                    <li id="li_{{=value.id}}">
                        <div>
                            <em>
                                {{=value.date}} &nbsp;&nbsp;&nbsp;&nbsp;订单号:{{=value.orderNumber}}
                            </em>
                            <em class="ebusiness-CL" onclick="del('{{=value.id}}')"></em>
                        </div>
                        <dl>
                            {{~value.goodsList:value1:index1}}
                            <dd>
                        <span class="ebusiness-I">
                              <a target="_blank" href="/mall/detail.do?id={{=value1.id}}">
                                  <img src="{{=value1.image}}">
                              </a>
                            <p>
                                <a target="_blank" href="/mall/detail.do?id={{=value1.id}}">
                                    {{=value1.name}}
                                </a>
                            </p>
                            <label>{{=value1.kind}}</label>
                        </span>
                                <span class="ebusiness-II">
                            <i>￥{{=value1.priceStr}}</i>
                        </span>
                                <span class="ebusiness-III">

                            <label>{{=value1.stateStr}}</label>
                            <%--已完成和已发货的订单可以查询物流信息--%>
                            {{ if(value1.state==6 || value1.state==3) { }}
                            <em onclick="toExpressPage('{{=value1.exCompanyNo}}','{{=value1.expressNo}}','{{=value.id}}','{{=value.address}}','{{=value.phoneNumber}}','{{=value1.name}}','{{=value1.kind}}')">查看物流</em>
                            {{}}}
                            <em style="margin-left: -30px;font-size: 13px;cursor: default;">电话：400-820-6735</em>
                        </span>
                                {{ if(index1==0) {}}
                                <span class="ebusiness-IV">
                            <%--<i>{{=(value.expOff+value.voucherOff) !=0 ? (value.expOff+value.voucherOff)/100 :''}}</i>--%>
                            <i>￥{{=value.totalPrice/100}}</i>
                            <em>
                                含{{ if(value.expOff != 0) { }}
                                积分抵用-￥{{=value.expOff/100}}，
                                {{ } }}{{ if(value.voucherOff != 0) { }}
                                抵用券抵用-￥{{=value.voucherOff/100}}，
                                {{ } }}运费￥{{=value.expressPrice/100}}
                            </em>
                        </span>

                                <span class="ebusiness-V">
                             <label onclick="orderDetails('{{=value.id}}')">订单详情</label>
                             {{?value1.state==1}}
                             <label onclick="pay('{{=value.id}}')" class="ebusiness-LJ">立即付款</label>
                             {{?}}
                        </span>
                                {{ }}}
                            </dd>
                            {{~}}
                        </dl>
                    </li>
                    {{~}}
                </script>

                <ul id="goodsTable"></ul>
            </div>

            <form id="orderForm" action="/mall/order/pay.do" method="post">
                <input type="hidden" id="orderId" name="orderId" value=""/>
            </form>

        </div>
    </div>
</div>

<a hidden class="backtop"></a>
<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>

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
    seajs.use('orderpage', function (orderpage) {
        orderpage.init();
    });
</script>
</body>
</html>