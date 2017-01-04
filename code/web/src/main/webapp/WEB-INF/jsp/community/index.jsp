<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <title>复兰教育社区</title>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="/static/css/newIndex/css.css">
    <script type="text/javascript" src="/static/js/modules/newIndex/jquery-1.11.1.js"></script>
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

            window.open('/user/qqlogin.do', "TencentLogin", "width=800,height=600,menubar=0,scrollbars=1, resizable=1,status=1,titlebar=0,toolbar=0,location=1");
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
                    <div class="login-name" id="userName">${userName}</div>
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
                <ul class="ul-photo" style="left: 0;" id="talentList">
                    <li onclick="window.open('http://hanghai.fulaan.com')">
                        <img src="/static/images/community/hanghai_mini.jpg">
                        <span >更多</span>
                        <div class="photo-text">
                            <h3 style="margin-top: 86px;font-size: 24px;">帆迪全球在线<br>航海课程</h3>
                        </div>
                    </li>
                    <li onclick="window.open('/forum/postIndex.do?pSectionId=575d4d8e0cf2ca0383166bba')">
                        <img src="/static/images/community/jixian_mini.jpg">
                        <span >更多</span>
                        <div class="photo-text">
                            <h3 style="margin-top: 86px;font-size: 24px;">极限航海巅峰<br>挑战</h3>
                        </div>
                    </li>
                    <li onclick="window.open('http://hanghai.fulaan.com')">
                        <img src="/static/images/community/hanghai_mini.jpg">
                        <span>更多</span>
                        <div class="photo-text">
                            <h3 style="margin-top: 86px;font-size: 24px;">帆迪全球在线<br>航海课程</h3>
                        </div>
                    </li>
                    <li onclick="window.open('/forum/postIndex.do?pSectionId=575d4d8e0cf2ca0383166bba')">
                        <img src="/static/images/community/jixian_mini.jpg">
                        <span>更多</span>
                        <div class="photo-text">
                            <h3 style="margin-top: 86px;font-size: 24px;">极限航海巅峰<br>挑战</h3>
                        </div>
                    </li>
                </ul>

                <div class="photo-btn">
                    <span class="sp1" id="prev-p"> < </span>
                    <span class="sp2" id="next-p"> > </span>
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
<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>

<script>
    seajs.use('/static/js/modules/newIndex/newIndex.js', function (newIndex) {
        newIndex.init();
    });
</script>
</body>
</html>
