<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <title>复兰教育社区</title>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit">
    <link rel="stylesheet" type="text/css" href="/static/css/newIndex/css.css">
    <script type="text/javascript" src="/static/js/modules/newIndex/jquery-1.11.1.js"></script>
    <link href="/static/js/modules/core/0.1.0/fancyBox/jquery.fancybox.css?v=2015041602" rel="stylesheet"
          type="text/css" media="screen">
    <script type="text/javascript" src="/static/js/swfobject.js"></script>
    <script type="text/javascript" src="/static/js/recorder.js"></script>
    <script type="text/javascript" src="/static/js/main.js"></script>
    <script type="text/javascript" src="/static/js/template.js"></script>
    <style type="text/css">
        body {
            background: #E6EAF2;
        }
    </style>
</head>
<body>
<div class="index-container clearfix">
    <div class="index-left">
        <img src="/static/images/newIndex/index_logo.png" class="logo" onclick="window.open('/')" style="cursor: pointer">
        <h1>加入社区，让"家校互动"更轻松</h1>
        <h3>社区通知、作业、学习资料、讨论、分享、<br>学习用品，一站式沟通</h3>
        <div class="d-btn">
            <button class="btn1" id="btn1">+创建新社区</button>
            <span class="sp-up"></span>
            <span class="sp-do"></span>
            <div class="com-list">
                <ul class="clearfix" id="coms">

                </ul>
            </div>
        </div>

        <div class="index-focus" id="news">
            <div class="jian-index"></div>
            <ul class="ul-index-n" id="communityNews">
            </ul>
        </div>
        <img src="/static/images/newIndex/lb_banner.png" class="ora-ban">
    </div>
    <script type="text/javascript">
        $(function () {

            $('#toplogin').click(function () {//登录
                $('.store-register').fadeToggle();
                $('.bg').fadeToggle();
            })

            $('#logout').click(function () {//退出登录
                logout();
            });

            $('#quitLog').click(function () {//退出登录
                logout();
            });


            $('#loginText').click(function () {//登录
                $('.store-register').fadeToggle();
                $('.bg').fadeToggle();
            })

            $('#redirect').click(function () {//登录

            })
        });

        function logout() {
            $.ajax({
                url: "/user/logout.do",
                type: "post",
                dataType: "json",
                data: {
                    'inJson': true
                },
                success: function () {
                    location.reload();
                }
            });
            ssoLoginout();
        }


        function ssoLoginout() {
            var logoutURL = "http://ah.sso.cycore.cn/sso/logout";

            $.ajax({
                url: logoutURL,
                type: "GET",
                dataType: 'jsonp',
                jsonp: "callback",
                crossDomain: true,
                cache: false,
                success: function (html) {

                },
                error: function (data) {

                }
            });
        }

        function redirectQ() {
            var currentUrl=encodeURI(encodeURI(location.href));
            window.open('/user/qqlogin.do?currentUrl='+currentUrl, "TencentLogin", "width=800,height=600,menubar=0,scrollbars=1, resizable=1,status=1,titlebar=0,toolbar=0,location=1");
        }

        function loginWeiXin() {

            window.open('/user/wechatlogin.do', "TencentLogin", "width=800,height=600,menubar=0,scrollbars=1, resizable=1,status=1,titlebar=0,toolbar=0,location=1");
        }

    </script>
    <div class="index-right">
        <div class="login-mk">
            <c:if test="${login == true}">
                <div class="login-already">
                    <div class="d1-set">
                        <div class="d1-img"></div>
                        <div class="d1-mk">
                            <%--<div class="p1" onclick="window.open('/forum/userCenter/user.do','_blank')"><span></span>个人设置</div>--%>
                            <div class="p1" onclick="window.open('/account/accountSafe.do','_blank')"><span></span>个人设置</div>
                            <div class="p2"  id="logout"><span></span>退出</div>
                        </div>
                    </div>
                    <div class="d2-msg">
                        <div class="d2-notice" <c:if test="${infoCount==0}">style="display: none" </c:if>>${infoCount}</div>
                        <div class="d2-img"></div>
                        <div class="d2-mk">
                            <div class="p1" onclick="window.location.href='/community/playmateNotice.do'"><span></span>玩伴通知<em <c:if test="${friendApplyCount==0}">style="display: none" </c:if>>${friendApplyCount}</em></div>
                            <div class="p2" onclick="window.location.href='/community/mySystemInfo.do'"><span></span>系统消息<em <c:if test="${systemInfoCount==0}">style="display: none" </c:if>>${systemInfoCount}</em></div>

                        </div>
                    </div>
                    <div class="login-name" id="userName">${nickName}</div>
                    <img src="${avatar}">
                </div>
            </c:if>
            <c:if test="${login == false}">
                <button onclick="window.open('/account/register.do')">注册</button>
                <%--<button onclick="window.open('/mall/register.do')">注册</button>--%>
                <button id="toplogin">登录</button>
                <span class="wx-l" onclick="loginWeiXin()">微信登录</span>
                <span class="qq-l" onclick="redirectQ()">QQ登录</span>
            </c:if>
        </div>
        <div class="link-mk clearfix">
            <div class="mk-s mk-ls" id="forum">
                <div class="mk8-pho"></div>
                <div class="mk-name">
                    <span>才艺之星</span>
                </div>
                <div class="mk-intro">各种版块，无论是发碎碎念<br>还是秀才艺</div>
            </div>
            <div class="line-thr"></div>
            <div class="mk-s mk-ls" id="competition">
                <div class="mk7-pho"></div>
                <div class="mk-name">
                    <span>大赛</span>
                </div>
                <div class="mk-intro">不定期开展各种竞赛项目，<br>总有适合你的</div>
                <div class="i-hot"></div>
            </div>
            <div class="line-thr"></div>
            <div class="mk-s mk-ls" id="playmate">
                <div class="mk1-pho"></div>
                <div class="mk-name">
                    <span>找玩伴</span>
                </div>
                <div class="mk-intro">结识附近志趣相投的小伙伴，<br>安全快乐的玩耍</div>
            </div>
            <div class="line-thr"></div>
            <div class="mk-s mk-ls" id="train">
                <div class="mk4-pho"></div>
                <div class="mk-name">
                    <span>找培训</span>
                </div>
                <div class="mk-intro">系统定位，培训机构不仅<br>要口碑好还要离家近</div>
            </div>
            <div class="line-acc">
                <div></div>
                <div></div>
                <div></div>
                <div></div>
            </div>
            <div class="mk-s mk-ls" id="forumIndex">
                <div class="mk6-pho"></div>
                <div class="mk-name">
                    <span>教育商城</span>
                </div>
                <div class="mk-intro">创科教育、汇聚精品<br>全额积分兑换商品</div>
            </div>
            <div class="line-thr"></div>
            <div class="mk-s">
                <div class="mk5-pho"></div>
                <div class="mk-name">
                    <span>在线学习</span>
                </div>
                <div class="mk-intro">优质学习资源在线网课，<br>足不出户学习好</div>
            </div>
            <div class="line-thr"></div>
            <div class="mk-s">
                <div class="mk3-pho"></div>
                <div class="mk-name">
                    <span>找家教</span>
                </div>
                <div class="mk-intro">便捷安全一对一，<br>名师家教自由选择</div>
            </div>
            <div class="line-thr"></div>
            <div class="mk-s">
                <div class="mk2-pho"></div>
                <div class="mk-name">
                    <span>亲子活动</span>
                </div>
                <div class="mk-intro">优质亲子活动，<br>陪伴有质量的成长</div>
            </div>
        </div>
        <div class="ivideo" style="height: 280px;">
                <%--<ul>
                    <li class="li1"><div></div><a onclick="tryPlayYCourse('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/57a45540de04cb45ae60bc7c.mp4.m3u8')" href="javascript:void(0)"></a></li>
                    <li class="li2"><div></div> <a class="fancybox" href="http://7xiclj.com1.z0.glb.clouddn.com/58656c823d4df96ad47517a3.jpg?imageView/1/h/500/w/500" data-fancybox-group="home" title="预览"></a></li>
                    <li class="li3"><div></div><a onclick="tryPlayYCourse('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/57ae5eb2de04cb5082f8ede3.mp4.m3u8')" href="javascript:void(0)"></a></li>
                    <li class="li4"><div></div><a class="fancybox" href="http://7xiclj.com1.z0.glb.clouddn.com/57b6c46ede04cb06131ced0d.JPG" data-fancybox-group="home" title="预览"></a></li>
                    <li class="li5"><div></div><a onclick="tryPlayYCourse('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/57ac8ba7de04cb644c2430c1.mp4.m3u8')" href="javascript:void(0)"></a></li>
                    <li class="li6"><div></div><a onclick="tryPlayYCourse('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/579ac9b03d4df94c677d4f8d.mp4.m3u8')" href="javascript:void(0)"></a></li>
                    <li class="li7"><div></div><a onclick="tryPlayYCourse('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5873638fcab6ec204895f1e4.m3u8')" href="javascript:void(0)"></a></li>
                    <li class="li8"><div></div><a onclick="tryPlayYCourse('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/57a09f683d4df94e7349721f.mp4.m3u8')" href="javascript:void(0)"></a></li>
                    <li class="li9"><div></div><a class="fancybox" href="http://7xiclj.com1.z0.glb.clouddn.com/57a51bab3d4df9703fd552a1.jpg?imageView/1/h/500/w/500" data-fancybox-group="home" title="预览"></a></li>
                    <li class="li10"><div></div><a onclick="tryPlayYCourse('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/57a4609b3d4df9703fd53a0f.mp4.m3u8')" href="javascript:void(0)"></a></li>
                    <li class="li11"><div></div><a onclick="tryPlayYCourse('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/57a5133d3d4df9703fd55161.mp4.m3u8')" href="javascript:void(0)"></a></li>
                    <li class="li12"><div></div><a onclick="tryPlayYCourse('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/57bbcda93d4df95eda9bc7ea.mp4.m3u8')" href="javascript:void(0)"></a></li>
                    <li class="li13"><div></div><a class="fancybox" href="http://7xiclj.com1.z0.glb.clouddn.com/57a493543d4df9703fd54c37.jpg?imageView/1/h/500/w/500" data-fancybox-group="home" title="预览"></a></li>
                    <li class="li14"><div></div><a onclick="tryPlayYCourse('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/57aebf8d3d4df92a03183502.mp4.m3u8')" href="javascript:void(0)"></a></li>
                    <li class="li15"><div></div><a onclick="tryPlayYCourse('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/57a5ee3a3d4df9703fd57bff.mp4.m3u8')" href="javascript:void(0)"></a></li>
                </ul>--%>
                    <div class="photolist">
                        <div class="div1">
                            <div class="doudiv mb8">
                                <img src="/static/images/forum/comp01.png">
                                <i onclick="tryPlayYCourse('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/579ac9b03d4df94c677d4f8d.mp4.m3u8')"></i>
                                <div class="bg20"></div>
                            </div>
                            <div class="doudiv">
                                <img src="/static/images/forum/comp02.png">
                                <i onclick="tryPlayYCourse('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/57ac8ba7de04cb644c2430c1.mp4.m3u8')"></i>
                                <div class="bg20"></div>
                            </div>
                        </div>
                        <div class="div2">
                            <div class="trediv mb8">
                                <img src="/static/images/forum/comp03.png">
                                <a class="fancybox" href="http://7xiclj.com1.z0.glb.clouddn.com/57b6c46ede04cb06131ced0d.JPG" data-fancybox-group="home" title="预览"><i>
                                </i> </a>
                                <div class="bg20"></div>
                            </div>
                            <div class="trediv mb8">
                                <img src="/static/images/forum/comp04.png">
                                <i onclick="tryPlayYCourse('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/57ae5eb2de04cb5082f8ede3.mp4.m3u8')"></i>
                                <div class="bg20"></div>
                            </div>
                            <div class="trediv">
                                <img src="/static/images/forum/comp05.png">
                                <a class="fancybox" href="http://7xiclj.com1.z0.glb.clouddn.com/58656c823d4df96ad47517a3.jpg?imageView/1/h/500/w/500" data-fancybox-group="home" title="预览"><i>
                                </i></a>
                                <div class="bg20"></div>
                            </div>
                        </div>
                        <div class="div1">
                            <div class="doudiv mb8">
                                <img src="/static/images/forum/comp06.png">
                                <i onclick="tryPlayYCourse('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/57a45540de04cb45ae60bc7c.mp4.m3u8')"></i>
                                <div class="bg20"></div>
                            </div>
                            <div class="doudiv">
                                <img src="/static/images/forum/comp07.png">
                                <i onclick="tryPlayYCourse('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/57aebf8d3d4df92a03183502.mp4.m3u8')"></i>
                                <div class="bg20"></div>
                            </div>
                        </div>
                        <div class="div2">
                            <div class="trediv mb8">
                                <img src="/static/images/forum/comp08.png">
                                <a class="fancybox" href="http://7xiclj.com1.z0.glb.clouddn.com/57a493543d4df9703fd54c37.jpg?imageView/1/h/500/w/500" data-fancybox-group="home" title="预览"><i>
                                </i></a>
                                <div class="bg20"></div>
                            </div>
                            <div class="trediv mb8">
                                <img src="/static/images/forum/comp09.png">
                                <i onclick="tryPlayYCourse('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/57bbcda93d4df95eda9bc7ea.mp4.m3u8')"></i>
                                <div class="bg20"></div>
                            </div>
                            <div class="trediv">
                                <img src="/static/images/forum/comp10.png">
                                <i onclick="tryPlayYCourse('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/57a5133d3d4df9703fd55161.mp4.m3u8')"></i>
                                <div class="bg20"></div>
                            </div>
                        </div>
                        <div class="div1">
                            <div class="doudiv mb8">
                                <img src="/static/images/forum/comp11.png">
                                <i onclick="tryPlayYCourse('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5873638fcab6ec204895f1e4.m3u8')"></i>
                                <div class="bg20"></div>
                            </div>
                            <div class="doudiv">
                                <img src="/static/images/forum/comp12.png">
                                <i onclick="tryPlayYCourse('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/57a5ee3a3d4df9703fd57bff.mp4.m3u8')"></i>
                                <div class="bg20"></div>
                            </div>
                        </div>
                        <div class="div2 mr0">
                            <div class="trediv mb8">
                                <img src="/static/images/forum/comp13.png">
                                <i onclick="tryPlayYCourse('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/57a4609b3d4df9703fd53a0f.mp4.m3u8')"></i>
                                <div class="bg20"></div>
                            </div>
                            <div class="trediv mb8">
                                <img src="/static/images/forum/comp14.png">
                                <i onclick="tryPlayYCourse('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/57a09f683d4df94e7349721f.mp4.m3u8')"></i>
                                <div class="bg20"></div>
                            </div>
                            <div class="trediv">
                                <img src="/static/images/forum/comp15.png">
                                <a class="fancybox" href="http://7xiclj.com1.z0.glb.clouddn.com/57a51bab3d4df9703fd552a1.jpg?imageView/1/h/500/w/500" data-fancybox-group="home" title="预览"><i>
                                </i></a>
                                <div class="bg20"></div>
                            </div>
                        </div>
                    </div>
        </div>
        <div class="photo-show">
            <div class="d-photo">
 <%--               <script id="talentTml" type="text/template">
                    {{~it:value:index}}
                    <li>
                        <img src="{{=value.activityImage}}">
                        <span onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}')">更多</span>
                        <div class="photo-text">
                            <h3 onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}')">
                                {{=value.mainTitle}}</h3>
                            <p class="p1">{{=value.title}}</p>
                            <p class="p5">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{=value.partContent}}</p>
                        </div>
                    </li>
                    {{~}}
                </script>--%>
                <ul class="ul-photo" style="left: -718px;" id="talentList">

                    <%--<li onclick="window.open('http://hanghai. .com')">--%>
                        <%--<img src="/static/images/community/hanghai_mini.jpg">--%>
                        <%--<span>更多</span>--%>
                        <%--<div class="photo-text">--%>
                            <%--<h3 style="margin-top: 86px;font-size: 24px;">帆迪全球在线<br>航海课程</h3>--%>
                        <%--</div>--%>
                    <%--</li>--%>
                    <%--<li onclick="window.open('/forum/postIndex.do?pSectionId=575d4d8e0cf2ca0383166bba')">--%>
                        <%--<img src="/static/images/community/jixian_mini.jpg">--%>
                        <%--<span>更多</span>--%>
                        <%--<div class="photo-text">--%>
                            <%--<h3 style="margin-top: 86px;font-size: 24px;">极限航海巅峰<br>挑战</h3>--%>
                        <%--</div>--%>
                    <%--</li>--%>
                </ul>

                <div class="photo-btn">
                    <span class="sp1" id="prev-p"> < </span>
                    <span class="sp2" id="next-p"> > </span>
                </div>
                <div class="greenControl clearfix">
                    <div class="g-on" index="1"></div>
                    <div index="2"></div>
                    <div index="3"></div>
                    <div index="4"></div>
                </div>
            </div>


        </div>
    </div>
</div>
<div class="store-foott-main">
    <div class="store-foott-left">
        <span>版权所有：上海复兰信息科技有限公司</span>
        <a target="_blank" href="http://www.fulaan-tech.com">www.fulaan-tech.com</a><br>
        <span>咨询热线：400-820-6735</span><br>
        <span>
                    <a href="/aboutus/k6kt">关于我们</a>
                    <a href="/contactus/k6kt">联系我们</a>
                    <a href="/service/k6kt">服务条款 </a>
                    <a href="/privacy/k6kt">隐私保护 </a>
                    <a href="/admin.do" target="_blank">后台管理</a>
                </span><br>
        <span>沪ICP备14004857号</span>
    </div>
    <div class="store-foott-right">
        <div>
            <img src="/static/images/newIndex/store-WEII.jpg">
            <p class="p1">
                微信、微博欢迎<br>搜索关注“复兰科技”<br>
            </p>
            <a target="_blank" href="http://weibo.com/FulaanTechnology"><img
                    src="/static/images/newIndex/store-WEB.png"></a>
            <a target="_blank" href="http://t.qq.com/FulaanTechnology"><img
                    src="/static/images/newIndex/store-WEBI.png"></a>
        </div>
    </div>
</div>
<div class="bg"></div>
<!--============登录================-->
<%@ include file="../common/login.jsp" %>
<div id="YCourse_player" class="player-container" style="display: none">
    <div id="player_div" class="player-div"></div>
    <div id="sewise-div"
         style="display: none; width: 630px; height: 360px; max-width: 800px;">
        <script type="text/javascript"
                src="/static/plugins/sewiseplayer/sewise.player.min.js"></script>

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
</div>

<script type="text/javascript">
    var isFlash = false;
    function getVideoType(url) {
        if (url.indexOf('polyv.net') > -1) {
            return "POLYV";
        }
        if (url.indexOf('.swf') > 0) {
            return 'FLASH';
        }
        return 'HLS';
    }

    function playerReady(name) {
        if (isFlash) {
            SewisePlayer.toPlay(playerReady.videoURL, "视频", 0, false);
        }
    }

    $('.player-close-btn').click(function () {
        $('#YCourse_player').hide();
        $(".bg").hide();
    });

    function download(url) {
        location.href = "/forum/userCenter/m3u8ToMp4DownLoad.do?filePath=" + url;
    }
    function tryPlayYCourse(url) {
        $("#YCourse_player").show();
        $(".player-close-btn").css({
            "display": 'block'
        });
        var videoSourceType = getVideoType(url);
        $('.bg').fadeIn('fast');
        var $player_container = $("#YCourse_player");
        $player_container.fadeIn();

        if (videoSourceType == "POLYV") {
            $('#sewise-div').hide();
            $('#player_div').show();
            var player = polyvObject('#player_div').videoPlayer({
                'width': '800',
                'height': '450',
                'vid': url.match(/.+(?=\.swf)/)[0].replace(/.+\//, '')
            });
        } else {
            $('#player_div').hide();
            $('#sewise-div').show();
            try {
                SewisePlayer.toPlay(url, "视频", 0, true);
            } catch (e) {
                playerReady.videoURL = url;
                isFlash = true;
            }
        }
    }
</script>

<script type="text/javascript" src="/static/js/modules/core/0.1.0/fancyBox/jquery.fancybox.js"></script>
<script type="text/javascript">
    $(".fancybox").fancybox({});
</script>
<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>

<script>
    seajs.use('/static/js/modules/newIndex/newIndex.js', function (newIndex) {
        newIndex.init();
    });
</script>
</body>
</html>
