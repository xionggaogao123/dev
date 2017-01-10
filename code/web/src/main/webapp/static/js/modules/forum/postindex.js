define(function (require, exports, module) {
    var Common = require('common');
    require('jquery');
    require('pagination');
    var login = $('body').attr('login') == 'true';
    var page = 1;
    var sortType = 9;
    var zan = 1;
    var gtTime = 0;
    $(document).ready(function () {
        var pSectionId = $('#pSectionId').val();
        getData(pSectionId);
        getClassifyCount(pSectionId);
        postList(page, pSectionId, -1, 0);//发帖列表

        getUserList("1");

        getCreamData();

        $('body').on('click', '#rely', function () {
            postList(page, pSectionId, -1, 0);
        });

        $('body').on('click', '#cream', function () {
            postList(page, pSectionId, 1, 0);
        })

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

        $('#postCount .classify').click(function () {
            $("#selectOne").find("option[value='-1']").attr("selected", true);
            $(this).addClass('red-b').siblings('#postCount .classify').removeClass('red-b');
            gtTime = 0;
            postList(page, pSectionId, -1, gtTime);
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

        $('#postBtn').click(function () {
            window.open('/forum/forumNotice.do');
        });

        $('#friendApply').click(function () {
            window.open('/forum/applyFriend.do');
        });

        $('#message').click(function () {
            window.open('/forum/myMessage.do');
        });

        $('#system').click(function () {
            window.open('/forum/mySystem.do');
        });

        $('.btn-ss').click(function () {
            var regular = $(this).siblings('.input-ss').val();
            //window.open('/forum/postSearch.do?regular=' + regular);
            window.open('/forum/postSearch.do?regular=' + encodeURI(encodeURI(regular)));
        });

        $('#collBan').click(function () {
            var collection = {};
            collection.postSectionId = $('#pSectionId').val();
            collection.type = 1;
            Common.getPostData('/forum/userCenter/addCollection.do', collection, function (resp) {
                if (resp.code == 200) {
                    location.href = '/forum/postIndex.do?pSectionId=' + pSectionId;
                } else {
                    alert(resp.message);
                }
            });
        });

        $('#qDao').click(function () {
            Common.getPostData('/forum/userCenter/signIn.do', {}, function (resp) {
                if (resp.code == "200") {
                    location.href = '/forum/postIndex.do?pSectionId=' + pSectionId;
                } else {
                    alert(resp.message);
                }

            });
        });

        $('#selectOne').change(function () {
            var value = $(this).val();
            if (value == 0) {
                gtTime = new Date().getTime() - 24 * 60 * 60 * 1000;
            } else if (value == 1) {
                gtTime = new Date().getTime() - 2 * 24 * 60 * 60 * 1000;
            } else if (value == 2) {
                gtTime = new Date().getTime() - 7 * 24 * 60 * 60 * 1000;
            } else if (value == 3) {
                gtTime = new Date().getTime() - 30 * 24 * 60 * 60 * 1000;
            } else if (value == 4) {
                gtTime = new Date().getTime() - 90 * 24 * 60 * 60 * 1000;
            }
            postList(page, pSectionId, -1, gtTime);
            gtTime = 0;
        });

        $('.fwtg').click(function () {
            $('.wind-tg').fadeIn();
            $('.bg').fadeIn();
        });

        $('.wind-tg .p01 em,.wind-tg .p04 button').click(function () {
            $('.wind-tg').fadeOut();
            $('.bg').fadeOut();
        });

        $('#selectTwo').change(function () {
            if ($(this).val() == 3) {
                sortType = 9;
                zan = 1;
            } else if ($(this).val() == 1 || $(this).val() == 2) {
                sortType = 2;
                zan = 1;
            } else if ($(this).val() == 4) {
                zan = 2;
            }
            postList(page, pSectionId, -1, gtTime);
        });

        $('#expressTheme').click(function () {
            goToPost(pSectionId);
        });

        $('#getTask').click(function () {
            goToTask();
        });

        if (login) {
            var noticeFlag = false;
            Common.getData('/forum/fTip.do', {}, function (resp) {
                if (resp.recordCount > 0) {
                    $('#postBtn').html('帖子' + '\<em></em>');
                } else if (resp.recordCount == 0) {
                    $('#postBtn').html('帖子');
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

    })

    function getCreamData(){
        Common.getPostData("/forum/getCreamData.do", {cream:1}, function (resp) {
            Common.render({
                tmpl: '#creamDataTmpl',
                data: resp.message,
                context: '#creamData',
                overwrite: 1
            });
        });
    }

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

    function goToPost(pSectionId) {
        $.ajax({
            url: "/forum/loginInfo.do?date=" + new Date(),
            type: "get",
            dataType: "json",
            data: {},
            success: function (resp) {
                var flag = resp.login;
                if (flag) {
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

    function getData(pSectionId) {

        var requestData = {};
        requestData.id = pSectionId;
        Common.getData("/forum/fSectionDetail.do", requestData, function (resp) {

            Common.render({
                tmpl: '#dataTml',
                data: resp,
                context: '#data',
                overwrite: 1
            });

        })
    }

    function getClassifyCount(pSectionId) {
        var requestData = {};
        requestData.id = pSectionId;
        requestData.classify = $('.red-b').attr('value');
        Common.getData("/forum/fSectionCountById.do", requestData, function (resp) {

            Common.render({
                tmpl: '#postCountTml',
                data: resp,
                context: '#postCount',
                overwrite: 1
            });

        })
    }

    function postList(page, pSectionId, cream, gtTime) {
        var isInit = true;
        var requestData = {};
        requestData.sortType = sortType;
        requestData.page = page;
        requestData.pageSize = 25;
        requestData.classify = $('.red-b').attr('value');
        requestData.cream = cream;
        requestData.gtTime = gtTime;
        requestData.postSection = pSectionId;
        requestData.zan = zan;
        requestData.person = "";
        Common.getData("/forum/fPosts.do?ti=" + new Date().getTime(), requestData, function (resp) {
            var posts = resp.list;
            $('.new-page-links').html("");
            if (posts.length > 0) {
                $('.new-page-links').jqPaginator({
                    totalPages: Math.ceil(resp.count / requestData.pageSize) == 0 ? 1 : Math.ceil(resp.count / requestData.pageSize),//总页数
                    visiblePages: 5,//分多少页
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
                            postList(n, pSectionId, cream, gtTime);
                            $('body,html').animate({scrollTop: 0}, 20);
                        }
                    }
                });
            }

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

    }
});