<%--
  Created by IntelliJ IDEA.
  User: fulaan
  Date: 16-3-11
  Time: 下午4:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>录课神器</title>
    <link rel="stylesheet" href="/static_new/css/recordd/recordd.css">
    <link rel="stylesheet" href="/static_new/css/reset.css">
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
</head>
<body>
<!--============引入头部========-->
<%@ include file="../common_new/head.jsp" %>
<div class="recordd-main">
    <!--.col-left-->
    <%@ include file="../common_new/col-left.jsp" %>
    <!--/.col-left-->
    <div class="recordd-right">
        <div class="recordd-top">
            <ul>
                <li>
                    录课神器
                </li>
            </ul>
        </div>
        <div class="recordd-info">
            <dl>
                <dd>
                    <em>1.</em><P>请先下载<a href="/upload/resources/recorder.exe">K6KT录课神器</a>,推荐选择安装在本地磁盘较大的位置。</p>
                </dd>
                <dd>
                    <em>2.</em><P>使用“K6KT录课神器”，您可以选择在线平台启动软件。第一次网页端启动软件，软件会弹出“外部协议请求”窗口，可按图2进行操作。</p>
                </dd>
                <dd>
                    <img src="/static_new/images/recordd/recordd-top.jpg">
                </dd>
                <dd>
                    <em>3.</em><p class="recordd-V recordd-M">"K6KT录课神器"启动后，会在桌面的右上角出现带有“K”的绿色图标（如右图）。准备好后，点击“点击录制”后，软件在后台开始录制视屏。此时“K”图标由绿色变成红色（如右图）</p>
                    <img src="/static_new/images/recordd/recordd-midd.png">
                </dd>
                <dd>
                    <em>4.</em><p class="recordd-M">再次点击红色“K”图标</p><img class="recordd-M" src="/static_new/images/recordd/recordd-K.png">，<p class="recordd-M">,视屏结束录制.结束录制后，自动弹出视屏窗口。</p>
                </dd>
                <dd>
                    <em>5.</em><p class="recordd-N">>此时可以选择浏览器您所录制的视屏，对录制的视屏满意后，您可以将视频保存在电脑的任意位置。</p>
                    <img class="recordd-T" src="/static_new/images/recordd/recordd-T.jpg">
                </dd>
            </dl>
        </div>
    </div>
</div>
<div style="clear:both"></div>
<!--=============底部版权=================-->
<%@ include file="../common_new/foot.jsp" %>

<script src="/static_new/js/sea.js?v=1"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>

<%----%>
<script>
    seajs.use('documentCheck');
</script>
</body>
</html>
