/**
 * Created by fl on 2015/8/21.
 */

define(['jquery','doT','common'],function(require, exports, module) {
    var Common = require('common');
    var teaDetail={};
    var option={};

    teaDetail.init = function() {




        var requestData = {};
        requestData.hwid = sessionStorage.getItem("homeworkId");
        //alert(requestData.hwid);
        Common.getData('/homework/detail.do', requestData, function (resp) {
            $("#hwtitle").text(resp.title);
            $("#teaName").text(resp.userName);
            $("#date").text("于"+ resp.time + "发表");
            var reg=new RegExp("\n","g");
            var content = resp.content.replace(reg,"<br/>");
            $("#con").html(content);
            $("#photo").attr("src",resp.userAvatar);
        });

        var string = sessionStorage.getItem("items");
        var obj = JSON.parse(string);
        if(obj){
            Common.render({
                tmpl: '#itemsTmpl',
                data: obj,
                context: '#items',
                overwrite: 1
            });
        }

        //学生提交作业列表
        sessionStorage.setItem("page",1);//默认显示第一页
        getStuSubmitList();

        //批改进阶练习
        $("body").on('click','.reverse-II',  function(){
            if($("#qbbj").hasClass('checkedClass')){
                alert("请到具体班级下查看统计！");
                return;
            }
            var exerciseId = $(this).attr("exerciseId");
            var classId = sessionStorage.getItem("classId");
            window.open('/exam/answer/stat/list.do?id=' + exerciseId + '&type=1' + '&classId=' +classId);
        })

        //查看统计
        $("body").on('click','.reverse-III',  function(){
            if($("#qbbj").hasClass('checkedClass')){
                alert("请到具体班级下查看统计！");
                return;
            }
            var lessonId = $(this).attr("lessonId");
            var classId = sessionStorage.getItem("classId");
            //Common.goTo('/lesson/stat.do?lessonId=' + lessonId);
            var homeworkId = $(this).attr("value");
            sessionStorage.setItem("homeworkId", homeworkId);
            sessionStorage.setItem("pg",$(this).attr("pg"));
            window.open('/lesson/stat.do?lessonId=' + lessonId + '&classId=' + classId);
        })

        //编辑
        $("body").on('click','.reverse-IV',  function(){
            var homeworkId = $(this).attr("value");
            var lessonId = $(this).attr("lessonId");
            sessionStorage.setItem("homeworkId", homeworkId);
            sessionStorage.setItem("lessonId", lessonId);
            sessionStorage.setItem("newHW",0);
            //alert(lessonId);
            Common.goTo("/homework/teacher/edit.do?lessonId=" + lessonId);
        })

        //删除
        $("body").on('click','.reverse-V',  function(){
            var msg = "您确定要删除吗？";
            if (confirm(msg) == true) {
                var requestData = {};
                var homeworkId = $(this).attr("value");
                var lessonId = $(this).attr("lessonId");
                requestData.hwid = homeworkId;
                requestData.lessonId = lessonId;
                Common.getData('/homework/remove.do', requestData, function (resp) {
                    if (resp.code == "200") {
                        Common.getData('/lesson/remove.do', requestData, function (resp) {
                            if (resp.code == "200") {
                                alert("删除成功");
                                Common.goTo("/homework/teacher.do");
                            }
                        });
                    }
                });
            }
        })

        //点击进入作业详情页
        $('body').on('click','.reverse-I',function(){
            if($("#qbbj").hasClass('checkedClass')){
                alert("请到具体班级下查看作业详情！");
                return;
            }
            var homeworkId = $(this).attr("value");
            var i = $(this).attr("index");
            sessionStorage.setItem("homeworkId", homeworkId);
            window.open("/homework/teacher/detail.do")
        });

        //删除提交的作业
        $("body").on('click','.deleteHW', function(){
            var msg = "您确定要删除吗？";
            if (confirm(msg) == true) {
                var requestData = {};
                requestData.homeworkId = sessionStorage.getItem("homeworkId");
                requestData.studentId = $(this).attr('userId');
                requestData.time = $(this).attr('time');
                Common.getData('/homework/deleteStuSubmitHW.do', requestData, function (resp) {
                    if (resp.code == '200') {
                        getStuSubmitList();
                    }
                });
            }
        })


        //if($('#file_attach')) {
        //    $('#file_attach').fileupload({
        //        url: '/homework/uploadattach.do',
        //        start: function (e) {
        //            $('#fileuploadLoading').show();
        //        },
        //        done: function (e, data) {
        //            if (data.dataType == 'iframe ') {
        //                var response = $('pre', data.result).text();
        //            } else {
        //                var response = data.result;
        //            }
        //            try {
        //                var ob = response;
        //                if (ob.uploadType == 0) {
        //                    alert("附件上传失败！");
        //                }
        //                sessionStorage.setItem("realname", ob.realname);
        //                sessionStorage.setItem("filename", data.files[0].name);
        //                var uf = $('<p style="padding:5px 0;cursor:pointer;" class="uploadedFiles" realname="' + ob.realname + '">' + data.files[0].name + '</p>').hover(function () {
        //                    if ($(this).find('img').length == 0) {
        //                        $(this).append("<a style=\"margin-left:20px;\" onclick=\"$(this).closest('p').remove();\"><img src=\"/img/dustbin.png\" /></a>");
        //
        //                    } else {
        //                        $(this).find('img').show();
        //                    }
        //                }, function () {
        //                    $(this).find('img').hide();
        //                });
        //                $("#reply_section").append(uf);
        //            } catch (err) {
        //            }
        //        },
        //        fail: function (e, data) {
        //
        //        },
        //        always: function (e, data) {
        //            $('#fileuploadLoading').hide();
        //        }
        //    });
        //}

        //返回
        $("#back").on('click', function(){
            Common.goTo("/homework/teacher.do?type=" + sessionStorage.getItem('qianhoutype'));
        })




        //作业列表
        function getStuSubmitList() {
            var requestData = {};
            requestData.hwid = sessionStorage.getItem("homeworkId");
            requestData.classId = sessionStorage.getItem("classId");
            requestData.page = sessionStorage.getItem("page");
            requestData.classType = sessionStorage.getItem("classType");
            requestData.pageSize = 10;
            Common.getData('/homework/submit/list.do', requestData, function (resp) {
                Common.render({
                    tmpl: '#hwReplyTmpl',
                    data: resp.rows,
                    context: '#hwReply',
                    overwrite: 1
                });

                option.total= resp.total;
                option.pagesize= resp.pageSize;
                option.currentpage=resp.page;
                if(true){
                    option.operate=function (totalPage) {
                        $('.page-index span').each(function () {
                            $(this).click(function(){
                                if(requestData.page!=$(this).text()){
                                    //requestData.page=$(this).text();
                                    sessionStorage.setItem("page",$(this).text());
                                    getStuSubmitList();
                                }
                            });
                        });
                        $('.first-page').click(function(){
                            if(requestData.page!=1) {
                                //requestData.page = 1;
                                sessionStorage.setItem("page",1);
                                getStuSubmitList();
                            }
                        });
                        $('.last-page').click(function(){
                            if(requestData.page!=totalPage) {
                                //requestData.page = totalPage;
                                sessionStorage.setItem("page",totalPage);
                                getStuSubmitList();
                            }
                        })
                    }
                }
                initPaginator(option);
                if(resp.rows.length>0){
                    $(".page-paginator").show();
                } else {
                    $(".page-paginator").hide();
                }

            });
        }

        //涂鸦修改
        $('body').on('click', '.hw-submit-img.teacher-check', function () {
            var pUrl = $(this).attr('src');
            var imageId = $(this).attr('data-id');
            //var pUrl = "/upload/homework/homework-55fa2faa0cf2992555d916eb.jpg";
            var requestData = {};
            requestData.imageUrl = pUrl;
            Common.getData('/homework/getImage.do',requestData,function(resp){
                var picUrl;
                if(resp.code == 200){
                    picUrl = resp.url;
                } else {
                    picUrl = pUrl;
                }
                checkPrintPos();
                $('#check-hw-container').show();
                $('.alert-bg').show();
                var so = new FlashObject("/static/plugins/pictureEditor/pictureEditor.swf", "flashApp", "1000px", "700px", "8");
                so.addVariable("picUrl", picUrl);
                so.addVariable("uploadUrl", "/homework/saveeditedimage.do?imageId=" + imageId);
                so.addParam("wmode", "transparent");
                so.write("check-hw-container");
                $('#check-hw-container').append('<div class="close-check-hw"></div>');
            })
        });
        //$('body').on('click', '.hw-submit-img.teacher-check', function () {
        //    var pUrl = $(this).attr('src');
        //    var imageId = $(this).attr('data-id');
        //    checkPrintPos();
        //    $('#check-hw-container').show();
        //    $('.alert-bg').show();
        //    var picUrl = pUrl;
        //    var so = new FlashObject("/static/plugins/pictureEditor/pictureEditor.swf", "flashApp", "1000px", "700px", "8");
        //    so.addVariable("picUrl", picUrl);
        //    so.addVariable("uploadUrl", "/homework/saveeditedimage.do?imageId=" + imageId);
        //    so.addParam("wmode", "transparent");
        //    so.write("check-hw-container");
        //    $('#check-hw-container').append('<div class="close-check-hw"></div>');
        //});
        $('body').on('click', '.close-check-hw', function () {
            $('#check-hw-container').empty().hide();
            $('.alert-bg').hide();
            $('#fullbg').hide();
        });


        //批阅
        $('body').on('click', '#PY', function(){
            var requestData = {};
            var button = $(this);
            requestData.studentId = $(this).attr("studentId");
            requestData.time = $(this).attr("time");
            requestData.homeworkId = sessionStorage.getItem("homeworkId");
            Common.getData('/homework/correct.do', requestData, function (resp) {
                if(resp) {
                    button.text("已批阅");
                }
            });
        })

        //弹出回复框
        $('body').on('click', '.reverse-HF', function(){
            sessionStorage.setItem("studentId", $(this).attr("studentId"));
            sessionStorage.setItem("time", $(this).attr("time"));
            $(".detail-SJ").show();
            $(".detail-SJJ").show();
            $(".detail-popup").show();
        })

        //回复
        $("#submit").on('click', function(){
            var requestData = {};
            requestData.studentId = sessionStorage.getItem("studentId");
            requestData.time = sessionStorage.getItem("time");
            var content = $("#textarea").val();
            if(content.trim()==""){
                alert("请输入回复内容");
                return;
            }
            requestData.content = content;
            requestData.hwId = sessionStorage.getItem("homeworkId");
            requestData.classId = sessionStorage.getItem("classId");
            var flist = '';
            $('.uploadedFiles').each(function() {
                var localname = $(this).text();
                flist += $(this).attr('realname') + ',' + localname + ';';
            });
            for (var i = 0; i < $('.img-photo-take').length; i++) {
                flist += $('.img-photo-take').eq(i).attr('realname') + ',' + $('.img-photo-take').eq(i).attr('textname') + ';';
            }
            requestData.filenamelist = flist;
            requestData.voicefile = $('.voice').attr('url');
            Common.getData('/homework/reply.do', requestData, function (resp) {
                alert("回复成功");
                $(".detail-SJ").hide();
                $(".detail-SJJ").hide();
                $(".detail-popup").hide();
                getStuSubmitList();
            });

        })
        //取消回复
        $("#quit").on('click', function(){
            $(".detail-SJ").hide();
            $(".detail-SJJ").hide();
            $(".detail-popup").hide();
        })

        //录音
        var foo = true;
        $(".a12").on('click', function(){
            showflash('#recordercontainer1');
        })
        function showflash(container) {
            var mc = new FlashObject("/static/plugins/audiorecorder.swf", "recorderApp", "350px", "23px", "8");
            mc.setAttribute("id", "recorderApp");
            mc.setAttribute("name", "recorderApp");

            mc.addVariable("uploadAction", "/homework/uploadvoice.do");
            mc.addVariable("fileName", "audio");
            mc.addVariable("recordTime", 10 * 60 * 1000);
            mc.addVariable("appName", "recorderApp");
            mc.write("myContent");
            if (foo) {
                $(container).append($('#recorder'));
                $('#recorder .sanjiao').show();
                $("#myContent").show();
                foo = false;
            } else {
                $("#myContent").hide();
                $('#recorder .sanjiao').hide();
                foo = true;
            }
        }
        function checkPrintPos() {
            var whigh = document.documentElement.clientHeight;
            var wwidth = document.documentElement.clientWidth;
            if (wwidth > 900) {
                $('#check-hw-container').css({
                    'top': (whigh - 700) / 2,
                    'left': (wwidth - 1000) / 2
                });
            }
        }


        //上传附件
        Common.fileUpload('#file_attach','/homework/uploadattach.do','#fileuploadLoading',function(e,data){
            if (data.dataType == 'iframe ') {
                var response = $('pre', data.result).text();
            } else {
                var response = data.result;
            }
            try {
                var ob = response;
                if (ob.uploadType == 0) {
                    alert("附件上传失败！");
                }
                sessionStorage.setItem("realname", ob.realname);
                sessionStorage.setItem("filename", data.files[0].name);
                var uf = $('<p style="padding:5px 0;cursor:pointer;" class="uploadedFiles" realname="' + ob.realname + '">' + data.files[0].name + '</p>').hover(function () {
                    if ($(this).find('img').length == 0) {
                        $(this).append("<a style=\"margin-left:20px;\" onclick=\"$(this).closest('p').remove();\"><img src=\"/img/dustbin.png\" /></a>");

                    } else {
                        $(this).find('img').show();
                    }
                }, function () {
                    $(this).find('img').hide();
                });
                $("#reply_section").append(uf);
            } catch (err) {}
        });

    }

    // 分页初始化
    initPaginator=function (option) {
        var totalPage = '';
        $('.page-paginator').show();
        $('.page-index').empty();
        if (option.total % option.pagesize == 0) {
            totalPage = option.total / option.pagesize;
        } else {
            totalPage = parseInt(option.total / option.pagesize) + 1;
        }
        buildPaginator(totalPage, option.currentpage);
        option.operate(totalPage);
    }
    buildPaginator =function (totalPage, currentPage) {
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



    module.exports = teaDetail;
});
