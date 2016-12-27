/*
 * @Author: Tony
 * @Date:   2016-10-24 14:42:16
 * @Last Modified by:   Tony
 * @Last Modified time: 2016-10-25 10:09:10
 */

define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    var communityJoin = {};

    var acid = '';
    var select = '';

    communityJoin.init = function () {

        getMyCommunity();
        //获取热门社区
        getHotCommunity();

        getPublishedActivitys();
        getSignedActivitys();
        getAttendActivitys();
    };

    $(document).ready(function () {

        $('#activity-signed-div').show();
        $('#activity-published-dev').hide();
        $('#activity-attended-div').hide();


        $('body').on('click', '.alert-diglog em,.alert-diglog .alert-btn-esc', function () {

            $('.alert-diglog').fadeOut();
            $('.bg').fadeOut();
        });

        $('body').on('click', '#ul-activity-signed li button', function () {

            $('.alert-diglog').fadeIn();
            $('.bg').fadeIn();

            $('.alert-diglog .alert-main span').html('确定要取消此次报名吗？');
            acid = $(this).attr('value');
            select = 'signed';
        });

        $('body').on('click', '#ul-activity-published li button', function () {
            $('.alert-diglog').fadeIn();
            $('.bg').fadeIn();
            $('.alert-diglog .alert-main span').html('确定要取消此次活动吗？');
            acid = $(this).attr('value');
            select = 'published';
        });

        $('.alert-diglog .alert-btn-sure').click(function () {
            var requestParm = {
                acid: acid
            };

            if(select == 'signed') {
                common.getData("/factivity/cancelSign.do", requestParm, function (resp) {
                    if (resp.code == '200') {
                        $('.alert-diglog').fadeOut();
                        $('.bg').fadeOut();

                        getPublishedActivitys();
                        getSignedActivitys();
                        getAttendActivitys();
                    } else {
                        alert(resp.message);
                    }
                });
            } else {
                common.getData("/factivity/cancelPublish.do", requestParm, function (resp) {
                    if (resp.code == '200') {
                        $('.alert-diglog').fadeOut();
                        $('.bg').fadeOut();

                        getPublishedActivitys();
                        getSignedActivitys();
                        getAttendActivitys();
                    } else {
                        alert(resp.message);
                    }
                });
            }
        });

        $('body').on('click', '#myActivity-span', function () {
            $('#my-community-span').removeClass('hd-green-cur');
            $(this).addClass('hd-green-cur');
            $('.container .hd-cont-f1').hide();
            $('.container .hd-cont-f2').show();
        });

        $('body').on('click', '#my-community-span', function () {
            $('#myActivity-span').removeClass('hd-green-cur');
            $(this).addClass('hd-green-cur');

            $('.container .hd-cont-f1').show();
            $('.container .hd-cont-f2').hide();
        });

        $('body').on('click', '#my-community-1', function () {
            $('#my-community-2').removeClass('hd-cf-cur2');
            $('#my-community-3').removeClass('hd-cf-cur2');
            $(this).addClass('hd-cf-cur2');
            activity_cur = 1;
            renderActivity();
        });

        $('body').on('click', '#my-community-2', function () {
            $('#my-community-1').removeClass('hd-cf-cur2');
            $('#my-community-3').removeClass('hd-cf-cur2');
            $(this).addClass('hd-cf-cur2');
            activity_cur = 2;
            renderActivity();
        });

        $('body').on('click', '#my-community-3', function () {
            $('#my-community-1').removeClass('hd-cf-cur2');
            $('#my-community-2').removeClass('hd-cf-cur2');
            $(this).addClass('hd-cf-cur2');
            activity_cur = 3;
            renderActivity();
        });

        $(".hx-notice").click(function () {
            window.open('/webim/index','_blank');
        });

        hx_update();

        setInterval(hx_update,1000 * 60);

        $('.hd-nav span').click(function() {
            $(this).addClass('hd-green-cur').siblings('.hd-nav span').removeClass('hd-green-cur');
        });

        $('.btn-ok').click(function () {
            var param={};
            var searchId=$('#searchId').val();
            param.relax=searchId;
            common.getData("/community/search.do",param,function(result){
                if(result.code=="200"){
                    template('#searchCommuntyTmpl','#searchCommunty',result.message);
                    // $('.hd-cont-f2').show();
                }else{
                    alert(result.message);
                }
            })
        });

        $('body').on('click','.lookUp',function(){
            judge($(this));
        })

        $('body').on('click','.join',function(){
            var communityId=$(this).attr('communityId');
            var param={};
            param.communityId=communityId;
            common.getData('/community/join.do',param,function(resp){
                if(resp.code=="200"){
                    alert(resp.message);
                    getMyCommunity();
                }else{
                    alert(resp.message);
                }
            })
        })

        $('body').on('mouseleave','.ul-my-com li',function(){
            $('.ul-my-com li .com-hover-card .sp3').removeClass('sp33').addClass('sp-short');
            $('.ul-my-com li .com-hover-card .sp3 em').show();
            $(this).children('.com-hover-card').hide();
        });
        $('body').on('mouseover','.ul-my-com li',function(){
            $(this).children('.com-hover-card').show();
        });

        $('body').on('click','.joinCom',function () {
            var communityId=$(this).attr('cid');
            joinCommunity(communityId);
        })

        $('body').on('click', '.com-set-my-btn', function () {
            window.location.href='/community/communitySet.do';
        })


    })

    function joinCommunity(communityId){
        common.getData('/community/join.do',{communityId:communityId},function(resp){
            if(resp.code=="200"){
                getMyCommunity();
                getHotCommunity();
            }else{
                alert(resp.message);
            }
        })
    }

    function getHotCommunity(){
        common.getData('/community/hotCommunitys.do',{pageSize:9},function (resp) {
            if(resp.code=="200"){
                template('#hotCommunityTmpl','#hotCommunity',resp.message);
            }
        })
    }

    function judge(obj){
        var communityId=obj.attr('communityId');
        var param={};
        param.communityId=communityId;
        common.getData('/community/judge.do',param,function(resp){
            if(resp.code=="200"){
                if(resp.message){
                    window.location.href='/community/communityPublish.do?communityId='+communityId;
                }else{
                    alert("你不是该社区成员，不能查看该社区！");
                }
            }
        })
    }

    function template(tmpl,ctx,data){
        common.render({
            tmpl:tmpl,
            context:ctx,
            data:data,
            overwrite:1
        })
    }
    function getMyCommunity(){
        common.getData("/community/myCommunitys.do",{pageSize:9,platform:"web"},function(result){
            if(result.code="200"){
                if(undefined!=result.message.list){
                    template('#myCommunityTmpl','#myCommunity',result.message.list);
                }else{
                    template('#myCommunityTmpl','#myCommunity',result.message);
                }
            }else{
                alert(result.message);
            }
        })
    }

    function hx_update() {

        $.ajax({
            url:'/group/offlineMsgCount.do',
            success: function(resp){
                var offCount = resp.message.offlineCount;
                if(offCount > 0) {
                    $('#hx-icon').removeClass("sp2");
                    $('#hx-icon').addClass('sp1');
                } else {
                    $('#hx-icon').addClass('sp2');
                }
                $('#hx-msg-count').text('您有' + offCount + '条未读消息');
            }
        });
    }

    function getPublishedActivitys(n) {
        var requestParm = {
            page: n
        };
        var init = true;
        common.getData("/factivity/published.do", requestParm, function (resp) {
            if (resp.code == '200') {
                if(resp.message.result.length <= 0 ) {
                    $('#activity-published-dev img').show();
                    $('#ul-activity-published').hide();
                    $('.published-page').hide();
                    return;
                }
                $('.published-page').jqPaginator({
                    totalPages: resp.message.totalPages,//总页数
                    visiblePages: 10,//分多少页
                    currentPage: resp.message.page,//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (init) {
                            init = false;
                        } else {
                            getPublishedActivitys(n);
                        }
                    }
                });
                template('#activityBox', '#ul-activity-published', resp.message.result);

                $('#ul-activity-published li button').each(function () {

                    $(this).text('取消活动');
                    var acid = $(this).attr('value');
                    $(this).click(function () {
                        var requestParm = {
                            acid: acid
                        };
                        common.getData("/factivity/cancelPublish.do", requestParm, function (resp) {
                            if (resp.code == '200') {
                                alert("取消报名成功");
                                me.parent().hide();
                            } else {

                            }
                        });
                    });
                });
            }
        });
    }

    function getSignedActivitys(n) {
        var requestParm = {
            page: n
        };
        var init = true;
        common.getData("/factivity/signed.do", requestParm, function (resp) {
            if (resp.code == '200') {

                if(resp.message.result.length <= 0 ) {
                    $('#activity-signed-div img').show();
                    $('#ul-activity-signed').hide();
                    $('.signed-page').hide();
                    return;
                }
                $('.signed-page').jqPaginator({
                    totalPages: resp.message.totalPages,//总页数
                    visiblePages: 10,//分多少页
                    currentPage: resp.message.page,//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (init) {
                            init = false;
                        } else {
                            getSignedActivitys(n);
                        }
                    }
                });
                template('#activityBox', '#ul-activity-signed', resp.message.result);

                $('#ul-activity-signed li button').click(function () {

                    alert('haha');
                    var me = $(this);
                    var requestParm = {
                        acid: $(this).attr('value')
                    };
                    common.getData("/factivity/cancelSign.do", requestParm, function (resp) {
                        if (resp.code == '200') {
                            alert("取消报名成功");
                            me.parent().hide();
                        } else {

                        }
                    });
                });
            }
        });
    }

    function getAttendActivitys(n) {
        var requestParm = {
            page: n
        };
        var init = true;
        common.getData("/factivity/attended.do", requestParm, function (resp) {
            if (resp.code == '200') {
                if(resp.message.result.length <= 0 ) {
                    $('#activity-attended-div img').show();
                    $('#ul-activity-attended').hide();
                    $('.attended-page').hide();
                    return;
                }
                $('.attended-page').jqPaginator({
                    totalPages: resp.message.totalPages,//总页数
                    visiblePages: 10,//分多少页
                    currentPage: resp.message.page,//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (init) {
                            init = false;
                        } else {
                            getAttendActivitys(n);
                        }
                    }
                });
                template('#activityBox', '#ul-activity-attended', resp.message.result);
            }
        });
    }

    function renderActivity() {
        if (activity_cur === 1) {

            $('#activity-signed-div').show();
            $('#activity-published-dev').hide();
            $('#activity-attended-div').hide();

        } else if (activity_cur === 2) {

            $('#activity-signed-div').hide();
            $('#activity-published-dev').show();
            $('#activity-attended-div').hide();

        } else if (activity_cur === 3) {

            $('#activity-signed-div').hide();
            $('#activity-published-dev').hide();
            $('#activity-attended-div').show();
        }
    }

    module.exports = communityJoin;
});