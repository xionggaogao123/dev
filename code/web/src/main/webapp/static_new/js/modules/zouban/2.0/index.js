/**
 * Created by Wangkaidong on 2016/7/11.
 *
 * 7.27 15:42
 */

define(function (require, exports, module) {
    /**
     *初始化参数
     */
    require('rome');
    require('layer');
    var Common = require('common');
    var Paginator = require('initPaginator');
    var zoubanIndex = {};
    var STATE = 0;
    var TIMETABLE_STATE = false; //课表是否发布
    var TIMEEVENT = [];


    var DAY = {
        1: '周一',
        2: '周二',
        3: '周三',
        4: '周四',
        5: '周五',
        6: '周六',
        7: '周日'
    };

    var SECTION = {
        1: '第一节',
        2: '第二节',
        3: '第三节',
        4: '第四节',
        5: '第五节',
        6: '第六节',
        7: '第七节',
        8: '第八节',
        9: '第九节',
        10: '第十节',
        11: '第十一节',
        12: '第十二节'
    };

    var CLASSTIME = [];
    CLASSTIME[0] = "08:00~08:45";
    CLASSTIME[1] = "08:55~09:40";
    CLASSTIME[2] = "10:00~10:45";
    CLASSTIME[3] = "10:55~11:40";
    CLASSTIME[4] = "13:00~13:45";
    CLASSTIME[5] = "13:55~14:40";
    CLASSTIME[6] = "15:00~15:45";
    CLASSTIME[7] = "15:55~16:40";
    CLASSTIME[8] = "17:00~17:50";
    CLASSTIME[9] = "19:00~19:50";
    CLASSTIME[10] = "20:00~20:50";
    CLASSTIME[11] = "21:00~21:50";


    zoubanIndex.init = function () {
        getTermList();
        getGradeList();

        if ($('body').attr('term') != '') {
            $('#termListCtx').val($('body').attr('term'));
            $('#gradeListCtx').val($('body').attr('gradeId'));
        }

        getState();
        getClassroomList(1);
        getTermConf();
        getGradeMode();

        TIMETABLE_STATE = isPublic();

        $('#kbGradeListCtx').hide();
        $('#kbClassListCtx').hide();
    };


    $(document).ready(function () {
        $('#kbClassListCtx').change(function () {
            if ($(this).val() == "All") {
                getTimetableConf();
            } else {
                getClassTimetableEvent();
            }
        });

        $('#kbGradeListCtx').change(function () {
            getTimetableConf();
        });

        $('#kbTermList').change(function () {
            $('#kbGradeListCtx option:eq(0)').attr('selected', 'selected');
            getTimetableConf();
        });

        $(".alert-close,.alert-btn-qx").click(function () {
            $(".classset-alert").hide();
            $('.bg').hide();
        })

        $(".alert-btn-next").click(function () {
            $("#eventTime").hide();
            $("#eventTeacher").show();
        })

        $('body').on('click', '.alert-btn-prev', function () {
            $("#eventTime").show();
            $("#eventTeacher").hide();
        });
        $(".alert-close,.alert-btn-qx").click(function () {
            $("#eventTime").show();
            $("#eventTeacher").hide();
        })

        $(".alert-close,.alert-btn-qx").click(function () {
            $(".alert-now").show();
            $(".alert-next").hide();
        })
        $(".table-edit").click(function () {
            $(".zxjs-alert").show();
        })
        $(".alert-close,.alert-btn-qx").click(function () {
            $(".zxjs-alert").hide();
        })
        $(".table-xq").click(function () {
            $(".zxjs-index").hide();
            $(".zxjs-xq").show();
        })
        $(".table-xq-edit").click(function () {
            $(".xq-edit-alert").show();
        })
        $(".alert-close,.alert-btn-qx").click(function () {
            $(".xq-edit-alert").hide();
        });




        //====================================================课表结构设置================================================
        $('body').on('click', '#SUB', function () {
            $('#kb').show();
            $('#sw').hide();
            $(this).addClass("zb-active").siblings().removeClass("zb-active");
            $('#tab-KBJG').show();
            $('#tab-SWSZ').hide();
            getTimetableConf();
        });

        $('body').on('click', '#lock', function () {
            if(TIMETABLE_STATE) {
                layer.alert('课表已发布，不能更改课表配置');
            } else {
                layer.confirm('如果已经排课，将会清空相关课程，确定要修改吗？', function(index) {
                    lockConf();
                    layer.close(index);
                });
            }
        });
        $('body').on('click', 'input:checkbox[name="days"]', function () {
            var day = $(this).val();
            $('#timetableCtx tr').each(function () {
                $(this).find('td').eq(day).toggleClass('zouban-unuse');
            });
            $('#timetableCtx tr').find('[x=' + day + ']').each(function () {
                if ($(this).find('span').text() != "") {
                    $(this).toggleClass('even-color');
                    $(this).find('span').toggle();
                }
            });
        });

        $('body').on('change', '#classCountSelect', function () {
            changeConf();
        });


        //===================================================事务设置====================================================
        $('body').on('click', '#TES', function () {
            $('#kb').hide();
            $('#sw').show();
            $(this).addClass("zb-active").siblings().removeClass("zb-active");
            $('#tab-KBJG').hide();
            $('#tab-SWSZ').show();

            getEventList();
        });

        $('body').on('change', '#kbTermList2', function() {
            getEventList();
        });


        $('body').on('click', '.new-add', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布，不能添加事务！');
            } else {
                $("#setEventWindow").attr('eventId', '').show();
                $('.bg').show();
                generateEventPanel();
                $('#eventTime').show();
                $('#eventTeacher').hide();
            }
        });

        $('body').on('click', '.editEvent', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布，不可编辑！');
            } else {
                var eventId = $(this).attr('eventId');
                $("#setEventWindow").attr('eventId', eventId).show();
                $('.bg').show();
                generateEventPanel(eventId);
                $('#eventTime').show();
                $('#eventTeacher').hide();
            }
        });
        $('body').on('click', '#submitEvent', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                if (!$('#eventName').val()) {
                    layer.alert('请输入事务名');
                    return;
                } else {
                    addOrUpdateEvent();
                }
            }
        });
        $('body').on('click', '.delEvent', function () {
            var eventId = $(this).attr('eventId');
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布,不能删除事务！');
            } else {
                layer.confirm('确定要删除该事务吗？', function (index) {
                    delEvent(eventId);
                    layer.close(index);
                });
            }
        });

        $(".set-list li").click(function () {
            $(".tab-main>div").hide();
            $(".set-index").hide();
            var names = $(this).attr("id");
            $(".back-index").show();
            $("#" + "tab-" + names).show();
            if (names == "KBPZ") {
                getTimetableConf();
            } else if(names == "ZNGL"){
                window.location.href = window.location.href;
            }
        });
        $(".back-index").click(function(){
            $(".set-index").show();
            $(".tab-main>div,.back-index").hide();
        });


        //----------------------------------------------------教务管理---------------------------------------------------
        function gotoStep(url) {
            var term = $('#termListCtx').val();
            var gradeId = $('#gradeListCtx').val();
            var gradeName = $('#gradeListCtx option:selected').text();
            window.location.href = url + "?term=" + term + "&gid=" + gradeId + "&gnm=" + gradeName;
        }

        $('body').on('click', '#step1', function () {
            if (STATE >= 1) {
                if (STATE < 5) {
                    gotoStep('/zouban/subjectConfig.do');
                } else {
                    layer.alert("课表已发布！如果需要修改本步骤，请取消发布");
                    return;
                }

            } else {
                return;
            }
        });
        $('body').on('click', '#step2', function () {
            if (STATE >= 2) {
                if (STATE < 5) {
                    gotoStep('/zouban/studentXuanke.do');
                } else {
                    layer.alert("课表已发布！如果需要修改本步骤，请取消发布");
                    return;
                }

            } else {
                return;
            }
        });
        $('body').on('click', '#step3', function () {
            if (STATE >= 3) {
                if (STATE < 5) {
                    gotoStep('/zouban/fenban.do');
                } else {
                    layer.alert("课表已发布！如果需要修改本步骤，请取消发布");
                    return;
                }

            } else {
                return;
            }
        });
        $('body').on('click', '#step4', function () {
            if (STATE >= 4) {
                gotoStep('/zouban/paike.do');
            } else {
                return;
            }
        });
        $('body').on('click', '#step5', function () {
            if (STATE >= 5) {
                gotoStep("/zouban/kebiao.do");
            } else {
                return;
            }
        });
        $('body').on('click', '#step6', function () {
            if (STATE >= 6) {
                gotoStep("/zouban/tiaoke.do");
            } else {
                return;
            }
        });

        $('body').on('click', '.even', function () {
            if(TIMETABLE_STATE) {
                layer.alert('课表已发布，不可设置!');
            } else {
                $(this).toggleClass('even-color');
                var term = $('#kbTermList').val();
                var gradeId = $('#kbGradeListCtx').val();
                var x = $(this).attr('x');
                var y = $(this).attr('y');
                lockTimeTableConf();

                if ($(this).html() == "") {
                    var requestParam = {};
                    requestParam.term = term;
                    requestParam.gradeId = gradeId;
                    requestParam.x = x;
                    requestParam.y = y;
                    Common.getPostData('/zouban/timetableConf/addClassEventList.do', requestParam, function (resp) {
                        if (resp.code = 200) {
                            getTimetableConf();
                        }
                    });
                } else if ($(this).html() != "") {
                    var requestParam = {};
                    requestParam.term = term;
                    requestParam.gradeId = gradeId;
                    requestParam.x = x;
                    requestParam.y = y;
                    Common.getData('/zouban/timetableConf/removeClassEvent.do', requestParam, function (resp) {
                        if (resp.code = 200) {
                            getTimetableConf();
                        }
                    });
                }
            }

        });
        $('body').on('click', '.evenClass', function () {
            $(this).toggleClass('even-color');
            var term = $('#kbTermList').val();
            var gradeId = $('#kbGradeListCtx').val();
            var classId = $('#kbClassListCtx').val();
            var x = $(this).attr('x');
            var y = $(this).attr('y');
            if ($(this).html() == "") {
                //$(this).html("无课");
                //设置事务
                var requestParam = {};
                requestParam.term = term;
                requestParam.gradeId = gradeId;
                requestParam.x = x;
                requestParam.y = y;
                requestParam.classId = classId;
                Common.getPostData('/zouban/timetableConf/addEventClassList.do', requestParam, function (resp) {
                    if (resp.code = 200) {
                        getClassTimetableEvent();
                    }
                });
            } else if ($(this).html() != "") {
                //$(this).html("");
                var requestParam = {};
                requestParam.term = term;
                requestParam.gradeId = gradeId;
                requestParam.classId = classId;
                requestParam.x = x;
                requestParam.y = y;
                Common.getPostData('/zouban/timetableConf/removeEventClass.do', requestParam, function (resp) {
                    if (resp.code = 200) {
                        getClassTimetableEvent();
                    }
                });
            }

        });


        $('body').on('change', '#termListCtx', function () {
            getState();
        });

        $('body').on('change', '#gradeListCtx', function () {
            getGradeMode();
            getState();
        });


        //---------------------------------------------------教室管理----------------------------------------------------
        $('body').on('click', '#addClassroom', function () {
            getNotArrangeClassroom('', '');
            $(".bg").show();
            $(".popup-JSGL").show();
        });
        $('body').on('click', '.editClassroom', function () {
            var classroomId = $(this).attr('classroomId');
            var classroomName = $(this).attr('classroomName');
            var classId = $(this).attr('classId');
            var className = $(this).attr('className');
            getNotArrangeClassroom(className, classId);
            $('#classroomName').val(classroomName);
            $('#classListCtx').val(classId);
            $('.JSGL-TJ').attr('classroomId', classroomId);
            $(".bg").show();
            $(".popup-JSGL").show();
        });
        $('body').on('click', '.deleteClassroom', function () {
            var classroomId = $(this).attr('classroomId');
            layer.confirm('确定要删除教室？', function (index) {
                removeClassroom(classroomId);
                layer.close(index);
            });
        });
        $('body').on('click', '.JSGL-TJ', function () {
            var classroomId = $(this).attr('classroomId');
            var classroomName = $('#classroomName').val().trim().replace(/\(/g, '（').replace(/\s/g, '');
            var classId = $('#classListCtx').val();

            Common.getData('/classroom/isExist.do', {name: classroomName}, function (resp) {
                if (resp.code == '200') {
                    layer.alert('该教室已存在，请重新命名');
                    return;
                } else {
                    if (classroomId != '') {
                        updateClassroom(classroomId, classroomName, classId);
                    } else {
                        addClassroom(classroomName, classId);
                    }
                    clearClassroom();
                    $(".bg").hide();
                    $(".popup-JSGL").hide();
                }
            });

        });
        $('body').on('click', '.closeClassRoom', function () {
            clearClassroom();
            $(".bg").hide();
            $(".popup-JSGL").hide();
        });
        function clearClassroom() {
            $('#classroomName').val('');
            $('#classListCtx option:eq(0)').attr('selected', 'selected');
            $('.JSGL-TJ').attr('classroomId', '');
        }

        //---------------------------------------------------教学周管理--------------------------------------------------
        $('body').on('click', '.right3-1', function () {
            if(TIMETABLE_STATE) {
                layer.alert("课表已发布，不能设置教学周！");
            } else {
                getTerm();
                $('.bg').show();
                $('.weekwind').show();
            }
        });
        $('body').on('click', '.close', function () {
            $('.bg').hide();
            $('.weekwind').hide();
        });
        $('body').on('click', '.week-conf', function () {
            addTerm();
            $('.bg').hide();
            $('.weekwind').hide();
        });
        $('body').on('click','.AllSelect', function(){
            if ($(this).prop("checked")) {
                $(this).closest('tr').find('input:checkbox[name="teacher"]').prop("checked", true);
            } else {
                $(this).closest('tr').find('input:checkbox[name="teacher"]').removeAttr("checked");
            }
        });
        $('body').on('click','.selectTeacher',function(){
            if($(this).prop("checked")){
                $(this).closest('tr').find('input:checkbox[name="teacher"]').each(function(){
                    if($(this).prop("checked")){
                        $(this).closest('tr').find('input:checkbox[name="AllSelect"]').prop("checked", true);
                    }else{
                        $(this).closest('tr').find('input:checkbox[name="AllSelect"]').removeAttr("checked");
                        return false;
                    }
                })
            }else{
                $(this).closest('tr').find('input:checkbox[name="AllSelect"]').removeAttr("checked");
            }
        })


    });


    /**
     * 新增/更新事务
     */
    function addOrUpdateEvent() {
        var eventId = $('#setEventWindow').attr('eventId');
        var eventName = $('#eventName').val();
        var pointList = [];
        var teacherList = [];

        $('input:checkbox[name="time"]:checked').each(function () {
            pointList.push({
                x: $(this).attr('x'),
                y: $(this).attr('y')
            });
        });
        $('input:checkbox[name="teacher"]:checked').each(function () {
            teacherList.push({
                id: $(this).val(),
                name: $(this).attr('teacherName')
            });
        });

        var event = {
            id: eventId,
            name: eventName,
            pointList: pointList,
            teacherList: teacherList
        };

        var url = '/zouban/timetableConf/addOrUpdateEvent.do?term=' + $('#kbTermList2').val();

        $.ajax({
            url: url,
            type: 'POST',
            data: JSON.stringify(event),
            contentType: 'application/json',
            success: function (resp) {
                if (resp.code == '200') {
                    getEventList();
                } else {
                    if (eventId) {
                        layer.alert('更新失败');
                    } else {
                        layer.alert('新增失败');
                    }
                }
                $('.bg').hide();
                $('#setEventWindow').hide();
            },
            error: function () {
                layer.alert('更新失败');
            }
        });
    }


    /**
     * 删除事务
     * @param eventId
     */
    function delEvent(eventId) {
        var url = '/zouban/timetableConf/removeEvent.do';
        var param = {};
        param.term = $('#kbTermList2').val();
        param.eventId = eventId;

        Common.getPostData(url, param, function (resp) {
            if (resp.code == '200') {
                getEventList();
            } else {
                layer.alert('删除失败,' + resp.message);
            }
        });
    }

    /**
     * 获取班级列表
     */
    function getClassList() {
        var url = '/zouban/common/getClassList.do';
        var param = {};
        param.gradeId = $('#kbGradeListCtx').val();

        Common.getData(url, param, function (resp) {
            template('#kbClassListTmpl', '#kbClassListCtx', resp.classList);
        });
    }


    /**
     * 设置课表结构
     */
    function changeConf() {
        //上课天数
        var usedDays = [0, 0, 0, 0, 0, 0, 0];
        $('input:checkbox[name="days"]:checked').each(function () {
            usedDays[$(this).val() - 1] = 1;
        });

        var classCount = $('#classCountSelect').val();

        //上课节数
        var section = [];
        //上课时间
        var classTime = [];
        for (var i = 0; i < classCount; i++) {
            section.push(i + 1);
            classTime.push(CLASSTIME[i]);
        }
        generateConf('#timetableTmpl', '#timetableCtx', usedDays, section, classTime, TIMEEVENT);
    }


    /**
     * 先保存课表配置
     */
    function lockTimeTableConf() {
        var url = '/zouban/timetableConf/lock.do';
        var param = {};
        param.term = $('#kbTermList').val();
        param.gradeId = "";
        param.classCount = $('#classCountSelect').val();
        param.days = [];
        param.classTime = [];
        param.eventList = [];
        param.lock = 1;

        $('input:checkbox[name="days"]:checked').each(function () {
            param.days.push($(this).val());
        });
        $('input[name="classTime"]').each(function () {
            param.classTime.push($(this).val());
        });

        Common.getPostData(url,JSON.stringify(param),function(resp){
        });
    }

    /**
     * 锁定课表配置
     */
    function lockConf() {
        var url = '/zouban/timetableConf/lock.do';
        var param = {};
        param.term = $('#kbTermList').val();
        param.gradeId = "";
        param.classCount = $('#classCountSelect').val();
        param.days = [];
        param.classTime = [];
        param.eventList = [];
        param.lock = 1;

        $('input:checkbox[name="days"]:checked').each(function () {
            param.days.push($(this).val());
        });
        $('input[name="classTime"]').each(function () {
            param.classTime.push($(this).val());
        });

        $.ajax({
            url: url,
            type: 'POST',
            data: JSON.stringify(param),
            contentType: 'application/json',
            success: function (resp) {
                if (resp.code == '200') {
                    getTimetableConf();
                    layer.alert('保存成功');
                } else {
                    layer.alert('保存失败');
                }
            }
        });
    }

    /**
     * 检查是否有课表发布
     */
    function isPublic() {
        var url = '/zouban/paike/checkPublish.do';
        var param = {};
        param.term = $('#termListCtx2').val();

        var publish = false;
        Common.getData(url, param, function (resp) {
            if (resp.code == '200') {
                publish = resp.message;
            } else {
                layer.alert('发生未知错误，请勿做任何修改');
                console.log(resp.message);
            }
        });
        return publish;
    }

    /**
     * 事务配置
     */
    function getEventTime(timeList) {
        var url = '/zouban/timetableConf/getTimetableConf.do';
        var param = {};
        param.term = $('#kbTermList').val();

        Common.getData(url, param, function (resp) {
            var days = resp.timetableConf.days;
            var classCount = resp.timetableConf.classCount;

            //新增事务-时间-天
            var eventTimeHead = [];
            for (var i = 0; i < days.length; i++) {
                eventTimeHead.push(DAY[days[i]]);
            }

            //新增事务-时间-节
            var eventTimeSection = [];
            for (var i = 0; i < classCount; i++) {
                eventTimeSection.push(SECTION[i + 1]);
            }

            var eventTime = {
                eventTimeHead: eventTimeHead,
                eventTimeSection: eventTimeSection
            };

            template('#eventTimeTmpl', '#eventTimeCtx', eventTime)

            if (timeList) {
                for (var i = 0; i < timeList.length; i++) {
                    $('input:checkbox[name="time"][x="' + timeList[i].x + '"][y="' + timeList[i].y + '"]').prop('checked', true);
                }
            }
        });
    }

    /**
     * 获取学科老师列表
     */
    function getSubjectTeacher(teacherList) {
        var url = '/zouban/timetableConf/subjectTeacher.do';
        var param = {};
        param.term = $('#kbTermList2').val();

        Common.getData(url, param, function (resp) {
            template('#subjectTeacherTmpl', '#subjectTeacherCtx', resp.subjectTeacherList);

            if (teacherList) {
                for (var i = 0; i < teacherList.length; i++) {
                    $('input:checkbox[name="teacher"][value="' + teacherList[i].id + '"]').prop('checked', true);
                }
            }
            //全选按钮
            $('.AllSelect').each(function(){
                var that=this;
                $(this).closest('tr').find('input:checkbox[name="teacher"]').each(function(){
                    if($(this).prop("checked")){
                        $(that).prop("checked", true);
                    }else{
                        $(that).removeAttr("checked");
                        return false;
                    }
                });
            });
        });
    }

    /**
     * 绘制新增事务弹窗内容
     */
    function generateEventPanel(eventId) {
        if (!eventId) {
            getEventTime();
            getSubjectTeacher();
        } else {
            var url = '/zouban/timetableConf/eventDetail.do';
            var param = {};
            param.term = $('#kbTermList2').val();
            param.eventId = $('#setEventWindow').attr('eventId');

            Common.getData(url, param, function (resp) {
                $('#eventName').val(resp.eventDetail.name);

                var pointList = [];
                var teacherList = [];

                if (resp.eventDetail != null) {
                    pointList = resp.eventDetail.pointList;
                    teacherList = resp.eventDetail.teacherList;

                }
                getEventTime(pointList);
                getSubjectTeacher(teacherList);
            });
        }
    }

    /**
     * 获取事务列表
     */
    function getEventList() {
        var url = '/zouban/timetableConf/getEventList.do';
        var param = {};
        param.term = $('#kbTermList2').val();

        Common.getData(url, param, function (resp) {
            var data = resp.eventList;

            for (var i = 0; i < data.length; i++) {
                var pointList = data[i].pointList;
                data[i].timeList = [];
                for (var j = 0; j < pointList.length; j++) {
                    var x = pointList[j].x;
                    var y = pointList[j].y;
                    data[i].timeList.push(DAY[x] + SECTION[y]);
                }
            }

            template('#eventListTmpl', '#eventListCtx', data);
        });
    }


    function getClassTimetableEvent() {
        var url = '/zouban/timetableConf/classTimeTableEvent.do';
        var param = {};
        var gradeId = $('#kbGradeListCtx').val();
        param.term = $('#kbTermList').val();
        param.gradeId = gradeId;
        param.classId = $('#kbClassListCtx').val();


        Common.getData(url, param, function (resp) {
            var days = resp.timetableConf.days;
            var classCount = resp.timetableConf.classCount;
            var pointDtos = resp.timetableConf.pointDTOs;
            //上课天数
            for (var i = 0; i < days.length; i++) {
                $('input:checkbox[name="days"][value="' + days[i] + '"]').prop('checked', true);
            }
            //上课节数
            $('#classCountSelect').val(classCount);


            //上课节数
            var section = [];
            for (var i = 0; i < classCount; i++) {
                section.push(i + 1);
            }

            var classTime = resp.timetableConf.classTime;
            //上课天数
            var usedDays = [0, 0, 0, 0, 0, 0, 0];
            for (var i = 0; i < days.length; i++) {
                usedDays[i] = 1;
            }
            //事务列表
            var events = [];
            for (var i = 0; i < classCount; i++) {
                events[i] = [];
                for (var j = 0; j < days.length; j++) {
                    events[i][j] = [];
                }
            }

            for (var i = 0; i < pointDtos.length; i++) {
                var pointEvent = pointDtos[i];
                events[pointEvent.y - 1][pointEvent.x - 1].push({
                    name: pointEvent.description
                });
            }
            generateConf('#eventClassTmpl', '#timetableCtx', usedDays, section, classTime, events);
        });

    }

    /**
     * 加载课表结构以及班级事务
     */
    function getTimetableConf() {
        var url = '/zouban/timetableConf/timetableConf.do';
        var param = {};
        var gradeId = $('#kbGradeListCtx').val();
        param.term = $('#kbTermList').val();
        param.gradeId = gradeId;


        Common.getData(url, param, function (resp) {
            var days = resp.timetableConf.days;
            var classCount = resp.timetableConf.classCount;
            //var lock = resp.timetableConf.lock;
            var pointDtos = resp.timetableConf.pointDTOs;
            $('input:checkbox[name="days"]').prop('disabled', false);
            $('#classCountSelect').prop('disabled', false);
            $('#kbGradeListCtx').show();
            $('#TES').show();


            //上课天数
            for (var i = 0; i < days.length; i++) {
                $('input:checkbox[name="days"][value="' + days[i] + '"]').prop('checked', true);
            }
            //上课节数
            $('#classCountSelect').val(classCount);


            //上课节数
            var section = [];
            for (var i = 0; i < classCount; i++) {
                section.push(i + 1);
            }

            var classTime = resp.timetableConf.classTime;
            //上课天数
            var usedDays = [0, 0, 0, 0, 0, 0, 0];
            for (var i = 0; i < days.length; i++) {
                usedDays[i] = 1;
            }
            //事务列表
            var events = [];
            for (var i = 0; i < classCount; i++) {
                events[i] = [];
                for (var j = 0; j < days.length; j++) {
                    events[i][j] = [];
                }
            }

            for (var i = 0; i < pointDtos.length; i++) {
                var pointEvent = pointDtos[i];
                if(pointEvent.x <= days.length && pointEvent.y <= classCount) {
                    events[pointEvent.y - 1][pointEvent.x - 1].push({
                        name: pointEvent.description
                    });
                }
            }
            TIMEEVENT = events;


            generateConf('#classEventTmpl', '#timetableCtx', usedDays, section, classTime, events);
        });
    }

    /**
     * 生成班级事件课表结构table
     *
     * @param section
     * @param classTime
     * @param usedDays
     */
    function generateConf(tmpl, ctx, usedDays, section, classTime, events) {
        var confData = {
            usedDays: usedDays,
            section: section,
            classTime: classTime,
            eventList: events
        };
        template(tmpl, ctx, confData);
    }

    function template(tmpl, ctx, data) {
        Common.render({
            tmpl: tmpl,
            data: data,
            context: ctx,
            overwrite: 1
        });
    }

    /**
     * 获取学年列表
     */
    function getTermList() {
        Common.getData("/zouban/common/getTermList.do", {}, function (resp) {
            var termList = resp.termList;
            template('#termListTmpl', '#termListCtx', termList);
            template('#termListTmpl', '#termListCtx2', termList);
            template('#termListTmpl', '#kbTermList', termList);
            template('#termListTmpl', '#kbTermList2', termList);
        });
    }

    /**
     * 获取年级列表
     */
    function getGradeList() {
        Common.getData("/zouban/common/getGradeList.do", {}, function (resp) {
            Common.render({
                tmpl: '#gradeListTmpl',
                data: resp.gradeList,
                context: '#gradeListCtx',
                overwrite: 1
            });
            Common.render({
                tmpl: '#kbGradeListTmpl',
                data: resp.gradeList,
                context: '#kbGradeListCtx',
                overwrite: 1
            });
        });
    }

    /**
     * 获取年级走班模式
     */
    function getGradeMode() {
        var url = '/zouban/baseConfig/getMode.do';
        var param = {};
        param.gradeId = $('#gradeListCtx').val();

        Common.getData(url, param, function (resp) {
            if (resp && resp.mode == -1) {
                layer.alert("请联系复兰管理员绑定走班模式", function () {
                    window.location.href = "/user/homepage.do";
                });
            } else if (resp && resp.mode != 0) {
                $('#step2, #step3').parent().show();
            } else {
                $('#step2, #step3').parent().hide();
            }
        });
    }


    /**
     * 获取走班进度
     */
    function getState() {
        var url = "/zouban/baseConfig/getState.do";
        var param = {};
        param.term = $('#termListCtx').val();
        param.gradeId = $('#gradeListCtx').val();

        Common.getData(url, param, function (resp) {
            STATE = resp.state;
        });
        switch (STATE) {
            case 1:
                $('#step1').addClass('step-name').removeClass("step-unset");
                $('#step2, #step3, #step4, #step5, #step6').addClass("step-unset").removeClass("step-name");
                break;
            case 2:
                $('#step1,#step2').addClass('step-name').removeClass("step-unset");
                $('#step3, #step4, #step5, #step6').addClass("step-unset").removeClass("step-name");
                break;
            case 3:
                $('#step1,#step2,#step3').addClass('step-name').removeClass("step-unset");
                $(' #step4, #step5, #step6').addClass("step-unset").removeClass("step-name");
                break;
            case 4:
                $('#step1,#step2,#step3, #step4').addClass('step-name').removeClass("step-unset");
                $('#step5, #step6').addClass("step-unset").removeClass("step-name");
                break;
            case 5:
                $('#step1,#step2,#step3, #step4,#step5').addClass('step-name').removeClass("step-unset");
                $('#step6').addClass("step-unset").removeClass("step-name");
                break;
            case 6:
                $('#step1, #step2, #step3, #step4, #step5, #step6').addClass('step-name').removeClass("step-unset");
                break;
        }
    }


    /**
     * 获取教室列表
     * @param page
     */
    function getClassroomList(page) {
        var url = '/classroom/findClassroomListPage.do';
        var param = {};
        param.page = page;
        param.pageSize = 20;

        Common.getData(url, param, function (resp) {
            var option = {
                total: resp.rowCount,
                pagesize: resp.pageSize,
                currentpage: resp.page,
                operate: function (totalPage) {
                    $('.page-index span').each(function () {
                        $(this).one("click", function () {
                            getClassroomList($(this).text());
                        });
                    });
                    $('.first-page').one("click", function () {
                        getClassroomList(1);
                    });
                    $('.last-page').one("click", function () {
                        getClassroomList(totalPage);
                    });
                }
            };
            Paginator.initPaginator(option);

            Common.render({
                tmpl: '#classRoomListTmpl',
                data: resp.rows,
                context: '#classRoomListCtx',
                overwrite: 1
            });
        });
    }

    /**
     * 获取未分配班级
     * @param className
     * @param classId
     */
    function getNotArrangeClassroom(className, classId) {
        var url = '/classroom/findClassInfoBySchoolId.do';

        Common.getData(url, {}, function (resp) {
            var classList = resp.classList;
            if (className != '') {
                classList.push({id: classId, className: className});
            }
            Common.render({
                tmpl: '#classListTmpl',
                data: classList,
                context: '#classListCtx',
                overwrite: 1
            });
        });
    }

    /**
     * 新增教室
     * @param name
     * @param classId
     */
    function addClassroom(name, classId) {
        var url = '/classroom/addClassroom.do';
        var param = {};
        param.name = name;
        param.classId = classId;

        Common.getPostData(url, param, function (resp) {
            if (resp.code = '200') {
                getClassroomList(1);
            } else {
                layer.alert('新增失败');
            }
        });
    }

    /**
     * 更新教室
     * @param classroomId
     * @param name
     * @param classId
     */
    function updateClassroom(classroomId, name, classId) {
        var url = '/classroom/updateClassroom.do';
        var param = {};
        param.classroomId = classroomId;
        param.name = name;
        param.classId = classId;

        Common.getPostData(url, param, function (resp) {
            if (resp.code = '200') {
                getClassroomList(1);
            } else {
                layer.alert('更新失败');
            }
        });
    }

    /**
     * 删除教室
     * @param id
     */
    function removeClassroom(id) {
        var url = '/classroom/removeClassroom.do';
        var param = {};
        param.classroomId = id;

        Common.getPostData(url, param, function (resp) {
            if (resp.code == '200') {
                getClassroomList(1);
            } else {
                layer.alert('删除失败，' + resp.message);
            }
        });
    }

    /**
     * 获取学期配置
     */
    function getTermConf() {
        var url = '/zouban/baseConfig/termConfig.do';
        var param = {};
        param.term = $('#termListCtx2').val();

        Common.getData(url, param, function (resp) {
            var data = resp.termConfig;
            var firstTerm = generalDateList(data.fts, data.fte);
            var secondTerm = generalDateList(data.sts, data.ste);
            var firstTermTime = data.fts + "~" + data.fte;
            var secondTermTime = data.sts + "~" + data.ste;
            var firstTermWeek = data.fweek;
            var secondTermWeek = data.sweek;

            //第一学期
            $("#firstTermWeek").text("第一学期 共" + firstTermWeek + "周");
            $("#firstTermTime").text(firstTermTime);
            Common.render({
                tmpl: '#termWeekTempJS',
                data: firstTerm,
                context: '#firstTermList',
                overwrite: 1
            });
            Common.render({
                tmpl: '#termDateTempJS',
                data: firstTerm,
                context: '#firstTermData',
                overwrite: 1
            });

            //第二学期
            $("#secondTermWeek").text("第二学期 共" + secondTermWeek + "周");
            $("#secondTermTime").text(secondTermTime);
            Common.render({
                tmpl: '#termWeekTempJS',
                data: secondTerm,
                context: '#secondTermList',
                overwrite: 1
            });
            Common.render({
                tmpl: '#termDateTempJS',
                data: secondTerm,
                context: '#secondTermData',
                overwrite: 1
            });
        });
    }

    /**
     * 生成校历
     * @param termStart
     * @param termEnd
     * @returns {Array}
     */
    function generalDateList(termStart, termEnd) {
        var ftDays = [];
        var fts = strToDate(termStart);
        var fts_mon = fts.getMonth();
        var fts_fir_date = fts.getDate();
        var fts_week = fts.getDay();//星期，0~6 从星期天开始
        for (var i = 0; i < fts_week; i++) {
            ftDays.push("");
        }

        var curDate = new Date();
        curDate.setYear(fts.getYear());
        curDate.setMonth(fts_mon + 1);
        curDate.setDate(0);
        var alldays = curDate.getDate();
        var fte = strToDate(termEnd);
        var fte_mon = fte.getMonth();
        if (fte_mon < fts_mon)
            fte_mon += 12;
        var monthDays2 = fte.getDate();
        if (fte_mon == fts_mon) {
            //第一个月
            ftDays.push((fts_mon + 1) + "月/" + fts_fir_date);
            for (fts_fir_date = fts_fir_date + 1; fts_fir_date <= monthDays2; fts_fir_date++) {
                ftDays.push(fts_fir_date);
            }
        }
        else {
            //第一个月
            ftDays.push((fts_mon + 1) + "月/" + fts_fir_date);
            for (fts_fir_date = fts_fir_date + 1; fts_fir_date <= alldays; fts_fir_date++) {
                ftDays.push(fts_fir_date);
            }
        }
        //中间月
        for (var i = fts_mon + 1; i < fte_mon; i++) {
            curDate.setMonth(i + 1);
            if (i >= 12) {
                curDate.setYear(fts.getYear() + 1);
                curDate.setMonth(i - 11);
            }
            curDate.setDate(0);
            var monthDay = curDate.getDate();
            ftDays.push((i + 1) + "月/1");
            for (var j = 2; j <= monthDay; j++) {
                ftDays.push(j);
            }
        }
        if (fte_mon > fts_mon) {
            //最后一个月
            curDate.setYear(fte.getYear());
            if (fte_mon >= 12)
                fte_mon -= 12;
            curDate.setMonth(fte_mon + 1);
            curDate.setDate(0);

            ftDays.push((fte_mon + 1) + "月/1");
            for (var i = 2; i <= monthDays2; i++) {
                ftDays.push(i);
            }
        }
        return ftDays;
    };

    function strToDate(str) {
        var val = Date.parse(str);
        var newDate = new Date(val);
        return newDate;
    }

    /**
     * 获取学期配置
     */
    function getTerm() {
        var url = '/zouban/baseConfig/termConfig.do';
        var param = {};
        param.term = $('#termListCtx2').val();

        Common.getData(url, param, function (resp) {
            var data = resp.termConfig;

            $('.week-conf').attr('termId', data.id);

            $('#term').text(data.year);
            $("#firstStart").val(data.fts);
            $("#firstEnd").val(data.fte);
            $("#secondStart").val(data.sts);
            $("#secondEnd").val(data.ste);
            $("#firstWeek").text("共" + data.fweek + "周");
            $("#secondWeek").text("共" + data.sweek + "周");

            rome(firstStart, {time: false, initialValue: data.fts, dateValidator: rome.val.beforeEq(firstEnd)}).
                off('data').
                on('data', function (value) {
                    var week = calWeekCount(value, $("#firstEnd").val());
                    $("#firstWeek").text("共" + week + "周");
                    $("#s").val(value);
                });
            rome(firstEnd, {time: false, initialValue: data.fte, dateValidator: rome.val.afterEq(firstStart)}).
                off('data').
                on('data', function (value) {
                    var week = calWeekCount($("#firstStart").val(), value);
                    $("#firstWeek").text("共" + week + "周");
                    $("#firstEnd").val(value);
                });
            rome(secondStart, {time: false, initialValue: data.sts, dateValidator: rome.val.beforeEq(secondEnd)}).
                off('data').
                on('data', function (value) {
                    var week = calWeekCount(value, $("#secondEnd").val());
                    $("#secondWeek").text("共" + week + "周");
                    $("#secondStart").val(value);
                });
            rome(secondEnd, {time: false, initialValue: data.ste, dateValidator: rome.val.afterEq(secondStart)}).
                off('data').
                on('data', function (value) {
                    var week = calWeekCount($("#secondStart").val(), value);
                    $("#secondWeek").text("共" + week + "周");
                    $("#secondEnd").val(value);
                });
        });
    };


    /**
     * 自动计算当前共有多少周
     * @param sDate1 开始日期
     * @param sDate2 结束日期
     */
    function calWeekCount(sDate1, sDate2) {
        var days = DateDiff(sDate2, sDate1);
        var start = strToDate(sDate1);
        var fts_week = start.getDay();//星期，0~6 从星期天开始
        days += fts_week;
        if (days % 7 == 0)
            return days / 7 + 1;
        return Math.ceil(days / 7);
    };

    /**
     * 添加学期
     */
    function addTerm() {
        if (checkTermConf()) {
            var url = '/zouban/baseConfig/termConfig.do';
            var param = {};
            param.id = $('.week-conf').attr('termId');
            param.year = $('#termListCtx2').val();
            param.fts = $('#firstStart').val();
            param.fte = $('#firstEnd').val();
            param.sts = $('#secondStart').val();
            param.ste = $('#secondEnd').val();

            $.ajax({
                url: url,
                type: 'post',
                data: JSON.stringify(param),
                contentType: 'application/json',
                success: function (resp) {
                    if (resp.code == "200") {
                        layer.alert('设置成功');
                        getTermConf();
                    } else {
                        layer.alert('设置失败');
                    }
                }
            });
        } else {
            return;
        }
    };

    /**
     * 检查学期设置
     */
    function checkTermConf() {
        var firstTermStart = new Date($("#firstStart").val());
        var firstTermEnd = new Date($("#firstEnd").val());
        var secondTermStart = new Date($("#secondStart").val());
        var secondTermEnd = new Date($("#secondEnd").val());

        if (isNaN(firstTermStart.getTime()) || isNaN(firstTermEnd.getTime()) ||
            isNaN(secondTermStart.getTime()) || isNaN(secondTermEnd.getTime())) {
            layer.alert("请正确输入学期时间");
            return false;
        }
        if (firstTermEnd.getTime() < firstTermStart.getTime()) {
            layer.alert("第一学期放假日期小于开学日期");
            return false;
        }
        if (secondTermEnd.getTime() < secondTermStart.getTime()) {
            layer.alert("第二学期放假日期小于开学日期");
            return false;
        }
        if (secondTermStart.getTime() < firstTermEnd.getTime()) {
            layer.alert("第二学期开学日期小于第一学期放假日期");
            return false;
        }
        if (DateDiff($("#firstStart").val(), $("#firstEnd").val()) > 180) {
            layer.alert("第一学期上课天数过长，请重新设置");
            return false;
        }
        if (DateDiff($("#secondStart").val(), $("#secondEnd").val()) > 180) {
            layer.alert("第二学期上课天数过长，请重新设置");
            return false;
        }
        return true;
    }

    /**
     * 计算时间差
     * @param sDate1
     * @param sDate2
     * @returns {Number}
     * @constructor
     */
    function DateDiff(sDate1, sDate2) {    //sDate1和sDate2是2006-12-18格式
        var aDate, oDate1, oDate2, iDays;
        aDate = sDate1.split("-");
        oDate1 = new Date(aDate[0] + '-' + aDate[1] + '-' + aDate[2]);   //转换为12-18-2006格式
        aDate = sDate2.split("-");
        oDate2 = new Date(aDate[0] + '-' + aDate[1] + '-' + aDate[2]);
        iDays = parseInt(Math.abs(oDate1 - oDate2) / 1000 / 60 / 60 / 24);   //把相差的毫秒数转换为天数
        return iDays;
    }





    module.exports = zoubanIndex;

});
