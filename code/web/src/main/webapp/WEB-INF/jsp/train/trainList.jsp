<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/12/5
  Time: 9:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>找培训</title>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="/static/css/train/findtrain.css">
    <script type="text/javascript" src="/static/js/modules/train/jquery-1.8.0.min.js"></script>
    <script type="text/javascript"
            src="http://webapi.amap.com/maps?v=1.3&key=3a1cd4cff6fcbdf71ea760da6957fb94"></script>
    <%--<script type="text/javascript" src="/static/js/modules/train/trainList.js"></script>--%>
</head>
<body region="${region}">
<%@ include file="../common/head.jsp" %>
<div id="containerMap"></div>
<div class="train-container">
    <div class="top-city">
        <img src="/static/images/train/location01.png">
        <div class="city">
            <span class="city-name Left">上海</span>
            <div class="city-select Left">
                <a href="javascript:void(0);" class="selector">切换省份</a>
                <div class="d-traing"></div>
                <div class="hide_city_group clearfix" id="topRegion">
                </div>
                <script type="text/template" id="topRegionTmpl">
                 {{~it:value:index}}
                 <div> <a href="javascript:void(0)" regionId="{{=value.id}}" regionName="{{=value.name}}">{{=value.name}}</a></div>
                 {{~}}
                </script>
            </div>
            <span class="common-bg city-logo"></span>
        </div>
        <%--<button>添加商户信息</button>--%>
        <%--<button>老师入口</button>--%>
        <%--<button>培训机构入口</button>--%>
    </div>
    <div class="train-nav" id="trainTop">
    </div>
    <div class="train-type">
        <div class="clearfix d1f ">
            <div class="dl">
                <p class="sp1">分类</p>
                <span class="cur2" itemId="">不限</span>
            </div>
            <div class="dr clearfix" id="itemType">
            </div>
            <script id="itemTypeTmpl" type="text/template">
                {{~it:value:index}}
                  <p><span itemId="{{=value.id}}">{{=value.name}}</span></p>
                {{~}}
            </script>
        </div>
        <div class="clearfix d2f">
            <div class="dl">
                <p class="sp1">位置</p>
                <span class="cur2" region="">不限</span>
            </div>
            <div class="dr clearfix" id="region">
            </div>
            <script type="text/template" id="regionTmpl">
            {{~it:value:index}}
            <p><span region="{{=value.id}}">{{=value.name}}</span></p>
            {{~}}
            </script>
        </div>
    </div>
    <div class="lesson-menu">
        <span class="cur3 bln" tip="1">默认</span>
        <span tip="2">最新发布<img src="/static/images/train/arrow_lesson.png"></span>
        <%--<p class="p-page">--%>
            <%--<i class="i1"></i><em>1</em>/1<i class="i2"></i>--%>
        <%--</p>--%>
    </div>
    <ul class="lesson-list clearfix" id="institute">
        <div class="h-load" hidden>
            <div class="loading-d">
                <img src="/static/images/loading.gif">
                <span>数据加载中...</span>
            </div>
        </div>
    </ul>
    <div class="new-page-links"></div>
    <script type="text/template" id="instituteTmpl">
    {{~it:value:index}}
    <li>
        <img src="{{=value.imageUrl}}" onclick="window.open('/train/trainDetail.do?detailId={{=value.id}}&itemId='+$('#trainTop').data($('#trainTop').find('.cur1').text()))">
        <div class="name">{{=value.name}}</div>
        <div class="star">
            <span>{{=value.score}}</span>
            <p>
                {{~value.unScoreList:unscore:i}}
                <img src="/static/images/train/star_gray.png">
                {{~}}
                {{~value.scoreList:score:i}}
                <img src="/static/images/train/star_golden.png">
                {{~}}
            </p>
        </div>
    </li>
    {{~}}
    </script>
</div>
<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/train/trainList.js', function (trainList) {
        trainList.init();
    });
</script>
</body>
</html>
