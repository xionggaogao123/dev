/**
 * Created by wangkaidong on 2016/6/15.
 */

define(function (require, exports, module) {
    require("jquery");
    require("doT");
    var Common = require('common');
    var bianbanrule = {};


    bianbanrule.init = function () {
        findFengDuan();
    }

    $(document).ready(function () {
        $('body').on('click', '.placement-a', function () {//返回首页
            window.open('../paike/index.do?version=58&year=' + encodeURI(encodeURI($('#termShow').text())) + '&gradeId=' +
            $('#grade').attr("gid"), '_self');
        });
        $('body').on('click', '#FD', function () {//设置分段
            $(this).addClass("place-cur").siblings().removeClass("place-cur");
            $(".content>div").hide();
            var aa = $(this).attr("id");
            $("#" + "placement-" + aa).show();
        });
        $('body').on('click', '#FB', function () {//学生分班
            if (getState() > 1) {
                $(this).addClass("place-cur").siblings().removeClass("place-cur");
                $(".content>div").hide();
                $('#classList').show();
                setGroupSubjectSelect(1);
            } else {
                return;
            }
        });
        $('body').on('click', '#JS', function () {//老师和教室
            if (getState() > 2) {
                $(this).addClass("place-cur").siblings().removeClass("place-cur");
                $(".content>div").hide();
                var aa = $(this).attr("id");
                $("#" + "placement-" + aa).show();
                setGroupSubjectSelect(2);
            } else {
                return;
            }
        });

        $(".place-BJ").click(function () {
            $(".placement-popup").show();
            $(".bg").show();
        })
        $(".sports-II").click(function () {
            $(".placement-popup").hide();
            $(".placement-popup-FB").hide();
            $(".placement-popup-JS").hide();
            $(".bg").hide();
        })
        $(".place-FB-BJ").click(function () {
            $(".placement-popup-FB").show();
            $(".bg").show();
        })
        $(".place-JS-BJ").click(function () {
            $(".placement-popup-JS").show();
            $(".bg").show();
        });


        //==============================分段========================================
        $('body').on('click', '#autoFenDuan', function () {
            $(".bg").show();
            $(".section-CUR").show();
        });
        $('body').on('click', '.closeFenDuan', function () {
            $(".bg").hide();
            $(".section-CUR").hide();
        });
        $('body').on('click', '.section-CUR-QR', function () {
            autoFenDuan();
        });

        //==================================分班============================================
        $('body').on('click', '#fenban', function(){
            $('.bg').show();
            $('.placement-popup-FB').show();
        });
        $('body').on('click', '.closeFenBan', function(){
            $('.bg').hide();
            $('.placement-popup-FB').hide();
        });
        $('body').on('click', '.submitFenBan', function(){
            $('.bg').hide();
            $('.placement-popup-FB').hide();
            autoFenban();
        });
        $('body').on('change', '#fenduanSelectCtx, #subjectSelectCtx', function () {
            getCourseList(1);
        });
        $('body').on('click', '.tab-check', function () {//查看学生
            $('#main').hide();
            $('.right-main2').show();
            $('#courseName').text($(this).attr('courseName'));
            $('#stuCount').text($(this).attr('count'));
            findClassStudentList($(this).attr('couId'));
        });
        $('body').on('click', '.back-btn', function(){
            $('#main').show();
            $('.right-main2').hide();
        });

        //====================================老师和教室========================================
        $('body').on('change', '#fenduanSelectCtx2, #subjectSelectCtx2', function () {
            getCourseList(2);
        });
        $('body').on('click', '.edit-set-tea', function(){
            $(".bg").show();
            $(".placement-popup-JS").show();
            $('#cName').attr('courseId', $(this).attr('couId'));
            $('#cName').text($(this).attr('cn'));
            getTeacherAndClassRoom($(this).attr('subId'));
        });
        $('body').on('click', '.closeJS', function(){
            $(".bg").hide();
            $(".placement-popup-JS").hide();
        });
        $('body').on('click', '#updateJS', function(){//提交
            setTeacherAndClassRoom();
        });
        $('body').on('click', '#setTeaClsRoom', function(){//一键设置老师和教室
            autoSetTeacherAndClassRoom();
        });
        $('body').on('click', '#fzbSetTeaClsRoom', function(){//一键设置非走班老师
            autoSetFzbTeacher();
        });
        $('body').on('change', '#courseType', function(){
            if($(this).val() == 1) {//走班
                $('#setTeaClsRoom').show();
                $('#fzbSetTeaClsRoom').hide();
                $('#fenduan').show();
                $('#fenduanSelectCtx2').show();
                $('#subject').show();
                $('#subjectSelectCtx2').show();
                getCourseList(2);
            } else {//非走班
                $('#setTeaClsRoom').hide();
                $('#fzbSetTeaClsRoom').show();
                $('#fenduan').hide();
                $('#fenduanSelectCtx2').hide();
                $('#subject').hide();
                $('#subjectSelectCtx2').hide();
                getFzbCourse();
            }
        });

    });


    /**
     * 获取分班进度
     * @returns {number}
     */
    function getState() {
        var url = '/bianban/getBianBanState.do';
        var param = {};
        param.gradeId = $("#grade").attr('gid');
        param.term = $("#term").text();

        var state = 1;
        Common.getData(url, param, function (resp) {
            if (resp) {
                state = resp.state;
            }
        });
        return state;
    }

    //======================================================分段========================================================

    /**
     * 自动分段
     */
    function autoFenDuan() {
        var url = '/bianban/autoFengDuan.do';
        var param = {};
        param.term = $("#term").text();
        param.gradeId = $("#grade").attr('gid');
        param.count = $("#count").val();
        Common.getPostData(url, param, function (resp) {
            if (resp.code == "200") {
                $(".bg").hide();
                $(".section-CUR").hide();
                findFengDuan();
            } else {
                alert("自动分段失败，请重新设置！");
            }
        });
    }

    /**
     * 查询分段信息
     */
    function findFengDuan() {
        var url = '/bianban/findFengDuan.do';
        var param = {};
        param.term = $("#term").text();
        param.gradeId = $("#grade").attr('gid');
        Common.getData(url, param, function (resp) {
            if (resp) {
                Common.render({
                    tmpl: '#fenduanListTmpl',
                    data: resp.fenduanClassList,
                    context: '#fenduanListCtx',
                    overwrite: 1
                });
            }
        });
    }


    //=================================================分班==============================================================

    /**
     * 获取分段、学科
     * @param type 1: 分班 2: 老师和教室
     * */
    function setGroupSubjectSelect(type) {
        var url = '/bianban/getgroupSubjectSelect.do';
        var param = {};
        param.term = $('#term').text();
        param.gradeId = $('#grade').attr('gid');
        param.type = 1;
        Common.getData(url, param, function (resp) {
            if(type == 1){
                Common.render({
                    tmpl: '#fenduanSelectTmpl',
                    data: resp.classFendDuanList,
                    context: '#fenduanSelectCtx',
                    overwrite: 1
                });
                Common.render({
                    tmpl: '#subjectListTmpl',
                    data: resp.subjectlist,
                    context: '#subjectSelectCtx',
                    overwrite: 1
                });
                getCourseList(1);
            }
            if(type == 2){
                Common.render({
                    tmpl: '#fenduanSelectTmpl',
                    data: resp.classFendDuanList,
                    context: '#fenduanSelectCtx2',
                    overwrite: 1
                });
                Common.render({
                    tmpl: '#subjectListTmpl',
                    data: resp.subjectlist,
                    context: '#subjectSelectCtx2',
                    overwrite: 1
                });
                getCourseList(2);
            }
        });
    };

    /**
     * 自动分班
     * */
    function autoFenban(){
        $('.bg').show();
        $('.bianbanTip').show();

        var url = '/bianban/addAutoBianBan.do';
        var param = {};
        param.term = $('#term').text();
        param.gradeId = $('#grade').attr('gid');
        param.advMax = $('#advMax').val();
        param.advMin = $('#advMin').val();
        param.simMax = $('#simMax').val();
        param.simMin = $('#simMin').val();

        Common.getDataAsync(url,param,function(resp){
            if(resp){
                if(resp.flg){
                    getCourseList(1);
                }else{
                    alert("分班失败");
                }
            }
            $('.bg').hide();
            $('.bianbanTip').hide();
        });
    }

    /**
     * 获取分班列表
     * @param type 1: 分班 2: 老师和教室
     * */
    function getCourseList(type) {
        var url = "/bianban/findBianBanList.do";
        var param = {};
        param.term = $('#term').text();
        param.gradeId = $('#grade').attr('gid');
        param.type = 1;
        if(type == 1){
            param.groupId = $('#fenduanSelectCtx').val();
            param.subjectId = $('#subjectSelectCtx').val();
        }else{
            param.groupId = $('#fenduanSelectCtx2').val();
            param.subjectId = $('#subjectSelectCtx2').val();
        }
        Common.getData(url, param, function (resp) {
            if(type == 1){
                Common.render({
                    tmpl: '#courseListTmpl',
                    data: resp.rows,
                    context: '#courseListCtx',
                    overwrite: 1
                });
            }
            if(type == 2){
                Common.render({
                    tmpl: '#teaClsRmTmpl',
                    data: resp.rows,
                    context: '#teaClsRmCtx',
                    overwrite: 1
                });
            }
        });

    };

    /**
     * 查看班级学生
     */
    function findClassStudentList(courseId) {
        var url = '/bianban/findClassStudentList.do';
        var param = {};
        param.courseClassId = courseId;
        Common.getData(url, param,function(resp){
            console.log(resp);
            Common.render({
                tmpl: '#studentListTmpl',
                data: resp.rows.studentCengJiDTOs,
                context: '#studentListCtx',
                overwrite: 1
            });
        });
    };


    //===================================================老师和教室=======================================================
    /**
     * 获取老师和教室
     * */
    function getTeacherAndClassRoom(subjectId){
        var url = '/bianban/teacherAndClassRoom.do';
        var param = {};
        param.term = $('#term').text();
        param.gradeId = $('#grade').attr('gid');
        param.subjectId = subjectId;
        param.groupId = $('#fenduanSelectCtx2').val();
        console.log(param);
        Common.getData(url, param, function(resp){
            Common.render({
                tmpl: '#teacherTmpl',
                data: resp.teacherList,
                context: '#teacherCtx',
                overwrite: 1
            });
            Common.render({
                tmpl: '#classRoomTmpl',
                data: resp.classRoomList,
                context: '#classRoomCtx',
                overwrite: 1
            });
        });
    }

    /**
     * 设置老师和教室
     * */
    function setTeacherAndClassRoom(){
        var url = '/bianban/teacherAndClassRoom.do';
        var param = {};
        param.term = $('#term').text();
        param.gradeId = $('#grade').attr('gid');
        param.courseId = $('#cName').attr('courseId');
        param.teacherId = $('#teacherCtx').val();
        param.teacherName = $('#teacherCtx option:selected').text();
        param.classRoomId = $('#classRoomCtx').val();

        Common.getPostData(url, param, function(resp){
            if(resp.code == "200"){
                if($('#courseType').val() == 1) {
                    getCourseList(2);
                } else {
                    getFzbCourse();
                }
            } else {
                alert(resp.message);
            }
            $(".bg").hide();
            $(".placement-popup-JS").hide();
        });
    }

    /**
     * 一键设置老师和教室
     */
    function autoSetTeacherAndClassRoom(){
        var url = '/bianban/autoSetTeacherAndRoom.do';
        var param = {};
        param.term = $('#term').text();
        param.gradeId = $('#grade').attr('gid');
        param.type = 1;
        Common.getPostData(url, param, function(resp){
           if(resp.code == "200"){
               getCourseList(2);
           } else {
               alert(resp.message);
           }
        });
    }

    /**
     * 一键设置非走班老师
     * */
    function autoSetFzbTeacher(){
        var url = '/bianban/autoSetTeacherAndRoom.do';
        var param = {};
        param.term = $('#term').text();
        param.gradeId = $('#grade').attr('gid');
        param.type = 2;
        Common.getPostData(url, param, function(resp){
            if(resp.code == "200"){
                getFzbCourse();
            } else {
                alert(resp.message);
            }
        });
    }

    /**
     * 获取非走班课
     * */
    function getFzbCourse() {
        var url = "/bianban/findBianBanList.do";
        var param = {};
        param.term = $('#term').text();
        param.gradeId = $('#grade').attr('gid');
        param.type = 2;
        param.groupId = '';
        param.subjectId = '';
        Common.getData(url, param, function (resp) {
            Common.render({
                tmpl: '#feizoubanTeaClsRmTmpl',
                data: resp.rows,
                context: '#teaClsRmCtx',
                overwrite: 1
            });
        });
    }



    module.exports = bianbanrule;

});
