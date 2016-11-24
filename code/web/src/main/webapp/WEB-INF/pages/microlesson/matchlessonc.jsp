<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
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
  <script type="text/javascript" src="/static/js/flippedcoursemaintain2.js"></script>

  <script type="text/html" id="video-list-template">
    {{each videoList as value index}}
    <div class="video-container">
      <div class="image-div">
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


      getLessonInformation();

    });
    function addque() {
      location.href = "/question/addQuestion/" + "${retype}" + "/" + courseId;
    }
  </script>
  <title>题目维护</title>
</head>
<body mid="${matchid}">
<%@ include file="../common_new/head-cloud.jsp"%>
<%--<%@ include file="../common/header.jsp" %>--%>
<div id="content_main_container">
  <div id="content_main">

    <%--<%@ include file="../common_new/col-left.jsp" %>--%>


    <div id="right-container" style="/*margin-left: 220px;*/ padding-left: 0">
      <div id="upload-banner">
        <div class="content-div">
          <div class="content-title">信息修改</div>
          <hr style="background-color: #FF7E00"/>

          <div style="margin: 20px 20px 0">
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
              上传视频
            </div>
            <hr style="background-color: #FF7E00"/>

            <div style="margin:20px 0 0 20px; position: relative">
              <input type="file" name="file_upload" id="file_upload"/>

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
            <div style="margin-left:20px;color:#666666;">支持的文件格式：avi/mp4/mpg/flv/wmv/mov/mkv，支持文件大小：300M以内
            </div>

            <div class="video-list-container"></div>
          </div>
        </div>
      </div>


      <div>
        <div class="content-div">
          <div class="content-title">辅助学习资料</div>
          <hr class="content-title-hr"/>
          <div class="courseware">
            <a class="courselink" onclick="showDialog('.inside-dialog')"><img
                    src="/img/courseupload.png"></a>
          </div>
          <div id="courseware-list">

          </div>

          <div>
            <div class="pagination">
            </div>
          </div>
        </div>
      </div>

      <div style="margin: 30px auto; text-align: center">
        <input onclick="updatecourse()" type="button" class="submit-button" value="保存"
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
                  <input type="hidden" name="lid" value="${param.lessonId}"/>
                  <input type="hidden" name="ftype" id="ftype" value="0"/>
                </div>
              </dd>
              <dd style="margin: 20px 0">课件名：
                <input id="courseWareName" name="name" placeholder="请输入课件的名称..."
                       style="background-color: #EFEFEF;padding: 10px;width:240px;" type="text"/></dd>
              <dd>
                <input type="button" class="submit-button" value="上传" style="background: #FF7E00"
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
<%@ include file="../common/flippedrootc.jsp" %>

</body>
<script>
  window.onload = function () {
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
</script>
</html>