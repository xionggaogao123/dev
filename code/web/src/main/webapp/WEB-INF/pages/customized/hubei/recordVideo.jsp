<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/8/23
  Time: 15:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
  <title>录播回顾</title>
  <meta charset="utf-8">
  <link rel="stylesheet" type="text/css" href="/customizedpage/hubei/css/live.css">
  <script type="text/javascript" src="/static/js/WdatePicker.js"></script>
  <script type="text/javascript" src="/static/js/jquery.min.js"></script>

</head>
<body>
<div class="header">
  <img src="/customizedpage/hubei/images/hubeiLogo.png">
  <span class="span-hello cu-pointer" onclick="customizedLogout()">退出</span>
  <span class="span-hello">hi,${userName}</span>
</div>
<%--<div class="header">--%>
  <%--<img src="/customizedpage/hubei/images/hubei_logo.png">--%>
<%--</div>--%>
<div class="videolist-f">
  <div class="videolist">
    <div class="review-title">
      <span>录播回顾</span>
      <img src="/customizedpage/hubei/images/bg100.png">
      <div class="sel-module">
        <img src="/customizedpage/hubei/images/img_canlendar.png">
        <input style="cursor:pointer" type="text" onfocus="WdatePicker({maxDate:'%y-%M-%d'})" class="Wdate" id="startDateInput" />
        <button id="select">全部视频</button>
          <select id="kl" required="required" class="sel-sel">
              <option value="">全部</option>
              <option value="hubei">湖北</option>
              <option value="xizang">西藏</option>
          </select>
      </div>
    </div>
    <div class="clearfix" id="videoList">

    </div>
    <div class="new-page-links"></div>
  </div>
</div>
<div class="footer-b">
  <div class="footer-cont2">
    <span class="sp1">主办：湖北省教育厅&nbsp;&nbsp;&nbsp;&nbsp;/</span>
    <span class="sp1">技术支持：上海复兰信息科技有限公司&nbsp;&nbsp;&nbsp;&nbsp;/</span>
    <span class="sp1">客服热线：400 820 6735</span>
    <%--<a class="sp2">版主中心</a>--%>
    <%--<a class="sp2">站长管理&nbsp;|</a>--%>
    <%--<a class="sp2">后台管理&nbsp;|</a>--%>
  </div>
</div>

<div id="YCourse_player" class="player-container" style="display: none">
  <div id="player_div" class="player-div"></div>
  <div id="sewise-div"
       style="display: none; width: 630px; height: 360px; max-width: 800px;">
    <script type="text/javascript"
            src="/static/plugins/sewiseplayer/sewise.player.min.js"></script>

    <span class="player-close-btn"></span>
    <script type="text/javascript">
      SewisePlayer.setup({
        server: "vod",
        type: "m3u8",
        skin: "vodFlowPlayer",
        logo: "none",
        lang: "zh_CN",
        topbardisplay: 'enable',
        videourl: ''
      });
    </script>
  </div>
</div>

<script type="text/javascript">

  var isFlash = false;
  function getVideoType(url) {
    if (url.indexOf('polyv.net') > -1) {
      return "POLYV";
    }
    if (url.indexOf('.swf')>0) {
      return 'FLASH';
    }
    return 'HLS';
  }

  function playerReady(name) {
    if (isFlash) {
      SewisePlayer.toPlay(playerReady.videoURL, "视频", 0, false);
    }
  }

  $('.player-close-btn').click(function(){
    $('#YCourse_player').hide();
    $(".bg").hide();
    /*var pSectionId = $('#pSectionId').val();
     var postId = $('#postId').val();
     var personId = $('#personId').val();
     location.href='/forum/postDetail.do?pSectionId='+pSectionId+'&postId='+postId+'&personId='+personId;*/
  })

  function tryPlayYCourse(url) {
    $("#YCourse_player").show();
    $(".player-close-btn").css({
      "display": 'block'
    });
    var videoSourceType = getVideoType(url);
    $('.bg').fadeIn('fast');
    var $player_container = $("#YCourse_player");
    $player_container.fadeIn();

    if (videoSourceType == "POLYV") {
      $('#sewise-div').hide();
      $('#player_div').show();
      var player = polyvObject('#player_div').videoPlayer({
        'width': '800',
        'height': '450',
        'vid': url.match(/.+(?=\.swf)/)[0].replace(/.+\//, '')
      });
    } else {
      $('#player_div').hide();
      $('#sewise-div').show();
      try {
        SewisePlayer.toPlay(url, "视频", 0, true);
      } catch (e) {
        playerReady.videoURL = url;
        isFlash = true;
      }
    }
  }
</script>

<script id="tmpl" type="text/template">
  {{~it:video:index}}
   <div class="video-li">
     <%--<img src="/customizedpage/hubei/images/ppp.png" class="img-img">--%>
       <img src="/img/fenmian.jpg" class="img-img">
     <img src="/customizedpage/hubei/images/img_play.png" onclick="tryPlayYCourse('{{=video.url}}')" class="img-play">
    <p>{{=video.name}}<img src="/customizedpage/hubei/images/delll.png" class="remove" videoId="{{=video.id}}"></p>
  </div>
  {{~}}
</script>
<script type="text/javascript">
  function customizedLogout(){

    var url='/customized/logout.do';
    $.ajax({
      type: "GET",
      data:{},
      url: url,
      async: false,
      dataType: "json",
      contentType: "application/x-www-form-urlencoded; charset=UTF-8",
      success: function(resp){
        location.href="/customized/hubei/hubeiLogin.do";
      }
    });

  }

</script>
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('/static_new/js/modules/customized/recordVideo.js');
</script>
</body>
</html>
