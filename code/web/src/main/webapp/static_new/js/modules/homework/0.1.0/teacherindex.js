/**
 * Created by fl on 2015/8/17.
 */
define(['jquery','doT','easing','common','experienceScore'],function(require, exports, module) {
    var teaIndex={};
    var Common = require('common');
    var option={};
    var commonData=[];

    teaIndex.init = function() {




        var requestData = {};
        var date = new Date();
        Common.getData('/homework/teacher/section.do?time='+ date.getTime(), requestData, function (resp) {
            Common.render({
                tmpl: '#classSubjectTmpl',
                data: resp.classSubjectList,
                context: '#classSubject',
                overwrite: 1
            });
            sessionStorage.setItem("classType",resp.classType);

            //Common.render({
            //    tmpl: '#classListTmpl',
            //    data: resp,
            //    context: '#classList',
            //    overwrite: 1
            //});
        });
        sessionStorage.setItem("type", $('.hwtype.checkedType').attr('value'));//默认全部类型
        sessionStorage.setItem("classId", $("#classSubject span").first().attr("classId"));//默认第一个班级
        sessionStorage.setItem("className", $("#classSubject span").first().text());//默认第一个班级
        sessionStorage.setItem("subjectId", $("#classSubject span").first().attr("subjectId"));//默认第一个班级
        $("#classSubject span").first().addClass("checkedClass");
        sessionStorage.setItem("page", 1);//默认第一页
        sessionStorage.setItem("term", 0);//默认本学期
        sessionStorage.setItem("hwCon", 0);//默认全部内容
        sessionStorage.setItem("qianhoutype", $('body').attr('type'));
        getHomeWorkList();


        //选择班级
        $('body').on('click','.classSubject',function(){
            var classId = $(this).attr("classId");
            var subjectId = $(this).attr("subjectId");
            var className = $(this).attr("className");
            var classType = $(this).attr("classType");
            sessionStorage.setItem("classType", classType);
            sessionStorage.setItem("classId", classId);
            sessionStorage.setItem("subjectId", subjectId);
            sessionStorage.setItem("className", className);
            $(".checkedClass").removeClass("checkedClass");
            $(this).addClass("checkedClass");
            sessionStorage.setItem("page",1);
            getHomeWorkList();

        });

        //选择类型
        $('body').on('click','.hwtype',function(){
            var homeworkType = $(this).attr("value");
            sessionStorage.setItem("type", homeworkType);
            $(this).siblings(".checkedType").removeClass("checkedType");
            $(this).addClass("checkedType");
            //alert(homeworkType);
            sessionStorage.setItem("page",1);
            getHomeWorkList();
        });

        //选择内容
        $('body').on('click','.hwcon',function(){
            var homeworkCon = $(this).attr("value");
            sessionStorage.setItem("hwCon", homeworkCon);
            $(this).siblings(".checkedType").removeClass("checkedType");
            $(this).addClass("checkedType");
            sessionStorage.setItem("page",1);
            getHomeWorkList();
        });

        //选择学期
        $('body').on('click','.hwterm',function(){
            sessionStorage.setItem("term", $(this).attr("value"));
            $(".checkedTerm").removeClass("checkedTerm");
            $(this).addClass("checkedTerm");
            sessionStorage.setItem("page",1);
            getHomeWorkList();
        });

        //新建作业
        $(".he-ba-ri").on('click', function(){
            //var subjectId = sessionStorage.getItem("subjectId");
            //if(null == subjectId) {
            //    alert("请先选择一个班级（科目）");
            //    return;
            //}
            //$(".homework-bg").show();
            //$(".homework-popup").show();
            sessionStorage.setItem("newHW",1);
            var requestData = {};
            requestData.title = "";
            requestData.content = "";
            requestData.subjectId = sessionStorage.getItem("subjectId");
            requestData.type = 4;
            requestData.voicefile = "";
            if(requestData.subjectId == "undefined"){
                alert("您没有教任何班级，无法新建作业");
                return false;
            }
            Common.getData('/homework/add.do', requestData, function (resp) {
                if(resp.code == '500') {
                    alert("发布失败!");
                } else {
                    /*if (resp.score) {
                        scoreManager(resp.scoreMsg, resp.score);
                    }*/
                    sessionStorage.setItem("homeworkId", resp.message);
                    var lessonId = resp.lessonId;
                    Common.goTo("teacher/edit.do?lessonId=" + lessonId);
                }
            });
        })


        //发布
        $("#fb").on('click', function(){
            if($(".popup-te").val().trim() == "") {
                alert("请输入作业名称");
                return;
            }
            var requestData = {};
            requestData.title = $(".popup-te").val().trim();
            if(requestData.title.length > 50){
                alert("标题应不多于50个字符！");
                return;
            }
            requestData.content = $("#con").val();
            var classIdList=$("input:checkbox:checked").map(function(index,elem) {
                return "0," + $(elem).val() + ",1";
            }).get().join(';');
            requestData.classIdList = classIdList;
            //requestData.voicefile = null;
            requestData.subjectId = sessionStorage.getItem("subjectId");
            requestData.type = $("input:radio:checked").val();
            if(requestData.classIdList=="" || requestData.type==null){
                alert("请完善信息！")
                return;
            }
            var voicefile = $('.voice').attr('url');
            requestData.voicefile = voicefile;
            Common.getData('/homework/add.do', requestData, function (resp) {
                //alert(resp.code + "===" + resp.message);
                if(resp.code == '500') {
                    alert("发布失败!");
                } else {
                    /*if (resp.score) {
                        scoreManager(resp.scoreMsg, resp.score);
                    }*/
                    $(".popup-te").val("");
                    sessionStorage.setItem("homeworkId", resp.message);
                    var lessonId = resp.lessonId;
                    Common.goTo("teacher/edit.do?lessonId=" + lessonId);
                }
            });
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

        //批改进阶练习
        $("body").on('click','.bar',  function(){
            if($("#qbbj").hasClass('checkedClass')){
                alert("请到具体班级下查看统计！");
                return;
            }
            var exerciseId = $(this).attr("exerciseId");
            var classId = sessionStorage.getItem("classId");
            window.open('/exam/answer/stat/list.do?id=' + exerciseId + '&type=1' + '&classId=' +classId);
        })

        //查看统计
        $("body").on('click','.trash, .process',  function(){
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
        $("body").on('click','.see',  function(){
            var homeworkId = $(this).attr("value");
            var lessonId = $(this).attr("lessonId");
            sessionStorage.setItem("homeworkId", homeworkId);
            sessionStorage.setItem("lessonId", lessonId);
            sessionStorage.setItem("newHW",0);
            //alert(lessonId);
            Common.goTo("teacher/edit.do?lessonId=" + lessonId);
        })

        //删除
        $("body").on('click','.del',  function(){
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
                                getHomeWorkList();
                            }
                        });
                    }
                });
            }
        })

        //点击进入作业详情页
        $('body').on('click','.edit',function(){
           if($("#qbbj").hasClass('checkedClass')){
                alert("请到具体班级下查看作业详情！");
                return;
            }
            var homeworkId = $(this).attr("value");
            var i = $(this).attr("index");
            sessionStorage.setItem("homeworkId", homeworkId);
            sessionStorage.setItem("items", JSON.stringify(commonData[i]));
            window.open("teacher/detail.do")
        });
        $('body').on('click','.hwtitle',function(){
            //var homeworkId = $(this).attr("value");
            //sessionStorage.setItem("homeworkId", homeworkId);
            //Common.goTo("teacher/detail.do")
            var homeworkId = $(this).attr("value");
            var lessonId = $(this).attr("lessonId");
            var i = $(this).attr("index");
            sessionStorage.setItem("homeworkId", homeworkId);
            sessionStorage.setItem("lessonId", lessonId);
            sessionStorage.setItem("pg",0);
            sessionStorage.setItem("role",1);//老师
            sessionStorage.setItem("items", JSON.stringify(commonData[i]));
            Common.goTo("teacher/detail1.do?lessonId=" + lessonId);
        });


    }

    //作业列表
     function getHomeWorkList() {
         var requestData = {};
         requestData.page = sessionStorage.getItem("page");
         requestData.pageSize = 10;
         requestData.classId = sessionStorage.getItem("classId");
         requestData.type = sessionStorage.getItem("type");
         requestData.subjectId = sessionStorage.getItem("subjectId");
         requestData.term = sessionStorage.getItem("term");
         requestData.contentType = sessionStorage.getItem("hwCon");
         Common.getData('/homework/teacher/list.do', requestData, function (resp) {
             if (resp.rows.length > 0) {//有作业
                 commonData = resp.rows;
                 $("#nohomework").hide();
                 $("#homeworkList").show();
                 Common.render({
                     tmpl: '#homeworkListTmpl',
                     data: resp.rows,
                     context: '#homeworkList',
                     overwrite: 1
                 });

                 option.total = resp.total;
                 option.pagesize = resp.pageSize;
                 option.currentpage = resp.page;
                 if (true) {
                     option.operate = function (totalPage) {
                         $('.page-index span').each(function () {
                             $(this).click(function () {
                                 if (requestData.page != $(this).text()) {
                                     //requestData.page=$(this).text();
                                     sessionStorage.setItem("page", $(this).text());
                                     getHomeWorkList();
                                 }
                             });
                         });
                         $('.first-page').click(function () {
                             if (requestData.page != 1) {
                                 //requestData.page = 1;
                                 sessionStorage.setItem("page", 1);
                                 getHomeWorkList();
                             }
                         });
                         $('.last-page').click(function () {
                             if (requestData.page != totalPage) {
                                 //requestData.page = totalPage;
                                 sessionStorage.setItem("page", totalPage);
                                 getHomeWorkList();
                             }
                         })
                     }
                 }
                 initPaginator(option);
            } else {//没作业
                 $("#nohomework").show();
                 $("#homeworkList").hide();
                 $(".page-paginator").hide();
             }
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

    //新建作业
    function createNewHomework() {
        var data = {};
        data.name = "sc321";
        Common.getData('/lesson/createclasslesson.do', data, function (resp) {
            alert(resp.code + "===" + resp.message);
        });
    }

    function getVideo() {
        var data = {};
        data.lessonId = "54f57a5cf6f28b7261cd0f4e";
        //Common.getData('/lesson/getviewlesson.do', data, function (resp) {
        //    alert(resp.name + "===" + resp.videoCount);
        //});
    }


    module.exports = teaIndex;
});
