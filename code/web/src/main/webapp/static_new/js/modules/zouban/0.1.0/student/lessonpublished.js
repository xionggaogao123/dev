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

    var lessonpublished = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    lessonpublished.init = function () {



    };
    $(document).ready(function(){
        $(".class-select").click(
            function(){
                $(".class-list").removeClass("cont-style");
                $(".class-select").addClass("cont-style");
                $(".right-main1").hide();
                $(".right-main2").show();}
        )
    });

    $(document).ready(function(){
        $(".class-list").click(
            function(){
                $(".class-list").addClass("cont-style");
                $(".class-select").removeClass("cont-style");
                $(".right-main1").show();
                $(".right-main2").hide();}
        )
    });



    $(document).ready(function(){
        $(".select-sub").click(
            function(){
                $(".sub-div").show();
            });
    });

    $(document).ready(function(){
        $(".sub-can").click(
            function(){
                $(".sub-div").hide();
            });
    });

    $(document).ready(function(){
        $(".win-cha").click(
            function(){
                $(".sub-div").hide();
            });
        $("#select").click(function(){
            lessonpublished.getStudentTimetable(Number($("#weekShow").val()));

        });
    });

    lessonpublished.getStudentTimetable=function(week)
    {
        $.ajax({
            url:'/timetable/getStudentTimeTable.do',
            type:"post",
            dataType:"json",
            data:{
                studentId:"",
                week:week
            },
            success:function(rep){
                data.conf=rep.conf;
                data.course=rep.course;
                $('#div01').empty();
                Common.render({tmpl: $('#tempJs'), data: {data: data}, context: '#div01'});
                lessonpublished.getNoticeList();
            }
        });
    };
    //获取调课通知
    lessonpublished.getNoticeList=function()
    {
        $.ajax({
            url:"/paike/getNoticeDetailList.do",
            type:"post",
            dataType:"json",
            data:{
                term:$("#term").val(),
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
    //获取教学周列表
    lessonpublished.getWeekList=function()
    {
        var weekList=[];
        for(var i=1;i<=Number($("#allweek").val());i++)
        {
            if(i==Number($("#week").val()))
            {
                weekList.push({id:i,name:"第"+i+"周(本周)"});
            }
            else {
                weekList.push({id: i, name: "第" + i + "周"});
            }
        }
        $("#weekShow").empty();
        Common.render({tmpl: $('#weekTempJs'), data: {data: weekList}, context: '#weekShow'});
        $("#weekShow").val(Number($("#week").val()));
        //chargeteacher.getClassTable(Number($("#week").val()));
        lessonpublished.getStudentTimetable(Number($("#week").val()));
    };
    var data={
        "conf":{
            "id":"03213",
            "schoolId":"08543",
            "term":"2015~2016学年第一学期",
            "gradeId":"43546",
            "classDays":[1,2,3,4,5,6,7],
            "classCount":7,
            "classTime":["8:00~8:45", "8:55~9:40", "10:00~10:45", "10:55~11:40", "14:00~14:45", "14:55~15:40", "16:00~16:45"],
            "events":[]
        },
        "course":[{"xIndex":1,"yIndex":1,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":1,"yIndex":3,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":1,"yIndex":2,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":1,"yIndex":5,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":1,"yIndex":4,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":1,"yIndex":6,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":1,"yIndex":7,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":3,"yIndex":1,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":3,"yIndex":3,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":3,"yIndex":2,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":3,"yIndex":5,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":2,"yIndex":4,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":3,"yIndex":6,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":3,"yIndex":7,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":4,"yIndex":1,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":4,"yIndex":3,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":4,"yIndex":2,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":4,"yIndex":5,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":4,"yIndex":4,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":4,"yIndex":6,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":4,"yIndex":7,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":5,"yIndex":1,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":5,"yIndex":3,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":5,"yIndex":2,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":7,"yIndex":5,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":5,"yIndex":4,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":5,"yIndex":6,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":5,"yIndex":7,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":6,"yIndex":1,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":6,"yIndex":3,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":6,"yIndex":2,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":6,"yIndex":5,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":6,"yIndex":4,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":6,"yIndex":6,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"},
            {"xIndex":6,"yIndex":7,"courseId":"343","className":"物理等级考1","classRoom":"207教室","teacherName":"王大鹏"}
        ]
    };

    //lessonpublished.getStudentTimetable(Number($("#week").val()));
    lessonpublished.getWeekList();
    lessonpublished.init();

    $(document).ready(function(){
        $("body").on("click","#printPDF1",function()
        {
            window.location='/timetable/exportStu.do?week='+Number($("#weekShow").val());
        });
    })
});