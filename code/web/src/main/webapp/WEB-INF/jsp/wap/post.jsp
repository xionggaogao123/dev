<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>分享点赞</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <!--[if IE]>
    <link type="text/css" rel="stylesheet" href="/wap/css/detail01.css">
    <link href="/static/js/modules/core/0.1.0/fancyBox/jquery.fancybox.css?v=2015041602" rel="stylesheet"
          type="text/css" media="screen">
    <![endif]-->
    <link type="text/css" rel="stylesheet" href="/wap/css/detail03.css" media="only screen and (max-width:1920px)"/>
    <!-- <link type="text/css" rel="stylesheet" href="css/detail01.css" media="only screen and (max-width:1080px)" />  -->
    <link type="text/css" rel="stylesheet" href="/wap/css/detail02.css" media="only screen and (max-width:750px)"/>
    <script type="text/javascript" src="/wap/js/jquery-1.11.1.js"></script>
    <script type="text/javascript" src="/wap/js/post.js"></script>
    <script type="text/javascript" src="/wap/js/TouchSlide.1.1.js"></script>
    <script src="http://g.tbcdn.cn/mtb/lib-flexible/0.3.4/??flexible_css.js,flexible.js"></script>
    <script type="text/javascript" src="/static/plugins/fancyBox/jquery.fancybox.pack.js"></script>
</head>
<body>
<input type="hidden" id="postId" value="${postId}">
<input type="hidden" id="replyId" value="${replyId}">
<div class="container">
    <div id="postContent" style="padding:0 6px">
    </div>
    <div class="gray1"></div>
    <p class="post-hfs"><span id="count"></span>条回复</p>
    <div id="hsCont">
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
        if (url.indexOf('.swf') > 0) {
            return 'FLASH';
        }
        return 'HLS';
    }

    function playerReady(name) {
        if (isFlash) {
            SewisePlayer.toPlay(playerReady.videoURL, "视频", 0, false);
        }
    }

    $('.player-close-btn').click(function () {
        $('#YCourse_player').hide();
        $(".bg").hide();
    });
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
<script id="detailTml" type="text/template">
    {{~it:value:index}}
    <div class="holder-info clearfix">
        {{?value.avt!=null}}
        <img src="{{=value.avt}}">
        {{?}}
        {{?value.avt==null}}
        <img src="/static/images/forum/head_picture1.png">
        {{?}}

        <p class="hp1">
            <span id="userName">{{=value.personName}}</span><br>
            <em id="postTheme">主题:{{=value.postTitle}}</em><br>
            <em id="replyCount">帖子:{{=value.rc}}</em>
            <em id="score">积分:{{=value.score}}</em>
        </p>
    </div>

    {{?value.comment!=null&&value.comment!=""}}
    {{?value.version!=null&&value.version!=""}}
    {{=value.version}}
    {{?}}
    {{?value.version==null||value.version==""}}
    {{=value.content}}
    {{?}}
    {{?}}

    {{?value.comment==null||value.comment==""}}
    {{~value.videoList:video:i}}
    <video src="{{=video.videoUrl}}" width="82" height="82" controls>
        <source src=\"movie.mp4\" type=\"video/mp4\"/>
        <source src="movie.ogg" type="video/ogg"/>
    </video>
    {{~ }}
    <div>
        {{=value.plainText}}
    </div>
    {{~value.imageList:images:i}}
    <img src="{{=images}}"><br/>
    {{~ }}
    {{?}}


    <div class="title-zan">点赞数排名</div>
    <div class="zan-list">
        <div class="zan-f">
            {{~value.fReplyDTOList:fReplyDTO:i}}
            <div class="zan-li">
                {{?fReplyDTO.remove!=1}}
                {{?fReplyDTO.imageSrc!=null&&fReplyDTO.imageSrc !='http://7xiclj.com1.z0.glb.clouddn.com/'}}
                <img src="{{=fReplyDTO.imageSrc}}">
                {{?}}
                {{?fReplyDTO.imageSrc==null||fReplyDTO.imageSrc =='http://7xiclj.com1.z0.glb.clouddn.com/'}}
                <img src="/static/images/forum/head_picture1.png">
                {{?}}
                <p>{{=fReplyDTO.replyNickName}}</p>
                <p><img src="/wap/images/red_heart.png">{{=fReplyDTO.praiseCount}}</p>
                {{?}}
            </div>
            {{~ }}

        </div>
    </div>
    {{?value.IsLogin==1}}
    <p class="zan-my">我的点赞数：<img src="{{=value.avt}}">{{=value.praiseCount}}</p>
    {{?}}
    {{~}}
</script>

<script id="hsContTml" type="text/template">
    {{~it:value:index}}
    <div class="hf-cont">
        {{?value.imageSrc!=null}}
        <img src="{{=value.imageSrc}}" class="hf-pic">
        {{?}}
        {{?value.imageSrc!=null}}
        <img src="/static/images/forum/head_picture1.png" class="hf-pic">
        {{?}}
        <p class="p1">{{=value.replyNickName}}<em>{{=value.floor}}楼</em></p>
        <p class="p2">{{=value.plainText}}</p>
        <p class="p2 clearfix">
            {{~value.imageList:images:i}}
            <span><img src="{{=images}}" class="ss" index="i">
            </span>
            {{~}}
            {{~value.videoList:video:i}}
        <div class="vid-wrappper">
            <video src="{{=video.videoUrl}}" poster="{{=video.imageUrl}}" width="92" height="92" controls>
                <source src=\"movie.mp4\" type=\"video/mp4\"/>
                <source src="movie.ogg" type="video/ogg"/>
            </video>
        </div>
        {{~}}
        </p>
        <p class="p3">
            {{?value.isZan==1}}
            <span class="sp1"><img src="/wap/images/red_heart.png" class='redzan'>({{=value.praiseCount}})</span>
            {{?}}
            {{?value.isZan==0}}
            <span class="sp1"><img src="/wap/images/post_white_heart.png" class='redzan'
                                   id="dianzan"><span>(</span><span id="din">{{=value.praiseCount}}</span><span>)</span></span>
            {{?}}
            <span class="sp2"><img src="/wap/images/post_recall.png" id="comment">({{=value.replyToCount}})</span>
        </p>
    </div>
    <div class="check-more click-download">查看更多评论</div>
    <div class="div-ma">
        <img src="/static/images/forum/forum_app.png" style="width: 100%">
    </div>
    <div class="clickdown click-download">点击下载APP</div>
    <div class="zan-cont-fixed">
        <div class="clickzan"><img src="/static/images/forum/orange_xin_no.png" class="img1" hidden="hidden"><img
                src="/static/images/forum/orange_xin_ok.png" class="img2"><span
                id="praise">({{=value.praiseCount}})</span><span>帮他点赞</span></div>
    </div>
    {{~}}
</script>

<script src="/static/js/sea.js"></script>
<!-- Custom js -->
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/forum/appShare.js', function (appShare) {
        appShare.init();
    });
</script>
</body>
</html> 