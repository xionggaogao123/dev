/**
 * Created by qiangm on 2015-10-16.
 */
'use strict';
define(['doT', 'common', 'jquery'], function (require, exports, module) {
    /**
     *初始化参数
     */
    require("jquery");
    require("doT");
    var gezhiArranging = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    gezhiArranging.init = function () {



    };

    $(function () {
        $(".backUrl").click(function () {
            window.open('../paike/index.do?version=58&year=' + encodeURI(encodeURI($('#termVal').val())) + '&gradeId=' +
            $('#gradeId').val(), '_self');
        });
        $(".arranging-GR").click(function () {
            $(".bg").show();
            $(".gring-CUR").show();
        });


        $(".tab-top li").click(function () {
            $(this).addClass("cur").siblings("li").removeClass("cur");
            $("#contentt>div").hide();
            var name = $(this).attr("id");
            $("#" + "tab-" + name).show().siblings().hide();
            var index = $(this).index();
            if (index == 0) {

            }
            else if (index == 1) {
                gezhiArranging.getGradeGroupClass();
            }
            else if (index == 2)//小走班
            {
                $("#physicalSet").hide();
                gezhiArranging.getXZBcourse(0);
            }
            else if (index == 3)//体育走班
            {
                $("#xzbSet").hide();
                gezhiArranging.getPhysicalcourse(0);
            }
            else if (index == 4) {
                $("#xzbSet").hide();
                $("#physicalSet").hide();
                gezhiArranging.getGradeClassList(4);
            }
            else if(index == 5)
            {
                $("#xzbSet").hide();
                $("#physicalSet").hide();
                gezhiArranging.getGradeClassList(5);
            }
        });


        $(".Rclass-JY-TCC-QX").click(function () {
            $(".Rclass-JY-SJ").hide();
            $(".Rclass-JY-SJJ").hide();
            $(".Rclass-JY-TC").hide();
        });

        $(".Xclass-JY-TCC-QX").click(function () {
            $(".Xclass-JY-SJ").hide();
            $(".Xclass-JY-SJJ").hide();
            $(".Xclass-JY-TC").hide();
        });
    });
    //================================课表配置=========================================
    //获取课表配置
    gezhiArranging.getTimetableConf = function () {
        var gradeId = $("#gradeId").val();
        $.ajax({
            url: "/paike/findCourseConf.do",
            type: "post",
            dataType: "json",
            data: {
                gradeId: gradeId,
                term: $("#termVal").val()
            },
            success: function (data) {
                //本地暂存
                courseConfDTO = data;
                $("#term").text(data.term);
                for (var i = 0; i < data.classDays.length; i++) {
                    $(":checkbox[value='" + data.classDays[i] + "'][name='classDays']").prop("checked", true);
                }
                $(".timetable-SE").val(data.classCount);
                gezhiArranging.generalTable(data.classTime);
            }
        })
    };

    $(document).ready(function () {
        //修改上课节数
        $("body").on("change", ".timetable-SE", function () {
            courseConfDTO.classCount = Number($(".timetable-SE").val());
            var arr = new Array(10);
            arr[0] = "08:00~08:45";
            arr[1] = "08:55~09:40";
            arr[2] = "10:00~10:45";
            arr[3] = "10:55~11:40";
            arr[4] = "14:00~14:45";
            arr[5] = "14:55~15:40";
            arr[6] = "16:00~16:45";
            arr[7] = "16:55~17:40";
            arr[8] = "19:00~19:50";
            arr[9] = "20:00~20:50";
            gezhiArranging.generalTable(arr);
            if (courseConfDTO.classCount > courseConfDTO.classTime.length) {
                var need = courseConfDTO.classCount - courseConfDTO.classTime.length;
                for (var k = 0; k < need; k++) {
                    courseConfDTO.classTime.push("");
                }
            }
            else if (courseConfDTO.classCount < courseConfDTO.classTime.length) {
                var need = courseConfDTO.classTime.length - courseConfDTO.classCount;
                courseConfDTO.classTime.splice(courseConfDTO.classCount - 1, need);
            }
            for (var i = 0; i < courseConfDTO.classCount; i++) {
                courseConfDTO.classTime[i] = arr[i];
            }
        });
        //修改上课天数
        $("body").on("change", ":checkbox[name='classDays']", function () {
            if (this.checked) {
                courseConfDTO.classDays.push(Number($(this).val()));
            }
            else {
                courseConfDTO.classDays.splice($.inArray(Number($(this).val()), courseConfDTO.classDays), 1);
            }
            var arr = new Array(10);
            arr[0] = "08:00~08:45";
            arr[1] = "08:55~09:40";
            arr[2] = "10:00~10:45";
            arr[3] = "10:55~11:40";
            arr[4] = "14:00~14:45";
            arr[5] = "14:55~15:40";
            arr[6] = "16:00~16:45";
            arr[7] = "16:55~17:40";
            arr[8] = "19:00~19:50";
            arr[9] = "20:00~20:50";
            gezhiArranging.generalTable(arr);
        });
        //教研弹窗
        $(".JY-wind-x").click(function () {
            $(".bg").hide();
            $(".Rclass-JY-wind").hide();
            $(".Rclass-JY-wind2").hide();
        });

        //设置集体活动弹窗
        $("body").on("click", ".arranging-Y", function () {
            $(".bg").show();
            $(".gring-CUR").show();
            var obj = $(this).parent().find("td").first();
            var yIndex = obj.find("strong").text();
            var time = obj.find("input").val();
            var xIndex = $(this).parent().find("td").index($(this));
            groupStudy = [];
            courseEventDTO = {
                id: "",
                xIndex: xIndex,
                yIndex: Number(yIndex.substring(2, 1)),
                forbidEvent: [],
                groupStudy: [],
                personEvent: []
            };

            TeacherEventDTO = {
                Iid: "",
                subjectId: "",
                subjectName: "",
                teacherName: "",
                teacherId: "",
                event: ""
            };
            for (var i = 0; i < courseConfDTO.events.length; i++) {
                if (courseConfDTO.events[i].xIndex == xIndex && "第" + courseConfDTO.events[i].yIndex + "节" == yIndex) {
                    groupStudy = courseConfDTO.events[i].groupStudy;
                    courseEventDTO = courseConfDTO.events[i];
                    for(var p=0;p<courseEventDTO.forbidEvent.length;p++)
                    {
                        forbidEvent_base.push(courseEventDTO.forbidEvent[p]);
                    }
                    for(var p=0;p<courseEventDTO.groupStudy.length;p++)
                    {
                        groupStydy_base.push(courseEventDTO.groupStudy[p]);
                    }
                    for(var p=0;p<courseEventDTO.personEvent.length;p++)
                    {
                        personEnent_base.push(courseEventDTO.personEvent[p]);
                    }
                    TeacherEventDTO = courseEventDTO.personEvent;
                    break;
                }
            }
            gezhiArranging.openWindow(xIndex, yIndex, time);
            gezhiArranging.getSubjectUser();

        });
        //设置老师
        $("body").on("click", "#pSubjectListShow", function () {
            var id = $(this).val();
            for (var i = 0; i < subjectTeacherList.length; i++) {
                if (subjectTeacherList[i].t.idStr == id) {
                    gezhiArranging.showTeacherBySubject(subjectTeacherList[i]);
                    break;
                }
            }
        });
        //修改上课时间
        $("body").on("change", ".startTime", function () {
            //WdatePicker({dateFmt:'HH:mm'});
            var index = $("#tableShow").find("tr").index($(this).parent().parent()) - 1;
            var date=$(this).val();
            if(date.length!=11)
            {
                alert("时间格式有误");
                $(this).val(courseConfDTO.classTime[index]);
                return;
            }
            var dateArray=date.split("~");
            if(dateArray.length!=2)
            {
                alert("时间格式有误");
                $(this).val(courseConfDTO.classTime[index]);
                return;
            }
            var date1=dateArray[0];
            var date2=dateArray[1];
            var dateArr1=date1.split(":");
            var dateArr2=date2.split(":");
            if(dateArr1.length!=2||dateArr2.length!=2)
            {
                alert("时间格式有误");
                $(this).val(courseConfDTO.classTime[index]);
                return;
            }
            if(isNaN(dateArr1[0])||isNaN(dateArr1[1])||isNaN(dateArr2[0])||isNaN(dateArr2[1]))
            {
                alert("时间格式有误");
                $(this).val(courseConfDTO.classTime[index]);
                return;
            }
            courseConfDTO.classTime[index] = $(this).val();
        });
    });
    //生成课表结构
    gezhiArranging.generalTable = function (classTime) {
        var classSelectDays = [];
        $(":checkbox[name='classDays']").each(function () {
            if (this.checked) {
                classSelectDays.push(Number($(this).val()));
            }
        });
        var tableData = {
            count: new Array(Number($(".timetable-SE").val())),
            classDays: [1, 2, 3, 4, 5, 6, 7],
            classSelectDays: classSelectDays,
            classTime: classTime,
            events: courseConfDTO.events
        };
        $("#tableShow").empty();
        Common.render({tmpl: $('#tableTempJs'), data: {data: tableData}, context: '#tableShow'});
    };
    //弹窗
    gezhiArranging.openWindow = function (xIndex, yIndex, time) {
        var day = "";
        if (xIndex == 1)
            day = "星期一";
        else if (xIndex == 2)
            day = "星期二";
        else if (xIndex == 3)
            day = "星期三";
        else if (xIndex == 4)
            day = "星期四";
        else if (xIndex == 5)
            day = "星期五";
        else if (xIndex == 6)
            day = "星期六";
        else if (xIndex == 7)
            day = "星期日";
        var data = {
            xIndex: day,
            yIndex: yIndex,
            time: time
        };

        //头部信息
        $("#gringTopShow").empty();
        Common.render({tmpl: $('#gringTopTempJs'), data: {data: data}, context: '#gringTopShow'});
        //个人事务
        var personEvent = [];
        for (var i = 0; i < courseConfDTO.events.length; i++) {
            if (courseConfDTO.events[i].xIndex == xIndex && "第" + courseConfDTO.events[i].yIndex + "节" == yIndex) {
                personEvent = courseConfDTO.events[i].personEvent;
                break;
            }
        }
        $("#eventShow").empty();
        Common.render({tmpl: $('#eventTempJs'), data: {data: personEvent}, context: '#eventShow'});
        //不排课事件
        forbidEvent = [];
        for (var i = 0; i < courseConfDTO.events.length; i++) {
            if (courseConfDTO.events[i].xIndex == xIndex && "第" + courseConfDTO.events[i].yIndex + "节" == yIndex) {
                forbidEvent = courseConfDTO.events[i].forbidEvent;
                break;
            }
        }
        $("#forbieEventShow").empty();
        Common.render({tmpl: $('#forbitEventJs'), data: {data: forbidEvent}, context: '#forbieEventShow'});
    };
    var forbidEvent = [];
    //获取学科以及附属老师
    gezhiArranging.getSubjectUser = function () {
        $.ajax({
            url: "/paike/getSubjectUsers.do",
            type: "post",
            dataType: "json",
            success: function (data) {
                //集体调研
                var subjectList = [];
                for (var i = 0; i < data.length; i++) {
                    //判断是否是选中的
                    var have = 0;
                    for (var j = 0; j < groupStudy.length; j++) {
                        if (groupStudy[j].id == data[i].t.idStr) {
                            have = 1;
                            break;
                        }
                    }
                    subjectList.push({id: data[i].t.idStr, name: data[i].t.value, have: have});
                }
                $("#subjectListShow").empty();
                Common.render({tmpl: $('#subjectListTempJs'), data: {data: subjectList}, context: '#subjectListShow'});
                //个人事务
                $("#pSubjectListShow").empty();
                Common.render({
                    tmpl: $('#pSubjectListTempJs'),
                    data: {data: subjectList},
                    context: '#pSubjectListShow'
                });
                if (data.length > 0) {
                    gezhiArranging.showTeacherBySubject(data[0]);
                }
                for (var i = 0; i < data.length; i++) {
                    subjectTeacherList.push(data[i]);
                }

                if (forbidEvent.length > 0) {
                    $(".groupEvent").attr("disabled", true);
                    $("#pSubjectListShow").attr("disabled", true);
                    $("#pTeacherShow").attr("disabled", true);
                    $(".removeX").css("display", "none");
                    $(".teacherEvent").attr("disabled", true);
                    $(".arranging-TJ").attr("disabled", true);
                }
                else {
                    $(".groupEvent").attr("disabled", false);
                    $("#pSubjectListShow").attr("disabled", false);
                    $("#pTeacherShow").attr("disabled", false);
                    $(".removeX").css("display", "");
                    $(".teacherEvent").attr("disabled", false);
                    $(".arranging-TJ").attr("disabled", false);
                }
            }
        })
    };
    //科目变化教师相应变化
    gezhiArranging.showTeacherBySubject = function (data) {
        var teacherList = [];
        for (var i = 0; i < data.list.length; i++) {
            var obj = data.list[i];
            teacherList.push({id: obj.idStr, name: obj.value});
        }
        $("#pTeacherShow").empty();
        Common.render({tmpl: $('#pTeacherTempJs'), data: {data: teacherList}, context: '#pTeacherShow'});
    };
    var subjectTeacherList = [];
    var courseConfDTO = {
        id: "",
        schoolId: "",
        term: "",
        gradeId: "",
        classDays: [],
        classCount: 8,
        classTime: [],
        events: []
    };
    var courseEventDTO = {
        id: "",
        xIndex: "",
        yIndex: "",
        forbidEvent: [],
        groupStudy: [],
        personEvent: []
    };
    var forbidEvent_base=[];
    var groupStydy_base=[];
    var personEnent_base=[];

    var TeacherEventDTO = {
        id: "",
        subjectId: "",
        subjectName: "",
        teacherName: "",
        teacherId: "",
        event: ""
    };
    var groupStudy = [];
    $(document).ready(function () {
        $("body").on("click", ".fbEvent", function () {
            if ($(this).hasClass("selected")) {
                $(this).removeClass("selected");
                courseEventDTO.forbidEvent.splice($.inArray($(this).attr("value"), courseEventDTO.forbidEvent), 1);
                //下方可见
                $(".groupEvent").attr("disabled", false);
                $("#pSubjectListShow").attr("disabled", false);
                $("#pTeacherShow").attr("disabled", false);
                $(".removeX").css("display", "");
                $(".teacherEvent").attr("disabled", false);
                $(".arranging-TJ").attr("disabled", false);
            }
            else {
                $(".fbEvent").removeClass("selected");
                courseEventDTO.forbidEvent = [];
                $(this).addClass("selected");
                courseEventDTO.forbidEvent.push($(this).text());
                //下方不可见
                $(".groupEvent").attr("disabled", true);
                $("#pSubjectListShow").attr("disabled", true);
                $("#pTeacherShow").attr("disabled", true);
                $(".removeX").css("display", "none");
                $(".teacherEvent").attr("disabled", true);
                $(".arranging-TJ").attr("disabled", true);
            }
        });
        $("body").on("click", ".groupEvent", function () {
            if ($(this).hasClass("selected")) {
                $(this).removeClass("selected");
                for (var i = 0; i < courseEventDTO.groupStudy.length; i++) {
                    if (courseEventDTO.groupStudy[i].id == $(this).attr("value")) {
                        courseEventDTO.groupStudy.splice(i, 1);
                        i--;
                    }
                }
            }
            else {
                $(this).addClass("selected");
                courseEventDTO.groupStudy.push({id: $(this).attr("value"), name: ""});
            }
        });
        $("body").on("click", ".arranging-TJ", function () {
            var subId = $("#pSubjectListShow").val();
            var subName = $("#pSubjectListShow").find("option:selected").text();
            var teaId = $("#pTeacherShow").val();
            var teaName = $("#pTeacherShow").find("option:selected").text();
            var event = $(".teacherEvent").val();
            if (subId != "" && subName != "" && teaId != "" && teaName != "" && event != "") {
                for (var i = 0; i < courseEventDTO.personEvent.length; i++) {
                    if (teaId == courseEventDTO.personEvent[i].teacherId &&
                        subId == courseEventDTO.personEvent[i].subjectId) {
                        alert("该事务已经存在！");
                        return;
                    }
                }
                TeacherEventDTO = {};
                TeacherEventDTO.subjectId = subId;
                TeacherEventDTO.subjectName = subName;
                TeacherEventDTO.teacherId = teaId;
                TeacherEventDTO.teacherName = teaName;
                TeacherEventDTO.event = event;
                courseEventDTO.personEvent.push(TeacherEventDTO);
                gezhiArranging.showPersonEvent(courseEventDTO.personEvent);
                $(".teacherEvent").val("");
            }
            else {
                alert("填写不完整");
            }
        });
        $("body").on("click", ".removeX", function () {
            var obj = $(this).parent();
            var subId = obj.find("td").eq(0).attr("sid");
            var teaId = obj.find("td").eq(1).attr("tid");
            for (var i = 0; i < courseEventDTO.personEvent.length; i++) {
                if (teaId == courseEventDTO.personEvent[i].teacherId &&
                    subId == courseEventDTO.personEvent[i].subjectId) {
                    courseEventDTO.personEvent.splice(i, 1);
                    gezhiArranging.showPersonEvent(courseEventDTO.personEvent);
                    return;
                }
            }
        });
        //自定义不可排课事件
        $("body").on("click","#customBtn",function(){
            $("#custom").show();
            $("#customVal").val("");
        });
        //添加自定义不可排课事件
        $("body").on("click","#customAdd",function(){
            var content=$("#customVal").val();
            if(content=="")
            {
                alert("输入不可为空!");
                return;
            }
            var have=false;
            //判断是都已经有存在
            $(".fbEvent").each(function(){
                var v=$(this).text();
                if(v==content)
                {
                    have=true;
                    return;
                }
            });
            if(have)
            {
                alert("已经存在相同的不可排课事件");
                return;
            }
            //如果已经有选择，去除已经选择的
            $(".fbEvent").removeClass("selected");
            $("#customBtn").remove();
            $("#forbieEventShow").append('<button class="fbEvent selected">'+content+'</button><button id="customBtn">自定义</button>');
            var newForbid=[];
            newForbid.push(content);
            courseEventDTO.forbidEvent=newForbid;
            $("#custom").hide();
            //下方不可见
            $(".groupEvent").attr("disabled", true);
            $("#pSubjectListShow").attr("disabled", true);
            $("#pTeacherShow").attr("disabled", true);
            $(".removeX").css("display", "none");
            $(".teacherEvent").attr("disabled", true);
            $(".arranging-TJ").attr("disabled", true);
        });
        //取消---撤销已修改数据
        $(".arranging-QX,.update-X").click(function () {
            $(".bg").hide();
            $(".gring-CUR").hide();
            //先删除原来的数据，再添加新的数据
            /*for (var i = 0; i < courseConfDTO.events.length; i++) {
                if (courseConfDTO.events[i].xIndex == courseEventDTO.xIndex &&
                    courseConfDTO.events[i].yIndex == courseEventDTO.yIndex) {
                    courseConfDTO.events.splice(i, 1);
                    i--;
                    break;
                }
            }
            courseEventDTO.forbidEvent=forbidEvent_base;
            courseEventDTO.groupStudy=groupStydy_base;
            courseEventDTO.personEvent=personEnent_base;
            courseConfDTO.events.push(courseEventDTO);*/
        });
        //确定
        $("body").on("click", ".arranging-QD", function () {
            $(".bg").hide();
            $(".gring-CUR").hide();
            //先删除原来的数据，再添加新的数据
            for (var i = 0; i < courseConfDTO.events.length; i++) {
                if (courseConfDTO.events[i].xIndex == courseEventDTO.xIndex &&
                    courseConfDTO.events[i].yIndex == courseEventDTO.yIndex) {
                    courseConfDTO.events.splice(i, 1);
                    i--;
                    break;
                }
            }
            if (courseEventDTO.forbidEvent.length > 0) {
                courseEventDTO.groupStudy = [];
                courseEventDTO.personEvent = [];
            }
            courseConfDTO.events.push(courseEventDTO);
            gezhiArranging.generalTable(courseConfDTO.classTime);
            $(".groupEvent").attr("disabled", false);
            $("#pSubjectListShow").attr("disabled", false);
            $("#pTeacherShow").attr("disabled", false);
            $(".removeX").css("display", "");
            $(".teacherEvent").attr("disabled", false);
        });
        $("body").on("click", "#confSave", function () {
            for (var i = 0; i < courseConfDTO.classTime.length; i++) {
                if (courseConfDTO.classTime[i] == undefined || courseConfDTO.classTime[i] == null ||
                    courseConfDTO.classTime[i] == "") {
                    alert("上课时间未设置");
                    return;
                }
            }
            if (confirm("若本年级已经有选课，本次保存课表结构会将已选课表清空，确认本次操作吗？")) {
                //重新整理courseConfDTO,除去超过上课天数的事件

                for(var i=0;i<courseConfDTO.events.length;i++)
                {
                    var x=courseConfDTO.events[i].xIndex;
                    var y=courseConfDTO.events[i].yIndex;
                    var have=false;
                    for(var m=1;m<=courseConfDTO.classCount;m++)
                    {
                        for(var n in courseConfDTO.classDays)
                        {
                            if(x==courseConfDTO.classDays[n]&&y==m)
                            {
                                have=true;
                                break;
                            }
                        }
                        if(have)
                        {
                            break;
                        }
                    }
                    if(!have)//不包含，去除多余事件
                    {
                        courseConfDTO.events.splice(i,1);
                        i--;
                    }
                }
                $.ajax({
                    url: "/paike/addCourseConf.do",
                    type: "post",
                    contentType: "application/json",
                    data: JSON.stringify(courseConfDTO),
                    success: function (resp) {
                        if(resp.code == "200") {
                            alert("添加成功");
                        } else {
                            alert("添加失败");
                        }
                    }
                });
            }
        });
    });
    gezhiArranging.showPersonEvent = function (data) {
        $("#eventShow").empty();
        Common.render({tmpl: $('#eventTempJs'), data: {data: data}, context: '#eventShow'});
    };
    gezhiArranging.init();
    gezhiArranging.getTimetableConf();
    ///////=================================走班排课=========================================
    $(document).ready(function () {
        //走班详情显示
        $("body").on("mouseover", ".Rclass-main-ZB", function () {
            $(this).children("div").show();
        });
        //走班详情隐藏
        $("body").on("mouseout", ".Rclass-main-ZB", function () {
            $(this).children("div").hide();
        });
        //走班详情显示
        $("body").on("mouseover", ".Rclass-main-PE", function () {
            $(this).children("div").show();
        });
        //走班详情隐藏
        $("body").on("mouseout", ".Rclass-main-PE", function () {
            $(this).children("div").hide();
        });
        //删除走班课
        $("body").on("click", ".ZBdelete", function () {
            var x = $(this).attr("x");
            //var x = $(this).parent().parent().parent().attr("x");
            //var y = $(this).parent().parent().parent().attr("y");
            var y = $(this).attr("y");
            var courseId = $(this).attr("courseId").substring(0,$(this).attr("courseId").length-1);
            //var classId=$("#classShow2").val();
            var classIds = "";
            $("#classShow2 option").each(function (i) {
                classIds += this.value + ",";
            });
            if (classIds.length == 0) {
                alert("没有班级");
                return;
            }
            classIds = classIds.substring(0, classIds.length - 1);
            if(lockState==0)//未锁定
                gezhiArranging.removeCourse(classIds, x, y, courseId);
        });
        //确认排课
        $("body").on("click", ".Rclass-main-Green", function () {
            //if (confirm("确定要排在该位置吗？")) {
                var classIds = "";
                $("#classShow2 option").each(function (i) {
                    classIds += this.value + ",";
                });
                if (classIds.length == 0) {
                    alert("没有班级");
                    return;
                }
                classIds = classIds.substring(0, classIds.length - 1);
                //var classId=$("#classShow2").val();
                var courseConfId = $("#classConfId").val();
                selectXIndex = Number($(this).attr("x"));
                selectYIndex = Number($(this).attr("y"));
                gezhiArranging.addCourse(classIds, selectXIndex, selectYIndex, selectCourseId, selectRoomId, selectTeacherId, courseConfId);
            //}
        });
        //询问是否排在调研时段
        $("body").on("click", ".Rclass-main-JY", function () {
            $(".bg").show();
            $(".Rclass-JY-wind").show();
            selectXIndex = Number($(this).attr("x"));
            selectYIndex = Number($(this).attr("y"));
        });
        //筛选
        $("body").on("click", "#selectValue", function () {
            //gezhiArranging.getXZBPoint();
            gezhiArranging.showDetailTable($("#groupShow2").val());
        });
        //锁定/解锁课表
        $("body").on("click", "#lockTable", function () {
            var classIds = "";
            $("#classShow2 option").each(function (i) {
                classIds += this.value + ",";
            });
            if (classIds.length == 0) {
                alert("没有班级");
                return;
            }
            classIds = classIds.substring(0, classIds.length - 1);
            if ($("#lockTable").text() == "未锁定(锁定)")//执行锁定
            {
                $.ajax({
                    url: "/paike/lockTable.do",
                    type: "post",
                    dataType: "json",
                    data: {
                        term: $("#termVal").val(),
                        classId: classIds,
                        groupId: $("#groupShow2").val(),
                        gradeId: $("#gradeId").val()
                    },
                    success: function (data) {
                        if (data.result == "SUCCESS") {
                            $("#lockTable").text("已锁定(解锁)");
                            gezhiArranging.lockTable();
                        }
                        else {
                            alert("走班课程全部排完之后才可以锁定");
                        }
                    }
                });
            }
            else if ($("#lockTable").text() == "已锁定(解锁)") {
                if (confirm("解锁课表将会删除本段内所有班级的非走班课表以及小走班课程，是否执行？")) {
                    $.ajax({
                        url: "/paike/unLockTable.do",
                        type: "post",
                        dataType: "json",
                        data: {
                            term: $("#termVal").val(),
                            classId: classIds,
                            gradeId: $("#gradeId").val()
                        },
                        success: function (data) {
                            if (data.result == "SUCCESS") {
                                $("#lockTable").text("未锁定(锁定)");
                                gezhiArranging.unlockTable();
                                gezhiArranging.showDetailTable($("#groupShow2").val());
                            }
                            else {
                                alert("解锁失败");
                            }
                        }
                    });
                }
            }
        });
    });

    gezhiArranging.showDetailTable = function (groupId) {
        var classId = $("#classShow2").val();
        if (classId == "" || classId == null) {
            alert("没有班级数据！");
            return;
        }
        else {
            $.ajax({
                url: "/paike/courseArrangeShowForGezhi.do",
                type: "post",
                dataType: "json",
                data: {
                    term: $("#termVal").val(),
                    classId: classId,
                    groupId: groupId
                },
                success: function (data) {
                    $("#classConfId").val(data.courseConf.id);
                    courseConfDTO2 = data.courseConf;
                    choosedCourse = data.chooseCourse.courseList;
                    unArrangeData = data.unChooseCourse;
                    gezhiArranging.getUnArrageCourse();
                    availablePoint = [];
                    gezhiArranging.generalData(0);
                    var lock = data.lock;
                    if (lock == 1) {
                        $("#lockTable").text("已锁定(解锁)");
                        gezhiArranging.lockTable();
                    }
                    else {
                        $("#lockTable").text("未锁定(锁定)");
                        gezhiArranging.unlockTable();
                    }
                }
            });
            /*$.ajax({
                url: "/paike/getXZBPoint.do",
                type: "post",
                dataType: "json",
                data: {
                    year: $("#termVal").val(),
                    gradeId:$("#gradeId").val()
                },
                success: function (data) {
                    $("#alertShow2").text("提醒：浅绿色标注位置代表预留的小走班排课位置，当前共有小走班位置 "+data.length+"个");
                }
            });*/
        }
    };
    //获取年假、段、班级
    gezhiArranging.getGradeGroupClass = function () {
        $.ajax({
            url: "/paike/getGradeDuanClass.do",
            type: "post",
            dataType: "json",
            data: {
                term: $("#termVal").val(),
                gradeId: $("#gradeId").val()
            },
            success: function (data) {
                gradeGroupClass = data;
                groupList = [];
                if (gradeGroupClass.length > 0) {
                    var list = gradeGroupClass[0].groupInfo;
                    for (var i = 0; i < list.length; i++) {
                        groupList.push({
                            groupId: list[i].groupId, groupName: "第" + list[i].group + "段",
                            groupIndex: list[i].group, classList: list[i].courseInfo
                        });
                    }
                }
                $("#groupShow2").empty();
                Common.render({tmpl: $('#groupTempJs2'), data: {data: groupList}, context: '#groupShow2'});
                classList = [];
                if (groupList.length > 0) {
                    var map = groupList[0].classList;
                    $.each(map, function (key, values) {
                        classList.push({classId: key, className: values});
                    });
                }
                $("#classShow2").empty();
                Common.render({tmpl: $('#classTempJs2'), data: {data: classList}, context: '#classShow2'});
                gezhiArranging.showDetailTable($("#groupShow2").val());
            }
        });
    };

    var gradeGroupClass = [];
    var groupList = [];
    var classList = [];
    var courseConfDTO2 = {
        id: "",
        schoolId: "",
        term: "",
        gradeId: "",
        classDays: [],
        classCount: 8,
        classTime: [],
        events: []
    };
    var groupStudy = [];
    var xzbPoint=[];
    function getGroupStudy(subjectId) {
        groupStudy = [];
        for (var i = 0; i < courseConfDTO2.events.length; i++) {
            var map = courseConfDTO2.events[i].groupStudy;
            $.each(map, function (key, values) {
                if (values.id == subjectId) {
                    groupStudy.push({x: courseConfDTO2.events[i].xIndex, y: courseConfDTO2.events[i].yIndex});
                }
            });
        }
    }

    gezhiArranging.generalData = function (state) {
        var paikeDate = {};
        paikeDate.id = courseConfDTO2.id;
        paikeDate.schoolId = courseConfDTO2.schoolId;
        paikeDate.term = courseConfDTO2.term;
        paikeDate.gradeId = courseConfDTO2.gradeId;
        paikeDate.classDays = courseConfDTO2.classDays.sort();
        paikeDate.classCount = courseConfDTO2.classCount;
        paikeDate.classTime = courseConfDTO2.classTime;
        paikeDate.state = state;//0查看，1选课
        if (availablePoint.length == 0) {
            paikeDate.state = 0;
        }
        paikeDate.detail = [];
        for (var i = 0; i < paikeDate.classDays.length; i++) {
            var x = paikeDate.classDays[i];
            for (var j = 1; j <= paikeDate.classCount; j++) {
                var y = j;
                var type = 3;//0走班 1小走班 2非走班 3 无课
                var courseList = [];
                var isOk = 0;//0不可排课，1可以排课,2 集体调研  询问,3小走班预留
                var fb = [];//不可排课事件
                var gs = [];//集体调研事件
                var te = [];//个人事务
                var xzb = 0;//1表示是小走班预留点
                var subjectName = "";
                //遍历availablePoint
                for (var m = 0; m < availablePoint.length; m++) {
                    if (availablePoint[m].x == x && availablePoint[m].y == y) {
                        isOk = 1;
                        //availablePoint.splice(m,1);
                        //m--;
                        break;
                    }
                }
                //遍历choosedCourse
                for (var n = 0; n < choosedCourse.length; n++) {
                    if (choosedCourse[n].xIndex == x && choosedCourse[n].yIndex == y && choosedCourse[n].type != 3 &&
                        choosedCourse[n].type != 4) {
                        courseList = choosedCourse[n].courseIdList;
                        type = choosedCourse[n].type;
                        if(type==5)
                            type=2;
                        subjectName = choosedCourse[n].subjectName;
                        //choosedCourse.splice(n,1);
                        //n--;
                        break;
                    }
                }
                //遍历 groupStudy
                for (var p = 0; p < groupStudy.length; p++) {
                    if (groupStudy[p].x == x && groupStudy[p].y == y && isOk == 1) {
                        isOk = 2;
                        //groupStudy.splice(p,1);
                        //p--;
                        break;
                    }
                }
                //遍历 courseConfDTO2
                for (var k = 0; k < courseConfDTO2.events.length; k++) {
                    if (courseConfDTO2.events[k].xIndex == x && courseConfDTO2.events[k].yIndex == y) {
                        fb = courseConfDTO2.events[k].forbidEvent;
                        gs = courseConfDTO2.events[k].groupStudy;
                        te = courseConfDTO2.events[k].personEvent;
                        break;
                    }
                }
                //遍历小走班预留点
                for(var m=0;m<xzbPoint.length;m++)
                {
                    if(xzbPoint[m].x==x&&xzbPoint[m].y==y && isOk==1)
                    {
                        isOk=3;
                        break;
                    }
                }
                paikeDate.detail.push({
                    x: x, y: y, type: type, courseList: courseList, isOk: isOk,
                    fb: fb, gs: gs, te: te, subjectName: subjectName
                });
            }
        }

        $("#tableShow2").empty();
        Common.render({tmpl: $('#tableTempJs2'), data: {data: paikeDate}, context: '#tableShow2'});
        $(".Rclass-ZB-SJ").css("left", $(".ZB_table_base").width() + 2);
        $(".Rclass-ZB-SJJ").css("left", $(".ZB_table_base").width() + 1);
        $(".Rclass-ZB-TC").css("left", $(".ZB_table_base").width() + 24);
        $(".Rclass-JY-SJ").css("left", $(".ZB_table_base").width() + 2);
        $(".Rclass-JY-SJJ").css("left", $(".ZB_table_base").width() + 1);
        $(".Rclass-JY-TC").css("left", $(".ZB_table_base").width() + 24);
       /* $(".zbtr").find("td").each(function(i)
        {
            for(var m=0;m<xzbPoint.length;m++)
            {
                if(xzbPoint[m].x==$(".zbtr").find("td").eq(i).attr("x")&&
                    xzbPoint[m].y==$(".zbtr").find("td").eq(i).attr("y"))
                {
                    //$(this).addClass("xzbPointTag");
                    $(this).css("background-color","#8EF50B");
                }
            }
        });*/
    };
    //锁定表格
    gezhiArranging.lockTable = function () {
        //$(".removeCourseX").hide();
        lockState=1;
        $("body").off("click", ".unArrangeList");
    };
    //解锁表格
    gezhiArranging.unlockTable = function () {
        lockState=0;
        //$(".removeCourseX").show();
        $("body").off("click", ".unArrangeList");
        $("body").on("click", ".unArrangeList", function () {
            gezhiArranging.showUnarrangeClass($(this));
        });
    };

    //获取可以排课的时间点
    gezhiArranging.getAvailablePoint = function (classId, courseId, classRoomId, teacherId, courseConfId) {
        $.ajax({
            url: "/paike/getAvailablePointForZB.do",
            type: "post",
            dataType: "json",
            data: {
                term: $("#termVal").val(),
                classId: classId,
                courseId: courseId,
                classRoomId: classRoomId,
                teacherId: teacherId,
                courseConfId: courseConfId
            },
            success: function (data) {
                availablePoint = data;
                gezhiArranging.generalData(1);
            }
        });
    };

    //获取小走班预留时间点
    /*gezhiArranging.getXZBPoint = function()
    {
        $.ajax({
            url: "/paike/getXZBPoint.do",
            type: "post",
            dataType: "json",
            data: {
                year: $("#termVal").val(),
                gradeId:$("#gradeId").val()
            },
            success: function (data) {
                xzbPoint=data;
                $("#alertShow2").text("提醒：浅绿色标注位置代表预留的小走班排课位置，当前共有小走班位置 "+data.length+"个");
            }
        });
    };*/
    //添加课程
    gezhiArranging.addCourse = function (classId, xIndex, yIndex, courseId, classRoomId, teacherId, courseConfId) {
        $.ajax({
            url: "/paike/courseAddForGezhi.do",
            type: "post",
            dataType: "json",
            data: {
                term: $("#termVal").val(),
                classId: classId,
                xIndex: xIndex,
                yIndex: yIndex,
                courseId: courseId,
                classRoomId: classRoomId,
                teacherId: teacherId,
                courseConfId: courseConfId,
                index: 0,
                groupId: $("#groupShow2").val()
            },
            success: function (data) {
                if (data.result == false)
                    alert("添加课程失败");
                else {
                    $("#classConfId").val(data.courseConf.id);
                    courseConfDTO2 = data.courseConf;
                    choosedCourse = data.chooseCourse.courseList;
                    unArrangeData = data.unChooseCourse;
                    gezhiArranging.getUnArrageCourse();
                    availablePoint = [];
                    gezhiArranging.generalData(0);
                    //统计小走班个数
                    $.ajax({
                        url: "/paike/getXZBPoint.do",
                        type: "post",
                        dataType: "json",
                        data: {
                            year: $("#termVal").val(),
                            gradeId:$("#gradeId").val()
                        },
                        success: function (data) {
                            $("#alertShow2").text("提醒：浅绿色标注位置代表预留的小走班排课位置，当前共有小走班位置 "+data.length+"个");
                        }
                    });
                }
            }
        })
    };
    //删除课程
    gezhiArranging.removeCourse = function (classId, xIndex, yIndex, courseId) {
        $.ajax({
            url: "/paike/courseRemoveForGezhi.do",
            type: "post",
            dataType: "json",
            data: {
                term: $("#termVal").val(),
                classId: classId,
                xIndex: xIndex,
                yIndex: yIndex,
                courseId: courseId,
                index: 0,
                groupId: $("#groupShow2").val()
            },
            success: function (data) {
                if (data.result == false)
                    alert("删除课程失败");
                else {

                    $("#classConfId").val(data.courseConf.id);
                    courseConfDTO2 = data.courseConf;
                    choosedCourse = data.chooseCourse.courseList;
                    unArrangeData = data.unChooseCourse;
                    gezhiArranging.getUnArrageCourse();
                    availablePoint = [];
                    gezhiArranging.generalData(0);
                }
            }
        })
    };
    var unArrangeData = [];
    //待排课程显示
    gezhiArranging.getUnArrageCourse = function () {
        Common.render({
            tmpl: $('#unArrangeCourseTempJs2'),
            data: {data: unArrangeData},
            context: '#unArrangeCourseShow2',
            overwrite: 1
        });
    };
    var lockState=0;//未锁定
    //arranging.getUnArrageCourse();
    var choosedCourse = [];
    var availablePoint = [];
    var zoubanCourseConf = {
        "courseConf": courseConfDTO2,
        "chooseCourse": {
            schoolId: "xxx", courseList: [{id: "xx", xIndex: 2, yIndex: 4, courseIdList: ["211", "222", "333"]},
                {id: "xx", xIndex: 4, yIndex: 4, courseIdList: ["211", "222"]}]
        },
        "unChooseCourse": [],
        "pointList": [{x: 1, y: 2}, {x: 3, y: 4}]
    };
    //走班选课年级显示
    gezhiArranging.showGradeGroupClass = function () {
        $("#gradeShow2").empty();
        Common.render({tmpl: $('#gradeTempJs2'), data: {data: gradeGroupClass}, context: '#gradeShow2'});
        groupList = [];
        if (gradeGroupClass.length > 0) {
            var list = gradeGroupClass[0].groupInfo;
            for (var i = 0; i < list.length; i++) {
                groupList.push({
                    groupId: list[i].groupId, groupName: "第" + list[i].group + "段",
                    groupIndex: list[i].group, classList: list[i].courseInfo
                });
            }
        }
        $("#groupShow2").empty();
        Common.render({tmpl: $('#groupTempJs2'), data: {data: groupList}, context: '#groupShow2'});
        classList = [];
        if (groupList.length > 0) {
            var map = groupList[0].classList;
            $.each(map, function (key, values) {
                classList.push({classId: key, className: values});
            });
        }
        $("#classShow2").empty();
        Common.render({tmpl: $('#classTempJs2'), data: {data: classList}, context: '#classShow2'});
    };
    //未排课程显示
    gezhiArranging.showUnarrangeClass = function (target) {
        if (target.hasClass("Rclass-Hov")) {
            target.removeClass("Rclass-Hov");
            gezhiArranging.showDetailTable($("#groupShow2").val());
            groupStudy = [];
            return;
        }
        target.addClass("Rclass-Hov").siblings().removeClass("Rclass-Hov");
        getGroupStudy(target.attr("sid"));

        //执行获取可排时间点
        var classId = $("#classShow2").val();
        var courseId = target.attr("cid");
        var classRoomId = target.attr("crid");
        var teacherId = target.attr("tid");
        var courseConfId = $("#classConfId").val();
        selectCourseId = courseId;
        selectRoomId = classRoomId;
        selectTeacherId = teacherId;
        gezhiArranging.getAvailablePoint(classId, courseId, classRoomId, teacherId, courseConfId);
    };
    $(document).ready(function () {
        //走班选课班级显示
        $("body").on("change", "#groupShow2", function () {
            var groupId = $(this).val();
            classList = [];
            for (var i = 0; i < groupList.length; i++) {
                if (groupList[i].groupId == groupId) {
                    var map = groupList[i].classList;
                    $.each(map, function (key, values) {
                        classList.push({classId: key, className: values});
                    });
                    break;
                }
            }
            $("#classShow2").empty();
            Common.render({tmpl: $('#classTempJs2'), data: {data: classList}, context: '#classShow2'});
        });
        //未排课程显示
        $("body").on("click", ".unArrangeList", function () {
            gezhiArranging.showUnarrangeClass($(this));
        });
        //调研时段继续排课
        $("body").on("click", ".submitData", function () {
            //var classId=$("#classShow2").val();
            /*var classIds = "";
            $("#classShow2 option").each(function (i) {
                classIds += this.value + ",";
            });
            if (classIds.length == 0) {
                alert("没有班级");
                return;
            }
            classIds = classIds.substring(0, classIds.length - 1);
            var courseConfId = $("#classConfId").val();

            gezhiArranging.addCourse(classIds, selectXIndex, selectYIndex, selectCourseId, selectRoomId, selectTeacherId, courseConfId);*/
            $(".Rclass-JY-wind").hide();
            $(".bg").hide();
        });
    });
    var selectCourseId = "";
    var selectRoomId = "";
    var selectTeacherId = "";
    var selectXIndex = 1;
    var selectYIndex = 1;


    //================================================非走班==========================================================
    //获取年级、班级列表
    gezhiArranging.getGradeClassList = function (index) {
        $.ajax({
            url: "/paike/getGradeClassList.do",
            type: "post",
            dataType: "json",
            data: {
                gradeId: $("#gradeId").val()
            },
            success: function (data) {
                fzbGradeClassList = data;
                gezhiArranging.showGradeClass(index);
                gezhiArranging.showDetailTable4(true,index);
            }
        });
    };
    var fzbGradeClassList = [];
    var fzbClassList = [];
    gezhiArranging.showGradeClass = function (index) {
        /*$("#gradeShow4").empty();
         Common.render({tmpl: $('#gradeTempJs4'), data: {data: fzbGradeClassList}, context: '#gradeShow4'});*/
        fzbClassList = [];
        if (fzbGradeClassList.length > 0) {
            var map = fzbGradeClassList[0].classInfo;
            $.each(map, function (key, value) {
                fzbClassList.push({classId: key, className: value});
            });
        }
        if (index == 4) {

            $("#classShow4").empty();
            Common.render({tmpl: $('#classTempJs4'), data: {data: fzbClassList}, context: '#classShow4'});
        }
        else if(index==5) {
            $("#classShow5").empty();
            Common.render({tmpl: $('#classTempJs5'), data: {data: fzbClassList}, context: '#classShow5'});
        }
    };
    gezhiArranging.autoArrange = function () {
        $.ajax(
            {
                url: "/paike/autoArrangeCourse.do",
                type: "post",
                dataType: "json",
                data: {
                    term: $("#termVal").val(),
                    classId: $("#classShow4").val(),
                    gradeId:$("#gradeId").val(),
                    courseConfId: $("#classConfId").val(),
                    type: autoArrangeType
                },
                success: function (data) {
                    if (data.code == "500") {
                        gezhiArranging.showDetailTable4(false,4);
                        $("#FZBshow p").text("提示：自动排课失败，请尝试手动排课!");
                        $("#FZBshow").css("display", "block");
                        return;
                    }
                    if (data.result == false) {
                        gezhiArranging.showDetailTable4(false,4);
                        $("#FZBshow p").text("提示：自动排课失败，请尝试手动排课!");
                        $("#FZBshow").css("display", "block");
                    }
                    else {
                        $("#FZBshow").css("display", "none");
                        $("#classConfId").val(data.courseConf.id);
                        courseConfDTO2 = data.courseConf;
                        choosedCourse = data.chooseCourse.courseList;
                        unArrangeData = data.unChooseCourse;
                        gezhiArranging.getUnArrageCourse4();
                        availablePoint = [];
                        gezhiArranging.generalData4(0,4);
                    }
                }
            }
        );
    };
    $(document).ready(function () {
        //非走班
        $("body").on("click", "#selectFeizouban", function () {
            gezhiArranging.showDetailTable4(true,4);
        });
        //其他走班
        $("body").on("click", "#selectFeizouban5", function () {
            gezhiArranging.showDetailTable4(true,5);
        });
        //其他走班一键排课
        $("body").on("click", "#autoArrange5", function () {
            gezhiArranging.autoSortOther();
        });
        //其他走班一键清空
        $("body").on("click", "#autoClear5", function () {
            gezhiArranging.autoClearOther();
        });
        //非走班详情显示
        $("body").on("mouseover", ".Nclass-main-ZB", function () {
            $(this).children("div").show();
        });
        $("body").on("mouseout", ".Nclass-main-ZB", function () {
            $(this).children("div").hide();
        });
        //非走班详情显示
        $("body").on("mouseover", ".Nclass-main-PE", function () {
            $(this).children("div").show();
        });
        //非走班详情隐藏
        $("body").on("mouseout", ".Nclass-main-PE", function () {
            $(this).children("div").hide();
        });
        $("body").on("click", "#autoArrange", function () {
            gezhiArranging.autoArrange();
        });
        //非走班删除课程
        $("body").on("click", ".feizoubanDel", function () {
            //if (confirm("确定移除该课程吗？")) {
                $.ajax({
                    url: "/paike/courseRemove.do",
                    type: "post",
                    dataType: "json",
                    data: {
                        term: $("#termVal").val(),
                        classId: $("#classShow4").val(),
                        xIndex: Number($(this).attr("x")),
                        yIndex: Number($(this).attr("y")),
                        courseId: $(this).attr("cid"),
                        index: 2,
                        groupId: ""
                    },
                    success: function (data) {
                        gezhiArranging.showDetailTable4(true,4);
                    }
                });
            //}
        });
        //非走班添加课程
        $("body").on("click", ".feizoubanAdd", function () {
            //if (confirm("确定在该位置排课吗？")) {
                var classIds = $("#classShow4").val();
                var courseConfId = $("#classConfId").val();
                selectXIndex = Number($(this).attr("x"));
                selectYIndex = Number($(this).attr("y"));
            gezhiArranging.addCourse4(classIds, selectXIndex, selectYIndex, selectCourseId, selectRoomId, selectTeacherId, courseConfId);

           // }
        });
        //非走班排课
        $("body").on("click", ".unArrangeListFZB", function () {
            gezhiArranging.unArrangeFZBShow($(this));
        });
        //询问是否排在调研时段
        $("body").on("click", ".Nclass-main-JY", function () {
            $(".bg").show();
            $(".Rclass-JY-wind2").show();
            selectXIndex = Number($(this).attr("x"));
            selectYIndex = Number($(this).attr("y"));
        });
        //调研时段继续排课
        $("body").on("click", ".submitData2", function () {
            /*var classId = $("#classShow4").val();
            var courseConfId = $("#classConfId").val();

            gezhiArranging.addCourse4(classId, selectXIndex, selectYIndex, selectCourseId, selectRoomId, selectTeacherId, courseConfId);*/
            $(".Rclass-JY-wind2").hide();
            $(".bg").hide();
        });
    });
    gezhiArranging.unArrangeFZBShow = function (target) {
        if (target.hasClass("Rclass-Hov")) {
            target.removeClass("Rclass-Hov");
            gezhiArranging.showDetailTable4(true,4);
            groupStudy = [];
            return;
        }
        target.addClass("Rclass-Hov").siblings().removeClass("Rclass-Hov");
        getGroupStudy(target.attr("sid"));

        //执行获取可排时间点
        var classId = $("#classShow4").val();
        var courseId = target.attr("cid");
        var classRoomId = target.attr("crid");
        var teacherId = target.attr("tid");
        var courseConfId = $("#classConfId").val();
        selectCourseId = courseId;
        selectRoomId = classRoomId;
        selectTeacherId = teacherId;
        gezhiArranging.getAvailablePoint4(classId, courseId, classRoomId, teacherId, courseConfId);
    };
    //添加非走班课程
    gezhiArranging.addCourse4 = function (classId, xIndex, yIndex, courseId, classRoomId, teacherId, courseConfId) {
        $.ajax({
            url: "/paike/fzbCourseAdd.do",
            type: "post",
            dataType: "json",
            data: {
                term: $("#termVal").val(),
                classId: classId,
                xIndex: xIndex,
                yIndex: yIndex,
                courseId: courseId,
                classRoomId: classRoomId,
                teacherId: teacherId,
                courseConfId: courseConfId,
                index: 2,
                groupId: ""
            },
            success: function (data) {
                if (data.result == false)
                    alert("添加课程失败");
                else {
                    gezhiArranging.showDetailTable4(true,4);
                }
            }
        })
    };
    //获取可以排课的时间点
    gezhiArranging.getAvailablePoint4 = function (classId, courseId, classRoomId, teacherId, courseConfId) {
        $.ajax({
            url: "/paike/getAvailablePointFZB.do",
            type: "post",
            dataType: "json",
            data: {
                term: $("#termVal").val(),
                classId: classId,
                courseId: courseId,
                classRoomId: classRoomId,
                teacherId: teacherId,
                courseConfId: courseConfId
            },
            success: function (data) {
                availablePoint = data;
                gezhiArranging.generalData4(1,4);
            }
        });
    };

    gezhiArranging.showDetailTable4 = function (arrangeVal,index) {
        var classId = $("#classShow4").val();
        if(index==5) {
            classId = $("#classShow5").val();
        }
        if (classId == "" || classId == null) {
            alert("没有班级数据！");
            return;
        }
        else {
            $.ajax({
                url: "/paike/courseArrangeShow.do",
                type: "post",
                dataType: "json",
                data: {
                    term: $("#termVal").val(),
                    classId: classId,
                    index: 2,
                    groupId: ""
                },
                success: function (data) {
                    $("#classConfId").val(data.courseConf.id);
                    courseConfDTO2 = data.courseConf;
                    choosedCourse = data.chooseCourse.courseList;
                    unArrangeData = data.unChooseCourse;
                    gezhiArranging.getUnArrageCourse4();
                    availablePoint = [];
                    gezhiArranging.generalData4(0,index);
                    var lock = data.lock;
                    if (lock == 0 && $("#mode").val()!="zhuZhouIndex") {
                        $("body").off("click", ".unArrangeListFZB");
                        $("#FZBshow p").text("提示：走班课锁定之后才可以进行非走班课排课！");
                        $("#FZBshow").css("display", "block");
                        $("body").off("click", "#autoArrange");
                    }
                    else {
                        if (arrangeVal)
                            $("#FZBshow").css("display", "none");
                        $("body").off("click", ".unArrangeListFZB");
                        $("body").on("click", ".unArrangeListFZB", function () {
                            gezhiArranging.unArrangeFZBShow($(this));
                        });
                        $("body").off("click", "#autoArrange");
                        $("body").on("click", "#autoArrange", function () {
                            gezhiArranging.autoArrange();
                        });
                    }
                }
            });
        }
    };
    gezhiArranging.generalData4 = function (state,index) {
        var paikeDate = {};
        paikeDate.id = courseConfDTO2.id;
        paikeDate.schoolId = courseConfDTO2.schoolId;
        paikeDate.term = courseConfDTO2.term;
        paikeDate.gradeId = courseConfDTO2.gradeId;
        paikeDate.classDays = courseConfDTO2.classDays.sort();
        paikeDate.classCount = courseConfDTO2.classCount;
        paikeDate.classTime = courseConfDTO2.classTime;
        paikeDate.state = state;//0查看，1选课
        if (availablePoint.length == 0) {
            paikeDate.state = 0;
        }
        paikeDate.detail = [];
        for (var i = 0; i < paikeDate.classDays.length; i++) {
            var x = paikeDate.classDays[i];
            for (var j = 1; j <= paikeDate.classCount; j++) {
                var y = j;
                var type = 3;//0走班 1小走班 2非走班 3 无课
                var courseList = [];
                var isOk = 0;//0不可排课，1可以排课,2 集体调研  询问
                var fb = [];//不可排课事件
                var gs = [];//集体调研事件
                var te = [];//个人事务
                var subjectName = "";
                //遍历availablePoint
                for (var m = 0; m < availablePoint.length; m++) {
                    if (availablePoint[m].x == x && availablePoint[m].y == y) {
                        isOk = 1;
                        //availablePoint.splice(m,1);
                        //m--;
                        break;
                    }
                }
                //遍历choosedCourse
                for (var n = 0; n < choosedCourse.length; n++) {
                    if (choosedCourse[n].xIndex == x && choosedCourse[n].yIndex == y && choosedCourse[n].type != 3) {
                        courseList = choosedCourse[n].courseIdList;
                        type = choosedCourse[n].type;
                        if(type==5)
                            type=2;
                        subjectName = choosedCourse[n].subjectName;
                        break;
                    }
                }
                //遍历 groupStudy
                for (var p = 0; p < groupStudy.length; p++) {
                    if (groupStudy[p].x == x && groupStudy[p].y == y && isOk == 1) {
                        isOk = 2;
                        //groupStudy.splice(p,1);
                        //p--;
                        break;
                    }
                }
                //遍历 courseConfDTO2
                for (var k = 0; k < courseConfDTO2.events.length; k++) {
                    if (courseConfDTO2.events[k].xIndex == x && courseConfDTO2.events[k].yIndex == y) {
                        fb = courseConfDTO2.events[k].forbidEvent;
                        gs = courseConfDTO2.events[k].groupStudy;
                        te = courseConfDTO2.events[k].personEvent;
                        break;
                    }
                }
                paikeDate.detail.push({
                    x: x, y: y, type: type, courseList: courseList, isOk: isOk,
                    fb: fb, gs: gs, te: te, subjectName: subjectName
                });
            }
        }
        for (var n = 0; n < choosedCourse.length; n++) {
            if (choosedCourse[n].type == 3) {
                for (var k = 0; k < paikeDate.detail.length; k++) {
                    var obj = paikeDate.detail[k];
                    if (obj.x == choosedCourse[n].xIndex && obj.y == choosedCourse[n].yIndex) {
                        for (var p = 0; p < choosedCourse[n].courseIdList.length; p++) {
                            if(obj.courseList.indexOf(choosedCourse[n].courseIdList[p])==-1)
                                obj.courseList.push(choosedCourse[n].courseIdList[p]);
                        }
                        obj.subjectName += "/选修";
                        break;
                    }
                }
            }
        }
        if(index==4) {
            $("#tableShow4").empty();
            Common.render({tmpl: $('#tableTempJs4'), data: {data: paikeDate}, context: '#tableShow4'});
        }
        else {
            $("#tableShow5").empty();
            Common.render({tmpl: $('#tableTempJs5'), data: {data: paikeDate}, context: '#tableShow5'});
        }
        $(".Nclass-ZB-SJ").css("left", $(".ZB_table_base").width() + 2);
        $(".Nclass-ZB-SJJ").css("left", $(".ZB_table_base").width() + 1);
        $(".Nclass-ZB-TC").css("left", $(".ZB_table_base").width() + 24);
        $(".Nclass-JY-SJ").css("left", $(".ZB_table_base").width() + 2);
        $(".Nclass-JY-SJJ").css("left", $(".ZB_table_base").width() + 1);
        $(".Nclass-JY-TC").css("left", $(".ZB_table_base").width() + 24);
    };
    gezhiArranging.getUnArrageCourse4 = function () {
        $("#unArrangeCourseShow4").empty();
        Common.render({
            tmpl: $('#unArrangeCourseTempJs4'),
            data: {data: unArrangeData},
            context: '#unArrangeCourseShow4'
        });
    };
    var autoArrangeType = 0;//0需要考虑每天一节课，1不需要考虑

    //=============================小走班选课=====================================
    //获取小走班已排列表
    gezhiArranging.getXZBcourse = function (tag) {
        $.ajax({
            url: "/timetable/xzbCourseTable.do",
            type: "post",
            dataType: "json",
            data: {
                gradeId: $("#gradeId").val(),
                year: $("#termVal").val()
            },
            success: function (data) {
                var courseList = [];
                var allCourseList = [];
                for (var i = 0; i < data.conf.classDays.length; i++) {
                    var x = data.conf.classDays[i];
                    for (var y = 1; y <= data.conf.classCount; y++) {
                        var have = false;
                        for (var k = 0; k < data.course.length; k++) {
                            if (data.course[k].xIndex == x &&
                                data.course[k].yIndex == y) {
                                courseList.push({
                                    xIndex: x,
                                    yIndex: y,
                                    courseIdList: data.course[k].courseIdList,
                                    name: "小走班"
                                });
                                for (var j = 0; j < data.course[k].courseIdList.length; j++) {
                                    var ctr = data.course[k].courseIdList[j];
                                    allCourseList.push({
                                        xIndex: x,
                                        yIndex: y,
                                        courseName: ctr.courseName,
                                        studentCount: ctr.conflictCount,
                                        teacherName: ctr.teacherName,
                                        classroomName: ctr.classRoom,
                                        teacherId: ctr.teacherId,
                                        classroomId: ctr.classRoomId,
                                        subjectId: ctr.subjectId,
                                        courseId: ctr.courseId
                                    });
                                }
                                have = true;
                                break;
                            }
                        }
                        if (!have) {
                            courseList.push({xIndex: x, yIndex: y, courseIdList: [], name: ""});
                        }
                    }
                }
                $("#FZBcourseShow").empty();
                Common.render({
                    tmpl: $('#tableTempFZBJs'),
                    data: {data: data, course: courseList},
                    context: '#FZBcourseShow'
                });
                $(".Nclass-ZB-SJ").css("left", $(".ZB_table_base").width());
                $(".Nclass-ZB-SJJ").css("left", $(".ZB_table_base").width());
                $(".Nclass-ZB-TC").css("left", $(".ZB_table_base").width() + 20);
                $("#courseListShow").empty();
                Common.render({tmpl: $('#courseListTempJs'), data: {data: allCourseList}, context: '#courseListShow'});
                if (tag == 1) {
                    $("#tab-Xclass").hide();
                    $("#xzbSet").show();
                }
            }
        });
    };
    $(document).ready(function () {
        $("body").on("click", ".setTeaCRxzb", function () {
            $("#tab-Xclass").hide();
            $("#xzbSet").show();
        });
        $("body").on("click", "#finsihBtn", function () {
            $("#tab-Xclass").show();
            $("#xzbSet").hide();
        });
        $("body").on("click", ".setting", function () {
            $(".xzbSetEl").show();
            var subjectId = $(this).attr("sid");
            var teacherId = $(this).attr("tid");
            var classroomId = $(this).attr("rid");
            var courseId = $(this).attr("coid");
            var courseName = $(this).attr("nm");
            var x = $(this).attr("x");
            var y = $(this).attr("y");
            var gradeId = $("#gradeId").val();
            if (courseName == "自习") {
                gezhiArranging.getAllTeacherList(teacherId, x, y);
            }
            else {
                gezhiArranging.getTeacherList(subjectId, teacherId);
            }
            gezhiArranging.findClassroomList(classroomId, x, y);
            $("#coursenm").text(courseName);
            $("#courseType").val("xzb");
            $("#courseId").val(courseId);
            $(".edit-set-div").show();
            $(".bg").show();
        });
        //查看学生
        $("body").on("click", ".view", function () {
            var courseId = $(this).attr("coid");
            $(".clash-detial").show();
            $(".bg").show();
            $(".p1").text($(this).parent().find("td").eq(1).text());
            $("#teacher").text("老师：" + $(this).parent().find("td").eq(3).text());
            $("#classRoom").text("上课时间:" + $(this).parent().find("td").eq(0).text());
            $.ajax({
                url: "/paike/findXZBDetail.do",
                type: "post",
                dataType: "json",
                data: {
                    gradeId: $("#gradeId").val(),
                    courseId: courseId
                },
                success: function (data) {
                    $(".clash-tab").empty();
                    Common.render({tmpl: $('#detailTempJs'), data: {data: data}, context: '.clash-tab'});
                }
            })
        });
        //关闭学生详情页面
        $("body").on("click", ".detial-cl", function () {
            $(".clash-detial").hide();
            $(".bg").hide();
        });
        $(".canc-btn").click(
            function () {
                $(".edit-set-div").hide();
                $(".bg").hide();
            });
        $(".setwind-cl").click(
            function () {
                $(".edit-set-div").hide();
                $(".bg").hide();
            });
        $('.cofi-btn').click(function () {
            var courseType = $("#courseType").val();
            if (courseType == "xzb") {
                $.ajax({
                    url: "/bianban/updateClassCourseInfo.do",
                    type: "post",
                    dataType: "json",
                    data: {
                        courseClassId: $("#courseId").val(),
                        teacherId: $("#teacherlist").val(),
                        classRoomId: $("#classroomlist").val(),
                        teacherName: $("#teacherlist").find("option:selected").text(),
                        weekcnt: 1,//不用
                        type: 3,
                        classId: ""//不用

                    },
                    success: function (rep) {
                        if (rep.flg) {
                            $(".edit-set-div").hide();
                            $(".bg").hide();
                            gezhiArranging.getXZBcourse(1);
                        }
                    }
                });
            }
            /*else {
                $.ajax({
                    url: "/bianban/updateClassCourseInfo.do",
                    type: "post",
                    dataType: "json",
                    data: {
                        courseClassId: $("#courseId").val(),
                        teacherId: $("#teacherlist").val(),
                        classRoomId: "",//不用
                        teacherName: $("#teacherlist").find("option:selected").text(),
                        weekcnt: 1,//不用
                        type: 4,
                        classId: ""//不用

                    },
                    success: function (rep) {
                        if (rep.flg) {
                            $(".edit-set-div").hide();
                            $(".bg").hide();
                            gezhiArranging.getPhysicalcourse(1);
                        }
                    }
                });
            }*/
        });
        //自动编排小走班
        $("#autoXZB").click(function () {
            if (confirm("如果您已经排课成功，再次自动编排将会覆盖已排小走班课程")) {
                $.ajax({
                    url: "/paike/autoXZB.do",
                    type: "post",
                    dataType: "json",
                    data: {
                        year: $("#termVal").val(),
                        gradeId: $("#gradeId").val()
                    },
                    success: function (data) {
                        if (data.result == "unlock") {
                            alert("请先锁定走班课");
                        }
                        else if (data.result != "200") {
                            alert("小走班自动编班失败，请尝试修改走班组合");
                        }
                        else {
                            gezhiArranging.getXZBcourse(1);
                        }
                    }
                })
            }
        })
    });
    gezhiArranging.getTeacherList = function (subjectId, teacherId) {
        $.ajax({
            url: "/bianban/findTeacherBySubjectId.do",
            type: "post",
            dataType: "json",
            data: {
                subjectId: subjectId
            },
            success: function (data) {
                $("#teacherlist").empty();
                Common.render({tmpl: $('#teacherlistTempJs'), data: {data: data}, context: '#teacherlist'});
                if (teacherId != "null") {
                    $("#teacherlist").val(teacherId);
                }
            }
        });
    };
    gezhiArranging.getAllTeacherList = function (teacherId, x, y) {
        if (teacherId == "null")
            teacherId = "";
        $.ajax({
            url: "/bianban/findTeacherNoClassBySchool.do",
            type: "post",
            dataType: "json",
            data: {
                teacherId: teacherId,
                year: $("#termVal").val(),
                x: x,
                y: y
            },
            success: function (data) {
                $("#teacherlist").empty();
                Common.render({tmpl: $('#teacherlistTempJs'), data: {data: data}, context: '#teacherlist'});
                if (teacherId != "") {
                    $("#teacherlist").val(teacherId);
                }
            }
        });
    };
    gezhiArranging.findClassroomList = function (classroomId, x, y) {
        if (classroomId == "null")
            classroomId = "";
        $.ajax({
            url: "/classroom/findFreeClassroomList.do",
            type: "post",
            dataType: "json",
            data: {
                year: $("#termVal").val(),
                classroomId: classroomId,
                x: x,
                y: y
            },
            success: function (data) {
                $("#classroomlist").empty();
                Common.render({tmpl: $('#classroomlistTempJs'), data: {data: data}, context: '#classroomlist'});
                if (classroomId != "") {
                    $("#classroomlist").val(classroomId);
                }
            }
        });
    };
    //=======================================体育走班======================================
    //获取体育走班已排列表
    gezhiArranging.getPhysicalcourse = function (tag) {
        $.ajax({
            url: "/timetable/physicalCourseTable.do",
            type: "post",
            dataType: "json",
            data: {
                gradeId: $("#gradeId").val(),
                year: $("#termVal").val()
            },
            success: function (data) {
                var courseList = [];
                var allCourseList = [];
                for (var i = 0; i < data.conf.classDays.length; i++) {
                    var x = data.conf.classDays[i];
                    for (var y = 1; y <= data.conf.classCount; y++) {
                        var have = false;
                        for (var k = 0; k < data.course.length; k++) {
                            if (data.course[k].xIndex == x &&
                                data.course[k].yIndex == y) {
                                var ctr0 = data.course[k].courseIdList[0];
                                //var courseName = ctr0.courseName.substring(5, ctr0.courseName.length - 1);
                                var courseName = ctr0.courseName.substring(0,ctr0.courseName.lastIndexOf("-"));
                                courseList.push({
                                    xIndex: x,
                                    yIndex: y,
                                    courseIdList: data.course[k].courseIdList,
                                    name: courseName
                                });
                                for (var j = 0; j < data.course[k].courseIdList.length; j++) {
                                    var ctr = data.course[k].courseIdList[j];
                                    allCourseList.push({
                                        xIndex: x,
                                        yIndex: y,
                                        courseName: ctr.courseName.substring(0, 4),
                                        studentCount: ctr.conflictCount,
                                        teacherName: ctr.teacherName,
                                        classroomName: ctr.classRoom,
                                        teacherId: ctr.teacherId,
                                        classroomId: ctr.classRoomId,
                                        subjectId: ctr.subjectId,
                                        courseId: ctr.courseId,
                                        className: ctr.courseName.substring(5, ctr.courseName.length - 1)
                                    });
                                }
                                have = true;
                                break;
                            }
                        }
                        if (!have) {
                            courseList.push({xIndex: x, yIndex: y, courseIdList: [], name: ""});
                        }
                    }
                }
                Common.render({
                    tmpl: '#tableTempPhyJs',
                    data: {data: data, course: courseList},
                    context: '#physicalCourseShow',
                    overwrite:1
                });
                $(".Nclass-ZB-SJ").css("left", $(".ZB_table_base").width());
                $(".Nclass-ZB-SJJ").css("left", $(".ZB_table_base").width());
                $(".Nclass-ZB-TC").css("left", $(".ZB_table_base").width() + 20);
                /*$("#phyCourseListShow").empty();
                Common.render({
                    tmpl: $('#phyCourseListTempJs'),
                    data: {data: allCourseList},
                    context: '#phyCourseListShow'
                });
                $("#xzbSet").hide();
                if (tag == 1) {
                    flag = 0;
                    $("#tab-Pclass").hide();
                    $("#physicalSet").show();
                }*/
            }
        });
    };
    var flag = 0;
    $(document).ready(function () {
        $("#removeFeizouban").click(function () {
            if (confirm("确定删除本班非走班课程?")) {
                $.ajax({
                    url: "/paike/removeFZBCourse.do",
                    type: "post",
                    dataType: "json",
                    data: {
                        year: $("#termVal").val(),
                        classId: $("#classShow4").val(),
                        gradeId:$("#gradeId").val()
                    },
                    success: function (data) {
                        if (!data.result == "200") {
                            alert("删除失败");
                        }
                        else {
                            gezhiArranging.showDetailTable4(true,4);
                        }
                    }
                })
            }
        });
        $("#clearPhy").click(function () {
            if (confirm("确定删除已经排好的体育走班课程?")) {
                $.ajax({
                    url: "/paike/removeCourse.do",
                    type: "post",
                    dataType: "json",
                    data: {
                        year: $("#termVal").val(),
                        gradeId: $("#gradeId").val(),
                        type: 4
                    },
                    success: function (data) {
                        if (!data.result == "200") {
                            alert("删除失败");
                        }
                        else {
                            gezhiArranging.getPhysicalcourse(0);
                        }
                    }
                })
            }
        });
        $("#clearXZB").click(function () {
            if (confirm("确定删除已经排好的小走班课程?")) {
                $.ajax({
                    url: "/paike/removeCourse.do",
                    type: "post",
                    dataType: "json",
                    data: {
                        year: $("#termVal").val(),
                        gradeId: $("#gradeId").val(),
                        type: 3
                    },
                    success: function (data) {
                        if (!data.result == "200") {
                            alert("删除失败");
                        }
                        else {
                            gezhiArranging.getXZBcourse(0);
                        }
                    }
                })
            }
        });
        $("body").on("click", "#autoSortPhy", function () {
            if (confirm("如果您已经排课成功，再次自动编排将会删除已排的非走班课程以及体育走班课程")) {
                $.ajax({
                    url: "/paike/autoPhysical.do",
                    type: "post",
                    dataType: "json",
                    data: {
                        year: $("#termVal").val(),
                        gradeId: $("#gradeId").val()
                    },
                    success: function (data) {
                        if (data.result == "unlock") {
                            alert("请先锁定走班课");
                        }
                        else if (data.result != "200") {
                            alert("体育走班自动编班失败");
                            gezhiArranging.getPhysicalcourse(0);
                        }
                        else {
                            gezhiArranging.getPhysicalcourse(1);
                        }
                    }
                })
            }
            /*$(".bg").show();
            $(".phy-conf").show();*/
        });
        $(".py-btn").click(function () {
            $(".phy-conf").hide();
            $(".bg").hide();
        });
        $("body").on("click", ".py-ok", function () {
            var classCount = $("#classCount").val();
            if (isNaN(classCount)) {
                alert("课时数请输入数字");
                return;
            }
            if (classCount <= 0 || classCount > 5) {
                alert("课时数控制在1~5之间");
                return;
            }
            var repeat = 0;
            if ($("#repeat").attr("checked")) {
                repeat = 1;
            }
            if (confirm("如果您已经排课成功，再次自动编排将会删除已排的非走班课程以及体育走班课程")) {
                $.ajax({
                    url: "/paike/autoPhysical.do",
                    type: "post",
                    dataType: "json",
                    data: {
                        year: $("#termVal").val(),
                        gradeId: $("#gradeId").val(),
                        classCount: classCount,
                        repeat: repeat
                    },
                    success: function (data) {
                        if (data.result == "unlock") {
                            alert("请先锁定走班课");
                        }
                        else if (data.result != "200") {
                            alert("体育走班自动编班失败");
                            gezhiArranging.getPhysicalcourse(0);
                        }
                        else {
                            gezhiArranging.getPhysicalcourse(1);
                        }
                    }
                })
            }
            $(".phy-conf").hide();
            $(".bg").hide();
        });
        $("body").on("click", "#setTeaPhy", function () {
            flag = 0;
            $("#tab-Pclass").hide();
            $("#physicalSet").show();
        });
        $("body").on("click", "#finsihBtnPhy", function () {
            $("#tab-Pclass").show();
            $("#physicalSet").hide();
        });
        $("body").on("click", ".settingPhy", function () {
            $(".xzbSetEl").hide();
            var subjectId = $(this).attr("sid");
            var teacherId = $(this).attr("tid");
            var courseId = $(this).attr("coid");
            var gradeId = $("#gradeId").val();

            gezhiArranging.getTeacherList(subjectId, teacherId);

            $("#coursenm").text($(this).attr("nm") + $(this).attr("classnm"));
            $("#courseId").val(courseId);
            $("#courseType").val("physical");
            $(".bg").show();
            $(".edit-set-div").show();
        });
        $(".canc-btn").click(
            function () {
                $(".edit-set-div").hide();
                $(".bg").hide();
            });
        $(".setwind-cl").click(
            function () {
                $(".edit-set-div").hide();
                $(".bg").hide();
            });
        //一键设置体育走班学生
        $("#autoSetPhy").click(function(){
            gezhiArranging.autoSetPhyTea();
        });


        //体育调课
        var $from = null;
        var $to = null;
        var availablePoints = [];//课调课时间列表
        $('body').on('click','.Xclass-main-right table td',function(){
            if(flag == 0){
                $from = $(this);
                if($from.text().trim() == ''){//空格子点击无效
                    return;
                }else{
                    var classCount = $("#classCount").val();
                    //获取课调课时间点
                    var url = '/paike/getPEAvailablePoint.do';
                    var param = {};
                    param.term = $("#termVal").val();
                    param.gradeId = $("#gradeId").val();
                    Common.getData(url,param,function(data){
                        if(data){
                            if(data.code == "200"){
                                if(data.pointList.length > 0){
                                    availablePoints = data.pointList;
                                }
                            }else{
                                alert(data.message);
                            }
                        }
                    });

                    $(this).siblings('td').each(function(){
                        for(var i = 0;i < availablePoints.length; i++){
                            if($(this).attr('x') == availablePoints[i].x && $(this).attr('y') == availablePoints[i].y){
                                $(this).addClass('Xclass-Green');
                                break;
                            }
                        }
                    });
                    $(this).parent().siblings().find('td').each(function(){
                        for(var i = 0;i < availablePoints.length; i++){
                            if($(this).attr('x') == availablePoints[i].x && $(this).attr('y') == availablePoints[i].y){
                                $(this).addClass('Xclass-Green');
                                break;
                            }
                        }
                    });
                    flag = 1;
                }
            }else{
                $to = $(this);
                if(!$(this).hasClass('Xclass-Green')){//不可排课时间点
                    if($to.attr('x') == $from.attr('x') && $to.attr('y') == $from.attr('y')){
                        $to.siblings('td').each(function(){
                            if($(this).hasClass('Xclass-Green')){
                                $(this).removeClass('Xclass-Green');
                            }
                        });
                        $to.parent().siblings().find('td').each(function(){
                            if($(this).hasClass('Xclass-Green')){
                                $(this).removeClass('Xclass-Green');
                            }
                        });
                        flag = 0;
                    }
                    return;
                }else{
                    var url = '/paike/adjustPhysical.do';
                    var param = {};
                    param.term = $("#termVal").val();
                    param.gradeId = $("#gradeId").val();
                    param.xFrom = $from.attr('x');
                    param.yFrom = $from.attr('y');
                    param.xTo = $to.attr('x');
                    param.yTo = $to.attr('y');
                    Common.getPostData(url,param,function(data){
                        if(data.code == "200"){
                            $to.parent().siblings().find('td').each(function(){
                                if($(this).hasClass('Xclass-Green')){
                                    $(this).removeClass('Xclass-Green');
                                }
                            });
                            gezhiArranging.getPhysicalcourse(0);
                            flag = 0;
                        }else{
                            alert(data.message);
                        }
                    });
                }
            }
        });

    });
    //一键设置体育走班老师
    gezhiArranging.autoSetPhyTea=function()
    {
        $.ajax({
            url:"/paike/autoSetTeacher.do",
            type:"post",
            dataType:"json",
            data:{
                year:$("#termVal").val(),
                gradeId:$("#gradeId").val(),
                type:4
            },
            success:function(data)
            {
                gezhiArranging.getPhysicalcourse(1);
            }
        });
    };
    //其他学科一键编排
    gezhiArranging.autoSortOther=function()
    {
        if($("#classShow5").val()==null||$("#classShow5").val()=="")
        {
            return;
        }
        $.ajax({
            url:"/paike/autoSortOther.do",
            type:"post",
            dataType:"json",
            data:{
                year:$("#termVal").val(),
                gradeId:$("#gradeId").val(),
                classId:$("#classShow5").val()
            },
            success:function(data)
            {
                if(data.result=="排课成功")
                {
                    gezhiArranging.showDetailTable4(true,5);
                }
                else{
                    alert(data.result);
                }
            }
        })
    };
    //其他学科一键清空
    gezhiArranging.autoClearOther=function()
    {
        if($("#classShow5").val()==null||$("#classShow5").val()=="")
        {
            return;
        }
        $.ajax({
            url:"/paike/autoClearOther.do",
            type:"post",
            dataType:"json",
            data:{
                year:$("#termVal").val(),
                gradeId:$("#gradeId").val()
            },
            success:function(data)
            {
                if(data.result=="200")
                {
                    gezhiArranging.showDetailTable4(true,5);
                }
                else{
                    alert("清空失败");
                }
            }
        });
    };
    //=============================兴趣拓展课===================================================
    $(document).ready(function()
    {
        $("#arrangeInterest").on("click",function()
        {
            $.ajax({
                url:"/paike/addInterestClassToTimetable.do",
                type:"post",
                data:{
                    year:$("#termVal").val(),
                    gradeId:$("#gradeId").val()
                },
                success:function(data)
                {
                    if(data.code=="200")
                    {
                        alert("导入成功");
                    }
                    else
                    {
                        alert("导入失败");
                    }
                }
            })
        });
    });
});