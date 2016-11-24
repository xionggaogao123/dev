/* 
 * @Author: Tony
 * @Date:   2015-07-09 11:03:00
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-07-16 11:32:58
 */

'use strict';
define('activityView', ['jquery', 'doT', 'easing', 'common', 'fancybox','experienceScore'], function (require, exports, module) {
    /**
     *初始化参数
     */
    /*require("jquery");
     require("doT");
     require("rome");*/
    //require("fancybox");
    //require("ajaxfileupload");
    var wanban = {},
        Common = require('common');

    var actId = $("body").attr("actId");
    var inviteId = $("body").attr("inviteId");
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
        $("body").on('click', '.huodong_leftT dl dt .yqhy', function () {
            $("#pm-content").val("");
            invitedFriendList = {data: []};
            initChoosedFriend();
            $(".gay,.hylb").fadeIn();
        });
        $(".hylb .gb").click(function (event) {
            /* Act on the event */
            $(".gay,.hylb").fadeOut();
        });
    });
    /*弹出层*/

    /*列表*/
    $(document).ready(function () {
        var RH = $(".huodong_leftB .tlq .floor .rt").height();
        $(".huodong_leftB .tlq .floor .lo").height(RH + 25);

        $('body').on('click', ".rt .me1", function () {
            $(this).css({
                "display": 'none'
            }).next(".me2").css({
                "display": 'inline'
            });
            $(this).parent().parent().parent().parent().children().filter('dl').children().filter('dt').css({
                "display": 'block'
            });
        });
        $('body').on('click', ".rt .me2", function () {
            $(this).css({
                "display": 'none'
            }).prev(".me1").css({
                "display": 'inline'
            });
            $(this).parent().parent().parent().parent().children().filter('dl').children().filter('dt').css({
                "display": 'none'
            });
        });
        //子评论
        $('body').on('click', '.subclassBtn', function () {
            writeSubDiscuss($(this).attr("disid"), $(this).parent().children().filter(".subdiscuss").val());
        });
        /*讨论区和相册之间的切换*/
        $(".huodong_leftB ul li").click(function (event) {
            $(this).addClass('dq').siblings('li').removeClass('dq');
            $(".tlqxc").eq($(this).index()).css({
                "display": 'block'
            }).siblings().css({
                "display": 'none'
            });
        });
        $(".ImG").css({"display": 'none'});
        /*讨论区和相册之间的切换*/
        /*删除图片*/
        $("body").on('click', ".ImG s", function (event) {
            $(".ImG").css({"display": 'none'});
            $("#prevImg").attr("src", "");
            $("#file").val("");
        });
        /*删除图片*/
        /*换一换*/
        $("body").on('click', ".changeHotAct", function () {
            wanban.getHotActivity();
        });
        /*换一换*/
        /*收起展开图片*/
        $(".huodong_right ul li:gt(7)").hide();
        $("body").on('click', ".sqb2", function () {
            $(this).hide().siblings(".sqb1").show();
            $(".huodong_right ul li").show();
        });
        $("body").on('click', ".sqb1", function () {
            $(".huodong_right ul li:gt(7)").hide();
            $(this).hide().siblings(".sqb2").show();
        });
        require.async("ajaxfileupload", function (ajaxfileupload) {
            $("body").on("click", "#publish", function () {
                ajaxFileUpload();
            });
        });
        $("body").on('click', '.frinedLi', function () {
            var userId = $(this).attr("userId");
            var userName = $(this).attr("userName");
            var userImg = $(this).attr("userImg");
            invitedFriend(userId, userName, userImg)
        });
        /*发送好友邀请*/
        $("body").on("click", "#sendMsg", function () {
            if ($("#choosedFriend").val() == "") {
                alert("请选择好友");
                return;
            }
            else {
                sendInvitMsg();
                //$(".gay,.hylb").fadeOut();
            }
        });
        /*收起展开图片*/
        $("body").on("click", "#uploadPic", function () {
            addImage();
            $("#file").off("change", function () {
            });
            $("#file").on('change', function () {
                previewImage(this);
                $(".ImG").css({
                    "display": 'block'
                });
            });
        });
        /*取消活动*/
        $("body").on("click", ".qxhd", function () {
            cancelAct();
        });
        /*退出活动*/
        $("body").on("click", ".tchd", function () {
            quitAct();
        });
        /*参加活动*/
        $("body").on("click", ".cjhd", function () {
            attendAct();
        });
        /*接收邀请*/
        $("body").on("click", ".jieshou", function () {
            acceptInit();
        });
        /*拒绝邀请*/
        $("body").on("click", ".jujue", function () {
            rejectInit();
        });
        /*考虑下*/
        $("body").on("click", ".klyx", function () {
            hesitateInvite();
        });
        //删除回复以及评论
        $("body").on("click", ".delDiscuss", function () {
            var actId = $("body").attr("actId");
            var disId = $(this).attr("disId");
            var img = $(this).attr("imgSrc");
            var hasPic = false;
            if (img != "")
                hasPic = true;
            deleteReply(actId, disId, hasPic);
        });
        if ($('.fancybox') != undefined) {
            $('.fancybox').fancybox();
        }
    });

    /**
     * 删除回复以及评论
     * @param actId
     * @param repId
     */
    function deleteReply(actId, repId, hasPic) {
        $.ajax({
            url: "/activity/deleteReply.do",
            type: "POST",
            data: {
                actId: actId,
                repId: repId,
                hasPic: hasPic
            },
            success: function (obj) {
                if (obj.code == "200") {
                    $(".yiti").remove();
                    $(".tlq").empty();
                    Common.render({
                        tmpl: $('#discussList'),
                        data: {data: obj.disList, disCount: obj.disCount},
                        context: '.tlq'
                    });

                    $(".xc").children().filter("dl").remove();
                    $(".xc").empty();
                    //图片
                    Common.render({tmpl: $('#xc'), data: {data: obj.disList}, context: '.xc'});
                }
            }
        });
    }

    /**
     * 邀请的好友列表
     * @type {{data: Array}}
     */
    var invitedFriendList = {data: []};

    /**
     * 邀请好友
     * @param userId
     * @param userName
     * @param userImg
     */
    function invitedFriend(userId, userName, userImg) {

        for (var i = 0; i < invitedFriendList.data.length; i++) {
            if (invitedFriendList.data[i].id == userId) {
                invitedFriendList.data.splice(i, 1);
                initChoosedFriend();
                return;
            }
        }
        invitedFriendList.data.push({
            userName: userName,
            id: userId,
            userImg: userImg
        });
        initChoosedFriend();
    }

    /**
     * 返回邀请的好友Id列表
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
     * 取消活动
     */
    function cancelAct() {
        if (window.confirm('确定要删除此次活动吗？')) {
            $.ajax({
                type: "GET",
                url: "/activity/cancel.do?actId=" + actId,
                success: function (msg) {
                    if (msg.code == "200") {
                        /*$(".yqhy").remove();
                         $(".qxhd").text("此活动已删除");
                         $(".qxhd").removeClass("qxhd");*/
                        history.go(-1);
                    }
                }
            });
        }
    }

    /**
     * 参加活动
     * @param id
     */
    function attendAct() {
        $.ajax({
            type: "GET",
            url: "/activity/attend.do?fromDevice=0&actId=" + actId,
            success: function (msg) {
                if (msg.code == "200") {
                    /*$(".cjhd").text("已参加");
                     $(".cjhd").removeClass("cjhd");*/
                    if (msg.score) {
                        scoreManager(msg.scoreMsg, msg.score);
                    }
                    wanban.getActivityView();
                } else {
                    alert(msg.message);
                }
            }
        });
    }

    /**
     * 退出活动
     */
    function quitAct() {
        if (window.confirm('确定要退出该次活动吗？')) {
            $.ajax({
                type: "GET",
                url: "/activity/quit.do?actId=" + actId,
                success: function (msg) {
                    if (msg.code == "200") {
                        /* $(".tchd").text("已退出活动");
                         $(".tchd").removeClass("tchd");*/
                        wanban.getActivityView();
                    }
                }
            });
        }
    }

    /**
     * 接受活动邀请
     */
    function acceptInit() {
        $.ajax({
            type: "GET",
            url: "/activity/accept.do?fromDevice=0&id=" + inviteId,
            success: function (msg) {
                for (var key in msg) {
                    if (key == "200") {
                        $(".jujue,.klyx").remove();
                        $(".jieshou").text("已接受邀请");
                        $(".jieshou").removeClass("tebie");
                        if (msg.score) {
                            scoreManager(msg.scoreMsg, msg.score);
                        }
                    }
                    else {
                        alert(msg.message);
                    }
                }
            }
        });
    }

    /**
     * 拒绝邀请
     */
    function rejectInit() {
        if (window.confirm('确定要拒绝邀请吗')) {
            $.ajax({
                type: "GET",
                url: "/activity/reject.do?id=" + inviteId,
                success: function (msg) {
                    for (var key in msg) {
                        if (key == "200") {
                            $(".jieshou,.klyx").remove();
                            $(".jujue").text("已拒绝该活动邀请");
                            $(".jujue").removeClass("jujue");
                        }
                        else {
                            alert(msg.message);
                        }
                    }
                }
            });
        }
    }

    /**
     * 考虑下
     */
    function hesitateInvite() {
        {
            $.ajax({
                type: "GET",
                url: "/activity/hesitate.do?id=" + inviteId,
                success: function (msg) {
                    for (var key in msg) {
                        if (key == "200") {
                            $(".jieshou,.jujue").remove();

                            $(".klyx").text("我先好好考虑下");
                            $(".klyx").removeClass("klyx");
                        } else {
                            alert(msg.message);
                        }
                    }
                }
            });
        }
    }

    /**
     * 发送邀请私信
     */
    function sendInvitMsg() {
        var actName = $("#activityTitle").text();
        var msg = "快来参加<a href='" + window.location.href + "'>" + actName + "</a>吧";
        if (jQuery("#pm-content").val() != "") {
            msg = jQuery("#pm-content").val();
        }
        $.ajax({
            url: "/activity/invite.do",
            type: "post",
            data: {
                'actId': $("body").attr("actId"),
                'guestIds': generalSendList(),
                'msg': msg
            },
            success: function (data) {
                $(".gay,.hylb").fadeOut();
            }
        });
    }

    /**
     * 初始化已经选择的邀请好友
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
     * 回复消息
     * @param repId
     * @param content
     */
    function writeSubDiscuss(repId, content) {
        if (!content || content == "") {
            return;
        }
        $.ajax({
            type: "POST",
            url: '/activity/addWebSubdiscuss.do',
            data: {actId: actId, content: content, repId: repId},
            success: function (obj) {
                if (obj.code == "200") {
                    $(".yiti").remove();
                    $(".tlq").empty();
                    Common.render({
                        tmpl: $('#discussList'),
                        data: {data: obj.disList, disCount: obj.disCount},
                        context: '.tlq'
                    });
                    $(".xc").children().filter("dl").remove();
                    //图片
                    $(".xc").empty();
                    Common.render({tmpl: $('#xc'), data: {data: obj.disList}, context: '.xc'});
                }
            }
        });
    }

    /**
     * 评论添加图片
     */
    function addImage() {
        var ie = navigator.appName == "Microsoft Internet Explorer" ? true : false;
        if (ie) {
            document.getElementById("file").click();
            document.getElementById("filename").value = document.getElementById("file").value;
        } else {
            var a = document.createEvent("MouseEvents");
            a.initEvent("click", true, true);
            document.getElementById("file").dispatchEvent(a);
        }
    }

    /**
     * 预览图片
     * @param file
     */
    function previewImage(file) {
        var MAXWIDTH = 100;
        var MAXHEIGHT = 100;
        if (file.files && file.files[0]) {
            var img = document.getElementById('prevImg');
            img.onload = function () {
                var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
                img.width = rect.width;
                img.height = rect.height;
            };
            var reader = new FileReader();
            reader.onload = function (evt) {
                img.src = evt.target.result;
            };
            reader.readAsDataURL(file.files[0]);
        }
    }

    /**
     * 计算图片尺寸
     * @param maxWidth
     * @param maxHeight
     * @param width
     * @param height
     * @returns {{top: number, left: number, width: *, height: *}}
     */
    function clacImgZoomParam(maxWidth, maxHeight, width, height) {
        var param = {top: 0, left: 0, width: width, height: height};
        if (width > maxWidth || height > maxHeight) {
            var rateWidth = width / maxWidth;
            var rateHeight = height / maxHeight;

            if (rateWidth > rateHeight) {
                param.width = maxWidth;
                param.height = Math.round(height / rateWidth);
            } else {
                param.width = Math.round(width / rateHeight);
                param.height = maxHeight;
            }
        }

        param.left = Math.round((maxWidth - param.width) / 2);
        param.top = Math.round((maxHeight - param.height) / 2);
        return param;
    }

    /**
     * 获取活动详情
     */
    wanban.getActivityView = function () {

        $.ajax({
            url: "/activity/ajaxActivityView.do",
            type: "GET",
            data: {
                actId: actId
            },
            success: function (data) {
                //活动
                $('.mainmore').empty();
                Common.render({tmpl: $('#huodong_leftT'), data: data.activity, context: '.mainmore'});
                //type
                $('#acttypediv').empty();
                Common.render({tmpl: $('#actType'), data: data, context: '#acttypediv'});
                //发起人
                $('.fqr').empty();
                Common.render({tmpl: $('#fqr'), data: data.organInfo, context: '.fqr'});
                $("#orgId").val(data.organInfo.id);
                /*参与成员*/
                $('.cycy').empty();
                Common.render({tmpl: $('#cycy'), data: {data: data.users}, context: '.cycy'});
                //评论
                $('.tlq').empty();
                Common.render({
                    tmpl: $('#discussList'),
                    data: {data: data.disList, disCount: data.disCount},
                    context: '.tlq'
                });
                //图片
                $('.xc').empty();
                Common.render({tmpl: $('#xc'), data: {data: data.disList}, context: '.xc'});
                //参加人
                $('#attendCount').empty();
                Common.render({
                    tmpl: $('#attendCountJs'),
                    data: {att: data.activity.attendCount, max: data.activity.memberCount},
                    context: '#attendCount'
                });
                /**
                 * 参与好友只显示两行
                 */
                $(".sqb1").trigger("click");
            }
        });
    };

    var i = 0;
    /**
     * 获取热门活动
     */
    wanban.getHotActivity = function () {
        i++;
        $.ajax({
            url: '/activity/hot.do?topN=' + i + '&id=' + Math.floor(Math.random() * ( 1000 + 1)),
            type: 'post',
            dataType: "json",
            success: function (result) {
                $(".rmhd").children().filter("dd").remove();
                /*热门活动*/
                Common.render({tmpl: $('#rmhd'), data: {data: result}, context: '.rmhd'});
            }
        });
    };
    /**
     * 异步上传,发表评论
     */
    function ajaxFileUpload() {
        var c = jQuery("#discuss_con").val();
        if (!c || c.length > 500) {
            alert("评论内容为空或者内容太长（不超过500字）");
            return;
        }
        var activityData = {actId: actId, content: c};
        $.ajaxFileUpload
        (
            {
                url: '/activity/addwebdiscuss.do',
                secureuri: false,
                fileElementId: 'file',
                dataType: 'JSON',
                param: activityData,
                success: function (msg) //服务器成功响应处理函数
                {
                    var obj = eval("(" + msg + ")");
                    if (obj.code != "200") {
                        alert("请重新上传活动评论");
                    }
                    else {
                        $(".yiti").remove();
                        //评论
                        $(".tlq").empty();
                        Common.render({
                            tmpl: $('#discussList'),
                            data: {data: obj.disList, disCount: obj.disCount},
                            context: '.tlq'
                        });
                        $(".xc").children().filter("dl").remove();
                        //图片
                        $(".xc").empty();
                        Common.render({tmpl: $('#xc'), data: {data: obj.disList}, context: '.xc'});
                        //清除图片
                        $(".ImG").css({"display": 'none'});
                        $("#prevImg").attr("src", "");
                        $("#discuss_con").val("");
                        $("#file").val("");
                    }
                },
                error: function (data, status, e)//服务器响应失败处理函数
                {
                    alert("服务器繁忙，请稍后再试");
                }
            }
        )
    }

    //推荐好友
    wanban.showAllFriends = function () {
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
    };
    wanban.getActivityView();
    wanban.getHotActivity();
    wanban.showAllFriends();
});