/**
 * Created by qiangm on 2015/7/18.
 */

define(function (require, exports, modules) {
    require("jquery");
    require("doT");
    var uid = "";
    var uname = "";
    var classId = $.trim($("body").attr("classid"));
    var type = $.trim($("body").attr("type"));

    /**
     * 获取学生列表
     * @param _classId
     */
    function getStudentListByClassId() {
        $.ajax(
            {
                url: "/myclass/getStudentList.do",
                type: "get",
                datatype: "json",
                data: {
                    classId: classId,
                    type: type
                },
                success: function (data) {
                    if (data.studentList.length > 0) {
                        uid = data.studentList[0].id;
                        uname = data.studentList[0].userName;
                        getStudentAttendance();
                    }
                    var source = document.getElementById("nameSheet").innerHTML;
                    var template1 = doT.template(source);
                    $("#nameSheet_dl").html(template1({className: data.className, studentList: data.studentList}));
                }
            }
        );
    }

    /**
     * 页面初始化
     */
    function pageinit() {
        $('body').on('change', '.nameSheet_radio', function () {
            uid = $(this).attr("uid");
            uname = $(this).attr("uname");
            getStudentAttendance();
        });
        $('body').on('click', '.deleteBtnClass', function () {
            var aid = $(this).attr("aid");
            deleteAttendance(aid);
        });
        $("#saveClick").on("click", function () {
            addAttendance();
        });
    }

    /**
     * 添加考勤记录
     */
    function addAttendance() {
        var date = $("#dt").val();
        var time = $("#time").val();
        var remark = $("#remark").val();

        if (uid == "") {
            alert("该班级没有学生");
            return;
        }

        if (date == "") {
            alert("日期不能为空");
            return;
        }

        $.ajax({
            url: "/myclass/addAttendance.do",
            type: "post",
            dataType: "json",
            data: {
                studentId: uid,
                classId: classId,
                date: date,
                time: time,
                remark: remark
            },
            success: function (data) {
                if (data == true) {
                    alert("添加成功");

                    getStudentAttendance()
                }
                else {
                    alert("该时间段内已存在考勤记录，请重新添加");
                }
            },
            fail: function () {
                alert("添加失败");
            }
        });
    }

    /**
     * 清除添加表单内容
     */
    function clearForm() {
        $("#dt").val("");
        $("#time").val("0");
        $("#remark").val("");
    }

    /**
     * 获取考勤记录列表
     */
    function getStudentAttendance() {
        $("#attendanceName").html(uname + "同学考勤记录表");
        $.ajax({
            url: "/myclass/getAttendance.do",
            type: "get",
            dataType: "json",
            data: {
                studentId: uid,
                classId: classId
            },
            success: function (data) {
                var source = document.getElementById("attendanceSheet").innerHTML;
                var template1 = doT.template(source);

                $("#attendanceSheetBody").html(template1(data));
            }
        });
        clearForm();

    }

    /**
     * 初始化rome日历插件
     */
    function initRome() {
        var now = new Date();
        var timeStr = now.getFullYear() + "-" + (now.getMonth() + 1) + "-" + now.getDate();
        var moment = rome.moment;
        rome(dt,
            {time: false, min: '2015-01-01', max: timeStr}
        );
    }

    /**
     * 删除考勤记录
     * @param aid
     */
    function deleteAttendance(aid) {
        if (confirm("确定要删除该考勤记录吗？")) {
            $.ajax({
                url: '/myclass/deleteAttendance.do',
                type: 'post',
                dataType: 'json',
                data: {
                    attendanceId: aid
                },
                success: function (data) {
                    alert("删除成功");
                    getStudentAttendance();
                },
                error: function () {
                    alert("删除失败");
                }
            });
        }
    }

    exports.attendance = function (classId) {

        getStudentListByClassId();
        pageinit();
        initRome();
    }
});
