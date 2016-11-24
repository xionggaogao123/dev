/**
 * Created by fl on 2015/8/21.
 */

define(['jquery','common','fancybox'],function(require, exports, module) {
    var Common = require('common');
    require('fileupload');
    require('fancybox');
    var stuDetail={};
    var option={};
    var tty = 1;

    stuDetail.init = function() {



        changeHref();

        if(!IsPC()){
            $('.take-photo').hide();
        }

        //学生提交作业列表
        sessionStorage.setItem("page",1);//默认第一页
        sessionStorage.setItem("compage",1);//默认显示讨论第一页
        if(sessionStorage.getItem("pg")==1 && sessionStorage.getItem("role")!=1){//需要老师批改
            $("#hwdetail").show();
            $("#wt").show();
            getStuSubmitList();
        } else {
            $("#hwdetail").hide();
            $("#wt").hide();
            $(".detail-bottom").show();
            $("#tl").addClass("detail-cur");
            //获得讨论列表
            getStuCommentList();
        }


        var requestData = {};
        requestData.hwid = sessionStorage.getItem("homeworkId");
        Common.getData('/homework/detail.do', requestData, function (resp) {
            $("#hwtitle").text(resp.title);
            $("#teaName").text(resp.userName);
            $("#date").text("于"+ resp.time + "发表");
            var reg=new RegExp("\n","g");
            var content = resp.content.replace(reg,"<br/>");
            $("#con").html(content);
            $("#photo").attr("src",resp.userAvatar);
                Common.render({
                    tmpl: '#teaVoiceTmpl',
                    data: resp.voiceFile,
                    context: '#teaVoice',
                    overwrite: 1
                });

            //查询lesson相关信息
            var requestData = {};
            requestData.lessonId = resp.lessonId;
            sessionStorage.setItem("lessonId", resp.lessonId);
            Common.getData('/lesson/getviewlesson.do?time=' + new Date(), requestData, function (resp) {
                sessionStorage.setItem("isParent", resp.isParent);
                if(resp.exercise){
                    tty = resp.exercise.value1;
                }
                if (sessionStorage.getItem("pg") == 1 && sessionStorage.getItem("role")!=1) {
                    if (resp.isParent == 0) {
                        $("#dohomework").show();
                        $("#reply_section").show();
                    }
                }
                if (resp.exerciseStat == 1 && resp.isParent == 0) {
                    $("#doexercise").attr("exerciseId", resp.exerciseId);
                    var jnum = sessionStorage.getItem("jnum");
                    if(jnum>0){
                        $("#doexercise").text("去做进阶练习(共"+jnum+"题)");
                    }
                    $("#doexercise").show();
                    sessionStorage.setItem("exerciseId", resp.exerciseId);
                } else if (resp.exerciseStat == 2) {
                    $("#doexercise").attr("exerciseId", resp.exerciseId);
                    $("#getAnswer").show();
                    sessionStorage.setItem("exerciseId", resp.exerciseId);
                }

            });
        });
        if(sessionStorage.getItem("role") == 1){
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
        }




        //点击去做作业
        $("#dohomework").on('click', function() {
            $(".detail-bottom-I").show();
            $(".detail-bottom-II").show();
            $(".detail-bottom").hide();
            $("#wt").addClass("detail-cur")
            $("#tl").removeClass("detail-cur");
            //获得作业列表
            getStuSubmitList()
            window.location.href = "#end";
            $("#textarea").focus();

        })

        $("#FH").on('click', function(){
            if(sessionStorage.getItem("role")==1){//老师
                Common.goTo("/homework/teacher.do?type=" + sessionStorage.getItem("qianhoutype"));
            } else {
                Common.goTo("/homework/student.do?type=" + sessionStorage.getItem("qianhoutype"));
            }

        })


        //点击去做进阶练习
        $("#doexercise").on('click', function(){
            var wordexerciseId = sessionStorage.getItem("exerciseId");
            var lessonId = sessionStorage.getItem("lessonId");
            window.location.href="/exam/view.do?id="+wordexerciseId+"&type=1&lesson="+lessonId+"&homework=1&tty=" + tty;
        })

        //查看进阶练习结果
        $("#getAnswer").on('click', function(){
            var wordexerciseId = sessionStorage.getItem("exerciseId");
            window.location.href="/exam/view.do?id="+wordexerciseId+"&type=2";
        })

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


        //Common.fileUpload('#file_attach','/homework/uploadattach.do','#fileuploadLoading',function(e,data){
        //    if (data.dataType == 'iframe ') {
        //        var response = $('pre', data.result).text();
        //    } else {
        //        var response = data.result;
        //    }
        //    try {
        //        var ob = response;
        //        if (ob.uploadType == 0) {
        //            alert("附件上传失败！");
        //        }
        //        sessionStorage.setItem("realname", ob.realname);
        //        sessionStorage.setItem("filename", data.files[0].name);
        //        var uf = $('<p style="padding:5px 0;cursor:pointer;" class="uploadedFiles" realname="' + ob.realname + '">' + data.files[0].name + '</p>').hover(function () {
        //            if ($(this).find('img').length == 0) {
        //                $(this).append("<a style=\"margin-left:20px;\" onclick=\"$(this).closest('p').remove();\"><img src=\"/img/dustbin.png\" /></a>");
        //
        //            } else {
        //                $(this).find('img').show();
        //            }
        //        }, function () {
        //            $(this).find('img').hide();
        //        });
        //        $("#reply_section").append(uf);
        //    } catch (err) {}
        //});


        //提交作业
        $("#submit").on('click', function(){
            var requestData = {};
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
                //var localname = '拍摄照片' + i;
                flist += $('.img-photo-take').eq(i).attr('realname') + ',' + $('.img-photo-take').eq(i).attr('textname') + ';';
            }
            requestData.filenamelist = flist;
            requestData.voicefile = $('.voice').attr('url');
            Common.getData('/homework/submit.do', requestData, function (resp) {
                if(resp.code == '200') {
                    getStuSubmitList();
                }
            });

            $('#upload-photo-container').empty();
            $("#textarea").val("");
        })


        //添加评论
        $("#tjpl").on('click', function(){
            var requestData = {};
            requestData.lessonId = sessionStorage.getItem("lessonId");
            requestData.comment = $("#comment").val();
            if(requestData.comment == ""){
                alert("内容不能为空！");
                return;
            }
            Common.getData('/lesson/comment.do', requestData, function (resp) {
                if(resp.code == '200') {
                    $("#comment").val("");
                    getStuCommentList();
                }
            });
        })

        //删除评论
        $("body").on('click','.deleteCM', function(){
            var msg = "您确定要删除吗？";
            if (confirm(msg) == true) {
                var requestData = {};
                requestData.lessonId = sessionStorage.getItem("lessonId");
                requestData.userId = $(this).attr('userId');
                requestData.time = $(this).attr('time');
                Common.getData('/lesson/deleteComment.do', requestData, function (resp) {
                    if (resp.code == '200') {
                        getStuCommentList();
                    }
                });
            }
        })

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


        $(".detail-info-left a").click(function(){
            $(this).addClass("detail-cur").siblings().removeClass("detail-cur");
        })
        $("#wt").click(function(){
            $(".detail-bottom-I").show();
            $(".detail-bottom-II").show();
            $(".detail-bottom").hide();

            //获得作业列表
            getStuSubmitList()
        })
        $("#tl").click(function(){
            $(".detail-bottom-I").hide();
            $(".detail-bottom-II").hide();
            $(".detail-bottom").show();
            //获得讨论列表
            getStuCommentList();
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
        //作业列表
        function getStuSubmitList() {
            var requestData = {};
            requestData.hwid = sessionStorage.getItem("homeworkId");
            requestData.classId = sessionStorage.getItem("classId");
            requestData.page = sessionStorage.getItem("page");
            requestData.pageSize = 10;
            requestData.classType = sessionStorage.getItem("classType");
            var date = new Date();
            Common.getData('/homework/submit/list.do?time='+ date.getTime(), requestData, function (resp) {
                if(resp.rows.length>0){
                    $(".page-paginator").show();
                } else {
                    $(".page-paginator").hide();
                }
                Common.render({
                    tmpl: '#hwdetailTmpl',
                    data: resp.rows,
                    context: '#hwdetail',
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
            });
            if (sessionStorage.getItem("isParent") == 1) {//家长
                $("#reply_section1").hide();
                $("#detail-bottom").hide();
            }
        }

        //讨论列表
        function getStuCommentList() {
            var requestData = {};
            requestData.lessonId = sessionStorage.getItem("lessonId");
            requestData.limit = 10;
            requestData.compage = sessionStorage.getItem("compage");
            requestData.skip = (sessionStorage.getItem("compage")-1)*requestData.limit;
            var date = new Date();
            Common.getData('/lesson/comment/list.do?time='+ date.getTime(), requestData, function (resp) {
                if(resp.rows.length>0){
                    $(".page-paginator").show();
                } else {
                    $(".page-paginator").hide();
                }
                Common.render({
                    tmpl: '#commentListTmpl',
                    data: resp.rows,
                    context: '#commentList',
                    overwrite: 1
                });

                option.total= resp.total;
                option.pagesize= requestData.limit;
                option.currentpage= requestData.compage;
                if(true){
                    option.operate=function (totalPage) {
                        $('.page-index span').each(function () {
                            $(this).click(function(){
                                if(requestData.compage!=$(this).text()){
                                    //requestData.page=$(this).text();
                                    sessionStorage.setItem("compage",$(this).text());
                                    getStuCommentList();
                                }
                            });
                        });
                        $('.first-page').click(function(){
                            if(requestData.compage!=1) {
                                //requestData.page = 1;
                                sessionStorage.setItem("compage",1);
                                getStuCommentList();
                            }
                        });
                        $('.last-page').click(function(){
                            if(requestData.compage!=totalPage) {
                                //requestData.page = totalPage;
                                sessionStorage.setItem("compage",totalPage);
                                getStuCommentList();
                            }
                        })
                    }
                }
                initPaginator(option);
            });
            $(".detail-bottom-I").hide();
            $(".detail-bottom").show();
            if (sessionStorage.getItem("isParent") == 1) {//家长
                $("#reply_section1").hide();
                $("#detail-bottom").hide();
            }
            if(sessionStorage.getItem("role")==1){
                $('.delete').show();
            }
        }

        // 拍照
        webcam.set_quality(90);
        webcam.set_shutter_sound(true);
        webcam.set_hook('onComplete', 'uploadPhoto');

        $('.not-found-close').on('click', function() {
            $('.not-found-camera').hide();
            $('.alert-bg').hide();
        });

        $('.take-photo').on('click', function() {
            $('.alert-bg').show();
            $('.take-photo-container').show();
            $('.take-photo-btn').show();
        });

        $('.take-photo-btn').on('click', function() {
            webcam.freeze();
            $('.take-photo-btn').hide();
            $('.take-photo-again').show();
            $('.take-photo-upload').show();
        });
        $('.take-photo-again').on('click', function() {
            webcam.reset();
            resetPhoto();
        });

        $('.take-photo-upload').on('click', function() {
            webcam.dump();
            $('.photo-close').click();
        });

        $('.photo-close').on('click', function() {
            $('.alert-bg').hide();
            $('.take-photo-container').hide();
            resetPhoto();
        });

        $('#upload-photo-container').on('click', '.delete-photo', function() {
            $(this).parent().remove();
        });

        function resetPhoto() {
            $('.take-photo-btn').show();
            $('.take-photo-again').hide();
            $('.take-photo-upload').hide();
        }

        function uploadPhoto(base64) {
            var target = $('#upload-photo-container');
            src = 'data:image/jpeg;base64,' + base64;
            $.ajax({
                url: '/commonupload/base64image.do',
                type: 'post',
                dataType: 'json',
                data: {
                    base64ImgData: base64
                },
                success: function(data) {
                    console.log(data);
                    target.append('<div class="upload-photo-img"><img class="img-photo-take" src="' + src + '" realname="' + data.path + '" textname="'+data.name+'"><img class="delete-photo" src="/img/error-grey.png"></div>');
                },
                error: function() {
                    console.log('convertImages error');
                }
            });
            webcam.reset();
        }

        $(".detail-info-left a").click(function(){
            $(this).addClass("detail-cur").siblings().removeClass("detail-cur");
        })
        $(".wt").click(function(){
            $(".detail-bottom-I").show();
            $(".detail-bottom").hide();
        })
        $(".tl").click(function(){
            $(".detail-bottom-I").hide();
            $(".detail-bottom").show();
        })

        $("a.fancybox").fancybox();

        //上传附件
        $('#file_attach').fileupload({
            url: '/homework/uploadattach.do',
            start: function(e) {
                $('#fileuploadLoading').show();
            },
            done: function(e, data) {
                if (data.dataType == 'iframe ') {
                    var response = $( 'pre', data.result ).text();
                } else {
                    var response = data.result;
                }
                try {
                    var ob = response;
                    if (ob.uploadType == 0) {
                        alert("附件上传失败！");
                    }
                    sessionStorage.setItem("realname",ob.realname);
                    sessionStorage.setItem("filename",data.files[0].name);
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
                } catch (err) {
                }
            },
            fail: function (e, data) {

            },
            always: function (e, data) {
                $('#fileuploadLoading').hide();
            }
        });


    }

    // 分页初始化
    initPaginator=function (option) {
        var totalPage = '';
        //$('.page-paginator').show();
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

    function checkImage(str) {
        var ext = str.substring(str.length-3,str.length);
        var rule = /(jpg|gif|bmp|png)/;
        if(rule.test(ext)) {
            return true;
        }else {
            return false;
        }
    }

    function IsPC(){
        var userAgentInfo = navigator.userAgent;
        var Agents = new Array("Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod");
        var flag = true;
        for (var v = 0; v < Agents.length; v++) {
            if (userAgentInfo.indexOf(Agents[v]) > 0) { flag = false; break; }
        }
        return flag;
    }

    function changeHref(){
        $('#docList').find('a').each(function(index, ele){
            var href = $(ele).attr('href');
            if(href != null){
                var fileKey = href.substring(href.lastIndexOf('/') + 1);
                var fileName = $(this).siblings('span').text() + href.substring(href.lastIndexOf('.'));
                //alert(href + '   ' + fileKey + '   ' + fileName);

                href = '/commonupload/doc/down.do?type=2&fileKey='+fileKey+'&fileName=' + fileName
                $(ele).attr('href', href)
            }

        })
    }





    module.exports = stuDetail;
});
