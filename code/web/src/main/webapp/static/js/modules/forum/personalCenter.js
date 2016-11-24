/*
 * @Author: Tony
 * @Date:   2016-06-13 17:33:07
 * @Last Modified by:   Tony
 * @Last Modified time: 2016-06-15 12:12:08
 */

define(function (require, exports, module) {
    var Common = require('common');
    require('jquery');
    require('pagination');
    var personalCenter = {};
    var page = 1;
    var sortType = 2;
    var concernFlag=$('body').attr('concernFlag');
    var zan = 1;
    var personId; //发帖信息用户Id

    personalCenter.init = function () {

        personId = $('#personId').val();

        postList(page, 0);//发帖列表

        replyList(page);
    };
    $(document).ready(function () {
        $('.personal-nav span').click(function () {
            $(this).addClass('personal-span-cur').siblings('.personal-nav span').removeClass('personal-span-cur');
        });
        $('.personal-nav .span1').click(function () {
            $('.personal-infor').show();
            $('.personal-theme').hide();
        });
        $('.personal-nav .span2').click(function () {
            $('.personal-theme').show();
            $('.personal-infor').hide();
            $('#newPage1').hide();
        });

        $('#applyFriend').click(function () {
            $.ajax({
                url: "/forum/loginInfo.do?date=" + new Date(),
                type: "get",
                dataType: "json",
                data: {},
                success: function (resp) {
                    if(resp.login){
                        $('.apply').show();
                        $('.bg').show();
                    } else {
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            });
        });

        $('#sendMessage').click(function () {
            $.ajax({
                url: "/forum/loginInfo.do?date=" + new Date(),
                type: "get",
                dataType: "json",
                data: {},
                success: function (resp) {
                    if(resp.login){
                        $('.sendInf').show();
                        $('.bg').show();
                    } else {
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            });
        });

        $('#concern').click(function(){
            $.ajax({
                url: "/forum/loginInfo.do?date=" + new Date(),
                type: "get",
                dataType: "json",
                data: {},
                success: function (resp) {
                    if(resp.login){
                        concern();
                    } else {
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            });
        })


        $('.apply .p1 em').click(function () {
            $('.apply').hide();
            $('.bg').hide();
        });

        $('.sendInf .p1 em').click(function () {
            $('.sendInf').hide();
            $('.bg').hide();
        });


        $('#confirm').click(function () {
            var contentValue = $.trim($('#contentValue').val());
            if (contentValue == "") {
                alert("请输入附言");
                return;
            }
            if (contentValue.length > 10) {
                alert("附言不能超过10个字");
                return;
            }
            var param = {};
            param.content = contentValue;
            param.personId = $('#personId').val();
            Common.getPostData('/forum/userCenter/applyFriend.do', param, function (resp) {
                if (resp.code == "200") {
                    $('.apply').hide();
                    $('.bg').hide();
                    alert("好友申请已发出");
                } else {
                    $('.apply').hide();
                    $('.bg').hide();
                    alert(resp.message);
                }
            });
        });

        $('#confirmSend').click(function () {
            var contentValue = $('#content').val();
            if (contentValue == "") {
                alert("请输入消息内容");
                return;
            }
            var param = {};
            param.content = contentValue;
            param.personId = $('#personId').val();
            Common.getPostData('/forum/userCenter/sendInf.do', param, function (resp) {
                if (resp.code == "200") {
                    $('.sendInf').hide();
                    $('.bg').hide();
                    alert("消息已发送出去");
                } else {
                    $('.sendInf').hide();
                    $('.bg').hide();
                    alert(resp.message);
                }
            });
        });

        $('.p-theme em').click(function () {
            $(this).addClass('bold1').siblings('.p-theme em').removeClass('bold1');
        });
        $('.p-theme .em1').click(function () {
            $('.theme-zt').show();
            $('.theme-hf').hide();

            $('#newPage1').hide();
            $('#newPage').show();
        });
        $('.p-theme .em2').click(function () {
            $('.theme-hf').show();
            $('.theme-zt').hide();

            $('#newPage1').show();
            $('#newPage').hide();
        });
    })


    function concern(){
        var personalId=$('#personId').val();
        var param={};
        param.concernId=personalId;
        param.remove=concernFlag;
        Common.getData('/community/concernPerson.do',param,function(resp){
            if(resp.code=="200"){
                if(resp.message==1){
                    $('#concern').html("关注TA");
                    concernFlag=0;
                }else{
                    $('#concern').html("取消关注");
                    concernFlag=1;
                }

            }
        })
    }
    function postList(page, gtTime) {
        var isInit = true;
        var requestData = {};
        requestData.sortType = sortType;
        requestData.page = page;
        requestData.pageSize = 15;
        requestData.classify = -1;
        //requestData.cream=-1;
        requestData.gtTime = gtTime;
        requestData.postSection = "";
        requestData.zan = zan;
        requestData.person = $('#personId').val();
        //requestData.top=-1;
        Common.getData("/forum/fPosts.do?ti=" + new Date().getTime(), requestData, function (resp) {
            var posts = resp.list;
            $('#newPage').show();
            $('#newPage').html("");
            if (posts.length > 0) {
                $('#newPage').jqPaginator({
                    totalPages: Math.ceil(resp.count / requestData.pageSize) == 0 ? 1 : Math.ceil(resp.count / requestData.pageSize),//总页数
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
                            postList(n, gtTime);
                            $('body,html').animate({scrollTop: 0}, 20);
                        }
                    }
                });
            }
            Common.render({
                tmpl: '#postListTml',
                data: posts,
                context: '#postList',
                overwrite: 1
            });

        })

    }

    function replyList(page) {
        var isInit = true;
        var requestData = {};
        requestData.page = page;
        requestData.pageSize = 15;
        requestData.sortType = sortType;
        requestData.person = $('#personId').val();
        Common.getData("/forum/fPostsByReplyPerson.do?ti=" + new Date().getTime(), requestData, function (resp) {
            var replies = resp.list;
            $('#newPage1').show();
            $('#newPage1').html("");
            if (replies.length > 0) {
                $('#newPage1').jqPaginator({
                    totalPages: Math.ceil(resp.count / requestData.pageSize) == 0 ? 1 : Math.ceil(resp.count / requestData.pageSize),//总页数
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
                            replyList(n);
                            $('body,html').animate({scrollTop: 0}, 20);
                        }
                    }
                });
            }
            Common.render({
                tmpl: '#replyListTml',
                data: replies,
                context: '#replyList',
                overwrite: 1
            });

        })

        $('.theme-hf .p1').click(function () {
            /* Act on the event */
            $(this).next('.p2').slideToggle();
            $(this).toggleClass('p-b');
        });
    }

    module.exports = personalCenter;
});
