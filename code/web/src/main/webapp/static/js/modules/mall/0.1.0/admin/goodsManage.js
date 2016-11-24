/**
 * Created by wangkaidong on 2016/4/19.
 */
define(function (require, exports, module) {
    require('jquery');
    require('doT');
    require('pagination');
    var Common = require('common');
    var state = 0;

    init = function () {
        $('.navbar-nav li[state="0"]').trigger('click');
    }

    $(document).ready(function () {
        $('.navbar-nav li').click(function () {
            $(this).addClass('active').siblings().removeClass('active');
            state = $(this).attr('state');
            getGoodsList(1);
        });

        $('body').on('click', '.delete', function () {
            if (confirm("本功能为物理删除，删除后无法恢复，确定要删除？")) {
                var goodsId = $.trim($(this).parent().attr('goodsId'));
                $.ajax({
                    url: '/mall/admin/goods/' + goodsId + '.do',
                    type: 'DELETE',
                    data: {},
                    success: function (result) {
                        alert(result.message);
                        if (result.code == "200") {
                            getGoodsList(1);
                        }
                    }
                });
            }
        });

        $('body').on('click', '.edit', function () {
            var goodsId = $.trim($(this).parent().attr('goodsId'));
            window.open('/admin/upsetGoods.do?goodsId=' + goodsId, 'target');
            //window.location.href = '/mall/admin/upsetGoods.do?goodsId='+goodsId;
        });

        init();
    });

    function getGoodsList(page) {
        var requestParam = {};
        requestParam.state = state;
        requestParam.page = page;
        requestParam.pageSize = 10;

        Common.getData("/admin/goodsList.do", requestParam, function (resp) {
            paginator(resp);

            Common.render({
                tmpl: '#goodsListTmpl',
                data: resp.goodsList,
                context: '#goodsList',
                overwrite: 1
            });
        });

        if (state == 0) {//上架的商品不能删除
            $('.delete').hide();
        }
    }

    function paginator(data) {
        var isInit = true;
        $('.new-page-links').html("");
        if (data.goodsList.length > 0) {
            $('.new-page-links').jqPaginator({
                totalPages: Math.ceil(data.count / data.pageSize) == 0 ? 1 : Math.ceil(data.count / data.pageSize),//总页数
                visiblePages: 7,//分多少页
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
                        getGoodsList(n);
                    }
                }
            });
        }
    }

});




