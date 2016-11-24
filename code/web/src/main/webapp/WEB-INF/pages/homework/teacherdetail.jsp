<%--
  Created by IntelliJ IDEA.
  User: fulaan
  Date: 15-8-17
  Time: 下午5:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html style="height: 100%;min-height: 900px">
<head>
  <title>作业</title>
  <link rel="stylesheet" href="/static_new/css/homework.css">
  <link href="/static_new/css/reset.css" rel="stylesheet"/>
  <link href="/static_new/css/schoolsecurity.css?v=2015041602" rel="stylesheet" />
    <link href="/static_new/css/friendcircle/homepage.css?v=1" type="text/css" rel="stylesheet">
  <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.fileupload.js"></script>
  <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
  <script type="text/javascript" src="/static/js/lessons/lesson-view.js"></script>
  <%--计算发表日期--%>
  <script src="/static/js/shareforflipped.js" type="text/javascript"></script>
  <%--录音--%>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
  <script type="text/javascript" src="/static/js/swfobject.js"></script>
  <script type="text/javascript" src="/static/js/recorder.js"></script>
  <script type="text/javascript" src="/static/js/main.js"></script>

  <script type="application/javascript">
    $(function(){
      $(".reverse-HF").click(function(){
        $(".detail-SJ").show();
        $(".detail-SJJ").show();
        $(".detail-popup").show();
      })
      $(".detail-DE").click(function(){
        $(".detail-SJ").hide();
        $(".detail-SJJ").hide();
        $(".detail-popup").hide();
      })
    })
  </script>
</head>
<body>

<!--===========================startt头部==============================-->
<%@ include file="../common_new/head.jsp" %>
<!--===========================end头部==============================-->
<div class="reverse-main">
  <!--===========================start左边导航==============================-->
  <%@ include file="../common_new/col-left.jsp" %>
  <!--===========================end左边导航==============================-->
    <!--===========================start广告==============================-->
    <!--广告-->
    <c:choose>
        <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
        </c:when>
        <c:otherwise>
            <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
        </c:otherwise>
    </c:choose>
    <!--广告-->
    <!--===========================end广告==============================-->
  <div class="reverse-right">
    <div class="reverse-top">
      <dl>
        <dt>
          <em id="back">返回&gt;</em>
        </dt>
        <dd>
          <img src="" width="50" height="50" id="photo">
          <em class="reverse-E">[作业]</em><i id="hwtitle"></i>
            <%--<div class="reverse-LA">--%>
                <%--<span class="reverse-I">--%>
                    <%--<strong>批改作业</strong>--%>
                <%--</span><!--批改作业-->--%>
                <%--<span class="reverse-II">--%>
                    <%--<strong>批改进阶</strong>--%>
                <%--</span><!--批改进阶-->--%>
                <%--<span class="reverse-III">--%>
                    <%--<strong>查看统计</strong>--%>
                <%--</span><!--查看统计-->--%>
                <%--<span class="reverse-IV">--%>
                    <%--<strong>编辑</strong>--%>
                <%--</span><!--编辑-->--%>
                <%--<span class="reverse-V">--%>
                    <%--<strong>删除</strong>--%>
                <%--</span><!--删除-->--%>
            <%--</div>--%>

            <div class="reverse-LA" id="items">
            <script id="itemsTmpl" type="text/template">
                {{? it.pg==1}}
                <span class="reverse-I"  value="{{=it.id}}" lessonId="{{=it.lessonId}}">
                    <strong>批改作业</strong>
                </span><!--批改作业-->
                {{?}}
                {{? it.exerciseId!="noExercise"}}
                <span class="reverse-II"  value="{{=it.id}}" lessonId="{{=it.lessonId}}" exerciseId="{{=it.exerciseId}}">
                    <strong>批改进阶</strong>
                </span><!--批改进阶-->
                {{?}}
                <span class="reverse-III"  value="{{=it.id}}" lessonId="{{=it.lessonId}}" pg="{{=it.pg}}">
                    <strong>查看统计</strong>
                </span><!--查看统计-->
                <span class="reverse-IV"  value="{{=it.id}}" lessonId="{{=it.lessonId}}">
                    <strong>编辑</strong>
                </span><!--编辑-->
                <span class="reverse-V"  value="{{=it.id}}" lessonId="{{=it.lessonId}}">
                    <strong>删除</strong>
                </span><!--删除-->
            </script>
            </div>
            <br>
          <strong id="teaName"></strong><label id="date"></label>
        </dd>
        <dd>
          <p id="con"></p>
        </dd>
      </dl>
    </div>
    <div class="reverse-info">
      <dl id="hwReply">

        <%--<dd>--%>
          <%--<div class="reverse-info-left">--%>
            <%--<img src="http://placehold.it/50x50" width="50" height="50">--%>
            <%--<em>hao</em><label>发表于一天前</label><br>--%>
            <%--<p>laskjfljakjflajfajlfkjalkjflasjfljasdlfkj</p>--%>
          <%--</div>--%>
          <%--<div class="reverse-info-right">--%>
            <%--<button class="reverse-HF">回复</button>--%>
            <%--<button>批复</button>--%>
          <%--</div>--%>
          <%--<div style="clear: both"></div>--%>
          <%--<label>播放</label>--%>
        <%--</dd>--%>

      </dl>

      <%--分页--%>
      <div class="page-paginator" style="clear:both;;">
        <span class="first-page">首页</span>
                        <span class="page-index">
                            <span class="active">1</span>
                            <%--<span>2</span>--%>
                            <%--<span>3</span>--%>
                            <%--<span>4</span>--%>
                            <%--<span>5</span>--%>
                            <i>···</i>
                        </span>
        <span class="last-page">尾页</span>

      </div>
      <%--分页结束--%>
      <!--==========================回复弹出框===========================-->
      <div class="detail-SJ"></div>
      <div class="detail-SJJ"></div>
      <div class="detail-popup">
        <%--<textarea></textarea><br>--%>
        <%--<span>语音</span><i>添加附件</i><strong>拍照</strong><br>--%>
        <%--<label>播放</label>--%>
        <%--<div>--%>
          <%--<p>1.成绩.txt</p><button class="detail-DE">提交</button>--%>
        <%--</div>--%>
          <div id="reply_section" class="detail-bottom-I">
            <div id="upload-photo-container">
            </div>
            <div style="height:auto;margin-bottom:0;" class="detail-bottom-lt">
        <textarea id="textarea"></textarea>

              <div id="recordercontainer1">
                <div id="recorder" class="">
                  <div class="area">
                    <span class="a12" onclick="showflash('recordercontainer1')">
                        <%--<img src="/img/mic.png" style="width: 19px; height: 19px;"/>--%>
                        语音
                    </span>

                    <div style="padding-top: 10px;position: absolute;z-index: 50000;">
                      <div class="sanjiao"
                           style="width: 0;height: 0;border-left: 8px solid transparent;border-right: 8px solid transparent;border-bottom: 10px solid rgb(100,100,100);position:absolute;display: inline-block;top:0px;left:10px;display:none;"></div>
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
              <div id="attacher">
                <label for="file_attach">
                  <img src="/img/fileattach.png"/>
                  添加附件
                </label>
                <img src="/img/loading4.gif" id="fileuploadLoading" style="display:none;"/>

                <div style="width: 0; height: 0; overflow: visible">
                  <input id="file_attach" type="file" name="file" value="添加附件" size="1"
                         style="width: 0; height: 0; opacity: 0">
                </div>
              </div>
              <%--<div class="take-photo"><strong>拍照</strong></div>--%>
              <%--<div class="teacher-exam-submit" style="margin-left: 170px;">--%>
              <%--<p><a class="exam-submit" onclick="submitReply();"><img id="submitloading" src="/img/loading4.gif" style="display:none;vertical-align: middle;margin-right:5px;"/>提交</a></p>--%>
              <%--</div>--%>
              <button id="submit">提交</button><button id="quit">取消</button>
            </div>
          </div>
      </div>
    </div>
  </div>
</div>

<div id="check-hw-container"></div>
<div class="alert-bg" style="z-index: 99"></div>
<div style="clear: both"></div>
<%@ include file="../common_new/foot.jsp" %>

<script id="hwReplyTmpl" type="text/template">
  {{ for(var i in it) { }}
  <dd>
    <div class="reverse-info-left">
      <img src="{{=it[i].avatar}}" width="50" height="50">
      <em>{{=it[i].userName}}</em><label>发表于{{=getAdaptTimeFromNow(it[i].time)}}</label><br>
      <p>{{=it[i].content}}</p>
    </div>
    <div class="reverse-info-right">
        <img class="delete deleteHW"  src="/img/dustbin.png" title="删除" userId="{{=it[i].userId}}" time="{{=it[i].time}}">
      <button class="reverse-HF" studentId="{{=it[i].userId}}" time="{{=it[i].time}}">回复</button>
      <button id="PY" studentId="{{=it[i].userId}}" time="{{=it[i].time}}">{{? it[i].correct==1}}已{{?}}批阅</button>
    </div>

    <%--<label>播放</label>--%>
    {{? it[i].voiceFile&&it[i].voiceFile.length>0}}
    <p style="margin: 10px 0px 10px 60px"><a onclick="playVoice('{{=it[i].voiceFile[0].value}}');" url="{{=it[i].voiceFile[0].value}}"><img
            src="/img/yuyin.png">播放</a></p>
    {{?}}

        {{? it[i].docFile&& it[i].docFile.length>0}}

            <div class="contentImg-container">
            {{for (var j in it[i].docFile) { }}
                {{var at = it[i].docFile[j];}}
                {{if(checkImage(at.value)) { }}
                    <img class="hw-submit-img teacher-check" data-id="{{=at.idStr}}" src="{{=encodeURI(at.value)}}" />
                {{}}}
            {{ } }}
            </div>
            {{for (var j in it[i].docFile) { }}
                {{var at = it[i].docFile[j];}}
                {{if(!checkImage(at.value)) { }}
                     <p style="margin: 10px 0px 10px 60px"><a href="/commondownload/file.do?url={{=encodeURI(at.value)}}&name={{=at.name}}"><img
                        src="/img/fileattach.png"/>附件{{=parseFloat(j)+1}}:{{=at.name}}</a></p>
                {{}}}
            {{ }}}
        {{?}}



  <%--老师回复的内容--%>
  {{var hf = it[i].hf;}}
  {{ for(var i in hf) { }}
  <div class="detail-bottom-II">
    <img src="{{=hf[i].avatar}}" width="50px" height="50px">
    <em>{{=hf[i].userName}}</em><i>发表于{{=getAdaptTimeFromNow(hf[i].time)}}</i>

    <p>{{=hf[i].content}}</p>
    {{? hf[i].voiceFile&&hf[i].voiceFile.length>0}}
    <p><a onclick="playVoice('{{=hf[i].voiceFile[0].value}}');" url="{{=hf[i].voiceFile[0].value}}"><img
            src="/img/yuyin.png">播放</a></p>
    {{?}}

    {{? hf[i].docFile&& hf[i].docFile.length>0}}
    {{for (var j in hf[i].docFile) { }}
    {{var hfat = hf[i].docFile[j];}}
    {{var hfstr = hfat.value;}}
    {{var hfext = hfstr.substring(hfstr.length-3,hfstr.length);}}
    {{var hfrule = /(jpg|gif|bmp|png)/;}}
    {{? hfrule.test(hfext) }}
    <%--{{? checkImage(at.value)}}--%>
    <a class="fancybox" href="{{=encodeURI(hfat.value)}}" data-fancybox-group="homework"><img class="homework-img" title="点击查看大图" src="{{=encodeURI(hfat.value)}}"></a>
    {{??}}
    <p><a href="/commondownload/file.do?url={{=encodeURI(hfat.value)}}&name={{=hfat.name}}"><img
            src="/img/fileattach.png"/>附件{{=parseFloat(j)+1}}:{{=hfat.name}}</a></p>
    {{?}}
    {{ } }}
    {{ }}}
    <div style="clear: both;"></div>
  </div>
  {{ } }}
  <%--老师回复结束--%>

  </dd>
  {{ } }}
</script>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('/static_new/js/modules/homework/0.1.0/teacherdetail.js', function (teaDetail) {
    teaDetail.init();
  });
</script>
<script type="text/javascript">
    function checkImage(str) {
        var ext = str.substring(str.length - 3, str.length);
        var rule = /(jpg|gif|bmp|png)/;
        if (rule.test(ext)) {
            return true;
        } else {
            return false;
        }
    }
    //上传涂鸦后回调函数
    function uploadComplete() {
        $('#check-hw-container').empty().hide();
        $('.alert-bg').hide();
        $('#fullbg').hide();
        location.reload();
    }
</script>
</body>
</html>
