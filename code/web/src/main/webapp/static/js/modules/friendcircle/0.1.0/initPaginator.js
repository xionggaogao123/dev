/**
 * Created by qiangm on 2015/8/10.
 */
/*
 * @Author: Tony
 * @Date:   2015-07-14 16:35:19
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-07-16 13:54:23
 */

'use strict';
define(['doT', 'common', 'jquery'], function (require, exports, module) {
    /**
     * 分页初始化
     * @param option
     */
    var paginator = {};
    paginator.initPaginator = function (option) {
        var totalPage = '';
        if (option.total == 0)
            $('.page-paginator').hide();
        else
            $('.page-paginator').show();
        $('.page-index').empty();
        if (option.total % option.pagesize == 0) {
            totalPage = option.total / option.pagesize;
        } else {
            totalPage = parseInt(option.total / option.pagesize) + 1;
        }
        buildPaginator(totalPage, parseInt(option.currentpage));
        option.operate(totalPage);
    };

    /**
     * 构造分页
     * @param totalPage
     * @param currentPage
     */
    function buildPaginator(totalPage, currentPage) {
        if (totalPage > 5) {
            if (currentPage < 4) {
                for (var i = 1; i <= 5; i++) {
                    if (i == currentPage) {
                        $('.page-index').append('<span class="active">' + i + '</span>');
                    } else {
                        $('.page-index').append('<span>' + i + '</span>');
                    }
                }
                $('.page-index').append('<i>···</i>');
            } else if (currentPage >= 4 && currentPage < (totalPage - 2)) {
                $('.page-index').append('<i>···</i>');
                for (var i = currentPage - 2; i <= currentPage + 2; i++) {
                    if (i == currentPage) {
                        $('.page-index').append('<span class="active">' + i + '</span>');
                    } else {
                        $('.page-index').append('<span>' + i + '</span>');
                    }
                }
                $('.page-index').append('<i>···</i>');
            } else {
                $('.page-index').append('<i>···</i>');
                for (var i = totalPage - 4; i <= totalPage; i++) {
                    if (i == currentPage) {
                        $('.page-index').append('<span class="active">' + i + '</span>');
                    } else {
                        $('.page-index').append('<span>' + i + '</span>');
                    }
                }
            }
        } else {
            for (var i = 1; i <= totalPage; i++) {
                if (i == currentPage) {
                    $('.page-index').append('<span class="active">' + i + '</span>');
                } else {
                    $('.page-index').append('<span>' + i + '</span>');
                }
            }
        }
    }

    module.exports = paginator;
});