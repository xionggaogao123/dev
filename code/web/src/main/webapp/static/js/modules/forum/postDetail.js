/**
 * Created by admin on 2016/6/3.
 */
define(['jquery', 'pagination', 'social', 'common'], function (require, exports, module) {
    var Common = require('common');
    require('pagination');
    var postDetail = {};
    var login = $('body').attr('login') == 'true';
    var page = 1;
    var pageV = $('#pageN').val();
    var floor = $('#floor').val();
    var sortType = 1;
    var flag = 0;
    var reply;
    var userId; //登录用户Id
    var pSectionId; //板块Id
    var userPermission; //用户权限
    var personId; //发帖信息用户Id
    var pageCount; //传递的参数
    var postFlagId;
    var postId //帖子Id
    var recall = 0;//主贴标志
    var rr = 0;//回复贴标志
    var timeText = $('#timeText').val();

    var inSet;//大赛标志（1）
    if (pageV) {
        page = pageV;
    }
    var sort = $('#sortType').val();
    var cate = $('#cate').val();
    if (sort) {
        sortType = sort;
    }

    //分享地址
    var qzone = 'http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url={url}&title={title}&pics={pic}&summary={content}';
    var sina = 'http://service.weibo.com/share/share.php?url={url}&title={title}&pic={pic}&searchPic=false';
    var tqq = 'http://share.v.t.qq.com/index.php?c=share&a=index&url={url}&title={title}&appkey=801cf76d3cfc44ada52ec13114e84a96';
    var douban = 'http://www.douban.com/share/service?href={url}&name={title}&text={content}&image={pic}';
    var weixin = 'http://qr.liantu.com/api.php?text={url}';
    var qqfriend = 'http://connect.qq.com/widget/shareqq/index.html?url={url}&title={title}&desc=&summary={content}&site=baidu&pics={pic}';
    var weiFriend = 'http://s.jiathis.com/?webid=weixin&uid=0&jtss=0&appkey=&ralateuid=&url={url}&title={title}&pic={pic}&acn=&acu=&summary={content}&isexit=false'

    var shareSettings = {
        url: encodeURIComponent(window.location.href),
        title: document.title,
        content: '',
        pic: ''
    };
    postDetail.init = function () {
        pSectionId = $('#pSectionId').val();
        postId = $('#postId').val();
        personId = $('#personId').val();
        inSet = $('#InSet').val();
        $("#edui1_bottombar").hide();
        $("#pageS").attr("value", page);
        getData(pSectionId); //板块信息
        getPostData(postId); //发帖人的详细信息

        getReplyData(page, postId, pSectionId, "");//发帖的贴子对应的回帖列表信息

        getBtnZan();

        getParticipates(0);

        if (timeText) {
            window.location.href = "#" + timeText;
        } else {
            $(document).scrollTop(floor);
        }

    };
    $(document).ready(function () {


        $('#toplogin').click(function () {
            $('.notice-dl').text('中国最专业的青少年素质教育社区!').css({
                'color': '#FF4918',
                'font-weight': 'bold',
                'font-size': '24px'
            });
        });

        $('#ddd').click(function () {//登录
            $('.store-register').fadeToggle();
            $('.bg').fadeToggle();
        });


        $('body').on('click', '.deleteLOL', function () {
            var that = this;
            var replyId = $(this).attr("replyId");
            var rpid = $(this).attr("rpid");
            if (confirm("你确定要删除该评论？")) {
                $.ajax({
                    url: "/admin/deletelol",
                    data: {
                        lolId: replyId,
                        rpid: rpid
                    },
                    success: function (resp) {
                        window.location.reload();
                    }
                });
            }
        });

        $('#downloadLike').click(function () {
            window.location = "/reply/likeinfo?postId=" + postId;
        });

        $('#downloadLikeZip').click(function () {
            window.location = "/reply/likeinfoZip?postId=" + postId;
        });

        //检查对象，#boxs是要随滚动条固定的ID
        var offset = $('#sel-type').offset();
        $(window).scroll(function () {
            //检查对象的顶部是否在游览器可见的范围内
            var scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
            if (offset.top < scrollTop) {
                $('#sel-type').addClass('fixedd');
            }
            else {
                $('#sel-type').removeClass('fixedd');
            }
        });

        function share(ti) {
            var ul = location.href;
            var sendUrl = getSendUrl(ti, ul);
            var option;
            if (sendUrl) {
                option = {
                    content: '分享帖子',
                    url: encodeURIComponent(sendUrl),
                    titile: '分享帖子'
                };
            } else {
                option = {};
            }
            return option;
        }

        //分享
        $('body').on("click", ".tQQ", function () {
            var option = share($(this).attr("ti"));
            tQQ(this, option);
        });

        $(".spa1").hide();
        $(".btn-dss").on("click", function () {
            var formScore = parseInt($("#formScore").html());
            var inputScore = parseInt($("#inputScore").val());
            if (inputScore > formScore) {
                $(".spa1").show();
                return;
            }
            var paramData = {};
            paramData.postId = postId;
            paramData.score = inputScore;
            Common.getPostData('/forum/rewordPost.do', paramData, function (resp) {
                if (resp.code == "200") {
                    $('#windBox').fadeOut();
                    $('.bg').fadeOut();
                } else {
                    alert(resp.message);
                }
            });
        });

        $("#inputScore").on('input', function (e) {
            $(".spa1").hide();
        });

        $('body').on("click", ".qZone", function () {
            var option = share($(this).attr("ti"));
            qZone(this, option);
        });

        $('body').on("click", ".sina", function () {
            var option = share($(this).attr("ti"));
            sinaWeibo(this, option);
        });

        $('body').on("click", ".douban", function () {
            var option = share($(this).attr("ti"));
            doubanShare(this, option);
        });

        $('body').on("click", ".weixin", function () {
            var option = share($(this).attr("ti"));
            weixinShare(this, option);
        });

        $('body').on("click", ".friendQQ", function () {
            var option = share($(this).attr("ti"));
            qqShare(this, option);
        });

        $('body').on('click', '.wind-besta .p1 em', function () {
            $('.wind-besta').fadeOut();
        });

        $('body').on("click", ".weixinFriend", function () {
            var option = share($(this).attr("ti"));
            weiFriendShare(this, option);
        });
        $('body').on('click', '.join-cont .sp3 .p1', function () {
            getParticipates(1);
        });
        $('body').on('click', '.join-cont .sp3 .p2 em', function () {
            $('.join-cont .sp3 .p2').slideUp();
            $('.join-cont .sp3 .p1').removeClass('bord');
        });

        //分享按钮
        $('body').on("click", ".msb_main", function () {
            if ($(this).hasClass("disabled")) return;
            var e = 500;//动画时间
            var t = 250;//延迟时间
            var r = $(this).parent().find(".msb_network_button").length;  //分享组件的个数
            var i = 50;
            var s = e + (r - 1) * t;
            var o = 1;
            var a = $(this).outerWidth();
            var f = $(this).outerHeight();
            var c = $(this).parent().find(".msb_network_button:eq(0)").outerWidth();
            var h = $(this).parent().find(".msb_network_button:eq(0)").outerHeight();
            var p = (a - c) / 2; //起始位置
            var d = (f - h) / 2; //起始位置
            var v = 0 / 180 * Math.PI;
            if (!$(this).hasClass("active")) {
                $(this).addClass("disabled").delay(s).queue(function (e) {
                    $(this).removeClass("disabled").addClass("active");
                    e()
                });
                $(this).parent().find(".msb_network_button").each(function () {
                    var n = p + (p + i * o) * Math.cos(v);  //结束位置
                    var r = d + (d + i * o) * Math.sin(v);  //结束位置
                    $(this).css({
                        display: "block",
                        left: p + "px",
                        top: d + "px"
                    }).stop().delay(t * o).animate({
                        left: n + "px",
                        top: r + "px"
                    }, e);
                    o++
                })
            } else {
                o = r;
                $(this).addClass("disabled").delay(s).queue(function (e) {
                    $(this).removeClass("disabled").removeClass("active");
                    e()
                });
                $(this).parent().find(".msb_network_button").each(function () {
                    $(this).stop().delay(t * o).animate({
                        left: p,
                        top: d
                    }, e);
                    o--
                })
            }
        });

        $('body').on('click', '#skl', function () {
            sortType = 2;
            getReplyData(1, postId, pSectionId, "");

        });

        $('body').on('click', '#sll', function () {
            sortType = 11;
            getReplyData(1, postId, pSectionId, "");
        });

        $('#expressTheme').click(function () {
            goToPost(pSectionId);
        });
        $('.post-jb').click(function () {
            $.ajax({
                url: "/forum/loginInfo.do?date=" + new Date(),
                type: "get",
                dataType: "json",
                data: {},
                success: function (resp) {
                    if (resp.login) {
                        $('.wind-jb').fadeIn();
                        $('.bg').fadeIn();
                    } else {
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            });
        });
        $('.wind-jb .p1 em,.wind-jb .btn-jb-x').click(function () {
            $('.wind-jb').fadeOut();
            $('.bg').fadeOut();
            $('.wind-jb textarea').slideUp();
        });
        $('.btn-qxs, .wind-ds>p em').click(function () {
            $('.wind-ds').fadeOut();
            $('.bg').fadeOut();
        });
        $('.wind-jb .i2').click(function () {
            $('.wind-jb textarea').slideDown();
        });
        $('.wind-jb .i1').click(function () {
            $('.wind-jb textarea').slideUp();
        });
        $('.btn-ds').click(function () {
            $.ajax({
                url: "/forum/loginInfo.do?date=" + new Date(),
                type: "get",
                dataType: "json",
                data: {},
                success: function (resp) {
                    if (resp.login) {
                        $('#windBox').fadeIn();
                        $('.bg').fadeIn();
                    } else {
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            });
        });

        $('body').on('click', '#emTop', function () {
            $('.wind-recall').fadeOut();
        });

        $('body').on('click', '.btn-jb-o', function () {
            $.ajax({
                url: "/forum/loginInfo.do?date=" + new Date(),
                type: "get",
                dataType: "json",
                data: {},
                success: function (resp) {
                    var flag = resp.login;
                    if (flag) {
                        var param = {};
                        if ($('#ggg').val() == "") {
                            var reason = $('input[name="reason-jb"]:checked').attr('value');
                            if (reason == undefined) {
                                alert("请选择一个");
                                return;
                            }
                        } else {
                            reason = $('#ggg').val();
                        }

                        param.reason = reason;
                        param.postId = postId;
                        Common.getPostData('/forum/addReported.do', param, function (resp) {
                            if (resp.code == "200") {
                                alert("举报成功！");
                                location.href = '/forum/postDetail.do?pSectionId=' + pSectionId + '&postId=' + postId + '&personId=' + personId;
                            } else {
                                alert(resp.message);
                            }
                        });

                    } else {
                    }
                }
            });
        });

        $('.delzt').click(function () {
            $('.post-wind .qu-p1').text('您确定要删除选择的主题吗？');
            $('.post-wind .qu-p2').text('（如删除该主题，评论也将删除）');
            $('.post-wind').fadeIn();
            $('.bg').fadeIn();
            flag = 1;
        });

        $('#topSet').click(function () {
            $('.post-wind .qu-p1').text('您确认要将选择的主题置顶吗？');
            $('.post-wind .qu-p2').text('');
            $('.post-wind').fadeIn();
            $('.bg').fadeIn();
            flag = 3;
        });
        $('#creamSet').click(function () {
            $('.post-wind .qu-p1').text('您确认要将选择的主题设为精华吗？');
            $('.post-wind .qu-p2').text('');
            $('.post-wind').fadeIn();
            $('.bg').fadeIn();
            flag = 4;
        });
        $('#topNoSet').click(function () {
            $('.post-wind .qu-p1').text('您确认要将选择的主题取消置顶吗？');
            $('.post-wind .qu-p2').text('');
            $('.post-wind').fadeIn();
            $('.bg').fadeIn();
            flag = 5;
        });
        $('#creamNoSet').click(function () {
            $('.post-wind .qu-p1').text('您确认要将选择的主题取消精华吗？');
            $('.post-wind .qu-p2').text('');
            $('.post-wind').fadeIn();
            $('.bg').fadeIn();
            flag = 6;
        });
        $('.post-wind h2 em, .post-wind #btnCancel').click(function () {
            $('.post-wind').fadeOut();
            $('.bg').fadeOut();
        });

        $('body').on('click', '#through', function () {
            var t = $('#floorll').val();
            if (t == "") {
                alert("请填写楼层");
                return;
            }
            if (isNaN(t)) {
                alert("只能填写数字");
                return;
            }

            $.ajax({
                url: "/forum/floorTimeText.do",
                type: "post",
                data: {
                    floor: t,
                    postId: $('#postId').val()
                },
                success: function (data) {
                    if (data.message == 0) {
                        alert("楼层不存在");
                        return;
                    }
                    var offset = 0;
                    if (inSet == 1 || (inSet == -1 && cate == 1)) {
                        offset = 80;
                    }
                    if ($("#" + data.message).length > 0) {
                        $("html,body").animate({scrollTop: $("#" + data.message).offset().top - offset}, 1000);
                    } else {
                        if (inSet == 1 || (inSet == -1 && cate == 1)) {
                            location.href = '/forum/floorPosition.do?floor=' + t + '&postId=' + $('#postId').val() + '&pageSize=15';
                        } else {
                            location.href = '/forum/floorPosition.do?floor=' + t + '&postId=' + $('#postId').val() + '&pageSize=8';
                        }
                    }
                },
                error: function (data) {

                }
            });
        });


        $('#coll').click(function () {
            $.ajax({
                url: "/forum/loginInfo.do?date=" + new Date(),
                type: "get",
                dataType: "json",
                data: {},
                success: function (resp) {
                    var flag = resp.login;
                    if (flag) {
                        var collection = {};
                        collection.postSectionId = $('#postId').val();
                        collection.type = 0;
                        Common.getPostData('/forum/userCenter/addCollection.do', collection, function (resp) {
                            if (resp.code == 200) {
                                $('#coll').html('收藏(' + resp.message + ')');
                            } else {
                                alert(resp.message);
                            }
                        });
                    } else {
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            });
        });

        $('body').on('click', '.zanReply', function () {
            var replyId = $(this).attr('value');
            var that = this;
            $.ajax({
                url: "/forum/loginInfo.do?date=" + new Date(),
                type: "get",
                dataType: "json",
                data: {},
                success: function (resp) {
                    var flag = resp.login;
                    if (flag) {
                        var params = {};
                        params.replyId = replyId;
                        params.userReply = userId;
                        Common.getPostData('/forum/updateReplyBtnZan.do', params, function (resp) {
                            if (resp.code == 200) {
                                $(that).children('span').html('(' + resp.message + ')');
                            } else {
                                alert(resp.message);
                            }
                        });
                    } else {
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            });
        });
        $('#zan').click(function () {
            if (inSet == 1 || (inSet == -1 && cate == 1)) {
                ll();
                return;
            }
            var fll = $(this).attr('zan');
            $.ajax({
                url: "/forum/loginInfo.do?date=" + new Date(),
                type: "get",
                dataType: "json",
                data: {},
                success: function (resp) {
                    var flag = resp.login;
                    if (flag) {
                        if (fll == "true") {
                            $("#zan").removeClass('btn-zan').addClass('zan-btn');
                            $("#zan").attr('zan', false);
                        } else {
                            $("#zan").removeClass('zan-btn').addClass('btn-zan');
                            $("#zan").attr('zan', true);
                        }
                        var params = {};
                        params.post = postId;
                        params.flag = fll;
                        params.userReply = userId;
                        Common.getPostData('/forum/updateBtnZan.do', params, function (resp) {
                            if (resp.code == 200) {
                                $('#dds').html(resp.message);
                            } else {
                                alert(resp.message);
                            }
                        });
                    } else {
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            });
        });

        $('#editPost').click(function () {
            window.open('/forum/newPost.do?postId=' + postId);
        });

        $('body').on('click', '.btn-sub', function () {
            $.ajax({
                url: "/forum/loginInfo.do?date=" + new Date(),
                type: "get",
                dataType: "json",
                data: {},
                success: function (resp) {
                    var flag = resp.login;
                    if (flag) {
                        var param = {};
                        var number = "";
                        //var number=$('input[name="radio-s"]:checked').attr('value');
                        $(".checkVote").each(function () {
                            if ($(this).is(':checked')) {
                                var item = $(this).attr('value');
                                if (number != "") {
                                    number = number + "," + item;
                                } else {
                                    number = item;
                                }
                            }
                        })
                        if (number == "") {
                            alert("请选择一个");
                            return;
                        }
                        param.number = number;
                        param.voteId = postId;
                        Common.getPostData('/forum/userCenter/addFVote.do', param, function (resp) {
                            if (resp.code == "200") {
                                location.href = '/forum/postDetail.do?pSectionId=' + pSectionId + '&postId=' + postId + '&personId=' + personId;
                            } else {
                                alert(resp.message);
                            }
                        });

                    } else {
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            });
        });

        $('body').on('click', '.btn-fb', function () {
            var replyPostId = $(this).attr('value');
            var replyContent = $(this).parent().prev().val();
            var str = "";
            if (replyContent.lastIndexOf(':') > -1) {
                str = replyContent.substr(0, replyContent.indexOf(":"));
                replyContent = replyContent.substr(replyContent.indexOf(":") + 1);
            }
            if (replyPostId == postId) {
                postFlagId = 1;
            } else {
                postFlagId = 0;
            }
            goToPostReply(pSectionId, replyPostId, postFlagId, replyContent, str);
        });

        $('body').on('click', '.ssfd', function () {
            var value = $(this).attr('value');
            if (value.lastIndexOf('回') > -1) {
                value = value.substr(0, value.indexOf('回'));
            }
            var str = "回复" + "@" + value + ":";
            $(this).parent().parent().parent().next('.s1').find("textarea:first-child").val(str);
            $(this).parent().parent().parent().next('.s1').slideToggle();
        });

        $('body').on('click', '.selfWant', function () {
            $(this).parent().parent().next('.s1').slideToggle();
        });

        $('body').on('click', '.em1', function () {
            $(this).hide();
            $(this).next('.em2').show();
            $(this).parent().next('.recall-s').slideDown();
        });
        $('body').on('click', '.em2', function () {
            $(this).hide();
            $(this).prev('.em1').show();
            $(this).parent().next('.recall-s').slideUp();
        });
        $('body').on('click', '.em3', function () {
            $(this).hide();
            $(this).next('.em4').show();
            if (rr == 1) {
                $(this).parent().next().next('.recall-sf').slideDown();
            }
            $(this).parent().next('.recall-sf').slideUp();

        });

        $('body').on('click', '.em5', function () {
            $(this).hide();
            $(this).next('.em6').show();
            if (recall == 1) {
                $(this).parent().next().next('.recall-sf').slideDown();
            }
            $(this).parent().next('.recall-sf').slideUp();

        });

        $('body').on('click', '.em6', function () {
            $(this).hide();
            $(this).prev('.em5').show();
            if (recall == 1) {
                $(this).parent().next().next('.recall-sf').slideUp();
            }
            $(this).parent().next('.recall-sf').slideDown();

        });

        $('body').on('click', '.em4', function () {
            $(this).hide();
            $(this).prev('.em3').show();
            if (rr == 1) {
                $(this).parent().next().next('.recall-sf').slideUp();
            }
            $(this).parent().next('.recall-sf').slideDown();


        });

        $('body').on('click', '.search', function () {
            $(this).parent().parent().children('.llp').show();
            $(this).hide();
            $(this).prev().hide();
            $(this).next().show();
        });

        $('body').on('click', '.soQi', function () {
            $(this).parent().parent().children('.llp').hide();
            $(this).hide();
            $(this).prev().show();
            $(this).prev().prev().show();
        });

        $('body').on('click', '.xs', function () {
            var ppName = $(this).attr('personName');
            var ppId = $(this).attr('value');
            $('#aa').attr('personId', ppId);
            $.ajax({
                url: "/forum/loginInfo.do?date=" + new Date(),
                type: "get",
                dataType: "json",
                async: false,
                data: {},
                success: function (resp) {
                    var flag = resp.login;
                    if (flag) {
                        $('.sendInf').show();
                        $('.bg').show();
                        $('#aa').html(ppName);
                    } else {
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            });

        });

        $('.sendInf .p1 em').click(function () {
            $('.sendInf').hide();
            $('.bg').hide();
        });


        $('#confirmSend').click(function () {
            var contentValue = $('#content').val();
            if (contentValue == "") {
                alert("请输入消息内容");
                return;
            }
            var param = {};
            param.content = contentValue;
            param.personId = $('#aa').attr('personId');
            Common.getPostData('/forum/userCenter/sendInf.do', param, function (resp) {
                if (resp.code == "200") {
                    $('.sendInf').hide();
                    $('#content').val("");
                    $('.bg').hide();
                    alert("消息已发送出去");
                } else {
                    $('.sendInf').hide();
                    $('.bg').hide();
                    alert(resp.message);
                }
            })
        });

        $('.wind-besta p em,#llj').click(function () {
            $('.wind-besta').hide();
            $('.bg').hide();
        });

        $('body').on('click', '#llk', function () {
            var sol = $('#sol').val();
            var requestData = {};
            requestData.postId = $('#postId').val();
            requestData.sol = sol;
            Common.getData('/forum/sol.do', requestData, function (resp) {
                if (resp.code == 200) {
                    location.href = location.href = '/forum/postDetail.do?pSectionId=' + pSectionId + '&postId=' + $('#postId').val() + '&personId=' + personId + "&ti=" + new Date().getTime();
                } else {
                    alert(resp.message);
                }
            });
        });

        $('body').on('click', '.join-cont .sp3 .p2 .i1', function (e) {
            var name = $(this).attr('participateName');
            if (confirm("确定删除" + name + "这个参赛者吗?")) {
                var id = $(this).attr('participateId');
                if (id == $('#comment').data('participateId')) {
                    $('#comment').data('remove', 0);
                    $('#comment').removeData('participateId');
                }
                $.ajax({
                    type: "GET",
                    data: {participateId: id},
                    url: "/forum/removeParticipate.do",
                    async: false,
                    dataType: "json",
                    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                    success: function (rep) {
                        if (rep.code == "200") {
                            getParticipates(0);
                            alert("删除成功!");
                        } else {
                            alert(rep.message);
                        }
                    }
                });
            }
        });


        $('body').on('click', '.wind-join .btn1', function () {
            var $name = $('#participateName');
            var $age = $('#participateAge');
            var $relation = $('#participateRelation');
            var $sex = $("[name='sex']:checked");
            var $school = $('#participateSchool');

            if ($name.val() == "") {
                $name.css("border", "1px solid #f00");
                $name.next('.nameClass').html('姓名不能为空');
                $name.next('.nameClass').show();
                return;
            } else {
                if ($name.val().length > 10) {
                    $name.css("border", "1px solid #f00");
                    $name.next('.nameClass').html('姓名长度不要超过10个字');
                    $name.next('.nameClass').show();
                }
            }

            if ($age.val() != "") {
                if (isNaN($age.val())) {
                    $('.ageClass').html("填写的年龄必须是数字");
                    $('.ageClass').show();
                    return;
                } else {
                    if (parseInt($age.val()) <= 0) {
                        $('.ageClass').html("填写的年龄不能小于或等于0");
                        $('.ageClass').show();
                        return;
                    }
                }
            }
            if ($relation.val() == "") {
//                $name.css("border","1px solid #E6E6E6");
                $relation.css("border", "1px solid #f00");
                $relation.next('.relationRegular').html("联系方式不能为空");
                $relation.next('.relationRegular').show();
                return;
            }
            var phonePattern = /(^(([0+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$)|(^0{0,1}1[3|4|5|6|7|8|9][0-9]{9}$)/;
            var emailPattern = /^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
            if (phonePattern.test($relation.val()) || emailPattern.test($relation.val())) {
            } else {
                $relation.css("border", "1px solid #f00");
                $relation.next('.relationRegular').html("请填写符合规范的邮箱或者手机号");
                $relation.next('.relationRegular').show();
                return;
            }
            var sex = -1;
            if (undefined != sex) {
                sex = $sex.val();
            }
            var joinId = $('.wind-join').data("joinId");
            var param = {
                id: joinId,
                name: $name.val(),
                relation: $relation.val(),
                sex: sex,
                age: $age.val(),
                school: $school.val()
            };
            $.ajax({
                type: "GET",
                data: param,
                url: "/forum/saveParticipator.do",
                async: false,
                dataType: "json",
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                success: function (rep) {
                    if (rep.code == "200") {
                        $('.wind-join').removeData("joinId");
                        initJoin();
                        $('.wind-join').fadeOut();
                        $('.bg').fadeOut();
                        getParticipates(0);
                        if (joinId == "") {
                            alert("保存成功!");
                        } else {
                            alert("更新成功!");
                        }

                    } else {
                        alert(rep.message);
                    }
                }
            });
        });
        $('body').on('click', '.ppl', function () {
            $('.wind-besta').show();
            $('.bg').show();
            $('#sol').val($(this).attr('sol'));
        })

        var drt = $('#drt').val();
        if (drt == "2") {
            var sol = $('kkl').val();
            if (sol != "1") {
                $('.btn-best').show();
            }
        }

    })


    function getParticipates(type) {
        $('#participateList').data("count", 0);
        if (type == 0) {
            $('.join-cont .sp3 .p2').slideUp();
        }
        Common.getData('/forum/getParticipates.do', {}, function (resp) {
            if (resp.code == "200") {
                if (resp.message.length > 0) {
                    $('#participateList').data("count", 1);
                    if ($('#comment').data('remove') == 0) {
                        $('.join-cont .p1').html('未选择');
                    } else if ($('.join-cont .p1').html() == "") {
                        $('.join-cont .p1').html('未选择');
                    }

                    if(resp.message.length ==1){
                        $('.join-cont .p1').html(''+resp.message[0].name);
                        $('#comment').data('participateId', resp.message[0].id);
                    }
                    Common.render({
                        tmpl: '#participateListTmpl',
                        data: resp.message,
                        context: '#participateList',
                        overwrite: 1
                    });
                    for (var i in resp.message) {
                        $('#participateList').data(resp.message[i].id, resp.message[i]);
                    }
                    if (type == 1) {
                        $('.join-cont .sp3 .p2').slideToggle();
                    }

                    // $('.join-cont .sp3 .p1').removeClass('bord');
                } else {
                    $('.join-cont .sp3 .p1').removeClass('bord');
                    $('.join-cont .sp4').hide();
                    $('.join-cont .p1').html('');
                    $('.join-cont .sp3 .p2').slideUp();
                    $('#participateList').empty();
                    $('.wind-join .btn1').html('保存并发表');
                    $('.wind-join .btn-cancel').html('跳过填写,并发表');
                }
            }
        });
    }

    function ll() {
        var h = $(document).height() - $(window).height();
        $(document).scrollTop(h);
    }

    function validateReply(content) {
        if (content == "") {
            alert("请输入内容");
            return false;
        }
        return true;
    }

    function goToPostReply(pSectionId, postId, postFlagId, content, str) {
        $.ajax({
            url: "/forum/loginInfo.do?date=" + new Date(),
            type: "get",
            dataType: "json",
            async: false,
            data: {},
            success: function (resp) {
                var flag = resp.login;
                if (flag) {
                    var nickName = resp.nickName + str;
                    var flag = validateReply(content);
                    if (flag) {
                        var requestData = {};
                        requestData.comment = "";
                        requestData.postSectionId = pSectionId;
                        requestData.postId = postId;
                        requestData.plainText = "";
                        requestData.personId = personId;
                        requestData.nickName = nickName;
                        requestData.content = content;
                        requestData.imageStr = "";
                        requestData.videoStr = "";
                        if (postFlagId == 1) {
                            requestData.postFlagId = 0;
                            requestData.postReplyId = postId;
                            sendId = postId;
                        } else if (postFlagId == 0) {
                            requestData.postFlagId = 0;
                            requestData.postReplyId = $('#postId').val();
                            sendId = $('#postId').val();
                        }
                        $.ajax({
                            type: "post",
                            data: requestData,
                            url: '/forum/addFReply.do',
                            async: false,
                            dataType: "json",
                            traditional: true,
                            success: function (result) {
                                $('#title').hide();
                                var pageCount = $('#pageN').val();
                                if (pageCount) {
                                } else {
                                    pageCount = 1;
                                }
                                getReplyList(postId);
                            }
                        });
                    } else {
                        $('#title').show();
                    }
                } else {
                    $('.store-register').fadeToggle();
                    $('.bg').fadeToggle();
                }
            }
        });

    }

    function getReplyList(postId) {
        var param = {};
        param.replyId = postId;
        Common.getData('/forum/getReply.do', param, function (result) {
            if (postId == $('#postId').val()) {
                if (result.repliesList.length > 1) {
                    if (recall == 0) {
                        Common.render({
                            tmpl: '#lsReplies',
                            data: result.repliesList,
                            context: '#recallls',
                            overwrite: 1
                        });
                        if (result.repliesList.length > 5) {
                            var l = result.repliesList.length - 5;
                            $('#recallls').children('p').children('span').eq(0).text("还有" + l + "条,");
                        } else {
                            $('#recallls').children('p').children('span').eq(1).hide();
                        }
                    } else {
                        Common.render({
                            tmpl: '#lsReplies',
                            data: result.repliesList,
                            context: '#recallOnel',
                            overwrite: 1
                        });
                        if (result.repliesList.length > 5) {
                            var l = result.repliesList.length - 5;
                            $('#recallOnel').children('p').children('span').eq(0).text("还有" + l + "条,");
                        } else {
                            $('#recallOnel').children('p').children('span').eq(1).hide();
                        }
                    }

                } else {
                    $('#recallOne').children('div').eq(0).hide();
                    $('#recallOne').children('div').eq(1).show();
                    $('#recallOne').children('p').children('em').eq(0).removeClass('em1').addClass('em5');
                    $('#recallOne').children('p').children('em').eq(1).removeClass('em2').addClass('em6');
                    Common.render({
                        tmpl: '#lsReplies',
                        data: result.repliesList,
                        context: '#recallOnel',
                        overwrite: 1
                    });
                    $('#recallOnel').children('p').children('span').eq(1).hide();
                    recall = 1;
                }
            } else {
                if (result.repliesList.length > 1) {
                    if (rr == 0) {
                        Common.render({
                            tmpl: '#lsReplies',
                            data: result.repliesList,
                            context: '#' + postId,
                            overwrite: 1
                        });
                        if (result.repliesList.length > 5) {
                            var l = result.repliesList.length - 5;
                            $('#' + postId).children('p').children('span').eq(0).text("还有" + l + "条,");
                        } else {
                            $('#' + postId).children('p').children('span').eq(1).hide();
                        }
                    } else {
                        Common.render({
                            tmpl: '#lsReplies',
                            data: result.repliesList,
                            context: $('.' + postId).children('div').eq(1).children('div').eq(0),
                            overwrite: 1
                        });
                        if (result.repliesList.length > 5) {
                            var l = result.repliesList.length - 5;
                            $('.' + postId).children('div').eq(1).children('div').children('p').children('span').eq(0).text("还有" + l + "条,");
                        } else {
                            $('.' + postId).children('div').eq(1).children('div').children('p').children('span').eq(1).hide();
                        }
                    }

                } else {
                    $('.' + postId).children('div').eq(0).hide();
                    $('.' + postId).children('div').eq(1).show();
                    $('.' + postId).children('p').children('em').eq(0).removeClass('em1').addClass('em3');
                    $('.' + postId).children('p').children('em').eq(1).removeClass('em2').addClass('em4');
                    Common.render({
                        tmpl: '#lsReplies',
                        data: result.repliesList,
                        context: $('.' + postId).children('div').eq(1).children('div').eq(0),
                        overwrite: 1
                    });
                    $('.' + postId).children('div').eq(1).children('div').children('p').children('span').eq(1).hide();
                    rr = 1;
                }
            }
            $('.s1t').val("");
            $('.llp').hide();
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

    function getData(pSectionId) {

        var requestData = {};
        requestData.id = pSectionId;
        Common.getData("/forum/fSectionDetail.do", requestData, function (resp) {

            Common.render({
                tmpl: '#pTml',
                data: resp,
                context: '#pSeDetail',
                overwrite: 1
            });

        })
    }

    function getPostData(postId) {
        var requestData = {};
        requestData.postId = postId;
        Common.getData("/forum/fPostDetail.do", requestData, function (resp) {

            var cream = resp[0].cream;
            var top = resp[0].top;

            Common.render({
                tmpl: '#postRightTml',
                data: resp,
                context: '#postRight',
                overwrite: 1
            });

            var zanleng = $('#zan-listt li').length;
            if (zanleng < 13) {
                $('.show-more-zan').parent().hide();
                $('.li-diand').hide();
            }
            if (zanleng > 13) {
                $('.li-diand').addClass('li-po-ab');
                $('#zan-listt').addClass('zan-hei');
                $('.show-more-zan').parent().addClass('zan-btn-po')
            }


            $('body').on('click', '.zanc2', function () {
                $('#zan-listt').addClass('zan-hei');
                $(this).html('全部').addClass('zanc1').removeClass('zanc2');
                $(this).parent().addClass('zan-btn-po');
                $('.li-diand').show();
            });
            $('body').on('click', '.zanc1', function () {
                $('#zan-listt').removeClass('zan-hei');
                $(this).html('收起').removeClass('zanc1').addClass('zanc2');
                $(this).parent().removeClass('zan-btn-po');
                $('.li-diand').hide();
            });

            if (resp[0].InSet == 1 || (resp[0].InSet == -1 && resp[0].cate == 1 )) {
                $("#ssl").hide();
                $("#comment").html("我要参与");
                $("#areahh").html("参与活动区");
                $("#expressTheme").hide();
                $("#shl").hide();
                $("#cope").html("大赛");
                $("#zan").html("我要参与");
                $("#pinlun").html("我要参与");
                $("#try").addClass("ha4");
                $("#tryl").addClass("ha4");
                $("#trr").removeClass("ha4").addClass("ha3");
                $("#trrl").removeClass("ha4").addClass("ha3");
            } else {
                $("#sel-type").hide();
            }
            if (resp[0].fl.length > 0) {
                $('#recallTwo').show();
                Common.render({
                    tmpl: '#postRepliesTml',
                    data: resp[0].fl,
                    context: '#recallls',
                    overwrite: 1
                });
                if (resp[0].fl.length > 5) {
                    var l = resp[0].fl.length - 5;
                    $('#recallls').children('p').children('span').eq(0).text("还有" + l + "条,");
                } else {
                    $('#recallls').children('p').children('span').eq(1).hide();
                }
            } else {
                $('#recallOne').show();
            }

            Common.render({
                tmpl: '#replyTml',
                data: resp,
                context: '#postDetailData',
                overwrite: 1
            });

            $('#fpostSearch').change(function () {
                var person = $(this).val();
                if (person == "1") {
                    person = "";
                } else if (person == "2") {
                    person = $('#personId').val();
                }
                getReplyData(1, postId, pSectionId, person);
            });

            $('.recall-sf #selfWant').click(function () {
                $(this).parent().parent().next('.s1').slideToggle();
            });

            $('#search').click(function () {
                $(this).parent().parent().children('.llp').show();
                $(this).hide();
                $(this).prev().hide();
                $(this).next().show();
            });

            $('#soQi').click(function () {
                $(this).parent().parent().children('.llp').hide();
                $(this).hide();
                $(this).prev().show();
                $(this).prev().prev().show();
            });

            personId = resp[0].personId;
            requestData.postSectionId = pSectionId;
            requestData.personId = personId;
            Common.getData("/forum/fUserDetail.do", requestData, function (resp) {
                Common.render({
                    tmpl: '#pUserTml',
                    data: resp,
                    context: '#' + personId,
                    overwrite: 1
                });

                Common.getData("/forum/loginInfo.do?date=" + new Date(), {}, function (resp) {
                    userId = resp.userId;
                    userPermission = resp.userPermission;
                });

                if (userId) {
                    if (userId == personId) {
                        $('#deletePost').show();
                    }
                }

                if (userPermission) {
                    if (userPermission > 100) {
                        $('#downloadLike').show();
                        $('#downloadLikeZip').show();
                        $('#deletePost').show();
                        $('#editPost').show();
                        if (cream == 1) {
                            $('#creamNoSet').show();
                        } else if (cream == 0) {
                            $('#creamSet').show();
                        }
                        if (top == 1) {
                            $('#topNoSet').show();
                        } else if (top == 0) {
                            $('#topSet').show();
                        }
                    }
                }

            });

        })
    }

    function getBtnZan() {
        if (login) {
            var paramData = {};
            paramData.userReplyId = userId;
            paramData.id = $('#postId').val();
            Common.getData('/forum/btnZan.do', paramData, function (resp) {
                if (resp.code == 200) {
                    if (resp.message) {
                        //点赞过了
                        $('#zan').addClass('zan-btn');
                        $('#zan').attr('zan', false);//取消点赞的标志
                    } else {
                        //未点赞
                        $('#zan').addClass('btn-zan');
                        $('#zan').attr('zan', true);//点赞的标志
                    }
                }
            });
        }
    }

    function getReplyData(page, postId, pSectionId, personId) {
        var isInit = true;
        var requestData = {};
        requestData.sortType = sortType;
        requestData.page = page;
        requestData.pageSize = 8;
        if (inSet == 1 || (inSet == -1 && cate == 1)) {
            requestData.pageSize = 15;
        }
        requestData.person = personId;
        requestData.post = postId;
        requestData.postSection = pSectionId;
        $("#pageS").attr("value", page);
        Common.getData("/forum/fReply.do", requestData, function (resp) {
            var result = resp.list;
            // alert(JSON.stringify(result));
            $('.new-page-links').html("");
            if (result.length > 0) {
                if (Math.ceil(resp.count / requestData.pageSize) == 0) {
                    pageCount = 1;
                } else {
                    pageCount = Math.ceil(resp.count / requestData.pageSize);
                }
                $('#pageN').val(pageCount);
                $('.new-page-links').jqPaginator({
                    totalPages: Math.ceil(resp.count / requestData.pageSize) == 0 ? 1 : Math.ceil(resp.count / requestData.pageSize),//总页数
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
                            getReplyData(n, postId, pSectionId, personId);
                            $('body,html').animate({scrollTop: 0}, 20);
                        }
                    }
                });
            }

            if (resp.count <= 8) {
                $('.new-page-links').html("");
            }

            Common.render({
                tmpl: '#pReplyListTml',
                data: result,
                context: '#listReply',
                overwrite: 1
            });

            $.extend({
                getUrlVars: function () {
                    var vars = [], hash;
                    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
                    for (var i = 0; i < hashes.length; i++) {
                        hash = hashes[i].split('=');
                        vars.push(hash[0]);
                        vars[hash[0]] = hash[1];
                    }
                    return vars;
                },
                getUrlVar: function (name) {
                    return $.getUrlVars()[name];
                }
            });

            //渲染完毕
            var floor = $.getUrlVar('floor');
            if (floor && floor != 0) {
                $.ajax({
                    url: "/forum/floorTimeText.do",
                    type: "post",
                    data: {
                        floor: floor,
                        postId: $('#postId').val()
                    },
                    success: function (data) {
                        if (data.message == 0) {
                            alert("楼层不存在");
                            return;
                        }
                        var offset = 0;
                        if (inSet == 1) {
                            offset = 80;
                        }
                        if ($("#" + data.message).length > 0) {
                            $("html,body").animate({scrollTop: $("#" + data.message).offset().top - offset}, 1000);
                        } else {
                            if (inSet == 1 || (inSet == -1 && cate == 1)) {
                                location.href = '/forum/floorPosition.do?floor=' + t + '&postId=' + $('#postId').val() + '&pageSize=15';
                            } else {
                                location.href = '/forum/floorPosition.do?floor=' + t + '&postId=' + $('#postId').val() + '&pageSize=8';
                            }
                        }
                    },
                    error: function (data) {

                    }
                });
            }

            //增加下载附件
            $('.downloadAttach').click(function () {
                var replyId = $(this).attr('value');
                window.location = "/reply/downloadAttach?replyId=" + replyId;
            });

            $('.btn-ok').click(function () {
                addUserPhone();
            });

            $('.btn-no').click(function () {
                $('.bg').fadeOut();
                $('.wind-num').fadeOut();
            });

            $('#cancel').click(function () {
                $('.bg').fadeOut();
                $('.wind-num').fadeOut();
            });

            //pdf
            var options = {
                pdfOpenParams: {
                    pagemode: "thumbs",
                    navpanes: 0,
                    toolbar: 0,
                    statusbar: 0,
                    messages: 1,
                    view: "FitV"
                }
            };


            $(".img-pdf-rt").each(function () {
                var pdf = $(this).attr('url');
                $(this).click(function () {
                    window.open('/preview/pdf?pdf=' + pdf);
                });
            });

            $(".pdf").each(function () {

                if (PDFObject.supportsPDFs) {
                    var pdf = $(this).attr('url');
                    $(this).parent().find(".pdf-rt").click(function () {
                        window.open('/preview/pdf?pdf=' + pdf);
                    });
                    PDFObject.embed(pdf, this, options);
                } else {
                    $(this).append("<p>此PDF暂时不支持您的浏览器，请使用chrome或者QQ浏览器，或者360浏览器</p>");
                }

            });

            $(".aplayer").each(function () {
                var mp3 = $(this).attr('url');
                var ap1 = new APlayer({
                    element: this,
                    narrow: false,
                    autoplay: false,
                    showlrc: false,
                    music: {
                        title: '语音',
                        author: ' ',
                        url: mp3,
                        pic: '/static/dist/musicplayer/music/background.png'
                    }
                });
                ap1.init();
            });


            $('.post-bt-hide').each(function () {
                var pheight = $(this).outerHeight();
                if (pheight > 384) {
                    $(this).css({
                        'height': '380px',
                        'overflow': 'hidden',
                        'background': '#f9f9f9',
                        'border': '1px solid #e5e5e5'
                    });
                    $(this).next().show();
                    $(this).children('.jianbian').show();
                }
            });

            $('body').on('click', '.btn-zk', function () {
                $(this).next().show();
                $(this).hide();
                $(this).parent().prev().css({
                    'height': 'auto',
                    'overflow': 'visible',
                    'background': '#ffff',
                    'border': 'none'
                });
                $(this).parent().prev().children('.jianbian').hide();
            });

            $('body').on('click', '.btn-sq', function () {
                $(this).prev().show();
                $(this).hide();
                $(this).parent().prev().css({
                    'height': '231px',
                    'overflow': 'hidden',
                    'background': '#f9f9f9',
                    'border': '1px solid #e5e5e5'
                });
                $(this).parent().prev().children('.jianbian').show();
            });


            for (var i in result) {
                Common.render({
                    tmpl: '#lsReplies',
                    data: result[i].repliesList,
                    context: '#' + result[i].fReplyId,
                    overwrite: 1
                });
                if (result[i].repliesList.length > 5) {
                    var l = result[i].repliesList.length - 5;
                    $('#' + result[i].fReplyId).children('p').children('span').eq(0).text("还有" + l + "条,");
                } else {
                    $('#' + result[i].fReplyId).children('p').children('span').eq(1).hide();
                }
                //渲染完之后
                $('.aplayer1').each(function () {
                    var ap = new APlayer({
                        element: $(this),
                        narrow: true,
                        autoplay: false,
                        showlrc: false,
                        music: {
                            title: 'Sugar',
                            author: 'Maroon 5',
                            url: '/static/music/Sugar.mp3',
                            pic: '/static/music/Maroon5.jpg'
                        }
                    });
                    ap.init();
                });
            }

            Common.getData("/forum/loginInfo.do?date=" + new Date(), {}, function (resp) {
                userId = resp.userId;
                if (resp.login) {
                    if (resp.userPermission > 100) {
                        $('.delpl').show();
                        $(".deleteLOL").show();
                    }
                }
            });

            $('.llp').hide();

            $('.deleteBox').click(function () {
                reply = $(this).attr('value');
                $('.post-wind .qu-p1').text('您确定要删除选择的评论吗？');
                $('.post-wind .qu-p2').text('');
                $('.post-wind').fadeIn();
                $('.bg').fadeIn();
                flag = 2;
            });

            $('.good').click(function () {
                reply = $(this).attr('value');
                $('#goodBox').fadeIn();
                $('.bg').fadeIn();
            });

            $('#goodButton').click(function () {
                var requestPost = {};
                requestPost.reply = reply;
                requestPost.value = $('#input').val();
                Common.getPostData("/admin/goodgood.do", requestPost, function (resp) {
                    if (resp.code == 200) {
                        window.location.reload();
                    } else {
                        alert(resp.message);
                    }
                })
            });

            $('#btnConfirm').click(function () {
                var requestPost = {};
                if (flag == 1) {
                    requestPost.post = postId;
                    Common.getPostData("/forum/deletePost.do", requestPost, function (resp) {
                        if (resp.code == 200) {
                            location.href = '/forum/postIndex.do?pSectionId=' + pSectionId;
                        } else {
                            alert(resp.message);
                        }
                    })
                } else if (flag == 2) {
                    requestPost.reply = reply;
                    requestPost.post = postId;
                    Common.getPostData("/forum/deleteReply.do", requestPost, function (resp) {
                        if (resp.code == 200) {
                            location.href = '/forum/postDetail.do?pSectionId=' + pSectionId + '&postId=' + postId + '&personId=' + personId + '&page=1&ti=' + new Date().getTime();
                        } else {
                            alert(resp.message);
                        }
                    })
                } else if (flag == 3) {
                    requestPost.post = postId;
                    requestPost.top = 1;
                    Common.getPostData("/forum/updatePostTop.do", requestPost, function (resp) {
                        if (resp.code == 200) {
                            location.href = '/forum/postIndex.do?pSectionId=' + pSectionId;
                        } else {
                            alert(resp.message);
                        }

                    })
                } else if (flag == 4) {
                    requestPost.post = postId;
                    requestPost.personId = $('#personId').val();
                    requestPost.cream = 1;
                    Common.getPostData("/forum/updatePostCream.do", requestPost, function (resp) {
                        if (resp.code == 200) {
                            location.href = '/forum/postIndex.do?pSectionId=' + pSectionId;
                        } else {
                            alert(resp.message);
                        }

                    })
                } else if (flag == 5) {
                    requestPost.post = postId;
                    requestPost.top = 0;
                    Common.getPostData("/forum/updatePostTop.do", requestPost, function (resp) {
                        if (resp.code == 200) {
                            location.href = '/forum/postIndex.do?pSectionId=' + pSectionId;
                        } else {
                            alert(resp.message);
                        }

                    })
                } else if (flag == 6) {
                    requestPost.post = postId;
                    requestPost.personId = $('#personId').val();
                    requestPost.cream = 0;
                    Common.getPostData("/forum/updatePostCream.do", requestPost, function (resp) {
                        if (resp.code == 200) {
                            location.href = '/forum/postIndex.do?pSectionId=' + pSectionId;
                        } else {
                            alert(resp.message);
                        }

                    })
                }
                $('.post-wind').fadeOut();
                $('.bg').fadeOut();
            });

            $('.replyPerson').each(function () {
                    $(this).change(function () {
                        var person = $(this).val();
                        if (person == "1") {
                            person = "";
                        }
                        getReplyData(1, postId, pSectionId, person);
                    });
                }
            );

            $('.reply-left').each(function () {
                var that = this;
                var data = {};
                data.postSectionId = pSectionId;
                data.personId = $(this).attr('personId');
                Common.getData("/forum/fUserDetail.do", data, function (resp) {
                    Common.render({
                        tmpl: '#pUserTml',
                        data: resp,
                        context: that,
                        overwrite: 1
                    });
                });
            });

        })
    }

    function replaceAPI(api, options) {
        api = api.replace('{url}', options.url);
        api = api.replace('{title}', options.title);
        api = api.replace('{content}', options.content);
        api = api.replace('{pic}', options.pic);

        return api;
    }

    function tQQ(target, options) {
        var options = $.extend({}, shareSettings, options);

        window.open(replaceAPI(tqq, options));
    }

    function qZone(target, options) {
        var options = $.extend({}, shareSettings, options);

        window.open(replaceAPI(qzone, options));
    }

    function sinaWeibo(target, options) {
        var options = $.extend({}, shareSettings, options);

        window.open(replaceAPI(sina, options));
    }

    function doubanShare(target, options) {
        window.open(replaceAPI(douban, $.extend({}, shareSettings, options)));
    }

    function weixinShare(target, options) {
        window.open(replaceAPI(weixin, $.extend({}, shareSettings, options)));
    }

    function qqShare(target, options) {
        window.open(replaceAPI(qqfriend, $.extend({}, shareSettings, options)));
    }

    function weiFriendShare(target, options) {
        window.open(replaceAPI(weiFriend, $.extend({}, shareSettings, options)));
    }

    function getSendUrl(ti, ul) {
        var sendUrl;
        if (ti) {
            if (ul.indexOf("page") > -1) {
                var kl = ul.substr(0, ul.indexOf("&page"));
                sendUrl = kl + "&page=" + $("#pageS").attr("value") + "&timeText=" + ti + "&sortType=" + sortType;
            } else {
                if (ul.indexOf("timeText") > -1) {
                    var jl = ul.substr(0, ul.indexOf("&timeText"));
                    sendUrl = jl + "&page=" + $("#pageS").attr("value") + "&timeText=" + ti + "&sortType=" + sortType;
                } else {
                    sendUrl = ul + "&page=" + $("#pageS").attr("value") + "&timeText=" + ti + "&sortType=" + sortType;
                }
            }
        }
        return sendUrl;
    }

    module.exports = postDetail;
});