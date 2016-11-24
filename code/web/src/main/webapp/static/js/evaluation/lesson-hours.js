/**
 * Created by Niu Xin on 14-10-19.
 */

function deleteLessonHour(hour_index) {
    ConfirmDialog('确定删除该课时？', 'doDeleteLessonHour(' + hour_index + ')');
}

function doDeleteLessonHour(hour_index) {
    MessageBox('删除中...', 0);
    $.ajax({
        url:"/myclass/removeLesson.do",
        data: {'classId': classId, 'lessonIndex': hour_index, termType:termType},
        type: "post",
        success: function(response) {
            location.reload();
        },
        error: function(e) {
            MessageBox('服务器响应错误。', -1);
        }
    });
}


$(function(){


    var index;
    $('.update-btn').on('click', function(){
        var lesson = $(this).prev().children('div');
        index = lesson.attr('index');
        $('#weeks').val(lesson.attr('wi'));
        $('#title').val(lesson.text());
        $(".homework-bg").show();
        $(".homework-popup").show();
    })

    if(sessionStorage.getItem("addLesson") == 1){
        $('.update-btn').last().click();
        sessionStorage.setItem("addLesson", 0);
    }

    $('.popup-to-ri').click(function(){
        $(".homework-bg").hide();
        $(".homework-popup").hide();
    })

    $('#fb').click(function(){
        //alert(classId + "===" + index);
        var lessonName = $('#title').val();
        var weekIndex = $('#weeks').val();
        $.ajax({
            url:"/myclass/editLessonName.do",
            data: {'classId': classId, 'index': index, 'lessonName':lessonName, 'weekIndex':weekIndex},
            type: "post",
            success: function(response) {
                location.reload();
            },
            error: function(e) {
                MessageBox('服务器响应错误。', -1);
            }
        });
    })

    $('#addLesson').click(function(){
        var classId = $(this).attr("classId");
        var termType = $(this).attr("termType");
        $.ajax({
            url:"/myclass/appAddLesson.do",
            data: {'classId': classId, 'termType': termType},
            type: "post",
            success: function(resp) {
                if(resp.code == '200'){
                    sessionStorage.setItem("addLesson", 1);
                    location.reload();
                } else {
                    alert(resp.message);
                }

            },
            error: function(e) {
                MessageBox('服务器响应错误。', -1);
            }
        });
    })

    //
})

