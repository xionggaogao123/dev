/**
 * Created by admin on 2016/11/17.
 */
/**
 * Created by admin on 2016/10/27.
 */
define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    var communityHead = {};
    var nickName = $('#apply').attr('applyName');

    var ons = [];
    var myOns = [];
    communityHead.init = function () {
        getAllOns();
    };


    $(document).ready(function () {

        $('body').on('click', '#chat', function () {
            $('.wind-jwb').fadeIn();
            $('.bg').fadeIn();
        });
        $('.wind-erw .d-x').click(function(){
            $('.wind-erw').fadeOut();
            $('.bg').fadeOut();
        })

        // $('.mine-er').click(function(){
        //     $('.wind-erw').fadeIn();
        //     $('.bg').fadeIn();
        // })

        $('body').on('click', '.wind-jwb .btn1', function () {
            var regular = $('#jwbUid').val();
            if (regular == "") {
                alert('请填写用户信息,不能为空！');
                return;
            }
            $('#jwbLoad').show();
            getUserInfo(1);
        });

        $('body').on('click', '.wind-jwb .p1 em', function () {
            cancelFriend();
        });

        $('body').on('click', '#btn1', function () {
            comb2();
            getMyTags();
        });


        $('body').on('click', '#editTag', function () {
            common.getData('/community/getMyTags.do', {}, function (resp) {
                var tags = resp.message;
                for (var i in tags) {
                    var obj = $('#myTxt').find('[tag=' + tags[i] + ']');
                    obj.addClass('bq-cur');
                    var str = "<span class=\"bq-cur\" code=\"" + obj.attr('code') + "\">" + obj.text() + "</span>";
                    $('#selected').append(str);
                }

                $('.bg').fadeIn();
                $('.wind-biaoq').fadeIn();
            })
        });

        $('body').on('click', '#editOns', function () {

            $('.ons-div span').each(function () {
                $(this).removeClass('bq-cur');
            });

            $('.ons-div span').each(function () {
                for (var i = 0; i < myOns.length; i++) {
                    if (myOns[i].code == $(this).attr('code')) {
                        $(this).addClass('bq-cur');
                    }
                }
            });

            $('.bg').fadeIn();
            $('.wind-ons').fadeIn();
        });

        $('body').on('click', '.wind-biaoq .p1 em,.btn-add-no', function () {
            comb2();
            getMyTags();
        });

        $('body').on('click', '.jwbUserId', function () {
            jwb($(this));
        });

        $('body').on('click', '.ons-div span', function () {
            if ($(this).hasClass('bq-cur')) {
                $(this).removeClass('bq-cur');
            } else {
                $(this).addClass('bq-cur');
            }
        });

        $('body').on('click', '.wind-ons .p3 .btn-add-no,.wind-ons .p1 em', function () {
            $('.wind-ons').fadeOut();
            $('.bg').fadeOut();
        });

        $('body').on('click', '.wind-ons .p3 .btn1', function () {

            ons = [];
            $('.ons-div span').each(function () {
                var code = $(this).attr('code');
                if ($(this).hasClass('bq-cur')) {
                    ons.push(code);
                }
            });

            var onsLi = '';
            for(var i=0;i<ons.length;i++) {
                onsLi += ons[i] + ',';
            }
            var requestParm = {
                ons: onsLi
            };

            common.getData('/mate/updateMateData.do', requestParm, function (resp) {
                if (resp.code == '200') {
                    $('.wind-ons').fadeOut();
                    $('.bg').fadeOut();

                    getAllOns();
                } else {
                    alert(resp.message);
                }
            });
        });

        $('body').on('click', '#myTxt span', function () {
            var that = this;
            if ($(this).hasClass('bq-cur')) {
                var param = {};
                param.code = $(this).attr('code');
                common.getData('/community/pullUserTag.do', param, function (resp) {
                    if (resp.code == "200") {
                        $('#selected').find('[code=' + $(that).attr('code') + ']').remove();
                        $(that).removeClass('bq-cur');
                    }
                })
            } else {
                //先判断是否超过六个标签
                var count = 0;
                $('#selected span').each(function () {
                    count++;
                });
                if (count >= 6) {
                    alert("用户最多选择六个标签");
                    return;
                }
                var str = "<span class=\"bq-cur\" code=\"" + $(this).attr('code') + "\">" + $(this).text() + "</span>";
                $('#selected').append(str);
                $(this).addClass('bq-cur');
                var userTag = {};
                userTag.code = $(this).attr('code');
                userTag.tag = $(this).text();
                $.ajax({
                    type: "POST",
                    url: '/community/pushUserTag.do',
                    async: false,
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(userTag),
                    success: function (result) {
                    }
                });
            }
        });

        $('body').on('click','#myInfo',function(){
            common.getData('/community/getMyInfo.do',{},function(resp){
                if(resp.code=="200"){
                    var message=resp.message;
                    $('.wind-myInfo .d1').find('img').attr('src',message.avatar);
                    $('.wind-myInfo .d1 .dp1').html(message.nickName);
                    $('.wind-myInfo .d1 .dp2').html("UID:"+message.uid);
                    $('.wind-myInfo .d2').find('img').attr('src',message.qrCode);
                    $('.wind-myInfo').fadeIn();
                    $('.bg').fadeIn();
                }

            })
        })

        $('body').on('click','.wind-myInfo .d-x',function(){
            $('.wind-myInfo').fadeOut();
            $('.bg').fadeOut();
        })

    });

    function jwb(obj) {
        var userId = obj.attr('userId');
        var contentValue = "我是" + nickName + ",请求加为好友";
        var param = {};
        param.content = contentValue;
        param.personId = userId;
        common.getPostData('/forum/userCenter/applyFriend.do', param, function (resp) {
            if (resp.code == "200") {
                obj.html("等待回复");
                obj.css('color', 'gray').css('border', '0');
                alert("玩伴申请已发出");
            } else {
                obj.html("等待回复");
                obj.css('color', 'gray').css('border', '0');
                alert(resp.message);
            }
        });
    }

    function cancelFriend() {
        comb3();

    }

    function comb3() {
        $('#jwbUid').val("");
        $('.wind-jwb').fadeOut();
        $('.bg').fadeOut();
        $('#jwbUserList').empty();
        $('#jwbUser').hide();
        $('#jwb').html('');
    }


    function getUserInfo(page) {
        var isInit = true;
        var regular = $('#jwbUid').val();
        var requestData = {};
        requestData.regular = regular;
        requestData.page = page;
        requestData.pageSize = 5;
        $.ajax({
            type: "GET",
            data: requestData,
            url: '/community/getUserInfo.do',
            async: true,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (resp) {
                $('#jwbLoad').hide();
                $('#jwb').show().html("");
                if (resp.code == "200") {
                    $('#jwbUser').show();
                    var resultData = resp.message.list;
                    $('#jwbCount').text(resp.message.count);
                    if (resultData.length > 0) {
                        $('#jwb').jqPaginator({
                            totalPages: Math.ceil(resp.message.count / requestData.pageSize) == 0 ? 1 : Math.ceil(resp.message.count / requestData.pageSize),//总页数
                            visiblePages: 2,//分多少页
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
                                    getUserInfo(n);
                                    $('body,html').animate({scrollTop: 0}, 20);
                                }
                            }
                        });
                    }
                    template('#jwbUserListTmpl', '#jwbUserList', resultData);
                } else {
                    alert(resp.message);
                }
            }
        });
    }

    function getMyTags() {
        $.ajax({
            type: "GET",
            data: {},
            url: '/community/getMyTags.do',
            async: false,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (resp) {
                $('#myTags').empty();
                var tags = resp.message;
                var str = "";
                for (var i in tags) {
                    str += "<em>" + tags[i] + "</em>";
                }
                str += "<em id=\"editTag\">编辑标签</em>";
                $('#myTags').append(str);
            }
        });
    }

    function getAllOns() {
        $.ajax({
            type: "GET",
            url: '/mate/sortType.do',
            success: function (resp) {
                var str = '';
                for (var i = 0; i < resp.message.times.length; i++) {
                    str += '<span code="' + resp.message.times[i].code + '">' + resp.message.times[i].data + '</span>';
                }
                $('.ons-div').empty();
                $('.ons-div').append(str);
            }
        });

        myOns = [];
        common.getData('/mate/getMyOns.do', {}, function (resp) {
            var str = '';
            for (var i = 0; i < resp.message.length; i++) {
                str += '<em>' + resp.message[i].data + '</em>';
                myOns.push(resp.message[i]);
            }
            $('#myOns').empty();
            str += '<em id="editOns">编辑时间段</em>';
            $('#myOns').append(str);
        });
    }

    function comb2() {
        $('.wind-biaoq').fadeOut();
        $('.bg').fadeOut();
        $('.bq-list span').removeClass('bq-cur');
        $('#selected').empty();
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

    module.exports = communityHead;
});