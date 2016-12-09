/**
 * Created by admin on 2016/10/31.
 */
/**
 * Created by admin on 2016/10/27.
 */
define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    var communityAllType = {};

    var activity_cur = 1;
    communityAllType.init = function () {
        getMyCommunity();

        //获取该社区详情列表
        getCommunityDetail();

        getPublishedActivitys();

        getSignedActivitys();

        getAttendActivitys();
    };

    $(document).ready(function () {

        $(".hx-notice").click(function () {
            window.open('/webim/index', '_blank');
        });

        $('body').on('click', '.spread', function () {
            spread($(this));
        });

        $('body').on('click', '.collect', function () {
            collect($(this));
        });

        $('body').on('click','.alert-diglog em,.alert-diglog .alert-btn-esc',function () {

            $('.alert-diglog').fadeOut();
            $('.bg').fadeOut();
        });


        hx_update();
        setInterval(hx_update, 1000 * 60);

        $('body').on('click', '.login-mk-btn .d2', function () {
            $('.store-register').fadeToggle();
            $('.bg').fadeToggle();
        });

        $('#activity-signed-div').show();
        $('#activity-published-dev').hide();
        $('#activity-attended-div').hide();


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

        if($('#target').val() == 'activity') {

            $('#my-community-span').removeClass('hd-green-cur');
            $('#myActivity-span').addClass('hd-green-cur');
            $('.container .hd-cont-f1').hide();
            $('.container .hd-cont-f2').show();
        }

    });

    function spread(obj) {
        obj.closest('p').css('max-height', '140px');
        obj.closest('span').html('<em class="spread">[收起全文]</em>').removeClass('spread').addClass('collect');
    }

    function collect(obj) {
        obj.closest('p').css('max-height', '60px');
        obj.closest('span').html('...<em class="spread">[展开全文]</em>').removeClass('collect').addClass('spread');
    }


    //获取该社区详情信息
    function getCommunityDetail() {
        var param = {};
        param.pageSize = 1;
        common.getData('/community/getAllTypeMessage.do', param, function (resp) {
            if (resp.code == "200") {
                var announcement = resp.message.announcement;
                var activity = resp.message.activity;
                var share = resp.message.share;
                var means = resp.message.means;
                var homework = resp.message.homework;
                var materials = resp.message.materials;
                template('#announcementTmpl', '#announcement', announcement);
                template('#activityTmpl', '#activity', activity);
                template('#shareTmpl', '#share', share);
                template('#meansTmpl', '#means', means);
                template('#homeworkTmpl', '#homework', homework);
                template('#materialsTmpl', '#materials', materials);

                var tempStr = "<div class=\"notice-container clearfix com-nothing\">";
                if (announcement.length == 0) {
                    var str = tempStr + "还未发布通知" + "</div>";
                    $('#announcement').append(str);
                }
                if (activity.length == 0) {
                    var str = tempStr + "还未发布活动报名" + "</div>";
                    $('#activity').append(str);
                }
                if (share.length == 0) {
                    var str = tempStr + "还未发布火热分享" + "</div>";
                    $('#share').append(str);
                }
                if (means.length == 0) {
                    var str = tempStr + "还未发布学习用品需求" + "</div>";
                    $('#means').append(str);
                }
                if (homework.length == 0) {
                    var str = tempStr + "还未发布作业" + "</div>";
                    $('#homework').append(str);
                }
                if (materials.length == 0) {
                    var str = tempStr + "还未发布学习资料" + "</div>";
                    $('#materials').append(str);
                }
            }
        })
    }


    //获取我的社区
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
                    var me = $(this);
                    $(this).click(function () {

                        $('.alert-diglog').fadeIn();
                        $('.bg').fadeIn();

                        $('.alert-diglog .alert-main span').html('确定要取消此次活动吗？');

                        $('.alert-diglog .alert-btn-sure').click(function () {
                            var requestParm = {
                                acid: acid
                            };
                            common.getData("/factivity/cancelPublish.do", requestParm, function (resp) {
                                if (resp.code == '200') {
                                    me.parent().hide();

                                    $('.alert-diglog').fadeOut();
                                    $('.bg').fadeOut();
                                } else {
                                    alert(resp.message);
                                }
                            });
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
                    $('div.signed-page').hide();
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

                    $('.alert-diglog').fadeIn();
                    $('.bg').fadeIn();

                    $('.alert-diglog .alert-main span').html('确定要取消此次报名吗？');

                    var me = $(this);
                    var acid = $(this).attr('value');

                    $('.alert-diglog .alert-btn-sure').click(function (){
                        var requestParm = {
                            acid: acid
                        };
                        common.getData("/factivity/cancelSign.do", requestParm, function (resp) {
                            if (resp.code == '200') {
                                me.parent().hide();
                                $('.alert-diglog').fadeOut();
                                $('.bg').fadeOut();
                                me.parent().hide();
                            } else {
                                alert(resp.message);
                            }
                        });
                    });
                });
            } else {
                $('#activity-signed-div img').show();
                $('#ul-activity-signed').hide();
                $('div.signed-page').hide();
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

                $('#ul-activity-attended li button').hide();
            } else {
                $('#activity-attended-div img').show();
                $('#ul-activity-attended').hide();
                $('.attended-page').hide();
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

    //加载模板
    function template(tmpl, ctx, data) {
        common.render({
            tmpl: tmpl,
            context: ctx,
            data: data,
            overwrite: 1
        })
    }

    module.exports = communityAllType;
});