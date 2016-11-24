/**
 * Created by Wangkaidong on 2016/7/16.
 */
define(function (require, exports, module) {
    require('layer');
    var Common = require('common');
    var zoubanFenban = {};

    var TERM = '';
    var GRADEID = '';
    var GROUPLIST = {};

    zoubanFenban.init = function () {

        TERM = $('body').attr('term');
        GRADEID = $('body').attr('gradeId');

        if($('body').attr('mode') == 1) {
            getFenDuanList();
        }
        if($('body').attr('mode') == 2) {
            getCourseList();
            $('.set-div>div').hide();
            $('#tab-BBSZ').show();
        }
        getSubjectList();
        getGroupList();
    }

    $(document).ready(function () {
        //返回首页
        $('body').on('click', '.back', function () {
            window.location.href = "/zouban/baseConfig.do?term=" + $('body').attr('term') + "&gradeId=" + $('body').attr('gradeId');
        });
        $('body').on('click', '#FDSZ', function () {
            $(this).addClass('m-active').siblings().removeClass('m-active');
            $('.set-div>div').hide();
            $('#tab-FDSZ').show();
        });
        $('body').on('click', '#FCSZ', function () {
            $(this).addClass('m-active').siblings().removeClass('m-active');
            $('.set-div>div').hide();
            $('#tab-FCSZ').show();
        });
        $('body').on('click', '#BBSZ', function () {
            getCourseList();
            $(this).addClass('m-active').siblings().removeClass('m-active');
            $('.set-div>div').hide();
            $('#tab-BBSZ').show();
        });
        $('body').on('click', '#XSSZ', function () {
            if (getState() > 3) {
                $(this).addClass('m-active').siblings().removeClass('m-active');
                $('.set-div>div').hide();
                $('#tab-XSSZ').show();
                getStuCourseList();
            } else {
                layer.alert('请先完成第二步');
            }
        });
        $('body').on('click', '#LSHJS', function () {
            if (getState() > 3) {
                $(this).addClass('m-active').siblings().removeClass('m-active');
                $('.set-div>div').hide();
                $('#tab-LSHJS').show();
                getTeaCourseList();
            } else {
                layer.alert('请先完成第二步');
            }

        });


        //----------------------------------------------1.分段----------------------------------------------------------
        $('body').on('click', '.autofd', function () {//自动分段弹窗
            $('.bg').show();
            $('#autoFenDuanWindow').show();
        });
        $('body').on('click', '.autoFenduanClose', function () {//关闭自动分段弹窗
            $('.bg').hide();
            $('#autoFenDuanWindow').hide();
        });
        $('body').on('click', '#autoFenDuan', function () {//自动分段
            autoFenDuan();
            $('.bg').hide();
            $('#autoFenDuanWindow').hide();
        });
        $(".tstep-fdsz").click(function () {//调整分段弹窗
            getChangeFenDuanList();
            $('.bg').show()
            $('#changeFenDuanWindow').show();
        })
        $('body').on('click', '#changeFenDuanCtx input:radio', function () {//调整分段
            var classId = $(this).parents('tr').attr('classId');
            var oldGroupId = $(this).parents('tr').attr('groupId');
            var newGroupId = $(this).val();
            changeFenDuan(classId, oldGroupId, newGroupId);
        });

        $('body').on('click', '#changeConfirm', function () {//调整分段确定
            getFenDuanList();
            $('.bg').hide()
            $('#changeFenDuanWindow').hide();
        });
        $('body').on('click', '.changeClose', function () {//关闭调整分段
            $('.bg').hide()
            $('#changeFenDuanWindow').hide();
        });

        //--------------------------------------------------2.分层-------------------------------------------------------
        $('body').on('click', '#download', function () {
            downloadTemplate();
        });
        $('body').on('click', '#upload', function () {
            if ($('#file').val() == '') {
                layer.alert('请选择文件');
                return;
            } else {
                importScore();
            }
        });

        //--------------------------------------------------3.分班-------------------------------------------------------
        $('body').on('click', '.tstep-ksbb', function () {

            $('.bg').show();
            $('#autoFenBanWindow').show();
        });
        $('body').on('click', '.autoFenbanClose', function () {
            $('.bg').hide();
            $('#autoFenBanWindow').hide();
        });
        $('body').on('click', '#autoFenBan', function () {
            autoFenBan();
        });
        $('body').on('change', '#groupListCtx', function () {
            getCourseList();
        });
        $('body').on('click', '#importZBCourse', function () {
            $('.bg').show();
            $('#importZoubanCourseWindow').show();
        });
        $('body').on('click', '.importWindowClose', function() {
            $('.bg').hide();
            $('#importZoubanCourseWindow').hide();
        });
        $('body').on('click', '#downloadZBCourseTemplate', function() {
            downloadZBCourseTemplate();
        });
        $('body').on('click', '#uploadCourseFile', function () {
            if ($('#zbCourseFile').val() == '') {
                layer.alert('请选择文件');
                return;
            } else {
                importZBCourse();
                $('.bg').hide();
                $('#importZoubanCourseWindow').hide();
            }
        });
        //--------------------------------------------------4.调整学生---------------------------------------------------
        $('body').on('change', '#studentGroupCtx', function () {
            getStuCourseList();
        });
        $('body').on('change', '#level2', function () {
            getStuCourseList();
        });
        $('body').on('click', '.stu-edit', function () {
            $('#courseName').text($(this).attr('courseName') + ' ' + $(this).attr('count') + '人');
            $('.stuSubmit').attr('courseId', $(this).attr('courseId'));
            $('.bg').show()
            $("#updateCourseNameWindow").show();
        });
        $('body').on('click', '.stuClose', function () {
            $('.bg').hide()
            $("#updateCourseNameWindow").hide();
        });
        $('body').on('click', '.stuSubmit', function () {//修改教学班名
            if ($('#newName').val().replace(/\s/g, '') == '') {
                layer.alert('名称不能为空');
                return;
            } else {
                if (checkCourseName($('#newName').val().replace(/\s/g, ''))) {
                    layer.alert('该名称已存在');
                    return;
                } else {
                    updateCourseName($(this).attr('courseId'), $('#newName').val());
                }
            }
            $('.bg').hide()
            $("#updateCourseNameWindow").hide();
        });
        $('body').on('click', '.stu-detail', function () {
            findClassStudentList($(this).attr('courseId'));
            $('#className').text($(this).attr('courseName'));
            $('.changeSubmit').attr('courseId', $(this).attr('courseId'));
            $('.tab-main').hide();
            $('.stu-changeClass').show();
        });
        $('body').on('click', '.backMain', function () {
            getStuCourseList();
            $('.tab-main').show();
            $('.stu-changeClass').hide();
        });
        $('body').on('click', '.active-change', function () {
            $('.changeSubmit').attr('studentId', $(this).attr('studentId'));
            $('.bg').show();
            $('#changeClassWindow').show();
        });
        $('body').on('click', '.changeClassClose', function () {
            $('.bg').hide();
            $('#changeClassWindow').hide();
        });
        $('body').on('click', '.changeSubmit', function () {
            var studentId = $(this).attr('studentId');
            var oldClassId = $(this).attr('courseId');
            var newClassId = $('#availableCourseCtx').val();
            changeStuClass(studentId, oldClassId, newClassId);
            $('.bg').hide();
            $('#changeClassWindow').hide();
        });
        $('body').on('click', '.stu-distribution', function() {
            $('.allot-title h3').text($(this).attr('courseName'));
            $('.allot-alert').attr('courseId', $(this).attr('courseId')).show();
            $('.bg').show();

            getSubjectGroupList();
            getSubjectGroupStuList();
            getZBCourseStuList();
        });
        $('body').on('click', '.SQ-X, .SZ-QX, .SZ-TJ', function() {
            $('.allot-alert').attr('courseId', '').hide();
            $('.bg').hide();
            getStuCourseList();
        });
        $('body').on('change', '#subjectGroupListCtx', function() {
            getSubjectGroupStuList();
        });
        $('body').on('click', '.left-ul li', function() {
            updateZBCourseStu($(this).attr('stuId'), 0);
        });
        $('body').on('click', '.right-ul li', function() {
            updateZBCourseStu($(this).attr('stuId'), 1);
        });

        //--------------------------------------------------5.老师和教室--------------------------------------------------
        $('body').on('change', '#teacherGroupCtx', function () {
            getTeaCourseList();
        });
        $('body').on('change', '#level', function () {
            getTeaCourseList();
        });
        $('body').on('click', '.lsandjs-edit', function () {
            getTeacherAndClassroom($(this).attr('group'));
            $('.bg').show()
            $(".lsandjs-alert").show();
        });
        $('body').on('click', '.teaClose', function () {
            $('.bg').hide()
            $(".lsandjs-alert").hide();
        });
        $('body').on('click', '#setTeaClsrm', function () {
            updateTeacherAndClassroom();
        });
        $('body').on('click', '.autoSetTeaAndClsrm', function () {
            autoSetTeaAndClsrm();
        });
    });



    /**
     * 加载模板公共方法
     *
     * @param tmpl
     * @param ctx
     * @param data
     */
    function template(tmpl, ctx, data) {
        Common.render({
            tmpl: tmpl,
            data: data,
            context: ctx,
            overwrite: 1
        });
    }




    /**
     * 获取分班进度
     * @returns {number}
     */
    function getState() {
        var state = 0;
        var url = '/zouban/fenban/fenBanState.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;

        Common.getData(url, param, function (resp) {
            state = resp.state;
        });

        return state;
    }

    /**
     * 获取分段列表
     */
    function getFenDuanList() {
        var url = '/zouban/fenban/findFenDuan.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;


        Common.getData(url, param, function (data) {
            GROUPLIST = data.fenDuanList;
            template('#fenDuanTmpl', '#fenDuanCtx', data.fenDuanList);
        });
        getGroupList();
    }

    /**
     * 自动分段
     */
    function autoFenDuan() {
        var url = '/zouban/fenban/autoFenDuan.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.count = $('#count').val();

        Common.getData(url, param, function (resp) {
            if (resp.code == '200') {
                getFenDuanList();
            } else {
                layer.alert('自动分段失败，请重新设置');
            }
        });
    }

    /**
     * 调整分段获取分段列表
     */
    function getChangeFenDuanList() {
        var url = '/zouban/fenban/changeFenDuanList.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;

        Common.getData(url, param, function (data) {
            template('#changeFenDuanTmpl', '#changeFenDuanCtx', data.fenDuanList);
        });
    }

    /**
     * 调整分段
     * @param classId
     * @param oldGroupId
     * @param newGroupId
     */
    function changeFenDuan(classId, oldGroupId, newGroupId) {
        var url = '/zouban/fenban/changeFenDuan.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.classId = classId;
        param.oldGroupId = oldGroupId;
        param.newGroupId = newGroupId;

        Common.getPostData(url, param, function (resp) {
            if (resp.code == '200') {
                getChangeFenDuanList();
            } else {
                layer.alert('调整分段失败');
            }
        });
    }


    /**
     * 获取走班学科
     */
    function getSubjectList() {
        var url = '/zouban/common/subjectList.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;

        Common.getData(url, param, function (data) {
            template('#subjectListTmpl', '#subjectListCtx', data.subjectList);
        });
    }

    /**
     * 下载成绩模板
     */
    function downloadTemplate() {
        var subjects = '';
        $('input[name="subject"]:checked').each(function () {
            subjects += $(this).val() + ',';
        });
        if (subjects == '') {
            layer.alert('请至少选择一门课程');
            return;
        }
        window.location.href = '/zouban/fenban/downloadTemplate.do?term=' + TERM + '&gradeId=' + GRADEID + '&subjects=' + subjects;
    }

    /**
     * 上传学生成绩
     */
    function importScore() {
        var index = layer.load(2, {shade: [0.3, '#000']});
        $.ajaxFileUpload({
            url: '/zouban/fenban/import.do?term=' + TERM + '&gradeId=' + GRADEID,
            secureuri: false, //是否需要安全协议，一般设置为false
            fileElementId: 'file', //文件上传域的ID
            dataType: 'json', //返回值类型 一般设置为json

            success: function (data) {
                layer.close(index);
                layer.alert(data.message);
            },
            error: function () {
                layer.close(index);
                layer.alert("导入失败");
            }
        });
    }

    /**
     * 分段列表
     */
    function getGroupList() {
        template('#groupListTmpl', '#groupListCtx' ,GROUPLIST);
        template('#groupListTmpl', '#groupCtx' ,GROUPLIST);
        template('#groupListTmpl', '#teacherGroupCtx' ,GROUPLIST);
        template('#groupListTmpl', '#studentGroupCtx' ,GROUPLIST);
    }


    /**
     * 下载走班课模板
     */
    function downloadZBCourseTemplate() {
        window.location.href = '/zouban/fenban/downloadZBCourseTemplate.do?gradeId=' + GRADEID;
    }



    /**
     * 导入走班课教学班
     */
    function importZBCourse() {
        var index = layer.load(2, {shade: [0.3, '#000']});
        $.ajaxFileUpload({
            url: '/zouban/fenban/importZoubanCourse.do?term=' + TERM + '&gradeId=' + GRADEID,
            secureuri: false, //是否需要安全协议，一般设置为false
            fileElementId: 'zbCourseFile', //文件上传域的ID
            dataType: 'json', //返回值类型 一般设置为json

            success: function (data) {
                getCourseList();
                layer.close(index);
                layer.alert(data.message);
            },
            error: function (xhr, e) {
                console.log(e);
                layer.close(index);
                layer.alert(e);
            }
        });
    }


    /**
     * 自动分班
     */
    function autoFenBan() {
        var url = '/zouban/fenban/autoFenBan.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.groupId = $('#groupCtx').val();
        param.advMax = $('#advMax').val();
        param.advMin = $('#advMin').val();
        param.simMax = $('#simMax').val();
        param.simMin = $('#simMin').val();
        param.classroomCount = $('#classroomCount').val();

        if (param.advMax == '' || param.advMin == '' || param.simMax == '' || param.simMin == '') {
            layer.alert('参数不能为空');
            return;
        }
        var index = layer.load(2, {shade: [0.3, '#000']});

        Common.getDataAsync(url, param, function (data) {
            if (data.code == '500') {
                getCourseList();
                layer.close(index);
                console.log(data.message);
                layer.alert('分班失败，请重新设置参数');
            } else {
                getCourseList();
                layer.close(index);
                $('.bg').hide();
                $('#autoFenBanWindow').hide();
            }
        });
    }

    /**
     * 获取教学班列表
     */
    function getCourseList() {
        var url = '/zouban/fenban/findCourseList.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.groupId = $('#groupListCtx').val();

        Common.getData(url, param, function (data) {
            template('#courseListTmpl', '#couresListCtx', data.courseList);
        });
    }

    /**
     * 学生设置-获取教学班列表
     */
    function getStuCourseList() {
        var url = '/zouban/fenban/courseList.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
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
     * 修改教学班名称
     * @param courseId
     * @param courseName
     */
    function updateCourseName(courseId, courseName) {
        var url = '/zouban/fenban/updateCourseName.do';
        var param = {};
        param.courseId = courseId;
        param.courseName = courseName;

        Common.getPostData(url, param, function (resp) {
            if (resp.code == '200') {
                getStuCourseList();
            } else {
                layer.alert('修改失败');
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
            template('#availableCourseTmpl', '#availableCourseCtx', resp.availableCourseList);
        });
    }

    /**
     * 学生调班
     * @param studentId
     * @param oldClassId
     * @param newClassId
     */
    function changeStuClass(studentId, oldClassId, newClassId) {
        var url = '/zouban/fenban/changeStuClass.do';
        var param = {};
        param.studentId = studentId;
        param.oldClassId = oldClassId;
        param.newClassId = newClassId;

        Common.getPostData(url, param, function (resp) {
            if (resp.code == '200') {
                findClassStudentList(oldClassId);
            } else {
                layer.alert('调班失败');
            }
        });
    }


    /**
     * 老师和教室-获取教学班列表
     */
    function getTeaCourseList() {
        var url = '/zouban/fenban/courseList.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.groupId = $('#teacherGroupCtx').val();
        param.level = $('#level').val();

        Common.getData(url, param, function (resp) {
            template('#teaCourseListTmpl', '#teaCourseListCtx', resp.message);
        });
    }

    /**
     * 获取老师和教室
     * @param group
     */
    function getTeacherAndClassroom(group) {
        var url = '/zouban/fenban/teacherAndClassroom.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.group = group;

        Common.getData(url, param, function (resp) {
            template('#teaAndClsrmTmpl', '#teaAndClsrmCtx', resp.list);
        });

    }

    /**
     * 设置老师和教室
     */
    function updateTeacherAndClassroom() {
        var list = [];
        var existTeacher = '';
        var existClassroom = '';
        var flag = true;

        $('#teaAndClsrmCtx').children().each(function () {
            var courseId = $(this).attr('courseId');
            var teacherId = $(this).find('.teacher').val();
            var teacherName = $(this).find('.teacher').find('option:selected').text();
            var classroomId = $(this).find('.classroom').val();

            if (existTeacher.indexOf(teacherId) != -1) {
                layer.alert('老师设置重复，请重新设置');
                flag = false;
                return false;
            } else {
                existTeacher += teacherId + ',';
            }
            if (existClassroom.indexOf(classroomId) != -1) {
                layer.alert('教室设置重复，请重新设置');
                flag = false;
                return false;
            } else {
                existClassroom += classroomId + ',';
            }

            var data = {};
            data.courseId = courseId;
            data.teacherId = teacherId;
            data.teacherName = teacherName;
            data.classroomId = classroomId;
            list.push(data);
        });

        if (flag) {
            $.ajax({
                url: '/zouban/fenban/teacherAndClassroom.do?term=' + TERM + '&gradeId=' + GRADEID,
                type: 'POST',
                data: JSON.stringify(list),
                dataType: 'json',
                contentType: 'application/json',
                success: function (resp) {
                    if (resp.code == '200') {
                        getTeaCourseList();
                        $('.bg').hide();
                        $('.lsandjs-alert').hide();
                    } else {
                        layer.alert('设置失败');
                    }
                }
            });
        }
    }

    /**
     * 一键设置老师和教室
     */
    function autoSetTeaAndClsrm() {
        var url = '/zouban/fenban/autoSetTeacherAndRoom.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;

        Common.getPostData(url, param, function (resp) {
            if (resp.code == '200') {
                getTeaCourseList();
            } else {
                layer.alert('自动设置失败');
            }
        });

    }

    /**
     * 检查教学班名称是否重复
     *
     * @param courseName
     */
    function checkCourseName(courseName) {
        var url = '/zouban/fenban/checkCourseName.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.courseName = courseName;

        var flag = false;
        Common.getData(url, param, function (resp) {
            flag = resp.result == 1;
        });
        return flag;
    }


    /**
     * 获取学科组合列表
     */
    function getSubjectGroupList() {
        var url = '/zouban/fenban/subjectGroupList.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.courseId = $('.allot-alert').attr('courseId');

        Common.getData(url, param, function (resp) {
            template('#subjectGroupListTmpl', '#subjectGroupListCtx', resp.message);
        });
    }

    /**
     * 获取组合学生列表
     */
    function getSubjectGroupStuList() {
        var url = '/zouban/fenban/subjectGroupStuList.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.subjectGroupId = $('#subjectGroupListCtx').val();
        param.courseId = $('.allot-alert').attr('courseId');

        Common.getData(url, param, function (resp) {
            template('#studentListTmpl', '.left-ul', resp.message);
        });
    }

    /**
     * 获取教学班学生列表
     */
    function getZBCourseStuList() {
        var url = '/zouban/fenban/zoubanCourseStuList.do';
        var param = {};
        param.courseId = $('.allot-alert').attr('courseId');

        Common.getDataAsync(url, param, function (resp) {
            template('#studentListTmpl', '.right-ul', resp.message);
        });
    }

    /**
     * 更新教学班学生
     */
    function updateZBCourseStu(studentId, type) {
        var url = '/zouban/fenban/updateZBCourseStu.do';
        var param = {};
        param.courseId = $('.allot-alert').attr('courseId');
        param.studentId = studentId;
        param.type = type;

        Common.getPostData(url, param, function (resp) {
            if(resp.code == '500') {
                console.log(resp.message);
            } else {
                getSubjectGroupStuList();
                getZBCourseStuList();
            }
        });
    }




    module.exports = zoubanFenban;

});