<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0"/>
    <title>聊天</title>
    <!-- HTML5 element  -->
    <script src="/static/dist/webim/demo/javascript/dist/browser-polyfill.min.js"></script>

    <style>
        body{
            height: 100%;
            background: url("/static/images/web_im_bg.jpg") no-repeat center;
            background-size: cover;
        }

        ::-webkit-scrollbar{width:12px;}
        ::-webkit-scrollbar-track{background-color:#2E3238;}
        ::-webkit-scrollbar-thumb{background-color:#414449;border-radius: 7px;}
        ::-webkit-scrollbar-thumb:hover {background-color:#666}
        ::-webkit-scrollbar-thumb:active {background-color:#666}
    </style>
</head>
<body>
<section id='main' class='w100'>
    <article id='demo'></article>
    <article id='components'></article>
</section>
<!--config-->
<script src="/static/dist/webim/demo/javascript/dist/webim.config.js"></script>
<script>
    if (WebIM.config.isWindowSDK) {
        document.title = "环信Demo";
    }
    if (WebIM.config.isDebug) {
        document.write("<script src='/static/dist/webim/sdk/strophe-1.2.8.js'><\/script>");
        document.write("<script src='/static/dist/webim/demo/javascript/dist/debug.js'><\/script>");
    } else {
        <!--A JavaScript library for XMPP over Websocket-->
        document.write("<script src='/static/dist/webim/sdk/strophe-1.2.8.min.js'><\/script>");
    }
</script>
<!--sdk-->
<script src='/static/dist/webim/sdk/websdk-1.1.3.js'></script>
<!--webrtc-->
<script>
    if (WebIM.config.isWebRTC) {
        document.write("<script src='/static/dist/webim/webrtc/adapter.js'><\/script>");
        document.write("<script src='/static/dist/webim/webrtc/webrtc-1.0.0.js'><\/script>");
    }
</script>
<!--[if lte IE 9]>
<script src="/static/dist/webim/demo/javascript/dist/swfupload/swfupload.min.js"></script>
<![endif]-->
<!--demo javascript-->
<script src="/static/dist/webim/demo/javascript/dist/demo.js"></script>
</body>
</html>
