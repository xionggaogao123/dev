<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript">
    $(function () {
        if ($('.ebusiness-main').attr('hidden') == null) {
            getGoodsNum();
        }


        $('#toplogin').click(function () {//登录
            $('.store-register').fadeToggle();
            $('.bg').fadeToggle();
        })

        $('#myMarket').click(function () {//登录
            $('.store-register').fadeToggle();
            $('.bg').fadeToggle();
            $(this).attr('value', 1);
        })

        $('#logout').click(function () {//退出登录
            logout();
        })
        $('.input-password').keydown(function (event) {
            if (event.which == 13) {
                loginIndex();
            }
        });

        $('.store-car').click(function () {
            goToCart();
        })

        $('.input-huntt').click(function () {
            sessionStorage.setItem("page", 1);
            var regular = $(this).siblings('.input-hunt').val();
            window.location.href = '/mall/index?regular=' + regular;
        })

        $('.input-hunt').keydown(function (event) {
            if (event.which == 13) {
                sessionStorage.setItem("page", 1);
                var regular = $(this).val();
                sessionStorage.setItem("mall_regular", regular);
                window.location.href = '/mall/index?regular=' + regular;
            }
        });
    });

    function goToCart() {
        $.ajax({
            url: "/mall/loginInfo.do?date=" + new Date(),
            type: "get",
            dataType: "json",
            data: {},
            success: function (resp) {
                var flag = resp.login;
                if (flag) {
                    location.href = '/mall/cars/load.do';
                } else {
                    $('.store-register').fadeToggle();
                    $('.bg').fadeToggle();
                }
            }
        });
    }
    function loginIndex() {
        $('.login-btn').addClass('active');
        $.ajax({
            url: "/user/login.do",
            type: "post",
            dataType: "json",
            data: {
                'name': $(".input-account").val(),
                'pwd': $(".input-password").val(),
                'verifyCode': $(".input-verycode").val()
            },
            success: function (data) {
                if (data.code == 200) {
                    $('#veryCodeSpan').hide();
                    var session = data.message;
                    $('#userName').text('欢迎您，' + session.userName);
                    $('body').attr('login', 'true');
                    if (session.k6kt == 1) {
                        $('#k6kt').show();
                    } else {
                        $('#k6kt').hide();
                    }
                    getGoodsNum();
                    showLoginTitle(true);
                } else {
                    if (data.message == '用户名非法' || data.message == 'accountError') {
                        $('.input-account').addClass('error');
                        $('.username-error').show();
                    } else if (data.message.substr(0, 3) == '验证码') {
                        $('.input-verycode').addClass('error');
                        $('.verycode-error').text(data.message);
                        $('.verycode-error').show();
                        $('#veryCodeSpan').show();
                        //changeImg();
                    } else if (data.message.substr(0, 3) == '2秒内') {
                        //alert("2秒内不可重复登陆");
                    } else {
                        $('.input-password').addClass('error');
                        $('.password-error').show();
                        var strCookie = document.cookie;
                        var arrCookie = strCookie.split("; ");
                        for (var i = 0; i < arrCookie.length; i++) {
                            var arr = arrCookie[i].split("=");
                            if ("password" == arr[0]) {
                                var pwErCoun = arr[1];
                                if (pwErCoun >= 3) {
                                    $('#veryCodeSpan').show();
                                    //changeImg();
                                }
                            }
                        }
                    }
                }
            },
            complete: function () {
                $('.login-btn').removeClass('active');
            }
        });
    }

    function logout() {
        $.ajax({
            url: "/user/logout.do",
            type: "post",
            dataType: "json",
            data: {
                'inJson': true
            },
            success: function (data) {

                var href = location.href;
                if (href.indexOf("mall") > 0) {
                    location.href = "/mall";
                }

                $('body').attr('login', 'false');
                $('#goodsCount').text('（0件商品）');
                showLoginTitle(false);

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
            $('.ebusiness-main').show();
            $('.login-bar').hide();
        } else {
            $('.ebusiness-main').hide();
            $('.login-bar').show();
        }
    }

    function getGoodsNum() {
        $.ajax({
            url: "/mall/cars/list.do?date=" + new Date(),
            type: "get",
            dataType: "json",
            data: {},
            success: function (resp) {
                if (resp.list) {
                    var goodsCount = resp.list.length;
                    if (goodsCount > 0) {
                        $('#goodsCount').show().text(goodsCount);
                    } else {
                        $('#goodsCount').hide();
                    }

                }
            }
        });
    }

</script>

<div class="header" <c:if test="${login == true}">style="margin: 0 auto; display: none;"</c:if>>
    <div class="header-cont">
        <img src="/static/images/entrance/fl_mall_logo.png" style="cursor: pointer" onclick="window.open('/mall')">
        <a href="/" target="_blank" class="ha1">首页</a>
        <a href="/competition" target="_blank" class="ha5">
            <img src="/static/images/logo_competitioin.png">大赛<img class="hot_a" src="/static/images/forum/hot.png">
        </a>
        <a href="/forum" target="_blank" class="ha4">论坛</a>
        <a href="/mall" target="_blank" class="ha2" <c:if test="${k6kt == 0}">style="display: none" </c:if>>商城</a>
        <a href="/integrate" target="_blank" class="ha3">特惠</a>
        <a href="http://www.k6kt.com" target="_blank" class="ha6">智慧校园</a>
        <a href="#" class="a-app"><img src="/static/images/forum/forum_phone.png"><img class="er"
                                                                                       src="/static/images/forum/forum_app.png">
            手机app</a>
        <%--<a>手机商城</a>--%>
        <span onclick="window.open('/mall/register.do')">注册</span>
        <span id="toplogin">登录</span>
    </div>
</div>

<div class="header-bar" <c:if test="${login == false}">style="display: none;" </c:if>>
    <div class="header-cont">
        <img src="/static/images/entrance/fl_mall_logo.png">
        <a href="/" target="_blank" class="ha1">首页</a>
        <a href="/competition" target="_blank" class="ha5">
            <img src="/static/images/logo_competitioin.png">大赛<img class="hot_a" src="/static/images/forum/hot.png">
        </a>
        <a href="/forum" target="_blank" class="ha4">论坛</a>
        <a href="/mall" target="_blank" class="ha2">商城</a>
        <a href="/integrate" target="_blank" class="ha3">特惠</a>
        <a href="http://www.k6kt.com" target="_blank" class="ha6">智慧校园</a>
        <a href="#" class="a-app"><img src="/static/images/forum/forum_phone.png"><img class="er"
                                                                                       src="/static/images/forum/forum_app.png">
            手机app</a>
        <%--<a>手机商城
            <div></div>
        </a>--%>
       <%-- <span id="logout">[退出]</span>
        <span id="userName" onclick="window.open('/forum/userCenter/user.do')">Hi, ${userName}</span>--%>
        <div class="login-already">
            <div class="d1-set">
                <div class="d1-img"></div>
                <div class="d1-mk">
                    <div class="p1" onclick="window.open('/forum/userCenter/user.do')"><span></span>个人设置</div>
                    <div class="p2"  id="logout"><span></span>退出</div>
                </div>
            </div>
            <div class="d2-msg">
                <div class="d2-img"></div>
                <div class="d2-mk">
                    <div class="p1"><span></span>玩伴通知<em>10</em></div>
                    <div class="p2"><span></span>系统消息<em>10</em></div>
                </div>
            </div>
            <div class="login-name" id="userName">${userName}</div>
            <img src="">
        </div>
    </div>
</div>

<div class="ebusiness-main" <c:if test="${login == false}">style="display: none;" </c:if>>
    <div class="ebusiness-main-top">
        <div class="ebusiness-main-top-l">
            <img src="/img/K6KT/main-page/store-ebusiness-V.png" onclick="window.open('/mall/mallSection.do','target')">
            <%--<c:if test="${k6kt == 0}">style="display: none" </c:if>--%>
            <a href="/user/homepage.do" id="k6kt" style="display: none"></a>
        </div>
        <div class="ebusiness-main-top-r">
            <input class="input-hunt" type="text" placeholder="" value="${param.regular}"><em
                class="input-huntt">搜索</em>
            <div>
                <span class="ebusiness-r-w"
                      onclick="window.open('/mall/order/page.do')">我的复兰商城&nbsp;&nbsp;&nbsp;&gt;</span>
                <span class="ebusiness-r-l">
                <em class="ebusiness-D" onclick="window.open('/mall/order/page.do')">交易中心</em>
                <em class="ebusiness-Z" onclick="window.open('/mall/userCenter/collection.do')">关注中心</em>
                <em class="ebusiness-G" onclick="window.open('/mall/userCenter/user.do')">设置中心</em>

            </span>
                <span class="store-car">购物车<em id="goodsCount" style="display: none"></em></span>
            </div>
        </div>
    </div>
</div>

<div class="login-bar" <c:if test="${login == true}">style="margin: 0 auto; display: none;"</c:if>>
    <div class='title-bar-container' style="height:80px;overflow: hidden;">
        <div class="ebusiness-main-top-l">
            <%--<img class="title-logo" src="/img/K6KT/main-page/store-logo.png">--%>
            <img class="title-logo" src="/img/K6KT/main-page/store-ebusiness-V.png"
                 onclick="window.open('/mall/index.do','target')">
            <i style="display: inline-block;width: 130px;"></i>
            <!--申请试用-->
        </div>
        <div class="ebusiness-main-top-r">
            <span class="store-car">购物车</span>
            <span class="ebusiness-r-w" id="myMarket">我的复兰商城&nbsp;&nbsp;&nbsp;</span>
            <input class="input-hunt" type="text" placeholder="" value="${param.regular}"><em
                class="input-huntt">搜索</em>
            <%-- <input id="input-first" class="input-password" type="password" placeholder="密码" tabindex="2">
             <input id="input-last" class="input-account" type="text" placeholder="用户名/邮箱/手机号" tabindex="1" value="">--%>
        </div>
        <div id="tips-msg">
            <span class="verycode-error"></span>
            <span class="password-error">密码错误</span>
            <span class="username-error">用户名不存在</span>
        </div>
    </div>
</div>
