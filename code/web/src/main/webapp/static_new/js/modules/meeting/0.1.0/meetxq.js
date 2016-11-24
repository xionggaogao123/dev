/*
 * @Author: Alex
 * @Date:   2016-08-10 14:24:31
 * @Last Modified by:   Alex
 * @Last Modified time: 2016-08-10 14:24:31
 */

'use strict';
define(['jquery','doT','easing','common','sharedpart','uploadify','fancybox'],function(require,exports,module){
    /**
     *初始化参数
     */
    var meetxq = {},
        Common = require('common');
    var someFileFailed = false;
    //提交参数
    var meetxqData = {};
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * homepage.init()
     */
    meetxq.init = function(){


        meetxq.uploadFile();
        meetxq.selChatLogList();
        //设置初始页码
        meetxqData.page = 1;
        //设置每页数据长度
        meetxqData.pageSize = 12;

        meetxq.selMeetDetail(1);
        $(".meetiongxq-tp span").click(function(){
            $("#meetimgxq-right-II").hide();
            $("#meetimgxq-right-III").show();
        })
        $(".meeting-top i").click(function(){
            $(".meeting-TPXQ").hide();
        });
        $("#showIss").click(function(){
            $('#issue').val('');
            $(".YT-popup").show();
            $(".zhiban-meng").show();
        });
        $("#closeIssue").click(function(){
            $(".YT-popup").hide();
            $(".zhiban-meng").hide();
        });

        $("#sureIssue").click(function(){
            meetxq.addSureIssue();
        });
        $("#meetimgxq-right-III li").click(function(){
            $(".meeting-TPXQ").show();
        })
        $(".meetiong-TP-IMG").click(function(){
            $(".zhiban-meng").show();
            $(".meeting-FQTP").show();
        })
        $(".meeting-top i").click(function(){
            $(".meeting-FQTP").hide();
        })
        $(".FQTP-QX").click(function(){
            $(".zhiban-meng").hide();
            $(".meeting-FQTP").hide();
        });
        $('.del-vote').click(function() {
            if (confirm("确定要删除该条投票吗？")) {
                meetxqData.voteId = $('#voteid').val();
                meetxq.removeVoteInfo();
            }
        });
        $('.del-vote2').click(function() {
            if (confirm("确定要删除该条投票吗？")) {
                meetxqData.voteId = $('#voteid').val();
                meetxq.removeVoteInfo();
            }
        });
        $('.meeting-past').click(function() {
            meetxqData.meetId = $('#mid').val();
            meetxq.checkInMeeting();
        });
        $(".path-start").click(function(){
            window.location.href = "/meeting/meeting.do";
        });
        $('.meeting-leave').click(function() {
            if (confirm("确定要签退吗？签退后不可以加入会议!")) {
                meetxqData.meetId = $('#mid').val();
                meetxq.checkOutMeeting();
            }
        });
        $('.meeting-JL').click(function() {
            meetxqData.meetId = $('#mid').val();
            meetxq.endMeetingInfo();
        });

        $(".submit-vote").click(function(){
            meetxqData.voteId = $('#voteid').val();
            meetxqData.chooseId=$("input[name='radio']:checked").val();
            meetxq.updateVoteInfo();
        });

        $(".FQTP-QD").click(function(){
            meetxq.addVoteInfo();
        });
        //$("#sendMsg").click(function(){
        //    meetxq.addMessage();
        //});

        $(".meeting-ul li").click(function(){
            $(".meeting-ul li").removeClass("meeting-active");
            $(this).addClass("meeting-active")
            $(".li-con>div").hide();
            var $name = $(this).attr("id");
            if ($name=='HWYC') {
                meetxq.selMeetDetail(1);
            } else if ($name=='FYRSX') {
                meetxq.selMeetDetail(2);
            } else if ($name=='HYCL') {
                meetxq.selMeetDetail(3);
            } else if ($name=='CHRY') {
                meetxq.selMeetDetail(4);
            } else if ($name=='TP') {
                meetxq.selMeetDetail(5);
            } else if ($name=='YT') {
                meetxq.selMeetDetail(6);
            }
            $("#" + "tab-" + $name).show();
        });
        $("#addChoose").click(function(){
          $('#chooselist').append("<dd><em></em><input><i class='del-choose'>X</i></dd>");
            $('.del-choose').click(function() {
                $(this).parent().remove();
            });
        })
    }
    meetxq.addMessage = function() {
        Common.getPostData('/meeting/addMessage.do',meetxqData,function(rep){
            if (rep.flag) {
               $('.talk-enter').val('');
            } else {
                alert("发送失败！");
            }
        });
    }
    meetxq.endMeetingInfo = function() {
        Common.getPostData('/meeting/endMeetingInfo.do',meetxqData,function(rep){
            if (rep.flag) {
                alert("会议存档成功！");
                window.location.href = "/meeting/meeting.do";
            } else {
                alert("会议存档失败！");
            }
        });
    }
    meetxq.updateVoteInfo = function() {
        Common.getPostData('/meeting/updateVoteInfo.do',meetxqData,function(rep){
            if (rep.flag) {
                alert("投票成功！");
                meetxq.selVoteDetail(1);
            } else {
                alert("投票失败！");
            }
        });
    }
    meetxq.selVoteUserList = function(name) {
        Common.getPostData('/meeting/selVoteUserList.do',meetxqData,function(rep){
            $('.meetiong-TPXQ-sk').html("选项："+ name);
            $(".meeting-TPXQ").show();
            $('.users').html('');
            Common.render({tmpl:$('#users_temp'),data:rep,context:'.users'});
        });
    }
    meetxq.checkInMeeting = function() {
        Common.getPostData('/meeting/checkInMeeting.do',meetxqData,function(rep){
            if (rep.flag) {
                alert("签到成功！");
                $('.meeting-past').hide();
                if ($('#show').val()=="1") {
                    $('.meeting-leave').show();
                }
            } else {
                alert("签到失败！");
            }
        });
    }
    meetxq.checkOutMeeting = function() {
        Common.getPostData('/meeting/checkOutMeeting.do',meetxqData,function(rep){
            if (rep.flag) {
                alert("签退成功！");
                window.location.href = "/meeting/meeting.do";
            } else {
                alert("签退失败！");
            }
        });
    }
    meetxq.selVoteDetail = function(type) {
        Common.getPostData('/meeting/selVoteDetail.do',meetxqData,function(rep){
            if (rep.rows.name!='') {
                if (type==0) {
                    $('.meetiongxq-JX').text("进行中");
                } else if (type==1) {
                    $('.meetiongxq-JX').text("结束");
                }
                if (rep.rows.choose=='') {
                    $('#voteUserName').text(rep.rows.userName);
                    $('#time').text(rep.rows.time);
                    $('#voteid').val(rep.rows.id);
                    $('#voteName').html(rep.rows.name);
                    $(".meetimgxq-right-I").hide();
                    $("#meetimgxq-right-II").show();
                    $("#meetimgxq-right-III").hide();
                    $('.vote-desc').html('');
                    Common.render({tmpl:$('#vote-desc_templ'),data:rep,context:'.vote-desc'});
                } else {
                    $('#voteUserName2').text(rep.rows.userName);
                    $('#time2').text(rep.rows.time);
                    $('#voteid').val(rep.rows.id);
                    $('#voteName2').html(rep.rows.name);
                    $(".meetimgxq-right-I").hide();
                    $("#meetimgxq-right-II").hide();
                    $("#meetimgxq-right-III").show();
                    $('.vote-desc2').html('');
                    Common.render({tmpl:$('#vote-desc_templ2'),data:rep,context:'.vote-desc2'});
                    $('#cnt').text("("+rep.rows.count+")");
                    $('.vote-desc2 li').click(function() {
                        meetxqData.chooseId = $(this).attr('cid');
                        meetxqData.voteId = $('#voteid').val();
                        meetxq.selVoteUserList($(this).attr('cnm'));
                    });
                }
            }
            $('.meetiongxq-JX').click(function(){
                $('.meetimgxq-right-I').show();
                $('.meetimgxq-right-II').hide();
            });
        });
    }
    meetxq.removeVoteInfo = function() {
        Common.getPostData('/meeting/removeVoteInfo.do',meetxqData,function(rep){
            if (rep.flag) {
                alert("删除成功！");
                meetxq.selMeetDetail(5);
            } else {
                alert("删除失败！");
            }
        });
    }
    meetxq.addSureIssue = function() {
        if ($.trim($('#issue').val())=='') {
            alert("请输入议题。");
            return;
        }
        meetxqData.content=$.trim($('#issue').val());
        Common.getPostData('/meeting/addIssue.do',meetxqData,function(rep){
            if (rep.flag) {
                alert("添加成功！");
                $('.YT-popup').hide();
                $('.zhiban-meng').hide();
                meetxq.selMeetDetail(6);
            } else {
                alert("添加失败！");
            }
        });
    }
    meetxq.addVoteInfo = function() {
        meetxqData.meetId=$('#mid').val();
        meetxqData.theme = $.trim($('#theme').val());
        var ans='';
        $('#chooselist input').each(
            function(i) {
                ans += $('#chooselist input').eq(i).val() + ',';
            }
        );

        if ($.trim($('#theme').val())=='') {
            alert("请输入投票主题。");
            return;
        }
        meetxqData.answers = ans;
        Common.getPostData('/meeting/addVoteInfo.do',meetxqData,function(rep){
            if (rep.flag) {
                alert("发起投票成功！");
                $(".zhiban-meng").hide();
                $(".meeting-FQTP").hide();
                meetxq.selMeetDetail(5);
            } else {
                alert("发起投票失败！");
            }
        });
    }
    meetxq.selChatLogList = function() {
        meetxqData.meetId=$('#mid').val();
        Common.getPostData('/meeting/selChatLogList.do',meetxqData,function(rep){
            if (rep.rows!=null) {
                for (var i=0;i<rep.rows.length;i++) {
                    $('.meeting-talk-ul').append("<li><span>"+rep.rows[i].userName+"<em>"+rep.rows[i].time+"</em></span><p>"+rep.rows[i].content+"</p></li>");
                }
            }
        });
    }
    meetxq.selMeetDetail = function(type) {
        meetxqData.meetId=$('#mid').val();
            Common.getPostData('/meeting/selMeetingDetail.do',meetxqData,function(rep){
                if (rep.rows!=null) {
                    if (type==1) {
                        $('#meetProcess').html(rep.rows.process);
                    } else if(type==2) {
                        $('#tab-FYRSX').html(rep.rows.order);
                    } else if(type==3) {
                        if (rep.rows.coursewareList!=null && rep.rows.coursewareList.length!=0) {
                            $('.upload-list').empty();
                            for(var i=0;i<rep.rows.coursewareList.length;i++) {
                                var href = rep.rows.coursewareList[i].value;
                                var fileKey = href.substring(href.lastIndexOf('/') + 1);
                                var fileName = rep.rows.coursewareList[i].type;
                                href = '/commonupload/doc/down.do?type=2&fileKey='+fileKey+'&fileName=' + fileName;
                                $('.upload-list').append("<li><span>" + rep.rows.coursewareList[i].type + "</span><div class='meeting-a'><a class='yulan' pth="+ rep.rows.coursewareList[i].value +">预览</a><i>|</i><a href="+href+" fnm="+fileName+" target='_blank'>下载</a></div></li>");
                            }
                            $('.yulan').click(function() {
                                var url = $(this).attr("pth");
                                var urlarg = url.split(".")[url.split(".").length-1];
                                if (urlarg=="doc"||urlarg=="docx"||urlarg=="xls"||urlarg=="xlsx"||urlarg=="ppt"||urlarg=="pptx") {
                                    window.open("http://ow365.cn/?i=9666&furl="+url);
                                } else {
                                    window.open(url);
                                }
                            });
                        }
                    } else if(type==4) {
                        if(rep.rows.checkUserList!=null && rep.rows.checkUserList.length!=0) {
                            $('.check-userlist').empty();
                            for(var i=0;i<rep.rows.checkUserList.length;i++) {
                                $('.check-userlist').append("<li uid=" + rep.rows.checkUserList[i].id + " unm=" + rep.rows.checkUserList[i].userName + "><img src='"+rep.rows.checkUserList[i].imgUrl+"'><span>" + rep.rows.checkUserList[i].userName + "</span></li>");
                            }
                        }
                        if(rep.rows.noCheckUserList!=null && rep.rows.noCheckUserList.length!=0) {
                            $('.nocheck-userlist').empty();
                            for(var i=0;i<rep.rows.noCheckUserList.length;i++) {
                                $('.nocheck-userlist').append("<li uid=" + rep.rows.noCheckUserList[i].id + " unm="+rep.rows.noCheckUserList[i].userName + "><img src='"+rep.rows.noCheckUserList[i].imgUrl+"'><span>" + rep.rows.noCheckUserList[i].userName + "</span></li>");
                            }
                            $(".check-userlist li").click(function(){
                                meetxqData.userId = $(this).attr("uid");
                                meetxq.selUserInfo();
                            });
                            $(".nocheck-userlist li").click(function(){
                                meetxqData.userId = $(this).attr("uid");
                                meetxq.selUserInfo();
                            });
                        }
                    } else if (type==5) {
                        $('.meeting-TP-dl').html('');
                        Common.render({tmpl:$('#meeting-TP-dl_templ'),data:rep,context:'.meeting-TP-dl'});
                        if (rep.rows.voteDTOList!=null && rep.rows.voteDTOList.length!=0) {
                            $('.meetimgxq-right-II').show();
                            meetxqData.voteId=rep.rows.voteDTOList[0].id;
                            meetxq.selVoteDetail(rep.rows.voteDTOList[0].status);
                        } else {
                            $(".meetimgxq-right-I").show();
                            $("#meetimgxq-right-II").hide();
                            $("#meetimgxq-right-III").hide();
                        }
                        $('.dd-vote').click(function() {
                            $('.meetimgxq-right-II').show();
                            meetxqData.voteId=$(this).attr('vid');
                            meetxq.selVoteDetail($(this).attr('stus'));
                        });
                    } else if (type==6) {
                        $('.YT-info').html('');
                        Common.render({tmpl:$('#YT-info_templ'),data:rep,context:'.YT-info'});
                        $('.YT-del').click(function() {
                            meetxqData.issueId=$(this).attr('sid');
                            Common.getPostData('/meeting/delIssue.do',meetxqData,function(rep){
                                if (rep.flag) {
                                    alert("删除成功！");
                                    meetxq.selMeetDetail(6);
                                } else {
                                    alert("删除失败！");
                                }
                            });
                        });

                    }
                }
            });

    }
    meetxq.selUserInfo = function() {
        Common.getPostData('/meeting/selUserInfo.do',meetxqData,function(rep){
            $('.meeting-lx-title').text("联系方式("+rep.name+")");
            $('#phone').text(rep.phone==null?"":rep.phone);
            $('#email').text(rep.email);
        });
    }
    meetxq.uploadFile = function() {
        $('#file_upload').uploadify({
            'swf': "/static/plugins/uploadify/uploadify.swf",
            'uploader': '/meeting/file/upload.do?meetId='+$('#mid').val(),
            'method': 'post',
            'buttonText': '',
            'fileTypeDesc': '',
            'fileSizeLimit': '300MB',
            'fileTypeExts': '*.*',
            'multi': true,
            'fileObjName': 'Filedata',
            'onUploadSuccess': function (file, response, result) {
                try {
                    var json = $.parseJSON(response);
                    if (json.flg) {
                        var href = json.vurl;
                        var fileKey = href.substring(href.lastIndexOf('/') + 1);
                        var fileName = json.name;
                        href = '/commonupload/doc/down.do?type=2&fileKey='+fileKey+'&fileName=' + fileName
                        $('.upload-list').append("<li><span>" + json.name + "</span><div class='meeting-a'><a class='yulan' pth="+json.vurl+">预览</a><i>|</i><a href="+json.vurl + " fnm="+fileName+" target='_blank'>下载</a></div></li>");
                        $('.yulan').click(function() {
                            var url = $(this).attr("pth");
                            var urlarg = url.split(".")[url.split(".").length-1];
                            if (urlarg=="doc"||urlarg=="docx"||urlarg=="xls"||urlarg=="xlsx"||urlarg=="ppt"||urlarg=="pptx") {
                                window.open("http://ow365.cn/?i=9666&furl="+url);
                            } else {
                                window.open(url);
                            }
                        });
                    } else {
                        someFileFailed = true;
                    }
                    /*关闭图片*/
                    //$(".meeting-a").click(function(event) {
                    //    $(this).parent().remove();
                    //});
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
            MessageBox("上传失败，请重试。", -1)
            someFileFailed = false;
        }
    }
    meetxq.init();
});