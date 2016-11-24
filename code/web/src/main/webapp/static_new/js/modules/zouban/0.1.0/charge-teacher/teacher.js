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
    var teacher = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    teacher.init = function () {



    };
    $(function(){
        $(".right-title  li").click(function(){
            $(this).addClass("cur").siblings("li").removeClass("cur");
            var index=$(this).index();
            if(index==0)
            {
                //if(classTable.course.length==0)
                //{
                    teacher.getClassTable(Number($("#weekShow").val()));
                //}
            }
            else if(index==1)
            {
                //if(subjectList.length==0)
                //{
                    teacher.getSubjectList();
                //}
            }
            else if(index==2)
            {
                teacher.findXuanKeConf();
            }
            else if(index==3)
            {
                teacher.xuankeGongshi();
            }
            else if(index==4)
            {
                teacher.getTeacherGradeList();
            }
            /*else if(index==5)
            {
                teacher.courseClassDetail();
            }*/
        })
    });

    $(document).ready(function($) {
        $(".main1-li").click(function() {
            $(".right-main7").hide();
            $(".right-main1").show();
            $(".right-main2").hide();
            $(".right-main3").hide();
            $(".right-main4").hide();
            $(".right-main5").hide();
            $(".right-main6").hide();
            $(".main3a").hide();
            $(".main2a").hide();
        });
    });
    $(document).ready(function($) {
        $(".main2-li").click(function() {
            $(".right-main7").hide();
            $(".right-main2").show();
            $(".right-main1").hide();
            $(".right-main3").hide();
            $(".right-main4").hide();
            $(".right-main5").hide();
            $(".right-main6").hide();
            $(".main3a").hide();
            $(".main2a").hide();
        });
    });
    $(document).ready(function($) {
        $(".main3-li").click(function() {
            $(".right-main7").hide();
            $(".right-main3").show();
            $(".right-main2").hide();
            $(".right-main1").hide();
            $(".right-main4").hide();
            $(".right-main5").hide();
            $(".right-main6").hide();
            $(".main3a").hide();
            $(".main2a").hide();
        });
    });
    $(document).ready(function($) {
        $(".main4-li").click(function() {
            $(".right-main4").show();
            $(".right-main2").hide();
            $(".right-main3").hide();
            $(".right-main1").hide();
            $(".right-main5").hide();
            $(".right-main6").hide();
            $(".main3a").hide();
            $(".main2a").hide();
        });
    });
    $(document).ready(function($) {
        $(".main5-li").click(function() {
            $(".right-main7").hide();
            $(".right-main3").hide();
            $(".right-main2").hide();
            $(".right-main1").hide();
            $(".right-main4").hide();
            $(".right-main5").show();
            $(".right-main6").hide();
            $(".main3a").hide();
            $(".main2a").hide();
        });
    });
    $(document).ready(function($) {
        $(".main6-li").click(function() {
            $(".right-main7").hide();
            $(".right-main4").hide();
            $(".right-main2").hide();
            $(".right-main3").hide();
            $(".right-main1").hide();
            $(".right-main5").hide();
            $(".right-main6").show();
            $(".main3a").hide();
            $(".main2a").hide();
            teacher.courseClassDetail();
        });
    });
    $(document).ready(function($) {
        $(".hide1-x").click(function() {
            $(".main1-hide").hide();
            $(".main3-hide2").hide();
            $(".bg").hide();
        });
    });


    $(document).ready(function($) {
        $(".check-list").click(function() {
            $(".main3a").show();
            $(".right-main3").hide();
            teacher.getMyclassStuChoose();
        });
    });

    $(document).ready(function($) {
        $(".main3a-1").click(function() {
            $(".main3a").hide();
            $(".right-main3").show();
        });
    });

    $(document).ready(function($) {
        $(".tab-choose").click(function() {
            $(".schedule-CUR").show();
        });
    });

    $(document).ready(function($) {
        $(".main3-hide .hide3-1 i").click(function() {
            $(".main3-hide").hide();
            $(".bg").hide();
        });
    });

    $(document).ready(function() {
        $("body").on("click",".btn-qx",function() {
            $(".main3-hide").hide();
            $(".main3-hide2").hide();
            $(".bg").hide();
        });
        $("body").on("click",".main6-3 em",function() {
            $(".main2a").show();
            $(".right-main6").hide();
            //$(".main3-2").text(data.term);
            $("#s1").text($(this).parent().parent().children("td").eq(0).text());
            $("#s2").text("教室："+$(this).parent().parent().children("td").eq(3).text());
            $("#s3").text("人数："+$(this).parent().parent().children("td").eq(1).text());
            var courseId=$(this).attr("cid");
            teacher.courseDetail(courseId);
        });
        $(".main2a-1").click(function() {
            $(".main2a").hide();
            $(".right-main6").show();
        });
    });



    teacher.init();
    //===========================课表=============================

    //获取本班学生
    teacher.getStudentList=function()
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
                teacher.getWeekList();
            }
        });
    };
    //获取教学周列表
    teacher.getWeekList=function()
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
        $("#weekShow2").empty();
        Common.render({tmpl: $('#weekShowTempJs'), data: {data: weekList}, context: '#weekShow2'});
        $("#weekShow").val(Number($("#curweek").val()));
        $("#weekShow2").val(Number($("#curweek").val()));
        teacher.getClassTable(Number($("#curweek").val()));
        teacher.getClassNoticeList();
    };
    teacher.getStudentList();
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
    var tablePublish=1;//已发布
    $(document).ready(function(){
        $("body").on("click","#getTimetable",function(){
            var stuId=$("#stuList").val();
            if(stuId=="0")
            {
                teacher.getClassTable(Number($("#weekShow").val()));
            }
            else{
                teacher.getStudentTable(stuId,Number($("#weekShow").val()));
            }
            teacher.getClassNoticeList();
        });
        //走班详情
        $("body").on("click",".zb",function() {
            $(".main1-hide").show();
            $(".bg").show();
            var select_x=Number($(this).attr("x"));
            var select_y=Number($(this).attr("y"));
            teacher.getDetailHead(select_x,select_y);
            teacher.getDetail(select_x,select_y,Number($("#weekShow").val()));
        });
        //体育走班详情
        $("body").on("click",".tyzb",function() {
            $(".main1-hide").show();
            $(".bg").show();
            var select_x=Number($(this).attr("x"));
            var select_y=Number($(this).attr("y"));
            teacher.getDetailHead(select_x,select_y);
            teacher.getPhysicalDetail(select_x,select_y,Number($("#weekShow").val()));
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
            teacher.getDetail(select_x,select_y,Number($("#weekShow").val()));
        });
        //走班非走班变化
        $("body").on("change",".showType",function(){
            showType=Number($(this).val());
            var stuId=$("#stuList").val();
            if(stuId=="0")
            {
                $(".main1-3").empty();
                Common.render({tmpl: $('#classTableTempJs'), data: {data: classTable,type:showType}, context: '.main1-3'});
            }
            else{
                $(".main1-3").empty();
                Common.render({tmpl: $('#tempJs'), data: {data: courseData,type:showType}, context: '.main1-3'});
            }
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
                window.location='/timetable/exportStu.do?term='+encodeURI(encodeURI($("#termVal").val()))+'&stuId='+stuId+'&stuName='+stuName+'&week='+Number($("#weekShow").val());            }
        });
    });
    teacher.getDetail=function(select_x,select_y,week)
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
    teacher.getPhysicalDetail=function(select_x,select_y,week)
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
    teacher.getDetailHead=function(select_x,select_y)
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
    teacher.getStudentTable=function(stuId,week)
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
    teacher.getClassNoticeList=function()
    {
        $.ajax({
            url:"/paike/getNoticeDetailList.do",
            type:"post",
            dataType:"json",
            data:{
                term:$("#termVal").val(),
                gradeId:$("#gradeId").val(),
                classId:$("#classId").val(),
                week:Number($("#weekShow").val())
            },
            success:function(data)
            {
                $('#noticeShow2').empty();
                if(data.length>0)
                    Common.render({tmpl: $('#noticeTempJs'), data: {data: data}, context: '#noticeShow2'});
            }
        })
    };
    var classTable={};
    //获取班级课表
    teacher.getClassTable=function(week)
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
                if(data=="")
                {
                    tablePublish=0;
                    $(".right-main1").hide();
                    $(".right-main7").show();
                    $(".no-class").text("排课未完成");
                }
                $(".termShow").text(data.conf.term);
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
    teacher.getSubjectList=function()
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
                teacher.getStudentChooseList();
            }
        });
    };
    //获取学生选课去向
    teacher.getStudentChooseList=function()
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
                if(data.length==0)
                {
                    $(".right-main2").hide();
                    $(".right-main7").show();
                    $(".no-class").text("编班未完成");
                    return;
                }
                $(".main2-3").empty();
                Common.render({tmpl: $('#stuChooseTempJs'), data: {data: data}, context: '.main2-3'});
            }
        });
    };
    $(document).ready(function(){
        $("body").on("click","#getStudentList",function(){
            teacher.getStudentChooseList();
        });
    });
    //===========================本班选课进度==================================
    //获取本班选课进度
    teacher.findXuanKeConf=function()
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
                if(data=="")
                {
                    $(".right-main3").hide();
                    $(".right-main7").show();
                    $(".no-class").text("选课未开放");
                    return;
                }
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
            teacher.getStuChooseList(subjectId);
        });
        $("body").on("click",".tab-choose",function() {
            $(".main3-hide").show();
            $(".bg").show();
            $("#userId").val($(this).attr("uid"));
            teacherData.studentName=$(this).parent().parent().children("td").eq(0).text();
            teacher.getConf();
        });
    });
    //获取学生选课名单
    teacher.getStuChooseList=function(subjectId)
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
    teacher.getMyclassStuChoose=function()
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
    var teacherData={};
    $(document).ready(function(){
        //提交选课结果、
        $("body").on("click",".submitXuanke",function()
        {
            /*if(advList.length!=teacherData.advCount || simList.length!=teacherData.simCount)
            {
                alert(advList.length+"=="+simList.length);
                alert("请选择"+teacherData.advCount+"门等级考，"+teacherData.simCount+"门合格考");
                return;
            }*/

            teacherData.advance=convertToStr(advList);
            teacherData.simple=convertToStr(simList);
            teacherData.stuId=$("#userId").val();
            teacherData.stuName=teacherData.studentName;
            teacherData.xuankeId=$("#xuankeid").val();
            Common.getPostData('/xuanke/teacherXK.do', teacherData,function(rep){
                if(rep=="200")
                {
                    alert("选课成功！");
                    teacher.getConf();
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
    teacher.getConf=function()
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
            teacherData.advCount=rep.conf.advanceCount;
            teacherData.simCount=rep.conf.simpleCount;
            Common.render({tmpl: $('#xuankeTempJs'), data: {data: rep,advChoose:advChoose,simChoose:simChoose,
                stuName:teacherData.studentName}, context: '.hide3-2'});
        });
    };
    //======================================================选课公示==================================================
    teacher.xuankeGongshi=function()
    {
        $.ajax({
            url:"/timetable/getXuankeGongshi.do",
            type:"post",
            success:function(data)
            {
                if(data.code=="500")
                {
                    $(".right-main4").hide();
                    $(".right-main7").show();
                    $(".no-class").text("选课结果未公示");
                    return;
                }
                //$(".main3-2").text(data.term);
                //$("#ac").text(data.advanceCount);
                //$("#sc").text(data.simpleCount);
                $("#time").text(data.time);
                $(".main4-3").empty();
                Common.render({tmpl: $('#gongshiTempJs'), data: {data: data.list}, context: '.main4-3'});
            }
        });
    };
    //===============================课表=================================
    var teacherTable={
        "course": [{xIndex:1,yIndex:1,className:"xx",classRoom:"xx"}],
        conf:{
            "id":"03213",
            "schoolId":"08543",
            "term":"2015~2016学年第一学期",
            "gradeId":"43546",
            "classDays":[1,2,3,4,5,6,7],
            "classCount":7,
            "classTime":["8:00~8:45", "8:55~9:40", "10:00~10:45", "10:55~11:40", "14:00~14:45", "14:55~15:40", "16:00~16:45"],
            "events":[]
        }
    };
    teacher.getTeacherGradeList=function()
    {
        $.ajax({
            url:"/timetable/getTeacherGradeList.do",
            type:"post",
            data:{
                year:$("#yearVal").val()
            },
            success:function(data)
            {
                $("#gradeShow").empty();
                Common.render({tmpl: $('#gradeTempJs'), data: {data: data}, context: '#gradeShow'});
                teacher.getTeacherTable(Number($("#weekShow2").val()));
            }
        })
    };
    //获取调课通知
    teacher.getNoticeList=function()
    {
        $.ajax({
            url:"/paike/getTeacherNoticeDetailList.do",
            type:"post",
            dataType:"json",
            data:{
                term:$("#termVal").val(),
                gradeId:$("#gradeShow").val(),
                week:Number($("#weekShow2").val())
            },
            success:function(data)
            {
                $('#noticeShow').empty();
                if(data.length>0)
                    Common.render({tmpl: $('#noticeTempJs'), data: {data: data}, context: '#noticeShow'});
            }
        })
    };
    teacher.getTeacherTable=function(week)
    {
        var gradeId=$("#gradeShow").val();
        if(gradeId==null)
        {
            $(".right-main5").hide();
            $(".right-main7").show();
            $(".no-class").text("该老师未带班或排课未完成");
            return;
        }
        $.ajax({
            url:"/timetable/getTeacherTimeTable.do",
            type:"post",
            data:{
                term:$("#termVal").val(),
                year:$("#yearVal").val(),
                teacherId:"",
                gradeId:gradeId,
                week:week
            },
            success:function(data)
            {
                if(data=="")
                {
                    $(".right-main5").hide();
                    $(".right-main7").show();
                    $(".no-class").text("排课未完成");
                    return;
                }
                teacherTable.conf=data.conf;
                teacherTable.course=data.course;

                //集体调研
                var subjectList = [];
                for (var i = 0; i < data.conf.events.length; i++) {
                    var obj=data.conf.events[i];
                    var x=obj.xIndex;
                    var y=obj.yIndex;

                    for (var j = 0; j < obj.groupStudy.length; j++) {
                        if(data.subject.indexOf(obj.groupStudy[j].id))
                       {
                            var name= obj.groupStudy[j].name;
                            break;
                        }
                    }
                    subjectList.push({x:x, y:y,name: name});
                }
                $(".termShow").text(data.conf.term);
                $(".main5-3").empty();
                Common.render({tmpl: $('#teacherTableTempJs'), data: {data: teacherTable,sub:subjectList}, context: '.main5-3'});
                teacher.getNoticeList();
            }
        })
    };
    $(document).ready(function(){
        $("body").on("click","#getDetail",function(){
            teacher.getTeacherTable(Number($("#weekShow2").val()));
        });
        $("body").on("click","#exportTable1",function(){
            var gradeName=$("#gradeShow").find("option:selected").text();
            gradeName=encodeURI(gradeName);
            gradeName=encodeURI(gradeName);
            window.location='/timetable/exportTea.do?term='+encodeURI(encodeURI($("#termVal").val()))+'&gradeId='+$("#gradeShow").val()+'&gradeName='+gradeName+"&teacherId=&teacherName=&week="+Number($("#weekShow2").val());
        });
    });
    //======================================教学班信息==============================
    //教学班信息
    teacher.courseClassDetail=function()
    {
        if(tablePublish==0)
        {
            $(".right-main6").hide();
            $(".right-main7").show();
            $(".no-class").text("选课结果未公示");
            return;
        }
        $.ajax({
            url:"/timetable/getCourseClassDetail.do",
            type:"post",
            success:function(data)
            {
                $(".main6-3").empty();
                Common.render({tmpl: $('#courseTempJs'), data: {data: data}, context: '.main6-3'});
            }
        });
    };
    //教学班学生列表
    teacher.courseDetail=function(courseId)
    {
        $.ajax({
            url:"/timetable/getListSheet.do",
            type:"post",
            data:{
                courseId:courseId
            },
            success:function(data)
            {
                $(".main2a-4").empty();
                Common.render({tmpl: $('#detailTempJs2'), data: {data: data}, context: '.main2a-4'});
            }
        });
    };

});