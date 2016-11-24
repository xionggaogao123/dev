<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
<!DOCTYPE HTML>
<html>
<head>
    <meta name="renderer" content="webkit">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="/static/css/dialog.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/flippedclassroom.css"/>
    <link rel="stylesheet" type="text/css" href="/static/plugins/uploadify/uploadify.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/lesson/video-list.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/zTreeStyle/zTreeStyle.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/tree.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/k6kt.css"/>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="/static/js/ztree/jquery.ztree.all-3.5.min.js"></script>
    <script type="text/javascript" src="/static/plugins/uploadify/jquery.uploadify.min.js"></script>
    <script type="text/javascript" src="/static/plugins/bowser.min.js"></script>
    <script type="text/javascript" src="/static/js/template.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/js/common/protocol-detect.js"></script>
    <script type="text/javascript" src="/static/js/flippedcoursemaintain.js"></script>

    <script type="text/html" id="video-list-template">
        {{each videoList as value index}}
        <div class="video-container">
            <div class="image-div">
                <div class="bg-img"></div>
                <img src="{{value.value? value.value:'/img/K6KT/video-cover.png'}}" width="138" height="78"
                     tag="{{value.sortTag}}" onerror="coverError(this);" state="1"/>
                <%--<a class="move-left"><div></div></a>--%>
                <a class="delete" onclick="removeLessonVideo(this)">
                    <div></div>
                </a>
                <%--<a class="move-right"><div></div></a>--%>
            </div>
            <div class="video-name" videoid="{{value.id}}">{{value.type? value.type:'未命名'}}</div>
        </div>
        {{/each}}
    </script>
    <script type="text/javascript">
        var currentPageID = 1;
        var courseId = '${param.lessonId}';


        var courseName, courseDescr, courseStartTime, courseEndTime;

        var someFileFailed = false;
        function uploadComplete(queueData) {
            if (someFileFailed) {
                MessageBox("部分视频上传失败，请重试。", -1)
                someFileFailed = false;
            }
        }

        $(function () {
            $('#retype').val("${retype}");
            $('#file_upload').uploadify({
                'swf': "/static/plugins/uploadify/uploadify.swf",
                'uploader': '/lesson/video/upload.do?aa=${param.lessonId}',
                'method': 'post',
                'buttonText': '选择文件',
                'fileTypeDesc': '视频文件',
                'fileSizeLimit': '300MB',
                'fileTypeExts': '*.avi; *.mp4; *.mpg; *.flv; *.wmv; *.mov; *.mkv',
                'multi': true,
                'fileObjName': 'Filedata',
                'formData': {'lessonId': '${param.lessonId}'},
                'onUploadSuccess': function (file, response, result) {
                    try {
                        var json = $.parseJSON(response);
                        if (json.code == 200) {
                            getLessonVideos();
                        } else {
                            //MessageBox(json.uploadType, -1);
                            someFileFailed = true;
                        }
                    } catch (err) {
                    }
                },
                'onQueueComplete': uploadComplete,
                'onUploadError': function (file, errorCode, errorMsg, errorString) {
                    //MessageBox("服务器响应错误。", -1);
                    someFileFailed = true;
                }
            });

            /*getGradeInfoOfTeacher(function (data) {
                if (data.gradeIdList) {
                    for (var i in data.gradeIdList) {
                        var v = data.gradeIdList[i];
                        $('#grade-list').append('<option value="' + v.status + '">' + v.description + '</option>');
                    }
                }
            });*/
            getLessonInformation();
            /*
            $.ajax({
                url: "/reverse/getFztest.action",
                data: "courseID=" + courseId,
                type: "post",
                dataType: "json",
                success: function (data) {
                    var html = "<table class='question-list-table'><tr>";
                    if (data.isUser == true) {
                        html += "<th>题目编号</th><th>日期</th></tr>";
                        $.each(data.list, function (i, pro) {
                            html += "<tr><td><a href='/reverse/courseexam/" + pro.id + "/" + courseId + "/0/" + "${retype}" + "'>" + (i + 1) + "</a></td>" +
                            "<td>" + pro.cTime + "</td>" +
                            "</tr>";
                        });
                    } else {
                        html += "<th>题目编号</th><th>日期</th><th>删除</th><th>位置调整</th></tr>";
                        $.each(data.list, function (i, pro) {
                            html += "<tr><td><a href='/reverse/courseexam/" + pro.id + "/" + courseId + "/0/" + "${retype}" + "'>" + (i + 1) + "</a></td>" +
                            "<td>" + pro.cTime + "</td>" +
                            "<td><a href='/reverse/deleteFztest.action?fztestID=" + pro.id + "&courseID=" + courseId + "&retype=" + "${retype}" + "'><i class='fa fa-times fa-2x'></i></a></td>" +
                            "<td><a href='/reverse/updateFztestSequence.action?fztestID=" + pro.id + "&courseID=" + courseId + "&retype=" + "${retype}" + "'><i class='fa fa-share fa-2x'></i></a></td>" +
                            "</tr>";
                        });
                    }
                    html += "</table>";
                    $("#cc").append(html);
                }
            });*/
        });
        function addque() {
            location.href = "/question/addQuestion/" + "${retype}" + "/" + courseId;
        }
    </script>
    <title>题目维护</title>
</head>
<body>
<%@ include file="../common_new/head.jsp" %>
<div id="content_main_container">
    <div id="content_main">

        <%@ include file="../common_new/col-left.jsp" %>


        <div id="right-container" style=" padding-left: 0">
            <div id="upload-banner">
                <div class="content-div">
                    <div class="content-title">信息修改</div>
                    <hr style="background-color: #FF7E00"/>

                    <div style="margin: 20px 20px 0;height: 50px;">
                        <div style="float: right; overflow: hidden;">
                        </div>

                        <div style="float: left">
                            <dl style="font-size: 14px; color:#666666;">
                                <dd>课程名：</dd>
                                <dd>
                                    <input id="course-name" style="background-color: white;padding: 10px;width:380px;"
                                           type="text"/>
                                </dd>
                            </dl>
                        </div>
                    </div>

                    <div id="video-upload-section">
                        <div class="content-title" style="margin-top: 30px">
                            微课视频
                        </div>
                        <hr style="background-color: #FF7E00"/>

                        <div style="margin:20px 0 0 20px; position: relative;margin-bottom: 15px;">
                            <input type="file" name="file_upload" id="file_upload" style="opacity: 0;z-index: 999999999;position: relative;cursor: pointer;"/>
                            <div id="file_upload-button" class="uploadify-button" style="height: 30px; line-height: 30px; width: 120px;position:relative;top:-25px;cursor: pointer"><span class="uploadify-button-text">选择文件</span></div>
                            <div onclick="skipCheckProtocol(this)" class="uploadify-button"
                                 link="recorder://lessionid=${param.lessonId}$/"
                                 style="width: 120px; height: 30px; line-height: 30px; position: absolute; left: 200px; top: 0; cursor: pointer">
                                录制微课
                            </div>

                            <div style="position: absolute; left: 400px; top: 0; line-height: 30px; color: #666666; font-size: 14px;">
                                <i class="fa fa-exclamation-circle fa-lg"></i> 首次录课前，请下载安装 <a
                                    href="/upload/resources/recorder.exe" style="color: red;"><i
                                    class="fa fa-video-camera"></i> 录课神器</a>
                            </div>
                        </div>
                        <div style="margin-left:20px;color:#666666;">支持的文件格式：avi/mp4/mpg/flv/wmv/mov/mkv，支持文件大小：100M以内
                        </div>

                        <div class="video-list-container"></div>
                    </div>
                </div>
            </div>


            <div>
                <div class="content-div" style="overflow: hidden">
                    <div class="content-title">
                        微课进阶练习
                    </div>
                    <hr class="content-title-hr"/>


                    <%--小练习配置--%>
                    <c:choose>
                        <c:when test="${exerciseId!=null}">
                            <div style="width:900px;height: 30px;background-color: #ffffff;text-indent: 2em;line-height: 30px;overflow: hidden;">
                                <div style="width: 35em;height: 30px;overflow: hidden;float: left;">
                                        ${exerciseName}
                                </div>
                                <div style="float: right;position: relative;right: 20px;">
                                    <span onclick="configdDtk('${exerciseId}')" style="color: #FF7F00;position: relative;right: 30px;cursor: pointer">配置</span>
                                    <span  onclick="deleteWord('${exerciseId}',this)" style="background-color: #E35A61;color: #666666;display: inline;padding: 0px 6px;border-radius: 15px;font-weight: bold;cursor: pointer">x</span>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div style="height: 200px;width: 600px;position: relative;overflow: hidden;float: left">
                                <form  id="submitWord" action="/lesson/exe/upload.do" method="post" enctype="multipart/form-data" >
                                    <div style="height: 40px;width: 500px;position: relative;left: 30px;">
                                        习题名称 <input name="name" id="practiseName" placeholder="必填" style="text-indent: 0.5em;width: 400px;height: 30px;border:1px solid #C3C3C3;border-radius: 3px;margin-top: 3px;">
                                    </div>
                                    <div style="height: 40px;width: 500px;position: relative;left: 30px;top:10px;overflow: hidden">
                                        上传习题
                                        <input type='text' name='textfield' placeholder="必填，doc或者docx类型的文档" readonly="true" id='questWord' class='txt'style="text-indent: 0.5em;width: 300px;height: 30px;border:1px solid #C3C3C3;border-radius: 3px;margin-top: 3px;background-color: #ffffff;line-height: 30px;;" />
                                        <input type='button' class='btn' value='选择文件'style="background-color: #ffffff;padding: 5px 13px;border-radius: 3px;margin-left: 15px;border: 1px solid #C3C3C3;cursor: pointer;font-family: 'Microsoft YaHei'" />
                                        <input type="file" name="examDoc" class="file" id="questWord1" size="28" onchange="document.getElementById('questWord').value=this.value" style="opacity: 0;filter:alpha(opacity=0);cursor: pointer;position: absolute;left: 373px;top: 3px;border:1px solid #C3C3C3;height: 30px;width: 80px;" />

                                    </div>
                                    <div style="height: 40px;width: 500px;position: relative;left: 30px;top:10px;">
                                        上传解析
                                        <input type='text' name='textfield' placeholder="可选，学生交卷后可查看解析，文档类型同上" id='answerWord' readonly class='txt'style="text-indent: 0.5em;width: 300px;height: 30px;border:1px solid #C3C3C3;border-radius: 3px;margin-top: 3px;background-color: #ffffff;line-height: 30px;;" />
                                        <input type='button' class='btn' value='选择文件'style="background-color: #ffffff;padding: 5px 13px;border-radius: 3px;margin-left: 15px;border: 1px solid #C3C3C3;cursor: pointer;font-family: 'Microsoft YaHei'" />
                                        <input type="file" name="parseDoc" class="file" id="answerWord1" size="28" onchange="document.getElementById('answerWord').value=this.value" style="opacity: 0;filter:alpha(opacity=0);cursor: pointer;position: absolute;left: 373px;top: 3px;border:1px solid #C3C3C3;height: 30px;width: 80px;" />

                                    </div>
                                    <a style="height: 50px;width: 500px;position: relative;left: 30px;top:20px;">
                                        <span style=" width: 300px;height: 30px;margin-top: 3px;"></span>
                                        <span  onclick="submitWord()" style="background-color: #fda616;padding: 6px 30px;border-radius: 5px; border: 1px solid #C3C3C3;cursor: pointer;color: #666666;position: relative;top: -10px;left: 30px;" >上传并配置</span>
                                    </a>
                                        <%--wordType表示该文档的作用类型  分为  课后习题和在线考试  1表示课后习题--%>
                                    <input type="hidden" name="lessonId" value="${param.lessonId}">
                                    <input type="hidden" name="type" value="2">
                                </form>
                            </div>

                        </c:otherwise>
                    </c:choose>

                </div>
            </div>
            <div>
                <div class="content-div">
                    <div class="content-title">教师教学资料</div>
                    <hr class="content-title-hr"/>
                    <div class="w-tab" style="overflow: hidden;">
                        <ul class="clearfix">
                            <li id="w-1" class="w-cur" ftp="0"><a href="javascript:;">课件</a> </li>
                            <li id="w-2" ftp="1"><a href="javascript:;">微课</a> </li>
                            <li id="w-3" ftp="2"><a href="javascript:;">教案</a> </li>
                            <li id="w-4" ftp="3"><a href="javascript:;">学案</a> </li>
                            <li id="w-5" ftp="4"><a href="javascript:;">习题</a> </li>
                        </ul>
                    </div>
                    <div class="w-wrap">
                        <div class="div-courseware clearfloat" style="width: 90%;">
                            <a class="courselink" onclick="showDialog('.inside-dialog')" style="float: left;">
                                <button class="course-btn" style="background: #fb8200;color: #fff;">选择文件</button>
                            </a>
                            <a class="w-push" onclick="">
                                <button onclick="updateCourseWare('')" id="push">一键推送</button>
                            </a>
                        </div>
                        <div class="w-main">
                            <div id="tab-w-1">
                                <div id="courseware-list">


                                </div>
                            </div>
                            <div id="tab-w-2">
                                <div id="courseware-list2">


                                </div>
                            </div>
                            <div id="tab-w-3">
                                <div id="courseware-list3">


                                </div>
                            </div>
                            <div id="tab-w-4">
                                <div id="courseware-list4">


                                </div>
                            </div>
                            <div id="tab-w-5">
                                <div id="courseware-list5">


                                </div>
                            </div>
                        </div>
                    </div>
                    <%--<div id="courseware-list">--%>
                    <%--<div class="div-courseware">--%>
                    <%--<a class="courselink" onclick="showDialog('.inside-dialog')">--%>
                    <%--<button class="course-btn">选择文件</button>--%>
                    <%--</a>--%>
                    <%--</div>--%>

                    <%--</div>--%>







                    <div>
                        <div class="pagination">
                        </div>
                    </div>
                </div>
            </div>
            <c:if test="${dirType == 'BACK_UP'}">
                <div class="content-div" style="display: none">
                    <c:choose>
                        <c:when test="${pushCount>0}">
                            <%--<input type="checkbox" id="syncPush" checked="checked"><label for="syncPush">同步已推送课程</label>--%>
                        </c:when>
                        <c:otherwise>
                            <div class="content-title" title="保存后同步推送到选中目录">推送</div>
                            <hr class="content-title-hr">
                            <div class="pushtree-split-container">
                                <h4>班级课程</h4>
                                <ul id="classSubjectDirUl" class="ztree dir-tree"></ul>
                            </div>
                            <div class="pushtree-split-container">
                                <h4>校本资源</h4>
                                <ul id="schoolDirUl" class="ztree dir-tree"></ul>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>

            <div style="margin: 30px auto; text-align: center">
                <input onclick="UpdateFZCourseInfo()" type="button" class="submit-button" value="保存"
                       style="font-size:13px; width:80px"/>
                <%--<input onclick="ResetCourseInfo()" type="button" class="submit-button" value="取消" style="margin-left:20px;background: gray;width: 80px; font-size:13px"/>--%>
            </div>

            <div id="fullbg">
            </div>

            <div class="inside-dialog" style="width:300px; margin-left: -150px; overflow: hidden">
                <div class="dialog-title">
                    <i class="fa fa-cloud-upload fa-2x" style="vertical-align: middle;margin-right:10px"></i>课件上传
                    <i class="fa fa-times-circle fa-lg" style="margin-top:5px;float: right;cursor: pointer"
                       onclick="closeDialog('.inside-dialog')"></i>
                </div>
                <div style="margin: 20px; overflow: visible">
                    <form action="/lesson/ware/add.do" method="post" enctype="multipart/form-data">
                        <dl>
                            <dd>
                                <div style="overflow: visible">
                                    <input name="doc" type="file"/>
                                    <input type="hidden" name="lid" id="lid" value="${param.lessonId}"/>
                                    <input type="hidden" name="ftype" id="ftype" value="0"/>
                                </div>
                            </dd>
                            <dd style="margin: 20px 0;display:none;">课件名：
                                <input id="courseWareName" name="name" placeholder="请输入课件的名称..."
                                       style="background-color: #EFEFEF;padding: 10px;width:240px;" type="text"/></dd>
                            <dd>
                            <dd  style="margin: 20px 0"></dd>
                                <input type="button" class="submit-button" value="上传" style="background: #FF7E00;"
                                       onClick="UploadCourseWare(this.form)"/>
                                <input type="text" hidden="hidden"/>
                            </dd>
                        </dl>
                    </form>
                </div>
            </div>


            <!-- Mozilla Only -->
            <iframe id="hiddenIframe" src="about:blank" style="display:none"></iframe>
            <!-- IE Case 1 -->
            <a id="hiddenLink" style="display:none;" href="#">custom protocol</a>
            <input id="protocol" value="" style="width: 0; height: 0;"/>

        </div>

    </div>
</div>
<%@ include file="../common_new/foot.jsp" %>

</body>
<script>
    window.onload = function () {
        <%--var word = '${currentWord.id}';--%>
        <%--if (word != null && word != '') {--%>
            <%--tip('试卷上传成功！3秒后进入配置页面......', 'configAtOnce()');--%>
        <%--}--%>
    }
    function go2Config() {
        setTimeout(function () {
            destroyCurrentWord();
            window.location.href = '/exercise/paperDetailConfig.do?wordexerciseId=${currentWord.id}';
        }, 3000);
    }
    function destroyCurrentWord() {
        $('#prompt-div').remove();
        $.ajax({
            url: '/exercise/destroyCurrentWord.do',
            data: {},
            type: 'post',
            dataType: 'json',
            success: function (data) {

            }

        });
    }
    function configAtOnce() {
        destroyCurrentWord()
        window.location.href = '/exercise/paperDetailConfig.do?wordexerciseId=${currentWord.id}';
    }
    function tip(msg, operation) {
        var message = '<p style="margin-bottom: 30px">' + msg + '</p>';
        message += '<input onclick="' + operation + '" type="button" class="submit-button"  value="直接进入" style="width: 80px; font-size:13px;background: #4DA34E;border-radius:5px;margin-left:320px;" />';
        $("#prompt-div").remove();
        var pdiv = '<div id="prompt-div">';
        pdiv += '<div class="message-box">';
        pdiv += '<div class="message-box-title">提示';
        pdiv += '</div><div>';
        pdiv += message;
        pdiv += '</div></div>';
        $("body").append(pdiv);
        $("#prompt-div").fadeIn();
        go2Config();
    }

    function configdDtk(exeid) {
        $.ajax({
            url: '/exam/canconf.do',
            data: {
                id: exeid
            },
            type: 'post',
            dataType: 'json',
            success: function (data) {
                if (data.code=='200') {
                	 window.location.href = '/exam/view.do?id=' + exeid+'&lesson=${param.lessonId}';
                } else {
                    alert("亲，该试卷已有学生答题啦,不能再配置了哟！");
                    return;
                }
            }

       });

    }
    function deleteWord(id, node) {
        k = confirm("确定要删除该试题吗");
        if (k) {
            $.ajax({
                url: '/lesson/exe/remove.do',
                type: 'post',
                dataType: "json",
                data: {
                    exeId: id,
                    lessonId:'${param.lessonId}'
                },
                success: function (result) {
                    if (result) {
                        alert("删除成功");
                        $(node).parent().parent().remove();
                        window.location.reload(true);
                    } else {
                        alert("删除失败T-T，请联系客服");
                    }
                }
            });
        }
    }
    function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return unescape(r[2]);
        return null; //返回参数值
    }

    function submitWord(){
        var k=checkForm();
        if(k){
            if($("#answerWord1").val()==null || $.trim($("#answerWord1").val())==""){
                $("#answerWord1").remove();
            }
            //$("#submitWord").submit();
            MessageBox('文档解析中......，请您耐心等待,上传完毕后自动进入练习配置', 0);
            fileUpload($("#submitWord").get(0), function (response) {
                try {

                    var json = $.parseJSON(response);
                    //var exam = json[0];
                    //exerciseId = exam.id;
                   //window.location.href = '/exam/view/' + json.exerciseId;
                    
                    window.location.href = '/exam/view.do?id=' + json.exerciseId+"&lesson=${param.lessonId}";
                } catch (e) {
                    MessageBox('上传错误。', -1);
                }
            });
        }
    }
    function checkForm(){
        var practiseName=$("#practiseName").val();
        var questWord=$("#questWord").val();
        var answerWord=$("#answerWord").val();
        if(practiseName==null || $.trim(practiseName)==""){
            alert("请输入习题名称");
            return false;
        }else if(questWord==null || $.trim(questWord)==""){
            alert("请上传习题");
            return false;
        }else if(suffix(questWord)!=".doc" && suffix(questWord)!=".docx"){
            alert("只能上传word文档(格式为doc或docx)");
            return false;
        }else if($.trim(answerWord)!=""){
            if(suffix(answerWord)!=".doc" && suffix(answerWord)!=".docx"){
                alert("只能上传word文档(格式为doc或docx)");
                return false;
            }
        }
        return true;
    }
    function triggerOnclick(id){
        $("#"+id).trigger('click');
    }
    function suffix(str){
        var k=str.lastIndexOf(".");
        return str.substring(k,str.length);
    }
    var $li=$(".w-tab").find("li");
    var $tab=$(".w-main").children("div");
    var len=$li.length;
    $li.click(function(){
        var $index=$(this).index();
        if ($index==2) {
            $('#push').hide();
        } else {
            $('#push').show();
        }
        $(this).addClass("w-cur").siblings().removeClass("w-cur");
        $tab.hide();
        $(".w-main").children("div").eq($index).show();
        $('#ftype').val($(this).attr('ftp'));
    })
</script>
</html>