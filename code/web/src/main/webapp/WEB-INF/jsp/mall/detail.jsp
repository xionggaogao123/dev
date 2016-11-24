<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta property="qc:admins" content="227117016363634637575144"/>
    <title>复兰商城</title>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
    <link href="/static/js/modules/core/0.1.0/fancyBox/jquery.fancybox.css?v=2015041602" rel="stylesheet"
          type="text/css" media="screen"/>
    <!-- Start of KF5 supportbox script -->
    <script src="//assets.kf5.com/supportbox/main.js" id="kf5-provide-supportBox"
            kf5-domain="fulankeji.kf5.com"></script>
    <!-- End of KF5 supportbox script -->
</head>
<body id="${param.id}" login="${login}">
<%--===============头部================--%>
<%@ include file="mallSectionHead.jsp" %>
<div style="border-top:4px solid #ff510d"></div>
<div class="ebusiness-T">
    <ul class="ul-nav-s" id="listAc">
    </ul>
</div>

<!--================查看商品==============-->
<div class="store-check">
    <div class="store-check-top">
        <div class="store-check-XQ">
            <div class="store-check-left">
                <div class="div-img">
                    <img src="" id="sugimg">
                </div>
                <div id="images" class="img-list"></div>
                <div class="img-list-IM">
                    <c:if test="${isCollection == true}">
                        <img id="collectionB" class="img-listt" src="/static/images/detail/img-list-Hsolid.png">
                    </c:if>
                    <c:if test="${isCollection == false}">
                        <img id="collectionA" class="img-listt" src="/static/images/detail/img-list-solid.png">
                        <img id="collectionB" style="display: none;" class="img-listt"
                             src="/static/images/detail/img-list-Hsolid.png">
                    </c:if>
                </div>
            </div>
            <div class="store-check-right">
                <dl>
                    <dt>
                    <h1 id="name"></h1>
                    <p class="p-intro"></p>
                    </dt>
                    <dt>
                        优惠价<i id="disCountPrice">￥&nbsp;</i>
                        <span class="text-yj">原价</span>&nbsp;<em id="price" hidden>￥</em>
                    </dt>
                    <dt style="display:none">
                    <div style="width: 425px;height: 72px;background: #ffd6df">
                        <div style="height: 41px;background: #ff500d;color:#fff;">
                            <span class="span-yh-f" style="">限时优惠</span>
                            <span class='span-yh' id="djs"></span>
                        </div>
                        <img class="img-jb" src="/images/logo_rmb.png">
                        <span class="span-jfkd"></span>
                    </div>
                    </dt>
                    <dt>
                        <i class="Detail-S"></i>
                        <i class="Detail-Y">运费：
                            <label id="selectPrvLabel"
                                   style="border: 1px solid #aaa;padding: 1px;margin-right:10px;width:70px;background-color:white;color:#000;">
                                <i id="selectProvince"><%--选择地区--%></i>
                            </label><i id="exPress"><%--非包邮地区--%></i>

                            <div id="selectPrvCtx"
                                 style="display: none;position: absolute;top:30px;left:0px;width:265px;padding:5px;background-color: white;z-index:2;border:1px solid #ccc;"></div>
                            <script id="selectPrvJS" type="application/template">
                                {{~it:value:index}}
                                <label style="width:60px;background-color:white;color:#000;margin:1px;">
                                    <i onclick="selectPrvClk('{{=value.name}}')">{{=value.name}}</i>
                                </label>
                                {{~}}
                            </script>
                        </i>
                        <i class="i-free">订单满99元免运费</i>
                    </dt>
                    <dt>
                        <%--<span>七天无理由退货&nbsp;|&nbsp;正品保证</span>--%>
                        <span class="Detail-I"></span>
                        <span class="Detail-II"></span>
                        <span class="Detail-III"></span>
                    </dt>
                    <div style="border-top: 1px solid #dddddd;" id="kindList"></div>
                    <dt>
                        <i>数量：</i>
                        <label id="reduceNum">-</label><input id="num" value="1"><label id="addNum">+</label>
                    </dt>
                    <dt>
                        <span class="store-LJ">立即购买</span>
                        <span class="store-JR">加入购物车</span>
                        <c:if test="${isStudent == true}">
                            <span class="store-JZ">家长一键购</span>
                        </c:if>
                    </dt>
                </dl>
            </div>
        </div>
        <!--==================商品详情&买家评价=================-->
        <div class="store-check-MJ">
            <div class="store-MJ-left">
                <div class="store-MM">
                    <ul>
                        <li class="store-MJ">
                            商品详情
                        </li>
                        <li class="store-MJJ">
                            买家评价（
                            <n id="mjpj">0</n>
                            ）
                        </li>
                    </ul>
                </div>
                <!--=================商品详情================================-->
                <div class="store-pingjia"></div>
                <!--====================买家评价=============================-->
                <div class="store-shangpin">
                    <div id="commentList"></div>
                </div>
            </div>
            <div class="store_MJ-right">
                <div>
                    <div class="store-MJ-right-top">
                        您可能喜欢的商品
                    </div>
                    <div class="store-MJ-right-bottom">
                        <ul id="goodsList"></ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>
<a hidden class="backtop"></a>

<!--============登录================-->
<%@ include file="../common/login.jsp" %>


<script id="kindListTmpl" type="text/template">
    {{ for(var i in it) { }}
    <dd>
        <em id="{{=it[i].kindId}}">{{=it[i].kindName}}：</em>
        {{var specList = it[i].specList;}}
        {{ for(var j in specList){ }}
        <span id="{{=specList[j].id}}" p="{{=specList[j].price}}">{{=specList[j].name}}</span>
        {{} }}
    </dd>
    {{ } }}
</script>

<script id="commentListTmpl" type="text/template">
    {{ for(var i in it) { }}
    <div class="store-message">
        <img class="store-message-IM" src="{{=it[i].userAvatar}}">

        <div>
            <em>{{=it[i].userName}}</em>
            {{ for(var k=0; k< it[i].score;k++){ }}
            <img src="/static/images/store-Hxing.png">
            {{}}}
            <p>{{=it[i].content}}</p>
            {{var imgs = it[i].images;}}
            {{for(var j in imgs){ }}
            <a class="fancybox" href="{{=imgs[j].value}}" data-fancybox-group="{{=it[i].eCommentId}}">
                <img class="store-message-IMM" src="{{=imgs[j].value}}">
            </a>
            {{} }}

            <%--<img class="store-message-IMM" src="http://img14.360buyimg.com/n0/jfs/t2461/281/145335373/97081/8af73dbf/55f0e80aN532efcae.jpg">--%>
            <%--<label>颜色：黑色</label>--%>
        </div>
        <%--<span>{{=it[i].date}}</span>--%>
    </div>
    {{ } }}
</script>

<script id="goodsListTmpl" type="text/template">
    {{ for(var i in it) { }}
    <li class="detail" id="{{=it[i].goodsId}}">
        <a href="/mall/detail.do?id={{=it[i].goodsId}}" target="_blank"></a>
        <img class="store-IIM" src="/static/images/store-NEW.png">

        <div>
            <img src="{{=it[i].suggestImage}}">
            <dl>
                <dd>{{=it[i].goodsName}}</dd>
                <dt>￥&nbsp;{{=it[i].price/100}}</dt>
                <em></em>
                <%--<a href="javascript:;"></a>--%>
            </dl>
        </div>
    </li>
    {{ } }}
</script>

<script id="listAcTml" type="text/template">
    <li class="li1" onclick="window.open('/mall/mallSection.do')">商城专区首页<em>></em>
        <div class="ul-nav-bg "></div>
    </li>
    <li class="li2" onclick="window.open('/integrate.do')"><span>节日特惠专区</span><em>></em></li>
    <li class="li2" onclick="window.open('/mall/discount.do')"><span>全积分兑换专区</span><em>></em></li>
    {{ for(var i in it) { }}
    <li class="li2"><span value="{{=it[i].id}}" class="listData"></span><em>></em></li>
    {{ } }}
    <li class="li2" onclick="window.open('/mall/index.do')"><span>全部商品</span><em>></em></li>
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
<!-- 获取所在地省份 -->
<script type="text/javascript" src="http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js"></script>
<script>
    seajs.use('/static/js/modules/mall/0.1.0/detail', function (ex) {
        ex.selectPrvClk(remote_ip_info.province);//获取所在地运费模板
    });
</script>

</body>
</html>
