/* 
 * @Author: Tony
 * @Date:   2015-07-02 11:11:25
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-07-02 11:54:01
 */
define('teachermoralculture', ['doT', 'common'], function (require, exports, module) {
    /**
     *初始化参数
     */
    var teachermoralculture = {},
        Common = require('common');

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teachermoralculture.init()
     */
    teachermoralculture.init = function () {
        Common.cal('calId');
        Common.leftNavSel();
        teachermoralculture.changeGrade();

        teachermoralculture.editH4Title();

        teachermoralculture.getStudentsScoreData();

        $("#semesterId").change(function () {
            teachermoralculture.editH4Title();
            teachermoralculture.getStudentsScoreData();
        });

        $("#gradeId").change(function () {
            teachermoralculture.changeGrade();
            teachermoralculture.editH4Title();
            teachermoralculture.getStudentsScoreData();
        });

        $("#classId").change(function () {
            teachermoralculture.editH4Title();
            teachermoralculture.getStudentsScoreData();
        });

    };
    teachermoralculture.editH4Title = function () {
        var semesterName = $("#semesterId").find("option:selected").text();
        var gradeName = $("#gradeId").find("option:selected").text();
        var className = $("#classId").find("option:selected").text();
        $("#h4Title").text(semesterName + gradeName + className);
    }
    teachermoralculture.changeGrade = function () {
        var selData = {};
        selData.gradeId = $("#gradeId").val();
        Common.getPostData('/moralCultureScore/getGradeClassValue.do', selData, function (rep) {
                var list = rep.classList;
                var html = "";
                $.each(list, function (i, item) {
                    html += '<option value=' + item.id + '>' + item.className + '</option>';
                });
                $("#classId").html(html);
            }
        );
    }

    teachermoralculture.getStudentsScoreData = function () {
        var selData = {};
        selData.semesterId = $("#semesterId").val();
        selData.gradeId = $("#gradeId").val();
        selData.classId = $("#classId").val();
        Common.getPostData('/moralCultureScore/selClassAllStudentScore.do', selData, function (rep) {
                $('.table').html('');
                Common.render({tmpl: $('#table'), data: rep, context: '.table'});
                /*
                 * 绑定跳转的雷达图页面
                 * */
                $('.tr-class').bind("click", function (event) {
                    var semesterId = $("#semesterId").val();
                    var userId = $(this).attr('id');
                    window.location.href = "/moralCultureScore/moralCultureScorePage.do?semesterId=" + semesterId + "&userId=" + userId;
                    //teachermoralculture.handleSchoolSecurity();
                });
            }
        );
    }
    teachermoralculture.init();
    //module.exports=teachermoralculture;
});