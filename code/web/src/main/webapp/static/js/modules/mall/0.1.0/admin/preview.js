/**
 * Created by fl on 2016/3/1.
 */
'use strict';
define(function (require, exports, module) {
    var Common = require('common');
    var kindPrices = [];

    $(function () {
        var goodsString = sessionStorage.getItem("goods");
        var goods = JSON.parse(goodsString);
        showGoods(goods);


        $('body').on('click', '.store-check-right dl dd span', function () {//选择规格
            $(this).addClass("store-BJ").siblings().removeClass("store-BJ");
            var kindId = $(this).siblings('em').attr("id");
            var flag = false;
            for (var i in kindPrices) {
                if (kindPrices[i].id == kindId) {
                    kindPrices[i].price = $(this).attr('p');
                    flag = true;
                    setPrice(goods);
                    break;
                }
            }

            if (!flag) {
                var kind = {};
                kind.id = kindId;
                kind.price = $(this).attr('p');
                kindPrices.push(kind);
                setPrice(goods);
            }
        })
    });


    function showGoods(goods) {
        $('#name').text(goods.goodsName);
        $('#title').text(goods.goodsName);
        setPrice(goods);
        $('#images').empty();
        if (null == goods.images || goods.images.length <= 0) {//小图
            $('#sugimg').prop('src', goods.suggestImage);
        } else {
            $('#sugimg').prop('src', goods.images[0].value);
            var imgs = '';
            for (var i = 0; i < goods.images.length; i++) {
                imgs += '<img src="' + goods.images[i].value + '">';
            }
            $('#images').append(imgs);
        }

        Common.render({//规格
            tmpl: '#kindListTmpl',
            data: goods.kindDTOList,
            context: '#kindList',
            overwrite: 1
        });

        //商品详情
        $('.store-pingjia').empty();
        $('.store-pingjia').append(goods.html);

        //评价概要
        var list = goods.commentSummary;
        var max = 0;
        for (var i in list) {
            var num = list[i].value;
            if (num > max) {
                max = num;
            }
        }
        for (var i = 1; i < 6; i++) {
            $('#score' + i).text(list[5 - i].value);
            if (list[5 - i].value == max) {
                $('#score' + i).addClass('store-SPPP');
                $('#score' + i).next().addClass('store-SPPP');
                $('#score' + i).prev().addClass('store-SPP');
            }
        }

    }

    function setPrice(goods) {
        var basePrice = goods.price;
        var minPrice = basePrice - 0;
        var maxPrice = basePrice - 0;
        var kinds = goods.kindDTOList;
        for (var i in kinds) {
            var flag = false;
            var kindPrice = 0;
            var minSpecPrice = kinds[i].specList[0].price;
            var maxSpecPrice = 0;

            for (var k in kindPrices) {
                if (kindPrices[k].id == kinds[i].kindId) {
                    kindPrice = kindPrices[k].price - 0;
                    flag = true;
                    break;
                }
            }
            if (flag) {//确定规格
                minSpecPrice = kindPrice;
                maxSpecPrice = kindPrice;
            } else {
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
            }

            minPrice += minSpecPrice - 0;
            maxPrice += maxSpecPrice - 0;

        }

        if (minPrice == maxPrice) {
            $('#price').text('￥' + minPrice / 100);
        } else {
            $('#price').text('￥' + minPrice / 100 + ' - ' + maxPrice / 100);
        }
    }


});