define('ecart', ['jquery', 'doT', 'common'], function (require, exports, module) {
    var ecart = {};
    require('jquery');
    require('doT');
    common = require('common');


    ecart.init = function () {
        load();
        getCategories();
    }

    load = function () {
        var url = "/mall/cars/list.do?aa=" + Math.random();
        common.getData(url, {}, function (rep) {
            if (rep.list.length > 0) {
                try {
                    jQuery("#tprice_div").text("￥" + rep.priceStr);
                    jQuery("#tprice_em").text("￥" + rep.priceStr);
                    jQuery("#tcount").text(rep.count);
                    jQuery("#totalPay").val(rep.price);
                    common.render({
                        tmpl: $('#goods_script'),
                        data: rep.list,
                        context: '#goodsTable',
                        overwrite: 1
                    });
                    $('.store-cart').show();
                    $('.store-bottom').show();
                } catch (x) {
                    console.log(x);
                }
            } else {
                $('.empty-car').show();
            }
        });
    }

    function getCategories() {
        common.getData("/mall/categories.do", {}, function (resp) {
            common.render({
                tmpl: '#listAcTml',
                data: resp.goodsCategories,
                context: '#listAc',
                overwrite: 1
            });

            $('.listData').each(function () {
                var parentId = $(this).attr('value');
                var that = this;
                common.getData("/mall/categories.do", {level: 2, parentId: parentId}, function (resp) {
                    common.render({
                        tmpl: '#listTml',
                        data: resp.goodsCategories,
                        context: that,
                        overwrite: 1
                    });
                });
            });

        })
    }

    $(document).ready(function () {
        var parentGoods;
        var presentGoodsId;
        $('.del').click(function () {
            parentGoods = $(this);
            presentGoodsId = $(this).attr('id');
            $('.store-affirm-bb').show();
            $('.bg').show();
        });
        $('.store-bb-QR').click(function () {
            var url = "/mall/cars/del.do?ebcId=" + presentGoodsId;
            common.getData(url, {}, function (rep) {
                var price = parseInt(jQuery("#in_" + presentGoodsId).attr("price"));
                var count = parseInt(jQuery("#in_" + presentGoodsId).attr('count'));
                if (parentGoods.parent().siblings().first().find("input[name=chkItem]:checked").length > 0) {
                    changePrice(presentGoodsId, price, count * -1);
                }

                jQuery("#tr_" + presentGoodsId).remove();
            });
            $('.bg').hide();
            $('.store-affirm-bb').hide();
        });
        $('.store-bb-QX').click(function () {
            $('.bg').hide();
            $('.store-affirm-bb').hide();
        });

        $('body').on('blur', '.store-cart-IN input[name="count"]', function () {
            var ebcId = $(this).attr('ebcId');
            var price = Number($(this).parent().attr('price'));
            var count = Number($(this).val().trim()) - Number($(this).parent().attr('count'));
            $(this).parent().attr('count', $(this).val().trim());
            update(ebcId, price, count, false);
        });
    });

    update = function (ebcId, price, count, flag) {
        if (!jQuery("#input_" + ebcId).is(":checked")) {

            jQuery("#input_" + ebcId).prop("checked", true);
            changeCartGoods(ebcId, price);
        }


        var c = parseInt(jQuery("#in_" + ebcId).attr('count').trim());
        if (c <= 1 && count == -1) {
            alert("数量不能为0");
            return;
        } else {
            if (flag) {
                var oldCount = Number(jQuery("#in_" + ebcId).children().val());
                jQuery("#in_" + ebcId).attr('count', oldCount + count);
                jQuery("#in_" + ebcId).children().val(oldCount + count);
            }
        }

        var url = "/mall/cars/update.do?ebcId=" + ebcId + "&count=" + count;
        common.getData(url, {}, function (rep) {
            changePrice(ebcId, price, count);
        });
    }

    toAddress = function () {
        var ids = "";
        jQuery(".store-CA").each(function () {
            if (jQuery(this).prop("checked") && !jQuery(this).prop("disabled")) {
                ids += jQuery(this).attr("ebcId") + ",";
            }
        });

        if (!ids) {
            alert("请选择商品");
            return;
        }

        jQuery("#ebcIds").val(ids);
        jQuery("#ebcform").submit();
    }


    changeGoods = function () {
        if (jQuery("#t_checkbox").is(":checked")) {
            jQuery(".store-CA").each(function () {
                if (!$(this).is(":checked")) {
                    jQuery(this).prop("checked", true).trigger('change');
                }
            });
        }
        else {
            jQuery(".store-CA").each(function () {
                if ($(this).is(":checked")) {
                    $("[name ='chkItem']").prop("checked", false).trigger('change');
                }
            });
        }
    }

    changeCartGoods = function (ebcId, price) {
        var c = parseInt(jQuery("#in_" + ebcId).attr('count').trim());

        if (!jQuery("#input_" + ebcId).is(":checked")) {
            jQuery("#t_checkbox").prop('checked', false);
            c = c * -1;
        }

        changePrice(ebcId, price, c);
    }


    changePrice = function (ebcId, price, count) {

        jQuery("#tcount").text(parseInt(jQuery("#tcount").text()) + count);

        var totPrice = parseInt(jQuery("#totalPay").val());
        totPrice = totPrice + price * count;
        jQuery("#totalPay").val(totPrice);
        var t_money = "￥" + (totPrice / 100).toFixed(1);
        jQuery("#tprice_div").text(t_money);
        jQuery("#tprice_em").text(t_money);
    }


    module.exports = ecart;
});
