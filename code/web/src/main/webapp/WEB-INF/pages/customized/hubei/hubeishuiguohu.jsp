<%--
  Created by IntelliJ IDEA.
  User: yan
  Date: 2016/2/23
  Time: 10:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <title>湖北省教育厅援藏教学平台</title>
  <meta charset="utf-8">
  <meta name="renderer" content="webkit">
  <%--<link rel="stylesheet" type="text/css" href="/customizedpage/hubei/css/live-tl.css">--%>
  <script type="text/javascript" src="/static/js/jquery.min.js"></script>
  <script type="text/javascript" src="/static/js/jwplayer/jwplayer.js"></script>
  <script type="text/javascript" src="/static/js/jwplayer/jwplayer.html5.js"></script>
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/doT.min.js"></script>
  <script type="application/javascript">
    $(document).ready(function()
    {
      jwplayer("container").setup({
        sources: [
          {
            file: "rtmp://139.196.198.137/hubei/hubei"
          }
        ],
        image: "/customizedpage/hubei/images/bg.jpg",
        autostart: true,
        width: 980,
        height: 540,
        primary: "flash"
      });
    })

  </script>
</head>
<body style="background: #000;">
<%@ include file="xizangshannan.jsp"%>
<%--<div class="header">
  &lt;%&ndash;<div class="headmid">&ndash;%&gt;
    &lt;%&ndash;&lt;%&ndash; <div class="logo"></div>&ndash;%&gt;&ndash;%&gt;
  &lt;%&ndash;</div>&ndash;%&gt;
    <img src="/customizedpage/hubei/images/hubeiLogo.png">
    <span class="span-hello cu-pointer" onclick="customizedLogout()">退出</span>
    <span class="span-hello">hi,${userName}</span>
</div>--%>
<%--<embed src="../images/ForElise.mp3" hidden="true" autostart="true" loop="true" type="audio/x-wav" width="0" height="0">--%>
<div style="background: rgb(247, 247, 247);width: 50%;float:right;margin-top: 50px;">
<div class="container" style="color:#34538b;">
  <%--<script type="text/javascript" src="http://www.sewise.com/demos/source/player/sewise.player.min.js?server=live&type=rtmp&streamurl=rtmp://139.196.198.137/myapp/hubei&autostart=true&buffer=5&lang=zh_CN&logo=http://onvod.sewise.com/libs/swfplayer/skin/images/logo.png&poster=http://jackzhang1204.github.io/materials/poster.png&title=湖北&skin=liveWhite"></script>--%>
<%--  <div class="cont-top clearfix">
    <div class="top1">正在直播</div>
    <div class="top2"></div>
    <div class="top4">
      安全监控
    </div>
  </div>--%>

  <div id="container_wrapper" style="position: relative; width: 100%; height: 540px;">
    <object type="application/x-shockwave-flash" data="http://139.196.198.137:8080/jwplayer/jwplayer.flash.swf"
            width="100%" height="100%" bgcolor="#000000" id="container" name="container" tabindex="0">
      <param name="allowfullscreen" value="true">
      <param name="allowscriptaccess" value="always">
      <param name="seamlesstabbing" value="true">
      <param name="wmode" value="opaque">
    </object>
    <div id="container_jwpsrv" style="position: absolute; top: 0px; z-index: 10;">
      <div class="afs_ads" style="width: 1px; height: 1px; position: absolute; background: transparent;">
        &nbsp;</div>
    </div>
  </div>


</div>
  </div>
<%--<div class="footer">
  <div class="footer-cont">
    <span class="sp1">主办：湖北省教育厅&nbsp;&nbsp;&nbsp;&nbsp;/</span>
    <span class="sp1">技术支持：上海复兰信息科技有限公司&nbsp;&nbsp;&nbsp;&nbsp;/</span>
    <span class="sp1">客服热线：400 820 6735</span>
    &lt;%&ndash;<a class="sp2">版主中心</a>&ndash;%&gt;
    &lt;%&ndash;<a class="sp2">站长管理&nbsp;|</a>&ndash;%&gt;
    &lt;%&ndash;<a class="sp2">后台管理&nbsp;|</a>&ndash;%&gt;
  </div>--%>
</div>
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
</body>
</html>
