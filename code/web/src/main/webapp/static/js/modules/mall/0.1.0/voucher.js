/**
 * Created by fl on 2016/1/15.
 */
define(function (require, exports, module) {
    var Common = require('common');
    require('pagination');
    require('jquery');
    var login = $('body').attr('login') == 'true';


    (function () {

        getExperience();
        getVouchers('unused');

        getCategories();
        $('.coupon-QD').click(function () {
            rechargeVoucher();
        })

        $('.recharge, .coupon-QX').click(function () {
            $('.bg').toggle();
            $('.coupon-bg').toggle();
            $('#warning').hide();
            $('#voucherno').val('');
        })

        $('.coupon-query-CX li').click(function () {
            $(this).addClass("coupon-H").siblings().removeClass("coupon-H");
            var state = $(this).attr('value');
            getVouchers(state);
        })

        $(".coupon-CK").click(function () {
            $(".coupon-info").hide();
            $(".coupon-query").show();
        })
        $("#back").click(function () {
            $(".coupon-info").show();
            $(".coupon-query").hide();
        })

        $('.XQ-del').click(function () {
            var msg = '确认删除该抵用券吗？';
            if (confirm(msg)) {
                deleteVoucher($(this));
            }
        })

        $(".jifen-sm").click(function () {
            $(".bg").show();
            $(".jifen-bg").show();
        });

        $(".diyong-sm").click(function () {
            $(".bg").show();
            $(".diyong-bg").show();
        });

        $(".jifen-ck").click(function () {
            $(".bg").hide();
            $(".jifen-bg").hide();
        });

        $(".diyong-ck").click(function () {
            $(".bg").hide();
            $(".diyong-bg").hide();
        });

    })()


    function getExperience() {
        Common.getData("/mall/order/experiences.do", {}, function (resp) {
            var totalExp = resp.exp;
            $("#totalExp").text(totalExp);
        });
    }

    function getVouchers(state) {
        Common.getData("/mall/vouchers.do", {state: state}, function (resp) {
            var vouchers = resp.vouchers;
            $("#vouchercount").text(vouchers.length);

            Common.render({
                tmpl: '#vouchersTmpl',
                data: vouchers,
                context: '#vouchers',
                overwrite: 1
            })
        });
    }

    function rechargeVoucher() {
        var voucherNo = $.trim($('#voucherno').val());
        if (voucherNo == '') {
            $('#warning').text('请输入充值码').show();
            return false;
        }
        Common.getPostData("/mall/vouchers/num/" + voucherNo + ".do", {}, function (resp) {
            if (resp.code == '200') {
                alert(resp.msg);
                getVouchers();
                $('#voucherno').val('');
                $('.bg').toggle();
                $('.coupon-bg').toggle();
            } else {
                $('#warning').text(resp.msg).show();
            }
        });
    }

    function deleteVoucher($ele) {
        var voucherId = $ele.parents('dd').attr('id');

        Common.getPostData("/mall/vouchers/" + voucherId + ".do", {state: 5}, function (resp) {
            if (resp.code == '200') {
                alert('删除成功');
                getVouchers();
            } else {
                alert(resp.msg);
            }
        });

    }

    function getCategories() {
        Common.getData("/mall/categories.do", {}, function (resp) {
            Common.render({
                tmpl: '#listAcTml',
                data: resp.goodsCategories,
                context: '#listAc',
                overwrite: 1
            });

            $('.listData').each(function () {
                var parentId = $(this).attr('value');
                var that = this;
                Common.getData("/mall/categories.do", {level: 2, parentId: parentId}, function (resp) {
                    Common.render({
                        tmpl: '#listTml',
                        data: resp.goodsCategories,
                        context: that,
                        overwrite: 1
                    });
                });
            });

        })
    }


});
