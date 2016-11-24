define('orderpage', ['jquery', 'doT', 'common'], function (require, exports, module) {
    var orderpage = {};
    require('jquery');
    require('doT');
    common = require('common');
    var ALLORDER = -1;


    $(window).scroll(function () {
        if ($(window).scrollTop() > 100) {
            $(".backtop").fadeIn(1000);
        }
        else {
            $(".backtop").fadeOut(1000);
        }
    });

    $(".backtop").click(function () {
        $('body,html').animate({scrollTop: 0}, 1000);
        return false;
    });

    load = function (orderState) {
        var url = "/mall/order/data.do?aa=" + Math.random();
        common.getData(url, {orderState: orderState}, function (rep) {
            try {
                $('li[orderstate='+orderState+']').children().text(rep.length);
                common.render({
                    tmpl: $('#goods_script'),
                    data: rep,
                    overwrite: 1,
                    context: '#goodsTable'
                });
            } catch (x) {
            }
        });
    }


    del = function (id) {
        if (confirm("确定要删除此订单？")) {
            var url = "/mall/order/remove.do?id=" + id;
            common.getData(url, {}, function (rep) {
                if (rep.code != "200") {
                    alert(rep.message);
                }
                else {
                    jQuery("#li_" + id).remove();
                }

            });
        }
    }


    gotoCars = function () {
        common.goTo("/mall/cars/load.do");
    }

    orderpage.init = function () {
        load(ALLORDER);

        $('body').on('click', '.ebusiness-info li', function () {
            $(this).addClass('ebusiness-info-li').siblings().removeClass('ebusiness-info-li');
            load($(this).attr('orderstate'));
        })
    }

    toExpressPage = function (exCompanyNo, expressNo, orderId, address, phoneNumber, goodsName, goodsKind) {
        window.open("/mall/order/expressPage.do?exCompanyNo=" + exCompanyNo
        + "&expressNo=" + expressNo + "&orderId=" + orderId + "&address=" + encodeURI(encodeURI(address)) + "&phone=" + phoneNumber
        + "&gName=" + encodeURI(encodeURI(goodsName)) + "&gKind=" + encodeURI(encodeURI(goodsKind)), "target");
    }

    pay = function (orderId) {
        jQuery("#orderId").val(orderId);
        jQuery("#orderForm").submit();
    }

    orderDetails = function (orderId) {
        window.open("/mall/order/orderDetail.do?orderId=" + orderId, "target");
    }

    module.exports = orderpage;
});
