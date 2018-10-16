<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>复兰教育社区</title>
    <link rel="stylesheet" type="text/css" href="/static/css/forum/forum.css">
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
    <script type="text/javascript" src="/static/js/modules/forum/ZeroClipboard.js"></script>

    <style>
        .lunt-index-hide {
            display: none;
        }

        .header-cont .ha4 {
            background: #E8E8E8;
            color: #3764a0;
        }
    </style>

    <script type="text/javascript">
        $(document).ready(function () {
            $('#toplogin').click(function () {
                $('.notice-dl').text('中国最专业的青少年素质教育社区!').css({
                    'color': '#FF4918',
                    'font-weight': 'bold',
                    'font-size': '24px'
                });
            })
            $('#loginText').click(function () {
                $('.notice-dl').text('中国最专业的青少年素质教育社区!').css({
                    'color': '#FF4918',
                    'font-weight': 'bold',
                    'font-size': '24px'
                });
            })
        })

    </script>

    <!--旧版轮播-->
    <%--<script> $(function () {--%>
    <%--var container = $('#container');--%>
    <%--var list = $('#list');--%>
    <%--var buttons = $('#buttons span');--%>
    <%--var prev = $('#prev');--%>
    <%--var next = $('#next');--%>
    <%--var index = 1;--%>
    <%--var len = 4;--%>
    <%--var interval = 3000;--%>
    <%--var timer;--%>


    <%--function animate (offset) {--%>
    <%--var left = parseInt(list.css('left')) + offset;--%>
    <%--if (offset>0) {--%>
    <%--offset = '+=' + offset;--%>
    <%--}--%>
    <%--else {--%>
    <%--offset = '-=' + Math.abs(offset);--%>
    <%--}--%>
    <%--list.animate({'left': offset}, 500, function () {--%>
    <%--if(left > -200){--%>
    <%--list.css('left', -720 * len);--%>
    <%--}--%>
    <%--if(left < (-720 * len)) {--%>
    <%--list.css('left', -720);--%>
    <%--}--%>
    <%--});--%>
    <%--}--%>

    <%--function showButton() {--%>
    <%--buttons.eq(index-1).addClass('on').siblings().removeClass('on');--%>
    <%--}--%>

    <%--function play() {--%>
    <%--timer = setTimeout(function () {--%>
    <%--next.trigger('click');--%>
    <%--play();--%>
    <%--}, interval);--%>
    <%--}--%>
    <%--function stop() {--%>
    <%--clearTimeout(timer);--%>
    <%--}--%>

    <%--next.bind('click', function () {--%>
    <%--if (list.is(':animated')) {--%>
    <%--return;--%>
    <%--}--%>
    <%--if (index == 4) {--%>
    <%--index = 1;--%>
    <%--}--%>
    <%--else {--%>
    <%--index += 1;--%>
    <%--}--%>
    <%--animate(-720);--%>
    <%--showButton();--%>
    <%--});--%>

    <%--prev.bind('click', function () {--%>
    <%--if (list.is(':animated')) {--%>
    <%--return;--%>
    <%--}--%>
    <%--if (index == 1) {--%>
    <%--index = 4;--%>
    <%--}--%>
    <%--else {--%>
    <%--index -= 1;--%>
    <%--}--%>
    <%--animate(720);--%>
    <%--showButton();--%>
    <%--});--%>

    <%--buttons.each(function () {--%>
    <%--$(this).bind('click', function () {--%>
    <%--if (list.is(':animated') || $(this).attr('class')=='on') {--%>
    <%--return;--%>
    <%--}--%>
    <%--var myIndex = parseInt($(this).attr('index'));--%>
    <%--var offset = -720 * (myIndex - index);--%>

    <%--animate(offset);--%>
    <%--index = myIndex;--%>
    <%--showButton();--%>
    <%--})--%>
    <%--});--%>

    <%--container.hover(stop, play);--%>

    <%--play();--%>

    <%--});--%>
    <%--</script>--%>

    <%--轮播js--%>
    <script>
        $(function () {
            var imgContainer = $('#imgContainer');
            var list = $('#imgList');
            var upDown = $('#upDown')
            var buttonss = $('#ulButtons li');
            var prev = $('#prev');
            var next = $('#next');
            var index = 1;
            var len = 4;
            var interval = 3000;
            var timer;


            function animate(offset) {
                var top = parseInt(list.css('top')) + offset;
                if (offset > 0) {
                    offset = '+=' + offset;
                }
                else {
                    offset = '-=' + Math.abs(offset);
                }
                list.animate({'top': offset}, 300, function () {
                    if (top > -200) {
                        list.css('top', -295 * len);
                    }
                    if (top < (-295 * len)) {
                        list.css('top', -295);
                    }
                });
            }

            function showButton() {
                $('#ulButtons li').eq(index - 1).addClass('on').siblings().removeClass('on');
            }

            function play() {
                timer = setTimeout(function () {
                    next.trigger('click');
                    play();
                }, interval);
            }

            function stop() {
                clearTimeout(timer);
            }

            next.bind('click', function () {
                if (list.is(':animated')) {
                    return;
                }
                if (index == 4) {
                    index = 1;
                }
                else {
                    index += 1;
                }
                animate(-295);
                showButton();
            });

            prev.bind('click', function () {
                if (list.is(':animated')) {
                    return;
                }
                if (index == 1) {
                    index = 4;
                }
                else {
                    index -= 1;
                }
                animate(295);
                showButton();
            });


            buttonss.each(function () {
                $(this).bind('click', function () {
                    if (list.is(':animated') || $(this).attr('class') == 'on') {
                        return;
                    }
                    var myIndex = parseInt($(this).attr('index'));
                    var offset = -295 * (myIndex - index);

                    animate(offset);
                    index = myIndex;
                    showButton();
                })
            });

            imgContainer.hover(stop, play);
            buttonss.hover(stop, play);
            upDown.hover(stop, play);
            play();

        });
    </script>
    <%--轮播js--%>


</head>
<body login="${login}" style="background: #f7f7f7">
<%@ include file="../common/head.jsp" %>
<div class="container clearfix">
    <div class="cont-left">
        <div class="scroll-contianer" id="imgContainer">
            <div class="scroll-cont" style="top:-295px;" id="imgList">
            </div>
        </div>
        <div class="type-nav">
            <span class="span-cur">论坛</span>
            <span>热帖</span>
            <button>我要发帖</button>
        </div>
        <div id="sectionlist">
            <c:forEach items="${sections}" var="section" varStatus="status">
                <div class="div-list clearfix">
                    <a target="_blank" href="/forum/postIndex.do?pSectionId=${section['fSectionId']}">
                        <div class="bk-li bkl${status.index + 1}">
                            <img src="/static/images/forum/img_ltbk0${status.index + 1}.png">
                            <p class="p1">版主：${section['sectionName']}</p>
                            <p class="p2">${section['name']}</p>
                            <p class="p3">${section['memoName']}</p>
                            <p class="p4">
                                主题数：${section['themeCount']}&nbsp;&nbsp;&nbsp;&nbsp;帖数：${section['postCount']}</p>
                            <p class="p5">
                                <span class="sp2">${section['totalScanCount']}</span>
                                <span class="sp3"></span>
                                <span class="sp2">${section['totalCommentCount']}</span>
                                <span class="sp1"></span>
                            </p>
                        </div>
                    </a>
                </div>
            </c:forEach>
        </div>
        <div class="cont-sqrt hot">
            <ul class="ul-hot-post" id="listPost">
            </ul>
        </div>
    </div>
    <div class="cont-right">
        <ul class="ul-control" id="ulButtons">
        </ul>
        <div class="up-down" id="upDown">
            <span class="sp1 fl" id="prev">
                <img src="/static/images/forum/entrance_up.png">
            </span>
            <span class="sp2 fr" id="next">
                <img src="/static/images/forum/entrance_down.png">
            </span>
        </div>
        <div class="green-qd" style="display: none">
            <span id="dateR"></span>&nbsp;<span id="dayR"></span>
            <em <c:if test="${login == false}">style="display: none;" </c:if>><c:if test="${signIn == false}">
            <span id="qDao">每日签到</span>
            </c:if><c:if test="${signIn == true}">已签到</c:if></em>
        </div>
        <div class="clearfix" <c:if test="${login == true}">style="margin: 0 auto; display: none;"</c:if>>
            <div class="btn-dl-red" id="loginText">登录</div>
            <div class="btn-zc-red" onclick="window.open('/account/register.do')">注册</div>
        </div>
        <div class="personal-c clearfix" <c:if test="${login == false}">style="display: none;" </c:if>>
            <img onclick="window.open('/forum/personal.do?personId=${userId}')" style="cursor: pointer"
            <c:if test="${avatar ==null||avatar =='http://doc.k6kt.com/'}">
                 src="/static/images/forum/head_picture.png"</c:if>
            <c:if test="${avatar !=null&&avatar !='http://doc.k6kt.com/'}"> src="${avatar}"
                 </c:if>width="53" height="53">
            <span class="span1" onclick="window.open('/forum/userCenter/user.do')"
                  style="cursor: pointer">${userName}</span>

            <span class="span2">
            <em>用户等级 : </em>
            <span class="span21" onclick="window.open('/forum/forumLevel.do')">
                <c:if test="${stars==1}">
                    <img src="/static/images/forum/level_tong.png">
                    <em>lv.1</em>
                </c:if>
                <c:if test="${stars==2}">
                    <img src="/static/images/forum/level_tong.png">
                    <img src="/static/images/forum/level_tong.png">
                    <em>lv.2</em>
                </c:if>
                <c:if test="${stars==3}">
                    <img src="/static/images/forum/level_sliver.png">
                    <em>lv.3</em>
                </c:if>
                <c:if test="${stars==4}">
                    <img src="/static/images/forum/level_sliver.png">
                    <img src="/static/images/forum/level_tong.png">
                    <em>lv.4</em>
                </c:if>
                <c:if test="${stars==5}">
                    <img src="/static/images/forum/level_sliver.png">
                    <img src="/static/images/forum/level_tong.png">
                    <img src="/static/images/forum/level_tong.png">
                    <em>lv.5</em>
                </c:if>
                <c:if test="${stars==6}">
                    <img src="/static/images/forum/level_sliver.png">
                    <img src="/static/images/forum/level_sliver.png">
                    <em>lv.6</em>
                </c:if>
                <c:if test="${stars==7}">
                    <img src="/static/images/forum/level_sliver.png">
                    <img src="/static/images/forum/level_sliver.png">
                    <img src="/static/images/forum/level_tong.png">
                    <em>lv.7</em>
                </c:if>
                 <c:if test="${stars==8}">
                     <img src="/static/images/forum/level_sliver.png">
                     <img src="/static/images/forum/level_sliver.png">
                     <img src="/static/images/forum/level_tong.png">
                     <img src="/static/images/forum/level_tong.png">
                     <em>lv.8</em>
                 </c:if>
                 <c:if test="${stars==9}">
                     <img src="/static/images/forum/level_golden.png">
                     <em>lv.9</em>
                 </c:if>
                 <c:if test="${stars==10}">
                     <img src="/static/images/forum/level_golden.png">
                     <img src="/static/images/forum/level_tong.png">
                     <em>lv.10</em>
                 </c:if>
                 <c:if test="${stars==11}">
                     <img src="/static/images/forum/level_golden.png">
                     <img src="/static/images/forum/level_tong.png">
                     <img src="/static/images/forum/level_tong.png">
                     <em>lv.11</em>
                 </c:if>
                 <c:if test="${stars==12}">
                     <img src="/static/images/forum/level_golden.png">
                     <img src="/static/images/forum/level_sliver.png">
                     <em>lv.12</em>
                 </c:if>
            </span>
        </span>
            <em class="em-set" style="cursor: pointer" onclick="window.open('/forum/userCenter/user.do')"></em>
            <div class="clearfix"></div>
            <div class="personal-btn">
                <p>积分：${forumScore}&nbsp;&nbsp;&nbsp;&nbsp;经验值：${forumExperience}</p>
                <div class="div-notice"><span id="notice">通知</span>
                    <span class="span1"><em><</em></span>
                    <p>
                        <span id="message">消息</span>
                        <span id="friendApply">好友请求</span>
                        <span id="system">系统提醒</span>
                    </p>
                </div>
                <button style="cursor: pointer" id="tieZi">帖子</button>
                <button style="cursor: pointer" class="fwtg">邀请</button>
                <button onclick="window.open('/forum/userCenter/collection.do')">收藏</button>
                <button style="cursor: pointer" id="quitLog">退出</button>
                <button onclick="window.open('/forum/userCenter/manageCenter.do')" <c:if
                        test="${userPermission < 100}"> style="cursor: pointer;display: none" </c:if>>管理中心
                </button>
            </div>
        </div>
        <div class="div-bkrk clearfix" style="display: none">
            <p>论坛搜索</p>
            <input onkeydown="Event()" class="input-ss" type="text">
            <button class="btn-ss">搜索</button>
        </div>
        <script type="text/javascript">
            function Event() {
                var e = window.event || arguments.callee.caller.arguments[0];
                if (e && e.keyCode == 13) {
                    var regular = $('.input-ss').val();
                    window.open('/forum/postSearch.do?regular=' + encodeURI(encodeURI(regular)));
                }
            }
        </script>
        <div class="ltwh" <c:if test="${login==false}">style="display: none" </c:if>>
            <div class="iwant" id="getTask">领取今日任务</div>
        </div>
        <div class="ltwh" >
            <div class="wh-title mission-to">资讯</div>
            <ul class="forin-zx" id="creamData">
                
                <%--<li>> 五毛拿走不谢！</li>--%>
                <%--<li>> 五毛拿走不谢！五毛拿走不谢！五毛拿走不谢！</li>--%>
                <%--<li>> 五毛拿走不谢！</li>--%>
                <%--<li>> 五毛拿走不谢！</li>--%>
                <%--<li>> 五毛拿走不谢！</li>--%>
                <%--<li>> 五毛拿走不谢！</li>--%>
            </ul>
            <script type="text/template" id="creamDataTmpl">
                {{~it:value:index}}
                <li style="cursor:pointer;" onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}&personId={{=value.personId}}')">> {{=value.postTitle}}！</li>
                {{~}}
            </script>
        </div>
        <div class="ltwh" style="display: none">
            <div class="wh-title clearfix">
                <span value="1">活跃会员排行榜</span>
                <span value="2" class="wh-cur">新入会员排行榜</span>
            </div>
            <div id="lsU">
            </div>
        </div>
    </div>
</div>
<div class="newpost-wind">
    <div class="nav-lt">论坛导航<em>×</em></div>
    <p class="p1">社区论坛&nbsp;>&nbsp;<span id="spanItem">晒才艺</span><a id="SectionItem" name="575d4d2c0cf2ca0383166bb9">[进入版块]</a>
        <button id="sendPost">发新帖</button>
    </p>
    <ul class="ul-newpost">
        <li class="li1">
            <ul class="ul-s" id="uls">
                <c:forEach items="${sections}" var="section">
                    <li><a href="/forum/postIndex.do?pSectionId=${section['fSectionId']}">${section['name']}</a></li>
                </c:forEach>
            </ul>
        </li>
        <li class="li"></li>
        <li class="li"></li>
    </ul>
</div>
<div class="wind-tg">
    <p class="p01">提示<em>×</em></p>
    <p class="p02">推广链接</p>
    <p class="p03">
        <span id="fe_text">http://www.fulaan.com/forum?fromUser=${userName}&uId=${userId}</span>
        <button id="d_clip_button" class="my_clip_button" data-clipboard-target="fe_text">复制</button>
    </p>
    <p class="p04">
        <button>确定</button>
    </p>
</div>
<div class="bg"></div>
</div>

<script type="text/javascript" language="javascript">
    var clip = new ZeroClipboard(document.getElementById("d_clip_button"), {
        moviePath: "/static/js/modules/forum/ZeroClipboard.swf"
    });

    clip.on('complete', function (client, args) {
        alert("复制成功，复制内容为：" + args.text);
    });
</script>
<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>
<!--删除商品弹出框-->
<div class="bg"></div>
<!--============登录================-->
<%@ include file="../common/login.jsp" %>

<script id="ContentTml" type="text/template">
    {{ for(var i in it) { }}
    <div class="cy-card">
        <div class="card-name">
            {{=it[i].memo}}
            <em>版主：{{=it[i].sectionName}}</em>
        </div>
        <div class="card-cont" onclick="window.open('/forum/postIndex.do?pSectionId={{=it[i].fSectionId}}')">
            <div class="card-logo"></div>
            <p class="p1">{{=it[i].name}}</p>
            <p class="p2">{{=it[i].memoName}}</p>
            <p class="p3" value="{{=it[i].fSectionId}}">

            </p>
        </div>
    </div>
    {{ } }}
</script>

<script id="ulsTml" type="text/template">
    {{ for(var i in it) { }}
    <li value="{{=it[i].fSectionId}}" name="{{=it[i].name}}">{{=it[i].name}}<em></em></li>
    {{ } }}
</script>


<script id="listTml" type="text/template">
    {{~it:value:index}}
    {{?index==3}}
    <img id="first" style="cursor: pointer"
         onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}')"
         src="{{=value.activityImage}}" alt="{{=index+1}}">
    {{?}}
    <img style="cursor: pointer"
         onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}')"
         src="{{=value.activityImage}}" alt="{{=index+1}}">
    {{?index==0}}
    <img id="last" style="cursor: pointer"
         onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}')"
         src="{{=value.activityImage}}" alt="{{=index+1}}">
    {{?}}
    {{~}}
</script>

<script id="ooTml" type="text/template">
    {{~it:value:index}}
    {{?index==3}}
    <img id="first" style="cursor: pointer"
         onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}')"
         src="{{=value.activityImage}}" alt="{{=index+1}}">
    {{?}}
    <img style="cursor: pointer"
         onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}')"
         src="{{=value.activityImage}}" alt="{{=index+1}}">
    {{?index==0}}
    <img id="last" style="cursor: pointer"
         onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}')"
         src="{{=value.activityImage}}" alt="{{=index+1}}">
    {{?}}
    {{~}}
</script>

<script id="ScTml" type="text/template">
    {{ for(var i in it) { }}
    <span>主题数：{{=it[i].themeCount}}</span>
    <span>贴数：{{=it[i].postCount}}</span>
    <em class='em-p'>{{=it[i].totalCommentCount}}</em>
    <em class="em-c">{{=it[i].totalScanCount}}</em>
    {{ } }}
</script>

<script id="postListTml" type="text/template">
    {{~it:value:index}}
    <span onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}')">
  <li>
    <img src="{{=value.imageSrc}}" width="225" height="149">
    <h3>{{=value.postTitle}}</h3>
    <p class="p1">{{=value.plainText}}</p>
    <p class="p2">
      <span class="span1">{{=value.personName}}</span>
      <span class="span2">{{=value.comment}}</span>
      <span class="span3">{{=value.scanCount}}</span>
      <span class="span4">{{=value.commentCount}}</span>
    </p>
  </li>
    </span>
    {{~}}
</script>


<script id="poTml" type="text/template">
    {{~it:value:index}}
    <span onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}')">
    <div class="focus-post">
      <div class="foucs-img-f">
        <img src="{{=value.activityImage}}" width="100" height="100">
      </div>
      <span>{{=value.activityMemo}}
      </span>
    </div>
  </span>
    {{~}}
</script>


<script id="bllTml" type="text/template">
    {{~it:value:index}}
        <li index="{{=index+1}}" onclick="document.location.href='/forum/postDetail.do?postId={{=value.fpostId}}&pSectionId={{=value.postSectionId}}'">
            <img src="{{=value.activityImage}}">
            <p>{{=value.activityMemo}}</p>
        </li>
    {{~}}
</script>

<script id="userListTml" type="text/template">
    {{~it:value:index}}
    <a target="_blank" href="/forum/personal.do?personId={{=value.id}}">
        <div class="whli clearfix">
            <div>
                {{?value.avt!=null}}
                <img src="{{=value.avt}}" width="55" height="55">
                {{?}}
                {{?value.avt==null}}
                <img src="/static/images/forum/head_picture.png" width="55" height="55">
                {{?}}
            </div>
            <span value="{{=value.id}}">{{=value.nickName}}</span>
            <%--<em>访问TA主页</em>--%>
        </div>
    </a>
    {{~}}
</script>

<script id="kklTml" type="text/template">
    {{ for(var i in it) { }}
    <div class="mk-f">
        <div class="mk fl" onclick="window.open('/forum/postIndex.do?pSectionId={{=it[i].fSectionId}}')">
            <div class="d11">{{=it[i].memo}}</div>
            <div class="d12"></div>
            <div class="d21">
                <h3>{{=it[i].name}}</h3>
                <p>{{=it[i].memoName}}</p>
                <p class="p-icon" value="{{=it[i].fSectionId}}">
                </p>
            </div>
            <div class="d22"></div>
        </div>
    </div>
    {{ } }}
</script>

<script id="ssTml" type="text/template">
    {{ for(var i in it) { }}
    <img src="/static/images/forum/pp_white.png"><span>{{=it[i].totalCommentCount}}</span>
    <img src="/static/images/forum/eye_white.png"><span>{{=it[i].totalScanCount}}</span>
    {{ } }}
</script>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static/js/sea.js"></script>
<!-- Custom js -->
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/forum/index.js');
</script>
</body>
</html>
