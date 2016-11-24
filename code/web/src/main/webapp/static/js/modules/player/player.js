/**
 * Created by liwei on 15/8/27.
 */
(function ($) {
    window.playerLinks = {};

    var fileType = {
        txt: ['pdf', 'doc'],
        video: ['avi', 'mp4', 'mpg', 'flv', 'wmv', 'mov', 'mkv'],
        voice: ['mp3'],
        img: ['jpg', 'jpeg', 'gif', 'png']
    }

    playerLinks.eachArr = function (file, arr, fn) {
        for (var i = 0; i < arr.length; i++) {
            if (file === arr[i]) {
                fn && fn();
                break;
            }
        }
    }

    playerLinks.previewSwf = function (url) {
        //swf播放
        $('#dialogPlayer').FlexPaperViewer({
            config: {
                jsDirectory: '/static/js/modules/flexpaper/FlexPaperViewer/',
                SwfFile: url,
                //SwfFile: 'http://10.0.0.2/fulan_edu_research/html/js/pdf2swf.swf',
                Scale: 0.6,
                ZoomTransition: 'easeOut',
                ZoomTime: 0.5,
                ZoomInterval: 0.2,
                FitPageOnLoad: true,
                FitWidthOnLoad: false,
                FullScreenAsMaxWindow: false,
                ProgressiveLoading: false,
                MinZoomSize: 0.2,
                MaxZoomSize: 5,
                SearchMatchAll: false,
                InitViewMode: 'SinglePage',
                ViewModeToolsVisible: true,
                ZoomToolsVisible: true,
                NavToolsVisible: true,
                CursorToolsVisible: true,
                SearchToolsVisible: true,
                localeChain: 'en_US'
            }
        })
    }

    playerLinks.dialog = function (callback) {
        Showbo.Msg.defaultWidth = 640;
        Showbo.Msg.alert('<div id="dialogPlayer"></div>', '预览');
        $('#dvMsgBox').find('.top').css('margin-bottom', '10px').next('.body').height(350);
        callback && callback();

    }

    playerLinks.init = function (jobj, fn) {


        $('body').on('click', jobj, function () {
            var url = fn($(this));
            var isfile = false,
                fileTxtArr = $(this).attr('href').split('.'),
                fileTxt = fileTxtArr[fileTxtArr.length - 1];


            /*if(window.SewisePlayer.doStop){
             window.SewisePlayer.doStop();
             }*/

            //文档预览
            playerLinks.eachArr(fileTxt, fileType.txt, function () {
                playerLinks.dialog(function () {
                    playerLinks.previewSwf(url)
                    isfile = true;
                });
            });

            //视频预览
            playerLinks.eachArr(fileTxt, fileType.video, function () {
                playerLinks.dialog(function () {
                    SewisePlayer.setup({
                        server: "vod",
                        type: "m3u8",
                        skin: "vodFlowPlayer",
                        logo: "none",
                        lang: "zh_CN",
                        topbardisplay: 'enable',
                        videourl: url
                    }, 'dialogPlayer');
                    isfile = true;
                });
                $('#dvMsgBtns').find('input').on('click', function () {
                    if (window.SewisePlayer.doStop) {
                        window.SewisePlayer.doStop();
                    }
                })
            });

            //音频预览
            playerLinks.eachArr(fileTxt, fileType.voice, function () {

                playerLinks.dialog(function () {
                    $('#dialogPlayer').height(30).closest('.body').height(85);
                    SewisePlayer.setup({
                        server: "vod",
                        type: "mp3",
                        //skin: "vodFlowPlayer", 	//和video一样支持皮肤的噢 :-)
                        videourl: url

                    }, 'dialogPlayer');
                    isfile = true;
                });

                $('#dvMsgBtns').find('input').on('click', function () {
                    if (window.SewisePlayer.doStop) {
                        window.SewisePlayer.doStop();
                    }
                })
            });

            //图片预览
            playerLinks.eachArr(fileTxt, fileType.img, function () {
                playerLinks.dialog(function () {
                    $('#dialogPlayer').html('<img src="' + url + '" />');
                    isfile = true;
                });
            });

            if (!isfile) {
                playerLinks.dialog(function () {
                    $('#dialogPlayer').height(90).html('<a href="' + url + '" target="_blank">下载</a>').closest('.body').height(110);
                    isfile = false;
                })
            }

            return false;
        });
    }

})(jQuery)

