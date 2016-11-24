/**
 * Created by qiangm on 2015/8/20.
 */
'use strict';
define(['doT', 'common','initPaginator'],function (require, exports, module) {
    /**
     *初始化参数
     */
    require("doT");
    var Paginator = require('initPaginator');
    var manageLeave = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    manageLeave.init = function () {


    };
    $(document).ready(function()
    {
        $("body").on("click",".sh",function()
        {
            $(window).bind('beforeunload', function () {
                return '审核未完成，返回将撤销所有审核，确定离开此页面吗？';
            });
            $(".tqingjia_maincon").hide();
            $(".qjshenhe_maincon").show();
            $("#backOrReply").text("返回");
            var leaveId=$(this).attr("li");
            manageLeave.leaveId=leaveId;
            manageLeave.getLeaveDetail(leaveId);
            if(manageLeave.allCourse==0)
            {
                $("#agreeLeave").text("同意请假");
            }
            else{
                $("#agreeLeave").text("未代课"+manageLeave.allCourse+"节");
            }
            $(".ulShow").hide();
        });
        $("body").on("click","#backOrReply",function()
        {
            if($(this).text()=="我的请假")
            {
                //window.location="/leave/teacher.do?version=5h";
                window.open("/leave/teacher.do?index=5&version=16","_target");
            }
            else if($(this).text()=="返回")
            {
                if(confirm("审核未完成，返回将撤销所有审核，确定吗？")) {
                    manageLeave.removeReplace();
                    //$(".tqingjia_maincon").show();
                    $(window).unbind('beforeunload');
                    $(".qjshenhe_maincon").hide();
                    $(".qingjia").show();
                    $(".daike").hide();
                    $("#backOrReply").text("我的请假");
                    $(".ulShow").show();
                }
            }
        });
        //种类变化
        $("body").on("change",".tselect_time",function()
        {
            manageLeave.getAllLeaves(1,10);
        });
        //类型变化
        $("body").on("change",".tselect_type",function()
        {
            manageLeave.getAllLeaves(1,10);
        });
        //老师变化
        /*$("body").on("change",".teacherChoose",function()
        {
            manageLeave.getAllLeaves(1,10);
        });*/
        $(".teacherChooseInp").on("blur",function()
        {
            manageLeave.getAllLeaves(1,10);
        });

        //点击驳回
        $("body").on("click",".rejectLeave",function()
        {
            var leaveId=$(this).attr("li");
            manageLeave.rejectLeave(leaveId);
        });
        //星期变化
        $("body").on("change",".weekList",function()
        {
            var week=Number($(this).val());
            manageLeave.initTable(week);
            manageLeave.courseId ="";
            manageLeave.courseName ="";
            manageLeave.week = week;
            manageLeave.x =1;
            manageLeave.y =1;
        });
        //点击课表
        $("body").on("click",".qjshenhe_table_avtive",function()
        {
            $(".qjshenhe_table_avtive").removeClass("course_choosed");
            $('#teacherList').empty();
            var x=Number($(this).attr("x"));
            var y=Number($(this).attr("y"));
            var courseName=$(this).attr("cnm");
            var week=Number($(".weekList").val());
            manageLeave.courseId=$(this).attr("cid");
            manageLeave.courseName=courseName;
            manageLeave.x=x;
            manageLeave.y=y;
            manageLeave.week=week;
            manageLeave.getAvailableTeacher(week,x,y,courseName);
            $(this).addClass("course_choosed");
        });
        //确认代课
        $("body").on("click","#replaceCourse",function()
        {
            if(manageLeave.courseId && manageLeave.courseName && manageLeave.week && manageLeave.x && manageLeave.y) {
                if ($("#teacherList").val()) {
                    var teacherName=$("#teacherList").find("option:selected").text();
                    manageLeave.replaceLeave($("#teacherList").val(), manageLeave.oldTeacherId, manageLeave.courseId,
                        manageLeave.courseName, manageLeave.week, manageLeave.x, manageLeave.y,teacherName);
                }
                else {
                    alert("无可代课老师");
                }
            }
            //$(".qjshenhe_table_avtive").removeClass("course_choosed");

        });
        //同意请假
        $("body").on("click","#agreeLeave",function()
        {
            if(manageLeave.replaceList.length==manageLeave.allCourse) {
                var replaceIds = "";
                for (var item in manageLeave.replaceList) {
                    replaceIds += manageLeave.replaceList[item].replaceId + ",";
                }
                if (replaceIds.length > 0) {
                    replaceIds = replaceIds.substring(0, replaceIds.length - 1);
                }
                manageLeave.agreeLeave(replaceIds, manageLeave.leaveId);
            }
        });
        $("#qingjiaLi").click(function()
        {
            $(".qingjia").show();
            $(".daike").hide();
            $("#daikeLi").addClass("grqingjia_active");
            $("#qingjiaLi").removeClass("grqingjia_active");
            manageLeave.getAllLeaves(1,10);
        });
        $("#daikeLi").click(function()
        {
            $(".daike").show();
            $(".qingjia").hide();
            $("#qingjiaLi").addClass("grqingjia_active");
            $("#daikeLi").removeClass("grqingjia_active");
            manageLeave.getDaikeList(1,10);
        });
        $(".termList2").on("change",function()
        {
            manageLeave.getDaikeList(1,10);
        });
        /*$(".teacherChoose2").on("change",function()
        {
            manageLeave.getDaikeList(1,10);
        });*/
        $(".teacherChooseInp2").on("blur",function()
        {
            manageLeave.getDaikeList(1,10);
        });
    });
    manageLeave.getYearList = function () {
        $.ajax({
            url: "/zouban/common/getTermList.do",
            type: "get",
            dataType: "json",
            success: function (data) {
                data = data.termList;
                var termList=[];
                for(var i=0;i<data.length;i++)
                {
                    termList.push(data[i]+"第二学期");
                    termList.push(data[i]+"第一学期");
                }

                $(".termList2").empty();
                Common.render({tmpl: $('#termTempJs'), data: {data: termList}, context: '.termList2'});
            }
        });

    };
    manageLeave.getDaikeList=function(page,pageSize)
    {
        /*var teacherId=$(".teacherChoose2").val();*/

        var teacherId="";
        var name=$(".teacherChooseInp2").val();
        if(name=="")
        {
            teacherId="";
        }
        else{
            var userId="";
            for(var i=0;i<manageLeave.teacherList.rows.length;i++)
            {
                if(manageLeave.teacherList.rows[i].userName==name)
                {
                    userId=manageLeave.teacherList.rows[i].id;
                    break;
                }
            }
            if(userId=="")
            {
                $(".tqingjia_inf").hide();
                $(".noReplace").show();
                $(".page-paginator").hide();
                $(".noLeave").hide();
                return;
            }
            else{
                teacherId=userId;
            }
        }


        var term=$(".termList2").val();
        $.ajax({
            url:"/leave/getAllReplace.do",
            type:"post",
            dataType:"json",
            data:{
                teacherId:teacherId,
                term:term,
                page:page,
                pageSize:pageSize
            },
            success:function(data)
            {
                if(data.total==0)
                {
                    $(".tqingjia_inf").hide();
                    $(".noReplace").show();
                    $(".page-paginator").hide();
                    $(".noLeave").hide();
                }
                else {
                    $(".noReplace").hide();
                    $(".tqingjia_inf").show();
                    $(".page-paginator").show();
                    $(".noLeave").hide();
                    var option = {
                        total: data.total,
                        pagesize: data.pageSize,
                        currentpage: Number(page),
                        operate: function (totalPage) {
                            $('.page-index span').each(function () {
                                $(this).off("click");
                                $(this).click(function () {
                                    manageLeave.getDaikeList(Number($(this).text()), pageSize);
                                });
                            });
                            $('.first-page').off("click");
                            $('.first-page').click(function () {
                                manageLeave.getDaikeList(1, pageSize);
                            });
                            $('.last-page').off("click");
                            $('.last-page').click(function () {
                                manageLeave.getDaikeList(totalPage, pageSize);
                            });
                        }
                    };
                    Paginator.initPaginator(option);
                    $(".tqingjia_inf").empty();
                    Common.render({tmpl: $("#replaceTempJs"), data: {data: data.list}, context: ".tqingjia_inf"});
                }
            }
        })
    };
    //获取全校老师列表
    manageLeave.getAllTeacherList=function()
    {
        $.ajax({
            url:"/myschool/teacherlist.do",
            type:"post",
            dataType:"json",
            data:{
                page:1,
                pageSize:1000
            },
            success:function(data)
            {
                /*$(".teacherChoose").empty();
                Common.render({tmpl:$("#teacherTempJs"),data:{data:data.rows},context:".teacherChoose"});
                $(".teacherChoose2").empty();
                Common.render({tmpl:$("#teacherTempJs"),data:{data:data.rows},context:".teacherChoose2"});*/
                manageLeave.teacherList=data;
            }
        })
    };
    manageLeave.teacherList=[];
    //获取详情
    manageLeave.getLeaveDetail=function(leaveId)
    {
        Common.getData('/leave/getLeaveCourseDetail.do',{leaveId:leaveId},function(data){
            console.log(data);
            if(data){
                $('#applyPerson').text(data.leave.userName);
                manageLeave.oldTeacherId=data.leave.userId;
                $('#applyDate').text(data.leave.applyDateStr);
                $(".replyMessage").text(data.leave.replyMessage);
                $("#leaveDetail").text(data.leave.dateFrom+"~"+data.leave.dateEnd+"    共计"+data.leave.classCount+"课时");
                $('#tt').text(data.leave.title);
                $('#dt').text(data.leave.content);
                manageLeave.allCourse=data.leave.classCount;
                manageLeave.AllCourseList=data.course;
                $('.weekList').empty();
                Common.render({tmpl: $('#weekTempJs'), data: {data:data.week}, context: '.weekList'});
                manageLeave.initTable(data.week[0]);
            }
        });
    };
    //显示课表
    manageLeave.initTable=function(week)
    {
        var weekCourseList=[];
        for(var i=0;i<manageLeave.AllCourseList.length;i++)
        {
            if(manageLeave.AllCourseList[i].week==week)
            {
                weekCourseList.push(manageLeave.AllCourseList[i]);
            }
        }
        //填补空缺的
        for(var j =0;j<manageLeave.tableConf.classDays.length;j++)
        {
            var m=manageLeave.tableConf.classDays[j];
            for(var n=1;n<=manageLeave.tableConf.classCount;n++)
            {
                var have=false;
                for(var i=0;i<weekCourseList.length;i++)
                {
                    if(weekCourseList[i].x==m&&weekCourseList[i].y==n)
                    {
                        have=true;
                        break;
                    }
                }
                if(!have)
                {
                    weekCourseList.push({week:1,x:Number(m),y:n,courseId:"",courseName:""})
                }
            }
        }
        var replaceList=[];
        for(var item in manageLeave.replaceList)
        {
            if(manageLeave.replaceList[item].week==week)
            {
                replaceList.push(manageLeave.replaceList[item]);
            }
        }
        $('.qjshenhe_table').empty();
        Common.render({tmpl: $('#tableTmpJs'), data: {
            conf: manageLeave.tableConf,
            course:weekCourseList,
            replace:replaceList
        }, context: '.qjshenhe_table'});
    };
    //驳回
    manageLeave.rejectLeave=function(leaveId)
    {
        $.ajax({
            url:"/leave/rejectLeave.do",
            type:"post",
            dataType:"json",
            data:{
                leaveId:leaveId
            },
            success:function(data)
            {
                if(data.result=="200")
                {
                    manageLeave.getAllLeaves(1,10);
                }
                else{
                    alert("驳回失败");
                }
            }
        })
    };
    //获取课表结构配置
    manageLeave.getTableConf=function()
    {
        Common.getPostData("/leave/findTableConf.do",{},function(data)
        {
            manageLeave.tableConf=data;
        })
    };
    //获取可以代课的老师列表
    manageLeave.getAvailableTeacher=function(week,x,y,courseName)
    {
        $.ajax({
            url:"/leave/getAvailableTeacherList.do",
            type:"post",
            dataType:"json",
            data:{
                week:week,
                x:x,
                y:y,
                courseName:courseName
            },
            success:function(data)
            {
                $('#teacherList').empty();
                Common.render({tmpl: $('#teacherTemJs'), data: {data: data}, context: '#teacherList'});
            }
        })
    };
    //确认代课
    manageLeave.replaceLeave=function(teacherId,oldTeacherId,courseId,courseName,week,x,y,teacherName)
    {
        $.ajax({
            url:"/leave/replaceCourse.do",
            type:"post",
            dataType:"json",
            data:{
                teacherId:teacherId,
                oldTeacherId:oldTeacherId,
                courseId:courseId,
                courseName:courseName,
                week:week,
                x:x,
                y:y,
                leaveId:manageLeave.leaveId
            },
            success:function(data)
            {
                var have=false;
                for(var item in manageLeave.replaceList)
                {
                    if(manageLeave.replaceList[item].x==x&&manageLeave.replaceList[item].y==y
                        &&manageLeave.replaceList[item].week==week)
                    {
                        manageLeave.replaceList[item].replaceId=data.repId;
                        manageLeave.replaceList[item].replaceTea=teacherName;
                        have=true;
                        break;
                    }
                }
                if(!have)
                {
                    manageLeave.replaceList.push({x:x,y:y,week:week,replaceId:data.repId,replaceTea:teacherName});
                }
                manageLeave.initTable(Number($(".weekList").val()));
                var left=manageLeave.allCourse-manageLeave.replaceList.length;
                if(left==0)
                {
                    $("#agreeLeave").text("同意请假");
                }
                else {
                    $("#agreeLeave").text("未代课"+left+"节");
                }
                $(".qjshenhe_table_avtive[x='"+x+"'][y='"+y+"']").addClass("course_choosed");
            }
        })
    };
    //同意请假
    manageLeave.agreeLeave=function(replaceIds,leaveId)
    {
        $.ajax({
            url:"/leave/agreeLeave.do",
            type:"post",
            dataType:"json",
            data:{
                replaceIds:replaceIds,
                leaveId:leaveId
            },
            success:function(data)
            {
                if(data.result=="200")
                {
                    $(window).unbind('beforeunload');
                    //$(".tqingjia_maincon").hide();
                    $(".qjshenhe_maincon").hide();
                    $(".qingjia").show();
                    $(".daike").hide();
                    $("#backOrReply").text("我的请假");
                    $(".ulShow").show();
                    manageLeave.getAllLeaves(1,10);

                    manageLeave.replaceList=[];//代课记录
                    manageLeave.courseId="";//课程id
                    manageLeave.courseName="";//课程名
                    manageLeave.oldTeacherId="";//原上课老师
                    manageLeave.x=1;//星期几
                    manageLeave.y=1;//第几节课
                    manageLeave.week=1;//周
                    manageLeave.leaveId="";//请假id
                    manageLeave.allCourse=1;//请假共涉及到的课总数
                }
            }
        })
    };
    //放弃审核
    manageLeave.removeReplace=function()
    {
        var replaceIds = "";
        for (var item in manageLeave.replaceList) {
            replaceIds += manageLeave.replaceList[item].replaceId + ",";
        }
        if (replaceIds.length > 0) {
            replaceIds = replaceIds.substring(0, replaceIds.length - 1);
        }
        $.ajax({
            url:"/leave/removeReplace.do",
            type:"post",
            dataType:"json",
            data:{
                replaceIds:replaceIds
            },
            success:function(data)
            {
                if(data.result=="200")
                {
                    //$(".tqingjia_maincon").hide();
                    $(".qjshenhe_maincon").hide();
                    $(".qingjia").show();
                    $(".daike").hide();
                    $("#backOrReply").text("我的请假");
                    $(".ulShow").show();
                    manageLeave.getAllLeaves(1,10);

                    manageLeave.replaceList=[];//代课记录
                    manageLeave.courseId="";//课程id
                    manageLeave.courseName="";//课程名
                    manageLeave.oldTeacherId="";//原上课老师
                    manageLeave.x=1;//星期几
                    manageLeave.y=1;//第几节课
                    manageLeave.week=1;//周
                    manageLeave.leaveId="";//请假id
                    manageLeave.allCourse=1;//请假共涉及到的课总数
                }
            }
        })
    };
    //获取请假列表
    manageLeave.getAllLeaves=function(page,pageSize)
    {
        //var teacherId=$(".teacherChoose").val();

        var teacherId="";
        var name=$(".teacherChooseInp").val();
        if(name=="")
        {
            teacherId="";
        }
        else{
            var userId="";
            for(var i=0;i<manageLeave.teacherList.rows.length;i++)
            {
                if(manageLeave.teacherList.rows[i].userName==name)
                {
                    userId=manageLeave.teacherList.rows[i].id;
                    break;
                }
            }
            if(userId=="")
            {
                $(".tqingjia_inf").hide();
                $(".noReplace").hide();
                $(".page-paginator").hide();
                $(".noLeave").show();
                return;
            }
            else{
                teacherId=userId;
            }
        }

        var day=Number($(".tselect_time").val());
        var type=Number($(".tselect_type").val());
        $.ajax({
            url:"/leave/getAllLeaves.do",
            type:"post",
            dataType:"json",
            data:{
                teacherId:teacherId,
                day:day,
                type:type,
                page:page,
                pageSize:pageSize
            },
            success:function(data)
            {
                if(data.total==0)
                {
                    $(".tqingjia_inf").hide();
                    $(".noReplace").hide();
                    $(".page-paginator").hide();
                    $(".noLeave").show();
                }
                else {
                    $(".noReplace").hide();
                    $(".tqingjia_inf").show();
                    $(".page-paginator").show();
                    $(".noLeave").hide();
                    var option = {
                        total: data.total,
                        pagesize: data.pageSize,
                        currentpage: Number(page),
                        operate: function (totalPage) {
                            $('.page-index span').each(function () {
                                $(this).off("click");
                                $(this).click(function () {
                                    manageLeave.getAllLeaves(Number($(this).text()), pageSize);
                                });
                            });
                            $('.first-page').off("click");
                            $('.first-page').click(function () {
                                manageLeave.getAllLeaves(1, pageSize);
                            });
                            $('.last-page').off("click");
                            $('.last-page').click(function () {
                                manageLeave.getAllLeaves(totalPage, pageSize);
                            });
                        }
                    };
                    Paginator.initPaginator(option);
                    $(".tqingjia_inf").empty();
                    Common.render({tmpl: $("#leaveApplyListJs"), data: {data: data.list}, context: ".tqingjia_inf"});
                }
            }
        })
    };
    manageLeave.replaceList=[];//代课记录
    manageLeave.courseId="";//课程id
    manageLeave.courseName="";//课程名
    manageLeave.oldTeacherId="";//原上课老师
    manageLeave.x=1;//星期几
    manageLeave.y=1;//第几节课
    manageLeave.week=1;//周
    manageLeave.tableConf={};//课表结构
    manageLeave.AllCourseList=[];//请假涉及到的所有课程
    manageLeave.leaveId="";//请假id
    manageLeave.allCourse=1;//请假共涉及到的课总数
    manageLeave.init();
    manageLeave.getAllTeacherList();
    manageLeave.getAllLeaves(1,10);
    manageLeave.getTableConf();
    manageLeave.getYearList();
});