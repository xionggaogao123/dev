/**
 * Created by fl on 2016/7/14.
 * 7.27
 */

define(function (require, exports, module) {
    var Common = require('common');
    require('layer');
    var CONFIG = {
        subList: {},
        subjectConfDTO: {}
    };
    var BASEDATA = base;//变量base在subjectConfig.jsp底部声明

    var CLASSTYPE = {
        ZOUBAN: 1,
        FEIZOUBAN: 2,
        GROUPZOUBAN: 7,
        ODDEVEN: 8
    }
    var $POPUP_ZB = $(".popup-XKGL-ZB");
    var $POPUP_FZB = $(".popup-XKGL");
    var $POPUP_TYK = $('.popup-TYK');
    var $POPUP_FZZB = $('.popup-addFZZB');
    var $POPUP_DSZK = $('.DSZ-XKGL');
    var $POPUP_DSZKT = $('.DSZ-SZ');
    var $POPUP_FZZBT = $('.FZZB-SZ');
    var $POPUP_FZZBALLOC = $('.popup-FZZB');

    var RELEASE = false;


    $(function () {



        isRelease();

        //返回首页
        $('body').on('click', '.back', function () {
            window.location.href = "/zouban/baseConfig.do?term=" + BASEDATA.term + "&gradeId=" + BASEDATA.gradeId;
        });

        //走班学科配置列表
        var type = CLASSTYPE.ZOUBAN;
        var mode = $('body').attr('mode');
        if (mode == 0) {
            type = CLASSTYPE.FEIZOUBAN;
        }
        getSubjectConfig(type);

        //保存走班科目配置
        $('.XKGL-ZB-TJ').click(function () {
            postSubjectConfig($(this).parent().parent(), CLASSTYPE.ZOUBAN);
            $('#zbTips').hide();
        })
        //编辑走班科目配置
        $('body').on('click', '#ZB-subjects .edit', function () {
            $('#zbTips').show();
            editZBSubjectConfig($(this).parent().parent())
        })
        //编辑分组走班学科配置
        $('body').on('click', '#FZZB-SUBJECT .edit', function () {
            $('#fzzbSubject').prop('disabled', true);
            $('#classesWrap :input').prop('disabled', true);
            $('#fenban').prop('disabled', true);
            editFZZBSubjectConfig($(this).parent().parent());
        })
        //编辑分组走班老师设置
        $('body').on('click', '#FZZBTeacherList .edit', function () {
            $('.bg').show();
            $POPUP_FZZBT.show();
            editFZZBTeacherList($(this).parent().parent());
        })
        //分组走板行政班列表
        $('body').on('click', '#FZZBTeacherList .stu-allot', function () {
            allocFZZBTeacherList($(this).parent().parent());
        })

        //删除分组走班教学班
        $('body').on('click', '.del-fzzb', function() {
            removeFZZBCourse($(this).attr('courseId'),$(this).attr('groupId'));
        });

        //编辑单双周课学科配置
        $('body').on('click', '#DSZK-SUBJECT .edit', function () {
            editDSZKSubjectConfig($(this).parent().parent());
        })
        //编辑单双周课老师设置
        $('body').on('click', '#zoubanOddEven .edit', function () {
            var ele = $(this).parent().parent();
            layer.confirm('修改老师配置之后，已排课时将清除，确定要进行该操作吗？', function (index) {
                $(".bg").show();
                $(".DSZ-SZ").show();
                editDSZKTeacher(ele);
                layer.close(index);
            });
        })
        //删除单双周课老师设置
        $('body').on('click', '#zoubanOddEven .del', function () {
            deleteDSZKCourse($(this).parent().parent());
        })
        //保存单双周课老师设置
        $('body').on('click', '#DSZKTJ', function () {
            addDSZKTJ();
        })

        //删除单双周课学科配置
        $('body').on('click', '#DSZK-SUBJECT .del', function () {
            deleteDSZKSubjectConfig($(this).parent().parent());
        })
        //删除分组走班学科配置
        $('body').on('click', '#FZZB-SUBJECT .del', function () {
            deleteFZZBSubjectConfig($(this).parent().parent());
        })
        //删除走班科目配置
        $('body').on('click', '#ZB-subjects .del', function () {
            if(!RELEASE) {
                deleteSubjectConfig($(this).parent().parent(), CLASSTYPE.ZOUBAN);
            } else {
                layer.alert('已经开始选课，不能删除！如果确实要删除，请取消选课公布。');
            }
        })

        //保存非走班科目配置
        $('#addFzb').click(function () {
            postSubjectConfig($(this).parent().parent(), CLASSTYPE.FEIZOUBAN);
        })


        //编辑非走班科目配置
        $('body').on('click', '#FZB-subjects .edit', function () {
            $('#fzbTips').show();
            editFZBSubjectConfig($(this).parent().parent())
        })
        //删除非走班科目配置
        $('body').on('click', '#FZB-subjects .del', function () {
            deleteSubjectConfig($(this).parent().parent(), CLASSTYPE.FEIZOUBAN)
        })
        //保存体育课
        $('.TYK-TJ').click(function () {
            if (!BASEDATA.isPEEdit) {
                addPECourse();
            } else {
                updatePECourse();
            }
        })
        //编辑体育班科目配置
        $('body').on('click', '.PECourses .edit', function () {
            //禁用修改老师，行政班
            $('.popup-TYK-IN').prop('disabled', true);
            editPECourse($(this).parents('tr'))
        })
        //删除体育班科目配置
        $('body').on('click', '.PECourses .del', function () {
            deletePECourse($(this).parents('tr'))
        })
        //非走班老师设置
        $(".teacher-set").click(function () {
            $("#tab-FZBK-II,#tab-FZBK-I").hide();
            $("#tab-FZBK-I").show();
            FZBCourseList()
        })
        //单双周课老师设置
        $(".teacher-set-I").click(function () {
            $("#tab-FZBK-1,#tab-FZBK-2").hide();
            $("#tab-FZBK-2").show();
            getDSZKZoubanConfig();
        })
        //分组走班课老师设置
        $(".teacher-set-II").click(function () {
            $("#tab-FZZB-I,#tab-FZZB-II").hide();
            $("#tab-FZZB-II").show();
            getFZZBTeacherList();
        })
        //切换学科查看非走班课
        $('#subjectList').change(function () {
            FZBCourseList()
        })
        //跳出编辑非走班课老师弹窗
        $('body').on('click', '#FZBCourses .edit', function () {
            $(".bg").show();
            $(".popup-SZ").show();
            getTeachersBySubjectId($(this));
        })
        $('body').on('click', '#FZBCourses .del', function () {
            deleteFZBKTeacher($(this));
        })
        //新增单双周课
        $('body').on('click', '#addDSZK', function () {
            addDSZK();
        })

        //跳出单双周老师设置弹窗
        $('body').on('click', '#tab-FZBK-2 .edit', function () {

            getTeachersBySubjectId($(this));
        })
        //跳出分组走班分配学生弹窗
        $('body').on('click', '#tab-FZZB-II .stu-allot', function () {
            $(".bg").show();
            $(".allot-alert").show();
            getTeachersBySubjectId($(this));
        })
        //跳出分组走班学科管理编辑弹窗
        $('body').on('click', '#tab-XKGl-1 .edit', function () {
            $(".bg").show();
            $(".FZZB-edit").show();
            CONFIG.subjectConfDTO.subjectConfId=$(this).parent().parent().attr('subConfigId');
            //getTeachersBySubjectId($(this));
        })
        //跳出分组走板学科管理
        $('body').on('click', '.FZZB-II-XZ', function () {
            $('#fzzbSubject').prop('disabled', false);
            $('#classesWrap :input').prop('disabled', false);
            $('#classesWrap :input').prop('checked', false);
            $('#fenban').prop('disabled', false);
            CONFIG.subjectConfDTO.subjectConfId="";
            $('#fzzbSubject').prop('disabled', false);
            $(".bg").show();
            $(".FZZB-edit").show();
        })
        //保存分组走班配置
        $('body').on('click', '#addFzzb', function () {
            addFZZB();
        })
        getFZZBSubjectConfig();
        //保存设置非走班课老师
        $('#SZTJ').click(function () {
            setTeacher();
        })
        //一键分非走班老师
        $('#FZBKSZ').click(function () {
            layer.confirm('如果已经排课，将清空所有已排课程，确定要设置吗？', function(index) {
                autoSetTeacherAndRoom();
                layer.close(index);
            });
        });
        //一键设置分组走班老师和教室
        $('#FZZBSZl').click(function () {
            layer.confirm('如果已经排课，将清空所有已排课程，确定要设置吗？', function(index) {
                autoSetFZZBTeacherAndRoom();
                layer.close(index);
            });
        })
        //一键设置单双周老师
        $('#DSZKSZ').click(function () {
            layer.confirm('如果已经排课，将清空所有已排课程，确定要设置吗？', function(index) {
                autoSetDSZKTeacher();
                layer.close(index);
            });

        })

        //
        $('body').on('click', '#SZCONFIRM', function () {
            editFZZBItem();
        })

        $('#FZZBsubjectList').change(function () {
            BASEDATA.subjectId = $(this).val();
            getFZZBTeacherList();
        })
        BASEDATA.subjectId = "All";
        getFZZBTeacherList();
        getDSZKSubjectConfig();
        getDSZKZoubanConfig();

        $('#AllocClassInfo').change(function () {
            getClassStudentInfo($(this).val());
        })

        $('body').on('click', '.classInfo', function () {
            addTeachStudentInfo($(this));
        })

        $('body').on('click', '.teachInfo', function () {
            removeTeachStudentInfo($(this));
        })

    });



    function isRelease() {
        Common.postDataAsync("/zouban/subjectConfig/isRelease.do", {xuankeId: BASEDATA.xuankeId}, function (resp) {
            RELEASE = resp.message;
        });
    }

    function removeTeachStudentInfo(ele) {
        var param = {};
        param.studentId = ele.attr('stuId');
        param.oddEvenId = BASEDATA.zoubanCourseId;
        Common.getDataAsync("/zouban/subjectConfig/removeTeachStudent.do", param, function (resp) {
            if (resp.code == '500') {
            } else {
                getClassStudentInfo(BASEDATA.classId);
                getTeachStudentInfo(BASEDATA.zoubanCourseId);
            }
        });

    }

    function addTeachStudentInfo(ele) {
        var param = {};
        param.studentId = ele.attr('stuId');
        param.oddEvenId = BASEDATA.zoubanCourseId;
        Common.getDataAsync("/zouban/subjectConfig/addTeachStudent.do", param, function (resp) {
            if (resp.code == '500') {
            } else {
                getClassStudentInfo(BASEDATA.classId);
                getTeachStudentInfo(BASEDATA.zoubanCourseId);
            }
        });

    }

    function getClassStudentInfo(classId) {
        var requestParam = {};
        BASEDATA.classId = classId;
        requestParam.classId = classId;
        requestParam.oddEvenId = BASEDATA.zoubanCourseId;
        Common.getDataAsync("/zouban/subjectConfig/getClassStudentInfo.do", requestParam, function (resp) {
            Common.render({
                tmpl: '#ClassStudentInfoTmpl',
                data: resp.userDetailInfoDTOs,
                context: '#ClassStudentInfo',
                overwrite: 1
            });
        })
    }

    function getTeachStudentInfo(oddEvenId) {
        var requestParam = {};
        requestParam.oddEvenId = oddEvenId;
        Common.getDataAsync("/zouban/subjectConfig/getTeachStudentInfo.do", requestParam, function (resp) {
            Common.render({
                tmpl: '#TeachStudentInfoTmpl',
                data: resp.teachInfo,
                context: '#TeachStudentInfo',
                overwrite: 1
            });
        })
    }

    function allocFZZBTeacherList(ele) {
        var requestParam = {};
        BASEDATA.zoubanCourseId = ele.attr('zbCourseId');
        requestParam.oddEvenId = ele.attr('zbCourseId');
        Common.getDataAsync("/zouban/subjectConfig/getAllocClassInfo.do", requestParam, function (resp) {
            Common.render({
                tmpl: '#AllocClassInfoTmpl',
                data: resp.idNameDTOs,
                context: '#AllocClassInfo',
                overwrite: 1
            });

            $('#AllocClassInfo').find('option:eq(0)').trigger('change');
        })
        getTeachStudentInfo(BASEDATA.zoubanCourseId);
    }


    function editFZZBItem() {
        var data = [];
        var validateTeacher = "";
        var validateRoom = "";
        $('#FZZBSZ dd').each(function () {
            var ZoubanGroupDTO = {};
            ZoubanGroupDTO.className = $(this).find('.FzzbClassName').val();
            ZoubanGroupDTO.zoubanCourseId = $(this).find('.FzzbCourseId').val();
            ZoubanGroupDTO.teacherId = $(this).find('.tte').find('option:selected').val();
            if (validateTeacher == "" || validateTeacher == undefined) {
                validateTeacher = $(this).find('.tte').find('option:selected').val();
            } else {
                validateTeacher = validateTeacher + "," + $(this).find('.tte').find('option:selected').val();
            }
            ZoubanGroupDTO.teacherName = $(this).find('.tte').find("option:selected").text();
            ZoubanGroupDTO.classRoomId = $(this).find('.ttt').find('option:selected').val();
            if (validateRoom == "" || validateRoom == undefined) {
                validateRoom = $(this).find('.ttt').find('option:selected').val();
            } else {
                validateRoom = validateRoom + "," + $(this).find('.ttt').find('option:selected').val();
            }
            ZoubanGroupDTO.group = BASEDATA.group;
            ZoubanGroupDTO.term = BASEDATA.term;
            ZoubanGroupDTO.gradeId = BASEDATA.gradeId;
            data.push(ZoubanGroupDTO);
        });

        var str = validateTeacher.split(",");
        var temp = validateTeacher.split(",");
        for (var i in str) {
            for (var j in temp) {
                if (parseInt(j) >= parseInt(i)) {
                    var index = parseInt(j) + 1;
                    if (temp[index]) {
                        if (str[i] == temp[index]) {
                            layer.alert("选择的老师不能相同！");
                            return false;
                        }
                    }
                }
            }
        }
        var room = validateRoom.split(",");
        var tempRoom = validateRoom.split(",");
        for (var i in room) {
            for (var j in tempRoom) {
                if (parseInt(j) >= parseInt(i)) {
                    var index = parseInt(j) + 1;
                    if (tempRoom[index]) {
                        if (room[i] == tempRoom[index]) {
                            layer.alert("选择的教室不能相同！");
                            return false;
                        }
                    }
                }
            }
        }


        $.ajax({
            type: "POST",
            url: "/zouban/subjectConfig/editFZZBInfo.do",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(data),
            success: function (resp) {
                if (resp.code == '200') {
                    layer.alert("编辑成功");
                    getFZZBTeacherList();
                } else {
                    layer.alert(resp.message);
                }
                $('.bg').hide();
                $POPUP_FZZBT.hide();
            }
        });

    }

    function getDSZKZoubanConfig() {
        var requestData = {};
        requestData.type = CLASSTYPE.ODDEVEN;
        requestData.term = BASEDATA.term;
        requestData.gradeId = BASEDATA.gradeId;
        Common.getDataAsync("/zouban/subjectConfig/findDSZKTeacherList.do", requestData, function (resp) {
            Common.render({
                tmpl: '#zoubanOddEvenTmpl',
                data: resp.zouBanCourseDTOs,
                context: '#zoubanOddEven',
                overwrite: 1
            });
        })
    }

    function getFZZBTeacherList() {
        var requestData = {};
        requestData.type = CLASSTYPE.GROUPZOUBAN;
        requestData.term = BASEDATA.term;
        requestData.gradeId = BASEDATA.gradeId;
        requestData.subjectId = BASEDATA.subjectId;
        Common.getDataAsync("/zouban/subjectConfig/findFZZBTeacherList.do", requestData, function (resp) {
            Common.render({
                tmpl: '#FZZBTeacherListTmpl',
                data: resp.zoubanCourseList,
                context: '#FZZBTeacherList',
                overwrite: 1
            });
        })
    }

    function getFZZBSubjectConfig() {
        var requestData = {};
        requestData.type = CLASSTYPE.GROUPZOUBAN;
        requestData.term = BASEDATA.term;
        requestData.gradeId = BASEDATA.gradeId;
        Common.getDataAsync("/zouban/subjectConfig/findFZZBSubjectConfList.do", requestData, function (resp) {
            Common.render({
                tmpl: '#FZZB-subjectsTmpl',
                data: resp.subjectConfList,
                context: '#FZZB-SUBJECT',
                overwrite: 1
            });
            Common.render({
                tmpl: '#FZZBsubjectListTmpl',
                data: resp.subjectConfList,
                context: '#FZZBsubjectList',
                overwrite: 1
            });
        })
    }

    function autoSetTeacherAndRoom() {
        var requestData = {
            term: BASEDATA.term,
            gradeId: BASEDATA.gradeId,
            type: 2
        };
        Common.postDataAsync("/zouban/subjectConfig/autoSetTeacherAndRoom.do", requestData, function (resp) {
            if (resp.code == '500') {
                layer.alert(resp.message)
            }
            FZBCourseList();
        });
    }

    function autoSetDSZKTeacher() {
        var requestData = {
            term: BASEDATA.term,
            gradeId: BASEDATA.gradeId,
            type: 8
        };
        Common.postDataAsync("/zouban/subjectConfig/autoSetTeacherAndRoom.do", requestData, function (resp) {
            if (resp.code == '500') {
                layer.alert("设置失败：" + resp.message);
            }
            getDSZKZoubanConfig();
        });
    }

    function autoSetFZZBTeacherAndRoom() {
        var requestData = {
            term: BASEDATA.term,
            gradeId: BASEDATA.gradeId,
            type: 7
        };
        Common.postDataAsync("/zouban/subjectConfig/autoSetFZZBTeacherAndRoom.do", requestData, function (resp) {
            if (resp.code == '200') {
                getFZZBTeacherList();
                layer.alert("设置成功")
            } else {
                layer.alert(resp.message)
            }
        });
    }

    function getTeachersBySubjectId(node) {
        var tr = node.parents('tr');
        var className = tr.find('.className').text();
        var courseName = tr.find('.courseName').text();
        var weekTime = tr.find('.weekTime').text();
        $('.popup-SZ h2').hide().html(className + '<label>' + courseName + '</label>').show();

        $('#weekTime').val(weekTime);
        var subjectId = node.attr('subId');
        BASEDATA.courseId = node.attr('course');
        Common.getDataAsync("/zouban/subjectConfig/teachers.do", {
            subjectId: subjectId,
            gradeId: BASEDATA.gradeId
        }, function (resp) {
            Common.render({
                tmpl: '#teachersTmpl',
                data: resp.teacherList,
                context: '#teachers',
                overwrite: 1
            });
            $('#teachers').val(node.attr('tid'));
        })
    }

    function setTeacher() {
        var teacher = $('#teachers').find('option:selected');
        var weekTime = $('#weekTime').val();
        if (weekTime == undefined || weekTime == "") {
            alert("不能为空！");
            return;
        }
        if (isNaN(weekTime)) {
            alert("请填写数字");
            return;
        }
        var requestData = {
            courseId: BASEDATA.courseId,
            teacherId: teacher.val(),
            teacherName: teacher.text(),
            weekTime: weekTime
        };
        Common.postDataAsync("/zouban/subjectConfig/teachers.do", requestData, function (resp) {
            if (resp.code == '200') {
                FZBCourseList()
                $('.popup-SZ').hide();
                $('.bg').hide();
            } else {
                layer.alert(resp.message)
            }
        })
    }

    function FZBCourseList() {
        var requestData = {
            term: BASEDATA.term,
            gradeId: BASEDATA.gradeId,
            subjectId: $('#subjectList').val()
        }
        Common.getDataAsync("/zouban/subjectConfig/findBianBanList.do", requestData, function (resp) {
            Common.render({
                tmpl: '#FZBCoursesTmpl',
                data: resp.rows,
                context: '#FZBCourses',
                overwrite: 1
            });
        })
    }


    function editPECourse(ele) {
        showPEWin();

        BASEDATA.mClassId = ele.attr("mClassId");
        BASEDATA.fClassId = ele.attr("fClassId");
        BASEDATA.isPEEdit = true;

        $POPUP_TYK.find('#courseName').val(ele.attr('courseName'));
        $POPUP_TYK.find('#lessonCount').val(ele.find('.lessonCount').text());
        var MTeacherId = ele.attr("MTeacherId");
        $POPUP_TYK.find('#MPETeachers input:radio[value=' + MTeacherId + ']').prop('checked', true);
        var FTeacherId = ele.attr("FTeacherId");
        $POPUP_TYK.find('#FPETeachers input:radio[value=' + FTeacherId + ']').prop('checked', true);
        var adminClassIds = ele.attr('adminClassId').split(',');
        for (var i in adminClassIds) {
            $POPUP_TYK.find('.classes input:checkbox[value=' + adminClassIds[i] + ']').prop('checked', true);
        }
    }

    function showPEWin() {
        $POPUP_TYK.find('#courseName').val("");
        $POPUP_TYK.find('#lessonCount').val(1);
        $POPUP_TYK.find('#MPETeachers input:radio').prop('checked', false);
        $POPUP_TYK.find('#FPETeachers input:radio').prop('checked', false);
        $POPUP_TYK.find('.classes input:checkbox').prop('checked', false);
        $(".bg").show();
        $POPUP_TYK.show();
    }

    function deletePECourse(ele) {
        layer.confirm("将清空课表中相关课程，确定要删除？", function (index) {
            var requestData = {};
            requestData.term = BASEDATA.term;
            requestData.mClassId = ele.attr("mClassId");
            requestData.fClassId = ele.attr("fClassId");
            Common.getData("/zouban/subjectConfig/deletePECourse.do", requestData, function (resp) {
                if (resp.code == '500') {
                    layer.alert('删除失败');
                }
            });
            getPECourseList();
            $(".bg").hide();
            ele.hide();
            layer.close(index);
        });
    }

    function addDSZK() {
        //先判断编辑过后是否相同
        var requestData = {};
        requestData.oddEvenId = BASEDATA.oddEvenId;
        requestData.term = BASEDATA.term;
        requestData.gradeId = BASEDATA.gradeId;
        requestData.subjectId = $POPUP_DSZK.find('#danzhou').val() + "," + $POPUP_DSZK.find('#shuanzhou').val();
        if ($POPUP_DSZK.find('#danzhou').val() == $POPUP_DSZK.find('#shuanzhou').val()) {
            layer.alert("单周课不能与双周课重复！");
            return false;
        }
        requestData.xuankeId = BASEDATA.xuankeId;
        Common.getDataAsync("/zouban/subjectConfig/addDSZK.do", requestData, function (resp) {
            $(".bg").hide();
            $POPUP_DSZK.hide();
            if (resp.code == '500') {
                console.log(resp.message);
                layer.alert(resp.message.message);

            } else {
                getDSZKSubjectConfig();
                layer.alert('新增成功');
            }
        });
    }

    function getDSZKSubjectConfig() {
        var requestData = {};
        requestData.type = CLASSTYPE.ODDEVEN;
        requestData.term = BASEDATA.term;
        requestData.gradeId = BASEDATA.gradeId;
        Common.getDataAsync("/zouban/subjectConfig/findDSZKSubjectConfList.do", requestData, function (resp) {
            Common.render({
                tmpl: '#DSZK-subjectsTmpl',
                data: resp.oddEvenDTOList,
                context: '#DSZK-SUBJECT',
                overwrite: 1
            });
        })
    }

    function addFZZB() {
        var requestData = {};
        requestData.subjectConfId = CONFIG.subjectConfDTO.subjectConfId;
        requestData.term = BASEDATA.term;
        requestData.gradeId = BASEDATA.gradeId;
        requestData.lessonCount = $POPUP_FZZB.find('#lessonNo').val();
        requestData.classIds = $POPUP_FZZB.find("input:checkbox:checked").map(function (index, elem) {
            return $(elem).val();
        }).get().join(',');
        requestData.classNumber = $POPUP_FZZB.find('#fenban').val();
        requestData.subjectId = $POPUP_FZZB.find('#fzzbSubject').val();
        requestData.subjectName = $POPUP_FZZB.find('#fzzbSubject').find("option:selected").text();
        requestData.xuankeId = BASEDATA.xuankeId;
        if (!checkFzzbData(requestData)) {
            return false;
        }
        Common.getDataAsync("/zouban/subjectConfig/addFZZB.do", requestData, function (resp) {
            if (resp.code == '500') {
                layer.alert(resp.message);
                $POPUP_FZZB.hide();
                $(".bg").hide();
            } else {
                getFZZBSubjectConfig();
                layer.alert('操作成功');
                $(".bg").hide();
                $POPUP_FZZB.hide();
            }
        })

    }

    function checkFzzbData(requestData) {
        if (requestData.classIds == "" || requestData.classIds == undefined) {
            layer.alert('至少选择一个关联班级');
            return false;
        }
        return true;

    }

    function addPECourse() {
        var requestData = {};
        requestData.term = BASEDATA.term;
        requestData.gradeId = BASEDATA.gradeId;
        requestData.className = $.trim($POPUP_TYK.find('#courseName').val());
        requestData.lessonCount = $POPUP_TYK.find('#lessonCount').val();
        requestData.classIds = $POPUP_TYK.find("input:checkbox:checked").map(function (index, elem) {
            return $(elem).val();
        }).get().join(',');
        requestData.mTeacherId = $POPUP_TYK.find("input[name='teacherMName']:checked").val();
        requestData.mTeacherName = $POPUP_TYK.find("input[name='teacherMName']:checked").next().text();
        requestData.fTeacherId = $POPUP_TYK.find("input[name='teacherFName']:checked").val();
        requestData.fTeacherName = $POPUP_TYK.find("input[name='teacherFName']:checked").next().text();
        if (!checkTYData(requestData)) {
            return false;
        }
        Common.getDataAsync("/zouban/subjectConfig/addPECourse.do", requestData, function (resp) {
            if (resp.code == '500') {
                layer.alert('新增失败');
            } else {
                getPECourseList();
                layer.alert('新增成功');
                $(".bg").hide();
                $POPUP_TYK.hide();
            }
        })
    }

    function updatePECourse() {
        var requestData = {};
        requestData.term = BASEDATA.term;
        requestData.mClassId = BASEDATA.mClassId;
        requestData.fClassId = BASEDATA.fClassId;
        requestData.className = $.trim($POPUP_TYK.find('#courseName').val());
        requestData.lessonCount = $POPUP_TYK.find('#lessonCount').val();
        requestData.classIds = $POPUP_TYK.find("input:checkbox:checked").map(function (index, elem) {
            return $(elem).val();
        }).get().join(',');
        requestData.mTeacherId = $POPUP_TYK.find("input[name='teacherMName']:checked").val();
        requestData.mTeacherName = $POPUP_TYK.find("input[name='teacherMName']:checked").next().text();
        requestData.fTeacherId = $POPUP_TYK.find("input[name='teacherFName']:checked").val();
        requestData.fTeacherName = $POPUP_TYK.find("input[name='teacherFName']:checked").next().text();
        if (!checkTYData(requestData)) {
            return false;
        }
        Common.getDataAsync("/zouban/subjectConfig/updatePECourse.do", requestData, function (resp) {
            if (resp.code == '500') {
                layer.alert('更新失败');
            } else {
                getPECourseList();
                layer.alert('更新成功');
                $(".bg").hide();
                $POPUP_TYK.hide();
            }
        })
    }

    function checkTYData(requestData) {
        if (requestData.className == '') {
            layer.alert('体育课名称不能为空');
            return false;
        } /*else if(requestData.classIds.split(',').length != 2){
         alert('关联行政班级必须为2个');
         return false;
         } */ else if (requestData.mTeacherId == null) {
            layer.alert('男生班体育老师未设置');
            return false;
        } else if (requestData.fTeacherId == null) {
            layer.alert('女生班体育老师未设置');
            return false;
        }
        return true;
    }

    function getPECourseList() {
        Common.getData("/zouban/subjectConfig/getPECourseList.do", {
            gradeId: BASEDATA.gradeId,
            term: BASEDATA.term
        }, function (resp) {
            Common.render({
                tmpl: '#PECoursesTmpl',
                data: resp.courseList,
                context: '.PECourses',
                overwrite: 1
            });
        })
    }

    function editDSZKSubjectConfig(ele) {
        BASEDATA.oddEvenId = ele.attr('oddEvenId');
        var subjectIds = ele.attr('subId').split(",");
        $POPUP_DSZK.find('#danzhou').val(subjectIds[0]);
        $POPUP_DSZK.find('#shuanzhou').val(subjectIds[1]);
        $(".bg").show();
        $POPUP_DSZK.show();
    }

    function addDSZKTJ() {
        var requestData = {};
        requestData.zoubanEvenId = BASEDATA.zouBanOddEvenId;
        requestData.teacherId = $('#DANZHOU-LIST').val() + "," + $('#SHUANZHOU-LIST').val();
        requestData.teacherName = $('#DANZHOU-LIST').find("option:selected").text() + "," +
        $('#SHUANZHOU-LIST').find("option:selected").text();
        Common.postDataAsync("/zouban/subjectConfig/editDSZKTeacher.do", requestData, function (resp) {
            if (resp.code == '500') {
                layer.alert('编辑失败');
            } else {
                layer.alert('编辑成功');
            }
            $('.bg').hide();
            $POPUP_DSZKT.hide();
            getDSZKZoubanConfig();
        });

    }

    function editDSZKTeacher(ele) {
        var requestData = {};
        BASEDATA.zouBanOddEvenId = ele.attr('zoubanEvenId');
        requestData.subjectId = ele.attr('subId');
        var teacherId = ele.attr('teaId');
        requestData.gradeId = BASEDATA.gradeId;
        Common.postDataAsync("/zouban/subjectConfig/findOddEvenTeacher.do", requestData, function (resp) {
            Common.render({
                tmpl: '#DANZHOUTmpl',
                data: resp.oddDTOs,
                context: '#DANZHOU-LIST',
                overwrite: 1
            });
            Common.render({
                tmpl: '#SHUANZHOUTmpl',
                data: resp.evenDTOs,
                context: '#SHUANZHOU-LIST',
                overwrite: 1
            });

            if ("null" != teacherId) {
                var item = teacherId.split(',');
                $POPUP_DSZKT.find('#DANZHOU-LIST').val(item[0]);
                $POPUP_DSZKT.find('#SHUANZHOU-LIST').val(item[1]);
            } else {
                $('#DANZHOU-LIST option:eq(0)').attr('selected', 'selected');
                $('#SHUANZHOU-LIST option:eq(0)').attr('selected', 'selected');
            }
        });
        $(".bg").show();
        $POPUP_DSZKT.show();
    }

    function editFZZBSubjectConfig(ele) {
        CONFIG.subjectConfDTO.subjectConfId = ele.attr('subConfigId');
        $POPUP_FZZB.find('#fzzbSubject').val(ele.attr('subId'));
        $POPUP_FZZB.find('#lessonNo').val(ele.attr('lessonCount'));
        $POPUP_FZZB.find('#fenban').val(ele.attr('classNum'));
        $POPUP_FZZB.find('#classesWrap input:checkbox[name=classesWrap]').prop('checked', false);
        var classIds = ele.attr('classIds').split(',');
        for (var i in classIds) {
            $POPUP_FZZB.find('#classesWrap input:checkbox[value=' + classIds[i] + ']').prop('checked', true);
        }

        $(".bg").show();
        $POPUP_FZZB.show();
    }

    function editFZZBTeacherList(ele) {
        var requestData = {};
        BASEDATA.group = ele.attr('group');
        requestData.group = ele.attr('group');
        requestData.term = BASEDATA.term;
        requestData.gradeId = BASEDATA.gradeId;
        //先加载老师数据
        var classRommList;
        var teacherList;
        var param = {};
        param.subjectId = ele.attr('subId');
        param.gradeId = BASEDATA.gradeId;
        Common.getData("/zouban/subjectConfig/findEditFZZBTeacher.do", param, function (resp) {
            teacherList = resp.idNameDTOs;
        })
        Common.getData("/zouban/subjectConfig/findEditFZZBClassRoom.do", requestData, function (resp) {
            if(resp.code == '200') {
                var result = resp.message.zoubanGroupDTOs;
                classRommList = resp.message.idNameDTOs;
                Common.render({
                    tmpl: '#FZZBSZTmpl',
                    data: result,
                    context: '#FZZBSZ',
                    overwrite: 1
                });

                Common.render({
                    tmpl: '#tteTmpl',
                    data: teacherList,
                    context: '.tte',
                    overwrite: 1
                });

                Common.render({
                    tmpl: '#tttTmpl',
                    data: classRommList,
                    context: '.ttt',
                    overwrite: 1
                });

                for (var i in result) {
                    if (result[i].teacherId != "") {
                        $('#' + result[i].teId).val(result[i].teacherId);
                    } else {
                        $('#' + result[i].teId).find('option:eq(0)').attr('selected', 'selected');
                    }
                    if (result[i].classRoomId != "") {
                        $('#' + result[i].crId).val(result[i].classRoomId);
                    } else {
                        $('#' + result[i].crId).find('option:eq(0)').attr('selected', 'selected');
                    }
                }
            } else {
                layer.alert(resp.message);
            }
        });
    }

    function deleteFZZBSubjectConfig(ele) {
        var requestData = {};
        requestData.subjectConfigId = ele.attr('subConfigId');
        requestData.term = BASEDATA.term;
        requestData.gradeId = BASEDATA.gradeId;
        requestData.subjectId = ele.attr('subId');
        layer.confirm('删除学科后，课表中的相关课程会被清空，确定删除本学科吗？', function (index) {
            Common.postDataAsync("/zouban/subjectConfig/deleteFZZBSubject.do", requestData, function (resp) {
                if (resp.code == '500') {
                    layer.alert('删除失败');
                }
                getFZZBSubjectConfig();
            });
            layer.close(index);
        });
    }

    function deleteFZBKTeacher(ele) {
        var requestData = {};
        requestData.courseId = ele.attr('course') + ",";
        requestData.term = BASEDATA.term;
        requestData.gradeId = BASEDATA.gradeId;
        layer.confirm('删除该行政班非走班课后，课表中的相关课程会被清空，确定删除该设置吗？', function (index) {
            Common.postDataAsync("/zouban/subjectConfig/deleteDSZKTeacher.do", requestData, function (resp) {
                if (resp.code == '500') {
                    layer.alert('删除失败');
                }
                FZBCourseList()
            });
            layer.close(index);
        });
    }

    function deleteDSZKCourse(ele) {
        var requestData = {};
        requestData.courseId = ele.attr('zoubanEvenId');
        requestData.term = BASEDATA.term;
        requestData.gradeId = BASEDATA.gradeId;
        layer.confirm('删除学科后，后续步骤要重新设置，确定删除该设置吗？', function (index) {
            Common.postDataAsync("/zouban/subjectConfig/deleteDSZKTeacher.do", requestData, function (resp) {
                if (resp.code == '500') {
                    layer.alert('删除失败');
                }
                //getDSZKSubjectConfig();
                getDSZKZoubanConfig();
            });
            layer.close(index);
        });
    }

    function deleteDSZKSubjectConfig(ele) {
        var requestData = {};
        requestData.oddEvenId = ele.attr('oddEvenId');
        requestData.term = BASEDATA.term;
        requestData.gradeId = BASEDATA.gradeId;
        requestData.subjectId = ele.attr('subId');
        layer.confirm('删除学科后，课表中的相关课程会被清空，确定删除本学科吗？', function (index) {
            Common.postDataAsync("/zouban/subjectConfig/deleteDSZKSubject.do", requestData, function (resp) {
                if (resp.code == '500') {
                    layer.alert('删除失败');
                }
                getDSZKSubjectConfig();
                getDSZKZoubanConfig();
            });
            layer.close(index);
        });
    }

    function editZBSubjectConfig(ele) {
        CONFIG.subjectConfDTO.subjectConfId = ele.attr('subConfigId');
        $POPUP_ZB.find('.subjects').val(ele.attr('subId')).attr('disabled', true);
        $POPUP_ZB.find('.lessonNo-DJ').val(ele.find('.adv').text());
        $POPUP_ZB.find('.lessonNo-HG').val(ele.find('.sim').text());

        $(".bg").show();
        $POPUP_ZB.show();
    }

    function editFZBSubjectConfig(ele) {
        CONFIG.subjectConfDTO.subjectConfId = ele.attr('subConfigId');
        $POPUP_FZB.find('.subjects').val(ele.attr('subId')).attr('disabled', true);
        $POPUP_FZB.find('.lessonNo-DJ').val(ele.find('.adv').text());

        $(".bg").show();
        $POPUP_FZB.show();
    }


    function removeFZZBCourse(courseId,groupId) {
        var url = '/zouban/subjectConfig/deleteFZBKTeacher.do';
        var param = {};
        param.term = BASEDATA.term;
        param.courseId = courseId;


        layer.confirm('删除学科后，后续步骤要重新设置，确定删除该设置吗？', function (index) {
            Common.postDataAsync(url, param, function (resp) {
                if (resp.code == '500') {
                    layer.alert('删除失败');
                }
                getFZZBTeacherList();
            });
            layer.close(index);
        });
    }


    function deleteSubjectConfig(ele, type) {
        var subjectConfigId = ele.attr('subConfigId');
        layer.confirm('删除学科后，课表中的相关课程会被清空，确定删除本学科吗？', function (index) {
            Common.postDataAsync("/zouban/subjectConfig/deleteSubjectConf/" + subjectConfigId + "/deletion.do", {}, function (resp) {
                if (resp.code == '500') {
                    layer.alert('删除失败');
                }
                getSubjectConfig(type);
            });
            layer.close(index);
        });
    }

    function getSubjectConfig(type) {
        var requestData = {};
        requestData.type = type;
        requestData.gradeId = BASEDATA.gradeId;
        requestData.xuankeId = BASEDATA.xuankeId;
        Common.getDataAsync("/zouban/subjectConfig/findSubjectConfList.do", requestData, function (resp) {
            if (resp.code == '200') {
                var subjectConfList = resp.message;

                if (CLASSTYPE.ZOUBAN == type) {
                    Common.render({
                        tmpl: '#ZB-subjectsTmpl',
                        data: subjectConfList,
                        context: '#ZB-subjects',
                        overwrite: 1
                    });
                } else if (CLASSTYPE.FEIZOUBAN == type) {
                    Common.render({
                        tmpl: '#FZB-subjectsTmpl',
                        data: subjectConfList,
                        context: '#FZB-subjects',
                        overwrite: 1
                    });

                    Common.render({
                        tmpl: '#subjectListTmpl',
                        data: subjectConfList,
                        context: '#subjectList',
                        overwrite: 1
                    });
                }
            } else {
                console.log(resp.message);
            }
        });
    }

    function postSubjectConfig($win, type) {
        var subjectConfDTO = CONFIG.subjectConfDTO;
        subjectConfDTO.subjectId = $win.find('.subjects').val();
        subjectConfDTO.subjectName = $win.find('.subjects').find('option:selected').text();
        subjectConfDTO.type = type;
        subjectConfDTO.advanceTime = $win.find('.lessonNo-DJ').val();
        if (type == CLASSTYPE.ZOUBAN) {
            subjectConfDTO.simpleTime = $win.find('.lessonNo-HG').val();
        }


        Common.postDataAsync("/zouban/subjectConfig/subjectConf.do", subjectConfDTO, function (resp) {
            $win.hide();
            if (resp.code == '500') {
                layer.alert(resp.message);
            } else {
                layer.alert("保存成功");
            }
            $(".bg").hide();
            getSubjectConfig(type);
        });
    }

    //预加载的数据
    $(function () {
        //学科列表
        Common.getDataAsync("/zouban/subjectConfig/findSubjectList.do", {gradeId: BASEDATA.gradeId}, function (resp) {
            CONFIG.subList = resp.message;
            Common.render({
                tmpl: '#subjectTmpl',
                data: CONFIG.subList,
                context: '.subjects',
                overwrite: 1
            });
        })
        //班级列表
        Common.getDataAsync("/myschool/classlist.do", {gradeid: BASEDATA.gradeId}, function (resp) {
            CONFIG.adminClasses = resp.rows;
            Common.render({
                tmpl: '#classTmpl',
                data: CONFIG.adminClasses,
                context: '.classes',
                overwrite: 1
            });
            Common.render({
                tmpl: '#classesWrapTmpl',
                data: CONFIG.adminClasses,
                context: '.classesWrap',
                overwrite: 1
            });
        })
        //体育老师列表
        Common.getDataAsync("/zouban/subjectConfig/findPETeacher.do", {gradeId: BASEDATA.gradeId}, function (resp) {
            CONFIG.PETeachers = resp.teacherList;
            Common.render({
                tmpl: '#MPETeachersTmpl',
                data: CONFIG.PETeachers,
                context: '#MPETeachers',
                overwrite: 1
            });
            Common.render({
                tmpl: '#FPETeachersTmpl',
                data: CONFIG.PETeachers,
                context: '#FPETeachers',
                overwrite: 1
            });
        })


        CONFIG.subjectConfDTO = {
            type: -1,
            term: BASEDATA.term,
            gradeId: BASEDATA.gradeId,
            xuankeId: BASEDATA.xuankeId,
            subjectConfId: "",
            subjectId: "",
            subjectName: "",
            advanceTime: 0,
            simpleTime: 0,
            ifFengCeng: 0
        }
    })


    $(function () {
        //走班  新增学科
        $(".ZBK-II-XZ").click(function () {
            if (!RELEASE) {
                CONFIG.subjectConfDTO.subjectConfId = "";
                $(".bg").show();
                $POPUP_ZB.show();
                $POPUP_ZB.find('.subjects').attr('disabled', false);
            } else {
                layer.alert('已经开始选课，不能新增！如果确实要新增，请取消选课公布。');
            }
        });
        //新增非走班课
        $(".FZBK-II-XZ").click(function () {
            CONFIG.subjectConfDTO.subjectConfId = "";
            $(".bg").show();
            $POPUP_FZB.show();
            $POPUP_FZB.find('.subjects').attr('disabled', false);
        })
        //新增分组走班课
        $(".FZBK-II-XZ").click(function () {
            CONFIG.subjectConfDTO.subjectConfId = "";
            $(".bg").show();
            $POPUP_FZB.show();
            $POPUP_FZB.find('.subjects').attr('disabled', false);
        })
        //新增单双周课
        $(".DSZ-II-XZ").click(function () {
            BASEDATA.oddEvenId = "";
            $(".bg").show();
            $(".DSZ-XKGL").show();
            $POPUP_FZB.find('.subjects').attr('disabled', false);
        })
        //新增体育课
        $(".TYK-XZ").click(function () {
            $('.popup-TYK-IN').prop('disabled', false);
            showPEWin();
            BASEDATA.isPEEdit = false;
        });
        //点击X或取消  弹窗消失
        $(".SQ-X, .XKGL-QX, .TYK-QX, .SZ-QX").click(function () {
            $(".bg").hide();
            $(this).parent().parent().hide();
            $('#zbTips').hide();
            $('#fzbTips').hide();
        })

        $("#fenpei").click(function () {
            $(".bg").hide();
            $(this).parent().parent().hide();
            getFZZBTeacherList();
        });


        $(".tab-head li").click(function () {
            $(this).addClass("cur").siblings().removeClass("cur");
            $(".tab-main>div").hide();
            var names = $(this).attr("id");
            $("#" + "tab-" + names).show();
            $('.zouban-title').show();
        })
        $(".zouban-addbtn").find("li").click(function () {
            $_zbli = $(".zouban-addbtn").find("li");
            $_zbli.removeClass("zb-active");
            $(this).addClass("zb-active");
        })
        $(".zouban-addbtn-I").find("li").click(function () {
            $_zbli = $(".zouban-addbtn-I").find("li");
            $_zbli.removeClass("w-active");
            $(this).addClass("w-active");
        })
        $(".zouban-addbtn-II").find("li").click(function () {
            getFZZBSubjectConfig();
            $_zbli = $(".zouban-addbtn-II").find("li");
            $_zbli.removeClass("v-active");
            $(this).addClass("v-active");
        })
        $(".subject-manage").click(function () {
            $("#tab-FZBK-II,#tab-FZBK-I").hide();
            $("#tab-FZBK-II").show();
        })
        //单双周课学科管理
        $(".subject-manage-I").click(function () {
            $("#tab-FZBK-1,#tab-FZBK-2").hide();
            $("#tab-FZBK-1").show();
        })
        //分组走班学科管理
        $(".subject-manage-II").click(function () {
            $("#tab-FZZB-II,#tab-FZZB-I").hide();
            $("#tab-FZZB-I").show();
        })
        $(".FZBK-top-left span").click(function () {
            $(this).addClass("FZBK-cur").siblings().removeClass("FZBK-cur");
            $(".fzbk>div").hide();
            var namee = $(this).attr("id");
            $("#" + "tab-" + namee).show();
        })
        $(".FZBK-XX").click(function () {
            $(".bg").show();
            $(".popup-DR").show();
        })

        $('#ZBK').click(function () {
            getSubjectConfig(CLASSTYPE.ZOUBAN)
        })
        $('#FZBK').click(function () {
            getSubjectConfig(CLASSTYPE.FEIZOUBAN)
        })
        $('#TYK').click(function () {
            getPECourseList();
        })


    })


});
