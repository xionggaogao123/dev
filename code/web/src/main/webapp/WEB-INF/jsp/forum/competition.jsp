<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>大赛</title>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="/static/css/forum/forum.css">
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
    <style>
        .lunt-index-hide {
            display: none;
        }

        .header-cont .ha5 {
            background: #E8E8E8;
            color: #3764a0;
        }
    </style>
</head>
<body style="background: #f4f4f4">
<%@ include file="../common/head.jsp" %>
<div class="cop-container clearfix">
    <div class="c-bg-f">
        <div class="c-bg-s"></div>
    </div>
    <div class="c-bg-cir-f">
        <div class="cir-bg-s"></div>
    </div>
    <div class="c-list">
        <div class="photolist">
            <div class="div1">
                <div class="doudiv mb8">
                    <img src="/static/images/forum/comp01.png">
                    <i></i>
                    <div class="bg20"></div>
                </div>
                <div class="doudiv">
                    <img src="/static/images/forum/comp02.png">
                    <i></i>
                    <div class="bg20"></div>
                </div>
            </div>
            <div class="div2">
                <div class="trediv mb8">
                    <img src="/static/images/forum/comp03.png">
                    <i></i>
                    <div class="bg20"></div>
                </div>
                <div class="trediv mb8">
                    <img src="/static/images/forum/comp04.png">
                    <i></i>
                    <div class="bg20"></div>
                </div>
                <div class="trediv">
                    <img src="/static/images/forum/comp05.png">
                    <i></i>
                    <div class="bg20"></div>
                </div>
            </div>
            <div class="div1">
                <div class="doudiv mb8">
                    <img src="/static/images/forum/comp06.png">
                    <i></i>
                    <div class="bg20"></div>
                </div>
                <div class="doudiv">
                    <img src="/static/images/forum/comp07.png">
                    <i></i>
                    <div class="bg20"></div>
                </div>
            </div>
            <div class="div2">
                <div class="trediv mb8">
                    <img src="/static/images/forum/comp08.png">
                    <i></i>
                    <div class="bg20"></div>
                </div>
                <div class="trediv mb8">
                    <img src="/static/images/forum/comp09.png">
                    <i></i>
                    <div class="bg20"></div>
                </div>
                <div class="trediv">
                    <img src="/static/images/forum/comp10.png">
                    <i></i>
                    <div class="bg20"></div>
                </div>
            </div>
            <div class="div1">
                <div class="doudiv mb8">
                    <img src="/static/images/forum/comp11.png">
                    <i></i>
                    <div class="bg20"></div>
                </div>
                <div class="doudiv">
                    <img src="/static/images/forum/comp12.png">
                    <i></i>
                    <div class="bg20"></div>
                </div>
            </div>
            <div class="div2 mr0">
                <div class="trediv mb8">
                    <img src="/static/images/forum/comp13.png">
                    <i></i>
                    <div class="bg20"></div>
                </div>
                <div class="trediv mb8">
                    <img src="/static/images/forum/comp14.png">
                    <i></i>
                    <div class="bg20"></div>
                </div>
                <div class="trediv">
                    <img src="/static/images/forum/comp15.png">
                    <i></i>
                    <div class="bg20"></div>
                </div>
            </div>
        </div>
        <div class="hj-divss" onclick="window.open('/competionStatus')">获奖名单</div>
        <div class="c-topsq1">
            <img src="/static/images/forum/golden_jiangbei.png" class="img-gj">
            <div>
                <p class="p1">
                    <span class="sp1">大赛</span>
                    <span class="sp2">主题：${count} /</span>
                </p>
                <p class="p2">才艺小咖秀（我要做网红）--艺术类、运动、赛事、台球、轮滑、极限运动、F1...</p>
            </div>
            <button id="goCompetition">大赛</button>
        </div>
        <p class="p1">
            <span class="act-ing-cur">全部大赛</span>
            <%--<span id="offActivity">已结束活动</span>--%>
        </p>

        <script>
            $(document).ready(function () {
                $('.c-list>.p1 span').click(function () {
                    $(this).addClass('act-ing-cur').siblings('.c-list>.p1 span').removeClass('act-ing-cur');
                });
                $('.wind-winner .ww-btn').click(function () {
                    $('.bg').fadeOut();
                    $('.wind-winner').fadeOut();
                });
            });
        </script>
        <div class="kt-list">
            <img src="/static/images/forum/c_girl.png" class="img1">
            <img src="/static/images/forum/c_monster.png" class="img2">
            <img src="/static/images/forum/c_duck.png" class="img3">
            <img src="/static/images/forum/c_bear.png" class="img4">
            <img src="/static/images/forum/c_loli.png" class="img5">
        </div>
        <ul class="ul-compit" id="talentList">

        </ul>
    </div>
</div>
<c:if test="${ipScan == false}">
    <div class="wind-winner">
        <div class="ww-cont">
            <div class="ww-btn" onclick=""></div>
            <div class="ww-btn2" onclick=""></div>
            <ul class="ul-winner">
                <li class="li1">
                    <span>积木大比拼</span>
                    <span>我最红</span>
                    <span>萌宠秀</span>
                    <span>铁人三项</span>
                    <span>乐器演奏</span>
                </li>
                <li>
                    <span>aapii2016</span>
                </li>
                <li>
                    <span>车其轩</span>
                    <span>谢添</span>
                    <span>伍声</span>
                    <span>车宜轩</span>
                    <span>余以岑111</span>
                </li>
                <li>
                    <span>蓝精灵</span>
                    <span>Li66</span>
                    <span>李贤一</span>
                    <span>小王子</span>
                    <span>程馨悦</span>
                </li>
                <li>
                    <span>shine</span>
                    <span>胡溢昕</span>
                    <span>胡乐乐</span>
                    <span>侍颖</span>
                    <span>辛十牙</span>
                </li>
            </ul>
        </div>
    </div>
</c:if>
<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>
<!--删除商品弹出框-->
<!--============登录================-->
<%@ include file="../common/login.jsp" %>
<script id="talentTml" type="text/template">
    {{~it:value:index}}
    <li>
        <img class="img1" src="{{=value.activityImage}}"
             onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}')">
        <p class="lp1"><span
                onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}')">{{=value.activityMemo}}</span>
        </p>
        <p class="lp2">比赛内容：</p>
        <p class="lp3">{{=value.plainText}}</p>
        <p class="lp2">比赛时间：</p>
        <p class="lp3">{{=value.activityStartTime}}~{{=value.activityEndTime}}</p>
        {{?value.inSet == 1}}
        <img class="img2" src="/static/images/forum/hot_active_ing.png">
        {{?}}
        {{?value.inSet == -1}}
        <img class="img3" src="/static/images/forum/hot_active_end.png">
        {{?}}
    </li>
    {{~}}
</script>

<script id="OfftalentTml" type="text/template">
    {{~it:value:index}}
    <li>
        <img class="img1" src="{{=value.activityImage}}"
             onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}')">
        <p class="lp1"><span
                onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}')">{{=value.activityMemo}}</span>
        </p>
        <p class="lp2">比赛内容：</p>
        <p class="lp3">{{=value.plainText}}</p>
        <p class="lp2">比赛时间：</p>
        <p class="lp3">{{=value.activityStartTime}}~{{=value.activityEndTime}}</p>
        <img class="img3" src="/static/images/forum/hot_active_end.png">
    </li>
    {{~}}
</script>
<script src="/static/js/sea.js"></script>
<!-- Custom js -->
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/forum/competition.js');
</script>
</body>
</html>
