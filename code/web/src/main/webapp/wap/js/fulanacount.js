/**
 * Created by James on 2017/11/10.
 */
'use strict'
define(['doT', 'common','easing', 'jquery','pagination'],function(require, exports, module){
    var fulannAcount = {};
    var Common = require('common');
    require('pagination');
    var page = 1;
    var page2 = 1;
    var isInit = true;
    var isInit2 = true;
    fulannAcount.init = function(){
        fulannAcount.getHuoEntry();
        fulannAcount.getIntList();
        $("body").on("change","#slist",function(){
            var number = $("#slist").val();
;            fulannAcount.getResultList(page2,number);
        })
        var number = $("#slist").val();
        fulannAcount.getResultList(page2,number);
    }
    fulannAcount.getResultList = function(page2,number){
        var paramData = {};
        paramData.lessonId = $("#id").val();
        paramData.number = number;
        paramData.page = page2;
        paramData.pageSize = 5;
        paramData.type = 1;
        Common.getPostData('/jxmapi/smallLesson/selectAnswerList.do',paramData,function(rep){
            if(rep.code==200){
                var result = rep.message.list;
                var count = rep.message.count;
                $("#it3").html("");
                $('.new-page-links2').show();
                if(result.length>0){
                    $('.new-page-links2').jqPaginator({
                        totalPages: Math.ceil( count / paramData.pageSize) == 0 ? 1 : Math.ceil(count / paramData.pageSize),//总页数
                        visiblePages: 5,//分多少页
                        currentPage: parseInt(page2),//当前页数
                        first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                        prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                        next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                        last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                        page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                        onPageChange: function (n) { //回调函数
                            if (isInit2) {
                                isInit2 = false;
                            } else {
                                if(n!=page2) {
                                    page2=n;
                                    var number = $("#slist").val();
                                    fulannAcount.getResultList(n,number);
                                }
                            }
                        }
                    });
                    Common.render({tmpl:$('#it3_teml'),data:rep,context:'#it3'});
                }else{
                    alert("抱歉，没有找到您要查找的数据!");
                    $('.new-page-links2').hide();
                }
            }
        })
    }


    fulannAcount.getIntList = function(){
        var paramData = {};
        paramData.lessonId = $("#id").val();
        Common.getPostData('/jxmapi/smallLesson/selectIntList.do',paramData,function(rep){
            if(rep.code==200){
                var htm = "";
                var result = rep.message;
                if(result.length >0){
                    for(var i = 0;i<result.length;i++){
                        htm += "<option value='"+result[i]+"'>第"+result[i]+"次答题</option>";
                    }
                }else{
                    alert("抱歉没有找到答题信息列表")
                    $("#answerList").hide();
                }
                $("#slist").html(htm);
            }
        })
    }
    fulannAcount.getHuoEntry = function(id){
        var paramData = {};
        paramData.page = page;
        paramData.pageSize = 10;
        //paramData.dateTime = "";
        paramData.lessonId = $("#id").val();
        Common.getPostData('/jxmapi/smallLesson/selectUserResultList.do',paramData,function(rep){
            if(rep.code==200){
                //alert(rep);
                var result = rep.message.dto1;
                var count2 = rep.message.count;
                var all = rep.message.all;
                $("#all").html("（班级平均活跃度："+all/count2+"）");
                $("#it1").html("");
                $("#it2").html("");
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
                                    fulannAcount.getHuoEntry(n);
                                }
                            }
                        }
                    });
                    Common.render({tmpl:$('#it1_teml'),data:rep,context:'#it1'});
                    Common.render({tmpl:$('#it2_teml'),data:rep,context:'#it2'});
                    /*$('.ul-moving img').map(function(){
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

                    });*/
                }else{
                    alert("抱歉，没有找到您要查找的数据!");
                    $('.new-page-links').hide();
                }

            }
        })
    }




    fulannAcount.init();


})
