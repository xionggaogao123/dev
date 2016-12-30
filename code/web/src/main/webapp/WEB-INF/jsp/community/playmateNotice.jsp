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
</div>

    <div class="f-cont">
        <div class="hd-nav">
            <span class="hd-green-cur">我的社区</span>
            <%--<span>找学习</span>--%>
            <%--<span>找玩伴</span>--%>
        </div>
    </div>
<div class="container">
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
                <div class="com-tit">社区通知</div>
                <div class="tab-sys">
                    <span class="span-sys" id="sysinfocount">系统消息<i <c:if test="${xitongCount<=0}">style="display: none" </c:if>></i></span>
                    <span id="applycount">加入请求<i <c:if test="${applyComCount<=0}">style="display: none" </c:if>></i></span>
                </div>

                <ul class="ul-sysNotice" id="sysNotice" style="display: none">
                    <%--<li class="clearfix">--%>
                        <%--<span class="sp1">用户<em>哎哎哎</em>请求加入<em>复兰教育社区</em>（来自扫描二维码）<i class="i2">√ 同意</i><i class="i2">× 拒绝</i></span>--%>
                        <%--<span class="sp2">2016-11-21 15:00</span>--%>
                    <%--</li>--%>
                    <%--<li class="clearfix">--%>
                        <%--<span class="sp1">用户<em>哎哎哎</em>请求加入<em>复兰教育社区</em>（来自搜索社区ID）<i class="i3">您已拒绝TA的请求</i></span>--%>
                        <%--<span class="sp2">2016-11-21 15:00</span>--%>
                    <%--</li>--%>
                    <%--<li class="clearfix">--%>
                        <%--<span class="sp1">用户<em>哎哎哎</em>请求加入<em>复兰教育社区</em>（来自搜索社区ID）<i class="i3">您已通过TA的请求</i></span>--%>
                        <%--<span class="sp2">2016-11-21 15:00</span>--%>
                        <%--<span class="sp3">备注：我是AAA</span>--%>
                    <%--</li>--%>
                    <%--<li class="clearfix">--%>
                        <%--<span class="sp1">用户<em>哎哎哎</em>请求加入<em>复兰教育社区</em>（来自搜索社区ID）<i class="i4 i-retry">再次请求</i></span>--%>
                        <%--<span class="sp2">2016-11-21 15:00</span>--%>
                    <%--</li>--%>
                </ul>
                <script type="text/template" id="sysNoticeTmpl">
                    {{~it:value:index}}
                    <li class="clearfix">
                      {{?value.type==0}}
                        <span class="sp1">你已通过{{?value.way==1}}搜索ID{{??value.way==2}}扫描二维码{{?}}请求加入<em>{{=value.communityName}}</em>
                            {{?value.status==1}}
                              {{?value.reviewState==0}}
                                  <i class="i3">您的请求已被接受</i>
                              {{??}}
                               <i class="i4 i-retry" arg="{{=value.communityId}}" arg1="{{=value.communityName}}">再次申请</i><i class="i3">您的请求已被拒绝</i>
                              {{?}}
                            {{??}}
                             <i class="i3">等待回复</i>
                            {{?}}
                        </span>
                        <span class="sp2">{{=value.time}}</span>
                      {{??}}
                        <span class="sp1">用户<em>{{=value.userName}}</em>请求加入<em>{{=value.communityName}}</em>（来自{{?value.way==1}}搜索ID{{??value.way==2}}扫描二维码{{?}}）
                            {{?value.authority==1}}
                              {{?value.status==0}}
                              <i class="i3">你没有权限处理该信息</i>
                              {{??}}
                               {{?value.owner==1}}
                                 <i class="i3">您已{{?value.approvedStatus==0}}通过{{??value.approvedStatus==1}}拒绝{{?}}TA的请求</i>
                               {{??}}
                                 <i class="i3">{{=value.roleStr}}{{=value.reviewName}}已{{?value.approvedStatus==0}}通过{{??value.approvedStatus==1}}拒绝{{?}}TA的请求</i>
                               {{?}}
                              {{?}}
                            {{??value.authority==0}}
                              {{?value.status==0}}
                               <i class="i2 agreeApply" arg0="{{=value.userId}}" arg1="{{=value.reviewKeyId}}" arg2="{{=value.communityId}}" arg3="{{=value.userName}}">√ 同意</i><i class="i2 refuseApply">× 拒绝</i>
                              {{??}}
                                 {{?value.owner==1}}
                                 <i class="i3">您已{{?value.approvedStatus==0}}通过{{??value.approvedStatus==1}}拒绝{{?}}TA的请求</i>
                                 {{??value.owner==0}}
                                 <i class="i3">{{=value.roleStr}}{{=value.reviewName}}已{{?value.approvedStatus==0}}通过{{??value.approvedStatus==1}}拒绝{{?}}TA的请求</i>
                                 {{?}}
                              {{?}}
                            {{?}}
                        </span>
                        <span class="sp2">{{=value.time}}</span>
                        {{?value.applyMessage!=""}}
                        <span class="sp3">备注：{{=value.applyMessage}}</span>
                        {{?}}
                      {{?}}
                    </li>
                    {{~}}
                </script>


                <ul class="ul-sysNotice" id="mySystemInfo">
                </ul>
                <script type="text/template" id="mySystemInfoTmpl">
                    {{~it:value:index}}
                    {{?value.type==1}}
                    <li class="clearfix">
                        <span class="sp1">{{=value.roleStr}}<em>{{=value.nickName}}</em>退出{{=value.communityName}}</span>
                        <span class="sp2">{{=value.time}}</span>
                    </li>
                    {{??value.type==2}}
                    <li class="clearfix">
                        <span class="sp1">人事任命”— {{=value.roleStr}}<em>{{=value.nickName}}</em>成为{{=value.communityName}}新一任社长了</span>
                        <span class="sp2">{{=value.time}}</span>
                    </li>
                    {{??value.type==3}}
                    <li class="clearfix">
                        <span class="sp1">“人事任命”— 恭喜您成为{{=value.communityName}}的社长，您有权对本社区进行管理......</span>
                        <span class="sp2">{{=value.time}}</span>
                    </li>
                    {{??value.type==4}}
                    <li class="clearfix">
                        <span class="sp1">Hi，恭喜您{{=value.communityName}}创建成功，祝您学习愉快。如有问题，随时与我联系。</span>
                        <span class="sp2">{{=value.time}}</span>
                    </li>
                    {{??}}
                    <li class="clearfix">
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
                        <em class="em1" <c:if test="${menuItem==3}">style="display: none"</c:if>></em><em class="em2" <c:if test="${menuItem==3}">style="display: block;"</c:if>></em><span onclick="window.location.href='/community/mySystemInfo.do'">社区通知</span><button <c:if test="${menuItem==3}">class="btn2"</c:if>
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
<div class="sign-alert si-retry">
    <p class="alert-title">申请提示<em>×</em></p>
    <div class="alert-main">
        <span>您确认申请加入“<em id="retryName"></em>”吗？</span>
        <input type="text" id="beizhumsg" placeholder="备注" >
    </div>
    <div class="alert-btn">
        <button class="alert-btn-sure" id="applytip">确认</button>
        <button class="alert-btn-esc" id="retryCancel">取消</button>
    </div>
</div>
<!--权限end-->


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
