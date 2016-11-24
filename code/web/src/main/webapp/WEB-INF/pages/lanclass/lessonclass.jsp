<%--
  Created by IntelliJ IDEA.
  User: wang_xinxin
  Date: 2015/11/27
  Time: 17:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.fulaan.base.controller.BaseController" %>
<%@ page import="com.pojo.user.UserRole" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>


<%@page import="com.pojo.app.SessionValue" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>复兰科技-互动课堂</title>
    <link href="/static_new/css/reset.css" rel="stylesheet"/>
    <link href="/static_new/css/lanclass/lanclass.css" rel="stylesheet">
    <link href="/static_new/js/modules/core/0.1.0/fancyBox/jquery.fancybox.css?v=2015041602" rel="stylesheet"
          type="text/css" media="screen"/>
    <meta charset="utf-8">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="application/javascript">
        var polyv_player;
        var video_thumb_list;
        var current_video = -1;

        var watch_timer = null;

        var watchedTime, videoLength;
        var videoId;
        var courseid = "";
        var isFlash = false;
        function playVideo(index) {
            if (index >= video_thumb_list.length || index < 0) {
                return;
            }
            s2j_onPlayOver.disabled = true;
            $('.video-thumb-list > div').removeClass('active');
            current_video = index;
            var dom = video_thumb_list[index];
            var videoURL = dom.getAttribute('url');
            var videoName = dom.getAttribute('name');

            var videoType = getVideoType(videoURL);
            var uploading = dom.getAttribute('state') == 0;

            $('#player-div').empty();
            try {
                SewisePlayer.doStop();
            } catch (e) {
            }

            switch (videoType) {
                case "FLASH":
                    $(".video-player-container").hide();
                    $('#player-div').show();
                    $("#player-div").html('<div id="swfobject_replace"></div>');
                    swfobject.embedSWF(videoURL, "swfobject_replace", "600", "405", "9.0.0");
                    break;
                case "POLYV":
                    $(".video-player-container").hide();
                    $('#player-div').show();
                    var _start = videoURL.lastIndexOf('/') + 1, _end = videoURL.lastIndexOf('.');
                    var polyv_vid = videoURL.substring(_start, _end);
                    var player = polyvObject('#player-div').videoPlayer({
                        'width': '600',
                        'height': '405',
                        'vid': polyv_vid
                    });
                    break;
                case "HLS":
                    $(".video-player-container").hide();
                    if (uploading) {
                        $('#converting-img').show();
                    } else {
                        $('#sewise-div').show();
                        try {
                            SewisePlayer.toPlay(videoURL, videoName, 0, true);
                        } catch (e) {
                            playerReady.videoURL = videoURL;
                            playerReady.videoName = videoName;
                            isFlash = true;
                        }
                    }
                    break;
            }

            $(dom).parent().addClass('active');

            videoId = dom.getAttribute('video-id');
            videoLength = parseInt(dom.getAttribute('video-length')) || 0;

            switch (videoType) {
                case "FLASH":
                    if (bowser.ios || bowser.android && typeof swfobject == 'undefined') {
                        var html = '<div class="player-prompt">您的系统不支持Flash，请使用PC观看。</div>';
                        $('#player-div').html(html);
                    } else {
                        $('#player-div').css('background', 'white');
                        clearTimeout(watch_timer);
                        watch_timer = setTimeout(function () {
                            $.ajax({
                                url: '/experience/studentScoreLog.do',
                                type: 'POST',
                                dataType: 'json',
                                async: false,
                                data: {
                                    'lessonId': courseid,
                                    'relateId': videoId,
                                    'scoretype': 1
                                },
                                success: function (data) {
                                    if (data.resultcode == 0) {
                                        scoreManager(data.desc, data.score);
                                    }
                                },
                                error: function () {
                                }
                            });

                            $.getJSON("/video/viewrecord/add.do",
                                    {
                                        'lessonId': courseid,
                                        'videoId': videoId,
                                        'viewType': 1
                                    },
                                    function (data) {
                                    });

                        }, 1000);
                    }
                    break;
                case "POLYV":
                    $('#player-div').css('background', 'black');
                    if (bowser.ios || bowser.android && typeof swfobject == 'undefined') {
                        var video = $('#player-div video')[0];
                        if (typeof video != "undefined") {
                            video.addEventListener('play', s2j_onPlayStart);
                            video.addEventListener("pause", s2j_onVideoPause);
                            video.addEventListener('ended', s2j_onPlayOver);
                            video.play();
                        }
                    }
                case "HLS":
                    if (!uploading) {
                        watchedTime = 0;
                        $.getJSON("/video/viewrecord/add.do",
                                {
                                    'lessonId': courseid,
                                    'videoId': videoId,
                                    'viewType': 0
                                },
                                function (data) {
                                });
                    }
            }
            s2j_onPlayOver.disabled = false;
        }

        function playerReady(name) {
            if (isFlash) {
                SewisePlayer.toPlay(playerReady.videoURL, playerReady.videoName, 0, false);
                //SewisePlayer.pause();
            }
        }

        function getVideoType(url) {
            if (url.indexOf('polyv.net') > -1) {
                return "POLYV";
            }
            if (url.endWith('.swf')) {
                return 'FLASH';
            }
            return 'HLS';
        }


        function s2j_onPlayOver() {
            if (s2j_onPlayOver.disabled) {
                return;
            }
            playVideo(current_video + 1);
            clearInterval(watch_timer);
        }

        function s2j_onPlayStart() {
            clearInterval(watch_timer);
            watch_timer = setInterval(watchTimeCheck, 1000);
        }

        function s2j_onVideoPause() {
            clearInterval(watch_timer);
        }

        var onStop = s2j_onPlayOver;
        var onStart = s2j_onPlayStart;
        var onPause = s2j_onVideoPause;

        function watchTimeCheck() {
            watchedTime++;

            if (watchedTime > videoLength / 1000 * 0.8) {
                clearInterval(watch_timer);
                watchedTime = 0;
                $.getJSON("/video/viewrecord/add.do",
                        {
                            'lessonId': courseid,
                            'videoId': videoId,
                            'viewType': 1
                        },
                        function (data) {
                        });
                $.getJSON("/experience/studentScoreLog.do",
                        {
                            'lessonId': courseid,
                            'relateId': videoId,
                            'scoretype': 1
                        },
                        function (data) {
                            if (data.resultcode == 0) {
                                scoreManager(data.desc, data.score);
                            }
                        });
            }
        }


                $(function () {
                    $(".homepage-right-top").css('display', 'none');
                })
        $(function(){
            $(".sta-hov").click(function(){
                $(".interactive-xuesheng").show();
                $(".L-interactive").empty();
            })
        })
    </script>
</head>

<body>
<!--#head-->
<!--=================================引入头部=======i=====================================-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

    <!--.col-left-->
    <%@ include file="../common_new/col-left.jsp" %>
    <!--/.col-left-->

    <!--.col-right-->
    <!--广告-->
    <c:choose>
        <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
        </c:when>
        <c:otherwise>
            <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
        </c:otherwise>
    </c:choose>

    <div class="col-right">
        <input type="hidden" id="lessonid" value="${ilid}">
        <input type="hidden" id="classid" value="${classId}">
        <!--***********************互动课堂内容*************************-->
        <div class="L-interactive">
            <dl>
                <dt>
                    <span>${name}</span>
                </dt>
                <dd>
                    <!--===================视频位置===================-->
                    <%--<div>--%>
                    <%--<img src="http://placehold.it/770x350" class="banner-info" />--%>
                    <%--</div>--%>
                    <div id="videoplayer-div">
                        <div id='player-div' class="video-player-container"></div>
                        <div id='sewise-div' class="video-player-container">
                            <script type="text/javascript"
                                    src="/static/plugins/sewiseplayer/sewise.player.min.js"></script>
                        </div>
                        <img id="converting-img" src="/img/converting_big.png" class="video-player-container"/>
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

                        <div class="video-thumb-list">
                            <c:forEach items="${videoList}" var="video" varStatus="loop">
                                <div>
                                    <div class="indicator"><i class="fa fa-play"></i></div>
                                    <div class="video-thumb" name="${video.type}" url="${video.value1}"
                                         video-length="${video.length}" video-id="${video.id}"
                                         state="${video.uploadState}"
                                         onclick="playVideo(${loop.index})">
                                        <img src="${empty video.value? '/img/K6KT/video-cover.png': video.value}"
                                             onerror="coverError(this);"/>

                                        <div class="video-name">${video.type}</div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </dd>
                <dd>
                    <div class="tab-sta">
                        <div class="sta-top">
                            <ul>
                                <li class="sta-cur" id="xingwei">
                                    <a href="javascript:;">行为统计</a>
                                </li>
                                <li id="kaoshi">
                                    <a href="javascript:;">练习统计</a>
                                </li>
                                <li id="dati">
                                    <a href="javascript:;">答题统计</a>
                                </li>
                                <li id="ketang">
                                    <a href="javascript:;">课件统计</a>
                                </li>
                                <li id="xuesheng">
                                    <a href="javascript:;">学生上传</a>
                                </li>
                            </ul>
                        </div>
                        <div id="contentt">
                            <!-==================当没有数据时显示===================-->
                            <div class="lanclass-hov">
                                <img src="/static_new/images/lanclass/lanclass-TT.png">
                            </div>
                            <!--================行为统计=================-->
                            <div class="sta-xingwei" id="sta-xingwei">
                                <dl>
                                    <dt>
                                        <span>活跃度统计</span>
                                        <i>*学生每参与一次互动操作，就可以累计一次活跃度</i>
                                    </dt>
                                    <dd class="echarts-pgfId">
                                        <span id="xwtj">

                                        </span>
                                        <div class="sta-hov"></div>
                                    </dd>
                                    <div id="sta-QD">
                                        <dt>
                                            <span>抢答统计</span>
                                        </dt>
                                        <dd>
                                            <table class="questiontotal">
                                            </table>
                                            <script type="text/template" id="questiontotal_templ">
                                                <tr>
                                                    <th>姓名</th>
                                                    <th>举手次数</th>
                                                    <th>回答次数</th>
                                                </tr>
                                                {{ if(it.length>0){ }}
                                                {{ for (var i = 0, l = it.length; i < l; i++) { }}
                                                {{var obj=it[i];}}
                                                <tr>
                                                    <td>{{=obj.stuName}}</td>
                                                    <td>{{=obj.answerCount}}</td>
                                                    <td>{{=obj.raceCount}}</td>
                                                </tr>
                                                {{ } }}
                                                {{ } }}
                                            </script>
                                        </dd>
                                    </div>
                                </dl>
                            </div>
                            <!--==================考试统计==================-->
                            <div class="sta-kaoshi" id="sta-kaoshi">
                                <dl>
                                    <dt>
                                        <h1 id="examdec">第一次练习详情</h1>
                                    </dt>
                                    <dd>
                                        <ul>
                                            <li class="kaoshi-cur" id="zjms"><a href="javascript:;">整卷模式</a></li>
                                            <li id="dtms"><a href="javascript:;">单题模式</a></li>
                                        </ul>
                                        <span <c:if test="${examTimesList== null || fnn:length(examTimesList) == 0}"> style="display:none;" </c:if>>
                                            <select id="examlist">
                                                <c:forEach items="${examTimesList}" var="examTimes">
                                                    <option value="${examTimes.key}">${examTimes.value}</option>
                                                </c:forEach>
                                            </select>
                                        </span>
                                        <select id="sigleExamlist" style="display: none;" class="sigleExam">
                                        </select>
                                        <script type="text/template" id="sigleExam_templ">
                                            {{ if(it.length>0){ }}
                                            {{ for (var z = 0, l = it.length; z < l; z++) { }}
                                            {{var obj=it[z];}}
                                            <option value="{{=obj.key}}">{{=obj.value}}</option>
                                            {{ } }}
                                            {{ } }}
                                        </script>
                                    </dd>
                                    <!--=================stat整卷模式==================-->
                                    <dl class="sta-zjms" id="sta-zjms">
                                        <dd>
                                            <span>班级统计</span>
                                            <%--<button>查看试卷</button>--%>
                                        </dd>
                                        <dd class="echarts-bjtj">
                                            <span id="bjtj">

                                            </span>
                                        </dd>
                                        <dd>
                                            <table class="stuexamlist">
                                            </table>
                                            <script type="text/template" id="stuexamlist_templ">
                                                <tr>
                                                    <th>姓名</th>
                                                    <th>正确率</th>
                                                    <th>用时</th>
                                                    {{ if(it.length>0){ }}
                                                    {{ for (var i = 0, l = it[0].examDetailList.length; i < l; i++) { }}
                                                    <th>{{=i+1}}</th>
                                                    {{ } }}
                                                    {{ } }}
                                                </tr>
                                                {{ if(it.length>0){ }}
                                                {{ for (var z = 0, l = it.length; z < l; z++) { }}
                                                {{var obj=it[z];}}
                                                <tr>
                                                    <td>{{=obj.userName}}</td>
                                                    <td><span class="lessonclass-rete">{{=obj.correctRateStr}}</span></td>
                                                    <td><span class="lessonclass-use">{{=obj.useTime}}</span></td>
                                                    {{? obj.examDetailList.length > 0}}
                                                        {{ for (var j = 0, k = obj.examDetailList.length; j < k; j++) { }}
                                                            {{var obj2=obj.examDetailList[j];}}
                                                            {{? obj2.result==1}}
                                                                <td  title="{{=obj2.answerDes}}">
                                                                    <span class="lessonclass-ziti">
                                                                        {{if(obj2.format!='subjective'){}}
                                                                            {{=obj2.answerDes}}
                                                                        {{}else if(obj2.answerFilePath!=''){}}
                                                                            <a class="fancybox" href="{{=obj2.answerFilePath}}" data-fancybox-group="home" title="预览">
                                                                             <img src="/static_new/images/lanclass/stu-answer.png">
                                                                            </a>
                                                                        {{}}}
                                                                    </span>
                                                                </td>
                                                            {{??}}
                                                                <td  title="{{=obj2.answerDes}}">
                                                                    <span class="bjti-error lessonclass-ziti">
                                                                        {{if(obj2.format!='subjective'){}}
                                                                            {{=obj2.answerDes}}
                                                                        {{}else if(obj2.answerFilePath!=''){}}
                                                                            <a class="fancybox" href="{{=obj2.answerFilePath}}" data-fancybox-group="home" title="预览">
                                                                                <img src="/static_new/images/lanclass/stu-answer.png">
                                                                            </a>
                                                                        {{}}}
                                                                    </span>
                                                                </td>
                                                            {{?}}
                                                        {{ } }}
                                                    {{??}}
                                                    {{ for (var n = 0, m = it[0].examDetailList.length; n < m; n++) { }}
                                                        <td>
                                                        </td>
                                                    {{ } }}
                                                    {{?}}
                                                </tr>
                                                {{ } }}
                                                {{ } }}
                                            </script>
                                        </dd>
                                    </dl>
                                    <!--=================end整卷模式==================-->
                                    <!--=================stat单卷模式==================-->
                                    <dl class="sta-dtms" id="sta-dtms">
                                        <dd>
                                            <span id="singlequestion"><%--<em>(正确答案A)</em>--%></span>
                                            <%--<button>查看试卷</button>--%>
                                        </dd>
                                        <dd class="echarts-sqtj">
                                            <span id="djms">

                                            </span>
                                        </dd>
                                        <dd>
                                            <table class="singleAnswer">
                                            </table>
                                            <script type="text/template" id="singleAnswer_templ">
                                                <tr>
                                                    <th>姓名</th>
                                                    <th>答案</th>
                                                </tr>
                                                {{ if(it.length>0){ }}
                                                {{ for (var i = 0, l = it.length; i < l; i++) { }}
                                                {{var obj=it[i];}}
                                                <tr>
                                                    <td>{{=obj.userName}}</td>
                                                    {{? obj.result=='1'}}
                                                        <td>
                                                            {{if(obj.format!='subjective'){}}
                                                            {{=obj.answerDes}}
                                                            {{}else if(obj.answerFilePath!=''){}}
                                                            <a class="fancybox" href="{{=obj.answerFilePath}}" data-fancybox-group="home" title="预览">
                                                                <img src="/static_new/images/lanclass/stu-answer.png">
                                                            </a>
                                                            {{}}}
                                                        </td>
                                                    {{??}}
                                                         <td  class="bjti-error">
                                                             {{if(obj.format!='subjective'){}}
                                                             {{=obj.answerDes}}
                                                             {{}else if(obj.answerFilePath!=''){}}
                                                             <a class="fancybox" href="{{=obj.answerFilePath}}" data-fancybox-group="home" title="预览">
                                                                 <img src="/static_new/images/lanclass/stu-answer.png">
                                                             </a>
                                                             {{}}}
                                                         </td>
                                                    {{?}}

                                                </tr>
                                                {{ } }}
                                                {{ } }}
                                            </script>
                                        </dd>
                                    </dl>
                                    <!--=================end整卷模式==================-->
                                </dl>
                            </div>
                            <!--=====================答题统计======================-->
                            <div class="sta-dati" id="sta-dati">
                                <dl>
                                    <dt>
                                        <h1 id="answer"></h1>
                                    </dt>
                                    <dd <c:if test="${answerTimesList== null || fnn:length(answerTimesList) == 0}"> style="display:none;" </c:if>>
                                        <select id="answertime">
                                            <c:forEach items="${answerTimesList}" var="answerTimes">
                                                <option value="${answerTimes.key}">${answerTimes.value}</option>
                                            </c:forEach>
                                        </select>
                                    </dd>
                                    <div class="dati-data" id="dati-data">
                                        <dl>
                                            <dd>
                                                <span id="answer2"></span>
                                            </dd>
                                            <dd class="echarts-astol">
                                              <span id="awto">

                                              </span>
                                            </dd>
                                            <dd>
                                                <table class="answertotal">
                                                </table>
                                                <script type="text/template" id="answertotal_templ">
                                                    <tr>
                                                        <th>姓名</th>
                                                        <th>答案</th>
                                                        <th>用时</th>
                                                    </tr>
                                                    {{ if(it.length>0){ }}
                                                    {{ for (var i = 0, l = it.length; i < l; i++) { }}
                                                    {{var obj=it[i];}}
                                                    <tr>
                                                        <td>{{=obj.userName}}</td>
                                                        <td>{{=obj.answerDes}}</td>
                                                        <td>{{=obj.useTime}}</td>
                                                    </tr>
                                                    {{ } }}
                                                    {{ } }}
                                                </script>
                                            </dd>
                                        </dl>
                                    </div>
                                    <div class="lanclass-hov-dati">
                                        <img src="/static_new/images/lanclass/lanclass-TT.png">
                                    </div>
                                </dl>
                            </div>

                            <!--=====================课堂课件======================-->
                            <div class="sta-ketang" id="sta-ketang">
                                <span class="techfilelist">
                                </span>
                                <script type="text/template" id="techfilelist_templ">
                                    {{ if(it.length>0){ }}
                                    {{ for (var i = 0, l = it.length; i < l; i++) { }}
                                    {{var obj=it[i];}}

                                    {{?obj.isVideo=="Y"}}
                                        <a href="/interactLesson/downloadVideo.do?videoId={{=obj.videoId}}" target="_blank">
                                            <li><img src="/img/coursecommon.png" style="width: 160px;height: 110px;"></li>
                                            <span class="lessonclass-ho" title=" {{=obj.fileName}}"> {{=obj.fileName}}</span>
                                        </a>
                                    {{??}}
                                        {{?obj.imgUrl.indexOf(".png")>0 || obj.imgUrl.indexOf(".jpg")>0}}
                                            <a class="fancybox" href="{{=obj.imgUrl}}" data-fancybox-group="home" title="预览">
                                                <li><img title="点击查看大图" src="{{=obj.imgUrl}}" style="width: 160px;height: 110px;"></li>
                                                <span class="lessonclass-ho" title=" {{=obj.fileName}}"> {{=obj.fileName}}</span>
                                            </a>
                                        {{??}}
                                            <a href="{{=obj.imgUrl}}" target="_blank">
                                                <li>
                                                {{?obj.imgUrl.indexOf(".doc")>0 || obj.imgUrl.indexOf(".docx")>0}}
                                                    <img src="/img/coursedoc.png" style="width: 160px;height: 110px;">
                                                {{??obj.imgUrl.indexOf(".pdf")>0}}
                                                    <img src="/img/coursepdf.png" style="width: 160px;height: 110px;">
                                                {{??obj.imgUrl.indexOf(".xls")>0 || obj.imgUrl.indexOf(".xlsx")>0}}
                                                    <img src="/img/coursexls.png" style="width: 160px;height: 110px;">
                                                {{??obj.imgUrl.indexOf(".ppt")>0 || obj.imgUrl.indexOf(".pptx")>0}}
                                                    <img src="/img/courseppt.png" style="width: 160px;height: 110px;">
                                                {{??obj.imgUrl.indexOf(".swf")>0}}
                                                    <img src="/img/courseswf.png" style="width: 160px;height: 110px;">
                                                {{??}}
                                                    <img src="/img/coursecommon.png" style="width: 160px;height: 110px;">
                                                {{?}}
                                                </li>
                                                <span class="lessonclass-ho" title=" {{=obj.fileName}}"> {{=obj.fileName}}</span>
                                            </a>
                                        {{?}}
                                    {{?}}

                                    {{ } }}
                                    {{ } }}
                                </script>
                            </div>
                            <!--=====================课堂课件======================-->
                            <div class="sta-xuesheng" id="sta-xuesheng">
                                <span>
                                    <select id="stucnt">
                                        <c:forEach items="${fileTimesList}" var="fileTimes">
                                            <option value="${fileTimes.key}">${fileTimes.value}</option>
                                        </c:forEach>
                                    </select>
                                </span>
                                <span class="stufilelist">
                                </span>
                                <script type="text/template" id="stufilelist_templ">
                                    {{ if(it.length>0){ }}
                                    {{ for (var i = 0, l = it.length; i < l; i++) { }}
                                    {{var obj=it[i];}}

                                        {{?obj.imgUrl.indexOf(".png")>0 || obj.imgUrl.indexOf(".jpg")>0}}
                                            <a class="fancybox" href="{{=obj.imgUrl}}" data-fancybox-group="home" title="预览">
                                                <li><img title="点击查看大图" src="{{=obj.imgUrl}}" style="width: 160px;height: 110px;">
                                                </li>
                                                <span class="lessonclass-ho" title=" {{=obj.fileName}}"> {{=obj.fileName}}</span>
                                            </a>
                                        {{??}}
                                            <a href="{{=obj.imgUrl}}" target="_blank">
                                                <li>
                                                {{?obj.imgUrl.indexOf(".doc")>0 || obj.imgUrl.indexOf(".docx")>0}}
                                                <img src="/img/coursedoc.png" style="width: 160px;height: 110px;">
                                                {{??obj.imgUrl.indexOf(".pdf")>0}}
                                                <img src="/img/coursepdf.png" style="width: 160px;height: 110px;">
                                                {{??obj.imgUrl.indexOf(".xls")>0 || obj.imgUrl.indexOf(".xlsx")>0}}
                                                <img src="/img/coursexls.png" style="width: 160px;height: 110px;">
                                                {{??obj.imgUrl.indexOf(".ppt")>0 || obj.imgUrl.indexOf(".pptx")>0}}
                                                <img src="/img/courseppt.png" style="width: 160px;height: 110px;">
                                                {{??obj.imgUrl.indexOf(".swf")>0}}
                                                <img src="/img/courseswf.png" style="width: 160px;height: 110px;">
                                                {{??}}
                                                <img src="/img/coursecommon.png" style="width: 160px;height: 110px;">
                                                {{?}}
                                                </li>
                                                <span class="lessonclass-ho" title=" {{=obj.fileName}}"> {{=obj.fileName}}</span>
                                            </a>
                                        {{?}}
                                    {{ } }}
                                    {{ } }}
                                </script>
                            </div>



                        </div>
                    </div>
                </dd>
            </dl>
        </div>
    </div>
<div class="interactive-xuesheng">
    <dl>
        <dt>
            <em>学生单人统计情况</em>
        </dt>
        <dd>
            <select>
                <option>129</option>
                <option>126</option>
                <option>124</option>
                <option>122</option>
                <option>102</option>
                <option>127</option>
                <option>100</option>
                <option>149</option>
                <option>147</option>
                <option>145</option>
                <option>143</option>
                <option>141</option>
            </select>
        </dd>
        <dd>
            <em>抢答统计</em>
            <img src="/static_new/images/lanclass/xuesheng-1.jpg">
            <em>快速答题统计</em>
            <select class="xuesheng-se">
                <option>第一次</option>
                <option>第二次</option>
                <option>第三次</option>
            </select>
            <img src="/static_new/images/lanclass/xuesheng-2.png">
            <em>考试统计</em>
            <select class="xuesheng-se">
                <option>第一次</option>
                <option>第二次</option>
                <option>第三次</option>
            </select>
            <img src="/static_new/images/lanclass/xuesheng-3.png">
            <em>上传统计</em>
            <img src="/static_new/images/lanclass/xueshegn-4.jpg">
        </dd>
    </dl>
</div>
</div>

<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>


<script>
    seajs.use('lessonclass');
</script>

</body>
</html>
