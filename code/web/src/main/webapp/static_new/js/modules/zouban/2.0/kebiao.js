/**
 * Created by fl on 2016/7/26.
 */
define(function (require, exports, module) {
    var Common = require('common');
    require('layer');
    var CONFIG = {};
    var BASEDATA = base;//变量base在kebiao.jsp底部声明
    var GROUPLIST = {};//分段信息0

    $(function () {



        termList();
        getAllWeek();


        $('#termListCtx, #weekSelectCtx').change(function() {
            getClassTimeTable();
            getTeacherTimeTable();
            getStudentTimeTable();
            getZBStuList();
        });


        //=====================行政班课表==========================
        //行政班级列表
        getClasses();
        $('#XZB').click(function () {
            $('.title-left').show();
            $('.tab-main').show();
            $('.stu-changeClass').hide();
        })
        //行政班切换班级查看课表
        $('#tab-XZB .admin-classes').change(function () {
            getClassTimeTable();
        })
        $('body').on('click', '#classTable .zb', function () {
            $('.bg').show();
            $('.xz-mx-alert').show();
            var x = $(this).attr('x');
            var y = $(this).attr('y');
            getDetailHead(x, y);
            getDetailList(x, y);
        })
        //更换节次显示上课时间
        $('#jieci').change(function () {
            var y = $(this).val();
            $("#time").text(CONFIG.detailTime[y - 1]);
        })
        $('#weeks, #jieci').change(function () {
            var x = $('#weeks').val();
            var y = $('#jieci').val();
            getDetailList(x, y);
        })
        //导出
        $("#exportTable").click(function () {
            var classId = $('#tab-XZB .admin-classes').val();
            window.location = '/zouban/kebiao/exportAllStu.do?term=' + $('#termListCtx').val() + '&year=' + BASEDATA.year
            + '&gradeId=' + BASEDATA.gradeId + '&classId=' + classId + "&type=0&week=" + $('#weekSelectCtx').val();
        });

        //=====================教师课表==========================
        $('#LSKB').click(function () {
            getSubgjectTeacher();
            $('.title-left').show();
            $('.tab-main').show();
            $('.stu-changeClass').hide();
        })
        $('#subject2').change(function () {
            showTeahcers();
        })
        $('#teachers').change(function () {
            getTeacherTimeTable();
        })
        //导出老师课表
        $("#exportTeacher").click(function () {
            if ($("#teachers").val() == null || $("#teachers").val() == "") {
                layer.alert("没有教师数据");
                return;
            }
            window.location = '/zouban/kebiao/exportTea.do?term=' + $('#termListCtx').val() + '&gradeId=' + BASEDATA.gradeId +
            '&gradeName=' + BASEDATA.gradeName + '&teacherId=' + $("#teachers").val() + '&teacherName=' +
            $("#teachers").find("option:selected").text() + '&type=0&week=' + $('#weekSelectCtx').val();
        });

        //=====================学生课表==========================
        $('#XSKB').click(function () {
            getStudents();
            $('.title-left').show();
            $('.tab-main').show();
            $('.stu-changeClass').hide();
        })
        $('#tab-XSKB .admin-classes').change(function () {
            getStudents();
        })
        $('#students').change(function () {
            getStudentTimeTable();
        })
        $("#exportStu").click(function () {
            window.location = '/zouban/kebiao/exportStu.do?week=' + $('#weekSelectCtx').val() + '&stuId=' + $('#students').val() +
            '&stuName=' + $('#students').find('option:selected').text() + '&term=' + $('#termListCtx').val();
        });

        //====================走班学生名单=========================
        $('body').on('click', '#ZBSTU', function () {
            getFenDuanList();
            getZBCourseList();
            $('.title-left').hide();
            $('.tab-main').show();
            $('.stu-changeClass').hide();
        });
        $('body').on('change', '#studentGroupCtx', function () {
            getZBCourseList();
        });
        $('body').on('change', '#level2', function () {
            getZBCourseList();
        });
        $('body').on('click', '.stu-detail', function () {
            findClassStudentList($(this).attr('courseId'));
            $('#className').text($(this).attr('courseName')+":"+$(this).attr('teacherName'));
            $('.tab-main').hide();
            $('.stu-changeClass').show();
        });
        //返回
        $('body').on('click', '.backMain', function () {
            getZBCourseList();
            $('.tab-main').show();
            $('.stu-changeClass').hide();
        });
        $("#exportZBStuList").click(function () {
            window.location = '/zouban/kebiao/exportZBStuList.do?term=' + BASEDATA.year
            + '&gradeId=' + BASEDATA.gradeId + '&groupId=' + $('#studentGroupCtx').val() + '&level=' + $('#level2').val();
        });
    });
    /**
     * 获取分段列表
     */
    function getFenDuanList() {
        var url = '/zouban/fenban/findFenDuan.do';
        var param = {};
        param.term = BASEDATA.year;
        param.gradeId = BASEDATA.gradeId;


        Common.getData(url, param, function (data) {
            GROUPLIST = data.fenDuanList;
            template('#studentGroupTmpl', '#studentGroupCtx', data.fenDuanList);
        });
    }
    /**
     * 获取走班课程列表
     */
    function getZBCourseList() {
        var url = '/zouban/fenban/courseList.do';
        var param = {};
        param.term = BASEDATA.year;
        param.gradeId = BASEDATA.gradeId;
        param.groupId = $('#studentGroupCtx').val();
        param.level = $('#level2').val();
        Common.getData(url, param, function (resp) {
            if (resp.code == '200') {
                template('#stuCourseListTmpl', '#stuCourseListCtx', resp.message);
            } else {
                layer.alert(resp.message);
            }
        });
    }
    /**
     * 获取教学班学生列表
     * @param courseId
     */
    function findClassStudentList(courseId) {
        var url = '/zouban/fenban/findClassStudentList.do';
        var param = {};
        param.courseId = courseId;
        Common.getData(url, param, function (resp) {
            console.log(resp);
            $('#stuCount').text(resp.count);
            template('#stuListTmpl', '#stuListCtx', resp);
        });
    }

    function template(tmpl, ctx, data) {
        Common.render({
            tmpl: tmpl,
            context: ctx,
            data: data,
            overwrite: 1
        });
    }


    function termList() {
        var year = BASEDATA.year;
        var termList = [];
        termList.push(year + "第一学期");
        termList.push(year + "第二学期");
        template('#termListTmpl', '#termListCtx', termList);
    }

    function getAllWeek() {
        var url = '/zouban/kebiao/getAllWeek.do';
        var param = {};
        param.term = $('#termListCtx').val();

        Common.getData(url, param, function(data) {
            var weekList = [];
            for(var i = 0; i < data; i++) {
                weekList.push(i + 1);
            }
            template('#weekSelectTmpl', '#weekSelectCtx', weekList);
        });
    }


    function getDetailHead(x, y) {
        var requestData = {
            term: BASEDATA.year,
            gradeId: BASEDATA.gradeId
        }
        Common.getDataAsync("/zouban/kebiao/getDetailHead.do", requestData, function (resp) {
            var detailWeek = resp.classDays;
            var detailTime = resp.classTime;
            CONFIG.detailTime = detailTime;
            Common.render({
                tmpl: '#weeksTmpl',
                data: {data: detailWeek, x: x},
                context: '#weeks',
                overwrite: 1
            });
            Common.render({
                tmpl: '#jieciTmpl',
                data: {data: detailTime, y: y},
                context: '#jieci',
                overwrite: 1
            });
            if (detailTime.length > 0)
                $("#time").text(detailTime[y - 1]);

        })
    }

    function getDetailList(x, y) {
        var classId = $('#tab-XZB .admin-classes').val();
        var requestData = {
            term: $('#termListCtx').val(),
            classId: classId,
            xIndex: x,
            yIndex: y,
            type: 1,
            week: $('#weekSelectCtx').val()
        }
        Common.getDataAsync("/zouban/kebiao/getDetailList.do", requestData, function (resp) {
            Common.render({
                tmpl: '#adminclassdetailTmpl',
                data: resp,
                context: '#adminclassdetail',
                overwrite: 1
            });
        })
    }

    //班级
    function getClasses() {
        Common.getDataAsync("/myschool/classlist.do", {gradeid: BASEDATA.gradeId}, function (resp) {
            CONFIG.adminClasses = resp.rows;
            Common.render({
                tmpl: '#admin-classesTmpl',
                data: CONFIG.adminClasses,
                context: '.admin-classes',
                overwrite: 1
            });
            getClassTimeTable();
        })

    }

    //行政班课表
    function getClassTimeTable() {
        var classId = $('#tab-XZB .admin-classes').val();
        var requestData = {
            year: BASEDATA.year,
            term: $('#termListCtx').val(),
            gradeId: BASEDATA.gradeId,
            classId: classId,
            type: 2,
            week: $('#weekSelectCtx').val()
        }
        Common.getDataAsync("/zouban/kebiao/getClassTimeTable.do", requestData, function (resp) {
            Common.render({
                tmpl: '#classTableTmpl',
                data: resp,
                context: '#classTable',
                overwrite: 1
            });
        })
    }

    function getGradeSubjectCourse() {
        var requestData = {
            year: BASEDATA.year,
            gradeId: BASEDATA.gradeId
        }
        Common.getDataAsync("/zouban/kebiao/getGradeSubjectCourse.do", requestData, function (resp) {
            CONFIG.groupInfo = resp.groupInfo;
            Common.render({
                tmpl: '#subjects1Tmpl',
                data: resp.groupInfo,
                context: '#subjects1',
                overwrite: 1
            });
            showTeachingClasses();

        })
    }

    function showTeachingClasses() {
        var teachingClasses = CONFIG.groupInfo[$('#subjects1').val()];
        Common.render({
            tmpl: '#jiaoxuebanTmpl',
            data: teachingClasses,
            context: '#jiaoxueban',
            overwrite: 1
        });
        getCourseTimeTable();
    }

    function getCourseTimeTable() {
        var courseId = $('#jiaoxueban').val();
        var requestData = {
            year: BASEDATA.year,
            term: BASEDATA.term,
            gradeId: BASEDATA.gradeId,
            courseId: courseId,
            week: BASEDATA.curweek
        }
        Common.getDataAsync("/zouban/kebiao/getCourseTimeTable.do", requestData, function (resp) {
            Common.render({
                tmpl: '#subjectTableTemp',
                data: {data: resp},
                context: '#subjectTable',
                overwrite: 1
            });
        })
    }

    function getSubgjectTeacher() {
        var requestData = {
            term: BASEDATA.year,
            gradeId: BASEDATA.gradeId
        }
        Common.getDataAsync("/zouban/kebiao/getSubgjectTeacher.do", requestData, function (resp) {
            CONFIG.subjectTeachers = resp;
            Common.render({
                tmpl: '#subject2Tmpl',
                data: resp,
                context: '#subject2',
                overwrite: 1
            });
            showTeahcers();
        })
    }

    function showTeahcers() {
        var teachers = CONFIG.subjectTeachers[$('#subject2').val()].teacherList;
        Common.render({
            tmpl: '#teachersTmpl',
            data: teachers,
            context: '#teachers',
            overwrite: 1
        });
        getTeacherTimeTable()
    }

    function getTeacherTimeTable() {
        var requestData = {
            year: BASEDATA.year,
            term: $('#termListCtx').val(),
            gradeId: BASEDATA.gradeId,
            teacherId: $('#teachers').val(),
            week: $('#weekSelectCtx').val()
        }
        Common.getDataAsync("/zouban/kebiao/getTeacherTimeTable.do", requestData, function (resp) {
            Common.render({
                tmpl: '#teacherTableTmpl',
                data: resp,
                context: '#teacherTable',
                overwrite: 1
            });
        })
    }

    function getStudents() {
        var classId = $('#tab-XSKB .admin-classes').val();
        Common.getDataAsync("/myclass/statstus.do", {classId: classId}, function (resp) {
            Common.render({
                tmpl: '#studentsTmpl',
                data: resp.totalList,
                context: '#students',
                overwrite: 1
            });
            getStudentTimeTable()
        });
    }

    function getStudentTimeTable() {
        var studentId = $('#students').val();
        var requestData = {
            studentId: studentId,
            week: $('#weekSelectCtx').val(),
            term: $('#termListCtx').val(),
            year: BASEDATA.year
        };
        Common.getDataAsync("/zouban/kebiao/getStudentTimeTable.do", requestData, function (resp) {
            Common.render({
                tmpl: '#studentTableTmpl',
                data: resp,
                context: '#studentTable',
                overwrite: 1
            });
        });
    }


    $(function () {
        $(".tab-head li").click(function () {
            $(".tab-head li").removeClass("cur");
            $(this).addClass("cur");
            $(".tab-main>div").hide();
            var name = $(this).attr("id");
            $("#" + "tab-" + name).show();
        })

        $('.alert-close').click(function () {
            $('.bg').hide();
            $('.xz-mx-alert').hide();
        })
    })


});

