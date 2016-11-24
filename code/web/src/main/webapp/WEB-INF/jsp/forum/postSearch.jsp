<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>搜索</title>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="/static/css/forum/forum.css">
    <link rel="stylesheet" type="text/css" href="/static/css/main.css">
</head>
<body style="background: #F2F2F2" regular="${regular}">

<%@ include file="../common/head.jsp" %>

<div class="search-cont">
    <p class="spp-top">
        <a href="#" class="s-backh"></a>
        <em> > </em>
        <a href="#">论坛</a>
        <em> > </em>
        <a href="#">搜索</a>
    </p>
    <div class="result-cont" style="display: block">
        <div class="sd-top">
            <p class="p1">搜索内容<em></em></p>
            <p class="p2">
                <input type="text" class="seat" onkeydown="Event()" placeholder="请输入搜索内容">
                <button class="sea">搜索</button>
            </p>
            <div class="p1" hidden>
                <input type="radio" name="search" value="post" checked>帖子
                <input type="radio" name="search" value="reply">回复
                <input type="radio" name="search" value="human">人
            </div>
        </div>

        <div class="result-list" id="resultList"></div>

        <div class="div-nofound1" id="notFound" style="display: none">
            <h3>抱歉，没有找到与<em><span style="color: red;" id="notText"></span></em>相关的帖子</h3>
            <p>建议您：</p>
            <p>1、查看是否发贴</p>
            <p>2、条件查询查询不到帖子</p>
        </div>
        <div class="new-page-links"></div>
    </div>

    <div class="search-page" style="display: none">
        <div class="sd-top">
            <p class="p1">帖子<em></em></p>
            <p class="p2">
                <input type="text" placeholder="请输入搜索内容">
                <button class="btn-s1">搜索</button>
            </p>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>

<script id="postListTml" type="text/template">
    {{~it:value:index}}
    <div class="result-li">
        <p class="p1">
            <a class="a1">[{{=value.postSectionName}}]</a>
            <a style="cursor: pointer"
               onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}&personId={{=value.personId}}')"
               class="a2">{{=value.postTitle}}</a>
        </p>
        <p class="p2">
            <span class="span1">发帖时间:{{=value.timeText}}</span>
            <em class="em1">{{=value.scanCount}}</em>
            <em class="em2">{{=value.commentCount}}</em>
            <span class="span2">发帖人：<a>{{=value.personName}}</a></span>
        </p>
    </div>
    {{~}}
</script>

<script type="text/javascript">
    function Event() {
        var e = window.event || arguments.callee.caller.arguments[0];
        if (e && e.keyCode == 13) {
            var regular = $('.seat').val();
            window.location.href = '/forum/postSearch.do?regular=' + encodeURI(encodeURI(regular));

        }
    }
</script>

<script src="/static/js/sea.js"></script>
<!-- Custom js -->
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/forum/postSearch.js');
</script>
</body>
</html>
