
function getviewCoursesUserInfo() {

    if(courseid) {
        $.ajax({
            url: "/lesson/video/stu/list.do",
            type: "post",
            dataType: "json",
            data: {
                lessonId: courseid,
                skip: 0,
                limit: 20
            },
            success: function (data) {
                showViewCoursesUserInfo(data);
            },
            complete: function () {
            }
        });
    }
}

function showViewCoursesUserInfo(data) {
    var html = '';
    if (data) {
        for (var i = 0; i < data.length; i++) {
            var vcui = data[i];
            html += '<li><img src="' + vcui.avt + '"/><span>' + vcui.name + '</span></li>';
        }
    }
    var tdiv = $('#viewed_video_students ul');
    tdiv.html(html);
}

function getRelatedCourses() {
    //$.ajax({
    //    url: "/reverse/selRelatedCourses.action",
    //    type: "get",
    //    dataType: "json",
    //    data: {
    //        'courseId': courseid
    //    },
    //    success: function (data) {
    //        showRelatedCourses(data);
    //    },
    //    complete: function () {
    //    }
    //});
}

function showRelatedCourses(data) {
    var html = '';
    if (data.rows) {
        for (var i = 0; i < data.rows.length; i++) {
            var rc = data.rows[i];
            var cimg = !rc.imageUrl || rc.instype == 2 ? '/img/cloud_course.png' : rc.imageUrl;
            var url = '';
            if ($('#className').val().length != 0) {
                url += $('#classId').val() + '/';
            }

            if ($('#teacherName').val().length != 0) {
                url += $('#teacherId').val() + '/';
            }
            html += '<li><a href="/courseview/' + url + rc.id + '"><img src="' + cimg + '"/><span class="ellipsis">' + rc.courseName + '</span></a></li>';
        }
    }
    var tdiv = $('#relatived_course_list ul');
    tdiv.html(html);
}

function getAnswers() {
    //$.ajax({
    //    url: "/reverse/getFztest.action",
    //    type: "post",
    //    dataType: "json",
    //    data: {
    //        courseID: courseid
    //    },
    //    success: function (data) {
    //        if (data.list == null || data.list.length == 0) {
    //            $("#answer_list_container").hide();
    //        }
    //        else {
    //            $("#answer_list_container").show();
    //            showAnswers(data);
    //        }
    //    }
    //});
}

function showAnswers(data) {
    var html = '';
    if (data.list) {
        var k = 1;
        for (var i = 0; i < data.list.length; i++) {
            var ans = data.list[i];
            var date = new Date(ans.createTime.replace(/-/g, '/').replace('T', ' '));
            var month = date.getMonth() + 1;
            month = month > 9 ? month : '0' + month;
            var day = date.getDate();
            day = day > 9 ? day : '0' + day;
            if (ans.doFlag) {
                //<a href="/reverse/courseexam/'+ ans.id +'/'+ courseid + '/1" target="_blank"> </a>
                html += '<li style="cursor:not-allowed;"><p><span>' + ans.name + '</span><span><input type="checkbox" checked disabled/></span></p></li>';
                sequence++;
            }
            else {//未批改的题目
                if (k == 1 && role == 0) {
                    html += '<li><p><i class="fa fa-play fa-2 file-op" style="margin-left: -18px;margin-right: 8px;color:#666;"></i><a href="/reverse/courseexamnext/' + sequence + '/' + courseid + '/1"><span>' + (ans.name ? ans.name : ('题目' + (i + 1))) + '</span><span><input type="checkbox" disabled/></span></a></p></li>';
                    k++;
                } else {
                    html += '<li><p><span>' + ans.name + '</span><span><input type="checkbox" disabled/></span></p></li>';
                }
            }
        }
    }

    var tdiv = $('#answer_list ul');
    tdiv.html(html);
    $("#answer_list >h4 a").attr("href", '/reverse/courseexamnext/' + sequence + '/' + courseid + '/1');
}

function getComments(page, pageSize, bRefresh, basync) {

    var skip = (page - 1) * pageSize;
    $.ajax({
        url: "/lesson/comment/list.do",
        type: "post",
        dataType: "json",
        data: {
            'lessonId': courseid,
            'skip': skip,
            'limit': '5'
        },
        success: function (data) {
            currentCommPage = page + 1;
            showComments(data, page, bRefresh);
        }
    });
}

function showComments(data, page, bRefresh) {
    var html = '';

    if (data.rows) {
        for (var i = 0; i < data.rows.length; i++) {
            var comm = data.rows[i];
            var lasttime = (new Date()).getTime() - comm.time;
            lasttime /= 1000;
            var timeStr = "";
            if (lasttime / (3600 * 24) > 1) {
                timeStr += Math.floor(lasttime / (3600 * 24)) + "天前";
            }
            else if (lasttime / 3600 > 1) {
                timeStr += Math.floor(lasttime / 3600) + "小时前";
            }
            else if (lasttime / 60 > 1) {
                timeStr += Math.floor(lasttime / 60) + "分钟前";
            }
            else {
                if (timeStr > 0) {
                    timeStr += Math.floor(lasttime / 60) + "秒前";
                }
                else {
                    timeStr += "刚刚";
                }
            }
            /* <span class="reply_time">'+ timeStr +'</span> */
            html += '<div style="margin-left:40px;"><div class="discussion_pic"><img style="width:38px;height:38px;" src="' + comm.avatar + '"></div>';
            //if (comm.commenttype == 0) {
                html += '<div class="discussion_detail"><p>' + comm.name + '&nbsp;&nbsp;' + timeStr + '</p><p>' + comm.comment + '</p></div></div>';
            //}
            //else {
            //    var prefix = '/upload/voice/';
            //    html += '<div class="discussion_detail"><p>' + comm.name + '&nbsp;&nbsp;' + timeStr + '</p><p><a onclick="showPlayed(this);" url="' + prefix + comm.comment + '"><img src="/img/yuyin.png"></a></p></div></div>';
            //}
        }
    }
    $('#comm_footer').remove();
    var remain = data.total - page * 5;
    var remainlink = remain > 0 ? "javascript:getComments(currentCommPage,5)" : "javascript:";
    html += '<div id="comm_footer"><a class="orange" href="' + remainlink + '">后面还有' + (remain > 0 ? remain : 0) + '条评论，点击查看>></a></div>';
    var tdiv = $('#discussion_list');
    if (typeof bRefresh == 'undefined') {
        tdiv.append(html);
    }
    else {
        tdiv.html(html);
    }

}

function submitComment() {

    var comment_content = $.trim($('#commentText').val());
    if (comment_content == '') {
        alert("请输入评论内容。");
        return;
    }

    $('#commentBtn').attr('disabled', 'disabled');
    $('#commentBtn').attr('value', '提交中...');
    $('#commentBtn').css('cursor', 'wait');

    var userComment = {
        'lessonId': courseid,
        'comment': comment_content
    };

    $.ajax({
        url: "/lesson/comment.do",
        type: "post",
        data: userComment,
        complete: function (data) {
            $('#commentBtn').removeAttr('disabled');
            $('#commentBtn').attr('value', '提交评论');
            $('#commentBtn').css('cursor', 'pointer');
        },
        success: function () {
            $('#commentText').val("");
            $('#commentBtn').attr('value', '提交评论');
            $('#commentBtn').css('cursor', 'pointer');
            alert("评论成功发表！");
            //window.location.reload();
            getComments(1, 5, true);
        },
        error: function (e) {
            alert("评论提交失败。");
        }
    });
}

function showPlayed(ob) {
    var player = '<embed src="' + $(ob).attr('url') + '" belong="' + CurrentUId + '" width="0" height="0" autostart="true" />';
    $('embed[belong=' + CurrentUId + ']').remove();
    $('body').append(player);
}

function loadRecorder(ob) {
    $('.recorder').attr('id', '');
    $(ob).next().attr('id', 'recorder').show();
    $('#recorder record_button').click();
}


$(function () {
    //$('.loading').show();
	
	try
	{
      getComments(currentCommPage, 5);
	}catch(x)
	{}
	
    getAnswers();
    //getCoursewareInfo();
    getviewCoursesUserInfo();
    getRelatedCourses();
    //$('.loading').fadeOut(1000);
    $('.video-course-name').text($('#courseName').val());
});

var foo = true;
function showflash(container) {
    var mc = new FlashObject("/business/Main.swf", "recorderApp", "350px", "23px", "8");
    mc.setAttribute("id", "recorderApp");
    mc.setAttribute("name", "recorderApp");

    mc.addVariable("uploadAction", "audiosave.jsp?userId=" + CurrentUId + "%26outId=" + courseid + "%26type=0%26commenttype=1%26voicetype=0");
    mc.addVariable("fileName", "aaa");
    mc.write("myContent");
    if (foo) {
        $(container).append($('#recorder'));
        $('#recorder .sanjiao').show();
        $("#myContent").show();
        foo = false;
    } else {
        $("#myContent").hide();
        $('#recorder .sanjiao').hide();
        foo = true;
    }
}