/**
 * Created by fl on 2016/7/26.
 */
define(function(require,exports,module){
    var Common = require('common');
    var CONFIG = {

    };
    var BASEDATA = base;

    $(function(){
        getCurrTerm();
        termList();
        getGradeList();
        getSelfTimeTable();
        getSubgjectTeacher();
        $('#termListCtx, #weekSelectCtx').change(function() {
            getSelfTimeTable();
            getTeacherTimeTable();
            getClassTimeTable();
            getStudentTimeTable();
            getZBStuList();
        });
        //我的课表
        $('#MYTABLE').click(function () {
            $('.title-left').show();
            $('.tab-main').show();
            $('.stu-changeClass').hide();
            getSelfTimeTable();
        })
        //老师课表处理过程
        $('#LSKB').click(function () {
            $('.title-left').show();
            $('.tab-main').show();
            $('.stu-changeClass').hide();
        })
        $('#teachers').change(function () {
            getTeacherTimeTable();
        })


        $('#subject2').change(function () {
            showTeahcers();
        })
        $('#kbGradeListCtx').change(function () {
            getSubgjectTeacher();
        })
        $("#exportMyself").click(function () {
            window.location = '/zouban/teacher/exportTea.do?term=' + $('#termListCtx').val() + '&week=' + $('#weekSelectCtx').val();
        });
        $("#exportClassTeacher").click(function () {
            var year = $('#termListCtx').val();
            year = year.substring(0,year.indexOf("第"));
            var indexAndId = $('#subject2').val();
            var arg =  indexAndId.split(":");
            window.location = '/zouban/teacher/exportCourseTeacher.do?term=' + $('#termListCtx').val() + '&year=' + year
            +'&courseName='+ $("#subject2").find("option:selected").text() +'&courseId=' + arg[1] +"&week=" + $('#weekSelectCtx').val();
        });
        //行政班
        $('#XZB').click(function () {
            getClasses();
            $('.title-left').show();
            $('.tab-main').show();
            $('.stu-changeClass').hide();
        })
        //切换年级的时候切换行政班和课表
        $('#xzbGradeListCtx').change(function () {
            getClasses();
        })
        //切换行政班刷新课表
        $('#xzbClassListCtx').change(function () {
            getClassTimeTable();
        })
        //走班课详情
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

        $("#exportStuTable").click(function () {
            var classId = $('#xzbClassListCtx').val();
            var year = $('#termListCtx').val();
            year = year.substring(0,year.indexOf("第"));
            window.location = '/zouban/kebiao/exportAllStu.do?term=' + $('#termListCtx').val() + '&year=' + year
            + '&gradeId=' + $('#xzbGradeListCtx').val() + '&classId=' + classId + "&type=0&week=" + $('#weekSelectCtx').val();
        });
        $("#exportClassTable").click(function () {
            var year = $('#termListCtx').val();
            year = year.substring(0,year.indexOf("第"));
            window.location = '/zouban/teacher/exportClass.do?term=' + $('#termListCtx').val() + '&year=' + year
            +'&gradeName='+ $("#xzbGradeListCtx").find("option:selected").text() +'&gradeId=' + $('#xzbGradeListCtx').val() +"&week=" + $('#weekSelectCtx').val();
        });
        //学生课表选择
        $('#XSKB').click(function () {
            getStuClasses();
            $('.title-left').show();
            $('.tab-main').show();
            $('.stu-changeClass').hide();
        })
        $('#xsGradeListCtx').change(function () {
            getStuClasses();
        })
        $('#xsClassListCtx').change(function () {
            getStudents();
        })
        $('#students').change(function () {
            getStudentTimeTable();
        })


        //走班课学生列表
        $('body').on('click', '#ZBSTU', function () {
            getZBKGradeList();
            getFenDuanList();
            getZBCourseList();
            $('.title-left').hide();
            $('.tab-main').show();
            $('.stu-changeClass').hide();
        });
        $('body').on('change', '#zbkGradeListCtx', function () {
            getFenDuanList();
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
            var year = $('#termListCtx').val();
            year = year.substring(0,year.indexOf("第"));
            window.location = '/zouban/kebiao/exportZBStuList.do?term=' + year
            + '&gradeId=' + $('#zbkGradeListCtx').val() + '&groupId=' + $('#studentGroupCtx').val() + '&level=' + $('#level2').val();
        });
    })
    function template(tmpl, ctx, data) {
        Common.render({
            tmpl: tmpl,
            context: ctx,
            data: data,
            overwrite: 1
        });
    }

    /**
     * 获取当前学年
     */
    function getCurrTerm() {
        Common.getData("/zouban/kebiao/getCurrTerm.do", {}, function (resp) {
            $('#termListCtx').text(resp.message);
            $('#weekSelectCtx').text("第" + resp.currWeek + "周");
            var weekList = [];
            for (var i = 0; i < resp.integers; i++) {
                weekList.push(i + 1);
            }
            //加载周数
            Common.render({
                tmpl: '#weekSelectTmpl',
                data: weekList,
                context: '#weekSelectCtx',
                overwrite: 1
            });

            $('#weekSelectCtx').val(resp.currWeek);
        });
    }
    /**
     * 获取学期
     */
    function termList() {
        Common.getData("/zouban/common/getTermList.do", {}, function (resp) {
            var termList = resp.termList;
            var termLis=[];
            for(var i=0;i<termList.length;i++){
                termLis.push(termList[i] + "第一学期");
                termLis.push(termList[i] + "第二学期");
            }
            template('#termListTmpl', '#termListCtx', termLis);
        });

    }


    /**
     * 获取年级列表
     */
    function getGradeList() {
        Common.getData("/zouban/teacher/getPublishGradeList.do", {term:$('#termListCtx').val()}, function (resp) {
            Common.render({
                tmpl: '#xzbGradeListTmpl',
                data: resp.gradeList,
                context: '#xzbGradeListCtx',
                overwrite: 1
            });
            Common.render({
                tmpl: '#xsGradeListTmpl',
                data: resp.gradeList,
                context: '#xsGradeListCtx',
                overwrite: 1
            });

        });
    }

    /**
     * 获取年级列表
     */
    function getZBKGradeList() {
        Common.getData("/zouban/teacher/getGradeList.do", {}, function (resp) {
            Common.render({
                tmpl: '#zbkGradeListTmpl',
                data: resp.gradeList,
                context: '#zbkGradeListCtx',
                overwrite: 1
            });
        });
    }
    /**
     * 获取学科列表
     */
    function getSubgjectTeacher() {
        var requestData = {
        }
        Common.getDataAsync("/zouban/teacher/getSubgjectTeacher.do", requestData, function (resp) {
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

    /**
     * 显示老师列表
     */
    function showTeahcers() {
        var indexAndId = $('#subject2').val();
        var arg =  indexAndId.split(":");
        var teachers = CONFIG.subjectTeachers[arg[0]].teacherList;
        Common.render({
            tmpl: '#teachersTmpl',
            data: teachers,
            context: '#teachers',
            overwrite: 1
        });
        getTeacherTimeTable()
    }
    /**
     * 获取自己课表
     */
    function getSelfTimeTable() {
        var year = $('#termListCtx').val();
        var requestData = {
            year: year.substring(0,year.indexOf("第")),
            term: $('#termListCtx').val(),
            gradeId: "All",
            teacherId: $('#teachers').val(),
            week: $('#weekSelectCtx').val()
        }
        Common.getDataAsync("/zouban/kebiao/getTeacherTimeTable.do", requestData, function (resp) {
            Common.render({
                tmpl: '#myselfTableTmpl',
                data: resp,
                context: '#myselfTable',
                overwrite: 1
            });

        })
    }
    /**
     * 获取老师课表
     */
    function getTeacherTimeTable() {
        var year = $('#termListCtx').val();
        var requestData = {
            year: year.substring(0,year.indexOf("第")),
            term: $('#termListCtx').val(),
            gradeId: "All",
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

    /**
     * 行政班
     */
    function getClasses() {
        Common.getDataAsync("/myschool/classlist.do", {gradeid: $('#xzbGradeListCtx').val()}, function (resp) {
            CONFIG.adminClasses = resp.rows;
            Common.render({
                tmpl: '#xzbClassListTmpl',
                data: CONFIG.adminClasses,
                context: '#xzbClassListCtx',
                overwrite: 1
            });
            getClassTimeTable();
        })

    }

    //行政班课表
    function getClassTimeTable() {
        var classId = $('#xzbClassListCtx').val();
        var year = $('#termListCtx').val();
        year = year.substring(0,year.indexOf("第"));
        var requestData = {
            year: year,
            term: $('#termListCtx').val(),
            gradeId: $('#xzbGradeListCtx').val(),
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
    /**
     * 学生课表-行政班
     */
    function getStuClasses() {
        Common.getDataAsync("/myschool/classlist.do", {gradeid: $('#xsGradeListCtx').val()}, function (resp) {
            CONFIG.adminClasses = resp.rows;
            Common.render({
                tmpl: '#xsClassListTmpl',
                data: CONFIG.adminClasses,
                context: '#xsClassListCtx',
                overwrite: 1
            });
            getStudents();
        })

    }

    function getDetailHead(x, y) {
        var year = $('#termListCtx').val();
        year = year.substring(0,year.indexOf("第"));
        var requestData = {
            term: year,
            gradeId: $('#xzbGradeListCtx').val()
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
        var classId = $('#xzbClassListCtx').val();
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
    /**
     * 学生列表
     */
    function getStudents() {
        var classId = $('#xsClassListCtx').val();
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

    /**
     * 学生课表
     */
    function getStudentTimeTable() {
        var studentId = $('#students').val();
        var year = $('#termListCtx').val();
        year = year.substring(0,year.indexOf("第"));
        var requestData = {
            studentId: studentId,
            week: $('#weekSelectCtx').val(),
            term: $('#termListCtx').val(),
            year: year
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

    /**
     * 获取分段列表
     */
    function getFenDuanList() {
        var url = '/zouban/fenban/findFenDuan.do';
        var param = {};
        var year = $('#termListCtx').val();
        year = year.substring(0,year.indexOf("第"));
        param.term = year;
        param.gradeId = $('#zbkGradeListCtx').val();


        Common.getData(url, param, function (data) {
            GROUPLIST = data.fenDuanList;
            template('#studentGroupTmpl', '#studentGroupCtx', data.fenDuanList);
            getZBCourseList();
        });
    }
    /**
     * 获取走班课程列表
     */
    function getZBCourseList() {
        var url = '/zouban/fenban/courseList.do';
        var param = {};
        var year = $('#termListCtx').val();
        year = year.substring(0,year.indexOf("第"));
        param.term = year;
        param.gradeId = $('#zbkGradeListCtx').val();
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

