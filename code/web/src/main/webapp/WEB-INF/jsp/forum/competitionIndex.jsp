<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>复兰教育社区--大赛</title>
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
</head>
<body login="${login}" signIn="${signIn}" style="background: #f7f7f7">
<%@ include file="../common/head.jsp" %>
<input id="pSectionId" type="hidden" value="${pSectionId}">
<div class="container">
    <div class="post-head clearfix">
        <div class="new-theam" id="expressTheme">&nbsp;&nbsp;&nbsp;&nbsp;发贴</div>
        <div class="top-mk">
            <p class="p1">
                <span class="span1">大赛</span>
                <span class="span3">主题:<em>${count}</em>&nbsp;/&nbsp;</span>
                <em <c:if test="${login==false}">style="display: none"</c:if>><span id="collBan"><c:if
                        test="${collect == false}">收藏该版块</c:if></span>
                    <c:if test="${collect == true}">已收藏</c:if></em>
            </p>
            <p class="p2">才艺小咖秀（我要做网红）--艺术类、运动、赛事、台球、轮滑、极限运动、F1...</p>
            <img src="/static/images/forum/golden_jiangbei.png" width="194" height="152">
        </div>
    </div>
    <div class="cont-main clearfix">
        <div class="cont-left">
            <div class="post-list-sel">
                <span class="red-b span1 classify" value="-1">全部</span>
            </div>
            <div class="clearfix"></div>
            <div id="listPost">
            </div>
            <div class="div-nofound1" id="notFound">
                <h3>抱歉，没有找到与<em>"${param.regular}"</em>相关的帖子</h3>
                <p>建议您：</p>
                <p>1、查看是否发贴</p>
                <p>2、条件查询查询不到帖子</p>
            </div>
            <div class="new-page-links"></div>
        </div>
        <div class="cont-right">
            <div class="green-qd">
                <span id="dateR"></span>&nbsp;<span id="dayR"></span><em
                    <c:if test="${login == false}">style="display: none;" </c:if>><c:if test="${signIn == false}">
                <sapn id="qDao">每日签到</sapn>
            </c:if>
                <c:if test="${signIn == true}">已签到</c:if></em>
            </div>
            <div class="clearfix" <c:if test="${login == true}">style="margin: 0 auto; display: none;"</c:if>>
                <div class="btn-dl-red" id="loginText">登录</div>
                <div class="btn-zc-red"><span onclick="window.open('/mall/register.do')">注册</span></div>
            </div>
            <div class="personal-c clearfix" <c:if test="${login == false}">style="display: none;" </c:if>>
                <img onclick="window.open('/forum/userCenter/user.do')" style="cursor: pointer"
                <c:if test="${avatar ==null||avatar =='http://7xiclj.com1.z0.glb.clouddn.com/'}">
                     src="/static/images/forum/head_picture.png"</c:if>
                <c:if test="${avatar !=null&&avatar !='http://7xiclj.com1.z0.glb.clouddn.com/'}"> src="${avatar}"
                     </c:if>width="53" height="53">
                <span onclick="window.open('/forum/userCenter/user.do')" style="cursor: pointer"
                      class="span1">${userName}</span>
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
                    <button style="cursor: pointer" id="postBtn">帖子</button>
                    <button style="cursor: pointer" class="fwtg">邀请</button>
                    <button onclick="window.open('/forum/userCenter/collection.do')">收藏</button>
                    <button style="cursor: pointer" id="quitLog">退出</button>
                    <button onclick="window.open('/forum/userCenter/manageCenter.do')" <c:if
                            test="${userPermission < 100}"> style="cursor: pointer;display: none" </c:if>>管理中心
                    </button>
                </div>
            </div>
            <div class="div-bkrk clearfix">
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
            <div class="div-bkrk clearfix">
                <p>版块分类</p>
                <c:forEach items="${sections}" var="section" varStatus="status">
                    <span class="span${status.index + 1}"
                          onclick="location.href='/forum/postIndex.do?pSectionId=${section['fSectionId']}'">${section['name']}</span>
                </c:forEach>
            </div>
            <div class="ltwh" <c:if test="${login==false}">style="display: none" </c:if>>
                <div class="iwant" id="getTask">领取今日任务</div>
            </div>
            <div class="ltwh" style="display: none">
                <div class="wh-title mission-to">资讯</div>
                <ul class="forin-zx">
                    <li>> 五毛拿走不谢！</li>
                    <li>> 五毛拿走不谢！</li>
                    <li>> 五毛拿走不谢！五毛拿走不谢！五毛拿走不谢！</li>
                    <li>> 五毛拿走不谢！</li>
                    <li>> 五毛拿走不谢！</li>
                    <li>> 五毛拿走不谢！</li>
                    <li>> 五毛拿走不谢！</li>
                </ul>
            </div>
            <div class="ltwh">
                <div class="wh-title clearfix">
                    <span value="1">活跃会员排行榜</span>
                    <span value="2" class="wh-cur">新入会员排行榜</span>
                </div>
                <div id="lsU">
                </div>
            </div>
        </div>
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
<%@ include file="../common/footer.jsp" %>
<div class="bg"></div>
<!--============登录================-->
<%@ include file="../common/login.jsp" %>
<script id="postListTml" type="text/template">
    {{~it:value:index}}
    <div class="post-sinp">
        {{?value.image!=null}}
        <img onclick="window.open('/forum/personal.do?personId={{=value.personId}}')" src="{{=value.image}}" width="50"
             height="50">
        {{?}}
        {{?value.image==null}}
        <img onclick="window.open('/forum/personal.do?personId={{=value.personId}}')"
             src="/static/images/forum/head_picture.png" width="50" height="50">
        {{?}}
        <p class="p1">
            {{?value.isTop==1}}
            <span style="color: red;cursor: pointer"
                  onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}&personId={{=value.personId}}')">{{=value.postTitle}}</span>
            {{?}}
            {{?value.isTop==0}}
            <span style="color: #000000;cursor: pointer"
                  onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}&personId={{=value.personId}}')">{{=value.postTitle}}</span>
            {{?}}
            {{?value.inSet==1}}
            <em class="box-red">大赛</em>
            {{?}}
            {{?value.inSet==-1}}
            <em class="box-red">已结束</em>
            {{?}}
        <p class="p2">{{=value.personName}}<em></em>&nbsp;来自{{=value.platform}}&nbsp;&nbsp;{{?value.replyUserName!=null}}{{=value.replyUserName}}{{?}}&nbsp;&nbsp;最后回复于{{=value.replyTime}}天前
            <span class="post-dz">{{=value.praiseCount}}</span>
            <span class="post-tl">{{=value.commentCount}}</span>
            <span class="post-c">{{=value.scanCount}}</span>
        </p>
    </div>
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
        </div>
    </a>
    {{~}}
</script>
<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/forum/competitionIndex.js');
</script>
</body>
</html>
