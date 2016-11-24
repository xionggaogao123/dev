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
    var publishcourse = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    publishcourse.init = function () {


    };

    $(function () {
        $(".right-title  li").click(function () {
            $(this).addClass("cur").siblings("li").removeClass("cur");
            var index = $(this).index();
            if (index == 0) {
                //publishcourse.getGradeClass();
            }
            else if (index == 1) {
                publishcourse.getGradeSubjectCourse();
            }
            else if (index == 2) {
                publishcourse.getSubjectTeacher();
            }
        })
    });


    $(document).ready(function ($) {
        $(".main1-li").click(function () {
            $(".right-main1").show();
            $(".right-main2").hide();
            $(".right-main3").hide();
        });
        $(".main2-li").click(function () {
            $(".right-main2").show();
            $(".right-main1").hide();
            $(".right-main3").hide();
        });
        $(".main3-li").click(function () {
            $(".right-main3").show();
            $(".right-main1").hide();
            $(".right-main2").hide();
        });

        $(".back").click(function () {
            //$(".col-right").hide();
            $(".col-right0").show();
            $(".main21-hide").show();
        });
        $(".backUrl").click(function () {

            window.open('../paike/index.do?version=58&year=' + encodeURI(encodeURI($('#year').val())) + '&gradeId=' +
            $('#gradeId').val(), '_self');
        });
    });
    publishcourse.init();
    //===============================教学班课表=================================================
    //模拟数据
    var gsc = [];
    var subjectList = [];
    var courseList = [];
    var courseTable = {
        /*
         "point": [{x:1,y:1},{x:1,y:2},{x:2,y:3}],
         conf:{
         "id":"03213",
         "schoolId":"08543",
         "term":"2015~2016学年第一学期",
         "gradeId":"43546",
         "classDays":[1,2,3,4,5,6,7],
         "classCount":7,
         "classTime":["8:00~8:45", "8:55~9:40", "10:00~10:45", "10:55~11:40", "14:00~14:45", "14:55~15:40", "16:00~16:45"],
         "events":[]
         },
         "teacher":"王老师",
         "classroom":"A312"*/
    };
    //获取年级学科教学班组合
    publishcourse.getGradeSubjectCourse = function () {
        $.ajax({
            url: "/timetable/getGradeSubjectCourse.do",
            type: "post",
            dataType: "json",
            data: {
                term: $("#year").val(),
                gradeId: $("#gradeId").val()
            },
            success: function (data) {
                subjectList = [];
                if (data) {
                    var map = data.groupInfo;
                    if (map != null) {
                        $.each(map, function (key, value) {
                            subjectList.push({subjectName: key, courseList: value});
                        });
                    }
                }
                $("#subjectShow1").empty();
                Common.render({tmpl: $('#subjectTempJs1'), data: {data: subjectList}, context: '#subjectShow1'});
                courseList = [];
                if (subjectList.length > 0) {
                    var map = subjectList[0].courseList;
                    if (map != null) {
                        $.each(map, function (key, value) {
                            courseList.push({courseId: key, courseName: value});
                        });
                    }
                }
                $("#courseShow1").empty();
                Common.render({tmpl: $('#courseTempJs1'), data: {data: courseList}, context: '#courseShow1'});

                publishcourse.getCourseTimetable();
            }
        });
    };
    //获取教学班课表
    publishcourse.getCourseTimetable = function () {
        $.ajax({
            url: "/timetable/getCourseTimeTable.do",
            type: "post",
            dataType: "json",
            data: {
                year: $("#year").val(),
                term: term,
                courseId: $("#courseShow1").val(),
                gradeId: $("#gradeId").val(),
                week: week
            },
            success: function (data) {
                courseTable.point = data.point;
                courseTable.conf = data.conf;
                courseTable.teacher = data.teacher;
                courseTable.classroom = data.classroom;
                $(".main1-3").empty();
                Common.render({tmpl: $('#courseTempJs'), data: {data: courseTable}, context: '.main1-3'});
                ;
            }
        });
    };

    $(document).ready(function () {

        $("body").on("change", "#subjectShow1", function () {
            var subjectName = $(this).val();
            courseList = [];
            for (var i = 0; i < subjectList.length; i++) {
                if (subjectList[i].subjectName == subjectName) {
                    var map = subjectList[i].courseList;
                    if (map != null) {
                        $.each(map, function (key, value) {
                            courseList.push({courseId: key, courseName: value});
                        });
                    }
                }
            }
            $("#courseShow1").empty();
            Common.render({tmpl: $('#courseTempJs1'), data: {data: courseList}, context: '#courseShow1'});
        });
        $("body").on("click", ".main1-btn1", function () {
            if ($("#courseShow1").val() == null || $("#courseShow1").val() == "") {
                alert("没有教学班");
                return;
            }
            publishcourse.getCourseTimetable();
        });
    });
    //publishcourse.getGradeSubjectCourse();
    //============================================教师课表====================================
    var subjectTeacher = [];
    var teacherList = [];
    var teacherTable = {
        /*
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
         }*/
    };
    publishcourse.getSubjectTeacher = function () {
        $.ajax(
            {
                url: "/timetable/getSubgjectTeacher.do",
                type: "post",
                success: function (data) {
                    subjectTeacher = data;
                    $("#subjectShow3").empty();
                    Common.render({tmpl: $('#subjectTempJs3'), data: {data: subjectTeacher}, context: '#subjectShow3'});
                    teacherList = [];
                    if (subjectTeacher.length > 0) {
                        var map = subjectTeacher[0].teacherList;
                        if (map != null) {
                            $.each(map, function (key, value) {
                                teacherList.push({teacherId: value.id, teacherName: value.name});
                            });
                        }
                    }
                    $("#teacherShow3").empty();
                    Common.render({tmpl: $('#teacherTempJs3'), data: {data: teacherList}, context: '#teacherShow3'});

                    publishcourse.getTeacherTimetable();
                }
            }
        );
    };
    publishcourse.getTeacherTimetable = function () {
        if ($("#teacherShow3").val() == null || $("#teacherShow3").val() == "") {
            alert("没有教师数据!");
            return;
        }
        $.ajax(
            {
                url: "/timetable/getTeacherTimeTable.do",
                type: "post",
                data: {
                    year: $("#year").val(),
                    term: term,
                    teacherId: $("#teacherShow3").val(),
                    gradeId: $("#gradeId").val(),
                    /*type:$("#type").val(),*/
                    week: week
                },
                success: function (data) {
                    teacherTable.conf = data.conf;
                    teacherTable.course = data.course;

                    $("#teacherTableShow").empty();
                    Common.render({
                        tmpl: $('#teacherTableTempJs'),
                        data: {data: teacherTable},
                        context: '#teacherTableShow'
                    });
                }
            }
        );
    };
    $(document).ready(function () {
        //学科变化
        $("body").on("change", "#subjectShow3", function () {
            var subjectId = $(this).val();
            teacherList = [];
            for (var i = 0; i < subjectTeacher.length; i++) {
                if (subjectTeacher[i].subjectId == subjectId) {
                    var map = subjectTeacher[i].teacherMap;
                    if (map != null) {
                        $.each(map, function (key, value) {
                            teacherList.push({teacherId: key, teacherName: value});
                        });
                    }
                }
            }
            $("#teacherShow3").empty();
            Common.render({tmpl: $('#teacherTempJs3'), data: {data: teacherList}, context: '#teacherShow3'});
        });
        //获取详情
        $("body").on("click", ".selectTeacher", function () {
            publishcourse.getTeacherTimetable();
        });
        //导出老师课表
        $("body").on("click", ".exportTeacher", function () {
            if ($("#teacherShow3").val() == null || $("#teacherShow3").val() == "") {
                alert("没有教师数据");
                return;
            }
            window.location = '/timetable/exportTea.do?term=' + encodeURI(encodeURI(term)) + '&gradeId=' + $("#gradeId").val() +
            '&gradeName=' + encodeURI(encodeURI($("#gradeName").val())) + '&teacherId=' + $("#teacherShow3").val() + '&teacherName=' +
            encodeURI(encodeURI($("#teacherShow3").find("option:selected").text())) + '&type=0&week=' + week;
        });
    });
//=========================================行政班课表==================================
    var gradeClass = [];
    var classList = [];
    var classTable = {
        /*
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
         }*/
    };
    var detailWeek = [];
    var detailTime = [];
    var classDetail = [{
        className: "",
        people: "",
        teacherName: "",
        classRoom: "",
        myClassAmount: ""
    }];

    publishcourse.getGradeClass = function () {
        $.ajax(
            {
                url: "/timetable/getGradeClassList.do",
                type: "post",
                data: {
                    gradeId: $("#gradeId").val()
                },
                success: function (data) {
                    gradeClass = data;
                    classList = [];
                    if (gradeClass.length > 0) {
                        var map = gradeClass[0].classInfo;
                        if (map != null) {
                            $.each(map, function (key, value) {
                                classList.push({classId: key, className: value});
                            });
                        }
                    }
                    $("#classShow2").empty();
                    Common.render({tmpl: $('#classTempJs2'), data: {data: classList}, context: '#classShow2'});
                    publishcourse.getClassTimetable(5);
                }
            }
        );
    };
    publishcourse.getAdjustClass = function () {
        $.ajax({
            url: "/paike/findAdjustClass.do",
            type: "post",
            dataType: "json",
            data: {
                term: term,
                gradeId: $("#gradeId").val(),
                week: week
            },
            success: function (data) {
                var adjustClassList = [];
                for (var i = 0; i < data.length; i++) {
                    adjustClassList.push({classId: data[i].id, className: data[i].className});
                }

                $("#classShow2").empty();
                Common.render({tmpl: $('#classTempJs2'), data: {data: adjustClassList}, context: '#classShow2'});
                if (data.length == 0) {
                    $("#classTableShow").empty();
                }
                else {
                    publishcourse.getClassTimetable(4);
                }
            }
        })
    };
    publishcourse.getClassTimetable = function (type) {
        $.ajax(
            {
                url: "/timetable/getClassTimeTable.do",
                type: "post",
                data: {
                    year: $("#year").val(),
                    term: term,
                    gradeId: $("#gradeId").val(),
                    classId: $("#classShow2").val(),
                    type: type,//4调课 5原始课表
                    week: week
                },
                success: function (data) {
                    classTable.conf = data.conf;
                    classTable.course = data.course;
                    $("#classTableShow").empty();
                    Common.render({tmpl: $('#classTableTempJs'), data: {data: classTable}, context: '#classTableShow'});
                }
            }
        );
    };
    publishcourse.getDetail = function (select_x, select_y) {
        var type = 0;
        if ($("#adjust").is(":checked")) {
            type = 1;
        }
        $.ajax(
            {
                url: "/timetable/getDetailList.do",
                type: "post",
                dataType: "json",
                data: {
                    term: term,
                    classId: $("#classShow2").val(),
                    xIndex: select_x,
                    yIndex: select_y,
                    type: type,
                    week: week
                },
                success: function (data) {
                    classDetail = data;
                    $("#detailShow").empty();
                    Common.render({tmpl: $('#detailTempJs'), data: {data: classDetail}, context: '#detailShow'});
                    $(".room").show();
                }
            }
        );
    };
    publishcourse.getPhysicalDetail = function (select_x, select_y) {
        var type = 0;
        if ($("#adjust").is(":checked")) {
            type = 1;
        }
        $.ajax(
            {
                url: "/timetable/getPhysicalDetailList.do",
                type: "post",
                dataType: "json",
                data: {
                    term: term,
                    classId: $("#classShow2").val(),
                    xIndex: select_x,
                    yIndex: select_y,
                    type: type,
                    week: week
                },
                success: function (data) {

                    classDetail = data;
                    $("#detailShow").empty();
                    Common.render({tmpl: $('#detailTempJs'), data: {data: classDetail}, context: '#detailShow'});
                    $(".room").hide();
                }
            }
        );
    };
    publishcourse.getDetailHead = function (select_x, select_y) {
        $.ajax(
            {
                url: "/timetable/getDetailHead.do",
                type: "post",
                data: {
                    term: $("#year").val(),
                    gradeId: $("#gradeId").val()
                },
                success: function (data) {
                    detailWeek = data.classDays;
                    detailTime = data.classTime;

                    $("#weekshow").empty();
                    Common.render({
                        tmpl: $('#weekTempJs'),
                        data: {data: detailWeek, x: select_x},
                        context: '#weekshow'
                    });
                    $("#classDetailShow").empty();
                    Common.render({
                        tmpl: $('#classDetailTempJs'),
                        data: {data: detailTime, y: select_y},
                        context: '#classDetailShow'
                    });
                    if (detailTime.length > 0)
                        $(".i2").text(detailTime[select_y - 1]);
                }
            }
        );
    };
    $(document).ready(function () {
        //获取详情
        $("body").on("click", ".getClassTable", function () {
            if ($("#classShow2").val() == null) {
                alert("没有班级");
                return;
            }
            if ($("#adjust").is(":checked")) {
                publishcourse.getClassTimetable(4);
            }
            else {
                publishcourse.getClassTimetable(5);
            }
        });
        //详情页
        $("body").on("click", ".zb", function () {
            $(".main2-hide").show();
            $(".classShow").text($("#classShow2").find("option:selected").text());
            var select_x = Number($(this).attr("x"));
            var select_y = Number($(this).attr("y"));
            publishcourse.getDetailHead(select_x, select_y);
            /*if(detailTime.length>0)
             $(".i2").text(detailTime[0]);*/
            publishcourse.getDetail(select_x, select_y);
        });
        //详情页
        $("body").on("click", ".tyzb", function () {
            $(".main2-hide").show();
            $(".classShow").text($("#classShow2").find("option:selected").text());
            var select_x = Number($(this).attr("x"));
            var select_y = Number($(this).attr("y"));
            publishcourse.getDetailHead(select_x, select_y);
            /*if(detailTime.length>0)
             $(".i2").text(detailTime[0]);*/
            publishcourse.getPhysicalDetail(select_x, select_y);
        });
        $("body").on("click", ".main2wind-cl", function () {
            $(".main2-hide").hide();
        });
        //课时变化
        $("body").on("click", "#classDetailShow", function () {
            var yIndex = Number($(this).val());
            $(".i2").text(detailTime[yIndex - 1]);
        });
        $("body").on("click", ".hidemain-btn", function () {
            var select_x = Number($("#weekshow").val());
            var select_y = Number($("#classDetailShow").val());
            publishcourse.getDetail(select_x, select_y);
        });
        //导出课表
        $("body").on("click", ".exportTable", function () {
            var gradeId = $("#gradeId").val();
            var classId = $("#classShow2").val();
            window.location = '/timetable/exportAllStu.do?term=' + encodeURI(encodeURI(term)) + '&year=' + encodeURI(encodeURI($("#year").val()))
            + '&gradeId=' + gradeId + '&classId=' + classId + "&type=0&week=" + week;
        });
        //调课班级列表
        $("body").on("change", "#adjust", function () {
            if ($("#adjust").is(':checked')) {
                publishcourse.getAdjustClass();
            }
            else {
                publishcourse.getGradeClass();
            }
        });
    });
    //==============================教学周首页=========================================
    publishcourse.getTermConf = function () {
        $.ajax({
            url: "/paike/findTermEntry.do",
            type: "post",
            dataType: "json",
            data: {
                year: $("#year").val()
            },
            success: function (data) {
                var firstTerm = publishcourse.generalDateList(data.fts, data.fte);
                var secondTerm = publishcourse.generalDateList(data.sts, data.ste);
                var firstTermTime = data.fts + "~" + data.fte;
                var secondTermTime = data.sts + "~" + data.ste;
                var firstTermWeek = data.fweek;
                var secondTermWeek = data.sweek;
                $("#firstTermWeek").text("第一学期 共" + firstTermWeek + "周");
                $("#firstTermTime").text(firstTermTime);
                $("#firstTermList").empty();
                Common.render({tmpl: $('#firstTermWeekTempJS'), data: {term: firstTerm}, context: '#firstTermList'});
                $("#firstTermData").empty();
                Common.render({tmpl: $('#firstTermDateTempJS'), data: {term: firstTerm}, context: '#firstTermData'});

                $("#secondTermWeek").text("第二学期 共" + secondTermWeek + "周");
                $("#secondTermTime").text(secondTermTime);
                $("#secondTermList").empty();
                Common.render({tmpl: $('#secondTermWeekTempJS'), data: {term: secondTerm}, context: '#secondTermList'});
                $("#secondTermData").empty();
                Common.render({tmpl: $('#secondTermDateTempJS'), data: {term: secondTerm}, context: '#secondTermData'});
            }
        });
    };
    //生成校历
    publishcourse.generalDateList = function (termStart, termEnd) {
        var ftDays = [];
        var fts = strToDate(termStart);
        var fts_mon = fts.getMonth();
        var fts_fir_date = fts.getDate();
        var fts_week = fts.getDay();//星期，0~6 从星期天开始
        for (var i = 0; i < fts_week; i++) {
            ftDays.push("");
        }

        var curDate = new Date();
        curDate.setYear(fts.getYear());
        curDate.setMonth(fts_mon + 1);
        curDate.setDate(0);
        var alldays = curDate.getDate();
        //第一个月
        ftDays.push((fts_mon + 1) + "月/" + fts_fir_date);
        for (fts_fir_date = fts_fir_date + 1; fts_fir_date <= alldays; fts_fir_date++) {
            ftDays.push(fts_fir_date);
        }
        var fte = strToDate(termEnd);
        var fte_mon = fte.getMonth();
        if (fte_mon < fts_mon)
            fte_mon += 12;
        //中间月
        for (var i = fts_mon + 1; i < fte_mon; i++) {
            curDate.setMonth(i + 1);
            if (i >= 12) {
                curDate.setYear(fts.getYear() + 1);
                curDate.setMonth(i - 11);
            }
            curDate.setDate(0);
            var monthDay = curDate.getDate();
            ftDays.push((i + 1) + "月/1");
            for (var j = 2; j <= monthDay; j++) {
                ftDays.push(j);
            }
        }
        //最后一个月
        curDate.setYear(fte.getYear());
        if (fte_mon >= 12)
            fte_mon -= 12;
        curDate.setMonth(fte_mon + 1);
        curDate.setDate(0);
        var monthDays2 = fte.getDate();
        ftDays.push((fte_mon + 1) + "月/1");
        for (var i = 2; i <= monthDays2; i++) {
            ftDays.push(i);
        }
        return ftDays;
    };
    function strToDate(str) {
        var val = Date.parse(str);
        var newDate = new Date(val);
        return newDate;
    }

    publishcourse.getTermConf();
    $(document).ready(function () {
        $("body").on("click", ".link", function () {
            $("#adjust").attr("checked", false);
            term = $("#year").val() + $(this).attr("term");
            week = Number($(this).attr("week"));
            $(".col-right").show();
            $(".col-right0").hide();
            $(".main21-hide").hide();
            publishcourse.getGradeClass();
            $(".weekShow2").text("第" + week + "周");
            $(".termShowSp").text(term);
        });
        term = $("#term").val();
        week = Number($("#week").val());
        //$(".col-right").show();
        //$(".col-right0").hide();
        publishcourse.getGradeClass();
        $(".weekShow2").text("第" + week + "周");
        $(".right0-5").hide();
        $(".main21-hide").hide();
        $("#secondTermTime").hide();
        $("body").on("change", "#termChange", function () {
            if ($("#termChange").val() == 1) {
                $(".right0-3").show();
                $(".right0-5").hide();
                $("#firstTermTime").show();
                $("#secondTermTime").hide();
            }
            else if ($("#termChange").val() == 2) {
                $(".right0-3").hide();
                $(".right0-5").show();
                $("#firstTermTime").hide();
                $("#secondTermTime").show();
            }
        });
        $("body").on("click", ".main2wind-cl2", function () {
            $(".main21-hide").hide();
        });
    });
    var term = "";
    var week = 1;
    var type = 0;
});