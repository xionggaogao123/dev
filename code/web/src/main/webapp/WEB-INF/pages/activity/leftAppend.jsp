<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%--朋友圈--%>
<div id="friendcircle2" style="display: none">
    <div id="center_left_info">
        <div id="center_left_info_I">
            <img id="friendApplyImg" src="../img/activity/friend-request.png">
            <span>${friendApplyCount}</span>
            <span style="margin-top: -4px">好友申请</span>
        </div>
        <div id="centent_left_info_M">
            <img id="myFriendImg" src="../img/activity/myfriend.png">
            <span>我的好友</span>
        </div>
        <div id="center_left_info_C">
            <img id="activityInviteImg" src="../img/activity/events.png">
            <span>活动邀请</span>
        </div>
    </div>
    <div style="background-color:#F07474;width: 220px;height: 30px;font-size: 12px;">
        <div style="color: #ffffff;font-weight: bold;padding:5px 0 0 10px;">添加好友，去参加他/她发起的活动吧！</div>
    </div>
    <div id="center_left_info_IN">
        <input type="text" id="keywords" placeholder="搜索好友">
        <img id="searchIMg" src="../img/activity/fdjing.png">
    </div>

    <div class="center_left">
        <div id="center_left_top">
            <div class="info-card"></div>
            <span>推荐好友</span>
            <a class="recommandFriend"><span id="changefriend" class="change"
                                                           style="padding-left: 60px;">
        <img style="position: relative;top:4px;" src="../img/activity/xuanzhuang_2.png">换一批</span></a>
        </div>
        <div id="center_left_li">
            <%--<ul style="padding-left: 10px;"></ul>--%>
            <ul class="recommandFiendList"></ul>
        </div>
    </div>
</div>
