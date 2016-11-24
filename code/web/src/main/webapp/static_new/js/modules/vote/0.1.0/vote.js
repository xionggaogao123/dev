/**
 * Created by qiangm on 2015/11/23.
 */
'use strict';
define(['doT', 'common', 'jquery', 'initPaginator', 'rome', 'fancybox'], function (require, exports, module) {
    /**
     *初始化参数
     */
    var Paginator = require('initPaginator');
    var vote = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    vote.init = function () {



    };

    /**
     * 初始化rome日历插件
     */
    function initRome() {
        try {
            var moment = rome.moment;

            var now = new Date();
            var timeStr = now.getFullYear() + "-" + (now.getMonth() + 1) + "-" + now.getDate();
            rome(starttimeforvote, {min: timeStr, time: false, dateValidator: rome.val.beforeEq(closetimeforvote)});
            rome(closetimeforvote,
                {min: timeStr, time: false, dateValidator: rome.val.afterEq(starttimeforvote)}
            );
        }
        catch (e) {

        }
    }

    initRome();

    $(document).ready(function ($) {
        //nth-child for ie8
        //var browser=navigator.appName
        //var b_version=navigator.appVersion
        //var version=b_version.split(";");
        //var trim_Version=version[1].replace(/[ ]/g,"");

        //if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion .split(";")[1].replace(/[ ]/g,"")=="MSIE8.0"){
            /*$(".vote-li dl dd:nth-child(1)").css('cursor','pointer');
            $(".ul-hxr li:nth-child(2)").css({'font-size': '12px','margin-bottom': '7px'});
            $(".vote-li dl dd:nth-child(1)").css({'color': '#5B5B5B','font-size': '16px', 'margin-top': '11px'});
            $(".bm-slide1 i:nth-child(1)").css('background','url(../../../../images/tp_tool_img.png)');
            $(".bm-slide1 i:nth-child(2)").css('background','url(../../../../images/tp_tool_yy.png)');
            $(".tc-out2 button,.dwote2 button:nth-child(2)").css({'background': '#468CC8', 'color': '#FFF', 'border-radius':'4px', 'padding': '5px 0px', 'width': '80px', 'border': 'none'});
            $(".dwote2 button:nth-child(1)").css({'background': '#FF7900', 'color': '#FFF', 'border-radius': '4px', 'padding': '5px 0px', 'width': '80px'});
            $(".tc-out2 button:nth-child(1)").css({'background': '#9D9D9D', 'margin-left': '10px'});
            $(".bm-k dl dd:nth-child(1)").css({'color': '#fff', 'background': '#546FB4' , 'line-height': '40px', 'padding':'0 13px', 'font-size': '17px'});
            $(".bm-k dl dd:nth-child(1) i").css({'float': 'right', 'font-size': '22px', 'cursor': 'pointer'});
            $(".bm-k dl dd:nth-child(2)").css({'text-align': 'center', 'background-color': '#fff', 'padding':'20px 30px', 'border-bottom': '1px solid #ddd', 'background': 'url(../../../../images/jinggao.jpg) no-repeat'});
            $(".bm-k dl dd:nth-child(3) button").css({'display': 'inline-block', "color": '#FFF', 'border-radius': '4px', 'padding': '5px 0px', 'width': '80px', 'border': 'none', 'background': '#FF7900', 'margin':'10px 0 10px 120px'});
            $(".li-hxr-cont1 p:nth-child(1)").css('color', '#18A3E4');
            $(".xg-slide1 i:nth-child(1)").css('background', 'url(../../../../images/tp_tool_img.png)');
            $(".xg-slide1 i:nth-child(2)").css('background', 'url(../../../../images/tp_tool_yy.png)');
            $(".xg-slide1 i:nth-child(3)").css('background', 'url(../../../../images/tp_tool_vedio.png)');*/
        //;


        $(".span-back").click(function () {
            $(".vote-detail").hide();
            $(".vote-cont").show();
            vote.getVoteList(currentPage, 5);
        });

        $(".vote-li dl dd .div-hxr .hxr-chi button").click(function () {
            $(this).fadeOut("fast");
            $(this).next().css({
                "display": 'inline-block'
            });
        });

        $('body').click(function (e) {
            var target = $(e.target);
            // 如果#overlay或者#btn下面还有子元素，可使用
            // !target.is('#btn *') && !target.is('#overlay *')
            if (!target.is('.btn-bm') && !target.is('.btn-tc') && $(".vote-detail").css("display") == "none") {
                if (target.is(".vote-li2")) {
                    var selectId = target.parent().attr("eid");
                    vote.getDetail(selectId);
                    $(".vote-detail").show();
                    $(".vote-cont").hide();
                }
                else if (target.parent().is(".vote-li2")) {
                    var selectId = target.parent().parent().attr("eid");
                    vote.getDetail(selectId);
                    $(".vote-detail").show();
                    $(".vote-cont").hide();
                }
                else if (target.parent().parent().is(".vote-li2")) {
                    var selectId = target.parent().parent().parent().attr("eid");
                    vote.getDetail(selectId);
                    $(".vote-detail").show();
                    $(".vote-cont").hide();
                }
            }
        });


        /*检查表单*/

        $(".check-f2 button, .check-form p i").click(function () {
            $(".check-form").fadeOut('fast');
            $(".bg").fadeOut('slow');
        });

        $(".check2-f2 button, .check-form2 p i").click(function () {
            $(".check-form2").fadeOut('fast');
            $(".bg").fadeOut('slow');
            vote.getDetail($(".delete-elect").attr("eid"));
        });

        //报名弹出
        $("body").on("click", ".btn-bm", function () {
            if ($(this).text() == "报名") {
                //$(".bm-slide").slideUp('400');
                $(this).parent().parent().next().slideToggle('400');
                $(this).text("取消");
            }
            else {
                $(this).parent().parent().next().slideUp('400');
                $(this).text("报名");
            }
        });

        $(".bm-k dl dd:nth-child(3) button,.bm-k dl dd:nth-child(1) i").click(function () {
            $(".bm-k").fadeOut('fast');
            $(".bg").fadeOut('fast');
        });



        $("body").on("click", ".span-stu", function () {
            $(this).toggleClass("dd-down");
            $(".ul-stu").slideToggle("fast");
        });

        $("body").on("click", ".span-pre", function () {
            $(this).toggleClass("dd-down");
            $(".ul-pre").slideToggle("fast");
        });
        $("body").on("click", ".span-par", function () {
            $(this).toggleClass("dd-down");
            $(".ul-par").slideToggle("fast");
        });
        $("body").on("click", ".span-tea", function () {
            $(this).toggleClass("dd-down");
            $(".ul-tea").slideToggle("fast");
        });
        $("body").on("click", ".span-fri", function () {
            $(this).toggleClass("dd-down");
            $(".ul-fri").slideToggle("fast");
        });
        $("body").on("click", ".span-edu", function () {
            $(this).toggleClass("dd-down");
            $(".ul-edu").slideToggle("fast");
        });

        $(".tc-tj p i, .tj-left button").click(function () {
            $(".tc-tj").fadeOut("fast");
            $(".bg").fadeOut("fast");
        });

        $(".delete-wote p i,.delete-wote .dwote2 button:nth-child(2),.tc-out p i,.tc-out .tc-out2 button:nth-child(2)").click(function () {
            $(".delete-wote").fadeOut();
            $(".tc-out").fadeOut();
            $(".bg").fadeOut();
        })

        $("body").on("click", ".btn-xg", function () {
            $(this).parent().hide();
            $(this).parent().next().slideToggle('400');
            $(this).hide();
        });

        $(".xg-slide1 button").click(function () {
            $(".li-hxr-cont2").slideDown("400");
            $(".xg-slide").slideUp("400");
        });

        //删除投票选举
        $("body").on("click", ".delete-elect", function () {
            global_voteId = $(this).attr("eid");
            $(".delete-wote").fadeIn();
            $(".bg").fadeIn();
            $(".dwote1").text("确认删除该投票选举吗？");
            //vote.deleteVote(voteId);
        });
        $("body").on("click", ".ok", function () {
            $(".delete-wote").fadeOut();
            $(".bg").fadeOut();
            vote.deleteVote();
        });

        $("body").on("click", ".tc-out .tc-out2 button:nth-child(1)", function () {
            $(".tc-out").fadeOut();
            $(".bg").fadeOut();

            vote.quitVote();
        });
        //退出竞选
        $("body").on("click", ".btn-tc", function () {
            global_voteId = $(this).attr("eid");
            global_obj = $(this);
            $(".tc-out").fadeIn('slow');
            $(".bg").fadeIn('fast');
            //vote.quitVote(voteId,$(this));
        });
        //投他一票
        $("body").on("click", ".ttyp-bt", function () {
            var voteId = $(this).attr("eid");

            vote.vote(voteId, this);
        });
        //报名
        $("body").on("click", ".bm-btn", function () {
            var a = $(this).parent().prev().val();
            if (a == "") {
                $(".bm-k").fadeIn('fast');
                $(".bg").fadeIn('fast');
            }
            else {
                vote.runForElect(this);
            }
        });

        //显示录音工具
        $("body").on("click", ".type0", function () {
            vote.showRecordflash(this, $(this).attr("eid"), 0);
        });
        //修改报名
        $("body").on("click", ".updateOK", function () {
            vote.updateMyInfo(this);
        });
        //删除图片、视频
        $("body").on("click", ".blog-img-delete", function () {
            $(this).parent().remove();
        });
        //关闭视频播放框
        $("body").on("click", ".player-close-btn", function () {
            vote.closeElectMovie();
        });

    });
    var mc = null;

    var global_voteId = "";
    var global_obj = null;
    //报名
    vote.runForElect = function (btn) {
        var electId = $(btn).parent().parent().attr("eid");
        var $bm = $(btn).closest('.tp-bm-box');
        $.ajax({
            url: '/elect/runForElect.do',
            type: 'POST',
            data: {
                electId: electId,
                manifesto: $(btn).parent().prev().val(),
                picUrls: $.map($bm.find('.candidate-img'), function (img) {
                    return $(img).attr('src');
                }),
                videoId: $bm.find('.vote-vedio-container ul li').attr('data-id'),
                voiceUrl: $bm.find('.vote-audio-container ul p a').attr('url')
            },
            traditional: true,
            success: function (elect) {
                if (elect.code == "500") {
                    alert("服务器繁忙，报名失败");
                    return;
                }
                $(".tp-bm-box").hide();
                if ($('.vote-cont').is(':hidden')) {
                    vote.getDetail(electId);
                } else {//在主界面
                    vote.getVoteList(currentPage, 5);

                }
                if (elect.score){
                    vote.scoreManager(elect.scoreMsg, elect.score);
                }
                /*var score=$(".user-info").find("span").eq(0).text();
                var score1=Number(score.substring(3))+Number(elect.score);
                $(".user-info").find("span").eq(0).text("经验值"+score1);*/
            }
        });
    };
    //投票
    vote.vote = function (voteId, obj) {
        $.ajax({
            url: '/elect/vote.do',
            type: 'POST',
            data: {
                electId: voteId,
                candidateId: $(obj).parent().attr("uid")
            },
            dataType: 'json',
            success: function (elect) {
                if (elect.code == "500") {
                    alert("服务器繁忙，投票失败");
                    return;
                }
                if (elect.msg) {
                    alert(elect.msg);
                    return;
                }
                if (elect.score) {
                    vote.scoreManager(elect.scoreMsg, elect.score);
                    /*var score=$(".user-info").find("span").eq(0).text();
                    var score1=Number(score.substring(3))+Number(elect.score);
                    $(".user-info").find("span").eq(0).text("经验值"+score1);*/
                }
                var vCount = 0;
                for (var i = 0; i < elect.elect.candidates.length; i++) {
                    if (elect.elect.candidates[i].id == $(obj).parent().attr("uid")) {
                        vCount = elect.elect.candidates[i].ballots.length;
                        break;
                    }
                }
                if (vCount > 0) {
                    if($(obj).prev().text()!="")
                        $(obj).prev().text(vCount + "票");
                }
                $(obj).after('<span class="tp-yt" style="display: inline-block"></span>');
                $(obj).remove();
            }
        });
    };
    var recordObj = null;
    //展开录音
    vote.showRecordflash = function (obj, electId, type) {
        var $flash = $(obj).parent('.bm-slide1');
        try {
            if (recordObj != null) {
                recordObj.empty();
            }
            if (mc == null) {
                mc = new FlashObject("/static/plugins/audiorecorder.swf", "recorderApp", "350px", "23px", "8");
                mc.setAttribute("id", "recorderApp");
                mc.setAttribute("name", "recorderApp");
                mc.addVariable("uploadAction", "/elect/upload.do");
            }
            if (type == 0) {
                var index = $(obj).attr("index");
                recordObj = $("#myContent-" + electId + "-" + index);
                mc.write("myContent-" + electId + "-" + index);
                $flash.show();
                $flash.find('.sanjiao').show();
                $flash.find("#myContent-" + electId + "-" + index).show();
            }
            else if (type == 1) {
                recordObj = $("#myContent1-" + electId);
                mc.write("myContent1-" + electId);
                $flash.show();
                $flash.find('.sanjiao').show();
                $flash.find("#myContent1").show();
            }
            else if (type == 2) {
                var index = $(obj).attr("index");
                mc.write("myContent2-" + electId + "-" + index);
                recordObj = $("#myContent2-" + electId + "-" + index);
                $flash.show();
                $flash.find('.sanjiao').show();
                $flash.find("#myContent2-" + electId + "-" + index).show();
            }
        }
        catch (e) {
        }
    };


    function closeElectMovie() {
        $('.dialog-bg').fadeOut('fast');
        var $player_container = $("#elect-player");
        $player_container.fadeOut('fast', function () {
            $('#elect-player-div').empty();
            try {
                SewisePlayer.doStop();
            } catch (e) {
            }
        });
    }

    vote.quitVote = function () {
        $.ajax({
            url: '/elect/abstain.do',
            data: {'electId': global_voteId},
            type: 'get',
            datatype: 'json',
            success: function (response) {
                if (response.code == "500") {
                    alert("服务器繁忙，退出失败");
                    return;
                }
                if ($('.vote-cont').is(':hidden')) {
                    vote.getDetail(global_voteId);
                } else {//在主界面
                    var targetList = global_obj.parent().prev().find(".div-hxr").eq(0).find(".hxr-list");
                    if (targetList.length > 1) {
                        targetList.each(function (i, ele) {
                            var uid = ele.attributes.uid.value;
                            if (uid == userId) {
                                //删除
                                $(ele).remove();
                                return;
                            }
                        });
                    }
                    else {
                        targetList.eq(0).remove();
                        $(this).parent().prev().find(".hxr-hxr").eq(0).
                            append('<span class="no-hxr">目前没有候选人哦~~快快报名吧!</span>');
                    }
                    global_obj.parent().append('<button class="btn-bm" eid="' + global_voteId + '">报名</button>');
                    global_obj.remove();
                }
                if(response.score){
                    vote.scoreManager(response.scoreMsg, response.score);
                }
                /*var score=$(".user-info").find("span").eq(0).text();
                var score1=Number(score.substring(3))+Number(response.score);
                $(".user-info").find("span").eq(0).text("经验值"+score1);*/
            }, error: function () {
                alert('删除失败!');
            }
        });
    };
    vote.deleteVote = function () {
        $.ajax({
            url: '/elect/delete.do',
            data: {'id': global_voteId},
            type: 'get',
            datatype: 'json',
            success: function (response) {
                $(".vote-detail").hide();
                $(".vote-cont").show();
                vote.getVoteList(0, 5);
                if(response.score){
                    vote.scoreManager(response.scoreMsg, response.score);
                }
            },
            error: function () {
                alert('服务器错误!');
            }

        });
    };
    vote.toggle = function (index) {
        var eleClass = ".btn-bm:eq(" + index + ")";
        $("body").on("click", eleClass, function () {
            $(".bm-slide").slideToggle('400');
        });
    };
    //关闭视频播放
    vote.closeElectMovie = function () {
        $('.bg').fadeOut('fast');
        var $player_container = $("#elect-player");
        $player_container.fadeOut('fast', function () {
            $('#elect-player-div').empty();
            try {
                SewisePlayer.doStop();
            } catch (e) {
            }
        });
    };
    //修改投票宣言
    vote.updateMyInfo = function (btn) {
        //var $elect = $(btn).closest('.elect');
        var $bm = $(btn).closest('.tp-bm-box');
        $.ajax({
            url: '/elect/updateCandidate.do',
            data: {
                'electId': $(btn).parent().parent().attr('eid'),
                'id': $(btn).attr("uid"),
                'manifesto': $(btn).parent().prev().val(),
                'picUrls': $.map($bm.find('.candidate-img'), function (img) {
                    return $(img).attr('src');
                }),
                'videoId': $bm.find('.vote-vedio-container ul li').attr('data-id'),
                'voiceUrl': $bm.find('.vote-audio-container ul p a').attr('url')
            },
            traditional: true,
            type: 'post',
            dataType: 'json',
            success: function (data) {
                vote.getDetail($(btn).parent().parent().attr('eid'));
            },
            error: function () {

            }
        });
    };
    vote.launchVote = function () {
        if (vote.checkvote()) {
            var voteCatogery = $.map($('.view-catogery:checked'), function (checkbox) {
                return $(checkbox).val();
            });
            var elect = {
                'name': $('#voteName').val(),
                'description': $('#descriptforvote').val().replace(/\n/g, '<br>'),
                'classIds': voteCatogery,
                parentEligible: false,
                studentEligible: false,
                teacherEligible: false,
                leaderEligible: false,
                'parentVotable': $('#parentVotable').prop('checked'),//投票角色
                'studentVotable': $('#studentVotable').prop('checked'),
                'teacherVotable': $('#teacherVotable').prop('checked'),
                'leaderVotable': $('#leaderVotable').prop('checked'),
                'startDate': $('#starttimeforvote').val().replace(/-/g, ''),
                'endDate': $('#closetimeforvote').val().replace(/-/g, ''),
                'ballotCount': $('#ballotCount').val(),
                'publish':0
            };
            if($(".gbjg").is(':checked'))
            {
                elect.publish=0;
            }
            else{
                elect.publish=1;
            }
            var customCandidateArr = new Array();
            if ($('.zdhxr').prop('checked')) {

                elect.candidateArr = selectUserId;
            } else {
                elect.parentEligible = $('#parentEligible').prop('checked');
                elect.studentEligible = $('#studentEligible').prop('checked');
                elect.teacherEligible = $('#teacherEligible').prop('checked');
                elect.leaderEligible = $('#leaderEligible').prop('checked');
            }
            $.ajax({
                url: '/elect/addElect.do',
                type: 'post',
                data: elect,
                traditional: true,
                success: function (response) {
                    vote.getVoteList(0, 5);
                    $("#voteName").val("");
                    $("#descriptforvote").val("");
                    $("#selectAllSchool").prop("checked", false);
                    $(".view-catogery").prop("checked", false);
                    $(".cxjs_role").prop("checked", false);
                    $(".zdhxr").prop("checked", false);
                    selectUserId = [];
                    selectUserMap = [];
                    $(".div-zd").hide();
                    $(".tp_role").prop("checked", false);
                    $("#starttimeforvote").val("");
                    $("#closetimeforvote").val("");
                    $("#ballotCount").val("3");
                    if (response.score) {
                        vote.scoreManager(response.scoreMsg, response.score);
                        /*var score=$(".user-info").find("span").eq(0).text();
                        var score1=Number(score.substring(3))+Number(response.score);
                        $(".user-info").find("span").eq(0).text("经验值"+score1);*/
                    }
                },
                error: function () {
                }
            });
        }
    };
    vote.scoreManager = function (msg, score) {
        score = (score > 0) ? '+' + score : score;
        var msgShow = '<div id="msgScore" style="position: fixed;border: 4px solid #c9c9c9;height: 20px;width: 300px;overflow:hidden;top: 40%;left: 50%;margin-left:-150px;text-align: center;background: #fff;padding: 30px;color: #565656;font: 600 14px \'Microsoft YaHei\';z-index:100;"><p>' + msg + '! <strong style="color:#f80;">' + score + '</strong>经验值' + '</p></div>';
        $('body').prepend(msgShow);
        $('#msgScore').fadeOut(4000, function () {
            $('#msgScore').remove();
        });
    };

    vote.getDetail = function (elected) {
        $.ajax({
            url: "/elect/viewElect.do",
            type: "get",
            data: {
                electId: elected
            },
            success: function (data) {
                vote.wrapElectData(data);
                $("#detailShow").empty();
                Common.render({
                    tmpl: $("#detailTempJs"),
                    data: {data: data, userId: $("#userId").val()},
                    context: "#detailShow"
                });
                $("#detailBorder").empty();
                Common.render({
                    tmpl: $("#introListTempJs"),
                    data: {data: data, userId: $("#userId").val()},
                    context: "#detailBorder"
                });

                $("body").on("click", ".type1", function () {
                    vote.showRecordflash(this, $(this).attr("eid"), 1);
                });
                $("body").on("click", ".type2", function () {
                    vote.showRecordflash(this, $(this).attr("eid"), 2);
                });
                //上传图片
                $('.imgforvote').fileupload({
                    url: '/elect/upload.do',
                    paramName: 'file',
                    done: function (e, response) {
                        var $imgUl = $(this).parent().find('.vote-img-container ul');
                        $imgUl.find('.tip-msg').remove();
                        if ($imgUl.find('.candidate-img').length < 9) {
                            $imgUl.append('<li><a class="fancybox" target="_blank" href="' + response.result[0] + '" data-fancybox-group="home" title="预览"><img class="candidate-img" src="' + response.result[0] + '"></img></a><i class="fa fa-times blog-img-delete"></i></li>');
                        }
                    },
                    progressall: function (e, data) {
                        var $imgUl = $(this).parent().find('.vote-img-container ul');
                        if ($imgUl.find('.tip-msg').length == 0) {
                            $imgUl.prepend('<li class="tip-msg">正在上传...</li>');
                        }
                    }
                });
                //上传视频
                $('.videoforvote').fileupload({
                    url: '/commonupload/video.do',
                    paramName: 'Filedata',
                    formData: {'type': 'elect'},
                    done: function (e, response) {
                        var imgUrl = response.result.videoInfo.imageUrl;
                        if (imgUrl == "")
                            imgUrl = '/img/K6KT/video-cover.png';
                        $(this).closest('.tp-bm-box').find('.vote-vedio-container ul').html('<li data-id="' + response.result.videoInfo.id + '">' +
                        '<img class="candidate-vedio-cover" src="' + imgUrl + '"></img><img src="/img/play.png" class="video-play-icon"/><i class="fa fa-times blog-img-delete"></i></li>');
                    },
                    progressall: function (e, data) {
                        $(this).closest('.tp-bm-box').find('.vote-vedio-container ul').html('正在上传...');
                    }
                });
            }
        });
    };
    vote.checkvote = function () {
        var title = $('#voteName').val();
        if (title.length == 0) {
            $(".check-form").fadeIn();
            $(".bg").fadeIn();
            $(".check-f1").text("请输入标题！");
            return false;
        }
        else if (title.length > 35) {
            $(".check-form").fadeIn();
            $(".bg").fadeIn();
            $(".check-f1").text("投票标题不能超过35个字!");
            return false;
        }
        var description = $("#descriptforvote").val().replace(/\n/g, '<br>');
        if (description.length > 500) {
            $(".check-form").fadeIn();
            $(".bg").fadeIn();
            $(".check-f1").text("说明文字不能超过500个字符！");
            return false;
        }
        if ($('.view-catogery:checked').length == 0) {
            $(".check-form").fadeIn();
            $(".bg").fadeIn();
            $(".check-f1").text("请选择范围!");
            return false;
        }
        if ($('.cxjs_role:checked').length == 0) {
            if ($('.zdhxr:checked').length == 0) {
                $(".check-form").fadeIn();
                $(".bg").fadeIn();
                $(".check-f1").text("请选择参选角色!");
                return false;
            }
            else {
                if (selectUserId.length == 0) {
                    $(".check-form").fadeIn();
                    $(".bg").fadeIn();
                    $(".check-f1").text("请选择参选角色!");
                    return false;
                }
            }
        }
        if ($('.tp_role:checked').length == 0) {
            $(".check-form").fadeIn();
            $(".bg").fadeIn();
            $(".check-f1").text("请选择投票角色!");
            return false;
        }
        var num = $.trim($('#ballotCount').val());
        if (num == "") {
            $(".check-form").fadeIn();
            $(".bg").fadeIn();
            $(".check-f1").text("请设置每人票数!");
            return false;
        }
        if (!$('#starttimeforvote').val()) {
            $(".check-form").fadeIn();
            $(".bg").fadeIn();
            $(".check-f1").text("请选择开始日期！");
            return false;
        }

        if (!$('#closetimeforvote').val()) {
            $(".check-form").fadeIn();
            $(".bg").fadeIn();
            $(".check-f1").text("请选择结束日期！");
            return false;
        }
        return true;
    };
    vote.getClassList = function () {
        var userRole = Number($("#role").val());
        if ((userRole & 64) || (userRole & 8) || (userRole & 2)) {
            $.ajax({
                url: "/elect/getClassList.do",
                type: "get",
                success: function (data) {
                    $("#classListShow").empty();
                    Common.render({tmpl: $("#classListTempJs"), data: {data: data}, context: "#classListShow"});
                }
            });
        }

    };
    vote.getContactList = function () {
        $.ajax({
            url: "/user/getAddressBookPc.do",
            type: "get",
            success: function (data) {
                $("#contractShow").empty();
                Common.render({tmpl: $("#contractTempJs"), data: {data: data}, context: "#contractShow"});
            }
        });
    };
    vote.getVoteList = function (page, size) {
        $.ajax({
            url: "/elect/elects.do",
            type: "post",
            data: {
                'page': page,
                'size': size
            },
            success: function (data) {
                if (data.content.length == 0) {
                    if (currentPage > 0) {
                        vote.getVoteList(currentPage - 1, 5);
                    }
                    return;
                }
                $.each(data.content, function (i, elect) {
                    vote.wrapElectData(elect);
                });
                $(".border").empty();
                Common.render({tmpl: $("#voteListTempJs"), data: {data: data.content}, context: ".border"});
                currentPage = page;
                var option = {
                    total: data.totalElements,
                    pagesize: data.size,
                    currentpage: page + 1,
                    operate: function (totalPage) {
                        $('.page-index span').each(function () {
                            $(this).off("click");
                            $(this).on("click", function () {
                                vote.getVoteList(Number($(this).text()) - 1, size);
                            });
                        });
                        $('.first-page').off("click");
                        $('.first-page').on("click", function () {
                            vote.getVoteList(0, size);
                        });
                        $('.last-page').off("click");
                        $('.last-page').on("click", function () {
                            vote.getVoteList(totalPage - 1, size);
                        });
                    }
                };
                Paginator.initPaginator(option);
                //上传图片
                $('.imgforvote').fileupload({
                    url: '/elect/upload.do',
                    paramName: 'file',
                    done: function (e, response) {
                        var $imgUl = $(this).parent().find('.vote-img-container ul');
                        $imgUl.find('.tip-msg').remove();
                        if ($imgUl.find('.candidate-img').length < 9) {
                            $imgUl.append('<li><a class="fancybox"  target="_blank" href="' + response.result[0] + '" data-fancybox-group="home3" title="预览"><img class="candidate-img" src="' + response.result[0] + '"></img></a><i class="fa fa-times blog-img-delete"></i></li>');
                        }
                    },
                    progressall: function (e, data) {
                        var $imgUl = $(this).parent().find('.vote-img-container ul');
                        if ($imgUl.find('.tip-msg').length == 0) {
                            $imgUl.prepend('<li class="tip-msg">正在上传...</li>');
                        }
                    }
                });
                //上传视频
                $('.videoforvote').fileupload({
                    url: '/commonupload/video.do',
                    paramName: 'Filedata',
                    formData: {'type': 'elect'},
                    done: function (e, response) {
                        var imgUrl = response.result.videoInfo.imageUrl;
                        if (imgUrl == "")
                            imgUrl = '/img/K6KT/video-cover.png';
                        $(this).closest('.tp-bm-box').find('.vote-vedio-container ul').html('<li data-id="' + response.result.videoInfo.id + '">' +
                        '<img class="candidate-vedio-cover" src="' + imgUrl + '"></img><img src="/img/play.png" class="video-play-icon"/><i class="fa fa-times blog-img-delete"></i></li>');
                    },
                    progressall: function (e, data) {
                        $(this).closest('.tp-bm-box').find('.vote-vedio-container ul').html('正在上传...');
                    }
                });
            }
        });
    };
    vote.wrapElectData = function (elect) {
        elect.voted = 0;
        if (elect.candidates) {
            $.each(elect.candidates, function (index, candidate) {
                if (candidate.ballots != null && $.inArray(userId, candidate.ballots) >= 0) {
                    candidate.voted = true;
                    elect.voted++;
                }
                if (candidate.id == userId) {
                    elect.signed = true;
                }
            });
            elect.candidates = elect.candidates.sort(function (a, b) {
                var ab = a.ballots ? a.ballots.length : 0,
                    bb = b.ballots ? b.ballots.length : 0;
                if (ab != bb) {
                    return bb - ab;
                }
                return new Date(a.signTime) - new Date(b.signTime);
            });
            //计算百分比
            if (!elect.voting) {
                elect.totalBallotCount = 0;
                $.each(elect.candidates, function (index, candidate) {
                    if (candidate.ballots != null) {
                        elect.totalBallotCount += candidate.ballots.length;
                    } else {
                        candidate.percent = 0;
                    }

                });
                $.each(elect.candidates, function (index, candidate) {
                    if (candidate.ballots != null) {
                        candidate.percent = candidate.ballots.length * 100 / elect.totalBallotCount;
                    }
                });
            }
        }
        if (elect.ballotCount == null) {
            elect.ballotCount = 1;
        }
        if (elect.voted < elect.ballotCount && elect.candidates && elect.candidates.length > elect.voted) {
            //有余票且有多余候选人可投票
            elect.votable = true;
        }

        //参选权限
        vote.eligibleRight(elect, nb_role);
        //投票权限
        vote.votableRight(elect, nb_role);
        //判断是否开始投票
        var nowDate = new Date();
        var formatDate = nowDate.getFullYear() + '/' + ((nowDate.getMonth() + 1) < 10 ? '0' + (nowDate.getMonth() + 1) : (nowDate.getMonth() + 1)) + '/' + nowDate.getDate();
        if (elect.startDate) {
            if (elect.startDate <= formatDate) {
                elect.begin = true;
            } else {
                elect.begin = false;
            }
        } else {
            elect.begin = true;
        }
    };
    //添加参选条件
    vote.eligibleRight = function (elect, nb_role) {
        elect.eligRight = false;

        if ((nb_role & 2) == 1 << 1 && elect['teacherEligible']) {
            elect.eligRight = true;
        }
        if ((nb_role & 1) == 1 && elect['studentEligible']) {
            elect.eligRight = true;
        }
        if ((nb_role & 8) == 8 && elect['leaderEligible']) {
            elect.eligRight = true;
        }
        if ((nb_role & 4) == 4 && elect['parentEligible']) {
            elect.eligRight = true;
        }
    };
    //投票权限
    vote.votableRight = function (elect, nb_role) {
        elect.voteRight = false;

        if ((nb_role & 2) == 1 << 1 && elect.teacherVotable) {
            elect.voteRight = true;
        }
        if ((nb_role & 1) == 1 && elect.studentVotable) {
            elect.voteRight = true;
        }
        if ((nb_role & 8) == 8 && elect.leaderVotable) {
            elect.voteRight = true;
        }
        if ((nb_role & 4) == 4 && elect.parentVotable) {
            elect.voteRight = true;
        }
        //票用完
        if (elect.voteRight) {
            elect.enable = false;
            if (elect.voted == elect.ballotCount) {
                elect.enable = false;
            }
        }
    };
    $(document).ready(function () {
        //选择用户
        $("body").on("click", ".selUser", function () {
            var userId = $(this).attr("ui");
            var userName = $(this).attr("un");
            for (var i = 0; i < selectUserId.length; i++) {
                if (userId == selectUserId[i]) {
                    return;
                }
            }
            selectUserId.push(userId);
            selectUserMap.push({id: userId, name: userName});
            $("#recipient").empty();
            Common.render({tmpl: $("#selectedUserTempJs"), data: {data: selectUserMap}, context: "#recipient"});
        });
        //删除用户
        $("body").on("click", ".fdelete-candidate", function () {
            var userId = $(this).attr("ui");
            for (var i = 0; i < selectUserId.length; i++) {
                if (userId == selectUserId[i]) {
                    selectUserId.splice(i, 1);
                }
            }
            for (var i = 0; i < selectUserMap.length; i++) {
                if (userId == selectUserMap[i].id) {
                    selectUserMap.splice(i, 1);
                }
            }
            $(this).prev('em').remove();
            $(this).remove();
        });
        //确认选择
        $("body").on("click", "#selectOk", function () {
            $(".tc-tj").fadeOut("fast");
            $(".bg").fadeOut("fast");
            $(".div-zd").empty();
            Common.render({tmpl: $("#selectedUserTempJs"), data: {data: selectUserMap}, context: ".div-zd"});
        });
        //弹出用户选择看
        $(".tjhxr").click(function () {
            $(".tc-tj").fadeIn('slow');
            $(".bg").fadeIn('fast');
            $("#recipient").empty();
            Common.render({tmpl: $("#selectedUserTempJs"), data: {data: selectUserMap}, context: "#recipient"});
        });
        //发布投票选举
        $("body").on("click", ".btn-fb", function () {
            vote.launchVote();
        });
        //选择全校
        $("body").on("change", "#selectAllSchool", function () {
            if (this.checked == true) {
                $(".view-catogery").prop("checked", true);
            }
            else {
                $(".view-catogery").prop("checked", false);
            }
        });
        //添加候选人
        $("body").on("click", ".zdhxr", function () {
            $(".div-zd").slideToggle("fast");
            $(".tjhxr").fadeToggle("fast");
            $(".cxjs_role").prop("checked", false);
        });
        //参选角色
        $("body").on("click", ".cxjs_role", function () {
            $(".div-zd").hide();
            $(".tjhxr").hide();
            $(".zdhxr").prop("checked", false);
        });
        if ($('.fancybox') != undefined) {
            $('.fancybox').fancybox();
        }
    });
    var selectUserId = [];
    var selectUserMap = [];
    var currentPage = 0;
    var nb_role = Number($("#role").val());
    var userId = $("#userId").val();
    vote.getClassList();
    vote.getContactList();
    vote.getVoteList(0, 5);
    vote.init();
});