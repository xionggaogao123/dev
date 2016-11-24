<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/5/23
  Time: 9:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>特惠专区</title>
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
    <script src="/static/js/modules/core/0.1.0/jquery.min.js"></script>
    <style>
        .login-bar {
            border-bottom: none;
        }

        .nav-bg {
            top: -3px;
        }

        .header-cont .ha3 {
            background: #E8E8E8;
            color: #3764a0;
        }
    </style>

    <script>
        window.onload = function () {
            var oTop = document.getElementById("to_top");
            var screenw = document.documentElement.clientWidth || document.body.clientWidth;
            var screenh = document.documentElement.clientHeight || document.body.clientHeight;
            oTop.style.left = screenw - oTop.offsetWidth + "px";
            oTop.style.top = screenh - oTop.offsetHeight + "px";
            window.onscroll = function () {
                var scrolltop = document.documentElement.scrollTop || document.body.scrollTop;
                oTop.style.top = screenh - oTop.offsetHeight + scrolltop + "px";
            }
            oTop.onclick = function () {
                document.documentElement.scrollTop = document.body.scrollTop = 570;
            }


            var oTop2 = document.getElementById("to_top2");
            var screenw2 = document.documentElement.clientWidth || document.body.clientWidth;
            var screenh2 = document.documentElement.clientHeight || document.body.clientHeight;
            oTop2.style.left = screenw2 - oTop2.offsetWidth + "px";
            oTop2.style.top = screenh2 - oTop2.offsetHeight + "px";
            window.onscroll = function () {
                var scrolltop2 = document.documentElement.scrollTop || document.body.scrollTop;
                oTop2.style.top = screenh2 - oTop2.offsetHeight + scrolltop2 + "px";
            }
            oTop2.onclick = function () {
                document.documentElement.scrollTop = document.body.scrollTop = 2000;
            }
        }

    </script>
</head>
<body>
<%@ include file="mallSectionHead.jsp" %>
<div class="main-vo">
    <div style="height:400px;width: 1250px;margin:0 auto;overflow: visible;position: relative"><img
            src="../../../static/images/banner_read.jpg" style="max-width: 1903px;position: absolute;left: -326px;"></div>
    <div class="nav-vo" style="bottom:10px;">
        <div class="nav-voo">
            <div class="nav-bg"></div>
            <ul class="orange-all" id="listAc">
                <%--<li class="bg-red" onclick="window.open('/mall/index.do')"><p>商城首页<em>></em></p></li>--%>
                <%--<li class="li-s store-cur" onclick="window.open('/mall/integrate.do')" ><p>活动专区</p><span onclick="window.open('/mall/integrate.do')">端午节特惠</span>、<span onclick="window.open('/mall/discount.do')">全积分兑换</span><em>></em></li>--%>
                <%--&lt;%&ndash;<li class="li-s" onclick="window.open('/mall/discount.do')" >全积分兑换专区<em>></em></li>&ndash;%&gt;--%>
                <%--<li class="li-s"><p>晒才艺</p><span>文具</span>、<span>乐器</span>、<span>棋类</span></li>--%>
                <%--<li class="li-s" onclick="window.open('/mall/index.do?categoryId=56eb6a1d0cf234ce7e479c24')"><p>STEM创客</p><span>STEM创客</span>、<span>益智玩具</span><em>></em></li>--%>
                <%--<li class="li-s" onclick="window.open('/mall/index.do?categoryId=56eb6a0b0cf234ce7e479c1c')"><p>读书/学霸</p><span>教辅教材</span>、<span>小说文学</span><em>></em></li>--%>
                <%--<li class="li-s" onclick="window.open('/mall/index.do?categoryId=56eb6a2d0cf234ce7e479c26')"><p>演讲口才</p><span>名人传记</span>、<span>成功励志</span><em>></em></li>--%>
                <%--<li class="li-s" onclick="window.open('/mall/index.do?categoryId=56eb6a2d0cf234ce7e479c26')"><p>安全健康</p><span>智能硬件</span><em>></em></li>--%>
            </ul>
        </div>
        <%--<div class="register_banner" style="width: 54%;top:70%;min-width: 1040px;" >
            <img src="../../../images/hot_banner_zc.png" style="width: 100%;cursor:pointer;" onclick="window.open('/mall/register.do')">
            <img src="../../../images/pet_61.png" class="pets">
        </div>--%>
    </div>
</div>
<div class="div-s1">
    <div class="register_banner">
        <img src="/static/images/dui_arrow1.png">
    </div>
</div>
<div class="fixed-ff">
    <div class="fiexd-f">
        <div class="control-f" id="fixed-s">
            <img src="/static/images/duif1.png" id="to_top">
            <img src="/static/images/duif2.png" id="to_top2">
        </div>
    </div>
</div>
<div class="pro-list act-red">
    <div class="register_banner clearfix pad25 prolist" id="goodsList">
    </div>
    <div class="div-nofound1" id="notFound">
        <h3>抱歉，没有找到与<em>"${param.regular}"</em>相关的商品</h3>
        <p>建议您：</p>
        <p>1、看看输入的文字是否有误</p>
        <p>2、分成多个词语再次搜索</p>
    </div>
</div>
<div class="div-s1a">
    <div class="register_banner">
        <img src="/static/images/dui_arrow2.png">
    </div>
</div>
<div class="pro-list act-red">
    <div class="register_banner clearfix pad25 prolist">
        <div class="pro-li">
            <a href="/mall/detail.do?id=56fba24c0cf27bf68fcfd07e" target="_blank">
                <div class="div-pro">
                    <img src="../../../static/images/hot_sp_1.png" class="img-pro">
                </div>
                <div class="pro-detail">
                    <h3>儿童益智&nbsp;创意礼品</h3>
                    <p class="p1">实木荷木五合一套装跳棋/五子棋/飞行棋/蛇棋/斗兽棋</p>
                    <p class="p2">
                        <em>￥47.50</em>
                        <button>立即换购</button>
                    </p>
                </div>
            </a>
        </div>
        <div class="pro-li">
            <a href="/mall/detail.do?id=570e0ada0cf2372af0f4c696" target="_blank">
                <div class="div-pro">
                    <img src="../../../static/images/hot_sp_4.png" class="img-pro">
                </div>
                <div class="pro-detail">
                    <h3>儿童益智&nbsp;创意礼品</h3>
                    <p class="p1">沙包 儿童丢沙袋 卡通帆布 纯手工制作（3个装)</p>
                    <p class="p2">
                        <em><i>￥</i>10.50</em>
                        <button>立即换购</button>
                    </p>
                </div>
            </a>
        </div>
        <div class="pro-li">
            <a href="/mall/detail.do?id=5715aa0a0cf2f633eef5adc6" target="_blank">
                <div class="div-pro">
                    <img src="../../../static/images/hot_sp_2.png" class="img-pro">
                </div>
                <div class="pro-detail">
                    <h3>世界名著&nbsp;青少年中小学生课外必读图书籍</h3>
                    <p class="p1">老人与海 精装版 海明威当代近代文学作品</p>
                    <p class="p2">
                        <em>￥12.90</em>
                        <button>立即换购</button>
                    </p>
                </div>
            </a>
        </div>
        <div class="pro-li">
            <a href="/mall/detail.do?id=570e0a770cf2372af0f4c67e" target="_blank">
                <div class="div-pro">
                    <img src="../../../static/images/hot_sp_3.png" class="img-pro">
                </div>
                <div class="pro-detail">
                    <h3>儿童益智&nbsp;创意礼品</h3>
                    <p class="p1">毽子 3铁片鸡毛毽子 比赛毽子（3个装）&nbsp;</p>
                    <p class="p2">
                        <em>￥9.90</em>
                        <button>立即换购</button>
                    </p>
                </div>
            </a>
        </div>
    </div>
</div>
<%----%>
<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>
<!--============登录================-->
<%@ include file="../common/login.jsp" %>

<script id="goodsListTmpl" type="text/template">
    {{ for(var i in it) { }}
    <div class="pro-li">
        <a href="/mall/detail.do?id={{=it[i].goodsId}}" target="_blank">
            <!--<img class="store-IIM" src="/images/store-NEW.png">-->
            <div class="div-pro">
                <img src="{{=it[i].suggestImage}}?" class="img-pro">
            </div>
            <div class="pro-detail">

                <h3>{{=it[i].goodsName}}</h3>
                {{ if(null!=it[i].introduction) { }}
                <p class="p1">{{=it[i].introduction}}&nbsp;</p>
                {{} }}
                {{ if(null==it[i].introduction) { }}
                <p class="p1">&nbsp;</p>
                {{} }}
                <p class="p2">
                    <em>￥{{=it[i].discountPrice/100}}</em>
                    <i>￥{{=it[i].price/100}}&nbsp;</i>
                    <button>立即抢购</button>
                </p>
            </div>
        </a>
    </div>
    {{ } }}
</script>

<script id="listAcTml" type="text/template">
    <li class="bg-red"><p><span style="cursor: pointer" onclick="window.open('/mall')">商城专区首页</span></p></li>
    <li class="li-s store-cur">
        <%--<p onclick="window.open('/mall/integrate.do')">活动专区</p>--%>
        <span>
                <span onclick="location.href='/integrate'">特惠专区</span>
            </span>
        <em>></em>
    </li>
    <%--        <li class="li-s ">
                &lt;%&ndash;<p onclick="window.open('/mall/integrate.do')">活动专区</p>&ndash;%&gt;
                <span>
                    <span onclick="location.href='/mall/discount.do'">全积分兑换专区</span>
                </span>
                <em>></em>
            </li>--%>
    {{ for(var i in it) { }}
    <li class="li-s">
        <%--<p onclick="window.open('/mall/index.do?categoryId={{=it[i].id}}')">{{=it[i].name}}</p>--%>
        <span value="{{=it[i].id}}" class=" listData"></span><span>&nbsp;</span>
        <em>></em>
    </li>
    {{ } }}
    <li class="li-s ">
        <%--<p onclick="window.open('/mall/integrate.do')">活动专区</p>--%>
        <span>
                <span onclick="window.open('/mall/index.do')">全部商品</span>
            </span>
        <em>></em>
    </li>
</script>

<script id="listTml" type="text/template">
    {{ for(var i in it) { }}
    <span onclick="window.open('/mall/index.do?categoryId={{=it[i].parentId}}&levelCategoryId={{=it[i].id}}')">{{=it[i].name}}</span> /
    {{ } }}
</script>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static/js/sea.js"></script>
<!-- Custom js -->
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/mall/0.1.0/integrate.js');
</script>
</body>
</html>
