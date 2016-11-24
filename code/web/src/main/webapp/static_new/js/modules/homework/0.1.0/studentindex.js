/**
 * Created by fl on 2015/8/21.
 */

define(function(require, exports, module) {
    var Common = require('common');
    var stuIndex={};
    var option={};
    var classId;

    stuIndex.init = function() {




        var requestData = {};
        var date = new Date();
        Common.getData('/homework/student/section.do?time='+ date.getTime(), requestData, function (resp) {
            Common.render({
                tmpl: '#classSubjectTmpl',
                data: resp.classSubjectList,
                context: '#classSubject',
                overwrite: 1
            });
            sessionStorage.setItem("classId", resp.classId);
            sessionStorage.setItem("classType", resp.classType);
            classId = resp.classId;
        });
        sessionStorage.setItem("type", $('.hwtype.checkedType').attr('value'));//默认全部类型
        sessionStorage.setItem("subjectId", "000000000000000000000000");//默认全部学科
        sessionStorage.setItem("page",1);//默认显示第一页
        sessionStorage.setItem("term",0);//默认本学期
        sessionStorage.setItem("hwCon", 0);//默认全部内容

        getHomeWorkList();

        //选择科目
        $('body').on('click','.classSubject',function(){
            var subjectId = $(this).attr("subjectId");
            sessionStorage.setItem("subjectId", subjectId);
            sessionStorage.setItem("classType", $(this).attr("classType"));
            sessionStorage.setItem("classId", $(this).attr("classId"));
            sessionStorage.setItem("page",1);
            $(".checkedClass").removeClass("checkedClass");
            $(this).addClass("checkedClass");
            getHomeWorkList();

        });

        $("#qbbj").on('click', function(){
            sessionStorage.setItem("subjectId", "000000000000000000000000");
            sessionStorage.setItem("classType", 1);
            sessionStorage.setItem("classId", classId);
            sessionStorage.setItem("page",1);
            $(".checkedClass").removeClass("checkedClass");
            $(this).addClass("checkedClass");
            getHomeWorkList();
        })

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

        //点击进入作业详情页
        $('body').on('click','#detail',function(){
            //if($(this).attr("pg")==1) {
                var homeworkId = $(this).attr("value");
                var lessonId = $(this).attr("lessonId");
                sessionStorage.setItem("homeworkId", homeworkId);
                sessionStorage.setItem("lessonId", lessonId);
                sessionStorage.setItem("jnum", $(this).attr("jnum"));
                sessionStorage.setItem("pg",$(this).attr("pg"));
                sessionStorage.setItem("role",0);//学生
            sessionStorage.setItem("qianhoutype", $('body').attr('type'));
                Common.goTo("student/detail.do?lessonId=" + lessonId);
                //Common.goTo("/lesson/view.do?lessonId=" + lessonId);
            //}
        });


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




    }

    //作业列表
    function getHomeWorkList() {
        var requestData = {};
        requestData.page = sessionStorage.getItem("page");
        requestData.pageSize = 10;
        var classIds = "";
        $('.classSubject').each(function(){
            var classId = $(this).attr('classId');
            if(classIds.indexOf(classId) < 0){
                classIds +=  classId + ',';
            }
        });
        //requestData.classId = sessionStorage.getItem("classId");
        requestData.classId = classIds;
        requestData.type = sessionStorage.getItem("type");
        requestData.subjectId = sessionStorage.getItem("subjectId");
        requestData.term = sessionStorage.getItem("term");
        requestData.contentType = sessionStorage.getItem("hwCon");
        Common.getData('/homework/student/list.do', requestData, function (resp) {
            if(resp.rows.length>0) {
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
                                sessionStorage.setItem("page", sessionStorage.getItem("totalPage"));
                                getHomeWorkList();
                            }
                        })
                    }
                }
                initPaginator(option);
            } else {
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
        sessionStorage.setItem("totalPage", totalPage);
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




    module.exports = stuIndex;
});
