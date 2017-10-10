/**
 * Created by James on 2017/10/10.
 */
'use strict';
define(['doT', 'common','easing', 'jquery','pagination'], function (require, exports, module) {
    var fulanIndex = {};
    var Common = require('common');
    require('pagination');
    var page = 1;
    var isInit = true;
    fulanIndex.init = function(){
        fulanIndex.getList(page);
        $("body").on("click",".sp-x",function(){
            if(confirm("是否删除该课程？")){
                var id = $(this).attr("name");
                fulanIndex.delEntry(id);
            }
        })
        $("body").on("click",".sp-e",function(){
            var id = $(this).attr("name");
            fulanIndex.updateEntry(id);
        })
        $("body").on("click",".attr",function(){
            var userId = $("#userId").val();
            var userName = $("#userName").val();
            var id = $(this).attr("name");
            window.location.href = "/changeLesson/description.do?userId="+userId+"&userName="+userName+"&id="+id;
        })

    }
    fulanIndex.delEntry = function(id){
        var paramData = {};
        paramData.lessonId = id;
        Common.getPostData("/smallLesson/delLessonEntry.do",paramData,function(rep){
            if(rep.code==200){
                alert("删除成功！");
            }
        })
    }
    fulanIndex.getEntry = function(id){
        var paramData = {};
        paramData.lessonId = id;
        Common.getPostData("/smallLesson/delLessonEntry.do",paramData,function(rep){
            if(rep.code==200){
                alert("删除成功！");
            }
        })
    }

    fulanIndex.updateEntry = function(id,name){
        var paramData = {};
        paramData.lessonId = id;
        paramData.name = name;
        Common.getPostData("/smallLesson/updateLessonName.do",paramData,function(rep){
            if(rep.code==200){
                alert("修改成功！");
            }
        })
    }


    fulanIndex.getList = function(page){
        var paramData = {};
        paramData.page = page;
        paramData.pageSize = 5;
        //paramData.dateTime = "";
        paramData.userId = $("#userId").val();
        Common.getPostData('/smallLesson/selectLessonList.do',paramData,function(rep){
            if(rep.code==200){
                //alert(rep);
                var result = rep.message.rows;
                var count2 = rep.message.count;
                $("#studentList").html("");
                $('.new-page-links').show();
                if(result.length>0){
                    $('.new-page-links').jqPaginator({
                        totalPages: Math.ceil( count2 / paramData.pageSize) == 0 ? 1 : Math.ceil(count2 / paramData.pageSize),//总页数
                        visiblePages: 5,//分多少页
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
                                if(n!=page) {
                                    page=n;
                                    fulanIndex.getList(n);
                                }
                            }
                        }
                    });
                    Common.render({tmpl:$('#studentList_templ'),data:rep,context:'#studentList'});
                    $('.ul-moving img').map(function(){
                        var $wid = $(this).width();
                        var $hig = $(this).height();
                        var $bi = $wid/$hig;
                        var ml = -($wid/$hig*180-180)/2;
                        var mt = -($hig/$wid*180-180)/2;
                        if($bi>=1){
                            $(this).css({'height':'200px','marginLeft':ml+'px'})
                        }else{
                            $(this).css({'width':'200px','marginTop':mt+'px'})
                        }

                    });
                }else{
                    alert("抱歉，没有找到您要查找的数据!");
                    $('.new-page-links').hide();
                }

            }
        })
    }
    fulanIndex.init();

})
