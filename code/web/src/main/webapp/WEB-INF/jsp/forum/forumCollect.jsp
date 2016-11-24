<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>收藏中心</title>
    <link rel="stylesheet" type="text/css" href="/static/css/forum/forum.css">
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
    <style type="text/css">
        .header-cont .ha4 {
            background: #E8E8E8;
            color: #3764a0;
        }
    </style>
</head>
<body>
<%@ include file="../common/head.jsp" %>
<div class="container">
    <div class="set-left">
        <h2>收藏</h2>
        <ul class="ul-forumset">
            <li class="li1 li-cur"><img src="/static/images/forum/img_cellect1.png">板块</li>
            <li class="li2"><img src="/static/images/forum/img_cellect2.png">帖子</li>
        </ul>
    </div>
    <div class="set-right">
        <div class="right-zl">
            <p class="p-forumset">
                <span>收藏的板块</span>
            </p>
            <ul class="ul-cellect" id="section">
            </ul>
        </div>
        <div class="right-aq">
            <p class="p-forumset">
                <span>收藏的帖子</span>
            </p>
            <ul class="ul-cellect" id="post">

            </ul>
        </div>
    </div>
</div>
<%@ include file="../common/footer.jsp" %>
<%@ include file="../common/login.jsp" %>
<script type="text/template" id="sectionTml">
    <li>
        <span class="span4">板块</span>
        <span class="span5">收藏时间</span>
    </li>
    {{~it:value:index}}
    <li>
        <label>
            <%--<input type="checkbox">--%>
            <img src="/static/images/forum/img_personal_theme.png">
            <span class="span1" onclick="window.open('/forum/postIndex.do?pSectionId={{=value.postSectionId}}')">{{=value.postTitle}}</span>
        </label>
        <span class="span2">{{=value.time}}</span>
        <button class="delete" deleteId="{{=value.id}}">删除</button>
    </li>
    {{~}}
</script>


<script type="text/template" id="postTml">
    <li>
        <span class="span4">主题</span>
        <span class="span5">收藏时间</span>
    </li>
    {{~it:value:index}}
    <li>
        <label>
            <%--<input type="checkbox">--%>
            <img src="/static/images/forum/img_personal_theme.png">
            <span class="span1"
                  onclick="window.open('/forum/postDetail.do?pSectionId={{=value.sectionId}}&postId={{=value.postSectionId}}&personId={{=value.personId}}')">{{=value.postTitle}}</span>
        </label>
        <span class="span2">{{=value.time}}</span>
        <button class="delete" deleteId="{{=value.id}}">删除</button>
    </li>
    {{~}}
</script>

<script src="/static/js/sea.js"></script>
<!-- Custom js -->
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/forum/forumCollect.js');
</script>
</body>
</html>
