/**
 * Created by qiangm on 2015/8/20.
 */
'use strict';
define(['doT', 'common', 'jquery'],function (require, exports, module) {
    /**
     *初始化参数
     */
    require("jquery");
    require("doT");
    var chargeteacher = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    chargeteacher.init = function () {



    };
    $(function(){
        $(".right-title  li").click(function(){
            $(this).addClass("cur").siblings("li").removeClass("cur");
            var index=$(this).index();
            if(index==0)
            {
                if(classTable.course.length==0)
                {
                    chargeteacher.getClassTable(Number($("#weekShow").val()));
                }
            }
            else if(index==1)
            {
                if(subjectList.length==0)
                {
                    chargeteacher.getSubjectList();
                }
            }
            else if(index==2)
            {
                chargeteacher.findXuanKeConf();
            }
            else if(index==3)
            {
                chargeteacher.xuankeGongshi();
            }
        })
    });

    $(document).ready(function($) {
        $("body").on("click",".main1-li",function() {
            $(".right-main1").show();
            $(".right-main2").hide();
            $(".right-main3").hide();
            $(".right-main4").hide();
            $(".main3a").hide();
        });
        $("body").on("click",".main2-li",function() {
            $(".right-main2").show();
            $(".right-main1").hide();
            $(".right-main3").hide();
            $(".right-main4").hide();
            $(".main3a").hide();
        });
        $("body").on("click",".main3-li",function() {
            $(".right-main3").show();
            $(".right-main2").hide();
            $(".right-main1").hide();
            $(".right-main4").hide();
            $(".main3a").hide();
        });
        $("body").on("click",".main4-li",function() {
            $(".right-main4").show();
            $(".right-main2").hide();
            $(".right-main3").hide();
            $(".right-main1").hide();
            $(".main3a").hide();
        });
        $("body").on("click",".hide1-x",function() {
            $(".main1-hide").hide();
            $(".main3-hide2").hide();
            $(".bg").hide();
        });
        $("body").on("click",".check-list",function() {
            $(".main3a").show();
            $(".right-main3").hide();
            chargeteacher.getMyclassStuChoose();
        });
        $("body").on("click",".main3a-1",function() {
            $(".main3a").hide();
            $(".right-main3").show();
        });
        $("body").on("click",".tab-choose",function() {
            $(".schedule-CUR").show();
        });
        $("body").on("click",".main3-hide .hide3-1 i",function() {
            $(".main3-hide").hide();
            $(".bg").hide();
        });
        $("body").on("click"," .btn-qx",function() {
            $(".main3-hide").hide();
            $(".main3-hide2").hide();
            $(".bg").hide();
        });
    });



    chargeteacher.init();
    //===========================课表=============================

    //获取本班学生
    chargeteacher.getStudentList=function()
    {
        $.ajax({
            url:"/timetable/getStudentList.do",
            type:"post",
            success:function(data)
            {
                $("#gradeId").val(data.gradeId);
                $("#classId").val(data.classId);
                $("#stuList").empty();
                Common.render({tmpl: $('#stuTemJs'), data: {data: data.list}, context: '#stuList'});
                chargeteacher.getWeekList();
            }
        });
    };
    //获取教学周列表
    chargeteacher.getWeekList=function()
    {
        var weekList=[];
        for(var i=1;i<=Number($("#allweek").val());i++)
        {
            if(i==Number($("#curweek").val()))
            {
                weekList.push({id:i,name:"第"+i+"周(本周)"});
            }
            else {
                weekList.push({id: i, name: "第" + i + "周"});
            }
        }
        $("#weekShow").empty();
        Common.render({tmpl: $('#weekShowTempJs'), data: {data: weekList}, context: '#weekShow'});
        $("#weekShow").val(Number($("#curweek").val()));
        chargeteacher.getClassTable(Number($("#curweek").val()));
        chargeteacher.getClassNoticeList();
    };
    chargeteacher.getStudentList();
    var detailWeek=[];
    var detailTime=[];
    var classDetail=[{
        className:"",
        people:"",
        teacherName:"",
        classRoom:"",
        myClassAmount:""
    }];
    var showType=3;//全部
    var courseData={};
    $(document).ready(function(){
        $("body").on("click","#getTimetable",function(){
            var stuId=$("#stuList").val();
            if(stuId=="0")
            {
                chargeteacher.getClassTable(Number($("#weekShow").val()));
            }
            else{
                chargeteacher.getStudentTable(stuId,Number($("#weekShow").val()));
            }
            chargeteacher.getClassNoticeList();
        });
        $("body").on("click","#exportTable",function(){
            var stuId=$("#stuList").val();
            var stuName=$("#stuList").find("option:selected").text();
            if(stuId=="0")
            {
                var gradeId=$("#gradeId").val();
                var classId=$("#classId").val();
                window.location='/timetable/exportAllStu.do?term='+encodeURI(encodeURI($("#termVal").val()))+'&year=' +
                encodeURI(encodeURI($("#yearVal").val()))+
                '&gradeId='+gradeId+'&classId='+classId+"&type=0&week="+Number($("#weekShow").val());
            }
            else{
                stuName=encodeURI(stuName);
                stuName=encodeURI(stuName);
                window.location='/timetable/exportStu.do?term='+encodeURI(encodeURI($("#termVal").val()))+'&stuId='+stuId+'&stuName='+stuName+'&week='+Number($("#weekShow").val());
            }
        });
        //走班详情
        $("body").on("click",".zb",function() {
            $(".main1-hide").show();
            $(".bg").show();
            var select_x=Number($(this).attr("x"));
            var select_y=Number($(this).attr("y"));
            chargeteacher.getDetailHead(select_x,select_y);
            chargeteacher.getDetail(select_x,select_y,Number($("#weekShow").val()));
        });
        //走班详情
        $("body").on("click",".tyzb",function() {
            $(".main1-hide").show();
            $(".bg").show();
            var select_x=Number($(this).attr("x"));
            var select_y=Number($(this).attr("y"));
            chargeteacher.getDetailHead(select_x,select_y);
            chargeteacher.getPhysicalDetail(select_x,select_y,Number($("#weekShow").val()));
        });
        //课时变化
        $("body").on("click","#classDetailShow",function(){
            var yIndex=Number($(this).val());
            $(".i2").text(detailTime[yIndex-1]);
        });
        $("body").on("click",".select-btn",function()
        {
            var select_x=Number($("#weekshow").val());
            var select_y=Number($("#classDetailShow").val());
            chargeteacher.getDetail(select_x,select_y,Number($("#weekShow").val()));
        });
        //走班非走班变化
        $("body").on("change",".showType",function(){
            showType=Number($(this).val());
            var stuId=$("#stuList").val();
            if(stuId=="0")
            {
                $(".main1-3").empty();
                Common.render({tmpl: $('#classTableTempJs'), data: {data: classTable,type:showType}, context: '.main1-3'});
                //chargeteacher.getClassTable();
            }
            else{
                $(".main1-3").empty();
                Common.render({tmpl: $('#tempJs'), data: {data: courseData,type:showType}, context: '.main1-3'});
            }
        });
    });
    chargeteacher.getDetail=function(select_x,select_y,week)
    {
        $.ajax(
            {
                url:"/timetable/getDetailList.do",
                type:"post",
                dataType:"json",
                data:{
                    term:$("#termVal").val(),
                    classId:$("#classId").val(),
                    xIndex:select_x,
                    yIndex:select_y,
                    type:0,
                    week:week
                },
                success:function(data){
                    classDetail=data;
                    $("#detailShow").empty();
                    Common.render({tmpl: $('#detailTempJs'), data: {data: classDetail}, context: '#detailShow'});
                    $(".room").show();
                }
            }
        );
    };
    chargeteacher.getPhysicalDetail=function(select_x,select_y,week)
    {
        $.ajax(
            {
                url:"/timetable/getPhysicalDetailList.do",
                type:"post",
                dataType:"json",
                data:{
                    term:$("#termVal").val(),
                    classId:$("#classId").val(),
                    xIndex:select_x,
                    yIndex:select_y,
                    type:0,
                    week:week
                },
                success:function(data){
                    classDetail=data;
                    $("#detailShow").empty();
                    Common.render({tmpl: $('#detailTempJs'), data: {data: classDetail}, context: '#detailShow'});
                    $(".room").hide();
                }
            }
        );
    };
    chargeteacher.getDetailHead=function(select_x,select_y)
    {
        $.ajax(
            {
                url:"/timetable/getDetailHead.do",
                type:"post",
                data:{
                    term:$("#yearVal").val(),
                    gradeId:$("#gradeId").val()
                },
                success:function(data){
                    detailWeek=data.classDays;
                    detailTime=data.classTime;

                    $("#weekshow").empty();
                    Common.render({tmpl: $('#weekTempJs'), data: {data: detailWeek,x:select_x}, context: '#weekshow'});
                    $("#classDetailShow").empty();
                    Common.render({tmpl: $('#classDetailTempJs'), data: {data: detailTime,y:select_y}, context: '#classDetailShow'});
                    if(detailTime.length>0)
                        $(".i2").text(detailTime[select_y-1]);
                }
            }
        );
    };
    //获取学生课表
    chargeteacher.getStudentTable=function(stuId,week)
    {
        $.ajax({
            url:"/timetable/getStudentTimeTable.do",
            type:"post",
            data:{
                studentId:stuId,
                week:week
            },
            success:function(rep)
            {
                courseData.conf=rep.conf;
                courseData.course=rep.course;
                $(".main1-3").empty();
                Common.render({tmpl: $('#tempJs'), data: {data: courseData,type:showType}, context: '.main1-3'});
            }
        });
    };
    //获取调课通知
    chargeteacher.getClassNoticeList=function()
    {
        $.ajax({
            url:"/paike/getNoticeDetailList.do",
            type:"post",
            dataType:"json",
            data:{
                term:$("#termVal").val(),
                gradeId:"",
                classId:"",
                week:Number($("#weekShow").val())
            },
            success:function(data)
            {
                $('#noticeShow').empty();
                if(data.length>0)
                    Common.render({tmpl: $('#noticeTempJs'), data: {data: data}, context: '#noticeShow'});
            }
        })
    };
    var classTable={};
    //获取班级课表
    chargeteacher.getClassTable=function(week)
    {
        $.ajax({
            url:"/timetable/getClassTimeTable.do",
            type:"post",
            data:{
                year:$("#yearVal").val(),
                term:$("#termVal").val(),
                gradeId:$("#gradeId").val(),
                classId:$("#classId").val(),
                type:0,
                week:week
            },
            success:function(data)
            {
                $(".termShow").text($("#termShow").val());
                classTable.conf=data.conf;
                classTable.course=data.course;
                $(".main1-3").empty();
                Common.render({tmpl: $('#classTableTempJs'), data: {data: classTable,type:showType}, context: '.main1-3'});
            }
        });
    };

    //===================================================本班学生去向=========================================================
    var subjectList=[];
    //获取学科列表
    chargeteacher.getSubjectList=function()
    {
        $.ajax({
            url:"/timetable/getSubjectList.do",
            type:"post",
            data:{
                gradeId:$("#gradeId").val()
            },
            success:function(data)
            {
                subjectList=data;
                $("#subjectShow").empty();
                Common.render({tmpl: $('#sublistTempJs'), data: {data: subjectList}, context: "#subjectShow"});
                chargeteacher.getStudentChooseList();
            }
        });
    };
    //获取学生选课去向
    chargeteacher.getStudentChooseList=function()
    {
        $.ajax({
            url:"/timetable/getStudentChooseList.do",
            type:"post",
            data:{
                courseId:$("#subjectShow").val(),
                gradeId:$("#gradeId").val(),
                classId:$("#classId").val()
            },
            success:function(data)
            {
                $(".main2-3").empty();
                Common.render({tmpl: $('#stuChooseTempJs'), data: {data: data}, context: '.main2-3'});
            }
        });
    };
    $(document).ready(function(){
        $("body").on("click","#getStudentList",function(){
            chargeteacher.getStudentChooseList();
        });
    });
    //===========================本班选课进度==================================
    //获取本班选课进度
    chargeteacher.findXuanKeConf=function()
    {
        $.ajax({
            url:"/timetable/findXuanKeConf.do",
            type:"post",
            data:{
                gradeId:$("#gradeId").val(),
                classId:$("#classId").val()
            },
            success:function(data)
            {
                $("#wwc").html("未完成选课："+data.xuankecount);
                $("#timeShow").html("开放时间："+data.startDate+"~"+data.endDate);
                $(".main3-3").empty();
                Common.render({tmpl: $('#subjectConfTempJs'), data: {data: data.subConfList}, context: '.main3-3'});
            }
        });
    };
    $(document).ready(function(){
        $("body").on("click",".tab-detial",function() {
            $(".main3-hide2").show();
            $(".bg").show();
            $("#subjectName").html($(this).attr("snm"));
            var subjectId=$(this).attr("sid");
            chargeteacher.getStuChooseList(subjectId);
        });
        $("body").on("click",".tab-choose",function() {
            $(".main3-hide").show();
            $(".bg").show();
            $("#userId").val($(this).attr("uid"));
            chargeteacherData.studentName=$(this).parent().parent().children("td").eq(0).text();
            chargeteacher.getConf();
        });
    });
    //获取学生选课名单
    chargeteacher.getStuChooseList=function(subjectId)
    {
        $.ajax({
            url:"/timetable/getStuChooseList.do",
            type:"post",
            data:{
                gradeId:$("#gradeId").val(),
                subjectId:subjectId,
                classId:$("#classId").val()
            },
            success:function(data)
            {
                $(".hide4-3").empty();
                Common.render({tmpl: $('#chooseListTempJs'), data: {adv: data.adv,sim:data.sim,al:data.al,sl:data.sl}, context: '.hide4-3'});
            }
        });
    };
    //获取学生选课详情
    chargeteacher.getMyclassStuChoose=function()
    {
        $.ajax({
            url:"/timetable/getMyclassStuChoose.do",
            type:"post",
            data:{
                gradeId:$("#gradeId").val(),
                classId:$("#classId").val()
            },
            success:function(data)
            {
                $(".main3a-3").empty();
                Common.render({tmpl: $('#xuankeDetailTempJs'), data: {data:data}, context: '.main3a-3'});
            }
        });
    };
    //================================================选课=================================================
    var chargeteacherData={};
    $(document).ready(function(){
        //提交选课结果、
        $("body").on("click",".submitXuanke",function()
        {
            /*if(advList.length!=chargeteacherData.advCount || simList.length!=chargeteacherData.simCount)
            {
                alert(advList.length+"=="+simList.length);
                alert("请选择"+chargeteacherData.advCount+"门等级考，"+chargeteacherData.simCount+"门合格考");
                return;
            }*/

            chargeteacherData.advance=convertToStr(advList);
            chargeteacherData.simple=convertToStr(simList);
            chargeteacherData.stuId=$("#userId").val();
            chargeteacherData.stuName=chargeteacherData.studentName;
            chargeteacherData.xuankeId=$("#xuankeid").val();
            Common.getPostData('/xuanke/teacherXK.do', chargeteacherData,function(rep){
                if(rep=="200")
                {
                    alert("选课成功！");
                    chargeteacher.getConf();
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
                $(this).removeClass("ch-me");
                $(this).text("我选");
                advList.splice($.inArray($(this).parent().parent().attr("sid"),advList),1);
            }
            else
            {
                $(this).addClass("ch-me");
                $(this).text("已选");
                advList.push($(this).parent().parent().attr("sid"));
                var index2=jQuery.inArray($(this).parent().parent().attr("sid"),simList);
                if(index2!=-1)
                {
                    simList.splice(index2, 1);
                    $(this).parent().parent().children().eq(2).find("em").removeClass("ch-me");
                    $(this).parent().parent().children().eq(2).find("em").text("我选");
                }
            }
        });
        $("body").on("click",".simcourse",function()
        {
            if($(this).text()=="已选")
            {
                $(this).removeClass("ch-me");
                $(this).text("我选");
                simList.splice($.inArray($(this).parent().parent().attr("sid"),simList),1);
            }
            else
            {
                $(this).addClass("ch-me");
                $(this).text("已选");
                simList.push($(this).parent().parent().attr("sid"));
                var index2=jQuery.inArray($(this).parent().parent().attr("sid"),advList);
                if(index2!=-1)
                {
                    advList.splice(index2, 1);
                    $(this).parent().parent().children().eq(1).find("em").removeClass("ch-me");
                    $(this).parent().parent().children().eq(1).find("em").text("我选");
                }
            }
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
    chargeteacher.getConf=function()
    {
        Common.getPostData('/xuanke/getXuankeResultAdmin.do', {userId:$("#userId").val()},function(rep){
            $(".hide3-2").empty();
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
            $("#xuankeid").val(rep.conf.xuankeId);
            chargeteacherData.advCount=rep.conf.advanceCount;
            chargeteacherData.simCount=rep.conf.simpleCount;
            Common.render({tmpl: $('#xuankeTempJs'), data: {data: rep,advChoose:advChoose,simChoose:simChoose,
                stuName:chargeteacherData.studentName}, context: '.hide3-2'});
        });
    };
    //======================================================选课公示==================================================
    chargeteacher.xuankeGongshi=function()
    {
        $.ajax({
            url:"/timetable/getXuankeGongshi.do",
            type:"post",
            success:function(data)
            {
                //$(".main3-2").text(data.term);
                //$("#ac").text(data.advanceCount);
                //$("#sc").text(data.simpleCount);
                $("#time").text(data.time);
                $(".main4-3").empty();
                Common.render({tmpl: $('#gongshiTempJs'), data: {data: data.list}, context: '.main4-3'});
            }
        });
    };

});