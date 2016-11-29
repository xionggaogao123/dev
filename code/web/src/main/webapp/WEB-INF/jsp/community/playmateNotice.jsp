<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/11/7
  Time: 15:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>我的玩伴</title>
    <link rel="stylesheet" type="text/css" href="/static/css/community/community.css">
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
    <%--<script type="text/javascript" src="js/comMember.js"></script>--%>
</head>
<body style="background: #f5f5f5;" menuItem="${menuItem}">
<%--==============头部===================--%>
<%@ include file="../common/head.jsp" %>
<div class="container">
    <%@ include file="_layout.jsp" %>
    <div class="f-cont">
        <div class="hd-nav">
            <span class="hd-green-cur">我的社区</span>
            <%--<span>找学习</span>--%>
            <%--<span>找玩伴</span>--%>
        </div>
    </div>
    <div class="hd-cont-f hd-cont-f1">
        <div class="com-left">
            <div class="com-left-s" id="friendInform">
                <div class="com-tit" id="title">玩伴通知</div>
                <script type="text/template" id="friendApplyTmpl">
                    {{~it:value:index}}
                    <li>
                        <img src="{{=value.avatar}}">
                        <p class="p4 accept" applyId="{{=value.id}}">接受</p>
                        <p class="p5 refuse" applyId="{{=value.id}}">拒绝</p>
                        <p class="p-li"></p>
                        <p class="p1">{{=value.nickName}}</p>
                        <p class="p2">TA的标签：<em>
                            {{~value.tags:tag:i}}
                            {{=tag}}
                            {{~}}
                        </em></p>
                    </li>
                    {{~}}
                </script>
                <ul class="ul-pm-notice" id="friendApply">
                </ul>

                <script type="text/template" id="myPartnersTmpl">
                    {{~it:value:index}}
                    <li>
                        <div>
                            <img src="{{=value.avator}}">
                            <p>{{=value.roleStr}}</p>
                            <em></em>
                        </div>
                        <p class="p1"><span>{{=value.nickName}}</span><img src="/static/images/community/mem_bj.png"
                                                                           userId="{{=value.userId}}"
                                                                           remarkId="{{=value.remarkId}}"></p>
                        {{?value.tagType==0}}
                        <p class="p2"><em class="em3">该用户未设置标签</em></p>
                        {{??}}
                        <p class="p2">共同标签：
                            {{~value.tags:tag:i}}
                            <em class="em1">{{=tag}}</em>、
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
                            <img src="/static/images/community/mem_xx.png"><em onClick="window.open('/webim/index?userId={{=value.userId}}')" >发信息</em>
                            <img src="/static/images/community/mem_gx.png"><em><span class="cancelFriend" nickName="{{=value.nickName}}" userId="{{=value.userId}}">取消玩伴</span></em>
                        </p>
                    </li>
                    {{~}}
                </script>
                <div class="new-page-links" id="friendInformPage"></div>
            </div>
            <div class="com-left-s" id="SysInfo" style="display: none">
                <div class="com-tit">系统消息</div>
                <ul class="ul-sysNotice" id="mySystemInfo">
                    <%--<li>--%>
                        <%--<span class="sp1">用户<em>shawn</em>退出喷子社区</span>--%>
                        <%--<span class="sp2">2016-12-21 15:56</span>--%>
                    <%--</li>--%>
                    <%--<li>--%>
                        <%--<span class="sp1">用户<em>shawn</em>退出喷子社区</span>--%>
                        <%--<span class="sp2">2016-12-21 15:56</span>--%>
                    <%--</li>--%>
                    <%--<li>--%>
                        <%--<span class="sp1"><i></i>"人事任命"—恭喜您成为喷子社区的社长，您有权对本社区进行管理....<a href="###">去试试></a></span>--%>
                        <%--<span class="sp2">2016-12-21 15:56</span>--%>
                    <%--</li>--%>
                </ul>
                <script type="text/template" id="mySystemInfoTmpl">
                    {{~it:value:index}}
                    {{?value.type==1}}
                    <li>
                        <span class="sp1">{{=value.roleStr}}<em>{{=value.nickName}}</em>退出{{=value.communityName}}</span>
                        <span class="sp2">{{=value.time}}</span>
                    </li>
                    {{??value.type==2}}
                    <li>
                        <span class="sp1">人事任命”— {{=value.roleStr}}<em>{{=value.nickName}}</em>成为{{=value.communityName}}新一任社长了</span>
                        <span class="sp2">{{=value.time}}</span>
                    </li>
                    {{??value.type==3}}
                    <li>
                        <span class="sp1">“人事任命”— 恭喜您成为{{=value.communityName}}的社长，您有权对本社区进行管理......</span>
                        <span class="sp2">{{=value.time}}</span>
                    </li>
                    {{??value.type==4}}
                    <li>
                        <span class="sp1">Hi，恭喜您{{=value.communityName}}创建成功，祝您学习愉快。如有问题，随时与我联系。</span>
                        <span class="sp2">{{=value.time}}</span>
                    </li>
                    {{??}}
                    <li>
                        <span class="sp1">恭喜您成为{{=value.communityName}}的社长，您有权对本社区进行管理......</span>
                        <span class="sp2">{{=value.time}}</span>
                    </li>
                    {{?}}
                    {{~}}
                </script>
                <div class="new-page-links" id="sysInfoPage"></div>
            </div>

        </div>
        <div class="com-right">
            <div class="com-right-s clearfix">
                <div class="com-tit">全部</div>
                <ul class="ul-comr-all">
                    <li class="li1 <c:if test="${menuItem==2}">blue-cur</c:if>" style="cursor: pointer">
                        <em class="em1" <c:if test="${menuItem==2}">style="display: none"</c:if>></em><em class="em2" <c:if test="${menuItem==2}">style="display: block;"</c:if>></em><span onclick="window.location.href='/community/myPartners.do'">我的玩伴</span><button <c:if test="${menuItem==2}">class="btn2"</c:if>
                        <c:if test="${menuItem==1||menuItem==3}">class="btn1"</c:if>>${friendCount}</button>
                    </li>
                    <li class="li2" style="cursor: pointer">
                        <em class="em1" ></em><em class="em2" ></em><span onclick="window.open('/community/friendList.do')">我关注的人</span><button class="btn1">${concernCount}</button>
                    </li>
                    <li class="li3 <c:if test="${menuItem==1}">blue-cur</c:if>" style="cursor: pointer">
                        <em class="em1" <c:if test="${menuItem==1}">style="display: none"</c:if>></em><em class="em2" <c:if test="${menuItem==1}">style="display: block;"</c:if>></em><span onclick="window.location.href='/community/playmateNotice.do'">玩伴通知</span><button <c:if test="${menuItem==1}">class="btn2"</c:if>
                        <c:if test="${menuItem==2||menuItem==3}">class="btn1"</c:if>>${friendApplyCount}</button>
                    </li>
                    <li class="li4 <c:if test="${menuItem==3}">blue-cur</c:if>" style="cursor: pointer">
                        <em class="em1" <c:if test="${menuItem==3}">style="display: none"</c:if>></em><em class="em2" <c:if test="${menuItem==3}">style="display: block;"</c:if>></em><span onclick="window.location.href='/community/mySystemInfo.do'">系统消息</span><button <c:if test="${menuItem==3}">class="btn2"</c:if>
                        <c:if test="${menuItem==2||menuItem==1}">class="btn1"</c:if>>${systemInfoCount}</button>
                    </li>
                </ul>
            </div>
            <div class="com-right-s">
                <div class="com-tit">我的社区</div>
                <ul class="ul-my-com clearfix" id="myCommunity">
                </ul>
            </div>
        </div>
        <script type="text/template" id="myCommunityTmpl">
            {{~it:value:index}}
            <li>
                <img src="{{=value.logo}}" onclick="window.open('/community/communityPublish?communityId={{=value.id}}')">
                <p>{{=value.name}}</p>
            </li>
            {{~}}
        </script>
    </div>
</div>
<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>

<%--环信消息通知--%>
<div class="hx-notice">
    <span class="sp2" id="hx-icon"></span>
    <span class="sp3" id="hx-msg-count">您有0条未读消息</span>
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
        <span >您确定要设置<em class="em-f">shawn</em>为副社长的权限吗？</span>
    </div>
    <div class="alert-btn">
        <button class="alert-btn-sure">确认</button>
        <button class="alert-btn-esc">取消</button>
    </div>
</div>
<div class="sign-alert si-s3">
    <p class="alert-title">提示<em>×</em></p>
    <div class="alert-main">
        <span >确认要取消和<em class="em-f">shawn</em>的玩伴关系吗？</span>
    </div>
    <div class="alert-btn">
        <button class="alert-btn-sure" id="sureCancel">确认</button>
        <button class="alert-btn-esc">取消</button>
    </div>
</div>
<div class="bg"></div>

<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/community/playmateNotice.js', function (playmateNotice) {
        playmateNotice.init();
    });
</script>
</body>
</html>