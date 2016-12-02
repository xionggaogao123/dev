<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>管理社区</title>
    <link rel="stylesheet" type="text/css" href="/static/css/community/community.css">
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
</head>
<body style="background: #f5f5f5;">
<%--==============头部===================--%>
<%@ include file="../common/head.jsp" %>
<div class="container">

    <%@ include file="_layout.jsp" %>
</div>
<div class="f-cont">
    <div class="hd-nav">
        <span class="hd-green-cur">我的社区</span>
    </div>
</div>
<div class="container">
    <div class="hd-cont-f hd-cont-f1">
        <div class="com-left">
            <div class="com-left-s">
                <div class="com-tit">热门社区</div>
                <div class="my-comm-container clearfix">
                    <ul class="ul-my-com" id="hotCommunity">

                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>



<div class="bg"></div>

<%--环信消息通知--%>
<div class="hx-notice">
    <span class="sp1"></span>
    <span class="sp3">您有3条未读消息</span>
</div>


<script type="text/template" id="communityTmpl">
    {{~it:value:index}}
    <li>
        <a><img src="{{=value.logo}}"></a>
        <p>{{=value.name}}</p>
        <div class="com-hover-card clearfix">
            <div class="clearfix">
                <img src="{{=value.logo}}"><span></span>
                <span class="sp1">{{=value.name}}</span>
                <span class="sp2">社区ID：{{=value.searchId}}</span>
                <span class="sp-short sp3">社区简介：<em>...[详细]</em>{{=value.desc}} </span>
            </div>
            <p>
                <button class="join" cid="{{=value.id}}">+加入社区</button>
            </p>
            <div class="train-f">
                <div class="down-train"></div>
            </div>
        </div>
    </li>
    {{~}}
</script>

<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script type="text/javascript"
        src="/static/js/modules/core/0.1.0/jquery-upload/vendor/jquery.ui.widget.js?v=1"></script>
<script type="text/javascript" src="/static/js/modules/core/0.1.0/jquery-upload/jquery.fileupload.js"></script>
<script>
    seajs.use('/static/js/modules/community/hotCommunity.js', function (hotCommunity) {
        hotCommunity.init();
    });
</script>
</body>
</html>