/**
 * Created by qiangm on 2015/10/20.
 */
'use strict';
define(['doT', 'common', 'jquery'],function (require, exports, module) {
    /**
     *初始化参数
     */
    require("jquery");
    require("doT");
    var classrule = {},
        Common = require('common');
    var classruleData = {};
    classruleData.type = 1;
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    classrule.init = function () {


        $(".right-main1").hide();
        $(".update-X").click(function(){
            $(".bg").hide();
            $(".gezhiConf").hide();
            $(".adjust-CUR").hide();
            $(".section-CUR").hide();
            $(".ranking-CUR").hide();
            $(".Sschedule-CUR").hide();
            $(".batch-CUR").hide();
            $(".tishi-CUR").hide();
        });
        $(".backUrl").click(
            function () {
                window.open('../paike/index.do?version=58&year='+ encodeURI(encodeURI($('#termShow').text()))+'&gradeId='+
                $('#gradeShow').attr("gid"),'_self');
            });

        classrule.getgroupSubjectSelect();

    };



    classrule.getgroupSubjectSelect = function() {
        classruleData.term = $("#termShow").text();
        classruleData.gradeId = $("#gradeShow").attr('gid');
        classruleData.type=2;
        Common.getPostData('/bianban/getgroupSubjectSelect.do', classruleData,function(rep){
            $('#xuankeid').val(rep.xuankeId);
            /*$("#fenduanlist2").empty();
            Common.render({tmpl: $('#fenduanlistTempJs'), data: {data: rep.classFendDuanList}, context: '#fenduanlist2'});
            $("#subjectlist").empty();
            Common.render({tmpl: $('#subjectlistTempJs'), data: {data: rep.subjectlist}, context: '#subjectlist'});*/

            //设置老师
            $("#fenduanlist2-tea").empty();
            Common.render({tmpl: $('#fenduanlistTempJs'), data: {data: rep.classFendDuanList}, context: '#fenduanlist2-tea'});
            $("#subjectlist-tea").empty();
            Common.render({tmpl: $('#subjectlistTempJs'), data: {data: rep.subjectlist}, context: '#subjectlist-tea'});

           /* //设置学生
            $("#fenduanlist2-stu").empty();
            Common.render({tmpl: $('#fenduanlistTempJs'), data: {data: rep.classFendDuanList}, context: '#fenduanlist2-stu'});
            $("#subjectlist-stu").empty();
            Common.render({tmpl: $('#subjectlistTempJs'), data: {data: rep.subjectlist}, context: '#subjectlist-stu'});

            //设置教室
            $("#fenduanlist2-room").empty();
            Common.render({tmpl: $('#fenduanlistTempJs'), data: {data: rep.classFendDuanList}, context: '#fenduanlist2-room'});
            $("#subjectlist-room").empty();
            Common.render({tmpl: $('#subjectlistTempJs'), data: {data: rep.subjectlist}, context: '#subjectlist-room'});
            classrule.findBianBanList();*/
        });
    };


    classrule.init();


    //==================================================================================================
    $(document).ready(function(){
        $("#gezhiBianBan").click(function(){
            classrule.getGezhiConf();
            $(".gezhiConf").show();
            $(".bg").show();
        });
        $(".curri-TJ1").click(function(){
            $(".gezhiConf").hide();
            $(".bg").hide();
            classrule.setGezhiConf();
        });
        $(".curri-QX1").click(function(){
            $(".gezhiConf").hide();
            $(".bg").hide();
        });
        $("body").on("change",".adv",function(){
            var groupId=$(this).attr("gid");
            var subjectId=$(this).attr("sid");
            var count=$(this).val();
            if(isNaN(count))
            {
                $(this).val("");
                return;
            }
            count=Number(count);
            for(var i=0;i<gezhiConfValue.gezhiConfList.length;i++)
            {
                if(gezhiConfValue.gezhiConfList[i].groupId==groupId && gezhiConfValue.gezhiConfList[i].subjectId==subjectId)
                {
                    gezhiConfValue.gezhiConfList[i].advCount=count;
                    return;
                }
            }
        });
        $("body").on("change",".sim",function(){
            var groupId=$(this).attr("gid");
            var subjectId=$(this).attr("sid");
            var count=$(this).val();
            if(isNaN(count))
            {
                $(this).val("");
                return;
            }
            count=Number(count);
            for(var i=0;i<gezhiConfValue.gezhiConfList.length;i++)
            {
                if(gezhiConfValue.gezhiConfList[i].groupId==groupId && gezhiConfValue.gezhiConfList[i].subjectId==subjectId)
                {
                    gezhiConfValue.gezhiConfList[i].simCount=count;
                    return;
                }
            }
        });
    });
    //==================================================================格致中学补充========================================
    $(document).ready(function(){
        $(".tab-rules-V").click(function(){
            if(classruleState>=3) {
                $(this).addClass('rules-LI');
                $(this).removeClass('rules-LII');
                $(".tab-rules-LI").addClass('rules-LII');
                $(".tab-rules-LIII").addClass('rules-LII');
                $(".tab-rules-LIIII").addClass('rules-LII');
                $(".tab-rules-VII").addClass('rules-LII');
                $(".tab-rules-VI").addClass('rules-LII');
                $(".tab-li-I").hide();
                $(".tab-li-II").hide();
                $(".tab-li-III").hide();
                $(".sfd-dd").hide();
                $(".tab-DT-I").hide();
                $(".tab-DT-II").show();
                $(".fdh-tab").hide();
                $(".right-main1").hide();
                $(".right-main-tea").show();
                $(".right-main-stu").hide();
                $(".right-main-room").hide();
                classrule.getGezhiCourseList($("#fenduanlist2-tea").val(), $("#subjectlist-tea").val(), $("#subject-type").val(), 0);
            }
        });
        $(".tab-rules-VI").click(function(){
            if(classruleState>=4) {
                $(this).addClass('rules-LI');
                $(this).removeClass('rules-LII');
                $(".tab-rules-LI").addClass('rules-LII');
                $(".tab-rules-LIII").addClass('rules-LII');
                $(".tab-rules-LIIII").addClass('rules-LII');
                $(".tab-rules-VII").addClass('rules-LII');
                $(".tab-rules-V").addClass('rules-LII');
                $(".tab-li-I").hide();
                $(".tab-li-II").hide();
                $(".tab-li-III").hide();
                $(".sfd-dd").hide();
                $(".tab-DT-I").hide();
                $(".tab-DT-II").show();
                $(".fdh-tab").hide();
                $(".right-main1").hide();
                $(".right-main-tea").hide();
                $(".right-main-stu").show();
                $(".right-main-room").hide();
                classrule.getGezhiCourseList($("#fenduanlist2-stu").val(), $("#subjectlist-stu").val(), 1, 1);
            }
        });
        $(".tab-rules-VII").click(function(){
            if(classruleState>=5) {
                $(this).addClass('rules-LI');
                $(this).removeClass('rules-LII');
                $(".tab-rules-LI").addClass('rules-LII');
                $(".tab-rules-LIII").addClass('rules-LII');
                $(".tab-rules-LIIII").addClass('rules-LII');
                $(".tab-rules-VI").addClass('rules-LII');
                $(".tab-rules-V").addClass('rules-LII');
                $(".tab-li-I").hide();
                $(".tab-li-II").hide();
                $(".tab-li-III").hide();
                $(".sfd-dd").hide();
                $(".tab-DT-I").hide();
                $(".tab-DT-II").show();
                $(".fdh-tab").hide();
                $(".right-main1").hide();
                $(".right-main-tea").hide();
                $(".right-main-stu").hide();
                $(".right-main-room").show();
                classrule.getGezhiCourseList($("#fenduanlist2-room").val(), $("#subjectlist-room").val(), 1, 2);
            }
        });

       /* $('body').on('change', '#fenduanlist2-tea', function () {
            var subjectId = $("#subjectlist-tea").val();
            if ($("#subject-type").val() == 2) {
                subjectId = -1;
            }
            classrule.getGezhiCourseList($("#fenduanlist2-tea").val(), subjectId, $("#subject-type").val(), 0);
        });*/
        $('body').on('change', '#subjectlist-tea', function () {
            var subjectId = $("#subjectlist-tea").val();
            if ($("#subject-type").val() == 2) {
                subjectId = -1;
            }
            classrule.getGezhiCourseList(-1, subjectId, 2, 0);
        });

        //自动设置老师
        $("#autoSetTea").click(function(){
            classrule.autoSetTeacher(2);
        });

        $("#main1-2btn-stu").click(function(){
            classrule.studentBianban();
        });

        $("#autoSetClassroom").click(function(){
            classrule.autoSetClassroom();
        });
        //设置老师
        $("body").on("click",".edit-set-tea",function(){
            $(".edit-set-div-tea").show();

            $('#term2').html($('#termShow').text());
            $('#grade2-tea').html($('#gradeShow').text());
            $('#snm').html($(this).attr('snm'));
            if ($(this).attr('cn').indexOf('等级考')!=-1) {
                $('#ktp').html('等级考');
            } else {
                $('#ktp').html('合格考');
            }
            $('#coursenm').html($(this).attr('cn'));
            if($("#subject-type").val()==1)
                $('#duan-tea').html('第'+$(this).attr('gp')+'段');
            else {
                $('#duan-tea').html('');
                $('#ktp').html('');
            }
            var subjectId = $(this).attr('sid');
            Common.getPostData('/paike/findTeachersBySubjectAndGrade.do', {subjectId:subjectId,gradeId:$("#gradeShow").attr('gid')},function(rep){
                $("#teacherlist").empty();
                Common.render({tmpl: $('#teacherlistTempJs'), data: {data: rep}, context: '#teacherlist'});
            });
            if ($(this).attr('tid')!='null') {
                $('#teacherlist').val($(this).attr('tid'));
            }
            $("#courseId-set").val($(this).attr("couid"));
            $("#roomId-set").val($(this).attr("crid"));
            $("#weekCnt-set").val($(this).attr("cnt"));
            $("#classId-set").val($(this).attr("cid"));
            $(".bg").show();
        });
        //关闭设置弹窗
        $(".canc-btn,.setwind-cl").click(
            function () {
                $(".edit-set-div-tea").hide();
                $(".edit-set-div-room").hide();
                $(".bg").hide();
            });
        //保存设置老师
        $("#tea-cofi-btn").click(function(){
            var type=Number($("#subject-type").val());
            classrule.saveTeacher(type);
        });
        //设置教室
        $('body').on('click',".edit-set-room",function()
        {
            $(".edit-set-div-room").show();

            $('#term2-room').html($('#termShow').text());
            $('#grade2-room').html($('#gradeShow').text());
            $('#snm-room').html($(this).attr('snm'));
            if ($(this).attr('cn').indexOf('等级考')!=-1) {
                $('#ktp-room').html('等级考');
            } else if ($(this).attr('cn').indexOf('合格考')!=-1){
                $('#ktp-room').html('合格考');
            }
            $('#coursenm-room').html($(this).attr('cn'));
            $('#duan-room').html('第'+$(this).attr('gp')+'段');
            var subjectId = $(this).attr('sid');
            Common.getPostData('/paike/findAvailableRoom.do', {
                year:$("#termShow").text(),
                gradeId:$("#gradeShow").attr('gid'),
                groupId:$(this).attr("gid")},
                function(rep){
                $("#classroomlist").empty();
                Common.render({tmpl: $('#classroomlistTempJs'), data: {data: rep}, context: '#classroomlist'});
            });
            if ($(this).attr('crid')!='null') {
                $('#classroomlist').val($(this).attr('crid'));
            }
            $("#courseId-set-room").val($(this).attr("couid"));
            $("#teacherId-set-room").val($(this).attr("tid"));
            $("#teacherName-set-room").val($(this).attr("tnm"));
            $("#weekCnt-set-room").val("1");
            $("#classId-set-room").val("1");
            $(".bg").show();
        });
        //保存教室
        $("#room-cofi-btn").click(function(){
            classrule.saveClassRoom();
        });
        //查看学生名单
        $("body").on("click",".tab-check",function(){
            $('#term6').html($('#termShow').text());
            $('#grade6').html($('#gradeShow').text());
            $(".tab-col").hide();
            $(".right-main2").show();
            classruleData.courseClassId = $(this).attr('couid');
            classrule.findClassStudentList(1);
        });
    });
    var gezhiConfValue={};
    var type=0;

    //保存设置老师
    classrule.saveTeacher = function(type)
    {
        Common.getPostData('/bianban/updateClassCourseInfo.do', {
            courseClassId:$("#courseId-set").val(),
            teacherId:$('#teacherlist').val(),
            classRoomId:$("#roomId-set").val()=="null"?"":$("#roomId-set").val(),
            teacherName:$('#teacherlist').find("option:selected").text(),
            weekcnt:$("#weekCnt-set").val(),
            type:2,
            classId:$("#classId-set").val()
        },function(rep){
            if(rep.flg) {
                $(".edit-set-div-tea").hide();
                $(".bg").hide();
                var subjectId = $("#subjectlist-tea").val();
                if ($("#subject-type").val() == 2) {
                    subjectId = -1;
                }
                classrule.checkTeacherFinish(type);
                classrule.getGezhiCourseList(-1, subjectId,2, 0);
            }
        });
    };
    //保存教室
    classrule.saveClassRoom = function()
    {
        Common.getPostData('/bianban/updateClassCourseInfo.do', {
            courseClassId:$("#courseId-set-room").val(),
            teacherId:$('#teacherId-set-room').val(),
            classRoomId:$('#classroomlist').val(),
            teacherName:$('#teacherName-set-room').val(),
            weekcnt:1,
            type:1,
            classId:""
        },function(rep){
            if(rep.flg) {
                $(".edit-set-div-room").hide();
                $(".bg").hide();
                classrule.checkClassroomFinish();
                classrule.getGezhiCourseList($("#fenduanlist2-room").val(), $("#subjectlist-room").val(), 1, 2);
            }
        });
    };
    //格致编班
    classrule.getGezhiConf=function()
    {
        $.ajax({
            url:"/paike/bianbanConf.do",
            type:"post",
            dataType:"json",
            data:{
                year:$("#termShow").text(),
                gradeId:$("#gradeShow").attr('gid')
            },
            success:function(data)
            {
                gezhiConfValue.gezhiConfList=data;
                $("#temp").empty();
                var mode=$("#mode").val();
                if(mode=="gezhiIndex")
                {
                    Common.render({tmpl: $('#tempConfJs'), data: {data:data}, context: '#temp'});
                }
                else if(mode=="changZhengIndex")
                {
                    Common.render({tmpl: $('#tempConfCZJs'), data: {data:data}, context: '#temp'});
                }

            }
        })
    };
    //自动设置教室
    classrule.autoSetClassroom=function()
    {
        $.ajax({
            url:"/paike/setGezhiClassroom.do",
            type:"post",
            dataType:"json",
            data:{
                year:$("#termShow").text(),
                gradeId:$("#gradeShow").attr("gid")
            },
            success:function(data)
            {
                if(data.result!="200")
                {
                    alert("自动设置教室失败，可能原因：教室数目不足,请在首页教室管理中添加");
                }
                else
                {
                    classrule.getGezhiCourseList($("#fenduanlist2-room").val(), $("#subjectlist-room").val(), 1, 2);
                    classrule.checkClassroomFinish();
                }
            }
        })
    };
    var classruleState=Number($("#state").val());//记录步骤
    //学生编班
    classrule.studentBianban=function()
    {
        $(".bianbanTip").show();
        $(".bg").show();
        $.ajax({
            url:"/paike/studentBianban.do",
            type:"post",
            dataType:"json",
            data:{
                year:$("#termShow").text(),
                gradeId:$("#gradeShow").attr("gid")
            },
            success:function(data)
            {
                if(data.result==200)
                {
                    classruleState=5;
                }
                else
                {
                    alert("编排失败，请调整班级组合或学生选课");
                }
                $(".bianbanTip").hide();
                $(".bg").hide();
                classrule.getGezhiCourseList($("#fenduanlist2-stu").val(), $("#subjectlist-stu").val(), 1, 1);

            }
        })
    };
    classrule.getGezhiCourseList=function(groupId,subjectId,type,index)
    {
        $.ajax({
            url:"/bianban/findBianBanList.do",
            type:"post",
            data:{
                term:$("#termShow").text(),
                gradeId:$("#gradeShow").attr('gid'),
                groupId:groupId,
                subjectId:subjectId,
                type:type
            },
            dataType:"json",
            success:function(rep)
            {
                if(index==0) {
                    if (type == 1) {
                        $("#courseclslist-tea").empty();
                        Common.render({
                            tmpl: $('#courseclslistTempJs-tea'),
                            data: {data: rep.rows},
                            context: '#courseclslist-tea'
                        });
                    }
                    else if (type == 2) {
                        $("#courseclslist-tea").empty();
                        Common.render({
                            tmpl: $('#feizoubanlistTempJs'),
                            data: {data: rep.rows},
                            context: '#courseclslist-tea'
                        });
                    }
                }
                else if(index==1)
                {
                    $("#courseclslist-stu").empty();
                    Common.render({
                        tmpl: $('#courseclslistTempJs-stu'),
                        data: {data: rep.rows},
                        context: '#courseclslist-stu'
                    });
                }
                else if(index==2)
                {
                    $("#courseclslist-room").empty();
                    Common.render({
                        tmpl: $('#courseclslistTempJs-room'),
                        data: {data: rep.rows},
                        context: '#courseclslist-room'
                    });
                }

            }
        })
    };
    //自动设置老师
    classrule.autoSetTeacher=function(type)
    {
        $.ajax({
            url:"/paike/autoSetTeacher.do",
            type:"post",
            dataType:"json",
            data:{
                year:$("#termShow").text(),
                gradeId:$("#gradeShow").attr("gid"),
                type:type
            },
            success:function(data)
            {
                var subjectId = $("#subjectlist-tea").val();
                if ($("#subject-type").val() == 2) {
                    subjectId = -1;
                }
                classrule.getGezhiCourseList(-1, subjectId, 2, 0);
                //classrule.checkTeacherFinish(type);
            }
        })
    };
    //设置老师进度值
    classrule.checkTeacherFinish=function(type)
    {
        $.ajax({
            url:"/paike/setTeacherState.do",
            type:"post",
            data:{
                year:$("#termShow").text(),
                gradeId:$("#gradeShow").attr("gid"),
                type:2
            },
            success:function(data)
            {
                if(data=="finish")
                {
                    classruleState=4;
                }
            }
        })
    };
    //设置教室进度值
    classrule.checkClassroomFinish=function()
    {
        $.ajax({
            url:"/paike/setClassroomState.do",
            type:"post",
            data:{
                year:$("#termShow").text(),
                gradeId:$("#gradeShow").attr("gid")
            },
            success:function(data)
            {
                if(data=="finish")
                {
                    classruleState=6;
                }
            }
        })
    };
    classrule.setGezhiConf=function()
    {
        var advTotal=[];
        var groupIdList=[];
        for(var i=0;i<gezhiConfValue.gezhiConfList.length;i++)
        {
            if($.inArray(gezhiConfValue.gezhiConfList[i].groupId,groupIdList)==-1)
            {
                groupIdList.push(gezhiConfValue.gezhiConfList[i].groupId);
                advTotal.push({group:gezhiConfValue.gezhiConfList[i].groupId,advCount:0,simCount:0});
            }
        }

        for(var i=0;i<gezhiConfValue.gezhiConfList.length;i++)
        {
            for(var k=0;k<advTotal.length;k++)
            {
                if(advTotal[k].group==gezhiConfValue.gezhiConfList[i].groupId)
                {
                    advTotal[k].advCount=advTotal[k].advCount+gezhiConfValue.gezhiConfList[i].advCount;
                    advTotal[k].simCount=advTotal[k].simCount+gezhiConfValue.gezhiConfList[i].simCount;
                    if(advTotal[k].advCount>15)
                    {
                        alert(gezhiConfValue.gezhiConfList[i].groupName+"等级考编班总数超过12");
                        return;
                    }
                    else if(advTotal[k].simCount>15)
                    {
                        alert(gezhiConfValue.gezhiConfList[i].groupName+"合格考编班总数超过12");
                        return;
                    }
                }
            }
        }
        gezhiConfValue.year=$("#termShow").text();
        gezhiConfValue.gradeId=$("#gradeShow").attr('gid');
        var url="/paike/gezhiBianban.do";
        var mode=$("#mode").val();
        if(mode=="gezhiIndex")
        {
            url="/paike/gezhiBianban.do";
        }
        else if(mode=="changZhengIndex")
        {
            url="/paike/changZhengBianban.do";
        }
        $.ajax(
            {
                url:url,
                type:"post",
                contentType:"application/json",
                data: JSON.stringify(gezhiConfValue),
                success:function(data)
                {
                    if(data=="200")
                    {
                        classruleState=3;
                    }
                    classrule.getgroupSubjectSelect();
                    classrule.findBianBanList();
                }
            }
        )
    };
    classrule.getGezhiCourseList(-1, -1, 2, 0);
});