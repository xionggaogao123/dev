<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.pojo.app.SessionValue"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<title>我的首页-复兰科技 K6KT-快乐课堂</title>
<link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
<link rel="stylesheet" href="/static/css/bootstrap-combined.school.css"/>
<link rel="stylesheet" type="text/css" href="/static/css/dialog.css"/>
<link rel="stylesheet" href="/static/css/teacherSheet.css?v=1209"/>
<link rel="stylesheet" type="text/css" href="/static/plugins/uploadify/uploadify.css"/>
<link rel="stylesheet" type="text/css" href="/static/plugins/uploadify/uploadify-userpage.css"/>
<link rel="stylesheet" type="text/css" href="/static/css/tp_vote.css">
<link rel="stylesheet" type="text/css" href="/static/css/questionnaire/style.css?v=122"/>
<link rel="stylesheet" type="text/css" href="/static/plugins/fancyBox/jquery.fancybox.css?v=2.1.5" media="screen"/>
<script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js" type="text/javascript"></script>
<script src="/static/js/shareforflipped.js" type="text/javascript"></script>
<script type="text/javascript" src="/static/js/swfobject.js"></script>
<script type="text/javascript" src="/static/js/recorder.js"></script>
<script type="text/javascript" src="/static/plugins/cameraform/htdocs/webcam.js"></script>
<script type="text/javascript" src="/static/js/main.js"></script>
<script type="text/javascript" src="/static/js/myexam.js"></script>
<script type="text/javascript" src="/static/js/template.js"></script>
<script type="text/javascript" src="/static/js/yphomeWithDel.js"></script>
<script type="text/javascript" src="/static/js/card.js"></script>
<script type="text/javascript" src="/static/js/experienceScore.js"></script>
<script type="text/javascript" src="/static/plugins/uploadify/jquery.uploadify.min.js"></script>
    <script type="text/javascript" src="/static/js/common/role.js"></script>
<script type="text/javascript" src="/static/js/vote.js"></script>


<script type="text/javascript" src="/static/plugins/jquery-upload/vendor/jquery.ui.widget.js"></script>
<script type="text/javascript" src="/static/plugins/jquery-upload/jquery.iframe-transport.js"></script>
<script type="text/javascript" src="/static/plugins/jquery-upload/jquery.fileupload.js"></script>
<script type="text/javascript" src="/static/js/WdatePicker.js"></script>
<script src='http://static.polyv.net/file/polyvplayer_v2.0.min.js'></script>
<script type="text/javascript" src="/static/plugins/fancyBox/jquery.fancybox.js?v=2.1.5"></script>




<style type="text/css">


	.Wdate{
		border:#999 1px solid;
		height:20px;
		background:#fff url(/img/datePicker.gif) no-repeat right;
	}
	.Wdate::-ms-clear{display:none;}
	
	.WdateFmtErr{
		font-weight:bold;
		color:red;
	}
    .cross_right {
        position: relative;
        float: right;
        font-size: 11px;
        font-weight: normal;
    }

    #content_main_container {
      position:relative;
        min-height: 66%;
        filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='/img/fc_user_bg_l.png', sizingMethod='crop');
    }

    #content_main {
        width: 900px;
        margin: 0 auto;
    }

    #content_left {
        float: left;
        width: 249px;
        margin-right: 20px;
    }
    #content_right{
        overflow:hidden;
    }
    #content_right > h4 {
        border-left: 4px solid rgb(255, 126, 0);
        padding-left: 10px;
    }

    /*与上一个框相距20px*/
    #fc_student_course {
        margin-top: 20px;
        background: white;
        border: 1px solid #D0D0D0;
    }

    /* course description  section **/
    .course_detail {
        padding: 10px 0 10px 10px;
        float: left;
        width: 105px;
        height: 144px;
        overflow: hidden;
    }

    .course_detail > * {
        float: left;
    }

    .course_detail > a > img {
        width: 105px;
        height: 116px;
        margin-bottom: 10px;
    }

    .course_des {
        width: 103px;
        text-align: center;
        font-size: 12px;
        color: #696969;
    }

    /** student rank */
    #fc_stu_rank {
        margin-top: 20px;
        background: white;
        border: 1px solid #D0D0D0;
        border-bottom: none;
        margin-bottom: 30px;
    }

    #fc_stu_rank > div > p > * {
        float: left;
    }

    #fc_stu_rank > div > p {
        padding: 10px 15px;
    }

    #fc_stu_rank > div > p > img {
        width: 46px;
        height: 46px;
    }

    #fc_stu_rank > div > p span {
        margin-top: 15px;
        margin-bottom: 15px;
    }

    .exprience {
        background: rgb(61, 135, 198);
        padding: 1px 3px;
        border-radius: 2px;
        color: white;
    }

    #discuss_list {
        background: white;
    }

    #discuss_list > div {
        float: left;
        padding: 10px;
        border: 1px solid #d0d0d0;
        border-top: none;
        width: 748px;
    }

    #discuss_list > div > * {
        float: left;
    }

    #discuss_list > div > img {
        width: 48px;
        height: 48px;
        margin-right: 10px;
    }

    #discuss_list > div > p {
        width: 500px;
        word-break: break-all;
        word-wrap: break-word;
    }

    /**   ***/
    #reply_list {
        border: 1px solid #d0d0d0;
        background: white;
        border-top: none;
    }

    .reply_detail {
        margin: 21px 25px;
        border-top: 1px solid #d0d0d0;
    }

    .reply_detail > * {
        float: left;
    }

    .reply_detail > div {
        margin-top: 15px;
    }

    .reply_detail > div > p {
        margin: 10px 0;
    }

    #reply_section {
        border: 1px solid #d0d0d0;
        background: white;
        padding: 15px 15px 15px 30px;
        margin-bottom: 30px;
    }

    #reply_section > div {
        margin: 10px 0;
    }

    #reply_section > div > * {
        float: left;
        margin-right: 15px;
    }

    #reply_section > div > span {
        margin-top: 5px;
    }

    #reply_section .submit {
        position: absolute;
        left: 450px;
        top: 110px;
        padding: 5px 10px;
        color: white;
        background: rgb(255, 126, 0);
    }

    .bigsize {
        font-size: 20px;
        font-weight: bold
    }



    /*exam*/
    .newexam_container {
        display: none;
    }

    .myexam_bar {
        border-bottom: 1px solid #d0d0d0;
        padding-top: 20px;
        padding-bottom: 20px;
        font-size: 17px;
        line-height: 30px;
    }

    .exam_list {
        background: white;
    }

    .exam_list > div {
        float: left;
        padding: 10px;
        border: 1px solid #d0d0d0;
        border-top: none;
        width: 589px;
    }

    .exam_list > div > * {
        float: left;
    }

    .exam_list > div > img {
        width: 48px;
        height: 48px;
        margin-right: 10px;
    }

    .exam_list > div > p {
        width: 500px;
    }

    .exam_title {
        font-size: 16px;
        margin-right: 10px;
    }

    .answer-sheet {
        border-left: 1px solid #d0d0d0;
        border-right: 1px solid #d0d0d0;
        border-bottom: 1px solid #d0d0d0;
        padding: 20px 20px;
        display: none;
    }

    .ques-class {
        font-weight: bold;
        padding-bottom: 5px;
        margin-bottom: 5px;
        border-bottom: 1px dashed #333;
    }

    .ques-title {
        font-weight: bold;
        margin-bottom: 10px;
    }

    .ques-content {
        margin-bottom: 10px;
        margin-top: 10px;
    }

    .textarea-answer {
        width: 83%;
        border: 1px solid #d0d0d0;
        border-radius: 4px;
    }

    .commit-btn {
        margin-left: 45%;
        background-color: #FF7000;
        color: #fff;
        border-radius: 3px;
        font-size: 15px;
    }

    .commit-btn:hover {
        background-color: rgb(255, 176, 103);
    }

    .ques-container input[type="checkbox"] {
        margin-left: 20px;
    }

    .ques-container textarea {
        border: 1px solid gray;
        border-radius: 3px;
        padding: 5px;
        font-size: 12px;
        height: 60px;
        width: 500px;
    }

    .ques-container input[type="file"] {
        position: absolute;
        left: 0;
        width: 80px;
        height: 30px;
        opacity: 0;
        filter: alpha(opacity=0);
        cursor: pointer;
    }
</style>
<script type="text/javascript">
    (function () {
        var showType = location.href.split('?')[1];
        if(typeof showType != 'undefined' && (showType.indexOf("index=2") >= 0 || showType.indexOf("index=8") >= 0 || showType.indexOf("showtype=1") >= 0) )
        {
            getFriendBlogInfo = function() {};
        }
    })();

    $(function () {
        $('#blog_content').focus(function () {
            $(this).attr('rows', '4');
        });

        $('#blog_content').blur(function () {
            var content = $(this).val();
            if (content == '') {
                $(this).attr('rows', '2');
            }
        });
    });


    function PlayedMusic(ob, rid) {
        var player = '<embed src="/upload/voice/' + $(ob).attr('url') + '" belong="' + rid + '" width="0" height="0" autostart="true" />';
        $('embed[belong=' + rid + ']').remove();
        $('body').append(player);
    }
</script>
<script type="text/javascript">
var currentPageID = 0;
var currentPage = 1;
var audioUploadAction = 'audiosave.jsp?userId=' + '${currentUser.id}' + '&voicetype=0';

var messageid = 0;
var nb_role = '${sessionValue.userRole}';
var nb_username='${sessionValue.userName}';
var nickname = '${sessionValue.userName}';
var imageUrl = '${sessionValue.maxAvatar}';
var userId = '${sessionValue.id}';
var userRole = ${sessionValue.userRole};


function getCourses(cid, async) {
//    var ret = [];
//    async = typeof async == 'undefined' ? false : true;
//    $.ajax({
//        url: "/reverse/selNewLesson.action",
//        type: "post",
//        dataType: "json",
//        async: async,
//        data: {
//            classid:cid
//        },
//        success: function(data) {
//            ret = data;
//            showCourses(data);
//        },
//        complete: function() {}
//    });
//    return ret;
}

function showCourses(data) {
    var html = '';
    if (data.rows) {
        for (var i = 0; i < data.rows.length; i++) {
            var co = data.rows[i];
            var cimg = co.imageUrl ? co.imageUrl : '/img/filecourse_newest.png';
            if (i % 2 == 0) {
                html += '<div class="course_detail" style="padding-right:10px;">';
            } else {
                html += '<div class="course_detail">';
            }
            html += '<a href="/courseview/' + co.id + '"><img src="' + cimg + '" /></a>';
            html += '<span class="course_des ellipsis">' + co.courseName + '</span></div>';
        }
    }
    var tdiv = $('#fc_course_list');
    tdiv.html(html);
}
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}

/* 获取讨论列表  ********************************************/
function getDiscusses(cid, classtype, coursetype, page, size) {
    $('#discuss_list').html('');
    $('#discuss_list').addClass('hardloadingb');
    var indextype = getQueryString("index");
    var mesgtype = 1;
    if (indextype==2) {
        mesgtype=2;
    } else {
        mesgtype = 1;
    }

    $.ajax({
        url: "/homework/student/list.do",
        type: "get",
        dataType: "json",
        async: true,
        data: {
            'classId': cid,
            'classtype': classtype,
            'type' : coursetype,
            'mesgtype': mesgtype,
            'page': page,
            'pageSize': size,
            'blogtype':blogType
        },
        success: function(data) {
            ret = data;
            showDiscusses(data, page);
            var total = data.rows.length > 0 ? data.total : 0;
            var to = Math.ceil(total / size);
            totalPages = to == 0 ? 1 : to;
            if (page == 1) {
                resetPaginator(totalPages);
            }else {
                resetPaginatorCurrentPage(page,totalPages);
            }
        },
        complete: function() {
            $('#discuss_list').removeClass('hardloadingb');
            if (!ret.rows || ret.rows.length == 0) {
                $('#example').hide();
            } else {
                $('#example').show();
            }
            $('#reply_section').hide();
            $("#reply_list").hide().html('');
        }
    });
}

function showDiscusses(data, cpage) {
    var target = $('#discuss_list');
    var html = '';
    if (data.rows) {
        var txt = getQueryString("index");
        txt = txt == 8 ? '通知' : '作业';
        for (var i in data.rows) {
            var ds = data.rows[i];
            var lastreply = "";
            if (ds.lastSubmitTime) {
                var lastreplyTime = getAdaptTimeFromNow(ds.lastSubmitTime);
                lastreply = '<span style="margin-left:15px;">最后回复' + lastreplyTime + '</span>';
            }
            var isbold = 'normal';
            if (!ds.isread) {
                isbold = 'bold';
            }
            var viewtxt = ds.submitCount == 0 ? '' : '已回复' + ds.submitCount + '条';
            var attach = '';
            if (ds.voiceFile) {
                attach += '<img src="/img/mic_work.png" style="margin-left:10px;"/>';
            }
            if (ds.voiceFile) {
                attach += '<img src="/img/fileattach.png" style="margin-left:10px;"/>';
            }
            html += '<div class="discuss-info" style="cursor:pointer;" msgid="' + ds.id + '"><img class="discuss-info-img" src="' + ds.userAvatar + '" target-id="' + ds.userid + '" role="' + ds.role + '"/>';
            html += '<p style="margin-bottom:5px;font-weight:' + isbold + ';word-break: break-all;" onclick="getDiscussDetail(\'' + ds.id + '\',' + cpage + ');"><a style="margin-right:10px;">[' + txt + ']</a>';
            html += ds.title + attach;
            if(txt!=1) {
                if((ds.onlinesubmittype==1) && (ds.mesgtype==2)) {
                    html+= '<span class="tiji-online">在线提交</span>';
                }else if((ds.onlinesubmittype==0) && (ds.mesgtype==2)){
                    html+= '<span class="tiji-offline">课堂提交</span>';
                }
            }
            html += '</p><p style="margin-top:5px"><span style="margin-right:10px;">' + ds.userName + '</span>';
            html += '<span>于' + ds.time + '发表</span>' + lastreply + '<span style="margin-left:10px;">' + viewtxt + '</span></p>';
            if (ds.istop) {
                html += '<a class="topdiscuss"><img cantop="1" istop="1" src="img/set_top.png" title="置顶"></a>';
            }
            html += '</div>';
        }
    }
    target.append(html);
}

function resetPaginator(totalPages) {
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
            if($(".chosen").attr('index') == 3){
                getStudentExamPaper(page, 8);
            }else{
                getDiscusses($('#fc_my_classes .my_class_item.active').attr('cid'), $('#fc_my_classes .my_class_item.active').attr('classtype'),$('#fc_my_classes .my_class_item.active').attr('ctype'), page, 8);
            }
        }
    });
}

function resetPaginatorRe(totalPages) {
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
            getReplyList($("#discuss_list >div:nth(1)").attr('msgid'), page, 8);
        }
    });
}

function resetPaginatorCurrentPage(page,totalPages) {
    $('#example').bootstrapPaginator("setOptions",{
        currentPage: page,
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
        onPageClicked: function(e,originalEvent,type,page){
            currentPage=page;
            if($(".chosen").attr('index') == 3){
                getStudentExamPaper(page, 8);
            }else{
                getDiscusses($('#fc_my_classes .my_class_item.active').attr('cid'),$('#fc_my_classes .my_class_item.active').attr('classtype'),$('#fc_my_classes .my_class_item.active').attr('ctype'),page,8);
            }
        }
    });
}


function resetSubmitForm() {
    $('#reply_section input[type=text]').val('');
    $('#reply_section textarea').val('');
    $('.uploadedFiles').remove();
    $('.play_button').hide();
    $('#upload-photo-container').empty();
    if (FWRecorder.recorder) {
        FWRecorder.defaultSize();
        $("#save_button object").css('opacity', 0);
    }
    $('p a.voice').remove();
}

/**** 提交回复  ***************************************************/
function submitReply() {
    var content = $('#reply_section textarea').val();

    var flist = '';

    $('.uploadedFiles').each(function() {
        var localname = $(this).text();
        flist += $(this).attr('realname') + ',' + localname + ';';
    });

    for (var i = 0; i < $('.img-photo-take').length; i++) {
        var localname = '拍摄照片' + i;
        flist += $('.img-photo-take').eq(i).attr('realname') + ',' + $('.img-photo-take').eq(i).attr('textname') + ';';
    }

    if (content.length > 0) {

        var activeClassId = $('.my_class_item.active').attr('cid');
        $.ajax({
            url: "/homework/submit.do",
            type: "post",
            dataType: "json",
            async: true,
            data: {
                'content': content,
                'classId':activeClassId,
                'hwId': $("#discuss_list >div:nth(1)").attr('msgid'),
                'filenamelist': flist,
                'voicefile': $('.voice').attr('url')
            },
            success: function(data) {
                ret = data;
                getDiscussDetail($("#discuss_list >div:nth(1)").attr('msgid'));
            },
            complete: function() {
                $('#submitloading').hide();
                resetSubmitForm();
                if (!!window.ActiveXObject || "ActiveXObject" in window){//判断ie
                    window.location.reload();
                }
            }
        });
    } else {
        alert("请输入内容！");
    }
}
// 获取系统通知
function getSystemMsgDetail() {
    $('#discuss_list').html('');
    $('#submit_section').hide();
    var attach = '';
    var html = '<div style="cursor:pointer;" onclick="getDiscusses('+ $('#fc_my_classes .my_class_item.active').attr('cid') + ',1,' + $('#fc_my_classes .my_class_item.active').attr('ctype') + ',1,8);"><a>返回></a></div><div class="discuss-info"><img class="discuss-info-img" src="/img/logo_b.png"/><p style="margin-bottom:5px;word-break: break-all;"><a style="margin-right:10px;">[通知]</a>亲，你的用户手册来啦！</p><p style="margin-top:5px"><span style="margin-right:10px;">复兰科技</span><span>于2014-08-28 09:00:00发表</span></p></div><div style="border-bottom:none;"><p style="margin-bottom:20px;text-indent: 2em;word-break: break-all;">校领导、老师、学生、家长均可点击附件下载对应版本的“K6KT-快乐课堂”用户手册，获得更多操作帮助！</p>';
    attach+='<p style="margin-left:30px;margin-bottom:10px;"><a href="/reverse/FileDownload?fileName=/upload/resources/用户手册-家长.pdf"><img src="/img/fileattach.png"/>附件1:用户手册-家长.pdf</a></p>';
    attach+='<p style="margin-left:30px;margin-bottom:10px;"><a href="/reverse/FileDownload?fileName=/upload/resources/用户手册-学生.pdf"><img src="/img/fileattach.png"/>附件2:用户手册-学生.pdf</a></p>';
    attach+='<p style="margin-left:30px;margin-bottom:10px;"><a href="/reverse/FileDownload?fileName=/upload/resources/用户手册-老师.pdf"><img src="/img/fileattach.png"/>附件3:用户手册-老师.pdf</a></p>';
    attach+='<p style="margin-left:30px;margin-bottom:10px;"><a href="/reverse/FileDownload?fileName=/upload/resources/用户手册-校领导.pdf"><img src="/img/fileattach.png"/>附件4:用户手册-校领导.pdf</a></p></div>';
    $('#discuss_list').html(html+attach);
    $('#example').hide();
    $('#discuss_list >div:nth(2)').css('border-bottom','');
    //getNotifyNum($('#fc_my_classes .my_class_item.active').attr('cid'),$('#fc_my_classes .my_class_item.active').attr('classtype'), $('#fc_my_classes .my_class_item.active').attr('ctype'));
}

/*  显示通知&作业详细  **************************************/
function getDiscussDetail(mid, cpage) {
    $('#discuss_list').html('');
    $('#discuss_list').addClass('hardloadingb');
    $.ajax({
        url: "/homework/detail.do",
        type: "get",
        dataType: "json",
        async: true,
        data: {
            'hwid': mid
        },
        success: function(data) {
            ret = data;
            showDiscussDetail(data, mid, cpage);
        },
        complete: function() {
            messageid = mid;
            $("#uploadForm").attr('action', audioUploadAction + '&mesgid=' + mid);
            if ($(".chosen").length > 0 && $(".chosen").attr('index') == 2) {
                $('#reply_section').show();
                getReplyList(mid, 1, 8);
                $('#example').show();
            } else {
                $('#reply_section').hide();
                $('#example').hide();
            }
            //getNotifyNum($('#fc_my_classes .my_class_item.active').attr('cid'), $('#fc_my_classes .my_class_item.active').attr('classtype'), $('#fc_my_classes .my_class_item.active').attr('ctype'));
            $("#reply_list").html('').show();
            $('#discuss_list').removeClass('hardloadingb');
        }
    });
}

function showDiscussDetail(data, mid, cpage) {
    var target = $('#discuss_list');
    var html = '';
    var ds = '';
    var cid = $('#fc_my_classes .my_class_item.active').attr('cid');
    var classtype = $('#fc_my_classes .my_class_item.active').attr('classtype');
    var coursetype = $('#fc_my_classes .my_class_item.active').attr('ctype');
    html += '<div style="cursor:pointer;" onclick="getDiscusses(\'' + cid + '\',' + classtype + ','+ coursetype + ',' + cpage + ',8);"><a>返回></a></div>';
    if (data) {
        var txt = $(".chosen").attr('index');
        txt = txt == 1 ? '通知' : '作业';

        ds = data;
        html += '<div class="discuss-info" msgid="' + ds.id + '"><img class="discuss-info-img" src="' + ds.userAvatar + '" target-id="' + ds.userid + '" role="' + ds.role + '"/>';
        html += '<p style="margin-bottom:5px;word-break: break-all;"><a style="margin-right:10px;">[' + txt + ']</a>';
        html += ds.title;
        if(txt!=1) {
            //if((ds.onlinesubmittype==1) && (ds.mesgtype==2)) {
                html+= '<span class="tiji-online">在线提交</span>';
            //}else if((ds.onlinesubmittype==0) && (ds.mesgtype==2)){
            //    html+= '<span class="tiji-offline">课堂提交</span>';
            //}
        }
        html += '</p><p style="margin-top:5px"><span style="margin-right:10px;">' + ds.userName + '</span>';
        html += '<span>于' + ds.time + '发表</span></p>';
        html += '</div>';
    }
    html += '<div style="border-bottom:none;"><p style="margin-bottom:20px;text-indent: 2em;word-break: break-all;">' + ds.content + '</p>';

    if (data.voiceFile&& data.voiceFile.length>0) {
        html += '<p style="margin-left:30px;margin-bottom:10px;"><a onclick="playVoice(\'' + data.voiceFile[0].value + '\');" url="' + data.voiceFile[0].value  + '"><img src="/img/yuyin.png">播放</a></p>';
    }
    if (data.docFile&& data.docFile.length>0) {

        for (var i in data.docFile) {
            var at = data.docFile[i];
            html += '<p style="margin-left:30px;margin-bottom:10px;"><a href="/commondownload/file.do?url=' + encodeURI(at.value) + '&name='+at.name+'"><img src="/img/fileattach.png"/>附件' + (parseFloat(i) + 1) + ':' + at.name + '</a></p>';
        }
        html += '</div>';
    }
    target.html(html);
}

/***  获取回复列表    ********************/

function getReplyList(mid, page, size) {
    $("#reply_list").addClass('hardloadingb');
    $("#reply_list").show();

    var activeClassId = $('.my_class_item.active').attr('cid');
    $.ajax({
        url: "/homework/submit/list.do",
        type: "get",
        dataType: "json",
        async: true,
        data: {
            'hwid': mid,
            'classId':activeClassId,
            'page': page,
            'pageSize': size,
            'did':new Date().getTime()
        },
        success: function(data) {
            ret = data;
            showReplyList(data);
            var total = data.rows.length > 0 ? data.total : 0;
            var to = Math.ceil(total / size);
            totalPages = to == 0 ? 1 : to;
            if (page == 1) {
                resetPaginatorRe(totalPages);
            }
            if (total > 0) {
                $('#example').show();
            } else {
                $('#example').hide();
                $("#reply_list").hide();
                $('#discuss_list >div:nth(2)').css('border-bottom', '');
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
            var timeStr = getAdaptTimeFromNow(hf.time);
            var pichtml = '';
            var atthtml = '';

            html += '<div style="position:relative;"><div class="reply_detail"><div style="margin-right:15px;"><img src="' + hf.avatar + '" style="width:45px;height:45px;"/></div>';
            html += '<div><p><span style="font-size: 16px;color:#696969;margin-right:20px;">' + hf.userName + '</span><span style="color:#9a9a9a;">发表于' + timeStr + '</span></p>';
            if (hf.isview == 1) {
                html += '<p style="word-break:break-all;">' + hf.content + '</p>';
                if (hf.voiceFile&& hf.voiceFile.length>0) {
                    html += '<p style="margin-left:30px;margin-bottom:10px;"><a onclick="playVoice(\'' + hf.voiceFile[0].value + '\');" url="' + hf.voiceFile[0].value  + '"><img src="/img/yuyin.png">播放</a></p>';
                }
                for (var j in hf.docFile) {
                    var file = hf.docFile[j];
                    if(checkImage(file.value)) {
                        pichtml += '<a class="fancybox" href="'+ encodeURI(file.value) +'" data-fancybox-group="homework"><img class="homework-img" title="点击查看大图" src='+ encodeURI(file.value) +'></a>';
                    }else {
                        atthtml+='<p><a href="/commondownload/file.do?url=' + encodeURI(file.value) + '&name='+file.name+'"><img src="/img/fileattach.png" />附件' + (parseFloat(j) + 1) + '：' + file.name + '</a></p>';
                    }
                }
                html+='<div class="contentImg-container">'+pichtml+'</div>'+atthtml;
            } else {
                html += '<p>该作业已提交！</p>';
            }
            html += '</div><a class="delreply" onclick="delReply(' + hf.id + ');"><img candel="' + hf.ismytask + '" src="img/dustbin.png" title="删除" style="display:none;position:absolute;top:25px;right:25px;"/></a>';
            html += '</div></div>';
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

function checkImage(str) {
    var ext = str.substring(str.length-3,str.length);
    var rule = /(jpg|gif|bmp|png)/;
    if(rule.test(ext)) {
        return true;
    }else {
        return false;
    }
}

function delReply(id) {
    if (confirm('确定删除这条记录？')) {
        $.ajax({
            url: "/reverse/delStuTaskInfo.action",
            type: "post",
            dataType: "json",
            async: true,
            data: {
                'mesgreplyid': id
            },
            success: function(data) {
                ret = data;
                getDiscussDetail($("#discuss_list >div:nth(1)").attr('msgid'));
            },
            complete: function() {}
        });
    }
}

//在页面加载完成之后,注册js代码
$(function() {
    $('.loading').show();
    $('#fc_my_classes').find('div.my_class_item:nth(0)').addClass('active');
    getStudentRank(true,$('#fc_my_classes .my_class_item.active').attr('cid'));
    getCourses($('#fc_my_classes .my_class_item.active').attr('cid'));
    var currentClassId = $('#fc_my_classes .my_class_item.active').attr('cid');


    $("#fc_my_classes .my_class_item").each(function(i) {
        $(this).bind('click', function() {
            $(this).siblings().removeClass('active');
            $(this).addClass('active');
            var cid = $(this).attr('cid');
            var classtype = $(this).attr('classtype');
            var coursetype = $(this).attr('ctype');
            resetSubmitForm();
            getStudentRank(true, cid);
            getCourses(cid);
            //getNotifyNum(cid, classtype,coursetype);
            $(".tab_button.chosen").click();
            currentClassId = cid;
        });
    });


    /* 作业通知切换 */
    $(".tab_button").each(function(i) {
        if (i == 0) {
            $(this).addClass('chosen');
        } else {
            $(this).addClass('notchosen');
        }
        $(this).bind('click', function() {
            if (!$(this).hasClass('chosen')) {
                $(this).addClass('chosen').removeClass('notchosen');
                $(this).find('span').removeClass('notity_b');
            }
            $(this).siblings().removeClass('chosen').addClass('notchosen');
            $(this).siblings().find('span').addClass('notity_b');
            resetSubmitForm();
            var cid = $('#fc_my_classes .my_class_item.active').attr('cid');
            var classtype = $('#fc_my_classes .my_class_item.active').attr('classtype');
            var coursetype = $('#fc_my_classes .my_class_item.active').attr('ctype');
            //getNotifyNum(cid, classtype,coursetype);
            var index = $(this).attr('index');
            $('.vote-container').hide();
            if (index == 3) {
                getStudentExamPaper(1,8);
                $('.answer-sheet').hide();
                $('.mycomment-container').hide();
                $('.friend-container').hide();
                $('#submit_section').hide();
                $('.newexam_container').show();
                $('#discuss_list').hide();
                $('.exam_list').show();
                $('.myexam_bar>span').text("在线考试");
                $('.newexam_form').hide();
                $('.viewexam-container').hide();
                $('.correctexam-container').hide();
                $('.one_blog_container').hide();
                $('#reply_section').hide();
                $('#reply_list').hide();
                $('#example').show();
            } else if (index == 0) {
                $('#blog_content').val('');
                $('.answer-sheet').hide();
                $('#reply_section').hide();
                $('.mycomment-container').hide();
                $('.friend-container').show();
                $('#submit_section').hide();
                $('#discuss_list').hide();
                $('.newexam_container').hide();
                $('.order-bar .active').click();
                $('.one_blog_container').hide();
                $('#reply_list').hide();
                if(${roles:isStudentOrParent(sessionValue.userRole)}){//学生
	                $('#myinfo').addClass("myblog");
	                $('#mybloginfo').addClass("myblog");
                    $('#form-container').hide();
                }else{
                	$('#myinfo').removeClass("myblog");
                    $('#mybloginfo').removeClass("myblog");
                    $('#form-container').show();
                }
            } else if (index == 4) {//i=1 微家园
                resetSubmitForm();
                $('#reply_list').show();
                $('.answer-sheet').hide();
                $('.mycomment-container').hide();
                $('.friend-container').show();
                $('#submit_section').hide();
                $('#discuss_list').hide();
                $('.newexam_container').hide();
                $('.order-bar .active').click();
                $('.one_blog_container').hide();
                $('#reply_list').hide();
                if(${roles:isTeacher(sessionValue.userRole)} || (${roles:isHeadmaster(sessionValue.userRole)} && ${roles:isK6ktHelper(sessionValue.userRole)})){//老师
                	$('#myinfo').addClass("myblog");
					$('#mybloginfo').addClass("myblog");
                    $('#form-container').hide();
                }else{
                	$('#myinfo').removeClass("myblog");
                    $('#mybloginfo').removeClass("myblog");
                    $('#form-container').show();
                }
            }else if(index == 5) {//投票选举
            	getVoteList(0,5,1);
            	$('.vote-container').show();
                $('.answer-sheet').hide();
                $('#reply_list').hide();
                $('.mycomment-container').hide();
                $('.friend-container').hide();
                $('#submit_section').hide();
                $('#discuss_list').hide();
                $('#reply_section').hide();
                $('.newexam_container').hide();
                $('.one_blog_container').hide();
                $('#reply_list').hide();
                if(${roles:isTeacher(sessionValue.userRole)} || (${roles:isHeadmaster(sessionValue.userRole)} && ${roles:isK6ktHelper(sessionValue.userRole)})){//老师
                	$('#myinfo').addClass("myblog");
                    $('#mybloginfo').addClass("myblog");
                    $('#form-container').hide();
                }else{
                	$('#myinfo').removeClass("myblog");
                    $('#mybloginfo').removeClass("myblog");
                    $('#form-container').show();
                }
            }else{
                getDiscusses(cid, classtype,coursetype, 1, 8);
                $('#submit_section').show();
                $('.newexam_container').hide();
                $('#discuss_list').show();
                $('.friend-container').hide();
                $('.mycomment-container').hide();
                $('.one_blog_container').hide();
                $('.answer-sheet').hide();
                $('#reply_list').hide();
            }
        });
    });


    /*  上传附件   ****************************************/
    $('#file_attach').fileupload({
        url: '/homework/uploadattach.do',
        start: function(e) {
            $('#fileuploadLoading').show();
        },
        done: function(e, data) {
            if (data.dataType == 'iframe ') {
                var response = $( 'pre', data.result ).text();
            } else {
                var response = data.result;
            }
            try {
                var ob = response;
                if (ob.uploadType == 0) {
                    alert("附件上传失败！");
                }
                var uf = $('<p style="padding:5px 0;cursor:pointer;" class="uploadedFiles" realname="' + ob.realname + '">' + data.files[0].name + '</p>').hover(function () {
                    if ($(this).find('img').length == 0) {
                        $(this).append("<a style=\"margin-left:20px;\" onclick=\"$(this).closest('p').remove();\"><img src=\"/img/dustbin.png\" /></a>");

                    } else {
                        $(this).find('img').show();
                    }
                }, function () {
                    $(this).find('img').hide();
                });
                $("#reply_section").append(uf);
            } catch (err) {
            }
        },
        fail: function (e, data) {

        },
        always: function (e, data) {
            $('#fileuploadLoading').hide();
        }
    });


    <!--修改为使用侧边栏的班级-->
    //getNotifyNum($('#fc_my_classes .my_class_item.active').attr('cid'), $('#fc_my_classes .my_class_item.active').attr('classtype'), $('#fc_my_classes .my_class_item.active').attr('ctype'));

    $(".tab_button:nth(0)").click();

    $('#example').bootstrapPaginator({
        currentPage: 1,
        totalPages: 1,
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
        onPageClicked: function(e, originalEvent, type, page) {}
    });

    $('.loading').fadeOut(1000);

    // 拍照
    webcam.set_quality(90);
    webcam.set_shutter_sound(true);
    webcam.set_hook('onComplete', 'uploadPhoto');

    $('.not-found-close').on('click', function() {
        $('.not-found-camera').hide();
        $('.alert-bg').hide();
    });

    $('.take-photo').on('click', function() {
        $('.alert-bg').show();
        $('.take-photo-container').show();
        $('.take-photo-btn').show();
    });

    $('.take-photo-btn').on('click', function() {
        webcam.freeze();
        $('.take-photo-btn').hide();
        $('.take-photo-again').show();
        $('.take-photo-upload').show();
    });
    $('.take-photo-again').on('click', function() {
        webcam.reset();
        resetPhoto();
    });

    $('.take-photo-upload').on('click', function() {
        webcam.dump();
        $('.photo-close').click();
    });

    $('.photo-close').on('click', function() {
        $('.alert-bg').hide();
        $('.take-photo-container').hide();
        resetPhoto();
    });

    $('#upload-photo-container').on('click', '.delete-photo', function() {
        $(this).parent().remove();
    });
});

function resetPhoto() {
    $('.take-photo-btn').show();
    $('.take-photo-again').hide();
    $('.take-photo-upload').hide();
}

function uploadPhoto(base64) {
    var target = $('#upload-photo-container');
    src = 'data:image/jpeg;base64,' + base64;
    $.ajax({
        url: '/commonupload/base64image.do',
        type: 'post',
        dataType: 'json',
        data: {
            base64ImgData: base64
        },
        success: function(data) {
            console.log(data);
            target.append('<div class="upload-photo-img"><img class="img-photo-take" src="' + src + '" realname="' + data.path + '" textname="'+data.name+'"><img class="delete-photo" src="/img/error-grey.png"></div>');
        },
        error: function() {
            console.log('convertImages error');
        }
    });
    webcam.reset();
}

var foo = true;
function showflash(container)
{
    var mc = new FlashObject("/static/plugins/audiorecorder.swf", "recorderApp", "350px", "23px", "8");
    mc.setAttribute("id","recorderApp");
    mc.setAttribute("name","recorderApp");

    mc.addVariable("uploadAction","/homework/uploadvoice.do");
    mc.addVariable("fileName","audio");
    mc.write("myContent");
    if (foo) {
        $('#'+container).append($('#recorder'));
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
</script>

    <%--引入投票选举的模板--%>
    <jsp:include page="../elect/templateScripts.jsp"></jsp:include>

</head>
<body style="font-family:Microsoft YaHei;" ng-app="index">
<%@ include file="../common_new/head.jsp" %>
<div style="background-color: #ffffff;width: 100%">
<div id="elect-player" class="player-container">
    <div id="elect-player-div" class="player-div"></div>
    <div id="sewise-div" style="display: none; width: 800px; height: 450px;">
        <script type="text/javascript" src="/static/plugins/sewiseplayer/sewise.player.min.js"></script>
    </div>
    <span onclick="closeElectMovie()" class="player-close-btn"></span>
</div>
<script type="text/javascript">
    SewisePlayer.setup({
        server : "vod",
        type : "m3u8",
        skin : "vodFlowPlayer",
        logo : "none",
        lang: "zh_CN",
        topbardisplay : 'disabled',
        videourl: ''
    });
</script>

<c:set var="classList" value="${classInfoList}" scope="request"/>

<jsp:include page="/infoBanner.do">
    <jsp:param name="bannerType" value="homePage"/>
</jsp:include>

<input id="currentId" type="hidden" value="${sessionValue.id}">

<div id="content_main_container" style="width:1200px;overflow: hidden;">
    <div id="content_main" ng-controller="IndexCtrl">
        <%@ include file="../common_new/col-left.jsp" %>
        <div id="content_left" style="display: none">

    <div>
        <div class="questionnaire-index-menu" ng-click="queryQuestionnaire()" onclick="showContent('questionnaire')">
            问卷调查<span ng-bind="unread" ng-show="unread>0" class="sparkle-unread"></span>
        </div>
    </div>


    <div id="fc_student_course">
        <h3 style="padding:10px;color:white;background:rgb(255,126,0);margin:0;">最新课程</h3>
        <div id="fc_course_list">
        </div>
        <div class="slideTogg"></div>

    </div>


    <div id="fc_stu_rank">
        <h3 style="margin:0;color:white;background:rgb(61,135,198);padding:15px;">同学排行</h3>
    </div>
</div>

<div id="content_right">
    <jsp:include page="../common/right_2.jsp"></jsp:include>
    <div id="questionnaire_section" class="hidden" style="min-height: 1200px;background: #F8F8F8;overflow: visible;width: 800px;">
      <jsp:include page="../questionnaire/list.jsp"></jsp:include>
    </div>
    <!-- <h4>家校互动区 </h4>
    <hr style="background-color: #ff7e00; margin-top: 10px;">  -->
<div id="discuss_section" style="overflow: visible">
    <div id="tab_section">
        <a id="weixiaoyuan" index="0" class="tab_button my_friend" style="font-weight: bold;display:none">微校园</a>
        <a id="weijiayuan" index="4" class="tab_button my_home" style="font-weight: bold;display:none">微家园</a>
        <a id="tongzhi" index="1" class="tab_button my_notice" style="font-weight: bold;display:none">我的通知</a>
        <a index="2" class="tab_button my_homework" style="display:none">我的作业</a>
        <%--<a index="3" class="tab_button my_exam">在线考试</a>--%>
        <a index="5" class="tab_button my_vote" style="display:none">投票选举</a>
    </div>

    <%
      String index= request.getParameter("hw");
      if(StringUtils.isNotBlank(index)) {
    %>
    <div id="fc_my_classes" style="min-height: 50px;width: 768px;border: 1px solid #D0D0D0;border-top:none;background-color: #ffffff;line-height: 50px;font-size: 16px;overflow: visible">
        <c:forEach items="${classList}" var="classInfo">
            <div cid="${classInfo.id}" classtype="${classInfo.classType}" class="my_class_item ypstudent" ctype="1">${classInfo.className}</div>
        </c:forEach>
    </div>
    <%
        } else {
    %>
    <div id="fc_my_classes" style="display:none">
        <c:forEach items="${classList}" var="classInfo">
            <div cid="${classInfo.id}" classtype="${classInfo.classType}" class="my_class_item ypstudent" ctype="1">${classInfo.className}</div>
        </c:forEach>
    </div>
    <%}%>
    <div id="" style="width: 770px;overflow: visible">
        <!-- 我的考试 -->
        <div class='newexam_container'>
            <div class='myexam_bar'>
                <span>在线考试</span>
            </div>
            <!-- 我的考试列表 -->
            <div class='exam_list'>
            </div>
            <script type="text/html" id="stu-exam-temp">
                {{each data as value index}}
                <div style="position:relative;">
                    <img src="/img/class.png" />
                    <p style="margin-bottom:5px;">
                        {{if value.state == '0'}}
                        <a onclick="showStudentExamPaper({{value.id}})" class='exam_title'>{{value.name}}</a>
                        {{else if value.state == '1'}}
                        <a onclick="showPaperHaveDone({{value.id}})" class='exam_title'>{{value.name}}</a>
                        {{else}}
                        <a onclick="showPaperCorrected({{value.id}})" class='exam_title'>{{value.name}}</a>
                        {{/if}}
                    </p>
                    <p style="margin-top:5px">
                        <span style="margin-right:10px;">{{value.nickName}}</span>
                        <span>于 {{value.createTime.replace('T', ' ')}} 发表</span>
                        <span style="text-align:center;padding: 0 10px;float:right;border-radius:3px;background-color: orange; color:white">{{value.state | stateFormat}}</span>
                    </p>
                </div>
                {{/each}}
            </script>
        </div>
        <!-- 我的考试列表 -->
        <!-- 答题 -->
        <div class='answer-sheet' style="background-color: white">
        </div>
        <!-- 答题 -->

        <!-- 我的投票开始 -->
		<div class='vote-container'style="overflow: visible">
            <a name="vote-container" style="visibility: hidden"></a>

			<!-- 发起投票 -->

		    <!-- 发起投票 -->
            <div class="elect-list-container" style="overflow: visible">

		    </div>
		</div>
        <!-- 我的投票结束 -->

        <!-- 朋友圈开始 -->
        <div class='friend-container'>
            <div class='friend-main-container'>
                <div id="form-container" >
                    <img src="/img/tell.jpg"><button class='receive_btn'>收到的评论</button>
                    <textarea id="blog_content" class='input-newthing' rows='2' placeholder="说点什么吧"></textarea>
                    <div style="position:relative;min-height: 32px;">
                        <label id="upload-blog-img" for="image-upload" style="cursor:pointer;">
                             <img src="/img/tp_tool_img.png" alt="" title="上传图片"/>
                             <span style="line-height: 18px;height: 18px;vertical-align: top;margin-left: 3px;">上传图片</span>
                        </label>

                        <div class="size-zero">
                            <input type="file" name="image-upload" id="image-upload" accept="image/*"
                                   multiple="multiple"/>
                        </div>
                        <img src="/img/loading4.gif" id="blogpicuploadLoading"/>
                        <div id="img-container"><ul></ul></div>
                        <a class='publish-commit' onclick="submitBlog();">提交</a>
                        <select class="select-public" id="sendtype">
                            <option value="1">公开</option>
                            <option value="3">本班</option>
                            <option value="2">本年级</option>
                        </select>
                    </div>
                </div>

                <div class='order-bar'>
                    <span class='order-blue active' index="1">最新</span>
                    <span>|</span>
                    <span class='order-blue' index="2">最热</span>
                    <span id="myinfo">|</span>
                    <span class='order-blue' index="3" id="mybloginfo">我的帖子</span>
                    <select class="select-order" id="order">
                        <option value="1" selected="selected">查看全校</option>
                        <option value="2">查看本年级</option>
                        <option value="3">查看本班</option>
                    </select>
                </div>
                <!-- 用户回复栏 -->
                <div id="blog-list" style="min-height:150px;">

                </div>
                <!-- 用户回复栏 -->

            </div>
        </div>
        <!-- 朋友圈结束 -->

        <!-- 一个blog开始 -->
        <div class="one_blog_container">
            <div class='one_blog_bar'>
                <span>我的朋友圈</span>
            </div>
            <div class="one_blog">

            </div>
        </div>
        <!-- 一个blog结束 -->


        <!-- 收到的评论开始 -->
        <div class='mycomment-container'>
            <div class='mycomment_bar'>
                <span>我的评论</span>
                <button class='receive_btn'>收到的评论</button>
            </div>
            <div class='mycomment-main-container'>

            </div>
        </div>
        <!-- 收到的评论结束 -->

        <div id="discuss_list" style="min-height:150px;">
        </div>
        <div id="reply_list">

        </div>
        <div style="margin-top: 30px; margin-bottom: 30px; text-align:center;overflow: visible;">
            <div id="example"></div>
        </div>
    </div>

<%--<% if(session.getAttribute("currentUser") != null && ((UserInfo)session.getAttribute("currentUser")).getRole()!=4){  %>--%>
<div id="reply_section" style="display:none;">
    <div id="upload-photo-container">
    </div>
    <div style="height:auto;margin-bottom:0;">
        <textarea style="width:500px;height:80px;border:1px solid #d0d0d0;border-radius:4px;padding:10px;background:rgb(238,238,238);text-indent:2em;" placeholder="同学们，你需要首先在这里输入文字哦，是对你提交作业的一个描述。接下来，你还可以上传附件，拍摄照片作为作业上传呢。最后点击“提交”按钮。
    如果你需要提交“语音作业”，那么首先在文字框里面输完文字描述之后，请点击下方按钮进行录音（最长不要超过10分钟哦）。录音结束后，点击录音条右方的“上传”按钮。最后再点击文字框下方的“提交”按钮。这个语音作业就提交成功啦！"></textarea>
        <div id="recordercontainer1" style="clear:both;margin:8px;">
            <div id="recorder" class="">
                <div class="area">
                    <span class="a12" onclick="showflash('recordercontainer1')">
                        <img src="/img/mic.png" style="width: 19px; height: 19px;"/>
                        语音
                    </span>

                    <div style="padding-top: 10px;position: absolute;z-index: 50000;">
                        <div class="sanjiao" style="width: 0;height: 0;border-left: 8px solid transparent;border-right: 8px solid transparent;border-bottom: 10px solid rgb(100,100,100);position:absolute;display: inline-block;top:0px;left:10px;display:none;"> </div>
                        <div id="myContent">


                        </div>
                    </div>
                </div>
                <%--<form id="uploadForm" name="uploadForm" action="audiosave.jsp?userId=${currentUser.id}&voicetype=0">--%>
                    <%--<input name="authenticity_token" value="xxxxx" type="hidden">--%>
                    <%--<input name="upload_file[parent_id]" value="1" type="hidden">--%>
                    <%--<input name="format" value="json" type="hidden">--%>
                <%--</form>--%>
            </div>
        </div>
        <div id="attacher" style="margin: 12px 40px 0 40px; overflow:hidden;">
            <label for="file_attach" style="cursor: pointer;">
                <img src="/img/fileattach.png" />
                添加附件
            </label>
            <img src="/img/loading4.gif" id="fileuploadLoading" style="display:none;"/>
            <div style="width: 0; height: 0; overflow: visible">
                <input id="file_attach" type="file" name="file" value="添加附件" size="1" style="width: 0; height: 0; opacity: 0">
            </div>
        </div>
        <div class="take-photo">拍照</div>
        <div class="teacher-exam-submit" style="margin-left: 170px;">
            <p><a class="exam-submit" onclick="submitReply();"><img id="submitloading" src="/img/loading4.gif" style="display:none;vertical-align: middle;margin-right:5px;"/>提交</a></p>
        </div>
    </div>
</div>
<%--<% } %>--%>
    </div>
</div>
</div>
</div>
<div id="fullbg">
</div>
<%@ include file="../common_new/foot.jsp"%>
<div class="alert-bg" style="z-index: 99">  </div>
<div class="letter-container">
    <div class="letter-title">发送私信给</div>
    <div class="letter-close">&times;</div>
    <textarea class="letter-input" rows="3" placeholder="请输入您要对ta说的话..."></textarea>
    <a href="javascript:;" class="letter-release">发送</a>
</div>
<!-- 拍照 -->
<div class="take-photo-container"><div class="photo-title">拍照答题</div>
    <div class="photo-close">&times;</div>
    <div class="take-photo-form">
        <script language="JavaScript">document.write(webcam.get_html(550, 400));</script>
    </div>
    <div class="take-photo-btn">拍照</div>
    <div class="take-photo-again">重新拍摄</div>
    <div class="take-photo-upload">上传照片</div>
</div>
<div class="not-found-camera">
    <div class="not-found-close">&times;</div>
    <div class="not-found-content">抱歉，没有检测到摄像头</div>
</div>
<div class="send-result-bg"></div>
<div class="send-result">√ 发送成功</div>
<div class="info-card"></div>

<div id="bg" class="bg" style="display: none;"></div>


<script type="text/javascript" src="/static/plugins/es5-shim/es5-shim.min.js"></script>
<script type="text/javascript" src="/static/plugins/es5-shim/es5-sham.min.js"></script>
<script type="text/javascript" src="/static/plugins/moment/min/moment.min.js"></script>
<script type="text/javascript" src="/static/plugins/angular-1.2.28/angular.min.js"></script>
<script type="text/javascript" src="/static/plugins/angular-1.2.28/angular-animate.min.js"></script>
<script type="text/javascript" src="/static/plugins/ui-bootstrap-tpls-0.11.2.min.js"></script>
<script type="text/javascript" src="/static/js/filter.js"></script>
<script type="text/javascript" src="/static/js/index/app.js"></script>
<script type="text/javascript" src="/static/js/questionnaire/service.js"></script>
<script type="text/javascript" src="/static/js/questionnaire/controller.js"></script>


<script type="text/javascript">
    //根据url的参数判断如何跳转
    $(function(){
        var curtUrl = window.location.href;
        var showType = curtUrl.split('?')[1];

        if (typeof showType == 'undefined') {
            $("#weixiaoyuan").show();
            $('#version91').css('color', 'red');
        }

        //投票选举
        if(showType == 'showtype=1'){
            $('#tab_section').hide();
            $('#tab_section .my_vote').trigger('click');
        } else if(showType == 'showtype=2'){ //问卷
            $('.questionnaire-index-menu').trigger('click');
        } else if(showType == 'showtype=3'){ //培训视频
            playMovie();
        } else {
          //  $('#version9').addClass('active');
            //微校园
            if(showType!=undefined) {
                if (showType.indexOf("index=6") >= 0) {
                    jQuery("#weixiaoyuan").show();
                    jQuery("#weixiaoyuan").trigger('click');
                } else if (showType.indexOf("index=7") >= 0) {
                    jQuery("#weijiayuan").show();
                    jQuery("#weijiayuan").trigger('click');
                } else if (showType.indexOf("index=8") >= 0) {
                    jQuery("#tongzhi").show();
                    jQuery("#tongzhi").trigger('click');
                } else {
                    $(".tab_button.my_homework").show();
                    $(".tab_button.my_homework").trigger('click');
                }
            }
        }
    });
</script>
    <style type="text/css">
        #version9.active {
            background: #FF8A00 url('/img/JXHD.png') no-repeat 211px 5px;
            color: white
        }
        #version9.active + .homepage-left-two {
            display: block;
        }
    </style>
</div>
</body>
</html>