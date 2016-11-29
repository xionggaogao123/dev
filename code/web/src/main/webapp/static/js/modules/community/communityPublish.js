/**
 * Created by admin on 2016/10/27.
 */
define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    var communityId = $('body').attr('communityId');
    var personId=$('body').attr('userId');
    var communityPublish = {};
    communityPublish.init = function () {
        getMyCommunity();
        getCurrCommunity();
        //获取该社区详情列表
        getCommunityDetail();

        judgeMember();
    };

    $(document).ready(function () {

        $(".hx-notice").click(function () {
            window.open('/webim/index','_blank');
        });

        hx_update();

        setInterval(hx_update,1000 * 60);


        $('body').on('click', '.btn-yq', function () {
            $.ajax({
                url: "/forum/loginInfo.do?date=" + new Date(),
                type: "get",
                dataType: "json",
                async: false,
                data: {},
                success: function (resp) {
                    var flag = resp.login;
                    if (flag) {
                        $('.wind-yq').fadeIn();
                        $('.bg').fadeIn();
                    }else{
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            });

        });
        $('body').on('click', '.wind-yq .btn1', function () {
            var regular = $('#uid').val();
            if(regular==""){
                alert('请填写用户信息,不能为空！');
                return;
            }
            $('#load').show();
            getUserInfo(1);
        });

        $('body').on('click','.wind-wait .p1 em',function(){
            $('.wind-wait').fadeOut();
            $('.bg').fadeOut();
        });

        $('body').on('click', '.wind-yq .p1 em', function () {
            cancelFriend();
        });

        // $('body').on('click', '#invite', function () {
        //     inviteFriend();
        // })
        $('body').on('click','.btn3',function(){
            if($(this).html()=="邀请"){
                inviteFriend($(this));
            }

        })


        $('body').on('click', '#cancel', function () {
            comb2();
        })
        $('body').on('click','.d-x',function(){
            $('.wind').fadeOut();
            $('.bg').fadeOut();
        });
        $('.em-com-er').click(function(){
            $('.wind-erw').fadeIn();
            $('.bg').fadeIn();
        });
        $('body').on('click', '#announce_all', function () {
            var url = '/community/communityMessageList?communityId=' + communityId + "&type=1";
            window.open(url, '_blank');
        });

        $('body').on('click', '#activity_all', function () {
            var url = '/community/communityMessageList?communityId=' + communityId + "&type=2";
            window.open(url, '_blank');
        });

        $('body').on('click', '#share_all', function () {
            var url = '/community/communityMessageList?communityId=' + communityId + "&type=3";
            window.open(url, '_blank');
        });

        $('body').on('click', '#means_all', function () {
            var url = '/community/communityMessageList?communityId=' + communityId + "&type=4";
            window.open(url, '_blank');
        });


        $('body').on('click', '#homework_all', function () {
            var url = '/community/communityMessageList?communityId=' + communityId + "&type=5";
            window.open(url, '_blank');
        });

        $('body').on('click', '#materials_all', function () {
            var url = '/community/communityMessageList?communityId=' + communityId + "&type=6";
            window.open(url, '_blank');
        });


        $('.publish-nav').data('type', 1);
        $('body').on('click', '.publish-nav li', function () {
            //存储数据
            storeData(parseInt($('.green').attr('type')));
            var type = parseInt($(this).attr('type'))
            $('.publish-nav').data('type', type);
            $('.publish-nav li span').removeClass('disb');
            $(this).children('.sp2').addClass('disb');
            $('.publish-nav li span').removeClass('disn');
            $(this).children('.sp1').addClass('disn');
            $(this).addClass('green').siblings('.publish-nav li').removeClass('green');
            $('.publish-btn span').show();
            $('.pub-fj-pro').hide();

            if (type == 1) {
                $('.publish-btn .sp2').hide();
            } else if (type == 6) {
                $('.pub-fj-pro').show();
            }
            //清空数据
            emptyData();

            //然后加载数据
            loadData(type);

        })
        $('.publish-nav li').eq(0).trigger('click');

        $('body').on('click', '.pub-img em', function () {
            $(this).closest('.pub-img').remove();
        })

        $('body').on('click', '.p-doc em', function () {
            $(this).closest('.p-doc').remove();
        })

        $('body').on('click', '#submit', function () {
            publish();
        })

        $('body').on('click', '#try', function () {
            getProductInfo();
        })
        
        $('body').on('click','#sl',function() {
            sl();
        })

        $('body').on('click', '#join', function () {
            var param = {};
            param.communityId = communityId;
            common.getData('/community/join.do', param, function (resp) {
                if (resp.code == "200") {
                    alert(resp.message);
                    getMyCommunity();
                } else {
                    alert(resp.message);
                }
            })
        })

        $('body').on('click', '.commit', function () {
            var that=this;
            $.ajax({
                url: "/forum/loginInfo.do?date=" + new Date(),
                type: "get",
                dataType: "json",
                async: false,
                data: {},
                success: function (resp) {
                    var flag = resp.login;
                    if (flag) {
                        enterCommunityDetail($(that));
                    }else{
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            })

        })

        $('body').on('click', '.member-more', function () {
            $.ajax({
                url: "/forum/loginInfo.do?date=" + new Date(),
                type: "get",
                dataType: "json",
                async: false,
                data: {},
                success: function (resp) {
                    var flag = resp.login;
                    if (flag) {
                        window.location.href = "/community/communityMember.do?communityId=" + communityId;
                    }else{
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            })
        })

        $('body').on('click','.spread',function () {
            spread($(this));
        })
        $('body').on('click','.collect',function () {
            collect($(this));
        })

        $('body').on('click','.personNal',function(){
            personNal($(this));
        })

        $('body').on('click','.delete-detail',function(){
            showDelete($(this));
        })

        $('body').on('click','#sureCancel',function(){
            sureCancel();
        })

        $('body').on('click','.si-s3 em,.si-s3 .alert-btn-esc',function(){
            ss3();
        })

        $('body').on('click','.login-mk-btn .d2',function () {
            $('.store-register').fadeToggle();
            $('.bg').fadeToggle();
        })
    })

    function sureCancel(){
        var param={};
        param.detailId=$('.si-s3').data('detailId');
        common.getData('/community/removeDetailById.do',param,function (resp) {
            ss3();
            getCommunityDetail();
        })
    }

    function ss3(){
        $('.si-s3').hide();
        $('.bg').hide();
    }


    function showDelete(obj){
        $('.si-s3').data('detailId',obj.attr('detailId'));
        $('.si-s3').show();
        $('.bg').show();
    }

    function judgeMember(){
        $.ajax({
            url: "/forum/loginInfo.do?date=" + new Date(),
            type: "get",
            dataType: "json",
            async: false,
            data: {},
            success: function (resp) {
                var flag = resp.login;
                if (flag) {
                    common.getData('/community/judgeMember', {communityId: communityId}, function (resp) {
                        if (resp.code == "200") {
                            if (resp.message) {
                                $('#join').hide();
                            }
                        }
                    });
                }
            }
        });
    }

    function personNal(obj){
        var userId=obj.attr('userId');
        $.ajax({
            url: "/forum/loginInfo.do?date=" + new Date(),
            type: "get",
            dataType: "json",
            async: false,
            data: {},
            success: function (resp) {
                var flag = resp.login;
                if (flag) {
                    if(userId!=personId){
                        window.open('/community/userData.do?userId='+userId);
                    }
                }else{
                    $('.store-register').fadeToggle();
                    $('.bg').fadeToggle();
                }
            }
        });

    }

    function  spread(obj) {
        obj.closest('p').css('max-height','140px');
        obj.closest('span').html('<em class="spread">[收起全文]</em>').removeClass('spread').addClass('collect');
    }

    function collect(obj) {
        obj.closest('p').css('max-height','60px');
        obj.closest('span').html('...<em class="spread">[展开全文]</em>').removeClass('collect').addClass('spread');
    }

    function getUserInfo(page) {
        var isInit = true;
        var regular = $('#uid').val();
        var requestData = {};
        requestData.regular = regular;
        requestData.page=page;
        requestData.pageSize=5;
        $.ajax({
            type: "GET",
            data: requestData,
            url: '/community/getUserInfo.do',
            async: true,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            // beforeSend:function() {
            //
            // },
            // complete:function(data) {
            //    $('#load').hide();
            // },
            success: function (resp) {
                $('#load').hide();
                $('.new-page-links').html("");
                if (resp.code == "200") {
                    $('#dlUser').show();
                    var resultData = resp.message.list;
                    $('#userCount').text(resp.message.count);
                    if (resultData.length > 0) {
                        $('.new-page-links').jqPaginator({
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
                    template('#userListTmpl','#userList',resultData);
                } else {
                    alert(resp.message);
                }
            }
        });
    }
    
    function sl() {
        $("div[class='datagrid-mask-msg']").remove();
    }

    function inviteFriend(obj) {
        var userId = obj.attr('userId');
        var param = {};
        param.communityId = communityId;
        param.userId = userId;
        common.getData('/community/inviteMember.do', param, function (resp) {
            if(resp.code=="200"){
                obj.html('邀请成功');
                obj.css('color','gray').css('border','0');
            }else{
                obj.html('已经是会员');
                obj.css('color','gray').css('border','0');
                alert(resp.message);
            }
        })
    }

    function cancelFriend(){
        comb2();
        //重新加载数据
        getCurrCommunity();

    }

    function comb2() {
        $('#uid').val("");
        $('.wind').fadeOut();
        $('.bg').fadeOut();
        $('#userList').empty();
        $('#dlUser').hide();
        $('.new-page-links').html('');
    }

    function enterCommunityDetail(obj) {
        var communityDetailId = obj.attr('itemId');
        var requestParam = {};
        requestParam.communityDetailId = communityDetailId;
        requestParam.communityId=communityId,
        common.getData('/community/enterCommunityDetail.do', requestParam, function (resp) {
            if (resp.code == "200") {
                var count=parseInt(obj.next().find('em').html())+1;
                obj.next().find('em').html(count);
                alert(resp.message);
            } else {
                alert(resp.message);
            }
        })
    }


    //根据路径获取商品信息
    function getProductInfo() {
        var url = $('#shareUrl').val();
        if (url == "" || url == undefined) {
            // alert("路径不能为空！");
            return;
        }
        var Expression=/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
        var objExp=new RegExp(Expression);
        if(objExp.test(url)==false){
            // alert("路径不符合规范！");
            return;
        }
        $('.pub-pro-show').data('shareUrl',url);
        var param = {};
        param.url = url;
        common.getData('/community/shareUrl.do', param, function (resp) {
            $('.pub-pro-show').empty();
            if(resp.code=="200"){
                var str = "<img src=\"" + resp.message.imageUrl + "\"" + ">" +
                    "<p class=\"p1\"><a onclick=\"window.open('"+url+"')\">" + resp.message.productDescription + "</a></p>" +
                    "<p class=\"p2\">" + resp.message.productPrice + "</p>";
                $('.pub-pro-show').append(str);
            }else{
                alert(resp.message);
            }


        })
    }

    //加载数据
    function loadData(type) {
        $('.pub-fj-img').html($('.publish-nav').data('pubIm' + type));
        $('.pub-fj-doc').html($('.publish-nav').data('pubDoc' + type));
        $('#voice_notice').html($('.publish-nav').data("pubVoice" + type));
    }

    //清空数据
    function emptyData() {
        $('.pub-fj-img').empty();
        $('.pub-fj-doc').empty();
        $('#voice_notice').empty();
    }

    //存储数据
    function storeData(type) {
        $('.publish-nav').data('pubIm' + type, $('.pub-fj-img').html());
        $('.publish-nav').data('pubDoc' + type, $('.pub-fj-doc').html());
        $('.publish-nav').data("pubVoice" + type, $('#voice_notice').html());
    }

    //添加附件信息
    function addAttachData(attachements) {
        $('.p-doc').each(function () {
            var item = {};
            item.url = $(this).attr('url');
            item.flnm = $(this).find('span').text();
            attachements.push(item);
        })
    }

    //添加图片信息
    function addImageData(images) {
        $('.pub-img').each(function () {
            var item = {};
            item.url = $(this).find('img').attr('src');
            item.flnm = $(this).attr('fileName');
            images.push(item);
        })
    }

    //添加语音信息
    function addVoice(vedios) {
        $('.voice').each(function () {
            var item = {};
            item.url = $(this).attr('url');
            item.flnm = $(this).attr('fileName');
            vedios.push(item);
        })
    }

    //设置空值
    function setMessage(message) {
        if($('.pub-pro-show').data('shareUrl')!=undefined&&
            $('.pub-pro-show').data('shareUrl')!=null
            &&$('.pub-pro-show').data('shareUrl')!=""){
            message.shareUrl = $('.pub-pro-show').data('shareUrl');
        }else{
            message.shareUrl="";
        }

        message.shareImage = "";
        message.shareTitle = "";
        message.sharePrice = "";
    }

    //发布新社区内容
    function publish() {
        if (validate()) {
            var message = {};
            var type = $('.publish-nav').data('type')
            message.communityId = communityId;
            message.title = $('#title').val();
            message.content = $('#content').val();
            message.type = type;
            //加载附件信息
            var attachements = new Array();
            var images = new Array();
            var vedios = new Array();
            //添加附件信息
            addAttachData(attachements);
            //添加图片信息
            addImageData(images);
            //添加语音信息
            addVoice(vedios);

            message.attachements = attachements;
            message.images = images;
            message.vedios = vedios;
            if (type == 6) {
                if ($('.pub-pro-show').html() == "") {
                    setMessage(message);
                } else {
                    message.shareUrl =  $('.pub-pro-show').data('shareUrl');
                    message.shareImage = $('.pub-pro-show').find('img').attr('src');
                    message.shareTitle = $('.pub-pro-show').find('a').html();
                    message.sharePrice = $('.pub-pro-show').find('.p2').html();
                    ;
                }
            } else {
                setMessage(message);
            }
            var url = "/community/newMessage.do";
            $.ajax({
                type: "POST",
                url: url,
                async: false,
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(message),
                success: function (result) {
                    if (result.code == "200") {
                        $('#title').val("");
                        $('#content').val("");
                        if (type != 1) {
                            emptyData();
                        }
                        if(type==6){
                            $('.pub-pro-show').empty();
                            $('#shareUrl').val("");
                        }
                        $('#announcement').empty();
                        $('#activity').empty();
                        $('#share').empty();
                        $('#means').empty();
                        $('#homework').empty();
                        $('#materials').empty();
                        getCommunityDetail();
                    } else {
                        alert(result.message);
                    }
                }
            });

        }
    }

    function validate() {
        var title = $('#title').val();
        var content = $('#content').val();
        if (title == "" || title == undefined) {
            alert("标题不能为空!");
            return false;
        }

        if(title.length>=15){
            alert("标题字数不能超过15个");
            return;
        }
        if (content == "" || content == undefined) {
            alert("内容不能为空!");
            return false;
        }
        if(content.length>=200){
            alert("内容字数不能超过200个!");
            return false;
        }
        return true;
    }

    //获取当前社区
    function getCurrCommunity() {
        common.getData('/community/' + communityId, {}, function (resp) {
            if (resp.code == "200") {
                $('.com-rlt').find('img').attr('src', resp.message.logo);
                $('.com-rlt').find('.p1').html(resp.message.name);
                $('.com-rlt').find('.p2').html("社区ID:" + resp.message.searchId);
                $('.com-rlt').find('.p3').html("社区简介：" + resp.message.desc);
                $('.com-now').find('.p1').html(resp.message.name);
                $('.com-now').find('img').attr('src', resp.message.logo);
                $('.com-now').find('.p2').html("社区ID:" + resp.message.searchId);

                $('.wind-erw').find('.d1').find('img').attr('src', resp.message.logo);
                $('.wind-erw').find('.d1').find('.dp1').html(resp.message.name);
                $('.wind-erw').find('.d1').find('.dp2').html("社区ID:" + resp.message.searchId);
                $('.wind-erw').find('.d2').find('img').attr('src', resp.message.qrUrl);
                template('#memberTmpl', '#member', resp.message.members);
            } else {
                alert(resp.message);
            }
        })
    }

    //获取我的社区
    function getMyCommunity() {
        common.getData("/community/myCommunitys.do", {}, function (result) {
            if (result.code = "200") {
                template('#myCommunityTmpl', '#myCommunity', result.message);
            } else {
                alert(result.message);
            }
        })
    }

    //获取该社区详情信息
    function getCommunityDetail() {
        var param = {};
        param.communityId = communityId;
        param.pageSize = 1;
        common.getData('/community/typeMessages.do', param, function (resp) {
            if (resp.code == "200") {
                var announcement = resp.message.announcement;
                var activity = resp.message.activity;
                var share = resp.message.share;
                var means = resp.message.means;
                var homework = resp.message.homework;
                var materials = resp.message.materials;

                template('#announcementTmpl', '#announcement', announcement);
                template('#activityTmpl', '#activity', activity);
                template('#shareTmpl', '#share', share);
                template('#meansTmpl', '#means', means);
                template('#homeworkTmpl', '#homework', homework);
                template('#materialsTmpl', '#materials', materials);


                var tempStr="<div class=\"notice-container clearfix com-nothing\">";
                if (announcement.length == 0) {
                    var prev="<div class=\"com-tit\" id=\"announce_all\">社区通知<em>全部</em></div>";
                    var str=prev+tempStr+"该社区还未发布通知"+"</div>";
                    $('#announcement').append(str);
                }
                if (activity.length == 0) {
                    var prev="<div class=\"com-tit\" id=\"activity_all\">组织活动报名<em>全部</em></div>";
                    var str=prev+tempStr+"该社区还未发布活动报名"+"</div>";
                    $('#activity').append(str);
                }
                if (share.length == 0) {
                    var prev="<div class=\"com-tit\" id=\"share_all\">火热分享<em>全部</em></div>";
                    var str=prev+tempStr+"该社区还未发布火热分享"+"</div>";
                    $('#share').append(str);
                }
                if (means.length == 0) {
                    var prev="<div class=\"com-tit\" id=\"means_all\">学习资料<em>全部</em></div>";
                    var str=prev+tempStr+"该社区还未发布学习资料"+"</div>";
                    $('#means').append(str);
                }
                if (homework.length == 0) {
                    var prev="<div class=\"com-tit\" id=\"homework_all\">作业<em>全部</em></div>";
                    var str=prev+tempStr+"该社区还未发布作业"+"</div>";
                    $('#homework').append(str);
                }
                if (materials.length == 0) {
                    var prev="<div class=\"com-tit\" id=\"materials_all\">学习用品<em>全部</em></div>";
                    var str=prev+tempStr+"该社区还未发布学习用品需求"+"</div>";
                    $('#materials').append(str);
                }
            }
        })
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


    function hx_update() {

        $.ajax({
            url:'/group/offlineMsgCount.do',
            success: function(resp){
                var hx_notice = $('.hx-notice span');
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

    module.exports = communityPublish;
});