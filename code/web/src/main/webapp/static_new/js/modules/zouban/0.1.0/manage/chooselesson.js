/**
 * Created by qiangm on 2015/10/20.
 */
'use strict';
define(['doT', 'common', 'jquery'],function (require, exports, module) {
    /**
     *初始化参数
     */
    require("jquery");
    require("doT");
    var chooselesson = {},
        Common = require('common');
    var chooselessonData = {};
    chooselessonData.type = 1;
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    chooselesson.init = function () {



    };

    $(document).ready(function(){
        $(".backUrl").click(
            function () {
                window.open('../paike/index.do?version=58&year='+ encodeURI(encodeURI($('#term').text()))+'&gradeId='+
                $('#gradeShow').attr("gid"),'_self');
            });
        //设置分段
        $("body").on("click",".tab-schedule-SZ",function(){
            window.location.href='/zouban/classrule.do?version=58&term='+$('#term').text()+'&gid='+$('#gradeShow').attr('gid')+'&gnm='+ $('#gradeShow').text();
        });
        //调整学生选课
        $("body").on("click",".tab-schedule-TZ",function(){
            chooselesson.getClassList();
            $(".tab-schedule").css("display","none");
            $(".tab-Sschedule").css("display","block");
            $("#term3").html($("#term").html());
            $("#grade3").html($("#gradeShow").text());
            chooselesson.studentXuanKeList();
        });
        //调整学生返回
        $("body").on("click","#backBtn",function(){
            $(".tab-schedule").css("display","block");
            $(".tab-Sschedule").css("display","none");
        });
        //明细
        $("body").on("click",".viewdetail",function(){
            $(".tab-schedule").css("display","none");
            $(".tab-Xschedule").css("display","block");
            $("#term2").html($("#term").html());
            $("#grade2").html($("#gradeShow").text());
            chooselesson.getClassList();
            $('#subjectmaps').val($(this).attr('sid'));
            chooselesson.xuanKeSubjectDetail();
        });
        //明细返回
        $("body").on("click","#backBtn2",function(){
            $(".tab-schedule").css("display","block");
            $(".tab-Xschedule").css("display","none");
        });
        //调整选课
        $("body").on("click",".TZxuanke",function()
        {
            /*$(".tab-Sschedule").css("display","block");
            $(".tab-Xschedule").css("display","none");*/
            $(".bg").show();
            $(".schedule-CUR").show();
            $("#userId").val($(this).attr("uid"));
            chooselessonData.studentName=$(this).parent().children().eq(1).text();
            chooselessonData.className=$(this).parent().children().eq(3).text();
            //$("#stuName").text($(this).parent().children().eq(1).text());
            //$("#stuName").html("222");
            chooselesson.getConf();
        });
        $("body").on("click",".xuankeTd",function(){
            $(".bg").show();
            $(".schedule-CUR").show();
            $("#userId").val($(this).attr("uid"));
            chooselessonData.studentName=$(this).parent().children().eq(0).text();
            chooselessonData.className=$(this).parent().children().eq(1).text();
            //$("#stuName").text($(this).parent().children().eq(0).text());
            //$("#stuName").html("222");
            chooselesson.getConf();
        });
        //关闭弹窗
        $("body").on("click",".update-X,.schedule-CUR-BU",function(){
            $(".bg").hide();
            $(".schedule-CUR").hide();
        });

        $('body').on('change', '#gradeShow', function () {
            chooselesson.findXuanKeConf();
        });

        chooselesson.findXuanKeConf();
        //$("#seachxk").click(function(){
        //    chooselesson.findXuanKeConf();
        //});
        $('#seachuser').click(function(){
            chooselesson.studentXuanKeList();
        });
    });

    chooselesson.getClassList = function() {
        chooselessonData.gradeId = $("#gradeShow").attr('gid');
        Common.getPostData('/course/getClassList.do', chooselessonData,function(rep){
            $("#classlist").empty();
            Common.render({tmpl: $('#classlistTempJs'), data: {data: rep}, context: '#classlist'});
            $("#classlist2").empty();
            Common.render({tmpl: $('#classlistTempJs2'), data: {data: rep}, context: '#classlist2'});
        });
    }

    chooselesson.findXuanKeConf = function() {
        chooselessonData.term = $("#term").html();
        chooselessonData.gradeId = $("#gradeShow").attr('gid');
        chooselessonData.type=1;

        Common.getPostData('/zouban/findXuanKeConf.do', chooselessonData,function(rep){
            $("#startdt").html(rep.startDate);
            $("#enddt").html(rep.endDate);
            $("#xkcnt").html(rep.xuankecount);
            $("#xuankeid").val(rep.xuankeId);
            chooselessonData.xuankeId=rep.xuankeId;
            $("#subjectlist").empty();
            $("#subjectmaps").empty();
            Common.render({tmpl: $('#subjectConfTempJs'), data: {data: rep.subConfList}, context: '#subjectlist'});
            Common.render({tmpl: $('#subjectmapTempJs'), data: {data: rep.subConfList}, context: '#subjectmaps'});
        });
    }

    chooselesson.xuanKeSubjectDetail = function(){
        chooselessonData.subjectId=$("#subjectmaps").val();
        chooselessonData.classId=$("#classlist").val();
        Common.getPostData('/zouban/xuanKeSubjectDetail.do', chooselessonData,function(rep){
            $("#stulist").empty();
            Common.render({tmpl: $('#stulistTempJs'), data: {data: rep.sublist}, context: '#stulist'});
        });
    }

    chooselesson.studentXuanKeList = function() {
        chooselessonData.choose=$("#choosetype").val();
        chooselessonData.classId=$("#classlist2").val();
        chooselessonData.userName=$("#username").val();
        chooselessonData.xuankeId=$("#xuankeid").val();
        Common.getPostData('/zouban/studentXuanKeList.do', chooselessonData,function(rep){
            $("#subjectlist2").empty();
            Common.render({tmpl: $('#subjectConfTempJs2'), data: {data: rep.rows}, context: '#subjectlist2'});
        });
    }


    chooselesson.init();
    //===========================
    $(document).ready(function(){
        $("body").on("click","#getXuankeDetail",function()
        {
            chooselessonData.xuankeId=$("#xuankeid").val();
            chooselessonData.subjectId=$("#subjectmaps").val();
            chooselessonData.type=1;
            chooselessonData.classId=$("#classlist").val();
            Common.getPostData('/zouban/xuanKeSubjectDetail.do', chooselessonData,function(rep){
                $("#stulist").empty();
                Common.render({tmpl: $('#stulistTempJs'), data: {data: rep.sublist}, context: '#stulist'});
            });
        });
        //提交选课结果、
        $("body").on("click",".submitXuanke",function()
        {
            /*if(advList.length!=chooselessonData.advCount || simList.length!=chooselessonData.simCount)
            {
                alert("请选择"+chooselessonData.advCount+"门等级考，"+chooselessonData.simCount+"门合格考");
                return;
            }*/

            chooselessonData.advance=convertToStr(advList);
            chooselessonData.simple=convertToStr(simList);
            chooselessonData.stuId=$("#userId").val();
            chooselessonData.stuName=chooselessonData.studentName;
            chooselessonData.xuankeId=$("#xuankeid").val();
            Common.getPostData('/xuanke/teacherXK.do', chooselessonData,function(rep){
                if(rep=="200")
                {
                    alert("选课成功！");
                    chooselesson.getConf();
                }
                else
                {
                    alert("选课失败！");
                }
            });
        });
        $("body").on("click",".advcourse",function()
        {
            if($(this).text()=="已选")
            {
                $(this).removeClass("schedule-CUR-hov");
                $(this).text("我选");
                advList.splice($.inArray($(this).parent().parent().attr("sid"),advList),1);
            }
            else
            {
                $(this).addClass("schedule-CUR-hov");
                $(this).text("已选");
                advList.push($(this).parent().parent().attr("sid"));
                var index2=jQuery.inArray($(this).parent().parent().attr("sid"),simList);
                if(index2!=-1)
                {
                    simList.splice(index2, 1);
                    $(this).parent().parent().children().eq(2).find("span").removeClass("schedule-CUR-hov");
                    $(this).parent().parent().children().eq(2).find("span").text("我选");
                }
            }
        });
        $("body").on("click",".simcourse",function()
        {
            if($(this).text()=="已选")
            {
                $(this).removeClass("schedule-CUR-hov");
                $(this).text("我选");
                simList.splice($.inArray($(this).parent().parent().attr("sid"),simList),1);
            }
            else
            {
                $(this).addClass("schedule-CUR-hov");
                $(this).text("已选");
                simList.push($(this).parent().parent().attr("sid"));
                var index2=jQuery.inArray($(this).parent().parent().attr("sid"),advList);
                if(index2!=-1)
                {
                    advList.splice(index2, 1);
                    $(this).parent().parent().children().eq(1).find("span").removeClass("schedule-CUR-hov");
                    $(this).parent().parent().children().eq(1).find("span").text("我选");
                }
            }
        });
        $("body").on("click",".autoXuanke",function(){
            chooselesson.autoXuanke();
        });
    });
    var advList=[];
    var simList=[];
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
    chooselesson.getConf=function()
    {
        Common.getPostData('/xuanke/getXuankeResultAdmin.do', {userId:$("#userId").val()},function(rep){
            $("#tableShow").empty();
            var advChoose=[];
            var simChoose=[];
            for(var i=0;i<rep.adv.length;i++)
            {
                advChoose.push(rep.adv[i].value);
            }
            for(var i=0;i<rep.sim.length;i++)
            {
                simChoose.push(rep.sim[i].value);
            }
            advList=[];
            for(var i=0;i<rep.conf.subConfList.length;i++)
            {
                if($.inArray(rep.conf.subConfList[i].subjectName,advChoose)>-1)
                {
                    advList.push(rep.conf.subConfList[i].subjectId);
                }
            }
            simList=[];
            for(var i=0;i<rep.conf.subConfList.length;i++)
            {
                if($.inArray(rep.conf.subConfList[i].subjectName,simChoose)>-1)
                {
                    simList.push(rep.conf.subConfList[i].subjectId);
                }
            }
            chooselessonData.advCount=rep.conf.advanceCount;
            chooselessonData.simCount=rep.conf.simpleCount;
            Common.render({tmpl: $('#tempJs'), data: {data: rep,advChoose:advChoose,simChoose:simChoose,
                stuName:chooselessonData.studentName,className:chooselessonData.className}, context: '#tableShow'});
        });
    };
    //模拟选课
    chooselesson.autoXuanke=function()
    {
        $.ajax({
            url:"/xuanke/autoXuanke.do",
            type:"post",
            dataType:"json",
            data:{
                gradeId:$("#gradeShow").attr('gid'),
                xuankeId:$("#xuankeid").val()
            },
            success:function(data)
            {
                if(data.result=="SUCCESS")
                {
                    chooselesson.findXuanKeConf();
                }
                else{
                    alert("自动选课失败");
                }
            }
        })
    }
});