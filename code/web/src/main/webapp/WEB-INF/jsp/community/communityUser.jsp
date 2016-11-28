<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <title>发布通知</title>
    <link rel="stylesheet" type="text/css" href="/static/css/community/community.css">
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
    <script type="text/javascript" src="/static/js/main.js"></script>
    <script type="text/javascript" src="/static/js/recorder.js"></script>
    <script type="text/javascript" src="/static/js/swfobject.js"></script>
</head>
<body style="background: #f5f5f5;" communityId="${communityId}" personId="${personId}" applyName="${applyName}">
<%--==============头部===================--%>
<%@ include file="../common/head.jsp" %>
<div class="container">
    <%--<%@ include file="_layout.jsp" %>--%>
    <div class="user-infor">
        <div class="d1"></div>
        <img src="${avatar}">
        <p class="p1"><span>${nickName}</span>(UID:<span>${personId}</span>)</p>
        <p class="p1">所属群组：${communityNames}</p>
        <p class="p2">
            <c:forEach items="${tags}" var="tag">
                <em>${tag}</em>
            </c:forEach>
        </p>
        <p class="p3">
            <span id="apply"><img src="/static/images/community/img_add.png">${friend}<em></em></span>
            <span style="cursor: pointer" onClick="window.open('/webim/index?userId=${personId}')"><img src="/static/images/community/img_chat.png">聊一聊<em></em></span>
        </p>
    </div>
</div>
<div class="f-cont">
    <div class="hd-nav" style="width: 1000px">
        <span class="hd-green-cur">TA的论坛</span>
        <%--<span>TA的玩伴</span>--%>
    </div>
</div>
<div class="container" id="theme">
    <div class="thme-ied">我发过的主题</div>
    <div style="background: #fff;padding-bottom:20px;">
        <table class="ul-tabtheme" id="postList">
            <%--<thead>--%>
            <%--<tr>--%>
                <%--<th width="70%" class="th1">主题</th>--%>
                <%--<th width="15%">所属板块</th>--%>
                <%--<th width="15%">回复/查看</th>--%>
            <%--</tr>--%>
            <%--</thead>--%>
            <%--<tbody >--%>
            <%--<td class="td1">小蚁思想碰撞下</td>--%>
            <%--<td>晒才艺</td>--%>
            <%--<td>50/100</td>--%>
            <%--</tbody>--%>
        </table>
        <script id="postListTml" type="text/template">
            <thead>
            <tr>
                <th width="70%" class="th1">主题</th>
                <th width="15%">所属板块</th>
                <th width="15%">回复/查看</th>
            </tr>
            </thead>
            {{~it:value:index}}
            <tbody>
            <td class="td1" style="cursor: pointer" onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}&personId={{=value.personId}}')">{{=value.postTitle}}</td>
            <td>{{=value.postSectionName}}</td>
            <td>{{=value.commentCount}}/{{=value.scanCount}}</td>
            </tbody>
            {{~}}
        </script>
        <div class="new-page-links" id="newPage"></div>
    </div>
</div>
<div class="container" id="imageTheme">

    <div style="text-align: center;background: #fff;margin-top:14px;"><img src="/static/images/community/no_data.jpg"></div>
        <div class="hd-cont-f hd-cont-f1" style="width: 1000px;background: #fff;display: none;">
            <p class="p-taf">TA发布的活动</p>
            <ul class="ul-hds">
                <li>
                    <button>进行中</button>
                    <p class="p1">
                        <span>#篮球#</span>周末下午打篮球！
                    </p>
                    <p class="p2">是的饭卡上发士大夫山豆根山豆根法国皇帝皇后</p>
                </li>
                <li>
                    <button>取消报名</button>
                    <p class="p1">
                        <span>#篮球#</span>周末下午打篮球！
                    </p>
                    <p class="p2">是的饭卡上发士大夫山豆根山豆根法国皇帝皇后</p>
                </li>
                <li>
                    <button>取消报名</button>
                    <p class="p1">
                        <span>#篮球#</span>周末下午打篮球！
                    </p>
                    <p class="p2">是的饭卡上发士大夫山豆根山豆根法国皇帝皇后</p>
                </li>
                <li>
                    <button>取消报名</button>
                    <p class="p1">
                        <span>#篮球#</span>周末下午打篮球！
                    </p>
                    <p class="p2">是的饭卡上发士大夫山豆根山豆根法国皇帝皇后</p>
                </li>
                <li>
                    <button>取消报名</button>
                    <p class="p1">
                        <span>#篮球#</span>周末下午打篮球！
                    </p>
                    <p class="p2">是的饭卡上发士大夫山豆根山豆根法国皇帝皇后</p>
                </li>
                <li>
                    <button>取消报名</button>
                    <p class="p1">
                        <span>#篮球#</span>周末下午打篮球！
                    </p>
                    <p class="p2">是的饭卡上发士大夫山豆根山豆根法国皇帝皇后</p>
                </li>
                <li>
                    <button>取消报名</button>
                    <p class="p1">
                        <span>#篮球#</span>周末下午打篮球！
                    </p>
                    <p class="p2">是的饭卡上发士大夫山豆根山豆根法国皇帝皇后</p>
                </li>
                <li>
                    <button>取消报名</button>
                    <p class="p1">
                        <span>#篮球#</span>周末下午打篮球！
                    </p>
                    <p class="p2">是的饭卡上发士大夫山豆根山豆根法国皇帝皇后</p>
                </li>
                <li>
                    <button>取消报名</button>
                    <p class="p1">
                        <span>#篮球#</span>周末下午打篮球！
                    </p>
                    <p class="p2">是的饭卡上发士大夫山豆根山豆根法国皇帝皇后</p>
                </li>
            </ul>
        </div>
</div>
<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>

<%--环信消息通知--%>
<div class="hx-notice">
    <span class="sp2" id="hx-icon"></span>
    <span class="sp3" id="hx-msg-count">您有0条未读消息</span>
</div>



<div class="wind-yins">
    <p class="p1">隐私设置<em>×</em></p>
    <label><input type="radio" name="ys-set">所有人可见</label>
    <label><input type="radio" name="ys-set">人认证好友可见</label>
    <label><input type="radio" name="ys-set">尽仅自己可见</label>
    <p class="p2">
        <button class="btn1">确认</button>
        <button class="btn2">取消</button>
    </p>
</div>
<div class="bg"></div>

<!-- template -->




<!-- end template -->
<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>

<script>
    seajs.use('/static/js/modules/community/communityUser.js', function (communityUser) {
        communityUser.init();
    });

</script>
</body>
</html>
