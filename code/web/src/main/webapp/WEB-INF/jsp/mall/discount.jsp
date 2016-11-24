<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>优惠专区</title>
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>

    <script src="/static/js/modules/core/0.1.0/jquery.min.js"></script>
    <style>
        .login-bar {
            border-bottom: none;
        }

        .nav-bg {
            top: 0;
        }

        .main-vo {
            margin-top: 10px;
        }
    </style>
</head>
<body>
<%@ include file="mallSectionHead.jsp" %>
<div class="main-vo" style="bottom:10px;">
    <div style="width:100%;height:400px;overflow: visible;width: 1250px;margin:0 auto;position: relative"><img
            class="top-child" src="../../../static/images/hot_top_banner.png" ">
    </div>
    <img src="../../../images/abbb.png" width="1920" style="position: absolute;top:0;left: 0;z-index: -2">
    <img src="../../../images/abbb.png" width="1920" style="position: absolute;bottom:0;left: 0;z-index: -1">
    <div class="nav-vo">
        <div class="nav-voo">
            <div class="nav-bg"></div>
            <ul class="orange-all" id="listAc">
                <%--<li class="bg-red" onclick="window.open('/mall/index.do')"><p>商城首页<em>></em></p></li>--%>
                <%--<li class="li-s" onclick="window.open('/mall/integrate.do')" ><p>活动专区</p><span>端午节特惠</span>、<span>全积分兑换</span><em>></em></li>--%>
                <%--&lt;%&ndash;<li class="li-s" onclick="window.open('/mall/discount.do')" >全积分兑换专区<em>></em></li>&ndash;%&gt;--%>
                <%--<li class="li-s"><p>晒才艺</p><span>文具</span>、<span>乐器</span>、<span>棋类</span></li>--%>
                <%--<li class="li-s" onclick="window.open('/mall/index.do?categoryId=56eb6a1d0cf234ce7e479c24')"><p>STEM创客</p><span>STEM创客</span>、<span>益智玩具</span><em>></em></li>--%>
                <%--<li class="li-s" onclick="window.open('/mall/index.do?categoryId=56eb6a0b0cf234ce7e479c1c')"><p>读书/学霸</p><span>教辅教材</span>、<span>小说文学</span><em>></em></li>--%>
                <%--<li class="li-s" onclick="window.open('/mall/index.do?categoryId=56eb6a2d0cf234ce7e479c26')"><p>演讲口才</p><span>名人传记</span>、<span>成功励志</span><em>></em></li>--%>
                <%--<li class="li-s" onclick="window.open('/mall/index.do?categoryId=56eb6a2d0cf234ce7e479c26')"><p>安全健康</p><span>智能硬件</span><em>></em></li>--%>
            </ul>
        </div>
    </div>
    <div class="sp-list">
        <div class="sp-top">
            <h1>全积分兑换专区</h1>
            <div onclick="window.location.href='/mall/register.do'" style="height:128px;cursor:pointer;"></div>
        </div>
        <div class="sp-li">
            <em></em>

            <div class="sp-img">
                <a href="/mall/detail.do?id=56fba24c0cf27bf68fcfd07e" target="_blank">
                    <img src="../../../static/images/hot_sp_1.png" width="400" height="260">
                </a>
            </div>
            <div class="sp-infor">
                <a href="/mall/detail.do?id=56fba24c0cf27bf68fcfd07e" target="_blank">
                    <p class="p1">儿童益智&nbsp;创意礼品</p>
                    <p class="p2">实木荷木五合一套装跳棋/五子棋/飞行棋/蛇棋/斗兽棋</p>
                </a>
                <p class="p3"><i>￥</i>47.50</p>
                <p class="p4">
                    <button onclick="window.open('/mall/detail.do?id=56fba24c0cf27bf68fcfd07e')">立即换购</button>
                    经验值兑换的积分，可全额抵用支付
                </p>
            </div>
        </div>
        <div class="sp-li">
            <em></em>

            <div class="sp-img">
                <a href="/mall/detail.do?id=5715aa0a0cf2f633eef5adc6" target="_blank">
                    <img src="../../../static/images/hot_sp_2.png" width="400" height="260">
                </a>
            </div>
            <div class="sp-infor">
                <a href="/mall/detail.do?id=5715aa0a0cf2f633eef5adc6" target="_blank">
                    <p class="p1">世界名著&nbsp;青少年中小学生课外必读图书籍</p>
                    <p class="p2">老人与海 精装版 海明威当代近代文学作品</p>
                </a>
                <p class="p3"><i>￥</i>12.90</p>
                <p class="p4">
                    <button onclick="window.open('/mall/detail.do?id=5715aa0a0cf2f633eef5adc6')">立即换购</button>
                    经验值兑换的积分，可全额抵用支付
                </p>
            </div>
        </div>
        <div class="sp-li">
            <em></em>
            <div class="sp-img">
                <a href="/mall/detail.do?id=570e0a770cf2372af0f4c67e" target="_blank">
                    <img src="../../../static/images/hot_sp_3.png" width="400" height="260">
                </a>
            </div>
            <div class="sp-infor">
                <a href="/mall/detail.do?id=570e0a770cf2372af0f4c67e" target="_blank">
                    <p class="p1">儿童益智&nbsp;创意礼品</p>
                    <p class="p2">毽子 3铁片鸡毛毽子 比赛毽子（3个装）</p>
                </a>
                <p class="p3"><i>￥</i>9.90</p>
                <p class="p4">
                    <button onclick="window.open('/mall/detail.do?id=570e0a770cf2372af0f4c67e')">立即换购</button>
                    经验值兑换的积分，可全额抵用支付
                </p>
            </div>
        </div>
        <div class="sp-li">
            <em></em>

            <div class="sp-img">
                <a href="/mall/detail.do?id=570e0ada0cf2372af0f4c696" target="_blank">
                    <img src="../../../static/images/hot_sp_4.png" width="400" height="260">
                </a>
            </div>
            <div class="sp-infor">
                <a href="/mall/detail.do?id=570e0ada0cf2372af0f4c696" target="_blank">
                    <p class="p1">儿童益智&nbsp;创意礼品</p>
                    <p class="p2">沙包 儿童丢沙袋 卡通帆布 纯手工制作（3个装）</p>
                </a>
                <p class="p3"><i>￥</i>10.50</p>
                <p class="p4">
                    <button onclick="window.open('/mall/detail.do?id=570e0ada0cf2372af0f4c696')">立即换购</button>
                    经验值兑换的积分，可全额抵用支付
                </p>
            </div>
        </div>
        <div class="sp-li">
            <em></em>

            <div class="sp-img">
                <a href="/mall/detail.do?id=570e0dda0cf2372af0f4c9cf" target="_blank">
                    <img src="../../../static/images/hot_sp_5.png" width="400" height="260">
                </a>
            </div>
            <div class="sp-infor">
                <a href="/mall/detail.do?id=570e0dda0cf2372af0f4c9cf" target="_blank">
                    <p class="p1">儿童益智&nbsp;创意礼品</p>
                    <p class="p2">陀螺 实木 健身 儿童传统玩具送鞭子 7/8/10厘米多款</p>
                </a>
                <p class="p3"><i>￥</i>8.00</p>
                <p class="p4">
                    <button onclick="window.open('/mall/detail.do?id=570e0dda0cf2372af0f4c9cf')">立即换购</button>
                    经验值兑换的积分，可全额抵用支付
                </p>
            </div>
        </div>
    </div>
</div>


<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>
<!--删除商品弹出框-->
<div class="bg"></div>
<!--============登录================-->
<%@ include file="../common/login.jsp" %>

<script id="listAcTml" type="text/template">
    <li class="bg-red li1"><p><span style="cursor: pointer" onclick="window.open('/mall')">商城专区首页</span></p></li>
    <li class="li-s">
        <span>
            <span onclick="location.href='/integrate'">节日特惠专区</span>
        </span>
        <em>></em>
    </li>
    <li class="li-s store-cur">
        <span>
            <span onclick="location.href='/mall/discount.do'">全积分兑换专区</span>
        </span>
        <em>></em>
    </li>
    {{ for(var i in it) { }}
    <li class="li-s">
        <span value="{{=it[i].id}}" class="listData"></span><span>&nbsp;</span>
        <em>></em></li>
    {{ } }}
    <li class="li-s">
        <span>
            <span onclick="window.open('/mall/index.do')">全部商品</span>
        </span>
        <em>></em>
    </li>
</script>

<script id="listTml" type="text/template">
    {{ for(var i in it) { }}
    <span onclick="window.open('/mall/index.do?categoryId={{=it[i].parentId}}&levelCategoryId={{=it[i].id}}')"><em>{{=it[i].name}}</em></span> /
    {{ } }}
</script>
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static/js/sea.js"></script>
<!-- Custom js -->
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/mall/0.1.0/discount.js');
</script>
</body>
</html>
