/**
 * Created by wangkaidong on 2016/5/5.
 */

define(function (require, exports, module) {
    require("jquery");
    require("doT");
    var Common = require('common');
    var lesson = {};

    lesson.init = function () {



        getWeek();
        getLessonList();



        $('body').on('click','#add',function(){
            $('.bg').show();
            $('.attendance').show();
        });

        $('body').on('click','.close',function(){
            attendanceInit();
        });

        $('body').on('click','.cancel',function(){
            attendanceInit();
        });

        $('body').on('click','.submit',function(){
            if($('#lessonName').val() == ''){
                alert('课时名称不能为空');
                return;
            }
            if($('#date').val() == ''){
                alert('上课时间不能为空');
                return;
            }
            if($('#week').val() == ''){
                alert('周不能为空');
                return;
            }
            if($('#day').val() == ''){
                alert('星期不能为空');
                return;
            }
            if($('#section').val() == ''){
                alert('节不能为空');
                return;
            }

            var url = '/attendance/addOrUpdateLesson.do';
            var param = {};
            param.courseId = $('body').attr('id');
            param.term = $('#term').text();
            param.lessonId = $('.attendance').attr('id');
            param.lessonName = $('#lessonName').val();
            param.date = $('#date').val();
            param.week = $('#week').val();
            param.day = $('#day').val();
            param.section = $('#section').val();

            Common.getPostData(url,param,function(resp){
                if(resp && resp.code == "200"){
                    attendanceInit();
                    getLessonList();
                }else{
                    if(param.lessonId == ''){
                        alert('新增失败');
                    }else{
                        alert('更新失败');
                    }
                }
            });

        });


        $('body').on('click','.lessonDetail',function() {
            var lessonId = $(this).attr('id');
            $('.attendance').attr('id',lessonId).show();
            $('.bg').show();
            var url = '/attendance/getAttendance.do';
            var param = {
                attendanceId:lessonId
            }
            Common.getData(url,param,function(data){
               if(data){
                   $('#lessonName').val(data.attendance.lessonName);
                   $('#date').val(data.attendance.date);
                   $('#week').val(strToInt(data.attendance.week));
                   $('#day').val(strToInt(data.attendance.day));
                   $('#section').val(strToInt(data.attendance.section));
               }
            });
        });
    }

    function attendanceInit(){
        $('.bg').hide();
        $('.attendance').hide();
        $('#lessonName').val('');
        $('#date').val('');
        $('#week').val(1);
        $('#day').val(1);
        $('#section').val(1);
    }


    function getWeek(){
        var url = '/attendance/week.do';
        var param = {
            term:$('#lessonList tr td:first').text()
        };

        Common.getData(url,param,function(data){
            if(data){
                var week = [];
                for(var i = 1;i <= data.week;i++){
                    var option = {};
                    option.value = i;
                    option.text = '第'+i+'周';
                    week.push(option);
                }

                Common.render({
                    tmpl:'#weekOptionsTmpl',
                    data:week,
                    context:'#week'
                });
            }
        });
    }

    function getLessonList() {
        var url = '/attendance/lessonList.do';
        var param = {
            term:$('#lessonList tr td:first').text(),
            courseId:$('body').attr("id")
        };

        Common.getData(url, param, function (data) {
            Common.render({
                tmpl: '#lessonTmpl',
                data: data.lessonList,
                context: '#lessonCtx',
                overwrite:1
            });
        });
    }

    toAttendance = function (id,term,grade,lessonId,className,teacherName,studentsCount){//考勤
        var url = '/attendance/attendAndScore.do';
        var param = {};
        param.id = id;
        param.term = term;
        param.grade = grade;
        param.lessonId = lessonId;
        param.className = className;
        param.teacherName = teacherName;
        param.studentsCount = studentsCount;

        window.location.href = url+'?index=7&version=5&id='+param.id+'&term='+param.term+'&grade='+param.grade+'&lessonId='+param.lessonId
            +'&className='+param.className+'&teacherName='+teacherName+'&studentsCount='+param.studentsCount;
    }

    function strToInt(str){
        var num = 0;
        switch (str){
            case "一": num = 1;break;
            case "二": num = 2;break;
            case "三": num = 3;break;
            case "四": num = 4;break;
            case "五": num = 5;break;
            case "六": num = 6;break;
            case "七": num = 7;break;
            case "八": num = 8;break;
            case "九": num = 9;break;
            case "十": num = 10;break;
            case "十一": num = 11;break;
            case "十二": num = 12;break;
            case "十三": num = 13;break;
            case "十四": num = 14;break;
            case "十五": num = 15;break;
            case "十六": num = 16;break;
            case "十七": num = 17;break;
            case "十八": num = 18;break;
            case "十九": num = 19;break;
            case "二十": num = 20;break;
            case "二十一": num = 21;break;
            case "二十二": num = 22;break;
            case "二十三": num = 23;break;

            default:break;
        }
        return num;
    }



    module.exports = lesson;
});