<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/10/25
  Time: 11:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>加入社区</title>
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
        <%--<span>我的学习</span>--%>
        <%--<span>我的玩伴</span>--%>
    </div>
</div>
<div class="container">
    <div class="back-prev">
        <a onclick="window.history.go(-1)">< 返回上一页</a>
    </div>
    <div class="hd-cont-f hd-cont-f1">
        <div class="com-left">
            <div class="com-left-s">
                <div class="com-tit">加入社区</div>
                <ul class="ul-create">
                    <li class="clearfix">
                        <span>搜索社区</span>
                        <input type="text" id="searchId" placeholder="请输入社区名称关键字或社区ID">
                    </li>
                    <li class="clearfix">
                        <span></span>
                        <button class="btn-ok">查找</button>
                    </li>
                </ul>
                <div id="searchCommunty">
                    <div class="com-rlt" style="display: none">
                        <img src="/static/images/community/result.png">
                        <p class="p1">复兰社区</p>
                        <p class="p4">
                            <em>社区二维码</em>
                            <em>查看</em>
                            <em>复制</em>
                        </p>
                        <p class="p2">社区ID:fulaan666</p>
                        <p class="p3">社区简介：本社区仅限用于家长和教师的日常沟通、学习...</p>
                        <button>加入社区</button>
                    </div>
                </div>
                <script type="text/template" id="searchCommuntyTmpl">
                    {{~it:value:index}}
                    <div class="com-rlt">
                        <img src="{{=value.logo}}">
                        <p class="p1">{{=value.name}}</p>
                        <p class="p4">
                            <%--<em onclick="">社区二维码</em>--%>
                            <em class="lookUp" communityId="{{=value.id}}">查看</em>
                            <%--<em>复制</em>--%>
                        </p>
                        <p class="p2">社区ID:{{=value.searchId}}</p>
                        <p class="p3">社区简介：{{=value.desc}}</p>
                        <button class="join" communityId="{{=value.id}}">加入社区</button>
                    </div>
                    {{~}}
                </script>

            </div>
        </div>
        <div class="com-right">
            <div class="com-right-s clearfix">
                <div class="com-tit">我的社区<span class="com-set-my-btn"></span></div>
                <ul class="ul-my-com" id="myCommunity">
                </ul>
                <script type="text/template" id="myCommunityTmpl">
                    {{~it:value:index}}
                    <li>
                        <img src="{{=value.logo}}"
                             onclick="window.open('/community/communityPublish?communityId={{=value.id}}')">
                        <p>{{=value.name}}</p>
                    </li>
                    {{~}}
                </script>
            </div>
            <div class="com-right-s clearfix">
                <div class="com-tit">热门社区</div>
                <script type="text/template" id="hotCommunityTmpl">
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
                                <button class="joinCom" cid="{{=value.id}}">+加入社区</button>
                            </p>
                            <div class="train-f">
                                <div class="down-train"></div>
                            </div>
                        </div>
                    </li>
                    {{~}}
                </script>
                <ul class="ul-my-com" id="hotCommunity">
                    <%--<li>--%>
                    <%--<img src="/static/images/community/wuling.png">--%>
                    <%--<p>五菱宏光超跑车队</p>--%>
                    <%--</li>--%>
                    <%--<li>--%>
                    <%--<img src="/static/images/community/wuling.png">--%>
                    <%--<p>五菱宏光超跑车队</p>--%>
                    <%--</li>--%>
                    <%--<li>--%>
                    <%--<img src="/static/images/community/wuling.png">--%>
                    <%--<p>五菱宏光超跑车队</p>--%>
                    <%--</li>--%>
                    <%--<li>--%>
                    <%--<img src="/static/images/community/wuling.png">--%>
                    <%--<p>五菱宏光超跑车队</p>--%>
                    <%--</li>--%>
                    <%--<li>--%>
                    <%--<img src="/static/images/community/wuling.png">--%>
                    <%--<p>五菱宏光超跑车队</p>--%>
                    <%--</li>--%>
                </ul>
            </div>
        </div>
    </div>
    <div class="hd-cont-f hd-cont-f2">
    </div>
</div>

<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>

<%--环信消息通知--%>
<div class="hx-notice">
    <span class="sp2" id="hx-icon"></span>
    <span class="sp3" id="hx-msg-count">你有0条未读消息</span>
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

<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/community/communityJoin.js', function (communityJoin) {
        communityJoin.init();
    });
</script>
</body>
</html>
