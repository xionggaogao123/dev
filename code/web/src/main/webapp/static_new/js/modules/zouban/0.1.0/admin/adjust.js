/**
 * Created by qiangm on 2015-10-16.
 */
'use strict';
define(['doT', 'common', 'jquery', 'initPaginator'], function (require, exports, module) {
    /**
     *初始化参数
     */
    require("jquery");
    require("doT");
    var Paginator = require('initPaginator');
    var adjust = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    adjust.init = function () {


    };

    adjust.init();
    //========================================首页===========================================================
    $(document).ready(function () {
        $(".backUrl").click(function(){
            window.open('../paike/index.do?version=58&year='+ encodeURI(encodeURI($('#year').val()))+'&gradeId='+
            $('#gradeId').val(),'_self');
        });
        $(".tab-adjust").show();
        $(".adjust-TJ").click(function () {
            $(".bg").show();
            $(".adjust-CUR").show();
            $("#type0").show();
            $("#type1").hide();
            $("#type2").hide();
            $("#CUR-select").val(0);
            $("#weekShow1_1").empty();
            Common.render({
                tmpl: $('#weekTempJs'),
                data: {data: adjust.generalWeekList1(Number($("#allweek").val()))},
                context: '#weekShow1_1'
            });
            $("#weekShow2_1").empty();
            Common.render({
                tmpl: $('#weekTempJs'),
                data: {data: adjust.generalWeekList1(Number($("#allweek").val()) - 1)},
                context: '#weekShow2_1'
            });
            $("#weekShow2_2").empty();
            Common.render({
                tmpl: $('#weekTempJs'),
                data: {data: adjust.generalWeekList2(Number($("#curweek").val()))},
                context: '#weekShow2_2'
            });
            $("#weekShow3_1").empty();
            Common.render({
                tmpl: $('#weekTempJs'),
                data: {data: adjust.generalWeekList1(Number($("#allweek").val()) - 1)},
                context: '#weekShow3_1'
            });
            $("#weekShow3_2").empty();
            Common.render({
                tmpl: $('#weekTempJs'),
                data: {data: adjust.generalWeekList2(Number($("#curweek").val()))},
                context: '#weekShow3_2'
            });
        });
        //类型变化
        $("body").on("change", "#typeSelect", function () {
            adjust.getNoticeList(1);
        });
        //查看
        $("body").on("click", ".viewNotice", function () {
            var noticeId = $(this).parent().attr("nid");
            $(".tab-adjust").hide();
            $(".tab-lesson").show();
            adjust.getNoticeDetail(noticeId);

        });
        //返回
        $("body").on("click", ".back-main", function () {
            $(".tab-lesson").hide();
            $(".tab-adjust").show();
            $("#noticeName").attr("readonly", false);
            $("#notice-des").attr("readonly", false);
            $("#publish_ok").show();
            $("#publish_cancel").show();
            $(".back-main").hide();
        });
        //删除
        $("body").on("click", ".deleteNotice", function () {
            var noticeId = $(this).parent().attr("nid");
            var tableIds = $(this).attr("tableIds");
            if (confirm("删除将会清除本调课通知以及对应的调课课表，确定删除吗？") == true) {
                $.ajax({
                    url: "/paike/deleteNotice.do",
                    type: "post",
                    dataType: "json",
                    data: {
                        noticeId: noticeId,
                        tableIds: tableIds
                    },
                    success: function (data) {
                        if (data.result == "SUCCESS") {
                            adjust.getNoticeList(1);
                        }
                        else {
                            alert("删除失败");
                        }
                    }
                })
            }
        });
    });
    adjust.getNoticeDetail = function (noticeId) {
        $.ajax({
            url: "/paike/findZoubanNotice.do",
            type: "post",
            dataType: "json",
            data: {
                noticeId: noticeId
            },
            success: function (data) {
                var detail = data.noticeDetails;
                if(detail.length>0)
                    $("#classShow4").text(detail[0].cl);
                else
                    $("#classShow4").text("");
                $("#adjustType").text(data.type_str);
                $("#noticeName").val(data.name);
                $("#noticeName").attr("readonly", true);
                $("#notice-des").val(data.descript);
                $("#notice-des").attr("readonly", true);
                $("#time").text(data.time);
                $("#publish_ok").hide();
                $("#publish_cancel").hide();
                $(".back-main").show();
                var detailData = [];
                for (var i = 0; i < detail.length; i++) {
                    detailData.push({
                        className: detail[i].cl, courseName: detail[i].co, teacherName: detail[i].te,
                        oldTime: detail[i].ot, newTime: detail[i].nt
                    });
                }
                $(".noticeShow").empty();
                Common.render({tmpl: $('#noticeTempJs'), data: {data: detailData}, context: '.noticeShow'});
            }
        })
    };
    adjust.getNoticeList = function (page) {
        $.ajax({
            url: "/paike/getNoticeList.do",
            type: "post",
            dataType: "json",
            data: {
                term: $("#term").val(),
                gradeId: $("#gradeId").val(),
                type: $("#typeSelect").val(),
                page: page,
                pageSize: 15
            },
            success: function (data) {
                var option = {
                    total: data.total,
                    pagesize: data.pageSize,
                    currentpage: data.page,
                    operate: function (totalPage) {
                        $('.page-index span').each(function () {
                            $(this).off("click");
                            $(this).click(function () {
                                adjust.getNoticeList($(this).text());
                            });
                        });
                        $('.first-page').off("click");
                        $('.first-page').click(function () {
                            adjust.getNoticeList(1);
                        });
                        $('.last-page').off("click");
                        $('.last-page').click(function () {
                            adjust.getNoticeList(totalPage);
                        });
                    }
                };
                Paginator.initPaginator(option);
                $("#noticeListShow").empty();
                Common.render({tmpl: $('#noticeListTempJs'), data: {data: data.list}, context: '#noticeListShow'});
            }
        })
    };
    adjust.getNoticeList(1);
    ///==============================================添加弹出页=========================================================

    var week = 1;
    adjust.generalWeekList1 = function (allweek) {
        var curweek = Number($("#curweek").val());
        week = curweek;
        var weekList = [];
        weekList.push({value: curweek, text: "第" + curweek + "周(本周)"});
        for (var i = curweek + 1; i <= allweek; i++) {
            weekList.push({value: i, text: "第" + i + "周"});
        }
        return weekList;
    };
    adjust.generalWeekList2 = function (startWeek) {
        var allweek = Number($("#allweek").val());
        var weekList = [];
        for (var i = startWeek + 1; i <= allweek; i++) {
            weekList.push({value: i, text: "第" + i + "周"});
        }
        return weekList;
    };
    $(document).ready(function () {
        $("body").on("change", "#CUR-select", function () {
            if ($(this).val() == 0) {
                $("#type0").show();
                $("#type1").hide();
                $("#type2").hide();
            }
            else if ($(this).val() == 1) {
                $("#type0").hide();
                $("#type1").show();
                $("#type2").hide();
            }
            else if ($(this).val() == 2) {
                $("#type0").hide();
                $("#type1").hide();
                $("#type2").show();
            }
        });
        //关闭
        $("body").on("click", ".update-X", function () {
            $(".bg").hide();
            $(".adjust-CUR").hide();
        });
        //取消
        $("body").on("click", ".C-quxiao", function () {
            $(".bg").hide();
            $(".adjust-CUR").hide();
        });
        //确定
        $("body").on("click", ".C-queding", function () {
            var index = Number($("#CUR-select").val());
            zoubanNoticeDTO.type_str = index;
            $("#twoWeek").hide();
            if (index == 0) {
                if ($("#weekShow1_1").val() == null) {
                    alert("没有教学周");
                    return;
                }
                $(".tab-linshi").show();
                $(".tab-kuazhou").hide();
                $("#left1").css("display","block");
                $("#left2").css("display","none");
                $(".typeShow").text("临时周内调课--" + $("#weekShow1_1").find("option:selected").text());
                choosedStartWeek = Number($("#weekShow1_1").val());
            }
            else if (index == 1) {
                if ($("#weekShow2_1").val() == null) {
                    alert("没有教学周");
                    return;
                }
                if ($("#weekShow2_1").val() == $("#weekShow2_2").val()) {
                    alert("请选择周内调课");
                    return;
                }
                $(".firstWeek").text($("#weekShow2_1").find("option:selected").text());
                $(".secondWeek").text($("#weekShow2_2").find("option:selected").text());
                $(".tab-linshi").show();
                $(".tab-kuazhou").hide();
                $("#left1").css("display","none");
                $("#left2").css("display","block");
                $(".typeShow").text("临时跨周调课--" + $("#weekShow2_1").find("option:selected").text()+"和" +
                $("#weekShow2_2").find("option:selected").text());
                choosedStartWeek = Number($("#weekShow2_1").val());
            }
            else if (index == 2) {
                if ($("#weekShow3_1").val() == null) {
                    alert("没有教学周");
                    return;
                }
                if ($("#weekShow3_1").val() == $("#weekShow3_2").val()) {
                    alert("请选择周内调课");
                    return;
                }
                $(".tab-linshi").show();
                $(".tab-kuazhou").hide();
                $("#left1").css("display","block");
                $("#left2").css("display","none");
                $(".typeShow").text("长期调课--" + $("#weekShow3_1").find("option:selected").text() + "至" +
                $("#weekShow3_2").find("option:selected").text());
                choosedStartWeek = Number($("#weekShow3_1").val());
            }
            $(".bg").hide();
            $(".adjust-CUR").hide();
            $(".tab-adjust").hide();
            adjust.getTimetableConf();
            $("#adjustType").text($("#CUR-select").find("option:selected").text());
            choosedCourse1=[];
            choosedCourse=[];
        });
        $("body").on("change", "#weekShow2_1", function () {
            $("#weekShow2_2").empty();
            Common.render({
                tmpl: $('#weekTempJs'),
                data: {data: adjust.generalWeekList2(Number($(this).val()))},
                context: '#weekShow2_2'
            });
        });
        $("body").on("change", "#weekShow3_1", function () {
            $("#weekShow3_2").empty();
            Common.render({
                tmpl: $('#weekTempJs'),
                data: {data: adjust.generalWeekList2(Number($(this).val()))},
                context: '#weekShow3_2'
            });
        });
    });

    //=====================================================临时周内调课===================================================
    $(document).ready(function () {
        $("#groupShow1").hide();

        $("body").on("change", "#courseTypeSelect", function () {
            var type = $(this).val();
            if (type == 0) {
                $("#groupShow1").hide();
                adjust.showClassList();
            }
            else if (type == 1) {
                $("#groupShow1").show();
                adjust.showGroupClass();
            }
        });
        //筛选
        $("body").on("click", ".selectBtn", function () {
            //调取
            if ($("#courseTypeSelect").val() == 0) {
                adjustMode = 0;
                $("#classShow1").attr("disabled", true);
                $("#classShow4").text($("#classShow1").find("option:selected").text());
            }
            else {
                adjustMode = 1;
                var classNames = "";
                $("#classShow1 option").each(function (i) {
                    classNames += this.text + " ";
                });
                $("#classShow4").text(classNames);
            }
            var classIds = "";
            if (adjustMode == 0)//非走班，班级只有一个
            {
                classIds = $("#classShow1").val();
            }
            else {
                $("#classShow1 option").each(function (i) {
                    classIds += this.value + ",";
                });
                if (classIds.length > 0) {
                    classIds = classIds.substring(0, classIds.length - 1);
                }
            }
            weekChoose=0;
            waitCourse = [];
            waitCourse1 = [];
            adjustNotice = [];
            $("#waitCourseShow").empty();
            $("#waitCourseShow2").empty();
            $("#waitCourseShow3").empty();
            if (Number($("#CUR-select").val()) == 2)//长期调课
            {
                var mode = 0;
                if (adjustMode == 0)
                    mode = 2;
                adjust.checkGroupAdjust(classIds, mode);
            }
            else if (Number($("#CUR-select").val()) == 0){
                $(".selectBtn").attr("disabled", true);
                $("#groupShow1").attr("disabled", true);
                $("#courseTypeSelect").attr("disabled", true);
                adjust.generalAdjustTable(classIds,choosedStartWeek);
            }
            else{
                $("#twoWeek").show();
                $(".selectBtn").attr("disabled", true);
                $("#groupShow1").attr("disabled", true);
                $("#courseTypeSelect").attr("disabled", true);
                adjust.generalAdjustTable(classIds,Number($("#weekShow2_2").val()));
                adjust.generalAdjustTable(classIds,Number($("#weekShow2_1").val()));
                weekChoose=1;
            }
        });
        //发布课表
        $("body").on("click", "#publish_course", function () {
            if (waitCourse.length > 0) {
                alert("课表未排完，无法发布");
                return;
            }
            if (adjustNotice.length == 0) {
                alert("课表无变化，无法发布");
                return;
            }
            $("#noticeName").val("");
            $("#notice-des").val("");
            $(".tab-linshi").hide();
            $(".tab-lesson").show();
            var today = new Date();
            $("#time").text(today.getFullYear() + "-" + (today.getMonth() + 1) + "-" + today.getDate())
        });
        //取消
        $("body").on("click", "#cancel,#back", function ()
        {
            if (waitCourse.length > 0 || adjustNotice.length > 0) {
                if(confirm("课表调课未完成，取消将撤销一切修改，是否取消?")==true){
                    $("#groupShow1").removeAttr("disabled");
                    $(".selectBtn").removeAttr("disabled");
                    $("#courseTypeSelect").removeAttr("disabled");
                    $("#classShow1").removeAttr("disabled");
                    if (Number(zoubanNoticeDTO.type_str) == 0 || Number(zoubanNoticeDTO.type_str) == 2) {
                        $("#waitCourseShow").empty();
                    }
                    else
                    {
                        $("#waitCourseShow2").empty();
                        $("#waitCourseShow3").empty();
                    }

                    $(".noticeShow").empty();
                    adjust.generalTableData(0, 0,[]);
                    if(this.id=="back")
                    {
                        $(".tab-linshi").hide();
                        $(".tab-adjust").show();
                        $("#tableShow2").empty();
                    }
                }
                return;
            }
            $("#groupShow1").removeAttr("disabled");
            $(".selectBtn").removeAttr("disabled");
            $("#courseTypeSelect").removeAttr("disabled");
            $("#classShow1").removeAttr("disabled");
            if (Number(zoubanNoticeDTO.type_str) == 0 || Number(zoubanNoticeDTO.type_str) == 2) {
                waitCourse=[];
                $("#waitCourseShow").empty();
                Common.render({tmpl: $('#waitCourseTempJs'), data: {data: waitCourse}, context: '#waitCourseShow'});
            }
            else
            {
                waitCourse1=[];
                $("#waitCourseShow2").empty();
                waitCourse=[];
                $("#waitCourseShow3").empty();
            }

            $(".noticeShow").empty();
            Common.render({tmpl: $('#noticeTempJs'), data: {data: []}, context: '.noticeShow'});
            if(this.id=="back")
            {
                $(".tab-linshi").hide();
                $(".tab-adjust").show();
                $("#tableShow2").empty();
            }
            adjust.generalTableData(0, 0,[]);
            zoubanNoticeDTO={};
        });

        //走班详情显示
        $("body").on("mouseover", ".Rclass-main-ZB", function () {
            $(this).children("div").show();
        });
        //走班详情隐藏
        $("body").on("mouseout", ".Rclass-main-ZB", function () {
            $(this).children("div").hide();
        });
        //走班详情显示
        $("body").on("mouseover", ".Rclass-main-PE", function () {
            $(this).children("div").show();
        });
        //走班详情隐藏
        $("body").on("mouseout", ".Rclass-main-PE", function () {
            $(this).children("div").hide();
        });
        //询问是否排在调研时段
        $("body").on("click",".Rclass-main-JY",function(){
            if (confirm("该时段有教研活动,确定要安排教研活动吗？")) {
                var classIds = "";
                if (adjustMode == 0) {
                    classIds = $("#classShow1").val();
                }
                else {
                    $("#classShow1 option").each(function (i) {
                        classIds += this.value + ",";
                    });
                    if (classIds.length == 0) {
                        alert("没有班级");
                        return;
                    }
                    classIds = classIds.substring(0, classIds.length - 1);
                }
                var selectXIndex = Number($(this).attr("x"));
                var selectYIndex = Number($(this).attr("y"));
                var week=choosedStartWeek;
                if(weekChoose==1)
                {
                    week=Number($("#weekShow2_1").val());
                }
                else if(weekChoose==2)
                {
                    week=Number($("#weekShow2_2").val());
                }
                adjust.addCourse(classIds, selectXIndex, selectYIndex, choosedCourseId, adjustMode,week);
            }
        });
        //删除走班课
        $("body").on("click", ".removeCourseX", function () {
            var x = $(this).parent().parent().parent().attr("x");
            var y = $(this).parent().parent().parent().attr("y");
            var courseId = $(this).attr("courseId");
            var classIds = "";
            $("#classShow1 option").each(function (i) {
                classIds += this.value + ",";
            });
            if (classIds.length == 0) {
                alert("没有班级");
                return;
            }
            classIds = classIds.substring(0, classIds.length - 1);
            var courseDetail = {
                courseId: courseId,
                courseName: $(this).parent().children().eq(0).text(),
                teacherName: $(this).parent().children().eq(1).text(),
                roomName: $(this).parent().children().eq(2).text(),
                teacherId: $(this).attr("tid"),
                roomId: $(this).attr("roomId"),
                x: x,
                y: y
            };
            var week=choosedStartWeek;
            if(weekChoose==1)
            {
                week=Number($("#weekShow2_1").val());
            }
            else if(weekChoose==2)
            {
                week=Number($("#weekShow2_2").val());
            }
            adjust.deleteCourse(x,y,courseId,courseDetail,classIds,week);
        });
        //非走班删除课程
        $("body").on("click", ".feizoubanDel", function () {
            if($(this).attr("cid")=="null")
            {
                return;
            }
            var courseDetail = {
                courseId: $(this).attr("cid"),
                courseName: $(this).attr("cname"),
                teacherName: $(this).attr("tname"),
                roomName: "",
                teacherId: $(this).attr("tid"),
                roomId: $(this).attr("rid"),
                subjectId:$(this).attr("sid"),
                x: $(this).attr("x"),
                y: $(this).attr("y")
            };
            var week=choosedStartWeek;
            if(weekChoose==1)
            {
                week=Number($("#weekShow2_1").val());
            }
            else if(weekChoose==2)
            {
                week=Number($("#weekShow2_2").val());
            }

            adjust.deleteCourse($(this).attr("x"),$(this).attr("y"),$(this).attr("cid"),courseDetail,$("#classShow1").val(),week);
        });
        //暂存区课程点击
        $("body").on("click", "#waitCourseShow li", function () {
            if($(this).hasClass("adjust-BK"))
            {
                $(this).removeClass("adjust-BK");
                adjust.generalTableData(0, 0,choosedCourse);
                //arranging.showDetailTable($("#groupShow2").val());
                groupStudy=[];
                return;
            }
            getGroupStudy($(this).attr("sid"));
            $(this).addClass("adjust-BK").siblings().removeClass("adjust-BK");
            var week=choosedStartWeek;
            adjust.getAdjustAvailablePoint($(this).attr("cid"), $(this).attr("rid"), $(this).attr("tid"),
                $(this).attr("x"), $(this).attr("y"), adjustMode,week);
            oldWeek=week;
            oldX=Number($(this).attr("x"));
            oldY=Number($(this).attr("y"));
            choosedCourseId = $(this).attr("cid");
        });
        //暂存区课程点击
        $("body").on("click", "#waitCourseShow2 li", function () {
            if( $(this).hasClass("adjust-BK"))
            {
                $(this).removeClass("adjust-BK");
                var classIds = "";
                if (adjustMode == 0)//非走班，班级只有一个
                {
                    classIds = $("#classShow1").val();
                }
                else {
                    $("#classShow1 option").each(function (i) {
                        classIds += this.value + ",";
                    });
                    if (classIds.length > 0) {
                        classIds = classIds.substring(0, classIds.length - 1);
                    }
                }
                adjust.generalAdjustTable(classIds,Number($("#weekShow2_2").val()));
                groupStudy=[];
                return;
            }
            $(this).addClass("adjust-BK").siblings().removeClass("adjust-BK");
            weekChoose=2;
            var week=Number($("#weekShow2_2").val());
            oldWeek=Number($("#weekShow2_1").val());;
            oldX=Number($(this).attr("x"));
            oldY=Number($(this).attr("y"));
            $(".firstWeek").removeClass("c-kebiao");
            $(".secondWeek").addClass("c-kebiao");
            adjust.getAdjustAvailablePoint($(this).attr("cid"), $(this).attr("rid"), $(this).attr("tid"),
                $(this).attr("x"), $(this).attr("y"), adjustMode,week);
            choosedCourseId = $(this).attr("cid");
        });
        //暂存区课程点击
        $("body").on("click", "#waitCourseShow3 li", function () {
            if( $(this).hasClass("adjust-BK"))
            {
                $(this).removeClass("adjust-BK");
                var classIds = "";
                if (adjustMode == 0)//非走班，班级只有一个
                {
                    classIds = $("#classShow1").val();
                }
                else {
                    $("#classShow1 option").each(function (i) {
                        classIds += this.value + ",";
                    });
                    if (classIds.length > 0) {
                        classIds = classIds.substring(0, classIds.length - 1);
                    }
                }
                adjust.generalAdjustTable(classIds,Number($("#weekShow2_1").val()));
                groupStudy=[];
                return;
            }
            $(this).addClass("adjust-BK").siblings().removeClass("adjust-BK");
            weekChoose=1;
            var week=Number($("#weekShow2_1").val());
            oldWeek=Number($("#weekShow2_2").val());
            oldX=Number($(this).attr("x"));
            oldY=Number($(this).attr("y"));
            $(".firstWeek").addClass("c-kebiao");
            $(".secondWeek").removeClass("c-kebiao");
            adjust.getAdjustAvailablePoint($(this).attr("cid"), $(this).attr("rid"), $(this).attr("tid"),
                $(this).attr("x"), $(this).attr("y"), adjustMode,week);
            choosedCourseId = $(this).attr("cid");
        });


        //确认排课
        $("body").on("click", ".Rclass-main-Green", function () {
            if (confirm("确定要排在该位置吗？")) {
                var classIds = "";
                if (adjustMode == 0) {
                    classIds = $("#classShow1").val();
                }
                else {
                    $("#classShow1 option").each(function (i) {
                        classIds += this.value + ",";
                    });
                    if (classIds.length == 0) {
                        alert("没有班级");
                        return;
                    }
                    classIds = classIds.substring(0, classIds.length - 1);
                }
                var selectXIndex = Number($(this).attr("x"));
                var selectYIndex = Number($(this).attr("y"));
                var week=choosedStartWeek;
                if(weekChoose==1)
                {
                    week=Number($("#weekShow2_1").val());
                }
                else if(weekChoose==2)
                {
                    week=Number($("#weekShow2_2").val());
                }
                adjust.addCourse(classIds, selectXIndex, selectYIndex, choosedCourseId, adjustMode,week);
            }
        });
        //跨周第一周
        $("body").on("click",".firstWeek",function(){
            if ($("#courseTypeSelect").val() == 0) {
                adjustMode = 0;
                $("#classShow1").attr("disabled", true);
                $("#classShow4").text($("#classShow1").find("option:selected").text());
            }
            else {
                adjustMode = 1;
                var classNames = "";
                $("#classShow1 option").each(function (i) {
                    classNames += this.text + " ";
                });
                $("#classShow4").text(classNames);
            }
            var classIds = "";
            if (adjustMode == 0)//非走班，班级只有一个
            {
                classIds = $("#classShow1").val();
            }
            else {
                $("#classShow1 option").each(function (i) {
                    classIds += this.value + ",";
                });
                if (classIds.length > 0) {
                    classIds = classIds.substring(0, classIds.length - 1);
                }
            }
            $(".selectBtn").attr("disabled", true);
            $("#groupShow1").attr("disabled", true);
            $("#courseTypeSelect").attr("disabled", true);
            weekChoose=1;
            if(choosedCourse.length==0)
                adjust.generalAdjustTable(classIds,Number($("#weekShow2_1").val()));
            else
                adjust.generalTableData(0, adjustMode,choosedCourse);
        });
        //跨周第二周
        $("body").on("click",".secondWeek",function(){
            if ($("#courseTypeSelect").val() == 0) {
                adjustMode = 0;
                $("#classShow1").attr("disabled", true);
                $("#classShow4").text($("#classShow1").find("option:selected").text());
            }
            else {
                adjustMode = 1;
                var classNames = "";
                $("#classShow1 option").each(function (i) {
                    classNames += this.text + " ";
                });
                $("#classShow4").text(classNames);
            }
            var classIds = "";
            if (adjustMode == 0)//非走班，班级只有一个
            {
                classIds = $("#classShow1").val();
            }
            else {
                $("#classShow1 option").each(function (i) {
                    classIds += this.value + ",";
                });
                if (classIds.length > 0) {
                    classIds = classIds.substring(0, classIds.length - 1);
                }
            }
            $(".selectBtn").attr("disabled", true);
            $("#groupShow1").attr("disabled", true);
            $("#courseTypeSelect").attr("disabled", true);
            weekChoose=2;
            if(choosedCourse1.length==0)
                adjust.generalAdjustTable(classIds,Number($("#weekShow2_2").val()));
            else
                adjust.generalTableData(0, adjustMode,choosedCourse1);
        });
        $("body").on("click",".linshi-top span",function () {
            $(this).addClass("c-kebiao").siblings().removeClass("c-kebiao");
        });
    });
    //检测是否可以长期调课
    adjust.checkGroupAdjust=function(classId,mode)
    {
        $.ajax({
            url:"/paike/groupAdjust.do",
            type:"post",
            dataType:"json",
            data:{
                term:$("#term").val(),
                weekStart:Number($("#weekShow3_1").val()),
                weekEnd:Number($("#weekShow3_2").val()),
                classId:classId,
                mode:mode
            },
            success:function(data)
            {
                if(data.result=="FAILD")
                {
                    alert("该区段内课表不一致，不能执行长期调课");
                }
                else{
                    $(".selectBtn").attr("disabled", true);
                    $("#classShow1").attr("disabled", true);
                    $("#groupShow1").attr("disabled", true);
                    $("#courseTypeSelect").attr("disabled", true);
                    adjust.generalAdjustTable(classId,choosedStartWeek);
                }
            }
        })
    };
    adjust.deleteCourse=function(x,y,cid,courseDetail,classIds,week)
    {
        if (confirm("确定移除该课程吗？")) {
            $.ajax({
                url: "/paike/adjustCourseRemove.do",
                type: "post",
                dataType: "json",
                data: {
                    term: $("#term").val(),
                    classId: classIds,
                    xIndex: x,
                    yIndex: y,
                    courseId: cid,
                    index: 2,
                    groupId: "",
                    week: week
                },
                success: function (data) {
                    if(weekChoose==2)
                        choosedCourse1 = data.courseList;
                    else
                        choosedCourse = data.courseList;
                    availablePoint = [];
                    adjust.generalTableData(0, adjustMode,data.courseList);
                    if(weekChoose==2)
                        waitCourse1.push(courseDetail);
                    else
                        waitCourse.push(courseDetail);
                    if (Number(zoubanNoticeDTO.type_str) == 0 || Number(zoubanNoticeDTO.type_str) == 2) {
                        $("#waitCourseShow").empty();
                        Common.render({tmpl: $('#waitCourseTempJs'), data: {data: waitCourse}, context: '#waitCourseShow'});
                    }
                    else
                    {
                        if(weekChoose==1) {
                            $("#waitCourseShow2").empty();
                            Common.render({
                                tmpl: $('#waitCourseTempJs'),
                                data: {data: waitCourse},
                                context: '#waitCourseShow2'
                            });
                        }
                        else if(weekChoose==2)
                        {
                            $("#waitCourseShow3").empty();
                            Common.render({
                                tmpl: $('#waitCourseTempJs'),
                                data: {data: waitCourse1},
                                context: '#waitCourseShow3'
                            });
                        }
                    }
                    //通知
                    var className = "";
                    if (adjustMode == 0)
                    {
                        className = $("#classShow1").find("option:selected").text();
                    }
                    else {
                        $("#classShow1 option").each(function (i) {
                            className += this.text + ",";
                        });
                        if (className.length == 0) {
                            alert("没有班级");
                            return;
                        }
                        className = className.substring(0, className.length - 1);
                    }
                    var have = false;
                    if(Number($("#CUR-select").val())==2)
                    {
                        week=Number($("#weekShow3_1").val())+"至"+Number($("#weekShow3_2").val());
                    }
                    for (var i = 0; i < adjustNotice.length; i++) {
                        if (adjustNotice[i].courseId == courseDetail.courseId && adjustNotice[i].oldTime==
                            "第" + week + "周星期" + courseDetail.x + "第" + courseDetail.y + "节") {
                            adjustNotice[i].newTime = "";
                            have = true;
                            break;
                        }
                    }
                    if (!have) {
                        adjustNotice.push({
                            courseId: courseDetail.courseId,
                            className: className,
                            courseName: courseDetail.courseName,
                            teacherName: courseDetail.teacherName,
                            oldTime: "第" + week + "周星期" + courseDetail.x + "第" + courseDetail.y + "节",
                            newTime: ""
                        });
                    }
                    $(".noticeShow").empty();
                    Common.render({tmpl: $('#noticeTempJs'), data: {data: adjustNotice}, context: '.noticeShow'});
                }
            });
        }
    };
    //添加课程
    adjust.addCourse = function (classId, xIndex, yIndex, courseId, courseType,week) {
        if (courseType == 0)//非走班
        {
            courseType = 2;
        }
        else if (courseType == 1)//走班
        {
            courseType = 0;
        }
        $.ajax({
            url: "/paike/addAdjustCourse.do",
            type: "post",
            dataType: "json",
            data: {
                term: $("#term").val(),
                classId: classId,
                xIndex: xIndex,
                yIndex: yIndex,
                courseId: courseId,
                week: week,
                courseType: courseType//0走班 2非走班
            },
            success: function (data) {
                if(weekChoose==2)
                    choosedCourse1 = data.courseList;
                else
                    choosedCourse = data.courseList;
                availablePoint = [];
                adjust.generalTableData(0, adjustMode,data.courseList);
                //从左侧去掉
                if(weekChoose==1) {
                    for (var i = 0; i < waitCourse1.length; i++) {
                        if (waitCourse1[i].courseId == courseId) {
                            waitCourse1.splice(i, 1);
                            break;
                        }
                    }
                }
                else
                {
                    for (var i = 0; i < waitCourse.length; i++) {
                        if (waitCourse[i].courseId == courseId) {
                            waitCourse.splice(i, 1);
                            break;
                        }
                    }
                }

                if (Number(zoubanNoticeDTO.type_str) == 0 || Number(zoubanNoticeDTO.type_str) == 2) {
                    $("#waitCourseShow").empty();
                    Common.render({tmpl: $('#waitCourseTempJs'), data: {data: waitCourse}, context: '#waitCourseShow'});
                }
                else
                {
                    if(weekChoose==2) {
                        $("#waitCourseShow2").empty();
                        Common.render({
                            tmpl: $('#waitCourseTempJs'),
                            data: {data: waitCourse},
                            context: '#waitCourseShow2'
                        });
                    }
                    else if(weekChoose==1)
                    {
                        $("#waitCourseShow3").empty();
                        Common.render({
                            tmpl: $('#waitCourseTempJs'),
                            data: {data: waitCourse1},
                            context: '#waitCourseShow3'
                        });
                    }
                }

                //通知
                var _oldweek=oldWeek;
                if(Number($("#CUR-select").val())==2)
                {
                    week=Number($("#weekShow3_1").val())+"至"+Number($("#weekShow3_2").val());
                    _oldweek=Number($("#weekShow3_1").val())+"至"+Number($("#weekShow3_2").val());
                }
                for (var i = 0; i < adjustNotice.length; i++) {//todo
                    if (adjustNotice[i].courseId == courseId &&
                        adjustNotice[i].oldTime == "第" + _oldweek + "周星期" + oldX + "第" + oldY + "节") {
                        adjustNotice[i].newTime = "第" + week + "周星期" + xIndex + "第" + yIndex + "节";
                        if (adjustNotice[i].newTime == adjustNotice[i].oldTime) {
                            adjustNotice.splice(i, 1);
                        }
                        break;
                    }
                }
                $(".noticeShow").empty();
                Common.render({tmpl: $('#noticeTempJs'), data: {data: adjustNotice}, context: '.noticeShow'});
            }
        })
    };
    //获取班级列表
    adjust.getClassList = function () {
        $.ajax({
            url: "/paike/getGradeClassList.do",
            type: "post",
            dataType: "json",
            data: {
                gradeId: $("#gradeId").val()
            },
            success: function (data) {
                gradeClass = data;
                adjust.showClassList();
            }
        });
    };
    //获取班级以及分段
    adjust.getDuanClassList = function () {
        $.ajax({
            url: "/paike/getGradeDuanClass.do",
            type: "post",
            dataType: "json",
            data: {
                term: $("#year").val(),
                gradeId: $("#gradeId").val()
            },
            success: function (data) {
                gradeGroupClass = data;
            }
        });
    };
    //显示班级列表
    adjust.showClassList = function () {
        var classList = [];
        if (gradeClass.length > 0) {
            var map = gradeClass[0].classInfo;
            $.each(map, function (key, value) {
                classList.push({classId: key, className: value});
            });
        }
        $("#classShow1").empty();
        Common.render({tmpl: $('#classTempJs1'), data: {data: classList}, context: '#classShow1'});
    };
    //显示班级以及分段列表
    adjust.showGroupClass = function () {
        var groupList = [];
        if (gradeGroupClass.length > 0) {
            var list = gradeGroupClass[0].groupInfo;
            for (var i = 0; i < list.length; i++) {
                groupList.push({
                    groupId: list[i].groupId, groupName: "第" + list[i].group + "段",
                    groupIndex: list[i].group, classList: list[i].courseInfo
                });
            }
        }
        $("#groupShow1").empty();
        Common.render({tmpl: $('#groupTempJs1'), data: {data: groupList}, context: '#groupShow1'});
        var classList = [];
        if (groupList.length > 0) {
            var map = groupList[0].classList;
            $.each(map, function (key, values) {
                classList.push({classId: key, className: values});
            });
        }
        $("#classShow1").empty();
        Common.render({tmpl: $('#classTempJs1'), data: {data: classList}, context: '#classShow1'});
    };
    adjust.getClassList();
    adjust.getDuanClassList();
    //获取课表配置
    adjust.getTimetableConf = function () {
        var gradeId = $("#gradeId").val();
        $.ajax({
            url: "/paike/findCourseConf.do",
            type: "post",
            dataType: "json",
            data: {
                gradeId: $("#gradeId").val(),
                term: $("#year").val()
            },
            success: function (data) {
                //本地暂存
                courseConfDTO = data;
                $("#courseConfId").val(data.id);
                adjust.generalTableData(0, 0,[]);
            }
        })
    };
    adjust.generalAdjustTable = function (classIds,week) {
        $.ajax({
            url: "/paike/generalAdjustTable.do",
            type: "post",
            dataType: "json",
            data: {
                term: $("#term").val(),
                classId: classIds,
                week: week
            },
            success: function (data) {
                if(week==Number($("#weekShow2_2").val()))
                    choosedCourse1 = data.courseList;
                else
                    choosedCourse = data.courseList;
                availablePoint = [];
                adjust.generalTableData(0, adjustMode,data.courseList);
            }
        });
    };
    var adjustMode = 0;//0非走班调课，1走班调课
    var groupStudy = [];//集体教研
    var availablePoint = [];//可用点
    var choosedCourse = [];//已经选好的课程
    var choosedCourse1 = [];//已经选好的课程,跨周第二周
    var waitCourse = [];//被移除的课程
    var waitCourse1 = [];//被移除的课程,跨周第二周
    var adjustNotice = [];//生成调课通知
    var gradeGroupClass = [];//段、班级组合列表
    var gradeClass = [];//班级列表
    var courseConfDTO = {};//课表结构
    var unArrangeCourse = [];//未排课程
    var choosedStartWeek = 1;
    var choosedCourseId = "";//左侧暂存区点击的课程id
    var weekChoose=0;//0其他，1第一周，2第二周
    var oldWeek=1;//原始周
    var oldX=1;//原始星期
    var oldY=1;//原始第几节  以上三个变量用于跨周调课使用
    function getGroupStudy(subjectId) {
        groupStudy = [];
        for (var i = 0; i < courseConfDTO.events.length; i++) {
            var map = courseConfDTO.events[i].groupStudy;
            $.each(map, function (key, values) {
                if (values.id == subjectId) {
                    groupStudy.push({x: courseConfDTO.events[i].xIndex, y: courseConfDTO.events[i].yIndex});
                }
            });
        }
    }

    //获取可用时间点
    adjust.getAdjustAvailablePoint = function (courseId, classRoomId, teacherId, x, y, type,week) {
        $.ajax({
            url: "/paike/getAdjustAvailablePoint.do",
            type: "post",
            dataType: "json",
            data: {
                term: $("#term").val(),
                classId: $("#classShow1").val(),
                courseId: courseId,
                classRoomId: classRoomId,
                teacherId: teacherId,
                courseConfId: $("#courseConfId").val(),
                type: type,
                x: x,
                y: y,
                week: week
            },
            success: function (data) {
                availablePoint = data;
                if(weekChoose==2)
                    adjust.generalTableData(1, adjustMode,choosedCourse1);
                else
                    adjust.generalTableData(1, adjustMode,choosedCourse);
            }
        });
    };
    /**
     * 生成课表数据
     * @param state //0查看，1选课
     */
    adjust.generalTableData = function (state, mode, choosedCourse) {
        //getGroupStudy
        var paikeDate = {};
        paikeDate.id = courseConfDTO.id;
        paikeDate.schoolId = courseConfDTO.schoolId;
        paikeDate.term = courseConfDTO.term;
        paikeDate.gradeId = courseConfDTO.gradeId;
        paikeDate.classDays = courseConfDTO.classDays.sort();
        paikeDate.classCount = courseConfDTO.classCount;
        paikeDate.classTime = courseConfDTO.classTime;
        paikeDate.state = state;//0查看，1选课
        if (availablePoint.length == 0) {
            paikeDate.state = 0;
        }
        paikeDate.detail = [];
        var todayDate = new Date();
        var todayWeek = todayDate.getDay();
        if (todayWeek == 0) {
            todayWeek = 7;
        }
        for (var i = 0; i < paikeDate.classDays.length; i++) {
            var clickWeek = 0;//0可以点击 1不可点击
            if(weekChoose==0) {
                if (choosedStartWeek == Number($("#curweek").val()) && todayWeek >= paikeDate.classDays[i]) {
                    clickWeek = 1;
                }
            }
            else if(weekChoose==1)
            {
                if (Number($("#weekShow2_1").val()) == Number($("#curweek").val()) && todayWeek >= paikeDate.classDays[i]) {
                    clickWeek = 1;
                }
            }
            else if(weekChoose==2)
            {
                if (Number($("#weekShow2_2").val()) == Number($("#curweek").val()) && todayWeek >= paikeDate.classDays[i]) {
                    clickWeek = 1;
                }
            }
            var x = paikeDate.classDays[i];
            for (var j = 1; j <= paikeDate.classCount; j++) {
                var y = j;
                var type = 3;//0走班  2非走班 3 无课
                var courseList = [];
                var isOk = 0;//0不可排课，1可以排课,2 集体调研  询问
                var fb = [];//不可排课事件
                var gs = [];//集体调研事件
                var te = [];//个人事务
                var mode = mode;//0非走班调课 1走班调课
                var subjectName = "";
                //遍历availablePoint
                for (var m = 0; m < availablePoint.length; m++) {
                    if (availablePoint[m].x == x && availablePoint[m].y == y) {
                        isOk = 1;
                        break;
                    }
                }
                //遍历choosedCourse
                for (var n = 0; n < choosedCourse.length; n++) {
                    if (choosedCourse[n].xIndex == x && choosedCourse[n].yIndex == y && choosedCourse[n].type!=3) {
                        courseList = choosedCourse[n].courseIdList;
                        type = choosedCourse[n].type;
                        if(type==5) {
                            courseList[0].courseId="null";
                            type = 2;
                        }
                        subjectName = choosedCourse[n].subjectName;
                        break;
                    }
                }
                //遍历 groupStudy
                for (var p = 0; p < groupStudy.length; p++) {
                    if (groupStudy[p].x == x && groupStudy[p].y == y && isOk == 1) {
                        isOk = 2;
                        break;
                    }
                }
                //遍历 courseConfDTO2
                for (var k = 0; k < courseConfDTO.events.length; k++) {
                    if (courseConfDTO.events[k].xIndex == x && courseConfDTO.events[k].yIndex == y) {
                        fb = courseConfDTO.events[k].forbidEvent;
                        gs = courseConfDTO.events[k].groupStudy;
                        te = courseConfDTO.events[k].personEvent;
                        break;
                    }
                }
                paikeDate.detail.push({
                    x: x, y: y, type: type, courseList: courseList, isOk: isOk,
                    fb: fb, gs: gs, te: te, mode: mode, subjectName: subjectName, week: clickWeek
                });
            }
        }
        for (var n = 0; n < choosedCourse.length; n++) {
            if(choosedCourse[n].type==3) {
                for (var k = 0; k < paikeDate.detail.length; k++) {
                    var obj = paikeDate.detail[k];
                    if (obj.x==choosedCourse[n].xIndex && obj.y==choosedCourse[n].yIndex)
                    {
                        for(var p=0;p<choosedCourse[n].courseIdList.length;p++) {
                            var have=false;
                            for(var m=0;m<obj.courseList.length;m++)
                            {
                                if(obj.courseList[m].courseId==choosedCourse[n].courseIdList[p].courseId)
                                {
                                    have=true;
                                }
                            }
                            if(!have)
                                obj.courseList.push(choosedCourse[n].courseIdList[p]);
                        }
                        obj.subjectName+="/选修";
                        obj.mode=0;
                        break;
                    }
                }
            }
        }
        $("#tableShow2").empty();
        Common.render({tmpl: $('#tableTempJs2'), data: {data: paikeDate}, context: '#tableShow2'});
        $(".Rclass-ZB-SJ").css("left", $(".ZB_table_base").width() + 2);
        $(".Rclass-ZB-SJJ").css("left", $(".ZB_table_base").width() + 1);
        $(".Rclass-ZB-TC").css("left", $(".ZB_table_base").width() + 24);
        $(".Rclass-JY-SJ").css("left", $(".ZB_table_base").width() + 2);
        $(".Rclass-JY-SJJ").css("left", $(".ZB_table_base").width() + 1);
        $(".Rclass-JY-TC").css("left", $(".ZB_table_base").width() + 24);
    };
    //=====================================================临时跨周调课===================================================
    //=====================================================长期调课===================================================
    //===========================================发布课表==============================================================
    $(document).ready(function () {
        //确定发布
        $("body").on("click", "#publish_ok", function () {
            zoubanNoticeDTO.id = "";
            zoubanNoticeDTO.week = "";
            zoubanNoticeDTO.tableIds = "";
            zoubanNoticeDTO.term = $("#term").val();
            zoubanNoticeDTO.gradeId = $("#gradeId").val();
            zoubanNoticeDTO.name = $("#noticeName").val();
            if(zoubanNoticeDTO.name==""||zoubanNoticeDTO.name==null)
            {
                alert("调课通知名称未填写");
                return;
            }
            zoubanNoticeDTO.time = $("#time").text();
            zoubanNoticeDTO.weekList = [];
            if (zoubanNoticeDTO.type_str == 0) {
                zoubanNoticeDTO.weekList.push(choosedStartWeek);
                zoubanNoticeDTO.type_str = "0";
            }
            else if (zoubanNoticeDTO.type_str == 1) {
                zoubanNoticeDTO.weekList.push(Number($("#weekShow2_1").val()));
                zoubanNoticeDTO.weekList.push(Number($("#weekShow2_2").val()));
                zoubanNoticeDTO.type_str = "1";
            }
            else if(zoubanNoticeDTO.type_str == 2) {
                for(var start=Number($("#weekShow3_1").val());start<=Number($("#weekShow3_2").val());start++)
                {
                    zoubanNoticeDTO.weekList.push(start);
                }
                zoubanNoticeDTO.type_str = "2";
            }
            zoubanNoticeDTO.noticeDetails = [];
            for (var i = 0; i < adjustNotice.length; i++) {
                zoubanNoticeDTO.noticeDetails.push({
                    id: "", cl: adjustNotice[i].className, co: adjustNotice[i].courseName,
                    te: adjustNotice[i].teacherName, ot: adjustNotice[i].oldTime, nt: adjustNotice[i].newTime
                });
            }
            zoubanNoticeDTO.descript = $("#notice-des").val();
            if(zoubanNoticeDTO.descript==""||zoubanNoticeDTO.descript==null)
            {
                alert("调课通知名称未填写");
                return;
            }
            zoubanNoticeDTO.classIds = [];
            zoubanNoticeDTO.classId = $("#classShow1").val();
            if (adjustMode == 0)//非走班，班级只有一个
            {
                zoubanNoticeDTO.classIds.push($("#classShow1").val());
            }
            else {
                $("#classShow1 option").each(function (i) {
                    zoubanNoticeDTO.classIds.push(this.value);
                });
            }
            adjust.publishAdjust();
        });
        //取消发布
        $("body").on("click", "#publish_cancel", function () {
            $(".tab-linshi").show();
            $(".tab-lesson").hide();
        });
    });
    adjust.publishAdjust = function () {
        if (confirm("发布后，将优先使用该周课表，确认本次操作吗？")) {
            $.ajax({
                url: "/paike/publishAdjustTable.do",
                type: "post",
                contentType: "application/json",
                data: JSON.stringify(zoubanNoticeDTO),
                success: function (data) {
                    if (data.result == "SUCCESS") {
                        $(".tab-lesson").hide();
                        $(".tab-adjust").show();
                        adjust.getNoticeList(1);
                        $("#groupShow1").removeAttr("disabled");
                        $(".selectBtn").removeAttr("disabled");
                        $("#courseTypeSelect").removeAttr("disabled");
                        $("#classShow1").removeAttr("disabled");

                        if (Number(zoubanNoticeDTO.type_str) == 0 || Number(zoubanNoticeDTO.type_str) == 2) {
                            $("#waitCourseShow").empty();
                        }
                        else
                        {
                            $("#waitCourseShow2").empty();
                            $("#waitCourseShow3").empty();
                        }

                        $(".noticeShow").empty();
                        Common.render({tmpl: $('#noticeTempJs'), data: {data: []}, context: '.noticeShow'});
                        //清空课表
                        $("#tableShow2").empty();
                        zoubanNoticeDTO={};
                    }
                    else {
                        alert("添加失败");
                    }
                },
                error: function (data) {
                    alert(data);
                }
            });
        }
    };
    var zoubanNoticeDTO = {
        id: "",
        term: "",
        gradeId: "",
        type_str: "",
        name: "",
        week: "",
        descript: "",
        time: "",
        tableIds: "",
        noticeDetails: [],
        weekList: [],
        classIds: [],
        classId: ""
    };
});