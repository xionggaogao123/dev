<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>社区成员</title>
    <link rel="stylesheet" type="text/css" href="/static/css/community/community.css">
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/css/friend/nearby.css">
</head>
<body style="background: #f5f5f5;" communityId="${communityId}" fulanId="${fulanId}">
<%--==============头部===================--%>
<%@ include file="../common/head.jsp" %>
<div class="container">
    <%@ include file="_layout.jsp" %>
</div>
<div class="f-cont">
    <div class="hd-nav">
        <span id="my-community-span" class="hd-green-cur">我的社区</span>
        <span id="myActivity-span">我的活动</span>
    </div>
</div>
<div class="container">
    <div class="back-prev">
        <a onclick="window.history.go(-1)">< 返回上一页</a>
    </div>
    <div class="hd-cont-f hd-cont-f1">
        <div class="com-left">

            <div class="com-rlt com-rlt2" <c:if test="${menuItem==0}">style="display: none" </c:if>>
                <img class="img1" src="http://doc.k6kt.com/582effea3d4df91126ff2b9a.png">
                <p class="p1">复兰社区</p>
                <p class="p4">
                    <c:if test="${operation!=1&&fuflaan!=1}"><em class="em1 quit">退出社区</em></c:if>
                    <c:if test="${operation==1}"><em class="em1 em-edit">编辑社区</em></c:if>
                </p>
                <p class="p2">社区ID:100001</p>
                <p class="p3">社区简介：复兰青少年素质教育社区，为全国青少年群体量身打造，集兴趣交流、学习社交、素质拓展为一体，是中国最专业的青少年素质教育社区。</p>
            </div>


            <div class="com-left-s">
                <div class="com-tit">
                    管理社区成员
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
                            <img src="{{=value.avator}}" style="cursor: pointer" onclick="window.open('/community/userData.do?userId={{=value.userId}}')">
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
                            <img src="/static/images/community/mem_xx.png"><em
                                onClick="window.open('/webim/index?userId={{=value.userId}}')">发信息</em>
                            {{?value.playmateFlag==1}}
                            <img src="/static/images/community/delf.png"><em><span class="cancelFriend"
                                                                                   nickName="{{=value.nickName}}"
                                                                                   userId="{{=value.userId}}">取消玩伴</span></em>
                            {{??value.playmateFlag==2}}
                            <img src="/static/images/community/mem_gx.png"><em><span nickName="{{=value.nickName}}"
                                                                                     class="wait">等待回复</span></em>
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
<%--            <div class="com-right-s clearfix">
                <div class="com-tit">当前社区</div>
                <div class="com-now">
                    <img src="/static/images/communit y/result.png"
                         onclick="window.open('/community/communityPublish?communityId=${communityId}')" width="60px"
                         height="60px">
                    <p class="p1">弗兰社区</p>
                    <p class="p2">社区ID：${searchId}</p>
                </div>
            </div>--%>
            <div class="com-right-s">
                <div class="com-tit">我的社区<a href="/community/communitySet.do" target="_blank"><span
                        class="com-set-my-btn"></span></a></div>
                <ul class="ul-my-com clearfix" id="myCommunity">
                </ul>
            </div>
        </div>
    </div>

    <!-- 找玩伴 -->
    <div class="hd-cont-f hd-cont-f2">
        <p class="p1">
            <span id="my-community-1" class="hd-cf-cur2">已报名活动</span>
            <span id="my-community-2">已发布活动</span>
            <span id="my-community-3">已参加活动</span>
        </p>

        <div id="activity-signed-div">
            <img src="/static/images/community/no_data.jpg" hidden>
            <ul class="ul-hds" id="ul-activity-signed">
            </ul>
            <div class="new-page-links signed-page"></div>
        </div>

        <div id="activity-published-dev">
            <img src="/static/images/community/no_data.jpg" hidden>
            <ul class="ul-hds" id="ul-activity-published">
            </ul>
            <div class="new-page-links published-page"></div>
        </div>

        <div id="activity-attended-div">
            <img src="/static/images/community/no_data.jpg" hidden>
            <ul class="ul-hds" id="ul-activity-attended">
            </ul>
            <div class="new-page-links attended-page"></div>
        </div>

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
        <span>您确定要设置<em class="em-f">shawn</em>为副社长的权限吗？</span>
    </div>
    <div class="alert-btn">
        <button class="alert-btn-sure">确认</button>
        <button class="alert-btn-esc">取消</button>
    </div>
</div>

<div class="sign-alert si-s3 cancel-parter">
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

<div class="sign-alert si-s5 wait-reply">
    <p class="alert-title">提示<em>×</em></p>
    <div class="alert-main">
        <span>请等待<em class="em-f">shawn</em>的回复！</span>
    </div>
    <div class="alert-btn">
        <button class="alert-btn-sure" id="reply">确认</button>
        <button class="alert-btn-esc">取消</button>
    </div>
</div>

<div class="sign-alert si-s3 alert-diglog">
    <p class="alert-title">提示<em>×</em></p>
    <div class="alert-main">
        <span>确认要取消和<em class="em-f">shawn</em>的玩伴关系吗？</span>
    </div>
    <div class="alert-btn">
        <button class="alert-btn-sure">确认</button>
        <button class="alert-btn-esc">取消</button>
    </div>
</div>

<%--编辑社区窗口--%>
<div class="wind-com-edit" >
    <div class="p1">编辑社区<em id="cancel">×</em></div>
    <div class="p2 clearfix">
        <span class="sp1">社区名称：</span>
        <input id="communityName" type="text">
    </div>
    <div class="p2 clearfix">
        <span class="sp1">社区logo：</span>
        <p>
            <img src="http://www.fulaan.com/static/images/community/upload.png" id="communityLogo">
            <label for="image-upload" style="cursor: pointer"><span class="btn-up">上传图片</span></label>
            <span class="img-tx">你可以上传JPG、GIF或PNG格式的文件，文件大小不能超过2M</span>
            <input name="image-upload" id="image-upload" accept="image/*" size="1" type="file" hidden="hidden">
        </p>
    </div>
    <div class="p2 clearfix">
        <span class="sp1" id="communityDesc">社区简介：</span>
        <textarea></textarea>
    </div>
    <div class="p2 clearfix">
        <span class="sp1">是否公开：</span>
        <select id="selectOpen">
            <option class="op1" value="0">不公开</option>
            <option class="op2" value="1">公开</option>
        </select>
        <span class="gk-tx xt1" id="xt1">"不公开"社区，系统将不会将本社区作为热门社区推荐给其他用户</span>
        <span class="gk-tx xt2" id="xt2">"公开"社区，系统将会降本社区作为热门社区推荐给其他用户</span>
    </div>
    <div class="p2">
        <button class="btn-save">保存</button>
    </div>
</div>


<div class="bg"></div>
<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script type="text/javascript"
        src="/static/js/modules/core/0.1.0/jquery-upload/vendor/jquery.ui.widget.js?v=1"></script>
<script type="text/javascript" src="/static/js/modules/core/0.1.0/jquery-upload/jquery.fileupload.js"></script>
<script>
    seajs.use('/static/js/modules/community/communityMember.js', function (communityMember) {
        communityMember.init();
    });


    //上传图片
    $('#image-upload').fileupload({
        url: '/community/images.do',
        done: function (e, response) {
            if (response.result.code != '500') {
                var image = response.result.message[0].path;
                $('#communityLogo').attr('src', image);
            } else {
                alert("上传失败，请重新上传！");
            }
        },
        progressall: function (e, data) {
            $(this).closest('.tp-bm-box').find('.vote-vedio-container ul').html('正在上传...');
        }
    });
</script>
</body>

<script type="text/template" id="activityBox">
    {{~it:value:index}}
    <li>
        <button value="{{=value.acid}}">取消报名</button>
        <p class="p1">
            <span># {{? value.activityTheme != null }}
                     {{= value.activityTheme.data }}
                    {{?}}
                  #</span> {{=value.title}}
        </p>
        <p class="p2">{{=value.description}}</p>
    </li>
    {{~}}
</script>

</html>
