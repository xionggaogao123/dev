<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/12/5
  Time: 9:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>培训详情</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <link rel="stylesheet" type="text/css" href="/static/css/train/findtrain.css">
    <link rel="stylesheet" type="text/css" href="/static/css/forum/forum.css">
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
    <script type="text/javascript" src="/static/js/modules/train/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="/static/js/modules/train/jquery.raty.min.js"></script>
    <script type="text/javascript"
    src="http://api.map.baidu.com/api?v=2.0&ak=TzFCVsUAf4RzyoOdgZ5tB10fASv5Dswy"></script>
    <%--<link rel="stylesheet" href="http://cache.amap.com/lbs/static/main1119.css"/>--%>
    <script type="text/javascript"
            src="http://webapi.amap.com/maps?v=1.3&key=3a1cd4cff6fcbdf71ea760da6957fb94"></script>
    <%--<script type="text/javascript" src="http://cache.amap.com/lbs/static/addToolbar.js"></script>--%>
    <%--<style type="text/css">--%>
    <%--#tip {--%>
    <%--height: 50px;--%>
    <%--background-color: #fff;--%>
    <%--padding-left: 10px;--%>
    <%--padding-right: 10px;--%>
    <%--border: 1px solid #969696;--%>
    <%--position: absolute;--%>
    <%--font-size: 12px;--%>
    <%--right: 10px;--%>
    <%--bottom: 20px;--%>
    <%--border-radius: 3px;--%>
    <%--line-height: 45px;--%>
    <%--}--%>

    <%--#tip input[type='button'] {--%>
    <%--margin-left: 10px;--%>
    <%--margin-right: 10px;--%>
    <%--margin-top: 10px;--%>
    <%--background-color: #0D9BF2;--%>
    <%--height: 30px;--%>
    <%--text-align: center;--%>
    <%--line-height: 30px;--%>
    <%--color: #fff;--%>
    <%--font-size: 12px;--%>
    <%--border-radius: 3px;--%>
    <%--outline: none;--%>
    <%--border: 0;--%>
    <%--float: right;--%>
    <%--}--%>
    <%--</style>--%>
    <%--<script type="text/javascript" src="/static/js/modules/train/trainDetail.js"></script>--%>
    <script type="text/javascript">
        $(function () {
            var offset = $('#trainRight').offset();
            $(window).scroll(function () {
                //检查对象的顶部是否在游览器可见的范围内
                var scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
                if (offset.top < scrollTop) {
                    $('#trainRight').addClass('fixed-r');
                }
                else {
                    $('#trainRight').removeClass('fixed-r');
                }
            });


            $("#p-star").raty({
                starOff: '/static/images/train/star_gray.png',
                starOn: '/static/images/train/star_golden.png',
                cancel: false,
                target: "#p-rel",
                hints: ['1分 差', '2分 较差', '3分 及格', '4分 满意', '5分 优秀'],
                targetKeep: true,
                width: 150,
            })

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

            $('.sear').click(function(){
                $('.full-map').fadeIn();
                $('.bg').fadeIn();
            });
            $('.full-map .p-x em').click(function(){
                $('.full-map').fadeOut();
                $('.bg').fadeOut();
            });
            $('#loginText').click(function () {//登录
                $('.store-register').fadeToggle();
                $('.bg').fadeToggle();
            })

            $('#redirect').click(function () {//登录

            })

        })

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

        function showLoginTitle(login) {
            if (login) {
                $('.header-bar').show();
                $('.header').hide();
            } else {
                $('.header').hide();
                $('.header-bar').show();
            }
        }

        function redirectQ() {

            location.href = '/user/qqlogin.do?historyUrl=' + encodeURIComponent(encodeURIComponent(window.location.href));
        }

        function loginWeiXin() {

            location.href = '/user/wechatlogin.do?historyUrl=' + window.location.href;
        }

    </script>
</head>
<body class="gray-cont" instituteId="${instituteId}" itemName="${itemDto.name}">
<div class="header">
    <div class="header-cont">
        <img src="/static/images/entrance/fl_mall_logo.png" style="cursor: pointer" onclick="window.open('/')">
        <a href="/" target="_blank" class="ha1">首页</a>
        <a href="/community/communityAllType.do" target="_blank">我的社区</a>
        <a id="try" href="/competition" target="_blank" class="ha5">
            <img src="/static/images/logo_competitioin.png">大赛<img class="hot_a" src="/static/images/forum/hot.png">
        </a>
        <a id="trr" href="/forum" target="_blank" class="ha4">论坛</a>
        <%--<a id="trr" href="/friend" target="_blank" class="ha4">找伙伴</a>--%>
        <a href="/mall" class="ha2" target="_blank">商城</a>
        <a href="/integrate" target="_blank" class="ha3">特惠</a>
        <a href="http://www.k6kt.com/" target="_blank" class="ha6">智慧校园</a>
        <a href="#" class="a-app"><img src="/static/images/forum/forum_phone.png">
            <img class="er" src="/static/images/forum/forum_app.png">
            手机app
        </a>
        <c:if test="${login == true}">
            <span<%-- id="logout"--%> style="display: none">[退出]</span>
            <span<%-- id="userName"--%> style="display: none;"
                                        onclick="window.open('/forum/userCenter/user.do')">Hi, ${userName}</span>
            <div class="login-already">
                <div class="d1-set">
                    <div class="d1-img"></div>
                    <div class="d1-mk">
                        <div class="p1" onclick="window.open('/forum/userCenter/user.do')"><span></span>个人设置</div>
                        <div class="p2" id="logout"><span></span>退出</div>
                    </div>
                </div>
                <div class="d2-msg">
                    <div class="d2-notice" <c:if test="${infoCount==0}">style="display: none" </c:if>>${infoCount}</div>
                    <div class="d2-img"></div>
                    <div class="d2-mk">
                        <div class="p1" onclick="window.location.href='/community/playmateNotice.do'">
                            <span></span>玩伴通知<em
                                <c:if test="${friendApplyCount==0}">style="display: none" </c:if>>${friendApplyCount}</em>
                        </div>
                        <div class="p2" onclick="window.location.href='/community/mySystemInfo.do'"><span></span>系统消息<em
                                <c:if test="${systemInfoCount==0}">style="display: none" </c:if>>${systemInfoCount}</em>
                        </div>

                    </div>
                </div>
                <div class="login-name" id="userName">${userName}</div>
                <img src="${avatar}">
            </div>
        </c:if>
        <c:if test="${login == false}">
            <span onclick="window.open('/mall/register.do')">注册</span>
            <span id="toplogin">登录</span>
            <a onclick="redirectQ()"><img src="/static/images/forum/Connect_logo_7.png"></a>
            <a onclick="loginWeiXin()"><img src="/static/images/forum/icon24_wx_button.png"/></a>
        </c:if>
    </div>
</div>
<%--<%@ include file="../common/head.jsp" %>--%>
<div class="container" style="padding-top: 21px;width: 1100px;">
    <div class="train-nav" id="trainTop">
        <%--<span>找家教</span>--%>
        <%--<span class="cur1">找培训</span>--%>
        <%--<span>在线学习</span>--%>
        <%--<span>亲子活动</span>--%>
        <%--<button>搜索</button>--%>
        <%--<input type="text">--%>
        <%--<select>--%>
        <%--<option>家教</option>--%>
        <%--<option>培训</option>--%>
        <%--<option>在线学习</option>--%>
        <%--<option>亲子活动</option>--%>
        <%--</select>--%>
        <%--<div class="select-arrow"></div>--%>
    </div>
    <div class="long-location">
        <c:forEach items="${dto.typeNames}" var="typeName" varStatus="status">
            ${typeName}&nbsp;>
        </c:forEach>
        <c:forEach items="${dto.regionNames}" var="regionName" varStatus="status">
            ${regionName}&nbsp;>
        </c:forEach>
        ${dto.name}
    </div>
    <div class="train-left">
        <div class="train-intro">
            <img src="${dto.mainPicture}">
            <div class="intro-txt">
                <div class="p1">${dto.name}</div>
                <div class="p2 clearfix">
                    <span class="sp1">营业时间：</span>
                    <span class="sp2">${dto.businessTime}</span>
                </div>
                <div class="p2 clearfix">
                    <span class="sp1">地理位置：</span>
                    <span class="sp2">${dto.address}</span>
                </div>
                <div class="p2 clearfix">
                    <span class="sp1">咨询电话：</span>
                    <span class="sp2">${dto.telephone}</span>
                </div>
                <div class="intro-star">
                    <p>
                        <c:forEach items="${dto.scoreList}" var="score" varStatus="status">
                            <img src="/static/images/train/star_golden.png">
                        </c:forEach>
                        <c:forEach items="${dto.unScoreList}" var="unscore" varStatus="status">
                            <img src="/static/images/train/star_gray.png">
                        </c:forEach>
                    </p>${dto.score}
                    <%--<img src="/static/images/train/shoucang.png">--%>
                </div>
            </div>
        </div>
        <div class="train-detail">
            <div class="nav">
                <span class="cur4 sp1">培训机构详情</span>
                <span class="sp2">用户评论<em>（0）</em></span>
            </div>
            <div class="train-infor">
                <h3 class="h3-intro">培训机构简介</h3>
                <div>${dto.description}</div>
            </div>
            <div class="train-pj">
                <div class="pj-cont">
                    <div class="pj-tit">评价 :</div>
                    <button id="submit">提交点评</button>
                    <textarea id="comment"></textarea>
                    <div class="star-pj">
                        <p>星级评分</p>
                        <p id="p-star"></p>
                        <p id="p-rel"></p>
                    </div>
                </div>
                <ul class="ul-train-pj" id="trainComment">
                    <%--<li>--%>
                    <%--<img src="/static/images/train/hot_train.png" class="head-img">--%>
                    <%--<p class="p1">asdaasasdfsdf</p>--%>
                    <%--<p>--%>
                    <%--<img src="/static/images/train/star_golden.png">--%>
                    <%--<img src="/static/images/train/star_golden.png">--%>
                    <%--<img src="/static/images/train/star_golden.png">--%>
                    <%--<img src="/static/images/train/star_golden.png">--%>
                    <%--<img src="/static/images/train/star_gray.png">--%>
                    <%--<span>4.5</span>--%>
                    <%--</p>--%>
                    <%--<p>课程不粗按时大大说的vfdf</p>--%>
                    <%--</li>--%>
                </ul>
                <script type="text/template" id="trainCommentTmpl">
                    {{~it:value:index}}
                    <li>
                        <img src="{{=value.avatar}}" class="head-img">
                        <p class="p1">{{=value.nickName}}</p>
                        <p>
                            {{~value.scoreList:score:i}}
                            <img src="/static/images/train/star_golden.png">
                            {{~}}
                            {{~value.unScoreList:unscore:i}}
                            <img src="/static/images/train/star_gray.png">
                            {{~}}
                            <span>{{=value.score}}</span>
                        </p>
                        <p>{{=value.comment}}</p>
                    </li>
                    {{~}}
                </script>
            </div>
            <div class="new-page-links" hidden></div>
        </div>
    </div>
    <div class="train-right" id="trainRight">
        <div id="AmapContainer" style="display: none"></div>
        <div class="train-map">
            <div class="sear"></div>
            <div id="mapContainer"></div>
            <div id="tip">
                <%--<input type="button" value="开始定位" onClick="javascript:toolBar.doLocation()"/>--%>
                <%--<span style="color: #C0C0C0">不支持IE9以下版本</span>--%>
            </div>
        </div>
        <div class="train-hot">
            <div class="nav">
                <span>热门培训机构</span>
            </div>
            <ul class="ul-hotrain" id="institute">

            </ul>
            <script type="text/template" id="instituteTmpl">
                {{~it:value:index}}
                <li class="clearfix">
                    <img src="{{=value.mainPicture}}"
                         onclick="window.open('/train/trainDetail.do?detailId={{=value.id}}&itemId='+$('#trainTop').data($('#trainTop').find('.cur1').text()))">
                    <div class="hot-infor">
                        <p>{{=value.name}}</p>
                        <p>
                            {{~value.scoreList:score:i}}
                            <img src="/static/images/train/star_golden.png">
                            {{~}}
                            {{~value.unScoreList:unscore:i}}
                            <img src="/static/images/train/star_gray.png">
                            {{~}}
                            <span>{{=value.score}}</span>
                        </p>
                    </div>
                </li>
                {{~}}
            </script>
        </div>
    </div>

</div>
<div class="full-map">
    <p class="p-x"><em>×</em></p>
</div>
<%@ include file="../common/login.jsp" %>
<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/train/trainDetail.js', function (trainDetail) {
        trainDetail.init();
    });
</script>

<script type="text/javascript">
    /***************************************
     由于Chrome、IOS10等已不再支持非安全域的浏览器定位请求，为保证定位成功率和精度，请尽快升级您的站点到HTTPS。
     ***************************************/
    var lng='${dto.lon}';
    var lat='${dto.lat}';
    if(lng==null||lng==""||lng==undefined){
        var map, geolocation;

        map = new AMap.Map('AmapContainer', {
            resizeEnable: true
        });
        map.plugin('AMap.Geolocation', function () {
            geolocation = new AMap.Geolocation({
                enableHighAccuracy: true,//是否使用高精度定位，默认:true
                timeout: 10000,          //超过10秒后停止定位，默认：无穷大
                buttonOffset: new AMap.Pixel(10, 20),//定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
                zoomToAccuracy: true,      //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
                buttonPosition: 'RB'
            });
            map.addControl(geolocation);
            geolocation.getCurrentPosition();
            AMap.event.addListener(geolocation, 'complete', completeFunc);//返回定位信息
            AMap.event.addListener(geolocation, 'error', errorFuc);      //返回定位出错信息
        });
    }else{
       loadMap(lng,lat);
    }

//    //解析定位结果
    function completeFunc(data) {
        var str=['定位成功'];
        str.push('经度：' + data.position.getLng());
        str.push('纬度：' + data.position.getLat());
        str.push('精度：' + data.accuracy + ' 米');
        str.push('是否经过偏移：' + (data.isConverted ? '是' : '否'));
//        document.getElementById('tip').innerHTML = str.join('<br>');
        loadMap(data.position.getLng(),data.position.getLat());
    }
//
    function loadMap(lng,lat){
        // 百度地图API功能
        var map = new BMap.Map("mapContainer");  //创建Map实例
        var point = new BMap.Point(lng,lat);  //创建Point位置实例
        map.centerAndZoom(point, 24);  //设置地图中心点及缩放级别
        map.addControl(new BMap.MapTypeControl());  //添加地图类型控件
        var marker = new BMap.Marker(point);  //创建一个Marker点
        map.addOverlay(marker);  //将Marker点覆盖到地图上
        marker.setAnimation(BMAP_ANIMATION_BOUNCE);  //使Marker点跳动起来
        map.enableScrollWheelZoom(true);
    }
    //解析定位错误信息
    function errorFuc(data) {
//        document.getElementById('tip').innerHTML = '定位失败';
    }
</script>
</body>
</html>
