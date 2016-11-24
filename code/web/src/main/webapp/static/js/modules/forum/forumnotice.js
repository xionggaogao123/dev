/*
 * @Author: Voyage
 * @Date:   2016-05-30 11:28:27
 * @Last Modified by:   Tony
 * @Last Modified time: 2016-06-02 15:40:55
 */

define(function (require, exports, module) {
    var Common = require('common');
    require('jquery');
    var forumNotice = {};
    var login = $('body').attr('login') == 'true';
    $(document).ready(function () {


        var menuItem = $('body').attr('menuItem');
        var userId = $('body').attr('userId');
        if (menuItem == 1) {
            $('.ul-forumset li').eq(menuItem).addClass('li-cur').siblings('.ul-forumset li').removeClass('li-cur');
            $('#post').show();
            $('#Inf').hide();
            $('#sys').hide();
            $('#friend').hide();
        } else if (menuItem == 0) {
            $('.ul-forumset li').eq(menuItem).addClass('li-cur').siblings('.ul-forumset li').removeClass('li-cur');
            $('#post').hide();
            $('#Inf').show();
            $('#sys').hide();
            $('#friend').hide();
        } else if (menuItem == 2) {
            $('.ul-forumset li').eq(menuItem).addClass('li-cur').siblings('.ul-forumset li').removeClass('li-cur');
            $('#post').hide();
            $('#Inf').hide();
            $('#sys').show();
            $('#friend').hide();
        } else if (menuItem == 3) {
            $('.ul-forumset li').eq(menuItem).addClass('li-cur').siblings('.ul-forumset li').removeClass('li-cur');
            $('#post').hide();
            $('#Inf').hide();
            $('#sys').hide();
            $('#friend').show();
        }
        $('body').on('click', '.redirect', function () {
            var request = {};
            request.fRecordId = $(this).attr('value');
            Common.getPostData('/forum/updateScan.do', request, function (resp) {
                location.href = '/forum/postDetail.do?pSectionId=' + resp.postSectionId + '&postId=' + resp.postId + '&personId=' + resp.userId + '&page=1&ti=' + new Date().getTime();
            })
        });
        getRecord(userId);

        getApplyFriend(userId);

        getFInformation(userId);

        getFSystem(userId);

        $('.btn-js').click(function () {
            var value = $(this).attr('acceptId');
            var requestPost = {};
            requestPost.applyId = value;
            Common.getData("/forum/userCenter/acceptFriend.do", requestPost, function (resp) {
                if (resp.code == 200) {
                    location.href = "/forum/applyFriend.do";
                } else {
                    alert(resp.message);
                }
            })

        });

        $('body').on('click', '#tou', function () {
            var personId = $(this).attr('deleteId');
            var requestPost = {};
            requestPost.personId = personId;
            Common.getData("/forum/userCenter/removeInf.do", requestPost, function (resp) {
                if (resp.code == 200) {
                    alert("删除成功了");
                    location.href = "/forum/myMessage.do?ti=" + new Date().getTime();
                } else {
                    alert(resp.message);
                }
            })
        });
        $('body').on('click', '.messageReply', function () {
            $('.sx-list').hide();
            $('.p-forumset .btn-fs').hide();
            $('.sx-li').show();
            $('.p-forumset .em1').show();
            var acceptName= $(this).attr('acceptName');
            var requestData = {};
            requestData.personId = $(this).attr('value');
            requestData.type = $(this).attr('type');
            Common.getData("/forum/userCenter/fMessagesList.do", requestData, function (resp) {
                var result = resp.list;
                Common.render({
                    tmpl: '#fMessagesTml',
                    data: result,
                    context: '#messagesList',
                    overwrite: 1
                });

                $('#count').html(resp.count);
                $('#nick').html(acceptName);
                $('#tou').attr('deleteId', resp.personId);

            })
        });

        $('.btn-jj').click(function () {
            var value = $(this).attr('refuseId');
            var requestPost = {};
            requestPost.applyId = value;
            Common.getData("/forum/userCenter/refuseFriend.do", requestPost, function (resp) {
                if (resp.code == 200) {
                    location.href = "/forum/applyFriend.do";
                } else {
                    alert(resp.message);
                }
            })

        });

        $('.ul-forumset li').click(function () {
            $(this).addClass('li-cur').siblings('.ul-forumset li').removeClass('li-cur');
            var value = $(this).attr('value');
            if (value == 1) {
                $('#Inf').show();
                $('#post').hide();
                $('#sys').hide();
                $('#friend').hide();
                getFInformation(userId);
            } else if (value == 2) {
                $('#post').show();
                $('#Inf').hide();
                $('#sys').hide();
                $('#friend').hide();
                getRecord(userId);
            } else if (value == 3) {
                $('#sys').show();
                $('#post').hide();
                $('#Inf').hide();
                $('#friend').hide();
                getFSystem(userId);
            } else if (value == 4) {
                $('#friend').show();
                $('#sys').hide();
                $('#post').hide();
                $('#Inf').hide();
                getApplyFriend(userId);
            }
        });
        $('.xx-box .em-bg').click(function () {
            $(this).toggleClass('em-red-bg');
            $(this).next().children('.div-set').toggleClass('show');
            $(this).children('.em4').toggleClass('show');
        });

        //$('.xx-box .p2,.xx-box .p3 .em-set>em').click(function(){
        //	$('.sx-list').hide();
        //	$('.p-forumset .btn-fs').hide();
        //	$('.sx-li').show();
        //	$('.p-forumset .em1').show();
        //});
        $('.sj-right .span1').click(function () {
            $(this).css('color', 'red')
            $('.fri-sel').slideDown();
        });
        $('.fri-sel .p1 button').click(function () {
            $('.fri-sel').slideUp();
        });
        //$('.p-forumset .btn-fs').click(function(){
        //
        //});
        $('.p-forumset .em1').click(function () {
            $(this).hide();
            $('.p-forumset .btn-fs').show();
            $('.sx-new').hide();
            $('.sx-li').hide();
            $('.sx-list').show();
        });

        $('body').on('click', '#listEm', function () {
            $(this).toggleClass('em-red-bg');
            $(this).children('.em4').toggleClass('show');
        });

        $('body').on('click', '#sendOne', function () {
            //$(this).hide();
            //$('.p-forumset .em1').show();
            //$('.sx-list').hide();
            //$('.sx-li').hide();
            //$('.sx-new').show();
            var content = $('#valhf').val();
            if (content == "") {
                alert("请输入消息内容");
                return;
            }
            var param = {};
            param.content = content;
            param.personId = $('#tou').attr('deleteId');
            Common.getPostData('/forum/userCenter/sendInf.do', param, function (resp) {
                if (resp.code == "200") {
                    //$('.sendInf').hide();
                    alert("消息已发送出去");
                    location.href = "/forum/myMessage.do?ti=" + new Date().getTime();
                } else {
                    $('.sendInf').hide();
                    alert(resp.message);
                }
            })
        });
        $('body').on('click', '.ll', function () {
            $(this).toggleClass('em-red-bg');
            $(this).next().children('.div-set').toggleClass('show');
            $(this).children('.em4').toggleClass('show');
        });

        $('body').on('click', '.lll', function () {
            var value = $(this).attr('value');
            var requestPost = {};
            requestPost.messageId = value;
            Common.getData("/forum/userCenter/removeMessage.do", requestPost, function (resp) {
                if (resp.code == 200) {
                    location.href = "/forum/myMessage.do?ti=" + new Date().getTime();
                } else {
                    alert(resp.message);
                }
            })
        });

        //$('.ul-forumset .li1').click(function(){
        //	$('.right-r').hide();
        //	$('.right-xx').show();
        //});
        //$('.ul-forumset .li2').click(function(){
        //	$('.right-r').hide();
        //	$('.right-tz').show();
        //});
        //$('.ul-forumset .li3').click(function(){
        //	$('.right-r').hide();
        //	$('.right-xt').show();
        //});
        //$('.ul-forumset .li4').click(function(){
        //	$('.right-r').hide();
        //	$('.right-qq').show();
        //});
    })

    function getRecord(userId) {
        var requestData = {};
        requestData.personId = userId;
        Common.getData("/forum/fRecordList.do", requestData, function (resp) {
            var result = resp.list;
            Common.render({
                tmpl: '#recordTml',
                data: result,
                context: '#post',
                overwrite: 1
            });

        })
    }


    function getApplyFriend(userId) {
        var requestData = {};
        requestData.personId = userId;
        Common.getData("/forum/userCenter/friendList.do", requestData, function (resp) {
            var result = resp.list;
            Common.render({
                tmpl: '#friendTml',
                data: result,
                context: '#friend',
                overwrite: 1
            });

        })
    }

    function getFInformation(userId) {
        var requestData = {};
        requestData.personId = userId;
        Common.getData("/forum/userCenter/fMessageList.do", requestData, function (resp) {
            var result = resp.list;
            Common.render({
                tmpl: '#fMessageTml',
                data: result,
                context: '#fMessage',
                overwrite: 1
            });

        })
    }

    function getFSystem(userId) {
        var requestData = {};
        requestData.personId = userId;
        Common.getData("/forum/userCenter/fSystemList.do", requestData, function (resp) {
            var result = resp.list;
            Common.render({
                tmpl: '#fSystemTml',
                data: result,
                context: '#sys',
                overwrite: 1
            });

        })
    }

    module.exports = forumNotice;
})