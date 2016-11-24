/*
 * @Author: Tony
 * @Date:   2015-10-14 10:27:31
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-10-15 10:16:38
 */

'use strict';

define(['doT', 'common', 'jquery'],function (require, exports, module) {
    /**
     *初始化参数
     */
    require("jquery");
    require("doT");
    var bianban = {},
        Common = require('common');
    var bianbanData = {};
    bianbanData.type = 1;
    bianbanData.weekcnt = 0;
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    bianban.init = function () {


        $(".backUrl").click(
            function () {
                window.open('../paike/index.do?version=58&year='+ encodeURI(encodeURI($('#termShow').text()))+'&gradeId='+
                $('#gradeShow').attr("gid"),'_self');
            });
        $(".editcl").click(
            function () {
                $(".right-main1").show();
                $(".right-main2").hide();
                $(".right-main3").hide();
                $(".right-main4").hide();
                bianbanData.type = 1;
                bianban.getgroupSubjectSelect();
            });
        bianban.getgroupSubjectSelect();
        $(".canc-btn").click(
            function () {
                $(".edit-set-div").hide();
            });
        $(".setwind-cl").click(
            function () {
                $(".edit-set-div").hide();
                $(".disclass-set-div").hide();
            });
        $(".back-btn").click(
            function () {
                $(".right-main1").show();
                $(".right-main2").hide();
                $(".right-main3").hide();
                $(".right-main4").hide();
            }
        );
        $(".disclass-canc").click(
            function () {
                $(".disclass-set-div").hide();
            });
        $(".notset").click(
            function () {
                $(".right-main1").hide();
                $(".right-main2").hide();
                $(".right-main3").hide();
                $(".right-main4").show();
                bianbanData.type = 2;
                bianban.getgroupSubjectSelect();
            });
        $("#select").click(
            function () {
                bianban.findBianBanList();
            });
        $("#autoSet").click(
            function () {
                bianban.autoSet();
            });
        $("#autoSetFZB").click(
            function () {
                bianban.autoSetFZB();
            });
        $("#autoBianBan").click(function(){
            bianban.addAutoBianBan();
        });
        $('.cofi-btn').click(function() {
            bianbanData.teacherId = $('#teacherlist').val();
            bianbanData.teacherName = $('#teacherlist').find("option:selected").text();
            bianbanData.classRoomId = $('#classroomlist').val();
            Common.getPostData('/bianban/updateClassCourseInfo.do', bianbanData,function(rep){
                if(rep.flg) {
                    $(".edit-set-div").hide();
                    bianban.findBianBanList();
                }
            });
        });
        $('.disclass-conf').click(function() {
            bianbanData.teacherId = $('#teacherlist2').val();
            bianbanData.teacherName = $('#teacherlist2').find("option:selected").text();
            bianbanData.weekcnt = $('#weekcnt').val();

            Common.getPostData('/bianban/updateClassCourseInfo.do', bianbanData,function(rep){
                if(rep.flg) {
                    $(".disclass-set-div").hide();
                    bianban.findBianBanList();
                }
            });
        });
        $(".main4-btn").click(function(){
            bianban.findBianBanList();
        });
        $('.select-all').click(function(){
            if ($('#userlist5  input:checkbox').is(':checked')) {
                $('#userlist5 input:checkbox').prop('checked', false);
            } else {
                $('#userlist5 input:checkbox').prop('checked', true);
            }
        });
        var users = [];
        $('.ex-arrow1').click(function(){
            users = [];
            bianbanData.courseClassId = $('#classlist5').val();
            bianbanData.index = 0;
            $(".main3-sel1 input[name='checkbox']:checked").each(function () {
                users.push(this.value);
            });
            bianbanData.studentArgs = users;
            bianban.updateClassCourseUserInfo(1);
        });
        $('.ex-arrow2').click(function(){
            users = [];
            bianbanData.courseClassId = $('#classlist5').val();
            bianbanData.index = 1;
            $(".main3-sel2 input[name='checkbox']:checked").each(function () {
                users.push(this.value);
            });
            bianbanData.studentArgs = users;
            bianban.updateClassCourseUserInfo(2);
        });

        //$('body').on('change', '#gradeShow', function () {
        //    bianban.findBianBanList();
        //});
        //$('body').on('change', '#termShow', function () {
        //    bianban.findBianBanList();
        //});
        //$('body').on('change', '#gradeShow2', function () {
        //    bianban.findBianBanList();
        //});
        //$('body').on('change', '#termShow2', function () {
        //    bianban.findBianBanList();
        //});
        $('#check').click(function(){
            window.location.href='/paike/clashCheck.do?version=58';
        });
    };

    $(function () {
        $(".right-title  li").click(function () {
            $(this).addClass("cont-style").siblings("li").removeClass("cont-style")
        });
    });

    bianban.getgroupSubjectSelect = function() {
        bianbanData.term = $('#termShow').text();
        bianbanData.gradeId = $("#gradeShow").attr('gid');
        Common.getPostData('/bianban/getgroupSubjectSelect.do', bianbanData,function(rep){
            $('#xuankeid').val(rep.xuankeId);
            if (bianbanData.type==1) {
                $("#fenduanlist").empty();
                Common.render({tmpl: $('#fenduanlistTempJs'), data: {data: rep.classFendDuanList}, context: '#fenduanlist'});
                $("#subjectlist").empty();
                Common.render({tmpl: $('#subjectlistTempJs'), data: {data: rep.subjectlist}, context: '#subjectlist'});
                //$("#fenduanlist3").empty();
                //Common.render({tmpl: $('#fenduanlistTempJs3'), data: {data: rep.classFendDuanList}, context: '#fenduanlist3'});
                //$("#subjectlist3").empty();
                //Common.render({tmpl: $('#subjectlistTempJs3'), data: {data: rep.subjectlist}, context: '#subjectlist3'});
            } else {
                $("#subjectlist2").empty();
                Common.render({tmpl: $('#subjectlistTempJs2'), data: {data: rep.subjectlist}, context: '#subjectlist2'});
            }
            bianban.findBianBanList();
        });
    };

    bianban.findBianBanList = function() {
        if (bianbanData.type==1) {
            bianbanData.term = $('#termShow').text();
            bianbanData.gradeId = $("#gradeShow").attr('gid');
            bianbanData.groupId = $("#fenduanlist").val();
            bianbanData.subjectId = $("#subjectlist").val();
        } else {
            bianbanData.term = $('#termShow2').text();
            bianbanData.gradeId = $("#gradeShow2").attr('gid');
            bianbanData.subjectId = $("#subjectlist2").val();
        }
        Common.getPostData('/bianban/findBianBanList.do', bianbanData,function(rep){
            if (bianbanData.type==1) {
                $("#courseclslist").empty();
                Common.render({tmpl: $('#courseclslistTempJs'), data: {data: rep.rows}, context: '#courseclslist'});
                $(".edit-set").click(
                    function () {
                        //bianban.findClassroomList();
                        $('#term2').html($('#termShow').text());
                        $('#grade2').html($('#gradeShow').text());
                        $('#snm').html($(this).attr('snm'));
                        if ($(this).attr('cn').indexOf('等级考')!=-1) {
                            $('#ktp').html('等级考');
                        } else {
                            $('#ktp').html('合格考');
                        }
                        bianbanData.courseClassId = $(this).attr('couid');
                        $('#coursenm').html($(this).attr('cn'));
                        $('#duan').html('第'+$(this).attr('gp')+'段');
                        bianbanData.subjectId = $(this).attr('sid');
                        bianban.getTeacherRoomList($(this).attr('sid'),$(this).attr('grpid'));
                        if ($(this).attr('tid')!='null') {
                            $('#teacherlist').val($(this).attr('tid'));
                        }
                        if ($(this).attr('crid')!='null') {
                            $('#classroomlist').val($(this).attr('crid'));
                        }
                        $(".edit-set-div").show();
                    });
                $(".tab-check").click(function () {
                        $('#term4').html($('#termShow').text());
                        $('#grade4').html($('#gradeShow').text());
                        $(".right-main1").hide();
                        $(".right-main3").hide();
                        $(".right-main4").hide();
                        $(".right-main2").show();
                        bianbanData.courseClassId = $(this).attr('couid');
                        bianban.findClassStudentList(1);
                    }
                );
                $(".peo-set").click(function () {
                        bianbanData.courseClassId = $(this).attr('couid');
                        bianban.findCourseById();
                        $('#term5').html($('#termShow').text());
                        $('#grade5').html($('#gradeShow').text());
                        $('#duan5').html('第'+$(this).attr('gp')+'段');
                        $('#cousename5').html($(this).attr('cn'));
                        bianbanData.orgcourseClassId = $(this).attr('couid');
                        $(".main3-sel2 input[name='checkbox']").each(function () {
                            $(this).parent().remove();
                        });
                        $(".right-main1").hide();
                        $(".right-main2").hide();
                        $(".right-main4").hide();
                        $(".right-main3").show();
                        bianban.findClassStudentList(2);
                        bianbanData.courseClassId = $('#classlist5').val();
                        bianban.findClassStudentList(3);
                    }
                );
            } else {
                $("#feizoubanlist").empty();
                Common.render({tmpl: $('#feizoubanlistTempJs'), data: {data: rep.rows}, context: '#feizoubanlist'});
                $(".disclass-set").click(
                    function () {
                        $('#term3').html($('#termShow2').text());
                        $('#grade3').html($('#gradeShow2').text());
                        $('#subject3').html($(this).attr('snm'));
                        $('#classname').html($(this).attr('cn'));
                        bianbanData.classId = $(this).attr("cid");
                        bianbanData.courseClassId = $(this).attr('couid');
                        bianbanData.subjectId = $(this).attr('sid');
                        Common.getPostData('/bianban/findTeacherBySubjectId.do', bianbanData,function(rep){
                            $("#teacherlist2").empty();
                            Common.render({tmpl: $('#teacherlistTempJs2'), data: {data: rep}, context: '#teacherlist2'});
                        });
                        $(".disclass-set-div").show();
                    });
            }

        });
    };

    bianban.findClassroomList = function() {
        Common.getPostData('/classroom/findClassroomList.do', bianbanData,function(rep){
            $("#classroomlist").empty();
            Common.render({tmpl: $('#classroomlistTempJs'), data: {data: rep}, context: '#classroomlist'});
        });
    };


    bianban.addAutoBianBan = function() {
        bianbanData.term = $('#termShow').text();
        bianbanData.gradeId = $("#gradeShow").attr('gid');
        bianbanData.type = 1;
        Common.getPostData('/bianban/addAutoBianBan.do', bianbanData,function(rep){
            if (rep.flg) {
                alert("自动编班成功！");
                bianban.findBianBanList();
            } else {
                alert("自动编班失败！");
            }

        });
    }
    bianban.findClassStudentList = function(type) {
        Common.getPostData('/bianban/findClassStudentList.do', bianbanData,function(rep){
            if (type==1) {
                $("#coursename").html(rep.rows.className);
                $("#teachername").html(rep.rows.teacherName);
                $("#classroom").html(rep.rows.classRoom);
                $("#usercnt").html(rep.rows.count);
                $("#userlist").empty();
                Common.render({tmpl: $('#userlistTempJs'), data: {data: rep.rows.studentCengJiDTOs}, context: '#userlist'});
            } else if(type==2) {
                $("#cnt5").html(rep.rows.count);
                $("#teachername5").html(rep.rows.teacherName);
                $("#classromm5").html(rep.rows.classRoom);
                $("#userlist5").empty();
                Common.render({tmpl: $('#userlistTempJs5'), data: {data: rep.rows.studentCengJiDTOs}, context: '#userlist5'});
            } else if (type==3) {
                $("#cnt6").html(rep.rows.count);
                $("#teachername6").html(rep.rows.teacherName);
                $("#classromm6").html(rep.rows.classRoom);
            }

        });
    };
    bianban.findCourseById = function() {
        Common.getPostData('/bianban/findCourseById.do', bianbanData,function(rep){
            if(rep!=null && rep.length!=0) {
                $("#classlist5").empty();
                Common.render({tmpl: $('#classlistTempJs5'), data: {data: rep}, context: '#classlist5'});
            } else {
                alert("只有一个班，不可调班！");
            }

        });
    };
    bianban.updateClassCourseUserInfo = function(type) {
        Common.getPostData('/bianban/updateClassCourseUserInfo.do', bianbanData,function(rep){
            if (rep.flg) {
                alert("换班成功！");
                if (type==1) {
                    $(".main3-sel1 input[name='checkbox']:checked").each(function () {
                        $(this).parent().remove();
                        $('#userlist6').append($(this).parent());
                        $('#userlist6 input:checkbox').prop('checked', false);
                    });
                } else {
                    $(".main3-sel2 input[name='checkbox']:checked").each(function () {
                        $(this).parent().remove();
                        $('#userlist5').append($(this).parent());
                        $('#userlist5 input:checkbox').prop('checked', false);
                    });
                }

            } else {
                alert("换班失败！");
            }
        });
    }
    bianban.autoSet=function()
    {
        $.ajax({
            url:"/paike/autoSetTeaRoomForJY.do",
            type:"post",
            dataType:"json",
            data:{
                year:$('#termShow').text(),
                gradeId:$("#gradeShow").attr('gid')
            },
            success:function(data)
            {
                if(data.result=="success") {
                    bianban.findBianBanList();
                }
                else{
                    alert("自动设置失败，失败原因："+data.reason);
                }
            }
        })
    };
    //非走班老师一键设置
    bianban.autoSetFZB=function()
    {
        $.ajax({
            url:"/paike/autoSetTeacher.do",
            type:"post",
            dataType:"json",
            data:{
                year:$('#termShow').text(),
                gradeId:$("#gradeShow").attr('gid'),
                type:2
            },
            success:function(data)
            {
                if(data.result=="200") {
                    bianban.findBianBanList();
                }
                else{
                    alert("自动设置失败");
                }
            }
        })
    };
    //走班获取老师教室列表
    bianban.getTeacherRoomList=function(subjectId,gradeId)
    {
        $.ajax({
            url:"/paike/getTeacherRoomForJY.do",
            type:"post",
            dataType:"json",
            data:{
                year:$('#termShow').text(),
                gradeId:$("#gradeShow").attr('gid'),
                subjectId:subjectId,
                groupId:gradeId
            },
            success:function(data)
            {
                $("#classroomlist").empty();
                Common.render({tmpl: $('#classroomlistTempJs'), data: {data: data.room}, context: '#classroomlist'});
                $("#teacherlist").empty();
                Common.render({tmpl: $('#teacherlistTempJs'), data: {data: data.teacher}, context: '#teacherlist'});
            }
        })
    }
    bianban.init();
});
