/**
 * Created by jerry on 2016/10/27.
 *
 */
define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    var communitySet = {};
    var page = 1;
    var activity_cur = 1;

    var saveFlag = false;
    communitySet.init = function () {
        getHotCommunity();
        getMyCommunity(page);

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

            if (select == 'signed') {
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

        $(".hx-notice").click(function () {
            window.open('/webim/index', '_blank');
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

        hx_update();

        setInterval(hx_update, 1000 * 60);

        $('body').on('click', '#cancel', function () {
            $('.wind-com-edit').fadeOut();
            $('.bg').fadeOut();
        });

        $('body').on('click', '.btn-save', function () {

            saveOperation();
        });

        $('body').on('click', '.quit', function () {
            var communityId = $(this).attr('communidyId');
            quitCommunity(communityId);
        });

        $('body').on('mouseleave', '.ul-my-com li', function () {
            $('.ul-my-com li .com-hover-card .sp3').removeClass('sp33').addClass('sp-short');
            $('.ul-my-com li .com-hover-card .sp3 em').show();
            $(this).children('.com-hover-card').hide();
        });
        $('body').on('mouseover', '.ul-my-com li', function () {
            $(this).children('.com-hover-card').show();
        });

        $('body').on('click', '.join', function () {
            var communityId = $(this).attr('cid');
            joinCommunity(communityId);
        })

        $('body').on('click', '.sp-edit', function () {
            var cmId = $(this).attr('cmId');
            common.getData('/community/' + cmId, {}, function (resp) {
                if (resp.code == "200") {
                    $('.wind-com-edit').data('id', cmId);
                    $('#communityName').val(resp.message.name);
                    $('#communityLogo').attr('src', resp.message.logo);
                    $('#communityDesc').next().val(resp.message.desc);
                    $('#selectOpen').val(resp.message.open);
                    $('.wind-com-edit').fadeIn();
                    $('.bg').fadeIn();
                }
            })

        })

        $('body').on('click', '.setTop', function () {
            setTop($(this));
        })

        $('body').on('change', '#selectOpen', function () {
            selectShow($(this));
        })


    });

    function selectShow(obj) {
        if (obj.val() == 0) {
            $('#xt1').show();
            $('#xt2').hide();
        } else {
            $('#xt1').hide();
            $('#xt2').show();
        }
    }

    function joinCommunity(communityId) {
        common.getData('/community/join.do', {communityId: communityId}, function (resp) {
            if (resp.code == "200") {
                getMyCommunity(page);
                getHotCommunity();
            } else {
                alert(resp.message);
            }
        })
    }

    //设置置顶
    function setTop(obj) {
        var communityId = obj.attr('cmId');
        var top;
        if (obj.attr('top') == 0) {
            top = 1;
        } else {
            top = 0;
        }
        common.getData('/community/updateCommunityTop.do', {communityId: communityId, top: top}, function (resp) {
            if (resp.code == "200") {
                alert(obj.text() + "成功!");
                if (top == 0) {
                    obj.attr('top', top);
                    obj.text('置顶');
                } else {
                    obj.attr('top', top);
                    obj.text('取消置顶');
                }
            }
        })
    }


    //获取我的社区
    function getHotCommunity() {
        common.getData("/community/hotCommunitys.do", {pageSize: 9}, function (result) {
            if (result.code = "200") {
                template('#communityTmpl', '#hotCommunity', result.message);

            } else {
                alert(result.message);
            }
        })
    }


    function getMyCommunity(page) {
        var isInit = true;
        var requestData = {};
        requestData.page = page;
        requestData.platform = "web";
        $.ajax({
            type: "GET",
            data: requestData,
            url: '/community/myCommunitys.do',
            async: true,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (resp) {
                $('#myPage').html("");
                if (resp.code == "200") {
                    if (undefined != resp.message.list) {
                        var resultData = resp.message.list;
                        $('#userCount').text(resp.message.count);
                        if (resultData.length > 0) {
                            $('#myPage').jqPaginator({
                                totalPages: Math.ceil(resp.message.count / resp.message.pageSize) == 0 ? 1 : Math.ceil(resp.message.count / resp.message.pageSize),//总页数
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
                                        getMyCommunity(n);
                                        $('body,html').animate({scrollTop: 0}, 20);
                                    }
                                }
                            });
                        }
                        template('#myCommunityTmpl', '#myCommunity', resultData);
                    } else {
                        template('#myCommunityTmpl', '#myCommunity', resp.message);
                    }
                } else {
                    alert(resp.message);
                }
            }
        });
    }


    function quitCommunity(communtiyId) {
        var data = {
            communityId: communtiyId
        };
        common.getData("/community/quit.do", data, function (result) {
            if (result.code = "200") {
                alert("退出成功");
                getMyCommunity(page);
                getHotCommunity();
            } else {
                alert(result.message);
            }
        });
    }


    function validateCommunityInfo() {
        var communityName = $('#communityName').val();
        var communityDes = $('#communityDesc').next().val();
        if (communityName == "" || communityName == undefined) {
            alert("社区名称不能为空！");
            saveFlag = false;
        }
        if (communityName.length > 15) {
            alert("社区名称不能超过15个字！");
            saveFlag = false;
        }
        if (communityDes == "" || communityDes == undefined) {
            alert("社区简介不能为空！");
            rsaveFlag = false;
        }
        if (communityDes.length > 100) {
            alert("社区简介字数不能超过100！");
            saveFlag = false;
        }
        saveFlag = true;
    }

    function saveOperation() {
        var param = {};
        validateCommunityInfo();
        param.logo = $('#communityLogo').attr('src');
        param.desc = $('#communityDesc').next().val();
        param.name = $('#communityName').val();
        param.open = $('#selectOpen').val();
        param.communityId = $('.wind-com-edit').data('id');
        if(saveFlag) {
            common.getData('/community/update.do', param, function (resp) {
                if (resp.code == '200') {
                    getMyCommunity(page);
                    $('.wind-com-edit').fadeOut();
                    $('.bg').fadeOut();
                    alert("保存成功！");
                } else {
                    alert(resp.message);
                }
            });
        }
    }

    function getPublishedActivitys(n) {
        var requestParm = {
            page: n
        };
        var init = true;
        common.getData("/factivity/published.do", requestParm, function (resp) {
            if (resp.code == '200') {
                if (resp.message.result.length <= 0) {
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

                if (resp.message.result.length <= 0) {
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
                if (resp.message.result.length <= 0) {
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
                $('#ul-activity-attended li button').hide();
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

    //加载模板
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

    module.exports = communitySet;
});