/**
 * Created by qiangm on 2015/8/20.
 */
'use strict';
define(function (require, exports, module) {
    /**
     *初始化参数
     */
    require("rome");
    require("doT");
    var teacherLeave = {},
        Common = require('common');
    var Paginator=require('initPaginator');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    teacherLeave.init = function () {


    };
    $(document).ready(function()
    {
        $("body").on("click","#qingjia",function()
        {
            $(".qingjia").show();
            $(".daike").hide();
            $("#qingjia").removeClass("grqingjia_active");
            $("#daike").addClass("grqingjia_active");
            teacherLeave.getLeaveList(1,10);

        });
        $("body").on("click","#daike",function()
        {
            $(".qingjia").hide();
            $(".daike").show();
            $("#qingjia").addClass("grqingjia_active");
            $("#daike").removeClass("grqingjia_active");
            teacherLeave.getMyReplace(1,10);
        });
        //点击我要请假
        $("body").on("click","#applyLeave",function()
        {
            $(".right1").hide();
            $(".right2").show();
            $(".right3").hide();
            $("#title").val("");
            $("#details").val("");
            teacherLeave.calLeaveInfo($("#from").val(),$("#end").val());
        });
        $("body").on("click","#back",function()
        {
            $(".right2").hide();
            $(".right1").show();
            $(".right3").hide();
        });
        $("body").on("click",".detail",function()
        {
            $(".right2").hide();
            $(".right3").show();
            $(".right1").hide();
            var leaveId=$(this).attr("li");
            Common.getData('/leave/getLeaveDetail.do',{leaveId:leaveId},function(data){
                //console.log(data);
                if(data){
                    $('#applyPerson').text(data.userName);
                    $('#applyDate').text(data.applyDateStr);
                    $(".replyMessage").text(data.replyMessage);
                    $("#leaveDetail").text(data.dateFrom+"~"+data.dateEnd+"    共计"+data.classCount+"课时");
                    if(data.reply==0||data.reply==2)
                    {
                        $("#deleteLeave").show();
                        $("#deleteLeave").attr("li",leaveId);
                    }
                    $('#tt').val(data.title);
                    $('#dt').text(data.content);
                }
            });
        });
        $("body").on("click","#detailBack",function()
        {
            $(".right2").hide();
            $(".right3").hide();
            $(".right1").show();
        });
        $("body").on("click",".qjtijiao_submit",function()
        {
            $(".qjmx_alert").show();
            $(".bg").show();
        });
        $("body").on("click",".qjmx_alert_cancel",function()
        {
            $(".qjmx_alert").hide();
            $(".bg").hide();
        });
        $('body').on('click','.qjmx_alert_sure',function(){
            var from = $('#from').val();
            var end = $('#end').val();
            var classCount = Number($("#classCount").text());
            var title = $('#title').val();
            var content = $('#details').val();
            if(title=="")
            {
                alert("标题不可为空");
                return;
            }
            if(content=="")
            {
                alert("内容不可为空");
                return;
            }
            applyLeave(from,end,classCount,title,content,0);
        });
        $('body').on('click','.qjmx_alert_look',function(){
            var from = $('#from').val();
            var end = $('#end').val();
            var classCount = Number($("#classCount").text());
            var title = $('#title').val();
            var content = $('#details').val();
            if(title=="")
            {
                alert("标题不可为空");
                return;
            }
            if(content=="")
            {
                alert("内容不可为空");
                return;
            }
            applyLeave(from,end,classCount,title,content,1);
        });
        //周切换
        $("body").on("click",".weekList",function()
        {
            var week=$(this).val();
            teacherLeave.initTable(week);
        });
        //结束时间变化
        $("#end").on("change",function()
        {
            teacherLeave.calLeaveInfo($("#from").val(),$("#end").val());
        });
        //删除请假
        $("body").on("click",".removeLeave",function()
        {
            $(".qjmx_confirm").show();
            $(".bg").show();
            var leaveId=$(this).attr("li");
            $("#leaveIdInp").val(leaveId);
            $("#leaveTypeInp").val(0);
        });
        //详情页删除请假记录
        $("body").on("click","#deleteLeave",function()
        {
            $(".qjmx_confirm").show();
            $(".bg").show();
            var leaveId=$(this).attr("li");
            $("#leaveIdInp").val(leaveId);
            $("#leaveTypeInp").val(1);
        });
        //确认删除
        $("body").on("click",".qjmx_confirm_sure",function()
        {
            $(".qjmx_confirm").hide();
            $(".bg").hide();
            var leaveId=$("#leaveIdInp").val();
            var type=$("#leaveTypeInp").val();
            teacherLeave.removeLeave(leaveId,type);
            $("#leaveIdInp").val("");
            $("#leaveTypeInp").val(0);
        });
        //取消删除
        $("body").on("click",".qjmx_confirm_cancel",function()
        {
            $(".qjmx_confirm").hide();
            $(".bg").hide();
            $("#leaveIdInp").val("");
            $("#leaveTypeInp").val(0);
        });
    });
    /**
     *
     * @param leaveId
     * @param type 0列表页，1详情页
     */
    teacherLeave.removeLeave = function(leaveId,type)
    {
        Common.getData("/leave/removeMyLeave.do",{leaveId:leaveId},function(data){
            if(data.code=="200")
            {
                if(type==0)
                    teacherLeave.getLeaveList(1,10);
                else if(type==1)
                {
                    teacherLeave.getLeaveList(1,10);
                    $(".right2").hide();
                    $(".right3").hide();
                    $(".right1").show();
                }
            }
            else{
                alert("删除失败");
                if(type==1)
                {
                    Common.getData('/leave/getLeaveDetail.do',{leaveId:leaveId},function(data1){
                        //console.log(data);
                        if(data1){
                            $("#deleteLeave").hide();
                            $('#applyPerson').text(data1.userName);
                            $('#applyDate').text(data1.applyDateStr);
                            $(".replyMessage").text(data1.replyMessage);
                            $("#leaveDetail").text(data1.dateFrom+"~"+data1.dateEnd+"    共计"+data1.classCount+"课时");
                            $('#tt').val(data1.title);
                            $('#dt').text(data1.content);
                        }
                    });
                }
                else if(type==0)
                {
                    teacherLeave.getLeaveList(1,10);
                }
            }
        });
    };

    /**
     * 獲取個人請假列表
     * */
    teacherLeave.getLeaveList = function(page,pageSize){
        Common.getData('/leave/getMyLeave.do',{page:page,pageSize:pageSize},function(data){
            if(data) {
                if(data.total==0)
                {
                    $(".noReplace").hide();
                    $(".daike").hide();
                    $(".qingjia").hide();
                    $(".noLeave").show();
                }
                else {
                    $(".noReplace").hide();
                    $(".daike").hide();
                    $(".qingjia").show();
                    $(".noLeave").hide();
                    var option = {
                        total: data.total,
                        pagesize: data.pageSize,
                        currentpage: data.page,
                        operate: function (totalPage) {
                            $('.page-index span').each(function () {
                                $(this).off('click');
                                $(this).click(function () {
                                    teacherLeave.getLeaveList(Number($(this).text()), 10);
                                });
                            });
                            $('.first-page').off("click");
                            $('.first-page').click(function () {
                                teacherLeave.getLeaveList(1, 10);
                            });
                            $('.last-page').off("click");
                            $('.last-page').click(function () {
                                teacherLeave.getLeaveList(totalPage, 10);
                            });
                        }
                    };
                    Paginator.initPaginator(option);
                    initLeaveList(data);
                }
            }
        })
    };
    //获取个人代课
    teacherLeave.getMyReplace = function(page,pageSize){
        Common.getData('/leave/findMyReplace.do',{page:page,pageSize:pageSize},function(data){
            if(data) {
                if(data.total==0)
                {
                    $(".noReplace").show();
                    $(".daike").hide();
                    $(".qingjia").hide();
                    $(".noLeave").hide();
                }
                else {
                    $(".noReplace").hide();
                    $(".daike").show();
                    $(".qingjia").hide();
                    $(".noLeave").hide();
                    var option = {
                        total: data.total,
                        pagesize: data.pageSize,
                        currentpage: data.page,
                        operate: function (totalPage) {
                            $('.page-index span').each(function () {
                                $(this).off('click');
                                $(this).click(function () {
                                    teacherLeave.getMyReplace(Number($(this).text()), 10);
                                });
                            });
                            $('.first-page').off("click");
                            $('.first-page').click(function () {
                                teacherLeave.getMyReplace(1, 10);
                            });
                            $('.last-page').off("click");
                            $('.last-page').click(function () {
                                teacherLeave.getMyReplace(totalPage, 10);
                            });
                        }
                    };
                    Paginator.initPaginator(option);
                    $('.myReplace').empty();
                    Common.render({tmpl: $('#replaceTempJs'), data: {data: data.list}, context: '.myReplace'});
                }
            }
        })
    };
    //获取本学期配置
    teacherLeave.getTermConf=function() {
        Common.getData("/leave/getCurrentTerm.do", {}, function (data) {
            teacherLeave.initRome(data);

        })
    };
    teacherLeave.initRome=function(data)
    {
        var startDay = data.start;
        var endDay = data.end;
        var moment = rome.moment;
        rome(from,{time: true,min:startDay,initialValue:startDay,inputFormat:'YYYY-MM-DD HH:mm'/*,dateValidator: rome.val.beforeEq(end)*/}).
            off('data').
            on('data', function (value) {
                if($("#end").val()<value)
                {
                    $("#end").val(value);
                }
                teacherLeave.calLeaveInfo(value,$("#end").val());

            });
        rome(end,{time: true,max:endDay,initialValue:startDay,inputFormat:'YYYY-MM-DD HH:mm',dateValidator: rome.val.afterEq(from)}).
            off('data').
            on('data', function (value) {
                teacherLeave.calLeaveInfo($("#from").val(),value);
            });
    }
    //获取课表结构配置
    teacherLeave.getTableConf=function()
    {
        Common.getPostData("/leave/findTableConf.do",{},function(data)
        {
            teacherLeave.tableConf=data;
        })
    };
    //获取课程数量
    teacherLeave.calLeaveInfo=function(dt1,dt2)
    {
        Common.getPostData("/leave/calLeaveInfo.do",{dateFrom:dt1,dateEnd:dt2},function(data)
        {
            $("#classCount").text(data.all);
            var weekList=data.week;
            teacherLeave.AllCourseList=data.course;
            $('.weekList').empty();
            Common.render({tmpl: $('#weekJs'), data: {data:weekList}, context: '.weekList'});
            teacherLeave.initTable(weekList[0]);
        });
    };

    teacherLeave.initTable=function(week)
    {
        var weekCourseList=[];
        for(var i=0;i<teacherLeave.AllCourseList.length;i++)
        {
            if(teacherLeave.AllCourseList[i].week==week)
            {
                weekCourseList.push(teacherLeave.AllCourseList[i]);
            }
        }
        //填补空缺的
        for(var j =0;j<teacherLeave.tableConf.classDays.length;j++)
        {
            var m=teacherLeave.tableConf.classDays[j];
            for(var n=1;n<=teacherLeave.tableConf.classCount;n++)
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
        $('.qjtijiao_table').empty();
        Common.render({tmpl: $('#tableTmpJs'), data: {conf: teacherLeave.tableConf,course:weekCourseList}, context: '.qjtijiao_table'});
    };

    function initLeaveList(data){
        $('.myLeave').empty();
        Common.render({tmpl: $('#myLeaveList'), data: {data: data.list}, context: '.myLeave'});
    }

    /**
     * 請假申請確定
     * */
    function applyLeave(from,end,classCount,title,content,type){
        Common.getPostData('/leave/addLeave.do',{dateFrom:from,dateEnd:end,classCount:classCount,title:title,content:content},function(data){
            if(data.result == 200){
                if(type==0) {
                    $(".qjmx_alert").hide();
                    $(".bg").hide();
                    $(".right1").show();
                    $(".right2").hide();
                    $(".right3").hide();
                    teacherLeave.getLeaveList(1, 10);
                }
                else if(type==1)
                {
                    $(".qjmx_alert").hide();
                    $(".bg").hide();
                    $(".right1").hide();
                    $(".right2").hide();
                    $(".right3").show();
                    teacherLeave.getLeaveList(1, 10);
                    if(data.leaveId!="") {
                        Common.getData('/leave/getLeaveDetail.do', {leaveId: data.leaveId}, function (data1) {
                            console.log(data1);
                            if (data1) {
                                $('#applyPerson').text(data1.userName);
                                $('#applyDate').text(data1.applyDateStr);
                                $(".replyMessage").text(data1.replyMessage);
                                $("#leaveDetail").text(data1.dateFrom + "~" + data1.dateEnd + "    共计" + data1.classCount + "课时");
                                if(data1.reply==0||data1.reply==2)
                                {
                                    $("#deleteLeave").show();
                                    $("#deleteLeave").attr("li",data.leaveId);
                                }
                                $('#tt').val(data1.title);
                                $('#dt').text(data1.content);
                            }
                        });
                    }
                }
            }
        })
    }

    /**
     * 初始化rome日历插件
     */
    function initRome() {

    }
    teacherLeave.tableConf={};
    teacherLeave.AllCourseList=[];
    teacherLeave.init();
    teacherLeave.getLeaveList(1,10);
    teacherLeave.getTermConf();
    teacherLeave.getTableConf();
});