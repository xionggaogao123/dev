/**
 * Created by admin on 2016/7/14.
 */

define(function (require, exports, module) {
    var Common = require('common');
    require('jquery');
    require('pagination');
    var page = 1;
    var regular = $('body').attr('regular');
    $(document).ready(function () {
        $('.btn-s1').click(function () {
            $('.search-page').hide();
            $('.result-cont').show();
        });

        $('.sea').click(function () {
            var regular = $(this).siblings('.seat').val();
            postList(page, regular);
        });

        postList(page, regular);
    });

    function postList(page, regular) {
        var isInit = true;
        var requestData = {};
        requestData.sortType = 2;
        requestData.page = page;
        requestData.pageSize = 15;
        requestData.zan = 1;
        requestData.regular = encodeURI(encodeURI(regular));
        Common.getData("/forum/fPosts.do?ti=" + new Date().getTime(), requestData, function (resp) {
            var posts = resp.list;
            $('.new-page-links').html("");
            if (posts.length > 0) {
                $('.new-page-links').jqPaginator({
                    totalPages: Math.ceil(resp.count / requestData.pageSize) == 0 ? 1 : Math.ceil(resp.count / requestData.pageSize),//总页数
                    visiblePages: 5,//分多少页
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
                            postList(n, regular);
                            $('body,html').animate({scrollTop: 0}, 20);
                        }
                    }
                });
            }

            if (posts.length == 0) {
                $('#notFound').show();
                $('#notText').html(regular);
            } else {
                $('#text').show();
                $('#notFound').hide();
                $('#text').html("结果：找到" + "<span style='color: red'" + '>' + regular + "<\/span>相关内容" + resp.count + "个");
            }
            Common.render({
                tmpl: '#postListTml',
                data: posts,
                context: '#resultList',
                overwrite: 1
            });
        })

    }
});