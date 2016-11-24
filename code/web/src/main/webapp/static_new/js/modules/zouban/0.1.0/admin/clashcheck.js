/**
 * Created by qiangm on 2015-10-16.
 */
'use strict';
define(['migrate','doT', 'common', 'jquery'], function (require, exports, module) {
    /**
     *初始化参数
     */
    require("jquery");
    require("doT");
    require("migrate");
    var clashcheck = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    clashcheck.init = function () {



    };
    $(document).ready(function () {
        $(".select-all").click(
            function () {
                $(".right-main2").show();
                $(".right-main3").hide();
            });

        $(".backindex").click(
            function () {
                window.open('../paike/'+$("#mode").val()+'.do?version=58&year='+ encodeURI(encodeURI($('#termShow').val()))+'&gradeId='+
                $('#gradeId').val(),'_self');
            });
    });
    clashcheck.conflictTest = function () {
        $.ajax({
            url: "/paike/conflictTest.do",
            type: "post",
            dataType: "json",
            data: {
                term:$("#termShow").val(),
                gradeId: $("#gradeId").val()
            },
            success: function (data) {
                if (data.result == "SUCCESS") {
                    //alert("检测冲突成功");
                    clashcheck.getAllConflict(0);
                }
                else {
                    alert("检测冲突失败");
                }
            }
        });
    };
    //获取年级、分段、课程分类
    clashcheck.getCourseType = function () {
        $.ajax({
            url: "/paike/getGradeGroupClass.do",
            type: "post",
            dataType: "json",
            data:{
                term:$("#termShow").val(),
                gradeId:$("#gradeId").val()
            },
            success: function (data) {
                gradeList = data;
                duanList = [];
                if (gradeList.length > 0) {
                    var list = gradeList[0].groupCourseList;
                    for (var i = 0; i < list.length; i++) {
                        duanList.push({group: list[i].group, groupId: list[i].groupId, courseInfo: list[i].courseInfo});
                    }
                }
                $("#groupShow").empty();
                Common.render({tmpl: $('#groupTempJs'), data: {data: duanList}, context: '#groupShow'});
                courseList = [];
                if (duanList.length > 0) {
                    var map = duanList[0].courseInfo;
                    if (map != null) {
                        $.each(map, function (key, value) {
                            courseList.push({courseId: key, courseName: value});
                        });
                    }
                }
                $("#courseShow").empty();
                Common.render({tmpl: $('#courseTempJs'), data: {data: courseList}, context: '#courseShow'});
                clashcheck.getAllConflict(0);
            }
        });
    };
    $(document).ready(function () {
        $("body").on("click", "#groupShow", function () {
            courseList = [];
            if (duanList.length > 0) {
                var groupId = $(this).val();
                for (var i = 0; i < duanList.length; i++) {
                    if (duanList[i].groupId == groupId) {
                        var map = duanList[i].courseInfo;
                        if (map != null) {
                            $.each(map, function (key, value) {
                                courseList.push({courseId: key, courseName: value});
                            });
                        }
                        break;
                    }
                }
            }
            $("#courseShow").empty();
            Common.render({tmpl: $('#courseTempJs'), data: {data: courseList}, context: '#courseShow'});
        });
        $("body").on("click", ".conflictTest", function () {
            clashcheck.conflictTest();
        });
        $("body").on("click", ".select", function () {
            var classId = $("#courseShow").val();
            clashcheck.getAllConflict(classId);
        });
        //冲突详情
        $("body").on("click", ".detial-cl", function () {
            $(".clash-detial").hide();
        });

        $("body").on("click", ".clash-canc", function () {
            $(".clash-detial").hide();
        });

        $("body").on("click", ".red-clash", function () {
            var cid1=$(this).attr("cid1");
            var cid2=$(this).attr("cid2");
            $(".clash-detial").show();
            var index = Number($(this).attr("index"));
            $(".p1").text($(this).parent().find("td").eq(0).attr("nm")+"/"+
            $(this).parent().parent().find("tr").eq(0).find("td").eq(2+index).attr("nm")+
            " 冲突详情");
            clashcheck.detail(cid1,cid2);
        });
    });
    //获取本段所有课程
    clashcheck.getAllConflict = function (classId) {
        if($("#groupShow").val()==null) {
            return;
        }
        $.ajax({
            url: "/paike/getAllConflict.do",
            type: "post",
            dataType: "json",
            data: {
                term:$("#termShow").val(),
                gradeId: $("#gradeId").val(),
                groupId: $("#groupShow").val()
            },
            success: function (data) {
                tableData = [];
                for (var i = 0; i < data.length; i++) {
                    tableData.push({
                        courseId: data[i].courseId,
                        courseName: data[i].courseName,
                        teacherName: data[i].teacherName,
                        conflictCourseIds: data[i].courseIds,
                        conflictCount: data[i].conflictCount

                    });
                }
                $("#MyTable").empty();
                Common.render({
                    tmpl: $('#tableTempJs'),
                    data: {data: tableData, classId: classId},
                    context: '#MyTable'
                });
                FixTable("MyTable", 2, 724, 614);
            }
        });
    };
    clashcheck.detail=function(courseId1,courseId2)
    {
        $.ajax({
            url: "/paike/conflictDetail.do",
            type: "post",
            dataType: "json",
            data: {
                term:$("#termShow").val(),
                gradeId: $("#gradeId").val(),
                courseId: courseId1,
                courseId2:courseId2
            },
            success: function (data) {
                conflictDetail=data;

                $("#teacher").text("任课教师冲突："+conflictDetail.teacher);
                $("#classRoom").text("上课教室冲突："+conflictDetail.classroom);
                $(".clash-tab").empty();
                Common.render({tmpl: $('#detailTempJs'), data: {data: conflictDetail.stu}, context: '.clash-tab'});
            }
        });
    };
    var conflictDetail={"teacher":"xx","classroom":"xxx","stu":[{student:"22",className:"xx"}]};
    var tableData = [];
    var gradeList = [];
    var duanList = [];
    var courseList = [];
    clashcheck.init();

    clashcheck.getCourseType();
    function FixTable(TableID, FixColumnNumber, width, height) {
        /// <summary>
        ///     锁定表头和列
        ///     <para> sorex.cnblogs.com </para>
        /// </summary>
        /// <param name="TableID" type="String">
        ///     要锁定的Table的ID
        /// </param>
        /// <param name="FixColumnNumber" type="Number">
        ///     要锁定列的个数
        /// </param>
        /// <param name="width" type="Number">
        ///     显示的宽度
        /// </param>
        /// <param name="height" type="Number">
        ///     显示的高度
        /// </param>
        if ($("#" + TableID + "_tableLayout").length != 0) {
            $("#" + TableID + "_tableLayout").before($("#" + TableID));
            $("#" + TableID + "_tableLayout").empty();
        }
        else {
            $("#" + TableID).after("<div id='" + TableID + "_tableLayout' style='overflow:hidden;height:" + height + "px; width:" + width + "px;'></div>");
        }
        $('<div id="' + TableID + '_tableFix"></div>'
        + '<div id="' + TableID + '_tableHead"></div>'
        + '<div id="' + TableID + '_tableColumn"></div>'
        + '<div id="' + TableID + '_tableData"></div>').appendTo("#" + TableID + "_tableLayout");
        var oldtable = $("#" + TableID);
        var tableFixClone = oldtable.clone(true);
        tableFixClone.attr("id", TableID + "_tableFixClone");
        $("#" + TableID + "_tableFix").append(tableFixClone);
        var tableHeadClone = oldtable.clone(true);
        tableHeadClone.attr("id", TableID + "_tableHeadClone");
        $("#" + TableID + "_tableHead").append(tableHeadClone);
        var tableColumnClone = oldtable.clone(true);
        tableColumnClone.attr("id", TableID + "_tableColumnClone");
        $("#" + TableID + "_tableColumn").append(tableColumnClone);
        $("#" + TableID + "_tableData").append(oldtable);
        $("#" + TableID + "_tableLayout table").each(function () {
            $(this).css("margin", "0");
        });
        var HeadHeight = $("#" + TableID + "_tableHead .row1").height();
        HeadHeight += 2;
        $("#" + TableID + "_tableHead").css("height", HeadHeight);
        $("#" + TableID + "_tableFix").css("height", HeadHeight);
        var ColumnsWidth = 0;
        var ColumnsNumber = 0;
        $("#" + TableID + "_tableColumn tr:last td:lt(" + FixColumnNumber + ")").each(function () {
            ColumnsWidth += $(this).outerWidth(true);
            ColumnsNumber++;
        });
        ColumnsWidth += 2;
        if ($.browser.msie) {
            switch ($.browser.version) {
                case "7.0":
                    if (ColumnsNumber >= 3) ColumnsWidth--;
                    break;
                case "8.0":
                    if (ColumnsNumber >= 2) ColumnsWidth--;
                    break;
            }
        }
        $("#" + TableID + "_tableColumn").css("width", ColumnsWidth);
        $("#" + TableID + "_tableFix").css("width", ColumnsWidth);
        $("#" + TableID + "_tableData").scroll(function () {
            $("#" + TableID + "_tableHead").scrollLeft($("#" + TableID + "_tableData").scrollLeft());
            $("#" + TableID + "_tableColumn").scrollTop($("#" + TableID + "_tableData").scrollTop());
        });
        $("#" + TableID + "_tableFix").css({ "overflow": "hidden", "position": "relative", "z-index": "50", "background-color": "Silver" });
        $("#" + TableID + "_tableHead").css({ "overflow": "hidden", "width": width - 17, "position": "relative", "z-index": "45", "background-color": "Silver" });
        $("#" + TableID + "_tableColumn").css({ "overflow": "hidden", "height": height - 17, "position": "relative", "z-index": "40", "background-color": "Silver" });
        $("#" + TableID + "_tableData").css({ "overflow": "scroll", "width": width, "height": height, "position": "relative", "z-index": "35" });
        if ($("#" + TableID + "_tableHead").width() > $("#" + TableID + "_tableFix table").width()) {
            $("#" + TableID + "_tableHead").css("width", $("#" + TableID + "_tableFix table").width());
            $("#" + TableID + "_tableData").css("width", $("#" + TableID + "_tableFix table").width() + 17);
        }
        if ($("#" + TableID + "_tableColumn").height() > $("#" + TableID + "_tableColumn table").height()) {
            $("#" + TableID + "_tableColumn").css("height", $("#" + TableID + "_tableColumn table").height());
            $("#" + TableID + "_tableData").css("height", $("#" + TableID + "_tableColumn table").height() + 17);
        }
        $("#" + TableID + "_tableFix").offset($("#" + TableID + "_tableLayout").offset());
        $("#" + TableID + "_tableHead").offset($("#" + TableID + "_tableLayout").offset());
        $("#" + TableID + "_tableColumn").offset($("#" + TableID + "_tableLayout").offset());
        $("#" + TableID + "_tableData").offset($("#" + TableID + "_tableLayout").offset());
    }
});