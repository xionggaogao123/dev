/**
 * Created by fl on 2016/1/15.
 */
define(['jquery', 'common', 'pagination', 'fancybox_seajs'], function (require, exports, module) {
    Common = require('common');
    require('jquery');
    require('pagination');
    require('fancybox_seajs');
    var login = $('body').attr('login') == 'true';
    var kindPrices = [];
    var goods;
    var fp = 0;//首件运费
    var aop = 0;//续费（元/件）
    var ep = 0;//总运费

    (function () {

        $('.text-yj').hide();
        //活动时间的变更
        var str = "";

        function GetTime() {
            str = "";
            str = str + "该商品距结束还剩";
            var EndTime = new Date('2016/06/02 00:00:00');
            var NowTime = new Date();
            var t = EndTime.getTime() - NowTime.getTime();

            var day = 0;
            var hour = 0;
            var minute = 0;
            var second = 0;

            if (t > 0) {
                day = Math.floor(t / 1000 / 60 / 60 / 24);
                hour = Math.floor(t / 1000 / 60 / 60 % 24);
                minute = Math.floor(t / 1000 / 60 % 60);
                second = Math.floor(t / 1000 % 60);
                if (day < 10) {
                    str += "\<em>0";
                    str = str + day + "\</em>天";
                } else {
                    str = str + "\<em>" + day + "\</em>天";
                }
                if (hour < 10) {
                    str += "\<em>0";
                    str = str + hour + "\</em>时";
                } else {
                    str = str + "\<em>" + hour + "\</em>时";
                }
                if (minute < 10) {
                    str += "\<em>0";
                    str = str + minute + "\</em>分";
                } else {
                    str = str + "\<em>" + minute + "\</em>分";
                }
                if (second < 10) {
                    str += "\<em>0";
                    str = str + second + "\</em>秒";
                } else {
                    str = str + "\<em>" + second + "\</em>秒";
                }
            } else {
                str = "";
            }
            $('#djs').html(str);
        }

        setInterval(GetTime, 0);

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

        $(".store-MJ-left ul li").click(function () {
            $(this).addClass("store-MJ").siblings().removeClass("store-MJ");
        })

        $(".store-MJJ").click(function () {
            $(".store-shangpin").show();
            $(".store-pingjia").hide();
            $('.new-page-links').show();
        })

        $(".store-MJ").click(function () {
            $(".store-shangpin").hide();
            $(".store-pingjia").show();
            $('.new-page-links').hide();
        })

        $('#addNum').click(function () {//增加数量
            var num = $('#num').val() - 0 + 1;
            if (num > 100) {
                num = 100;
            }
            $('#num').val(num);
            ep = fp + (num - 1) * aop;
            $('#exPress').text('￥' + ep.toFixed(2));
        })
        $('#reduceNum').click(function () {//减少数量
            var num = $('#num').val() - 1;
            if (num < 1) {
                num = 1;
            }
            $('#num').val(num);

            ep = fp + (num - 1) * aop;
            $('#exPress').text('￥' + ep.toFixed(2));
        })
        $('body').on('change', '#num', function () {//格式化个数
            var num = $('#num').val().replace(/[^0-9]/ig, "");
            if (num < 1) {
                num = 1;
            } else if (num > 100) {
                num = 100;
            }
            $('#num').val(num);
        })

        $('.store-LJ').click(function () {//立即购买
            var login = $('body').attr('login') == 'true';
            if (login) {
                var requestData = getGoodsInfo();
                if (1 == requestData.isValid) {
                    window.location.href = '/mall/order/address.do?buyNow=' + true
                        + '&goodsId=' + requestData.goodsId + '&count=' + requestData.count + '&kinds=' + requestData.kinds;
                }
            } else {
                $('.store-register').fadeToggle();
                $('.bg').fadeToggle();
            }
        });

        $('.store-JR').click(function () {//添加到购物车
            var login = $('body').attr('login') == 'true';
            if (login) {
                var requestData = getGoodsInfo();
                if (1 == requestData.isValid) {
                    Common.getData('/mall/cars/add.do', requestData, function (resp) {
                        if (resp) {
                            $('#goodsCount').text(resp.length);
                            alert('已加入购物车');
                        }
                    })
                }
            } else {
                $('.store-register').fadeToggle();
                $('.bg').fadeToggle();
            }

        });

        $('.store-JZ').click(function () {//家长一键购
            var login = $('body').attr('login') == 'true';
            if (login) {
                var requestData = getGoodsInfo();
                if (1 == requestData.isValid) {
                    window.location.href = '/mall/order/address.do?parentPay=' + true
                        + '&goodsId=' + requestData.goodsId + '&count=' + requestData.count + '&kinds=' + requestData.kinds;
                }
            } else {
                $('.store-register').fadeToggle();
                $('.bg').fadeToggle();
            }
        });

        $('body').on('mouseenter', '#images img', function () {//小图展示为大图
            $('#sugimg').prop('src', $(this).attr('src'));
        })

        $('#car').click(function () {//跳转到购物车
            var login = $('body').attr('login') == 'true';
            if (login) {
                window.open('/mall/cars/load.do');
            } else {
                $('.store-register').fadeToggle();
                $('.bg').fadeToggle();
            }

        })

        $('input:checkbox').click(function () {
            var ss = $(this).val();
            if (ss == 1) {
                $(this).val(0);
            } else {
                $(this).val(1);
            }
            commentList(1);
        })


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

        var flag = false;
        $('#selectProvince').click(function () {
            $('#selectPrvCtx').show();
            flag = false;
        });
        $('#selectPrvLabel').click(function () {
            $('#selectPrvCtx').show();
            flag = false;
        });

        $('body').click(function () {
            if (flag) {
                $('#selectPrvCtx').hide();
            }
            flag = true;
        });

        $('#collectionA').click(function () {
            var login = $('body').attr('login') == 'true';
            if (login) {
                var goodsId = $('body').attr('id');
                var url = '/mall/goods/' + goodsId + '/collection.do';
                var param = {goodsId: goodsId};
                $.post(url, param, function (data) {
                    if (data && data.code == "200") {
                        $('#collectionA').hide();
                        $('#collectionB').show();
                    }
                });
            } else {
                $('.store-register').fadeToggle();
                $('.bg').fadeToggle();
            }
        });

        (function () {
            var url = 'http://apis.map.qq.com/ws/district/v1/list';
            var param = {key: 'OZPBZ-FBMAU-6NSVD-4G2XT-SJ2F5-D5BHH', output: 'jsonp'};
            $.ajax({
                url: url,
                type: 'get',
                data: param,
                dataType: 'JSONP',
                success: function (data) {
                    $('#selectPrvCtx').empty();
                    Common.render({tmpl: $('#selectPrvJS'), data: data.result[0], context: '#selectPrvCtx'});
                }
            });
        })();

        goodsDetail();//详情

        //套餐如果只有一个选项，默认选中
        if ($('.store-check-right dl dd span').length == 1) {
            $('.store-check-right dl dd span').click();
        }
        ;

        commentList(1);//评论列表
        goodsList();//可能喜欢的商品
        getCategories();
        $("a.fancybox").fancybox();

    })()

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

    function goodsDetail() {
        var requestData = {};
        requestData.goodsId = $('body').attr('id');
        Common.getData('/mall/goods/' + requestData.goodsId + '.do', requestData, function (resp) {
            goods = resp.dto;
            $('#name').text(goods.goodsName);
            if (null != goods.introduction) {
                $('.p-intro').text(goods.introduction);
            }
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

            //设置抵用信息
            var msg = '';
            if (Number(goods.experienceOff) > 0) {
                var off = (goods.experienceOff / 100).toFixed(2);
                //msg+='商城积分可抵'+'\<i>'+off+'\</i>'+'元';
                msg += '商城积分可抵' + off + '元';
            }
            //if(Number(goods.voucherOff) > 0){
            //    var off = (goods.voucherOff/100).toFixed(2);
            //    msg += '抵用券可抵' + off + '元';
            //}
            if (msg == '') {
                $('.Detail-S').hide();
                //$('.span-jfkd').hide();
                //$('.img-jb').hide();
            } else {
                //$('.span-jfkd').text(msg);
                $('.Detail-S').text(msg);
            }

            Common.render({//规格
                tmpl: '#kindListTmpl',
                data: goods.kindDTOList,
                context: '#kindList',
                overwrite: 1
            });

            //商品详情
            //$('.store-pingjia').empty();
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


        })
    }

    function commentList(page) {
        var isInit = true;
        var requestData = {};
        var goodsId = $('body').attr('id');
        requestData.goodsId = goodsId;
        requestData.page = page;
        requestData.pageSize = 20;
        requestData.onlyImg = $('input:checkbox').val();
        Common.getData("/mall/goods/" + goodsId + "/comments.do", requestData, function (resp) {
            $('.new-page-links').html("");
            if (resp.list.length > 0) {
                $('.new-page-links').jqPaginator({
                    totalPages: Math.ceil(resp.count / requestData.pageSize) == 0 ? 1 : Math.ceil(resp.count / requestData.pageSize),//总页数
                    visiblePages: 10,//分多少页
                    currentPage: parseInt(page),//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (isInit) {
                            isInit = false;
                        } else {
                            commentList(n);
                        }
                    }
                });
            }

            $('#mjpj').text(resp.count);

            Common.render({
                tmpl: '#commentListTmpl',
                data: resp.list,
                context: '#commentList',
                overwrite: 1
            });

        })
    }

    function goodsList() {
        var requestData = {};
        requestData.sortType = 2;
        requestData.page = 1;
        requestData.pageSize = 3;
        Common.getData("/mall/goods.do", requestData, function (resp) {
            Common.render({
                tmpl: '#goodsListTmpl',
                data: resp.list,
                context: '#goodsList',
                overwrite: 1
            });

        })
    }

    function getGoodsInfo() {
        var data = {};
        data.isValid = 1;
        data.goodsId = $('body').attr('id');
        var flag = true;
        data.kinds = $('#kindList dd').map(function (index, ele) {
            var kind = $(ele).find('.store-BJ').attr('id');
            if (null == kind) {
                if (flag) {
                    alert('请选择' + $(ele).find('em').text());
                    flag = false;
                }
                data.isValid = 0;
                return '';
            } else {
                return kind;
            }
        }).get().join(',');
        data.count = $('#num').val();
        return data;
    }

    function setPrice(goods) {
        var basePrice = goods.price;
        var baseDisCount = goods.discountPrice;
        var minPrice = baseDisCount;
        var maxPrice = baseDisCount;
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

            minPrice += minSpecPrice;
            maxPrice += maxSpecPrice;

        }

        if (minPrice == maxPrice) {
            $('#disCountPrice').text('￥' + minPrice / 100);
        } else {
            $('#disCountPrice').text('￥' + minPrice / 100 + ' - ' + maxPrice / 100);
        }
        if (goods.price != goods.discountPrice) {
            basePrice = minPrice - baseDisCount + basePrice;
            if (minPrice == maxPrice) {
                $('#price').text('￥' + basePrice / 100);
            } else {
                var baseP = maxPrice - baseDisCount + basePrice;
                $('#price').text('￥' + basePrice / 100 + ' - ' + baseP / 100);
            }
            $('#price').show();
            $('.text-yj').show();
        }
    }

    selectPrvClk = function (prv) {
        $('#selectPrvCtx').hide();
        $('#selectProvince').text(prv);

        var goodsId = $('body').attr('id');
        var url = "/mall/goods/" + goodsId + "/expressPrice.do?goodsId=" + goodsId + "&province=" + encodeURI(encodeURI(prv));
        Common.getData(url, {}, function (data) {
            fp = Number(data.firstPrice);
            aop = Number(data.addOnePrice);
            var num = $('#num').val();
            ep = fp + (num - 1) * aop;
            $('#exPress').text('￥' + ep.toFixed(2));
        });
    }

    module.exports.selectPrvClk = selectPrvClk;
});

