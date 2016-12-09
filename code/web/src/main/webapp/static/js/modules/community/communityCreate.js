/*
 * @Author: Tony
 * @Date:   2016-10-24 14:42:16
 * @Last Modified by:   Tony
 * @Last Modified time: 2016-10-25 10:38:32
 */

define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common=require('common');
    var communityCreate = {};
    var acid = '';
    var select = '';
    communityCreate.init = function () {


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

        $('.hd-nav span').click(function () {
            $(this).addClass('hd-green-cur').siblings('.hd-nav span').removeClass('hd-green-cur');
        });
        $('.com-cre-infor p span i').click(function(){
            $(this).next('em').toggleClass('erw-em1').toggleClass('erw-em2');

                var id = $('#zk').text();
                if (id.indexOf("展开") > -1) {
                    $('#zk').text('收起');
                } else if (id.indexOf('收起') > -1) {
                    $('#zk').text('展开');
                }
        });

        hx_update();

        setInterval(hx_update,1000 * 60);


        $('body').on('click', '.com-set-my-btn', function () {
            window.location.href='/community/communitySet.do';
        });

        $('body').on('mouseleave','.ul-my-com li',function(){
            $('.ul-my-com li .com-hover-card .sp3').removeClass('sp33').addClass('sp-short');
            $('.ul-my-com li .com-hover-card .sp3 em').show();
            $(this).children('.com-hover-card').hide();
        });
        $('body').on('mouseover','.ul-my-com li',function(){
            $(this).children('.com-hover-card').show();
        });

        $('body').on('click','.join',function () {
            var communityId=$(this).attr('cid');
            joinCommunity(communityId);
        })

        $('.btn-ok').click(function () {
            //先验证信息 
            if(validateCommunityInfo()){
                if(judgeCreate($('#communityName').val())){
                    var param={};
                    param.logo=$('#imageUrl').attr('src');
                    param.desc=$('#communityDes').val();
                    param.name=$('#communityName').val();
                    param.open=$('#selectOpen').val();
                    common.getData('/community/create.do',param,function(resp){
                        if(resp.code=="200"){
                            $('#image').attr('src',resp.message.logo);
                            $('#comm_name').text(resp.message.name);
                            $('.sq-nm').html(resp.message.name);
                            $('#comm_id').text("社区ID:"+resp.message.searchId);
                            $('#QRcode').attr('src',resp.message.qrUrl);
                            $('.com-l1').hide();
                            $('.com-l2').show();
                            getMyCommunity();
                            getHotCommunity();
                        }else{
                            alert(resp.message);
                            return;
                        }

                    })
                }


            }
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

    function judgeCreate(communityName){
        var flag=true;
        common.getData('/community/judgeCreate.do',{communityName:communityName},function(resp){
            if(resp.message){
                flag=false;
                alert("该社区名称已存在！");
            }
        });
        return flag;
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

    /**
     * 获取热门社区
     */
    function getHotCommunity(){
        common.getData('/community/hotCommunitys.do',{pageSize:9},function (resp) {
            if(resp.code=="200"){
                template('#hotCommunityTmpl','#hotCommunity',resp.message);
            }
        })
    }

    function validateCommunityInfo(){

        var communityName=$('#communityName').val();
        var communityDes=$('#communityDes').val();
        if(communityName==""||communityName==undefined){
            alert("社区名称不能为空！");
            return false;
        }

        if(communityName.length>15){
            alert("社区名称不能超过15个字！");
            return false;
        }

        if(communityDes==""||communityDes==undefined){
            alert("社区简介不能为空！");
            return false;
        }
        if(communityDes.length>150){
            alert("社区简介字数不能超过150！");
            return false;
        }


        return true;

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

    function hx_update() {

        $.ajax({
            url:'/group/offlineMsgCount.do',
            success: function(resp){
                var hx_notice = $('.hx-notice span');
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


    module.exports = communityCreate;
});