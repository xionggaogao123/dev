/**
 * Created by admin on 2016/11/7.
 */
/*
 * @Author: Tony
 * @Date:   2016-10-24 14:42:16
 * @Last Modified by:   Tony
 * @Last Modified time: 2016-10-25 10:09:10
 */

define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    var communityJoin = {};
    var initPage = 1;
    var menuItem = $('body').attr('menuItem');
    communityJoin.init = function () {

        getMyCommunity();

        if (menuItem == 1) {
            $('#friendInform').show();
            $('#SysInfo').hide();
            //获取玩伴通知列表
            getFriendApplys(initPage);
        } else if (menuItem == 2) {
            $('#friendInform').show();
            $('#SysInfo').hide();
            //获取我的玩伴列表
            $('#title').html("我的玩伴列表");
            $('#friendApply').removeClass('ul-pm-notice').addClass('ul-member-list').addClass('clearfix');
            myPartners();
        } else {
            //获取我的系统消息
            $('#friendInform').hide();
            $('#SysInfo').show();
            getSystemInfo(1);
        }
    }
    $(document).ready(function () {

        $(".hx-notice").click(function () {
            window.open('/webim/index', '_blank');
        });

        hx_update();

        setInterval(hx_update, 1000 * 60);

        $('body').on('click', '.accept', function () {
            var applyId = $(this).attr('applyId');
            acceptOrRefuseFriend(applyId, '/forum/userCenter/acceptFriend.do');
        })

        $('body').on('click', '.refuse', function () {
            var applyId = $(this).attr('applyId');
            acceptOrRefuseFriend(applyId, '/forum/userCenter/refuseFriend.do');
        })

        $('body').on('click', '.alert-btn-esc,.si-s1 em, .si-s3 em', function () {
            $('.bg').fadeOut();
            $('.sign-alert').fadeOut();
        })

        $('body').on('click', '.ul-member-list li .p1 img', function () {
            $('.si-s1').fadeIn();
            $('.bg').fadeIn();
            $('#remark').val($(this).prev().text());
            $('#friendApply').data("remarkId", $(this).attr('remarkId'));
            $('#friendApply').data("userId", $(this).attr('userId'));
        });

        $('body').on('click', '.cancelFriend', function () {
            $('.si-s3').fadeIn();
            $('.bg').fadeIn();
            $('.si-s3').find('.alert-main').find('em').html($(this).attr('nickName'));
            $('#friendApply').data("cancelFriendId", $(this).attr('userId'));
        })

        $('body').on('click', '#confirm', function () {
            setNickName();
        })

        $('body').on('click', '#sureCancel', function () {
            sureCancel();
        })

        $('body').on('click', '#sysinfocount', function () {
            getSystemInfo(1);
            dealElement($(this));
        })

        $('body').on('click', '#applycount', function () {
            getValidateInfo(1);
            dealElement($(this));
        })

        $('body').on('click', '.i-retry', function () {
            $('.si-retry').data("cmId", $(this).attr('arg'));
            $('#retryName').html($(this).attr('arg1'));
            $('.si-retry').fadeIn();
            $('.bg').fadeIn();
        })

        $('body').on('click', '#retryCancel,.si-retry em', function () {
            closeRetry();
        })

        $('body').on('click', '.agreeApply', function () {
            var userName = $(this).attr('arg3');
            if (confirm("你确定同意来自" + userName + "的申请吗？")) {
                reviewApply($(this), 0);
            }
        })

        $('body').on('click', '.refuseApply', function () {
            var userName = $(this).prev().attr('arg3');
            if (confirm("你确定拒绝来自" + userName + "的申请吗？")) {
                reviewApply($(this).prev(), 1);
            }
        })

        $('body').on('click', '#applytip', function () {
            applytip();
        })

    })

    function closeRetry() {
        $('.si-retry').fadeOut();
        $('.bg').fadeOut();
    }

    function applytip() {
        var msg = $('#beizhumsg').val();
        if(msg.length>40){
            alert("备注长度不能超过40个!");
            return;
        }
        var communityId = $('.si-retry').data("cmId");
        common.getData('/community/join', {communityId: communityId, msg: msg}, function (resp) {
            alert(resp.message);
            $('#beizhumsg').val("");
            closeRetry();
            getValidateInfo(1);
        })
    }


    function reviewApply(obj, review) {
        var param = {
            reviewKeyId: obj.attr('arg1'),
            userId: obj.attr('arg0'),
            communityId: obj.attr('arg2'),
            approvedStatus: review
        }

        common.getData('/community/reviewApply', param, function (resp) {
            alert(resp.message);
            getValidateInfo(1);
        })

    }


    function dealElement(obj) {
        obj.addClass('span-sys').siblings('.tab-sys span').removeClass('span-sys');
    }

    function sureCancel() {
        var param = {};
        param.userIds = $('#friendApply').data("cancelFriendId");
        common.getData('/friend/delete.do', param, function (result) {
            if (result) {
                myPartners();
                alert("解除关系成功");
            } else {
                alert("解除关系失败");
            }
            $('.si-s3').fadeOut();
            $('.bg').fadeOut();
        })
    }

    function setNickName() {
        var remark = $('#remark').val();
        if (remark == "" || remark == undefined) {
            alert("备注名不能为空！");
            return;
        }
        var param = {};
        param.remarkId = $('#friendApply').data("remarkId");
        param.endUserId = $('#friendApply').data("userId");
        param.remark = remark;
        common.getData('/community/setRemark.do', param, function (resp) {
            if (resp.code == "200") {
                $('.bg').fadeOut();
                $('.sign-alert').fadeOut();
                myPartners();
            }
        })

    }

    function myPartners() {
        common.getData('/community/getMyPartners.do', {}, function (resp) {
            if (resp.code == "200") {
                template('#myPartnersTmpl', '#friendApply', resp.message);
            }
        })
    }


    function getValidateInfo(page) {
        var isInit = true;
        var requestData = {};
        requestData.page = page;
        $('#sysNotice').show();
        $('#mySystemInfo').hide();
        common.getData("/community/getValidateInfos.do", requestData, function (resp) {
            var resultData = resp.message.list;
            $('#sysInfoPage').html("");
            if (resultData.length > 0) {
                $('#sysInfoPage').jqPaginator({
                    totalPages: Math.ceil(resp.message.count / resp.message.pageSize) == 0 ? 1 : Math.ceil(resp.message.count / resp.message.pageSize),//总页数
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
                            getValidateInfo(n);
                            $('body,html').animate({scrollTop: 0}, 20);
                        }
                    }
                });
            }
            template('#sysNoticeTmpl', '#sysNotice', resultData);
        })
    }

    function getSystemInfo(page) {
        var isInit = true;
        var requestData = {};
        requestData.page = page;
        $('#sysNotice').hide();
        $('#mySystemInfo').show();
        common.getData("/community/getCommunitySysInfo.do", requestData, function (resp) {
            var resultData = resp.list;
            $('#sysInfoPage').html("");
            if (resultData.length > 0) {
                $('#sysInfoPage').jqPaginator({
                    totalPages: Math.ceil(resp.count / resp.pageSize) == 0 ? 1 : Math.ceil(resp.count / resp.pageSize),//总页数
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
                            getSystemInfo(n);
                            $('body,html').animate({scrollTop: 0}, 20);
                        }
                    }
                });
            }
            template('#mySystemInfoTmpl', '#mySystemInfo', resultData);
        })
    }

    function acceptOrRefuseFriend(applyId, url) {
        var param = {};
        param.applyId = applyId;
        common.getData(url, param, function (resp) {
            if (resp.code == "200") {
                getFriendApplys(initPage);
            } else {
                alert(resp.message);
            }
        })
    }

    function getFriendApplys(page) {
        var isInit = true;
        var requestData = {};
        requestData.page = page;
        common.getData("/community/getFriendApplys.do", requestData, function (resp) {
            var resultData = resp.message.result;
            $('#friendInformPage').html("");
            if (resultData.length > 0) {
                $('#friendInformPage').jqPaginator({
                    totalPages: Math.ceil(resp.message.totalCount / resp.message.pageSize) == 0 ? 1 : Math.ceil(resp.message.totalCount / resp.message.pageSize),//总页数
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
                            getFriendApplys(n);
                            $('body,html').animate({scrollTop: 0}, 20);
                        }
                    }
                });
            }
            template('#friendApplyTmpl', '#friendApply', resultData);
        })
    }

    function template(tmpl, ctx, data) {
        common.render({
            tmpl: tmpl,
            context: ctx,
            data: data,
            overwrite: 1
        })
    }

    function hx_update() {

        $.ajax({
            url: '/group/offlineMsgCount.do',
            success: function (resp) {
                var hx_notice = $('.hx-notice span');
                var offCount = resp.message.offlineCount;

                if (offCount > 0) {
                    $('#hx-icon').removeClass("sp2");
                    $('#hx-icon').addClass('sp1');
                } else {
                    $('#hx-icon').addClass('sp2');
                }
                $('#hx-msg-count').text('您有' + offCount + '条未读消息');
            }
        });
    }

    function getMyCommunity() {
        common.getData("/community/myCommunitys.do", {pageSize: 9, platform: "web"}, function (result) {
            if (result.code = "200") {
                if (undefined != result.message.list) {
                    template('#myCommunityTmpl', '#myCommunity', result.message.list);
                } else {
                    template('#myCommunityTmpl', '#myCommunity', result.message);
                }
            } else {
                alert(result.message);
            }
        })
    }

    module.exports = communityJoin;
});