<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>复兰教育商城</title>
    <link rel="stylesheet" type="text/css" href="/static/css/main.css">
    <link rel="stylesheet" type="text/css" href="/static/css/mallSection/mallindex.css">
    <%--<script type="text/javascript" src="/static/js/mallSection/jquery-1.11.1.js"></script>--%>
    <style>
        .div-center-f {
            border-top: none;
        }

        .nav-bg {
            top: 3px;
        }

        .ul-left-nav {
            top: -50px;
        }

        .header-cont .ha2 {
            background: #E8E8E8;
            color: #3764a0;
        }
    </style>
    <script type="text/javascript">

        window.onload = function () {
            var container = document.getElementById('container');
            var list = document.getElementById('list');
            var buttons = document.getElementById('buttons').getElementsByTagName('span');
            var prev = document.getElementById('prev');
            var next = document.getElementById('next');
            var index = 1;
            var len = 3;
            var animated = false;
            var interval = 3800;
            var timer;


            function animate(offset) {
                if (offset == 0) {
                    return;
                }
                animated = true;
                var time = 1200;
                var inteval = 10;
                var speed = offset / (time / inteval);
                var left = parseInt(list.style.left) + offset;

                var go = function () {
                    if ((speed > 0 && parseInt(list.style.left) < left) || (speed < 0 && parseInt(list.style.left) > left)) {
                        list.style.left = parseInt(list.style.left) + speed + 'px';
                        setTimeout(go, inteval);
                    }
                    else {
                        list.style.left = left + 'px';
                        if (left > -1920) {
                            list.style.left = -1920 * len + 'px';
                        }
                        if (left < (-1920 * len)) {
                            list.style.left = '-1920px';
                        }
                        animated = false;
                    }
                }
                go();
            }

            function showButton() {
                for (var i = 0; i < buttons.length; i++) {
                    if (buttons[i].className == 'on') {
                        buttons[i].className = '';
                        break;
                    }
                }
                buttons[index - 1].className = 'on';
            }

            function play() {
                timer = setTimeout(function () {
                    next.onclick();
                    play();
                }, interval);
            }

            function stop() {
                clearTimeout(timer);
            }

            next.onclick = function () {
                if (animated) {
                    return;
                }
                if (index == 3) {
                    index = 1;
                }
                else {
                    index += 1;
                }
                animate(-1920);
                showButton();
            }
            prev.onclick = function () {
                if (animated) {
                    return;
                }
                if (index == 1) {
                    index = 3;
                }
                else {
                    index -= 1;
                }
                animate(1920);
                showButton();
            }

            for (var i = 0; i < buttons.length; i++) {
                buttons[i].onclick = function () {
                    if (animated) {
                        return;
                    }
                    if (this.className == 'on') {
                        return;
                    }
                    var myIndex = parseInt(this.getAttribute('index'));
                    var offset = -1920 * (myIndex - index);

                    animate(offset);
                    index = myIndex;
                    showButton();
                }
            }

            container.onmouseover = stop;
            container.onmouseout = play;

            play();

        }
    </script>
</head>
<body login="${login}">
<%--===============头部================--%>
<%--<%@ include file="../forum/head.jsp" %>--%>
<%@ include file="mallSectionHead.jsp" %>

<%--===============头部================--%>
<div class="container">
    <div>
        <div class="div-center-f">
            <div style="height: 400px;position: absolute;left: -330px;top:0;width:1907px;border-top: 3px solid #ff500d;">
                <div id="container">
                    <div id="list" style="left: -1920px;">
                        <c:if test="${banners != null || fn:length(list) >2}">
                            <img src="${banners[0]['imageUrl']}" <c:if
                                    test="${!empty banners[0]['targetId']}"> style="cursor: pointer" onclick="window.open('/mall/detail.do?id=${banners[0]['targetId']}')"</c:if>/>
                            <img src="${banners[1]['imageUrl']}" <c:if
                                    test="${!empty banners[1]['targetId']}"> style="cursor: pointer" onclick="window.open('/mall/detail.do?id=${banners[1]['targetId']}')"</c:if>/>
                            <img src="${banners[2]['imageUrl']}" <c:if
                                    test="${!empty banners[2]['targetId']}"> style="cursor: pointer" onclick="window.open('/mall/detail.do?id=${banners[2]['targetId']}')"</c:if>/>
                            <img src="${banners[0]['imageUrl']}" <c:if
                                    test="${!empty banners[0]['targetId']}"> style="cursor: pointer" onclick="window.open('/mall/detail.do?id=${banners[0]['targetId']}')"</c:if>/>
                            <img src="${banners[1]['imageUrl']}" <c:if
                                    test="${!empty banners[1]['targetId']}"> style="cursor: pointer" onclick="window.open('/mall/detail.do?id=${banners[1]['targetId']}')"</c:if>/>
                        </c:if>
                    </div>
                    <a href="javascript:;" id="prev" class="arrow">&lt;</a>
                    <a href="javascript:;" id="next" class="arrow">&gt;</a>
                </div>
            </div>
            <div class="btn-bg"></div>
            <div id="buttons">
                <span index="1" class="on"></span>
                <span index="2"></span>
                <span index="3"></span>
            </div>
            <div class="nav-banner">

                <div class="nav-bg"></div>
                <ul class="ul-left-nav" id="listAc">

                </ul>
            </div>
        </div>
    </div>
    <!-- 限时抢购 begin -->
    <div class="mall-list-info">
        <span class="list-title">精品限时购</span>
        <span class="djs" id="djs"></span>
    </div>
    <div class="mall-list clearfix">
        <div class="xsyh">
            <span class="span-xsyh">下一场预告 22号10点</span>
            <img src="/static/images/mallSection/bgs1.png" class="img-xsyh">
        </div>
        <div class="nav-list" id="goodscategory">

        </div>
    </div>
    <!-- 限时抢购 end -->

    <!-- 广告 begin-->
    <div class="banner-red"></div>
    <!-- 广告 end-->

    <!-- 团购活动 begin-->
    <div class="mall-list-info">
        <span class="list-title">团购活动</span>
        <img src="/static/images/mallSection/img_baoyou.png">
    </div>
    <div class="mall-list clearfix" id="groupPurchase">

    </div>
    <!-- 团购活动 end-->

    <!-- 广告 begin-->
    <div class="banner-blue"></div>
    <!-- 广告 end-->

    <!-- 本月上新 begin -->
    <div class="mall-list-info">
        <span class="list-title">本月上新</span>
        <img src="/static/images/mallSection/img_gg.png">
    </div>
    <div class="mall-list clearfix">
        <ul class="ul-new-nav" id="category">

        </ul>
        <div>
            <ul class="ul-new clearfix" id="ulNew">

            </ul>
        </div>
    </div>

</div>

<%--圣诞背景--%>
<div class="christ-quan-wind">恭喜你获得<span id="christmasPrice">5</span>元优惠券<br><button style="width: 130px;height: 30px;margin-top:10px;color: #fff;border:none;border-radius: 3px;background: #FE4600" onclick="document.location.href=('/integrate')">快去选购...</button></div>
<div class="christ-bg" <c:if test="${login==false}">style="display: none" </c:if>></div>

<%--圣诞节活动 st--%>
<div class="christmas-wind" <c:if test="${login==false}">style="display: none" </c:if>>
    <img class="christ-light1" src="/static/images/mallSection/christ_1.png">
    <img class="christ-light2" src="/static/images/mallSection/christ_2.png">
    <div class="hide-light1 hl">
        <img src="/static/images/mallSection/light1.png">
    </div>
    <div class="hide-light2 hl">
        <img src="/static/images/mallSection/light2.png">
    </div>
    <div class="hide-light3 hl">
        <img src="/static/images/mallSection/light3.png">
    </div>
    <div class="hide-light4 hl">
        <img src="/static/images/mallSection/light4.png">
    </div>
    <div class="christ-x"><img src="/static/images/mallSection/close.png"></div>
</div>

<
<%--圣诞节活动 ed--%>



<!-- 本月上新 end-->
<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>
<a hidden class="backtop"></a>

<!--============登录================-->
<%@ include file="../common/login.jsp" %>
<!-- Javascript Files -->

<script id="listAcTml" type="text/template">
    <li class="bg-red">
        <p><span style="cursor: pointer" onclick="location.href='/mall'">商城专区首页</span></p>
    </li>
    <a class="nav-a">
        <li class="bg-orange">
            <%--<p onclick="window.open('/mall/integrate.do')">活动专区</p>--%>
            <span>
            <span onclick="window.open('/integrate')">节日特惠专区</span>
        </span>
            <em>></em>
        </li>
    </a>
    <a class="nav-a">
        <li class="bg-orange">
            <%--<p onclick="window.open('/mall/integrate.do')">活动专区</p>--%>
            <span>
            <span onclick="window.open('/mall/discount.do')">全积分兑换专区</span>
        </span>
            <em>></em>
        </li>
    </a>
    {{ for(var i in it) { }}
    <a class="nav-a">
        <li class="bg-orange">
            <%--<p onclick="window.open('/mall/index.do?categoryId={{=it[i].id}}')">{{=it[i].name}}</p>--%>
            <span value="{{=it[i].id}}" class="listData"></span><span>&nbsp;</span>
            <em>></em>
        </li>
    </a>
    {{ } }}
    <a class="nav-a">
        <li class="bg-orange" style="cursor: default">
            <%--<p onclick="window.open('/mall/integrate.do')">活动专区</p>--%>
            <span>
            <span style="cursor: pointer" onclick="window.open('/mall/index.do')">全部商品</span>
        </span>
            <em>></em>
        </li>
    </a>
</script>

<script id="listTml" type="text/template">
    {{ for(var i in it) { }}
    <span onclick="window.open('/mall/index.do?categoryId={{=it[i].parentId}}&levelCategoryId={{=it[i].id}}')">{{=it[i].name}}</span> /
    {{ } }}
</script>

<script id="nvTml" type="text/template">
    {{ for(var i in it) { }}
    <span>{{=it[i].name}}</span> /
    {{ } }}
</script>


<script id="catLTml" type="text/template">
    {{ for(var i in it) { }}
    <span>{{=it[i].name}}</span> /
    {{ } }}
</script>

<script id="goodscategoryTmpl" type="text/template">
    <%--<div class="nv" value="ALL">活动专区<div class="tran">--%>
    <%--</div>--%>
    <%--<ul class="list3" id="ALL">--%>
    <%--</ul>--%>
    <%--</div>{{=it[i].name}}--%>
    {{ for(var i in it) { }}
    <div class="nv" value="{{=it[i].id}}">
        <span class="spanItem" value="{{=it[i].id}}"></span>
        <div class="tran">
        </div>
        <ul class="list3" id="{{=it[i].id}}">
        </ul>
    </div>
    {{ } }}
</script>

<script id="goodsListTmpl" type="text/template">
    {{ for(var i in it) { }}
    <span onclick="window.open('/mall/detail.do?id={{=it[i].goodsId}}')">
  <li>
    <div class="pro-title" name="ss">{{=it[i].goodsName}}</div>
    <div class="img-pro-f">
      <img src="{{=it[i].suggestImage}}?imageView2/2/w/200/h/200" style="cursor: pointer" class="img-pro">
    </div>
    <span class="prince">￥&nbsp;{{=it[i].discountPrice/100}}</span>
    <button onclick="window.open('/mall/detail.do?id={{=it[i].goodsId}}')">立即抢购</button>
  </li>
      </span>
    {{ } }}
</script>

<script id="groupPurchaseTmpl" type="text/template">
    {{ for(var i in it) { }}
    <div class="li-tuangou">
        <div class="img-tg-f">
            <img src="{{=it[i].suggestImage}}?imageView2/2/w/200/h/200" style="cursor: pointer" class="img-tg"
                 onclick="window.open('/mall/detail.do?id={{=it[i].goodsId}}')">
        </div>
        <p class="p-pro-name p-pro">{{=it[i].goodsName}}</p>
        <p class="p-pro-prince p-pro">
            <em>￥&nbsp;{{=it[i].discountPrice/100}}</em>
            <span>￥&nbsp;{{=it[i].price/100}}<i></i></span>
        </p>
        <p class="p-pro-tag p-pro">
            <em>6折</em>
            <em>包邮</em>
        </p>
        <p class="p-pro-buy p-pro">
            <button onclick="window.open('/mall/detail.do?id={{=it[i].goodsId}}')">立即抢购</button>
            <!--<em>160人已抢购</em>-->
        </p>
    </div>
    {{ } }}
</script>

<script id="categoryTmpl" type="text/template">
    {{ for(var i in it) { }}
    <%--{{=it[i].name}}--%>
    <li class="cat" value="{{=it[i].id}}"><span class="catL" value="{{=it[i].id}}"></span></li>
    {{ } }}
</script>


<script id="goodsList" type="text/template">
    {{ for(var i in it) { }}
    <span onclick="window.open('/mall/detail.do?id={{=it[i].goodsId}}')">
  <li>
    <div class="img-new-pro-f">
      <img src="{{=it[i].suggestImage}}?imageView2/2/w/200/h/200" style="cursor: pointer" class="img-new-pro">
    </div>
    <p class="p-new-pro-name">{{=it[i].goodsName}}</p>
    <p class="p-new-pro-prince">￥&nbsp;{{=it[i].price/100}}
      <em>小编力推</em>
    </p>
  </li>
              </span>
    {{ } }}
</script>

<!-- initialize seajs Library -->
<script src="/static/js/sea.js"></script>
<!-- Custom js -->
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/mall/0.1.0/mallSection.js');
</script>
</body>
</html>
