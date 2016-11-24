/*
 * @Author: Alex
 * @Date:   2015-10-19 16:02:24
 * @Last Modified by:   Alex
 * @Last Modified time: 2015-10-19 17:13:56
 */

'use strict';
define(['doT', 'common'], function (require, exports, module) {
    /**
     *初始化参数
     */
    var lessoninstall = {},
        Common = require('common');
    var lessoninstallData = {};
    lessoninstallData.type = 1;
    var duan = {};
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * jiaoyujv_start.init()
     */
    lessoninstall.init = function () {


        /*$(".GB").click(function(){
         $(".GB").hide();
         $(".QXGB").show();
         });*/
        //$(".QXGB").click(function(){
        //    $(".QXGB").hide();
        //    $(".GB").show();
        //});
        lessoninstall.sublist();
        $(".backUrl").click(
            function () {
                window.open('../paike/index.do?version=58&year=' + encodeURI(encodeURI($('#team').text())) + '&gradeId=' +
                $('#gradeShow').attr("gid"), '_self');
            });
        $("#advchk").click(function () {
            if ($('#advchk').is(':checked')) {
                $('#advinput').attr("disabled", false);
            } else {
                $('#advinput').attr("disabled", true).val(0);
            }
        });
        $("#sipchk").click(function () {
            if ($('#sipchk').is(':checked')) {
                $('#sipinput').attr("disabled", false);
            } else {
                $('#sipinput').attr("disabled", true).val(0);
            }
        });
        $(".update-CUR-TJ").click(function () {
            $(".bg").show();
            $(".update-CUR").show();
            //$("#term1").html($("#team").text());
            //$("#grade1").html($("#gradeShow").text());

        });
        $(".update-X, .curri-QX").click(function () {
            $(".bg").hide();
            $(".update-CUR").hide();
            $(".shezhi-CUR").hide();
            $(".gongshi-CUR").hide();
            $(".fabu-CUR").hide();
            $(".Nupdate-CUR").hide();
            $(".tishi-CUR").hide();
            lessoninstallData.subjectConfId = '';
        });

        $(".curri-TJ_ZERO").click(function () {
            lessoninstall.updateXuanKeConf();
        });
        $(".curri-TJ_ONE").click(function () {
            lessoninstall.addSubjectConf();
        });
        $(".curri-TJ_THREE").click(function () {
            lessoninstall.addFeiZouBanSubjectConf();
        });
        $(".ifgongsi").click(function () {
            $(".bg").hide();
            $(".tishi-CUR").hide();
        });

        $(".tab-top ul li").click(function () {
            $(this).addClass("cur").siblings("li").removeClass("cur");
        });
        $(".shezhi-CUR-CS").click(function () {
            $("#opentime").val("");
            $("#endtime").val("");
            $(".bg").show();
            $(".shezhi-CUR").show();
            lessoninstall.findXuanKeConf();
        });
        $(".GB").click(function () {
            if (document.getElementById("xkgs").style.display == "inline-block") {
                $(".bg").show();
                $(".tishi-CUR").show();
            } else {
                $(".bg").show();
                $(".fabu-CUR").show();
            }
        });
        $(".fabu_TJ").click(function () {
            lessoninstall.isRelease(1);
        });

        $(".QXGB").click(function () {
            lessoninstall.isRelease(2);
        });

        $(".tab-Ncurriculum-update").click(function () {
            //$("#term2").html($("#team2").text());
            //$("#grade2").html($("#gradeShow2").text());
            $(".bg").show();
            $(".Nupdate-CUR").show();
            lessoninstallData.subjectConfId = '';

        });
        $(".tab-Xcurriculum-update").click(function () {
            $(".bg").show();
            $(".Nupdate-CUR").show();
        });
        $(".tab-zouban").click(function () {//走班课
            $(".tab-curriculum").show();
            $(".tab-Ncurriculum").hide();
            $(".tab-interestClass").hide();
            $(".dypeizhi_mian").hide();
            $(".tab-Xcurriculum").hide();
            $(".sports-S").hide();
            lessoninstallData.type = 1;
            lessoninstall.findXuanKeConf();
        });
        $(".tab-Nzouban").click(function () {//非走班课
            $(".tab-curriculum").hide();
            $(".tab-Ncurriculum").show();
            $(".tab-interestClass").hide();
            $(".dypeizhi_mian").hide();
            $(".tab-Xcurriculum").hide();
            $(".sports-S").hide();
            lessoninstallData.type = 2;
            lessoninstall.findXuanKeConf();
        });
        $(".tab-Xzouban").click(function () {//小走班课
            $(".tab-curriculum").hide();
            $(".tab-Ncurriculum").show();
            $(".tab-Xcurriculum").hide();
            $(".tab-interestClass").hide();
            $(".dypeizhi_mian").hide();
            $(".sports-S").hide();
            lessoninstallData.type = 3;
            lessoninstall.findXuanKeConf();
        });
        $(".cur-li").click(function () {//体育课
            $(".tab-curriculum").hide();
            $(".tab-Ncurriculum").hide();
            $(".tab-Xcurriculum").hide();
            $(".tab-interestClass").hide();
            $(".dypeizhi_mian").hide();
            $(".sports-S").show();
            lessoninstallData.type = 4;
            //lessoninstall.findXuanKeConf();
            getClassList();
            findPETeacher();
            getPEList();
        });
        $(".tab-Ozouban").click(function () {//其他走班课
            $(".tab-curriculum").hide();
            $(".tab-Ncurriculum").show();
            $(".tab-interestClass").hide();
            $(".dypeizhi_mian").hide();
            $(".tab-Xcurriculum").hide();
            $(".sports-S").hide();
            lessoninstallData.type = 5;
            lessoninstall.findXuanKeConf();
        });


        lessoninstall.findXuanKeConf();
        $(".GS").click(function () {
            $(".bg").show();
            $(".gongshi-CUR").show();
        });
        $(".gongshi_TJ").click(function () {
            lessoninstall.isPublish();
        });
        $(".QXGS").click(function () {
            lessoninstall.isPublish();
        });
        //$("#seachxk").click(function(){
        //    lessoninstall.findXuanKeConf();
        //});
        //$("#seachxk2").click(function(){
        //    lessoninstall.findXuanKeConf();
        //});
        $('body').on('change', '#team', function () {
            lessoninstall.findXuanKeConf();
        });
        $('body').on('change', '#gradeShow', function () {
            lessoninstall.findXuanKeConf();
        });
        $('body').on('change', '#team2', function () {
            lessoninstall.findXuanKeConf();
        });
        $('body').on('change', '#gradeShow2', function () {
            lessoninstall.findXuanKeConf();
        });

    };

    lessoninstall.isRelease = function (index) {
        Common.getPostData('/zouban/isRelease.do', lessoninstallData, function (rep) {
            if (rep.flg) {
                if (rep.isRelease == 2) {
                    alert("请先公示，公示后才可以发布！");
                } else if (rep.isRelease == 1) {
                    $("#state").html("已发布");
                    document.getElementById('xkgb').style.display = "none";
                    document.getElementById('xkqxgb').style.display = "inline-block";
                    $('#xkqxgs').attr("disabled", true);
                } else {
                    $("#state").html("公示中");
                    document.getElementById('xkgb').style.display = "inline-block";
                    document.getElementById('xkqxgb').style.display = "none";
                    $('#xkqxgs').attr("disabled", false);
                }
                $(".bg").hide();
                $(".fabu-CUR").hide();
            } else {
                alert("公布失败！");
            }
        });
    }
    lessoninstall.isPublish = function () {
        Common.getPostData('/zouban/isPublic.do', lessoninstallData, function (rep) {
            if (rep.flg) {
                if (rep.ispublic == 1) {
                    $("#state").html("公示中");
                    document.getElementById('xkgs').style.display = "none";
                    document.getElementById('xkqxgs').style.display = "inline-block";
                } else {
                    $("#state").html("未公示");
                    document.getElementById('xkgs').style.display = "inline-block";
                    document.getElementById('xkqxgs').style.display = "none";
                }
                $(".bg").hide();
                $(".gongshi-CUR").hide();
            } else {
                alert("公示失败！");
            }
        });
    }

    lessoninstall.findXuanKeConf = function () {
        //if (lessoninstallData.type==1) {
        lessoninstallData.term = $("#team").text();
        lessoninstallData.gradeId = $("#gradeShow").attr('gid');
        //} else {
        //    lessoninstallData.term = $("#team2").val();
        //    lessoninstallData.gradeId = $("#gradeShow2").val();
        //}

        Common.getPostData('/zouban/findXuanKeConf.do', lessoninstallData, function (rep) {
            //if (rep.xuankeId==null) {
            //    $('#xkgs').attr("disabled",true);
            //    $('#xkgb').attr("disabled",true);
            //    $('#xkgs').style.background = '#9c9c9c';
            //    $('#xkgb').style.background = '#9c9c9c';
            //} else {
            //    $('#xkgs').attr("disabled",false);
            //    $('#xkgb').attr("disabled",false);
            //
            //}
            if (lessoninstallData.type == 1) {
                $("#startdt").html(rep.startDate);
                $("#enddt").html(rep.endDate);
                $("#xuankeid").val(rep.xuankeId);
                lessoninstallData.xuankeId = rep.xuankeId;
                duan.xuankeId = rep.xuankeId;
                if (rep.xuankeId != null) {
                    $("#opentime").val(rep.opentime);
                    $("#endtime").val(rep.endtime);
                    //$('#advcnt').val(rep.advanceCount);
                    //$('#simcnt').val(rep.simpleCount);
                }
                document.getElementById('xkgs').style.display = "inline-block";
                document.getElementById('xkqxgs').style.display = "none";
                document.getElementById('xkgb').style.display = "inline-block";
                document.getElementById('xkqxgb').style.display = "none";
                if (rep.isPublic == 0) {
                    $("#state").html("未公示");
                } else if (rep.isPublic == 1 && rep.isRelease == 0) {
                    $("#state").html("公示中");
                    document.getElementById('xkgs').style.display = "none";
                    document.getElementById('xkqxgs').style.display = "inline-block";
                } else if (rep.isPublic == 1 && rep.isRelease == 1) {
                    $("#state").html("已发布");
                    document.getElementById('xkgs').style.display = "none";
                    document.getElementById('xkqxgs').style.display = "inline-block";
                    document.getElementById('xkgb').style.display = "none";
                    document.getElementById('xkqxgb').style.display = "inline-block";
                    $('#xkqxgs').attr("disabled", true);
                }
                //$("#advcnt1").html(rep.advanceCount);
                //$("#sipcnt1").html(rep.simpleCount);
                Common.render({
                    tmpl: $('#subjectConfTempJs'),
                    data: {data: rep.subConfList},
                    context: '#subjectlist',
                    overwrite: 1
                });
                $(".sub_delete").click(function () {
                    lessoninstallData.subjectConfId = $(this).attr('scid');
                    Common.getPostData('/zouban/deleteSubjectConf.do', lessoninstallData, function (rep) {
                        if (rep.flg) {
                            lessoninstall.findXuanKeConf();
                        } else {
                            alert("学科删除失败！");
                        }
                    });
                });
                $(".sub_update").click(function () {
                    lessoninstall.sublist();
                    $(".bg").show();
                    $(".update-CUR").show();
                    //$("#term1").html($("#team").text());
                    //$("#grade1").html($("#gradeShow").text());
                    lessoninstallData.subjectConfId = $(this).attr('scid');
                    Common.getPostData('/zouban/findSubjectConf.do', lessoninstallData, function (rep) {
                        if (rep.advanceTime != 0) {
                            $('#advchk').attr('checked', 'checked');
                            $("#advinput").val(rep.advanceTime);
                            $('#advinput').attr("disabled", false);
                        }
                        if (rep.simpleTime != 0) {
                            $('#sipchk').attr('checked', 'checked');
                            $("#sipinput").val(rep.simpleTime);
                            $('#sipinput').attr("disabled", false);
                        }
                        if (rep.ifFengCeng == 1) {
                            $("#fenceng1").attr("checked", 'checked');
                        } else {
                            $("#fenceng2").attr("checked", 'checked');
                        }
                        $('#explain').val(rep.explain);
                        $('#subjects').val(rep.subjectId);
                    });
                });
            } else {
                $("#subjectlist2").empty();
                Common.render({
                    tmpl: $('#subjectConfTempJs2'),
                    data: {data: rep.subConfList},
                    context: '#subjectlist2'
                });
                $(".nodelete").click(function () {
                    lessoninstallData.subjectConfId = $(this).attr('scid');
                    Common.getPostData('/zouban/deleteSubjectConf.do', lessoninstallData, function (rep) {
                        if (rep.flg) {
                            lessoninstall.findXuanKeConf();
                        } else {
                            alert("学科删除失败！");
                        }
                    });
                });
                $(".noupdate").click(function () {
                    //$("#term2").html($("#team2").text());
                    //$("#grade2").html($("#gradeShow2").text());
                    $(".bg").show();
                    $(".Nupdate-CUR").show();
                    lessoninstallData.subjectConfId = $(this).attr('scid');
                    Common.getPostData('/zouban/findSubjectConf.do', lessoninstallData, function (rep) {
                        if (rep.advanceTime != 0) {
                            $("#nuadvtm").val(rep.advanceTime);
                        }
                        $('#subjects2').val(rep.subjectId);
                    });
                });
            }

        });
    }

    lessoninstall.updateXuanKeConf = function () {
        lessoninstallData.term = $("#team").text();
        lessoninstallData.gradeId = $("#gradeShow").attr('gid');
        lessoninstallData.startDate = $("#opentime").val();
        lessoninstallData.endDate = $("#endtime").val();
        Common.getPostData('/zouban/updateXuanKeConf.do', lessoninstallData, function (rep) {
            if (rep.flg) {
                $(".bg").hide();
                $(".update-CUR").hide();
                $(".shezhi-CUR").hide();
                $(".gongshi-CUR").hide();
                $(".fabu-CUR").hide();
                $(".Nupdate-CUR").hide();
                lessoninstallData.xuankeId = rep.xuankeId;
                $("#xuankeid").val(rep.xuankeId);
                lessoninstall.findXuanKeConf();
            } else {
                alert("设置失败！");
            }

        });
    }

    lessoninstall.sublist = function () {
        Common.getPostData('/myschool/sublist.do', lessoninstallData, function (rep) {
            $("#subjects").empty();
            Common.render({tmpl: $('#subjectTempJs'), data: {data: rep.rows}, context: '#subjects'});
            $("#subjects2").empty();
            Common.render({tmpl: $('#subjectTempJs'), data: {data: rep.rows}, context: '#subjects2'});
            $("#subjectListShow").empty();
            Common.render({tmpl: $('#subjectTempJs'), data: {data: rep.rows}, context: '#subjectListShow'});
        });
    }
    lessoninstall.addSubjectConf = function () {
        lessoninstallData.term = $("#team").text();
        lessoninstallData.gradeId = $("#gradeShow").attr('gid');
        lessoninstallData.subjectId = $("#subjects").val();
        lessoninstallData.advanceTime = $("#advinput").val();
        lessoninstallData.simpleTime = $("#sipinput").val();
        lessoninstallData.ifFengCeng = $("input[type='radio']:checked").val();
        lessoninstallData.explain = $("#explain").val();
        Common.getPostData('/zouban/addOrUpdateSubjectConf.do', lessoninstallData, function (rep) {
            if (rep.flg) {
                if (rep.check) {
                    if (rep.type == 1) {
                        alert("添加成功！");
                        $("#xuankeid").val(rep.xuankeId);
                        lessoninstallData.xuankeId = rep.xuankeId;
                    } else {
                        alert("更新成功!");
                    }

                    $(".update-CUR").hide();
                    $(".bg").hide();
                    lessoninstall.findXuanKeConf();
                } else {
                    alert("科目已存在，不能重复添加！");
                }

            }
        });
    }

    lessoninstall.addFeiZouBanSubjectConf = function () {
        lessoninstallData.subjectId = $("#subjects2").val();
        lessoninstallData.subjectName = $("#subjects2").find("option:selected").text();
        lessoninstallData.advanceTime = $("#nuadvtm").val();
        Common.getPostData('/zouban/addOrUpdateNotSubjectConf.do', lessoninstallData, function (rep) {
            if (rep.flg) {
                if (rep.check) {
                    if (rep.type == 1) {
                        alert("添加成功！");
                    } else {
                        alert("更新成功!");
                    }
                    $(".Nupdate-CUR").hide();
                    $(".bg").hide();
                    lessoninstall.findXuanKeConf();
                } else {
                    alert("科目已存在，不能重复添加！");
                }

            }
        });
    };
    lessoninstall.init();

    //===========================================株洲模式走班===================================================
    var timeList = [];
    var globalCourseId = "";
    $(document).ready(function () {
        //拓展课tab点击
        $(".tab-Pzouban").click(function () {
            $(".tab-Ncurriculum").hide();
            $(".tab-curriculum").hide();
            $(".tab-interestClass").show();
            $(".dypeizhi_mian").hide();
            $(".sports-S").hide();
            lessoninstall.findClassroomList();
            lessoninstall.findInterestClassList();
        });
        //点击添加
        $(".tab-interest-update").click(function () {
            lessoninstall.findTeachersBySubjectId($("#subjectListShow").val(), "");
            globalCourseId = "";
            $(".zouban").show();
            $(".bg").show();
            $("#allClass").prop("checked", false);
            $("#show-duan").show();
            $('input:radio[name=classInp]').each(function (i) {
                $(this).prop("checked", false);
            });
        });
        //关闭弹窗
        $(".zouban-QX,.zouban-dl").on("click", function () {
            $(".zouban").hide();
            $(".bg").hide();
            $(".border").empty();
            timeList = [];
            $("#courseName").val("");
            $("#maxPeople").val("");
        });
        //弹窗确定
        $(".zouban-QD").on("click", function () {
            if ($("#courseName").val() == "") {
                alert("请输入课程名");
                return;
            }
            if ($("#maxPeople").val() == "") {
                alert("请输入最大人数");
                return;
            }
            /*if(timeList.length==0)
             {
             alert("请添加上课时间");
             return;
             }*/
            if ($("#classroomShow").val() == "") {
                alert("请设置上课地点");
                return;
            }
            if ($("#subjectListShow").val() == "") {
                alert("请设置所属学科");
                return;
            }
            if ($("#teacherShow").val() == "") {
                alert("请设置任课老师");
                return;
            }
            //添加或修改
            var x = Number($(".zouban-I").val());
            var y = Number($(".zouban-II").val());
            /*var classIds=[];
             $('input:checkbox[name=classInp]:checked').each(function(i){
             classIds.push($(this).val());
             });*/
            var groupId = "";
            if ($("#allClass").prop("checked")) {
                groupId = "";
            }
            else {
                groupId = $("input[name='classInp']:checked").val();
            }
            timeList.push({x: x, y: y});
            var zoubanCourseDTO = {
                "subjectId": $("#subjectListShow").val(),
                "zbCourseId": globalCourseId,
                "classRoomId": $("#classroomShow").val(),
                "gradeId": $("#gradeShow").attr('gid'),
                "teacherId": $("#teacherShow").val(),
                "teacherName": $("#teacherShow option:selected").text(),
                "courseName": $("#courseName").val(),
                "term": $("#team").text(),
                "max": Number($("#maxPeople").val()),
                "groupId": groupId,
                "lessonCount": timeList.length,
                "pointEntrylist": timeList
            };
            if (globalCourseId == "")
                lessoninstall.addInterestClass(zoubanCourseDTO);
            else {
                lessoninstall.updateInterestClass(zoubanCourseDTO);
            }
        });
        //学科改变，获取老师
        $("#subjectListShow").on("change", function () {
            lessoninstall.findTeachersBySubjectId($(this).val(), "");
        });

        //添加时间
        $(".zouban-tianjia").click(function () {
            var x = Number($(".zouban-I").val());
            var y = Number($(".zouban-II").val());
            var have = false;
            for (var i = 0; i < timeList.length; i++) {
                if (timeList[i].x == x && timeList[i].y == y) {
                    have = true;
                    break;
                }
            }
            if (!have) {
                timeList.push({x: x, y: y});
            }
            $(".border").empty();
            Common.render({tmpl: $('#timeTempJs'), data: {data: timeList}, context: '.border'});
        });
        //删除时间
        $("body").on("click", ".deleteTime", function () {
            var x = Number($(this).attr("x"));
            var y = Number($(this).attr("y"));
            for (var i = 0; i < timeList.length; i++) {
                if (timeList[i].x == x && timeList[i].y == y) {
                    timeList.splice(i, 1);
                    break;
                }
            }
            if (timeList.length == 0) {
                $(".border").empty();
                $(".border").append("<span>未添加上课时间</span>");
            }
            else {
                $(".border").empty();
                Common.render({tmpl: $('#timeTempJs'), data: {data: timeList}, context: '.border'});
            }
        });
        //删除兴趣班课程
        $("body").on("click", ".deleteInterest", function () {
            var courseId = $(this).attr("cid");
            if (confirm("您确定删除该兴趣班课程吗？")) {
                lessoninstall.deleteInterestClass(courseId);
            }
        });
        //编辑修改兴趣班课程
        $("body").on("click", ".editInterest", function () {
            var courseId = $(this).attr("cid");
            lessoninstall.findInterestClass(courseId);
        });
        //面向全年级
        $("body").on("change", "#allClass", function () {
            if ($("#allClass").prop("checked")) {
                $("#show-duan").hide();
            }
            else {
                $("#show-duan").show();
            }
        });
        $(".dypeizhi_edit").bind("click", function () {
            $(".bg").show();
            $(".dypeizhi_alert").show();
            lessoninstall.findAllFenDuan(2);
        });
        $(".dypeizhi_alert_close").bind("click", function () {
            $(".bg").hide();
            $(".dypeizhi_alert").hide();
        });
        $(".dypeizhi_add").bind("click", function () {
            lessoninstall.getLeftGradeClassList();
            $(".bg").show();
            $("#inpName").val("");
            $(".dypeizhi_alert2").show();
        });
        $(".dypeizhi_alert_close").bind("click", function () {
            $(".bg").hide();
            $(".dypeizhi_alert2").hide();
        });
        $(".tab-interest-setGroup").bind("click", function () {
            lessoninstall.findAllFenDuan(0);
            $(".dypeizhi_mian").show();
            $(".tab-interestClass").hide();
        });
        $(".dypeizhi_back").bind("click", function () {
            $(".dypeizhi_mian").hide();
            $(".tab-interestClass").show();
        });
    });
    var allClass = [];
    var choosedClassList = [];
    //获取本年级所有的班级
    lessoninstall.getGradeClass = function () {
        $.ajax(
            {
                url: "/timetable/getGradeClassList.do",
                type: "post",
                data: {
                    gradeId: $("#gradeShow").attr('gid')
                },
                success: function (data) {

                    allClass = [];
                    if (data.length > 0) {
                        var map = data[0].classInfo;
                        if (map != null) {
                            $.each(map, function (key, value) {
                                allClass.push({id: key, className: value});
                            });
                        }
                    }
                    //$("#classList").empty();
                    //Common.render({tmpl: $('#classTempJs'), data: {data: allClass}, context: '#classList'});
                }
            }
        );
    };
    lessoninstall.getGradeClass();
    //模拟自动选拓展课
    lessoninstall.autoSetInterestCourse = function () {
        var term = $("#team").text();
        var gradeId = $("#gradeShow").attr('gid');
        $.ajax({
            url: "/paike/autoSetInterestCourse.do",
            type: "post",
            data: {
                term: term,
                gradeId: gradeId
            },
            success: function (data) {
                if (data.result == "success") {
                    alert("模拟选课成功");
                }
                else {
                    alert(data.reason);
                }
            }
        })
    };
    //获取拓展课详情
    lessoninstall.findInterestClass = function (courseId) {
        $.ajax({
            url: "/paike/findInterestClass.do",
            type: "post",
            data: {
                courseId: courseId
            },
            success: function (data) {
                timeList = [];
                var timeListBase = data.pointEntrylist;
                for (var i = 0; i < timeListBase.length; i++) {
                    //timeList.push({x:timeListBase[i].x,y:timeListBase[i].y});
                    $(".zouban-I").val(timeListBase[i].x);
                    $(".zouban-II").val(timeListBase[i].y);
                }
                globalCourseId = data.zbCourseId;
                $("#courseName").val(data.courseName);
                $("#maxPeople").val(data.max);
                $("#subjectListShow").val(data.subjectId);
                /*$("#teacherShow").val(data.teacherId);*/
                $("#classroomShow").val(data.classRoomId);
                lessoninstall.findTeachersBySubjectId($("#subjectListShow").val(), data.teacherId);

                $(".zouban").show();
                $(".bg").show();
                if (data.groupId == "")// 全年级
                {
                    $("#allClass").prop("checked", true);
                    $("#show-duan").hide();
                }
                else {//
                    $("#allClass").prop("checked", false);
                    $("#show-duan").show();
                    $('input:radio[name=classInp]').each(function (i) {

                        if (data.groupId == $(this).val()) {
                            $(this).prop("checked", true);
                        }

                    });
                }
                /*$(".border").empty();
                 Common.render({tmpl: $('#timeTempJs'), data: {data:timeList}, context: '.border'});*/
            }
        })
    };
    //获取拓展课列表
    lessoninstall.findInterestClassList = function () {
        var term = $("#team").text();
        var gradeId = $("#gradeShow").attr('gid');
        $.ajax({
            url: "/paike/findInterestClassList.do",
            type: "post",
            data: {
                term: term,
                gradeId: gradeId
            },
            success: function (data) {
                $("#interestCourseShow").empty();
                Common.render({tmpl: $('#interestClassJs'), data: {data: data}, context: '#interestCourseShow'});
            }
        })
    };
    //新增拓展课
    lessoninstall.addInterestClass = function (zoubanCourseDTO) {
        $.ajax({
            url: "/paike/addInterestCourse.do",
            type: "post",
            contentType: 'application/json',
            data: JSON.stringify(zoubanCourseDTO),
            success: function (data) {
                if (data.code == "200") {
                    lessoninstall.findInterestClassList();
                    $(".border").empty();
                    timeList = [];
                    $("#courseName").val("");
                    $("#maxPeople").val("");
                    $(".zouban").hide();
                    $(".bg").hide();
                }
                else {
                    alert("添加失败");
                }
            }
        })
    };
    //修改拓展课
    lessoninstall.updateInterestClass = function (zoubanCourseDTO) {
        $.ajax({
            url: "/paike/updateInterestCourse.do",
            type: "post",
            contentType: 'application/json',
            data: JSON.stringify(zoubanCourseDTO),
            success: function (data) {
                if (data.code == "200") {
                    lessoninstall.findInterestClassList();
                    $(".border").empty();
                    timeList = [];
                    $("#courseName").val("");
                    $("#maxPeople").val("");
                    $(".zouban").hide();
                    $(".bg").hide();
                }
                else {
                    alert("修改失败");
                }
            }
        })
    };
    //删除拓展课
    lessoninstall.deleteInterestClass = function (courseId) {
        $.ajax({
            url: "/paike/deleteInterestCourse.do",
            type: "post",
            dataType: 'json',
            data: {courseId: courseId},
            success: function (data) {
                if (data.code == "200") {
                    lessoninstall.findInterestClassList();
                }
                else {
                    alert("删除失败");
                }
            }
        })
    };
    //获取教室列表
    lessoninstall.findClassroomList = function () {
        Common.getPostData('/paike/findAvailableRoom.do', {
                year: $("#team").text(),
                gradeId: $("#gradeShow").attr('gid'),
                groupId: null
            },
            function (rep) {
                $("#classroomShow").empty();
                Common.render({tmpl: $('#classroomTempJs'), data: {data: rep}, context: '#classroomShow'});
            });
    };
    //获取老师列表
    lessoninstall.findTeachersBySubjectId = function (subjectId, teacherId) {
        $.ajax({
            url: "/paike/findTeachersBySubjectAndGrade.do",
            type: "post",
            dataType: "json",
            data: {
                subjectId: subjectId,
                gradeId: $("#gradeShow").attr('gid')
            },
            success: function (data) {
                $("#teacherShow").empty();
                Common.render({tmpl: $('#teacherTempJs'), data: {data: data}, context: '#teacherShow'});
                if (teacherId != "") {
                    $("#teacherShow").val(teacherId);
                }
            }
        });
    };
    var allFenDuan = [];
    /**
     * 获取本年级所有分段
     * @param index 0分段列表页用，1添加拓展课时用，2编辑分段页面使用
     */
    lessoninstall.findAllFenDuan = function (index) {
        $.ajax({
            url: "/paike/findFenDuanList.do",
            type: "post",
            dataType: "json",
            data: {
                year: $("#team").text(),
                gradeId: $("#gradeShow").attr('gid')
            },
            success: function (data) {
                //if(index==1) {
                $("#duanList").empty();
                Common.render({tmpl: $('#duanTempJs'), data: {data: data}, context: '#duanList'});
                //}
                //else if(index==0){
                $("#duanTableShow").empty();
                Common.render({tmpl: $('#duanTableJs'), data: {data: data}, context: '#duanTableShow'});
                //}
                //else if(index==2)
                //{
                allFenDuan = data;
                $("#editFenDuanshow").empty();
                Common.render({
                    tmpl: $('#editFenDuanJs'),
                    data: {data: data, class: allClass},
                    context: '#editFenDuanshow'
                });
                // }
            }
        });
    };
    lessoninstall.findAllFenDuan(1);
    //==============================单元管理==============================================================
    var duanList = [];
    $(document).ready(function () {
        //添加段
        $("#addSave").click(function () {
            if ($("#inpName").val() == "") {
                alert("单元名称不可为空");
                return;
            }
            duan.classIds = [];
            $("input[name=classDuanInp]:checked").each(function () {
                duan.classIds.push($(this).val());
            });
            if (duan.classIds.length == 0) {
                alert("未选择班级");
                return;
            }
            duan.fenDuanIndex = 0;
            duan.fenDuanName = $("#inpName").val();
            duan.classNames = [];
            duan.fenDuanId = "";
            lessoninstall.addFenDuan(duan);
        });
        //取消添加
        $("#addCancel").click(function () {
            $(".dypeizhi_alert2").hide();
            $(".bg").hide();
        });
        //删除段
        $("body").on("click", ".deleteDuan", function () {
            if (confirm("您确定删除该段吗？删除后不可恢复")) {
                var duanId = $(this).attr("did");
                lessoninstall.deleteFenDuan(duanId);
            }

        });
        //取消修改
        $("#editCancel").click(function () {
            $(".dypeizhi_alert").hide();
            $(".bg").hide();
        });
        //编辑分段
        $("body").on("change", ".editClassCheck", function () {
            var duanId = $(this).attr("did");
            var id = $(this).val();
            if ($(this).prop("checked") == true) {
                //取消其他段内的勾选
                $(".editClassCheck").each(function () {
                    var did = $(this).attr("did");
                    var cid = $(this).val();
                    if (id == cid && did != duanId) {
                        $(this).prop("checked", false);
                    }
                })
            }
        });
        //保存编辑
        $("body").on("click", "#editSave", function () {
            duanList = [];
            $(".editDuanName").each(function () {
                var duanId = $(this).attr("did");
                var duanName = $(this).val();
                duanList.push({
                    "fenDuanId": duanId, "xuankeId": duan.xuankeId,
                    "fenDuanIndex": 0, "fenDuanName": duanName, "classIds": [], "classNames": []
                });
            });
            $(".editClassCheck").each(function () {
                var duanId = $(this).attr("did");
                if ($(this).prop("checked") == true) {
                    var cid = $(this).val();
                    for (var i = 0; i < duanList.length; i++) {
                        if (duanList[i].fenDuanId == duanId) {
                            duanList[i].classIds.push(cid);
                        }
                    }
                }
            });
            //console.log(duanList)
            lessoninstall.updateFenDuans();
        });
    });

    //编辑段
    lessoninstall.updateFenDuans = function () {
        $.ajax({
            url: "/paike/updateFenDuan.do",
            type: "post",
            contentType: 'application/json',
            data: JSON.stringify(duanList),
            success: function (data) {
                if (data.code == "200") {
                    $(".dypeizhi_alert").hide();
                    $(".bg").hide();
                    lessoninstall.findAllFenDuan(0);
                }
                else {
                    alert("添加失败");
                }
            }
        });
    };
    //添加段
    lessoninstall.addFenDuan = function (duan) {
        $.ajax({
            url: "/paike/addFenDuan.do",
            type: "post",
            contentType: 'application/json',
            data: JSON.stringify(duan),
            success: function (data) {
                if (data.code == "200") {
                    $(".dypeizhi_alert2").hide();
                    $(".bg").hide();
                    lessoninstall.findAllFenDuan(0);
                }
                else {
                    alert("添加失败");
                }
            }
        })
    };
    //删除段
    lessoninstall.deleteFenDuan = function (duanId) {
        $.ajax({
            url: "/paike/deleteFendDuan.do",
            type: "post",
            dataType: "json",
            data: {
                fenDuanId: duanId
            },
            success: function (data) {
                if (data.code == "200") {
                    lessoninstall.findAllFenDuan(0);
                }
                else {
                    alert("删除失败");
                }
            }
        })
    };
    //获取本年级下未被编段的班级
    lessoninstall.getLeftGradeClassList = function () {
        $.ajax({
            url: "/paike/getLeftGradeClassList.do",
            type: "post",
            dataType: "json",
            data: {
                year: $("#team").text(),
                gradeId: $("#gradeShow").attr('gid')
            },
            success: function (data) {
                $("#classList").empty();
                Common.render({tmpl: $('#classTempJs'), data: {data: data}, context: '#classList'});
            }
        })
    };


    //---------------------------------------------------体育课设置------------------------------------------------------
    $('body').on('click','#addPE',function(){//新增
        $(".bg").show();
        $(".sports-popup").show();
    });
    $('body').on('click','.sports-BJ',function(){//编辑
        var tr = $(this).parents('tr');
        $('#courseName').val(tr.attr('groupClassName'));
        $('input[name="classId"]').each(function(){
            if(tr.attr('adminClassId').indexOf($(this).val()) >= 0){
                //$(this).select();
                $(this).attr('checked',true);
            }
        });
        $('#teacherMCtx').val(tr.attr('mTeacherId'));
        $('#teacherFCtx').val(tr.attr('fTeacherId'));

        $(".sports-popup").attr("fClassId",tr.attr("fClassId"));
        $(".sports-popup").attr("mClassId",tr.attr("mClassId"));
        $(".bg").show();
        $(".sports-popup").show();
    });
    $('body').on('click','.delete',function(){//删除
        if(confirm("确定要删除吗？")){
            var mClassId = $(this).parents("tr").attr("mClassId");
            var fClassId = $(this).parents("tr").attr("fClassId");
            deletePECourse(mClassId,fClassId);
        } else {
            return;
        }
    });
    $('body').on('click','.PEClose',function(){//关闭
        //清空体育课信息
        $('#courseName').val('');
        $('input[name="classId"]').attr("checked",false);
        $('#teacherMCtx option:selected').attr('selected',false);
        $('#teacherFCtx option:selected').attr('selected',false);


        $(".bg").hide();
        $(".sports-popup").hide();
    });
    $('body').on('click','.popup-TT',function(){//提交
        $(".bg").hide();
        $(".sports-popup").hide();
        if($(".sports-popup").attr("mClassId")){//更新
            updatePE();
        } else {//新增
            addPE();
        }
    });


    function getClassList(){//班级列表
        var url = "/paike/getClassList.do";
        var param = {};
        param.gradeId = $("#PEGrade").attr("gid");
        Common.getData(url,param,function(resp){
            if(resp){
                var classList = resp.classList;

                Common.render({
                    tmpl:'#classListTmpl',
                    data:classList,
                    context:'#classListCtx'
                });
            }
        });
    }

    function findPETeacher(){//体育老师列表
        var url = "/paike/findPETeacher.do";
        var param = {};
        param.gradeId = $("#PEGrade").attr("gid");
        Common.getData(url,param,function(resp){
            if(resp){
                var teacherList = resp.teacherList;

                Common.render({
                    tmpl:'#teacherTmpl',
                    data:teacherList,
                    context:'#teacherMCtx'
                });
                Common.render({
                    tmpl:'#teacherTmpl',
                    data:teacherList,
                    context:'#teacherFCtx'
                });
            }
        });
    }

    function addPE(){//新增体育课
        var classIds = "";
        $("input:checkbox[name='classId']:checked").each(function(){
            classIds += $(this).val() + ",";
        });

        if(classIds.length == 0){
            alert("请选择班级");
            return;
        }

        var url = "/paike/addPECourse.do";
        var param = {};
        param.term = $("#term").text();
        param.gradeId = $("#PEGrade").attr("gid");
        param.className = $("#courseName").val();
        param.lessonCount = $("#lessonCount").val();
        param.classIds = classIds;
        param.teacherMId = $("#teacherMCtx").val();
        param.teacherMName = $("#teacherMCtx option:selected").text();
        param.teacherFId = $("#teacherFCtx").val();
        param.teacherFName = $("#teacherFCtx option:selected").text();
        Common.getPostData(url,param,function(resp){
            if(resp.code == "200"){
                getPEList();
            } else {
                alert(resp.message);
            }
        });
    }

    function updatePE(){//更新体育课
        var classIds = "";
        $("input:checkbox[name='classId']:checked").each(function(){
            classIds += $(this).val() + ",";
        });

        if(classIds.length == 0){
            alert("请选择班级");
            return;
        }

        var url = "/paike/updatePECourse.do";
        var param = {};
        param.mClassId = $(".sports-popup").attr("mClassId");
        param.fClassId = $(".sports-popup").attr("fClassId");
        param.className = $("#courseName").val();
        param.lessonCount = $("#lessonCount").val();
        param.adminClassId = classIds;
        param.mTeacherId = $("#teacherMCtx").val();
        param.mTeacherName = $("#teacherMCtx option:selected").text();
        param.fTeacherId = $("#teacherFCtx").val();
        param.fTeacherName = $("#teacherFCtx option:selected").text();
        Common.getPostData(url,param,function(resp){
            if(resp.code == "200"){
                getPEList();
            } else {
                alert(resp.message);
            }
        });
    }

    function getPEList(){//获取体育课
        var url = "/paike/getPECourseList.do";
        var param = {};
        param.term = $("#term").text();
        param.gradeId = $("#PEGrade").attr("gid");
        Common.getData(url,param,function(resp){
            if(resp){
                Common.render({
                    tmpl:'#PECourseListTmpl',
                    data:resp.courseList,
                    context:'#PECourseListCtx',
                    overwrite:1
                });
            }
        });
    }

    function deletePECourse(mClassId,fClassId){//删除体育课
        var url = "/paike/deletePECourse.do";
        var param = {};
        param.mClassId = mClassId;
        param.fClassId = fClassId;
        Common.getPostData(url,param,function(resp){
            if(resp.code == "200"){
                getPEList();
            } else {
                alert(resp.message);
            }
        });
    }





});