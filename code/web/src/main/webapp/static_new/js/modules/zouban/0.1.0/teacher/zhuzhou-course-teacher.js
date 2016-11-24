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
    var courseteacher = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    courseteacher.init = function () {



    };
    $(function(){
        $(".right-title  li").click(function(){
            $(this).addClass("cur").siblings("li").removeClass("cur");
            var index=$(this).index();
            if(index==0)
            {

            }
            else if(index==1)
            {
                courseteacher.courseClassDetail();
            }
            else if(index==2)
            {
                courseteacher.xuankeGongshi();
            }
        })
    });

    $(document).ready(function($) {
        $(".main1-li").click(function() {
            if(tablePublish==0) {
                $(".right-main1").hide();
                $(".right-main7").show();
                $(".no-class").text("该老师未带班或排课未完成");
                return;
            }
            $(".right-main1").show();
            $(".right-main2").hide();
            $(".right-main3").hide();
            $(".main2a").hide();
        });
    });
    $(document).ready(function($) {
        $(".main2-li").click(function() {
            $(".right-main2").show();
            $(".right-main1").hide();
            $(".right-main3").hide();
            $(".main2a").hide();
            courseteacher.courseClassDetail();
        });
    });
    $(document).ready(function($) {
        $(".main3-li").click(function() {
            $(".right-main3").show();
            $(".right-main2").hide();
            $(".right-main7").hide();
            $(".right-main1").hide();
            $(".main2a").hide();
        });
    });

    $(document).ready(function($) {
        $("body").on("click",".main2-3 em",function() {
            $(".main2a").show();
            $(".right-main2").hide();
            //$(".main3-2").text(data.term);
            $("#s1").text($(this).parent().parent().children("td").eq(0).text());
            $("#s2").text("教室："+$(this).parent().parent().children("td").eq(3).text());
            $("#s3").text("人数："+$(this).parent().parent().children("td").eq(1).text());
            var courseId=$(this).attr("cid");
            courseteacher.courseDetail(courseId);
        });
    });

    $(document).ready(function($) {
        $(".main2a-1").click(function() {
            $(".main2a").hide();
            $(".right-main2").show();
        });
    });

    courseteacher.init();
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
    courseteacher.getTeacherGradeList=function()
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
                courseteacher.getWeekList();
            }
        })
    };
    courseteacher.getWeekList=function()
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
        Common.render({tmpl: $('#weekTempJs'), data: {data: weekList}, context: '#weekShow'});
        $("#weekShow").val(Number($("#curweek").val()));
        courseteacher.getTeacherTable(Number($("#curweek").val()));
    };
    //获取调课通知
    courseteacher.getNoticeList=function()
    {
        $.ajax({
            url:"/paike/getTeacherNoticeDetailList.do",
            type:"post",
            dataType:"json",
            data:{
                term:$("#termVal").val(),
                gradeId:$("#gradeShow").val(),
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
    courseteacher.getTeacherTable=function(week)
    {
        var gradeId=$("#gradeShow").val();
        if(gradeId==null)
        {
            tablePublish=0;
            $(".right-main1").hide();
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
                    tablePublish=0;
                    $(".right-main1").hide();
                    $(".right-main7").show();
                    $(".no-class").text("排课未完成");
                    return;
                }
                teacherTable.conf=data.conf;
                teacherTable.course=data.course;

                //$(".termShow").text(data.conf.term);
                $(".main1-3").empty();
                Common.render({tmpl: $('#teacherTableTempJs'), data: {data: teacherTable}, context: '.main1-3'});
                courseteacher.getNoticeList();
            }
        })
    };
    $(document).ready(function(){
       $("body").on("click","#getDetail",function(){
           courseteacher.getTeacherTable(Number($("#weekShow").val()));
       });
    });
    var tablePublish=1;
    //======================================教学班信息==============================
    //教学班信息
    courseteacher.courseClassDetail=function()
    {
        if(tablePublish==0)
        {
            tablePublish=0;
            $(".right-main2").hide();
            $(".right-main7").show();
            $(".no-class").text("排课未完成");
            return;
        }
        $.ajax({
            url:"/timetable/getCourseClassDetail.do",
            type:"post",
            success:function(data)
            {
                $(".main2-3").empty();
                Common.render({tmpl: $('#courseTempJs'), data: {data: data}, context: '.main2-3'});
            }
        });
    };
    //教学班学生列表
    courseteacher.courseDetail=function(courseId)
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
                Common.render({tmpl: $('#detailTempJs'), data: {data: data}, context: '.main2a-4'});
            }
        });
    };
    //========================================选课公示===========================
    /*courseteacher.xuankeGongshi=function()
    {
        $.ajax({
            url:"/timetable/getXuankeGongshi.do",
            type:"post",
            success:function(data)
            {
                if(data.code=="500")
                {
                    $(".right-main3").hide();
                    $(".right-main4").hide();
                    $(".right-main7").show();
                    $(".no-class").text("选课结果未公示");
                    return;
                }
                //$(".main3-2").text(data.term);
                //$("#ac").text(data.advanceCount);
                //$("#sc").text(data.simpleCount);
                $("#time").text(data.time);
                $(".main3-4").empty();
                Common.render({tmpl: $('#gongshiTempJs'), data: {data: data.list}, context: '.main3-4'});
            }
        });
    };*/
    //获取选修课
    courseteacher.xuankeGongshi=function()
    {
        var gradeId=$("#gradeShow").val();
        var term=$("#yearVal").val();
        $.ajax({
            url:"/paike/findInterestClassList.do",
            type:"post",
            data:{
                term:term,
                gradeId:gradeId
            },
            success:function(data)
            {
                $(".main3-4").empty();
                Common.render({tmpl: $('#gongshiTempJs'), data: {data: data}, context: '.main3-4'});
            }
        });
    };

    courseteacher.getTeacherGradeList();
    //=========================================导出====================================
    $(document).ready(function(){
        $("body").on("click","#exportTable1",function(){
            var gradeName=$("#gradeShow").find("option:selected").text();
            gradeName=encodeURI(gradeName);
            gradeName=encodeURI(gradeName);
            window.location='/timetable/exportTea.do?term='+encodeURI(encodeURI($("#termVal").val()))+'&gradeId='+$("#gradeShow").val()+'&gradeName='+gradeName+"&teacherId=&teacherName=&week="+Number($("#weekShow").val());
        });
    });
});