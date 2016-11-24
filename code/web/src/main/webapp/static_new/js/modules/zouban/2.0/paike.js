/**
 * Created by Wangkaidong on 2016/7/25.
 */

define(function (require, exports, module) {
    require('jquery');
    require('doT');
    require('ajaxForm');
    require('layer');
    var Common = require('common');
    var paike = {};

    var TERM = '';
    var GRADEID = '';
    var TIMETABLE_STATE = false; //课表是否发布

    paike.init = function () {


        TERM = $('body').attr('term');
        GRADEID = $('body').attr('gradeId');
        TIMETABLE_STATE = isPublic();
    }


    $(document).ready(function () {
        function resetWaitFlag() {
            waitFlag = true;
            fzzbWaitFlag = true;
            peWaitFlag = true;
            fzbWaitFlag = true;
            courseId = '';
            fzzbCourseId = '';
            peCourseId = '';
            fzbCourseId = '';
        }

        //返回首页
        $('body').on('click', '.fourstep-back', function () {
            window.location.href = "/zouban/baseConfig.do?term=" + $('body').attr('term') + "&gradeId=" + $('body').attr('gradeId');
        });

        $('body').on('click', '#ZBK', function () {
            if (getLockState()) {
                $(this).addClass('cur').siblings().removeClass('cur');
                $('.tab-main>div').hide();
                $('#tab-ZBK').show();
                getGroupList();
                getArrangingZBCourseList();
                getArrangedZBTimetable();
                resetWaitFlag();
            } else {
                layer.alert('请先锁定课表结构设置');
            }
        });

        $('body').on('click', '#FZZBK', function () {
            if (getLockState()) {
                $(this).addClass('cur').siblings().removeClass('cur');
                $('.tab-main>div').hide();
                $('#tab-FZZBK').show();
                getArrangingFZZBCourse();
                getArrangedFZZBCourse();
                resetWaitFlag();
            } else {
                layer.alert('请先锁定课表结构设置');
            }
        });

        $('body').on('click', '#TYK', function () {
            if (getLockState()) {
                $(this).addClass('cur').siblings().removeClass('cur');
                $('.tab-main>div').hide();
                $('#tab-TYK').show();
                getArrangingPECourse();
                getArrangedPECourse();
                resetWaitFlag();
            } else {
                layer.alert('请先锁定课表结构设置');
            }
        });
        $('body').on('click', '#FZBK', function () {
            if (getLockState()) {
                if (getZBLockState()) {
                    if (checkPEFinished()) {
                        $(this).addClass('cur').siblings().removeClass('cur');
                        $('.tab-main>div').hide();
                        $('#tab-FZBK').show();
                        if (TIMETABLE_STATE) {
                            $('#publishTimetable').text('取消发布');
                        } else {
                            $('#publishTimetable').text('发布课表');
                            $('#tips').show();
                        }
                        getClassList();
                        getArrangingFZBCourse();
                        getArrangedFZBCourse();
                        resetWaitFlag();
                    } else {
                        layer.alert('请先排完体育课');
                    }
                } else {
                    layer.alert('请先排完走班课');
                }
            } else {
                layer.alert('请先锁定课表结构设置');
            }
        });
        $('body').on('click', '#CTSW', function () {
            if (getLockState()) {
                $(this).addClass('cur').siblings().removeClass('cur');
                getEventConflictList();
                $('.tab-main>div').hide();
                $('#tab-CTSW').show();
                resetWaitFlag();
            } else {
                layer.alert('请先锁定课表结构设置');
            }
        });

        $('body').on('click', '#ZXJS', function () {
            if (getLockState) {
                if (getZBLockState()) {
                    $(this).addClass('cur').siblings().removeClass('cur');
                    $('.tab-main>div').hide();
                    $('#tab-ZXJS').show();
                } else {
                    layer.alert('请先排完走班课');
                }
            } else {
                layer.alert('请先锁定课表结构设置');
            }
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
        })

        //=================================================走班课排课====================================================
        $('body').on('click', '#zbClearTimetable', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                layer.confirm('确定要清空所有已排课程？', function (index) {
                    clearTimetable(1, '');
                    getArrangingZBCourseList();
                    getArrangedZBTimetable();

                    layer.close(index);
                });
            }
        });

        $('body').on('mouseover, mouseenter', '.used-zouban-point, .used-event-point', function () {
            $(this).find('.zouban-class-con').show();
        });
        $('body').on('mouseout, mouseleave', '.used-zouban-point, .used-event-point', function () {
            $(this).find('.zouban-class-con').hide();
        });

        var waitFlag = true;
        var courseId = '';
        $('body').on('click', '.arranging-zouban-before', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                if (waitFlag) {
                    $(this).removeClass('arranging-zouban-before').addClass('arranging-zouban-after');
                    waitFlag = false;
                    $(this).find('span').each(function () {
                        courseId += $(this).attr('courseId') + ',';
                    });
                    getZBAvailablePoint(courseId);
                } else {
                    courseId = '';
                    return;
                }
            }
        });
        $('body').on('click', '.arranging-zouban-after', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                if (!waitFlag) {
                    $(this).removeClass('arranging-zouban-after').addClass('arranging-zouban-before');
                    $('.point-zouban').removeClass('available-zouban-point');
                    waitFlag = true;
                    courseId = '';
                } else {
                    courseId = '';
                    return;
                }
            }
        });
        $('body').on('click', '.available-zouban-point', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                var x = $(this).attr('x');
                var y = $(this).attr('y');
                addZBCourse(courseId, x, y);
                courseId = '';
                waitFlag = true;
            }
        });

        $('body').on('click', '.point-zouban.used-event-point.unused-point, .point-zouban.used-zouban-point.used-event-point', function () {
            if ($(this).hasClass('available-zouban-point')) {
                return;
            } else {
                if (TIMETABLE_STATE) {
                    layer.alert('课表已发布！');
                } else {
                    if (!waitFlag) {
                        var x = $(this).attr('x');
                        var y = $(this).attr('y');
                        layer.confirm('此时间有事务冲突，确定要排课吗？', function (index) {
                            addZBCourse(courseId, x, y);
                            courseId = '';
                            waitFlag = true;
                            layer.close(index);
                        });
                    }
                }
            }
        });

        $('body').on('click', '.used-zouban-point', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                if ($(this).hasClass('available-zouban-point')) {
                    return;
                } else {
                    if (waitFlag) {
                        var x = $(this).attr('x');
                        var y = $(this).attr('y');
                        removeZBCourse(x, y);
                    } else {
                        return;
                    }
                }
            }
        });
        $('body').on('change', '#groupSelectCtx', function () {
            getArrangingZBCourseList();
            getArrangedZBTimetable();
            resetWaitFlag();
        });


        //===============================================分组走班排课=====================================================
        $('body').on('click', '#fzzbClearTimetable', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                layer.confirm('确定要清空所有已排课程？', function (index) {
                    clearFZZBCoures();
                    layer.close(index);
                });
            }
        });

        $('body').on('mouseover, mouseenter', '.used-fzzb-point, .used-event-point', function () {
            $(this).find('.fzzb-class-con').show();
        });
        $('body').on('mouseout, mouseleave', '.used-fzzb-point, .used-event-point', function () {
            $(this).find('.fzzb-class-con').hide();
        });

        var fzzbWaitFlag = true;
        var fzzbCourseId = '';
        $('body').on('click', '.arranging-fzzb-before', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                if (fzzbWaitFlag) {
                    $(this).removeClass('arranging-fzzb-before').addClass('arranging-fzzb-after');
                    fzzbWaitFlag = false;
                    $(this).find('span').each(function () {
                        fzzbCourseId += $(this).attr('courseId') + ',';
                    });
                    getAvailablePointForFZZB(fzzbCourseId);
                } else {
                    fzzbCourseId = '';
                    return;
                }
            }
        });
        $('body').on('click', '.arranging-fzzb-after', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                if (!fzzbWaitFlag) {
                    $(this).removeClass('arranging-fzzb-after').addClass('arranging-fzzb-before');
                    $('.point-fzzb').removeClass('available-fzzb-point');
                    fzzbWaitFlag = true;
                    fzzbCourseId = '';
                } else {
                    fzzbCourseId = '';
                    return;
                }
            }
        });
        $('body').on('click', '.available-fzzb-point', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                var x = $(this).attr('x');
                var y = $(this).attr('y');
                addFZZBCourse(fzzbCourseId, x, y);
                fzzbCourseId = '';
                fzzbWaitFlag = true;
            }
        });

        $('body').on('click', '.point-fzzb.used-event-point.unused-point', function () {
            if ($(this).hasClass('available-fzzb-point')) {
                return;
            } else {
                if (TIMETABLE_STATE) {
                    layer.alert('课表已发布！');
                } else {
                    if (!fzzbWaitFlag) {
                        var x = $(this).attr('x');
                        var y = $(this).attr('y');
                        layer.confirm('此时间有事务冲突，确定要排课吗？', function (index) {
                            addFZZBCourse(fzzbCourseId, x, y);
                            fzzbCourseId = '';
                            fzzbWaitFlag = true;
                            layer.close(index);
                        });
                    }
                }
            }
        });

        $('body').on('click', '.used-fzzb-point', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                if ($(this).hasClass('available-fzzb-point')) {
                    return;
                } else {
                    if (fzzbWaitFlag) {
                        var x = $(this).attr('x');
                        var y = $(this).attr('y');
                        removeFZZBCourse(x, y);
                    } else {
                        return;
                    }
                }
            }
        });


        //===============================================体育课排课=======================================================
        $('body').on('click', '#peClearTimetable', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                layer.confirm('确定要清空所有已排课程？', function (index) {
                    clearTimetable(2, '');
                    getArrangingPECourse();
                    getArrangedPECourse();
                    layer.close(index);
                });
            }
        });
        $('body').on('click', '#autoArrangePE', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                layer.confirm('自动排课将清空已有的非走班课和体育课，确定要自动排课？', function (index) {
                    autoArrangePE();
                    layer.close(index);
                });
            }
        });
        $('body').on('mouseover, mouseenter', '.used-pe-point, .used-event-point', function () {
            $(this).find('.pe-class-con').show();
        });
        $('body').on('mouseout, mouseleave', '.used-pe-point, .used-event-point', function () {
            $(this).find('.pe-class-con').hide();
        });

        var peWaitFlag = true;
        var peCourseId = '';
        $('body').on('click', '.arranging-pe-before', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                if (peWaitFlag) {
                    $(this).removeClass('arranging-pe-before').addClass('arranging-pe-after');
                    peWaitFlag = false;
                    $(this).find('span').each(function () {
                        peCourseId += $(this).attr('courseId') + ',';
                    });
                    getAvailablePointForPE(peCourseId);
                } else {
                    peCourseId = '';
                    return;
                }
            }
        });
        $('body').on('click', '.arranging-pe-after', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                if (!peWaitFlag) {
                    $(this).removeClass('arranging-pe-after').addClass('arranging-pe-before');
                    $('.point-pe').removeClass('available-pe-point');
                    peWaitFlag = true;
                    peCourseId = '';
                } else {
                    peCourseId = '';
                    return;
                }
            }
        });
        $('body').on('click', '.available-pe-point', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                var x = $(this).attr('x');
                var y = $(this).attr('y');
                addPECourse(peCourseId, x, y);
                peCourseId = '';
                peWaitFlag = true;
            }
        });

        $('body').on('click', '.point-pe.used-event-point.unused-point', function () {
            if ($(this).hasClass('available-pe-point')) {
                return;
            } else {
                if (TIMETABLE_STATE) {
                    layer.alert('课表已发布！');
                } else {
                    if (!peWaitFlag) {
                        var x = $(this).attr('x');
                        var y = $(this).attr('y');
                        layer.confirm('此时间有事务冲突，确定要排课吗？', function (index) {
                            addPECourse(peCourseId, x, y);
                            peCourseId = '';
                            peWaitFlag = true;
                            layer.close(index);
                        });
                    }
                }
            }
        });

        $('body').on('click', '.used-pe-point', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                if (peWaitFlag) {
                    if ($(this).hasClass('available-pe-point')) {
                        return;
                    } else {
                        var x = $(this).attr('x');
                        var y = $(this).attr('y');
                        removePECourse(x, y);
                    }
                } else {
                    return;
                }
            }
        });

        //========================================================非走班排课==============================================

        $('body').on('click', '#clearFZBCourse', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                layer.confirm('确定要清空所有已排课程？', function (index) {
                    clearTimetable(3, $('#classListCtx').val());
                    getArrangingFZBCourse();
                    getArrangedFZBCourse();

                    layer.close(index);
                });
            }
        });
        $('body').on('click', '#autoArrangeFZBCourse', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                autoArrangeFZBCourse();
            }
        });
        $('body').on('change', '#classListCtx', function () {
            getArrangingFZBCourse();
            getArrangedFZBCourse();
            resetWaitFlag();
        });

        $('body').on('mouseover, mouseenter', '.unused-point, .fzb-zb-point, .fzb-pe-point, .used-fzb-point', function () {
            $(this).find('.fzb-class-con').show();
        });
        $('body').on('mouseout, mouseleave', '.unused-point, .fzb-zb-point, .fzb-pe-point, .used-fzb-point', function () {
            $(this).find('.fzb-class-con').hide();
        });

        var fzbWaitFlag = true;
        var fzbCourseId = '';
        $('body').on('click', '.arranging-fzb-before', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                if (fzbWaitFlag) {
                    $(this).removeClass('arranging-fzb-before').addClass('arranging-fzb-after');
                    fzbWaitFlag = false;
                    $(this).find('span').each(function () {
                        fzbCourseId += $(this).attr('courseId') + ',';
                    });
                    getFZBAvailablePoint(fzbCourseId);
                } else {
                    fzbCourseId = '';
                    return;
                }
            }
        });
        $('body').on('click', '.arranging-fzb-after', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                if (!fzbWaitFlag) {
                    $(this).removeClass('arranging-fzb-after').addClass('arranging-fzb-before');
                    $('.point-fzb').removeClass('available-fzb-point');
                    fzbWaitFlag = true;
                    fzbCourseId = '';
                } else {
                    fzbCourseId = '';
                    return;
                }
            }
        });
        $('body').on('click', '.available-fzb-point', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                var x = $(this).attr('x');
                var y = $(this).attr('y');
                addFZBCourse(fzbCourseId, x, y);
                fzbCourseId = '';
                fzbWaitFlag = true;
            }
        });
        $('body').on('click', '.point-fzb.unused-point.used-event-point', function () {
            if ($(this).hasClass('available-fzb-point')) {
                return;
            } else {
                if (TIMETABLE_STATE) {
                    layer.alert('课表已发布！');
                } else {
                    if (!fzbWaitFlag) {
                        var x = $(this).attr('x');
                        var y = $(this).attr('y');
                        layer.confirm('此时间有事务冲突，确定要排课吗？', function (index) {
                            addFZBCourse(fzbCourseId, x, y);
                            fzbCourseId = '';
                            fzbWaitFlag = true;
                            layer.close(index);
                        });
                    }
                }
            }
        });

        $('body').on('click', '.used-fzb-point', function () {
            if (TIMETABLE_STATE) {
                layer.alert('课表已发布！');
            } else {
                if (fzbWaitFlag) {
                    var x = $(this).attr('x');
                    var y = $(this).attr('y');
                    removeFZBCourse(x, y);
                } else {
                    return;
                }
            }
        });

        $('body').on('click', '#publishTimetable', function () {
            if ($(this).text() == '发布课表') {
                layer.confirm('请确认教学周是否已设置', function (index) {
                    publishTimetable();
                    layer.close(index);
                });
            } else {
                unPublishTimetable();
            }
        });

        if ($('body').attr('mode') != 0) {
            $('#ZBK').trigger('click');
        } else {
            $('#FZZBK').trigger('click');
        }
    });


    /**
     * 检查课表是否发布
     */
    function isPublic() {
        var url = '/zouban/paike/isPublic.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;

        var publish = false;
        Common.getData(url, param, function (resp) {
            if (resp.state == '1') {
                publish = true;
            }
        });
        return publish;
    }

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
        10: '第十节'
    };


    /**
     * 获取课表配置锁定状态
     * @returns {boolean}
     */
    function getLockState() {
        var url = '/zouban/timetableConf/isLocked.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;

        var lock = true;

        Common.getData(url, param, function (resp) {
            if (resp.lock == 'UNLOCK') {
                lock = false;
            }
        });

        return lock;
    }


    /**
     * 生成课表数据
     * @param timetableConf
     * @param courseItemList
     * @returns {{days: Array, sections: Array, classTime: (*|data.conf.classTime|teacherTable.conf.classTime|classTable.conf.classTime|tableData.classTime|courseConfDTO.classTime), courseItemList: Array}}
     */
    function generateData(timetableConf, courseItemList) {
        var days = timetableConf.days;
        var classCount = timetableConf.classCount;
        var classTime = timetableConf.classTime;
        var eventList = timetableConf.eventList;
        var classEventList = timetableConf.classEventList;


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


        //班级事务列表
        var classEvents = [];
        for (var i = 0; i < classCount; i++) {
            classEvents[i] = [];
            for (var j = 0; j < days.length; j++) {
                classEvents[i][j] = '';
            }
        }
        for (var i = 0; i < classEventList.length; i++) {
            var classEvent = classEventList[i];
            if (classEvent.x <= days.length && classEvent.y <= classCount) {
                classEvents[classEvent.y - 1][classEvent.x - 1] = '无课';
            }
        }


        //事务列表
        var events = [];
        for (var i = 0; i < classCount; i++) {
            events[i] = [];
            for (var j = 0; j < days.length; j++) {
                events[i][j] = [];
            }
        }
        for (var i = 0; i < eventList.length; i++) {
            var event = eventList[i];
            var pointList = event.pointList;
            for (var j = 0; j < pointList.length; j++) {
                var point = pointList[j];
                if (point.x <= days.length && point.y <= classCount) {
                    events[point.y - 1][point.x - 1].push({
                        id: event.id,
                        name: event.name
                    });
                }
            }
        }

        //课表item列表
        var itemList = [];
        for (var i = 0; i < classCount; i++) {
            itemList[i] = [];
            for (var j = 0; j < days.length; j++) {
                itemList[i][j] = {
                    courseList: [],
                    type: 0,
                    classEvent: classEvents[i][j],
                    eventList: events[i][j]
                };
            }
        }
        for (var i = 0; i < courseItemList.length; i++) {
            var item = courseItemList[i];
            var courseList = itemList[item.yIndex - 1][item.xIndex - 1].courseList;
            if (courseList != null && courseList.length > 0) {
                itemList[item.yIndex - 1][item.xIndex - 1].courseList = courseList.concat(item.courseIdList);
            } else {
                itemList[item.yIndex - 1][item.xIndex - 1].courseList = item.courseIdList;
            }
            itemList[item.yIndex - 1][item.xIndex - 1].type = item.type;
        }


        var data = {
            days: weekday,
            sections: sections,
            classTime: classTime,
            courseItemList: itemList
        };

        return data;
    }

    /**
     * 获取分段列表
     */
    function getGroupList() {
        var url = '/zouban/fenban/findFenDuan.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;

        Common.getData(url, param, function (resp) {
            template('#groupSelectTmpl', '#groupSelectCtx', resp.fenDuanList);
        });
    }

    /**
     * 获取走班课未排课程
     */
    function getArrangingZBCourseList() {
        var url = '/zouban/paike/getArrangingZBCourse.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.groupId = $('#groupSelectCtx').val();

        Common.getData(url, param, function (resp) {
            if (resp.code == "200") {
                template('#arrangingZBCourseTmpl', '#arrangingZBCourseCtx', resp.message);
            } else {
                layer.alert(resp.message, function () {
                    window.location.href = '/zouban/baseConfig.do?term=' + TERM + '&gradeId=' + GRADEID;
                });
            }
        });
    }

    /**
     * 获取走班课已排课表
     */
    function getArrangedZBTimetable() {
        var url = '/zouban/paike/getArrangedZBCourse.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.groupId = $('#groupSelectCtx').val();

        Common.getData(url, param, function (resp) {
            var timetableConf = resp.timetableConf;
            var courseItemList = resp.courseItemList;
            var data = generateData(timetableConf, courseItemList);

            template('#zbTimetableTmpl', '#zbTimetableCtx', data);
        });
    }

    /**
     * 获取走班课可用时间点
     * @param courseId
     */
    function getZBAvailablePoint(courseIdStr) {
        var url = '/zouban/paike/getAvailablePointForZB.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.courseIdStr = courseIdStr;
        param.groupId = $('#groupSelectCtx').val();

        Common.getData(url, param, function (resp) {
            if (resp.code == '200') {
                var pointList = resp.message;
                if (pointList.length > 0) {
                    for (var i = 0; i < pointList.length; i++) {
                        $('.point-zouban[x=' + pointList[i].x + '][y=' + pointList[i].y + ']').addClass('available-zouban-point');
                    }
                }
            } else {
                layer.alert(resp.message);
            }
        });
    }

    /**
     * 添加走班课
     *
     * @param courseIdStr
     */
    function addZBCourse(courseIdStr, x, y) {
        var url = '/zouban/paike/addZBCourse.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.courseIdStr = courseIdStr;
        param.groupId = $('#groupSelectCtx').val();
        param.x = x;
        param.y = y;

        Common.getPostData(url, param, function (resp) {
            if (resp.code == '200') {
                getArrangingZBCourseList();
                getArrangedZBTimetable();
            } else {
                layer.alert('添加失败');
            }
        });
    }

    /**
     * 移除某个位置的走班课
     *
     * @param x
     * @param y
     */
    function removeZBCourse(x, y) {
        var url = '/zouban/paike/removeZBCourse.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.groupId = $('#groupSelectCtx').val();
        param.x = x;
        param.y = y;

        Common.getPostData(url, param, function (resp) {
            if (resp.code == '200') {
                getArrangingZBCourseList();
                getArrangedZBTimetable();
            } else {
                layer.alert('删除失败');
            }
        });
    }

    /**
     * 检查走班课是否排完
     * @returns {boolean}
     */
    function getZBLockState() {
        var url = '/zouban/paike/getZBLockState.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;


        var lock = true;
        Common.getData(url, param, function (resp) {
            if (resp.code == '200') {
                if (resp.message == 'UNLOCK') {
                    lock = false;
                }
            } else {
                layer.alert('error');
            }
        });
        return lock;
    }


    /**
     * 清空已排课程()
     *
     * @param type{1:走班课, 2: 体育课, 3:非走班课}
     */
    function clearTimetable(type, classId) {
        var url = '/zouban/paike/clearTimetableCourse.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.type = type;
        param.classId = classId;

        Common.getPostData(url, param, function (resp) {
            if (resp.code == '200') {

            } else {
                layer.alert('清空失败');
            }
        });
    }


    /**
     * 获取分组走班未排课时
     */
    function getArrangingFZZBCourse() {
        var url = '/zouban/paike/getArrangingFZZBCourse.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;

        Common.getData(url, param, function (resp) {
            if (resp.code == "200") {
                template('#arrangingFZZBCourseTmpl', '#arrangingFZZBCourseCtx', resp.message);
            } else {
                layer.alert(resp.message, function () {
                    window.location.href = '/zouban/baseConfig.do?term=' + TERM + '&gradeId=' + GRADEID;
                });
            }
        });
    }


    function getArrangedFZZBCourse() {
        var url = '/zouban/paike/getArrangedFZZBCourse.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;

        Common.getData(url, param, function (resp) {
            var timetableConf = resp.timetableConf;
            var courseItemList = resp.courseItemList;
            var data = generateData(timetableConf, courseItemList);

            template('#fzzbTimetableTmpl', '#fzzbTimetableCtx', data);
        });
    }


    /**
     * 分组走班可用时间点
     */
    function getAvailablePointForFZZB(courseIdStr) {
        var url = '/zouban/paike/getAvailablePointForFZZB.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.courseIdStr = courseIdStr;

        Common.getData(url, param, function (resp) {
            if (resp.code == '200') {
                var pointList = resp.message;
                if (pointList.length > 0) {
                    for (var i = 0; i < pointList.length; i++) {
                        $('.point-fzzb[x=' + pointList[i].x + '][y=' + pointList[i].y + ']').addClass('available-fzzb-point');
                    }
                }
            } else {
                layer.alert(resp.message);
            }
        });
    }


    /**
     * 添加分组走班课
     *
     * @param courseIdStr
     * @param x
     * @param y
     */
    function addFZZBCourse(courseIdStr, x, y) {
        var url = '/zouban/paike/addFZZBCourse.do';
        var param = {};
        param.term = TERM;
        param.courseIdStr = courseIdStr;
        param.x = x;
        param.y = y;

        Common.getPostData(url, param, function (resp) {
            if (resp.code == '200') {
                getArrangingFZZBCourse();
                getArrangedFZZBCourse();
            } else {
                layer.alert('添加失败,' + resp.message);
            }
        });
    }

    /**
     * 移除某个位置的分组走班课
     *
     * @param x
     * @param y
     */
    function removeFZZBCourse(x, y) {
        var url = '/zouban/paike/removeFZZBCourse.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.x = x;
        param.y = y;

        Common.getPostData(url, param, function (resp) {
            if (resp.code == '200') {
                getArrangingFZZBCourse();
                getArrangedFZZBCourse();
            } else {
                layer.alert('删除失败');
            }
        });
    }


    /**
     * 清空分组走班课
     */
    function clearFZZBCoures() {
        var url = '/zouban/paike/clearFZZBCourse.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;

        Common.getPostData(url, param, function (resp) {
            if (resp.code == '200') {
                getArrangingFZZBCourse();
                getArrangedFZZBCourse();
            } else {
                layer.alert('清空失败');
                console.log(resp.message);
            }
        });
    }


    /**
     * 获取未排的体育课
     */
    function getArrangingPECourse() {
        var url = '/zouban/paike/getArrangingPECourse.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;

        Common.getData(url, param, function (resp) {
            template('#arrangingPECourseTmpl', '#arrangingPECourseCtx', resp.arrangingPECourseList);
        });
    }

    /**
     * 获取体育课已排课课表
     */
    function getArrangedPECourse() {
        var url = '/zouban/paike/getArrangedPECourse.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;

        Common.getData(url, param, function (resp) {
            var timetableConf = resp.timetableConf;
            var courseItemList = resp.courseItemList;
            var data = generateData(timetableConf, courseItemList);
            template('#peTimetableTmpl', '#peTimetableCtx', data);
        });
    }

    /**
     * 体育课自动排课
     */
    function autoArrangePE() {
        var url = '/zouban/paike/autoArrangePECourse.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;

        var index = layer.load(2, {shade: [0.3, '#000']});

        Common.postDataAsync(url, param, function (resp) {
            layer.close(index);

            if (resp.code == '500') {
                layer.alert('自动排课失败');
            }
            getArrangingPECourse();
            getArrangedPECourse();
        });
    }

    /**
     * 获取体育课可用时间点
     *
     * @param courseIdStr
     */
    function getAvailablePointForPE(courseIdStr) {
        var url = '/zouban/paike/getAvailablePointForPE.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.courseIdStr = courseIdStr;

        Common.getData(url, param, function (resp) {
            if (resp.code == '200') {
                var pointList = resp.message;
                if (pointList.length > 0) {
                    for (var i = 0; i < pointList.length; i++) {
                        $('.point-pe[x=' + pointList[i].x + '][y=' + pointList[i].y + ']').addClass('available-pe-point');
                    }
                }
            } else {
                layer.alert(resp.message);
            }
        });
    }

    /**
     * 添加体育课
     * @param courseIdStr
     */
    function addPECourse(courseIdStr, x, y) {
        var url = '/zouban/paike/addPECourse.do';
        var param = {};
        param.term = TERM;
        param.courseIdStr = courseIdStr;
        param.x = x;
        param.y = y;

        Common.getPostData(url, param, function (resp) {
            if (resp.code == '200') {
                getArrangingPECourse();
                getArrangedPECourse();
            } else {
                layer.alert('添加失败,' + resp.message);
            }
        });
    }

    /**
     * 移除某个位置的体育课
     *
     * @param x
     * @param y
     */
    function removePECourse(x, y) {
        var url = '/zouban/paike/removePECourse.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.x = x;
        param.y = y;

        Common.getPostData(url, param, function (resp) {
            if (resp.code == '200') {
                getArrangingPECourse();
                getArrangedPECourse();
            } else {
                layer.alert('删除失败');
            }
        });
    }

    /**
     * 检查体育课是否排完
     * @returns {boolean}
     */
    function checkPEFinished() {
        var url = '/zouban/paike/getPEState.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;

        var lock = true;
        Common.getData(url, param, function (resp) {
            if (resp.code == '200') {
                if (resp.message == 'UNLOCK') {
                    lock = false;
                }
            } else {
                layer.alert('error');
            }
        });
        return lock;
    }


    /**
     * 获取班级列表
     */
    function getClassList() {
        var url = '/zouban/common/getClassList.do';
        var param = {};
        param.gradeId = GRADEID;

        Common.getData(url, param, function (resp) {
            template('#classListTmpl', '#classListCtx', resp.classList);
        });
    }

    /**
     * 获取非走班未排课程
     */
    function getArrangingFZBCourse() {
        var url = '/zouban/paike/getArrangingFZBCourse.do';
        var param = {};
        param.term = TERM;
        param.classId = $('#classListCtx').val();

        Common.getData(url, param, function (resp) {
            template('#arrangingFZBCourseTmpl', '#arrangingFZBCourseCtx', resp.arrangingFZBCourseList);
        });
    }

    /**
     * 获取非走班课表
     */
    function getArrangedFZBCourse() {
        var url = '/zouban/paike/getArrangedFZBCourse.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.classId = $('#classListCtx').val();

        Common.getData(url, param, function (resp) {
            var timetableConf = resp.timetableConf;
            var courseItemList = resp.courseItemList;
            var data = generateData(timetableConf, courseItemList);

            template('#fzbTimetableTmpl', '#fzbTimetableCtx', data);
        });
    }

    /**
     * 获取非走班课程可用时间点
     *
     * @param courseId
     */
    function getFZBAvailablePoint(courseId) {
        var url = '/zouban/paike/getAvailablePointForFZB.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.classId = $('#classListCtx').val();
        param.courseId = courseId;

        Common.getData(url, param, function (resp) {
            if (resp.code == '200') {
                var pointList = resp.message;
                if (pointList.length > 0) {
                    for (var i = 0; i < pointList.length; i++) {
                        $('.point-fzb[x=' + pointList[i].x + '][y=' + pointList[i].y + ']').addClass('available-fzb-point');
                    }
                }
            } else {
                layer.alert(resp.message);
            }
        });
    }

    /**
     * 添加非走班课
     *
     * @param courseIdStr
     * @param x
     * @param y
     */
    function addFZBCourse(courseIdStr, x, y) {
        var url = '/zouban/paike/addFZBCourse.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.classId = $('#classListCtx').val();
        param.courseId = courseIdStr;
        param.x = x;
        param.y = y;

        Common.getPostData(url, param, function (resp) {
            if (resp.code == '200') {
                getArrangingFZBCourse();
                getArrangedFZBCourse();
            } else {
                layer.alert('添加失败,' + resp.message);
            }
        });
    }

    /**
     * 移除某个位置的非走班课
     *
     * @param x
     * @param y
     */
    function removeFZBCourse(x, y) {
        var url = '/zouban/paike/removeFZBCourse.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.classId = $('#classListCtx').val();
        param.x = x;
        param.y = y;

        Common.getPostData(url, param, function (resp) {
            if (resp.code == '200') {
                getArrangingFZBCourse();
                getArrangedFZBCourse();
            } else {
                layer.alert('删除失败');
            }
        });
    }

    /**
     * 非走班自动排课
     */
    function autoArrangeFZBCourse() {
        var url = '/zouban/paike/autoArrangeFZBCourse.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;
        param.classId = $('#classListCtx').val();

        var index = layer.load(2, {shade: [0.3, '#000']});

        Common.postDataAsync(url, param, function (resp) {
            getArrangingFZBCourse();
            getArrangedFZBCourse();
            layer.close(index);
            if (resp.code != '200') {
                layer.alert('自动排课未完成：' + resp.message);
                console.log(resp.message);
            }
        })
    }


    /**
     * 发布课表
     */
    function publishTimetable() {
        var url = '/zouban/paike/publishTimetable.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;

        var index = layer.load(2, {shade: [0.3, '#000']});

        Common.postDataAsync(url, param, function (resp) {
            if (resp.code == '200') {
                TIMETABLE_STATE = true;
                layer.close(index);
                layer.alert('课表发布成功');
                $('#publishTimetable').text('取消发布');
                $('#tips').hide();
            } else {
                layer.close(index);
                layer.alert(resp.message);
            }
        });
    }

    /**
     * 取消发布
     */
    function unPublishTimetable() {
        var url = '/zouban/paike/unPublishTimetable.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;

        Common.getPostData(url, param, function (resp) {
            if (resp.code == '200') {
                TIMETABLE_STATE = false;
                $('#publishTimetable').text('发布课表');
                $('#tips').show();
            } else {
                layer.alert('取消失败');
            }
        });
    }

    /**
     * 获取冲突事务列表
     */
    function getEventConflictList() {
        var url = '/zouban/paike/getConflictList.do';
        var param = {};
        param.term = TERM;
        param.gradeId = GRADEID;

        Common.getDataAsync(url, param, function (resp) {
            if (resp.code == '200') {
                console.log(resp.message);
                template('#conflictListTmpl', '#conflictListCtx', resp.message);
            }
        });
    }


    module.exports = paike;
});