<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>我的首页-复兰科技 K6KT</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link href="/static_new/js/modules/core/0.1.0/fancyBox/jquery.fancybox.css?v=2015041602" rel="stylesheet" type="text/css" media="screen"/>
  <link rel="stylesheet" type="text/css" href="/static/plugins/uploadify/uploadify.css"/>
  <!-- jquery artZoom4Liaoba styles -->
  <!-- Custom styles -->
  <link href="/static_new/css/homepage/shouye.css?v=2015041602" rel="stylesheet" />
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
  <script type="text/javascript" src="/static/js/WdatePicker.js"></script>
</head>
<body>
<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<div id="YCourse_player" class="player-container">
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
  $(function () {
    if (getQueryString('index')==6) {
      $('.title').html("微校/家园");
      if(${roles:isStudentOrParent(sessionValue.userRole)}){
        $('.tab-main').hide();
        $("#mybloginfo").css({
          "display": 'none'
        });
      } else {
        $('.tab-main').show();
        $("#mybloginfo").css({
          "display": 'block'
        });
      }
      $('#left-nav-version91').css('color', 'red');

    } else if (getQueryString('index')==7) {
      $('.title').html("微家园");
      if(${roles:isStudentOrParent(sessionValue.userRole)} || contains('${sessionValue.userName}','小助手',true)){
        $('.tab-main').show();
        $("#mybloginfo").css({
          "display": 'block'
        });
      } else {
        $('.tab-main').hide();
        $("#mybloginfo").css({
          "display": 'none'
        });
      }
      $('#left-nav-version92').css('color', 'red');
    }
  });
  function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
  }

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
  var isFlash = false;
  function getVideoType(url) {
    if (url.indexOf('polyv.net') > -1) {
      return "POLYV";
    }
    if (url.endWith('.swf')) {
      return 'FLASH';
    }
    return 'HLS';
  }

  function tryPlayYCourse(url) {
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
        SewisePlayer.toPlay(url, "微校园微家园", 0, true);
      } catch (e) {
        playerReady.videoURL = url;
        isFlash = true;
      }
    }
  }
  function playerReady(name) {
    if (isFlash) {
      SewisePlayer.toPlay(playerReady.videoURL, "微校园微家园", 0, false);
    }
  }
</script>
<!--/#head-->
<!--#content-->
<div id="content" class="clearfix">
  <!--.col-left-->
  <%@ include file="../common_new/col-left.jsp" %>
  <!--/.col-left-->
  <!--广告-->
  <c:choose>
    <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
      <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
    </c:when>
    <c:otherwise>
      <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
    </c:otherwise>
  </c:choose>
  <!--.col-right-->
  <div class="col-right">
    <!--.banner-info-->
    <%--<img src="http://placehold.it/835x100" class="banner-info" />--%>
    <!--/.banner-info-->
    <!-- 右侧上半部 -->
    <div class="coottop clearfix">
      <div class="tab-col">
        <div class="tab-head clearfix">
          <ul>
            <li class="cur"><a href="javascript:;" class="title">微校/家园</a></li>
          </ul>
        </div>
        <div id="tab_Main">
          <div class="tab-main">
            <button class="talk" id="ZI">
              收到的评论
              <em class="tab-deit" style="display: none;"></em>
            </button>
            <textarea  id="micoblog_content" placeholder="说点什么吧..." class="phd"></textarea>
            <div class="upload-info clearfix">
              <label id="upload-img" for="image-upload" style="cursor:pointer;">
                <img src="/static_new/images/icontu.jpg" class="upload-blog-image" alt="" title="上传图片">
                <span class="upload-image">图片</span>
              </label>

              <div class="size-zero">
                <input type="file" name="image-upload" id="image-upload" accept="image/*" multiple="multiple"/>
              </div>
              <img src="/img/loading4.gif" id="picuploadLoading"/>
                            <span class="iconvideo">
                            视频
                              <input type="file" name="file_upload" id="file_upload"/>
                            </span>

                            <span class="HUA">
                                <span class="iconhua">话题</span>
                                <!-- 话题 -->
                                <div class="huati">
                                  <div class="sjt1"></div>
                                  <div class="sjt2"></div>
                                  <i class="i_hua"></i>
                                  <ul class="themeUl2">
                                  </ul>
                                  <script type="text/template" id="themeUl_templ2">
                                    <li class="hot-huati">热门话题</li>
                                    {{ if(it.rows.length>0){ }}
                                    {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                                    {{var obj=it.rows[i];}}
                                    <a class="themeitem2"><li>{{=obj.themedsc}}</li></a>
                                    {{ } }}
                                    {{ } }}
                                  </script>
                                </div>
                            </span>
              <!-- 话题 -->

              <button type="button" class="orange-btn">提交</button>
              <%--<select id="sendtype">--%>
              <%--<option value="1">公开</option>--%>
              <%--<option value="3">本班</option>--%>
              <%--<option value="2">本年级</option>--%>
              <%--</select>--%>
              <select id="schooltype" style="width: 60px;">
                <option value="">全校</option>
                <c:forEach items="${schools}" var="schoolinfo">
                 <option value="${schoolinfo.schoolID}">${schoolinfo.userName} ${schoolinfo.schoolName}</option>
                </c:forEach>
              </select>
              <select id="blogtp2">
                <option value="1">微校园</option>
                <option value="2">微家园</option>
              </select>
              <c:if test="${roles:isHeadmaster(sessionValue.userRole)|| roles:isManager(sessionValue.userRole)}">
                <label for="zhu" class="homepage-zuti"> 主题帖</label>
                <input type="checkbox" id="theme">
              </c:if>
            </div>
            <!-- 图片的位置 -->
            <div class="kongbai clearfix" id="img-container">
              <ul class="clearfix">
                <!-- 需要隐藏 -->
                <li class="ixxe">
                  <%--<span class="ix">--%>
                  <%--<a class="" href="javascript:;" data-fancybox-group="home" title="预览">--%>
                  <%--<img src="" id="pic">--%>
                  <%--</a>--%>
                  <%--<i class="ixh"></i>--%>
                  <%--</span>--%>
                </li>
                <!-- 需要隐藏 -->
              </ul>
            </div>
            <!-- 图片的位置 -->
          </div>
          <div>
            <input type="checkbox" class="allcheck" name="allcheck">全选
            <button class="delblog">删除</button>
            <button class="iszhu">取消主题帖</button>
            <select id="choose1">
              <option value="0">全部</option>
              <option value="1">图片</option>
              <option value="2">视频</option>
              <option value="3">文本</option>
            </select>
            <select id="choose2">
              <option value="0">全部微博</option>
              <option value="1">我的点赞</option>
            </select>
          </div>
          <div>
            <select id="blogtp">
              <option value="1">微校园</option>
              <option value="2">微家园</option>
            </select>
            开始时间：
            <input type="text" id="bTime" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" style="width: 117px;">
            结束时间：
            <input type="text" id="eTime" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" style="width: 117px;">
            <select id="phone" class="select-phone">
              <option value="0" selected="selected">全部</option>
              <option value="1">PC</option>
              <option value="2">Android</option>
              <option value="3">IOS</option>
            </select>
            <select id="schoollist" class="select-schoollist" style="width: 120px;">
              <option value="" selected="selected">全部学校</option>
              <c:forEach items="${schools}" var="schoolinfo">
                <option value="${schoolinfo.schoolID}">${schoolinfo.userName} ${schoolinfo.schoolName}</option>
              </c:forEach>
            </select>
            关键字：
            <input id="keyword" style="width: 117px;" value="">
            <select id="btp">
              <option value="0">全部</option>
              <option value="1">已收藏</option>
              <option value="2">已推送</option>
              <option value="3">推送记录</option>
            </select>
            <%--onfocus="if(this.value == '搜索关键字') this.value = ''" onblur="if(this.value =='') this.value = '搜索关键字'"--%>
            <button id="seachindex">检索</button>
            <button id="download">下载</button>
          </div>
          <div class="tab-list clearfix">
            <!--.info-head-->
            <div class="info-head clearfix">
              <ul>
                <li class="order-blue active" index="1">最新</li>
                <li class="order-blue" index="2">最热</li>
                <li class="order-blue" index="3" id="mybloginfo">我的帖子</li>
              </ul>
              <%--<select id="order" class="select-order">--%>
              <%--<option value="1" selected="selected">查看全校</option>--%>
              <%--<option value="2">查看本年级</option>--%>
              <%--<option value="3">查看本班</option>--%>
              <%--</select>--%>
            </div>
            <!--/.info-head-->
            <!--.list-info-->
            <div class="hwk1">
            </div>

            <script type="text/template" id="hwk1_templ">
              {{ if(it.rows.length>0){ }}
              {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
              {{var obj=it.rows[i];}}
              <div class="list-info clearfix" mid="{{=obj.id}}">
                <input type="checkbox" class="checkitem" name="checkitem">
                <img src="{{=obj.userimage}}?imageView/1/h/65/w/65" class="user-img" />
                <div class="list-txt">
                  <h4>{{=obj.username}}
                    <em>{{=obj.roleDescription}}</em>
                    <em>{{=obj.schoolname}}</em>
                    {{? obj.top==1}}
                    <span  style="color: red;">主题帖</span>
                    {{?}}</h4>
                  <p>{{=obj.blogcontent}}
                    <em id="gradient"></em>
                  </p>

                  <a id="read-more"></a>
                  <ul class="clearfix">
                    {{~obj.videoFileDTOs:value:index}}
                    <a><li><img class="content-img videoshow2" vurl="{{=value.videoUrl}}" src="{{?value.imageUrl}}{{=value.imageUrl}}?imageView/1/h/80/w/80{{??}}/img/K6KT/video-cover.png{{?}}"><img src="/img/play.png" class="video-play-btn" onclick="tryPlayYCourse('{{=value.videoUrl}}')"></li></a>
                    {{~}}
                    {{~obj.filenameAry:value:index}}
                    {{ if(value!=''){ }}
                    <a class="fancybox" href="{{=value}}" data-fancybox-group="home" title="预览"><li><img class="content-img" title="点击查看大图" src="{{=value}}?imageView/1/h/80/w/80"></li></a>
                    {{}}}
                    {{~}}
                  </ul>
                  <div class="date-txt clearfix">
                    <span>{{=obj.timedes}}</span>
                    <span>来自{{=obj.clienttype}}</span>
                    <%--<span>最新评论于{{=value.long2}}天前</span>--%>
                    <a href="javascript:;" class="mox" id="{{=obj.id}}">评论(<span class="cmt">{{=obj.mreply}}</span>)</a>
                    <a href="javascript:;">|</a>
                    <a href="javascript:;" class="zan zan_cls" id="{{=obj.id}}" {{? obj.iszan==1}}style="color:red;"{{?}}>
                      <img id="{{=obj.id}}" zid="{{=obj.iszan}}" src="{{? obj.iszan==0}}/static_new/images/zan_1.png{{??}}/static_new/images/zan_2.jpg{{?}}" height="15" width="14" alt="">（<em>{{=obj.zancount}}</em>）
                    </a>
                    {{? obj.isdelete==1}}
                    <a href="javascript:;">
                      <%--<img  id="{{=obj.id}}" src="/static_new/images/icon-del.png" height="15" width="14" alt="" class="shoucang_cls" title="收藏" style="margin-right: 5px;">--%>
                      <span id="{{=obj.id}}" class="shoucang_cls">收藏</span>
                        <img  id="{{=obj.id}}" src="/static_new/images/icon-del.png" height="15" width="14" alt="" class="del_cls" title="删除此贴">

                    </a>
                    {{?}}
                  </div>
                  <div class="mo">
                    <div class="s1"></div>
                    <div class="s2"></div>
                    <textarea class="content-reply"></textarea>
                    <button mid="{{=obj.id}}" uid="{{=obj.userid}}" class="reply">评论</button>
                    <div class="hwk3">

                    </div>
                  </div>
                </div>

              </div>
              {{ } }}
              {{ } }}
            </script>

            <!--/.list-info-->
            <!--.page-links-->
            <%--<div class="page-links clearfix">--%>
            <%--<span>1</span>--%>
            <%--<a href="javascript:;">2</a>--%>
            <%--<a href="javascript:;">3</a>--%>
            <%--<a href="javascript:;">末页4</a>--%>
            <%--</div>--%>
            <div class="page-paginator">
              <span class="first-page">首页</span>
                        <span class="page-index">
                            <span class="active">1</span>
                            <span>2</span>
                            <span>3</span>
                            <span>4</span>
                            <span>5</span>
                            <i>···</i>
                        </span>
              <span class="last-page">尾页</span>
              <input style="width: 40px;" id="yema">
              <button id="tiaozhuan">跳转</button>
            </div>
            <!--/.page-links-->
          </div>
        </div>
        <script type="text/template" id="hwk3_templ">
          /*有评论的结构*/
          {{ if(it.rows.length>0){ }}
          {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
          {{var obj=it.rows[i];}}
          <div class="p_l">
            <dl class="clearfix">
              <dt><img src="{{=obj.userimage}}?imageView/1/h/65/w/65" alt=""></dt>
              <dd>
                <a href="javascript:;">{{=obj.username}}:{{? obj.replytype==3}}@{{=obj.busername}}:{{?}}</a>
                <p>{{=obj.blogcontent}}</p>
                <span>（{{=obj.timedes}}）</span>
              </dd>
            </dl>
            <div class="er_l">
              {{? obj.isdelete==1}}
              <a href="javascript:;" commentid="{{=obj.id}}" blogid="{{=obj.replyid}}" class="del_reply">
                <img src="/static_new/images/icon-del.png" height="15" width="14" alt="">
              </a>
              {{?}}
              <a href="javascript:;" class="zan zan_cls2" id="{{=obj.id}}">
                <img id="{{=obj.id}}" zid="{{=obj.iszan}}" src="{{? obj.iszan==0}}/static_new/images/zan_1.png{{??}}/static_new/images/zan_2.jpg{{?}}" height="15" width="14" alt="">
                （<em>{{=obj.zancount}}</em>）
              </a>
              <a href="javascript:;">|</a>
              <a href="javascript:;" class="hu_f">回复</a>
              <div class="moreply clearfix">
                <div class="s1"></div>
                <div class="s2"></div>
                <textarea placeholder="回复@~{{=obj.username}}~:" class="content-reply2"></textarea>
                <button commentid="{{=obj.id}}" blogid="{{=obj.replyid}}" uid="{{=obj.userid}}" class="reply2">评论</button>
              </div>
            </div>
          </div>
          {{ } }}
          {{ } }}
          /*有评论的结构*/
        </script>
        <!-- 评论 -->
        <div class="pinglun clearfix" id="PL">
          <p>我的评论</p>
          <button class="talk" id="FH">返回</button>
          <dl>
            <input type="text" id="bTime2" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})">
            <input type="text" id="eTime2" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})">
            <button class="seachReply" style="position: inherit;">检索</button>
          </dl>
          <dl class="clearfix hwk2">

          </dl>

          <!--.page-links-->
          <div class="page-paginator2">
            <span class="first-page2">首页</span>
                        <span class="page-index2">
                            <span class="active">1</span>
                            <span>2</span>
                            <span>3</span>
                            <span>4</span>
                            <span>5</span>
                            <i>···</i>
                        </span>
            <span class="last-page2">尾页</span>

          </div>

          <!--/.page-links-->
        </div>
        <!-- 评论 -->
      </div>
      <script type="text/template" id="hwk2_templ">
        {{ if(it.rows.length>0){ }}
        {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
        {{var obj=it.rows[i];}}
        <div class="clearfix bomt">

          <dt>
            <img src="{{=obj.userimage}}?imageView/1/h/65/w/65" alt="">
          </dt>
          <dd class="clearfix">
            <div class="zuojian"></div>
            <div class="zuojian1"></div>
            <a href="javascript:;"class="lanname">{{=obj.username}}|{{=obj.roleDescription}}|{{=obj.schoolname}}</a>
            <span>:</span>
            <div class="neirong">
              {{=obj.blogcontent}}
            </div>
            <div class="huifu">
              评论我的微博 :
              "<a href="javascript:;">{{=obj.replycontent}}</a>"
            </div>
            <div class="xn">
              {{=obj.createtime}}
            </div>
            <div class="sh">
              <a class="mycomment-delete" commentid="{{=obj.id}}" blogid="{{=obj.replyid}}">删除</a>&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="huifu1-1">回复</a>
            </div>

            /*回复*/
            <div class="huifu1 clearfix">
              <textarea placeholder="回复@{{=obj.username}}" class="content-reply3"></textarea>
              <button commentid="{{=obj.id}}" blogid="{{=obj.replyid}}" uid="{{=obj.userid}}" class="reply3">评论</button>
            </div>
            /*回复*/


          </dd>

        </div>
        {{ } }}
        {{ } }}
      </script>
      <div class="big-tu">
        <%--<a href="javascript:;">--%>
        <%--<img src="http://placehold.it/220x206" alt="">--%>
        <%--</a>--%>
        <!--.orange-col-->
        <%--<div class="orange-col">--%>
        <%--<div class="col-head">--%>
        <%--<h3>同学排行</h3>--%>
        <%--</div>--%>
        <%--<ul class="col-main paihan">--%>
        <%--<li class="clearfix">--%>
        <%--<img src="http://placehold.it/45x45" />--%>
        <%--<span>xinxin</span>--%>
        <%--<em>经验值<i>238</i></em>--%>
        <%--</li>--%>
        <%--<li class="clearfix">--%>
        <%--<img src="http://placehold.it/45x45 " />--%>
        <%--<span>xinxin</span>--%>
        <%--<em>经验值<i>238</i></em>--%>
        <%--</li>--%>
        <%--</ul>--%>
        <%--</div>--%>
        <!--/.orange-col-->
        <!-- 热门话题 -->
        <%--<div class="hottalk">--%>
          <%--<p>--%>
            <%--<span class="hot">热门话题</span>--%>
            <%--&lt;%&ndash;<span class="change">换一换</span>&ndash;%&gt;--%>
          <%--</p>--%>
          <%--<ul class="themeUl">--%>
          <%--</ul>--%>
          <%--<script type="text/template" id="themeUl_templ">--%>
            <%--{{ if(it.rows.length>0){ }}--%>
            <%--{{ for (var i = 0, l = it.rows.length; i < l; i++) { }}--%>
            <%--{{var obj=it.rows[i];}}--%>
            <%--<a><li class="themeitem" cid="{{=obj.id}}" con="{{=obj.themedsc}}">{{=obj.themedsc}}<span>{{=obj.count}}</span></li></a>--%>
            <%--{{ } }}--%>
            <%--{{ } }}--%>
          <%--</script>--%>

          <%--&lt;%&ndash;<div class="more"><a href="javascript:;">查看更多>></a></div>&ndash;%&gt;--%>

        <%--</div>--%>
        <!-- 热门话题 -->
      </div>
    </div>
    <!-- 右侧上半部 -->


    <div class="bg"></div>


  </div>
  <!--/.info-main-->


</div>
<!--/.info-list-->

<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->



<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('yunying');
</script>

</body>
</html>