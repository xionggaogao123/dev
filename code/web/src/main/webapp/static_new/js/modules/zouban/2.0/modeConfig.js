/**
 * Created by Wangkaidong on 2016/7/13.
 */

define(function (require, exports, module) {
    //require('jquery');
    require('doT');
    require('pagination');
    var Common = require('common');
    var modeConfig = {};

    modeConfig.init = function () {
        getSchoolList(1);
    }

    $(document).ready(function () {
        $('body').on('click', '#search', function () {
            var key = $(this).siblings('input').val();
            search(key, 1);
        });

        $('body').on('click', '#addSchool', function () {
            $('.bg').show();
            $('#schoolWindow').show();
            $('#submitSc').attr('schoolModeId', '');
        });
        $('body').on('blur', '#schoolId', function () {
            if ($('#schoolId').val().length !=24) {
                alert('请输入正确的学校id');
                return;
            } else {
                getSchoolName($(this).val());
            }
        });
        $('body').on('click', '#closeSc', function () {
            $('#schoolId').val('');
            $('#schoolName').val('');
            $('input[name="mode"][value != "0"]').attr('checked', false);
            $('#schoolId').attr('readonly', false).removeClass('readonly');
            $('.bg').hide();
            $('#schoolWindow').hide();
        });
        $('body').on('click', '.edit', function () {
            var schoolModeId = $(this).attr('schoolModeId');
            $('#submitSc').attr('schoolModeId', schoolModeId);
            getSchoolMode(schoolModeId);
            $('#schoolId').attr('readonly', true).addClass('readonly');
            $('.bg').show();
            $('#schoolWindow').show();
        });
        $('body').on('click', '#submitSc', function () {
            if ($('#schoolId').val().length !=24) {
                alert('请输入正确的学校id');
                return;
            }
            if ($(this).attr('schoolModeId') == '') {
                addSchool();
            } else {
                updateSchoolMode();
            }
            $('#schoolId').val('');
            $('#schoolName').val('');
            $('input[name="mode"][value != "0"]').attr('checked', false);
            $('#schoolId').attr('readonly', false).removeClass('readonly');
            $('.bg').hide();
            $('#schoolWindow').hide();
        });
        $('body').on('click', '.del', function () {
            if (confirm('确定要删除吗？')) {
                deleteSchool($(this).attr('schoolModeId'));
            } else {
                return;
            }
        });

        $('body').on('click', '.light>span', function () {
            $('#grade').text($(this).text());
            $('#setGrade').attr({'gid': $(this).attr('gid'), 'schoolModeId': $(this).attr('schoolModeId')});
            setGradeModeList($(this).attr('schoolModeId'));
            var gmode = $(this).attr('gmode');
            $('input[name="gradeMode"][value="' + gmode + '"]').prop('checked', true);
            $('.bg').show();
            $('#gradeWindow').show();
        });
        $('body').on('click', '#closeGr', function () {
            $('#setGrade').attr({'gid': '', 'schoolModeId': ''});
            $('.bg').hide();
            $('#gradeWindow').hide();
        });
        $('body').on('click', '#setGrade', function () {
            updateGradeMode($(this).attr('schoolModeId'), $(this).attr('gid'));
            $('#setGrade').attr({'gid': '', 'schoolModeId': ''});
            $('.bg').hide();
            $('#gradeWindow').hide();
        });
    });


    /**
     * 获取学校列表
     * @param page
     */
    function getSchoolList(page) {
        var url = '/zouban/modeConf/getZoubanModeList.do';
        var param = {};
        param.page = page;
        param.pageSize = 20;

        Common.getData(url, param, function (data) {
            Common.render({
                tmpl: '#schoolListTmpl',
                data: data.modeList,
                context: '#schoolListCtx',
                overwrite: 1
            });

            var isInit = true;
            $('.new-page-links').html("");
            if (data.modeList.length > 0) {
                $('.new-page-links').jqPaginator({
                    totalPages: Math.ceil(data.count / data.pageSize) == 0 ? 1 : Math.ceil(data.count / data.pageSize),//总页数
                    visiblePages: 5,//分多少页
                    currentPage: parseInt(data.page),//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (isInit) {
                            isInit = false;
                        } else {
                            getSchoolList(n);
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取学校名称
     * @param schoolId
     */
    function getSchoolName(schoolId) {
        var url = '/zouban/modeConf/getSchoolName.do';
        var param = {};
        param.schoolId = schoolId;

        Common.getData(url, param, function (resp) {
            if (resp) {
                $('#schoolName').val(resp.schoolName);
            }
        });
    }

    /**
     * 搜索
     * @param key
     */
    function search(key, page) {
        var url = '/zouban/modeConf/search.do';
        var param = {};
        param.key = key;
        param.page = page;
        param.pageSize = 20;

        Common.getData(url, param, function (data) {
            Common.render({
                tmpl: '#schoolListTmpl',
                data: data.modeList,
                context: '#schoolListCtx',
                overwrite: 1
            });

            var isInit = true;
            $('.new-page-links').html("");
            if (data.modeList.length > 0) {
                $('.new-page-links').jqPaginator({
                    totalPages: Math.ceil(data.count / data.pageSize) == 0 ? 1 : Math.ceil(data.count / data.pageSize),//总页数
                    visiblePages: 5,//分多少页
                    currentPage: parseInt(data.page),//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (isInit) {
                            isInit = false;
                        } else {
                            search(key, n);
                        }
                    }
                });
            }
        });
    }

    /**
     * 添加学校
     */
    function addSchool() {
        var url = '/zouban/modeConf/addSchool.do';
        var param = {};
        param.schoolId = $('#schoolId').val();
        param.schoolName = $('#schoolName').val();
        param.mode = [];

        $('input[name="mode"]:checked').each(function () {
            param.mode.push({
                name: $(this).next('span').text(),
                value: $(this).val()
            });
        });

        $.ajax({
            url: url,
            type: 'post',
            data: JSON.stringify(param),
            contentType: 'application/json',
            success: function (resp) {
                if (resp.code == '200') {
                    getSchoolList(1);
                } else {
                    alert("添加失败！  " + resp.message);
                }
            }
        });
    }

    /**
     * 获取学校走班模式
     * @param id
     */
    function getSchoolMode(id) {
        var url = '/zouban/modeConf/getSchoolMode.do';
        var param = {};
        param.id = id;

        Common.getData(url, param, function (resp) {
            var school = resp.schoolMode;
            $('#schoolId').val(school.schoolId);
            $('#schoolName').val(school.schoolName);
            var modeList = school.mode;

            for (var i = 0; i < modeList.length; i++) {
                var value = modeList[i].value;
                $('input[name="mode"][value="'+ value +'"]').each(function () {
                    $(this).prop('checked', true);
                });
            }
        });
    }

    /**
     * 更新学校走班模式
     */
    function updateSchoolMode() {
        var url = '/zouban/modeConf/updateSchool.do';
        var param = {};
        param.id = $('#submitSc').attr('schoolModeId');
        param.mode = [];

        $('input[name="mode"]:checked').each(function () {
            param.mode.push({
                name: $(this).next().text(),
                value: $(this).val()
            });
        });

        $.ajax({
            url: url,
            type: 'post',
            data: JSON.stringify(param),
            contentType: 'application/json',
            success: function (resp) {
                if (resp.code == '200') {
                    getSchoolList(1);
                } else {
                    alert('更新失败');
                }
            }
        });
    }

    /**
     * 删除学校
     * @param id
     */
    function deleteSchool(id) {
        var url = '/zouban/modeConf/deleteSchool/' + id + '.do';
        Common.getPostData(url, {}, function (resp) {
            if (resp.code == '200') {
                getSchoolList(1);
            } else {
                alert('删除失败');
            }
        })
    }

    /**
     * 获取年级走班模式
     * @param id
     */
    function setGradeModeList(id) {
        var url = '/zouban/modeConf/getSchoolMode.do';
        var param = {};
        param.id = id;

        Common.getData(url, param, function (resp) {
            var modeList = resp.schoolMode.mode;
            Common.render({
                tmpl: '#gradeModeListTmpl',
                data: modeList,
                context: '#gradeModeListCtx',
                overwrite: 1
            });
        });
    }

    /**
     * 修改年级走班模式
     * @param id
     */
    function updateGradeMode(id, gradeId) {
        var url = '/zouban/modeConf/updateGrade.do';
        var param = {};
        param.id = id;
        param.gradeId = gradeId;
        param.mode = $('input[name="gradeMode"]:checked').val();

        Common.getPostData(url, param, function (resp) {
            if (resp.code == '200') {
                getSchoolList(1);
                alert('修改成功');
            } else {
                alert('修改失败');
            }
        });
    }


    module.exports = modeConfig;
});
