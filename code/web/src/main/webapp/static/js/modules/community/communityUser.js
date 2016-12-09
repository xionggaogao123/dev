/**
 * Created by admin on 2016/11/17.
 */
/**
 * Created by admin on 2016/11/17.
 */
/**
 * Created by admin on 2016/10/27.
 */
define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    var communityUser = {};
    var page = 1;
    var sortType = 2;
    var personId=$('body').attr('personId');
    var applyName=$('body').attr('applyName');
    var zan = 1;
    var isLogin = false;
    communityUser.init = function () {
        postList(page, 0);//发帖列表
        getPublishedActivitys(1);
    };


    $(document).ready(function () {

        $.ajax({
            url: "/forum/loginInfo.do?date=" + new Date(),
            type: "get",
            dataType: "json",
            async: false,
            data: {},
            success: function (resp) {
                isLogin = resp.login;
            }
        });

        $('body').on('click', '.alert-diglog em,.alert-diglog .alert-btn-esc', function () {

            $('.alert-diglog').fadeOut();
            $('.bg').fadeOut();
        });

        $(".hx-notice").click(function () {
            window.open('/webim/index','_blank');
        });

        $('#forum-span').click(function () {
            $(this).addClass('hd-green-cur');
            $('#activity-span').removeClass('hd-green-cur');
            $('#theme-div').show();
            $('#activity-div').hide();
        });

        $('#activity-span').click(function () {
            $(this).addClass('hd-green-cur');
            $('#forum-span').removeClass('hd-green-cur');
            $('#theme-div').hide();
            $('#activity-div').show();
        });


        hx_update();

        setInterval(hx_update,1000 * 60);

        $('body').on('click','#apply',function(){
            if($(this).text()=="加玩伴"){
                var that=this;
                var contentValue = "我是"+applyName+",请求加为好友";
                var param = {};
                param.content = contentValue;
                param.personId = personId;
                common.getPostData('/forum/userCenter/applyFriend.do', param, function (resp) {
                    if (resp.code == "200") {
                        var str="<img src=\"/static/images/community/img_add.png\">等回复<em></em>";
                        $(that).html(str);
                        alert("玩伴申请已发出");
                    }
                });
            }
        })
    })


    function hx_update() {

        $.ajax({
            url:'/group/offlineMsgCount.do',
            success: function(resp){
                var offCount = resp.message.offlineCount;
                if(offCount > 0) {
                    $('#hx-icon').removeClass("sp2");
                    $('#hx-icon').addClass('sp1');
                } else {
                    $('#hx-icon').addClass('sp2');
                }
                $('#hx-msg-count').text('您有' + offCount + '条未读消息');
            }
        });
    }

    function postList(page, gtTime) {
        var isInit = true;
        var requestData = {};
        requestData.sortType = sortType;
        requestData.page = page;
        requestData.pageSize = 15;
        requestData.classify = -1;
        //requestData.cream=-1;
        requestData.gtTime = gtTime;
        requestData.postSection = "";
        requestData.zan = zan;
        requestData.person = personId;
        common.getData("/forum/fPosts.do?ti=" + new Date().getTime(), requestData, function (resp) {
            var posts = resp.list;
            $('#newPage').show();
            $('#newPage').html("");
            $('#theme').show();
            $('#imageTheme').hide();
            if (posts.length > 0) {
                $('#newPage').jqPaginator({
                    totalPages: Math.ceil(resp.count / requestData.pageSize) == 0 ? 1 : Math.ceil(resp.count / requestData.pageSize),//总页数
                    visiblePages: 3,//分多少页
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
                            postList(n, gtTime);
                            $('body,html').animate({scrollTop: 0}, 20);
                        }
                    }
                });
            }else{
                $('#imageTheme').show();
                $('#theme').hide();
            }
            template('#postListTml','#postList',posts);

        })
    }

    function getPublishedActivitys(n) {
        var requestParm = {
            page: n,
            personId:personId
        };
        var init = true;
        common.getData("/factivity/published.do", requestParm, function (resp) {
            if (resp.code == '200') {
                if(resp.message.result.length <= 0) {
                    $('.no-data').show();
                    $('.ac-have-data').hide();
                    return;
                }
                $('#activity-page').jqPaginator({
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
                template('#activityBox', '#published-activity', resp.message.result);

                $('#published-activity li button').click(function () {


                    if(!isLogin) {
                        $('.store-register').fadeIn();
                        $('.bg').fadeIn();
                        return;
                    }
                    $('.alert-diglog').fadeIn();
                    $('.bg').fadeIn();

                    $('.alert-diglog .alert-main span').html('确定要报名此次活动吗？');
                    var requestParm = {
                        acid: $(this).attr('value')
                    };
                    var me = $(this);
                    $('.alert-diglog .alert-btn-sure').click(function () {
                        common.getData("/factivity/sign.do", requestParm, function (resp) {
                            if (resp.code == '200') {
                                if(resp.code.message) {
                                    $('.alert-diglog').fadeOut();
                                    $('.bg').fadeOut();
                                    me.hide();
                                } else {
                                    alert("你已经报名了！");
                                    $('.alert-diglog').fadeOut();
                                    $('.bg').fadeOut();
                                    me.hide();
                                }

                            } else {
                                alert(resp.message);
                            }
                        });
                    });
                });
            }
        });
    }


    //加载模板
    function template(tmpl, ctx, data) {
        common.render({
            tmpl: tmpl,
            context: ctx,
            data: data,
            overwrite: 1
        })
    }

    module.exports = communityUser;
});