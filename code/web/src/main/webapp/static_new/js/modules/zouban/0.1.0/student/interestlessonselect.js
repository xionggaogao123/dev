/**
 * Created by qiangm on 2015/8/20.
 */
'use strict';
define(['doT', 'common', 'jquery'], function (require, exports, module) {
    /**
     *初始化参数
     */
    require("jquery");
    require("doT");
    var lessonselect = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    lessonselect.init = function () {



    };
    //解析url参数
    function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return unescape(r[2]); return null; //返回参数值
    }

    $(document).ready(function () {
        $(".right-main1").hide();
        $(".tzxuanke_mian").show();
        $(".class-select").click(
            function () {
                $(".class-list").removeClass("tzxuanke_active");
                $(".class-select").addClass("tzxuanke_active");
                $(".right-main1").hide();
                $(".tzxuanke_mian").show();
            }
        );
        $(".class-list").click(
            function () {
                $(".class-list").addClass("tzxuanke_active");
                $(".class-select").removeClass("tzxuanke_active");
                $(".right-main1").show();
                $(".tzxuanke_mian").hide();
            }
        );
    });
    var advCount=0;
    var simCount=0;
    var adv=[];
    var sim=[];
    var xuankeId="";
    $(document).ready(function(){
        $("body").on("click",".adv",function(){
            var subjectId=$(this).parent().parent().attr("sid");
            var index=jQuery.inArray(subjectId,adv);
            if(index==-1) {//没有
                $(this).addClass("selected");
                adv.push(subjectId);
                var index2=jQuery.inArray(subjectId,sim);
                if(index2!=-1)
                {
                    sim.splice(index2, 1);
                    $(this).parent().parent().find(".cell3").find(".sim").removeClass("selected");
                }
            }
            $(window).bind('beforeunload', function () {
                return '选课未提交，确定离开此页面吗？';
            });
        });
        $("body").on("click",".sim",function(){
            var subjectId=$(this).parent().parent().attr("sid");
            var index=jQuery.inArray(subjectId,sim);
            if(index==-1) {//没有
                $(this).addClass("selected");
                sim.push(subjectId);
                var index2=jQuery.inArray(subjectId,adv);
                if(index2!=-1)
                {
                    adv.splice(index2, 1);
                    $(this).parent().parent().find(".cell2").find(".adv").removeClass("selected");
                }
            }
            $(window).bind('beforeunload', function () {
                return '选课未提交，确定离开此页面吗？';
            });
        });
        //提交选课
        $("body").on("click",".select-sub",function(){
            //if(advCount==adv.length && simCount==sim.length)
                $(".sub-div").show();
            //else
             //   alert("请选择"+advCount+"门等级考，"+simCount+"门合格考");
        });
        $("body").on("click",".win-cha,.sub-can",function(){
            $(".sub-div").fadeOut();
        });
        $("body").on("click",".unChoose",function()
        {
            var courseId=$(this).attr("zid");
            var result=Number("#result");
            if(result==0)
            {
                alert("不在选课时间范围内，只可查看选课结果");
                return;
            }
            lessonselect.addXuankeResult(courseId);
            //$(".sub-div").fadeOut();
        });
    });
    var year="";
    //将array转成字符串，用,分割
    function convertToStr(obj)
    {
        var str="";
        for(var i=0;i<obj.length;i++)
        {
            str+=obj[i]+",";
        }
        if(str.length>1)
            str=str.substring(0,str.length-1);
        return str;
    }
    //向后台提交选课结果
    lessonselect.addXuankeResult=function(courseId)
    {
        //var advStr=convertToStr(adv);
        //var simStr=convertToStr(sim);
        $.ajax({
            url:"/xuanke/zhuzhouStuXK.do",
            type:"post",
            dataType:"json",
            data:{
                year:year,
                courseId:courseId
            },
            success:function(data)
            {
                if(data.result=="success")
                {
                    //alert("选课成功！");
                    for(var i=0;i<choosedCourse.length;i++)
                    {
                        if(choosedCourse[i]==data.quit)
                        {
                            choosedCourse.splice(i,1);
                            break;
                        }
                    }
                    choosedCourse.push(courseId);
                    $(".tzxuanke_mian").empty();
                    Common.render({tmpl: $('#zhuZhouCourseJs'), data: {groupCourse: groupCourse,singleCourse:singleCourse,
                        choosedCourse:choosedCourse}, context: '.tzxuanke_mian'});
                    //$(window).unbind('beforeunload');
                    //lessonselect.getXuankeConf();
                }
                else
                {
                    alert("选课失败！原因："+data.reason);
                }
            }
        });
    };

    var choosedCourse=[];
    var singleCourse=[];
    var groupCourse=[];
    //获取选课配置
    lessonselect.getXuankeConf=function()
    {
        $.ajax({
            url:"/xuanke/zhuzhouXuanke.do",
            type:"post",
            dataType:"json",
            success:function(data)
            {
                year=data.year;
                var term=data.term;
                var result=data.result;
                if(Number(result)==2) {
                    choosedCourse = data.choosedCourse;
                    singleCourse = data.singleCourse;
                    groupCourse = [];
                    $.each(data.groupCourse, function (key, value) {
                        groupCourse.push({subjectName: key, list: value});
                    });
                    $(".tzxuanke_mian").empty();
                    Common.render({
                        tmpl: $('#zhuZhouCourseJs'), data: {
                            groupCourse: groupCourse, singleCourse: singleCourse,
                            choosedCourse: choosedCourse
                        }, context: '.tzxuanke_mian'
                    });
                }
                else if(Number(data.result)==0)
                {
                    //$(".select-sub").attr("display","none");
                    alert("不在选课时间内");
                }
                /*$("#result").val(data.result);
                var advChoose=[];
                var simChoose=[];
                sim=[];
                adv=[];
                for(var i=0;i<data.adv.length;i++)
                {
                    advChoose.push(data.adv[i].value);
                }
                for(var i=0;i<data.sim.length;i++)
                {
                    simChoose.push(data.sim[i].value);
                }
                for(var i=0;i<data.conf.subConfList.length;i++)
                {
                    if($.inArray(data.conf.subConfList[i].subjectName,advChoose)>-1)
                    {
                        adv.push(data.conf.subConfList[i].subjectId);
                    }
                }
                for(var i=0;i<data.conf.subConfList.length;i++)
                {
                    if($.inArray(data.conf.subConfList[i].subjectName,simChoose)>-1)
                    {
                        sim.push(data.conf.subConfList[i].subjectId);
                    }
                }
                advCount=data.conf.advanceCount;
                simCount=data.conf.simpleCount;
                xuankeId=data.conf.xuankeId;
                $(".right-main2").empty();
                Common.render({tmpl: $('#tempJs'), data: {data: data,advChoose:advChoose,simChoose:simChoose}, context: '.right-main2'});
                if(Number(data.result)==0)
                {
                    //$(".select-sub").attr("display","none");
                    alert("不在选课时间内，仅可查看选课结果，如需选课，请联系班主任");
                }*/
            }
        })
    };


    lessonselect.init();
    lessonselect.getXuankeConf();
});