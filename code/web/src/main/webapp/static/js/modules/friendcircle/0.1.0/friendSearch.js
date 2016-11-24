/* 
 * @Author: Tony
 * @Date:   2015-07-15 16:01:07
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-07-15 17:36:29
 */

'use strict';
define(['doT', 'common', 'jquery', 'role', 'initPaginator'], function (require, exports, module) {
    /**
     *初始化参数
     */
    var wanban = {},
        Common = require('common'),
        Paginator = require('initPaginator');
    /**
     * 初始化左侧导航栏
     */
    wanban.init = function () {
        Common.cal('calId');
        Common.leftNavSel();
    };

    $(document).ready(function () {
        $("#searchBtn").on("click", function () {
            wanban.searchFriend(1);
        });
        var keywords = getUrlParam("keyWords");
        $("#searchkeywords").val(decodeURI(keywords));
        $("#searchkeywords").on("keydown", function () {
            if (event.keyCode == 13) {
                wanban.searchFriend(1);
            }
        });
        $("body").on("click", ".addFriendSpan", function () {
            var uid = $(this).attr("uid");
            wanban.addFriend(uid, $(this));
        });
        var keywords = $("#searchkeywords").val();
        if (keywords != null && $.trim(keywords) != "") {
            wanban.searchFriend(1);
        }
    });
    function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return unescape(r[2]);
        return null; //返回参数值
    }

    /**
     * 添加好友
     * @param fridenId
     * @param node
     */
    wanban.addFriend = function (fridenId, node) {
        $.ajax({
            url: '/friendcircle/addFriendApply.do',
            type: 'post',
            dataType: "json",
            data: {
                userId: '${currentUser.id}',
                friendId: fridenId
            },
            success: function (result) {
                if (result) {
                    alert("好友申请已发出");
                } else {
                    alert("添加失败");
                }
            }
        });
        $(node).replaceWith('申请已发出');
    };
    /**
     * 搜索好友
     */
    wanban.searchFriend = function (page) {
        var schoolType = parseInt($("#geo  option:selected").val());
        var roleType = parseInt($("#role  option:selected").val());
        var keywords = $("#searchkeywords").val();
        if (keywords == null || $.trim(keywords) == "") {
            alert("搜索关键字不能为空！");
            return;
        }
        keywords = $.trim(keywords);
        $.ajax({
            url: '/friendcircle/search.do',
            type: 'post',
            dataType: "json",
            data: {
                schoolType: schoolType,
                roleType: roleType,
                keyWord: keywords,
                page: page
            },
            success: function (result) {
                if (result) {
                    var option = {
                        total: result.total,
                        pagesize: result.pageSize,
                        currentpage: result.page,
                        operate: function (totalPage) {
                            $('.page-index span').each(function () {
                                $(this).off("click");
                                $(this).on("click", function () {
                                    wanban.searchFriend($(this).text());
                                });
                            });
                            $('.first-page').off("click");
                            $('.first-page').on("click", function () {
                                wanban.searchFriend(1);
                            });
                            $('.last-page').on("click");
                            $('.last-page').off("click", function () {
                                wanban.searchFriend(totalPage);
                            });
                        }
                    };
                    Paginator.initPaginator(option);
                    if (result.res == null || result.res == undefined || result.res.length < 1) {
                        $(".rlb").empty();
                        alert("亲，木有找到符合条件的好友T-T");
                    } else {
                        $(".rlb").empty();
                        Common.render({tmpl: $('#rlb'), data: {data: result.res}, context: '.rlb'});
                    }
                } else {
                    alert("服务器繁忙，请稍后");
                }
            }
        });
    };
    /**
     * 向已经选中的好友发送私信
     */
    function sendPrivateMsg() {
        var recipient = $.trim($('#choosedFriend').val());
        ;
        var pm = $.trim($('#pm-content').val());

        if (recipient == '' || pm == '') {
            alert("请输入联系人和私信内容。");
            return;
        }
        $.ajax({
            url: "/letter/add.do",
            type: "post",
            data: {
                'recipient': recipient,
                'message': pm
            },
            success: function (data) {
                alert("发送成功！");
                $(".gay,.hylb").fadeOut();
            },
            error: function (e) {
                alert('服务器响应请求失败，请稍后再试。');
            }
        });
    }

    /*弹出层*/
    $(function () {
        var L = 0;
        var T = 0;
        L = $(window).width() / 2 - $(".hylb").width() / 2;
        T = $(window).height() / 2 - $(".hylb").height() / 2;
        $(".hylb").css({
            'left': L,
            'top': T
        });
        $(".gay").height($(document).height());
        $("body").on("click", (".xsf"), function (event) {
            /* Act on the event */
            var userName = $(this).attr("userName");
            $("#choosedFriend").val(userName);
            $("#pm-content").val("");
            $(".gay,.hylb").fadeIn();
        });
        $("body").on("click", ".hylb .gb", function (event) {
            /* Act on the event */
            $(".gay,.hylb").fadeOut();
        });
        $("body").on("click", ".hylb_left button", function (event) {
            /* Act on the event */
            sendPrivateMsg();
        });
    });
    /*下拉部分*/
    $(".bj").next("ol").css({
        "display": 'none'
    });
    $(".bj").click(function (event) {
        $(this).toggleClass('jb').next("ol").toggle();
    });
    wanban.init();
});