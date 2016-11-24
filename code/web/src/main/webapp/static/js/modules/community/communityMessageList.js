define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    var communityMessageList = {};
    var communityId=$('#temp').attr('communityId');
    var type=$('#temp').attr('type');
    communityMessageList.init = function () {
        getMyCommunity();
        getMessages(1);
    };


    $(document).ready(function () {

        $(".detail").click(function () {
            var detailId = $(this).attr('value');
            var url='/community/communityDetail?detailId='+detailId;
            window.open(url, '_blank');
        });


        $(".open").click(function () {
            var detailId = $(this).attr('value');
            var url='/community/communityDetail?detailId='+detailId;
            window.open(url, '_blank');
        });

        $('body').on('click', '.commit', function () {
            var that=this;
            $.ajax({
                url: "/forum/loginInfo.do?date=" + new Date(),
                type: "get",
                dataType: "json",
                async: false,
                data: {},
                success: function (resp) {
                    var flag = resp.login;
                    if (flag) {
                        enterCommunityDetail($(that));
                    }else{
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            });
        })

    });

    function enterCommunityDetail(obj) {
        var communityDetailId = obj.attr('itemId');
        var requestParam = {};
        requestParam.communityDetailId = communityDetailId;
        requestParam.communityId=communityId,
            common.getData('/community/enterCommunityDetail.do', requestParam, function (resp) {
                if (resp.code == "200") {
                    var count=parseInt(obj.next().find('em').html())+1;
                    obj.next().find('em').html(count);
                    alert(resp.message);
                } else {
                    alert(resp.message);
                }
            })
    }


    //获取我的社区
    function getMyCommunity() {
        common.getData("/community/myCommunitys.do", {}, function (result) {
            if (result.code = "200") {
                template('#myCommunityTmpl', '#myCommunity', result.message);
            } else {
                alert(result.message);
            }
        })
    }

    function getMessages(page) {
        var isInit = true;
        var requestData = {};
        requestData.page = page;
        requestData.pageSize = 10;
        requestData.communityId=communityId;
        requestData.type=type;
        common.getData("/community/getMessage.do", requestData, function (result) {
            if (result.code = "200") {
                $('.new-page-links').html("");
                $('.new-page-links').jqPaginator({
                    totalPages: Math.ceil(result.message.totalCount / result.message.pageSize) == 0 ? 1 : Math.ceil(result.message.totalCount / result.message.pageSize),//总页数
                    visiblePages: 10,//分多少页
                    currentPage: parseInt(result.message.page),//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (isInit) {
                            isInit = false;
                        } else {
                            getMessages(n);
                            $('body,html').animate({scrollTop: 0}, 20);
                        }
                    }
                });

                if(type == 1){
                    template('#announcementTmpl', '#content', result.message.result);
                }
                if(type ==2){
                    template('#activityTmpl', '#content', result.message.result);
                }
                if(type ==3){
                    template('#shareTmpl', '#content', result.message.result);
                }
                if(type ==4){
                    template('#meansTmpl', '#content', result.message.result);
                }
                if(type ==5){
                    template('#homeworkTmpl', '#content', result.message.result);
                }
                if(type ==6){
                    template('#materialsTmpl', '#content', result.message.result);
                }
            }
        });
    }

    //加载模板
    function template(tmpl, ctx, data) {
        common.render({
            tmpl: tmpl,
            context: ctx,
            data: data,
            overwrite: 1
        });

        $(".detail").click(function () {
            var detailId = $(this).attr('value');
            var url='/community/communityDetail?detailId='+detailId;
            window.open(url, '_blank');
        });


        $(".open").click(function () {
            var detailId = $(this).attr('value');
            var url='/community/communityDetail?detailId='+detailId;
            window.open(url, '_blank');
        });
    }

    // $.urlParam = function(name){
    //     var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
    //     if (results==null){
    //         return null;
    //     }
    //     else{
    //         return results[1] || 0;
    //     }
    // };

    module.exports = communityMessageList;
});