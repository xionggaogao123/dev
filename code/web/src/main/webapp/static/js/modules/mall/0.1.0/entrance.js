/**
 * Created by admin on 2016/6/2.
 */

define(function (require, exports, module) {
    var Common = require('common');
    require('jquery');
    var sortType = 2;
    var login = $('body').attr('login') == 'true';

    //(function() {
    //
    //})();

    $(document).ready(function () {
        $('.type-nav span').click(function () {
            $(this).addClass('span-cur').siblings('.type-nav span').removeClass('span-cur');
        });
        $('.type-nav span:nth-child(1)').click(function () {
            $('.div-list').show();
            $('.cont-sqrt').hide();
        });
        $('.type-nav span:nth-child(2)').click(function () {
            $('.cont-sqrt').show();
            $('.div-list').hide();
        });
        $('.wh-title span').click(function () {
            $(this).removeClass('wh-cur').siblings('.wh-title span').addClass('wh-cur');
        });
        $('.good-nav span').click(function () {
            $(this).addClass('ora-cur').siblings('.good-nav span').removeClass('ora-cur');
        });
        getSection();
        getCategories();

        getFPost();


        var initValue = '56eb6a1d0cf234ce7e479c24';
        goodsList(initValue, -1, 1);
        $('body').on('click', '.spanItem', function () {
            //$('.spanItem:first').removeClass('ora-cur');
            var goodsCategory = $(this).attr('value');
            var activity = -1;
            if (goodsCategory == "ALL") {
                activity = 0;
            }
            goodsList(goodsCategory, activity, 1);
            $(this).addClass('ora-cur').siblings('.spanItem').removeClass('ora-cur');
        });
    })

    function goodsList(goodsCategory, activity, groupPurchase) {
        var state = 0;

        //sessionStorage.setItem("page", page);
        var requestData = {};
        requestData.sortType = sortType;
        requestData.page = 1;
        requestData.pageSize = 6;
        requestData.state = state;
        requestData.goodsCategory = goodsCategory;//商品分类
        requestData.activity = activity;
        requestData.groupPurchase = groupPurchase;
        requestData.regular = $('body').attr('regular');

        Common.getData("/mall/goods.do", requestData, function (resp) {
            var goods = resp.list;

            Common.render({
                tmpl: '#goodsListTmpl',
                data: goods,
                context: '#kk',
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
            $('#' + goods.goodsId + ' em').text('￥' + minPrice / 100);
        } else {
            $('#' + goods.goodsId + ' em').text('￥' + minPrice / 100 + ' - ' + maxPrice / 100);
        }
    }


    function getCategories() {
        Common.getData("/mall/categories.do", {}, function (resp) {
            Common.render({
                tmpl: '#goodscategoryTmpl',
                data: resp.goodsCategories,
                context: '#goodscategory',
                overwrite: 1
            });
            $('.spanItem').each(function () {
                var parentId = $(this).attr('value');
                var that = this;
                Common.getData("/mall/categories.do", {level: 2, parentId: parentId}, function (resp) {
                    Common.render({
                        tmpl: '#nvTml',
                        data: resp.goodsCategories,
                        context: that,
                        overwrite: 1
                    });
                });
            });
            $('.spanItem:first').addClass('ora-cur');

        })
    }

    function getFPost() {
        var requestData = {};
        requestData.sortType = 2;
        requestData.page = 1;
        requestData.classify = -1;
        requestData.cream = -1;
        requestData.gtTime = 0;
        requestData.postSection = "";
        requestData.pageSize = 8;
        requestData.inSet = 1;
        Common.getData("/forum/fPostsActivity.do", requestData, function (resp) {
            var total = resp.list;
            Common.render({
                tmpl: '#talentTml',
                data: total,
                context: '#talentList',
                overwrite: 1
            });
        })
    }

    function getSection() {
        Common.getData("/forum/fSection.do", {}, function (resp) {
            Common.render({
                tmpl: '#kklTml',
                data: resp,
                context: '#kkl',
                overwrite: 1
            });
        });

        $('.p-icon').each(function () {
            var id = $(this).attr('value');
            var that = this;
            Common.getData("/forum/fSectionCount.do", {id: id}, function (resp) {
                Common.render({
                    tmpl: '#ssTml',
                    data: resp,
                    context: that,
                    overwrite: 1
                });
            });
        });
        $('.mk:first').addClass('mk1');
        $('.mk').eq(1).addClass('mk2');
        $('.mk').eq(2).addClass('mk3');
        $('.mk').eq(3).addClass('mk4');
        $('.mk').eq(4).addClass('mk5');
        $('.mk').eq(5).addClass('mk6');
        $('.mk').eq(6).addClass('mk7');
        $('.mk').eq(7).addClass('mk8');
    }
});