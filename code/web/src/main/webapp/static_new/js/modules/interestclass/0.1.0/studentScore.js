/* 
* @Author: Tony
* @Date:   2015-08-05 10:34:59
* @Last Modified by:   Tony
* @Last Modified time: 2015-08-05 15:43:41
*/

define(function(require,exports,module){
    /**
     *初始化参数
     */
    require("jquery");
    require("doT");

    var studentScore = {},
        Common = require('common');
    //var content_zoom=require("content_zoom");
    var classId = $.trim($("body").attr("classid"));
    var stuId = $.trim($("body").attr("stuId"));
    var termType = $.trim($("body").attr("termType"));

    /**
     * 获取课程总评
     */
    studentScore.getClassTranscript=function()
    {
        $.ajax({
            url:"/myclass/getClassTranscript.do",
            type:"get",
            datatype:"json",
            data:{
                classId:classId,
                studentId:stuId,
                termType:termType
            },
            success:function(data)
            {
                if(data!=null&&data.code!="500") {
                    Common.render({tmpl: $('#lessonScript'), data: data, context: '#lessonScriptShow'});
                    if(data.resultspicsrc){
                        $('#imgOne').html('<img src="'+data.resultspicsrc+'"/>').attr('href',data.resultspicsrc);
                    }
                    $('#tc').text(data.teachercomments);
                    $('#at').text(data.attendance);
                }

            }
        })
    };
    /**
     * 获取课堂表现
     */
    studentScore.getStudentScore=function()
    {
        $.ajax({
           url:"/myclass/getLessonScore.do",
            type:"get",
            datatype:"json",
            data:{
                classId:classId,
                studentId:stuId
            },
            success:function(data)
            {
                if(data!=null)
                {
                    Common.render({tmpl: $('#lessonScore'), data: data, context: '#lessonScoreShow'});
                }
            }
        });
    };
    //require.async("content_zoom",function(content_zoom){
    //    jQuery('#imgOne').fancyZoom({scaleImg: true, closeOnClick: true});
    //    jQuery('#zoom_word_1').fancyZoom({width:400, height:200});
    //});


    studentScore.getClassTranscript();
    studentScore.getStudentScore();
});