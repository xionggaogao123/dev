/**
 * Created by wangkaidong on 2016/10/12.
 */
define(function (require, exports, module) {
    require('jquery');
    require('doT');
    require('ajaxForm');
    require('layer');
    require('pagination');
    var Common = require('common');
    var tiaoke = {};

    var TERM = '';
    var GRADEID = '';
    var page = 1;
    tiaoke.init = function () {



        TERM = $('body').attr('term');
        GRADEID = $('body').attr('gradeId');

        setWeek();
        getClassList();
        getNoticeList(page);
    }

    $(document).ready(function () {
        //返回首页
        $('body').on('click', '.fourstep-back', function () {
            window.location.href = "/zouban/baseConfig.do?term=" + $('body').attr('year') + "&gradeId=" + $('body').attr('gradeId');
        });

        //返回
        $('body').on('click', '.adjust-back', function () {
            window.location.href = "/zouban/tiaoke.do?term=" + $('body').attr('year') + "&gid=" + $('body').attr('gradeId') +
                "&gnm=" + $('body').attr('gradeName');
        });

        //新增调课弹窗
        $("#addNotice").click(function () {
            $(".bg").show();
            $(".adjust-save").show();
        });

        //关闭弹窗
        $(".alert-btn-qx,.alert-close").click(function () {
            $(".bg").hide();
            $(".alert-main").parent().hide();
        });

        //保存调课记录
        $('#saveNotice').click(function () {
            if(!$('#userName').val()) {
                layer.alert('调课申请人不能为空');
                return;
            }
            if(!$('#description').val()) {
                layer.alert('调课原因不能为空');
                return;
            }

            saveNotice();
            $(".bg").hide();
            $(".adjust-save").hide();
        });

        //调课页面
        $('body').on('click', '.TKTC', function () {
            $("#tab-TKJL").hide();
            $("#tab-TKTK").show();
            var noticeId = $(this).attr('noticeId');

            $('#saveChange').attr('noticeId', noticeId);

            getNoticeInfo(noticeId);
            getClassTimeTable();
        });

        //调课详情弹窗
        $('body').on('click', '.detail', function () {
            getNoticeDetail($(this).attr('noticeId'));
            $(".bg").show();
            $(".adjust-alert").show();
        });
        //导出
        $('body').on('click', '.DCTK', function(){
            var notivState = $(this).attr('noticeState');
            if(notivState!=0){
                var noticeId = $(this).attr('noticeId');
                window.location = '/zouban/tiaoke/exportTKNotice.do?noticeId='+noticeId;
            }

        });

        $('#startWeekCtx').change(function () {
            var startWeekVal = Number($(this).val());
            var endWeekVal = Number($('#endWeekCtx').val());
            if (startWeekVal > endWeekVal) {
                $('#endWeekCtx').val(startWeekVal);
            }
            checkNotice();
        });

        $('#endWeekCtx').change(function () {
            var startWeekVal = Number($('#startWeekCtx').val());
            var endWeekVal = Number($(this).val());
            if (endWeekVal < startWeekVal) {
                $('#startWeekCtx').val(endWeekVal);
            }
            checkNotice();
        });


        $('#classListCtx').change(function () {
            getClassTimeTable()
        });

        var avaFlag = true;
        var targetPoint = null;

        $('body').on('click', '.tiaoke-available-before', function () {
            if (avaFlag) {
                var teacherId = $(this).attr('teacherId');
                var x = $(this).attr('x');
                var y = $(this).attr('y');
                avaFlag = !getAvailablePointList(teacherId, x, y);
                targetPoint = $(this);
                $(this).removeClass('tiaoke-available-before').addClass('tiaoke-available-after');
            } else {
                return;
            }
        });

        $('body').on('click', '.tiaoke-available-after', function () {
            if (!avaFlag) {
                $('td').removeClass('availablePoint');
                $(this).removeClass('tiaoke-available-after').addClass('tiaoke-available-before');
                avaFlag = true;
                targetPoint = null;
            } else {
                return;
            }
        });

        $('body').on('click', '.availablePoint', function () {
            var p = $(this);
            addChange(targetPoint, p);
            $('td').removeClass('availablePoint').removeClass('tiaoke-available-after');
            targetPoint = null;
            avaFlag = true;
        });

        $('body').on('click', '#saveChange', function() {
            saveChange($(this).attr('noticeId'));
        });

        $('body').on('click', '#cancelChange', function () {
            removeChange();
        });

    });


    function template(tmpl, ctx, data) {
        Common.render({
            tmpl: tmpl,
            context: ctx,
            data: data,
            overwrite: 1
        });
    }

    //开始结束周
    function setWeek() {
        var allweek = Number($('body').attr('allweek'));
        var curweek = Number($('body').attr('curweek'));

        var weekList = [];
        for (var i = curweek == 0 ? 1 : curweek; i <= allweek; i++) {
            weekList.push(i);
        }
        template('#weekSelectTmpl', '#startWeekCtx', weekList);
        template('#weekSelectTmpl', '#endWeekCtx', weekList);
    }


    //班级列表
    function getClassList() {
        var url = '/myschool/classlist.do';
        var param = {};
        param.gradeid = GRADEID;

        Common.getData(url, param, function (resp) {
            template('#classListTmpl', '#classListCtx', resp.rows);
        });

    }

    //保存调课记录
    function saveNotice() {
        var url = '/zouban/tiaoke/addNotice.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.userName = $('#userName').val();
        param.description = $('#description').val();
        param.startWeek = $('#startWeekCtx').val();
        param.endWeek = $('#endWeekCtx').val();
        Common.getData(url, param, function (result) {
            if(result.code == '200') {
                getNoticeList(1);
            } else {
                layer.alert('error');
            }
        });
    }


    /**
     * 查询起始结束周
     *
     * @param noticeId
     */
    function getNoticeInfo(noticeId) {
        var url = '/zouban/tiaoke/getNoticeInfo.do';
        var param = {};
        param.noticeId = noticeId;

        Common.getData(url, param, function (result) {
            if(result.code == '200') {
                $('#startWeek').text("第" + result.message.startWeek + "周").attr('week', result.message.startWeek);
                $('#endWeek').text("第" + result.message.endWeek + "周").attr('week', result.message.endWeek);
            } else {
                layer.alert('error');
            }
        });
    }


    //获取调课通知
    function getNoticeList(page) {
        var url = '/zouban/tiaoke/getZoubanNotice.do';
        var param = {};
        param.page = page;
        param.pageSize = 20;
        param.term = TERM;
        param.gradeId = GRADEID;
        Common.getDataAsync(url, param, function (result) {
            console.log(result);
            var isInit = true;
            $('.new-page-links').html("");
            if (result.zoubanNotice.length > 0) {
                $('.new-page-links').jqPaginator({
                    totalPages: Math.ceil(result.count / result.pageSize) == 0 ? 1 : Math.ceil(result.count / result.pageSize),//总页数
                    visiblePages: 10,//分多少页
                    currentPage: parseInt(result.page),//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (isInit) {
                            isInit = false;
                        } else {
                            getNoticeList(n);
                        }
                    }
                });
            }
            template("#tiokeCtxTmpl", "#tiokeCtx", result.zoubanNotice);
            for (var i = 0; i < result.zoubanNotice.length; i++) {
                $('#newTable').data(result.zoubanNotice[i].id, result.zoubanNotice[i]);
            }
        });
    }

    //获取调课通知详情
    function getNoticeDetail(id) {
        var noticeEntry = $('#newTable').data(id);
        $('.adjust-fir').find('em').html(noticeEntry.userName);
        $('.adjust-sec').find('em').html(noticeEntry.description);
        var content = noticeEntry.noticeDetailDTOs;
        template('#noticeDetailTmpl', '#noticeDetailCtx', content);
    }


    //检查是否可调课
    function checkNotice() {
        var startWeek = Number($('#startWeekCtx').val());
        var endWeek = Number($('#endWeekCtx').val());

        var url = '/zouban/tiaoke/checkNotice.do';
        var param = {};
        param.term = TERM;
        param.startWeek = startWeek;
        param.endWeek = endWeek;

        Common.getData(url, param, function(resp) {
           if(resp.code = '200') {
               if(resp.message == 'false') {
                   $('#endWeekCtx').val($('#startWeekCtx').val());
                   layer.alert('已经进行过临时调课，不能长期调课');
               }
           } else {
               layer.alert(resp.message);
           }
        });
    }



    //行政班课表
    function getClassTimeTable() {
        var classId = $('#classListCtx').val();
        var requestData = {
            term: $('body').attr('term'),
            gradeId: $('body').attr('gradeId'),
            classId: classId,
            startWeek: Number($('#startWeek').attr('week')),
            endWeek: Number($('#endWeek').attr('week'))
        }
        Common.getData("/zouban/tiaoke/getTimetable.do", requestData, function (resp) {
            if (resp.code == '200') {
                var data = generateData(resp);
                template('#classTableTmpl', '#classTable', data);
            } else {
                layer.alert(resp.message);
                $('#startWeekCtx').val(requestData.startWeek);
                $('#endWeekCtx').val(requestData.startWeek);
            }
        })
        $('#tab-FZBK').show();
    }


    var DAY = {
        1: '周一',
        2: '周二',
        3: '周三',
        4: '周四',
        5: '周五',
        6: '周六',
        7: '周日'
    };

    function generateData(resp) {
        var timetableConf = resp.message.timetableConf;
        var courseItemList = resp.message.courseItemList;
        var days = timetableConf.days;
        var classCount = timetableConf.classCount;
        var classTime = timetableConf.classTime;


        //上课天数
        var weekday = [];
        for (var i = 0; i < days.length; i++) {
            weekday.push(DAY[days[i]]);
        }

        //上课节数
        var sections = [];
        for (var i = 0; i < classCount; i++) {
            sections.push(i + 1);
        }


        //课表item列表
        var itemList = [];
        for (var i = 0; i < classCount; i++) {
            itemList[i] = [];
            for (var j = 0; j < days.length; j++) {
                itemList[i][j] = {
                    courseId: "",
                    teacherName: "",
                    className: "",
                    teacherId: "",
                    courseItemId: "",
                    type: 0
                };
            }
        }
        for (var i = 0; i < courseItemList.length; i++) {
            var item = courseItemList[i];
            itemList[item.yIndex - 1][item.xIndex - 1].type = item.type;
            itemList[item.yIndex - 1][item.xIndex - 1].courseId = item.courseId;
            itemList[item.yIndex - 1][item.xIndex - 1].teacherName = item.teacherName;
            itemList[item.yIndex - 1][item.xIndex - 1].className = item.className;
            itemList[item.yIndex - 1][item.xIndex - 1].teacherId = item.teacherId;
            itemList[item.yIndex - 1][item.xIndex - 1].courseItemId = item.courseItemId;
        }


        var data = {
            days: weekday,
            sections: sections,
            classTime: classTime,
            courseItemList: itemList
        };

        return data;
    }


    //获取可用时间点
    function getAvailablePointList(teacherId, x, y) {
        var url = '/zouban/tiaoke/getAvailablePoint.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.classId = $('#classListCtx').val();
        param.teacherId = teacherId;
        param.week = $('#startWeekCtx').val();
        param.x = x;
        param.y = y;

        Common.getData(url, param, function (resp) {
            if (resp.code == '200') {
                var pointList = resp.message;
                if (pointList.length == 0) {
                    return false;
                }
                for (var i = 0; i < pointList.length; i++) {
                    $('td[x=' + pointList[i].x + '][y=' + pointList[i].y + ']').addClass('availablePoint');
                }
            }
        });
        return true;
    }


    function addChange(targetPoint, point) {
        var courseItemId1 = targetPoint.attr('courseItemId');
        var x1 = targetPoint.attr('x');
        var y1 = targetPoint.attr('y');
        var courseItemId2 = point.attr('courseItemId');
        var x2 = point.attr('x');
        var y2 = point.attr('y');

        var url = '/zouban/tiaoke/addChange.do';
        var param = {};
        param.term = TERM;
        param.classId = $('#classListCtx').val();
        param.weekStr = getWeekList();
        param.courseItemId1 = courseItemId1;
        param.x1 = x1;
        param.y1 = y1;
        param.courseItemId2 = courseItemId2;
        param.x2 = x2;
        param.y2 = y2;

        Common.getPostData(url, param, function (resp) {
            if (resp.code == '200') {
                getClassTimeTable();
            } else {
                layer.alert('调课失败\n' + resp.message);
            }
        });
    }


    function getWeekList() {
        var startWeek = Number($('#startWeek').attr('week'));
        var endWeek = Number($('#endWeek').attr('week'));

        var weekList = [];
        for (var i = startWeek; i <= endWeek; i++) {
            weekList.push(i);
        }
        return weekList;
    }


    function saveChange(noticeId) {
        var url = '/zouban/tiaoke/saveChange.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.weekStr = getWeekList();
        param.noticeId = noticeId;

        Common.getPostData(url, param, function (resp) {
            if (resp.code == '200') {
                layer.alert('发布成功！');
            } else {
                layer.alert('发布失败！\n' + resp.message);
            }
            getClassTimeTable();
        });
    }


    function removeChange() {
        var url = '/zouban/tiaoke/removeChange.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;

        Common.getPostData(url, param, function (resp) {
            if (resp.code != '200') {
                layer.alert(resp.message);
            }
            getClassTimeTable();
        });
    }


    module.exports = tiaoke;

});