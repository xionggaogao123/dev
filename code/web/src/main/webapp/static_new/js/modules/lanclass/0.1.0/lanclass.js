/**
 * @author 李伟
 * @module  成绩分析
 * @description
 * 成绩分析模块
 */
/* global Config */
define(['jquery','doT','easing','common','fancybox','echarts','initPaginator'],function(require,exports,module){
    /**
     *初始化参数
     */
    require("echarts");//引入插件
    var lanclass = {},
        Common = require('common'),
        Paginator = require('initPaginator');
    var lanclassData = {};
    var option={};
    lanclass.init = function() {


        $(".tab-col .tab-top ul li").click(function(){
            $(this).addClass("curl").siblings().removeClass("curl");
            lanclass.getInteractLessonList();
        });
        $(".stukq-back").click(function(){
            var url="/interactLesson/lanclass.do?index=4&version=1";
            Common.goTo(url);
        });
        var isStudentOrParent = $("#isStudentOrParent").val();
        if (isStudentOrParent) {
            lanclass.changeSubject();
        }
        //设置初始页码
        lanclassData.page = 1;
        //设置每页数据长度
        lanclassData.pageSize = 10;
        lanclass.getInteractLessonList();
    };

    lanclass.changeSubject=function(){
        $('.lanclass-list span') .click(function(){
            $(this).addClass("lanclass-kemu-hover").siblings().removeClass("lanclass-kemu-hover");
            //设置初始页码
            lanclassData.page = 1;
            //设置每页数据长度
            lanclassData.pageSize = 10;
            lanclass.getInteractLessonList();
        });
    }

    lanclass.getInteractLessonList = function() {
        lanclassData.csid = $('.curl').attr('cid');
        lanclassData.classid = $('.curl').attr('cid');
        lanclassData.subjectid = $('.lanclass-kemu-hover').attr("id");
        Common.getPostData('/interactLesson/getInteractLessonList.do', lanclassData,function(rep){
            $('.lanlesson').html('');
            if(rep.total>0){
                $(".lanclass-hov").hide();
                Common.render({tmpl: $('#lanlesson_templ'), data: rep.rows, context: '.lanlesson'});
                $(".lesimg").click(function(){
                    window.location.href="/interactLesson/lessonclass.do?lessonId=" + $(this).attr('ilid');
                })
                var isStudentOrParent = $("#isStudentOrParent").val();
                if (isStudentOrParent=='false') {
                    $(".lanclass-lock").click(function () {
                        lanclassData.lessonId = $(this).attr('ilid');
                        lanclassData.type = 2;
                        Common.getPostData('/interactLesson/editInteractLesson.do', lanclassData, function (rep) {
                            alert("已解锁！");
                            lanclass.getInteractLessonList();
                        });
                    });
                    $(".lanclass-Nkock").click(function () {
                        lanclassData.lessonId = $(this).attr('ilid');
                        lanclassData.type = 2;
                        Common.getPostData('/interactLesson/editInteractLesson.do', lanclassData, function (rep) {
                            alert("已上锁！");
                            lanclass.getInteractLessonList();
                        });
                    });
                    $(".lanclass-del").click(function () {
                        if (confirm("确定删除该条？")) {
                            lanclassData.lessonId = $(this).attr('ilid');
                            lanclassData.type = 3;
                            Common.getPostData('/interactLesson/editInteractLesson.do', lanclassData, function (rep) {
                                lanclass.getInteractLessonList();
                            });
                        }
                    });
                    $(".lanclass-edi").click(function () {
                        var dom = $(this).parents(".cours").find(".lanclass-in");
                        if (dom.attr("disabled") == "disabled") {
                            dom.removeAttr("disabled");
                            dom.css("border", "1px solid #dfdfdf");
                            var s = "/static_new/images/lanclass-true.png";
                            $(this).css("background", "url(" + s + ") no-repeat");
                        }else {
                            lanclassData.lessonId = $(this).attr('ilid');
                            lanclassData.type = 1;
                            lanclassData.lessonName = dom.val();
                            if($.trim(dom.val())==""){
                                alert("请输入互动课堂名称!");
                            }else{
                                Common.getPostData('/interactLesson/editInteractLesson.do', lanclassData, function (rep) {
                                    dom.attr("disabled", true);
                                    dom.css("border", "1px solid transparent").css("background", "white");
                                    var s = "/static_new/images/lanclass-edit.png";
                                    $(this).css("background", "url(" + s + ") no-repeat");
                                    lanclass.getInteractLessonList();
                                });
                            }
                        }
                    });
                }
            }else{
                $(".lanclass-hov").show();
            }
            option.total= rep.total;
            option.pagesize= rep.pageSize;
            option.currentpage=rep.page;
            option.operate=function (totalPage) {
                $('.page-index span').each(function () {
                    $(this).click(function(){
                        if(lanclassData.page!=parseInt($(this).text())){
                            lanclassData.page=$(this).text();
                            lanclass.getInteractLessonList();
                        }
                    });
                });
                $('.first-page').click(function(){
                    if(lanclassData.page!=1) {
                        lanclassData.page = 1;
                        lanclass.getInteractLessonList();
                    }
                });
                $('.last-page').click(function(){
                    if(lanclassData.page!=totalPage) {
                        lanclassData.page=totalPage;
                        lanclass.getInteractLessonList();
                    }
                })
            }
            Paginator.initPaginator(option);
            /*var option = {
                total: rep.total,
                pagesize: rep.pageSize,
                currentpage: rep.page,
                operate: function (totalPage) {
                    $('.page-index span').each(function () {
                        $(this).click(function(){
                            lanclassData.page=$(this).text();
                            lanclass.getInteractLessonList();
                        })
                    });
                    $('.first-page').click(function(){
                        lanclassData.page=1;
                        lanclass.getInteractLessonList();
                    });
                    $('.last-page').click(function(){
                        lanclassData.page=totalPage;
                        lanclass.getInteractLessonList();
                    })
                }
            }
            lanclass.initPaginator(option);*/
        });
    }
/*
    // 分页初始化
    lanclass.initPaginator=function (option) {
        var totalPage = '';
        $('.page-paginator').show();
        $('.page-index').empty();
        if (option.total % option.pagesize == 0) {
            totalPage = option.total / option.pagesize;
        } else {
            totalPage = parseInt(option.total / option.pagesize) + 1;
        }
        lanclass.buildPaginator(totalPage, option.currentpage);
        option.operate(totalPage);
    }

    lanclass.buildPaginator =function (totalPage, currentPage) {
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
    }*/

/*  function change_pic(){
    var imgObj = document.getElementById("bbb");
    var Flag=(imgObj.getAttribute("src",2)=="images/caocao.png")
    imgObj.src=Flag?"images/diaochan.png":"images/caocao.png";
  }*/

    lanclass.init();
});