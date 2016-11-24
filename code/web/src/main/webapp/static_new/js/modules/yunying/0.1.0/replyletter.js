/*
 * @Author: Tony
 * @Date:   2015-07-15 16:01:07
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-07-16 17:19:46
 */

'use strict';
define(['doT', 'common', 'jquery', 'initPaginator'], function (require, exports, module) {
    /**
     *初始化参数
     */
    var wanban = {},
        Common = require('common'),
        Paginator = require('initPaginator');

    var replyId = $("body").attr("replyId");
    var userid = $("body").attr("userid");
    var letterId = "";

    wanban.init = function() {


    };
    /**
     * 发送私信
     * @constructor
     */
    function SendReply() {
        var pm = $.trim($('#pm-content').val());
        if (pm == '') {
            alert("请输入回复内容。");
            return;
        }

        $.ajax({
            url: "/yunying/reply.do",
            type: "post",
            data: {
                'userid':userid,
                'recipient': replyId,
                'message': pm
            },
            success: function (data) {
                if (data.status == 'error') {
                    alert(data.errorMessage);
                } else {
                    $('#pm-content').val('');
                    alert('发送成功');
                    wanban.GetReplyList();
                }
            },
            error: function (e) {
                alert('服务器响应请求失败，请稍后再试');
            }
        });
    }

    /**
     * 获取回复的私信列表
     * @param tpage
     * @constructor
     */
    wanban.GetReplyList = function (tpage) {
        var page = tpage || 1;
        $.ajax({
            url: "/yunying/getreplyletters.do",
            type: "get",
            data: {
                'userid':userid,
                'replyId': replyId,
                page: page
            },
            dataType: "json",
            success: function (data) {
                var option = {
                    total: data.total,
                    pagesize: 10,
                    currentpage: page,
                    operate: function (totalPage) {
                        $('.page-index span').each(function () {
                            $(this).off("click");
                            $(this).one("click",function () {
                                wanban.GetReplyList($(this).text());
                            });
                        });
                        $('.first-page').off("click");
                        $('.first-page').one("click",function () {
                            wanban.GetReplyList(1);
                        });

                        $('.last-page').off("click");
                        $('.last-page').one("click",function () {
                            wanban.GetReplyList(totalPage);
                        });
                    }
                };
                Paginator.initPaginator(option);
                $("#receiverName").children().remove();
                Common.render({tmpl: $('#receiverNameJs'), data: {data: data.recipient}, context: '#receiverName'});
                $('.tan2').empty();
                Common.render({tmpl: $('#clearAllJs'), data: {data: data.recipient}, context: '.tan2'});
                $("#replyListOl").children().remove();
                Common.render({
                    tmpl: $('#replyListJs'),
                    data: {data: data.rows, recipient: data.recipient},
                    context: '#replyListOl'
                });
            }
        });
    };
    /**
     * 清空所有私信消息
     * @constructor
     */
    function ClearReplyList() {
        $.ajax({
            url: "/letter/deleteallreply.do",
            type: "get",
            data: {
                'replyId': replyId
            },
            success: function (data) {
                if (data.status == 'ok') {
                    //alert("删除成功");
                    setTimeout(function () {
                        location.href = '/message'
                    }, 500);
                } else {
                    //alert('请求错误。');
                }
            },
            error: function (e) {
                alert('服务器响应请求失败，请稍后再试。');
            }
        });
        $(".gay,.tan2").fadeOut();
    }

    /**
     * 删除回复
     * @constructor
     */
    function DelReply() {
        $.ajax({
            url: "/yunying/deletereply.do",
            type: "get",
            data: {
                'userid':userid,
                'pairId': replyId,
                'letterId': letterId
            },
            success: function (data) {
                if (data.status == 'ok') {
                    alert('删除成功');
                    wanban.GetReplyList(1);
                } else {
                    alert('请求错误。');
                }
            },
            error: function (e) {
                alert('服务器响应请求失败，请稍后再试。');
            }
        });
        $(".gay,.tan1").fadeOut();
    }

    /*字数限制*/
    $(document).keyup(function () {
        var _textarea = $('.toname textarea'),
            text = _textarea.val(),
            counter = text.length;
        if (1000 - counter < 0) {
            _textarea.val(text.substr(0, 1000));
            counter = 1000;
        }
        $('label').text('（限' + (1000 - counter) + '个字）');
    });

    /*字数限制*/
    $(document).ready(function () {
        $("body").on("click", "#sendPrivateMsgBtn", function () {
            SendReply();
        });
        /**弹出层1**/
        var L = 0;
        var T = 0;
        L = $(window).width() / 2 - $(".tan1").width() / 2;
        T = $(window).height() / 2 - $(".tan1").height() / 2;
        $(".tan1").css({
            'left': L,
            'top': T
        });
        $(".gay").height($(document).height());
        $("body").on("click", ".yxj span", function (event) {
            /* Act on the event */
            letterId = $(this).attr("letterId");
            $(".gay,.tan1").fadeIn();
        });
        $("body").on("click", ".tan1 p span,button.huidu", function (event) {
            /* Act on the event */
            $(".gay,.tan1").fadeOut();
        });
        $("body").on("click", "button.enter", function (event) {
            /* Act on the event */
            DelReply();
        });
        /**--弹出层1--**/
        /**弹出层2**/
        $(".tan2").css({
            'left': L,
            'top': T
        });
        $(".hsx").click(function (event) {
            /* Act on the event */
            $(".gay,.tan2").fadeIn();
        });
        $("body").on("click", ".tan2 p span,button.huidu", function (event) {
            /* Act on the event */
            $(".gay,.tan2").fadeOut();
        });
        $("body").on("click", "button.enter2", function (event) {
            /* Act on the event */
            ClearReplyList();
        });
        /**--弹出层2--**/
    });
    wanban.init();
    wanban.GetReplyList(1);
});