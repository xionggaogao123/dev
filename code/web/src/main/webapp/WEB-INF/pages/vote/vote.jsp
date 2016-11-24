<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>投票选举</title>
    <meta name="description" content="">
    <meta name="author" content=""/>
    <meta name="copyright" content=""/>
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/js/modules/core/0.1.0/fancyBox/jquery.fancybox.css?v=2015041602" rel="stylesheet"
          type="text/css" media="screen"/>
    <link href="/static_new/css/reset.css?v=1" rel="stylesheet"/>
    <link href="/static/css/homepage.css" type="text/css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css"/>


    <link href="/static_new/css/vote/votes.css?v=1" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="/static_new/css/friendcircle/rome.css?v=1"/>

    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->

    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
    <script type="text/javascript"
            src="/static_new/js/modules/core/0.1.0/jquery-upload/vendor/jquery.ui.widget.js?v=1"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.fileupload.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/js/swfobject.js"></script>
    <script type="text/javascript" src="/static/js/recorder.js"></script>
    <script type="text/javascript" src="/static/js/main.js"></script>

    <script src="/static/js/shareforflipped.js" type="text/javascript"></script>
    <script type="text/javascript" src="/static/js/swfobject.js"></script>
    <script type="text/javascript" src="/static/plugins/cameraform/htdocs/webcam.js"></script>
    
    <script>
        var isFlash = false;

        function getVideoType(url) {
            if (url.indexOf('polyv.net') > -1) {
                return "POLYV";
            }
            if (url.endWith('.swf')) {
                return 'FLASH';
            }
            return 'HLS';
        }

        function playTheMovie(url) {
            var videoSourceType = getVideoType(url);
            $('.bg').fadeIn('fast');
            var $player_container = $("#elect-player");
            $player_container.fadeIn();

            if (videoSourceType == "POLYV") {
                $('#sewise-div').hide();
                $('#elect-player-div').show();
                var player = polyvObject('#elect-player-div').videoPlayer({
                    'width': '800',
                    'height': '450',
                    'vid': url.match(/.+(?=\.swf)/)[0].replace(/.+\//, '')
                });
            } else {
                $('#elect-player-div').hide();
                $('#sewise-div').show();
                try {
                    SewisePlayer.toPlay(url, "投票选举", 0, true);
                } catch (e) {
                    playerReady.videoURL = url;
                    isFlash = true;
                }
            }
        }

        function playerReady(name) {
            if (isFlash) {
                SewisePlayer.toPlay(playerReady.videoURL, "投票选举", 0, false);
            }
        }
        function coverError(img) {
            var uploadState = img.getAttribute('state');
            if (uploadState == 0) {
                img.src = "/img/converting_small.png";
            }
        }

    </script>

</head>
<body>
<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->
<div id="elect-player" class="player-container">
    <div id="elect-player-div" class="player-div"></div>
    <div id="sewise-div" style="display: none; width: 800px; height: 450px;">
        <script type="text/javascript" src="/static/plugins/sewiseplayer/sewise.player.min.js"></script>
    </div>
    <span class="player-close-btn"></span>
    <script type="text/javascript">
        SewisePlayer.setup({
            server: "vod",
            type: "m3u8",
            skin: "vodFlowPlayer",
            logo: "none",
            lang: "zh_CN",
            topbardisplay: 'enable',
            videourl: ''
        });
    </script>
</div>
<!--#content-->
<div id="content" class="clearfix">
    <!--.col-left-->
    <%@ include file="../common_new/col-left.jsp" %>
    <!--/.col-left-->
    <!--.col-right-->
    <!--广告-->
    <c:choose>
        <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
        </c:when>
        <c:otherwise>
            <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
        </c:otherwise>
    </c:choose>
    <!--广告-->
    <div class="col-right">
        <input type="hidden" value="${role}" id="role">
        <input type="hidden" value="${userId}" id="userId">
        <!--.tab-col-->
        <div class="vote-cont">
            <c:choose>
                <c:when test="${roles:isHeadmaster(sessionValue.userRole)||roles:isManager(sessionValue.userRole)||
                roles:isTeacher(sessionValue.userRole)}">


                    <div class="vote-form">
                        <dl>
                            <dd>
                                <em>标题</em>
                                <input type="text" class="input-text" id="voteName" placeholder="请输入投票标题，不得超过35个字"
                                       onfocus="(this).placeholder=''" onblur="(this).placeholder='请输入投票标题，不得超过35个字'">
                            </dd>
                            <dd>
                                <em>投票规则</em>
                                <textarea id="descriptforvote"></textarea>
                            </dd>
                            <dd>
                                <em>范围</em>

                                <div class="dd-div voteCatogery" id="classListShow">

                                </div>
                                <script type="application/template" id="classListTempJs">
                                        <c:choose>
                                            <c:when test="${roles:isHeadmaster(sessionValue.userRole)||roles:isManager(sessionValue.userRole)}">
                                                <label class="allSchoolChoose">
                                                    <input type="checkbox" id="selectAllSchool">全校
                                                </label>
                                            </c:when>

                                        </c:choose>
                                    {{~it.data:value:index}}
                                    <label>
                                        <input type="checkbox" value="{{=value.id}}" title="{{=value.className}}"
                                               class="view-catogery">{{=value.className}}
                                    </label>
                                    {{~}}
                                </script>
                            </dd>
                            <dd>
                                <em>参选角色</em>

                                <div class="dd-div">
                                    <label>
                                        <input type="checkbox" id="teacherEligible" class="cxjs_role">老师
                                    </label>
                                    <label>
                                        <input type="checkbox" id="studentEligible" class="cxjs_role">学生
                                    </label>
                                    <label>
                                        <input type="checkbox" id="parentEligible" class="cxjs_role">家长
                                    </label>
                                    <label>
                                        <input type="checkbox" id="leaderEligible" class="cxjs_role">校领导
                                    </label>
                                </div>
                            </dd>
                            <dd>
                                <em>指定候选人</em>

                                <div class="dd-div">
                                    <input type="checkbox" class="zdhxr">
                                    <button class="tjhxr">添加候选人</button>
                                    <div class="div-zd">
                                    </div>

                                </div>
                            </dd>
                            <dd>
                                <em>公布结果</em>

                                <div class="dd-div">
                                    <input type="checkbox" class="gbjg" checked>
                                    <span>勾选后选举结果所有人可见</span>
                                </div>
                            </dd>
                            <dd>
                                <em>投票角色</em>

                                <div class="dd-div">
                                    <label>
                                        <input type="checkbox" id="teacherVotable" class="tp_role">老师
                                    </label>
                                    <label>
                                        <input type="checkbox" id="studentVotable" class="tp_role">学生
                                    </label>
                                    <label>
                                        <input type="checkbox" id="parentVotable" class="tp_role">家长
                                    </label>
                                    <label>
                                        <input type="checkbox" id="leaderVotable" class="tp_role">校领导
                                    </label>
                                </div>
                            </dd>
                            <dd>
                                <em>每人票数</em>
                                <input type="number" class="input" id="ballotCount" value="3" min="1"
                                       onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                                       onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'0')}else{this.value=this.value.replace(/\D/g,'')}"/>
                                    <%--onKeypress="if(this.value.length==0 && event.keyCode == 48) event.returnValue = false;else if (event.keyCode < 48 || event.keyCode > 57) event.returnValue = false;">--%>
                            </dd>
                            <dd>
                                <em>开始日期</em>
                                <input type="text" class="input" id="starttimeforvote" readonly="true">
                            </dd>
                            <dd>
                                <em>结束日期</em>
                                <input type="text" class="input" id="closetimeforvote" readonly="true">
                                <button class="btn-fb">发布</button>
                            </dd>

                        </dl>
                    </div>
                </c:when>

            </c:choose>
            <script type="application/template" id="voteListTempJs">
                {{~it.data:value:index}}
                <div class="vote-li" eid="{{=value.id}}" publisher="{{=value.publisher}}" style="cursor: pointer">
                    <dl class="vote-li2">
                        <dd>{{=value.name}}
                            <div class="vote-jx">{{?value.voting}}进行中{{??}}已结束{{?}}</div>
                        </dd>
                        <dd><em>投票规则：</em><span class="spem-vote span-break">{{=value.description}}</span></dd>
                        <dd><em>范围：</em><span class="spem-vote">
                        {{?value.classIds=="" || value.classIds == null || value.classIds.length ==0}}
                        全校
                        {{??}}
                            {{for(var v1 in value.classes){}}
                            <span>{{=value.classes[v1].className}}</span>
                            {{}}}
                        {{?}}</span>
                        </dd>
                        <dd><em>参选范围：</em>
                            {{?!value.teacherEligible && !value.studentEligible && !value.parentEligible &&
                            !value.leaderEligible}}
                            <span>自定义</span>
                            {{??}}
                            {{?value.teacherEligible}}
                            <span>老师</span>
                            {{?}}
                            {{?value.studentEligible}}
                            <span>学生</span>
                            {{?}}
                            {{?value.parentEligible}}
                            <span>家长</span>
                            {{?}}
                            {{?value.leaderEligible}}
                            <span>校领导</span>
                            {{?}}
                            {{?}}
                        </dd>
                        <dd><em>投票范围：</em>
                            {{?value.teacherVotable}}
                            <span>老师</span>
                            {{?}}
                            {{?value.studentVotable}}
                            <span>学生</span>
                            {{?}}
                            {{?value.parentVotable}}
                            <span>家长</span>
                            {{?}}
                            {{?value.leaderVotable}}
                            <span>校领导</span>
                            {{?}}
                        </dd>
                        <dd><em>每人票数：</em>{{=value.ballotCount}}票</dd>
                        <dd><em>公布结果：</em>{{?value.publish==0}}公布{{??}}不公布{{?}}</dd>
                        <dd><em>开始日期：</em>{{=value.startDate}}</dd>
                        <dd><em>结束日期：</em>{{=value.endDate}}</dd>
                        <dd><em>发起人：</em>{{=value.publishUser.userName}}</dd>
                        {{?value.voting}}
                        {{?!value.candidates || value.candidates.length == 0}}
                        <dd>
                            <em>候选人：</em>

                            <div class="div-hxr">
                                <span class="no-hxr">目前没有候选人哦~~快快报名吧!</span>
                            </div>
                            <div style="clear:both"></div>
                        </dd>
                        {{??}}
                        <dd>
                            <em>候选人：</em>

                            <div class="div-hxr votePeopleShow">
                                {{~value.candidates:v3:index3}}
                                <div class="hxr-chi hxr-list" uid="{{=v3.user.id}}">
                                    <img src="{{=v3.user.imgUrl}}">
                                            <span class="inf-hxr">
                                                <i>{{=v3.name}}</i><br/>
                                                <i class="inf-text">
                                                    {{?value.manageType==0}}
                                                    {{?v3.user.role}}
                                                        {{?v3.user.role & 8 }}
                                                            {{?v3.user.positionDec==null}}校领导{{??}}{{=v3.user.positionDec}}{{?}}
                                                        {{??v3.user.role & 2}}
                                                            {{?v3.user.positionDec==null}}老师{{??}}{{=v3.user.positionDec}}{{?}}
                                                        {{??v3.user.role & 4}}
                                                            <%--家长--%>{{=v3.classInfo.gradeName}}{{=v3.classInfo.className}}
                                                        {{??v3.user.role & 1}}
                                                            {{?v3.classInfo.className!=null}}
                                                                {{=v3.classInfo.gradeName}}{{=v3.classInfo.className}}
                                                            {{?}}
                                                        {{??}}
                                                            校领导
                                                        {{?}}
                                                    {{?}}
                                                    {{??value.manageType==1}}
                                                    {{=v3.user.schoolName}}
                                                    {{?}}</i>
                                            </span>
                                    <em>{{?value.publish==0||value.publish==10}}{{=v3.ballots?v3.ballots.length:0}}票{{?}}</em>
                                    {{?value.voteRight}}
                                    {{?v3.voted}}
                                    <span class="tp-yt" style="display: inline-block"></span>
                                    {{??value.ballotCount > value.voted && value.begin}}
                                    <button eid="{{=value.id}}" class="ttyp-bt">投ta一票</button>
                                    {{??value.ballotCount > value.voted}}
                                    <button>未开始投票</button>
                                    {{?}}
                                    {{?}}
                                </div>
                                {{~}}
                            </div>
                            <div style="clear:both"></div>
                        </dd>
                        {{?}}
                        {{?value.voting}}
                        {{?value.signed}}
                        <dd>
                            <button class="btn-tc" eid="{{=value.id}}">退出参选</button>
                        </dd>
                        {{??value.eligRight}}
                        <dd>
                            <button class="btn-bm" eid="{{=value.id}}">报名</button>
                        </dd>
                        {{?}}
                        {{?}}
                        {{??}}
                        {{?value.candidates ==null || value.candidates.length == 0}}
                        <dd>
                            <div class="no-hxr">Oh no,竟然没有人参加竞选~~</div>
                        </dd>
                        {{??}}
                        {{~value.candidates:v2:index2}}
                        <dd>
                            <img src="{{=v2.user.imgUrl}}" alt="" class="img-hxr"/>
                            <ul class="ul-hxr">
                                <li>{{=v2.name}}</li>
                                <li>{{?v2.user.role}}
                                    {{?v2.user.role &8}}
                                    {{?v2.user.positionDec==null}}校领导{{??}}{{=v2.user.positionDec}}{{?}}
                                    {{??v2.user.role &2}}
                                    {{?v2.user.positionDec==null}}老师{{??}}{{=v2.user.positionDec}}{{?}}
                                    {{??v2.user.role & 4}}
                                    <%--家长--%>{{=v2.classInfo.gradeName}}{{=v2.classInfo.className}}
                                    {{??v2.user.role & 1}}
                                    {{?v2.classInfo.className!=null}}
                                        {{=v2.classInfo.gradeName}}{{=v2.classInfo.className}}
                                    {{?}}
                                    {{??}}
                                    校领导
                                    {{?}}
                                    {{?}}
                                </li>
                                <li>
                                    {{?value.publish==0||value.publish==10}}
                                    {{?!v2.ballots}}
                                    <div class="div-ps0 div-ps">
                                        <span>0票</span>
                                    </div>
                                    {{??}}
                                    <div class="div-ps" style="width:{{=v2.percent}}%;">
                                        <span>{{=v2.ballots?v2.ballots.length:0}}票</span>
                                    </div>
                                    {{?}}
                                    {{?}}
                                </li>
                            </ul>
                        </dd>
                        {{~}}
                        {{?}}
                        {{?}}
                        <div style="clear:both"></div>
                    </dl>
                    <div class="bm-slide tp-bm-box" eid="{{=value.id}}">
                        <textarea placeholder="请输入参选内容，点击下方按钮还可以添加图片、语音和视频;视频上传后需要等待转码完成后才可以观看哦" class="input area-xy"
                                  onfocus="(this).placeholder=''"
                                  onblur="(this).placeholder='请输入参选内容，点击下方按钮还可以添加图片、语音和视频;视频上传后需要等待转码完成后才可以观看哦'"></textarea>

                        <div class="bm-slide1">

                            <label class="file_add" for="file_attach_{{=value.id}}_{{=index}}"><i></i></label>
                            <i class="showRecord type0" eid="{{=value.id}}" index="{{=index}}"></i>
                            <label class="video_add" for="video_attach_{{=value.id}}_{{=index}}"><i></i></label>
                            <button class="applyBtn bm-btn">报名</button>
                            <div class="sanjiao"
                                 style="width: 0;height: 0;
                             border-left: 8px solid transparent;
                             border-right: 8px solid transparent;
                             border-bottom: 10px solid rgb(100,100,100);
                             position:absolute;display: inline-block;top:0px;left:10px;">

                            </div>
                            <div id="myContent-{{=value.id}}-{{=index}}">


                            </div>
                        </div>
                        <input id="file_attach_{{=value.id}}_{{=index}}" type="file" name="file" value="添加图片"
                               size="1" style="width: 0; height: 0; opacity: 0" class="imgforvote"
                               accept="image/*">
                        <input id="video_attach_{{=value.id}}_{{=index}}" type="file" name="file" value="添加视频"
                               size="1" style="width: 0; height: 0; opacity: 0" class="videoforvote"
                               accept="video/*">

                        <div class="attcmt-container">
                            <div class="vote-img-container">
                                <ul></ul>
                            </div>
                            <div class="vote-audio-container">
                                <ul></ul>
                            </div>
                            <div class="vote-vedio-container">
                                <ul></ul>
                            </div>
                        </div>
                    </div>
                </div>
                {{~}}
            </script>
            <script type="application/template" id="votePeopleTempJs">
                {{~it.data.candidates:v3:index3}}
                <div class="hxr-chi hxr-list" uid="{{=v3.user.id}}">
                    <img src="{{=v3.user.imgUrl}}">
                                            <span class="inf-hxr">
                                                <i>{{=v3.name}}</i><br/>
                                                <i class="inf-text">{{?v3.user.role}}
                                                    {{?v3.user.role & 8}}
                                                        {{?v3.user.positionDec==null}}校领导{{??}}{{=v3.user.positionDec}}{{?}}
                                                    {{??v3.user.role &2 }}
                                                        {{?v3.user.positionDec==null}}老师{{??}}{{=v3.user.positionDec}}{{?}}
                                                    {{??v3.user.role & 4}}
                                                        <%--家长--%>{{=v3.classInfo.gradeName}}{{=v3.classInfo.className}}
                                                    {{??v3.user.role & 1}}
                                                        {{?v3.classInfo.className!=null}}
                                                            {{=v3.classInfo.gradeName}}{{=v3.classInfo.className}}
                                                        {{?}}
                                                    {{??}}
                                                        校领导
                                                    {{?}}
                                                    {{?}}</i>
                                            </span>
                    <em>{{?it.data.publish==0||it.data.publish==10}}{{=v3.ballots?v3.ballots.length:0}}票{{?}}</em>
                    {{?it.data.voteRight}}
                        {{?it.data.voted}}
                            <span class="tp-yt" style="display: inline-block"></span>
                        {{??it.data.ballotCount > it.data.voted && it.data.begin}}
                            <button eid="{{=it.data.id}}" class="ttyp-bt">投ta一票</button>
                        {{??it.data.ballotCount > it.data.voted}}
                            <button>未开始投票</button>
                        {{?}}
                    {{?}}
                </div>
                {{~}}
            </script>
            <div class="border">

            </div>
            <!--.page-links-->
            <div class="page-paginator">
                <span class="first-page">首页</span>
                            <span class="page-index">
                            <span class="active">1</span>
                            <span>2</span>
                            <span>3</span>
                            <span>4</span>
                            <span>5</span>
                            <i>···</i>
                            </span>
                <span class="last-page">尾页</span>
            </div>
            <!--/.page-links-->
        </div>
        <!--/.tab-col-->

        <div class="vote-detail">
            <span class="span-back">< 返回</span>

            <div class="vote-li" id="detailShow">

            </div>
            <script type="application/template" id="detailTempJs">
                <dl>
                    <dd>{{=it.data.name}}
                        {{?it.userId == it.data.publisher}}
                        <span class="delete-elect" eid="{{=it.data.id}}">删除</span>
                        {{?}}
                        <div class="vote-jx" style="cursor: default">{{?it.data.voting}}进行中{{??}}已结束{{?}}</div>
                    </dd>
                    <dd><em>投票规则：</em><span class="spem-vote span-break">{{=it.data.description}}</span></dd>
                    <dd><em>范围：</em><span class="spem-vote">
                        {{?it.data.classIds=="" || it.data.classIds == null || it.data.classIds.length ==0}}
                        全校
                        {{??}}
                        {{for(var v1 in it.data.classes){}}
                        <span>{{=it.data.classes[v1].className}}</span>
                        {{}}}
                        {{?}}</span>
                    </dd>
                    <dd><em>参选范围：</em>
                        {{?!it.data.teacherEligible && !it.data.studentEligible && !it.data.parentEligible &&
                        !it.data.leaderEligible}}
                        <span>自定义</span>
                        {{??}}
                        {{?it.data.teacherEligible}}
                        <span>老师</span>
                        {{?}}
                        {{?it.data.studentEligible}}
                        <span>学生</span>
                        {{?}}
                        {{?it.data.parentEligible}}
                        <span>家长</span>
                        {{?}}
                        {{?it.data.leaderEligible}}
                        <span>校领导</span>
                        {{?}}
                        {{?}}
                    </dd>
                    <dd><em>投票范围：</em>
                        {{?it.data.teacherVotable}}
                        <span>老师</span>
                        {{?}}
                        {{?it.data.studentVotable}}
                        <span>学生</span>
                        {{?}}
                        {{?it.data.parentVotable}}
                        <span>家长</span>
                        {{?}}
                        {{?it.data.leaderVotable}}
                        <span>校领导</span>
                        {{?}}
                    </dd>
                    <dd><em>每人票数：</em>{{=it.data.ballotCount}}票</dd>
                    <dd><em>公布结果：</em>{{?it.data.publish==0}}公布{{??}}不公布{{?}}</dd>
                    <dd><em>开始日期：</em>{{=it.data.startDate}}</dd>
                    <dd><em>结束日期：</em>{{=it.data.endDate}}</dd>
                    <dd><em>发起人：</em>{{=it.data.publishUser.userName}}</dd>
                    {{?it.data.voting}}
                    {{?!it.data.candidates || it.data.candidates.length == 0}}
                    <dd>
                        <em>候选人：</em>

                        <div class="div-hxr">
                            <span class="no-hxr">目前没有候选人哦~~快快报名吧!</span>
                        </div>
                        <div style="clear:both"></div>
                    </dd>
                    {{??}}
                    <dd>
                        <em>候选人：</em>

                        <div class="div-hxr">
                            {{~it.data.candidates:v3:index3}}
                            <div class="hxr-chi" uid="{{=v3.user.id}}">
                                <img src="{{=v3.user.imgUrl}}">
                                            <span class="inf-hxr">
                                                <i>{{=v3.name}}</i><br/>
                                                <i class="inf-text">{{?v3.user.role}}
                                                    {{?v3.user.role & 8}}
                                                        {{?v3.user.positionDec==null}}校领导{{??}}{{=v3.user.positionDec}}{{?}}
                                                    {{??v3.user.role &2}}
                                                        {{?v3.user.positionDec==null}}老师{{??}}{{=v3.user.positionDec}}{{?}}
                                                    {{??v3.user.role & 4}}
                                                        <%--家长--%>{{=v3.classInfo.gradeName}}{{=v3.classInfo.className}}
                                                    {{??v3.user.role & 1}}
                                                        {{?v3.classInfo.className!=null}}
                                                            {{=v3.classInfo.gradeName}}{{=v3.classInfo.className}}
                                                        {{?}}
                                                    {{??}}
                                                        校领导
                                                    {{?}}
                                                    {{?}}</i>
                                            </span>
                                <em>{{?it.data.publish==0||it.data.publish==10}}{{=v3.ballots?v3.ballots.length:0}}票{{?}}</em>
                                {{?it.data.voteRight}}
                                {{?v3.voted}}
                                <span class="tp-yt" style="display: inline-block"></span>
                                {{??it.data.ballotCount > it.data.voted && it.data.begin}}
                                <button eid="{{=it.data.id}}" class="ttyp-bt">投ta一票</button>
                                {{??it.data.ballotCount > it.data.voted}}
                                <button>未开始投票</button>
                                {{?}}
                                {{?}}
                            </div>
                            {{~}}
                        </div>
                        <div style="clear:both"></div>
                    </dd>
                    {{?}}
                    {{?it.data.voting}}
                    {{?it.data.signed}}
                    <dd>
                        <button class="btn-tc" eid="{{=it.data.id}}">退出参选</button>
                    </dd>
                    {{??it.data.eligRight}}
                    <dd>
                        <button class="btn-bm" eid="{{=it.data.id}}">报名</button>
                    </dd>
                    {{?}}
                    {{?}}
                    {{??}}
                    {{?it.data.candidates ==null || it.data.candidates.length == 0}}
                    <dd>
                        <div class="no-hxr">Oh no,竟然没有人参加竞选~~</div>
                    </dd>
                    {{??}}
                    {{~it.data.candidates:v2:index2}}
                    <dd>
                        <img src="{{=v2.user.imgUrl}}" alt="" class="img-hxr"/>
                        <ul class="ul-hxr">
                            <li>{{=v2.name}}</li>
                            <li>{{?v2.user.role}}
                                {{?v2.user.role &8}}
                                    {{?v2.user.positionDec==null}}校领导{{??}}{{=v2.user.positionDec}}{{?}}
                                {{??v2.user.role &2}}
                                    {{?v2.user.positionDec==null}}老师{{??}}{{=v2.user.positionDec}}{{?}}
                                {{??v2.user.role & 4}}
                                <%--家长--%>{{=v2.classInfo.gradeName}}{{=v2.classInfo.className}}
                                {{??v2.user.role & 1}}
                                    {{?v2.classInfo.className!=null}}
                                        {{=v2.classInfo.gradeName}}{{=v2.classInfo.className}}
                                    {{?}}
                                {{??}}
                                校领导
                                {{?}}
                                {{?}}
                            </li>
                            <li>
                                {{?it.data.publish==0||it.data.publish==10}}
                                {{?!v2.ballots}}
                                <div class="div-ps0 div-ps">
                                    <span>0票</span>
                                </div>
                                {{??}}
                                <div class="div-ps" style="width:{{=v2.percent}}%;">
                                    <span>{{=v2.ballots?v2.ballots.length:0}}票</span>
                                </div>
                                {{?}}
                                {{?}}
                            </li>
                        </ul>
                    </dd>
                    {{~}}
                    {{?}}
                    {{?}}
                    <div style="clear:both"></div>
                </dl>
                <div class="bm-slide tp-bm-box" eid="{{=it.data.id}}">
                    <textarea placeholder="请输入参选内容，点击下方按钮还可以添加图片、语音和视频;视频上传后需要等待转码完成后才可以观看哦" class="input area-xy"
                              onfocus="(this).placeholder=''"
                              onblur="(this).placeholder='请输入参选内容，点击下方按钮还可以添加图片、语音和视频;视频上传后需要等待转码完成后才可以观看哦'"></textarea>

                    <div class="bm-slide1">

                        <label class="file_add" for="file_attach3_{{=it.data.id}}"><i class="file-i"></i></label>
                        <i class="showRecord type1" eid="{{=it.data.id}}"></i>
                        <label class="video_add" for="video_attach3_{{=it.data.id}}"><i></i></label>
                        <button class="applyBtn bm-btn">报名</button>
                        <div class="sanjiao"
                             style="width: 0;height: 0;
                             border-left: 8px solid transparent;
                             border-right: 8px solid transparent;
                             border-bottom: 10px solid rgb(100,100,100);
                             position:absolute;display: inline-block;top:0px;left:10px;">

                        </div>
                        <div id="myContent1-{{=it.data.id}}">


                        </div>
                    </div>
                    <input id="file_attach3_{{=it.data.id}}" type="file" name="file" value="添加图片"
                           size="1" style="width: 0; height: 0; opacity: 0" class="imgforvote"
                           accept="image/*">
                    <input id="video_attach3_{{=it.data.id}}" type="file" name="file" value="添加视频"
                           size="1" style="width: 0; height: 0; opacity: 0" class="videoforvote"
                           accept="video/*">

                    <div class="attcmt-container">
                        <div class="vote-img-container">
                            <ul></ul>
                        </div>
                        <div class="vote-audio-container">
                            <ul></ul>
                        </div>
                        <div class="vote-vedio-container">
                            <ul></ul>
                        </div>
                    </div>
                </div>
            </script>
            <div id="detailBorder">

            </div>
            <script type="application/template" id="introListTempJs">
                {{~it.data.candidates:value:index}}
                <div class="li-hxr clearfix">
                    <img class="info-user-img" src="{{=value.user.imgUrl}}">

                    <div class="li-hxr-cont1">
                        <p class="hxr-p1">{{=value.name}}</p>

                        <p>{{=value.signTime}}</p>
                    </div>
                    <div class="li-hxr-cont2">
                        <!-- 竞选宣言 -->
                        {{? value.manifesto != null && value.manifesto != ''}}
                        <p style="word-break: break-all">{{=value.manifesto}}</p>
                        {{?}}
                        <dl class="jx">
                            <!-- 图片 -->
                            {{? value.picUrls != null && value.picUrls.length > 0}}
                            {{~value.picUrls : v1:index1}}
                            <dd>
                                <a class="fancybox" target="_blank" href="{{=v1}}" data-fancybox-group="elect"
                                   title="预览">
                                    <img class="candidate-img" src="{{=v1}}"></a>
                            </dd>
                            {{~}}
                            {{?}}
                            <!-- 音频 -->
                            {{? value.voiceUrl != null && value.voiceUrl.length > 0}}
                            <dd>
                                <a class="voice" onclick="playVoice('{{=value.voiceUrl}}');" url="{{=value.voiceUrl}}"
                                   value="{{=value.videoId}}"><img src="/img/yuyin.png"
                                                                   style="width:160px;height:22px;">播放</a>
                            </dd>
                            {{?}}
                            <!-- 视频 -->
                            {{? value.videoId!=null}}
                            <dd style="position: relative">
                                <img
                                        class="candidate-vedio-cover"
                                        src="{{?value.video.imageUrl}}{{=value.video.imageUrl}}{{??}}/img/K6KT/video-cover.png{{?}}"><img
                                    src="/img/play.png" class="video-play-btn"
                                    onclick="playTheMovie('{{=value.video.url}}')" vid="{{=value.video.url}}"/></li>
                            </dd>
                            {{?}}

                        </dl>
                        {{?(value.user.id==it.userId || it.data.publisher==it.userId) && it.data.voting}}
                        <button class="btn-xg">修改</button>
                        {{?}}
                    </div>
                    <div class="bm-slide tp-bm-box" eid="{{=it.data.id}}">
                        <textarea placeholder="请输入参选内容，点击下方按钮还可以添加图片、语音和视频;视频上传后需要等待转码完成后才可以观看哦" class="input area-xy"
                                  onfocus="(this).placeholder=''"
                                  onblur="(this).placeholder='请输入参选内容，点击下方按钮还可以添加图片、语音和视频;视频上传后需要等待转码完成后才可以观看哦'">{{?value.manifesto!=null&&value.manifesto!=""}}{{=value.manifesto}}{{?}}</textarea>

                        <div class="bm-slide1">

                            <label class="file_add" for="mfile_attach_{{=it.data.id}}_{{=index}}"><i></i></label>
                            <i class="showRecord type2" eid="{{=it.data.id}}" index="{{=index}}"></i>
                            <label class="video_add" for="mvideo_attach_{{=it.data.id}}_{{=index}}"><i></i></label>
                            <button class="applyBtn updateOK" uid="{{=value.id}}">确认修改</button>
                            <div class="sanjiao"
                                 style="width: 0;height: 0;
                             border-left: 8px solid transparent;
                             border-right: 8px solid transparent;
                             border-bottom: 10px solid rgb(100,100,100);
                             position:absolute;display: inline-block;top:0px;left:10px;">

                            </div>
                            <div id="myContent2-{{=it.data.id}}-{{=index}}">


                            </div>
                        </div>
                        <input id="mfile_attach_{{=it.data.id}}_{{=index}}" type="file" name="file" value="添加图片"
                               size="1" style="width: 0; height: 0; opacity: 0" class="imgforvote"
                               accept="image/*">
                        <input id="mvideo_attach_{{=it.data.id}}_{{=index}}" type="file" name="file" value="添加视频"
                               size="1" style="width: 0; height: 0; opacity: 0" class="videoforvote"
                               accept="video/*">

                        <div class="attcmt-container">
                            <div class="vote-img-container">
                                <ul>
                                    {{? value.picUrls != null && value.picUrls.length > 0}}
                                    {{~value.picUrls : v1:index1}}
                                    <li><a class="fancybox"  target="_blank" href="{{=v1}}" data-fancybox-group="home"
                                           title="预览">
                                        <img class="candidate-img" src="{{=v1}}">
                                    </a><i class="fa fa-times blog-img-delete"></i></li>
                                    {{~}}
                                    {{?}}
                                </ul>
                            </div>
                            <div class="vote-audio-container">
                                <ul>
                                    {{? value.voiceUrl != null && value.voiceUrl.length > 0}}
                                    <p><a class="voice" onclick="playVoice('{{=value.voiceUrl}}');"
                                          url="{{=value.voiceUrl}}">
                                        <img src="/img/yuyin.png" style="width:160px;height:22px;">播放</a>
                                        <a style="margin-left:20px;" onclick="$(this).closest('p').remove();">
                                            <img src="/img/dustbin.png"></a>
                                    </p>
                                    {{?}}
                                </ul>
                            </div>
                            <div class="vote-vedio-container">
                                <ul>
                                    {{? value.videoId!=null}}
                                    <li data-id="{{=value.videoId}}">
                                        <img class="candidate-vedio-cover"
                                             src="{{?value.video.imageUrl}}{{=value.video.imageUrl}}{{??}}/img/K6KT/video-cover.png{{?}}">
                                        <img src="/img/play.png" class="video-play-icon"/><i
                                            class="fa fa-times blog-img-delete">
                                    </i></li>
                                    {{?}}
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                {{~}}
            </script>
        </div>
        <!--/.tab-col-->
        <!--==========报名未输入内容提示弹窗st=============-->

        <div class="bm-k">
            <dl>
                <dd class="bm-dd1">
                    提示<i>×</i>
                </dd>
                <dd class="bm-dd2">
                    请输入竞选宣言
                </dd>
                <dd class="bm-dd3">
                    <button>确定</button>
                </dd>
            </dl>
        </div>
        <!--==========报名未输入内容提示弹窗ed=============-->

        <!--==========退出参选弹窗st======================-->
        <div class="tc-out">
            <p>提示<i>×</i></p>

            <div class="tc-out1">确定退出参选？</div>
            <div class="tc-out2">
                <button class="tc-yes">确定</button>
                <button class="tc-no">取消</button>
            </div>
        </div>
        <!--==========退出参选弹窗ed======================-->

        <!--==========检查投票设置表单内容st======================-->
        <div class="check-form">
            <p>提示<i>×</i></p>

            <div class="check-f1"></div>
            <div class="check-f2">
                <button>确定</button>
            </div>
        </div>
        <!--==========检查投票设置表单内容ed======================-->

        <div class="check-form2">
            <p>提示<i>×</i></p>
            <div class="check2-f1"></div>
            <div class="check2-f2">
                <button>确定</button>
            </div>
        </div>

        <!--==========删除投票st======================-->
        <div class="delete-wote">
            <p>提示<i>×</i></p>

            <div class="dwote1"></div>
            <div class="dwote2">
                <button class="ok">确定</button>
                <button class="dwote-no">取消</button>
            </div>
        </div>
        <!--==========删除投票ed======================-->
        <!-- =========添加候选人弹窗st==================== -->
        <div class="tc-tj">
            <p>指定候选人<i>×</i></p>

            <div class="tj-left">
                <em class="em">联系人：</em>
                <span>
                <div id="recipient">
                </div>
                </span>
                <script type="application/template" id="selectedUserTempJs">
                    {{~it.data:value:index}}
                    <div style="display: inline-block;line-height: 10px;">
                        <em>{{=value.name}}</em>
                        <i ui="{{=value.id}}" class="fa fa-times-circle fdelete-candidate"></i>
                    </div>
                    {{~}}
                </script>
                <button id="selectOk">确定</button>
            </div>
            <div class="tj-right">
                <dl id="contractShow">


                </dl>
                <script type="application/template" id="contractTempJs">
                    <dd><span class="span-stu">学生/同学</span>
                        <ul class="ul-stu">
                            {{~it.data.studentsList:value:index}}
                            <li ui="{{=value.id}}" un="{{=value.userName}}" class="selUser">
                                {{=value.nickName}}({{=value.userName}})
                            </li>
                            {{~}}
                        </ul>
                    </dd>
                    <dd><span class="span-edu">教育局</span>
                        <ul class="ul-edu">
                            {{~it.data.bureauList:value:index}}
                            <li ui="{{=value.id}}" un="{{=value.userName}}" class="selUser">
                                {{=value.nickName}}({{=value.userName}})
                            </li>
                            {{~}}
                        </ul>
                    </dd>
                    <dd><span class="span-par">家长</span>
                        <ul class="ul-par">
                            {{~it.data.parentsList:value:index}}
                            <li ui="{{=value.id}}" un="{{=value.userName}}" class="selUser">
                                {{?value.userName!=""}}
                                {{=value.nickName}}({{=value.userName}})
                                {{?}}
                            </li>
                            {{~}}
                        </ul>
                    </dd>
                    <dd><span class="span-pre">领导</span>
                        <ul class="ul-pre">
                            {{~it.data.presidentList:value:index}}
                            <li ui="{{=value.id}}" un="{{=value.userName}}" class="selUser">
                                {{=value.nickName}}({{=value.userName}})
                            </li>
                            {{~}}
                        </ul>
                    </dd>
                    <dd><span class="span-fri">好友</span>
                        <ul class="ul-fri">
                            {{~it.data.friendList:value:index}}
                            <li ui="{{=value.id}}" un="{{=value.userName}}" class="selUser">
                                {{=value.nickName}}({{=value.userName}})
                            </li>
                            {{~}}
                        </ul>
                    </dd>
                    <dd><span class="span-tea">老师</span>
                        <ul class="ul-tea">
                            {{~it.data.teachersList:value:index}}
                            <li ui="{{=value.id}}" un="{{=value.userName}}" class="selUser">
                                {{=value.nickName}}({{=value.userName}})
                            </li>
                            {{~}}
                        </ul>
                    </dd>
                </script>
            </div>
        </div>
        <!-- =========添加候选人弹窗ed==================== -->


        <!--半透明背景-->
        <div class="bg"></div>
    </div>
</div>
<!--/.col-right-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js?v=1"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('vote');
</script>
</body>
</html>

