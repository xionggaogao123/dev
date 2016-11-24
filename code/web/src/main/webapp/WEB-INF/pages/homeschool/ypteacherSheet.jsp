<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@page import="com.pojo.app.SessionValue"%>
<%@page import="com.pojo.user.UserRole"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>我的首页-复兰科技 K6KT-快乐课堂</title>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <link rel="stylesheet" href="/static/css/dialog.css" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/flippedclassroom.css"/>

    <link rel="stylesheet" href="/static/css/slideshow.css"/>
    <link rel="stylesheet" href="/static/css/bootstrap-combined.school.css"/>
    <link rel="stylesheet" href="/static/css/teacherSheet.css?v=1209"/>
    <link rel="stylesheet" type="text/css" href="/static/plugins/uploadify/uploadify.css"/>
    <link rel="stylesheet" type="text/css" href="/static/plugins/uploadify/uploadify-userpage.css"/>
    <!--link rel="stylesheet" type="text/css" href="/css/my_classes/my_classes.css"-->
    <link rel="stylesheet" type="text/css" href="/static/css/my_classes/my_colleagues.css">
    <link rel="stylesheet" href="/static/css/k6kt.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/tp_vote.css">
    <link rel="stylesheet" type="text/css" href="/static/css/chat.css">
    <link rel="stylesheet" type="text/css" href="/static/plugins/fancyBox/jquery.fancybox.css?v=2.1.5" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/static/css/artDialog/ui-dialog.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/questionnaire/style.css?v=122"/>



    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js" type="text/javascript"></script>
    <%--  <script src="/js/flippedcoursemaintain.js" type="text/javascript"></script> --%>
    <script src="/static/js/shareforflipped.js" type="text/javascript"></script>
    <script type="text/javascript" src="/static/js/swfobject.js"></script>
    <script type="text/javascript" src="/static/js/recorder.js"></script>
    <script type="text/javascript" src="/static/js/main.js"></script>
    <script type="text/javascript" src="/static/js/template.js"></script>
    <script type="text/javascript" src="/static/js/myexam.js"></script>
    <script type="text/javascript" src="/static/js/card.js"></script>
    <script type="text/javascript" src="/static/js/experienceScore.js"></script>
    <script type="text/javascript" src="/static/plugins/uploadify/jquery.uploadify.min.js"></script>
    <script type="text/javascript" src="/static/js/yphomeWithDel.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/js/vote.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/vendor/jquery.ui.widget.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.iframe-transport.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.fileupload.js"></script>
    <script type="text/javascript" src="/static/js/WdatePicker.js"></script>
    <script src='http://static.polyv.net/file/polyvplayer_v2.0.min.js'></script>
    <script type="text/javascript" src="/static/plugins/fancyBox/jquery.fancybox.js?v=2.1.5"></script>
    <script type="text/javascript" src="/static/js/fzprivatemessage.js"></script>
    <script type="text/javascript" src="/static/js/common/role.js"></script>
    <script type="text/javascript" src="/static/plugins/cameraform/htdocs/webcam.js"></script>
    <script src="/static/js/artDialog/dialog-plus-min.js"></script>
    <style type="text/css">
        #tab_section > a {
            display: none;
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
        var istudent = ${roles:isStudent(sessionValue.userRole)};
        var currentPageID = 0;
        var currentPage = 1;
        var fc_current_nav = 0;
        var nb_role = '${sessionValue.userRole}';
        var nb_username = '${sessionValue.userName}';
        var nickname = '${sessionValue.userName}';
        var imageUrl = '${sessionValue.maxAvatar}';
        var userId = '${sessionValue.id}';
        var userRole = '${sessionValue.userRole}';
        $(function () {
            //根据用户名密码登录系统

            if (${roles:isHeadmaster(sessionValue.userRole) || roles:isK6ktHelper(sessionValue.userRole)}) {
                document.getElementById("headmastertheme").style.display = "";
            }
            //添加附件

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
        /* * 
         *string:原始字符串 *substr:子字符串 
         *isIgnoreCase:忽略大小写 
         */
        function contains(string,substr,isIgnoreCase) {
            if(isIgnoreCase) {
                string=string.toLowerCase();
                substr=substr.toLowerCase(); }
            var startChar=substr.substring(0,1);
            var strLen=substr.length;
            for(var j=0;j<string.length-strLen+1;j++) {
                if(string.charAt(j)==startChar)
                {
                    if(string.substring(j,j+strLen)==substr){
                        return true; }
                }
            }
            return false;
        }

        function fixOnDatePicker() {
            $(".ui_timepicker").datetimepicker({
                //showOn: "button",
                //buttonImage: "./css/images/icon_calendar.gif",
                //buttonImageOnly: true,
                showSecond: false,
                timeFormat: 'HH:mm',
                onSelect: function () {
                }
            });
        }

        function delCourse(id) {
            if (confirm('课程将被删除，是否确定？')) {
                $.ajax({
                    url: "/reverse/deleteCourse.action",
                    type: "get",
                    dataType: "json",
                    async: false,
                    data: {
                        courseId: id
                    },
                    success: function (data) {
                        ret = data;
                        if (data.status = "ok") {
//    	      				 var cid= $('.control-bar .item.active').attr('cid');
                            var cid = $('#fc_my_classes .my_class_item.active').attr('cid');
//    	      				 var classtype= $('.control-bar .item.active').attr('classtype');
                            var classtype = $('#fc_my_classes .my_class_item.active').attr('classtype');
//    	      				 getLastCourses(true,cid,classtype);
                        }
                    }
                });
            }
        }



        /* 提交讨论列表  **************************************/
        function submitDiscuss() {
            var name = $("#submit_section >div:nth(0) input").val();
            var content = $("#submit_section >div:nth(3) textarea").val().replace(/\n/g, '<br>');
            var isOnline = $('.tiji-content.active').attr('isOnline') || null;

            var flist = $.map($('.uploadedFiles'), function (file) {
                return {
                    realName: $(file).attr('realname'),
                    localName: $(file).text()
                };
            });

            if ($.trim(name) == '') {
                alert('标题不可为空！');
                return false;
            }
            else {
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
            $("#submit_section >div:nth(1) input").each(function () {
                if ($(this).prop('checked')) {
                    bSClass = true;
                    cidlist += $(this).closest('li').attr('classtype') + ',' + $(this).closest('li').attr('cid') + ',' + $(this).closest('li').attr('ctype') + ';';
                }
            });
            if (!bSClass) {
                alert('请选择班级！');
                return false;
            }

            $('#submitloading').show();





            $.ajax({
                url: "/homework/add.do",
                type: "post",
                dataType: "json",
                async: true,
                data: {
                    'title': name,
                    'content': content,
                    'classIdList': cidlist,
                    'filenamelist': JSON.stringify(flist),
                    'onsubmittype': isOnline,
                    'voicefile': $('.voice').attr('url')
                },
                success: function (data) {
                    //ret = data;
                    /*if (data.score) {
                        scoreManager(data.scoreMsg, data.score);
                    }*/
                    if(data.code=="500")
                    {
                        alert("该班级未添加任课老师，请添加任课老师后发布作业");
                        return;
                    }
                    getDiscusses($('#fc_my_classes .my_class_item.active').attr('cid'), $('#fc_my_classes .my_class_item.active').attr('classtype'), $('#fc_my_classes .my_class_item.active').attr('ctype'), 1, 8);

                },
                complete: function () {
                    $('#submitloading').hide();
                    resetSubmitForm();

                    //var explorer = window.navigator.userAgent ;
                    if (!!window.ActiveXObject || "ActiveXObject" in window){//判断ie
                        window.location.reload();
                    }

                }
            });
        }

        /* 获取讨论列表  ********************************************/
        function getDiscusses(cid, classtype, type, page, size) {
            var ret = '';
            $('#discuss_list').html('');
            $('#discuss_list').addClass('hardloadingb');
            /*if($('.chosen').attr('index')==1) {
             $('#discuss_list').append('<div class="discuss-info" style="cursor:pointer;"><img class="discuss-info-img" src="/img/logo_b.png"/><p style="margin-bottom:5px;word-break: break-all;" onclick="getSystemMsgDetail();""><a style="margin-right:10px;">[通知]</a>亲，你的用户手册来啦！</p><p style="margin-top:5px"><span style="margin-right:10px;">复兰科技</span><span>于2014-8-28 09:00:00发表</span><span style="margin-left:10px;"></span></p><a class="deldiscuss"><img src="img/dustbin.png" title="删除" style="display:none;"/></a><a class="topdiscuss"><img cantop="0" istop="1" src="img/set_top.png" title="置顶" style="display:block;"/></a></div>');
             }*/
            $.ajax({
                url: "/homework/teacher/list.do",
                type: "get",
                dataType: "json",
                async: true,
                data: {
                    'classId': cid,
                    'classtype': classtype,
                    'type' : type,
                    'mesgtype': $('.chosen').attr('index'),
                    'page': page,
                    'pageSize': size
                },
                success: function (data) {
                    ret = data;
                    showDiscusses(data, page);
                    var total = data.rows.length > 0 ? data.total : 0;
                    var to = Math.ceil(total / size);
                    totalPages = to == 0 ? 1 : to;
                    if (page == 1) {
                        resetPaginator(totalPages);
                    } else {
                        resetPaginatorCurrentPage(page, totalPages);
                    }
                },
                complete: function () {
                    $('#discuss_list').removeClass('hardloadingb');
                    if (!ret.rows || ret.rows.length == 0) {
                        $('#example').hide();
                    }
                    else {
                        $('#example').show();
                    }
                    $('#submit_section').show();
                    $("#reply_list").html('');
                    $("#reply_list").hide();
                }
            });
        }

        function showDiscusses(data, cpage) {
            var target = $('#discuss_list');
            var html = '';
            if (data.rows) {
                var txt = $(".chosen").attr('index');
                txt = txt == 1 ? '通知' : '作业';
                for (var i in data.rows) {
                    var ds = data.rows[i];
                    var istop = ds.istop == 1 ? '' : 'none';
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
                    if (ds.docFile) {
                        attach += '<img src="/img/fileattach.png" style="margin-left:10px;"/>';
                    }
                    html += '<div class="discuss-info" style="cursor:pointer;" msgid="' + ds.id + '" ><img class="discuss-info-img" src="' + ds.userAvatar + '" target-id="' + ds.userid + '" role="' + ds.role + '"/>';
                    html += '<p style="margin-bottom:5px;font-weight:' + isbold + ';word-break: break-all;" onclick="getDiscussDetail(\'' + ds.id + '\',' + cpage + ');"><a style="margin-right:10px;">[' + txt + ']</a>';
                    html += ds.title + attach;
                    html += '<span class="tiji-online">在线提交</span>';

                    html += '</p><p style="margin-top:5px"><span style="margin-right:10px;">' + ds.userName + '</span>';
                    html += '<span>于' + ds.time + '发表</span>' + lastreply + '<span style="margin-left:10px;">' + viewtxt + '</span></p><a class="deldiscuss"><img src="img/dustbin.png" title="删除" style="display:none;"/></a>';
                    html += '<a class="topdiscuss"><img cantop="' + ds.ismytop + '" istop="' + ds.istop + '" src="img/set_top.png" title="置顶一周" style="display:' + istop + ';"/></a></div>';
                }
            }
            target.append(html);

            //if ((nb_role) || ('${sessionValue.userRole}' == 6 )) {
            $('#discuss_list >div').hover(function () {
                var topOb = $(this).find('.topdiscuss img');
                //if (topOb.attr('cantop') == 1) {
                //$(this).find('.topdiscuss img').show();
                $(this).find('.deldiscuss img').show();
                //}
            }, function () {
//                    var topOb = $(this).find('.topdiscuss img');
//                    if (topOb.attr('istop') == 1) {
//                        topOb.show();
//                    }
//                    else {
//                        topOb.hide();
//                    }
                $(this).find('.deldiscuss img').hide();
            });

            $('.deldiscuss').bind('click', function (e) {
                e.stopPropagation();
                deldiscuss($(this).closest('div').attr('msgid'));
            });
            $('.topdiscuss').bind('click', function (e) {
                e.stopPropagation();
                topdiscuss($(this).closest('div').attr('msgid'));
            });
        }
        //}

        // 获取系统通知
        function getSystemMsgDetail() {
            $('#discuss_list').html('');
            $('#submit_section').hide();
            var attach = '';
//      var html = '<div style="cursor:pointer;" onclick="getDiscusses('+ $('.control-bar .item.active').attr('cid') +',1,1,8);"><a>返回></a></div><div class="discuss-info"><img class="discuss-info-img" src="/img/logo_b.png"/><p style="margin-bottom:5px;word-break: break-all;"><a style="margin-right:10px;">[通知]</a>亲，你的用户手册来啦！</p><p style="margin-top:5px"><span style="margin-right:10px;">复兰科技</span><span>于2014-08-28 09:00:00发表</span></p></div><div style="border-bottom:none;"><p style="margin-bottom:20px;text-indent: 2em;word-break: break-all;">校领导、老师、学生、家长均可点击附件下载对应版本的“K6KT-快乐课堂”用户手册，获得更多操作帮助！</p>';
            var html = '<div style="cursor:pointer;" onclick="getDiscusses(' + $('#fc_my_classes .my_class_item.active').attr('cid') + ',1,'+ $('#fc_my_classes .my_class_item.active').attr('ctype') + ',1,8);"><a>返回></a></div><div class="discuss-info"><img class="discuss-info-img" src="/img/logo_b.png"/><p style="margin-bottom:5px;word-break: break-all;"><a style="margin-right:10px;">[通知]</a>亲，你的用户手册来啦！</p><p style="margin-top:5px"><span style="margin-right:10px;">复兰科技</span><span>于2014-08-28 09:00:00发表</span></p></div><div style="border-bottom:none;"><p style="margin-bottom:20px;text-indent: 2em;word-break: break-all;">校领导、老师、学生、家长均可点击附件下载对应版本的“K6KT-快乐课堂”用户手册，获得更多操作帮助！</p>';
            attach += '<p style="margin-left:30px;margin-bottom:10px;"><a href="/reverse/FileDownload?fileName=/upload/resources/用户手册-家长.pdf"><img src="/img/fileattach.png"/>附件1:用户手册-家长.pdf</a></p>';
            attach += '<p style="margin-left:30px;margin-bottom:10px;"><a href="/reverse/FileDownload?fileName=/upload/resources/用户手册-学生.pdf"><img src="/img/fileattach.png"/>附件2:用户手册-学生.pdf</a></p>';
            attach += '<p style="margin-left:30px;margin-bottom:10px;"><a href="/reverse/FileDownload?fileName=/upload/resources/用户手册-老师.pdf"><img src="/img/fileattach.png"/>附件3:用户手册-老师.pdf</a></p>';
            attach += '<p style="margin-left:30px;margin-bottom:10px;"><a href="/reverse/FileDownload?fileName=/upload/resources/用户手册-校领导.pdf"><img src="/img/fileattach.png"/>附件4:用户手册-校领导.pdf</a></p></div>';
            $('#discuss_list').html(html + attach);
            $('#example').hide();
            $('#discuss_list >div:nth(2)').css('border-bottom', '');
            //getNotifyNum($('#fc_my_classes .my_class_item.active').attr('cid'), $('#fc_my_classes .my_class_item.active').attr('classtype'), $('#fc_my_classes .my_class_item.active').attr('ctype'));
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
                success: function (data) {
                    ret = data;
                    showDiscussDetail(data, cpage);
                },
                complete: function () {
                    $('#discuss_list').removeClass('hardloadingb');
                    $('#submit_section').hide();
                    if ($(".chosen").length > 0 && $(".chosen").attr('index') == 2) {
                        $('#example').show();
                        getReplyList(mid, 1, 8);
                    }
                    else {
                        $('#example').hide();
                        $('#discuss_list >div:nth(2)').css('border-bottom', '');
                    }
                    //getNotifyNum($('#fc_my_classes .my_class_item.active').attr('cid'), $('#fc_my_classes .my_class_item.active').attr('classtype'), $('#fc_my_classes .my_class_item.active').attr('ctype'));
                }
            });
        }

        function showDiscussDetail(data, cpage) {
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
                if (txt != 1) {
                    //if ((ds.onlinesubmittype == 1) && (ds.mesgtype == 2)) {
                    html += '<span class="tiji-online">在线提交</span>';
                    //} else if ((ds.onlinesubmittype == 0) && (ds.mesgtype == 2)) {
                    //    html += '<span class="tiji-offline">课堂提交</span>';
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
                    'pageSize': size
                },
                success: function (data) {
                    ret = data;
                    showReplyList(data);
                    var total = data.rows.length > 0 ? data.total : 0;
                    var to = Math.ceil(total / size);
                    totalPages = to == 0 ? 1 : to;
                    if (page == 1) {
                        resetPaginatorRe(totalPages);
                    }
                    if (data.rows.length > 0) {
                        $('#example').show();
                        $('#discuss_list >div:nth(2)').css('border-bottom', 'none');
                    }
                    else {
                        $("#reply_list").hide();
                        $('#example').hide();
                        $('#discuss_list >div:nth(2)').css('border-bottom', '');
                    }
                },
                complete: function () {
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
                    html += '<div style="position:relative;"><div class="reply_detail"><div style="margin-right: 15px;"><img src="' + hf.avatar + '" style="width:45px;height:45px;"/></div>';
                    html += '<div><p><span style="font-size: 16px;color:#696969;margin-right:20px;">' + hf.userName + '</span><span style="color:#9a9a9a;">发表于' + timeStr + '</span></p>';
                    html += '<p style="word-break:break-all;">' + hf.content + '</p>';
                    if (hf.voiceFile&& hf.voiceFile.length>0) {
                        html += '<p style="margin-left:30px;margin-bottom:10px;"><a onclick="playVoice(\'' + hf.voiceFile[0].value + '\');" url="' + hf.voiceFile[0].value  + '"><img src="/img/yuyin.png">播放</a></p>';
                    }
                    for (var j in hf.docFile) {
                        var file = hf.docFile[j];
                        if (checkImage(file.value)) {
                            pichtml += '<img class="homework-img teacher-check" data-id="' + file.idStr + '" src=' + encodeURI(file.value) + '>';
                        } else {
                            atthtml += '<p><a href="/commondownload/file.do?url=' + encodeURI(file.value) + '&name='+file.name+'"><img src="/img/fileattach.png" />附件' + (parseFloat(j) + 1) + '：' + file.name + '</a></p>';
                        }
                    }
                    html += '<div class="contentImg-container">' + pichtml + '</div>' + atthtml;
                    html += '</div></div></div>';
                }
            }
            target.html(html);
        }

        function checkImage(str) {
            var ext = str.substring(str.length - 3, str.length);
            var rule = /(jpg|gif|bmp|png)/;
            if (rule.test(ext)) {
                return true;
            } else {
                return false;
            }
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
                            return "末页" + page;
                        case "page":
                            return page;
                    }
                },
                onPageClicked: function (e, originalEvent, type, page) {
                    currentPage = page;
                    if ($(".chosen").attr('index') == 3) {
                        getTeacherExamPaper(page, 8);
                    } else {
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
                            return "末页" + page;
                        case "page":
                            return page;
                    }
                },
                onPageClicked: function (e, originalEvent, type, page) {
                    currentPage = page;
                    getReplyList($("#discuss_list >div:nth(1)").attr('msgid'), page, 8);
                }
            });
        }

        function resetPaginatorCurrentPage(page, totalPages) {
            $('#example').bootstrapPaginator("setOptions", {
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
                            return "末页" + page;
                        case "page":
                            return page;
                    }
                },
                onPageClicked: function (e, originalEvent, type, page) {
                    currentPage = page;
                    if ($(".chosen").attr('index') == 3) {
                        getTeacherExamPaper(page, 8);
                    } else {
                        getDiscusses($('#fc_my_classes .my_class_item.active').attr('cid'), $('#fc_my_classes .my_class_item.active').attr('classtype'),$('#fc_my_classes .my_class_item.active').attr('ctype'), page, 8);
                    }
                }
            });
        }

        function resetSubmitForm() {
            $('#submit_section input[type=checkbox]').attr('checked', false);
            $('#submit_section input[type=text]').val('');
            $('#submit_section textarea').val('');
            $('.uploadedFiles').remove();
            $('.play_button').hide();
            if (FWRecorder.recorder) {
                FWRecorder.defaultSize();
                $("#save_button object").css('opacity', 0);
            }
            $('p a.voice').remove();
        }


        function deldiscuss(id) {
            if (confirm('确定删除这条记录？')) {
                $.ajax({
                    url: "/homework/remove.do",
                    type: "post",
                    dataType: "json",
                    async: true,
                    data: {
                        hwid: id
                    },
                    success: function (data) {
                        ret = data;
                        if (data.score) {
                            scoreManager(data.scoreMsg, data.score);
                        }
                        getDiscusses($('#fc_my_classes .my_class_item.active').attr('cid'), $('#fc_my_classes .my_class_item.active').attr('classtype'),$('#fc_my_classes .my_class_item.active').attr('ctype'), 1, 8);
                    },
                    complete: function () {
                    }
                });
            }
        }

        function topdiscuss(id) {
            $.ajax({
                url: "/reverse/mesgTopInfo.action",
                type: "post",
                dataType: "json",
                async: true,
                data: {
                    'messageId': id
                },
                success: function (data) {
                    if (data.resultCode == 0) {
                        ret = data;
                        getDiscusses($('#fc_my_classes .my_class_item.active').attr('cid'), $('#fc_my_classes .my_class_item.active').attr('classtype'),$('#fc_my_classes .my_class_item.active').attr('ctype'), 1, 8);
                    }
                },
                complete: function () {
                }
            });
        }

        function PlayedMusic(ob, rid) {
            var player = '<embed src="/upload/voice/' + $(ob).attr('url') + '" belong="' + rid + '" width="0" height="0" autostart="true" />';
            $('embed[belong=' + rid + ']').remove();
            $('body').append(player);
        }

        $(function () {
            // getTeacherInfo();
            $("#submit_section").hide();
            $(".vote-container").hide();
            $(".friend-container").show();
            $("#reply_list").hide();

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
                        $("#submit_section").append(uf);
                    } catch (err) {
                    }
                },
                fail: function (e, data) {

                },
                always: function (e, data) {
                    $('#fileuploadLoading').hide();
                }
            });


            /*  默认班级判断   *****************************************/
            /*在页面被载入时为左边班级列表选定默认班级,并且为每个班级绑定点击事件*/
            /*得到每个课程*/
            /* 作业通知切换 */
            $(".tab_button").each(function (i) {
                if (i == 0) {
                    $(this).addClass('chosen');
                }
                else {
                    $(this).addClass('notchosen');
                }
                $(this).bind('click', function () {
                    if (!$(this).hasClass('chosen')) {
                        $(this).addClass('chosen').removeClass('notchosen');
                        $(this).find('span').removeClass('notity_b');
                    }
                    $(this).siblings().removeClass('chosen').addClass('notchosen');
                    $(this).siblings().find('span').addClass('notity_b');
                    resetSubmitForm();
                    //index = i;
                    $('#submit_section').find('textarea').attr('placeholder', '');

                    var index = $(this).attr('index');
                    $('.vote-container').hide();
                    if (index == 3) {
                        $('#recorder').show();
                        $('#onlineSub').show();
                        $("#save_button").show();
                        $('#attacher').css('left', "170px");
                        $('#submit_section').find('textarea').attr('placeholder', '老师您好，您可以发布文字作业，附件作业。也可以要求学生们提交文字作业，附件作业，图片作业（您收到作业后可以直接在线涂鸦批改）。如果您想布置语音作业，那么首先在文字框里输完文字描述之后，请点击下方按钮进行录音（最长不要超过10分钟哦）。录音结束后，点击录音条右方的“上传”按钮。最后再点击文字框下方的”发表“按钮，这个作业就布置完成了！');
                    }
                    else {
                        $('#recorder').hide();
                        $('#onlineSub').hide();
                        $("#save_button").hide();
                        $('#attacher').css('left', "50px");
                    }
                    if (index == 2 || index == 1) {
                        $('#recorder').show();
                        //校长发布通知
                        if(index == 1 && (${roles:isHeadmaster(sessionValue.userRole) || roles:isK6ktHelper(sessionValue.userRole)})){
                            $('#pushToCalendar').css('visibility','visible');
                        }else{
                            $('#pushToCalendar').css('visibility','hidden');
                        }
                    }
                    var cid = $('#fc_my_classes .my_class_item.active').attr('cid');
                    var classtype = $('#fc_my_classes .my_class_item.active').attr('classtype');
                    var coursetype = $('#fc_my_classes .my_class_item.active').attr('ctype');
                    //getNotifyNum(cid, classtype,coursetype);
                    $('.btn-group').hide();

                    if (index == 3) {//i=3-->4
                        $('.mycomment-container').hide();
                        $('.friend-container').hide();
                        $('#submit_section').hide();
                        $('.newexam_container').show();
                        $('#discuss_list').hide();
                        $('.exam_list').show();
                        $('.myexam_bar>span').text("上传考卷");
                        $('.newexam_form').hide();
                        $('.viewexam-container').hide();
                        $('.correctexam-container').hide();
                        $('.one_blog_container').hide();
                        getTeacherExamPaper(1, 8);
                        $('#reply_list').hide();
                        $('#reply_section').hide();
                        $('#example').show();
                    } else if (index == 0) {//i=0
                        resetSubmitForm();
                        $('#reply_list').show();
                        $('.mycomment-container').hide();
                        $('.friend-container').show();
                        $('#submit_section').hide();
                        $('#discuss_list').hide();
                        $('.newexam_container').hide();
                        $('.order-bar .active').click();
                        $('.one_blog_container').hide();
                        $('#reply_list').hide();
                        if (${roles:isStudentOrParent(sessionValue.userRole)}) {//学生
                            $('#myinfo').addClass("myblog");
                            $('#mybloginfo').addClass("myblog");
                            $('#form-container').hide();
                        } else {
                            $('#myinfo').removeClass("myblog");
                            $('#mybloginfo').removeClass("myblog");
                            $('#form-container').show();
                        }
                    } else if (index == 4) {//i=1
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
                        if(${roles:isTeacher(sessionValue.userRole)} && !contains('${sessionValue.userName}','K6KT小助手',true)){//老师
                            $('#myinfo').addClass("myblog");
                            $('#mybloginfo').addClass("myblog");
                            $('#form-container').hide();
                        } else {
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
                        $('.newexam_container').hide();
                        $('#reply_section').hide();
                        $('.one_blog_container').hide();
                        $('#reply_list').hide();
                        if(${roles:isTeacher(sessionValue.userRole)} && !contains('${sessionValue.userName}','K6KT小助手',true)){//老师
                            $('#myinfo').addClass("myblog");
                            $('#mybloginfo').addClass("myblog");
                            $('#form-container').hide();
                        }else{
                            $('#myinfo').removeClass("myblog");
                            $('#mybloginfo').removeClass("myblog");
                            $('#form-container').show();
                        }
                    } else {//i=3 or i=2
                        getDiscusses(cid, classtype,coursetype, 1, 8);
                        $('#submit_section').show();
                        $('.newexam_container').hide();
                        $('#discuss_list_container').show();
                        $('#discuss_list').show();
                        $('.friend-container').hide();
                        $('.mycomment-container').hide();
                        $('.one_blog_container').hide();
                        $('#reply_list').hide();
                    }
                });
            });


            if ($('#fc_my_classes .my_class_item:nth(0)').length > 0) {
                var cid = $('#fc_my_classes .my_class_item:nth(0)').attr('cid');
                var classtype = $('#fc_my_classes .my_class_item:nth(0)').attr('classtype');
                var coursetype = $('#fc_my_classes .my_class_item:nth(0)').attr('ctype');
                currentClassId = cid;
                /*  默认班级的最近课程获取 **************************/
//    	        getLastCourses(true,cid,classtype);
                //TODO:先注掉
                getStudentRank(true, cid, classtype);
                //getNotifyNum(cid, classtype,coursetype);

                /** start  */
                if ($('#fc_my_classes .my_class_item').length < 5) {
                    $("#move_right").remove();
                    $("#move_left").remove();
                }

                //$('.control-bar >div').width($('.control-bar span').length*123);
                $('#fc_my_classes').find('div.my_class_item:nth(0)').addClass('active');
                $("#fc_my_classes .my_class_item").each(function (i) {
                    $(this).bind('click', function () {
                        $(this).siblings().removeClass('active');
                        $(this).addClass('active');
                        var cid = $(this).attr('cid');
                        var classtype = $(this).attr('classtype');
                        resetSubmitForm();
                        getStudentRank(true, cid, classtype);
//	   	        	    getLastCourses(true,cid,classtype);
                        getDiscusses($('#fc_my_classes .my_class_item.active').attr('cid'), $('#fc_my_classes .my_class_item.active').attr('classtype'),$('#fc_my_classes .my_class_item.active').attr('ctype'), 1, 8);
                        //getNotifyNum(cid, classtype,coursetype);
                        $(".my_notice").click();
                        currentClassId = cid;
                    });
                });

                // getDiscusses($('.control-bar .item.active').attr('cid'),1,8);
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
                                return "末页" + page;
                            case "page":
                                return page;
                        }
                    },
                    onPageClicked: function (e, originalEvent, type, page) {
                    }
                });
            }




            // 提交作业选择
            $('.tiji-content').on('click', function () {
                $(this).parent().find('.active').removeClass('active');
                $(this).addClass('active');
            });

            $('body').on('click', '.homework-img.teacher-check', function () {
                var pUrl = $(this).attr('src');
                var imageId = $(this).attr('data-id');
                checkPrintPos();
                $('#check-hw-container').show();
                $('.alert-bg').show();
                var picUrl = pUrl;
                var so = new FlashObject("/static/plugins/pictureEditor/pictureEditor.swf", "flashApp", "1000px", "700px", "8");
                so.addVariable("picUrl", picUrl);
                so.addVariable("uploadUrl", "/homework/saveeditedimage.do?imageId=" + imageId);
                so.addParam("wmode", "transparent");
                so.write("check-hw-container");
                $('#check-hw-container').append('<div class="close-check-hw"></div>');
            });

            $('body').on('click', '.close-check-hw', function () {
                $('#check-hw-container').empty().hide();
                $('.alert-bg').hide();
                $('#fullbg').hide();
            });

            window.onresize = function () {
                checkPrintPos();
            }

            function checkPrintPos() {
                var whigh = document.documentElement.clientHeight;
                var wwidth = document.documentElement.clientWidth;
                if (wwidth > 900) {
                    $('#check-hw-container').css({
                        'top': (whigh - 700) / 2,
                        'left': (wwidth - 1000) / 2
                    });
                }
            }


        });
        //图片上传完成回调函数
        function uploadComplete() {
            $('#check-hw-container').empty().hide();
            $('.alert-bg').hide();
            $('#fullbg').hide();
            getReplyList($("#discuss_list >div:nth(1)").attr('msgid'), 1, 8);
        }
    </script>

    <script type="text/javascript">


        var foo = true;
        function showflash(container) {
            var mc = new FlashObject("/static/plugins/audiorecorder.swf", "recorderApp", "350px", "23px", "8");
            mc.setAttribute("id", "recorderApp");
            mc.setAttribute("name", "recorderApp");

            mc.addVariable("uploadAction", "/homework/uploadvoice.do");
            mc.addVariable("fileName", "audio");
            mc.addVariable("recordTime", 10 * 60 * 1000);
            mc.addVariable("appName", "recorderApp");
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
        function loadRecorder(ob) {
            $('.recorder').attr('id', '');
            $(ob).next().attr('id', 'recorder').show();
            $('#recorder record_button').click();
        }
    </script>

    <%--引入投票选举的模板--%>
    <jsp:include page="../elect/templateScripts.jsp"></jsp:include>
</head>

<body style="font-family:Microsoft YaHei;" ng-app="index" ng-controller="IndexCtrl" ng-click="clickBody()">

<%@ include file="../common_new/head.jsp" %>
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
<jsp:include page="../common/infoBanner.jsp">
    <jsp:param name="bannerType" value="homePage"/>
</jsp:include>

<input id="currentId" type="hidden" value="${sessionValue.id}">
<div style="background-color: #ffffff;width:100%;position: relative;">
    <div id="content_main_container" style="width: 1200px;overflow:visible;">
        <div id="content_main">
            <%@ include file="../common_new/col-left.jsp" %>
            <div id="content_left" style="display: none">
                <div>
                    <div class="questionnaire-index-menu" ng-click="queryQuestionnaire()"
                         onclick="showContent('questionnaire')">问卷调查<span ng-bind="unread"
                                                                          ng-show="unread>0"
                                                                          class="sparkle-unread"></span>
                    </div>
                </div>
                <c:if test="${roles:isTeacher(sessionValue.userRole)}">
                    <div id="fc_my_colleagues" style="margin-bottom: 20px">
                        <a class="my_colleagues_a" href="/myColleagues">
                            <div class="my_colleagues">我的同事</div>
                        </a>
                    </div>
                </c:if>


                <div id="fc_stu_rank_con">
                    <h3 style="margin:0;color:white;background:rgb(61,135,198);padding:15px;">同学排行</h3>

                    <div id="fc_stu_rank"></div>
                    <%--   <div style="border-bottom:1px solid #D0D0D0;">
                       <p><img /><span style="margin-right:40px;margin-left:15px;">徐佳莹</span><span style="margin-right:10px;">经验值</span><span class="exprience">99</span></p>
                      </div> --%>
                </div>
            </div>

            <div id="content_right" style="overflow:visible;">
                <jsp:include page="../common/right.jsp"></jsp:include>
                <div id="questionnaire_section" class="hidden"style="min-height: 1200px;background: #F8F8F8;overflow: visible;width: 1000px;"   >
                    <jsp:include page="../questionnaire/list.jsp"></jsp:include>
                </div>
                <div id="discuss_section" style="margin-top: 10px;">
                    <div id="tab_section" >
                        <a id="weixiaoyuan" index="0" class="tab_button" style="font-weight: bold;z-index: 1">微校园</a>
                        <a id="weijiayuan" index="4" class="tab_button" style="font-weight: bold;z-index: 1">微家园</a>
                        <a id="tongzhi" index="1" class="tab_button" style="font-weight: bold;z-index: 1">通知</a>
                        <a index="2" class="tab_button my_homework" style="display:none">作业</a>
                        <%--<a index="3" class="tab_button my_exam">上传考卷</a>--%>
                        <a  index="5" class="tab_button my_vote" style="display:none">投票选举</a>
                        <%--<a href="/business/reverse/user/userHelp.jsp#teacherScore" class="jinfen-container" style="border-right: none;float: left;line-height: 38px;width: 770px;margin-top: -40px"> <p class="jifen">怎样获得经验值</p>&lt;%&ndash;img src="/img/wenhao.gif" alt="" class="wenhao">&ndash;%&gt;<i class="fa fa-question" style="color: #FDA616;font-size: 20px;position: relative;right: -645px;top: 8px"></i></a>--%>
                    </div>

                    <%
                        String index= request.getParameter("hw");
                        if(StringUtils.isNotBlank(index)) {
                    %>
                    <div id="fc_my_classes" style="min-height: 50px;width: 768px;border: 1px solid #D0D0D0;border-top:none;background-color: #ffffff;line-height: 50px;font-size: 16px;overflow: visible">
                        <c:forEach items="${classList}" var="classInfo">
                            <div class="ypstudent my_class_item" cid="${classInfo.id}" classtype="${classInfo.classType}" coursetype="${classInfo.coursetype}" ctype="1">${classInfo.className}</div>
                        </c:forEach>
                    </div>
                    <%
                    } else {
                    %>
                    <div id="fc_my_classes" style="display:none">
                        <c:forEach items="${classList}" var="classInfo">
                            <div class="ypstudent my_class_item" cid="${classInfo.id}" classtype="${classInfo.classType}">${classInfo.className}</div>
                        </c:forEach>
                    </div>
                    <%}%>

                    <div id="discuss_main" style="clear: both;overflow: visible">
                        <!--  <h3 style="color:#565a5d;">全部通知</h3> -->
                        <% SessionValue sessionValues = (SessionValue) request.getAttribute("sessionValue");
                            if (sessionValues != null) { %>
                        <div id="submit_section">
                            <div><span>标题：</span><input type="text"
                                                        style="width:300px;border:1px solid #d0d0d0;border-radius:4px;padding:5px 0;"/>
                            </div>
                            <div><span>班级：</span>
                                <ul style="margin:0;width:300px;">
                                    <c:forEach items="${classList}" var="cl">

                                        <li class="class-list-checkbox" cid="${cl.id}"
                                            classtype="${cl.classType}" coursetype="${cl.coursetype}" ctype="1"><input type="checkbox"/><span
                                                style="margin-left:5px;vertical-align:top;">${cl.className}</span></li>


                                    </c:forEach>
                                </ul>
                            </div>
                            <div id="onlineSub">
                                <span style="line-height:28px;">类型：</span>
                                <span class="tiji-content active" isOnline="1">在线提交</span>
                                <span class="tiji-content" isOnline="0">课堂提交</span>
                            </div>
                            <div style="height:auto;margin-bottom:0;"><span>内容：</span>
                            <textarea
                                    style="width:410px;height:100px;border:1px solid #d0d0d0;border-radius:4px;text-indent:2em;line-height:18px;"></textarea>

                                <div id="recordercontainer1" style="clear:both;margin-top:8px;margin-left:55px;">
                                    <div id="recorder" class="">
                                        <div class="area">
                                        <span class="a12" onclick="showflash('recordercontainer1')">
                                            <img src="/img/mic.png" style="width: 19px; height: 19px;"/>
                                            语音
                                        </span>

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
                                <div id="attacher" style="overflow:hidden;margin-top:12px;margin-left:20px;">
                                    <label for="file_attach" style="cursor: pointer">
                                        <img src="/img/fileattach.png" />
                                        添加附件
                                    </label>
                                    <img src="/img/loading4.gif" id="fileuploadLoading" style="display:none;"/>
                                    <div style="width: 0; height: 0; overflow: visible">
                                        <input id="file_attach" type="file" name="file" value="添加附件" size="1" style="width: 0; height: 0; opacity: 0">
                                    </div>
                                </div>
                                <div class="teacher-exam-submit" style="overflow: hidden">
                                    <p id="pushToCalendar" style="display: none;margin-right: 22px;height:0px;">
                                        <input type="checkbox" id="isPushToCalendar" style="vertical-align: middle;height: 0px"><label style="vertical-align: middle;">推送到日历</label>
                                    </p>
                                    <p style="display: inline-block;padding-left: 70px"><a class="exam-submit" onclick="submitDiscuss();">
                                        <img id="submitloading" src="/img/loading4.gif" style="display:none;vertical-align: middle;margin-right:5px;"/>发布</a>
                                    </p>
                                </div>
                            </div>
                        </div>
                        <% } %>


                        <!-- 我的投票开始 -->
                        <div class='vote-container'style="overflow: visible">
                            <a name="vote-container" style="visibility: hidden"></a>
                            <!-- 发起投票 -->
                            <div class="tp-con tp-publish" style="width: 770px;">
                                <form action="">
                                    <div class="clearfix tp-from-group">
                                        <label class="tp-control-label tpz tp-h-center" for="inputEmail3">标题</label>
                                        <div class="tp-col tpz"><input id="inputEmail3" class="tp-form-control" type="text" placeholder="请输入投票标题，不得超过20个字"></div>
                                    </div>
                                    <div class="clearfix tp-from-group">
                                        <label class="tp-control-label tpz" for="inputPassword3">投票规则</label>
                                        <div class="tp-col"><textarea id="descriptforvote" class="tp-form-control"></textarea> </div>
                                    </div>
                                    <div class="clearfix tp-from-group">
                                        <label class="tp-control-label tpz">范围</label>
                                        <div class="tp-col" id="voteCatogery">
                                            <% if (UserRole.isHeadmaster(sessionValues.getUserRole())) { %>
                                            <label class="ellipsis"><input type="checkbox" onchange="chooseElectRange(this)"
                                                                           value="school"/>全校</label>
                                            <% }  if (UserRole.isStudent(sessionValues.getUserRole()) || UserRole.isTeacher(sessionValues.getUserRole()) || UserRole.isHeadmaster(sessionValues.getUserRole())) { %>
                                            <c:forEach items="${classList}" var="classInfo">
                                                <label class="ellipsis" title="${classInfo.className}">
                                                    <input type="checkbox" onchange="chooseElectRange(this)" value="${classInfo.id}" class="catogeryitem view-catogery"/>${classInfo.className}
                                                </label>
                                            </c:forEach>
                                            <% } %>
                                        </div>
                                    </div>
                                    <div class="clearfix tp-from-group" style="position:relative;">
                                        <label class="tp-control-label tpz">参选角色</label>
                                        <div class="tp-col">
                                            <label class="ellipsis"><input type="checkbox" value="" class="catogeryitem eligibleCheck"
                                                                           id="teacherEligible"
                                                                           onchange="eligibleChecked(checked)">老师</label>
                                            <label class="ellipsis"><input type="checkbox" value="" class="catogeryitem eligibleCheck"
                                                                           id="studentEligible"
                                                                           onchange="eligibleChecked(checked)">学生</label>
                                            <label class="ellipsis"><input type="checkbox" value="" class="catogeryitem eligibleCheck"
                                                                           id="parentEligible"
                                                                           onchange="eligibleChecked(checked)">家长</label>
                                        </div>
                                    </div>
                                    <div class="clearfix tp-from-group" style="position:relative;">
                                        <label class="tp-control-label tpz">指定候选人</label>
                                        <div class="tp-col">

                                            <label class="ellipsis"><input type="checkbox" value="2" class="catogeryitem"
                                                                           onclick="displayAddContainer(checked, this)"
                                                                           id="customCandidateCheck"></label>
                                        </div>
                                    </div>
                                    <!-- 自定义联系人 start -->
                                    <div id="customCandidate" class="mydiv" style="display: none;">
                                        <div style="width:640px;height:40px;background:#546fb4;overflow:hidden;">
                                            <span style="font-size:16px;color:#fff;float:left;margin-left:20px;margin-top:10px;font-weight:bold;">指定候选人</span>
					        <span style="margin-right:20px;margin-top:10px;font-weight:bold;font-size:16px;color:#fff;float:right;">
					            <a onclick="closeDialog1('#customCandidate'); $('#contact-div').fadeOut();"  style="color:#fff;display:block; cursor:pointer">
                                    <img src="/img/shanchu.jpg" />
                                </a>
					        </span>
                                        </div>
                                        <div class="SContent-box">
                                            <div style="float:left;font-size:14px;margin-left:30px;margin-top:8px;overflow:hidden;">
                                                <span style="height: 116px;line-height: 116px;display: inline-block;position: absolute;">联系人：</span>
                                                <span style="margin-left: 60px;"><div id="recipient"></div></span>
                                            </div>
                                            <br><br><br>

                                            <div style="float:left;width:350px;height:40px;margin-top:10px;margin-left:75px;color:#9c9c9c;">
                                                <span style="float:left;"></span>
					            <span onclick="confirmCandidate()" style="cursor:pointer;float:right;width:75px;height:25px;background:#ff7625;color:#fff;margin-top:14px;font-size:14px;font-weight:bold;line-height:25px;">确定
								</span>
                                            </div>
                                        </div>
                                        <div class="contact-div">
                                            <p id="contact-list" style="text-align: center;line-height: 24px"></p>
                                        </div>
                                    </div>
                                    <!-- 自定义联系人 end -->
                                    <div class="custom-candidate">
                                        <div id="custom-container" class="tp-form-control tp-candidate"></div><a class="custom-add-btn" onclick="addCandidate()">添加候选人</a>
                                    </div>
                                    <div class="clearfix tp-from-group">
                                        <label class="tp-control-label tpz">投票角色</label>
                                        <div class="tp-col">
                                            <label class="ellipsis"><input type="checkbox" onchange="" value="" class="catogeryitem" id="teacherVotable">老师</label>
                                            <label class="ellipsis"><input type="checkbox" onchange="" value="" class="catogeryitem" id="studentVotable">学生</label>
                                            <label class="ellipsis"><input type="checkbox" onchange="" value="" class="catogeryitem" id="parentVotable">家长</label>
                                        </div>
                                    </div>
                                    <div class="clearfix tp-from-group">
                                        <label class="tp-control-label tpz tp-h-center">每人票数</label>
                                        <div class="tp-col clearfix ">
                                            <input class=" tp-form-control tpz inputtime" type="number" style="width:185px;" id="ballotCount"  min="1" value="3"/>
                                        </div>
                                    </div>
                                    <div class="clearfix tp-from-group">
                                        <label class="tp-control-label tpz tp-h-center">开始日期</label>
                                        <div class="tp-col clearfix ">
                                            <input class="Wdate tp-form-control tpz inputtime" type="text" style="width:185px;" id="starttimeforvote"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'#F{$dp.$D(\'closetimeforvote\')}'})" value=""/>
                                        </div>
                                    </div>
                                    <div class="clearfix tp-from-group">
                                        <label class="tp-control-label tpz tp-h-center">结束日期</label>
                                        <div class="tp-col clearfix ">
                                            <input class="Wdate tp-form-control tpz inputtime" type="text" style="width:185px;" id="closetimeforvote"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'starttimeforvote\')}'})" value=""/>
                                            <!-- <input id="inputEmail3 " class="tp-form-control tpz inputtime" type="text" > -->
                                            <button class="tp-btn tp-handle tpy" type="button" onclick="launchVote()">发布</button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <!-- 发起投票 -->


                            <%--投票列表--%>
                            <div class="elect-list-container" style="overflow: visible">
                            </div>
                        </div>
                        <!-- 我的投票结束 -->


                        <!-- 朋友圈开始 -->
                        <div class='friend-container'>
                            <div class='friend-main-container'>
                                <div id="form-container" >
                                    <img src="/img/tell.jpg">
                                    <button class='receive_btn'>收到的评论</button>
                                <textarea id="blog_content" class='input-newthing' rows='2'
                                          placeholder="说点什么吧"></textarea>
                                    <!-- <div id="blog_content" contenteditable="true" class='input-newthing'></div> -->
                                    <div style="position:relative;min-height: 32px;">
                                        <label id="upload-blog-img" for="image-upload" style="cursor:pointer;">
                                            <img src="/img/tp_tool_img.png" class="upload-blog-image" alt="" title="上传图片"/>
                                            <span class="upload-blog-image">上传图片</span>
                                        </label>

                                        <div class="size-zero">
                                            <input type="file" name="image-upload" id="image-upload" accept="image/*"
                                                   multiple="multiple"/>
                                        </div>
                                        <img src="/img/loading4.gif" id="blogpicuploadLoading"/>

                                        <div id="img-container">
                                            <ul></ul>
                                        </div>
                                        <a class='publish-commit' onclick="submitBlog();">提交</a>
                                        <select class="select-public" id="sendtype">
                                            <option value="1">公开</option>
                                            <option value="3">本班</option>
                                            <option value="2">本年级</option>
                                        </select>

                                        <div style="position:absolute;right:165px;top:4px;display: none;"
                                             id="headmastertheme"><input type="checkbox" id="theme"
                                                                         style="vertical-align: middle;"/><label for="theme"
                                                                                                                 style="vertical-align: middle;display: inline-block;font-family: 'Tahoma';margin-left: 3px;color: #FF8800;">主题帖</label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="friend-container-list">
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
                                <div id="blog-list">
                                </div>
                                <!-- 用户回复栏 -->
                            </div>
                        </div>
                        <!-- 朋友圈结束 -->



                        <!-- 收到的评论开始 -->
                        <div id="mycomments" class='mycomment-container'>
                            <div class='mycomment_bar'>
                                <span>我的评论</span>
                                <button class='receive_btn'>收到的评论</button>
                            </div>
                            <div class='mycomment-main-container'>

                            </div>
                        </div>
                        <!-- 收到的评论结束 -->

                        <div id="discuss_list_container">
                            <div id="discuss_list" style="min-height:150px;">
                                <%--  <div>
                                    <img />
                                    <p style="margin-bottom:5px"><a>[通知]</a>这是一条班级通知</p>
                                    <p style="margin-top:5px"><span>雪白透亮</span><span>于</span></p>
                                 </div> --%>


                            </div>
                        </div>
                        <div id="reply_list" style="min-height:150px;">

                        </div>

                        <div style="margin-top: 30px; margin-bottom: 30px; text-align:center;overflow:visible;">
                            <div id="example"></div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="fullbg">
    </div>

    <%@ include file="../common_new/foot.jsp" %>
    <!-- 弹出私信框 -->
    <div id="check-hw-container"></div>
    <div class="alert-bg" style="z-index: 99"></div>
    <div class="letter-container">
        <div class="letter-title">发送私信给</div>
        <div class="letter-close">&times;</div>
        <textarea class="letter-input" rows="3" placeholder="请输入您要对ta说的话..."></textarea>
        <a href="javascript:;" class="letter-release">发送</a>
    </div>
    <div class="send-result-bg"></div>
    <div class="send-result">√ 发送成功</div>
    <div class="info-card"></div>

    <div id="bg" class="bg" style="display: none;"></div>





    <%--
      String chat =request.getParameter("chat");
      if(StringUtils.isNotBlank(chat) && chat.equalsIgnoreCase("1"))
      {
          %>
           <%@ include file="/WEB-INF/pages/groupchat/chatPages.jsp" %>
           <%@ include file="/business/easymob-webim1.0/index.jsp" %>
          <%
      }

     --%>






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
                //playMovie();
            } else {
                // $('#version9').addClass('active');
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
        $(function(){
            $("body").on("click",".II",function(){
                $(".VVVV").show(300).delay(1000).hide(300);;
            });
            /*$(".II").click(function(){
                $(".VVVV").show();
            })*/
        })
        $(function(){
            $("body").on("click",".III",function(){
                $(".VVVVV").show(300).delay(1000).hide(300);;
            });
            /*$(".II").click(function(){
             $(".VVVV").show();
             })*/
        })
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
</div>
<div class="VVVV" style="width: 200px;height: 100px;background: white;border:1px solid #ff7e00;position: fixed;top:50%;left: 50%;margin-top: -50px;margin-left: -100px;line-height: 100px;color:#ff7e00;text-align: center;font-size: 16px;display: none">
    已收藏
</div>
<div class="VVVVV"  style="width: 200px;height: 100px;background: white;border:1px solid #ff7e00;position: fixed;top:50%;left: 50%;margin-top: -50px;margin-left: -100px;line-height: 100px;color:#ff7e00;text-align: center;font-size: 16px;display: none">
    已添加到成长记录
</div>
</body>
</html>