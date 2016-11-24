/**
 * Created by admin on 2016/5/24.
 */
define(function (require, exports, module) {
    var Common = require('common');
    require('jquery');
    var sortType = 2;
    var page = 1;

    (function () {
        goodsList(page);//商品列表

        getCategories();

    })();


    $(document).ready(function () {


        //检查对象，#boxs是要随滚动条固定的ID
        var offset = $('#fixed-s').offset();
        $(window).scroll(function () {
            //检查对象的顶部是否在游览器可见的范围内
            var scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
            if (offset.top < scrollTop) {
                $('#fixed-s').addClass('fixed-ftran');
            }
            else {
                $('#fixed-s').removeClass('fixed-ftran');
            }
        });
    });


    function goodsList(page) {
        var state = $('body').attr('state');
        if (state == '') {
            state = 0;
        }

        var requestData = {};
        requestData.sortType = sortType;
        requestData.page = page;
        requestData.pageSize = 99;
        requestData.state = state;
        requestData.activity = 0;
        Common.getData("/mall/integrateGoods.do", requestData, function (resp) {
            var goods = resp.list;

            if (goods.length == 0) {
                $('#notFound').show();
            } else {
                $('#notFound').hide();
            }
            Common.render({
                tmpl: '#goodsListTmpl',
                data: goods,
                context: '#goodsList',
                overwrite: 1
            });

            for (var i in goods) {
                setPrice(goods[i]);
            }

        })

    }


    function setPrice(goods) {
        var basePrice = goods.price;
        var minPrice = basePrice;
        var maxPrice = basePrice;
        var kinds = goods.kindDTOList;
        for (var i in kinds) {
            var minSpecPrice = kinds[i].specList[0].price;
            var maxSpecPrice = 0;

            var specs = kinds[i].specList;
            for (var j in specs) {
                var specPrice = specs[j].price;
                if (specPrice > maxSpecPrice) {
                    maxSpecPrice = specPrice;
                }
                if (specPrice < minSpecPrice) {
                    minSpecPrice = specPrice;
                }
            }


            minPrice += minSpecPrice;
            maxPrice += maxSpecPrice;

        }

        if (minPrice == maxPrice) {
            $('#' + goods.goodsId + ' em').text('' + minPrice / 100);
        } else {
            $('#' + goods.goodsId + ' em').text('' + minPrice / 100 + ' - ' + maxPrice / 100);
        }
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