/**
 * Created by fl on 2016/3/8.
 */
'use strict';
define(function(require,exports,module){
    var Common = require('common');
    require('pagination');
    require('jquery');
    var currentPage = 1;




    (function(){
        getVouchers(1);

        $('#generate').click(function(){
           generateVouchers();
        })

        $('body').on('click', '#voucherList .btn', function(){
            var voucherId = $(this).parents('tr').attr('id');
            var state = $(this).attr('state');
            updateVoucherState(voucherId, state);
        })


    })()

    function generateVouchers(){
        var expirationTime = $.trim($('#expirationTime').val());
        var count = $.trim($('#count').val());
        var denomination = $.trim($('#denomination').val());
        if(expirationTime == '' || count == '' || denomination == ''){
            alert('请完善信息');
            return false;
        }
        if(denomination <= 0){
            alert('面额必须大于零');
            return false;
        }
        var requestData = {};
        requestData.denomination = denomination;
        requestData.count = count;
        requestData.expirationTime = expirationTime;
        Common.getPostData('/mall/admin/vouchers.do', requestData, function(resp){
            if(resp.code == '200'){
                getVouchers(1);
                alert('添加成功');
            } else {
                alert('添加失败');
            }
        })
    }

    function updateVoucherState(voucherId, state){
        Common.getPostData('/mall/vouchers/' + voucherId + '.do', {state:state}, function(resp){
            if(resp.code == '200'){
                getVouchers(currentPage);
            } else {
                alert(resp.msg);
            }
        })
    }

    function getVouchers(page){
        currentPage = page;
        var requestParam = {};
        requestParam.page = page;
        requestParam.pageSize = 15;

        Common.getData("/mall/admin/vouchers.do", requestParam, function(resp){
            paginator(resp);

            Common.render({
                tmpl: '#voucherListTmpl',
                data: resp.vouchers,
                context:'#voucherList',
                overwrite: 1
            })


        })
    }

    function paginator(data){
        var isInit = true;
        $('.new-page-links').html("");
        if(data.vouchers.length > 0){
            $('.new-page-links').jqPaginator({
                totalPages: Math.ceil(data.count / data.pageSize) == 0 ? 1 : Math.ceil(data.count / data.pageSize),//总页数
                visiblePages: 10,//分多少页
                currentPage: parseInt(data.page),//当前页数
                first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                page: '<li class="page"><a href="javascript:void(0);">{{page}} <\/a><\/li>',
                onPageChange: function (n) { //回调函数
                    if (isInit) {
                        isInit = false;
                    } else {
                        getVouchers(n);
                    }
                }
            });
        }
    }





});


    

