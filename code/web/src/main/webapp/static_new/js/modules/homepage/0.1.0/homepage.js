/*
 * @Author: Tony
 * @Date:   2015-06-11 14:24:31
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-06-30 16:43:27
 */

'use strict';
define(['jquery','doT','easing','common','sharedpart','uploadify','fancybox','experienceScore'],function(require,exports,module){
    /**
     *初始化参数
     */
    var homepage = {},
        Common = require('common');
    var someFileFailed = false;
    //提交参数
    var homepageData = {};
    var blogType = 1;

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * homepage.init()
     */
    homepage.init = function(){


        homepage.uploadVideo();
        homepage.homepageFileUpload();
        homepage.getThemes();
        homepage.setNotifyNum();
        //设置初始页码
        homepageData.page = 1;
        //设置每页数据长度
        homepageData.pageSize = 12;

        homepageData.hottype = $('.active').attr('index');
        if (getQueryString('version')==1) {
            blogType = 1;
        } else if (getQueryString('version')==2) {
            blogType = 2;
        }
        // 查询数据
        homepage.initHomePageData(1);
        homepage.publishSchoolMicoSecurity();

        $("#ZI").click(function(event) {
            homepageData.type = 1;
            homepage.getFriendReplyInfo(this,1);
            $("#PL").css({
                "display": 'block'
            });
            $("#tab_Main").css({
                "display": 'none'
            });
        });
        $(".themeitem2").click(function(event) {
            $("#micoblog_content").val($("#micoblog_content").val()+$(this).text());
            $(".huati").css({
                "display": 'none'
            });
        });
        $(".player-close-btn").click(function(event) {
            homepage.closeCloudView();

        });

        $(".themeitem").click(function(event) {
            homepageData.theme=$(this).attr("con");
            homepageData.hottype = $('.active').attr('index');
            $("#tab_Main").css({
                "display": 'block'
            });
            $("#PL").css({
                "display": 'none'
            });
            homepage.initHomePageData(2);
        });
        $("#FH").click(function(event) {
            $('.tab-deit').hide();
            $("#tab_Main").css({
                "display": 'block'
            });
            $("#PL").css({
                "display": 'none'
            });
        });

        $(".iconhua").click(function(event) {
            $(".huati").css({
                "display": 'block'
            });
        });
        $(".i_hua").click(function(event) {
            $(".huati").css({
                "display": 'none'
            });
        });

        //年级筛选
        $('.select-order').change(function() {
            //设置初始页码
            homepageData.page = 1;
            //设置每页数据长度
            homepageData.pageSize = 12;
            homepage.initHomePageData(1);
        });
    };


    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }



    // blog 最新最热切换
    $('.order-blue').each(function(i) {
        $(this).bind('click', function() {
            $('.order-blue').removeClass('active');
            $(this).addClass('active');
            //设置初始页码
            homepageData.page = 1;
            //设置每页数据长度
            homepageData.pageSize = 12;
            homepageData.hottype = i+1;
                homepage.initHomePageData(1);

        });
    });
    var zantp = false;
    homepage.zanchoose = function() {
        $(".date-txt .zan,.er_l .zan").hover(function() {
            if ($(this).children('img').attr('src')=='/static_new/images/zan_2.jpg') {
                zantp = true;
            }
            $(this).css({
                "color": '#ff4500'
            });
            $(this).children('img').attr({
                "src": '/static_new/images/zan_2.jpg'
            });
        },function(){
            if (!zantp) {
                $(this).css({
                    "color": '#999'
                });
                $(this).children('img').attr({
                    "src": '/static_new/images/zan_1.png'
                });
            }
            zantp = false;
        });
    }
    homepage.initHomePageData = function(type) {
        homepageData.seachtype = $("#order").val();
        homepageData.blogtype = blogType;
        if (type==1) {
            homepageData.theme = "";
        }

        Common.getData('/homeschool/selFriendBlogInfo.do',homepageData,function(rep){
            $('.hwk1').html('');
            Common.render({tmpl: $('#hwk1_templ'), data: rep, context: '.hwk1'});
            //var slideHeight = 70;
            //var defHeight = $('.list-txt p').height();
            //if(defHeight >= slideHeight){
            //    $('.list-txt p').css('height' , slideHeight + 'px');
            //    $('#read-more').html('展开');
            //    $('#read-more').click(function(){
            //        var dom = $(this).parent('.list-txt').find('p');
            //        var curHeight = dom.height();
            //        if(curHeight == slideHeight){
            //            dom.animate({
            //                height: defHeight
            //            }, "normal");
            //            $('#read-more').html('隐藏');
            //            $('#gradient').fadeOut();
            //        }else{
            //            dom.animate({
            //                height: slideHeight
            //            }, "normal");
            //            $('#read-more').html('展开');
            //            $('#gradient').fadeIn();
            //        }
            //        return false;
            //    });
            //}
            $(".zhankai").click(function(event) {
                var content = $(this).attr('concent');
                var $button = $(this),
                    textDiv = $button.closest(".info-content");
                $button.detach();
                if (textDiv.is(".contentHidden")) {
                    textDiv.removeClass("contentHidden").html(content);
                    $button.text("收起").appendTo(textDiv);
                }
                else {
                    textDiv.addClass("contentHidden").html(content.substring(0, 105) + '...');
                    $button.text("展开").appendTo(textDiv);
                }
            });
            $(".mox").click(function(event) {
                homepageData.blogid = $(this).attr('id');
                homepageData.type=2;
                homepageData.page=1;
                homepageData.pageSize=0;
                homepage.getFriendReplyInfo(this,1);
                $(this).parents('.list-info').find('.mo').toggle();
            });
            $(".mo textarea").focus(function(event) {
                $(this).css({
                    "border": '1px solid #ffa33a'
                });
            });
            $(".mo textarea").blur(function(event) {
                $(this).css({
                    "border": '1px solid #d0d0d0'
                });
            });
            //$(".videoshow2").click(function(event) {
            //    $(".player-close-btn").css({
            //        "display": 'block'
            //    });
            //    homepage.tryPlayYCourse($(this));
            //});

            homepage.zanchoose();
            $('.fancybox').fancybox();
            var option = {
                total: rep.total,
                pagesize: rep.pageSize,
                currentpage: rep.page,
                operate: function (totalPage) {
                    $('.page-index span').each(function () {
                        $(this).click(function(){
                            homepageData.page=$(this).text();
                            homepage.initHomePageData(type);
                        })
                    });
                    $('.first-page').click(function(){
                        homepageData.page=1;
                        homepage.initHomePageData(type);
                    });
                    $('.last-page').click(function(){
                        homepageData.page=totalPage;
                        homepage.initHomePageData(type);
                    })
                }
            }
            homepage.initPaginator(option);

        });

        /*
         * 绑定zan按钮
         * */
        $('.zan_cls').bind("click",function(event){
            homepageData.blogid=$(this).attr('id');
            homepage.isBlogZan(this);
        });
        $(".reply").click(function(event) {
            homepageData.id=$(this).attr('mid');
            homepageData.blogid=$(this).attr('mid');
            var cnt = $(this).parents('.list-info').find('.content-reply').val();
            homepageData.blogcontent = cnt;
            homepageData.userid = $(this).attr('uid');
            homepageData.replytype=2;
            if (cnt.length == 0 || $.trim(cnt) == '') {
                alert("评论不可为空！");
                return;
            }
            homepage.replyBlog(this);
        });
        /*
         * 绑定删除按钮
         * */
        $('.del_cls').bind("click",function(event){
            if (confirm("确定要删除该条微博吗？")) {
                homepageData.id = $(this).attr('id');
                homepage.deleteMicroblog(1,this);
                //查询校园安全信息数据
                homepage.initHomePageData(type);
            }
        });
    }

    homepage.getFriendReplyInfo=function (dom,value) {
        Common.getData('/homeschool/friendReplyInfo.do',homepageData,function(rep){
            if (homepageData.type==1) {
                $('.hwk2').html('');
            Common.render({tmpl:$('#hwk2_templ'),data:rep,context:'.hwk2'});
                var option = {
                    total: rep.total,
                    pagesize: rep.pageSize,
                    currentpage: rep.page,
                    operate: function (totalPage) {
                        $('.page-index2 span').each(function () {
                            $(this).click(function () {
                                homepageData.page = $(this).text();
                                homepage.getFriendReplyInfo(this,1);
                            })
                        });
                        $('.first-page2').click(function () {
                            homepageData.page = 1;
                            homepage.getFriendReplyInfo(this,1);
                        });
                        $('.last-page2').click(function () {
                            homepageData.page = totalPage;
                            homepage.getFriendReplyInfo(this,1);
                        })
                    }
                }
                homepage.initPaginator2(option);
            } else {
                homepage.setNotifyNum();
                //if (value==1) {
                    var m = $(dom).parents('.list-info').find('.hwk3');
                var bid = $(dom).parents('.list-info');
                    if (homepageData.page==1) {
                        m.html('');
                    }
                    Common.render({tmpl:$('#hwk3_templ'),data:rep,context:m});
                //} else {
                //    $('.hwk3').html('');
                //    Common.render({tmpl:$('#hwk3_templ'),data:rep,context:'.hwk3'});
                //}
                $(dom).parents('.list-info').find('.card-more').remove();
                if (rep.page * rep.pageSize < rep.total) {
                    m.append('<a class="card-more" ><span class="more-txt">后面还有' + (rep.total - rep.page * rep.pageSize) + '条评论，点击查看>></span></a>');
                }
                $(".card-more").click(function(event){
                    homepageData.blogid = bid.attr('mid'),
                        homepageData.type=2;
                        homepageData.page = rep.page+1;
                        homepageData.pageSize = rep.pageSize;
                    homepage.getFriendReplyInfo(this,1);
                });
                $(".hu_f").click(function(event) {
                    $(this).parents('.er_l').find('.moreply').toggle();
                });
                $('.del_reply').bind('click', function () {
                    if (confirm('确认删除此条评论！')) {
                        homepageData.id = $(this).attr('commentid');
                        homepageData.blogid = $(this).attr('blogid');
                        homepage.deleteMicroblog(2,this);

                    }
                });
            }
            homepage.zanchoose();
        });
        /*
         * 绑定zan按钮
         * */
        $('.zan_cls2').bind("click",function(event){
            homepageData.blogid=$(this).attr('id');
            homepage.isBlogZan(this);
        });
        $(".huifu1-1").click(function(event) {
            $(this).parents('.bomt').find('.huifu1').toggle();
        });
        $(".huifu1 textarea").focus(function(event) {
            $(this).css({
                "border": '1px solid #ffa33a'
            });
        });
        $(".huifu1 textarea").blur(function(event) {
            $(this).css({
                "border": '1px solid #d0d0d0'
            });
        });
        $(".reply2").click(function(event) {
            homepageData.id=$(this).attr('blogid');
            var cnt2 = $(this).parents('.moreply').find('.content-reply2').val();
            homepageData.blogcontent = cnt2;
            homepageData.userid = $(this).attr('uid');
            homepageData.replytype=3;
            homepageData.replyid=$(this).attr('commentid');
            if (cnt2.length == 0 || $.trim(cnt2) == '') {
                alert("评论不可为空！");
                return;
            }
            homepage.replyBlog(this);
        });
        $(".reply3").click(function(event) {
            homepageData.id=$(this).attr('blogid');
            var cnt3 = $(this).parents('.huifu1').find('.content-reply3').val();
            homepageData.blogcontent = cnt3;
            homepageData.userid = $(this).attr('uid');
            homepageData.replytype=3;
            homepageData.replyid=$(this).attr('commentid');
            if (cnt3.length == 0 || $.trim(cnt3) == '') {
                alert("评论不可为空！");
                return;
            }
            homepage.replyBlog(this);
        });
        $('.mycomment-delete').bind('click', function () {
            if (confirm('确认删除此条评论！')) {
                homepageData.id = $(this).attr('commentid');
                homepageData.blogid = $(this).attr('blogid');
                homepage.deleteMicroblog(1,this);
                homepage.getFriendReplyInfo(this,1);
            }
        });
        if($('.fancybox')!=undefined){
            $('.fancybox').fancybox();
        }

    }

    // 分页初始化
    homepage.initPaginator=function (option) {
        var totalPage = '';
        $('.page-paginator').show();
        $('.page-index').empty();
        if (option.total % option.pagesize == 0) {
            totalPage = option.total / option.pagesize;
        } else {
            totalPage = parseInt(option.total / option.pagesize) + 1;
        }
        homepage.buildPaginator(totalPage, option.currentpage);
        option.operate(totalPage);
    }

    homepage.buildPaginator =function (totalPage, currentPage) {
        if (totalPage > 5) {
            if (currentPage < 4) {
                for (var i = 1; i <= 5; i++) {
                    if (i == currentPage) {
                        $('.page-index').append('<span class="active">' + i + '</span>');
                    } else {
                        $('.page-index').append('<span>' + i + '</span>');
                    }
                }
                $('.page-index').append('<i>···</i>');
            } else if (currentPage >= 4 && currentPage < (totalPage - 2)) {
                $('.page-index').append('<i>···</i>');
                for (var i = currentPage - 2; i <= currentPage + 2; i++) {
                    if (i == currentPage) {
                        $('.page-index').append('<span class="active">' + i + '</span>');
                    } else {
                        $('.page-index').append('<span>' + i + '</span>');
                    }
                }
                $('.page-index').append('<i>···</i>');
            } else {
                $('.page-index').append('<i>···</i>');
                for (var i = totalPage - 4; i <= totalPage; i++) {
                    if (i == currentPage) {
                        $('.page-index').append('<span class="active">' + i + '</span>');
                    } else {
                        $('.page-index').append('<span>' + i + '</span>');
                    }
                }
            }
        } else {
            for (var i = 1; i <= totalPage; i++) {
                if (i == currentPage) {
                    $('.page-index').append('<span class="active">' + i + '</span>');
                } else {
                    $('.page-index').append('<span>' + i + '</span>');
                }
            }
        }
    }

    // 分页初始化
    homepage.initPaginator2=function (option) {
        var totalPage = '';
        $('.page-paginator2').show();
        $('.page-index2').empty();
        if (option.total % option.pagesize == 0) {
            totalPage = option.total / option.pagesize;
        } else {
            totalPage = parseInt(option.total / option.pagesize) + 1;
        }
        homepage.buildPaginator2(totalPage, option.currentpage);
        option.operate(totalPage);
    }

    homepage.buildPaginator2 =function (totalPage, currentPage) {
        if (totalPage > 5) {
            if (currentPage < 4) {
                for (var i = 1; i <= 5; i++) {
                    if (i == currentPage) {
                        $('.page-index2').append('<span class="active">' + i + '</span>');
                    } else {
                        $('.page-index2').append('<span>' + i + '</span>');
                    }
                }
                $('.page-index2').append('<i>···</i>');
            } else if (currentPage >= 4 && currentPage < (totalPage - 2)) {
                $('.page-index2').append('<i>···</i>');
                for (var i = currentPage - 2; i <= currentPage + 2; i++) {
                    if (i == currentPage) {
                        $('.page-index2').append('<span class="active">' + i + '</span>');
                    } else {
                        $('.page-index2').append('<span>' + i + '</span>');
                    }
                }
                $('.page-index2').append('<i>···</i>');
            } else {
                $('.page-index2').append('<i>···</i>');
                for (var i = totalPage - 4; i <= totalPage; i++) {
                    if (i == currentPage) {
                        $('.page-index2').append('<span class="active">' + i + '</span>');
                    } else {
                        $('.page-index2').append('<span>' + i + '</span>');
                    }
                }
            }
        } else {
            for (var i = 1; i <= totalPage; i++) {
                if (i == currentPage) {
                    $('.page-index2').append('<span class="active">' + i + '</span>');
                } else {
                    $('.page-index2').append('<span>' + i + '</span>');
                }
            }
        }
    }

    /*
     * 发布一条校园安全信息
     * */
    homepage.publishSchoolMicoSecurity = function() {
        $('.orange-btn').click(function(event){
            var comment_content = $.trim($("#micoblog_content").val());
            var comment_placehd = $("#micoblog_content").attr('placeholder');
            if(comment_content == '' && comment_placehd != '' && comment_placehd != '来说一句'){
                comment_content = comment_placehd;
                $("#micoblog_content").val(comment_placehd);
            }
            var content = $("#micoblog_content").val().replace(/\n/g, '<br>');
            //if ($('jinyan').val()==1) {
            //    alert("对不起，你被禁言一周。");
            //    return;
            //}
            if(comment_content == ''){
                alert("请输入评论内容。");
                return;
            }
            if (homepage.getLength(content) > 1000) {
                alert('文字最多发1000个字符！');
                return;
            }

            var content = $("#micoblog_content").val().replace(/\n/g, '<br>');

            homepageData.blogcontent=content;

            $('.ixh').hide();
            //var arrayObj = [];
            var flist = '';
            var vids = '';
            var i = 0;
            if ($(".micobolg-img").length <= 9) {
                $(".micobolg-img").each(function() {
                    var srcPath=$(this).attr('src');
                    var type = $(this).attr('tp');
                    var vurl = $(this).attr('vurl');
                    if (type==1) {
                        flist += srcPath.substring(0,srcPath.indexOf("?")) + ',';
                    } else {
                        vids += $(this).attr('vid')+"@"+vurl+"@"+ srcPath.substring(0,srcPath.indexOf("?")) + ',';
                    }
                    i++;
                    $(this).parent().remove();
                });
            } else {
                alert('上传图片视频不可超过九张！');
                $('.ixh').show();
                return;
            }
            var arr = document.getElementById("theme");
            var themetype;
            if (arr!=null && arr.checked) {
                themetype = 1;
            } else {
                themetype = 0;
            }
            homepageData.filenameAry=flist;
            homepageData.videoAry=vids;
            homepageData.top = themetype;
            //leixing    改
            homepageData.blogtype = blogType;
            homepageData.sendtype = $('#sendtype').val();
            $('#picuploadLoading').show();
            Common.getData('/homeschool/publicBlog.do', homepageData,function(rep){
                if (rep.flag) {
                    alert("对不起，你被禁言一周。");
                    $("#micoblog_content").val("");
                    $('#micoblog_content').attr('placeholder','来说一句');
                    $('#picuploadLoading').hide();
                } else {
                    if (rep.score) {
                        scoreManager(rep.scoreMsg, rep.score);
                    }
                    $("#micoblog_content").val("");
                    $('#micoblog_content').attr('placeholder','来说一句');
                    $('#picuploadLoading').hide();
                    homepage.initHomePageData(1);
                }
            });
        });
    }

    /*
     * 上传校园安全附件信息
     * */
    homepage.homepageFileUpload = function(id) {
        /*
         * 点击附件按钮
         * */
        $('#image-upload').click(function(event){
            Common.fileUpload('#image-upload','/homeschool/addBlogPic.do','#picuploadLoading',function(e,response){
                var result = response.result;
                var rdata = typeof  result == 'string' ? $.parseJSON(result) : result[0] ? $.parseJSON(result[0].documentElement.innerText) : result;
                if (rdata.result) {
                    var url = rdata.path[0];
                    $('#img-container ul').append('<li class="ix"><a class="fancybox" href="' + url + '"data-fancybox-group="home" title="预览"><img class="micobolg-img" tp="1" src="' + url +'?imageView/1/h/60/w/60'+ '"></a> <i class="ixh"></i></li>');
                    $('#micoblog_content').attr('placeholder', '分享图片');
                    $('.fancybox').fancybox();
                }
                /*关闭图片*/
                $(".ixh").click(function(event) {
                    $(this).parent().remove();
                });
            });
        });
    }

    homepage.replyBlog = function(dom) {
        Common.getPostData('/homeschool/replyComment.do', homepageData,function(rep){
            var cmment = $(dom).parents('.list-info').find('.cmt');
            cmment.html(rep.replyCount);
            $(dom).parents('.list-info').find('.content-reply').val('');
            $(dom).parents('.moreply').find('.content-reply2').val('');
            homepage.getFriendReplyInfo(dom,1);
            if (rep.score) {
                scoreManager(rep.scoreMsg, rep.score);
            }
        });
    }

    /*
     * 删除一条微博信息
     * */
    homepage.deleteMicroblog = function(type,dom) {
            //删除一条微博信息
            Common.getData('/homeschool/delteMicroBlog.do', homepageData,function(){
                    alert("删除评论成功！");
                    var cmment = $(dom).parents('.list-info').find('.cmt');
                    var number = cmment.html();
                    cmment.html(number-1);
                    if (type==2) {
                        homepage.getFriendReplyInfo(dom,2);
                    }
                }
            );
    }

    /**
     * 赞
     */
    homepage.isBlogZan = function(dom) {
        Common.getData('/homeschool/isBlogZan.do', homepageData,function(rep){
                if (rep.flag) {
                    $(dom).attr({
                        "src": '/static_new/images/zan_2.jpg'
                    });
                    zantp = true;
                    $(dom).parent().find('em').text(rep.zanCount);
                    if (rep.score) {
                        scoreManager(rep.scoreMsg, rep.score);
                    }
                }
            }
        );
    }

    /**
     * 获取话题
     */
    homepage.getThemes = function() {
        Common.getData('/homeschool/getThemes.do', homepageData,function(rep){
                $('.themeUl').html('');
                Common.render({tmpl:$('#themeUl_templ'),data:rep,context:'.themeUl'});
                $('.themeUl2').html('');
                Common.render({tmpl:$('#themeUl_templ2'),data:rep,context:'.themeUl2'});
            }
        );
    }
    /*textarea*/
    $(function(){
        $(".coottop .tab-col .tab-main .phd").focus(function(event) {
            $(this).css({
                "height": '118px'
            });
        });
        $(".coottop .tab-col .tab-main .phd").blur(function(event) {
            if(this.value==''){
                $(this).css({
                    "height": '38px'
                });
            }
            else{
                $(this).css({
                    "height": '118px'
                });
            }

        });
    });

    homepage.uploadVideo = function() {
        $('#file_upload').uploadify({
            'swf': "/static/plugins/uploadify/uploadify.swf",
            'uploader': '/homeschool/video/upload.do',
            'method': 'post',
            'buttonText': '选择文件',
            'fileTypeDesc': '视频文件',
            'fileSizeLimit': '300MB',
            'fileTypeExts': '*.avi; *.mp4; *.mpg; *.flv; *.wmv; *.mov; *.mkv',
            'multi': true,
            'fileObjName': 'Filedata',
            'onUploadSuccess': function (file, response, result) {
                try {
                    var json = $.parseJSON(response);
                    if (json.flg) {
                        var imgUrl = json.vimage;
                        if (imgUrl=="") {
                            imgUrl = '/img/K6KT/video-cover.png';
                        }
                        $('#img-container ul').append('<li class="ix"><img class="micobolg-img videoshow" onclick="tryPlayYCourse(\'' + json.vurl + '\')" tp="2" style="cursor: pointer;" vid='+json.vid +' vurl='+json.vurl+' src="' + imgUrl +'?imageView/1/h/60/w/60'+ '"><i class="ixh"></i></li>');
                        $('#micoblog_content').attr('placeholder', '分享视频');
                    } else {
                        //MessageBox(json.uploadType, -1);
                        someFileFailed = true;
                    }
                    //$(".videoshow").click(function(event) {
                    //    $(".player-close-btn").css({
                    //        "display": 'block'
                    //    });
                    //    homepage.tryPlayYCourse($(this));
                    //});
                    /*关闭图片*/
                    $(".ixh").click(function(event) {
                        $(this).parent().remove();
                    });
                } catch (err) {
                }
            },
            'onQueueComplete': uploadComplete,
            'onUploadError': function (file, errorCode, errorMsg, errorString) {
                //MessageBox("服务器响应错误。", -1);
                someFileFailed = true;
            }
        });
    }
    function uploadComplete(queueData) {
        if (someFileFailed) {
            MessageBox("部分视频上传失败，请重试。", -1)
            someFileFailed = false;
        }
    }

    homepage.closeCloudView = function() {
        $('#YCourse_player').fadeOut('fast', function() {
            $('#YCourse_player').removeClass('flash-container');
            $("#player_div").removeClass('flash-player-div');
            $("#player_div").empty();
        });
        $('.dialog-bg').fadeOut('fast');
        $('.bg').fadeOut('fast');
        try {
            SewisePlayer.doStop();
        } catch (e) {
        }
    }

    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;
    }
    homepage.getLength = function(content) {
        var realLength = 0, len = content.length, charCode = -1;
        for (var i = 0; i < len; i++) {
            charCode = content.charCodeAt(i);
            if (charCode >= 0 && charCode <= 128) realLength += 1;
            else realLength += 2;
        }
        return realLength;
    };
    homepage.setNotifyNum = function () {
        Common.getData('/homeschool/getNoticeCount.do',homepageData,function(rep){
            if(rep.blogType==1) {
                $('.tab-deit').html(rep.blogCount);
                $('.tab-deit').show();
            } else {
                $('.tab-deit').hide();
            }
        });

    }
    homepage.init();
});