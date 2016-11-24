/**
 * Created by qiangm on 2015/8/20.
 */
'use strict';
define(['doT', 'common', 'jquery','rome','initPaginator'], function (require, exports, module) {
    /**
     *初始化参数
     */
    require("jquery");
    require("doT");
    require("rome");
    var Paginator=require('initPaginator');
    var zoubanindex = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    zoubanindex.init = function () {



    };

    $(document).ready(function () {
        $("body").on("click", ".main1-btn1", function () {
            zoubanindex.showProgress();
        });
        $("body").on("click","#step01",function(){
            var state=Number($(this).attr("state"));
            if(state==0)
                return;
            else if(state==8)
            {
                alert("课表已经发布，如需修改，请在发布课表步骤取消发布课表");
                return;
            }
            window.open('../zouban/lessonInstall.do?version=58&term='+ encodeURI(encodeURI($('#termShow').val()))+'&gid='+
            $('#gradeShow').val()+'&gnm='+ encodeURI(encodeURI($('#gradeShow').find('option:selected').text()))+"&mode=0",'_self');
        });

        $("body").on("click","#step02",function(){
            var state=Number($(this).attr("state"));
            if(state==0)
                return;
            else if(state==8)
            {
                alert("课表已经发布，如需修改，请在发布课表步骤取消发布课表");
                return;
            }
            window.open('../zouban/chooselesson.do?version=58&term='+ encodeURI(encodeURI($('#termShow').val()))+'&gid='+
            $('#gradeShow').val()+'&gnm='+ encodeURI(encodeURI($('#gradeShow').find('option:selected').text()))+"&mode=0",'_self');
        });
        $("body").on("click","#step03",function(){
            var state=Number($(this).attr("state"));
            if(state==0)
                return;
            else if(state==8)
            {
                alert("课表已经发布，如需修改，请在发布课表步骤取消发布课表");
                return;
            }
            window.open('../zouban/classrule.do?version=58&term='+ encodeURI(encodeURI($('#termShow').val()))+'&gid='+
            $('#gradeShow').val()+'&gnm='+ encodeURI(encodeURI($('#gradeShow').find('option:selected').text()))+"&mode=0",'_self');
        });
        $("body").on("click","#step04",function(){
            var state=Number($(this).attr("state"));
            if(state==0)
                return;
            else if(state==8)
            {
                alert("课表已经发布，如需修改，请在发布课表步骤取消发布课表");
                return;
            }
            window.open('../zouban/bianban.do?version=58&term='+ encodeURI(encodeURI($('#termShow').val()))+'&gid='+
            $('#gradeShow').val()+'&gnm='+ encodeURI(encodeURI($('#gradeShow').find('option:selected').text()))+"&mode=0",'_self');
        });

        $("body").on("click","#step05",function(){
            var state=Number($(this).attr("state"));
            if(state==0)
                return;
            else if(state==8)
            {
                alert("课表已经发布，如需修改，请在发布课表步骤取消发布课表");
                return;
            }
            window.open('../paike/clashCheck.do?version=58&term='+ encodeURI(encodeURI($('#termShow').val()))+'&gid='+
            $('#gradeShow').val()+'&gnm='+ encodeURI(encodeURI($('#gradeShow').find('option:selected').text()))+"&mode=0",'_self');
        });
        $("body").on("click","#step06",function(){
            var state=Number($(this).attr("state"));
            if(state==0)
                return;
            else if(state==8)
            {
                alert("课表已经发布，如需修改，请在发布课表步骤取消发布课表");
                return;
            }

            window.open('../paike/paike.do?version=58&term='+ encodeURI(encodeURI($('#termShow').val()))+'&gid='+
            $('#gradeShow').val()+'&gnm='+ encodeURI(encodeURI($('#gradeShow').find('option:selected').text()))+"&mode=0",'_self')
        });
        $("body").on("click","#viewTable0",function(){
            var state=Number($(this).attr("state"));
            if(state==0)
                return;
            var published=0;
            if(progressStat==8)
                published=1;
            window.open('../paike/getTimetable.do?version=58&&term='+ encodeURI(encodeURI($('#termShow').val()))
            +'&gid='+ $('#gradeShow').val()+'&gnm='+ encodeURI(encodeURI($('#gradeShow').find('option:selected').text()))+"&pub="+published+"&mode=0","_self");
        });
        $("body").on("click","#viewTable1",function(){
            var state=Number($(this).attr("state"));
            if(state==0)
                return;
            window.open('../paike/getPubTimetable.do?version=58&&term='+ encodeURI(encodeURI($('#termShow').val()))
            +'&gid='+ $('#gradeShow').val()+'&gnm='+ encodeURI(encodeURI($('#gradeShow').find('option:selected').text()))+"&mode=0","_self");
        });
        $("body").on("click","#adjsutCourse",function(){
            var state=Number($(this).attr("state"));
            if(state==0)
                return;
            else {
                //判断本学期是否结束
                $.ajax({
                    url: "/paike/findTermEntry.do",
                    type: "post",
                    dataType: "json",
                    data: {
                        year: $("#termShow").val()
                    },
                    success: function (data) {
                        if(data.ste<getNowFormatDate())
                        {
                            alert("当前学年已经放假！");
                            return "";
                        }
                        else{
                            window.open('../paike/adjustPage.do?version=58&gid=' + $('#gradeShow').val() + '&gnm=' +
                            encodeURI(encodeURI($('#gradeShow').find('option:selected').text())) + "&mode=0", '_self');
                        }
                    }
                });
            }
        });
        $(".gotoGezhi").click(function(){
            window.open('../paike/gezhiIndex.do?version=58&year=&gradeId=','_self');
        })
    });
    function getNowFormatDate() {
        var date = new Date();
        var seperator1 = "-";
        var seperator2 = ":";
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = year + seperator1 + month + seperator1 + strDate
            + " " + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
        return currentdate;
    }
    var progressStat=1;
    zoubanindex.showProgress=function()
    {
        var term = $("#termShow").val();
        var gradeId = $("#gradeShow").val();
        if (term == "" || term == null || gradeId == "" || gradeId == null) {
            alert("数据不完整");
            return;
        }
        $("#gradeId").val(gradeId);
        $.ajax({
            url: "/xuanke/getState.do",
            type: "post",
            dataType: "json",
            data: {
                term: term,
                gradeId: gradeId
            },
            success: function (data) {
                $("#chooseShow").text("已选择："+$("#termShow").val()+" "+$("#gradeShow option:selected").text());
                progressStat=data.state;
                if(progressStat==5||progressStat==6)
                {
                    progressStat=7;
                }
                $(".right-mian2 i").attr("style","color:#000");
                $(".mian2-grandson").attr("state",0);
                $("#adjsutCourse").attr("state",0);
                $(".mian2-grandson").removeAttr("style");
                for (var p = 0; p < 8; p++) {
                    var x = p % 3;
                    var y = Number((p - x) / 3);
                    var obj = $(".main2-div" + (y + 1)).children(".lie" + (x + 1)).eq(0);
                    obj.find("i").attr("style", "color:#fff");
                    obj.find("div").attr("style", "background:#fff;color:#000");
                    obj.find("div").attr("state",0);
                }
                for (var p = 0; p < data.state; p++) {
                    var x = p % 3;
                    var y = Number((p - x) / 3);
                    var obj = $(".main2-div" + (y + 1)).children(".lie" + (x + 1)).eq(0);
                    obj.find("i").attr("style", "color:#ff8a00");
                    obj.find("div").attr("style", "background:#ff8a00;color:#fff");
                    obj.find("div").attr("state",1);
                }
                if(data.state==8)
                {
                    $("#adjsutCourse").attr("state",1);
                    //1~6设为不可点击
                    for (var p = 0; p < 6; p++) {
                        var x = p % 3;
                        var y = Number((p - x) / 3);
                        var obj = $(".main2-div" + (y + 1)).children(".lie" + (x + 1)).eq(0);
                        obj.find("div").attr("state",8);
                    }
                }
            }
        });
    };
    zoubanindex.getTermList = function () {
        $.ajax({
            url: "/paike/getAllYear.do",
            type: "post",
            dataType: "json",
            success: function (data) {
                $("#termShow").empty();
                $("#termShow2").empty();
                Common.render({tmpl: $('#termTempJs'), data: {data: data}, context: '#termShow'});
                Common.render({tmpl: $('#termTempJs'), data: {data: data}, context: '#termShow2'});
                zoubanindex.getGradeList();
                var year=$("#year").val();
                if(year!="")
                {
                    $("#termShow").val(year);
                }
            }
        })
    };
    zoubanindex.getGradeList = function () {
        $.ajax({
            url: "/course/getGradeList.do",
            type: "post",
            dataType: "json",
            success: function (data) {
                $("#gradeShow").empty();
                Common.render({tmpl: $('#gradeTempJs'), data: {data: data}, context: '#gradeShow'});
                var year=$("#year").val();
                var gradeId=$("#gradeId").val();
                if(gradeId!="")
                {
                    $("#gradeShow").val(gradeId);
                }
                //if(year==""&&gradeId=="") {
                    zoubanindex.showProgress();
                //}
            }
        })
    };
    zoubanindex.init();
    zoubanindex.getTermList();

    ////================================================================
    $(function () {
        $(".right-title  li").click(function () {
            $(this).addClass("cont-style").siblings("li").removeClass("cont-style");
        })
    });


    $(document).ready(function () {
        $(".main1-title").click(
            function () {
                $(".right2-main").hide();
                $(".right-main").show();
                $(".right3-main").hide();
            }
        )
    });

    $(document).ready(function () {
        $(".main3-title").click(
            function () {
                $(".right2-main").hide();
                $(".right3-main").show();
                $(".right-main").hide();
                zoubanindex.getTermConf();
                $(".yearShow").text($("#termShow").val());
            }
        )
    });

    $(document).ready(function () {
        $(".error-x").click(
            function () {
                $(".error-span").hide();
            }
        )
    });

    $(document).ready(function () {
        $(".main2-title").click(
            function () {
                $(".right-main").hide();
                $(".right2-main").show();
                $(".right3-main").hide();
                $(".error-span").hide();
                zoubanindex.getClassRoomList(1);

            }
        )
    });
    zoubanindex.getClassRoomList = function (page) {
        $.ajax({
            url: "/classroom/findClassroomListPage.do",
            type: "post",
            dataType: "json",
            data: {
                page: page,
                pageSize: 20
            },
            success: function (data) {
                var option = {
                    total: data.rowCount,
                    pagesize: data.pageSize,
                    currentpage: data.page,
                    operate: function (totalPage) {
                        $('.page-index span').each(function () {
                            //$(this).off("click");
                            $(this).one("click",function () {
                                zoubanindex.getClassRoomList($(this).text());
                            });
                        });
                        //$('.first-page').off("click");
                        $('.first-page').one("click",function () {
                            zoubanindex.getClassRoomList(1);
                        });
                        //$('.last-page').off("click");
                        $('.last-page').one("click",function () {
                            zoubanindex.getClassRoomList(totalPage);
                        });
                    }
                };
                Paginator.initPaginator(option);
                $("#classRoomShow").empty();
                Common.render({tmpl: $('#classRoomTempJs'), data: {data: data.rows}, context: '#classRoomShow'});
            }
        })
    };
    zoubanindex.addClassRoom = function () {
        $.ajax({
            url: "/classroom/addClassroom.do",
            type: "post",
            dataType: "json",
            data: {
                name: $("#add_name").val(),
                remark: $("#add_remark").val(),
                classid:$('#classlist').val()
            },
            success: function (data) {
                if (data.result == "SUCCESS") {
                    alert("添加成功");
                    zoubanindex.getClassRoomList(1);
                    $(".hide-add").hide();
                    $(".bg").hide();
                }
                else {
                    alert("添加失败");
                }
            }
        })
    };
    zoubanindex.updateClassRoom = function () {
        $.ajax({
            url: "/classroom/updateClassroom.do",
            type: "post",
            dataType: "json",
            data: {
                classroomId: $("#classRoomId").val(),
                name: $("#update_name").val(),
                remark: $("#update_remark").val(),
                classid:$('#classlist2').val()
            },
            success: function (data) {
                if (data.result == "SUCCESS") {
                    alert("修改成功");
                    zoubanindex.getClassRoomList(1);
                    $(".hide-edit").hide();
                    $(".bg").hide();
                }
                else {
                    alert("修改失败");
                }
            }
        });
    };
    zoubanindex.deleteClassRoom = function (classRoomId) {
        $.ajax({
            url: "/classroom/removeClassroom.do",
            type: "post",
            dataType: "json",
            data: {
                classroomId: classRoomId
            },
            success: function (data) {
                if (data.result == "SUCCESS") {
                    alert("删除成功");
                    zoubanindex.getClassRoomList(1);
                }
                else {
                    alert("删除失败");
                }
            }
        });
    };
    zoubanindex.checkIfHave = function (classRoomId, name) {
        $.ajax({
            url: "/classroom/checkIfHave.do",
            type: "post",
            dataType: "json",
            data: {
                classroomId: classRoomId,
                name: name
            },
            success: function (data) {
                if (data.result == "SUCCESS") {
                    $(".error-span").show();
                    canSend = false;
                }
                else {
                    $(".error-span").hide();
                    canSend = true;
                }
            }
        })
    };

    zoubanindex.findClassInfoBySchoolId = function (cnm,cid) {
        $.ajax({
            url: "/classroom/findClassInfoBySchoolId.do",
            type: "post",
            dataType: "json",
            data: {
            },
            success: function (data) {
                if(cnm!="")
                {
                    data.push({"id":cid,"className":cnm});
                    $("#classlist2").empty();
                    Common.render({tmpl: $('#classlistTempJs'), data: {data: data}, context: '#classlist2'});
                    $('#classlist2').val(cid);
                }
                else {
                    $("#classlist").empty();
                    Common.render({tmpl: $('#classlistTempJs'), data: {data: data}, context: '#classlist'});
                }
            }
        });
    };

    zoubanindex.checkIfHave2 = function (classRoomId, name,type) {
        $.ajax({
            url: "/classroom/checkIfHave.do",
            type: "post",
            dataType: "json",
            data: {
                classroomId: classRoomId,
                name: name
            },
            success: function (data) {
                if (data.result == "SUCCESS") {
                    $(".error-span").show();
                    canSend = false;
                    alert("名称重复");
                }
                else {
                    $(".error-span").hide();
                    canSend = true;
                    if(type==1)
                    {
                        zoubanindex.updateClassRoom();
                    }
                    else if(type==0)
                    {
                        zoubanindex.addClassRoom();
                    }
                }
            }
        })
    };
    var canSend = true;
    $(document).ready(function () {
        $("body").on("click", ".delete", function () {
            zoubanindex.deleteClassRoom($(this).parent().attr("crid"));
        });
        $("body").on("blur", "#add_name", function () {
            zoubanindex.checkIfHave("", $("#add_name").val());
        });
        $("body").on("blur", "#update_name", function () {
            zoubanindex.checkIfHave($("#classRoomId").val(), $("#update_name").val());
        });
        $("body").on("click", ".hide-conf-add", function () {
            if($("#add_name").val()=="")
            {
                alert("名称不能为空");
                return;
            }
            if (canSend) {
                zoubanindex.checkIfHave2("", $("#add_name").val(),0);
            }
            else
            {
                alert("名称重复");
            }
        });
        $("body").on("click", ".hide-conf-update", function () {
            if($("#update_name").val()=="")
            {
                alert("名称不能为空");
                return;
            }
            if (canSend) {
                zoubanindex.checkIfHave2($("#classRoomId").val(), $("#update_name").val(),1);
            }
            else
            {
                alert("名称重复");
            }
        });
        $("body").on("click", ".right2-btn", function () {//打开添加
            zoubanindex.findClassInfoBySchoolId("","");
            $("#add_name").val("");
            $("#add_remark").val("");
            $(".hide-add").show();
            $(".error-span").hide();
            canSend = true;
            $(".bg").show();
        });

        $("body").on("click", ".tab-edit", function () {//打开修改
            zoubanindex.findClassInfoBySchoolId($(this).parent().attr("cnm"),$(this).parent().attr("cid"));
            $(".hide-edit").show();
            $("#classRoomId").val($(this).parent().attr("crid"));
            $("#update_name").val($(this).parent().parent().children().eq(0).text());
            $("#update_remark").val($(this).parent().parent().children().eq(2).text());

            canSend = true;
            $(".bg").show();
            $(".error-span").hide();
        });

        $("body").on("click", ".hide-x ,.hide-canc", function () {
            $(".hide-add").hide();
            $(".weekwind").hide();
            $(".hide-edit").hide();
            $(".bg").hide();
        });
        $("body").on("change","#termShow2",function()
        {
            zoubanindex.getTermConf();
            $(".yearShow").text($("#termShow2").val());
        });
    });
    //================================================教学周===============================================================
    zoubanindex.getTermConf=function()
    {
        $.ajax({
            url:"/paike/findTermEntry.do",
            type:"post",
            dataType:"json",
            data:{
                year:$("#termShow2").val()
            },
            success:function(data){
                var firstTerm=zoubanindex.generalDateList(data.fts,data.fte);
                var secondTerm=zoubanindex.generalDateList(data.sts,data.ste);
                var firstTermTime=data.fts+"~"+data.fte;
                var secondTermTime=data.sts+"~"+data.ste;
                var firstTermWeek=data.fweek;
                var secondTermWeek=data.sweek;
                $("#firstTermWeek").text("第一学期 共"+firstTermWeek+"周");
                $("#firstTermTime").text(firstTermTime);
                $("#firstTermList").empty();
                Common.render({tmpl: $('#firstTermWeekTempJS'), data: {term: firstTerm}, context: '#firstTermList'});
                $("#firstTermData").empty();
                Common.render({tmpl: $('#firstTermDateTempJS'), data: {term: firstTerm}, context: '#firstTermData'});

                $("#secondTermWeek").text("第二学期 共"+secondTermWeek+"周");
                $("#secondTermTime").text(secondTermTime);
                $("#secondTermList").empty();
                Common.render({tmpl: $('#secondTermWeekTempJS'), data: {term: secondTerm}, context: '#secondTermList'});
                $("#secondTermData").empty();
                Common.render({tmpl: $('#secondTermDateTempJS'), data: {term: secondTerm}, context: '#secondTermData'});
            }
        });
    };
    //生成校历
    zoubanindex.generalDateList=function(termStart,termEnd)
    {
        var ftDays=[];
        var fts=strToDate(termStart);
        var fts_mon=fts.getMonth();
        var fts_fir_date=fts.getDate();
        var fts_week=fts.getDay();//星期，0~6 从星期天开始
        for(var i=0;i<fts_week;i++)
        {
            ftDays.push("");
        }

        var curDate = new Date();
        curDate.setYear(fts.getYear());
        curDate.setMonth(fts_mon+1);
        curDate.setDate(0);
        var alldays=curDate.getDate();

        var fte=strToDate(termEnd);
        var fte_mon=fte.getMonth();
        if(fte_mon<fts_mon)
            fte_mon+=12;
        var monthDays2 = fte.getDate();
        if(fte_mon==fts_mon)
        {
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
        for(var i=fts_mon+1;i<fte_mon;i++)
        {
            curDate.setMonth(i+1);
            if(i>=12) {
                curDate.setYear(fts.getYear() + 1);
                curDate.setMonth(i-11);
            }
            curDate.setDate(0);
            var monthDay=curDate.getDate();
            ftDays.push((i+1)+"月/1");
            for(var j=2;j<=monthDay;j++)
            {
                ftDays.push(j);
            }
        }
        if(fte_mon>fts_mon) {
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
    //添加学期
    zoubanindex.addTerm=function()
    {
        var firstTermStart = new Date($("#firstStart").val());
        var firstTermEnd = new Date($("#firstEnd").val());
        var secondTermStart = new Date($("#secondStart").val());
        var secondTermEnd = new Date($("#secondEnd").val());

        if (isNaN(firstTermStart.getTime()) || isNaN(firstTermEnd.getTime())||
            isNaN(secondTermStart.getTime()) || isNaN(secondTermEnd.getTime())) {
            alert("请正确输入学期时间");
            return;
        }
        if (firstTermEnd.getTime() < firstTermStart.getTime()) {
            alert("第一学期放假日期小于开学日期");
            return;
        }
        if (secondTermEnd.getTime() < secondTermStart.getTime()) {
            alert("第二学期放假日期小于开学日期");
            return;
        }
        if (secondTermStart.getTime() < firstTermEnd.getTime()) {
            alert("第二学期开学日期小于第一学期放假日期");
            return;
        }
        if(DateDiff($("#firstStart").val(),$("#firstEnd").val())>180)
        {
            alert("第一学期上课天数过长，请重新设置");
            return;
        }
        if(DateDiff($("#secondStart").val(),$("#secondEnd").val())>180)
        {
            alert("第二学期上课天数过长，请重新设置");
            return;
        }
        $.ajax({
            url:"/paike/addTermEntry.do",
            type:"post",
            dataType:"json",
            data:{
                year:$("#termShow2").val(),
                fts:$("#firstStart").val(),
                fte:$("#firstEnd").val(),
                sts:$("#secondStart").val(),
                ste:$("#secondEnd").val()
            },
            success:function(data)
            {
                if(data.result!="SUCCESS")
                {
                    alert("添加失败");
                }
                else
                {
                    $(".weekwind").hide();
                    $(".bg").hide();
                    zoubanindex.getTermConf();
                }
            }
        });
    };
    //获取学期配置
    zoubanindex.getTerm=function()
    {
        $.ajax({
            url: "/paike/findTermEntry.do",
            type: "post",
            dataType: "json",
            data: {
                year: $("#termShow2").val()
            },
            success: function (data) {
                $("#firstStart").val(data.fts);
                $("#firstEnd").val(data.fte);
                $("#secondStart").val(data.sts);
                $("#secondEnd").val(data.ste);
                $("#firstWeek").text("共"+data.fweek+"周");
                $("#secondWeek").text("共"+data.sweek+"周");
                var moment = rome.moment;
                rome(firstStart,{ time: false ,initialValue:data.fts,dateValidator: rome.val.beforeEq(firstEnd)}).
                    off('data').
                    on('data', function (value) {
                        var week=zoubanindex.calWeekCount(value,$("#firstEnd").val());
                        $("#firstWeek").text("共"+week+"周");
                        $("#firstStart").val(value);
                    });
                rome(firstEnd,{ time: false  ,initialValue:data.fte,dateValidator: rome.val.afterEq(firstStart)}).
                    off('data').
                    on('data', function (value) {
                        var week=zoubanindex.calWeekCount($("#firstStart").val(),value);
                        $("#firstWeek").text("共"+week+"周");
                        $("#firstEnd").val(value);
                    });
                rome(secondStart,{ time: false  ,initialValue:data.sts,dateValidator: rome.val.beforeEq(secondEnd)}).
                    off('data').
                    on('data', function (value) {
                        var week=zoubanindex.calWeekCount(value,$("#secondEnd").val());
                        $("#secondWeek").text("共"+week+"周");
                        $("#secondStart").val(value);
                    });
                rome(secondEnd,{ time: false ,initialValue:data.ste,dateValidator: rome.val.afterEq(secondStart)}).
                    off('data').
                    on('data', function (value) {
                        var week=zoubanindex.calWeekCount($("#secondStart").val(),value);
                        $("#secondWeek").text("共"+week+"周");
                        $("#secondEnd").val(value);
                    });
            }
        });
    };
    //计算天数差的函数，通用
    //sDate1 后面日期
    function  DateDiff(sDate1,sDate2){    //sDate1和sDate2是2006-12-18格式
        var  aDate,  oDate1,  oDate2,  iDays;
        aDate  =  sDate1.split("-");
        oDate1  =  new  Date(aDate[0] + '-'+aDate[1]  +  '-'  +  aDate[2]) ;   //转换为12-18-2006格式
        aDate  =  sDate2.split("-");
        oDate2  =  new  Date(aDate[0] + '-' +aDate[1]  +  '-'  +  aDate[2]);
        iDays  =  parseInt(Math.abs(oDate1  -  oDate2)  /  1000  /  60  /  60  /24);   //把相差的毫秒数转换为天数
        return  iDays;
    }

    /**
     * 自动计算当前共有多少周
     * @param sDate1 开始日期
     * @param sDate2 结束日期
     */
    zoubanindex.calWeekCount=function(sDate1,sDate2)
    {
        var days=DateDiff(sDate2,sDate1);
        var start=strToDate(sDate1);
        var fts_week=start.getDay();//星期，0~6 从星期天开始
        days+=fts_week;
        if(days%7==0)
            return days/7+1;
        return Math.ceil(days/7);
    };
    $(document).ready(function(){
        $("body").on("click",".right3-1",function(){
            $(".weekwind").show();
            $(".bg").show();

            zoubanindex.getTerm();
        });
        //保存学期时间
        $("body").on("click",".week-conf",function(){
            zoubanindex.addTerm();
        });
    });
});