/* 
 * @Author: Tony
 * @Date:   2015-07-14 16:35:19
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-07-16 13:54:23
 */

'use strict';
define(['doT', 'common', 'jquery', 'role', 'initPaginator'], function (require, exports, module) {
    /**
     *初始化参数
     */
    var wanban = {},
        Common = require('common'),
        Paginator = require('initPaginator');
    wanban.init = function() {


    };
    /*不同角色点击加深背景*/
    $(function() {
        $(".slmT ol li").click(function(event) {
            $(this).addClass("shen").siblings().removeClass("shen");
        });
    });
    /*不同角色点击加深背景*/
    /**
     * 好友列表
     * @param page
     */
    wanban.getAllFriend = function (page) {
        $.ajax({
            url: "/friendcircle/ajaxGetFriendlist.do",
            type: "post",
            datatype: "json",
            data: {
                page: page
            },
            success: function (data) {
                var option = {
                    total: data.total,
                    pagesize: 12,
                    currentpage: data.page,
                    operate: function (totalPage) {
                        $('.page-index span').each(function () {
                            $(this).off("click");
                            $(this).one("click",function () {
                                wanban.getAllFriend($(this).text());
                            });
                        });
                        $('.first-page').off("click");
                        $('.first-page').one("click",function () {
                            wanban.getAllFriend(1);
                        });
                        $('.last-page').off("click");
                        $('.last-page').one("click",function () {
                            wanban.getAllFriend(totalPage);
                        });
                    }
                };
                Paginator.initPaginator(option);
                $(".rlb").children().remove();
                Common.render({tmpl: $('#rlb'), data: {data: data.userInfos}, context: '.rlb'});
            }

        });
    };

    /**
     * 好友申请列表
     * @param page
     */
    wanban.getFriendApplyList=function(page) {
        $.ajax({
            url: "/friendcircle/getFriendApplys.do",
            datatype: "json",
            type: "post",
            data: {
                page: page
            },
            success: function (data) {
                var option = {
                    total: data.total,
                    pagesize: 12,
                    currentpage: data.page,
                    operate: function (totalPage) {
                        $('.page-index span').each(function () {
                            $(this).off("click");
                            $(this).one("click",function () {
                                wanban.getFriendApplyList($(this).text());
                            });
                        });
                        $('.first-page').off("click");
                        $('.first-page').one("click",function () {
                            wanban.getFriendApplyList(1);
                        });

                        $('.last-page').off("click");
                        $('.last-page').one("click",function () {
                            wanban.getFriendApplyList(totalPage);
                        });
                    }
                };
                Paginator.initPaginator(option);
                $(".slmB").children().filter("li").remove();
                var source = document.getElementById("slmB").innerHTML;
                var template1 = doT.template(source);
                $("#pageinator").prepend(template1({data: data.rows}));
            }
        });
    };

    /**
     * 根据角色获取好友列表
     * @param page
     * @param type
     */
    function getFriendByType(page, type) {
        $.ajax({
            url: "/friendcircle/getfriendlistgroupbyrole.do",
            type: "post",
            datatype: "json",
            data: {
                roleid: type,
                apge: page
            },
            success: function (data) {
                var option = {
                    total: data.total,
                    pagesize: 12,
                    currentpage: data.page,
                    operate: function (totalPage) {
                        $('.page-index span').each(function () {
                            $(this).off("click");
                            $(this).on("click", function () {
                                getFriendByType($(this).text(), type);
                            });
                        });
                        $('.first-page').off("click");
                        $('.first-page').on("click", function () {
                            getFriendByType(1, type);
                        });
                        $('.last-page').off("click");
                        $('.last-page').on("click", function () {
                            getFriendByType(totalPage, type);
                        });
                    }
                };
                Paginator.initPaginator(option);
                $(".rlb").children().remove();
                Common.render({tmpl: $('#rlb'), data: {data: data.userInfos}, context: '.rlb'});
            }
        });
    }


    var invitedFriendList = {data: []};

    /**
     * 邀请好友--用于发起活动时
     * @param userId
     * @param userName
     * @param userImg
     */
    function invitedFriend(userId, userName) {
        for (var i = 0; i < invitedFriendList.data.length; i++) {
            if (invitedFriendList.data[i].id == userId) {
                invitedFriendList.data.splice(i, 1);
                initChoosedFriend();
                return;
            }
        }
        invitedFriendList.data.push({
            userName: userName,
            id: userId
        });
        initChoosedFriend();
    }

    /**
     * 初始化已经选择的好友
     */
    function initChoosedFriend() {
        var allFriend = "";
        for (var i = 0; i < invitedFriendList.data.length; i++) {
            allFriend += invitedFriendList.data[i].userName + ",";
        }
        if (allFriend != "")
            allFriend = allFriend.substring(0, allFriend.length - 1);
        $("#choosedFriend").val(allFriend);
    }

    /**
     * 向已经选中的好友发送私信
     */
    function sendPrivateMsg() {
        var recipient = $.trim($('#choosedFriend').val());
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

    /**
     * 删除好友
     * @param friendId
     * @param currentNode
     */
    function deleteFriend(friendId, currentNode) {
        if (confirm("确定要删除选择的好友吗？")) {
            $.ajax({
                url: "/friendcircle/delete.do",
                type: "post",
                dataType: "json",
                data: {
                    friendId: friendId
                },
                success: function (data) {
                    if (data) {
                        alert("删除成功！");
                        $(currentNode).parent().parent().remove();
                    } else {
                        alert("删除失败！");
                    }
                }
            });
        }
    }

    /**
     * 接受申请
     * @param id
     * @param node
     */
    function accept(id, node) {
        $.ajax({
            url: "/friendcircle/accept.do",
            type: "post",
            dataType: "json",
            data: {
                sponsorId: id,
                fromDevice: '0'
            },
            success: function (data) {
                if (data.code == 200) {
                    alert("同意请求");
                    $(node).parent().parent().remove();
                } else {
                    alert(data.message);
                }
            }
        });
    }

    /**
     * 拒绝申请
     * @param id
     * @param node
     */
    function refuse(id, node) {
        $.ajax({
            url: "/friendcircle/reject.do",
            type: "post",
            dataType: "json",
            data: {
                applyId: id
            },
            success: function (data) {
                if (data.code == 200) {
                    alert("拒绝请求");
                    $(node).parent().parent().remove();
                } else {
                    alert(data.message);
                }
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
            //getInvitedFriend();
            var userName=$(this).attr("userName");
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
        $("body").on("click", ".hong", function () {
            var userId = $(this).attr("userId");
            deleteFriend(userId, $(this));
        });
        $("body").on("click", ".acceptApply", function () {
            var friendId = $(this).attr("userId");
            accept(friendId, $(this));
        });
        $("body").on("click", ".refuseApply", function () {
            var friendId = $(this).attr("userId");
            refuse(friendId, $(this));
        });
    });
    /*下拉部分*/
    $(".bj").next("ol").css({
        "display": 'none'
    });
    $(".bj").click(function (event) {
        $(this).toggleClass('jb').next("ol").toggle();
    });
    /*下拉部分*/
    /*弹出层*/
    $(document).ready(function()
    {
        $(".slbm ol li").click(function (event) {
            $(this).addClass('cut').siblings('li').removeClass('cut');
            var index = $(this).index();
            switch (index) {
                case 0:
                    getFriendByType(1, 8);
                    break;
                case 1:
                    getFriendByType(1, 2);
                    break;
                case 2:
                    getFriendByType(1, 1);
                    break;
                case 3:
                    getFriendByType(1, 4);
                    break;
            }
        });
        /* 好友申请，列表的tab栏切换 */
        $(".sl li").click(function (event) {
            $(this).addClass('cut').siblings('li').removeClass('cut');
            $(".slm .slbm").eq($(this).index()).css({
                "display": 'block'
            }).siblings().css({
                "display": 'none'
            });
            if ($(this).index() == 0) {
                wanban.getAllFriend(1);
            }
            else {
                wanban.getFriendApplyList(1);
            }
        });
        $("body").on('click', '.frinedLi', function () {
            var userId = $(this).attr("userId");
            var userName = $(this).attr("userName");
            invitedFriend(userId, userName);
        });
        var type=$("body").attr("type");
        if(type==0)
        {
            wanban.getAllFriend(1);
        }
        else if(type==1)
        {
            $(".sl li").eq(1).addClass('cut').siblings('li').removeClass('cut');
            $(".slm .slbm").eq(1).css({
                "display": 'block'
            }).siblings().css({
                "display": 'none'
            });
            wanban.getFriendApplyList(1);
        }
    });
    wanban.init();
});