define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    var communityMessageList = {};
    var communityId = $('#temp').attr('communityId');
    var type = $('#temp').attr('type');
    var activity_cur = 1;
    communityMessageList.init = function () {
        getMyCommunity();
        getMessages(1);
    };


    $(document).ready(function () {

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

        $('#activity-signed-div').show();
        $('#activity-published-dev').hide();
        $('#activity-attended-div').hide();

        getPublishedActivitys();

        getSignedActivitys();

        getAttendActivitys();

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

        hx_update();

        setInterval(hx_update, 1000 * 60);

        $(".detail").click(function () {
            var detailId = $(this).attr('value');
            var url = '/community/communityDetail?detailId=' + detailId;
            window.open(url, '_blank');
        });


        $(".open").click(function () {
            var detailId = $(this).attr('value');
            var url = '/community/communityDetail?detailId=' + detailId;
            window.open(url, '_blank');
        });

        $('body').on('click', '.commit', function () {
            var that = this;
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
                    } else {
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            });
        })

        $('body').on('click', '.login-mk-btn .d2', function () {
            $('.store-register').fadeToggle();
            $('.bg').fadeToggle();
        })


        $('body').on('click','.delete-detail',function(){
            showDelete($(this));
        })

        $('body').on('click','#sureCancel',function(){
            sureCancel();
        })

        $('body').on('click','.si-s4 em,.si-s4 .alert-btn-esc',function(){
            ss4();
        })

        $('body').on('click', '.spread', function () {
            spread($(this));
        });

        $('body').on('click', '.collect', function () {
            collect($(this));
        });


    });

    function ss4(){
        $('.si-s4').hide();
        $('.bg').hide();
    }

    function sureCancel(){
        var param={};
        param.detailId=$('.si-s4').data('detailId');
        common.getData('/community/removeDetailById.do',param,function (resp) {
            ss4();
            getMessages(1);
        })
    }

    function showDelete(obj){
        $('.si-s4').data('detailId',obj.attr('detailId'));
        $('.si-s4').show();
        $('.bg').show();
    }

    function enterCommunityDetail(obj) {
        var communityDetailId = obj.attr('itemId');
        var requestParam = {};
        requestParam.communityDetailId = communityDetailId;
        requestParam.communityId = communityId,
            common.getData('/community/enterCommunityDetail.do', requestParam, function (resp) {
                if (resp.code == "200") {
                    var count = parseInt(obj.next().find('em').html()) + 1;
                    obj.next().find('em').html(count);
                    alert(resp.message);
                } else {
                    alert(resp.message);
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

    function getMessages(page) {
        var isInit = true;
        var requestData = {};
        requestData.page = page;
        requestData.pageSize = 10;
        requestData.communityId = communityId;
        requestData.type = type;
        common.getData("/community/getMessage.do", requestData, function (result) {
            if (result.code = "200") {
                $('.new-page-links').html("");
                if(result.message.result.length>0){
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
                }


                if (type == 1) {
                    template('#announcementTmpl', '#content', result.message.result);
                    if(result.message.result.length>0){
                        $('.announcementContent').each(function(){
                            contentDeal($(this));
                        })
                    }
                }
                if (type == 2) {
                    template('#activityTmpl', '#content', result.message.result);
                    if(result.message.result.length>0){
                        $('.activityContent').each(function(){
                            contentDeal($(this));
                        })
                    }
                }
                if (type == 3) {
                    template('#shareTmpl', '#content', result.message.result);
                    if(result.message.result.length>0){
                        $('.shareContent').each(function(){
                            contentDeal($(this));
                        })
                    }
                }
                if (type == 4) {
                    template('#meansTmpl', '#content', result.message.result);
                }
                if (type == 5) {
                    template('#homeworkTmpl', '#content', result.message.result);
                    if(result.message.result.length>0){
                        $('.homeworkContent').each(function(){
                            contentDeal($(this));
                        })
                    }
                }
                if (type == 6) {
                    template('#materialsTmpl', '#content', result.message.result);
                    if(result.message.result.length>0){
                        $('.materialsContent').each(function(){
                            contentDeal($(this));
                        })
                    }
                }
            }
        });
    }

    function spread(obj) {
        obj.closest('p').css('max-height', '783px');
        obj.closest('span').html('<em class="spread">[收起全文]</em>').removeClass('spread').addClass('collect');
    }

    function collect(obj) {
        obj.closest('p').css('max-height', '60px');
        obj.closest('span').html('<em class="spread">[展开全文]</em>').removeClass('collect').addClass('spread');
    }

    function contentDeal(obj){
        var str="<em class=\"spread\">[展开全文]</em>";
        var tempStr=obj.html().replace(/\n/g,"<br/>");
        if(tempStr.indexOf("<br/>")>-1){
            var list=tempStr.split("<br/>");
            var contentStr="";
            var totalCount=0;
            for(var j=0;j<list.length;j++){

                if(contentStr==""){
                    contentStr=list[j];
                }else{
                    contentStr=contentStr+"<br />"+list[j];
                }
                if(list[j]!=""){
                    totalCount=totalCount+list[j].length;
                }


            }
            if(list.length==1){
                if(totalCount>93){
                    obj.next().html(str);
                }
            }else if(list.length==2){
                if(totalCount>43){
                    obj.next().html(str);
                }
            }else if(list.length>2){
                obj.next().html(str);
            }
            obj.html(contentStr);
        }
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
            var url = '/community/communityDetail?detailId=' + detailId;
            window.open(url, '_blank');
        });


        $(".open").click(function () {
            var detailId = $(this).attr('value');
            var url = '/community/communityDetail?detailId=' + detailId;
            window.open(url, '_blank');
        });
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

    module.exports = communityMessageList;
});