//解决console 未定义bug  提高兼容性
if (!window.console || !console.firebug)

{
    var names = ["log", "debug", "info", "warn", "error", "assert", "dir", "dirxml",
        "group", "groupEnd", "time", "timeEnd", "count", "trace", "profile", "profileEnd"];

    window.console = {};
    for (var i = 0; i < names.length; ++i)
        window.console[names[i]] = function() {}
}


var nb_role = '${session.currentUser.role}';
var audioUploadAction = 'audiosave.jsp?userId=' + '${currentUser.id}' + '&voicetype=0';

$(function() {
    var controlBarList = $('.control-bar > *');
    var selectClassLi = $('.headmaster-select-class>li');
    var selectBarLi = $('.info-select-bar>li');
    
    window.onresize = function() {
        var target = $('.control-bar>div.active');
//        alert(target.offset().left.val());
//        if(target.offset()==undefined||target.offset()==null){
//            target.offset().left==0;
//        }
//        var ileft = target.offset().left + (target.width() + 40) / 2 - 12.5;
        var ileft = (target.width() + 40) / 2 - 12.5;
        $('.index-bottom-img').css({
            left: ileft
        });
    }
    
    
    // 上方导航条轮换
    controlBarList.on('click', function() {
        $(this).parent().find('.active').removeClass('active');
        $(this).addClass('active');
        $('#example').hide();
        switchBar($(this).text());
        var ileft = $(this).offset().left + ($(this).width() + 40) / 2 - 12.5;
        $('.index-bottom-img').animate({
            left: ileft
        }, 500);
    });

    selectClassLi.on('click', function() {
        $(this).parent().find('.active').removeClass('active');
        $(this).addClass('active');
        getBrowserList(0, $(this).attr('gid'), 0);
    });
    switchBar('微校园');

    // 班级页面各功能点击
    selectBarLi.on('click', function() {
        var classId = $('.second-tag').attr('cid');
        $(this).parent().find('.active').removeClass('active');
        $(this).addClass('active');
        $('.everything-container>div').hide();
        $('#example').hide();
        var target = $(this).find('.select-detail').attr('data-target');
        classDifferentFunc(target, classId);
        $('.' + target).show();
    });

    // 点击班级
    $('body').on('click', '.class-container>a', function() {
        var content = $(this).find('.class-headmaster-name').text();
        var classId = $(this).attr('cid');
        $('.first-tag').on('click', function() {
            $('.everything-container>div,.class-titlebar').hide();
            $('.class-list').show();
            $('#example').hide();
        });
        $('.second-tag').text(content);
        $('.second-tag').attr('cid', classId);
        $('#example').hide();
        $('.class-titlebar').show();
        $('.class-list').hide();
        $('.class-teacher-container').show();
        $('.info-select-bar').find('.active').removeClass('active');
        $('.info-select-bar>li').eq(0).addClass('active');
        var type=$(this).attr("type");
        if(type=="commonClass") {
            getClassStatistics(classId);
            getTeacherList(classId);
        }
    });

    // 老师页面
    $('.headmaster-teacher-subject>li,.headmaster-teacher-grade>li').on('click', function() {
        $(this).parent().find('.active').removeClass('active');
        $(this).addClass('active');
        var subjectId = $('.headmaster-teacher-subject').find('.active').attr('sid');
        var gradeId = $('.headmaster-teacher-grade').find('.active').attr('cid');
        getBrowserList(subjectId, gradeId, 1);
    });

    // 初始化页脚
    $('#example').bootstrapPaginator({
        currentPage: 1,
        totalPages: 1,
        itemTexts: function(type, page, current) {
            switch (type) {
                case "first":
                    return "首页";
                case "prev":
                    return "<";
                case "next":
                    return ">";
                case "last":
                    return "末页" + page;
                case "page":
                    return page;
            }
        },
        onPageClicked: function(e, originalEvent, type, page) {}
    });
    getFriendBlogInfo(1, 1, 12);


});


function switchBar(str) {
	var nb_username = $('#username').val();
    switch (str) {
        case '微校园':
            {
                $('.mycomment-container,.one_blog_container').hide();
                $('.headmaster-class-container').hide();
                $('.headmaster-note-container').hide();
                $('.friend-container').show();
                $('.headmaster-teacher-container').hide();
                blogType = 1;
                getFriendBlogInfo(1, 1, 12);
                $('#myinfo').removeClass("myblog");
            	$('#mybloginfo').removeClass("myblog");
                $('#form-container').show();
                break;
            }
        case '微家园':
	        {
	            $('.mycomment-container,.one_blog_container').hide();
	            $('.headmaster-class-container').hide();
	            $('.headmaster-note-container').hide();
	            $('.friend-container').show();
	            $('.headmaster-teacher-container').hide();
                blogType = 2;
	            getFriendBlogInfo(1, 1, 12);
	            if (nb_username.indexOf("K6KT小助手")!=-1) {
	            	$('#myinfo').removeClass("myblog");
                	$('#mybloginfo').removeClass("myblog");
	            	$('#form-container').show();	
	            } else {
	            	$('#myinfo').addClass("myblog");
                	$('#mybloginfo').addClass("myblog");
	            	$('#form-container').hide();
	            }
	            break;
	        }
        case "班级":
            {
                getBrowserList(0, 0, 0);
                $('.headmaster-select-class').find('li').removeClass('active');
                $('.headmaster-select-class').find('li').eq(0).addClass('active');
                $('.friend-container').hide();
                $('.mycomment-container,.one_blog_container').hide();
                $('.headmaster-teacher-container').hide();
                $('.headmaster-class-container').show();
                $('.everything-container>div,.class-titlebar').hide();
                $('.headmaster-note-container').hide();
                $('.class-list').show();
                $('#example').hide();
                break;
            }
        case "老师":
            {
                $('.headmaster-teacher-container').show();
                $('.mycomment-container,.one_blog_container').hide();
                $('.friend-container').hide();
                $('.headmaster-class-container').hide();
                $('.headmaster-note-container').hide();
                $('.headmaster-teacher-grade,.headmaster-teacher-subject').find('.active').removeClass('active');
                $('.headmaster-teacher-grade>li').eq(0).addClass('active');
                $('.headmaster-teacher-subject>li').eq(0).addClass('active');
                getBrowserList(0, 0, 1);
                break;
            }
        case "通知":
            {
                $('.mycomment-container,.one_blog_container').hide();
                $('.headmaster-class-container').hide();
                $('.friend-container').hide();
                $('.headmaster-teacher-container').hide();
                $('.headmaster-note-container').show();
                getClassList();
                getHeadNote(1,8);
                break;
            }
    }
}


function classDifferentFunc(str, cid) {
    switch (str) {
        case "class-teacher-container":
            {
                getTeacherList(cid);
                break;
            }
        case "class-course-container":
            {
                getCourses(cid, 1, 12);
                break;
            }
        case "class-work-container":
            {
                getDiscusses(cid, 1, 8, 2);
                break;
            }
        case "class-note-container":
            {
                getDiscusses(cid, 1, 8, 1);
                break;
            }
        case "class-exam-container":
            {
                getStudentExamPaper(cid, 1, 8);
                break;
            }
    }
}

function getBrowserList(sid, gid, bType) {
    var postData = {};

    if (typeof bType == 'undefined') {
        postData.browserType = 0;
    } else if (bType == 1) {
        postData.browserType = bType;
    } else {
        postData.browserType = bType;
    }
    if (typeof gid != 'undefined') {
        postData.gradeId = gid;
    } else {
        postData.gradeId = 0;
    }
    if (typeof sid != 'undefined') {
        postData.subjectId = sid;
    } else {
        postData.subjectId = 0;
    }

    $.ajax({
        url: "/myschool/selteacher.do",
        type: "get",
        dataType: "json",
        async: true,
        data: postData,
        success: function(data) {
            ret = data;
            if (bType == 0) {
                showClassList(data);
            } else {
                showTeacherList(data);
            }
        },
        error: function() {
            console.log('selSubjectTeacher error');
        }
    });
}

function showClassList(data) {
    $(".class-container").html('');
    var target = $(".class-container");
    var index=0;
    var html = '';
    if (data.commonClass) {
        var classArray=(data.commonClass);
        //classArray=classArray.concat(data.commonClass,data.interestClass);
        for (var i=0;i<classArray.length;i++) {
            var cla = classArray[i];
            var imgsrc = '/img/class_odd.png';
            index=1;
            if (i % 2 == 0) {
                index=0;
                imgsrc = '/img/class_even.png';
            }
            html += '<a class="col-xs-3 class-content" style="width:200px;" href="javascript:;" type="commonClass" cid="' + cla.id + '"><img class="class-img"style="width:130px" src="' + imgsrc + '"><div class="class-headmaster-name" title="'+ cla.className +'">' + cla.className + '</div></a>';
        }
    }
    if (data.interestClass) {
        var classArray=(data.interestClass);
        //classArray=classArray.concat(data.commonClass,data.interestClass);
        for (var i=0;i<classArray.length;i++) {
            var cla = classArray[i];
            var imgsrc = '/img/class_odd.png';
            if ((i+index) % 2 == 1) {
                imgsrc = '/img/class_even.png';
            }
            html += '<a class="col-xs-3 class-content" style="width:200px;" href="javascript:;" type="interestClass" cid="' + cla.id + '"><img class="class-img"style="width:130px" src="' + imgsrc + '"><div class="class-headmaster-name" title="'+ cla.className +'">' + cla.className + '</div></a>';
        }
    }
    target.html(html);
}
var headerclassid;
// 获取班级学生信息
function getClassStatistics(classid) {
	headerclassid = classid;
    $.ajax({
        url: "/myclass/statstus.do",
        type: "get",
        dataType: "json",
        async: false,
        data: {
            classId: classid
        },
        success: function(data) {
            if (data.totalList == null || data.totalList.length == 0) {

            } else {
                showStuList(data);
            }
        }
    });
}

function showStuList(data) {
    var html = '';
    if (data.totalList) {
        for (var i = 0; i < data.totalList.length; i++) {
            var stu = data.totalList[i];
            var attach = '';
            if (nb_role == 2 || nb_role == 9 || nb_role == 3) {
                attach = headerclassid + '/';
            }
            //html += '<li class="content"><a target="_blank" href="/myclass/lead2stustat/' + attach + stu.studentId + '"><span class="I">';
            html += '<li class="content"><a target="_blank"><span class="I">';
            html += '<img class="stu-list-img" src="' + stu.imageURL + '" role="' + stu.role + '" target-id="' + stu.studentId + '"/>';
            html += '<span>' + stu.userName + '</span></span><span class="II">' + stu.endViewNum + '</span>';
            html += '<span class="III">' + stu.endQuestionNum + '</span></a></li>';
        }
    }

    var studentList = $('#student-list');
    studentList.html(html);
}

function getTeacherList(cid) {
    $.ajax({
        url: "/myclass/teacherinofs.do",
        type: "get",
        dataType: "json",
        async: false,
        data: {
            classId: cid
        },
        success: function(data) {
            if (isTeacher(data.role)) {
                if (data.teacherList == null || data.teacherList.length == 0) {

                } else {
                    showTeacherList(data);
                }
            } else {
                $('#teacher-list').text('该班级没有老师');
            }
        }
    });
}

function showTeacherList(data) {
    var html = '';
    if (data.teacherList) {
        for (var i = 0; i < data.teacherList.length; i++) {
            var te = data.teacherList[i];
            var attach = '';
            if ( isHeadMaster(nb_role) || isK6ktHelper(nb_role) || isEducation(nb_role)) {
                attach = headerclassid + '/';
            }
            html += '<a target="_blank"  class="teacher-info" href="/teacher/course/' + attach + te.teacherId + '">';
            html += '<img class="teacher-img"  src="' + te.imageUrl + '" role="' + te.role + '" target-id="' + te.id + '">';
            html += '<div class="teacher-name" style="height: 18px;">' + te.userName + ' ' + te.subjectName + '</div></a>';
        }
        var teacherList = $('#teacher-list');
        teacherList.html(html);
    } else {
        for (var i = 0; i < data.rows.length; i++) {
            var te = data.rows[i];
            var attach = '';
           // html += '<a target="_blank"   class="teacher-info" href="/teacher/course/' + te.teacherId + '">';
            html += '<a target="_blank"   class="teacher-info" href="/teacher/course/' + te.id + '">';
            html += '<img class="teacher-img"  src="' + te.imgUrl + '" role="' + te.role + '" target-id="' + te.id + '">';
            html += '<div class="teacher-name">' + te.userName + '</div>';
            html += '<div class="teacher-experience">经验值：<span class="experience-span">' + te.experienceValue + '</span></div></a>';
        }
        $(".headmaster-teacher-info").html(html);
    }
}

// 本班课程
function getCourses(cid, page, size, async) {
    var ret = [];
    async = typeof async == 'undefined' ? false : true;
    if (async) {
        $('.loading').show();
    }
    $('.class-course-container').load('/myschool/classcourse.do', {
        classid: cid
    });
    return ret;
}

function showCourses(data) {
    var html = '';
    if (data.rows) {
        for (var i = 0; i < data.rows.length; i++) {
            var co = data.rows[i];
            co.courseContent = co.courseContent == null ? '' : co.courseContent;
            var cimg = co.instype == 2 ? '/img/cloud_course.png' : co.imageUrl;
            var dot = co.courseContent.length > 63 ? '...' : '';
            if (i % 2 == 0) {
                html += '<div class="fc_course_detail" style="margin-bottom:20px;">';
            } else {
                html += '<div class="fc_course_detail" style="margin-left:24px;margin-bottom:20px;">';
            }
            html += '<div>';
            html += '<a target="_blank" onclick="javascript:checkCourseTimeSpan(this);" url="/courseview/' + co.id + '"><img style="width: 140px; height: 153px;" src="' + cimg + '"/></a>';
            html += '<div>';
            html += '<p>' + co.courseType + '</p>';
            html += '<p class="orange bigsize" style="overflow:hidden;margin-top:20px;height:58px;line-height:20px;">' + co.courseName + '</p>';
            /* '<span class="cross_right">'+ co.viewNumber +'人阅读</span></p>'; */
            /* html += '<p style="padding-bottom: 10px;height:49px;">课件简介：' + co.courseContent.substring(0,63) + dot + '</p>'; */
            html += '<hr style="background: #D0D0D0;">';
            html += '<p>总课件为一个课时：包含<span class="orange" style="margin-right: 3px;">' + co.videoNum + '个视频;</span><span class="orange" style="margin-right: 3px;">' + co.docNum + '个文档;</span>';
            html += '<span class="orange" style="margin-right: 3px;">' + co.questionNum + '道练习题;</span></p>';
            html += '</div>';
            html += '<p>';
            html += '<span class="fc_setting">课程播放时间<input type="text"class="ui_timepicker blue" readonly value="' + co.startwatchtime + '">';
            html += '<input type="text" class="ui_timepicker blue" readonly value="' + co.endwatchtime + '"></span>';
            html += '<a target="_blank" href="/coursestat/' + co.id + '" class="backblue" style="position:absolute;right:10px;padding: 0 5px;">课程统计</a></p></div></div>';
        }
    }
    var tdiv = $('.class-course-container');
    tdiv.html(html);
}


// 本班作业及通知
function getDiscusses(cid, page, size, index) {
    $.ajax({
        url: "/homework/teacher/list.do",
        type: "get",
        dataType: "json",
        async: true,
        data: {
            'classId': cid,
            'subjectId':'000000000000000000000000',
            'page': page,
            'pageSize': size,
            'type':0
        },
        success: function(data) {
            ret = data;
            showDiscusses(data, page, index);
            var total = data.rows.length > 0 ? data.total : 0;
            var to = Math.ceil(total / size);
            totalPages = to == 0 ? 1 : to;
            if (page == 1) {
                resetPaginator(totalPages, index);
            } else {
                resetPaginatorCurrentPage(page, totalPages, index);
            }
        },
        complete: function() {
            if (!ret.rows || ret.rows.length == 0) {
                $('#example').hide();
            } else {
                $('#example').show();
            }
        }
    });
}

function showDiscusses(data, cpage, index) {
    $("#reply_list").hide();
    var target;
    if (index == 1) {
        target = $('.class-note-container');
    } else {
        target = $('.class-work-container');
    }
    var html = '';
    if (data.rows) {
        var txt = index == 1 ? '通知' : '作业';
        for (var i in data.rows) {
            var ds = data.rows[i];
            var lastreply = "";
            if (ds.time) {
                var lastreplyTime = getAdaptTimeFromNow(ds.time);
                lastreply = '<span style="margin-left:15px;">最后回复' + lastreplyTime + '</span>';
            }
            var isbold = 'normal';
            if (!ds.isread) {
                isbold = 'bold';
            }
            var viewtxt = ds.viewnum == 0 ? '' : '已回复' + ds.viewnum + '条';
            var attach = '';
            if (ds.isvoice) {
                attach += '<img src="/img/mic_work.png" style="margin-left:10px;"/>';
            }
            if (ds.isfile) {
                attach += '<img src="/img/fileattach.png" style="margin-left:10px;"/>';
            }
            html += '<div style="position:relative;cursor:pointer;" msgid="' + ds.id +
            '" onclick="getDiscussDetail(' + ds.id + ',' + cpage + ',' + index + ');"><img class="stu-list-img" src="' +
            ds.userAvatar + '" role="'+ ds.role +'" target-id="'+ ds.userid +'"/><p style="margin-bottom:5px;font-weight:' +
            isbold + ';"><a style="margin-right:10px;color:#FF9D58;">[' + txt + ']</a>';
            html += ds.mesgname + attach + '</p><p style="margin-top:5px"><span style="margin-right:10px;">' + ds.nickName + '</span>';
            html += '<span>于' + ds.createtime + '发表</span>' + lastreply + '<span style="margin-left:10px;">' + viewtxt + '</span></p>';
            html += '</div>';
        }
    }
    target.html(html);
}

// 作业通知详情
function getDiscussDetail(mid, cpage, index) {
    $.ajax({
        url: "/reverse/selMessageDetailInfo.action",
        type: "get",
        dataType: "json",
        async: true,
        data: {
            'messageId': mid
        },
        success: function(data) {
            ret = data;
            showDiscussDetail(data, mid, cpage, index);
        },
        complete: function() {
            $("#uploadForm").attr('action', audioUploadAction + '&mesgid=' + mid);
            if (index == 2) {
                getReplyList(mid, 1, 8, index);
                $('#example').show();
            } else {
                $('#example').hide();
            }
            $("#reply_list").html('').show();
        }
    });
}

function showDiscussDetail(data, mid, cpage, index) {
    var target;
    if (index == 1) {
        target = $('.class-note-container');
    } else {
        target = $('.class-work-container');
    }
    var html = '';
    var ds = '';
    var cid = $('.second-tag').attr('cid');
    html += '<div style="cursor:pointer;" onclick="getDiscusses(' + cid + ',' + cpage + ',8,' + index + ');"><a>返回></a></div>';
    if (data.fzMesgDetailInfo) {
        var txt = index == 1 ? '通知' : '作业';
        ds = data.fzMesgDetailInfo;
        html += '<div style="position:relative;" msgid="' + mid + '"><img src="' + ds.minImageURL + '" /><p style="margin-bottom:5px"><a style="margin-right:10px;">[' + txt + ']</a>';
        html += ds.mesgname + '</p><p style="margin-top:5px"><span style="margin-right:10px;">' + ds.nickName + '</span>';
        html += '<span>于' + ds.createtime + '发表</span></p>';
        html += '</div>';
    }

    if (data.attachmentInfoList) {
        html += '<div style="border-bottom:none;word-break: break-all;word-wrap: break-word;"><p style="margin-bottom:20px;text-indent:2em;width: 850px;margin-left: 12px;">' + ds.mesgcontent + '</p>';
        if (ds.voicename) {
            html += '<p style="margin-left:30px;margin-bottom:10px;"><a onclick="PlayedMusic($(this),' + ds.id + ');" url="' + ds.voicename + '"><img src="/img/yuyin.png">播放</a></p>';
        }
        for (var i in data.attachmentInfoList) {
            var at = data.attachmentInfoList[i];
            html += '<p style="margin-left:30px;margin-bottom:10px;"><a href="/reverse/FileDownload?fileName=' + encodeURI(encodeURI(at.pathname)) + '"><img src="/img/fileattach.png"/>附件' + (parseFloat(i) + 1) + ':' + at.name + '</a></p>';
        }
        html += '</div>';
    }
    target.html(html);
}

function resetPaginator(totalPages, index) {
    var classId = $('.second-tag').attr('cid');
    $('#example').bootstrapPaginator("setOptions", {
        currentPage: 1,
        totalPages: totalPages,
        itemTexts: function(type, page, current) {
            switch (type) {
                case "first":
                    return "首页";
                case "prev":
                    return "<";
                case "next":
                    return ">";
                case "last":
                    return "末页" + page;
                case "page":
                    return page;
            }
        },
        onPageClicked: function(e, originalEvent, type, page) {
            currentPage = page;
            if (index) {
                getDiscusses(classId, page, 8, index);
            } else {
                getStudentExamPaper(classId, page, 8);
            }

        }
    });
}

function resetPaginatorRe(totalPages, index) {
    var content;
    if (index == 1) {
        content = 'class-note-container';
    } else {
        content = 'class-work-container';
    }
    $('#example').bootstrapPaginator("setOptions", {
        currentPage: 1,
        totalPages: totalPages,
        itemTexts: function(type, page, current) {
            switch (type) {
                case "first":
                    return "首页";
                case "prev":
                    return "<";
                case "next":
                    return ">";
                case "last":
                    return "末页" + page;
                case "page":
                    return page;
            }
        },
        onPageClicked: function(e, originalEvent, type, page) {
            currentPage = page;
            getReplyList($("." + content + " >div:nth(1)").attr('msgid'), page, 8, index);
        }
    });
}

function resetPaginatorCourse(totalPages, ctype, cid) {
    var options = {
        currentPage: 1,
        totalPages: totalPages,
        itemTexts: function(type, page, current) {
            switch (type) {
                case "first":
                    return "首页";
                case "prev":
                    return "<";
                case "next":
                    return ">";
                case "last":
                    return "末页" + page;
                case "page":
                    return page;
            }
        },
        onPageClicked: function(e, originalEvent, type, page) {
            currentPage = page;
            getCourses(cid, page, 12);
        }
    };
    $('#example').bootstrapPaginator(options);
}

function resetPaginatorCurrentPage(page, totalPages, index) {
    var classId = $('.second-tag').attr('cid');
    $('#example').bootstrapPaginator("setOptions", {
        currentPage: page,
        totalPages: totalPages,
        itemTexts: function(type, page, current) {
            switch (type) {
                case "first":
                    return "首页";
                case "prev":
                    return "<";
                case "next":
                    return ">";
                case "last":
                    return "末页" + page;
                case "page":
                    return page;
            }
        },
        onPageClicked: function(e, originalEvent, type, page) {
            currentPage = page;
            if (index) {
                getDiscusses(classId, page, 8, index);
            } else {
                getStudentExamPaper(classId, page, 8);
            }

        }
    });
}

function  PlayedMusic(ob, rid) {
    var player = '<embed src="/upload/voice/' + $(ob).attr('url') + '" belong="' + rid + '" width="0" height="0" autostart="true" />';
    $('embed[belong=' + rid + ']').remove();
    $('body').append(player);
}

// 回复
function getReplyList(mid, page, size, index) {
    $("#reply_list").addClass('hardloadingb');
    $("#reply_list").show();
    $.ajax({
        url: "/reverse/selStuOrTeacherTaskInfo.action",
        type: "get",
        dataType: "json",
        async: true,
        data: {
            'mesgid': mid,
            'page': page,
            'pageSize': size
        },
        success: function(data) {
            ret = data;
            showReplyList(data);
            var total = data.rows.length > 0 ? data.total : 0;
            var to = Math.ceil(total / size);
            totalPages = to == 0 ? 1 : to;
            if (page == 1) {
                resetPaginatorRe(totalPages, index);
            }
            if (total > 0) {
                $('#example').show();
            } else {
                $('#example').hide();
                $("#reply_list").hide();
            }
        },
        complete: function() {
            $("#reply_list").removeClass('hardloadingb');
        }
    });
}

function showReplyList(data) {
    var target = $('#reply_list');
    var html = "";
    if (data.rows) {
        for (var i in data.rows) {
            var hf = data.rows[i];
            var timeStr = getAdaptTimeFromNow(hf.createtime);

            html += '<div style="position:relative;"><div class="reply_detail"><div style="margin-right:15px;"><img src="' + hf.minImageURL + '" style="width:45px;height:45px;"/></div>';
            html += '<div><p><span style="font-size: 16px;color:#696969;margin-right:20px;">' + hf.nickName + '</span><span style="color:#9a9a9a;">发表于' + timeStr + '</span></p>';
            if (hf.isview == 1) {
                html += '<p style="word-break:break-all;">' + hf.homeworkcontent + '</p>';
                if (hf.voicename) {
                    html += '<p><a onclick="PlayedMusic($(this),' + hf.id + ');" url="' + hf.voicename + '"><img src="/img/yuyin.png">播放</a></p>';
                }
                for (var j in hf.fileList) {
                    var file = hf.fileList[j];
                    html += '<p><a href="/reverse/FileDownload?fileName=' + encodeURI(encodeURI(file.pathname)) + '"><img src="/img/fileattach.png" />附件' + (parseFloat(j) + 1) + '：' + file.name + '</a></p>';
                }
            } else {
                html += '<p>该作业已提交！</p>';
            }
            html += '</div><a class="delreply" onclick="delReply(' + hf.id + ');"><img candel="' + hf.ismytask + '" src="img/dustbin.png" title="删除" style="display:none;position:absolute;top:25px;right:25px;"/></a>';
            html += '</div><a style="padding:2px 5px;position:absolute;top:0px;right:25px;background:rgb(225,225,225);">' + hf.floor + '#</a></div>';
        }
    }
    target.html(html);

    $('#reply_list >div').hover(function() {
        var ob = $(this).find('.delreply img');
        if (ob.attr('candel') == 1 && nb_role != 4) {
            $(this).find('.delreply img').show();
        }

    }, function() {
        $(this).find('.delreply img').hide();
    });
}

// 本班试卷
function getStudentExamPaper(classId, page, size) {
    $.ajax({
        url: '/reverse/stuPaperList.action',
        type: 'post',
        dataType: 'json',
        data: {
            classid: classId,
            'page': page,
            'pageSize': size
        },
        success: function(data) {
            var rows = data.data;
            $('.class-exam-container').empty();
            for (var i = 0; i < rows.length; i++) {
            	var time = rows[i].createTime;
            	if (time!=null) {
            		time = time.replace('T',' ');
            	} else {
            		time = '';
            	}
                var content = '';
                content += '<div style="position:relative;"><img src="/img/class.png"><p style="margin-bottom:5px;">';
                content += '<a onclick="showExamDetail(' + rows[i].id + ', \'' + rows[i].name + '\')" class="exam_title">' + rows[i].name + '</a></p><p style="margin-top:5px">';
                content += '<span style="margin-right:10px;">' + rows[i].nickName + '</span><span>于 ' + time + ' 发表</span></p></div>';
                $('.class-exam-container').append(content);
            }

            var total = data.total > 0 ? data.total : 0;
            var to = Math.ceil(total / size);
            totalPages = to == 0 ? 1 : to;
            if (page == 1) {
                resetPaginator(totalPages);
            } else {
                resetPaginatorCurrentPage(page, totalPages);
            }
            $('#example').show();
        },
        error: function() {
            console.log('getStudentExamPaper error');
        }
    });
}
var eID = null;
var eName = null;
function showExamDetail(eid, paperName) {
	eID = eid;
	eName = paperName;
	sName ="'" + paperName + "'";
	$('.class-exam-container').hide();
    $('.exam_list').hide();
    $('.viewexam-container').addClass('hardloadingt');
    $('.viewexam-container').empty().show();
    $('.correctexam-container').hide();

    $('.myexam_bar > span:first-child').html('<a onclick="$(\'.tab_button\')[3].click()">上传考卷</a> > <a onclick="showExamDetail(' + eid + ', \'' + paperName + '\')">' + paperName + '</a>');
    $('#example').hide();
    $.getJSON('/reverse/teaPaperDetial.action', {
        'paperId': eid
    }, function (response) {
        currentPaperData = response.data;
        $('.viewexam-container').removeClass('hardloadingt');
        var html = template('viewexam', response);
        var temp = '<div class="myexam_bar" style="border:none;"><span><a onclick="$(\'.info-select-bar>li\')[4].click()">本班练习</a> > <a onclick="showExamDetail(' + eid + ', \'' + paperName + '\')">' + paperName + '</a></span></div>';
        $('.viewexam-container').html(temp + html);
    });
}

function showCorrecting(index) {
    $('.myexam_bar > span:first-child').contents().eq(3).remove();
    var path = ' > 题目' + (index + 1);
    $('.myexam_bar > span:first-child').append(path);

    $('.viewexam-container').hide();
    $('.correctexam-container').addClass('hardloadingt');
    $('.correctexam-container').empty().show();
    //$('.correctexam-container').html('<div class="hardloadingb" style="min-height: 50px"></div>').show();
    $.getJSON('/reverse/teaCorrectView.action', {
        'questionId': currentPaperData[index].id
    }, function (response) {
        $('.correctexam-container').removeClass('hardloadingt');
        response['index'] = index;
        if (index < currentPaperData.length - 1) {
            response['hasNext'] = true;
        }
        var html = template('correctexam', response);
        html = html.replace('{eid}',eID).replace('{\'paperName\'}',sName).replace('{paperName}',eName);
        $('.correctexam-container').html(html);
    });
}

// 校长发通知
function submitDiscuss() {
    var name = $(".label-note-title").val();
    var content = $(".label-textarea").val().replace(/\n/g, '<br>');

    var flist = $.map($('.uploadedFiles'), function(file){
        return {
            realName : $(file).attr('realname'),
            localName : $(file).text()
        };
    });;

    if ($.trim(name) == '') {
        alert('标题不可为空！');
        return false;
    } else {
        if (name.getLength() > 100) {
            alert("输入标题格式为不多于50个字！");
            return false;
        }
    }
    var arr = document.getElementById("isPushToCalendar");
    var pushToCalendar;
    if (arr!=null && arr.checked) {
    	pushToCalendar = 1;
    } else {
    	pushToCalendar = 0;
    }
    var bSClass = false;
    var cidlist = '';
    $(".note-select input").each(function() {
        if ($(this).prop('checked') && !$(this).hasClass('all-class')) {
            bSClass = true;
            cidlist += $(this).closest('label').attr('classtype') + ',' + $(this).closest('label').attr('cid') + ',' + $(this).closest('label').attr('ctype') + ';';
        }
    });
    if (!bSClass) {
        alert('请选择班级！');
        return false;
    }

    $('#submitloading').show();

    $.ajax({
        url: "/reverse/addMessageInfo.action",
        type: "post",
        dataType: "json",
        async: true,
        data: {
            'mesgname': name,
            'mesgcontent': content,
            'mesgtype': 1,
            'classIdList': cidlist,
            'filenamelist': JSON.stringify(flist),
            'isPushToCalendar': pushToCalendar,
            'voiceid': $('.voice').attr('value')
        },
        success: function(data) {
        	document.getElementById("isPushToCalendar").checked = false;
            ret = data;
            alert("发表成功！");
            getHeadNote(1,8);
        },
        error: function() {
            console.log('addMessageInfo error');
        },
        complete: function() {
            $('#submitloading').hide();
            resetSubmitForm();
        }
    });
}

var foo = true;
function showflash(container, userId)
{
    var mc = new FlashObject("/business/Main.swf", "recorderApp", "350px", "23px", "8");
    mc.setAttribute("id","recorderApp");
    mc.setAttribute("name","recorderApp");

    mc.addVariable("uploadAction","audiosave.jsp?userId="+ userId + "%26voicetype=0%26isinform=1");
    mc.addVariable("fileName","aaa");
    mc.addVariable("recordTime",10*60*1000);
    mc.addVariable("appName","recorderApp");
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

function loadRecorder(ob){
    $('.recorder').attr('id','');
    $(ob).next().attr('id','recorder').show();
    $('#recorder record_button').click();
}

// 获取校长班级列表
function getClassList() {
    $.ajax({
        url: 'reverse/schoolClassList.action',
        type: 'post',
        dataType: 'json',
        data: {},
        success: function(data) {
            var classList = data.classList;
            var html = '<div class="note-select-label"><label class="checkbox-inline"><input type="checkbox" class="all-class"> 全校</label></div>';
            for (var i in classList) {
                html += '<div class="note-select-label">';
                for (var j = 0; j < classList[i].length; j++) {
                	if (classList[i][j].classtype==1 || classList[i][j].coursetype==1) {
                		html += '<label class="checkbox-inline" cid="' + classList[i][j].id + '" classtype="' + classList[i][j].classtype + '" ctype="1" title="'+ classList[i][j].classname +'">';
                    	html += '<input type="checkbox"> ' + classList[i][j].classname + '</label>';
                	} else {
                	html += '<label class="checkbox-inline" cid="' + classList[i][j].id + '" classtype="' + classList[i][j].classtype + '" ctype="1" title="'+ classList[i][j].classname +'">';
                    html += '<input type="checkbox"> ' + classList[i][j].classname + '上' + '</label>';
                    html += '<label class="checkbox-inline" cid="' + classList[i][j].id + '" classtype="' + classList[i][j].classtype + '" ctype="2" title="'+ classList[i][j].classname +'">';
                    html += '<input type="checkbox"> ' + classList[i][j].classname + '下' + '</label>';
                	}
                    
                }
                html += '</div>';
            }
            $('.note-select').empty();
            $('.note-select').append(html);

            $('.all-class').change(function(event) {
                if ($(this).is(':checked')) {
                    $('.note-select').find('input').prop('checked', true);
                } else {
                    $('.note-select').find('input').prop('checked', false);
                }
            });

            $('.note-select').find('input').change(function(event) {
                if (!$(this).is(':checked')) {
                    $('.all-class').prop('checked', false);
                }
            });
        },
        error: function() {
            console.log('getClassList error');
        }
    });
}

// 获取校长通知
function getHeadNote(page,size) {
    $.ajax({
        url: '/reverse/selHeadmasterMessageInfo.action',
        type: 'post',
        dataType: 'json',
        data: {
        	page: page,
            pageSize: size
        },
        success: function(data) {
            console.log(data);
            showHeadNote(data, 1);
            
            var total = data.rows.length > 0 ? data.total : 0;
            var to = Math.ceil(total / size);
            totalPages = to == 0 ? 1 : to;
            if (page == 1) {
                resetPaginatorNote(totalPages);
            }

            if (data.rows.length > 0) {
                $('#example').show();
            } else {
                $('#example').hide();
            }
        },
        error: function() {
            console.log('getHeadNote error');
        }
    });
}

function resetPaginatorNote(totalPages) {
    $('#example').bootstrapPaginator("setOptions", {
        currentPage: 1,
        totalPages: totalPages,
        itemTexts: function (type, page, current) {  
            switch (type) {  
                case "first":  
                    return "首页";  
                case "prev":  
                    return "<";  
                case "next":  
                    return ">";  
                case "last":  
                    return "末页"+page;  
                case "page":  
                    return  page;  
            }                 
        },  
        onPageClicked: function(e, originalEvent, type, page) {
            currentPage = page;
            getHeadNote(page,8);
        }
    });
}

function resetSubmitForm() {
    $('.note-select input[type=checkbox]').prop('checked', false);
    $('.release-note-container input[type=text]').val('');
    $('.label-upload textarea').val('');
    $('.uploadedFiles').remove();
    $('p:has(.voice)').remove();
}

function showHeadNote(data, cpage) {
    var target = $('.release-headmaster-note');
    target.empty();
    target.append('<div class="discuss-info" style="cursor:pointer;"><img class="discuss-info-img" src="/img/logo_b.png"/><p style="margin-bottom:5px;word-break: break-all;" onclick="getSystemMsgDetail();""><a style="margin-right:10px;color:#FF9D58;">[通知]</a>亲，你的用户手册来啦！</p><p style="margin-top:5px;width:323px;"><span style="margin-right:10px;">复兰科技</span><span>于2014-8-28 09:00:00发表</span></p><a class="deldiscuss"><img src="img/dustbin.png" title="删除" style="display:none;"/></a><a class="topdiscuss"><img cantop="0" istop="1" src="img/set_top.png" title="置顶" style="display:block;"/></a></div>');
    var html = '';
    if (data.rows) {
        for (var i in data.rows) {
            var ds = data.rows[i];
            var lastreply = "";
            if (ds.time) {
                var lastreplyTime = getAdaptTimeFromNow(ds.time);
                lastreply = '<span style="margin-left:15px;">最后回复' + lastreplyTime + '</span>';
            }
            var isbold = 'normal';
            var viewtxt = ds.viewnum == 0 ? '' : '已回复' + ds.viewnum + '条';
            var attach = '';
            if (ds.isvoice) {
                attach += '<img src="/img/mic_work.png" style="margin-left:10px;"/>';
            }
            if (ds.isfile) {
                attach += '<img src="/img/fileattach.png" style="margin-left:10px;"/>';
            }
            html += '<div style="position:relative;"><img src="' + ds.minImageURL + '" />';
            html += '<p style="margin-bottom:5px;font-weight:' + isbold + ';cursor:pointer;" msgid="' + ds.id + '" onclick="getHeadNoteDetail(' + ds.id + ',' + cpage + ',1);">';
            html += '<a style="margin-right:10px;color:#FF9D58;">[通知]</a>';
            html += ds.mesgname + attach + '</p><p style="margin-top:5px;"><span style="margin-right:10px;">' + ds.nickName + '</span>';
            html += '<span>于' + ds.createtime + '发表</span>' + lastreply + '<span style="margin-left:10px;">' + viewtxt + '</span>';
            html += '<span style="cursor:pointer;" onclick="deleteNote(' + ds.id + ')"><img style="margin-top:-3px;" src="/img/dustbin.png" title="删除"></span>';
            html += '<span style="cursor:pointer;margin-left:10px;" onclick="topdiscuss(' + ds.id + ')">';
            if (ds.istop == 1) {
                html += '<img cantop="' + ds.ismytop + '" istop="' + ds.istop + '" style="margin-top:-3px;" src="/img/set_top.png" title="置顶">';
            } else {
                html += '<img cantop="' + ds.ismytop + '" istop="' + ds.istop + '" style="margin-top:-3px;" src="/img/set_top.jpg" title="置顶">';
            }
            html += '</span></p></div>';
        }
    }
    target.append(html);
}

function getSystemMsgDetail() {
    var target = $('.release-headmaster-note');
    target.empty();
    var attach = '';
    var html = '<div style="cursor:pointer;" onclick="getHeadNote();"><a>返回></a></div><div class="discuss-info"><img class="discuss-info-img" src="/img/logo_b.png"/><p style="margin-bottom:5px;word-break: break-all;"><a style="margin-right:10px;">[通知]</a>亲，你的用户手册来啦！</p><p style="margin-top:5px"><span style="margin-right:10px;">复兰科技</span><span>于2014-08-28 09:00:00发表</span></p></div><div style="border-bottom:none;"><p style="margin-bottom:20px;text-indent: 2em;word-break: break-all;width:100%;">校领导、老师、学生、家长均可点击附件下载对应版本的“K6KT-快乐课堂”用户手册，获得更多操作帮助！</p>';
    attach += '<p style="margin-left:30px;margin-bottom:10px;"><a href="/reverse/FileDownload?fileName=/upload/resources/用户手册-家长.pdf"><img src="/img/fileattach.png"/>附件1:用户手册-家长.pdf</a></p>';
    attach += '<p style="margin-left:30px;margin-bottom:10px;"><a href="/reverse/FileDownload?fileName=/upload/resources/用户手册-学生.pdf"><img src="/img/fileattach.png"/>附件2:用户手册-学生.pdf</a></p>';
    attach += '<p style="margin-left:30px;margin-bottom:10px;"><a href="/reverse/FileDownload?fileName=/upload/resources/用户手册-老师.pdf"><img src="/img/fileattach.png"/>附件3:用户手册-老师.pdf</a></p>';
    attach += '<p style="margin-left:30px;margin-bottom:10px;"><a href="/reverse/FileDownload?fileName=/upload/resources/用户手册-校领导.pdf"><img src="/img/fileattach.png"/>附件4:用户手册-校领导.pdf</a></p></div>';
    target.html(html + attach);
}

function topdiscuss(mid) {
    $.ajax({
        url: "/reverse/mesgTopInfo.action",
        type: "post",
        dataType: "json",
        async: true,
        data: {
            'messageId': mid
        },
        success: function(data) {
        	if (data.resultCode==0) {
        		  ret = data;
                  getHeadNote(1,8);
        	}
        },
        complete: function() {}
    });
}

function deleteNote(mid) {
    if (confirm('确定要删除该条通知吗？')) {
    	var page = parseInt($('#example ul li.active').children('a').html());
        $.ajax({
            url: "/reverse/deleteMessageInfo.action",
            type: "post",
            dataType: "json",
            async: true,
            data: {
                'messageId': mid
            },
            success: function(data) {
                ret = data;
                getHeadNote(page,8);
            },
            error: function() {
                console.log('deleteMessageInfo error');
            }
        });
    }
}

function getHeadNoteDetail(mid, cpage, index) {
    $.ajax({
        url: "/reverse/selMessageDetailInfo.action",
        type: "get",
        dataType: "json",
        async: true,
        data: {
            'messageId': mid
        },
        success: function(data) {
            ret = data;
            showHeadNoteDetail(data, mid, cpage, index);
        },
        complete: function() {
            $("#uploadForm").attr('action', audioUploadAction + '&mesgid=' + mid);
            if (index == 2) {
                getReplyList(mid, 1, 8, index);
                $('#example').show();
            } else {
                $('#example').hide();
            }
            $("#reply_list").html('').show();
        }
    });
}

function showHeadNoteDetail(data, mid, cpage, index) {
    var target = $('.release-headmaster-note');
    var html = '';
    var ds = '';
    html += '<div style="cursor:pointer;" onclick="getHeadNote();"><a>返回></a></div>';
    if (data.fzMesgDetailInfo) {
        var txt = '通知';
        ds = data.fzMesgDetailInfo;
        html += '<div style="position:relative;" msgid="' + mid + '"><img src="' + ds.minImageURL + '" /><p style="margin-bottom:5px"><a style="margin-right:10px;">[' + txt + ']</a>';
        html += ds.mesgname + '</p><p style="margin-top:5px"><span style="margin-right:10px;">' + ds.nickName + '</span>';
        html += '<span>于' + ds.createtime + '发表</span></p>';
        html += '</div>';
    }

    if (data.attachmentInfoList) {
        html += '<div style="border-bottom:none;"><p style="margin-bottom:20px;text-indent:2em;">' + ds.mesgcontent + '</p>';
        if (ds.voicename) {
            html += '<p style="margin-left:30px;margin-bottom:10px;"><a onclick="PlayedMusic($(this),' + ds.id + ');" url="' + ds.voicename + '"><img src="/img/yuyin.png">播放</a></p>';
        }
        for (var i in data.attachmentInfoList) {
            var at = data.attachmentInfoList[i];
            html += '<p style="margin-left:30px;margin-bottom:10px;"><a href="/reverse/FileDownload?fileName=' + encodeURI(encodeURI(at.pathname)) + '"><img src="/img/fileattach.png"/>附件' + (parseFloat(i) + 1) + ':' + at.name + '</a></p>';
        }
        html += '</div>';
    }
    target.html(html);
}