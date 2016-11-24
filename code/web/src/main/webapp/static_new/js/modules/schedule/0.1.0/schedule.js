/**
 * Created by qiangm on 2015/8/20.
 */
'use strict';
define(['doT', 'common', 'rome', 'jquery'], function (require, exports, module) {
    /**
     *初始化参数
     */
    require("jquery");
    require("doT");
    var schedule = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    schedule.init = function () {



    };
    $(document).ready(function () {
        //年级变换
        $(".grade_select").on("change", function () {
            var gradeId = $(this).val();
            var ClassList = [];
            for (var i = 0; i < schedule.GradeClass.length; i++) {
                if (schedule.GradeClass[i].gradeId == gradeId) {
                    for (var j = 0; j < schedule.GradeClass[i].classDTOs.length; j++) {
                        ClassList.push({
                            "id": schedule.GradeClass[i].classDTOs[j].classId,
                            "name": schedule.GradeClass[i].classDTOs[j].className
                        });
                    }
                    $(".class_select").empty();
                    Common.render({tmpl: $('#gradeTempJs'), data: {data: ClassList}, context: '.class_select'});
                    if (ClassList.length > 0) {
                        schedule.getClassTable($(".term_select").val(), ClassList[0].id, schedule.week);
                    }
                    return;
                }
            }
        });
        //学期变化
        $(".term_select").on("change", function () {
            var term = $(this).val();
            schedule.getWeekList($(".term_select").val());
            schedule.getClassTable(term, $(".class_select").val(), schedule.week);
        });
        //星期变化
        $("#weekList").on("change", function () {
            schedule.week = Number($(this).val());
            schedule.getClassTable($(".term_select").val(), $(".class_select").val(), schedule.week);
        });
        //班级变换
        $(".class_select").on("change", function () {
            var classId = $(this).val();
            schedule.getClassTable($(".term_select").val(), classId, schedule.week);
        });
        //导入课表
        $("#importSchedule").click(function () {
            window.location = "/schedule/importSchedule.do?version=5k";
        });
        $("#scheduleLi").click(function () {
            $(".kebiao_maincon").show();
            $("#weekLi").addClass("unchoosed-li");
            $(".right3-main").hide();
            $("#scheduleLi").removeClass("unchoosed-li");
        });
        $("#weekLi").click(function () {
            $(".kebiao_maincon").hide();
            $(".right3-main").show();
            $("#weekLi").removeClass("unchoosed-li");
            $("#scheduleLi").addClass("unchoosed-li");
            schedule.getTermConf();
        });
        //发布课表
        $("#publishSchedule").click(function () {
            if (confirm("您确定要发布课表吗？")) {
                schedule.publishSchedule();
            }
        })
    });
    //发布课表
    schedule.publishSchedule = function () {
        $.ajax({
            url: "/schedule/publishSchedule.do",
            type: "post",
            dataType: "json",
            data: {
                term: $(".term_select").val()
            },
            success: function (data) {
                if (data.result == "FAILD") {
                    alert("发布失败");
                }
                else {
                    $("#publishSchedule").hide();
                    $("#noSchedule").hide();
                    $("#published").show();
                }
            }
        })
    };
    //获取学期列表
    schedule.getWeekList = function (term) {
        $.ajax({
            url: "/schedule/findWeeekList.do",
            type: "post",
            dataType: "json",
            data: {
                term: term
            },
            success: function (data) {
                var list = [];
                for (var i = 1; i <= data.all; i++) {
                    list.push(i);
                }
                $("#weekList").empty();
                Common.render({tmpl: $('#weekTempJs'), data: {data: list}, context: '#weekList'});
            }
        });
    };
    schedule.GradeClass = [];
    //schedule.conf={};
    //获取年级班级列表
    schedule.getGradeClass = function () {
        $.ajax(
            {
                url: "/schedule/getGradeClass.do",
                type: "post",
                data: {},
                success: function (data) {
                    schedule.GradeClass = data;
                    var GradeList = [];
                    var ClassList = [];
                    for (var i = 0; i < data.length; i++) {
                        GradeList.push({"id": data[i].gradeId, "name": data[i].gradeName});
                        if (i == 0) {
                            for (var j = 0; j < data[i].classDTOs.length; j++) {
                                ClassList.push({
                                    "id": data[i].classDTOs[j].classId,
                                    "name": data[i].classDTOs[j].className
                                });
                            }
                            $(".class_select").empty();
                            Common.render({tmpl: $('#gradeTempJs'), data: {data: ClassList}, context: '.class_select'});
                            if (ClassList.length > 0) {
                                schedule.getClassTable($("#term").val(), ClassList[0].id, schedule.week);
                            }
                        }
                    }
                    $(".grade_select").empty();
                    Common.render({tmpl: $('#gradeTempJs'), data: {data: GradeList}, context: '.grade_select'});
                }
            }
        )
    };
    //获取班级课表
    schedule.getClassTable = function (term, classId, week) {
        $.ajax({
            url: "/schedule/getClassSchedule.do",
            type: "post",
            dataType: "json",
            data: {
                term: term,
                classId: classId,
                week: week
            },
            success: function (data) {
                /*var classDays=data.classDays;
                 var classCount=data.classCount;
                 var course=data.course;*/
                if (data.type == 0)//无课表
                {
                    $("#publishSchedule").hide();
                    $("#noSchedule").show();
                    $("#published").hide();
                }
                else if (data.type == 1)//未发布
                {
                    $("#publishSchedule").show();
                    $("#noSchedule").hide();
                    $("#published").hide();
                }
                else if (data.type == 2)//已发布
                {
                    $("#publishSchedule").hide();
                    $("#noSchedule").hide();
                    $("#published").show();
                }
                $(".kebiao_table").empty();
                Common.render({tmpl: $('#courseTableJs'), data: {data: data}, context: '.kebiao_table'});
            }

        })
    };

    //================================================教学周===============================================================
    schedule.getYearList = function () {
        $.ajax({
            url: "/zouban/common/getTermList.do",
            type: "post",
            dataType: "json",
            success: function (data) {
                var termList = [];
                for (var i = 0; i < data.termList.length; i++) {
                    termList.push(data.termList[i] + "第二学期");
                    termList.push(data.termList[i] + "第一学期");
                }

                $(".term_select").empty();
                Common.render({tmpl: $('#termTempJs'), data: {data: termList}, context: '.term_select'});
                $(".term_select").val($("#term").val());

                $("#termShow2").empty();
                Common.render({tmpl: $('#termTempJs'), data: {data: data.termList}, context: '#termShow2'});
                $(".yearShow").text($("#termShow2").val());
                schedule.getWeekList($(".term_select").val());
            }
        });

    };
    schedule.getTermConf = function () {
        $.ajax({
            url: "/schedule/termConfig.do",
            type: "get",
            dataType: "json",
            data: {
                year: $("#termShow2").val()
            },
            success: function (data) {
                data = data.termConfig;
                var firstTerm = schedule.generalDateList(data.fts, data.fte);
                var secondTerm = schedule.generalDateList(data.sts, data.ste);
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
    schedule.generalDateList = function (termStart, termEnd) {
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
        var fte = strToDate(termEnd);
        var fte_mon = fte.getMonth();
        if (fte_mon < fts_mon)
            fte_mon += 12;
        var monthDays2 = fte.getDate();
        if (fte_mon == fts_mon) {
            //第一个月
            ftDays.push((fts_mon + 1) + "月/" + fts_fir_date);
            for (fts_fir_date = fts_fir_date + 1; fts_fir_date <= monthDays2; fts_fir_date++) {
                ftDays.push(fts_fir_date);
            }
        }
        else {
            //第一个月
            ftDays.push((fts_mon + 1) + "月/" + fts_fir_date);
            for (fts_fir_date = fts_fir_date + 1; fts_fir_date <= alldays; fts_fir_date++) {
                ftDays.push(fts_fir_date);
            }
        }

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
        if (fte_mon > fts_mon) {
            //最后一个月
            curDate.setYear(fte.getYear());
            if (fte_mon >= 12)
                fte_mon -= 12;
            curDate.setMonth(fte_mon + 1);
            curDate.setDate(0);
            //var monthDays2 = fte.getDate();
            ftDays.push((fte_mon + 1) + "月/1");
            for (var i = 2; i <= monthDays2; i++) {
                ftDays.push(i);
            }
        }
        return ftDays;
    };
    function strToDate(str) {
        var val = Date.parse(str);
        var newDate = new Date(val);
        return newDate;
    }

    //添加学期
    schedule.addTerm = function () {
        var firstTermStart = new Date($("#firstStart").val());
        var firstTermEnd = new Date($("#firstEnd").val());
        var secondTermStart = new Date($("#secondStart").val());
        var secondTermEnd = new Date($("#secondEnd").val());

        if (isNaN(firstTermStart.getTime()) || isNaN(firstTermEnd.getTime()) ||
            isNaN(secondTermStart.getTime()) || isNaN(secondTermEnd.getTime())) {
            alert("请正确输入学期时间");
            return;
        }
        if (firstTermEnd.getTime() < firstTermStart.getTime()) {
            alert("第一学期放假日期小于开学日期");
            return;
        }
        if (secondTermEnd.getTime() < secondTermStart.getTime()) {
            alert("第二学期放假日期小于开学日期");
            return;
        }
        if (secondTermStart.getTime() < firstTermEnd.getTime()) {
            alert("第二学期开学日期小于第一学期放假日期");
            return;
        }
        if (DateDiff($("#firstStart").val(), $("#firstEnd").val()) > 180) {
            alert("第一学期上课天数过长，请重新设置");
            return;
        }
        if (DateDiff($("#secondStart").val(), $("#secondEnd").val()) > 180) {
            alert("第二学期上课天数过长，请重新设置");
            return;
        }

        var termDTO = {
            year: $("#termShow2").val(),
            fts: $("#firstStart").val(),
            fte: $("#firstEnd").val(),
            sts: $("#secondStart").val(),
            ste: $("#secondEnd").val()
        };
        $.ajax({
            url: "/schedule/termConfig.do",
            type: "post",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(termDTO),
            success: function (data) {
                if (data.code != "200") {
                    alert("添加失败");
                }
                else {
                    $(".weekwind").hide();
                    $(".bg").hide();
                    schedule.getTermConf();
                }
            }
        });
    };
    //获取学期配置
    schedule.getTerm = function () {
        $.ajax({
            url: "/schedule/termConfig.do",
            type: "get",
            dataType: "json",
            data: {
                year: $("#termShow2").val()
            },
            success: function (data) {
                data = data.termConfig;
                $("#firstStart").val(data.fts);
                $("#firstEnd").val(data.fte);
                $("#secondStart").val(data.sts);
                $("#secondEnd").val(data.ste);
                $("#firstWeek").text("共" + data.fweek + "周");
                $("#secondWeek").text("共" + data.sweek + "周");
                var moment = rome.moment;
                rome(firstStart, {time: false, initialValue: data.fts, dateValidator: rome.val.beforeEq(firstEnd)}).
                    off('data').
                    on('data', function (value) {
                        var week = schedule.calWeekCount(value, $("#firstEnd").val());
                        $("#firstWeek").text("共" + week + "周");
                        $("#firstStart").val(value);
                    });
                rome(firstEnd, {time: false, initialValue: data.fte, dateValidator: rome.val.afterEq(firstStart)}).
                    off('data').
                    on('data', function (value) {
                        var week = schedule.calWeekCount($("#firstStart").val(), value);
                        $("#firstWeek").text("共" + week + "周");
                        $("#firstEnd").val(value);
                    });
                rome(secondStart, {time: false, initialValue: data.sts, dateValidator: rome.val.beforeEq(secondEnd)}).
                    off('data').
                    on('data', function (value) {
                        var week = schedule.calWeekCount(value, $("#secondEnd").val());
                        $("#secondWeek").text("共" + week + "周");
                        $("#secondStart").val(value);
                    });
                rome(secondEnd, {time: false, initialValue: data.ste, dateValidator: rome.val.afterEq(secondStart)}).
                    off('data').
                    on('data', function (value) {
                        var week = schedule.calWeekCount($("#secondStart").val(), value);
                        $("#secondWeek").text("共" + week + "周");
                        $("#secondEnd").val(value);
                    });
            }
        });
    };
    //计算天数差的函数，通用
    //sDate1 后面日期
    function DateDiff(sDate1, sDate2) {    //sDate1和sDate2是2006-12-18格式
        var aDate, oDate1, oDate2, iDays;
        aDate = sDate1.split("-");
        oDate1 = new Date(aDate[0] + '-' + aDate[1] + '-' + aDate[2]);   //转换为12-18-2006格式
        aDate = sDate2.split("-");
        oDate2 = new Date(aDate[0] + '-' + aDate[1] + '-' + aDate[2]);
        iDays = parseInt(Math.abs(oDate1 - oDate2) / 1000 / 60 / 60 / 24);   //把相差的毫秒数转换为天数
        return iDays;
    }

    /**
     * 自动计算当前共有多少周
     * @param sDate1 开始日期
     * @param sDate2 结束日期
     */
    schedule.calWeekCount = function (sDate1, sDate2) {
        var days = DateDiff(sDate2, sDate1);
        var start = strToDate(sDate1);
        var fts_week = start.getDay();//星期，0~6 从星期天开始
        days += fts_week;
        if (days % 7 == 0)
            return days / 7 + 1;
        return Math.ceil(days / 7);
    };
    $(document).ready(function () {
        $("body").on("click", ".right3-1", function () {
            schedule.findTablePub($("#termShow2").val());
        });
        //保存学期时间
        $("body").on("click", ".week-conf", function () {
            schedule.addTerm();
        });
        $("body").on("click", ".hide-x ,.hide-canc", function () {
            $(".hide-add").hide();
            $(".weekwind").hide();
            $(".hide-edit").hide();
            $(".bg").hide();
        });
        $("body").on("change", "#termShow2", function () {
            schedule.getTermConf();
            $(".yearShow").text($(this).val());
        })
    });
    schedule.findTablePub = function (year) {
        if ($(".class_select").val()) {
            $.ajax({
                url: "/schedule/getClassSchedule.do",
                type: "post",
                dataType: "json",
                data: {
                    term: year + "第一学期",
                    classId: $(".class_select").val(),
                    week: 1
                },
                success: function (data) {
                    if (data.type == 2)//已发布
                    {
                        $("#firstStart").attr("disabled", "disabled");
                        $("#firstEnd").attr("disabled", "disabled");
                    }
                    else {
                        $("#firstStart").removeAttr("disabled");
                        $("#firstEnd").removeAttr("disabled");
                    }

                    $.ajax({
                        url: "/schedule/getClassSchedule.do",
                        type: "post",
                        dataType: "json",
                        data: {
                            term: year + "第二学期",
                            classId: $(".class_select").val(),
                            week: 1
                        },
                        success: function (data) {
                            if (data.type == 2)//已发布
                            {
                                $("#secondStart").attr("disabled", "disabled");
                                $("#secondEnd").attr("disabled", "disabled");
                            }
                            else {
                                $("#secondStart").removeAttr("disabled");
                                $("#secondEnd").removeAttr("disabled");
                            }
                            $(".weekwind").show();
                            $(".bg").show();

                            schedule.getTerm();
                        }
                    })
                }
            })
        }
    }
    schedule.week = 1;
    schedule.init();
    schedule.getYearList();
    //schedule.getTermList();
    schedule.getGradeClass();
});