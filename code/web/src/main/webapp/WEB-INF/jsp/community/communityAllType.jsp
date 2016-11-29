<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>通知</title>
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/css/community/community.css">
    <link href="/static/js/modules/core/0.1.0/fancyBox/jquery.fancybox.css?v=2015041602" rel="stylesheet"
          type="text/css" media="screen">
</head>
<body style="background: #f5f5f5;">
<%--==============头部===================--%>
<%@ include file="../common/head.jsp" %>
<div class="container">
    <%@ include file="_layout.jsp" %>
</div>
<div class="f-cont">
    <div class="hd-nav">
        <c:if test="${login == false}">
            <div class="login-mk-btn">
                <div class="d1" onclick="window.open('/mall/register.do')"></div>
                <div class="d2"></div>
            </div>
        </c:if>
        <span class="hd-green-cur">我的社区</span>
        <%--<span>找学习</span>--%>
        <%--<span>找玩伴</span>--%>
    </div>
</div>
<div class="container">
    <div class="hd-cont-f hd-cont-f1">
        <div class="com-left">
            <script type="text/template" id="announcementTmpl">
                <div class="com-tit" id="announce_all">社区通知</div>
                {{~it:value:index}}
                <div class="notice-container clearfix">
                    <div class="notice-holder">
                        <img src="{{=value.imageUrl}}" style="cursor: pointer"
                             onclick="window.open('/community/userData.do?userId={{=value.userId}}')">
                        <p>{{=value.nickName}}</p>
                        <p class="p1">{{=value.roleStr}}</p>
                    </div>
                    <div class="notice-cont">
                        <p class="p-tz p1" style="cursor: pointer"
                           onclick="window.open('/community/communityDetail?detailId={{=value.id}}')">
                            {{=value.title}}</p>
                        <p class="p-cont">{{=value.content}}<span
                                class="sp-more">{{?value.content.length>=143}}...<em
                                class="spread">[展开全文]</em>{{?}}</span></p>
                        <p class="p-infor">
                            <span>消息来源：{{=value.communityName}}</span>
                            <span>发表时间：{{=value.time}}</span>
                        </p>
                    </div>
                </div>
                {{~}}
            </script>
            <div class="com-left-s" id="announcement">
            </div>
            <script type="text/template" id="activityTmpl">
                <div class="com-tit" id="activity_all">组织活动报名</div>
                {{~it:value:index}}
                <div class="notice-container clearfix">
                    <div class="notice-holder">
                        <img src="{{=value.imageUrl}}" style="cursor: pointer"
                             onclick="window.open('/community/userData.do?userId={{=value.userId}}')">
                        <p>{{=value.nickName}}</p>
                        <p class="p1">{{=value.roleStr}}</p>
                    </div>
                    <div class="notice-cont">
                        <p class="p-hd p1" style="cursor: pointer"
                           onclick="window.open('/community/communityDetail?detailId={{=value.id}}')">
                            {{=value.title}}</p>
                        <p class="p-cont">{{=value.content}}<span
                                class="sp-more">{{?value.content.length>=90}}...<em
                                class="spread">[展开全文]</em>{{?}}</span></p>
                        <p class="p-infor">
                            <span>消息来源：{{=value.communityName}}</span>
                            <span>发表时间：{{=value.time}}</span>
                        </p>
                    </div>
                    <%--<div class="notice-bm">--%>
                        <%--<button class="commit" itemId="{{=value.id}}">我要报名</button>--%>
                        <%--<span>已报名（<em>{{=value.partInCount}}</em>）人</span>--%>
                    <%--</div>--%>
                </div>
                {{~}}
            </script>
            <div class="com-left-s" id="activity">
            </div>
            <script type="text/template" id="materialsTmpl">
                <div class="com-tit" id="materials_all">学习用品</div>
                {{~it:value:index}}
                <div class="notice-container clearfix">
                    <div class="notice-holder">
                        <img src="{{=value.imageUrl}}" style="cursor: pointer"
                             onclick="window.open('/community/userData.do?userId={{=value.userId}}')">
                        <p>{{=value.nickName}}</p>
                        <p class="p1">{{=value.roleStr}}</p>
                    </div>
                    <div class="notice-cont">
                        <p class="p-tj p1" style="cursor: pointer"
                           onclick="window.open('/community/communityDetail?detailId={{=value.id}}')">
                            {{=value.title}}</p>
                        <p class="p-cont">{{=value.content}}<span
                                class="sp-more">{{?value.content.length>=90}}...<em
                                class="spread">[展开全文]</em>{{?}}</span></p>
                        <p class="p-infor">
                            <span>消息来源：{{=value.communityName}}</span>
                            <span>发表时间：{{=value.time}}</span>
                        </p>
                    </div>
                </div>
                {{~}}
            </script>
            <div class="com-left-s" id="materials">
            </div>
            <script type="text/template" id="shareTmpl">
                <div class="com-tit" id="share_all">火热分享</div>
                {{~it:value:index}}
                <div class="notice-container clearfix">
                    <div class="notice-holder">
                        <img src="{{=value.imageUrl}}" style="cursor: pointer"
                             onclick="window.open('/community/userData.do?userId={{=value.userId}}')">
                        <p>{{=value.nickName}}</p>
                        <p class="p1">{{=value.roleStr}}</p>
                    </div>
                    <div class="notice-cont">
                        <p class="p-fx p1" style="cursor: pointer"
                           onclick="window.open('/community/communityDetail?detailId={{=value.id}}')">
                            {{=value.title}}</p>
                        <p class="p-cont">{{=value.content}}<span
                                class="sp-more">{{?value.content.length>=90}}...<em
                                class="spread">[展开全文]</em>{{?}}</span></p>
                        <p class="p-img clearfix">
                            {{~value.images:image:i}}
                            <a class="fancybox" style="cursor:pointer;" href="{{=image.url}}" data-fancybox-group="home" title="预览">
                                <img src="{{=image.url}}?imageView2/1/w/100/h/100"><br/>
                            </a>
                            {{~}}
                        </p>
                        <p class="p-infor">
                            <span>消息来源：{{=value.communityName}}</span>
                            <span>发表时间：{{=value.time}}</span>
                        </p>
                    </div>
                    <%--<div class="notice-bm">--%>
                    <%--<button class="commit" itemId="{{=value.id}}">我要分享</button>--%>
                    <%--<span>已分享（{{=value.partInCount}}）人</span>--%>
                    <%--</div>--%>
                </div>
                {{~}}
            </script>
            <div class="com-left-s" id="share">
            </div>
            <script type="text/template" id="homeworkTmpl">
                <div class="com-tit" id="homework_all">作业</div>
                {{~it:value:index}}
                <div class="notice-container clearfix">
                    <div class="notice-holder">
                        <img src="{{=value.imageUrl}}" style="cursor: pointer"
                             onclick="window.open('/community/userData.do?userId={{=value.userId}}')">
                        <p>{{=value.nickName}}</p>
                        <p class="p1">{{=value.roleStr}}</p>
                    </div>
                    <div class="notice-cont">
                        <p class="p-zy p1" style="cursor: pointer"
                           onclick="window.open('/community/communityDetail?detailId={{=value.id}}')">
                            {{=value.title}}</p>
                        <p class="p-cont">{{=value.content}}<span
                                class="sp-more">{{?value.content.length>=90}}...<em
                                class="spread">[展开全文]</em>{{?}}</span></p>
                        <p class="p-infor">
                            <span>消息来源：{{=value.communityName}}</span>
                            <span>发表时间：{{=value.time}}</span>
                        </p>
                    </div>
                    <%--<div class="notice-bm">--%>
                    <%--<button class="commit" itemId="{{=value.id}}">提交作业</button>--%>
                    <%--<span>已提交（{{=value.partInCount}}）人</span>--%>
                    <%--</div>--%>
                </div>
                {{~}}
            </script>
            <div class="com-left-s" id="homework">
            </div>
            <script type="text/template" id="meansTmpl">
                <div class="com-tit" id="means_all">学习资料</div>
                {{~it:value:index}}
                <div class="notice-container clearfix">
                    <div class="notice-holder">
                        <img src="{{=value.imageUrl}}" style="cursor: pointer"
                             onclick="window.open('/community/userData.do?userId={{=value.userId}}')">
                        <p>{{=value.nickName}}</p>
                        <p class="p1">{{=value.roleStr}}</p>
                    </div>
                    <div class="notice-cont">
                        <p class="p-zl p1" style="cursor: pointer"
                           onclick="window.open('/community/communityDetail?detailId={{=value.id}}')">
                            {{=value.title}}</p>
                        <p class="p-hw">
                            {{~value.attachements:attachment:i}}
                            <span class="sp-hw">{{=attachment.flnm}}<a><span
                                    onclick="download('{{=attachment.url}}','{{=attachment.flnm}}')" style="cursor: pointer">下载</span></a></span>
                            {{~}}
                        </p>
                        <p class="p-infor">
                            <span>消息来源：{{=value.communityName}}</span>
                            <span>发表时间：{{=value.time}}</span>
                        </p>
                    </div>
                </div>
                {{~}}
            </script>
            <script type="text/javascript">
                function download(url, fileName) {
                    location.href = "/commondownload/downloadFile.do?remoteFilePath=" + url + "&fileName=" + fileName;
                }
            </script>
            <div class="com-left-s" id="means">
            </div>
        </div>
        <div class="com-right">
            <div class="com-btn">
                <span class="btn-create" onclick="window.location.href='/communityCreate.do'">创建社区</span>
                <span class="btn-join" onclick="window.location.href='/communityJoin.do'">加入社区</span>
            </div>
            <div class="com-right-s clearfix">
                <div class="com-tit">我的社区<a href="/community/communitySet.do"><span class="com-set-my-btn"></span></a>
                </div>
                <ul class="ul-my-com" id="myCommunity">

                </ul>
                <script type="text/template" id="myCommunityTmpl">
                    {{~it:value:index}}
                    <li>
                        <a href="/community/communityPublish?communityId={{=value.id}}"><img src="{{=value.logo}}"></a>
                        <p>{{=value.name}}</p>
                    </li>
                    {{~}}
                </script>
            </div>
        </div>
    </div>
</div>

<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>

<%--环信消息通知--%>
<div class="hx-notice">
    <span class="sp1"></span>
    <span class="sp3">您有3条未读消息</span>
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
<%@ include file="../common/login.jsp" %>
<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/community/communityAllType.js', function (communityAllType) {
        communityAllType.init();
    });
</script>
<script type="text/javascript" src="/static/js/modules/core/0.1.0/fancyBox/jquery.fancybox.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        $(".fancybox").fancybox({
        });

    })
</script>
</body>
</html>
