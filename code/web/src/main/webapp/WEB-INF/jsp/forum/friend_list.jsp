<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/6/13
  Time: 19:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>通知中心</title>
    <link rel="stylesheet" type="text/css" href="/static/css/forum/forum.css">
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
    <%--<script type="text/javascript" src="/static/js/modules/forum/friendlist.js"></script>--%>
</head>
<body>
<%@ include file="../common/head.jsp" %>
<div class="container">
    <div class="set-left">
        <h2>关注</h2>
        <ul class="ul-forumset">
            <li class="li1 li-cur"><img src="/static/images/forum/img_friendlist.png">关注列表</li>
        </ul>
    </div>

    <script id="concernListTmpl" type="text/template">
        <p class="p-forumset">
            <span>关注列表</span>
        </p>
        {{~it:value:index}}
        <div class="kk-friend" >
            <img src="{{=value.avatar}}">
            <p class="p1">{{=value.nickName}}</p>
            <div class="p2">
                <div class="p5">
                    <span onClick="window.open('/webim/index?userId={{=value.concernId}}')" class="sendMessage" userId="{{=value.concernId}}">发消息</span>
                </div>
                <div class="p6">
                    <span class="cancelConcern" style="cursor: pointer" concernId="{{=value.id}}">取消关注</span>
                </div>
            </div>
        </div>
        {{~}}
    </script>

    <div class="set-right" id="concernList">
    </div>
    <div class="new-page-links"></div>

</div>
<script src="/static/js/sea.js"></script>
<!-- Custom js -->
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/forum/friendList.js', function (friendList) {
        friendList.init();
    });

</script>
</body>
</html>
