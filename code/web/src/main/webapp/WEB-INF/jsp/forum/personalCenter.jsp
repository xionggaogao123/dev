<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>个人中心</title>
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/css/forum/forum.css">
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
    <style>
        .lunt-index-hide {
            display: none;
        }

        .container {
            min-height: 620px
        }

        .header-cont .ha4 {
            background: #E8E8E8;
            color: #3764a0;
        }
    </style>
</head>
<body concernFlag="${concernFlag}">
<input id="personId" type="hidden" value="${personId}">
<%@ include file="../common/head.jsp" %>
<div class="container">
    <div class="personal-main">
        <div class="personal-top">
            <c:if test="${imageSrc!=null}">
                <img src="${imageSrc}">
            </c:if>
            <c:if test="${imageSrc==null}">
                <img src="/static/images/forum/head_picture.png">
            </c:if>
            <p>${personName}</p>
            <c:if test="${userId != uid}">
                <p>
                    <button
                            <c:if test="${friendLog==true}">style="display: none"</c:if> id="applyFriend">添加好友
                    </button>
                    <button id="sendMessage">发送信息</button>
                    <button <c:if test="${friendLog==true}">style="display: none"</c:if> id="concern">
                        <c:if test="${concernFlag==1}">取消关注</c:if>
                        <c:if test="${concernFlag==0}">关注TA</c:if>
                    </button>
                </p>
            </c:if>
        </div>
        <div class="personal-nav">
            <span class="personal-span-cur span1">个人资料</span>
            <span class="span2">主题</span>
        </div>
        <div class="personal-infor">
            <p>
                <i class="span5">${personName}(UID:${uid})</i><!--(UID：1234631)-->
            </p>
            <p>统计信息：&nbsp;好友数 ${friendCount}<em>|</em>回帖数 ${replyCount}<em>|</em>主题数 ${postCount}</p>
            <p class="pb">
                <span>性别：${sex}</span>
                <span>生日：${birthDay}</span>
            </p>
            <p><span class="span1">活跃概况</span></p>
            <p>
                <span>用户组 新手上路</span>
                <span>积分 ${forumScore}</span>
            </p>
            <p>
                <span>在线时间 ${statisticTime}小时</span>
                <span>注册时间 ${registerTime}</span>
                <span>上次退出时间 ${quitTime}</span>
            </p>
            <p>
                <span>注册IP ${registerIp}</span>
                <span>上次访问IP ${interviewIP}</span>
                <span>本次活动时间 ${interviewTime}</span>
            </p>
            <p>
                <span>上次发表时间 ${interviewPostTime}</span>
                <span>所在时区使用系统默认</span>
            </p>
        </div>
        <div class="personal-theme">
            <p class="p-theme">
                <em class="em1 bold1">主题</em>
                <em class="em2">回复</em>
            </p>
            <div class="theme-zt" id="postList">

            </div>
            <div class="new-page-links" id="newPage" style="display: none"></div>
            <div class="theme-hf" id="replyList">

            </div>
            <div class="new-page-links" id="newPage1" style="display: none"></div>
        </div>
    </div>
</div>
<div class="bg"></div>
<%--添加好友弹窗--%>
<%@ include file="../common/footer.jsp" %>
<%@ include file="../common/login.jsp" %>

<div class="add-wind apply" style="display: none">
    <p class="p1">加为玩伴<em>×</em></p>
    <img src="${imageSrc}">
    <p class="p2">添加<em>${personName}</em>为玩伴，附言：</p>
    <input type="text" id="contentValue">
    <p class="p3">(附言为可选，最多10个字)</p>
    <div class="clearfix1"></div>
    <button id="confirm">确认</button>
</div>

<%--发送消息弹窗--%>
<div class="add-wind sendInf" style="display: none">
    <p class="p1">发送消息<em id="lol">×</em></p>
    <img src="${imageSrc}">
    <p class="p2">给<em>${personName}</em>发送消息，消息：</p>
    <input type="text" id="content">
    <p class="p3">(消息内容不能为空)</p>
    <div class="clearfix1"></div>
    <button id="confirmSend">确认</button>
</div>

<script id="postListTml" type="text/template">
    <p>
        <span class="span4">主题</span>
        <span class="span2">板块</span>
        <span class="span3">回复/查看</span>
    </p>
    {{~it:value:index}}
    <p>
        <span class="span1 span-bg"
              onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}&personId={{=value.personId}}')">{{=value.postTitle}}</span>
        <span class="span2">{{=value.postSectionName}}</span>
        <span class="span3">{{=value.commentCount}}/{{=value.scanCount}}</span>
    </p>
    {{~}}
</script>

<script id="replyListTml" type="text/template">
    <p>
        <span class="span4">回复</span>
        <span class="span2">板块</span>
        <span class="span3">回复/查看</span>
    </p>
    {{~it:value:index}}
    <p class="p1">
        <span class="span1 span-bg">{{=value.postTitle}}</span>
        <span class="span2">{{=value.postSectionName}}</span>
        <span class="span3">{{=value.commentCount}}/{{=value.scanCount}}</span>
    </p>
    <p class="p2"
       onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}&personId={{=value.personId}}')">
        · 我的回复</p>
    {{~}}
</script>

<script src="/static/js/sea.js"></script>
<!-- Custom js -->
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/forum/personalCenter.js', function (personalCenter) {
        personalCenter.init();
    });
</script>
</body>
</html>
