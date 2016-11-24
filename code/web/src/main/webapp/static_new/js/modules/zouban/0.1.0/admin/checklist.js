/**
 * Created by qiangm on 2015-10-16.
 */
'use strict';
define(['doT', 'common', 'jquery'], function (require, exports, module) {
    /**
     *初始化参数
     */
    require("jquery");
    require("doT");
    var checklist = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    checklist.init = function () {



    };

    $(document).ready(function ($) {
        $(".right-main2").show();
        $(".backUrl").click(function(){
            window.open('../paike/index.do?version=58&year='+ encodeURI(encodeURI($('#yearShow').val()))+'&gradeId='+
            $('#gradeId').val(),'_self');
        });
    });

    checklist.init();

//=========================================行政班课表==================================
    var gradeClass=[];
    var classList=[];
    var classTable={
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
    var detailWeek=[];
    var detailTime=[];
    var classDetail=[{
        className:"",
        people:"",
        teacherName:"",
        classRoom:"",
        myClassAmount:""
    }];
    checklist.getGradeClass=function()
    {
        $.ajax(
            {
                url:"/timetable/getGradeClassList.do",
                type:"post",
                data:{
                    gradeId:$("#gradeId").val()
                },
                success:function(data){
                    gradeClass=data;
                    classList=[];
                    if(gradeClass.length>0)
                    {
                        var map=gradeClass[0].classInfo;
                        if(map!=null) {
                            $.each(map, function (key, value) {
                                classList.push({classId: key, className: value});
                            });
                        }
                    }
                    $("#classShow2").empty();
                    Common.render({tmpl: $('#classTempJs2'), data: {data: classList}, context: '#classShow2'});

                    checklist.getClassTimetable();
                }
            }
        );
    };
    checklist.getClassTimetable=function()
    {
        $.ajax(
            {
                url:"/timetable/getClassTimeTable.do",
                type:"post",
                data:{
                    year:$("#yearShow").val(),
                    term:"",
                    gradeId:$("#gradeId").val(),
                    classId:$("#classShow2").val(),
                    type:3,
                    week:0
                },
                success:function(data){
                    classTable.conf=data.conf;
                    classTable.course=data.course;
                    $("#classTableShow").empty();
                    Common.render({tmpl: $('#classTableTempJs'), data: {data: classTable}, context: '#classTableShow'});
                }
            }
        );
    };
    checklist.getDetail=function(select_x,select_y)
    {
        $.ajax(
            {
                url:"/timetable/getDetailList.do",
                type:"post",
                dataType:"json",
                data:{
                    term:$("#yearShow").val(),
                    classId:$("#classShow2").val(),
                    xIndex:select_x,
                    yIndex:select_y,
                    type:3,
                    week:0
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
    checklist.getPhysicalDetail=function(select_x,select_y)
    {
        $.ajax(
            {
                url:"/timetable/getPhysicalDetailList.do",
                type:"post",
                dataType:"json",
                data:{
                    term:$("#yearShow").val(),
                    classId:$("#classShow2").val(),
                    xIndex:select_x,
                    yIndex:select_y,
                    type:3,
                    week:0
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
    checklist.getDetailHead=function(select_x,select_y)
    {
        $.ajax(
            {
                url:"/timetable/getDetailHead.do",
                type:"post",
                data:{
                    term:$("#yearShow").val(),
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
    checklist.getZoubanState=function()
    {
        $.ajax({
            url: "/xuanke/getState.do",
            type: "post",
            dataType: "json",
            data: {
                term:$("#yearShow").val(),
                gradeId: $("#gradeId").val()
            },
            success: function (data) {
                if(data.state==6)//已发布
                {
                    $("#publishShow").show();
                    $(".publish-table").text("取消发布");
                }
                else{
                    $("#publishShow").hide();
                    $(".publish-table").text("发布课表");
                }

            }
        });
    };
    $(document).ready(function(){

        //获取详情
        $("body").on("click",".main2-btn1",function(){
            checklist.getClassTimetable();
        });
        //详情页
        $("body").on("click",".zb",function()
        {
            $(".main2-hide").show();
            $(".classShow").text($("#classShow2").find("option:selected").text());
            var select_x=Number($(this).attr("x"));
            var select_y=Number($(this).attr("y"));
            checklist.getDetailHead(select_x,select_y);
            checklist.getDetail(select_x,select_y);

        });
        //详情页-体育走班
        $("body").on("click",".tyzb",function()
        {
            $(".main2-hide").show();
            $(".classShow").text($("#classShow2").find("option:selected").text());
            var select_x=Number($(this).attr("x"));
            var select_y=Number($(this).attr("y"));
            checklist.getDetailHead(select_x,select_y);
            checklist.getPhysicalDetail(select_x,select_y);

        });
        $("body").on("click",".main2wind-cl",function () {
            $(".main2-hide").hide();
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
            checklist.getDetail(select_x,select_y);
        });
        //发布课表
        $("body").on("click",".publish-table",function(){
            if($(".publish-table").text()=="发布课表") {
                if (confirm("确定要发布课表吗？")) {
                    $.ajax(
                        {
                            url: "/timetable/publishCourse.do",
                            type: "post",
                            data: {
                                term:$("#yearShow").val(),
                                gradeId: $("#gradeId").val()
                            },
                            success: function (data) {
                                if (data.result == "SUCCESS") {
                                    //alert("课表发布成功");
                                    $(".publish-table").text("取消发布");
                                    $("#publishShow").show();
                                }
                                else {
                                    alert("课表发布失败，失败原因：" + data.reason);
                                }
                            }
                        }
                    );
                }
            }
            else{//取消发布
                if (confirm("确定要取消发布课表吗？取消发布将会清除已发布版本和调课版本，仅保留原始排课数据。")) {
                    $.ajax(
                        {
                            url: "/timetable/cancelPublishCourse.do",
                            type: "post",
                            data: {
                                term:$("#yearShow").val(),
                                gradeId: $("#gradeId").val()
                            },
                            success: function (data) {
                                if (data.result == "SUCCESS") {
                                    $(".publish-table").text("发布课表");
                                    $("#publishShow").hide();
                                }
                                else {
                                    alert("取消发布失败");
                                }
                            }
                        }
                    );
                }
            }
        });
        //导出课表
        $("body").on("click",".exportTable",function(){
            var gradeId=$("#gradeId").val();
            var classId=$("#classShow2").val();
            window.location='/timetable/exportAllStu.do?term='+encodeURI(encodeURI($("#termShow").val()))+'&year='+encodeURI(encodeURI($("#yearShow").val()))+'&gradeId='+gradeId+'&classId='+classId+"&type=3&week=0";
        });
    });
    checklist.getZoubanState();
    checklist.getGradeClass();
});