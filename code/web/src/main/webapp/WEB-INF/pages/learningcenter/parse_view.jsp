<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="for" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>查看解析-复兰科技</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="/static/css/dialog.css" type="text/css"/>

    <script type="text/javascript" src="/static/js/jquery.js"></script>
    <script type="text/javascript" src="/static/plugins/flexpaper/flexpaper_flash.js"></script>
    <script type="text/javascript" src="/static/plugins/flexpaper/flexpaper_flash_debug.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/vendor/jquery.ui.widget.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.iframe-transport.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.fileupload.js"></script>

    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <link rel="stylesheet" href="/static/css/exercise/teacher_configuration.css" type="text/css">

    <script type="text/javascript">
        $(function(){
            //更改解析的文件
            $('#answer-word-upload').fileupload({
            	url: '/exam/file/replace.do',
                paramName: 'file',
                add:fileTypeCheck,
                start: function(e) {
                    MessageBox('解析上传中...', 0, 'save');
                },
                formData: {wordexerciseId : $("input[name='wordexerciseId']").val(),type:2},
                done: function (e, response) {
                	location.href="/exam/parse/view/"+$("input[name='wordexerciseId']").val();
                }
            });
        });


        //检查格式
        function fileTypeCheck(e, data) {
            var goUpload = true;
            var uploadFile = data.files[0];
            if (!(/\.(doc|docx)$/i).test(uploadFile.name)) {
                MessageBox('请选择doc或docx文件。', -1);
                goUpload = false;
            }
            if (goUpload == true) {
                data.submit();
            }
        }
    </script>
</head>
<body>
<div class="examine_top">
    <div class="examine_top_main">
        <div class="examine_top_main_left">
            <span class="examine_top_main_left_II">考试时间</span>
            <span class="examine_top_main_left_III">&nbsp;&nbsp;${time}&nbsp;&nbsp;</span>
            <span class="examine_top_main_left_IIII">分钟</span>
            <span class="examine_top_main_left_V">&nbsp;&nbsp;*注：解析将在试卷完成后自动发放</span>
        </div>
        <div class="examine_top_main_right">
            <label id="upload-answer-word" for="answer-word-upload" style="cursor: pointer;">
                 <span>更换文件</span>
            </label>

            <input type="file" name="answer-word-upload" id="answer-word-upload" accept="application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document" multiple="multiple" style="display: none">

            <input type="hidden" value="${id}" name="wordexerciseId"/>
        </div>
    </div>
</div>
<div class="examine_mian">
    <div class="examine_main_text">
        <a id="viewerPlaceHolder" style="width:645px;height:950px;display:inline" class="configuration_main_left_I"></a>
    </div>
</div>
<!--=================================底部==================================-->
<!--<div id="footer">

    <div id="footer-w">
        <span style="float: left">版权所有：上海复兰信息科技有限公司          <a href="http://www.fulaan-tech.com" target="_blank">www.fulaan-tech.com</a>        沪ICP备14004857号</span>
        <span style="float: right"><a href="/aboutus/k6kt">关于我们</a>  |  <a href="/contactus/k6kt">联系我们</a>   |  <a href="/service/k6kt">服务条款 </a> |  <a href="/privacy/k6kt">隐私保护 </a> |  <a href='http://wpa.qq.com/msgrd?v=1&uin=2803728882&site=qq&menu=yes' target="_blank">
            <img src="../images/QQService.png"></a></span>
    </div>
</div>--><div style="background-color: #ffffff;height: 50px;width: 100%;clear:both"></div>
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

<script type="text/javascript">
    var fp = new FlexPaperViewer(
            '/static/plugins/flexpaper/FlexPaperViewer',
            'viewerPlaceHolder', { config : {
                SwfFile : escape('${ansPath}'),
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
</script>
</body>
</html>
