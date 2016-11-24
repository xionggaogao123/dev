/**
 * Created by qiangm on 2015/7/17.
 */

define(function (require, exports, module) {
    require("jquery");
    require("doT");

    /**
     * 获取学生列表
     * @param classid
     */
    function getStudentList(classid) {
        $.ajax({
            url: "/myclass/statstus.do",
            type: "get",
            dataType: "json",
            async: false,
            data: {
                classId: classid
            },
            success: function (data) {
                if (data.totalList != null && data.totalList.length > 0) {
                    jQuery("#c-right-ll").remove();
                    //showStuList(data);
                    var source = document.getElementById("studentList").innerHTML;
                    var template1 = doT.template(source);
                    $("#studentList_dl").html(template1(data));
                }
            }
        });
    }

    /**
     * 获取教师列表
     * @param cid
     */
    function getTeacherList(cid) {
        $.ajax({
            url: "/myclass/teacherinofs.do",
            type: "get",
            dataType: "json",
            async: false,
            data: {
                classId: cid
            },
            success: function (data) {
                if (isTeacher(data.role) || isHeadMaster(data.role)) {
                    if (data.teacherList != null && data.teacherList.length != 0) {
                        $("#className").val(data.className);
                        $(".classNameShow").html(data.className);
                        var source = document.getElementById("teacherList").innerHTML;
                        var template1 = doT.template(source);
                        jQuery("#classListDd").html(template1({teacherList: data.teacherList}));

                    }
                } else {
                    $('#fc_teacher_list').hide();
                }
            }
        });
    }


    exports.pageInit = function () {
        $(document).ready(function () {
            $('#classname').text($('#className').val());
            $('body').on('blur', '.stuNumInput_after', function () {
                updateStuNum($(this));
            });
            $('body').on('blur', '.stuJobInput_after', function () {
                updateStuJob($(this));
            });

            function updateStuNum(target) {
                var stuNumBefore = $.trim(target.parent().find("input[class='stuNumInput_before']").val()) + "";
                var stuNumAfter = $.trim(target.val()) + "";
                var userId = target.parent().parent().attr("target-id");
                if (stuNumAfter != stuNumBefore) {
                    $.ajax(
                        {
                            url: "/myclass/updateStuNumOrJob.do",
                            type: "post",
                            data: {
                                userId: userId,
                                value: stuNumAfter,
                                type: 0
                            },
                            success: function (data) {
                                if (data == false) {
                                    alert("修改失败");
                                }
                                else {
                                    target.parent().find("input[class='stuNumInput_before']").val(stuNumAfter);
                                }
                            }
                        }
                    );
                }
            }

            /**
             * 更新学号职务
             * @param target
             */
            function updateStuJob(target) {
                var stuJobBefore = $.trim(target.parent().find("input[class='stuJobInput_before']").val()) + "";
                var stuJobAfter = $.trim(target.val()) + "";
                var userId = target.parent().parent().attr("target-id");
                if (stuJobAfter != stuJobBefore) {
                    $.ajax(
                        {
                            url: "/myclass/updateStuNumOrJob.do",
                            type: "post",
                            dataType: "json",
                            data: {
                                userId: userId,
                                value: stuJobAfter,
                                type: 1
                            },
                            success: function (data) {
                                if (data == false) {
                                    alert("修改失败");
                                }
                                else {
                                    target.parent().find("input[class='stuJobInput_before']").val(stuJobAfter);
                                }
                            }
                        }
                    )
                }
            }
        });
    };


    exports.statistics = function () {
        var classid = $.trim($("body").attr("classid"));
        $(".loading").show();

        getTeacherList(classid);
        getStudentList(classid);
        $(".loading").fadeOut(1000);

    };
});
