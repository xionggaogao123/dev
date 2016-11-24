/**
 * Created by wangkaidong on 2016/5/9.
 */

define(function(require, exports, module){
    require("jquery");
    require("doT");
    var Common = require('common');
    var attendance = {};

    attendance.init = function(){

        getStudentList();



        $('body').on('click','input[type="radio"]',function(){
            var url = '/attendance/attend.do';
            var param = {};
            param.attendanceId = $('body').attr('id');
            param.studentId = $(this).parents('tr').attr('id');
            param.attendance = $(this).val();
            Common.getPostData(url,param,function(resp){
                if(resp.code != "200"){
                    alert('考勤失败');
                }
            });
        });

        $('.stars-container i.fa').on({
            mouseenter:function() {
                starHover(this);
            },
            mouseleave:function() {
                starMouseOut(this);
            },
            click:function() {
                starClicked(this);
            }
        });

        $('.stars-container').each(function(index, element) {
            var $stars = $(element).children('i.fa');
            var score = $(this).attr('score');
            formatStars($stars, score);
        });

        $('.stars-container i.fa').each(function(){
            var num = $(this).parents('tr').attr('score');
            var desc;
            if(num == 5){
                desc = "优秀";
            } else if(num == 4){
                desc = "良好";
            } else if(num == 3){
                desc = "合格";
            } else {
                desc = "需努力";
            }
            $(this).siblings('span').text(desc);
        })
    }


    function getStudentList(){
        var url = '/attendance/getAttendance.do';
        var param = {
            attendanceId:$('body').attr('id')
        };
        Common.getData(url,param,function(data){
            if(data && data.attendance.studentList.length > 0){
                Common.render({
                    tmpl:'#studentListTmpl',
                    data:data.attendance.studentList,
                    context:'#studentListCtx'
                });
            }
        });
    }

    function formatStars($stars, score) {
        var i = 0;
        while(i < score) {
            $($stars[i]).removeClass('fa-star-o');
            $($stars[i]).addClass('fa-star');
            i++;
        }
        while(i < $stars.length) {
            $($stars[i]).removeClass('fa-star');
            $($stars[i]).addClass('fa-star-o');
            i++;
        }
    }

    function starHover(star) {
        var $star = $(star);
        $star.removeClass('fa-star-o');
        $star.addClass('fa-star');
        $star.nextAll('i.fa').removeClass('fa-star');
        $star.nextAll('i.fa').addClass('fa-star-o');
        $star.prevAll().removeClass('fa-star-o');
        $star.prevAll().addClass('fa-star');
    }

    function starMouseOut(star) {
        var $stars = $(star).parent().children('i.fa');
        var score = $(star).parent().attr('score');
        formatStars($stars, score);
    }

    function starClicked(star) {
        starHover(star);
        var $star = $(star);
        var num = $star.prevAll().length+1;
        var desc;
        if(num == 5){
            desc = "优秀";
        } else if(num == 4){
            desc = "良好";
        } else if(num == 3){
            desc = "合格";
        } else {
            desc = "需努力";
        }
        $star.siblings('span').text(desc);
        $star.parent().attr('score',num);

        var url = '/attendance/score.do';
        var param = {};
        param.attendanceId = $('body').attr('id');
        param.studentId = $star.parents('tr').attr('id');
        param.scoreItem = $star.parent().attr('scoreItem');
        param.score = $star.parent().attr('score');
        Common.getPostData(url,param,function(resp){
            if(resp.code != "200"){
                alert('打分失败');
            }
        });
    }



    module.exports = attendance;
});