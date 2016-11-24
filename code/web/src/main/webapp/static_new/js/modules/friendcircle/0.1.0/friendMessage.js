/* 
 * @Author: Tony
 * @Date:   2015-07-15 16:01:07
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-07-16 17:24:02
 */

'use strict';
define(['doT', 'common', 'jquery', 'initPaginator'], function (require, exports, module) {
    /**
     *初始化参数
     */
    var wanban = {},
        Common = require('common'),
        Paginator = require('initPaginator');

    var replyId = "";//待删除的消息Id.
    wanban.init = function() {


    };
    /**
     * 获取发私信的好友列表
     */
    function getInvitedFriend() {
        invitedFriendList = {data: []};
        $.ajax({
            url: "/user/getAddressBookPc.do",
            type: "post",
            dataType: "json",
            success: function (data) {
                if (data != null) {
                    /*$("#headMasterListOl").children().remove();
                    Common.render({tmpl: $('#headMasterListJs'),data: {data: data.presidentList},context: '#headMasterListOl'});
                    $("#teacherListOl").children().remove();
                    Common.render({tmpl: $('#teacherListJs'), data: {data: data.teachersList}, context: '#teacherListOl'});
                    $("#studentListOl").children().remove();
                    Common.render({tmpl: $('#studentListJs'), data: {data: data.studentsList}, context: '#studentListOl'});
                    $("#parentListOl").children().remove();
                    Common.render({tmpl: $('#parentListJs'), data: {data: data.parentsList}, context: '#parentListOl'});
                    $("#departmentListOl").children().remove();
                    Common.render({tmpl: $('#departmentListJs'), data: {data: data.bureauList}, context: '#departmentListOl'});
                    $("#friendListOl").children().remove();
                    Common.render({tmpl: $('#friendListJs'), data: {data: data.friendList}, context: '#friendListOl'});*/

                    //将数据暂存，为了搜索查询
                    allFriendList.presidentList=data.presidentList;
                    allFriendList.teachersList=data.teachersList;
                    allFriendList.studentsList=data.studentsList;
                    allFriendList.parentsList=data.parentsList;
                    allFriendList.bureauList=data.bureauList;
                    allFriendList.friendList=data.friendList;
                    showAllFriends();
                }
            }
        });
    }

    //根据关键词搜索，凡是包含在内的都搜索
    function searchFriend(friendName,list,name)
    {
        var presidentList=[];
        if(list!=null) {
            for (var i = 0; i < list.length; i++) {
                if (list[i].userName.indexOf(friendName) > -1) {
                    presidentList.push(list[i]);
                }
            }
        }
        $("#"+name+"Ol").children().remove();
        Common.render({tmpl: $('#'+name+'Js'),data: {data: presidentList},context: '#'+name+'Ol'});
    }
    //执行搜索方法
    function searchFunction()
    {
        var friendName=$("#searchContent").val();
        if(friendName=="")
        {
            showAllFriends();
        }
        else{
            searchFriend(friendName,allFriendList.presidentList,"headMasterList");
            searchFriend(friendName,allFriendList.teachersList,"teacherList");
            searchFriend(friendName,allFriendList.studentsList,"studentList");
            searchFriend(friendName,allFriendList.parentsList,"parentList");
            searchFriend(friendName,allFriendList.bureauList,"departmentList");
            searchFriend(friendName,allFriendList.friendList,"friendList");
        }
        $(".bj").removeClass("jb");
        $(".bj").next("ol").css("display","block");
    }
    //展示所有好友，清空搜索框执行搜索时执行
    function showAllFriends()
    {
        $("#headMasterListOl").children().remove();
        Common.render({tmpl: $('#headMasterListJs'),data: {data: allFriendList.presidentList},context: '#headMasterListOl'});
        $("#teacherListOl").children().remove();
        Common.render({tmpl: $('#teacherListJs'), data: {data: allFriendList.teachersList}, context: '#teacherListOl'});
        $("#studentListOl").children().remove();
        Common.render({tmpl: $('#studentListJs'), data: {data: allFriendList.studentsList}, context: '#studentListOl'});
        $("#parentListOl").children().remove();
        Common.render({tmpl: $('#parentListJs'), data: {data: allFriendList.parentsList}, context: '#parentListOl'});
        $("#departmentListOl").children().remove();
        Common.render({tmpl: $('#departmentListJs'), data: {data: allFriendList.bureauList}, context: '#departmentListOl'});
        $("#friendListOl").children().remove();
        Common.render({tmpl: $('#friendListJs'), data: {data: allFriendList.friendList}, context: '#friendListOl'});
    }
    var allFriendList={presidentList:[],
        teachersList:[],
        studentsList:[],
        parentsList:[],
        bureauList:[],
        friendList:[]};
    var invitedFriendList = {data: []};

    /**
     * 邀请好友
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
            allFriend += invitedFriendList.data[i].userName + ";";
        }
        if (allFriend != "")
            allFriend = allFriend.substring(0, allFriend.length - 1);
        $("#choosedFriend").val(allFriend);
    }

    /**
     * 删除
     * @constructor
     */
    function DeleteAllReply() {
        $.ajax({
            url: "/letter/deleteallreply.do",
            type: "get",
            data: {
                'replyId': replyId
            },
            success: function (data) {
                if (data.status == 'ok') {
                    //alert('删除成功');
                    wanban.GetPrivateMsgList(1);
                } else {
                    //MessageBox(data.errorMessage, -1);
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
     * 获取私信列表
     * @param page
     * @constructor
     */
    wanban.GetPrivateMsgList = function (page) {
        var tpage = page || 1;
        var totalPage;
        $.ajax({
            url: "/letter/list.do",
            type: "get",
            dataType: "json",
            data: {
                page: tpage
            },
            success: function (data) {
                if (data != null) {
                    var option = {
                        total: data.total,
                        pagesize: 10,
                        currentpage: tpage,
                        operate: function (totalPage) {
                            $('.page-index span').each(function () {
                                $(this).off("click");
                                $(this).one("click",function () {
                                    wanban.GetPrivateMsgList($(this).text());
                                });
                            });
                            $('.first-page').off("click");
                            $('.first-page').one("click",function () {
                                wanban.GetPrivateMsgList(1);
                            });

                            $('.last-page').off("click");
                            $('.last-page').one("click",function () {
                                wanban.GetPrivateMsgList(totalPage);
                            });
                        }
                    };
                    Paginator.initPaginator(option);
                    $(".hyyq").children().remove();
                    Common.render({tmpl: $('#hyyq'), data: {data: data.rows}, context: '.hyyq'});
                }
            }
        });
    };
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
                //刷新列表
                wanban.GetPrivateMsgList(1);
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
        $(".wfsx").click(function (event) {
            /* Act on the event */
            getInvitedFriend();
            $(".gay,.hylb").fadeIn();
        });
        $(".hylb .gb").click(function (event) {
            /* Act on the event */
            $(".gay,.hylb").fadeOut();
        });
        $(".sendMsg").click(function () {
            sendPrivateMsg();
        });
        $("body").on('click', '.frinedLi', function () {
            var userId = $(this).attr("userId");
            var userName = $(this).attr("userName");
            invitedFriend(userId, userName);
        });
        /*下拉部分*/
        $(".bj").next("ol").css({
            "display": 'none'
        });
        $(".bj").click(function (event) {
            $(this).toggleClass('jb').next("ol").toggle();
        });
        /*下拉部分*/
    });

    $(function () {
        var L = 0;
        var T = 0;
        L = $(window).width() / 2 - $(".tan2").width() / 2;
        T = $(window).height() / 2 - $(".tan2").height() / 2;
        $(".tan2").css({
            'left': L,
            'top': T
        });
        $(".gay").height($(document).height());
        $("body").on("click", ".yxj span", function (event) {
            /* Act on the event */
            $(".tan2").children().remove();
            var userName = $(this).attr("userName");
            replyId = $(this).attr("userId");

            Common.render({tmpl: $('#clearAllJs'), data: {data: userName}, context: '.tan2'});
            $(".gay,.tan2").fadeIn();
        });
        $("body").on("click", ".tan2 p span,button.huidu", function (event) {
            /* Act on the event */
            $(".gay,.tan2").fadeOut();
        });
        $("body").on("click", "button.enter", function (event) {
            /* Act on the event */
            DeleteAllReply();
            $(".gay,.tan2").fadeOut();
        });
        //好友搜索
        $("body").on("click","#searchBtn",function(){
            searchFunction();
        });
        $("#searchContent").on("keydown",function(){
            if(event.keyCode==13) {
                searchFunction();
            }
        });
    });
    /*弹出层*/
    wanban.init();
    wanban.GetPrivateMsgList(1);
});