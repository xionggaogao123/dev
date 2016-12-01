define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    var communityId = $('#communityId').attr('communityId');
    var detailId=$('#communityId').attr('detailId');
    var communityDetail = {};
    communityDetail.init = function () {

        getMessageDetail();
        getCurrCommunity();
    };


    $(document).ready(function () {

        hx_update();

        setInterval(hx_update,1000 * 60);

        // var requestData = {
        //     communityId: communityId,
        //     communityDetailId: detailId
        // };

        $('.alert-btn-sure').click(function () {
            var requestData = {
                communityId: communityId,
                communityDetailId: detailId,
                msg: $('#beizhu').val()
            };

            //活动报名
            common.getData("/community/enterCommunityDetail.do", requestData, function (resp) {

                if (resp.code == 200) {
                    var sharebtn = $('.share-btn');
                    sharebtn.text("取消报名");
                    var count = parseInt($('#partInCount').text()) + 1;
                    $('#partInCount').text(count);
                    getPartInUsers();
                } else {
                    alert(resp.message);
                }

                $('.sign-alert').fadeOut();
                $('.bg').fadeOut();
            });

        });


        $('.alert-btn-sure_cancel').click(function () {
            var requestData = {
                communityId: communityId,
                communityDetailId: detailId,
                join: -1
            };

            //活动报名
            common.getData("/community/enterCommunityDetail.do", requestData, function (resp) {

                if (resp.code == 200) {
                    var sharebtn = $('.share-btn');
                    sharebtn.text("我要报名");

                    var count = parseInt($('#partInCount').text()) - 1;
                    $('#partInCount').text(count);
                    getPartInUsers();

                } else {
                    alert(resp.message);
                }

                $('.esc-alert').fadeOut();
                $('.bg').fadeOut();

                getPartInUsers();
            });

        });

        $('body').on('click', '.dianzan', function () {
            zan($(this));
        })
        $('body').on('click', '.yidianzan', function () {
            zan($(this));
        })

        $('body').on('click','.un-mark',function(){
            markContent($(this));
        })


        $('body').on('click','.alert-btn-esc,.sign-alert em',function () {

            $('.sign-alert').fadeOut();
            $('.bg').fadeOut();
        });


        $('body').on('click','.alert-btn-esc_cancel,.esc-alert em',function () {

            $('.esc-alert').fadeOut();
            $('.bg').fadeOut();

        });

        $('.share-btn').click(function () {
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
                        var text = $(that).text();
                        if (text == '我要报名') {
                            $('.sign-alert').fadeIn();
                            $('.bg').fadeIn();
                        } else if (text == '取消报名') {
                            $('.esc-alert').fadeIn();
                            $('.bg').fadeIn();
                        }
                    }else{
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            });

        });


        $("#send").click(function () {
            $.ajax({
                url: "/forum/loginInfo.do?date=" + new Date(),
                type: "get",
                dataType: "json",
                async: false,
                data: {},
                success: function (resp) {
                    var flag = resp.login;
                    if (flag) {
                        var sendText = $('#send').text();
                        if (sendText == '提交作业') {
                            submitHomeWork();
                            return;
                        }

                        if (sendText == '我要分享') {
                            submitShare();
                            return;
                        }
                        var type = $('#communityType').data('type');
                        var text = $('textarea').val();
                        var requestData = {
                            text: text,
                            detailId: detailId,
                            type: type,
                            communityId: communityId
                        };
                        //活动报名
                        common.getData("/community/replyToDetail.do", requestData, function (resp) {
                            if (resp.code == 200) {
                                getPartInContent(1);
                                $('textarea').val('');
                                alert('提交成功！');
                            } else {
                                alert(resp.message);
                            }
                        });
                    }else{
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            });
        });

        $('body').on('click', '#submit', function () {
            $.ajax({
                url: "/forum/loginInfo.do?date=" + new Date(),
                type: "get",
                dataType: "json",
                async: false,
                data: {},
                success: function (resp) {
                    var flag = resp.login;
                    if (flag) {
                        submitRecommend();
                    }else{
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            });
        })


        $('.file-download').click(function () {
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
                        var url = $(that).attr("url");
                        var flnm = $(that).attr("flnm");
                        location.href = "/commondownload/downloadFile.do?remoteFilePath=" + url + "&fileName=" + flnm;
                        var type = $('#communityType').data('type');
                        var requestData = {
                            detailId: detailId,
                            type: type,
                            text: "下载",
                            communityId: communityId
                        };
                        //活动报名
                        common.getData("/community/replyToDetail.do", requestData, function (resp) {
                            if (resp.code == 200) {
                                getDownloadData(1);
                            } else {
                                alert("记录下载数据失败");
                            }
                        });
                    }else{
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            });
        });

        $('body').on('click', '#get', function () {
            getProductModel();
        });

        $('body').on('click', '.pub-img em', function () {
            $(this).closest('.pub-img').remove();
        })

        $('body').on('click', '.p-doc em', function () {
            $(this).closest('.p-doc').remove();
        })

        $('body').on('click','.login-mk-btn .d2',function () {
            $('.store-register').fadeToggle();
            $('.bg').fadeToggle();
        })

        $('body').on('click','#outResult',function(){
            $.ajax({
                url: "/forum/loginInfo.do?date=" + new Date(),
                type: "get",
                dataType: "json",
                async: false,
                data: {},
                success: function (resp) {
                    var flag = resp.login;
                    if (flag) {
                        exportPartInData();
                    }else{
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            });
        })


    });


    //获取当前社区
    function getCurrCommunity() {
        common.getData('/community/' + communityId, {}, function (resp) {
            if (resp.code == "200") {
                $('.com-now').find('.p1').html(resp.message.name);
                $('.com-now').find('img').attr('src', resp.message.logo);
                $('.com-rlt').find('.p2').html("社区ID:" + resp.message.searchId);
            } else {
                alert(resp.message);
            }
        })
    }

    function markContent(obj){
        var contentId=obj.attr('contentId');
        var param={};
        param.contentId=contentId;
        common.getData('/community/mark.do',param,function(resp){
            if(resp.code=="200"){
               getPartInContent(1);
            }else{
                alert(resp.message);
            }
        })
    }

    function zan(obj) {
        var zanId = obj.attr('zanId');
        var zan = obj.attr('ownerZan');
        if (zan == 1) {
            zan = 0;
        } else {
            zan = 1;
        }
        var param = {};
        param.partInContentId = zanId;
        param.zan = zan;
        common.getData('/community/zanToPartInContent.do', param, function (resp) {
            if (resp.message) {
                if (zan == 1) {
                    obj.text(parseInt(obj.text()) + 1);
                    obj.attr('ownerZan',1);
                    obj.addClass('yidianzan').removeClass('dianzan');
                    alert("点赞成功！");
                } else {
                    obj.text(parseInt(obj.text()) - 1);
                    obj.attr('ownerZan',0);
                    obj.addClass('dianzan').removeClass('yidianzan');
                    alert("取消赞成功！");
                }
            } else {
                if (zan == 1) {
                    alert("你已点过赞了！");
                } else {
                    alert("你还未点赞！");
                }
            }
        })
    }


    function getProductModel() {
        var url = $('#shareUrl').val();
        if (url == "" || url == undefined) {
            // alert("路径不能为空！");
            return;
        }
        var Expression=/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
        var objExp=new RegExp(Expression);
        if(objExp.test(url)==false){
            alert("路径不符合规范！");
            return;
        }
        $('.pub-pro-show').data('shareUrl', url);
        var param = {};
        param.url = url;
        $('.pub-pro-show').html("正在解析中...")
        common.getDataAsync('/community/shareUrl.do', param, function (resp) {
            $('.pub-pro-show').empty();
            if (resp.code == "200") {
                var str = "<img src=\"" + resp.message.imageUrl + "\"" + ">" +
                    "<span style=\"color: #3379A8;cursor: pointer\" onclick=\"window.open('"+url+"')\">" + resp.message.productDescription + "</span>" +
                    "<p class=\"p2\">" + resp.message.productPrice + "</p>";
                $('.pub-pro-show').append(str);
                $('.pub-pro-show').data("share", true);
            } else {
                alert(resp.message);
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
        });
    }

    // $.urlParam = function (name) {
    //     var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
    //     if (results == null) {
    //         return null;
    //     }
    //     else {
    //         return results[1] || 0;
    //     }
    // };


    function getMessageDetail() {

        var requestData = {
            detailId: detailId
        };
        common.getData("/community/messageDetail.do", requestData, function (resp) {
            $('.share-btn').fadeOut();
            if (resp.code = "200") {
                var type = $('#type');
                var sharebtn = $('.share-btn');
                var result = $('#result');
                var outResult = $('#outResult');
                var title = $('#title');
                var needs_inf = $('.needs-inf');
                var input_title = $('#input_title');
                var send = $("#send");
                var partInCount = $('#partInCount');
                var signNum = $(".sign-num");
                partInCount.text(resp.message.partInCount);
                if (resp.message.attachements.length == 0) {
                    $('.file-wrap').hide();
                }
                if (resp.message.type == 1) {
                    $('#communityType').data('type', 1);
                    type.addClass("p-tz");
                    result.text('回复列表');
                    title.text("通知详情");
                    $('.new-page-links').hide();
                    $('.share-upload').hide();
                    $('#partInContent').hide();
                    $('#outResult').hide();
                }
                if (resp.message.type == 2) {
                    getPartInUsers();
                    $('#communityType').data('type', 2);
                    $('.share-btn').fadeIn();
                    type.addClass("p-hd");
                    sharebtn.text("我要报名");
                    result.text('报名结果');
                    title.text("活动详情");
                    input_title.text("回复一下");
                    send.text("发表回复");
                    // outResult.show();
                    signNum.show();
                    sharebtn.show();
                    checkIsEnter();
                    $('.sign-title').show().find('span').html('报名结果');
                    $('#outResult').show();
                }
                if (resp.message.type == 3) {
                    $('#outResult').hide();
                    $('#communityType').data('type', 3);
                    type.addClass("p-fx");
                    // sharebtn.show();
                    // sharebtn.text("我要分享");
                    send.text('我要分享');
                    title.text("分享详情");
                    result.text('分享列表');
                    $('.publish-btn .sp2').hide();
                    $('#partInContent').show();
                    $('.new-page-links').show();
                    $('.share-upload').show();
                    $('.sign-details').show();
                    getPartInContent(1);
                }
                if (resp.message.type == 4) {
                    $('#outResult').hide();
                    $('#communityType').data('type', 4);
                    type.addClass("p-zl");
                    title.text("学习资料详情");
                    // result.text('回复列表');
                    //
                    // result.text('下载结果');
                    $('.new-page-links').hide();
                    $('.share-upload').hide();
                    $('.sign-details').show();
                    $('.sign-title').show().find('#dload').html('下载结果');
                    $('#dloadData').show();
                    $('#partInContent').hide();
                    getDownloadData(1);
                }
                if (resp.message.type == 5) {
                    $('#outResult').hide();
                    getPartInContent(1);
                    $('#communityType').data('type', 5);
                    type.addClass("p-zy");
                    title.text("作业详情");
                    // result.text('下载结果');
                    sharebtn.text("提交作业");
                    send.text("提交作业");
                    result.text("提交结果");
                    input_title.text("");
                    $('.new-page-links').show();
                    $('.share-upload').show();
                    $('.sign-details').show();
                    $('#uploadButton').show();
                    $('.sp4').hide();
                }
                if (resp.message.type == 6) {
                    $('#outResult').hide();
                    getPartInContent(1);
                    title.text("学习用品详情");
                    $('#communityType').data('type', 6);
                    type.addClass("p-tj");
                    // result.text('推荐列表');
                    // title.text("推荐详情");
                    // needs_inf.show();
                    $('#recommend').show();
                    $('.pub-pro-show').data("share", false);
                    $('.sign-title').show();
                }
            }
        });

    }


    /**
     * 加载下载数据
     */
    function getDownloadData(n){
        var isInit = true;
        var requestData = {
            communityDetailId: detailId,
            page: n,
            pageSize:24
        };
        //活动报名
        common.getData("/community/partInContent.do", requestData, function (resp) {
            if (resp.code == 200) {
                $('.new-page-links').show().html('');
                if(resp.message.result.length>0){
                    $('.new-page-links').jqPaginator({
                        totalPages: resp.message.totalPages,
                        visiblePages: 5,//分多少页
                        currentPage: resp.message.page,//当前页数
                        first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                        prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                        next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                        last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                        page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                        onPageChange: function (n) { //回调函数
                            if (isInit) {
                                isInit = false;
                            } else {
                                getDownloadData(n);
                                $('body,html').animate({scrollTop: 0}, 20);
                            }
                        }
                    });

                    template('#downLoadTmpl', '#signData', resp.message.result);
                }else{
                    $('#signData').empty();
                    var image="<img src=\"/static/images/community/no-info.jpg\">"
                    $('#signData').append(image);
                }

            }
        });
    }

    function exportPartInData(){
        location.href = "/community/exportPartInData.do?detailId=" + detailId ;
    }


    function getPartInUsers() {
        var requestData = {
            detailId: detailId
        };
        //活动报名
        common.getData("/community/partInUsers.do", requestData, function (resp) {

            if (resp.code == 200) {
                if(resp.message.length>0){
                    template('#userTmpl', '#partInContent', resp.message);
                }else{
                    $('#partInContent').empty();
                    var image="<img src=\"/static/images/community/no-info.jpg\">"
                    $('#partInContent').append(image);
                }

            }
        });
    }


    function getPartInContent(n) {
        var isInit = true;
        var requestData = {
            communityDetailId: detailId,
            page: n
        };
        //活动报名
        common.getData("/community/partInContent.do", requestData, function (resp) {

            if (resp.code == 200) {
                $('.new-page-links').html('');
                if(resp.message.result.length>0){
                    $('.new-page-links').jqPaginator({
                        totalPages: resp.message.totalPages,
                        visiblePages: 5,//分多少页
                        currentPage: resp.message.page,//当前页数
                        first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                        prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                        next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                        last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                        page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                        onPageChange: function (n) { //回调函数
                            if (isInit) {
                                isInit = false;
                            } else {
                                getPartInContent(n);
                                $('body,html').animate({scrollTop: 0}, 20);
                            }
                        }
                    });

                    template('#textTempl', '#partInContent', resp.message.result);
                }else{
                   $('#partInContent').empty();
                   var image="<img src=\"/static/images/community/no-info.jpg\" class='img-noinfor'>"
                   $('#partInContent').append(image);
                }

            }
        });
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

    function addShareVideos(vedios) {
        $('.content-DV').each(function () {
            var item = {};
            item.vurl = $(this).find('img').eq(0).attr('vurl');
            item.vimage = $(this).find('img').eq(0).attr('src');
            vedios.push(item);
        })
    }

    function submitRecommend() {
        var param = {};
        var type = $('#communityType').data('type');
        param.communityDetailId = detailId;
        param.shareCommend = $('#comment').val();
        param.type = type;
        param.communityId = communityId;
        var image=$('.pub-pro-show').find('img').attr('src')
        if (image!=undefined&&image!=null&&image!="") {
            param.description = $('.pub-pro-show').find('span').html();
            param.shareUrl = $('.pub-pro-show').data('shareUrl');
            param.shareImage = $('.pub-pro-show').find('img').attr('src');
            param.sharePrice = $('.pub-pro-show').find('p').html();
        }else{
            if($('.pub-pro-show').data('shareUrl')){
                param.shareUrl = $('.pub-pro-show').data('shareUrl');
            }else{
                var url=$('#shareUrl').val();
                if(url!=""){
                    var Expression=/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
                    var objExp=new RegExp(Expression);
                    if(objExp.test(url)==true){
                        param.shareUrl=$('#shareUrl').val();
                    }else{
                        alert("路径不符合规范");
                        return;
                    }
                }else{
                    // alert("推荐路径不能为空！");
                    return;
                }
            }
        }
        common.getData('/community/recommend.do', param, function (resp) {
            getPartInContent(1);
            $('#shareUrl').val("");
            $('#comment').val("");
            $('.pub-pro-show').empty();
            alert(resp.message);
        })
    }

    function submitHomeWork() {

        var text = $('textarea').val();
        //加载附件信息
        var attachements = new Array();
        var images = new Array();
        //添加附件信息
        addAttachData(attachements);
        //添加图片信息
        addImageData(images);

        var imageUrls = "";
        for (var i = 0; i < images.length; i++) {
            imageUrls += images[i]['url'] + ",";
        }

        var attUrls = "";
        for (var j = 0; j < attachements.length; j++) {
            attUrls += attachements[j]['flnm'] + "@" + attachements[j]['url'] + ",";
        }
        var type = $('#communityType').data('type');
        var requestData = {
            content: text,
            communityDetailId: detailId,
            images: imageUrls,
            attacheMents: attUrls,
            communityId: communityId,
            type: type
        };
        common.getData("/community/submitHWork.do", requestData, function (resp) {
            if (resp.code == 200) {
                $('textarea').val('');
                getPartInContent(1);
                $('.pub-fj-img').empty();
                $('.pub-fj-doc').empty();
                alert("提交成功");
            } else {
                alert(resp.message);
            }
        });

    }

    function submitShare() {

        var text = $('textarea').val();
        //加载附件信息
        var vedios = new Array();
        var images = new Array();
        //添加视频信息
        addShareVideos(vedios);

        //添加图片信息
        addImageData(images);

        var imageUrls = "";
        for (var i = 0; i < images.length; i++) {
            imageUrls += images[i]['url'] + ",";
        }

        var vedioStr = "";
        for (var i = 0; i < vedios.length; i++) {
            vedioStr += vedios[i]['vimage'] + "@" + vedios[i]['vurl'] + ",";
        }
        var type = $('#communityType').data('type');
        var requestData = {
            content: text,
            communityDetailId: detailId,
            images: imageUrls,
            communityId: communityId,
            vedios: vedioStr,
            type: type
        };
        common.getData("/community/shareImages.do", requestData, function (resp) {
            if (resp.code == 200) {
                getPartInContent(1);
                $('textarea').val('');
                $('.pub-fj-vedio').empty();
                $('.pub-fj-img').empty();
                alert("提交成功");
            } else {
                alert(resp.message);
            }
        });

    }

    function checkIsEnter() {
        $.ajax({
            url: "/forum/loginInfo.do?date=" + new Date(),
            type: "get",
            dataType: "json",
            async: false,
            data: {},
            success: function (resp) {
                var flag = resp.login;
                if (flag) {
                    //活动报名
                    common.getData("/community/isEnterCommunityDetail.do", {communityDetailId: detailId}, function (resp) {

                        if (resp.code == 200) {
                            var sharebtn = $('.share-btn');
                            if (resp.message) {
                                sharebtn.text("取消报名");
                            } else {
                                sharebtn.text("我要报名");
                            }
                        } else {
                            alert(resp.message);
                        }
                    });
                }
            }
        });
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



    module.exports = communityDetail;
});