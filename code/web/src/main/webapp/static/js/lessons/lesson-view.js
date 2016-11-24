/**
 * Created by Niu Xin on 14-9-26.
 */

var polyv_player;
var video_thumb_list;
var current_video = -1;

var watch_timer = null;

var watchedTime, videoLength;
var videoId;
var courseid="";
var isFlash = false;
var videoType;
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

    videoType = getVideoType(videoURL);
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
    videoLength = parseInt(dom.getAttribute('video-length'))||0;

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
                        async:false,
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

function playerReady(name){
    if(isFlash){
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
    if(s2j_onPlayOver.disabled) {
        return;
    }
    playVideo(current_video + 1);
    clearInterval(watch_timer);
}

function s2j_onPlayStart() {
    clearInterval(watch_timer);
    if(videoType == 'HLS'){
        videoLength= SewisePlayer.duration() * 1000;
        console.log(videoLength);
    }
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
    console.log(watchedTime);

    if (watchedTime > videoLength /1000* 0.8) {
        clearInterval(watch_timer);
        watchedTime = 0;
        $.getJSON("/video/viewrecord/add.do",
            {
                'lessonId': courseid,
                'videoId': videoId,
                'viewType': 1
            },
            function (data) {
                $('#video .active .video-info').text('已看完');
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

function coverError(img) {
    var uploadState = img.parentElement.getAttribute('state');
    if (uploadState == 0) {
        img.src = "/img/converting_small.png";
    }
}
$(document).ready(function(){
    $(".push-top i,.div-btn .btn-x").click(function(){
        $(".push-wind").fadeOut();
        $(".bg").fadeOut();
    });
    $(".tran-span").click(function(){
        $(this).parent().next().slideToggle();
        $(this).toggleClass("tran-ri").toggleClass("tran-do");
    })
    $(".ml-container li .fenzhi,.ul-ml li .fenzhi").css("background","url('../../../static_new/images/fenzhi_mid.png') no-repeat 6px -1px");


})

$(function () {
    if ($('.video-thumb').length <= 0) {
        $('#videoplayer-div').hide();
    } else {
        video_thumb_list = $('.video-thumb').get();
        playVideo(0);
    }

    /*
     var MOUSE_OVER = false;
     var video_list_div = $('.video-thumb-list');

     $('body').bind('mousewheel', function (e) {
     if (MOUSE_OVER) {
     if (e.preventDefault) {
     e.preventDefault();
     }
     e.returnValue = false;
     return false;
     }
     });

     video_list_div.mouseenter(function () {
     MOUSE_OVER = true;
     });
     video_list_div.mouseleave(function () {
     MOUSE_OVER = false;
     });

     video_list_div.on('mousewheel', function (event) {
     var scrollTop = $(this).scrollTop();
     var delta = event.deltaFactor * event.deltaY;
     $(this).scrollTop(scrollTop - Math.round(delta));
     });
     */
});