<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/11/1
  Time: 10:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>社区成员</title>
    <link rel="stylesheet" type="text/css" href="/static/css/community/community.css">
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
    <%--<script type="text/javascript" src="js/comMember.js"></script>--%>
</head>
<body style="background: #f5f5f5;" communityId="${communityId}">
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
                <div class="com-tit">
                    社区成员
                    <c:if test="${operation==1}">
                        <div class="com-b comb1">
                            <button class="btn1 b1">批量管理</button>
                            <button class="btn1 b2">权限管理</button>
                        </div>
                        <div class="com-b comb2">
                            <button class="btn2 b1">删除选中</button>
                            <button class="btn2 b2">取消批量管理</button>
                        </div>
                        <div class="com-b comb3">
                            <button class="btn2 b1">设为副社长</button>
                            <button class="btn2 b2">取消副社长</button>
                            <button class="btn2 b3">取消选中</button>
                        </div>
                    </c:if>
                </div>
                <script type="text/template" id="memberTmpl">
                    {{~it:value:index}}
                    <li>
                        <div>
                            <img src="{{=value.avator}}">
                            <p>{{=value.roleStr}}</p>
                            {{?value.playmateFlag==1}}
                            <em></em>
                            {{?}}
                        </div>
                        <p class="p1"><span>{{=value.nickName}}</span>
                            {{?value.isOwner!=1}}
                            <img src="/static/images/community/mem_bj.png"
                                 userId="{{=value.userId}}"
                                 remarkId="{{=value.remarkId}}">
                            {{?}}
                        </p>
                        {{?value.tagType==0}}
                        <p class="p2"><em class="em3">该用户未设置标签</em></p>
                        {{??}}
                        <p class="p2">{{?value.isOwner==1}}标签{{??}}共同标签{{?}}：
                            {{~value.tags:tag:i}}
                            <em class="em1">{{=tag}}</em>&nbsp;
                            {{~}}
                            {{?value.tags.length>0}}
                            等
                            {{?}}
                        </p>
                        {{?}}
                        <p class="p2">共同玩伴:{{~value.playmate:playmate:i}}
                            <em class="em1">{{=playmate}}</em>
                            {{~}}{{?value.playmate.length>0}}等{{?}}{{=value.playmateCount}}人</p>
                        <p class="p3">
                            {{?value.isOwner==0}}
                            <img src="/static/images/community/mem_xx.png"><em onClick="window.open('/webim/index?userId={{=value.userId}}')" >发信息</em>
                            {{?value.playmateFlag==1}}
                            <img src="/static/images/community/delf.png"><em><span class="cancelFriend"
                                                                                     nickName="{{=value.nickName}}"
                                                                                     userId="{{=value.userId}}">取消玩伴</span></em>
                            {{??value.playmateFlag==2}}
                            <img src="/static/images/community/mem_gx.png"><em><span nickName="{{=value.nickName}}" class="wait">等待回复</span></em>
                            {{??}}
                            <img src="/static/images/community/mem_gx.png"><em><span class="applyFriend"
                                                                                     nickName="{{=value.nickName}}"
                                                                                     userId="{{=value.userId}}">加为玩伴</span></em>
                            {{?}}
                            {{?}}
                        </p>
                        <p class="com-gou1"></p>
                        <p class="com-gou2"></p>
                        <p class="memberId" memberId="{{=value.id}}" userId="{{=value.userId}}"></p>
                    </li>
                    {{~}}
                </script>
                <ul class="ul-member-list clearfix" id="member">

                </ul>
                <div class="new-page-links" id="memberPage"></div>
            </div>


            <script type="text/template" id="myCommunityTmpl">
                {{~it:value:index}}
                <li>
                    <a href="/community/communityPublish?communityId={{=value.id}}"><img src="{{=value.logo}}"></a>
                    <p>{{=value.name}}</p>
                </li>
                {{~}}
            </script>

        </div>
        <div class="com-right">
            <div class="com-right-s clearfix">
                <div class="com-tit">当前社区</div>
                <div class="com-now">
                    <img src="/static/images/communit y/result.png"
                         onclick="window.open('/community/communityPublish?communityId=${communityId}')" width="60px"
                         height="60px">
                    <p class="p1">弗兰社区</p>
                    <p class="p2">社区ID：1321465</p>
                </div>
            </div>
            <div class="com-right-s">
                <div class="com-tit">我的社区<a href="/community/communitySet.do" target="_blank"><span class="com-set-my-btn"></span></a></div>
                <ul class="ul-my-com clearfix" id="myCommunity">
                </ul>
            </div>
        </div>
    </div>
</div>
<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>

<%--环信消息通知--%>
<div class="hx-notice">
    <span class="sp2" id="hx-icon"></span>
    <span class="sp3" id="hx-msg-count">你有0条未读消息</span>
</div>

<!--权限start-->
    <div class="sign-alert si-s1">
        <p class="alert-title">提示<em>×</em></p>
        <div class="alert-main">
            <span>设置备注名</span>
            <input type="text" placeholder="备注名" id="remark">
        </div>
        <div class="alert-btn">
            <button class="alert-btn-sure" id="confirm">确认</button>
            <button class="alert-btn-esc">取消</button>
        </div>
    </div>
    <!--权限end-->

    <div class="sign-alert si-s2">
        <p class="alert-title">提示<em>×</em></p>
        <div class="alert-main">
            <span>您确定要设置<em class="em-f">shawn</em>为副社长的权限吗？</span>
        </div>
        <div class="alert-btn">
            <button class="alert-btn-sure">确认</button>
            <button class="alert-btn-esc">取消</button>
        </div>
    </div>
    <div class="sign-alert si-s3">
        <p class="alert-title">提示<em>×</em></p>
        <div class="alert-main">
            <span>确认要取消和<em class="em-f">shawn</em>的玩伴关系吗？</span>
        </div>
        <div class="alert-btn">
            <button class="alert-btn-sure" id="sureCancel">确认</button>
            <button class="alert-btn-esc">取消</button>
        </div>
    </div>

    <div class="sign-alert si-s4">
        <p class="alert-title">提示<em>×</em></p>
        <div class="alert-main">
            <span>确认要和<em class="em-f">shawn</em>成为玩伴吗？</span>
            <input type="text" id="content" value="">
        </div>
        <div class="alert-btn">
            <button class="alert-btn-sure" id="applyFriend">确认</button>
            <button class="alert-btn-esc">取消</button>
        </div>
    </div>
    <div class="sign-alert si-s5">
        <p class="alert-title">提示<em>×</em></p>
        <div class="alert-main">
            <span>请等待<em class="em-f">shawn</em>的回复！</span>
        </div>
        <div class="alert-btn">
            <button class="alert-btn-sure" id="reply">确认</button>
            <button class="alert-btn-esc">取消</button>
        </div>
    </div>
    <div class="bg"></div>
    <script src="/static/js/sea.js"></script>
    <script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
    <script>
        seajs.use('/static/js/modules/community/communityMember.js', function (communityMember) {
            communityMember.init();
        });
    </script>
</body>
</html>
