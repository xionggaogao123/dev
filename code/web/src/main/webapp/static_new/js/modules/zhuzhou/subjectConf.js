/**
 * Created by wangkaidong on 2016/9/14.
 */
define(function (require, exports, module) {
    /**
     *初始化参数
     */
    require('layer');
    var Common = require('common');
    var subjectConf = {};


    subjectConf.init = function () {




        localStorage.clear();

        zbSubjectList();
        subjects();
        teachers();
        fzbSubjectList();
        getFZBCourseList();
    }

    $(document).ready(function () {
        $('body').on('click', '.tab-head ul li', function () {//走班、非走班切换
            $(this).addClass('cur').siblings().removeClass('cur');
            $('.tab-main > div').hide();
            $('#tab-' + $(this).attr('id')).show();
        });

        $('body').on('click', '.ZBK-II-XZ', function () {//新增走班
            $('.popup-XKGL-ZB, .bg').show();
        });

        $('body').on('click', '.zbClose', function () {
            $('.popup-XKGL-ZB, .bg').hide();
        });

        $('body').on('click', '.addZB', function () {
            //addZBCourse();
            $('.popup-XKGL-ZB, .bg').hide();
        });

        $('body').on('click', '.zouban-addbtn ul li', function () {//非走班1/2切换
            $(this).addClass('zb-active').siblings().removeClass('zb-active');
            $('.fzbk > div').hide();
            $('#tab-' + $(this).attr('id')).show();
        });

        $('body').on('click', '.FZBK-II-XZ', function () {
            $('.popup-XKGL, .bg').show();
        });

        $('body').on('click', '.fzbClose', function () {
            $('.popup-XKGL, .bg').hide();
        });

        $('body').on('click', '.addFZB', function () {
            $('.popup-XKGL, .bg').hide();
        });

        $('body').on('change', '#subjectListCtx', function () {
            getFZBCourseList($(this).val());
        });


    });


    var ZBSubjectList = [
        {subjectName: '语文', lessonCount: 2, teacherName: '严朝晖', classroom: 'A101'},
        {subjectName: '数学', lessonCount: 2, teacherName: '张耀华', classroom: 'A102'},
        {subjectName: '英语', lessonCount: 2, teacherName: '杜静', classroom: 'A101'},
        {subjectName: '物理', lessonCount: 2, teacherName: '周运生', classroom: 'A102'},
        {subjectName: '化学', lessonCount: 2, teacherName: '徐宣武', classroom: 'A103'},
        {subjectName: '生物', lessonCount: 2, teacherName: '刘丽华', classroom: 'A103'},
        {subjectName: '信息技术', lessonCount: 2, teacherName: '杜新宇', classroom: 'A104'},
        {subjectName: '创造发明', lessonCount: 2, teacherName: '刘鑫灿', classroom: 'A104'}
    ];

    var ZBTeachers = ['严朝晖', '张耀华', '杜静', '周运生', '徐宣武', '刘丽华', '杜新宇 ', '刘鑫灿'];

    var FZBSubjectList = [
        {subjectName: '语文', lessonCount: 5},
        {subjectName: '数学', lessonCount: 5},
        {subjectName: '英语', lessonCount: 5},
        {subjectName: '物理', lessonCount: 3},
        {subjectName: '化学', lessonCount: 3},
        {subjectName: '生物', lessonCount: 2},
        {subjectName: '政治', lessonCount: 2},
        {subjectName: '历史', lessonCount: 2},
        {subjectName: '地理', lessonCount: 2},
        {subjectName: '信息', lessonCount: 2},
        {subjectName: '音乐', lessonCount: 1},
        {subjectName: '美术', lessonCount: 1},
        {subjectName: '体育', lessonCount: 2},
        {subjectName: '英语口语', lessonCount: 1}
    ];

    var FZBCourseList = [
        {className: 'G1605', count: 47, subjectName: '语文', lessonCount: 5, teacherName: '高同英'},
        {className: 'G1609', count: 45, subjectName: '语文', lessonCount: 5, teacherName: '陈跃年'},
        {className: 'G1610', count: 47, subjectName: '语文', lessonCount: 5, teacherName: '杨玲'},
        {className: 'G1605', count: 47, subjectName: '数学', lessonCount: 5, teacherName: '杨海燕'},
        {className: 'G1609', count: 45, subjectName: '数学', lessonCount: 5, teacherName: '张耀华'},
        {className: 'G1610', count: 47, subjectName: '数学', lessonCount: 5, teacherName: '张耀华'},
        {className: 'G1605', count: 47, subjectName: '英语', lessonCount: 4, teacherName: '杜静'},
        {className: 'G1609', count: 45, subjectName: '英语', lessonCount: 4, teacherName: '杜静'},
        {className: 'G1610', count: 47, subjectName: '英语', lessonCount: 4, teacherName: '骆杏元'},
        {className: 'G1605', count: 47, subjectName: '物理', lessonCount: 3, teacherName: '周运生'},
        {className: 'G1609', count: 45, subjectName: '物理', lessonCount: 3, teacherName: '李惠'},
        {className: 'G1610', count: 47, subjectName: '物理', lessonCount: 3, teacherName: '曾团芳'},
        {className: 'G1605', count: 47, subjectName: '化学', lessonCount: 3, teacherName: '魏加录'},
        {className: 'G1609', count: 45, subjectName: '化学', lessonCount: 3, teacherName: '徐宜武'},
        {className: 'G1610', count: 47, subjectName: '化学', lessonCount: 3, teacherName: '徐宜武'},
        {className: 'G1605', count: 47, subjectName: '生物', lessonCount: 2, teacherName: '刘丽华'},
        {className: 'G1609', count: 45, subjectName: '生物', lessonCount: 2, teacherName: '贺迎飞'},
        {className: 'G1610', count: 47, subjectName: '生物', lessonCount: 2, teacherName: '刘丽华'},
        {className: 'G1605', count: 47, subjectName: '政治', lessonCount: 2, teacherName: '李素红'},
        {className: 'G1609', count: 45, subjectName: '政治', lessonCount: 2, teacherName: '李素红'},
        {className: 'G1610', count: 47, subjectName: '政治', lessonCount: 2, teacherName: '刘瑜'},
        {className: 'G1605', count: 47, subjectName: '历史', lessonCount: 2, teacherName: '江广文'},
        {className: 'G1609', count: 45, subjectName: '历史', lessonCount: 2, teacherName: '江广文'},
        {className: 'G1610', count: 47, subjectName: '历史', lessonCount: 2, teacherName: '田雨'},
        {className: 'G1605', count: 47, subjectName: '地理', lessonCount: 2, teacherName: '周卫东'},
        {className: 'G1609', count: 45, subjectName: '地理', lessonCount: 2, teacherName: '周卫东'},
        {className: 'G1610', count: 47, subjectName: '地理', lessonCount: 2, teacherName: '匡焕新'},
        {className: 'G1605', count: 47, subjectName: '信息', lessonCount: 2, teacherName: '杜新宇'},
        {className: 'G1609', count: 45, subjectName: '信息', lessonCount: 2, teacherName: '杜新宇'},
        {className: 'G1610', count: 47, subjectName: '信息', lessonCount: 2, teacherName: '杜新宇'},
        {className: 'G1605', count: 47, subjectName: '音乐', lessonCount: 1, teacherName: '熊立东'},
        {className: 'G1609', count: 45, subjectName: '音乐', lessonCount: 1, teacherName: '张依屏'},
        {className: 'G1610', count: 47, subjectName: '音乐', lessonCount: 1, teacherName: '张依屏'},
        {className: 'G1605', count: 47, subjectName: '美术', lessonCount: 1, teacherName: '史静'},
        {className: 'G1609', count: 45, subjectName: '美术', lessonCount: 1, teacherName: '史静'},
        {className: 'G1610', count: 47, subjectName: '美术', lessonCount: 1, teacherName: '史静'},
        {className: 'G1605', count: 47, subjectName: '体育', lessonCount: 2, teacherName: '体育老师'},
        {className: 'G1609', count: 45, subjectName: '体育', lessonCount: 2, teacherName: '体育老师'},
        {className: 'G1610', count: 47, subjectName: '体育', lessonCount: 2, teacherName: '体育老师'},
        {className: 'G1605', count: 47, subjectName: '英语口语', lessonCount: 1, teacherName: '外教'},
        {className: 'G1609', count: 45, subjectName: '英语口语', lessonCount: 1, teacherName: '外教'},
        {className: 'G1610', count: 47, subjectName: '英语口语', lessonCount: 1, teacherName: '外教'},
    ];


    function template(tmpl, ctx, data) {
        Common.render({
            tmpl: tmpl,
            context: ctx,
            data: data,
            overwrite: 1
        });
    }

    function zbSubjectList() {
        template('#ZBSubjectTmpl', '#ZBSubjectCtx', ZBSubjectList);
    }


    function subjects() {
        template('#subjectTmpl', '#subjectCtx', ZBSubjectList);
        template('#subjectTmpl', '#FZBSubjectCtx', FZBSubjectList);
    }

    function teachers() {
        template('#teacherTmpl', '#teacherCtx', ZBTeachers);
    }

    function addZBCourse() {

        var course = {};

        var zbCourseList = localStorage.getItem('zbCourseList') ? localStorage.getItem('zbCourseList') : [];
        zbCourseList.push(course);
        localStorage.setItem('zbCourseList', zbCourseList);
    }


    function fzbSubjectList() {
        template('#FZBSubjectListTmpl', '#FZBSubjectListCtx', FZBSubjectList);
        template('#subjectListTmpl', '#subjectListCtx', FZBSubjectList);
    }


    function getFZBCourseList(subject) {
        var courseList = FZBCourseList;

        if (subject && subject != "") {
            courseList = [];
            for (var i = 0; i < FZBCourseList.length; i++) {
                if (FZBCourseList[i].subjectName == subject) {
                    courseList.push(FZBCourseList[i]);
                }
            }
        }

        template('#FZBCourseListTmpl', '#FZBCourseListCtx', courseList);
    }


    module.exports = subjectConf;

});