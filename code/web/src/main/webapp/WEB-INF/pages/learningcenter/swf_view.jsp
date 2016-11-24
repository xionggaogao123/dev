<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="for" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>

<html>
<head>
<title>文档预览-复兰科技</title>
<meta charset="utf-8">
<link rel="stylesheet" href="/static/css/dialog.css" type="text/css"/>

<script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="/static/plugins/flexpaper/flexpaper_flash.js"></script>
<script type="text/javascript" src="/static/plugins/flexpaper/flexpaper_flash_debug.js"></script>
<script type="text/javascript" src="/static/plugins/jquery-upload/vendor/jquery.ui.widget.js"></script>
<script type="text/javascript" src="/static/plugins/jquery-upload/jquery.iframe-transport.js"></script>
<script type="text/javascript" src="/static/plugins/jquery-upload/jquery.fileupload.js"></script>
<script type="text/javascript" src="/static/js/sharedpart.js"></script>

<style type="text/css" media="screen">
    html, body  { height:100%; }
    body { margin:0; padding:0; overflow:auto; }
    #flashContent { display:none; }
</style>

<link rel="stylesheet" href="/static/css/exercise/teacher_configuration.css" type="text/css">


<%--pdf 预览 控件--%>
<script src="/static/js/exercise/plugins/util.js"></script>
<script src="/static/js/exercise/plugins/api.js"></script>
<script src="/static/js/exercise/plugins/metadata.js"></script>
<script src="/static/js/exercise/plugins/canvas.js"></script>
<script src="/static/js/exercise/plugins/webgl.js"></script>
<script src="/static/js/exercise/plugins/pattern_helper.js"></script>
<script src="/static/js/exercise/plugins/font_loader.js"></script>
<script src="/static/js/exercise/plugins/annotation_helper.js"></script>


<script>
    var page=1;
    function previewPdf(){
        PDFJS.workerSrc = '/static/js/exercise/plugins/worker_loader.js';
        var can=' <canvas id="the-canvas" />';
        $("#showPdf").empty();
        $("#showPdf").append(can);
        'use strict';
        PDFJS.getDocument('${pdfPath}').then(function(pdf) {
            pdf.getPage(1).then(function(page) {
                var scale = 1.1;
                var viewport = page.getViewport(scale);
                var canvas = document.getElementById('the-canvas');
                var context = canvas.getContext('2d');
                canvas.height = viewport.height;
                canvas.width = viewport.width;
                var renderContext = {
                    canvasContext: context,
                    viewport: viewport
                };
                page.render(renderContext);
            });
        });
    }
    function renderPage(i){
        PDFJS.workerSrc = '/static/js/exercise/plugins/worker_loader.js';
        var can=' <canvas id="the-canvas" />';
        $("#showPdf").empty();
        $("#showPdf").append(can);
        'use strict';
        PDFJS.getDocument('${pdfPath}').then(function(pdf) {
            if(i>pdf.numPages) {
                alert("已翻到末页！")
                i=pdf.numPages;
            }
            pdf.getPage(i).then(function(page) {
                var scale = 1.1;
                var viewport = page.getViewport(scale);
                var canvas = document.getElementById('the-canvas');
                var context = canvas.getContext('2d');
                canvas.height = viewport.height;
                canvas.width = viewport.width;
                var renderContext = {
                    canvasContext: context,
                    viewport: viewport
                };
                page.render(renderContext);
            });
        });
    }
    function previousPage(){
        page=page-1;
        if(page<1) page=1;
        renderPage(page);
    }
    function nextPage(){
        page+=1;
        renderPage(page);
    }
    function previewSwf(){
        var fp = new FlexPaperViewer(
                '/static/plugins/flexpaper/FlexPaperViewer',
                'viewerPlaceHolder', { config : {
                    SwfFile : escape('${swfPath}'),
                    Scale : 0.6,
                    ZoomTransition : 'easeOut',
                    ZoomTime : 0.5,
                    ZoomInterval : 0.2,
                    FitPageOnLoad : false,
                    FitWidthOnLoad : true,
                    FullScreenAsMaxWindow : false,
                    ProgressiveLoading : false,
                    MinZoomSize : 0.2,
                    MaxZoomSize : 5,
                    SearchMatchAll : false,
                    InitViewMode : 'SinglePage',
                    ViewModeToolsVisible : true,
                    ZoomToolsVisible : true,
                    NavToolsVisible : true,
                    CursorToolsVisible : true,
                    SearchToolsVisible : true,
                    localeChain: 'en_US'
                }});
    }
    $(document).ready(function () {
        //平台、设备和操作系统
        var system = {
            win: false,
            mac: false,
            xll: false
        };
        //检测平台
        var p = navigator.platform;
        system.win = p.indexOf("Win") == 0;
        system.mac = p.indexOf("Mac") == 0;
        system.x11 = (p == "X11") || (p.indexOf("Linux") == 0);
        //跳转语句
        if (system.win || system.mac || system.xll) {
            jQuery("#configuration_img").hide();
            previewSwf();

        } else {
            previewPdf();
        }
    });
</script>
<%--==============================================--%>




<script type="text/javascript">
$(function(){
    //注册text/template替换函数
    if (!String.prototype.format) {
        String.prototype.format = function() {
            var args = arguments;
            return this.replace(/{(\d+)}/g, function(match, number) {
                return typeof args[number] != 'undefined'
                        ? args[number]
                        : match
                        ;
            });
        };
    }

   

});




</script>


</head>
<body>

<div class="configuration_main">


    <div style="width:770px;height:60px;" id="configuration_img">
        <a href="javaScript:previousPage()"><img src="/img/te_first.jpg" style="margin-left: 100px;"></a>
        <a href="javaScript:nextPage()"><img src="/img/te_last.png" style="float: right;margin-right: 50px;"></a>
    </div>

<div class="configuration_main_left" id="showPdf" style="margin-top: 10px;height: 1020px!important">

    <a id="viewerPlaceHolder" style="width:645px;height:950px;display:inline" class="configuration_main_left_I" href="${pdfPath}"></a>
</div>








</div>

<!--=================================底部==================================-->
<!--<div id="footer">

    <div id="footer-w">
        <span style="float: left">版权所有：上海复兰信息科技有限公司          <a href="http://www.fulaan-tech.com" target="_blank">www.fulaan-tech.com</a>        沪ICP备14004857号</span>
        <span style="float: right"><a href="/aboutus/k6kt">关于我们</a>  |  <a href="/contactus/k6kt">联系我们</a>   |  <a href="/service/k6kt">服务条款 </a> |  <a href="/privacy/k6kt">隐私保护 </a> |  <a href='http://wpa.qq.com/msgrd?v=1&uin=2803728882&site=qq&menu=yes' target="_blank">
            <img src="/img/exercise/QQService.png"></a></span>
    </div>
</div>-->
<div style="background-color: #ffffff;height: 50px;width: 100%;clear:both"></div>
<div id="footer">
    <div id="footer-w">
        <span style="float: left">版权所有：上海复兰信息科技有限公司          <a href="http://www.fulaan-tech.com" target="_blank">www.fulaan-tech.com</a>        沪ICP备14004857号</span>
        <span style="float: right"><a href="/aboutus/k6kt">关于我们</a>  |  <a href="/contactus/k6kt">联系我们</a>   |  <a href="/service/k6kt">服务条款 </a> |  <a href="/privacy/k6kt">隐私保护 </a> |  <a href='http://wpa.qq.com/msgrd?v=1&uin=2803728882&site=qq&menu=yes' target="_blank"><img src="/img/QQService.png"></a></span>
    </div>
</div>
<div id="browser-updater">
    <div class="browser-updater-wrapper" style="display: none">
        <p>
            <span>亲，浏览器版本过低可能会影响您的使用体验，为了提升速度您可以：</span>
            <a class="browser-updater-chrome" href="/upload/resources/ChromeStandaloneSetup.exe">安装Chrome浏览器</a>
        </p>
        <a class="browser-updater-close" onclick="hideBrowserUpdater()"><i class="fa fa-close fa-2"></i></a>
    </div>
</div>

<!--===================================上传解析弹出框=====================================-->
<div class="teacher_popup_1" style="display: none" id="teacher_popup">
    <div class="teacher_popup_I">
        <span class="teacher_popup_II">提示</span>
    </div>
    <div class="teacher_popup_III">解析上传成功！</div>
    <div class="teacher_popup_IIII">
        <span class="teacher_popup_IIII_I" onclick="promptHidden()">完成</span>
        <span class="teacher_popup_IIII_II" onclick="checkAnswerWord()">查看解析</span>
    </div>
</div>

<script>
    //判断浏览器版本
    if (navigator.appName == 'Microsoft Internet Explorer' && parseFloat(navigator.appVersion) < 5) {
        var updater = document.getElementById('browser-updater');
        updater.style.display = 'block';
        setTimeout(function () {
            updater.style.display = 'none';
        }, 3000);
    }
</script>
</body>
<script type="text/javascript">
    var fp = new FlexPaperViewer(
            '/static/plugins/flexpaper/FlexPaperViewer',
            'viewerPlaceHolder', { config : {
                SwfFile : escape('${swfPath}'),
                Scale : 1,
                ZoomTransition : 'easeOut',
                ZoomTime : 0.5,
                ZoomInterval : 0.2,
                FitPageOnLoad : false,
                FitWidthOnLoad : true,
                FullScreenAsMaxWindow : false,
                ProgressiveLoading : false,
                MinZoomSize : 0.2,
                MaxZoomSize : 5,
                SearchMatchAll : false,
                InitViewMode : 'SinglePage',

                ViewModeToolsVisible : true,
                ZoomToolsVisible : true,
                NavToolsVisible : true,
                CursorToolsVisible : true,
                SearchToolsVisible : true,

                localeChain: 'en_US'
            }});

    function promptHidden(){
        $("#teacher_popup").hide();
    }
</script>




</html>