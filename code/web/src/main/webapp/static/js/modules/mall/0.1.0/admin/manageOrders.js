/**
 * Created by fl on 2016/3/8.
 */
'use strict';
define(function (require, exports, module) {
    var Common = require('common');
    require('pagination');
    require('jquery');

    var state = -1;


    (function () {
        getOrders(1);

        $('.navbar-nav li').click(function () {
            $(this).addClass('active').siblings().removeClass('active');
            state = $(this).attr('state');
            getOrders(1);
            transferNoToName();
        });


        $('body').on('blur', '.en', function () {
            var orderId = $(this).parents('li').attr('orderId');
            var goodsId = $(this).parents('dd').attr('goodsId');
            var objKinds = $(this).parents('dd').attr('objKinds');
            var requestData = {};
            requestData.objKinds = objKinds;
            requestData.expressNo = $(this).parents('.ebusiness-III').find('.en').val();
            requestData.expressComNo = $(this).parents('.ebusiness-III').find('.exCompanyNoCtx').val();
            updateExpress(orderId, goodsId, requestData);
        })

        $('body').on('change', '.exCompanyNoCtx', function () {
            var orderId = $(this).parents('li').attr('orderId');
            var goodsId = $(this).parents('dd').attr('goodsId');
            var objKinds = $(this).parents('dd').attr('objKinds');
            var requestData = {};
            requestData.objKinds = objKinds;
            requestData.expressNo = $(this).parents('.ebusiness-III').find('.en').val();
            requestData.expressComNo = $(this).parents('.ebusiness-III').find('.exCompanyNoCtx').val();
            updateExpress(orderId, goodsId, requestData);
        });

        //更改订单状态
        $('body').on('change', '.order-state', function () {
            var orderId = $(this).parents('.list-group-item').attr('orderId');

            $.ajax({
                type: "post",
                data: {orderState: $(this).val()},
                url: '/admin/orders/' + orderId + '.do',
                async: false,
                dataType: "json",
                traditional: true,
                success: function (rep) {
                    getOrders(1);
                    $('li.active').click();
                    transferNoToName();
                },
                statusCode: {
                    500: function (resp) {
                        alert(resp.responseJSON.message);
                    }
                }
            });

        })

        $('body').on('click', '.express', function () {
            var exCompanyNo = $(this).parent().attr('ecn');
            var expressNo = $(this).parent().attr('en');
            var orderId = $(this).parents('li').attr('orderId');
            var address = $(this).parents('li').attr('addr');
            var phoneNumber = $(this).parents('li').attr('ph');
            var goodsName = $(this).parents('.ebusiness-III').siblings('.ebusiness-I').find('p').text();
            var goodsKind = $(this).parents('.ebusiness-III').siblings('.ebusiness-I').find('label').text();
            window.open("/mall/order/expressPage.do?exCompanyNo=" + exCompanyNo
                + "&expressNo=" + expressNo + "&orderId=" + orderId + "&address=" + address + "&phone=" + phoneNumber
                + "&gName=" + encodeURI(encodeURI(goodsName)) + "&gKind=" + encodeURI(encodeURI(goodsKind)), "target");
        })

    })()

    function updateExpress(orderId, goodsId, requestData) {
        Common.getPostData('/admin/orders/' + orderId + '/goods/' + goodsId + '.do', requestData, function (resp) {

        })
    }

    var exCompanyList = [
        {no: 'BTWL', name: '百世物流'},
        {no: 'EMS', name: 'EMS'},
        {no: 'HHTT', name: '天天快递'},
        {no: 'QFKD', name: '全峰快递'},
        {no: 'SF', name: '顺丰快递'},
        {no: 'STO', name: '申通快递'},
        {no: 'SURE', name: '速尔快递'},
        {no: 'UAPEX', name: '全一快递'},
        {no: 'UC', name: '优速快递'},
        {no: 'YD', name: '韵达快递'},
        {no: 'YTO', name: '圆通速递'},
        {no: 'YZPY', name: '邮政平邮/小包'},
        {no: 'ZJS', name: '宅急送'},
        {no: 'ZTO', name: '中通速递'}
    ]

    /*物流公司选择*/
    var exCompanySelect = function () {
        Common.render({
            tmpl: $('#exCompanyNoTmpl'),
            data: exCompanyList,
            context: '.exCompanyNoCtx'
        });
    }

    /*把快递公司编号转换为名称*/
    function transferNoToName() {
        $('.ecn').each(function () {
            var no = $(this).text();
            $(this).text(getExCompanyNameByNo(no));
        });
    }

    /*根据编号获取快递名称*/
    function getExCompanyNameByNo(no) {
        for (var index in exCompanyList) {
            if (exCompanyList[index].no == no) {
                return exCompanyList[index].name;
            }
        }
    }

    function getOrders(page) {
        var requestParam = {};
        requestParam.state = state;
        requestParam.page = page;
        requestParam.pageSize = 10;

        Common.getData("/admin/orders.do", requestParam, function (resp) {
            paginator(resp);
            Common.render({
                tmpl: '#orderListTmpl',
                data: resp.orders,
                context: '#orderList',
                overwrite: 1,
                callback: exCompanySelect
            });
        })

    }

    function paginator(data) {
        var isInit = true;
        $('.new-page-links').html("");
        if (data.orders.length > 0) {
            $('.new-page-links').jqPaginator({
                totalPages: Math.ceil(data.count / data.pageSize) == 0 ? 1 : Math.ceil(data.count / data.pageSize),//总页数
                visiblePages: 10,//分多少页
                currentPage: parseInt(data.page),//当前页数
                first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                onPageChange: function (n) { //回调函数
                    if (isInit) {
                        isInit = false;
                    } else {
                        getOrders(n);
                    }
                }
            });
        }
    }

});


    

