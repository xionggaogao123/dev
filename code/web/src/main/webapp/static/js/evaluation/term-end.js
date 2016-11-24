/**
 * Created by Niu Xin on 14-10-16.
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

$(function () {
    $('.upload-statement').each(function(index, element) {
        var $input_file = $(element);
        var stu_id = $input_file.closest('tr').attr('stu-id');
        $input_file.fileupload({
            url: '/myclass/upload/lessonResultPic.do',
            formData: {'classId': classId, 'userId': stu_id, termType: termType},
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

    $('.stars-container').each(function(){
        var num = $(this).attr('stars');
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
        $(this).find('span').text(desc);
    })

    $('table.term-end-table .stars-container i.fa').on({
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

    $('table.term-end-table td.stars-container').each(function(index, element) {
        formatStars($(element));
    });

    $('.fancybox').fancybox();
});

function statementButtonAction(button) {
    if (button.getAttribute('state') == 'save') {
        saveStatement(button);
    } else {
        $(button).attr('state', 'save');
        $(button).html('保存');
        var textarea = $(button).closest('td').find('textarea');
        textarea.removeClass('readonly');
        textarea.prop('readonly', false);
    }
}

function saveStatement(save_button) {
    var textarea = $(save_button).closest('td').find('textarea');
    var statement = $.trim(textarea.val());
        $(save_button).html('<i class="fa fa-spinner fa-spin"></i>');
        $(save_button).prop('disabled', true);
        textarea.prop('readonly', true);
        var stu_id = $(save_button).closest('tr').attr('stu-id');
        $.ajax({
            url: '/myclass/saveTranscript.do',
            data: {
                'classid': classId,
                'userid': stu_id,
                'teachercomments': statement,
                termType:termType
            },
            type: 'post',
            success: function(response) {
                $(save_button).prop('disabled', false);
                $(save_button).html('修改');
                $(save_button).attr('state', 'change');
                textarea.addClass('readonly');
            },
            error: function(response) {
                MessageBox('修改失败。', -1);
                $(save_button).prop('disabled', false);
                textarea.prop('readonly', false);
            }
        });
}

/**
 * 批量保存老师的评语
 * add by peng
 */
function saveMultiStatements(){

    var $saveButtons = $("button[state='save']");
    var trascripts = [];
    if($saveButtons.length > 0){
        $saveButtons.each(function(){
            var textarea = $(this).closest('td').find('textarea');
            var statement = $.trim(textarea.val());

                var stu_id = $(this).closest('tr').attr('stu-id');
                var trascript = {
                    'classid':classId,
                    'coursetype':type,
                    'userid' : stu_id,
                    'teachercomments': statement
                };
                trascripts.push(trascript);
        });
        if(trascripts.length > 0){
            MessageBox('保存中...', 0);
            $.ajax({
                url: '/myclass/saveMultiTranscripts/' + termType + '.do?',
                type: 'post',
                data: JSON.stringify(trascripts),
                contentType: 'application/json',
                success: function (result) {
                    if (result == 'success') {
                        MessageBox('保存成功！', 1);
                        setTimeout(function() { location.href = '/myclass/tointerestclass?version=16'; }, 1000)
                    } else {
                        MessageBox('保存失败。', -1);
                    }
                },
                error: function(response) {
                    MessageBox('修改失败。', -1);
                }
            });
        }else{
            MessageBox('保存成功！', 1);
            setTimeout(function() { location.href = '/myclass/tointerestclass?version=16'; }, 1500)
        }
    }else{
        MessageBox('保存成功！', 1);
        setTimeout(function() { location.href =  '/myclass/tointerestclass?version=16'; }, 1500)
    }
}

function formatStars(container) {
    var starNum = container.attr('stars');
    starNum = parseInt(starNum);
    var starsList = $(container).children('i').toArray();
    var i = 0;
    while(i < starNum) {
        $(starsList[i]).removeClass('fa-star-o');
        $(starsList[i]).addClass('fa-star');
        i++;
    }
    while(i < starsList.length) {
        $(starsList[i]).removeClass('fa-star');
        $(starsList[i]).addClass('fa-star-o');
        i++;
    }
}

function starHover(star) {
    var td = $(star).parent();
    var starsList = td.children('i').toArray();
    var i = 0;
    for(i in starsList) {
        $(starsList[i]).removeClass('fa-star-o');
        $(starsList[i]).addClass('fa-star');
        if (starsList[i] === star) {
            break;
        }
    }
    while(++i < starsList.length) {
        $(starsList[i]).removeClass('fa-star');
        $(starsList[i]).addClass('fa-star-o');
    }
}

function starMouseOut(star) {
    var td = $(star).parent();
    formatStars(td);
}

function starClicked(star) {
    var td = $(star).parent();
    var starsList = td.children().toArray();
    for(var i in starsList) {
        if (starsList[i] === star) {
            var score = parseInt(i) + 1;
            var stu_id = $(star).closest('tr').attr('stu-id');
            td.attr('stars', score);
            var finalscore;
            var usualscore;
            if(td.attr("id")=='usualscore'){
                usualscore = score;
                finalscore = td.next().attr('stars');
            } else {
                finalscore = score;
                usualscore = td.prev().attr('stars');
            }
            showDesc(score,td);
            $.ajax({
                url: '/myclass/saveTranscript.do',
                data: {
                    'classid': classId,
                    'userid': stu_id,
                    'finalresult': finalscore,
                    'semesterscore':usualscore,
                    termType:termType
                },
                type: 'post',
                success: function(response) {
                }
            });
            return;
        }
    }
}

function showDesc(num, ele){
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
    ele.find('span').text(desc);
}