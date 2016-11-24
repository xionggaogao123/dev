/**
 * Created by wangkaidong on 2016/5/9.
 */
define(function(require,exports,module){
    require("jquery");
    require("doT");
    var Common = require('common');
    var xunshiScore = {};

    xunshiScore.init = function(){

        getLessonList();





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
            var num = $(this).parent().attr('score');
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



    function getLessonList() {
        var url = '/attendance/lessonList.do';
        var param = {
            term:$('#lessonList tr td:first').text(),
            courseId:$('body').attr("id")
        };

        Common.getData(url, param, function (data) {
            Common.render({
                tmpl: '#xunshiScoreTmpl',
                data: data.lessonList,
                context: '#xunshiScoreCtx'
            });
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

        var url = '';

        if($star.parent().attr('type') == 1){
            url = '/attendance/teacherScore.do';
        }else{
            url = '/attendance/courseScore.do';
        }

        var param = {};
        param.attendanceId = $star.parents('tr').attr('id');
        param.score = $star.parent().attr('score');
        Common.getPostData(url,param,function(resp){
            if(resp.code != "200"){
                alert('打分失败');
            }
        });
    }



    module.exports = xunshiScore;
});