<%--
  Created by IntelliJ IDEA.
  User: qiangm
  Date: 2015/7/27
  Time: 11:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<html>
<head>
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn"/>
    <title>好友圈</title>

    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">

    <link href="/static_new/css/reset.css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="/static/css/activity/center.css"/>
    <link rel="stylesheet" type="text/css" href="/static_new/css/friendcircle/rome.css?v=1"/>
    <link rel="stylesheet" type="text/css" href="/static_new/css/friendcircle/wanban.css?v=1"/>
    <link href="/static_new/js/modules/core/0.1.0/fancyBox/jquery.fancybox.css?v=2015041602" rel="stylesheet"
          type="text/css" media="screen"/>
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>
<body actId="${actId}" inviteId="${inviteId}">

<%@ include file="../common_new/head.jsp" %>
<input type="hidden" value="" id="orgId">

<div id="content" class="clearfix">

    <div class="huodong_left">
        <!-- 左侧活动 -->
        <div class="huodong_leftT">
            <!-- 活动名称 -->
            <div class="mainmore">

            </div>
            <script type="text/template" id="huodong_leftT">
                <%--{{~it.data:value:index}}--%>
                <h5 id="activityTitle">{{=it.name}}</h5>
                <ul>
                    <li>时间：{{=it.strEventStartDate}}至{{=it.strEventEndDate}}</li>
                    <li>地点：{{=it.location}}</li>
                    <li>说明：{{=it.description}}</li>
                    <li>可参与：
                        {{?it.visible=="FRIEND"}}
                        仅好友可参加
                        {{??it.visible=="INVITE_FRIEND"}}
                        邀请好友可参加
                        {{??}}
                        全部可参加
                        {{?}}
                    </li>
                </ul>
                <img src="{{=it.coverImage}}" alt="">
                <%--{{~}}--%>
            </script>
            <!-- 活动名称 -->
            <dl id="acttypediv">


                <!-- 针对不同的身份进行筛选 -->
            </dl>
            <div class="gay"></div>
            <div class="hylb" style="margin-top: -120px;margin-left: -100px;">
                <p>邀请好友<span class="gb"></span></p>

                <div class="hylb_main clearfix">
                    <div class="hylb_left">
                        <div class="t">
                            <span>联系人：</span>
                            <input id="choosedFriend" type="text" disabled="disabled">
                        </div>
                        <div class="b">
                            <span>内容：</span>
                            <textarea style="border: 1px solid #DBDBDB;height: 102px;" id="pm-content"
                                      placeholder="快来参加活动吧！"></textarea>
                        </div>
                        <button id="sendMsg">确定</button>
                    </div>
                    <div class="hylb_right">
                        <ul class="haoyou" style="width: 100%">
                            <div class="bj">好友</div>
                            <!-- 好友列表 -->
                            <script id="friendList" type="text/template">
                                {{~it.data:value:index}}
                                <li class="frinedLi" userId="{{=value.id}}" userName="{{=value.userName}}"
                                    userImg="{{=value.imgUrl}}">{{=value.userName}}
                                </li>
                                {{~}}
                            </script>
                        </ul>
                    </div>
                </div>
            </div>

            <!-- 弹出层 -->
            <script id="actType" type="text/template">
                {{?it.type==0}}
                <dt>
                    <span class="lanl">此活动已删除</span>
                </dt>
                {{??it.type==1}}
                <dt>
                    <span class="lanl">此活动已结束</span>
                </dt>
                {{??it.type==2}}
                <dt>
                    <span class="tebie yqhy">邀请好友</span>

                    <span class="lan qxhd">删除活动</span>
                </dt>
                {{??it.type==3}}
                <dt>
                    <span class="tebie tchd">退出活动</span>
                </dt>
                {{??it.type==4}}
                <dt>
                    <span class="tebie cjhd">参加活动</span>
                </dt>
                {{??it.type==5}}
                <dt>
                    <span class="tebie jieshou">接受</span>
                    <span class="lan jujue">拒绝</span>
                    <span class="lan klyx">考虑一下</span>
                </dt>
                {{??it.type==6}}
                <dt>
                    <span class="tebie jieshou">接受</span>
                    <span class="lan jujue">拒绝</span>
                </dt>
                {{?}}
            </script>

                <span class="spl" id="attendCount">

                    <%--1/100--%>
                </span>
            <script id="attendCountJs" type="application/template">
                <span>参与人数：{{=it.att}}/{{=it.max}}</span>
            </script>
        </div>
        <div class="huodong_leftB">
            <ul class="clearfix" style="padding-top: 8px;display: inherit">
                <li class="dq">讨论区</li>
                <li>相册</li>
            </ul>

            <div class="cx">
                <div class="tlqxc tlq" style="padding-bottom: 8px;">

                </div>


                <!-- 列表 -->
                <script type="text/template" id="discussList">
                    <div class="tlqt clearfix">
                        <textarea id="discuss_con"></textarea>
							<span id="uploadPic">
							上传图片

							</span>
                        <input type="file" id="file" style="display: none"
                               name="file" size="1" accept="image/gif, image/jpeg, image/bmp,image/jpg"/>
                        <button id="publish">发表</button>
                        <div class="ImG" id="preview" style="display: none">
                            <img id="prevImg" src="" alt="">
                            <s></s>
                        </div>
                    </div>
                    {{~it.data:value:index}}
                    <div class="yiti clearfix">
                        <div class="floor clearfix">
                            <div class="lo">
                                <ol>
                                    <li>{{=it.disCount-index}}楼</li>
                                    <li><img src="{{=value.userImage}}" alt=""></li>
                                    <li class="um">{{=value.userName}}</li>
                                </ol>
                            </div>
                            <div class="rt clearfix">
                                <p>{{=value.content}}</p>
                                {{?value.image!=""}}
                                <%--<img src="{{=value.image}}" alt="">--%>
                                <a class="fancybox" href="{{=value.image}}" data-fancybox-group="home" title="预览"><img
                                        class="content-img" title="点击查看大图" src="{{=value.image}}"></a>
                                {{?}}
											<span>
												{{=value.time}}
												<em class="me1" id="ssss">回复</em>
												<em style="display:none;" class="me2">收起</em>
                                                {{?value.userId=="${sessionValue.id}"||$("#orgId").val()=="${sessionValue.id}"}}
                                                <em class="delDiscuss" imgSrc="{{=value.image}}" disId="{{=value.id}}">删除</em>
                                                {{?}}
											</span>
                            </div>
                        </div>
                        <dl class="clearfix">
                            <!-- 回复的消息列表 -->
                            <div class="xxlb">
                                {{~value.subDiscussList:value2:index2}}
                                <dd class="clearfix">
                                    <img src="{{=value2.userImage}}" alt="">

                                    <p><em>{{=value2.userName}}：</em>{{=value2.content}}
                                        {{?value2.userId=="${sessionValue.id}"||$("#orgId").val()=="${sessionValue.id}"}}
                                        <em class="delDiscuss" disId="{{=value2.id}}" imgSrc=""
                                            style="cursor: pointer;">删除</em>
                                        {{?}}
                                    </p>
                                </dd>
                                {{~}}
                            </div>
                            <!-- 回复的消息列表 -->
                            <dt class="clearfix" style="display:none;">
                                <textarea class="subdiscuss"></textarea>
                                <button class="subclassBtn" disid="{{=value.id}}">发表</button>
                            </dt>
                        </dl>
                    </div>
                    {{~}}
                </script>


                <!-- 列表 -->
                <div class="tlqxc xc clearfix" style="display:none;">

                </div>
                <script type="text/template" id="xc">
                    {{~it.data:value:index}}
                    {{?value.image!=""}}
                    <dl>
                        <dt><a class="fancybox" href="{{=value.image}}" data-fancybox-group="home" title="预览"><img
                                class="content-img" title="点击查看大图" src="{{=value.image}}"></a>
                        </dt>
                        <%--<dt><img src="{{=value.image}}" alt="">--%>
                        <dd>来自{{=value.userName}}</dd>
                    </dl>
                    {{?}}
                    {{~}}
                </script>
            </div>
        </div>
        <!-- 左侧活动 -->
    </div>

    <div class="huodong_right">
        <!-- 右侧活动 -->
        <div class="Active">
            <span class="zd">成为活动发起人</span>
            <span>邀请同学们一起来玩</span>
            <a href="/activity/activityMain.do?type=1">发起活动</a>
        </div>
        <ol class="fqr">

        </ol>
        <!-- 发起人 -->
        <script type="text/template" id="fqr">
            <li>发起人</li>
            <img src="{{=it.imgUrl}}" alt="">
            <span>{{=it.userName}}</span>
        </script>
        <!-- 发起人 -->
        <ul class="clearfix cycy">

        </ul>

        <!-- 参与成员 -->
        <script type="text/template" id="cycy">
            <h4>
                参与成员
                <span style="display:none;" class="sqb1">收起></span>
                <span class="sqb2">全部></span>
            </h4>
            {{~it.data:value:index}}
            <li><img src="{{=value.imgUrl}}" alt=""></li>
            {{~}}
        </script>
        <!-- 参与成员 -->
        <dl class="rmhd">
            <dt>热门活动<span class="changeHotAct">换一换</span></dt>
            <!-- 热门活动 -->
            <script type="text/template" id="rmhd">
                {{~it.data:value:index}}
                <dd class="clearfix">
                    <a href="/activity/activityView.do?actId={{=value.id}}">
                        <img src="{{=value.coverImage}}" style="width: 80px;height: 80px;" alt="">
                    </a>
                    <span class="mc">{{=value.name}}</span>
                    <span>参与：<s>{{=value.memberCount}}</s></span>
                    <span>讨论：{{=value.discuss}} 照片：{{=value.image}}</span>
                </dd>
                {{~}}
            </script>
            <!-- 热门活动 -->
        </dl>
        <!-- 右侧活动 -->
    </div>

</div>
<%@ include file="../common_new/foot.jsp" %>
<script src="/static_new/js/sea.js?v=1"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('activityView');
</script>
</body>
</html>
