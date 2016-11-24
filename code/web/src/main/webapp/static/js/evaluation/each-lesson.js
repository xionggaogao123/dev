/**
 * Created by Niu Xin on 14-10-19.
 */
function fileTypeCheck(e, data) {
    var goUpload = true;
    var uploadFile = data.files[0];
    if (!(/\.(gif|jpg|jpeg|tiff|png)$/i).test(uploadFile.name)) {
        MessageBox('请选择图片文件。', -1);
        goUpload = false;
    }
    if (uploadFile.size > 5000000) { // 2mb
        MessageBox('文件不能超过5MB。', -1);
        goUpload = false;
    }
    if (goUpload == true) {
        data.submit();
    }
}
$(function() {
    $('.upload-statement').each(function(index, element) {
        var $input_file = $(element);
        var stu_id = $input_file.closest('tr').attr('stu-id');
        var lessonindex = $input_file.closest('tr').attr('index');
        $input_file.fileupload({
            url: '/myclass/upload/lessonResultPicture.do',
            formData: {'classId': classId, 'userId': stu_id, 'index':lessonindex, termType:termType},
            add: fileTypeCheck,
            start: function(e) {
                var $loading = $('#upload-statement-' + index).parent().prev();
                $loading.show();
            },
            done: function(e, data) {
                if (data.dataType == 'iframe ') {
                    var response = $( 'pre', data.result ).text();
                } else {
                    var response = data.result;
                }
                var td = $('#upload-statement-' + index).closest('td').prev();
                var img = td.find('img');
                if (img.length > 0) {
                    var $a = td.find('a');
                    $a.attr('href', response);
                    img.attr('src', response);
                } else {
                    var stu_name = td.prev().text();
                    var html = '<a class="fancybox" href="' + response + '" data-fancybox-group="gallery" title="' + stu_name + '">' +
                        '<img src="' + response + '" class="statement-image"/></a>';
                    td.html(html);
                }
            },
            fail: function (e, data) {

            },
            always: function (e, data) {
                var $loading = $('#upload-statement-' + index).parent().prev();
                $loading.hide();
            }
        });
    });

    $('input[name="stuscore"]').each(function(){
        //alert($(this).val());
        var num = $(this).val();
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



    $('.lesson-score-list .score-line i.fa').on({
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
        var score = $(element).children('input[name="stuscore"]').val();
        formatStars($stars, score);
    });

    $('body').on('click', 'input:radio', function(){
        var value = $(this).val();
        if(value == 0){
            var starscontainer = $(this).closest('td').next();
            starscontainer.find('i').removeClass('fa-star').addClass('fa-star-o');
            starscontainer.find('span').text("需努力");
            starscontainer.find('.score').val(0);
        }
    })


    $('.fancybox').fancybox();
});

function statementButtonAction(button) {
    if (button.getAttribute('state') == 'save') {
        //saveStatement(button);
        $(button).prop('disabled', false);
        $(button).html('修改');
        $(button).attr('state', 'change');
        var textarea = $(button).closest('td').find('textarea');
        textarea.addClass('readonly');
    } else {
        $(button).attr('state', 'save');
        $(button).html('保存');
        var textarea = $(button).closest('td').find('textarea');
        textarea.removeClass('readonly');
        textarea.prop('readonly', false);
    }
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
    var score = $(star).siblings('input[name="stuscore"]').val();
    formatStars($stars, score);
}

function starClicked(star) {
    starHover(star);
    var $star = $(star);
    var num = $star.prevAll().length+1;
    $star.siblings('input[name="stuscore"]').val(num);
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
}

function saveLessonScore(){
    //获取每一行信息 抽取成json对象  返回给服务器  同时页面跳转
    var lessonScores = [];
    $('.lesson-score-list .score-line').each(function (i, tr) {
        var statement = $.trim($(tr).find('textarea').val());
        var at = $(tr).find('input:radio:checked').val();
        var userid = $(tr).attr('stu-id');
        var lessonindex = $(tr).attr('index');
        var stuscore=$(tr).find('input[name="stuscore"]').val();
        var pictureUrl = $(tr).find('.fancybox').attr('href');
        var lessonScore = {
            'classid' : classId,
            'coursetype' : type,
            'teacherComment': statement,
            'attendance':at,
            'userid':userid,
            'lessonindex':lessonindex,
            'stuscore':stuscore,
            'pictureUrl':pictureUrl,
            'lessonName':name
        };
        //$(tr).find('input').each(function (j, input) {
        //    lessonScore[input.name] = input.value;
        //});
        lessonScores.push(lessonScore);
    });
    MessageBox('保存中...', 0);
    $.ajax({
        url: '/myclass/savels/'+ termType,
        type: 'post',
        data: JSON.stringify(lessonScores),
        contentType: 'application/json',
        success: function (result) {
            if (result == 'success') {
                MessageBox('保存成功！', 1);
                setTimeout(function() { location.href = '/myclass/lead2ls/1/' + classId + '/' + termType; }, 1000);
            } else {
                MessageBox('保存失败。', -1);
            }
        },
        error: function(e) {
            MessageBox('保存失败。', -1);
        }
    });
}