<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html style="background: white;">
<head>
    <meta property="qc:admins" content="223027642741216375"/>
    <title>复兰商城</title>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <!-- Start of KF5 supportbox script -->
    <script src="//assets.kf5.com/supportbox/main.js" id="kf5-provide-supportBox"
            kf5-domain="fulankeji.kf5.com"></script>
    <!-- End of KF5 supportbox script -->
    <style>
        .login-bar {
            border-bottom: none;
        }
    </style>
</head>
<body login="${login}" state="${param.state}" regular="${param.regular}">
<input id="categoryId" type="hidden" value="${categoryId}">
<input id="levelCategoryId" type="hidden" value="${levelCategoryId}">
<%--===============头部================--%>
<%@ include file="mallSectionHead.jsp" %>
<%--===============头部================--%>

<div class="store-main-shop">
    <!--=============头部广告===========-->
    <%-- <div class="store-GG" style="min-width: 1250px;">
       <img src="/img/K6KT/main-page/store-DZ.jpg">
     </div>--%>
    <!--============产品简介==========-->
    <%--<div class="store-main-top">
        <span>&lt;%&ndash;<a href="/mall">&ndash;%&gt;商城首页&lt;%&ndash;</a>&ndash;%&gt;&gt;</span><span id="menuName">STEM创客</span>
    </div>--%>
    <div class="store-CP">
        <div class="nav-bg1"></div>
        <div class="store-CP-left">
            <ul id="goodscategory" class="newgoodscategory"></ul>
        </div>
        <div class="store-guidee">
            <%--STEM--%>
            <div class="store-CP-guidee" id="guide1">
                <%--<img src="/img/eBusiness/stem.png">--%>
                <div class="div-four clearfix">
                    <video src="/img/eBusiness/wenju.mp4" width="275" controls>
                        <source type=video/mp4/>
                        Your browser does not support the video tag.
                    </video>

                    <%--<div id="videoSTEM" style="width: 260px; height: 200px;float: left;background: #000;">
                        <script type="text/javascript" src="/static/plugins/sewiseplayer/sewise.player.min.js"></script>
                    </div>
                    <script type="text/javascript">
                        SewisePlayer.setup({
                            server: "vod",
                            type: "mp4",
                            skin: "vodFlowPlayer",
                            logo: "none",
                            lang: "zh_CN",
                            topbardisplay: 'disable',
                            clarityButton: 'disable',
                            timeDisplay: 'disable',
                            videourl: '/img/eBusiness/stem.mp4'
                        });
                    </script>--%>
                </div>
                <p>

                <h1>书法</h1>
                <span>书法是中国古典艺术的一朵奇葩，在文字发展的历史中，没有哪一国的文字像中国的汉字这样，
                    发展成为一门独有的艺术形式。人们说书法是“无声的音乐”、“纸面上的舞蹈”，是“人类情感的心电图”。
                    书法艺术以其抽象、灵动、丰富的线条给人以复杂多样的审美感受。
                    著名抽象派绘画大师毕加索曾说：“倘若我是一个中国人，那么，我将不是一个画家，而是一个书法家，我要用我的书法来写我的画。”
                </span>
                </p>
            </div>
            <%--书籍绘本--%>
            <div class="store-CP-guidee" id="guide2">
                <%--<img src="/img/eBusiness/book2.png">--%>
                <%--<p>--%>
                <%--<h1>小朋友，知道映雪囊萤的故事吗？</h1>--%>
                <%--<span>晋朝时候，有一个人名叫孙康，非常好学。他家里很穷买不起灯油，冬天夜里，他常常不顾天寒地冻，在户外借着白雪的光亮读书。当时还有一个人，叫车胤，也和孙康一样，没有钱买灯油。夏天夜晚，他就捉了许多萤火虫，盛在纱袋里，用萤光照亮，夜以继日地学习。小朋友，你有好好学习吗？</span>--%>
                <%--</p>--%>
                <div class="div-four clearfix">
                    <video src="/img/eBusiness/stems.mp4" width="275" controls>
                        <source type=video/mp4/>
                        Your browser does not support the video tag.
                    </video>
                </div>
                <p>

                <h1>积木玩具</h1>
                <span>积木玩具是培养锻炼儿童智力最好的早教玩具，它提供更多的机会去发挥他们的想象力和创造力。
                    孩子们在摆搭之前要想好自己搭些什么，要观察，思考选用什么样的块块才能体现他所构想的形象，
                    因此对儿童的想象力，结构思维，造型能力是很好的锻炼，而且可发展孩子们的空间知觉，创造力，发挥儿童组织力和理解力，增强耐力及对建筑科学的兴趣。
                    所以，粑粑麻麻们，放下手机，拿出你们的时间和爱陪宝宝多玩一玩吧。
                    </span>
                </p>
            </div>
            <%--安全健康--%>
            <div class="store-CP-guidee" id="guide3">
                <div class="div-four clearfix">
                    <video src="/img/eBusiness/wenxue.mp4" width="275" controls>
                        <source type=video/mp4/>
                        Your browser does not support the video tag.
                    </video>
                    <%--<embed width="275" height="200" src="/img/eBusiness/health.mp4">--%>
                </div>
                <p>

                <h1>读书</h1>
                <span>读书---是要让真正的阳光住在心里
                          读书---不仅为人生指引方向，更使我们内心强大
                          读书---与思想共舞，充实美丽人生
                          读书---让生活更美好
                    </span>
                </p>
            </div>
            <%--学习文具--%>
            <div class="store-CP-guidee" id="guide4">
                <div class="clearfix div-four">
                    <video src="/img/eBusiness/mingren.mp4" width="275" controls>
                        <source type=video/mp4/>
                        Your browser does not support the video tag.
                    </video>
                    <%--<embed width="275" height="200" src="/img/eBusiness/learn.mp4">--%>
                </div>
                <p>

                <h1>名人传记</h1>
                <span> 但是任何一种时代的变化，任何社会的矛盾，都是年轻人的机会，如果不变化，在座的人你们一点机会都没有，
                    如果不变化，工业时代将走下去，人家就论资排辈，轮不到你什么机会。
                    只有变化才是年轻人的机会……我们必须适应变化的时代，你不变化一定死，没机会，
                    你变化了也许有机会。所以我是感谢这个了不起的时代。
                    ——马云 2013年5月4日加州斯坦福大学</span>
                </p>
            </div>
            <%--益智玩具--%>
            <div class="store-CP-guidee" id="guide5">
                <div class="clearfix div-four">
                    <video src="/img/eBusiness/jiankang.mp4" width="275" controls>
                        <source type=video/mp4/>
                        Your browser does not support the video tag.
                    </video>
                    <%--<embed allowfullscreen="true" width="275" height="200" src="/img/eBusiness/toy.mp4">--%>
                </div>
                <p>
                <h1>智能看护类机器人</h1>
                <span>智能看护类机器人，是目前中国市场上最流行的关注儿童安全健康的产品。
                    主要为婴幼儿和儿童看护提供智能化服务，360度智能监控、语音互动、智能早教等功能，可对孩子进行24小时的看护，
                    确保孩子的安全，并陪伴孩子，给孩子带来快乐和智力的启蒙教育等。
                    让你上班、出差的时候都能随时随地看到孩子在干嘛，让孩子的安全不再担心，
                    让暑期独处的孩子有最亲密的玩伴。。。
                </span>
                </p>
            </div>
            <%--筛选排序--%>
            <div class="store-guide-top">
                <div id="grade" style="display:none;">
                    <em class="store-guide-tt">分类：</em>
                    <span class="store-guide-top-yy">
                        <span id="gradeSpan" class="grade-cursor">教辅教材</span>
                        <span id="bookTypeSpan">课外阅读</span>
                    </span>
                    <span id="gradeCtx" class="store-guide-top-Y"></span>
                    <script id="gradeTmpl" type="application/template">
                        {{~it:value:index}}
                        <label>
                            <input type="checkbox" value="{{=value.id}}">
                            <span value="{{=value.id}}">{{=value.name}}</span>
                        </label>
                        {{~}}
                    </script>
                    <span id="bookTypeSearch" style="display: none;" class="store-guide-top-Y">
                        <span value="1">绘本</span>
                        <span value="2">文学</span>
                        <span value="3">科普百科</span>
                    </span>
                </div>
                <div id="classicTo" style="display:none;">
                    <em>分类：</em>
                    <span class="store-guide-top-yy" id="classic">
                    </span>
                </div>

                <div>
                    <em>价格：</em>
                    <span id="priceSearch" class="store-guide-top-Y">
                        <span value="0~99">0~99</span>
                        <span value="100~199">100~199</span>
                        <span value="200~499">200~499</span>
                        <span value="500~">500以上</span>
                    </span>
                </div>
                <div class="store-guide-bottom">
                    <em>排序：</em>
                    <span id="sort" class="store-guide-bottom-Y">
                        <span class="storee-SP <%--store-saixuan-t--%> sort" type="2">新品</span>
                        <span class="<%--store-saixuan-t--%> sort" type="6">销量</span>
                        <span class="store-saixuan-b sort" type="3">价格</span>
                        <span class="<%--store-saixuan-t--%> sort" type="8">人气</span>
                    </span>
                </div>
            </div>
            <div style="clear: both">
                <ul id="goodsList"></ul>
                <div class="div-nofound1" id="notFound">
                    <h3>抱歉，没有找到与<em>"${param.regular}"</em>相关的商品</h3>
                    <p>建议您：</p>
                    <p>1、看看输入的文字是否有误</p>
                    <p>2、分成多个词语再次搜索</p>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="new-page-links"></div>
<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>
<!--============登录================-->
<%@ include file="../common/login.jsp" %>

<script id="goodscategoryTmpl" type="text/template">
    <li value="ALL" class="bg-red">
        <p><span style="cursor: pointer" onclick="window.open('/mall.do')">商城专区首页</span></p>

    </li>
    <li class="li1">
        <span>
            <span onclick="location.href='/integrate.do'">特惠专区</span>
        </span>
        <em>&gt;</em>
    </li>
    {{ for(var i in it) { }}
    <li class="li1 {{=it[i].id}}" value="{{=it[i].id}}">
        <span class="li11" value="{{=it[i].id}}"></span>
        <em>&gt;</em>
    </li>
    {{ } }}
    <li value="ALL" class="li1 store-cur">
        <span>
            <span onclick="window.open('/mall/index.do')">全部商品</span>
        </span>
        <em>&gt;</em>
    </li>
</script>

<script id="goodsListTmpl" type="text/template">
    {{ for(var i in it) { }}
    <li class="detail" id="{{=it[i].goodsId}}">
        <a href="/mall/detail.do?id={{=it[i].goodsId}}" target="_blank"></a>
        <div>
            <img src="{{=it[i].suggestImage}}?imageView2/2/w/200/h/200">
            <dl>
                <dd>{{=it[i].goodsName}}</dd>
                <dt>
                    <em>￥&nbsp;{{=it[i].price/100}}</em></dt>
            </dl>
        </div>
    </li>
    {{ } }}
</script>

<script id="classicTmpl" type="text/template">
    <span class="lc" value="" id="trmAll">全部</span>
    {{ for(var i in it) { }}
    <span class="lc" id="{{=it[i].id}}" value="{{=it[i].id}}">{{=it[i].name}}</span>
    {{ } }}
</script>

<script id="li11Tmpl" type="text/template">
    {{ for(var i in it) { }}
    <span class="llc" value="{{=it[i].id}}">{{=it[i].name}}</span> /
    {{ } }}
</script>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static/js/sea.js"></script>
<!-- Custom js -->
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/mall/0.1.0/index.js');
</script>
</body>
</html>
