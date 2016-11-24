/**
 * Created by admin on 2016/11/7.
 */
/**
 * Created by admin on 2016/10/27.
 */
define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    var communityId = $('body').attr('communityId');
    var friendList = {};
    var initPage=1;
    friendList.init = function () {
       getConcernList(initPage);
    };

    $(document).ready(function () {


        $('body').on('click','.cancelConcern',function(){
            cancelConcern($(this));
        })
    })


    function cancelConcern(obj){
        var param={};
        var concernId=obj.attr('concernId');
        param.concernId=concernId;
        common.getData('/community/pullConcern.do',param,function(resp){
            if(resp.code=="200"){
                getConcernList(initPage);
            }
        })

    }

    function  getConcernList(page) {
        var isInit = true;
        var requestData = {};
        requestData.page = page;
        common.getData("/community/getConcernList.do", requestData, function (resp) {
            var resultData = resp.message.result;
            $('#newPage').show();
            $('.new-page-links').html("");
            if (resultData.length > 0) {
                $('.new-page-links').jqPaginator({
                    totalPages: Math.ceil(resp.message.totalCount / resp.message.pageSize) == 0 ? 1 : Math.ceil(resp.message.totalCount / resp.message.pageSize),//总页数
                    visiblePages: 3,//分多少页
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
                            getConcernList(n);
                            $('body,html').animate({scrollTop: 0}, 20);
                        }
                    }
                });
            }
            template('#concernListTmpl', '#concernList', resultData);
        })
    }



    //加载模板
    function template(tmpl, ctx, data) {
        common.render({
            tmpl: tmpl,
            context: ctx,
            data: data,
            overwrite: 1
        })
    }

    module.exports = friendList;
});