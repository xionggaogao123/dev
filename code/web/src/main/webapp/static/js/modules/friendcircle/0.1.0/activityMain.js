/**
 * Created by qiangm on 2015/7/27.
 */
define(function (require, exports, module) {

    require("jquery");
    require("doT");
    require("rome");

    var Paginator = require('initPaginator'),
        leftBar = require("leftBar");
    var activityMain = {},
        Common = require('common');
    activityMain.init = function () {
        Common.cal('calId');
        Common.leftNavSel();
        activityMain.appendHtml();
        activityMain.initPage();

    };
    /**
     * 上方Tabs切换
     */
    function tabInit() {
        $(document).ready(function () {
            $(function () {
                $('.wanban-head ul li').click(function (event) {
                    $(this).addClass('cur').siblings('li').removeClass('cur');
                    $('.listmain').eq($(this).index()).css({
                        "display": 'block'
                    }).siblings().css({
                        "display": 'none'
                    });
                    switch ($(this).index()) {
                        case 0:
                            getRecentAct(1);
                            break;
                        case 1:
                            getFriendAct(1);
                            break;
                        case 2:
                            getRecommendActivity(1);
                            break;
                        case 3:
                            getMyJoinActivity(1);
                            break;
                        case 4:
                            getMyPromoteActivity(1);
                            break;
                    }
                });
            });
        });
    }

    $(document).ready(function () {
        $('body').on('click', '.cancelHref', function () {
            var actId = $(this).attr('actid');
            cancelActivity(actId, $(this));
        });
        $('body').on('click', '.quitActivity', function () {
            var actId = $(this).attr('actid');
            quitActivity(actId, $(this));
        });
        $("#inviteFriend").on('click', function () {
            initChoosedFriend();
            showAllFriends();
            $(".gay").height($(document).height());
            $(".gay,.hylb").fadeIn();
        });
        $(".gb").click(function () {
            $(".gay,.hylb").hide();
        });
        $(".closeWindow").click(function () {
            $(".gay,.hylb").hide();
        });
        $("body").on('click', '.frinedLi', function () {
            var userId = $(this).attr("userId");
            var userName = $(this).attr("userName");
            var userImg = $(this).attr("userImg");
            invitedFriend(userId, userName, userImg)
        });
        $("body").on('click', '.deleteFriend', function () {
            var userId = $(this).parent().parent().attr("uid");
            deleteFriend(userId, $(this).parent().parent());
        });
        $("#coverImage").hide();
        $(".replaceImg").hide();
        require.async("ajaxfileupload", function (ajaxfileupload) {
            $(".addCoverImg").on('click', function () {
                var ie = navigator.appName == "Microsoft Internet Explorer" ? true : false;
                if (ie) {
                    document.getElementById("file").click();
                    document.getElementById("filename").value = document.getElementById("file").value;
                } else {
                    var a = document.createEvent("MouseEvents");
                    a.initEvent("click", true, true);
                    document.getElementById("file").dispatchEvent(a);
                }
                $(".file").off("change", function () {
                });
                $(".file").on("change", function () {
                    ajaxFileUploadPicture();
                });
            });
        });
        $(".delCoverImg").on("click", function () {
            $("#coverImage").hide();
            $(".replaceImg").hide();
            $("#coverImage").attr("src", "");
        });
        /**
         * 发起活动
         */
        $("#launchActivity").on('click', function () {
            launchActivity();
        });
    });
    /**
     * 发起活动前期检查
     */
    function launchActivity() {
        var name = jQuery("#nope").val();
        if (!name || (name.length > 10)) {
            alert("活动名称非空并且不超过10个字符");
            return;
        }
        var beginTimeStr;
        var endTimeStr;
        try {
            beginTimeStr = jQuery("#dt").val().replace(/-/g, "/") + ":00";
            endTimeStr = jQuery("#dt1").val().replace(/-/g, "/") + ":00";
            if (beginTimeStr == ":00") {
                alert("请输入活动开始时间");
                return;
            }
            if (endTimeStr == ":00") {
                alert("请输入活动结束时间");
                return;
            }

            var beginTime = new Date(beginTimeStr);
            var endTime = new Date(endTimeStr);

            if (isNaN(beginTime.getTime()) || isNaN(endTime.getTime())) {
                alert("请正确输入活动时间");
                return;
            }
            if (endTime.getTime() < beginTime.getTime()) {
                alert("您发起的活动'结束时间'早于'开始时间'");
                return;
            }
        } catch (x) {
            alert("活动时间选择有误！");
            return;
        }

        var address = jQuery("#address").val();
        if (!address) {
            alert("活动地址不能为空");
            return;
        }

        var def = jQuery("#defArea").val();
        if (def.length > 300) {
            alert("活动说明非空并且不超过300字");
            return;
        }
        var visible = jQuery("#pref_noapply").val();
        var joinCount = jQuery("#num").val();
        if (joinCount == "") {
            alert("请输入人数");
            return;
        }
        var iamge = jQuery("#coverImage").attr("src");

        var act = {
            name: name,
            eventStartDate: beginTimeStr,
            eventEndDate: endTimeStr,
            location: address,
            description: def,
            visible: visible,
            memberCount: Number(joinCount),
            coverImage: iamge
        };
        var activityData = {act: act, guestIds: generalSendList(), message: $.trim($('#pm-content').val())};
        var bb = JSON.stringify(activityData);
        launchActivityPost(activityData);
    }

    /**
     * 发起活动 请求
     * @param activityData
     */
    function launchActivityPost(activityData) {
        $.ajax({
            url: '/activity/promote.do',
            type: 'post',
            contentType: 'application/json',
            data: JSON.stringify(activityData),
            success: function (result) {
                if (result.code == "500") {
                    alert("发布失败");
                }
                else {
                    //清空数据
                    jQuery("#nope").val("");
                    jQuery("#dt").val("");
                    jQuery("#dt1").val("");
                    jQuery("#address").val("");
                    jQuery("#defArea").val("");
                    jQuery("#pref_noapply").val("公开");
                    jQuery("#num").val("");
                    location.href = "/activity/activityView.do?actId=" + result.act.id;
                }
            }
        });
    }

    /**
     * 上传封面图片
     * @returns {boolean}
     */
    function ajaxFileUploadPicture() {
        $.ajaxFileUpload
        (
            {
                url: '/activity/uploadPic.do',
                secureuri: false,
                fileElementId: 'file',
                dataType: 'text',
                success: function (data, status)  //服务器成功响应处理函数
                {
                    if (data != "500") {
                        var k = data.indexOf("<div");
                        if (k > 0) data = data.substring(0, k);
                        $("#coverImage").show();
                        $("#coverImage").attr("src", data);
                        $(".replaceImg").show();
                    }
                    else {
                        alert("请正确上传活动图片");
                    }
                },
                error: function (data, status, e)//服务器响应失败处理函数
                {
                    alert("服务器繁忙，请稍后再试");
                }
            }
        );
    }

    /**
     * 邀请好友--用于发起活动时
     * @param userId
     * @param userName
     * @param userImg
     */
    function invitedFriend(userId, userName, userImg) {
        for (var i = 0; i < invitedFriendList.data.length; i++) {
            if (invitedFriendList.data[i].id == userId)
                return;
        }
        invitedFriendList.data.push({
            userName: userName,
            id: userId,
            userImg: userImg
        });
        initChoosedFriend();
        $("#inviteFriend").parent().children().filter('dl').remove();
        var source = document.getElementById("invitedFriendList").innerHTML;
        var template1 = doT.template(source);
        $("#inviteFriend").before(template1({data: invitedFriendList.data}));
    }

    /**
     * 删除好友--用于发起活动时
     * @param userId
     * @param target
     */
    function deleteFriend(userId, target) {
        target.remove();
        for (var i = 0; i < invitedFriendList.data.length; i++) {
            if (invitedFriendList.data[i].id == userId) {
                invitedFriendList.data.splice(i, 1);
                return;
            }
        }
    }

    /**
     * 初始化已经选择的好友--用于发起活动时
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

    var invitedFriendList = {data: []};

    /**
     * 返回参加活动的好友Id列表
     * @returns {string}
     */
    function generalSendList() {
        var allFriend = "";
        for (var i = 0; i < invitedFriendList.data.length; i++) {
            allFriend += invitedFriendList.data[i].id + ",";
        }
        if (allFriend != "")
            allFriend = allFriend.substring(0, allFriend.length - 1);
        return allFriend;
    }

    /**
     * 列出所有好友--用于发起活动时
     */
    function showAllFriends() {
        $.ajax({
            type: "GET",
            url: "/friendcircle/listAll.do",
            success: function (msg) {
                try {
                    $(".bj").parent().children().filter('li').remove();
                    var source = document.getElementById("friendList").innerHTML;
                    var template1 = doT.template(source);
                    $(".bj").after(template1({data: msg}));

                } catch (x) {
                }
            }
        });
    }

    /**
     * 退出活动
     * @param actId
     * @param target
     */
    function quitActivity(actId, target) {
        $.ajax({
            url: "/activity/quit.do",
            type: "post",
            dataType: "json",
            data: {
                actId: actId
            },
            success: function (data) {
                if (data.code == 200) {
                    alert("退出成功！");
                    target.parent().remove();
                } else {
                    alert("退出失败！");
                }
            }
        });
    }

    /**
     * 取消活动
     * @param actId
     * @param target
     */
    function cancelActivity(actId, target) {
        $.ajax({
            url: "/activity/cancel.do",
            type: "post",
            dataType: "json",
            data: {
                actId: actId
            },
            success: function (data) {
                if (data.code == 200) {
                    alert("取消成功！");
                    target.after('<i class="qx">活动已取消</i>');
                    target.remove();
                    //$("div[flag=" + actId + "]").remove();
                } else {
                    alert(data.message);
                }
            }
        });
    }

    /**
     * 最新动态
     * @param reqPage
     */
    function getRecentAct(reqPage) {
        var page;
        if (reqPage == null || reqPage == undefined || reqPage < 1) {
            page = 1;
        } else {
            page = reqPage;
        }
        var url = "/activity/actTrackList.do";
        $.ajax({
            url: url,
            type: "post",
            dataType: "json",
            data: {
                page: page,
                pageSize: 10
            },
            success: function (data) {
                var option = {
                    total: data.total,
                    pagesize: 10,
                    currentpage: Number(page),
                    operate: function (totalPage) {
                        $('.page-index span').each(function () {
                            $(this).off("click");
                            $(this).click(function () {
                                getRecentAct($(this).text());
                            });
                        });
                        $('.first-page').off("click");
                        $('.first-page').click(function () {
                            getRecentAct(1);
                        });
                        $('.last-page').off("click");
                        $('.last-page').click(function () {
                            getRecentAct(totalPage);
                        });
                    }
                };
                Paginator.initPaginator(option);
                var source = document.getElementById("wTmpl").innerHTML;
                var template1 = doT.template(source);
                $(".recentActivity").html(template1({data: data.rows}));
            }
        });
    }

    /**
     * 好友活动
     * @param reqPage
     */
    function getFriendAct(reqPage) {
        var page;
        if (reqPage == null || reqPage == undefined || reqPage < 1) {
            page = 1;
        } else {
            page = reqPage;
        }
        var url = "/activity/friendsActivity.do";
        $.ajax({
            url: url,
            type: "post",
            dataType: "json",
            data: {
                page: page,
                pageSize: 10
            },
            success: function (data) {
                if (data != null || data != undefined) {

                    var option = {
                        total: data.total,
                        pagesize: data.pageSize,
                        currentpage: data.page,
                        operate: function (totalPage) {
                            $('.page-index span').each(function () {
                                $(this).off("click");
                                $(this).click(function () {
                                    getFriendAct($(this).text());
                                });
                            });
                            $('.first-page').off("click");
                            $('.first-page').click(function () {
                                getFriendAct(1);
                            });
                            $('.last-page').off("click");
                            $('.last-page').click(function () {
                                getFriendAct(totalPage);
                            });
                        }
                    };
                    Paginator.initPaginator(option);
                    var source = document.getElementById("hyhd").innerHTML;
                    var template1 = doT.template(source);
                    $(".friendActivity").html(template1({data: data.rows}));
                }
            }
        });
    }

    /**
     * 推荐活动
     * @param reqPage
     */
    function getRecommendActivity(reqPage) {
        var page;
        if (reqPage == null || reqPage == undefined || reqPage < 1) {
            page = 1;
        } else {
            page = reqPage;
        }
        $.ajax({
            url: "/activity/recommendActivity.do",
            type: "post",
            dataType: "json",
            data: {
                page: page,
                pageSize: 10
            },
            success: function (data) {
                if (data != null || data != undefined) {
                    var option = {
                        total: data.total,
                        pagesize: data.pageSize,
                        currentpage: data.page,
                        operate: function (totalPage) {
                            $('.page-index span').each(function () {
                                $(this).off("click");
                                $(this).click(function () {
                                    getRecommendActivity($(this).text());
                                });
                            });
                            $('.first-page').off("click");
                            $('.first-page').click(function () {
                                getRecommendActivity(1);
                            });
                            $('.last-page').off("click");
                            $('.last-page').click(function () {
                                getRecommendActivity(totalPage);
                            });
                        }
                    };
                    Paginator.initPaginator(option);
                    var source = document.getElementById("tjhd").innerHTML;
                    var template1 = doT.template(source);
                    $(".recomandActivity").html(template1({data: data.rows}));
                }
            }
        });
    }

    /**
     * 我参加的活动
     * @param reqPage
     */
    function getMyJoinActivity(reqPage) {
        var page;
        if (reqPage == null || reqPage == undefined || reqPage < 1) {
            page = 1;
        } else {
            page = reqPage;
        }

        $.ajax({
            url: "/activity/myAttendActivity.do",
            type: "post",
            dataType: "json",
            data: {
                page: page,
                pageSize: 10
            },
            success: function (data) {
                if (data != null || data != undefined) {
                    var option = {
                        total: data.total,
                        pagesize: data.pageSize,
                        currentpage: data.page,
                        operate: function (totalPage) {
                            $('.page-index span').each(function () {
                                $(this).off("click");
                                $(this).click(function () {
                                    getMyJoinActivity($(this).text());
                                });
                            });
                            $('.first-page').off("click");
                            $('.first-page').click(function () {
                                getMyJoinActivity(1);
                            });
                            $('.last-page').off("click");
                            $('.last-page').click(function () {
                                getMyJoinActivity(totalPage);
                            });
                        }
                    };
                    Paginator.initPaginator(option);
                    var source = document.getElementById("wcjd").innerHTML;
                    var template1 = doT.template(source);
                    $(".myjoinedactivity").html(template1({data: data.rows}));
                }
            }
        });
    }

    /**
     * 推荐活动
     * @param reqPage
     */
    function getMyPromoteActivity(reqPage) {
        var page;
        if (reqPage == null || reqPage == undefined || reqPage < 1) {
            page = 1;
        } else {
            page = reqPage;
        }
        $.ajax({
            url: "/activity/myOrganizedActivity.do",
            type: "post",
            dataType: "json",
            data: {
                page: page,
                pageSize: 10
            },
            success: function (data) {
                if (data != null || data != undefined) {
                    var option = {
                        total: data.total,
                        pagesize: data.pageSize,
                        currentpage: data.page,
                        operate: function (totalPage) {
                            $('.page-index span').each(function () {
                                $(this).off("click");
                                $(this).click(function () {
                                    getMyPromoteActivity($(this).text());
                                });
                            });
                            $('.first-page').off("click");
                            $('.first-page').click(function () {
                                getMyPromoteActivity(1);
                            });
                            $('.last-page').off("click");
                            $('.last-page').click(function () {
                                getMyPromoteActivity(totalPage);
                            });
                        }
                    };
                    Paginator.initPaginator(option);
                    var source = document.getElementById("wfbd").innerHTML;
                    var template1 = doT.template(source);
                    $(".mypromoteactivity").html(template1({data: data.rows}));
                }
            }
        });
    }

    /**
     * 初始化rome日历插件
     */
    function initRome() {
        var moment = rome.moment;
        rome(dt);
        var now = new Date();
        var timeStr = now.getFullYear() + "-" + (now.getMonth() + 1) + "-" + now.getDate();

        rome(dt1,
            {min: timeStr}
        );
    }

    activityMain.initPage = function () {
        leftBar.leftBar();
        tabInit();
        initRome();
        if (1 == $("#hiddenType").val()) {
            $('.wanban-head ul li:eq(5)').addClass('cur').siblings('li').removeClass('cur');
            $('.listmain').eq(5).css({
                "display": 'block'
            }).siblings().css({
                "display": 'none'
            });
        }
        else {
            getRecentAct(1);
        }
    };
    activityMain.appendHtml = function () {
        $(".orange-col").remove();
        $("#calId").children().remove();
        var content = $("#friendcircle2").html();
        var htmlContent = '<div id="friendcircle">' + content + '</div>';
        $("#calId").after(htmlContent);

    };
    module.exports = activityMain;
});