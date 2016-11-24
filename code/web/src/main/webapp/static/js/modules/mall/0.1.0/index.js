/**
 * Created by fl on 2016/1/15.
 */
define(function (require, exports, module) {
    var Common = require('common');
    require('pagination');
    require('jquery');
    var sortType = 2;
    var login = $('body').attr('login') == 'true';
    var page = 1;
    if (null != sessionStorage.getItem("page")) {
        page = sessionStorage.getItem("page");
    }

    //全局变量，产品一级分类以及产品二级分类
    //var goodsCategory="";
    //var levelGoodsCategory="";

    (function () {
        getCategories();
        //var category="";//商品分类
        goodsList(page);//商品列表

        $('#menuName').text('搜索');
        $('#gradeCtx span').removeClass('grade-cursor');
        $('#priceSearch span').removeClass('grade-cursor');


        $('body').on('click', '.sort', function () {
            $(this).addClass('storee-SP');
            $(this).siblings('span').removeClass('storee-SP');
            sortType = $(this).attr('type');
            if (sortType == 4) {
                $(this).attr('type', 3).removeClass('store-saixuan-b').addClass('store-saixuan-t');
            } else if (sortType == 3) {
                $(this).attr('type', 4).removeClass('store-saixuan-t').addClass('store-saixuan-b');
            }
            goodsList(1);
        });


        //产品二级分类
        $('body').on('click', '.lc', function () {
            var level = $(this).attr('value');
            //levelGoodsCategory=level;
            $(this).addClass('grade-cursor').siblings('#classic .lc').removeClass('grade-cursor');
            goodsList(1);
        });

        $('body').on('click', '#goodscategory li span span', function (event) {
            var parentId = $(this).parent().parent().attr('value');
            //var Id=$(this).attr('value');
            $(this).parent().parent().addClass('store-cur').siblings('li').removeClass('store-cur');
            $('.input-hunt').val('');
            $('body').attr('regular', '');
            $('#gradeCtx span').removeClass('grade-cursor');
            $('#bookTypeSearch span').removeClass('book-cursor');
            $('#priceSearch span').removeClass('price-cursor');
            $('#sort span').removeClass('storee-SP').first().addClass('storee-SP');

            sortType = 2;

            //if($(this).attr('value') == 'voucher'){
            //    window.location.href = '/mall/discount.do';
            //
            //}else if($(this).attr('value') == 'activity'){
            //    window.location.href = '/mall/integrate.do';
            //}


            //
            //$('#menuName').text($(this).parent().parent().find('span').text());
            getlevelCategories(parentId);  //加载二级产品分类
            $('#classic .lc:first').addClass('grade-cursor').siblings('#classic .lc').removeClass('grade-cursor');
            //$('.'+Id).addClass('grade-cursor').siblings('#classic .lc').removeClass('grade-cursor');
            //$('#'+Id).addClass('grade-cursor');
            goodsList(1);
            if (parentId == '56eb6a1d0cf234ce7e479c24') {
                $('#guide1').show();
                $('#guide2').hide();
                $('#guide3').hide();
                $('#guide4').hide();
                $('#guide5').hide();
                $('#classicTo').show();
                //$('#grade').hide();
                //$('#bookType').hide();
            } else if (parentId == '56eb6a0b0cf234ce7e479c1c') {
                $('#guide1').hide();
                $('#guide2').show();
                $('#guide3').hide();
                $('#guide4').hide();
                $('#guide5').hide();
                $('#classicTo').show();
                //$('#grade').hide();
            } else if (parentId == '56eb6a2d0cf234ce7e479c26') {
                $('#guide1').hide();
                $('#guide2').hide();
                $('#guide3').show();
                $('#guide4').hide();
                $('#guide5').hide();
                $('#classicTo').show();
                //$('#grade').show();
                //$('#bookType').hide();
            } else if (parentId == '56eb6a350cf234ce7e479c27') {
                $('#guide1').hide();
                $('#guide2').hide();
                $('#guide3').hide();
                $('#guide4').show();
                $('#guide5').hide();
                $('#classicTo').show();
                //$('#grade').hide();
                //$('#bookType').hide();
            } else if (parentId == '56eb6a440cf234ce7e479c29') {
                $('#guide1').hide();
                $('#guide2').hide();
                $('#guide3').hide();
                $('#guide4').hide();
                $('#classicTo').show();
                $('#guide5').show();
                //$('#grade').hide();
                //$('#bookType').hide();
            } else {
                $('#guide1').hide();
                $('#guide2').hide();
                $('#guide3').hide();
                $('#guide4').hide();
                $('#guide5').hide();
                $('#classicTo').hide();
                //$('#grade').hide();
                //$('#bookType').hide();
            }

            var videos = document.getElementsByTagName('video');
            for (var i = 0; i < videos.length; i++) {
                videos[i].pause()
            }
            event.stopPropagation();
        });


        //$('body').on('click', '#goodscategory li', function(){//更改产品分类
        //    $(this).addClass('store-cur').siblings('li').removeClass('store-cur');
        //    $('.input-hunt').val('');
        //    $('body').attr('regular','');
        //    $('#gradeCtx span').removeClass('grade-cursor');
        //    $('#bookTypeSearch span').removeClass('book-cursor');
        //    $('#priceSearch span').removeClass('price-cursor');
        //    $('#sort span').removeClass('storee-SP').first().addClass('storee-SP');
        //
        //    sortType = 2;

        //if($(this).attr('value') == 'voucher'){
        //    window.location.href = '/mall/discount.do';
        //
        //}else if($(this).attr('value') == 'activity'){
        //    window.location.href = '/mall/integrate.do';
        //}


        //
        //
        //$('#menuName').text($(this).find('span').text());
        //var parentId=$(this).attr('value'); //获取当前产品一级分类
        //getlevelCategories(parentId);  //加载二级产品分类
        //goodsCategory=parentId;
        //$('#classic .lc:first').addClass('grade-cursor').siblings('#classic .lc').removeClass('grade-cursor');
        //levelGoodsCategory=$('#classic .lc:first').attr('value');//获取当前产品二级分类
        //    goodsList(1);
        //    if($(this).attr('value') ==  '56eb6a1d0cf234ce7e479c24'){
        //        $('#guide1').show();
        //        $('#guide2').hide();
        //        $('#guide3').hide();
        //        $('#guide4').hide();
        //        $('#guide5').hide();
        //        $('#classicTo').show();
        //        //$('#grade').hide();
        //        //$('#bookType').hide();
        //    }else if($(this).attr('value') == '56eb6a0b0cf234ce7e479c1c'){
        //        $('#guide1').hide();
        //        $('#guide2').show();
        //        $('#guide3').hide();
        //        $('#guide4').hide();
        //        $('#guide5').hide();
        //        $('#classicTo').show();
        //        //$('#grade').hide();
        //    }else if($(this).attr('value') == '56eb6a2d0cf234ce7e479c26'){
        //        $('#guide1').hide();
        //        $('#guide2').hide();
        //        $('#guide3').show();
        //        $('#guide4').hide();
        //        $('#guide5').hide();
        //        $('#classicTo').show();
        //        //$('#grade').show();
        //        //$('#bookType').hide();
        //    }else if($(this).attr('value') == '56eb6a350cf234ce7e479c27'){
        //        $('#guide1').hide();
        //        $('#guide2').hide();
        //        $('#guide3').hide();
        //        $('#guide4').show();
        //        $('#guide5').hide();
        //        $('#classicTo').show();
        //        //$('#grade').hide();
        //        //$('#bookType').hide();
        //    }else if($(this).attr('value') == '56eb6a440cf234ce7e479c29'){
        //        $('#guide1').hide();
        //        $('#guide2').hide();
        //        $('#guide3').hide();
        //        $('#guide4').hide();
        //        $('#classicTo').show();
        //        $('#guide5').show();
        //        //$('#grade').hide();
        //        //$('#bookType').hide();
        //    }else{
        //        $('#guide1').hide();
        //        $('#guide2').hide();
        //        $('#guide3').hide();
        //        $('#guide4').hide();
        //        $('#guide5').hide();
        //        $('#classicTo').hide();
        //        //$('#grade').hide();
        //        //$('#bookType').hide();
        //    }
        //
        //    var videos = document.getElementsByTagName('video');
        //    for(var i = 0;i < videos.length;i++){
        //        videos[i].pause()
        //    }
        //});


        $('body').on('click', '#gradeCtx input', function () {//年级分类
            $(this).next().toggleClass('grade-cursor');
            goodsList(1);
        });

        $('body').on('click', '#bookTypeSearch span', function () {//课外阅读分类
            $(this).addClass('book-cursor').siblings().removeClass('book-cursor');
            goodsList(1);
        });

        $('body').on('click', '#gradeSpan', function () {//教辅教材
            $(this).addClass('grade-cursor').siblings().removeClass('grade-cursor');
            $('#bookTypeSearch span').removeClass('book-cursor');
            $('#gradeCtx').show();
            $('#bookTypeSearch').hide();
            goodsList(1);
        });
        $('body').on('click', '#bookTypeSpan', function () {//课外阅读
            $(this).addClass('grade-cursor').siblings('span').removeClass('grade-cursor');
            $('#gradeCtx label span').removeClass('grade-cursor');
            $('#gradeCtx label input').attr('checked', false);
            $('#bookTypeSearch').show();
            $('#gradeCtx').hide();
            goodsList(1);
        });


        $('body').on('click', '#priceSearch span', function () {//价格筛选
            $(this).addClass('price-cursor');
            $(this).siblings('span').removeClass('price-cursor');
            goodsList(1);
        });

        $('.input-hunt').change(function () {
            var regular = $(this).val();
            $('body').attr('regular', regular);
        })


    })();

    (function () {
        var categoryId = $('#categoryId').val();
        //var levelCategoryId = $('#levelCategoryId').val();
        if (categoryId) {
            //if(levelCategoryId){
            $('#goodscategory').find('li').find('span').find('span').each(function () {
                if ($(this).parent().parent().attr('value') == categoryId) {
                    $(this).trigger('click');
                }
            });
            //}else{
            //    $('#goodscategory').find('li').each(function(){
            //        if($(this).attr('value') == categoryId){
            //            //$(this).trigger('click');
            //            $(this).addClass('store-cur').siblings('li').removeClass('store-cur');
            //            $('.input-hunt').val('');
            //            $('body').attr('regular','');
            //            $('#gradeCtx span').removeClass('grade-cursor');
            //            $('#bookTypeSearch span').removeClass('book-cursor');
            //            $('#priceSearch span').removeClass('price-cursor');
            //            $('#sort span').removeClass('storee-SP').first().addClass('storee-SP');
            //
            //            sortType = 2;

            //if($(this).attr('value') == 'voucher'){
            //    window.location.href = '/mall/discount.do';
            //
            //}else if($(this).attr('value') == 'activity'){
            //    window.location.href = '/mall/integrate.do';
            //}

            //$('#menuName').text($(this).find('span').text());
            //var parentId=$(this).attr('value'); //获取当前产品一级分类
            //getlevelCategories(parentId);  //加载二级产品分类
            //goodsCategory=parentId;
            //$('#classic .lc:first').addClass('grade-cursor').siblings('#classic .lc').removeClass('grade-cursor');
            //levelGoodsCategory=$('#classic .lc:first').attr('value');//获取当前产品二级分类
            //        goodsList(1);
            //        if($(this).attr('value') ==  '56eb6a1d0cf234ce7e479c24'){
            //            $('#guide1').show();
            //            $('#guide2').hide();
            //            $('#guide3').hide();
            //            $('#guide4').hide();
            //            $('#guide5').hide();
            //            $('#classicTo').show();
            //            $('#grade').hide();
            //            $('#bookType').hide();
            //        }else if($(this).attr('value') == '56eb6a0b0cf234ce7e479c1c'){
            //            $('#guide1').hide();
            //            $('#guide2').show();
            //            $('#guide3').hide();
            //            $('#guide4').hide();
            //            $('#guide5').hide();
            //            $('#classicTo').show();
            //            $('#grade').hide();
            //        }else if($(this).attr('value') == '56eb6a2d0cf234ce7e479c26'){
            //            $('#guide1').hide();
            //            $('#guide2').hide();
            //            $('#guide3').show();
            //            $('#guide4').hide();
            //            $('#guide5').hide();
            //            $('#classicTo').show();
            //            $('#grade').show();
            //            $('#bookType').hide();
            //        }else if($(this).attr('value') == '56eb6a350cf234ce7e479c27'){
            //            $('#guide1').hide();
            //            $('#guide2').hide();
            //            $('#guide3').hide();
            //            $('#guide4').show();
            //            $('#guide5').hide();
            //            $('#classicTo').show();
            //            $('#grade').hide();
            //            $('#bookType').hide();
            //        }else if($(this).attr('value') == '56eb6a440cf234ce7e479c29'){
            //            $('#guide1').hide();
            //            $('#guide2').hide();
            //            $('#guide3').hide();
            //            $('#guide4').hide();
            //            $('#classicTo').show();
            //            $('#guide5').show();
            //            $('#grade').hide();
            //            $('#bookType').hide();
            //        }else{
            //            $('#guide1').hide();
            //            $('#guide2').hide();
            //            $('#guide3').hide();
            //            $('#guide4').hide();
            //            $('#guide5').hide();
            //            $('#classicTo').hide();
            //            $('#grade').hide();
            //            $('#bookType').hide();
            //        }
            //
            //        var videos = document.getElementsByTagName('video');
            //        for(var i = 0;i < videos.length;i++){
            //            videos[i].pause()
            //        }
            //    }
            //});

        }
    })();

    function goodsList(page) {
        var state = $('body').attr('state');
        if (state == '') {
            state = 0;
        }
        sessionStorage.setItem("page", page);
        var isInit = true;
        var requestData = {};
        requestData.sortType = sortType;
        requestData.page = page;
        requestData.pageSize = 16;
        requestData.state = state;
        requestData.goodsCategory = $('#goodscategory .store-cur').attr('value');
        requestData.levelGoodsCategory = $('#classic .grade-cursor').attr('value');
        if ($('#gradeCtx .grade-cursor').length > 0) {//教辅教材年级分类
            requestData.gradeCategory = '';
            $('#gradeCtx .grade-cursor').each(function () {
                requestData.gradeCategory += $(this).attr('value') + ',';
            });
        }
        if ($('#bookTypeSearch .book-cursor').length > 0) {//课外阅读分类
            requestData.bookCategory = $('.book-cursor').attr('value');
        }
        if ($('.price-cursor').length > 0) {//价格
            requestData.price = $('.price-cursor').attr('value');
        }
        requestData.regular = $('body').attr('regular');
        Common.getData("/mall/goods.do", requestData, function (resp) {
            var goods = resp.list;
            $('.new-page-links').html("");
            if (goods.length > 0) {
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
                            goodsList(n);
                            $('body,html').animate({scrollTop: 0}, 20);
                        }
                    }
                });
            }

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
            Common.render({
                tmpl: '#gradeTmpl',
                data: resp.gradeCategories,
                context: '#gradeCtx',
                overwrite: 1
            });

            $('.li11').each(function () {
                var parentId = $(this).attr('value');
                var that = this;
                Common.getData("/mall/categories.do", {
                    level: 2,
                    parentId: parentId
                }, function (resp) {
                    Common.render({
                        tmpl: '#li11Tmpl',
                        data: resp.goodsCategories,
                        context: that,
                        overwrite: 1
                    });
                })
            })
        })
    }

    function getlevelCategories(parentId) {
        Common.getData("/mall/categories.do", {
            level: 2,
            parentId: parentId
        }, function (resp) {
            Common.render({
                tmpl: '#classicTmpl',
                data: resp.goodsCategories,
                context: '#classic',
                overwrite: 1
            });

        })
    }

});
