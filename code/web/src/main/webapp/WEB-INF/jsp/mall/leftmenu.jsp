<%--
  Created by IntelliJ IDEA.
  User: wangkaidong
  Date: 2016/4/11
  Time: 18:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="ebusiness-main-left">
    <dl>
        <dt class="ebusiness-main-B">我的复兰商城</dt>
        <dd class="ebusiness-main-B">交易中心</dd>
        <span id="item1" class="ebusiness-main-HOV" onclick="window.location.href = '/mall/order/page.do'">我的订单</span>
        <span id="item2" onclick="window.location.href = '/mall/userCenter/voucher.do'">我的优惠券</span>
        <dd class="ebusiness-main-B">关注中心</dd>
        <span id="item3" onclick="window.location.href = '/mall/userCenter/collection.do'">我收藏的商品</span>
        <span id="item4" onclick="window.location.href = '/mall/userCenter/history.do'">我的浏览记录</span>
        <dd class="ebusiness-main-B">个人中心</dd>
        <span id="item5" onclick="window.location.href = '/mall/userCenter/user.do'">个人信息</span>
        <span id="item6" onclick="window.location.href = '/mall/userCenter/address.do'">地址设置</span>
    </dl>
</div>
<script>
    var menuItem = '${menuItem}';
    if (menuItem == 1) {
        $('#item1').addClass('ebusiness-main-HOV');
        $('#item1').siblings('span').removeClass('ebusiness-main-HOV');
    } else if (menuItem == 2) {
        $('#item2').addClass('ebusiness-main-HOV');
        $('#item2').siblings('span').removeClass('ebusiness-main-HOV');
    } else if (menuItem == 3) {
        $('#item3').addClass('ebusiness-main-HOV');
        $('#item3').siblings('span').removeClass('ebusiness-main-HOV');
    } else if (menuItem == 4) {
        $('#item4').addClass('ebusiness-main-HOV');
        $('#item4').siblings('span').removeClass('ebusiness-main-HOV');
    } else if (menuItem == 5) {
        $('#item5').addClass('ebusiness-main-HOV');
        $('#item5').siblings('span').removeClass('ebusiness-main-HOV');
    } else if (menuItem == 6) {
        $('#item6').addClass('ebusiness-main-HOV');
        $('#item6').siblings('span').removeClass('ebusiness-main-HOV');
    }
</script>