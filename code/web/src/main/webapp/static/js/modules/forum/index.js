/*
 * @Author: Tony
 * @Date:   2016-05-30 11:28:27
 * @Last Modified by:   Tony
 * @Last Modified time: 2016-05-31 14:02:51
 */
define(function (require, exports, module) {
    var Common = require('common');
    require('jquery');

    var login = $('body').attr('login') == 'true';
    var page = 1;
    (function () {

        getFPost();

        getUserList("1");

        var show_day = new Array('星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期日');
        var cur = new Date();
        var y = cur.getFullYear();
        var m = cur.getMonth() + 1;
        var d = cur.getDate();
        var day = cur.getDay();
        var dayText = show_day[day - 1];
        var text;
        if (m < 10) {
            if (d < 10) {
                text = y + "-0" + m + "-0" + d;
            } else {
                text = y + "-0" + m + "-" + d;
            }
        } else {
            if (d < 10) {
                text = y + "-" + m + "-0" + d;
            } else {
                text = y + "-" + m + "-" + d;
            }
        }
        $('#dateR').text(text);

        $('#dayR').text(dayText);

        $('.type-nav span').click(function () {
            $(this).addClass('span-cur').siblings('.type-nav span').removeClass('span-cur');
        });
        $('.type-nav span:nth-child(1)').click(function () {
            $('#sectionlist').show();
            $('.hot').hide();
        });
        $('.type-nav span:nth-child(2)').click(function () {
            $('#sectionlist').hide();
            $('.hot').show();
        });
        $('.type-nav button').click(function () {
            $('.newpost-wind').fadeIn();
            $('.bg').fadeIn();
        });
        $('.newpost-wind .nav-lt em').click(function () {
            $('.newpost-wind').fadeOut();
            $('.bg').fadeOut();
        });
        $('.ul-s li').click(function () {
            $('#spanItem').text($(this).attr('name'));
            $('#SectionItem').attr('name', $(this).attr('value'));
            $(this).addClass('ul-li-s').siblings('.ul-s li').removeClass('ul-li-s');

        });
        $('.wh-title span').click(function () {
            var temp = $(this).attr('value');
            if (temp == "1") {
                getUserList("1");
            } else if (temp == "2") {
                getUserList("2");
            }
            $(this).removeClass('wh-cur').siblings('.wh-title span').addClass('wh-cur');
        });
        $('.fwtg').click(function () {
            $('.wind-tg').fadeIn();
            $('.bg').fadeIn();
        });

        $('.wind-tg .p01 em,.wind-tg .p04 button').click(function () {
            $('.wind-tg').fadeOut();
            $('.bg').fadeOut();
        })

        $('#SectionItem').click(function () {
            var pSectionId = $('#SectionItem').attr('name');
            location.href = '/forum/postIndex.do?pSectionId=' + pSectionId;
        });


        $('#getTask').click(function () {
            goToTask();
        });

        $('#qDao').click(function () {
            Common.getPostData('/forum/userCenter/signIn.do', {}, function (resp) {
                if (resp.code == "200") {
                    location.href = '/forum';
                } else {
                    alert(resp.message);
                }

            });
        });

        $('.btn-ss').click(function () {
            var regular = $(this).siblings('.input-ss').val();
            window.open('/forum/postSearch.do?regular=' + encodeURI(encodeURI(regular)));
        })

        $('#system').click(function () {
            window.open('/forum/mySystem.do');
        });

        $('#friendApply').click(function () {
            window.open('/forum/applyFriend.do');
        });

        $('#message').click(function () {
            window.open('/forum/myMessage.do');
        });

        $('#tieZi').click(function () {
            window.open('/forum/forumNotice.do');
        });

        $('#sendPost').click(function () {
            getLogin();
        });

        if (login) {
            var noticeFlag = false;
            Common.getData('/forum/fTip.do', {}, function (resp) {
                if (resp.recordCount > 0) {
                    $('#tieZi').html('帖子' + '\<em></em>');
                } else if (resp.recordCount == 0) {
                    $('#tieZi').html('帖子');
                }
            });
            Common.getData('/forum/userCenter/countApplyFriend.do', {}, function (resp) {
                if (resp.applyCount > 0) {
                    $('#friendApply').html('好友请求' + '\<em class="redem2"></em>');
                    noticeFlag = true;
                } else if (resp.applyCount == 0) {
                    $('#friendApply').html('好友请求');
                }
            });
            Common.getData('/forum/userCenter/countMessage.do', {}, function (resp) {
                if (resp.messageCount > 0) {
                    $('#message').html('消息' + '\<em class="redem3"></em>');
                    noticeFlag = true;
                } else if (resp.messageCount == 0) {
                    $('#message').html('消息');
                }
            });
            Common.getData('/forum/userCenter/countSystem.do', {}, function (resp) {
                if (resp.systemCount > 0) {
                    $('#system').html('系统提醒' + '\<em class="redem2"></em>');
                    noticeFlag = true;
                } else if (resp.systemCount == 0) {
                    $('#system').html('系统提醒');
                }
            });
            if (noticeFlag) {
                $('#notice').html('通知' + '\<em class="red-em"></em>');
            } else {
                $('#notice').html('通知');
            }
        }

    })();

    function goToTask() {
        $.ajax({
            url: "/forum/loginInfo.do?date=" + new Date(),
            type: "get",
            dataType: "json",
            data: {},
            success: function (resp) {
                var flag = resp.login;
                if (flag) {
                    location.href = '/forum/task.do';
                } else {
                    $('.store-register').fadeToggle();
                    $('.bg').fadeToggle();
                }
            }
        });
    }

    function getLogin() {
        $.ajax({
            url: "/forum/loginInfo.do?date=" + new Date(),
            type: "get",
            dataType: "json",
            async: false,
            data: {},
            success: function (resp) {
                var flag = resp.login;
                if (flag) {
                    var pSectionId = $('#SectionItem').attr('name');
                    location.href = '/forum/newPost.do?pSectionId=' + pSectionId;
                } else {
                    $('.store-register').fadeToggle();
                    $('.bg').fadeToggle();
                }
            }
        });
    }


    function getUserList(flag) {
        var requestData = {};
        requestData.flag = flag;
        Common.getData("/forum/userList.do", requestData, function (resp) {
            Common.render({
                tmpl: '#userListTml',
                data: resp,
                context: '#lsU',
                overwrite: 1
            });
        });
    }

    function getFSection() {

        Common.getData("/forum/fSection.do", {}, function (resp) {
            Common.render({
                tmpl: '#kklTml',
                data: resp,
                context: '#kkl',
                overwrite: 1
            });
        });

        $('.p-icon').each(function () {
            var id = $(this).attr('value');
            var that = this;
            Common.getData("/forum/fSectionCount.do", {id: id}, function (resp) {
                Common.render({
                    tmpl: '#ssTml',
                    data: resp,
                    context: that,
                    overwrite: 1
                });
            });
        });
        $('.mk:first').addClass('mk1');
        $('.mk').eq(1).addClass('mk2');
        $('.mk').eq(2).addClass('mk3');
        $('.mk').eq(3).addClass('mk4');
        $('.mk').eq(4).addClass('mk5');
        $('.mk').eq(5).addClass('mk6');
        $('.mk').eq(6).addClass('mk7');
        $('.mk').eq(7).addClass('mk8');


        $('.mk-f:first').addClass('mk1-f');
        $('.mk-f').eq(1).addClass('mk2-f');
        $('.mk-f').eq(2).addClass('mk3-f');
        $('.mk-f').eq(3).addClass('mk4-f');
        $('.mk-f').eq(4).addClass('mk5-f');
        $('.mk-f').eq(5).addClass('mk6-f');
        $('.mk-f').eq(6).addClass('mk7-f');
        $('.mk-f').eq(7).addClass('mk8-f');

    }

    function getFPost() {
        var requestData = {};
        requestData.sortType = 2;
        requestData.page = 1;
        requestData.pageSize = 4;
        requestData.classify = -1;
        requestData.cream = -1;
        requestData.gtTime = 0;
        requestData.postSection = "";
        Common.getData("/forum/fPostsByCondition.do", requestData, function (resp) {
            var posts = resp.list;

            if (posts.length == 0) {
                $('#notFound').show();
            } else {
                $('#notFound').hide();
            }
            Common.render({
                tmpl: '#postListTml',
                data: posts,
                context: '#listPost',
                overwrite: 1
            });

        })
        requestData.pageSize = 25;
        requestData.inSet = 1;
        Common.getData("/forum/fPostsActivity.do", requestData, function (resp) {
            var sub = resp.subList;
            Common.render({
                tmpl: '#poTml',
                data: sub,
                context: '#contRight',
                overwrite: 1
            });

            Common.render({
                tmpl: '#bllTml',
                data: sub,
                context: '#ulButtons',
                overwrite: 1
            });

            $('#ulButtons li:first').addClass('on');

            Common.render({
                tmpl: '#listTml',
                data: sub,
                context: '#list',
                overwrite: 1
            });

            Common.render({
                tmpl: '#ooTml',
                data: sub,
                context: '#imgList',
                overwrite: 1
            });
        })
    }
});
