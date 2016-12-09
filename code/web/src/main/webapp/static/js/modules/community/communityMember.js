define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    require('jquery');
    var communityId = $('body').attr('communityId');
    var communityMember = {};
    var initPage = 1;
    //该社区副社长数目
    var communitySecondCount = 0;
    var activity_cur = 1;
    communityMember.init = function () {
        getMemberList(initPage);
        getCurrCommunity();
        getMyCommunity();

        getPublishedActivitys();
        getSignedActivitys();
        getAttendActivitys();
    };

    $(document).ready(function () {

        //活动  start
        $('#activity-signed-div').show();
        $('#activity-published-dev').hide();
        $('#activity-attended-div').hide();

        $('body').on('click', '#myActivity-span', function () {
            $('#my-community-span').removeClass('hd-green-cur');
            $(this).addClass('hd-green-cur');
            $('.container .hd-cont-f1').hide();
            $('.container .hd-cont-f2').show();
        });

        $('body').on('click', '#my-community-span', function () {
            $('#myActivity-span').removeClass('hd-green-cur');
            $(this).addClass('hd-green-cur');

            $('.container .hd-cont-f1').show();
            $('.container .hd-cont-f2').hide();
        });

        $('body').on('click', '#my-community-1', function () {
            $('#my-community-2').removeClass('hd-cf-cur2');
            $('#my-community-3').removeClass('hd-cf-cur2');
            $(this).addClass('hd-cf-cur2');
            activity_cur = 1;
            renderActivity();
        });

        $('body').on('click', '#my-community-2', function () {
            $('#my-community-1').removeClass('hd-cf-cur2');
            $('#my-community-3').removeClass('hd-cf-cur2');
            $(this).addClass('hd-cf-cur2');
            activity_cur = 2;
            renderActivity();
        });

        $('body').on('click', '#my-community-3', function () {
            $('#my-community-1').removeClass('hd-cf-cur2');
            $('#my-community-2').removeClass('hd-cf-cur2');
            $(this).addClass('hd-cf-cur2');
            activity_cur = 3;
            renderActivity();
        });

        $('body').on('click','.alert-diglog em,.alert-diglog .alert-btn-esc',function () {

            $('.alert-diglog').fadeOut();
            $('.bg').fadeOut();
        });

        $('body').on('click','#ul-activity-signed li button',function () {

            $('.alert-diglog').fadeIn();
            $('.bg').fadeIn();

            $('.alert-diglog .alert-main span').html('确定要取消此次报名吗？');

            var me = $(this);
            var acid = $(this).attr('value');

            $('.alert-diglog .alert-btn-sure').click(function (){
                var requestParm = {
                    acid: acid
                };
                common.getData("/factivity/cancelSign.do", requestParm, function (resp) {
                    if (resp.code == '200') {
                        me.parent().hide();
                        $('.alert-diglog').fadeOut();
                        $('.bg').fadeOut();
                        me.parent().hide();
                    } else {
                        alert(resp.message);
                    }
                });
            });
        });


        $('body').on('click','#ul-activity-published li button',function () {
            var acid = $(this).attr('value');

            $('.alert-diglog').fadeIn();
            $('.bg').fadeIn();

            var me = $(this);

            $('.alert-diglog .alert-main span').html('确定要取消此次活动吗？');

            $('.alert-diglog .alert-btn-sure').click(function () {
                var requestParm = {
                    acid: acid
                };
                common.getData("/factivity/cancelPublish.do", requestParm, function (resp) {
                    if (resp.code == '200') {
                        me.parent().hide();
                        $('.alert-diglog').fadeOut();
                        $('.bg').fadeOut();
                    } else {
                        alert(resp.message);
                    }
                });
            });
        });


        hx_update();

        setInterval(hx_update, 1000 * 60);

        $(".hx-notice").click(function () {
            window.open('/webim/index', '_blank');
        });

        $('body').on('click', '.comb1 .b1', function () {
            $('.com-b').hide();
            $('.comb2').show();
            $('.ul-member-list li').addClass('border-b');
            $('.com-gou1').show();
        });
        $('body').on('click', '.comb1 .b2', function () {
            $('.com-b').hide();
            $('.comb3').show();
            $('.ul-member-list li').addClass('border-b');
            $('.com-gou1').show();
            $('.comb3 .b2').removeAttr('disabled').css('color', '#23CD77').css('border', '1px solid #23CD77');
        });
        $('body').on('click', '.comb2 .b1', function () {
            //先判断当前人是否为社长或者副社长
            if (judgeOperation()) {
                //删除选中
                deleteSelection();
            } else {
                alert("你不是社长或者副社长，没有权限删除成员！");
            }

        });
        $('body').on('click', '.comb2 .b2', function () {
            comb2();
        });
        $('body').on('click', '.comb3 .b1', function () {
            if (judgeManager()) {
                //在判断该社区的副社长人数有多少
                if (judgeSecondMember()) {
                    setSecondMember(1);
                } else {
                    alert("副社长人数已满！");
                }

            } else {
                alert("你不是社长，没有权限设置副社长！");
            }

        });
        $('body').on('click', '.comb3 .b2', function () {
            if (judgeManager()) {
                setSecondMember(0);
            } else {
                alert("你不是社长，没有权限取消副社长！");
            }
        });
        $('body').on('click', '.comb3 .b3', function () {
            comb2();
        });
        $('body').on('click', '.com-gou1', function () {
            $(this).hide();
            $(this).next('.com-gou2').show();
            $(this).next().next('.memberId').addClass('com');
            if ($(this).closest('li').find('div').find('p').text() == "社区成员" ||
                $(this).closest('li').find('div').find('p').text() == "社长") {
                $('.comb3 .b2').attr('disabled', 'disabled').css('color', 'gray').css('border', '1px solid gray');
            }
        })

        $('body').on('click', '.com-gou2', function () {
            if (eachMembers() == 1) {
                $('.comb3 .b2').removeAttr('disabled').css('color', '#23CD77').css('border', '1px solid #23CD77');
            }
            $(this).hide();
            $(this).prev('.com-gou1').show();
            $(this).next('.memberId').removeClass('com');
        });
        $('body').on('click', '.ul-member-list li .p1 img', function () {
            $('.si-s1').fadeIn();
            $('.bg').fadeIn();
            $('#remark').val($(this).prev().text());
            $('#member').data("remarkId", $(this).attr('remarkId'));
            $('#member').data("userId", $(this).attr('userId'));
        });
        $('body').on('click', '.alert-btn-esc', function () {
            $('.bg').fadeOut();
            $('.sign-alert').fadeOut();
        })
        $('body').on('click', '.ul-member-list li .p3 .em-jc', function () {
            $('.cancel-parter').fadeIn();
            $('.bg').fadeIn();
        })

        $('body').on('click', '#confirm', function () {
            setNickName();
        })

        $('body').on('click', '.cancelFriend', function () {
            $('.cancel-parter').fadeIn();
            $('.bg').fadeIn();
            $('.cancel-parter').find('.alert-main').find('em').html($(this).attr('nickName'));
            $('#member').data("cancelFriendId", $(this).attr('userId'));
        })


        $('body').on('click', '#sureCancel', function () {
            sureCancel();
        })

        $('body').on('click', '.applyFriend', function () {
            $('.si-s4').fadeIn();
            $('.bg').fadeIn();
            $('.si-s4').find('.alert-main').find('em').html($(this).attr('nickName'));
            $('#member').data("applyFriend", $(this).attr('userId'));
        })


        $('body').on('click', '#applyFriend', function () {
            applyFriend();
        })

        $('body').on('click', '.wait', function () {
            $('.si-s5').fadeIn();
            $('.bg').fadeIn();
            $('.si-s5').find('.alert-main').find('em').html($(this).attr('nickName'));
        })

        $('body').on('click', '.cancel-parter alert-btn-esc,.cancel-parter em', function () {
            $('.cancel-parter').fadeOut();
            $('.bg').fadeOut();
        })


        $('body').on('click', '.si-s4 alert-btn-esc,.si-s4 em', function () {
            $('.si-s4').fadeOut();
            $('.bg').fadeOut();
        })

        $('body').on('click', '.si-s5 alert-btn-esc,.si-s5 em,#reply', function () {
            $('.si-s5').fadeOut();
            $('.bg').fadeOut();
        })
    })


    function applyFriend() {
        var content = $('#content').val();
        var param = {};
        param.content = content;
        param.personId = $('#member').data("applyFriend");
        common.getPostData('/forum/userCenter/applyFriend.do', param, function (resp) {
            $('.si-s4').fadeOut();
            $('.bg').fadeOut();
            if (resp.code == "200") {
                getMemberList(initPage);
            } else {
                alert("请求好友失败");
            }
        })
    }


    function sureCancel() {
        var param = {};
        param.userIds = $('#member').data("cancelFriendId");
        common.getData('/friend/delete.do', param, function (result) {
            $('.cancel-parter').fadeOut();
            $('.bg').fadeOut();
            if (result) {
                getMemberList(initPage);
                alert("解除关系成功");
            } else {
                alert("解除关系失败");
            }
        })
    }

    //获取当前社区
    function getCurrCommunity() {
        common.getData('/community/' + communityId, {}, function (resp) {
            if (resp.code == "200") {
                $('.com-now').find('.p1').html(resp.message.name);
                $('.com-now').find('img').attr('src', resp.message.logo);
            } else {
                alert(resp.message);
            }
        })
    }

    function eachMembers() {
        var i = 0;
        $('.com').each(function () {
            if ($(this).closest('li').find('div').find('p').text() == "社区成员" ||
                $(this).closest('li').find('div').find('p').text() == "社长") {
                i++;
            }
        });
        return i;
    }

    function judgeSecondMember() {
        var ret = false;
        var param = {};
        param.communityId = communityId;
        common.getData('/community/countSecondMember.do', param, function (resp) {
            if (resp.code == "200") {
                communitySecondCount = resp.message;
                if (resp.message < 2) {
                    ret = true;
                }
            }
        });
        return ret;
    }

    function setSecondMember(role) {
        var param = {};
        if (setParam(param, 'userId')) {
            setParam(param, 'memberId');
            if (role == 1) {
                var selectCount = 2 - communitySecondCount;
                if (selectCount < countSelect()) {
                    alert("还能选择的副社长数目为" + selectCount);
                    return;
                }
            }
            param.role = role;
            param.communityId = communityId;
            common.getData('/community/setSecondMembers.do', param, function (resp) {
                if (resp.code == "200") {
                    getMemberList(initPage);
                    comb2();
                } else {
                    getMemberList(initPage);
                    comb2();
                    alert(resp.message);
                }
            })
        }
    }

    function judgeManager() {
        var judge = false;
        var param = {};
        param.communityId = communityId;
        common.getData('/community/judgeManager.do', param, function (resp) {
            if (resp.code == "200") {
                judge = resp.message;
            }
        })
        return judge;
    }

    function judgeOperation() {
        var judge = false;
        var param = {};
        param.communityId = communityId;
        common.getData('/community/judgeOperation.do', param, function (resp) {
            if (resp.code == "200") {
                judge = resp.message;
            }
        });
        return judge;
    }

    function comb2() {
        $('.com-b').hide();
        $('.comb1').show();
        $('.ul-member-list li').removeClass('border-b');
        $('.com-gou1,.com-gou2').hide();
    }

    function setNickName() {
        var remark = $('#remark').val();
        if (remark == "" || remark == undefined) {
            alert("备注名不能为空！");
            return;
        }
        var param = {};
        param.remarkId = $('#member').data("remarkId");
        param.endUserId = $('#member').data("userId");
        param.remark = remark;
        common.getData('/community/setRemark.do', param, function (resp) {
            if (resp.code == "200") {
                $('.bg').fadeOut();
                $('.sign-alert').fadeOut();
                getMemberList(initPage);
            }
        })

    }

    function setParam(param, field) {
        var deleteIds = "";

        $('.com').each(function () {
            deleteIds = deleteIds + $(this).attr(field) + ",";
        })

        if (deleteIds == "") {
            alert("请至少选中一个");
            return false;
        }
        if (field == 'memberId') {
            param.memberId = deleteIds.substring(0, deleteIds.length - 1);
        } else {
            param.memberUserId = deleteIds.substring(0, deleteIds.length - 1);
        }

        return true;
    }

    //获取我的社区
    function getMyCommunity() {
        common.getData("/community/myCommunitys.do", {pageSize: 9, platform: "web"}, function (result) {
            if (result.code = "200") {
                if (undefined != result.message.list) {
                    template('#myCommunityTmpl', '#myCommunity', result.message.list);
                } else {
                    template('#myCommunityTmpl', '#myCommunity', result.message);
                }
            } else {
                alert(result.message);
            }
        })
    }

    function countSelect() {
        var i = 0;
        $('.com').each(function () {
            i++;
        })
        return i;
    }

    //删除选中
    function deleteSelection() {
        var param = {};
        if (setParam(param, 'memberId')) {
            setParam(param, 'userId');
            param.communityId = communityId;
            common.getData('/community/deleteMembers.do', param, function (resp) {
                if (resp.code == "200") {
                    getMemberList(initPage);
                    comb2();
                    alert(resp.message);
                } else {
                    getMemberList(initPage);
                    comb2();
                    alert(resp.message);
                }
            })
        }

    }

    function getMemberList(page) {
        var isInit = true;
        var requestData = {};
        requestData.page = page;
        requestData.pageSize = 12;
        requestData.communityId = communityId;
        common.getData("/community/getMemberList.do", requestData, function (resp) {
            var resultData = resp.message.result;
            $('#memberPage').html("");
            if (resp.message.totalCount > 12) {
                $('#memberPage').jqPaginator({
                    totalPages: Math.ceil(resp.message.totalCount / requestData.pageSize) == 0 ? 1 : Math.ceil(resp.message.totalCount / requestData.pageSize),//总页数
                    visiblePages: 10,//分多少页
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
                            getMemberList(n);
                            $('body,html').animate({scrollTop: 0}, 20);
                        }
                    }
                });
            }
            template('#memberTmpl', '#member', resultData);
        })

    }

    function template(tmpl, ctx, data) {
        common.render({
            tmpl: tmpl,
            context: ctx,
            data: data,
            overwrite: 1
        })
    }

    function hx_update() {

        $.ajax({
            url: '/group/offlineMsgCount.do',
            success: function (resp) {
                var offCount = resp.message.offlineCount;
                if (offCount > 0) {
                    $('#hx-icon').removeClass("sp2");
                    $('#hx-icon').addClass('sp1');
                } else {
                    $('#hx-icon').addClass('sp2');
                }
                $('#hx-msg-count').text('您有' + offCount + '条未读消息');
            }
        });
    }

    function getPublishedActivitys(n) {
        var requestParm = {
            page: n
        };
        var init = true;
        common.getData("/factivity/published.do", requestParm, function (resp) {
            if (resp.code == '200') {
                if (resp.message.result.length <= 0) {
                    $('#activity-published-dev img').show();
                    $('#ul-activity-published').hide();
                    $('.published-page').hide();
                    return;
                }
                $('.published-page').jqPaginator({
                    totalPages: resp.message.totalPages,//总页数
                    visiblePages: 10,//分多少页
                    currentPage: resp.message.page,//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (init) {
                            init = false;
                        } else {
                            getPublishedActivitys(n);
                        }
                    }
                });
                template('#activityBox', '#ul-activity-published', resp.message.result);
                $('#ul-activity-published li button').each(function () {
                    $(this).text('取消活动');
                });
            }
        });
    }

    function getSignedActivitys(n) {
        var requestParm = {
            page: n
        };
        var init = true;
        common.getData("/factivity/signed.do", requestParm, function (resp) {
            if (resp.code == '200') {

                if (resp.message.result.length <= 0) {
                    $('#activity-signed-div img').show();
                    $('#ul-activity-signed').hide();
                    $('.signed-page').hide();
                    return;
                }
                $('.signed-page').jqPaginator({
                    totalPages: resp.message.totalPages,//总页数
                    visiblePages: 10,//分多少页
                    currentPage: resp.message.page,//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (init) {
                            init = false;
                        } else {
                            getSignedActivitys(n);
                        }
                    }
                });
                template('#activityBox', '#ul-activity-signed', resp.message.result);
            }
        });
    }

    function getAttendActivitys(n) {
        var requestParm = {
            page: n
        };
        var init = true;
        common.getData("/factivity/attended.do", requestParm, function (resp) {
            if (resp.code == '200') {
                if (resp.message.result.length <= 0) {
                    $('#activity-attended-div img').show();
                    $('#ul-activity-attended').hide();
                    $('.attended-page').hide();
                    return;
                }
                $('.attended-page').jqPaginator({
                    totalPages: resp.message.totalPages,//总页数
                    visiblePages: 10,//分多少页
                    currentPage: resp.message.page,//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (init) {
                            init = false;
                        } else {
                            getAttendActivitys(n);
                        }
                    }
                });
                template('#activityBox', '#ul-activity-attended', resp.message.result);
                $('#ul-activity-attended li button').hide();
            }
        });
    }

    function renderActivity() {
        if (activity_cur === 1) {

            $('#activity-signed-div').show();
            $('#activity-published-dev').hide();
            $('#activity-attended-div').hide();

        } else if (activity_cur === 2) {

            $('#activity-signed-div').hide();
            $('#activity-published-dev').show();
            $('#activity-attended-div').hide();

        } else if (activity_cur === 3) {

            $('#activity-signed-div').hide();
            $('#activity-published-dev').hide();
            $('#activity-attended-div').show();
        }
    }


    module.exports = communityMember;
});