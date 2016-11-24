/**
 * @author 李伟
 * @module  成绩分析
 * @description
 * 成绩分析模块
 */
/* global Config */
define('headmastereclass',['jquery','doT','easing','common','pagination'],function(require,exports,module){
    /**
     *初始化参数
     */
    var headmastereclass = {},
        Common = require('common');
        require('pagination');
    var lanclassData = {};

    headmastereclass.init = function() {


        $("#gradeId").change(function(){
            //设置初始页码
            lanclassData.page = 1;
            //设置每页数据长度
            lanclassData.pageSize = 20;
            headmastereclass.getInteractLessonList();
        });

        $('#teaName').bind('keypress',function(event){
            if(event.keyCode == "13")
            {
                //设置初始页码
                lanclassData.page = 1;
                //设置每页数据长度
                lanclassData.pageSize = 20;
                headmastereclass.getInteractLessonList();
            }
        });

        $("#searchBtn").click(function(){
            //设置初始页码
            lanclassData.page = 1;
            //设置每页数据长度
            lanclassData.pageSize = 20;
            headmastereclass.getInteractLessonList();
        });
        //设置初始页码
        lanclassData.page = 1;
        //设置每页数据长度
        lanclassData.pageSize = 20;
        headmastereclass.getInteractLessonList();
    };

    headmastereclass.getInteractLessonList = function() {
        lanclassData.gradeId = $("#gradeId").val();
        lanclassData.teaName = $('#teaName').val();
        Common.getPostData('/interactLesson/getInteractLessonUseTeaList.do', lanclassData,function(rep){
            $('.sub-info-list').html('');
            if(rep.total>0) {
                Common.render({tmpl: $('#tea_templ'), data: rep, context: '.sub-info-list'});
                var totalPage = Math.ceil(rep.total / rep.pageSize) == 0 ? 1 : Math.ceil(rep.total / rep.pageSize);
                $('#pageDiv').jqPaginator({
                    totalPages: totalPage,//总页数
                    visiblePages: 6,//分多少页
                    currentPage: rep.page,//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        //点击页码的查询
                        //          	            	alert('当前第' + n + '页');
                        if (n != lanclassData.page) {
                            lanclassData.page = n;
                            headmastereclass.getInteractLessonList();
                        }
                    }
                });

                $(".searchDetail").click(function () {
                    var userId = $(this).attr('uid');
                    var url="/interactLesson/teaLanClass.do?userId="+userId+"&index=4&version=1";
                    Common.goTo(url);
                });
            }
        });
    }

    module.exports=headmastereclass;
});