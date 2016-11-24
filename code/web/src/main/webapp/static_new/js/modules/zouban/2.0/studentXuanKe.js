/**
 * Created by fl on 2016/7/16.
 * 7.27
 */
define(function (require, exports, module) {
    var Common = require('common');
    require('layer');
    var CONFIG = {};
    var BASEDATA = {
        term: $('body').attr('term'),
        gradeId: $('body').attr('gradeId'),
        gradeName: $('body').attr('gradeName'),
        xuankeId: $('body').attr('xuankeId')
    };

    $(function () {



        getXuanKeConf();
        getSubjectGroup();



        $(".tstep-tab li").click(function () {
            if (CONFIG.xuanKeConf.isRelease != 1) {
                layer.alert('请先公布选课设置');
                return false;
            }
            $(".tstep-tab li").removeClass("m-active");
            $(this).addClass("m-active");
            $(".set-div>div").hide();
            var name = $(this).attr("id");
            $("#" + "tab-" + name).show();
        })
        $('.back-xkjd').click(function () {
            $(".set-div>div").hide();
            $("#tab-XKJD").show();
        })
        $(".btn-settime").click(function () {
            $(".zhiban-meng").show();
            $(".settime-alert").show();
            $('#opentime').val(CONFIG.xuanKeConf.opentime);
            $('#endtime').val(CONFIG.xuanKeConf.endtime);
        })
        $(".zb-set-close,.alert-btn-qx").click(function () {
            $(".zhiban-meng").hide();
            $(".settime-alert").hide();
        })
        $(".zb-set-close,.alert-btn-qx").click(function () {
            $(".zhiban-meng").hide();
            $(".tzxk-alert").hide();
        });


        //更新选课时间
        $('#updateXuanKeTime').click(function () {
            updateXuanKeConf();
        })
        //开放关闭选课组合
        $('body').on('change', 'input[name="subjectgroup"]', function () {
            updatePublicState($(this))
        })
        //开启、关闭公布
        $('.btn-gongbu').click(function () {
            isRelease()
        })
        //下载模板
        $(".download").on("click", function () {
            window.location = '/zouban/studentXuanke/exportExcel.do?gradeId=' + BASEDATA.gradeId;
        });
        //上传学生选课结果
        $(".buu").on("click", function () {
            if ($("#file").val() == "") {
                layer.alert("请先选择上传文件");
            } else {
                uploadFile()
            }
        });
        //按科目查看学生选课详情
        $('.adminClassContext, .subjectContext').change(function () {
            xuanKeSubjectDetail($('.subjectContext').val(), $('.adminClassContext').val())
        });

        $('body').on('click', '.drjg-chakan', function () {
            $(".xsxkjd-con").hide();
            $(".look-link-one").show();
            var subjectId = $(this).attr('subid');
            xuanKeSubjectDetail(subjectId, "")
        })
        //按组合查看学生选课详情
        $('body').on('click', ".drjg-look", function () {
            $(".xsxkjd-con").hide();
            $(".look-link-two").show();
            var subjectGroupId = $(this).attr("sgid");
            $('#subjectGroupName').text($(this).parents('tr').find('label').text());
            getSubjectGroupStudent(subjectGroupId);
        })
        //筛选班级学生选课情况
        $('#TZXK').click(function () {
            var classId = $('.adminClassContext1').val();
            var choose = $('#choosetype').val();
            CONFIG.className = $(".adminClassContext1").find('option:selected').text()
            CONFIG.classId = classId;
            CONFIG.choose = choose;
            studentXuanKeList(classId, choose)
        })
        $('.adminClassContext1, #choosetype').change(function () {
            var classId = $('.adminClassContext1').val();
            var choose = $('#choosetype').val();
            CONFIG.className = $(".adminClassContext1").find('option:selected').text()
            CONFIG.classId = classId;
            CONFIG.choose = choose;
            studentXuanKeList(classId, choose)
        })
        //调整选课弹窗
        $('body').on('click', '.tzxkjg-tz', function () {
            $(".zhiban-meng").show();
            $(".tzxk-alert").show();
            var userId = $(this).attr('uid');
            var studentName = $(this).attr('unm');
            CONFIG.stuId = userId;
            CONFIG.stuName = studentName;
            getXuankeResultAdmin(userId, studentName, CONFIG.className)
        })
        //切换选课
        $('body').on('click', '.for-select', function () {
            chooseSubject($(this))
        })
        //保存调整后的选课结果
        $('#teacherXK').click(function () {
            teacherXuanke()
        })

        $('#saveInfo').click(function () {
            updateXuankeInfo()
        })


        classList();
        subjectGroupStudentNumPair();
    })

    function updateXuankeInfo() {
        var info = $.trim($('#info').val());
        Common.postDataAsync("/zouban/studentXuanke/updateXuanKeInfo.do", {
            xuankeId: BASEDATA.xuankeId,
            info: info
        }, function (resp) {
            if (resp.code == '200') {
                layer.alert('保存成功');
            }
        })
    }

    function teacherXuanke() {
        var advance = "";
        var simple = "";
        var advNum = 0;
        $('#xuankeResultAdminContext .sub').each(function (index, ele) {
            var subjectId = $(ele).attr('subid');
            var $td = $(ele).find('td').eq(1);
            if ($td.text() == '已选') {//等级考已选
                advance += subjectId + ",";
                advNum++;
            } else {
                simple += subjectId + ",";
            }
        })
        if (advNum != 3) {
            layer.alert("等级考、合格考应该各选3个");
            return false;
        }

        var requestData = {
            classId: CONFIG.classId,
            advance: advance,
            simple: simple,
            stuId: CONFIG.stuId,
            stuName: CONFIG.stuName,
            xuankeId: BASEDATA.xuankeId
        };
        Common.getDataAsync("/zouban/studentXuanke/teacherXK.do", requestData, function (resp) {
            if (resp == '200') {
                studentXuanKeList(CONFIG.classId, CONFIG.choose)
                layer.alert('操作成功');
                $(".zhiban-meng").hide();
                $(".tzxk-alert").hide();
                subjectGroupStudentNumPair();
            }

        })
    }

    function chooseSubject(ele) {
        if (!ele.hasClass('m-selected')) {
            ele.removeClass('m-selecte').addClass('m-selected');
            ele.text('已选');
            var sibling = ele.parents('td').siblings('.select').children('span');
            sibling.removeClass('m-selected').addClass('m-selecte');
            sibling.text('我选');
        }

    }

    function getXuankeResultAdmin(userId, studentName, className) {
        var requestData = {
            userId: userId,
            gradeId: BASEDATA.gradeId,
            term: BASEDATA.term
        };

        Common.getDataAsync("/zouban/studentXuanke/getXuankeResultAdmin.do", requestData, function (resp) {
            console.log(resp)
            var advChoose = [];
            var simChoose = [];
            for (var i = 0; i < resp.adv.length; i++) {
                advChoose.push(resp.adv[i].value);
            }
            for (var i = 0; i < resp.sim.length; i++) {
                simChoose.push(resp.sim[i].value);
            }
            var data = {
                subConfList: resp.conf.subConfList,
                advChoose: advChoose,
                simChoose: simChoose,
                stuName: studentName,
                className: className
            };

            Common.render({
                tmpl: '#xuankeResultAdminTmpl',
                data: data,
                context: '#xuankeResultAdminContext',
                overwrite: 1
            });

        })
    }

    function studentXuanKeList(classId, choose) {
        var requestData = {
            classId: classId,
            xuankeId: BASEDATA.xuankeId,
            choose: choose,
            userName: ""
        };

        Common.getDataAsync("/zouban/studentXuanke/studentXuanKeList.do", requestData, function (resp) {
            console.log(resp.rows)
            Common.render({
                tmpl: '#stuAdvTmpl',
                data: resp.rows,
                context: '#stuAdvContext',
                overwrite: 1
            });

        })
    }

    function getSubjectGroupStudent(subjectGroupId) {
        var requestData = {
            subjectGroupId: subjectGroupId,
            xuankeId: BASEDATA.xuankeId,
            gradeId: BASEDATA.gradeId,
            term: BASEDATA.term
        };
        Common.getDataAsync("/zouban/studentXuanke/subjectGroupStudent.do", requestData, function (resp) {
            if (resp.code == '200') {
                Common.render({
                    tmpl: '#subjectGroupStudentTmpl',
                    data: resp.message,
                    context: '#subjectGroupStudentContext',
                    overwrite: 1
                });
            }
        })
    }

    function uploadFile() {
        var index = layer.load(2, {shade: [0.3, '#000']});
        $.ajaxFileUpload({
            url: "/zouban/studentXuanke/import.do?gradeId=" + BASEDATA.gradeId + "&term=" + BASEDATA.term, //用于文件上传的服务器端请求地址
            secureuri: false, //是否需要安全协议，一般设置为false
            fileElementId: 'file', //文件上传域的ID
            dataType: 'json', //返回值类型 一般设置为json

            success: function (data, status) {//服务器成功响应处理函数
                if (data.result == 'fail') {
                    layer.close(index);
                    layer.alert("上传失败！第" + data.line + "行" + data.reason);
                } else {
                    layer.close(index);
                    layer.alert("上传成功！");
                }
                getXuanKeConf();
                subjectGroupStudentNumPair();
            },
            error: function (data, status, e) {//服务器响应失败处理函数
                layer.close(index);
                layer.alert("上传失败！");
            }
        });
    }

    function isRelease() {
        Common.postDataAsync("/zouban/studentXuanke/release.do", {xuankeId: BASEDATA.xuankeId}, function (resp) {
            $('.btn-gongbu').text(resp.isRelease == 1 ? '取消公布' : '公布');
            CONFIG.xuanKeConf.isRelease = resp.isRelease;

            getXuanKeConf();
            getSubjectGroup();
            subjectGroupStudentNumPair();
        });
    }

    function getXuanKeConf() {
        var requestData = {
            term: BASEDATA.term,
            gradeId: BASEDATA.gradeId,
            type: 1
        }
        Common.getData("/zouban/studentXuanke/findXuanKeConf.do", requestData, function (resp) {
            $('#begin').text(resp.startDate);
            $('#end').text(resp.endDate);
            $('#info').text(resp.info);
            $('#tab-XKJD .xsxkjd-title em').text(resp.xuankecount);
            $('.btn-gongbu').text(resp.isRelease == 1 ? '取消公布' : '公布');
            CONFIG.xuanKeConf = resp;
            Common.render({
                tmpl: '#subjectStuNumTmpl',
                data: resp.subConfList,
                context: '#subjectStuNumContext',
                overwrite: 1
            });
            Common.render({
                tmpl: '#subjectTmpl',
                data: resp.subConfList,
                context: '.subjectContext',
                overwrite: 1
            });
        });
    }

    //学科组合列表
    function getSubjectGroup() {
        var requestData = {year: BASEDATA.term, gradeId: BASEDATA.gradeId};
        Common.getData("/zouban/studentXuanke/subjectGroups.do", requestData, function (resp) {
            if (resp.code == '500') {
                layer.alert(resp.message + '.<br>请到平台管理-->学科设置关联学科或联系复兰科技！');
            } else {
                Common.render({
                    tmpl: '#subjectGroupsTmpl',
                    data: resp.message.subjectGroupDTOs,
                    context: '#subjectGroupsContext',
                    overwrite: 1
                });

                if (CONFIG.xuanKeConf.isRelease == 1) {
                    $('input[name="subjectgroup"]').prop('disabled', true);
                } else {
                    $('input[name="subjectgroup"]').prop('disabled', false);
                }
            }

        })
    }

    //更新选课时间
    function updateXuanKeConf() {
        var lessoninstallData = {};
        lessoninstallData.term = BASEDATA.term;
        lessoninstallData.gradeId = BASEDATA.gradeId;
        lessoninstallData.startDate = $("#opentime").val();
        lessoninstallData.endDate = $("#endtime").val();
        Common.getPostData('/zouban/studentXuanke/updateXuanKeConf.do', lessoninstallData, function (resp) {
            if (resp.flg) {
                $(".zhiban-meng").hide();
                $(".settime-alert").hide();
                getXuanKeConf();
            } else {
                layer.alert("设置失败！");
            }
        });
    }

    function updatePublicState($ele) {
        var id = $ele.attr('sgid');
        var isPublic = $ele.is(':checked');
        Common.postDataAsync("/zouban/studentXuanke/updateSubjectGroups.do", {
            subjectGroupId: id,
            isPublic: isPublic
        }, function (resp) {
            if (resp.code == '500') {
                layer.alert("更改失败")
            }
        })
    }

    function xuanKeSubjectDetail(subjectId, classId) {
        var requestData = {
            type: 1,
            term: BASEDATA.term,
            gradeId: BASEDATA.gradeId,
            xuankeId: BASEDATA.xuankeId,
            subjectId: subjectId,
            classId: classId
        }
        $('.subjectContext').val(subjectId);
        $('.adminClassContext').val(classId);
        Common.getDataAsync("/zouban/studentXuanke/xuanKeSubjectDetail.do", requestData, function (resp) {
            Common.render({
                tmpl: '#stuChooseTmpl',
                data: resp.sublist,
                context: '#stuChooseContext',
                overwrite: 1
            });
        })
    }

    function classList() {
        //班级列表
        Common.getDataAsync("/myschool/classlist.do", {gradeid: BASEDATA.gradeId}, function (resp) {
            CONFIG.adminClasses = resp.rows;
            Common.render({
                tmpl: '#adminClassTmpl',
                data: CONFIG.adminClasses,
                context: '.adminClassContext',
                overwrite: 1
            });
            Common.render({
                tmpl: '#adminClassTmpl1',
                data: CONFIG.adminClasses,
                context: '.adminClassContext1',
                overwrite: 1
            });
        })
    }

    function subjectGroupStudentNumPair() {
        //展示组合选课人数
        Common.getDataAsync("/zouban/studentXuanke/subjectGroupStudentNumPair.do", BASEDATA, function (resp) {
            if (resp.code == '500') {
                layer.alert(resp.message);
            } else {
                Common.render({
                    tmpl: '#subGroupNumTmpl',
                    data: resp.message,
                    context: '#subGroupNumContext',
                    overwrite: 1
                });
            }
        })
    }





});

