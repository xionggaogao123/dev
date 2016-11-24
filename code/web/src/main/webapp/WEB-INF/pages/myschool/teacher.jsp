<%@page import="org.apache.commons.lang.StringUtils" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
    <meta name="renderer" content="webkit">
    <title>全校管理-复兰科技 K6KT-快乐课堂</title>
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap-combined.school.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/k6kt.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/dialog.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/headmaster.css"/>
    <link rel="stylesheet" type="text/css" href="/static/plugins/uploadify/uploadify.css"/>
    <link rel="stylesheet" type="text/css" href="/static/plugins/uploadify/uploadify-userpage.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/chat.css">
    <script type="text/javascript" src='/static/js/jquery.min.js'></script>
    <script type="text/javascript" src="/static/js/bootstrap-paginator.min.js"></script>
    <script type='text/javascript' src='/static/plugins/bootstrap/js/bootstrap.min.js'></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/js/shareforflipped.js"></script>
    <script type="text/javascript" src="/static/js/swfobject.js"></script>
    <script type="text/javascript" src="/static/js/main.js"></script>
    <script type="text/javascript" src="/static/js/recorder.js"></script>
    <script type="text/javascript" src="/static/js/template.js"></script>
    <script type="text/javascript" src='/static/js/myschool/teacher.js'></script>
    <script type="text/javascript" src="/static/js/yphomeHeadmaster.js"></script>
    <script type="text/javascript" src="/static/js/card.js"></script>
    <script type="text/javascript" src="/static/plugins/uploadify/jquery.uploadify.min.js"></script>
    <script type="text/javascript" src="/static/js/chat.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/vendor/jquery.ui.widget.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.iframe-transport.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.fileupload.js"></script>
    <script type='text/javascript' src='/static/js/groupchat/strophe-custom-1.0.0.js'></script>
    <script type='text/javascript' src='/static/js/groupchat/json2.js'></script>
    <script type="text/javascript" src="/static/js/groupchat/easemob.im-1.0.0.js"></script>
    <script type="text/javascript" src="/static/js/groupchat/bootstrap.js"></script>
    <script type="text/javascript" src="/static/js/common/role.js"></script>
    <script type="text/javascript" src="/static/js/experienceScore.js"></script>


    <script>
        var userId ='${sessionValue.id}';
        var nickname = '${sessionValue.userName}';
        var imageUrl = '${sessionValue.avatar}';
    </script>
    <script type="text/html" id="viewexam">
        {{each data as value index}}
        <div class='ques-label'>
            <div class='ques-title'>题目{{index + 1}} ({{if value.type=='选择题'}}客观题{{else}}主观题{{/if}})</div>
            <div style='height:65px;overflow:hidden;'>
                <div style='width:20%;float:left;'>
                    {{if value.correctCount < value.totalDone}}
                    <button class='correct-btn' onclick="showCorrecting({{index}})">
                        批改试卷
                    </button>
                    {{else}}
                    <button class='correct-btn' style="background-color: lightskyblue"
                            onclick="showCorrecting({{index}})">查看题目
                    </button>
                    {{/if}}
                </div>
                <div style='width:20%;float:left;border-left:1px solid #555;'>
                    <div class='correct-title'>批卷进度</div>
                    <div class='correct-pro'>{{value.correctCount}}/{{value.totalDone}}</div>
                </div>
                <div style='width:19%;float:left;border-left:1px solid #555;border-right:1px solid #555;'>
                    <div class='correct-title'>正确率</div>
                    <div class='correct-pro'>{{value.accuracy}}</div>
                </div>
                <div style='width:39%;float:left;'>
                    {{if value.type=='选择题'}}
                    <table style='width:100%'>
                        <tr>
                            <td class='correct-title'>A</td>
                            <td class='correct-title'>B</td>
                            <td class='correct-title'>C</td>
                            <td class='correct-title'>D</td>
                        </tr>
                        <tr>
                            <td class='correct-table'>{{value.a}}</td>
                            <td class='correct-table'>{{value.b}}</td>
                            <td class='correct-table'>{{value.c}}</td>
                            <td class='correct-table'>{{value.d}}</td>
                        </tr>
                    </table>
                    {{/if}}
                </div>
            </div>
        </div>
        {{/each}}
    </script>
    <script>
        var conn = null;
        var currentPageID = 6;
        var nb_role = '${session.currentUser.role}'

        //easemobwebim-sdk注册回调函数列表
        $(document).ready(function () {
            conn = new Easemob.im.Connection();
            //初始化连接
            conn.init({
                //当连接成功时的回调方法
                onOpened: function () {
                    handleOpen(conn);
                },
                //当连接关闭时的回调方法
                onClosed: function () {
                    handleClosed();
                },
                //收到文本消息时的回调方法
                onTextMessage: function (message) {
                    handleTextMessage(message);
                },
                //收到表情消息时的回调方法
                onEmotionMessage: function (message) {
                    handleEmotion(message);
                },
                //收到图片消息时的回调方法
                onPictureMessage: function (message) {
                    handlePictureMessage(message);
                },
                //收到音频消息的回调方法
                onAudioMessage: function (message) {
                    handleAudioMessage(message);
                },
                onLocationMessage: function (message) {
                    handleLocationMessage(message);
                },
                //收到联系人订阅请求的回调方法
                onPresence: function (message) {
                    handlePresence(message);
                },
                //收到联系人信息的回调方法
                onRoster: function (message) {
                    handleRoster(message);
                },
                //异常时的回调方法
                onError: function (message) {
                    //handleError(message);
                }
            });
            conn.open({
                user: '${session.currentUser.id}',
                pwd: 'fulankeji',
                //连接时提供appkey
                appKey: '${easeMobAppKey}'
            });
        });

        $(function () {
            init();
            $('#username').val('${session.currentUser.userName}');

            $('#blog_content').focus(function () {
                $(this).attr('rows', '4');
            });

            $('#blog_content').blur(function () {
                var content = $(this).val();
                if (content == '') {
                    $(this).attr('rows', '2');
                }
            });


            $('#file_attachblog').fileupload({
                url: '/uploadNew.do',
                formData: {type: "blog"},
                add: fileTypeCheck,
                start: function (e) {
                    $('#fileuploadLoadingblog').show();
                },
                done: function (e, data) {
                    try {
                        var json = data.result;
                        if (json.result) {
                            $('#img-container ul').append('<li><img class="blog-img" src="' + json.path + '"><i class="fa fa-times blog-img-delete"></i></li>');
                            $('#blog_content').attr('placeholder', '分享图片');
                        }
                    } catch (err) {
                    }
                },
                fail: function (e, data) {

                },
                always: function (e, data) {
                    $('#fileuploadLoadingblog').hide();
                }
            });
        });
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


        function init() {
            var tag = jQuery("#hiddenValue").val();
            if ("1" == tag) jQuery("#banji_tag").trigger("click");
            if ("2" == tag) jQuery("#teacher_tag").trigger("click");
            if ("3" == tag) jQuery("#tongzhi_tag").trigger("click");

        }


        //根据url的参数判断如何跳转
        $(function () {

            var curtUrl = window.location.href;
            var showType = curtUrl.split('?')[1];
            //微校园
            if (typeof showType != 'undefined' && showType.indexOf("index=6") > 0) {
                jQuery("#weixiaoyuan").addClass("active");
                jQuery("#weixiaoyuan").show();
                jQuery("#weixiaoyuan").trigger('click');
            }
            if (typeof showType != 'undefined' && showType.indexOf("index=7") > 0) {
                jQuery("#weijiayuan").addClass("active");
                jQuery("#weijiayuan").show();
                jQuery("#weijiayuan").trigger('click');
            }
            var tag = getUrlParam("tag");
            if (tag == 1 || tag == 2) {
                $("#fenye").hide();
            }
        });
        function getUrlParam(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
            var r = window.location.search.substr(1).match(reg);  //匹配目标参数
            if (r != null) return unescape(r[2]);
            return null; //返回参数值
        }

    </script>
    <%--引入群组聊天模板 --%>
    <jsp:include page="/WEB-INF/pages/groupchat/chatTemplateScript.jsp"></jsp:include>

    <style type="text/css">
        .item {
            display: inline-block;
            width: 98px;
            font: 16px/38px 'microsoft yahei';
            text-align: center;
            border-right: 1px solid #DDDDDD;
            position: relative;
            float: left;
            cursor: pointer;
        }

        .item:hover {
            text-decoration: none;
            color: #FF8900;
        }

        .active {
            padding-bottom: -1px;
            color: #FF8900;
            background: white;
            /*border-top: 4px solid #FF8900;*/

        }


    </style>
</head>
<body>
<!-- 页头 -->
<%@ include file="../common_new/head.jsp" %>
<!-- 页头 -->
<form id="uploadForm" name="uploadForm" action="audiosave.jsp?userId=${sessionValue.id}&voicetype=0">
    <input name="authenticity_token" value="xxxxx" type="hidden">
    <input name="upload_file" value="1" type="hidden">
    <input name="format" value="json" type="hidden">
</form>
<input id="currentId" value="${currentUser.id}" type="hidden">
<input id="username" name="username" type="hidden">

<input type="hidden" id="hiddenValue" name="hiddenValue" value="${param.tag}"/>

<div style="background-color: #ffffff;width: 1000px;margin: 0 auto">
    <div id="content_main_container">
        <div id="content_main">
            <!-- 左侧导航-->
            <%@ include file="../common_new/col-left.jsp" %>

            <!-- left end -->
            <!-- right start-->
            <div id="right-container" style="float: none">

                <script type="text/javascript" src="/static/js/common/info-banner.js"></script>
                <script type="text/javascript" src="/static/js/common/protocol-detect.js"></script>
                <%@ include file="../common/right.jsp" %>
                <div id="info-banner-container"
                     style="position: relative;top:20px;background-color:#F1F1F1;width:770px;">
                    <div class="info-banner">
                        <div class="control-bar">

                            <span id="weixiaoyuan" class="item active" style="display:none;">微校园</span>
                            <span id="weijiayuan" class="item" style="display:none;">微家园</span>

                            <span id="banji_tag" class="item" style="display:none">班级</span>
                            <span id="teacher_tag" class="item" style="display:none">老师</span>
                            <span id="tongzhi_tag" class="item" style="display:none">通知</span>
                        </div>

                    </div>
                </div>

                <div class="main-container" style="width: 770px;position: relative;top: 30px;margin-left: 20px;">

                    <!-- 微校园开始 -->
                    <div class='friend-container'>
                        <div class='friend-main-container'
                             style="position: relative;left: -80px;top: -40px;width:770px;">
                            <div id="form-container">
                                <img src="/img/tell.jpg">
                                <button class='receive_btn'>收到的评论</button>
                                <textarea id="blog_content" class='input-newthing' rows='2'
                                          placeholder="说点什么吧"></textarea>
                                <!-- <div id="blog_content" contenteditable="true" class='input-newthing'></div> -->
                                <div style='position:relative;overflow:hidden;'>
                                    <form id="blog_pic" name="blog_pic" action="/reverse/addBlogPicture.action"
                                          method="post" enctype="multipart/form-data" style="float:left;">


                                        <label for="file_attachblog" style="cursor: pointer; font-weight: normal">
                                            <img src="/img/fileattach.png"/>
                                            上传图片
                                        </label>
                                        <img src="/img/loading4.gif" id="fileuploadLoadingblog" style="display:none;"/>

                                        <div style="width: 0; height: 0; overflow: visible">
                                            <input id="file_attachblog" type="file" name="file" value="添加附件" size="1"
                                                   style="width: 0; height: 0; opacity: 0" accept="image/*">
                                        </div>


                                    </form>
                                    <a class='publish-commit' onclick="submitBlog();">提交</a>

                                    <div style="position:absolute;right:165px;top:4px;"><input type="checkbox"
                                                                                               id="theme"
                                                                                               style="vertical-align: middle;"/><label
                                            for="theme"
                                            style="vertical-align: middle;display: inline-block;font-family: 'Tahoma';margin-left: 3px;color: #FF8800;font-weight:400;margin-bottom:0;">主题帖</label>
                                    </div>
                                    <div id="img-container">
                                        <ul></ul>
                                    </div>
                                </div>
                            </div>
                            <div class='order-bar'>
                                <span class='order-blue active' index="1">最新</span>
                                <span>|</span>
                                <span class='order-blue' index="2">最热</span>
                                <span id="myinfo">|</span>
                                <span class='order-blue' index="3" id="mybloginfo">我的帖子</span>
                                <select class="select-order" id="order" style="display:none;">
                                    <option value="1" selected="selected">查看全校</option>
                                    <option value="2">查看本年级</option>
                                    <option value="3">查看本班</option>
                                </select>
                            </div>
                            <!-- 用户回复栏 -->
                            <div id="blog-list"></div>
                            <!-- 用户回复栏 -->
                        </div>
                    </div>
                    <!-- 微校园结束 -->
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
                    <!-- 一个blog开始 -->
                    <div class="one_blog_container">
                        <div class='one_blog_bar'>
                            <span>我的微校园</span>
                        </div>
                        <div class="one_blog">
                        </div>
                    </div>
                    <!-- 一个blog结束 -->
                    <!-- 班级开始 -->
                    <div class='headmaster-class-container'>
                        <!-- 班级列表 -->
                        <div class="class-list">
                            <ul class='headmaster-select-class' style="width: 790px">
                                <li class="active">全部年级</li>
                                <c:forEach items="${gradeList}" var="grade">
                                    <li gid="${grade.gradeId}">${grade.name}</li>
                                </c:forEach>
                            </ul>
                            <div class="class-container">
                            </div>
                        </div>
                        <!-- 班级详细页 -->
                        <div class="class-titlebar">
                            <div class="class-tag">
                                <div class="first-tag">班级列表</div>
                                <div class="second-tag">三（1）班</div>
                            </div>
                            <ul class="info-select-bar">
                                <li class="active">
                                    <div class="select-detail" data-target="class-teacher-container">本班师生</div>
                                    <div class="select-img"></div>
                                </li>
                                <li>
                                    <div class="select-detail" data-target="class-course-container">本班课程</div>
                                    <div class="select-img"></div>
                                </li>
                                <li>
                                    <div class="select-detail" data-target="class-work-container">本班作业</div>
                                    <div class="select-img"></div>
                                </li>
                                <li>
                                    <div class="select-detail" data-target="class-note-container">本班通知</div>
                                    <div class="select-img"></div>
                                </li>
                                <li>
                                    <div class="select-detail" data-target="class-exam-container">本班练习</div>
                                    <div class="select-img"></div>
                                </li>
                            </ul>
                        </div>
                        <!-- 本班老师 -->
                        <div class="everything-container">
                            <div class="class-teacher-container">
                                <div class="teacher-list-title">老师列表</div>
                                <div class="border"></div>
                                <div id="teacher-list" class='teacher-list'>

                                </div>
                                <div class="teacher-list-title">学生列表</div>
                                <div class="border"></div>
                                <div class="student_header">
                                    <span class="I">学生姓名</span>|
                                    <span class="II">看完视频数</span>|
                                    <span class="III">做题数</span>
                                </div>
                                <div class="header_foot"></div>
                                <ul id="student-list" class='teacher-list'>

                                </ul>
                            </div>
                            <!-- 本班课程 -->
                            <div class="class-course-container">

                            </div>
                            <!-- 本班作业 -->
                            <div class="class-work-container">
                            </div>
                            <!-- 本班通知 -->
                            <div class="class-note-container">

                            </div>
                            <div id="reply_list"></div>
                            <!-- 本班考试 -->
                            <div class="class-exam-container">
                                <div style="position:relative;">
                                    <img src="/img/class.png">

                                    <p style="margin-bottom:5px;">
                                        <a href="javascript:;" class="exam_title">第三题</a>
                                    </p>

                                    <p style="margin-top:5px">
                                        <span style="margin-right:10px;">吴峥</span>
                                        <span>于 2014-07-09 13:39:00 发表</span>
                                    </p>
                                </div>
                            </div>

                            <!-- 查看试卷 -->
                            <div class='viewexam-container' style="background-color: white;padding: 0 40px;"></div>
                            <!-- 查看试卷 -->

                            <!-- 批改试卷 -->
                            <div class='correctexam-container' style="position: relative;">
                            </div>
                            <script type="text/html" id="correctexam">
                                <div class="myexam_bar" style="border:none;padding:20px;">
                                    <span><a onclick="$('.info-select-bar>li')[4].click()">本班练习</a> > <a
                                            onclick="showExamDetail({eid},{'paperName'})">{paperName}</a> > 题目{{index + 1}}</span>
                                </div>
                                <div class="correctexam-detail">
                                    <div class='correctexam-title'>题目{{index + 1}}</div>
                                    <div style='font-weight:bold;margin-bottom:10px;'>【题目】</div>
                                    <div style="position: relative; overflow: visible;">{{#question}}
                                    </div>
                                    {{each data as value index}}
                                    <div class='answer-sheet'>
                                        <div class='answer-name'>{{value.nickName}}</div>
                                        <div class='answer-content' style="overflow: visible">
                                            {{value.answerIndex}}<br/>
                                            {{if value.imgUrl && value.imgUrl != 'null'}}
                                            <img src="{{value.imgUrl}}"/>
                                            {{/if}}
                                        </div>
                                        <div style='width:100%;text-align:right;'>
                                            <label class='checkbox-inline answer-correct'>
                                                <input onchange="correctAnswer(this)" name="{{value.id}}" value="true"
                                                       type='radio' class='correct-answer'> 对
                                            </label>
                                            <label class='checkbox-inline answer-correct'>
                                                <input onchange="correctAnswer(this)" name="{{value.id}}" value="false"
                                                       type='radio' class='correct-answer'> 错
                                            </label>
                                        </div>
                                    </div>
                                    {{/each}}
                                    <div style="margin-top: 10px">
                                        {{if index > 0}}
                                        <a class="blue-link" style="float: left"
                                           onclick="showCorrecting({{index - 1}})">
                                            <上一题
                                        </a>
                                        {{/if}}
                                        {{if hasNext}}
                                        <a class="blue-link" style="float: right"
                                           onclick="showCorrecting({{index + 1}})">下一题></a>
                                        {{/if}}
                                    </div>
                                </div>
                            </script>
                            <!-- 批改试卷 -->

                        </div>
                    </div>
                    <!-- 班级结束 -->
                    <!-- 老师开始 -->
                    <div class="headmaster-teacher-container">
                        <div class="select-subject">
                            <ul class="headmaster-teacher-subject">
                                <li class='active' sid="0">全部学科</li>
                                <c:forEach items="${subjectList}" var="sl">
                                    <li sid="${sl.subjectId}">${sl.name}</li>
                                </c:forEach>
                            </ul>
                        </div>
                        <div class="select-grade">
                            <ul class="headmaster-teacher-grade">
                                <li class="active">全部年级</li>
                                <c:forEach items="${gradeList}" var="grade">
                                    <li cid="${grade.gradeId}">${grade.name}</li>
                                </c:forEach>
                            </ul>
                        </div>
                        <div class="headmaster-teacher-info">

                        </div>
                    </div>
                    <!-- 老师结束 -->

                    <!-- 通知开始 -->
                    <div class="headmaster-note-container">
                        <div class="release-note-container">
                            <div class="label-row">
                                <div class="col-xs-2">
                                    <span style="float:right;">标题：</span>
                                </div>
                                <input class="col-xs-10 label-note-title" type="text"/>
                            </div>
                            <div class="label-row">
                                <div class="col-xs-2">
                                    <span style="float:right;">班级：</span>
                                </div>
                                <div class="col-xs-10 note-select">

                                </div>
                            </div>
                            <div class="label-row">
                                <div class="col-xs-2">
                                    <span style="float:right;">内容：</span>
                                </div>
                                <div id="submit_section" class="col-xs-10 label-upload"
                                     style="padding:0px;position: static;">
                                    <textarea class="label-textarea" rows="4"></textarea>

                                    <div id="recordercontainer1">
                                        <div id="recorder" class="">
                                            <div class="area">
                                                <b class="a12" style="cursor: pointer"
                                                   onclick="showflash('recordercontainer1', ${currentUser.id})">
                                                    <img src="/img/mic.png" style="width: 19px; height: 19px;"/>
                                                    <span style="font-weight:100;">语音</span>
                                                </b>

                                                <div style="padding-top: 10px;position: absolute;z-index: 50000;">
                                                    <div class="sanjiao"
                                                         style="width: 0;height: 0;border-left: 8px solid transparent;border-right: 8px solid transparent;border-bottom: 10px solid rgb(100,100,100);position:absolute;display: inline-block;top:0px;left:10px;display:none;"></div>
                                                    <div id="myContent">


                                                    </div>
                                                </div>
                                            </div>
                                            <form id="uploadForm" name="uploadForm"
                                                  action="audiosave.jsp?userId=${currentUser.id}&voicetype=0&isinform=1">
                                                <input name="authenticity_token" value="xxxxx" type="hidden">
                                                <input name="upload_file[parent_id]" value="1" type="hidden">
                                                <input name="format" value="json" type="hidden">
                                            </form>
                                        </div>
                                    </div>
                                    <div id="attacher">
                                        <label for="file_attach" style="cursor: pointer; font-weight: normal">
                                            <img src="/img/fileattach.png"/>
                                            添加附件
                                        </label>
                                        <img src="/img/loading4.gif" id="fileuploadLoading" style="display:none;"/>

                                        <div style="width: 0; height: 0; overflow: visible">
                                            <input id="file_attach" type="file" name="file" value="添加附件" size="1"
                                                   style="width: 0; height: 0; opacity: 0">
                                        </div>
                                    </div>
                                    <div style="float:right">


                                        <p id="pushToCalendar"
                                           style="display: inline-block;margin-right: 22px;padding: 8px;">
                                            <input type="checkbox" id="isPushToCalendar"
                                                   style="vertical-align: middle;display:none;">
                                            <label style="vertical-align: middle;display: inline-block;font-family: 'Tahoma';margin-left: 3px;margin-bottom: 0;display:none;"></label>
                                        </p>

                                        <a class="label-note-submit" onclick="submitDiscuss();">提交</a>
                                    </div>
                                    <div id="attachment-container" style="clear:both">

                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="release-headmaster-note">

                        </div>
                    </div>
                    <!-- 通知结束 -->
                    <div style="margin-top: 30px; margin-bottom: 30px; text-align:center;" id="fenye">
                        <div id="example" style="display:none;"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 页尾 -->
    <%@ include file="../common_new/foot.jsp" %>
    <!-- 页尾 -->
    <div class="alert-bg" style="z-index: 99"></div>
    <div class="letter-container">
        <div class="letter-title">发送私信给</div>
        <div class="letter-close">&times;</div>
        <textarea class="letter-input" rows="3" placeholder="请输入您要对ta说的..."></textarea>
        <a href="javascript:;" class="letter-release">发送</a>
    </div>
    <div class="send-result-bg"></div>
    <div class="send-result">√ 发送成功</div>
    <div class="info-card"></div>

    <div id="bg" class="bg" style="display: none;"></div>


    <%
        String chat = request.getParameter("chat");
        if (StringUtils.isNotBlank(chat) && chat.equalsIgnoreCase("1")) {
    %>
    <%@ include file="/WEB-INF/pages/groupchat/chatPages.jsp" %>
    <%@ include file="/static/easymob/easymob-webim1.0/index.jsp" %>
    <%
        }

    %>
</div>
</body>
</html>